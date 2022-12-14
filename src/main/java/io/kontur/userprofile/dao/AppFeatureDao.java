package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.AppFeature;
import io.kontur.userprofile.model.entity.Feature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class AppFeatureDao {
    @PersistenceContext
    EntityManager entityManager;

    public void saveAppFeatures(List<AppFeature> appFeatures) {
        appFeatures.forEach(appFeature -> entityManager.persist(appFeature));
    }

    public void removeAppFeaturesFrom(App app, List<Feature> features) {
        List<AppFeature> toRemove = selectAppFeaturesFor(app)
            .stream()
            .filter(it -> features.contains(it.getFeature()))
            .toList();

        for (AppFeature appFeature : toRemove) {
            entityManager.remove(appFeature);
        }
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

    public Stream<Feature> getAppFeaturesIncludingDisabledAndBetaFor(App app) {
        List<AppFeature> appFeatures = selectAppFeaturesFor(app); //todo remove event feeds?

        return appFeatures.stream().map(AppFeature::getFeature);
    }

    private List<AppFeature> selectAppFeaturesFor(App app) {
        return entityManager.createQuery("from AppFeature a "
                    + "where a.app.id = ?1 ",
                AppFeature.class)
            .setParameter(1, app.getId())
            .getResultList();
    }
}
