package io.kontur.userprofile.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kontur.userprofile.AbstractIT;
import io.kontur.userprofile.dao.AppDao;
import io.kontur.userprofile.dao.CustomAppFeatureDao;
import io.kontur.userprofile.dao.FeatureDao;
import io.kontur.userprofile.model.dto.AppDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.AppController;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DaoIntegrityIT extends AbstractIT {
    @Autowired
    AppDao appDao;
    @Autowired
    CustomAppFeatureDao customAppFeatureDao;
    @Autowired
    FeatureDao featureDao;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    AppController appController;

    private static final String configurationOneString = """
            {"statistics": [{
                          "formula": "sumX",
                          "x": "population"
                        }, {
                          "formula": "sumX",
                          "x": "populated_area_km2"
                        }]}""";

    @Test
    @Transactional
    public void appCannotBeDeletedIfAppFeaturesExist() throws IOException {
        User user = createUser();
        givenUserIsAuthenticated(user);

        UUID id = createApp();
        App app = appDao.getApp(id);
        createAppFeature(app, false);

        try {
            delete(id);
            entityManager.flush();
            throw new RuntimeException("expected exception was not thrown!");
        } catch (PersistenceException e) {
            String sqlMessage = e.getMessage();

            assertTrue(sqlMessage.contains("ERROR: update or"
                + " delete on table \"app\" violates foreign key constraint"
                + " \"fk_custom_app_feature_app_id\" on table \"custom_app_feature\""));
        }
    }

    @Test
    @Transactional
    public void appCannotBeDeletedIfAppUserFeaturesExist() throws IOException {
        User user = createUser();
        givenUserIsAuthenticated(user);

        UUID id = createApp();
        App app = appDao.getApp(id);
        createAppFeature(app, true);

        customAppFeatureDao.deleteAllAppFeaturesFrom(app);

        try {
            delete(id);
            entityManager.flush();
            throw new RuntimeException("expected exception was not thrown!");
        } catch (PersistenceException e) {
            String sqlMessage = e.getMessage();
            assertTrue(sqlMessage.contains("ERROR: update or delete on table \"app\" violates "
                + "foreign key constraint \"fk_custom_app_feature_app_id\" on table "
                + "\"custom_app_feature\""));
        }
    }

    void createAppFeature(App app, boolean authenticated) {
        Feature feature = featureDao.getFeatureByName("side_bar");
        entityManager.createNativeQuery("insert into custom_app_feature (app_id, feature_id, authenticated, role_id, configuration_for_user_id, configuration) "
                        + "values (:appId, :featureId, :authenticated, null, null, null) "
                        + "on conflict do nothing")
                .setParameter("appId", app.getId())
                .setParameter("featureId", feature.getId())
                .setParameter("authenticated", authenticated)
                .executeUpdate();
    }

    UUID createApp() throws IOException {
        AppDto request = new AppDto();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode configurationOne = mapper.readTree(configurationOneString);

        request.setName(UUID.randomUUID().toString());
        request.setDescription(UUID.randomUUID().toString());
        request.setPublic(true);
        request.setFeaturesConfig(Map.of("map_layers_panel", configurationOne));
        request.setExtent(Arrays.asList(new BigDecimal(-1), new BigDecimal(-8),
                                        new BigDecimal(1), new BigDecimal(8)));

        AppDto result = appController.create(request);
        return result.getId();
    }

    void delete(UUID id) {
        App app = appDao.getApp(id);
        appDao.deleteApp(app);
    }

}
