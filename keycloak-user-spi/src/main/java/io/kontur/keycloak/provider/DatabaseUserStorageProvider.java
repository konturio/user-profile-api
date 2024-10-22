package io.kontur.keycloak.provider;

import io.kontur.keycloak.model.UserAdapter;
import io.kontur.keycloak.service.UserService;
import io.kontur.keycloak.service.UserServiceImpl;
import io.kontur.userprofile.model.entity.user.Group;
import io.kontur.userprofile.model.entity.user.Role;
import io.kontur.userprofile.model.entity.user.User;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import jakarta.ws.rs.core.MultivaluedMap;
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

@Stateful(passivationCapable = false)
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

    DatabaseUserStorageProvider(KeycloakSession session, ComponentModel model) {
        this.session = session;
        this.component = model;
        this.userService = new UserServiceImpl(session);
    }


    @Override
    public int getUsersCount(RealmModel realm) {
        return BigDecimal.valueOf(userService.getCount()).intValueExact();
    }

    @Override
    public UserModel addUser(RealmModel realm, String email) {
        String groupName = "event-api-public";
        Optional<GroupModel> group = realm.getGroupsStream()
            .filter(it -> it.getName().equals(groupName)).findAny();

        HashSet<Group> groups = new HashSet<Group>();
        if (group.isPresent()) {
            groups.add(new Group(group.get().getId(), group.get().getName()));
        }

        User user = User.builder()
            .username(email)
            .email(email)
            .groups(groups)
            .build();
        userService.createUser(user);
        log.infof("Created user: %s", user);

        MultivaluedMap<String, String> formParameters = session.getContext().getHttpRequest().getDecodedFormParameters();
        String phoneNumber = formParameters.getFirst("phone_number");
        String linkedin = formParameters.getFirst("linkedin");

        UserAdapter userAdapter = UserAdapter.fromEntity(user, session, realm, component);

        setUserAttribute(userAdapter, "phone_number", phoneNumber);
        setUserAttribute(userAdapter, "linkedin", linkedin);

        return userAdapter;
    }

    private void setUserAttribute(UserAdapter userAdapter, String attributeName, String attributeValue) {
        if (attributeValue != null && !attributeValue.isEmpty()) {
            userAdapter.setSingleAttribute(attributeName, attributeValue);
        }
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        log.debugf("Removing user: %s", user);
        return userService.removeUser(user.getUsername());
    }

    public Stream<UserModel> getUsersStream(RealmModel realm) {
        return userService.getAllUsers()
            .map(u -> UserAdapter.fromEntity(u, session, realm, component));
    }

    public Stream<UserModel> getUsersStream(RealmModel realm, int firstResult, int maxResults) {
        return getUsersStream(realm).skip(firstResult).limit(maxResults);
    }

    /**
     * Searches for users whose username, email, first name or last name contain provided string.
     */
    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params) {
        log.infof("Searching for users");
        if (params == null || params.isEmpty()) {
            log.infof("returning all users");
            return getUsersStream(realm);
        }

        for (Map.Entry entry : params.entrySet()) {
            System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
        }

        return userService.searchForUsers(params.get("keycloak.session.realm.users.query.search"))
            .map(u -> UserAdapter.fromEntity(u, session, realm, component));
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params,
                                                 Integer firstResult, Integer maxResults) {
        return searchForUserStream(realm, params).skip(firstResult).limit(maxResults);
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group) {
        log.debugf("Getting group members (id: '%s') (name: '%s')", group.getId(), group.getName());
        return userService.getGroupMembers(group.getName())
            .map(u -> UserAdapter.fromEntity(u, session, realm, component));
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm,
                                                   GroupModel group,
                                                   Integer firstResult,
                                                   Integer maxResults) {
        return getGroupMembersStream(realm, group).skip(firstResult).limit(maxResults);
    }

    @Override
    public Stream<UserModel> getRoleMembersStream(RealmModel realm,
                                                  RoleModel role,
                                                  Integer firstResult,
                                                  Integer maxResults) {
        return getRoleMembersStream(realm, role).skip(firstResult).limit(maxResults);
    }

    @Override
    public Stream<UserModel> getRoleMembersStream(RealmModel realm, RoleModel role) {
        return userService.getRoleMembers(role.getName())
            .map(it -> UserAdapter.fromEntity(it, session, realm, component));
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm,
                                                                String attrName,
                                                                String attrValue) {
        throw new RuntimeException("Searching users by attributes is not supported");
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        log.debugf("Getting user by keycloak id %s", id);
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();

        return getUserByUsername(realm, username);
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        log.debugf("Getting user by email %s", email);
        return userService.getUserByEmail(email)
            .map(u -> UserAdapter.fromEntity(u, session, realm, component)).orElse(null);
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
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
