package io.kontur.keycloak.model;

import io.kontur.userprofile.model.entity.user.Group;
import io.kontur.userprofile.model.entity.user.Role;
import io.kontur.userprofile.model.entity.user.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleContainerModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.RoleUtils;
import org.keycloak.storage.StorageId;

import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.*;

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
    public Set<RoleModel> getRealmRoleMappings() {
        return getRoleMappings().stream()
            .filter(it -> !it.isClientRole())
            .collect(Collectors.toSet());
    }

    @Override
    public Set<RoleModel> getClientRoleMappings(ClientModel client) {
        log.debugf("Client roles were requested for client with clientId:"
            + " %s (client.clientId: %s)", client.getId(), client.getClientId());
        return entity.getRoles().stream()
            .filter(Role::isClientRole)
            .filter(it -> client.getId().equals(it.getClientId()))
            .map(it -> RoleAdapter.fromEntity(it, client, component))
            .collect(Collectors.toSet());
    }

    @Override
    public boolean hasRole(RoleModel role) {
        return RoleUtils.hasRole(getRoleMappings(), role);
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
    public Set<RoleModel> getRoleMappings() {
        HashSet<RoleModel> roleMappings = new HashSet<>();

        if (realm.getDefaultRole() != null) {
            roleMappings.addAll(realm.getDefaultRole().getCompositesStream() //default realm roles
                .collect(Collectors.toSet()));
        }

        Set<RoleModel> entityRoles = entity.getRoles().stream()
            .map(r -> {
                if (r.getClientId() != null) {
                    ClientModel roleClient = realm.getClientById(r.getClientId());
                    return RoleAdapter.fromEntity(r, roleClient, component);
                }
                return RoleAdapter.fromEntity(r, realm, component);
            })
            .collect(Collectors.toSet());
        roleMappings.addAll(entityRoles);

        return roleMappings;
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
        if (val == null) return null;
        else return Long.valueOf(val);
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
        if (val == null) return true;
        else return Boolean.valueOf(val);
    }

    @Override
    public void setEnabled(boolean enabled) {
        setSingleAttribute(ENABLED_ATTRIBUTE, Boolean.toString(enabled));
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        if (FIRST_NAME.equals(name)) {
            setFirstName(value);
        } else if (LAST_NAME.equals(name)) {
            setLastName(value);
        } else if (EMAIL.equals(name)) {
            setEmail(value);
        }
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        if (UserModel.USERNAME.equals(name)) {
            setUsername((values != null && values.size() > 0) ? values.get(0) : null);
        } else {
            session.userFederatedStorage().setAttribute(realm, this.getId(), mapAttribute(name), values);
        }
    }

    @Override
    public void removeAttribute(String name) {
        session.userFederatedStorage().removeAttribute(realm, this.getId(), name);
    }

    @Override
    public String getFirstName() {
        return entity.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        entity.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return entity.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        entity.setLastName(lastName);
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
    public Set<GroupModel> getGroups() {
        return entity.getGroups().stream()
            .map(GroupAdapter::fromEntity)
            .collect(Collectors.toSet());
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
        return getGroups().stream().anyMatch(it -> group.getId().equals(it.getId()));
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
        MultivaluedHashMap<String, String> attributes = session.userFederatedStorage().getAttributes(realm, this.getId());
        if (attributes == null) {
            attributes = new MultivaluedHashMap<>();
        }
        return attributes;
    }

    @Override
    public Set<String> getRequiredActions() {
        return session.userFederatedStorage().getRequiredActions(realm, this.getId());
    }

    @Override
    public Stream<String> getRequiredActionsStream() {
        return session.userFederatedStorage().getRequiredActionsStream(realm, this.getId());
    }

    @Override
    public String getFirstAttribute(String name) {
        return session.userFederatedStorage().getAttributes(realm, this.getId()).getFirst(mapAttribute(name));
    }

    @Override
    public List<String> getAttribute(String name) {
        if (UserModel.USERNAME.equals(name)) {
            return Collections.singletonList(getUsername());
        }
        List<String> result = session.userFederatedStorage().getAttributes(realm, this.getId()).get(mapAttribute(name));
        return (result == null) ? Collections.emptyList() : result;
    }

    @Override
    public void addRequiredAction(String action) {
        session.userFederatedStorage().addRequiredAction(realm, this.getId(), action);
    }

    @Override
    public void removeRequiredAction(String action) {
        session.userFederatedStorage().removeRequiredAction(realm, this.getId(), action);
    }

    private Optional<Group> findGroupById(String groupId) {
        log.debugf("Looking for group with ID: %s", groupId);
        return entity.getGroups().stream().filter(it -> groupId.equals(it.getId()))
            .findAny();
    }

    protected String mapAttribute(String attributeName) {
        if (UserModel.FIRST_NAME.equals(attributeName)) {
            return FIRST_NAME_ATTRIBUTE;
        } else if (UserModel.LAST_NAME.equals(attributeName)) {
            return LAST_NAME_ATTRIBUTE;
        } else if (UserModel.EMAIL.equals(attributeName)) {
            return EMAIL_ATTRIBUTE;
        }
        return attributeName;
    }
}
