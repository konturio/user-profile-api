package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.AppUserFeature;
import io.kontur.userprofile.model.entity.Feature;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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

    public void deleteAllAppUserFeaturesFrom(App app) {
        List<AppUserFeature> appUserFeatures = selectAppUserFeaturesForDeleteFrom(app);
        appUserFeatures.forEach(auf -> entityManager.remove(auf));
    }

    private List<AppUserFeature> selectAppUserFeaturesFor(App app, String username) {
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
