package io.kontur.userprofile.service;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.UserCustomRoleDao;
import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.ActiveSubscriptionDto;
import io.kontur.userprofile.model.dto.UserDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.BillingPlan;
import io.kontur.userprofile.model.entity.UserBillingSubscription;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.kontur.userprofile.service.IntercomService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserDao userDao;
    private final UserCustomRoleDao userCustomRoleDao;
    private final AppService appService;
    private final AuthService authService;

    private final IntercomService intercomService;

    // Metrics
    private final MeterRegistry registry;

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUser(String username) {
        return userDao.getUser(username);
    }

    public User createUser(User user) {
        if (userDao.getUser(user.getUsername()) != null) {
            throw new WebApplicationException("Use other username!", HttpStatus.BAD_REQUEST);
        }
        if (userDao.getUserByEmail(user.getEmail()) != null) {
            throw new WebApplicationException("Use other email!", HttpStatus.BAD_REQUEST);
        }
        userDao.createUser(user);

        intercomService.syncUser(user);

        return user;
    }

    public User updateUser(User user, UserDto userDto) {
        user.setFullName(userDto.getFullName());
        user.setLanguage(userDto.getLanguage());
        user.setUseMetricUnits(userDto.isUseMetricUnits());
        user.setSubscribedToKonturUpdates(userDto.isSubscribedToKonturUpdates());
        user.setBio(userDto.getBio());
        user.setOsmEditor(userDto.getOsmEditor());
        user.setDefaultFeed(userDto.getDefaultFeed());
        user.setTheme(userDto.getTheme());
        user.setLinkedin(userDto.getLinkedin());
        user.setPhone(userDto.getPhone());
        user.setCallConsentGiven(userDto.isCallConsentGiven());
        user.setAccountNotes(userDto.getAccountNotes());
        user.setObjectives(userDto.getObjectives());
        user.setCompanyName(userDto.getCompanyName());
        user.setPosition(userDto.getPosition());
        user.setAmountOfGis(userDto.getAmountOfGis());

        userDao.updateUser(user);

        intercomService.syncUser(user);

        return user;
    }

    public Optional<ActiveSubscriptionDto> getActiveSubscription(UUID appId) {
        final App app = appService.getAppOrThrow(appId);
        final User user = authService.getCurrentUserOrElseThrow();

        return userCustomRoleDao.getActiveSubscription(user, app).map(ActiveSubscriptionDto::new);
    }

    public ActiveSubscriptionDto setActiveSubscription(UUID appId, String billingPlanId, String subscriptionId) {
        final App app = appService.getAppOrThrow(appId);
        final User user = authService.getCurrentUserOrElseThrow();
        final BillingPlan billingPlan = getBillingPlanByIdOrElseThrow(billingPlanId);

        final Counter c = Counter
                .builder("billing.subscription.activated")
                .description("The number of new subscriptions activated")
                .tag("subscription", subscriptionId)
                .tag("plan", billingPlan.getRole().getName())
                .tag("app", app.getName())
                .register(registry);
        c.increment();

        final UserBillingSubscription subscription = userCustomRoleDao.setActiveSubscription(user, app, billingPlan, subscriptionId);
        intercomService.syncUser(user);
        return new ActiveSubscriptionDto(subscription);
    }

    // also needed for subscription cancellation via a webhook
    public void expireActiveSubscription(String id) {
        final var subscription = userCustomRoleDao.expireActiveSubscription(id);
        final Counter c = Counter
                .builder("billing.subscription.deactivated")
                .description("The number of subscriptions deactivated")
                .tag("subscription", id)
                .tag("plan", subscription.getBillingPlan().getRole().getName())
                .tag("app", subscription.getApp().getName())
                .register(registry);
        c.increment();
        intercomService.syncUser(subscription.getUser());
    }

    public void reactivateSubscription(String id) {
        userCustomRoleDao.reactivateSubscription(id).ifPresent(subscription -> {
            // we might never get here due to an exception or subscription already being active
            // and that's intended behaviour
            final Counter c = Counter
                    .builder("billing.subscription.reactivated")
                    .description("The number of old subscriptions reactivated")
                    .tag("subscription", id)
                    .tag("plan", subscription.getBillingPlan().getRole().getName())
                    .tag("app", subscription.getApp().getName())
                    .register(registry);
            c.increment();
            intercomService.syncUser(subscription.getUser());
        });
    }

    public BillingPlan getBillingPlanByIdOrElseThrow(String id) {
        return Optional.ofNullable(userCustomRoleDao.getBillingPlanById(id))
                .orElseThrow(() -> new WebApplicationException("Billing plan not found by id " + id, HttpStatus.NOT_FOUND));
    }
}
