package io.kontur.userprofile;

import io.kontur.userprofile.dao.CustomAppFeatureDao;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.CustomAppFeature;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Disabled("Only for local debugging")
@SpringBootTest
public class ModelSavingIT {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    CustomAppFeatureDao customAppFeatureDao;

    @Test
    @Transactional
    @Rollback(value = false)
    public void test2() {
        User user = entityManager.createQuery("from User u where u.username = ?1", User.class)
            .setParameter(1, "isemichastnov@kontur.io")
            .getSingleResult();

        App userApp = new App(null, "myApp", "my app", user, false, null, null, null, null, null);
        entityManager.persist(userApp);

        Feature layersPanel =
            entityManager.createQuery("from Feature where name = ?1", Feature.class)
                .setParameter(1, "map_layers_panel")
                .getSingleResult();

        CustomAppFeature appFeature = new CustomAppFeature(userApp, layersPanel, false);
        entityManager.persist(appFeature);
    }

}
