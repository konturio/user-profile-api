package io.kontur.userprofile.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kontur.userprofile.AbstractIT;
import io.kontur.userprofile.dao.AppDao;
import io.kontur.userprofile.dao.AppFeatureDao;
import io.kontur.userprofile.dao.AppUserFeatureDao;
import io.kontur.userprofile.dao.FeatureDao;
import io.kontur.userprofile.model.dto.AppDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.AppFeature;
import io.kontur.userprofile.model.entity.AppUserFeature;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.AppController;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.wololo.geojson.Point;

@SpringBootTest
public class DaoIntegrityIT extends AbstractIT {
    @Autowired
    AppDao appDao;
    @Autowired
    AppFeatureDao appFeatureDao;
    @Autowired
    AppUserFeatureDao appUserFeatureDao;
    @Autowired
    FeatureDao featureDao;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    AppController appController;

    @Test
    @Transactional
    public void appCannotBeDeletedIfAppFeaturesExist() {
        User user = createUser();
        givenUserIsAuthenticated(user);

        UUID id = createApp();
        App app = appDao.getApp(id);
        createAppFeature(app);

        try {
            delete(id);
            entityManager.flush();
            throw new RuntimeException("expected exception was not thrown!");
        } catch (PersistenceException e) {
            assertEquals(ConstraintViolationException.class, e.getCause().getClass());

            ConstraintViolationException cve = (ConstraintViolationException) e.getCause();
            String sqlMessage = cve.getSQLException().getMessage();

            assertTrue(sqlMessage.contains("ERROR: update or"
                + " delete on table \"app\" violates foreign key constraint"
                + " \"app_features_app_app_id\" on table \"app_feature\""));
        }
    }

    @Test
    @Transactional
    public void appCannotBeDeletedIfAppUserFeaturesExist() {
        User user = createUser();
        givenUserIsAuthenticated(user);

        UUID id = createApp();
        App app = appDao.getApp(id);
        createAppUserFeature(app, user);

        appFeatureDao.deleteAllAppFeaturesFrom(app);

        try {
            delete(id);
            entityManager.flush();
            throw new RuntimeException("expected exception was not thrown!");
        } catch (PersistenceException e) {
            assertEquals(ConstraintViolationException.class, e.getCause().getClass());

            ConstraintViolationException cve = (ConstraintViolationException) e.getCause();
            String sqlMessage = cve.getSQLException().getMessage();
            assertTrue(sqlMessage.contains("ERROR: update or delete on table \"app\" violates "
                + "foreign key constraint \"app_user_features_app_app_id\" on table "
                + "\"app_user_feature\""));
        }
    }

    void createAppUserFeature(App app, User user) {
        Feature feature = featureDao.getFeatureByName("communities");
        AppUserFeature auf = new AppUserFeature(app, user, feature);
        entityManager.persist(auf);
    }

    void createAppFeature(App app) {
        Feature feature = featureDao.getFeatureByName("communities");
        AppFeature auf = new AppFeature(app, feature);
        entityManager.persist(auf);
    }

    UUID createApp() {
        AppDto request = new AppDto();
        request.setName(UUID.randomUUID().toString());
        request.setDescription(UUID.randomUUID().toString());
        request.setPublic(true);
        request.setFeatures(List.of("map_layers_panel"));
        request.setCenterGeometry(new Point(new double[] {1d, 2d}));
        request.setZoom(BigDecimal.ONE);

        AppDto result = appController.create(request);
        return result.getId();
    }

    void delete(UUID id) {
        App app = appDao.getApp(id);
        appDao.deleteApp(app);
    }

}
