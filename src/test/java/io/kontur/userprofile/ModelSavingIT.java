package io.kontur.userprofile;

import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.Role;
import io.kontur.userprofile.model.entity.Subscription;
import io.kontur.userprofile.model.entity.User;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Disabled("Only for local debugging")
@SpringBootTest
public class ModelSavingIT {
    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Transactional
    @Rollback(value = false)
    public void createIsemichastnov() {
        //public feature
        Feature publicFeature = entityManager.createQuery("from Feature where name = ?1",
            Feature.class).setParameter(1, Feature.Names.MAP_LAYERS_PANEL).getSingleResult();
        //non-public, non-beta feature
        Feature nonPublicNonBetaFeature = entityManager.createQuery("from Feature where name = ?1",
            Feature.class).setParameter(1, Feature.Names.ANALYTICS_PANEL).getSingleResult();
        //non-public, beta feature
        Feature nonPublicBetaFeature = entityManager.createQuery("from Feature where name = ?1",
            Feature.class).setParameter(1, Feature.Names.INTERACTIVE_MAP).getSingleResult();
        //event feed chosen by user
        Feature eventFeed = entityManager.createQuery("from Feature where name = ?1",
            Feature.class).setParameter(1, "test-gdacs").getSingleResult();


        Role betaRole = new Role();
        betaRole.setId("set-keycloak-id"); //should be created in Keycloak first!
        betaRole.setName(Role.Names.BETA_FEATURES);

        Subscription subscription =
            new Subscription(true, "disaster-ninja-02", "bbox something",
                "some type", "high severity", 123L);
        User isemichastnov = User.builder()
            .username("isemichastnov")
            .firstName("ilya")
            .lastName("semichastnov")
            .email("isemichastnov@kontur.io")
            .roles(Set.of(betaRole))
            .featuresEnabledByUser(
                List.of(publicFeature, nonPublicNonBetaFeature, nonPublicBetaFeature, eventFeed))
            .useMetricUnits(true)
            .subscription(subscription)
            .build();
        entityManager.persist(isemichastnov);

        System.out.println(isemichastnov);
    }
}
