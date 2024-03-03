package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.Feature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeatureDao {
    @PersistenceContext
    EntityManager entityManager;

    public Feature getFeatureByName(String name) {
        try {
            return entityManager.createQuery("from Feature f"
                    + " where f.name = ?1", Feature.class)
                .setParameter(1, name)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Feature> getFeaturesAddedByDefaultToUserApps() {
        return entityManager.createQuery("from Feature f"
                + " where f.defaultForUserApps = ?1 and f.enabled = true", Feature.class)
            .setParameter(1, true)
            .getResultList();
    }
}
