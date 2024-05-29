package io.kontur.userprofile.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.*;
import io.kontur.userprofile.model.dto.AppSummaryDto;
import io.kontur.userprofile.model.dto.AssetDto;
import io.kontur.userprofile.model.entity.*;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
@Transactional
public class AppService {
    public static final UUID DN2_ID = UUID.fromString("58851b50-9574-4aec-a3a6-425fa18dcb54");
    private final String DEFAULT_LANGUAGE = "en";

    private final AppDao appDao;
    private final FeatureDao featureDao;
    private final CustomAppFeatureDao customAppFeatureDao;
    private final FeatureService featureService;
    private final AuthService authService;

    private List<CustomAppFeature> createAppFeatures(App app,
                                               @NonNull Map<String,
                                               JsonNode> featuresConfig) {
        List<CustomAppFeature> appFeatures = new ArrayList<>();

        List<Feature> featuresAddedByDefaultToUserApps = featureService
                .getFeaturesAddedByDefaultToUserApps();
        featuresAddedByDefaultToUserApps
            .forEach(f -> appFeatures.add(new CustomAppFeature(app, f, false, featuresConfig.get(f.getName()))));

        for (Map.Entry<String, JsonNode> featureConfig : featuresConfig.entrySet()) {
            Feature feature = getFeatureForUserApp(featureConfig.getKey());
            if (!feature.isDefaultForUserApps()) {
                CustomAppFeature appFeature = new CustomAppFeature(app, feature, false, featureConfig.getValue());
                appFeatures.add(appFeature);
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
        if (!feature.isAvailableForUserApps()) {
            throw new WebApplicationException("Feature " + featureName
                    + " is not allowed for user apps", HttpStatus.BAD_REQUEST);
        }
        return feature;
    }

    public App createApp(App app, Map<String, JsonNode> featuresConfig) {
        app.setId(UUID.randomUUID());

        List<CustomAppFeature> appFeatures = createAppFeatures(app, featuresConfig);

        appDao.createApp(app);
        customAppFeatureDao.saveAppFeatures(appFeatures);

        return app;
    }

    public App updateApp(UUID id, App update, Map<String, JsonNode> featuresConfig) {

        App app = getAppForChange(id);
        app.setName(update.getName());
        app.setDescription(update.getDescription());
        app.setPublic(update.isPublic());
        app.setExtent(update.getExtent());
        app.setSidebarIconUrl(update.getSidebarIconUrl());
        app.setFaviconUrl(update.getFaviconUrl());

        customAppFeatureDao.deleteAllAppFeaturesFrom(app);

        if (featuresConfig != null) {
            customAppFeatureDao.saveAppFeatures(featuresConfig
                    .entrySet()
                    .stream()
                    .map(entry -> new CustomAppFeature(
                            app,
                            getFeatureForUserApp(entry.getKey()),
                            false,
                            entry.getValue()))
                    .toList());
        }

        return appDao.updateApp(app);
    }

    public void deleteApp(UUID id) {
        App app = getAppForChange(id);

        customAppFeatureDao.deleteAllAppFeaturesFrom(app);
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

    public App getApp(String domain) {
        App app = appDao.getApp(domain);
        if (app == null) {
            return null;
        }
        if (app.isPublic() || isAppOwnedByCurrentUser(app)) {
            return app;
        }
        return null;
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

    public Optional<AssetDto> getAssetByAppIdAndFileNameAndLanguage(UUID appId, String filename, String language) {
        Asset asset = appDao.getAssetByAppIdAndFileNameAndLanguage(appId, filename, language, DEFAULT_LANGUAGE);
        return asset == null ? Optional.empty() : Optional.of(AssetDto.fromEntity(asset));
    }

    public String parseLanguage(String userLanguage) {
        return StringUtils.isBlank(userLanguage) ? DEFAULT_LANGUAGE : userLanguage;
    }

    public void uploadAsset(UUID appId, String featureName, String description, MultipartFile file, String language) throws IOException {
        App app = appDao.getApp(appId);
        Feature feature = featureDao.getFeatureByName(featureName);
        validateAssetUploadParams(app, appId, feature, featureName, file);

        MediaType type = MediaType.parseMediaType(file.getContentType());
        appDao.createAsset(type.getType(), type.getSubtype(), file.getOriginalFilename(), description, language, appId, feature.getId(), file.getBytes());
    }

    private void validateAssetUploadParams(App app, UUID appId, Feature feature, String featureName, MultipartFile file) {
        if (app == null) {
            throw new InvalidParameterException("App with provided ID doesn't exist: " + appId);
        }
        if (feature == null) {
            throw new InvalidParameterException("Feature with provided name doesn't exist: " + featureName);
        }
        if (file == null || file.isEmpty()) {
            throw new InvalidParameterException("No file was uploaded.");
        }
        if (file.getContentType() == null || isBlank(file.getContentType())) {
            throw new InvalidParameterException("No content type provided.");
        }
        if (file.getOriginalFilename() == null || isBlank(file.getOriginalFilename())) {
            throw new InvalidParameterException("No filename provided.");
        }
    }
}
