package io.kontur.userprofile.service;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.AppFeatureDao;
import io.kontur.userprofile.dao.AppUserFeatureDao;
import io.kontur.userprofile.dao.FeatureDao;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final AuthService authService;
    private final AppFeatureDao appFeatureDao;
    private final FeatureDao featureDao;
    private final AppUserFeatureDao appUserFeatureDao;
    public static final UUID DN2_ID = UUID.fromString("58851b50-9574-4aec-a3a6-425fa18dcb54");
    public static final String DN2_NAME = "DN2";

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

    public Stream<Feature> getAppFeatures(App app) {
        return appFeatureDao.getAppFeaturesFor(app);
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

}
