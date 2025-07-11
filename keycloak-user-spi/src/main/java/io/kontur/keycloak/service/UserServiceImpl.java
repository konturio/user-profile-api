package io.kontur.keycloak.service;

import io.kontur.userprofile.model.entity.user.User;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Objects;
import java.util.Set;
import java.util.List;
import java.util.stream.Stream;
import javax.ejb.Singleton;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;

@JBossLog
@Singleton
public class UserServiceImpl extends JpaService<User> implements UserService {
    private static final String EMAIL_FIELD = "email";
    private static final String USERNAME_FIELD = "username";
    private static final String DEFAULT_TRIAL_ROLE = "kontur_atlas_trial";
    private static final String RISK_COMPASS_TRIAL_ROLE = "risk_compass_trial";
    private static final int DEFAULT_TRIAL_DAYS = 14;

    public UserServiceImpl(KeycloakSession session) {
        Set<JpaConnectionProvider> ss = session.getAllProviders(JpaConnectionProvider.class);
        entityManager = session
            .getProvider(JpaConnectionProvider.class, "user-store")
            .getEntityManager();
    }

    public long getCount() {
        return count(User.class);
    }

    public Stream<User> getAllUsers() {
        return getAll(User.class);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        try {
            return queryEntityBy(User.class, EMAIL_FIELD, email);
        } catch (Exception e) {
            handleLookupException(User.class, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        try {
            return queryEntityBy(User.class, USERNAME_FIELD, username);
        } catch (Exception e) {
            handleLookupException(User.class, e);
        }
        return Optional.empty();
    }

    @Override
    public Stream<User> searchForUsers(String search) {
        try {
            return queryUserByBasicParams(search);
        } catch (Exception e) {
            handleLookupException(User.class, e);
        }
        return Stream.of();
    }

    @Override
    public Stream<User> getGroupMembers(String groupName) {
        try {
            return queryUsersByGroupName(groupName);
        } catch (Exception e) {
            handleLookupException(User.class, e);
        }
        return Stream.of();
    }

    @Override
    public Stream<User> getRoleMembers(String name) {
        try {
            return queryUsersByRoleName(name);
        } catch (Exception e) {
            handleLookupException(User.class, e);
        }
        return Stream.of();
    }

    @Override
    public void createUser(User user) {
        entityManager.persist(user); // duplicates check is done by keycloak
        entityManager.flush(); // ensure ID is generated before assigning roles
        assignTrialRoles(
            user,
            List.of(DEFAULT_TRIAL_ROLE, RISK_COMPASS_TRIAL_ROLE),
            DEFAULT_TRIAL_DAYS
        );
    }

    private void assignTrialRoles(User user, List<String> roleNames, int days) {
        if (roleNames == null || roleNames.isEmpty()) {
            return;
        }

        int updatedRows = entityManager.createNativeQuery(
                "INSERT INTO user_custom_role (user_id, role_id, started_at, ended_at) " +
                "SELECT :userId, cr.id, :startedAt, :endedAt FROM custom_role cr " +
                "WHERE cr.name IN (:roleNames) AND NOT EXISTS (" +
                "SELECT 1 FROM user_custom_role ucr WHERE ucr.user_id = :userId AND ucr.role_id = cr.id)")
            .setParameter("userId", user.getId())
            .setParameter("roleNames", roleNames)
            .setParameter("startedAt", OffsetDateTime.now())
            .setParameter("endedAt", OffsetDateTime.now().plusDays(days))
            .executeUpdate();

        if (updatedRows > 0) {
            log.debugf("Assigned trial roles %s to user %d", roleNames, user.getId());
        } else {
            log.errorf("Failed to assign trial roles %s to user %d (already assigned or roles not found)", roleNames, user.getId());
        }
    }

    public boolean removeUser(String username) {
        Optional<User> user = getUserByUsername(username);
        if (user.isEmpty()) {
            return false;
        }
        try {
            entityManager.remove(user.get());
            log.infof("Removed user: %s", user.get());
            return true;
        } catch (Exception e) {
            log.errorf("Caught exception when trying to remove user %s: %s", username,
                e.getMessage(), e);
        }
        return false;
    }

    private void assignTrialRole(User user, String roleName, int days) {
        int updatedRows = entityManager.createNativeQuery(
                        "INSERT INTO user_custom_role (user_id, role_id, started_at, ended_at) " +
                                "SELECT :userId, cr.id, :startedAt, :endedAt FROM custom_role cr " +
                                "WHERE cr.name = :roleName AND NOT EXISTS (SELECT 1 FROM user_custom_role ucr WHERE ucr.user_id = :userId AND ucr.role_id = cr.id)")
                .setParameter("userId", user.getId())
                .setParameter("roleName", roleName)
                .setParameter("startedAt", OffsetDateTime.now())
                .setParameter("endedAt", OffsetDateTime.now().plusDays(days))
                .executeUpdate();

        if (updatedRows > 0) {
            log.debugf("Assigned trial role '%s' to user %d", roleName, user.getId());
        } else {
            log.errorf("Failed to assign trial role '%s' to user %d (already assigned or role not found)", roleName, user.getId());
        }
    }

    private Stream<User> queryUsersByGroupName(String groupName) {
        if (groupName == null || groupName.isBlank()) {
            return Stream.empty();
        }

        return entityManager.createQuery(
                "from User u inner join u.groups as g where g.name = ?1", User.class)
            .setParameter(1, groupName)
            .getResultStream();
    }

    private Stream<User> queryUsersByRoleName(String name) {
        if (name == null || name.isBlank()) {
            return Stream.empty();
        }

        return entityManager.createQuery(
                "from User u inner join u.roles as g where g.name = ?1", User.class)
            .setParameter(1, name)
            .getResultStream();
    }

    private Stream<User> queryUserByBasicParams(String search) {
        return entityManager.createQuery("from User u where u.username like ?1 or "
                + "u.email like ?1 or u.fullName like ?1",
            User.class).setParameter(1, "%" + Objects.toString(search, "") + "%").getResultStream();
    }
}
