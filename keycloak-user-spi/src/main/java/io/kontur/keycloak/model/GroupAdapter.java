package io.kontur.keycloak.model;

import io.kontur.userprofile.model.entity.user.Group;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.ReadOnlyException;

@JBossLog
public class GroupAdapter implements GroupModel {

    private final Group entity;

    private GroupAdapter(Group entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Group must not be null!");
        }
        this.entity = entity;
    }

    public static GroupAdapter fromEntity(Group entity) {
        return new GroupAdapter(entity);
    }

    @Override
    public Set<RoleModel> getRealmRoleMappings() {
        return Set.of(); //not supported for groups
    }

    @Override
    public Set<RoleModel> getClientRoleMappings(ClientModel app) {
        return Set.of(); //not supported for groups
    }

    @Override
    public boolean hasRole(RoleModel role) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void grantRole(RoleModel role) {
        throw new ReadOnlyException("Group is read only for this update");
    }

    @Override
    public Set<RoleModel> getRoleMappings() {
        return Set.of(); //not supported for groups
    }

    @Override
    public void deleteRoleMapping(RoleModel role) {
        throw new ReadOnlyException("Group is read only for this update");
    }

    @Override
    public String getId() {
        return entity.getId();
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    @Override
    public void setName(String name) {
        throw new ReadOnlyException("Group is read only for this update");
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        throw new ReadOnlyException("Group is read only for this update");
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        throw new ReadOnlyException("Group is read only for this update");
    }

    @Override
    public void removeAttribute(String name) {
        throw new ReadOnlyException("Group is read only for this update");
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
    public Map<String, List<String>> getAttributes() {
        return Map.of();
    }

    @Override
    public GroupModel getParent() {
        return null; //not supported yet
    }

    @Override
    public void setParent(GroupModel group) {
        throw new ReadOnlyException("Group is read only for this update");
    }

    @Override
    public String getParentId() {
        GroupModel parent = getParent();
        if (parent != null) {
            return getParent().getId();
        }
        return null;
    }

    @Override
    public Set<GroupModel> getSubGroups() {
        return Set.of(); //not supported
    }

    @Override
    public void addChild(GroupModel subGroup) {
        throw new ReadOnlyException("Group is read only for this update");
    }

    @Override
    public void removeChild(GroupModel subGroup) {
        throw new ReadOnlyException("Group is read only for this update");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GroupModel) {
            return getId().equals(((GroupModel) obj).getId());
        }
        return false;
    }
}
