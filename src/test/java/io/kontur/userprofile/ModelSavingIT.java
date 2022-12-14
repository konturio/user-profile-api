package io.kontur.userprofile;

import io.kontur.userprofile.dao.AppUserFeatureDao;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.AppFeature;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.user.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Disabled("Only for local debugging")
@SpringBootTest
public class ModelSavingIT {
    static final String MAP_LAYERS_PANEL = "map_layers_panel";
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    AppUserFeatureDao appUserFeatureDao;

//    @Test
//    @Transactional
//    @Rollback(value = false)
//    public void create() {
//        App app = new App(2L, "test", "test me", null, false);
//        User isemichastnov = User.builder()
//            .username("isemichastnov2")
//            .firstName("ilya")
//            .lastName("semichastnov")
//            .email("isemichastnov@kontur.io")
//            .useMetricUnits(true)
//            .build();
//        Feature publicNonBetaFeature = entityManager.createQuery("from Feature where name = ?1",
//            Feature.class).setParameter(1, MAP_LAYERS_PANEL).getSingleResult();
//
//        AppUserFeature appUserFeature = new AppUserFeature(app, isemichastnov, publicNonBetaFeature);
//        entityManager.persist(appUserFeature);
//
//    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void test2() {
        User user = entityManager.createQuery("from User u where u.username = ?1", User.class)
            .setParameter(1, "isemichastnov@kontur.io")
            .getSingleResult();

        App userApp = new App(null, "myApp", "my app", user, false, null, null, null, null);
        entityManager.persist(userApp);

        Feature layersPanel =
            entityManager.createQuery("from Feature where name = ?1", Feature.class)
                .setParameter(1, "map_layers_panel")
                .getSingleResult();

        AppFeature appFeature = new AppFeature(userApp, layersPanel, null);
        entityManager.persist(appFeature);
    }

//    @Test
//    @Transactional
//    @Rollback(value = false)
//    public void createIsemichastnov() {
//        //public feature
//        Feature publicFeature = entityManager.createQuery("from Feature where name = ?1",
//            Feature.class).setParameter(1, Feature.Names.MAP_LAYERS_PANEL).getSingleResult();
//        //non-public, non-beta feature
//        Feature nonPublicNonBetaFeature = entityManager.createQuery("from Feature where name = ?1",
//            Feature.class).setParameter(1, Feature.Names.ANALYTICS_PANEL).getSingleResult();
//        //non-public, beta feature
//        Feature nonPublicBetaFeature = entityManager.createQuery("from Feature where name = ?1",
//            Feature.class).setParameter(1, Feature.Names.INTERACTIVE_MAP).getSingleResult();
//        //event feed chosen by user
//        Feature eventFeed = entityManager.createQuery("from Feature where name = ?1",
//            Feature.class).setParameter(1, "test-gdacs").getSingleResult();
//
//
//        Role betaRole = new Role();
//        betaRole.setId("set-keycloak-id"); //should be created in Keycloak first!
//        betaRole.setName(Role.Names.BETA_FEATURES);
//
//        Subscription subscription =
//            new Subscription(true, "kontur-public", "bbox something",
//                "some type", "high severity", 123L);
//        User isemichastnov = User.builder()
//            .username("isemichastnov")
//            .firstName("ilya")
//            .lastName("semichastnov")
//            .email("isemichastnov@kontur.io")
//            .roles(Set.of(betaRole))
//            .featuresEnabledByUser(
//                List.of(publicFeature, nonPublicNonBetaFeature, nonPublicBetaFeature, eventFeed))
//            .useMetricUnits(true)
//            .subscription(subscription)
//            .build();
//        entityManager.persist(isemichastnov);
//
//        System.out.println(isemichastnov);
//    }
}
