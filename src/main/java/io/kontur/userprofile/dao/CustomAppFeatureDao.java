package io.kontur.userprofile.dao;

import com.fasterxml.jackson.databind.JsonNode;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.CustomAppFeature;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomAppFeatureDao {
    @PersistenceContext
    EntityManager entityManager;

    public void saveAppFeatures(List<CustomAppFeature> appFeatures) {
        appFeatures.forEach(appFeature -> {
            // Convert JSON null to Java/SQL null to prevent unexpected behavior when using this column later.
            if (appFeature.getConfiguration() != null && appFeature.getConfiguration().isNull()) {
                appFeature.setConfiguration(null);
            }
            entityManager.createNativeQuery("insert into custom_app_feature (app_id, feature_id, authenticated, role_id, configuration_for_user_id, configuration) "
                            + "values (:appId, :featureId, false, null, null, :configuration) "
                            + "on conflict do nothing")
                    .setParameter("appId", appFeature.getApp().getId())
                    .setParameter("featureId", appFeature.getFeature().getId())
                    .setParameter("configuration", appFeature.getConfiguration())
                    .executeUpdate();
        });
    }

    public int saveConfigurationForUser(App app, Feature feature, User user, JsonNode configuration) {
        if (configuration == null || configuration.isEmpty()) {
            return entityManager.createNativeQuery("delete from custom_app_feature "
                            + "where app_id = :appId "
                            + "and feature_id = :featureId "
                            + "and authenticated "
                            + "and configuration_for_user_id = :configurationForUserId")
                    .setParameter("appId", app.getId())
                    .setParameter("featureId", feature.getId())
                    .setParameter("configurationForUserId", user.getId())
                    .executeUpdate();
        }

        return entityManager.createNativeQuery("insert into custom_app_feature (app_id, feature_id, authenticated, role_id, configuration_for_user_id, configuration) "
                        + "values (:appId, :featureId, true, null, :configurationForUserId, :configuration) "
                        + "on conflict (app_id, feature_id, authenticated, role_id, configuration_for_user_id) "
                        + "do update set configuration = excluded.configuration")
                .setParameter("appId", app.getId())
                .setParameter("featureId", feature.getId())
                .setParameter("configurationForUserId", user.getId())
                .setParameter("configuration", configuration)
                .executeUpdate();
    }

    public void deleteAllAppFeaturesFrom(App app) {
        List<CustomAppFeature> appFeatures = entityManager.createQuery(
                "from CustomAppFeature a where a.app.id = ?1 and not authenticated",
                        CustomAppFeature.class)
                .setParameter(1, app.getId())
                .getResultList();;
        appFeatures.forEach(af -> entityManager.remove(af));
    }

    public List<CustomAppFeature> getAllFeaturesAvailableToUser(App app, String username, List<Long> roleIds) {
        Map<Long, CustomAppFeature> featureMap = new HashMap<>();
        updateFeaturesMap(featureMap, getGuestAppFeaturesFor(app), false);
        if (isNotBlank(username)) {
            updateFeaturesMap(featureMap, getAuthenticatedAppFeaturesFor(app), false);
            if (roleIds != null && !roleIds.isEmpty()) {
                updateFeaturesMap(featureMap, getRoleBasedAppFeaturesFor(app, roleIds), false);
            }
            updateFeaturesMap(featureMap, getConfigurationsDefinedForUser(app, username), true);
        }
        return new ArrayList<>(featureMap.values());
    }

    private void updateFeaturesMap(Map<Long, CustomAppFeature> featureMap, List<CustomAppFeature> features, boolean onlyOverride) {
        for (CustomAppFeature feature : features) {
            if (!onlyOverride || featureMap.containsKey(feature.getId())) {
                featureMap.put(feature.getId(), feature);
            }
        }
    }

    private List<CustomAppFeature> getGuestAppFeaturesFor(App app) {
        return entityManager.createQuery("from CustomAppFeature a "
                                + "where a.app.id = ?1 and not authenticated and a.feature.enabled",
                        CustomAppFeature.class)
                .setParameter(1, app.getId())
                .getResultList();
    }

    private List<CustomAppFeature> getAuthenticatedAppFeaturesFor(App app) {
        return entityManager.createQuery("from CustomAppFeature a "
                                + "where a.app.id = ?1 and authenticated and a.feature.enabled",
                        CustomAppFeature.class)
                .setParameter(1, app.getId())
                .getResultList();
    }

    private List<CustomAppFeature> getRoleBasedAppFeaturesFor(App app, List<Long> roleIds) {
        return entityManager.createQuery("from CustomAppFeature a "
                                + "where a.app.id = ?1 and authenticated and a.role.id in ?2 and a.feature.enabled",
                        CustomAppFeature.class)
                .setParameter(1, app.getId())
                .setParameter(2, roleIds)
                .getResultList();
    }

    private List<CustomAppFeature> getConfigurationsDefinedForUser(App app, String username) {
        return entityManager.createQuery("from CustomAppFeature a "
                                + "where a.app.id = ?1 and authenticated and a.configurationForUser.username = ?2 and a.feature.enabled",
                        CustomAppFeature.class)
                .setParameter(1, app.getId())
                .setParameter(2, username)
                .getResultList();
    }
}
