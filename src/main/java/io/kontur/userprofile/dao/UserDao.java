package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.User;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
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
            return entityManager.createQuery("from User", User.class)
                .getResultList();
        } catch (NoResultException e) {
            return List.of();
        }
    }

    public void createUser(User user) {
        if (getUser(user.getUsername()) != null) {
            throw new WebApplicationException("Use other username!", HttpStatus.BAD_REQUEST);
        }
        if (getUserByEmail(user.getEmail()) != null) {
            throw new WebApplicationException("Use other email!", HttpStatus.BAD_REQUEST);
        }
        entityManager.persist(user);
    }
}
