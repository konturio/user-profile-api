package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.*;
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

    public List<DatedRole> getActiveUserRoles(User user) {
        List<DatedRole> datedRoles =  entityManager.createQuery(
                        "select role.id as roleId, role.name as roleName, endedAt as expiredAt from UserCustomRole " +
                                "where user.id = :userId " +
                                "and startedAt < current_timestamp " +
                                "and (endedAt is null or current_timestamp < endedAt)",
                        DatedRole.class)
                .setParameter("userId", user.getId())
                .getResultList();

        List<DatedRole> subscriptionRoles = entityManager.createQuery(
                "select billingPlan.role.id as roleId, billingPlan.role.name as roleName, expiredAt as expiredAt "
                                + "from UserBillingSubscription "
                                + "where user.id = :userId "
                                + "and active",
                        DatedRole.class)
                .setParameter("userId", user.getId())
                .getResultList();

        datedRoles.addAll(subscriptionRoles);

        return datedRoles.stream().distinct().toList();
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

    // also needed for subscription cancellation via a webhook
    public UserBillingSubscription expireActiveSubscription(String id) {
        UserBillingSubscription subscription = entityManager.find(UserBillingSubscription.class, id);

        subscription.setActive(false);
        subscription.setExpiredAt(OffsetDateTime.now());

        entityManager.merge(subscription);
        entityManager.flush();

        return subscription;
    }

    public Optional<UserBillingSubscription> reactivateSubscription(String id) {
        // if already exists an active one do nothing
        // otherwise create a new one

        UserBillingSubscription subscription = entityManager.find(UserBillingSubscription.class, id);

        if (subscription == null) {
            // FIXME: can't create a new one without user id and app id!
            throw new WebApplicationException("Failed to activate unknown subscription " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // The subscription with the given id is the active one, nothing to do
        if (subscription.getActive()) return Optional.empty();

        // the user has another subscription tht's active for the same app
        // so expire it
        getActiveSubscription(subscription.getUser(), subscription.getApp())
            .map(UserBillingSubscription::getId)
            .ifPresent(this::expireActiveSubscription);

        // FIXME: we can't create a new subscription record with
        // the same subscription ID. Reactivating.
        subscription.setActive(true);
        subscription.setExpiredAt(null);

        entityManager.merge(subscription);
        entityManager.flush();

        return Optional.of(subscription);
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
