package io.kontur.keycloak.model;

import io.kontur.userprofile.model.entity.Role;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.ToString;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.RoleContainerModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.ReadOnlyException;

@JBossLog
@ToString
public class RoleAdapter implements RoleModel {

    private final Role entity;
    private final RoleContainerModel roleContainerModel;
    private final ComponentModel component;

    private RoleAdapter(Role entity, RoleContainerModel roleContainerModel,
                        ComponentModel component) {
        this.entity = entity;
        this.roleContainerModel = roleContainerModel;
        this.component = component;
    }

    public static RoleAdapter fromEntity(Role entity, RoleContainerModel roleContainerModel,
                                         ComponentModel component) {
        if (entity == null) {
            throw new IllegalArgumentException("Role must not be null!");
        }
        if (roleContainerModel == null) {
            throw new IllegalArgumentException("Role Container must not be null!");
        }
        return new RoleAdapter(entity, roleContainerModel, component);
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    @Override
    public void setName(String name) {
        throw new ReadOnlyException("Role is read only for this update");
    }

    @Override
    public String getDescription() {
        return entity.getName();
    }

    @Override
    public void setDescription(String description) {
        throw new ReadOnlyException("Role is read only for this update");
    }

    @Override
    public String getId() {
        return entity.getId();
    }

    @Override
    public boolean isComposite() {
        return false; //composite roles not supported yet
    }

    @Override
    public void addCompositeRole(RoleModel role) {
        throw new ReadOnlyException("Role is read only for this update");
    }

    @Override
    public void removeCompositeRole(RoleModel role) {
        throw new ReadOnlyException("Role is read only for this update");
    }

    @Override
    public Stream<RoleModel> getCompositesStream() {
        return Stream.of(); //composite roles not supported yet
    }

    @Override
    public boolean isClientRole() {
        return entity.isClientRole();
    }

    @Override
    public String getContainerId() {
        return roleContainerModel.getId();
    }

    @Override
    public RoleContainerModel getContainer() {
        return roleContainerModel;
    }

    @Override
    public boolean hasRole(RoleModel role) {
        return getId().equals(role.getId()); //composite roles not supported yet
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        throw new ReadOnlyException("Role is read only for this update");
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        throw new ReadOnlyException("Role is read only for this update");
    }

    @Override
    public void removeAttribute(String name) {
        throw new ReadOnlyException("Role is read only for this update");
    }

    @Override
    public Stream<String> getAttributeStream(String name) {
        return Stream.of();
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        return Map.of();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RoleModel) {
            return getId().equals(((RoleModel) obj).getId());
        }
        return false;
    }
}
