package io.kontur.userprofile.service;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.AppDao;
import io.kontur.userprofile.dao.AppFeatureDao;
import io.kontur.userprofile.dao.AppUserFeatureDao;
import io.kontur.userprofile.model.dto.AppSummaryDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.AppFeature;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppService {
    public static final UUID DN2_ID = UUID.fromString("58851b50-9574-4aec-a3a6-425fa18dcb54");

    private final AppDao appDao;
    private final AppFeatureDao appFeatureDao;
    private final AppUserFeatureDao appUserFeatureDao;
    private final FeatureService featureService;
    private final AuthService authService;

    private List<AppFeature> createAppFeatures(App app, List<String> featureNames) {
        List<AppFeature> appFeatures = new ArrayList<>();

        List<Feature> featuresAddedByDefaultToUserApps = featureService
            .getFeaturesAddedByDefaultToUserApps();
        featuresAddedByDefaultToUserApps.forEach(f -> {
            appFeatures.add(new AppFeature(app, f));
        });

        if (featureNames != null) {
            for (String featureName : featureNames) {
                Feature feature = getFeatureForUserApp(featureName);
                if (!feature.isDefaultForUserApps()) {
                    AppFeature appFeature = new AppFeature(app, feature);
                    appFeatures.add(appFeature);
                }
            }
        }
        return appFeatures;
    }

    private Feature getFeatureForUserApp(String featureName) {
        Feature feature = featureService.getFeatureByName(featureName);
        if (feature == null || !feature.isEnabled()) {
            throw new WebApplicationException("Feature with name " + featureName
                + " was not found", HttpStatus.BAD_REQUEST);
        }
        if (feature.isBeta()) {
            throw new WebApplicationException("Not allowed to use beta features!",
                HttpStatus.FORBIDDEN);
        }
        if (!feature.isAvailableForUserApps()) {
            throw new WebApplicationException("Feature " + featureName
                + " is not allowed for user apps", HttpStatus.BAD_REQUEST);
        }
        return feature;
    }

    public App createApp(App app, List<String> featureNames) {
        checkCenterGeometryIsAPointIfSpecified(app);
        app.setId(UUID.randomUUID());

        List<AppFeature> appFeatures = createAppFeatures(app, featureNames);

        appDao.createApp(app);
        appFeatureDao.saveAppFeatures(appFeatures);

        return app;
    }

    private void checkCenterGeometryIsAPointIfSpecified(App app) {
        if (app.getCenterGeometry() != null && !(app.getCenterGeometry() instanceof Point)) {
            throw new WebApplicationException("CenterGeometry must be a Point",
                HttpStatus.BAD_REQUEST);
        }
    }

    public App updateApp(UUID id, App update,
                         List<String> featureNames) {
        checkCenterGeometryIsAPointIfSpecified(update);

        App app = getAppForChange(id);
        app.setName(update.getName());
        app.setDescription(update.getDescription());
        app.setPublic(update.isPublic());
        app.setCenterGeometry(update.getCenterGeometry());

        List<Feature> currentFeatures = appFeatureDao.getAppFeaturesIncludingDisabledAndBetaFor(app)
            .toList();
        List<Feature> toRemove = currentFeatures.stream()
            .filter(it -> !featureNames.contains(it.getName()))
            .toList();
        List<AppFeature> toAdd = featureNames.stream()
            .filter(name -> currentFeatures.stream().noneMatch(it -> it.getName().equals(name)))
            .map(it -> new AppFeature(app, getFeatureForUserApp(it)))
            .toList();

        appFeatureDao.removeAppFeaturesFrom(app, toRemove);
        appFeatureDao.saveAppFeatures(toAdd);

        return appDao.updateApp(app);
    }

    public void deleteApp(UUID id) {
        App app = getAppForChange(id);

        appUserFeatureDao.deleteAllAppUserFeaturesFrom(app);
        appFeatureDao.deleteAllAppFeaturesFrom(app);
        appDao.deleteApp(app);
    }

    public UUID getDefaultId() {
        return DN2_ID;
    }

    public App getApp(UUID id) {
        App app = getAppOrThrow(id);

        if (app.isPublic() || isAppOwnedByCurrentUser(app)) {
            return app;
        }

        //owned by other user
        throw new WebApplicationException("App not found by id " + id, HttpStatus.NOT_FOUND);
    }

    public boolean isAppOwnedByCurrentUser(@NotNull App app) {
        Optional<String> currentUsername = authService.getCurrentUsername();
        if (app == null) {
            throw new IllegalArgumentException("App must not be null!");
        }

        return currentUsername.isPresent()
            && app.isOwnedByUsername(currentUsername.get());
    }

    public List<AppSummaryDto> getAppListForCurrentUser() {
        Optional<String> currentUsername = authService.getCurrentUsername();

        List<AppSummaryDto> publicApps = appDao.getPublicAppsList();

        if (currentUsername.isEmpty()) {
            return publicApps;
        }

        List<AppSummaryDto> userOwnedApps = appDao.getUserOwnedAppsList(currentUsername.get());

        List<AppSummaryDto> result = new ArrayList<>();
        result.addAll(publicApps);
        result.addAll(userOwnedApps);

        return result.stream().distinct().toList();
    }

    private App getAppOrThrow(UUID id) {
        App app = appDao.getApp(id);
        if (app == null) {
            throw new WebApplicationException("App not found by id " + id,
                HttpStatus.NOT_FOUND);
        }
        return app;
    }

    private App getAppForChange(UUID id) {
        App app = getAppOrThrow(id);
        if (!isAppOwnedByCurrentUser(app)) {
            throw new WebApplicationException("Forbidden to change app " + id,
                HttpStatus.FORBIDDEN);
        }
        return app;
    }


}
