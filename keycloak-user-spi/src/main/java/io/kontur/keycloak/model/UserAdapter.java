package io.kontur.keycloak.model;

import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.CREATED_TIMESTAMP_ATTRIBUTE;
import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.EMAIL_ATTRIBUTE;
import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.EMAIL_VERIFIED_ATTRIBUTE;
import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.ENABLED_ATTRIBUTE;
import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.FIRST_NAME_ATTRIBUTE;
import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.LAST_NAME_ATTRIBUTE;

import io.kontur.userprofile.model.entity.user.Group;
import io.kontur.userprofile.model.entity.user.Role;
import io.kontur.userprofile.model.entity.user.User;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

    public static final String LINKEDIN = "linkedin";
    public static final String PHONE = "phone";
    public static final String NEWSLETTER_CONSENT = "newsletterConsent";
    public static final String CALL_CONSENT = "callConsent";

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
        } else if (LINKEDIN.equals(name)) {
            setLinkedin(value);
        } else if (PHONE.equals(name)) {
            setPhone(value);
        } else if (NEWSLETTER_CONSENT.equals(name)) {
            setSubscribedToKonturUpdates(Boolean.valueOf(value));
        } else if (CALL_CONSENT.equals(name)) {
            setCallConsentGiven(Boolean.valueOf(value));
        } else if (CREATED_TIMESTAMP_ATTRIBUTE.equals(name)) {
            setCreatedAt(Instant.ofEpochMilli(value == null ? 0L : Long.valueOf(value)).atOffset(ZoneOffset.UTC));
        } else {
            UserStorageUtil
                .userFederatedStorage(session)
                .setSingleAttribute(realm, this.getId(), mapAttribute(name), value);
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
        } else if (LINKEDIN.equals(name)) {
            setLinkedin((values != null && values.size() > 0) ? values.get(0) : null);
        } else if (PHONE.equals(name)) {
            setPhone((values != null && values.size() > 0) ? values.get(0) : null);
        } else if (NEWSLETTER_CONSENT.equals(name)) {
            setSubscribedToKonturUpdates((values != null && values.size() > 0) ? Boolean.valueOf(values.get(0)) : null);
        } else if (CALL_CONSENT.equals(name)) {
            setCallConsentGiven((values != null && values.size() > 0) ? Boolean.valueOf(values.get(0)) : null);
        } else if (CREATED_TIMESTAMP_ATTRIBUTE.equals(name)) {
            setCreatedAt(
                values != null && !values.isEmpty() && values.get(0) != null
                    ? Instant.ofEpochMilli(Long.parseLong(values.get(0))).atOffset(ZoneOffset.UTC)
                    : Instant.ofEpochMilli(0L).atOffset(ZoneOffset.UTC)
            );
        } else {
            UserStorageUtil
                .userFederatedStorage(session)
                .setAttribute(realm, this.getId(), mapAttribute(name), values);
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

    public String getLinkedin() {
        return entity.getLinkedin();
    }

    public void setLinkedin(String linkedin) {
        entity.setLinkedin(linkedin);
    }

    public String getPhone() {
        return entity.getPhone();
    }

    public void setPhone(String phone) {
        entity.setPhone(phone);
    }

    public boolean isSubscribedToKonturUpdates() {
        return entity.isSubscribedToKonturUpdates();
    }

    public void setSubscribedToKonturUpdates(boolean subscribedToKonturUpdates) {
        entity.setSubscribedToKonturUpdates(subscribedToKonturUpdates);
    }

    public boolean isCallConsentGiven() {
        return entity.isCallConsentGiven();
    }

    public void setCallConsentGiven(boolean callConsentGiven) {
        entity.setCallConsentGiven(callConsentGiven);
    }

    public OffsetDateTime getCreatedAt() {
        return entity.getCreatedAt();
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        entity.setCreatedAt(createdAt);
    }

    @Override
    public boolean isEmailVerified() {
        String val = getFirstAttribute(EMAIL_VERIFIED_ATTRIBUTE);
        if (val == null) {
            return false;
        }
        return Boolean.valueOf(val);
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

        attributes.add(LINKEDIN, getLinkedin());
        attributes.add(PHONE, getPhone());
        attributes.add(NEWSLETTER_CONSENT, Boolean.toString(isSubscribedToKonturUpdates()));
        attributes.add(CALL_CONSENT, Boolean.toString(isCallConsentGiven()));

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
            return getCreatedAt() == null ? "0" : Long.toString(getCreatedAt().toInstant().toEpochMilli());
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
            if (LINKEDIN.equals(name)) {
                return getLinkedin();
            }
            if (PHONE.equals(name)) {
                return getPhone();
            }
            if (NEWSLETTER_CONSENT.equals(name)) {
                return Boolean.toString(isSubscribedToKonturUpdates());
            }
            if (CALL_CONSENT.equals(name)) {
                return Boolean.toString(isCallConsentGiven());
            }
        }
        return "";
    }

    @Override
    public Stream<String> getAttributeStream(String name) {
        if (UserModel.USERNAME.equals(name)) {
            return Stream.of(getUsername());
        }
        if (LINKEDIN.equals(name)) {
            return Stream.of(getLinkedin());
        }
        if (PHONE.equals(name)) {
            return Stream.of(getPhone());
        }
        if (NEWSLETTER_CONSENT.equals(name)) {
            return Stream.of(Boolean.toString(isSubscribedToKonturUpdates()));
        }
        if (CALL_CONSENT.equals(name)) {
            return Stream.of(Boolean.toString(isCallConsentGiven()));
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
