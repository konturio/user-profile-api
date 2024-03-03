package io.kontur.keycloak.model;

import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.*;

import io.kontur.userprofile.model.entity.user.Group;
import io.kontur.userprofile.model.entity.user.Role;
import io.kontur.userprofile.model.entity.user.User;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleContainerModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.RoleUtils;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageUtil;

@JBossLog
public class UserAdapter implements UserModel {
    private final User entity;
    private final KeycloakSession session;
    private final StorageId storageId;
    private final RealmModel realm;
    private final ComponentModel component;

    private UserAdapter(User entity, KeycloakSession session, RealmModel realm,
                        ComponentModel component) {
        this.entity = entity;
        this.session = session;
        this.realm = realm;
        this.component = component;
        storageId = new StorageId(component.getId(), getUsername());
    }

    public static UserAdapter fromEntity(User entity, KeycloakSession session, RealmModel realm,
                                         ComponentModel component) {
        if (entity == null) {
            throw new IllegalArgumentException("User must not be null!");
        }
        return new UserAdapter(entity, session, realm, component);
    }

    @Override
    public Stream<RoleModel> getRealmRoleMappingsStream() {
        return getRoleMappingsStream()
            .filter(it -> !it.isClientRole());
    }

    @Override
    public Stream<RoleModel> getClientRoleMappingsStream(ClientModel client) {
        log.debugf("Client roles were requested for client with clientId:"
            + " %s (client.clientId: %s)", client.getId(), client.getClientId());
        return entity.getRoles().stream()
            .filter(Role::isClientRole)
            .filter(it -> client.getId().equals(it.getClientId()))
            .map(it -> RoleAdapter.fromEntity(it, client, realm));
    }

    @Override
    public boolean hasRole(RoleModel role) {
        return RoleUtils.hasRole(getRoleMappingsStream(), role)
                || RoleUtils.hasRoleFromGroup(getGroupsStream(), role, true);
    }

    @Override
    public void grantRole(RoleModel roleModel) {
        String clientClientId = null; //null for a realm role //client.clientId in Keycloak model
        String clientId = null; //null for a realm role

        RoleContainerModel roleContainerModel = roleModel.getContainer();

        if (roleContainerModel instanceof ClientModel) {
            clientId = roleContainerModel.getId();
            clientClientId = ((ClientModel) roleContainerModel).getClientId();
        }

        Role role = new Role(roleModel.getId(), roleModel.getName(), clientId, clientClientId);

        entity.getRoles().add(role);
        log.infof("Granted role to %s: id %s, name: %s, clientId: %s, clientClientId: %s",
            entity.getId(), roleModel.getId(), roleModel.getName(), clientId, clientClientId);
    }

    @Override
    public Stream<RoleModel> getRoleMappingsStream() {
        HashSet<RoleModel> roleMappings = new HashSet<>();

        if (realm.getDefaultRole() != null) {
            roleMappings.addAll(realm.getDefaultRole().getCompositesStream() //default realm roles
                .collect(Collectors.toSet()));
        }

        roleMappings.addAll(getGroupsStream()
                            .flatMap(GroupModel::getRoleMappingsStream)
                            .collect(Collectors.toSet()));

        Set<RoleModel> entityRoles = entity.getRoles().stream()
            .map(r -> {
                if (r.getClientId() != null) {
                    ClientModel roleClient = realm.getClientById(r.getClientId());
                    if (roleClient != null) {
                        return RoleAdapter.fromEntity(r, roleClient, realm);
                    }
                }
                return RoleAdapter.fromEntity(r, realm, realm);
            })
            .collect(Collectors.toSet());
        roleMappings.addAll(entityRoles);

        return roleMappings.stream();
    }

    @Override
    public void deleteRoleMapping(RoleModel roleModel) {
        Optional<Role> toRemove = findRoleById(roleModel.getId());
        toRemove.ifPresent(role -> {
            entity.getRoles().remove(role);
            log.infof("Removed role from %s: id %s, name %s", entity.getId(),
                roleModel.getId(), roleModel.getName());
        });
    }

    private Optional<Role> findRoleById(String roleId) {
        return entity.getRoles().stream().filter(it -> roleId.equals(it.getId())).findAny();
    }


    @Override
    public String getId() {
        return storageId.getId(); //keycloak's id
    }

    @Override
    public String getUsername() {
        return entity.getUsername();
    }

    @Override
    public void setUsername(String username) {
    }

    @Override
    public Long getCreatedTimestamp() {
        String val = getFirstAttribute(CREATED_TIMESTAMP_ATTRIBUTE);
        if (val == null) {
            return null;
        }
        return Long.valueOf(val);
    }

    @Override
    public void setCreatedTimestamp(Long timestamp) {
        if (timestamp == null) {
            setSingleAttribute(CREATED_TIMESTAMP_ATTRIBUTE, null);
        } else {
            setSingleAttribute(CREATED_TIMESTAMP_ATTRIBUTE, Long.toString(timestamp));
        }

    }

    @Override
    public boolean isEnabled() {
        String val = getFirstAttribute(ENABLED_ATTRIBUTE);
        if (val == null) {
            return true;
        }
        return Boolean.parseBoolean(val);
    }

    @Override
    public void setEnabled(boolean enabled) {
        setSingleAttribute(ENABLED_ATTRIBUTE, Boolean.toString(enabled));
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        if (USERNAME.equals(name)) {
            setUsername(value);
        } else if (FIRST_NAME.equals(name)) {
            setFirstName(value);
        } else if (LAST_NAME.equals(name)) {
            setLastName(value);
        } else if (EMAIL.equals(name)) {
            setEmail(value);
        } else {
            UserStorageUtil.userFederatedStorage(session).setSingleAttribute(realm, this.getId(), mapAttribute(name), value);
        }
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        if (USERNAME.equals(name)) {
            setUsername((values != null && values.size() > 0) ? values.get(0) : null);
        } else if (EMAIL.equals(name)) {
            setEmail((values != null && values.size() > 0) ? values.get(0) : null);
        } else if (LAST_NAME.equals(name)) {
            setLastName((values != null && values.size() > 0) ? values.get(0) : null);
        } else if (FIRST_NAME.equals(name)) {
            setFirstName((values != null && values.size() > 0) ? values.get(0) : null);
        } else {
            UserStorageUtil.userFederatedStorage(session).setAttribute(realm, this.getId(), mapAttribute(name), values);
        }
    }

    @Override
    public void removeAttribute(String name) {
        UserStorageUtil.userFederatedStorage(session).removeAttribute(realm, this.getId(), name);
    }

    @Override
    public String getFirstName() {
        return entity.getFullName();
    }

    @Override
    public void setFirstName(String firstName) {
        entity.setFullName(firstName);
    }

    @Override
    public String getLastName() {
        return "";
    }

    @Override
    public void setLastName(String lastName) {
        //do nothing
    }

    @Override
    public String getEmail() {
        return entity.getEmail();
    }

    @Override
    public void setEmail(String email) {
        entity.setEmail(email);
    }

    @Override
    public boolean isEmailVerified() {
        String val = getFirstAttribute(EMAIL_VERIFIED_ATTRIBUTE);
        if (val == null) return false;
        else return Boolean.valueOf(val);
    }

    @Override
    public void setEmailVerified(boolean verified) {
        setSingleAttribute(EMAIL_VERIFIED_ATTRIBUTE, Boolean.toString(verified));
    }

    @Override
    public Stream<GroupModel> getGroupsStream() {
        return entity.getGroups().stream()
                .filter(g -> realm.getGroupById(g.getId()) != null)
                .map((Group group) -> GroupAdapter.fromEntity(group, realm));
    }

    @Override
    public void joinGroup(GroupModel groupModel) {
        Group group = new Group(groupModel.getId(), groupModel.getName());
        entity.getGroups().add(group);
        log.infof("Joined group, user %s: id %s, name %s", entity.getId(),
            groupModel.getId(), groupModel.getName());
    }

    @Override
    public void leaveGroup(GroupModel groupModel) {
        Optional<Group> toRemove = findGroupById(groupModel.getId());
        toRemove.ifPresent(group -> entity.getGroups().remove(group));

        log.infof("Left group, user %s: id %s, name %s", entity.getId(),
            groupModel.getId(), groupModel.getName());
    }

    @Override
    public boolean isMemberOf(GroupModel group) {
        log.debugf("Checking isMemberOf: %s", group.toString());
        //todo take parent groups into account once they're supported
        return getGroupsStream().anyMatch(it -> group.getId().equals(it.getId()));
    }

    @Override
    public String getFederationLink() {
        return null;
    }

    @Override
    public void setFederationLink(String link) {
    }

    @Override
    public String getServiceAccountClientLink() {
        return null;
    }

    @Override
    public void setServiceAccountClientLink(String clientInternalId) {
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        MultivaluedHashMap<String, String> attributes = UserStorageUtil
            .userFederatedStorage(session)
            .getAttributes(realm, this.getId());
        if (attributes == null) {
            attributes = new MultivaluedHashMap<>();
        }
        List<String> firstName = attributes.remove(FIRST_NAME_ATTRIBUTE);
        attributes.add(UserModel.FIRST_NAME,
                       firstName != null && firstName.size() >= 1 ? firstName.get(0) : null);
        List<String> lastName = attributes.remove(LAST_NAME_ATTRIBUTE);
        attributes.add(UserModel.LAST_NAME,
                       lastName != null && lastName.size() >= 1 ? lastName.get(0) : null);
        List<String> email = attributes.remove(EMAIL_ATTRIBUTE);
        attributes.add(UserModel.EMAIL, email != null && email.size() >= 1 ? email.get(0) : null);
        attributes.add(UserModel.USERNAME, getUsername());

        attributes.remove(EMAIL_VERIFIED_ATTRIBUTE);
        attributes.remove(ENABLED_ATTRIBUTE);

        return attributes;
    }

    @Override
    public Stream<String> getRequiredActionsStream() {
        return UserStorageUtil
            .userFederatedStorage(session)
            .getRequiredActionsStream(realm, this.getId());
    }

    @Override
    public String getFirstAttribute(String name) {
        if (CREATED_TIMESTAMP_ATTRIBUTE.equals(name)) {
            // todo: import attr from users table
            return "0";
        }
        String str = UserStorageUtil
            .userFederatedStorage(session)
            .getAttributes(realm, this.getId())
            .getFirst(mapAttribute(name));
        if (str != null) {
            return str;
        } else {
            if (USERNAME.equals(name)) {
                return entity.getEmail();
            }
        }
        return "";
    }

    @Override
    public Stream<String> getAttributeStream(String name) {
        if (UserModel.USERNAME.equals(name)) {
            return Stream.of(getUsername());
        }
        List<String> result = UserStorageUtil
            .userFederatedStorage(session)
            .getAttributes(realm, this.getId())
            .get(mapAttribute(name));
        return (result == null) ? Stream.of() : result.stream();
    }

    @Override
    public void addRequiredAction(String action) {
        UserStorageUtil
            .userFederatedStorage(session)
            .addRequiredAction(realm, this.getId(), action);
    }

    @Override
    public void removeRequiredAction(String action) {
        UserStorageUtil
            .userFederatedStorage(session)
            .removeRequiredAction(realm, this.getId(), action);
    }

    private Optional<Group> findGroupById(String groupId) {
        log.debugf("Looking for group with ID: %s", groupId);
        return entity.getGroups().stream().filter(it -> groupId.equals(it.getId()))
            .findAny();
    }

    
    public String mapAttribute(String attributeName) {
        if (UserModel.FIRST_NAME.equals(attributeName)) {
            return FIRST_NAME_ATTRIBUTE;
        } else if (UserModel.LAST_NAME.equals(attributeName)) {
            return LAST_NAME_ATTRIBUTE;
        } else if (UserModel.EMAIL.equals(attributeName)) {
            return EMAIL_ATTRIBUTE;
        } else if (attributeName.equals("username")) {
            return EMAIL_ATTRIBUTE;
        }
        return attributeName;
    }

    public SubjectCredentialManager credentialManager() {
        return new LegacyUserCredentialManager(this.session, this.realm, this);
    }
}
