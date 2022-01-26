package io.kontur.keycloak.model;

import io.kontur.userprofile.model.entity.Group;
import io.kontur.userprofile.model.entity.Role;
import io.kontur.userprofile.model.entity.User;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleContainerModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.RoleUtils;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;

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
        return storageId.getId(); //keyclock's id
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
        return null;
    }

    @Override
    public void setCreatedTimestamp(Long timestamp) {
    }

    @Override
    public boolean isEnabled() {
        return true; //hardcode
    }

    @Override
    public void setEnabled(boolean enabled) {
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
        log.info("name = " + name);
        if (values.size() == 1) {
            setSingleAttribute(name, values.get(0));
        } else {
            throw new RuntimeException("No multivalues attributes are supported!");
        }
    }

    @Override
    public void removeAttribute(String name) {
        throw new ReadOnlyException("User is read only for this update");
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
        return true; //hardcode
    }

    @Override
    public void setEmailVerified(boolean verified) {
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
        //can't return empty map here - this causes errors within Keycloak itself
        return Map.of("DON'T USE ATTRIBUTES", List.of());
    }

    @Override
    public Set<String> getRequiredActions() {
        return Set.of(); //actions like 'change temporary password' / etc (may be used by keycloak)
    }

    @Override
    public String getFirstAttribute(String name) {
        return null;
    }

    @Override
    public List<String> getAttribute(String name) {
        return List.of();
    }

    @Override
    public void addRequiredAction(String action) {
        //might be called on user login to keycloak. action example: change password on first login
    }

    @Override
    public void addRequiredAction(RequiredAction action) {
        //might be called on user login to keycloak. action example: change password on first login
    }

    @Override
    public void removeRequiredAction(String action) {
        //might be called on user login to keycloak. action example: change password on first login
    }

    private Optional<Group> findGroupById(String groupId) {
        log.debugf("Looking for group with ID: %s", groupId);
        return entity.getGroups().stream().filter(it -> groupId.equals(it.getId()))
            .findAny();
    }
}
