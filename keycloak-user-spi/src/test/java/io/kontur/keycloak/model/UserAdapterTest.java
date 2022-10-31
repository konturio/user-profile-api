package io.kontur.keycloak.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.EMAIL_VERIFIED_ATTRIBUTE;
import static org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage.ENABLED_ATTRIBUTE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import io.kontur.userprofile.model.entity.user.Group;
import io.kontur.userprofile.model.entity.user.Role;
import io.kontur.userprofile.model.entity.user.User;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.federated.UserFederatedStorageProvider;

public class UserAdapterTest {
    private final ComponentModel component = mock(ComponentModel.class);
    private final RealmModel realm = mock(RealmModel.class);
    private final ClientModel client1 = mock(ClientModel.class);
    private final ClientModel client2 = mock(ClientModel.class);
    private final String realmId = "realm_id";
    private final KeycloakSession session = mock(KeycloakSession.class);
    private final UserFederatedStorageProvider storage = mock(UserFederatedStorageProvider.class);

    private final String client1Id = "id-like-uuid-1";
    private final String client1ClientId = "clientId1SetByUser";
    private final String client2Id = "id-like-uuid-2";
    private final String client2ClientId = "clientId2SetByUser";

    private final Role realmRole1 = realmRoleEntity(id(1), name(1));
    private final Role realmRole2 = realmRoleEntity(id(2), name(2));
    private final Role client1Role1 = clientRoleEntity(id(3), name(3), client1Id, client1ClientId);
    private final Role client1Role2 = clientRoleEntity(id(4), name(4), client1Id, client1ClientId);
    private final Role client2Role1 = clientRoleEntity(id(5), name(5), client2Id, client2ClientId);
    private final Role client2Role2 = clientRoleEntity(id(6), name(6), client2Id, client2ClientId);

    private final Group group1 = new Group("some-id", "some-name");
    private final Group group2 = new Group("some-id-2", "some-name-2");

    @BeforeEach
    public void before() {
        when(client1.getId()).thenReturn(client1Id);
        when(client2.getId()).thenReturn(client2Id);
        when(client1.getClientId()).thenReturn(client1ClientId);
        when(client2.getClientId()).thenReturn(client2ClientId);
        when(realm.getClientById(client1Id)).thenReturn(client1);
        when(realm.getClientById(client2Id)).thenReturn(client2);

        when(realm.getId()).thenReturn(realmId);
        when(realm.getDefaultRole()).thenReturn(null);
        when(realm.getRoleById(any())).thenReturn(mock(RoleModel.class));
        when(realm.getGroupById(group1.getId())).thenReturn(mock(GroupModel.class));
        when(realm.getGroupById(group2.getId())).thenReturn(mock(GroupModel.class));


        when(session.userFederatedStorage()).thenReturn(storage);

        MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();
        attributes.add(ENABLED_ATTRIBUTE, "true");
        attributes.add(EMAIL_VERIFIED_ATTRIBUTE, "true");
        when(storage.getAttributes(eq(realm), any())).thenReturn(attributes);
    }


    @Test
    public void getRoleMappingsTest() {
        User user = givenUserContainsReamMappingAndTwoClientsMappings();

        UserAdapter userAdapter = createAdapterForUser(user);
        Set<RoleModel> roleAdapters = userAdapter.getRoleMappings();

        thenSetSizeIs(roleAdapters, 6);
        thenSetContainsTwoRealmRoles(roleAdapters);
        thenSetContainsTwoClient1Roles(roleAdapters);
        thenSetContainsTwoClient2Roles(roleAdapters);
    }

    @Test
    public void getRealmRoleMappingsTest() {
        User user = givenUserContainsReamMappingAndTwoClientsMappings();

        UserAdapter userAdapter = createAdapterForUser(user);
        Set<RoleModel> roleAdapters = userAdapter.getRealmRoleMappings();

        thenSetSizeIs(roleAdapters, 2);
        thenSetContainsTwoRealmRoles(roleAdapters);
    }

    @Test
    public void getClientRoleMappingsTest() {
        User user = givenUserContainsReamMappingAndTwoClientsMappings();

        UserAdapter userAdapter = createAdapterForUser(user);
        Set<RoleModel> roleAdapters = userAdapter.getClientRoleMappings(client1);

        thenSetSizeIs(roleAdapters, 2);
        thenSetContainsTwoClient1Roles(roleAdapters);
    }

    @Test
    public void getClient2RoleMappingsTest() {
        User user = givenUserContainsReamMappingAndTwoClientsMappings();

        UserAdapter userAdapter = createAdapterForUser(user);
        Set<RoleModel> roleAdapters = userAdapter.getClientRoleMappings(client2);

        thenSetSizeIs(roleAdapters, 2);
        thenSetContainsTwoClient2Roles(roleAdapters);
    }


    @Test
    public void hasRoleTest() {
        User user = givenUserContainsReamMappingAndTwoClientsMappings();
        UserAdapter adapter = createAdapterForUser(user);

        assertTrue(adapter.hasRole(createAdapterForRole(realmRole1)));
        assertTrue(adapter.hasRole(createAdapterForRole(realmRole2)));
        assertTrue(adapter.hasRole(createAdapterForRole(client1Role1)));
        assertTrue(adapter.hasRole(createAdapterForRole(client1Role2)));
        assertTrue(adapter.hasRole(createAdapterForRole(client2Role1)));
        assertTrue(adapter.hasRole(createAdapterForRole(client2Role2)));
    }

    @Test
    public void hasRoleNegTest() {
        User user = givenUserContainsReamMappingAndTwoClientsMappings();
        UserAdapter adapter = createAdapterForUser(user);

        Role someRealmRole = realmRoleEntity("some-id-1", "other");
        assertFalse(adapter.hasRole(createAdapterForRole(someRealmRole)));

        String someClientId = "someClientId";
        Role someClientRole = clientRoleEntity("some-id-2", "other2", someClientId, "something");
        ClientModel someClient = mock(ClientModel.class);
        when(realm.getClientById(someClientId)).thenReturn(someClient);
        assertFalse(adapter.hasRole(createAdapterForRole(someClientRole)));
    }

    @Test
    public void grantRoleTest() {
        User user = givenUserHasNoRolesOrGroups();
        UserAdapter userAdapter = createAdapterForUser(user);


        //grant client1 role
        userAdapter.grantRole(createAdapterForRole(client1Role1));
        //model mapping sizes
        thenModelRoleMappingSizesAre(userAdapter, 1, 1, 0, 0);
        //check entity is added
        thenSetSizeIs(user.getRoles(), 1);
        thenSetContainsRole(user.getRoles(), client1Role1);


        //grant client2 role
        userAdapter.grantRole(createAdapterForRole(client2Role1));
        //model mapping sizes
        thenModelRoleMappingSizesAre(userAdapter, 2, 1, 1, 0);
        //check entity is added
        thenSetSizeIs(user.getRoles(), 2);
        thenSetContainsRole(user.getRoles(), client1Role1);
        thenSetContainsRole(user.getRoles(), client2Role1);


        //grant realm role
        userAdapter.grantRole(createAdapterForRole(realmRole1));
        //model mapping sizes
        thenModelRoleMappingSizesAre(userAdapter, 3, 1, 1, 1);
        //check entity is added
        thenSetSizeIs(user.getRoles(), 3);
        thenSetContainsRole(user.getRoles(), client1Role1);
        thenSetContainsRole(user.getRoles(), client2Role1);
        thenSetContainsRole(user.getRoles(), realmRole1);
    }

    @Test
    public void deleteRoleMappingTest() {
        User user = givenUserContainsReamMappingAndTwoClientsMappings();
        UserAdapter userAdapter = createAdapterForUser(user);
        //initial state
        thenModelRoleMappingSizesAre(userAdapter, 6, 2, 2, 2);
        thenSetContainsRole(user.getRoles(), realmRole1);
        thenSetContainsRole(user.getRoles(), realmRole2);
        thenSetContainsRole(user.getRoles(), client1Role1);
        thenSetContainsRole(user.getRoles(), client1Role2);
        thenSetContainsRole(user.getRoles(), client2Role1);
        thenSetContainsRole(user.getRoles(), client2Role2);


        userAdapter.deleteRoleMapping(createAdapterForRole(realmRole1));
        thenModelRoleMappingSizesAre(userAdapter, 5, 2, 2, 1);
        thenSetDoesNotContainRole(user.getRoles(), realmRole1);

        userAdapter.deleteRoleMapping(createAdapterForRole(client1Role1));
        thenModelRoleMappingSizesAre(userAdapter, 4, 1, 2, 1);
        thenSetDoesNotContainRole(user.getRoles(), client1Role1);

        userAdapter.deleteRoleMapping(createAdapterForRole(client2Role1));
        thenModelRoleMappingSizesAre(userAdapter, 3, 1, 1, 1);
        thenSetDoesNotContainRole(user.getRoles(), client2Role1);

        userAdapter.deleteRoleMapping(createAdapterForRole(realmRole2));
        thenModelRoleMappingSizesAre(userAdapter, 2, 1, 1, 0);
        thenSetDoesNotContainRole(user.getRoles(), realmRole2);

        userAdapter.deleteRoleMapping(createAdapterForRole(client1Role2));
        thenModelRoleMappingSizesAre(userAdapter, 1, 0, 1, 0);
        thenSetDoesNotContainRole(user.getRoles(), client1Role2);

        userAdapter.deleteRoleMapping(createAdapterForRole(client2Role2));
        thenModelRoleMappingSizesAre(userAdapter, 0, 0, 0, 0);
        thenSetDoesNotContainRole(user.getRoles(), client2Role2);
    }

    @Test
    public void basicAttributesTest() {
        User user = User.builder()
            .id(123L)
            .username("some_username")
            .fullName("full name")
            .email("email@test.com")
            .build();

        UserAdapter adapter = createAdapterForUser(user);
        assertAdapter(user, adapter);
    }

    @Test
    public void setFirstNameTest() {
        User user = givenUserHasNoRolesOrGroups();
        user.setFullName("name1");
        UserAdapter userAdapter = createAdapterForUser(user);

        String newName = "new name";
        userAdapter.setFirstName(newName);

        assertEquals(newName, user.getFullName());
    }

    @Test
    public void setLastNameTest() {
        User user = givenUserHasNoRolesOrGroups();
        user.setFullName("name1");
        UserAdapter userAdapter = createAdapterForUser(user);

        String newName = "new name";
        userAdapter.setLastName(newName);

        assertEquals("name1", user.getFullName());
    }

    @Test
    public void setEmailTest() {
        User user = givenUserHasNoRolesOrGroups();
        user.setEmail("name1@qwe.com");
        UserAdapter userAdapter = createAdapterForUser(user);

        String newName = "new@email.com";
        userAdapter.setEmail(newName);

        assertEquals(newName, user.getEmail());
    }

    @Test
    public void getGroupsTest() {
        User user = givenUserIsMemberOfTwoGroups();
        UserAdapter adapter = createAdapterForUser(user);

        thenSetSizeIs(adapter.getGroups(), 2);
        thenGroupModelSetContainsGroup(adapter.getGroups(), group1);
        thenGroupModelSetContainsGroup(adapter.getGroups(), group2);
    }

    @Test
    public void getGroupsNegTest() {
        User user = givenUserHasNoRolesOrGroups();
        UserAdapter adapter = createAdapterForUser(user);

        thenSetSizeIs(adapter.getGroups(), 0);
    }

    @Test
    public void joinGroupTest() {
        User user = givenUserHasNoRolesOrGroups();
        UserAdapter userAdapter = createAdapterForUser(user);

        GroupAdapter groupAdapter1 = createAdapterForGroup(group1);
        userAdapter.joinGroup(groupAdapter1);
        assertTrue(userAdapter.isMemberOf(groupAdapter1));
        assertTrue(user.getGroups().contains(group1));
        thenSetSizeIs(user.getGroups(), 1);

        GroupAdapter groupAdapter2 = createAdapterForGroup(group2);
        userAdapter.joinGroup(groupAdapter2);
        assertTrue(userAdapter.isMemberOf(groupAdapter2));
        assertTrue(user.getGroups().contains(group2));
        thenSetSizeIs(user.getGroups(), 2);
    }

    @Test
    public void leaveGroupTest() {
        User user = givenUserIsMemberOfTwoGroups();
        UserAdapter adapter = createAdapterForUser(user);

        GroupAdapter groupAdapter1 = createAdapterForGroup(group1);
        adapter.leaveGroup(groupAdapter1);
        assertFalse(adapter.isMemberOf(groupAdapter1));
        thenSetSizeIs(user.getGroups(), 1);
        assertFalse(user.getGroups().contains(group1));

        GroupAdapter groupAdapter2 = createAdapterForGroup(group2);
        adapter.leaveGroup(groupAdapter2);
        assertFalse(adapter.isMemberOf(groupAdapter2));
        thenSetSizeIs(user.getGroups(), 0);
        assertFalse(user.getGroups().contains(group2));
    }

    @Test
    public void isMemberOfNegTest() {
        User user = givenUserHasNoRolesOrGroups();
        UserAdapter adapter = createAdapterForUser(user);

        assertFalse(adapter.isMemberOf(createAdapterForGroup(group1)));
        assertFalse(adapter.isMemberOf(createAdapterForGroup(group2)));
    }

    @Test
    public void isMemberOfTest() {
        User user = givenUserIsMemberOfTwoGroups();
        UserAdapter adapter = createAdapterForUser(user);

        assertTrue(adapter.isMemberOf(createAdapterForGroup(group1)));
        assertTrue(adapter.isMemberOf(createAdapterForGroup(group2)));
    }

    private User givenUserHasNoRolesOrGroups() {
        return User.builder()
            .roles(new HashSet<>())
            .groups(new HashSet<>())
            .build();
    }

    private void thenModelRoleMappingSizesAre(UserAdapter userAdapter, int roleMappings,
                                              int client1RoleMappings, int client2RoleMappings,
                                              int realmRoleMappings) {
        thenSetSizeIs(userAdapter.getRoleMappings(), roleMappings);
        thenSetSizeIs(userAdapter.getClientRoleMappings(client1), client1RoleMappings);
        thenSetSizeIs(userAdapter.getClientRoleMappings(client2), client2RoleMappings);
        thenSetSizeIs(userAdapter.getRealmRoleMappings(), realmRoleMappings);
    }

    private UserAdapter createAdapterForUser(User user) {
        return UserAdapter.fromEntity(user, session, realm, component);
    }

    private RoleAdapter createAdapterForRole(Role role) {
        if (role.isClientRole()) {
            return RoleAdapter.fromEntity(role, realm.getClientById(role.getClientId()), null);
        }
        return RoleAdapter.fromEntity(role, realm, null);
    }

    private GroupAdapter createAdapterForGroup(Group group) {
        return GroupAdapter.fromEntity(group, null);
    }


    private void assertAdapter(User user, UserAdapter adapter) {
        assertEquals(user.getUsername(), adapter.getUsername());
        assertEquals(user.getFullName(), adapter.getFirstName());
        assertEquals("", adapter.getLastName());
        assertEquals(user.getEmail(), adapter.getEmail());
        assertTrue(adapter.isEnabled());
        assertTrue(adapter.isEmailVerified());
    }

    private Optional<RoleModel> findRoleAdapterInSet(Set<RoleModel> input, String roleId) {
        return input.stream().filter(it -> roleId.equals(it.getId())).findAny();
    }

    private Optional<GroupModel> findGroupAdapterInSet(Set<GroupModel> input, String groupId) {
        return input.stream().filter(it -> groupId.equals(it.getId())).findAny();
    }

    private Optional<Role> findRoleInSet(Set<Role> input, String roleId) {
        return input.stream().filter(it -> roleId.equals(it.getId())).findAny();
    }

    private User givenUserContainsReamMappingAndTwoClientsMappings() {
        return User.builder()
            .roles(new HashSet<>(Set.of(
                realmRole1, realmRole2,
                client1Role1, client1Role2,
                client2Role1, client2Role2
            )))
            .build();
    }

    private User givenUserIsMemberOfTwoGroups() {
        return User.builder()
            .groups(new HashSet<>(Set.of(
                group1, group2
            )))
            .build();
    }

    private void thenSetSizeIs(Set<?> set, int number) {
        assertEquals(number, set.size());
    }

    private void thenGroupModelSetContainsGroup(Set<GroupModel> set, Group group) {
        Optional<GroupModel> groupModel = findGroupAdapterInSet(set, group.getId());
        assertTrue(groupModel.isPresent());
        assertGroup(group, groupModel.get());
    }

    private void thenSetContainsRole(Set<Role> set, Role role) {
        Optional<Role> roleFromSet = findRoleInSet(set, role.getId());
        assertTrue(roleFromSet.isPresent());
        assertEquals(role, roleFromSet.get());
    }

    private void thenSetDoesNotContainRole(Set<Role> set, Role role) {
        assertFalse(set.contains(role));
    }

    private void thenSetContainsTwoRealmRoles(Set<RoleModel> roleAdapters) {
        thenRoleModelSetContainsRole(roleAdapters, realmRole1);
        thenRoleModelSetContainsRole(roleAdapters, realmRole2);
    }

    private void thenSetContainsTwoClient1Roles(Set<RoleModel> roleAdapters) {
        thenRoleModelSetContainsRole(roleAdapters, client1Role1);
        thenRoleModelSetContainsRole(roleAdapters, client1Role2);
    }

    private void thenSetContainsTwoClient2Roles(Set<RoleModel> roleAdapters) {
        thenRoleModelSetContainsRole(roleAdapters, client2Role1);
        thenRoleModelSetContainsRole(roleAdapters, client2Role2);
    }

    private void thenRoleModelSetContainsRole(Set<RoleModel> roleAdapters, Role clientRole) {
        Optional<RoleModel> adapter =
            findRoleAdapterInSet(roleAdapters, clientRole.getId());
        assertTrue(adapter.isPresent());
        assertRole(clientRole, adapter.get());
    }

    private void assertGroup(Group sourceEntity, GroupModel groupAdapter) {
        assertEquals(sourceEntity.getId(), groupAdapter.getId());
        assertEquals(sourceEntity.getName(), groupAdapter.getName());
    }

    private void assertRole(Role sourceEntity, RoleModel roleAdapter) {
        assertEquals(sourceEntity.getId(), roleAdapter.getId());
        assertEquals(sourceEntity.getName(), roleAdapter.getName());
        assertEquals(sourceEntity.isClientRole(), roleAdapter.isClientRole());

        if (sourceEntity.isClientRole()) {
            assertEquals(sourceEntity.getClientId(), roleAdapter.getContainerId());
            assertEquals(sourceEntity.getClientId(), roleAdapter.getContainer().getId());
        } else {
            assertEquals(realm.getId(), roleAdapter.getContainerId());
            assertEquals(realm.getId(), roleAdapter.getContainer().getId());
        }
    }

    private String id(int i) {
        return "id-" + i;
    }

    private String name(int i) {
        return "name-" + i;
    }

    private Role realmRoleEntity(String id, String name) {
        return roleEntity(id, name, null, null);
    }

    private Role clientRoleEntity(String id, String name, String clientId, String clientClientId) {
        if (clientId == null || clientClientId == null) {
            throw new RuntimeException(
                "clientId and clientClientId are always not null for client roles");
        }
        return roleEntity(id, name, clientId, clientClientId);
    }

    private Role roleEntity(String id, String name, String clientId, String clientClientId) {
        return new Role(id, name, clientId, clientClientId);
    }

}
