package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
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

    public List<User> getAllUsers() {
        try {
            return entityManager.createQuery("from User", User.class)
                .getResultList();
        } catch (NoResultException e) {
            return List.of();
        }
    }
}
