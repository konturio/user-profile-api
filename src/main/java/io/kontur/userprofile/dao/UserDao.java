package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDao {
    @PersistenceContext
    EntityManager entityManager;

    public User getUser(String username) {
        try {
            return entityManager.createQuery("from User where username = ?1", User.class)
                .setParameter(1, username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getUserByEmail(String email) {
        try {
            return entityManager.createQuery("from User where email = ?1", User.class)
                .setParameter(1, email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return entityManager.createQuery("from User", User.class).getResultList();
        } catch (NoResultException e) {
            return List.of();
        }
    }

    public void createUser(User user) {
        entityManager.persist(user);
    }

    public void updateUser(User user) {
        entityManager.merge(user);
    }
}
