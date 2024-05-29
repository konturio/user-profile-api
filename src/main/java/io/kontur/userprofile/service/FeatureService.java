package io.kontur.userprofile.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.*;
import io.kontur.userprofile.model.entity.*;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import io.kontur.userprofile.model.entity.user.User;

import java.util.*;
import java.util.stream.Collectors;
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
    private final CustomAppFeatureDao customAppFeatureDao;
    private final UserCustomRoleDao userCustomRoleDao;
    private final FeatureDao featureDao;

    public String getDefaultDn2EventFeedForCurrentUser(App app) {
        return getAllFeaturesAvailableToUser(app).stream()
                .filter(Objects::nonNull)
                .filter(it -> FeatureType.EVENT_FEED == it.getType())
                .findFirst()
                .map(Feature::getName)
                .orElse(null);
    }

    public List<Feature> getFeaturesAddedByDefaultToUserApps() {
        return featureDao.getFeaturesAddedByDefaultToUserApps();
    }

    public List<CustomAppFeature> getAllAppFeaturesAvailableToUser(App app) {
        Optional<String> username = authService.getCurrentUsername();

        if (username.isEmpty() || userDao.getUser(username.get()) == null) {
            return customAppFeatureDao.getAllFeaturesAvailableToUser(app, null, null);
        }

        return customAppFeatureDao.getAllFeaturesAvailableToUser(app, username.get(), userCustomRoleDao.getUserRoleIds(username.get()));
    }

    public List<Feature> getAllFeaturesAvailableToUser(App app) {
        return getAllAppFeaturesAvailableToUser(app).stream()
                .map(CustomAppFeature::getFeature)
                .distinct()
                .collect(Collectors.toList());
    }

    public Feature getFeatureByName(String name) {
        return featureDao.getFeatureByName(name);
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

        if (customAppFeatureDao.saveConfigurationForUser(app, feature, user, configuration) < 1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok().build();
    }
}
