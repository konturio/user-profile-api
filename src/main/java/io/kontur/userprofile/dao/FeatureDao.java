package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.Feature;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
public class FeatureDao {
    @PersistenceContext
    EntityManager entityManager;

    public Feature getFeature(long id) {
        return entityManager.find(Feature.class, id);
    }

    public List<Feature> getPublicNonBetaFeatures() {
        return entityManager.createQuery("from Feature f where f.isPublic = true "
            + "and f.beta = false", Feature.class).getResultList();
    }
}
