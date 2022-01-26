package io.kontur.userprofile.service;

import static io.kontur.userprofile.config.WebSecurityConfiguration.ClaimParams.ROLE_PREFIX;
import static io.kontur.userprofile.config.WebSecurityConfiguration.ClaimParams.USERNAME_PREFIX;
import static io.kontur.userprofile.model.entity.Role.Names.BETA_FEATURES;

import io.kontur.userprofile.dao.FeatureDao;
import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.User;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final UserDao userDao;
    private final FeatureDao featureDao;

    public String getUserDefaultEventFeed(List<String> tokenClaims) {
        return getUserFeatures(tokenClaims)
            .filter(it -> FeatureType.EVENT_FEED == it.getType())
            .max(Comparator.comparing(it -> !it.isBeta()))
            .map(Feature::getName)
            .orElse(null);
    }

    private Stream<Feature> getPublicFeatures() {
        return featureDao.getPublicNonBetaFeatures().stream()
            .filter(Feature::isEnabled);
    }

    public Stream<Feature> getUserFeatures(List<String> tokenClaims) {
        Optional<User> currentUser = getCurrentUser(tokenClaims);
        if (currentUser.isEmpty()) {
            return getPublicFeatures();
        }

        boolean includeBetaFeatures = hasBetaFeatureRole(tokenClaims);

        return getUserFeatures(currentUser.get(), includeBetaFeatures);
    }

    private Stream<Feature> getUserFeatures(@NotNull User user, boolean includeBetaFeatures) {
        Stream<Feature> nonBetaFeatures = getNonBetaFeatures(user);
        if (includeBetaFeatures) {
            Stream<Feature> userBetaFeatures = getBetaFeatures(user);
            return Stream.concat(nonBetaFeatures, userBetaFeatures);
        }
        return nonBetaFeatures;
    }

    private Stream<Feature> getNonBetaFeatures(@NotNull User user) {
        if (user.getFeaturesEnabledByUser() == null) {
            return getPublicFeatures();
        }

        return user.getFeaturesEnabledByUser().stream()
            .filter(Feature::isEnabled)
            .filter(it -> !it.isBeta());
    }

    private Stream<Feature> getBetaFeatures(@NotNull User user) {
        if (user.getFeaturesEnabledByUser() == null) {
            return Stream.of();
        }

        return user.getFeaturesEnabledByUser().stream()
            .filter(Feature::isEnabled)
            .filter(Feature::isBeta);
    }


    private Optional<String> getCurrentUserName(List<String> tokenClaims) {
        return tokenClaims.stream()
            .filter(Objects::nonNull)
            .filter(it -> it.startsWith(USERNAME_PREFIX))
            .findAny()
            .map(this::removePrefixFromUsernameClaim);
    }

    private Optional<User> getCurrentUser(List<String> tokenClaims) {
        return getCurrentUserName(tokenClaims)
            .map(userDao::getUser);
    }

    private boolean hasBetaFeatureRole(List<String> tokenClaims) {
        return tokenClaims.stream()
            .anyMatch((ROLE_PREFIX + BETA_FEATURES)::equals);
    }

    private String removePrefixFromUsernameClaim(String claim) {
        return claim.substring(USERNAME_PREFIX.length());
    }
}
