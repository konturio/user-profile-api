package io.kontur.keycloak.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.kontur.userprofile.model.entity.user.Role;
import org.junit.jupiter.api.Test;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleContainerModel;

public class RoleAdapterTest {

    private final ComponentModel component = mock(ComponentModel.class);

    @Test
    public void getIdNameDescriptionTest() {
        Role entity = new Role();
        entity.setId("some-keycloak-id");
        entity.setName("some-name");

        RoleAdapter adapter = RoleAdapter.fromEntity(entity, realmModel("realm_id"), component);

        assertEquals(entity.getId(), adapter.getId());
        assertEquals(entity.getName(), adapter.getName());
        assertEquals(entity.getName(), adapter.getDescription());
    }

    @Test
    public void isClientRoleFalseTest() {
        Role entity = new Role();
        entity.setId("some-keycloak-id");
        entity.setName("some-name");

        RoleAdapter adapter = RoleAdapter.fromEntity(entity, realmModel("realm_id"), component);

        assertFalse(adapter.isClientRole());
    }

    @Test
    public void isClientRoleTest() {
        Role entity = new Role();
        entity.setId("some-keycloak-id");
        entity.setName("some-name");
        entity.setClientId("some-client-id");

        RoleAdapter adapter =
            RoleAdapter.fromEntity(entity, clientModel("some-id", "some-clientid"), component);

        assertTrue(adapter.isClientRole());
    }

    @Test
    public void getClientContainerTest() {
        String clientId = "some-client-id-bla-bla";
        String clientClientId = "eventApi";
        Role entity = new Role();
        entity.setId("some-keycloak-id");
        entity.setName("some-name");
        entity.setClientId(clientId);
        entity.setClientClientId(clientClientId);

        RoleAdapter adapter =
            RoleAdapter.fromEntity(entity, clientModel(clientId, clientClientId), component);

        assertEquals(clientId, adapter.getContainerId());
        assertEquals(clientId, adapter.getContainer().getId());
    }

    @Test
    public void getRealmContainerIdTest() {
        String realmId = "realm_id";
        Role entity = new Role();
        entity.setId("some-keycloak-id");
        entity.setName("some-name");

        RoleAdapter adapter =
            RoleAdapter.fromEntity(entity, realmModel(realmId), component);

        assertEquals(realmId, adapter.getContainerId());
        assertEquals(realmId, adapter.getContainer().getId());
    }

    @Test
    public void equalsByIdTest() {
        String id = "some-keycloak-id";

        Role entity1 = new Role();
        entity1.setId(id);
        entity1.setName("name 1");

        Role entity2 = new Role();
        entity2.setId(id);
        entity2.setName("name 2");

        RoleAdapter adapter1 = RoleAdapter.fromEntity(entity1, realmModel("realm_id"), component);
        RoleAdapter adapter2 =
            RoleAdapter.fromEntity(entity2, clientModel("some", "thing"), component);
        assertEquals(adapter1, adapter2);
    }

    @Test
    public void hasRoleTest() {
        Role entity = new Role();
        entity.setId("some-keycloak-id");
        entity.setName("some-name");

        RoleAdapter adapter = RoleAdapter.fromEntity(entity, realmModel("realm_id"), component);
        RoleAdapter adapter2 = RoleAdapter.fromEntity(entity, realmModel("realm_id"), component);

        assertTrue(adapter.hasRole(adapter2));
    }

    private RoleContainerModel clientModel(String id, String clientId) {
        ClientModel clientModel = mock(ClientModel.class);
        when(clientModel.getId()).thenReturn(id);
        when(clientModel.getClientId()).thenReturn(clientId);
        return clientModel;
    }

    private RoleContainerModel realmModel(String id) {
        RealmModel realmModel = mock(RealmModel.class);
        when(realmModel.getId()).thenReturn(id);
        return realmModel;
    }
}
