package io.kontur.userprofile.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.*;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.AppFeature;
import io.kontur.userprofile.model.entity.AppUserFeature;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import io.kontur.userprofile.model.entity.user.User;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final AuthService authService;
    private final AppDao appDao;
    private final UserDao userDao;
    private final AppFeatureDao appFeatureDao;
    private final FeatureDao featureDao;
    private final AppUserFeatureDao appUserFeatureDao;

    public String getDefaultDn2EventFeedForCurrentUser(App app) {
        return getCurrentUserAppFeatures(app)
                .filter(Objects::nonNull)
                .filter(it -> FeatureType.EVENT_FEED == it.getType())
                .max(Comparator.comparing(it -> !it.isBeta()))
                .map(Feature::getName)
                .orElse(null);
    }

    public List<Feature> getFeaturesAddedByDefaultToUserApps() {
        return featureDao.getFeaturesAddedByDefaultToUserApps();
    }

    public Stream<AppFeature> getAppFeaturesFor(App app) {
        return appFeatureDao.getAppFeaturesFor(app);
    }

    public Feature getFeatureByName(String name) {
        return featureDao.getFeatureByName(name);
    }

    public Stream<Feature> getCurrentUserAppFeatures(App app) {
        Optional<String> currentUsername = authService.getCurrentUsername();

        if (currentUsername.isEmpty()
                || !appUserFeatureDao.userHasAnyConfiguredFeatures(app, currentUsername.get())) {
            return getAppFeatures(app);
        }

        return getUserAppFeatures(currentUsername.get(), app);
    }

    public Stream<AppFeature> getAppFeaturesForCurrentUserAndFor(App app) {
        Optional<String> currentUsername = authService.getCurrentUsername();

        if (currentUsername.isEmpty()
                || !appUserFeatureDao.userHasAnyConfiguredFeatures(app, currentUsername.get())) {
            return getAppFeaturesFor(app);
        }

        return getAppFeaturesByUserForApp(currentUsername.get(), app);
    }

    public Stream<Feature> getAppFeatures(App app) {
        return appFeatureDao.getEnabledNonBetaAppFeaturesFor(app);
    }

    public ResponseEntity<Void> updateAppUserFeatureConfiguration(UUID appId, String featureName, JsonNode configuration) {
        Optional<String> usernameOpt = authService.getCurrentUsername();

        if (usernameOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        App app = appDao.getApp(appId);
        Feature feature = featureDao.getFeatureByName(featureName);
        User user = userDao.getUser(usernameOpt.get());

        if (app == null || feature == null || user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if (!feature.isAvailableForUserConfiguration())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if (!appUserFeatureDao.userHasConfiguredFeature(app, usernameOpt.get(), feature)) {
            appUserFeatureDao.saveAppUserFeatures(
                    appFeatureDao.getAppFeaturesFor(app)
                            .map(appFeature -> new AppUserFeature(appFeature.getApp(), user, appFeature.getFeature(), null))
                            .collect(Collectors.toList()));
        }

        if (appUserFeatureDao.updateAppUserFeatureConfiguration(app, usernameOpt.get(), feature, configuration) < 1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok().build();
    }

    private Stream<Feature> getUserAppFeatures(@NotNull String username,
                                               @NotNull App app) {
        Stream<Feature> allUserFeatures = appUserFeatureDao.getAppUserFeatures(app, username);

        boolean includeBetaFeatures = authService.currentUserHasBetaFeaturesRole();
        if (includeBetaFeatures) {
            return allUserFeatures;
        } else {
            return allUserFeatures.filter(it -> !it.isBeta());
        }
    }

    private Stream<AppFeature> getAppFeaturesByUserForApp(@NotNull String username,
                                                          @NotNull App app) {
        List<AppUserFeature> appUserFeatures = appUserFeatureDao
            .selectAppUserFeaturesFor(app, username);

        List<Feature> features = appUserFeatures.stream().map(AppUserFeature::getFeature).toList();

        Stream<AppFeature> appFeatures = getAppFeaturesFor(app)
                .filter(appFeature -> features.contains(appFeature.getFeature()))
                .peek(appFeature -> appFeature.setConfiguration(
                        appUserFeatures
                                .stream()
                                .filter(auf -> auf.getConfiguration() != null && auf.getFeature().equals(appFeature.getFeature()))
                                .map(AppUserFeature::getConfiguration)
                                .findFirst()
                                .orElse(appFeature.getConfiguration())
                ));

        if (!authService.currentUserHasBetaFeaturesRole()) {
            return appFeatures.filter(appFeature -> !appFeature.getFeature().isBeta());
        }
        return appFeatures;
    }

}
