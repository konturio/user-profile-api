package io.kontur.userprofile.service;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.AppFeatureDao;
import io.kontur.userprofile.dao.AppUserFeatureDao;
import io.kontur.userprofile.dao.FeatureDao;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.AppFeature;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final AuthService authService;
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
        List<Feature> allUserFeatures = appUserFeatureDao.getAppUserFeatures(app, username).toList();

        boolean includeBetaFeatures = authService.currentUserHasBetaFeaturesRole();
        if (includeBetaFeatures) {
            return getAppFeaturesFor(app)
                    .filter(appFeature -> allUserFeatures.contains(appFeature.getFeature()));
        } else {
            return getAppFeaturesFor(app)
                    .filter(appFeature -> allUserFeatures.contains(appFeature.getFeature()))
                    .filter(appFeature -> !appFeature.getFeature().isBeta());
        }
    }

}
