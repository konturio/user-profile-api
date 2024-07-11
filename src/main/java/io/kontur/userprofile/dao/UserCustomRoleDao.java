package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.BillingPlan;
import io.kontur.userprofile.model.entity.UserBillingSubscription;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCustomRoleDao {
    @PersistenceContext
    EntityManager entityManager;

    public List<Long> getUserRoleIds(String username) {
        List<Long> generalRoleIds = entityManager.createQuery("select distinct role.id "
                                + "from UserCustomRole "
                                + "where user.username = ?1 "
                                + "and startedAt < current_timestamp "
                                + "and (endedAt is null or current_timestamp < endedAt)",
                        Long.class)
                .setParameter(1, username)
                .getResultList();

        List<Long> subscriptionRoleIds = entityManager.createQuery("select distinct billingPlan.role.id "
                                + "from UserBillingSubscription "
                                + "where user.username = ?1 "
                                + "and active",
                        Long.class)
                .setParameter(1, username)
                .getResultList();

        generalRoleIds.addAll(subscriptionRoleIds);

        return generalRoleIds.stream().distinct().toList();
    }

    public Optional<UserBillingSubscription> getActiveSubscription(User user, App app) {
        List<UserBillingSubscription> results = entityManager.createQuery("from UserBillingSubscription "
                                + "where user.username = ?1 "
                                + "and active "
                                + "and app.id = ?2",
                        UserBillingSubscription.class)
                .setParameter(1, user.getUsername())
                .setParameter(2, app.getId())
                .getResultList();

        if (results.size() > 1) {
            // This condition is impossible as long as uq_user_billing_subscription_active constraint is present
            throw new WebApplicationException(format(
                    "More than one active subscription found. Username: %s, App: %s",
                    user.getUsername(), app.getId()), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
        return results.stream().findAny();
    }

    public UserBillingSubscription setActiveSubscription(User user, App app, BillingPlan billingPlan, String subscriptionId) {
        getActiveSubscription(user, app)
                .map(UserBillingSubscription::getId)
                .ifPresent(this::expireActiveSubscription);
        return createActiveSubscription(user, app, billingPlan, subscriptionId);
    }

    public BillingPlan getBillingPlanById(String id) {
        return entityManager.find(BillingPlan.class, id);
    }

    private void expireActiveSubscription(String id) {
        UserBillingSubscription subscription = entityManager.find(UserBillingSubscription.class, id);

        subscription.setActive(false);
        subscription.setExpiredAt(OffsetDateTime.now());

        entityManager.merge(subscription);
        entityManager.flush();
    }

    private UserBillingSubscription createActiveSubscription(User user, App app, BillingPlan billingPlan, String subscriptionId) {
        UserBillingSubscription subscription = new UserBillingSubscription(subscriptionId, billingPlan, app, user);
        try {
            entityManager.persist(subscription);
            entityManager.flush();
            return subscription;
        } catch (EntityExistsException e) {
            throw new WebApplicationException("Billing subscription already exists with ID " + subscriptionId, HttpStatus.CONFLICT);
        } catch (Exception e) {
            throw new WebApplicationException("Failed to save billing subscription " + subscriptionId, HttpStatus.CONFLICT);
        }
    }

}
