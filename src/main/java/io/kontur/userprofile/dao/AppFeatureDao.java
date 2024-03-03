package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.AppFeature;
import io.kontur.userprofile.model.entity.Feature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class AppFeatureDao {
    @PersistenceContext
    EntityManager entityManager;

    public void saveAppFeatures(List<AppFeature> appFeatures) {
        appFeatures.forEach(appFeature -> {
            if (appFeature.getConfiguration() == null || appFeature.getConfiguration().isNull()) {
                appFeature.setConfiguration(null);
            }
            entityManager.persist(appFeature);
        });
    }

    public void deleteAllAppFeaturesFrom(App app) {
        List<AppFeature> appFeatures = selectAppFeaturesFor(app);
        appFeatures.forEach(af -> entityManager.remove(af));
    }

    public Stream<AppFeature> getAppFeaturesFor(App app) {
        return selectAppFeaturesFor(app).stream()
                .filter(appFeature -> appFeature.getFeature().isEnabled())
                .filter(appFeature -> !appFeature.getFeature().isBeta());
    }

    public Stream<Feature> getEnabledNonBetaAppFeaturesFor(App app) {
        List<AppFeature> appFeatures = selectAppFeaturesFor(app); //todo remove event feeds?

        return appFeatures.stream().map(AppFeature::getFeature)
            .filter(Feature::isEnabled)
            .filter(it -> !it.isBeta());
    }

    private List<AppFeature> selectAppFeaturesFor(App app) {
        return entityManager.createQuery("from AppFeature a "
                    + "where a.app.id = ?1 ",
                AppFeature.class)
            .setParameter(1, app.getId())
            .getResultList();
    }
}
