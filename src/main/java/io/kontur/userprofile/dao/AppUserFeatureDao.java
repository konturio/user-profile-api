package io.kontur.userprofile.dao;

import com.fasterxml.jackson.databind.JsonNode;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.AppUserFeature;
import io.kontur.userprofile.model.entity.Feature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppUserFeatureDao {
    @PersistenceContext
    EntityManager entityManager;

    public Stream<Feature> getAppUserFeatures(App app, String username) {
        return selectAppUserFeaturesFor(app, username).stream()
            .map(AppUserFeature::getFeature);
    }

    public boolean userHasAnyConfiguredFeatures(App app, String username) {
        try {
            return entityManager.createQuery("select 1 from AppUserFeature a "
                    + "where a.app.id = ?1 and a.user.username = ?2")
                .setParameter(1, app.getId())
                .setParameter(2, username)
                .getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean userHasConfiguredFeature(App app, String username, Feature feature) {
        try {
            return entityManager.createQuery("select 1 from AppUserFeature a "
                            + "where a.app.id = ?1 and a.user.username = ?2 and a.feature.id = ?3")
                    .setParameter(1, app.getId())
                    .setParameter(2, username)
                    .setParameter(3, feature.getId())
                    .getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    public int updateAppUserFeatureConfiguration(App app, String username, Feature feature, JsonNode configuration) {
        return entityManager.createQuery("update AppUserFeature auf set auf.configuration = :configuration "
                        + "where auf.app.id = :appId "
                        + "and auf.user.username = :username "
                        + "and auf.feature.id = :featureId")
                .setParameter("configuration", configuration == null || configuration.isNull() ? null : configuration)
                .setParameter("appId", app.getId())
                .setParameter("username", username)
                .setParameter("featureId", feature.getId())
                .executeUpdate();
    }

    public void saveAppUserFeatures(List<AppUserFeature> appUserFeatures) {
        appUserFeatures.forEach(auf -> {
            if (!userHasConfiguredFeature(auf.getApp(), auf.getUser().getUsername(), auf.getFeature())){
                if (auf.getConfiguration() == null || auf.getConfiguration().isNull()) {
                    auf.setConfiguration(null);
                }
                entityManager.persist(auf);
            }
        });
    }

    public void deleteAllAppUserFeaturesFrom(App app) {
        List<AppUserFeature> appUserFeatures = selectAppUserFeaturesForDeleteFrom(app);
        appUserFeatures.forEach(auf -> entityManager.remove(auf));
    }

    public List<AppUserFeature> selectAppUserFeaturesFor(App app, String username) {
        return entityManager.createQuery("from AppUserFeature a "
                    + "where a.app.id = ?1 and a.user.username = ?2"
                    + " and a.feature.enabled = true",
                AppUserFeature.class)
            .setParameter(1, app.getId())
            .setParameter(2, username)
            .getResultList();
    }

    private List<AppUserFeature> selectAppUserFeaturesForDeleteFrom(App app) {
        return entityManager.createQuery("from AppUserFeature a "
                    + "where a.app.id = ?1",
                AppUserFeature.class)
            .setParameter(1, app.getId())
            .getResultList();
    }

}
