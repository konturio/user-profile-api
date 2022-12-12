package io.kontur.keycloak.provider;

import io.kontur.keycloak.model.UserAdapter;
import io.kontur.keycloak.service.UserService;
import io.kontur.userprofile.model.entity.user.Group;
import io.kontur.userprofile.model.entity.user.Role;
import io.kontur.userprofile.model.entity.user.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

@Stateful(passivationCapable=false)
@Local(DatabaseUserStorageProvider.class)
@JBossLog
@Getter
@Setter
@NoArgsConstructor
public class DatabaseUserStorageProvider
    implements UserLookupProvider,
    UserStorageProvider,
    UserRegistrationProvider,
    UserQueryProvider {

    @EJB
    private UserService userService;
    private KeycloakSession session;
    private ComponentModel component;

    @Override
    public int getUsersCount(RealmModel realm) {
        return BigDecimal.valueOf(userService.getCount()).intValueExact();
    }

    @Override
    public UserModel addUser(RealmModel realm, String email) {
        User user = User.builder()
            .username(email)
            .email(email)
            .build();
        userService.createUser(user);
        log.infof("Created user: %s", user);
        return UserAdapter.fromEntity(user, session, realm, component);
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        log.debugf("Removing user: %s", user);
        return userService.removeUser(user.getUsername());
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        return userService.getAllUsers()
            .map(u -> UserAdapter.fromEntity(u, session, realm, component))
            .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        List<UserModel> allUsers = getUsers(realm);
        return ListUtil.getEntriesFromList(allUsers, firstResult, maxResults);
    }

    /**
     * Searches for users whose username, email, first name or last name contain provided string.
     */
    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        log.debugf("Searching for user: search = '%s'", search);
        if (search == null || search.isBlank()) {
            return getUsers(realm);
        }

        return userService.searchForUsers(search)
            .map(u -> UserAdapter.fromEntity(u, session, realm, component))
            .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult,
                                         int maxResults) {
        return ListUtil.getEntriesFromList(searchForUser(search, realm), firstResult, maxResults);
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        if (params == null || params.isEmpty()) {
            return getUsers(realm);
        }
        throw new RuntimeException("Not implemented!"); //seems to be not used
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm,
                                         int firstResult, int maxResults) {
        return ListUtil.getEntriesFromList(searchForUser(params, realm), firstResult, maxResults);
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        log.debugf("Getting group members (id: '%s') (name: '%s')", group.getId(), group.getName());
        return userService.getGroupMembers(group.getName())
            .map(u -> UserAdapter.fromEntity(u, session, realm, component))
            .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult,
                                           int maxResults) {
        return ListUtil.getEntriesFromList(getGroupMembers(realm, group), firstResult, maxResults);
    }

    @Override
    public List<UserModel> getRoleMembers(RealmModel realm, RoleModel role, int firstResult,
                                          int maxResults) {
        return ListUtil.getEntriesFromList(getRoleMembers(realm, role), firstResult, maxResults);
    }

    @Override
    public List<UserModel> getRoleMembers(RealmModel realm, RoleModel role) {
        return userService.getRoleMembers(role.getName())
            .map(it -> UserAdapter.fromEntity(it, session, realm, component))
            .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue,
                                                        RealmModel realm) {
        throw new RuntimeException("Searching users by attributes is not supported");
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        log.debugf("Getting user by keycloak id %s", id);
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();

        return getUserByUsername(username, realm);
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        log.debugf("Getting user by email %s", email);
        return userService.getUserByEmail(email)
            .map(u -> UserAdapter.fromEntity(u, session, realm, component)).orElse(null);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        log.debugf("Getting user by username %s", username);
        return userService.getUserByUsername(username)
            .map(u -> UserAdapter.fromEntity(u, session, realm, component)).orElse(null);
    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {
        userService.getGroupMembers(group.getName())
            .forEach(user -> {
                Optional<Group> toRemove = user.getGroups().stream()
                    .filter(it -> group.getId().equals(it.getId())).findAny();
                toRemove.ifPresent(it -> user.getGroups().remove(it));
            });
    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {
        userService.getAllUsers().forEach(user -> {
            Optional<Role> toRemove = user.getRoles().stream()
                .filter(it -> role.getId().equals(it.getId())).findAny();
            toRemove.ifPresent(it -> user.getRoles().remove(it));
        });
    }

    @Override
    @Remove
    public void close() {
    }

    public static class ListUtil {

        protected static <T> List<T> getEntriesFromList(List<T> source, int fromIndex,
                                                        int maxEntries) {
            List<T> firstEntries = getEntriesStartingFromIndex(source, fromIndex);
            return getEntriesBeforeIndex(firstEntries, maxEntries);
        }

        protected static <T> List<T> getEntriesStartingFromIndex(List<T> source, int fromIndex) {
            if (source == null) {
                return List.of();
            }
            if (fromIndex <= 0) {
                return source;
            }
            if (fromIndex > source.size()) {
                return List.of();
            }
            return source.subList(fromIndex, source.size());
        }

        protected static <T> List<T> getEntriesBeforeIndex(List<T> source, int beforeIndex) {
            if (source == null) {
                return List.of();
            }
            if (beforeIndex < 0) {
                return source;
            }
            if (beforeIndex >= source.size()) {
                return source;
            }
            return source.subList(0, beforeIndex);
        }
    }
}
