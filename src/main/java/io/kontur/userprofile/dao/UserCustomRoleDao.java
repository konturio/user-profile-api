package io.kontur.userprofile.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCustomRoleDao {
    @PersistenceContext
    EntityManager entityManager;

    public List<Long> getUserRoleIds(String username) {
        return entityManager.createQuery("select distinct r.role.id from UserCustomRole r "
                                + "where r.user.username = ?1" +
                                "and (r.startedAt is null and r.endedAt is null or current_timestamp between r.startedAt and r.endedAt)",
                        Long.class)
                .setParameter(1, username)
                .getResultList();
    }
}
