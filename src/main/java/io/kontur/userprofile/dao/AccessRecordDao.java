package io.kontur.userprofile.dao;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.kontur.userprofile.model.entity.accesscontrol.AccessRecord;
import io.kontur.userprofile.model.entity.accesscontrol.ResourceType;
import io.kontur.userprofile.model.entity.accesscontrol.Right;
import io.kontur.userprofile.model.entity.accesscontrol.SubjectType;
import io.kontur.userprofile.model.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class AccessRecordDao {
    @PersistenceContext
    EntityManager entityManager;

    final UserDao userDao;

    public List<AccessRecord> getAllAccessRecords() {
        try {
            return entityManager.createQuery("from AccessRecord", AccessRecord.class).getResultList();
        } catch (NoResultException e) {
            return List.of();
        }
    }

    public void newLayerBy(User user, long layerId) {
        final User platform = userDao.getUser("platform");
        final Right owner = getOwnerRight();
        final ResourceType layerType = getLayerType();
        final SubjectType userType = getUserType();
        final AccessRecord r = AccessRecord.builder()
                            .user(platform)
                            .allow(true)
                            .right(owner)
                            .resourceId(layerId)
                            .resourceType(layerType)
                            .subjectId(user.getId())
                            .subjectType(userType)
                            .build();
        entityManager.persist(r);
        entityManager.flush();
    }

    private ResourceType getLayerType() {
        return entityManager.createQuery("from ResourceType where name = 'layer'", ResourceType.class).getSingleResult();
    }

    private SubjectType getUserType() {
        return entityManager.createQuery("from SubjectType where name = 'user'", SubjectType.class).getSingleResult();
    }

    private Right getOwnerRight() {
        return entityManager.createQuery("from Right where name = 'owner'", Right.class).getSingleResult();
    }
}
