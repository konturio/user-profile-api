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

        return user;
    }

    public Optional<ActiveSubscriptionDto> getActiveSubscription(UUID appId) {
        App app = appService.getAppOrThrow(appId);
        User user = authService.getCurrentUserOrElseThrow();

        return userCustomRoleDao.getActiveSubscription(user, app).map(ActiveSubscriptionDto::new);
    }

    public ActiveSubscriptionDto setActiveSubscription(UUID appId, String billingPlanId, String subscriptionId) {
        App app = appService.getAppOrThrow(appId);
        User user = authService.getCurrentUserOrElseThrow();
        BillingPlan billingPlan = getBillingPlanByIdOrElseThrow(billingPlanId);

        UserBillingSubscription subscription = userCustomRoleDao.setActiveSubscription(user, app, billingPlan, subscriptionId);
        return new ActiveSubscriptionDto(subscription);
    }

    // also needed for subscription cancellation via a webhook
    public void expireActiveSubscription(String id) {
        userCustomRoleDao.expireActiveSubscription(id);
    }

    public void reactivateSubscription(String id) {
        userCustomRoleDao.reactivateSubscription(id);
    }

    public BillingPlan getBillingPlanByIdOrElseThrow(String id) {
        return Optional.ofNullable(userCustomRoleDao.getBillingPlanById(id))
                .orElseThrow(() -> new WebApplicationException("Billing plan not found by id " + id, HttpStatus.NOT_FOUND));
    }
}
