package io.kontur.keycloak.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.kontur.keycloak.model.UserAdapter;
import io.kontur.keycloak.service.UserService;
import io.kontur.userprofile.model.entity.User;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.mockito.ArgumentMatcher;

public class DatabaseUserStorageProviderTest {
    private final static String email = "email@me.com";
    DatabaseUserStorageProvider provider;
    private UserService userService = mock(UserService.class);
    private KeycloakSession session = mock(KeycloakSession.class);
    private ComponentModel component = mock(ComponentModel.class);
    private RealmModel realm = mock(RealmModel.class);

    @BeforeEach
    public void before() {
        provider = new DatabaseUserStorageProvider();
        provider.setUserService(userService);
        provider.setSession(session);
        provider.setComponent(component);
    }

    @Test
    public void addUserTest() {
        provider.addUser(realm, email);

        verify(userService, times(1)).createUser(
            argThat(usernameAndEmailAreEqualTo(email)));
    }

    @Test
    public void removeUserTest() {
        provider.removeUser(realm, userModel(email));

        verify(userService, times(1)).removeUser(email);
    }

    @Test
    public void getUsersTest() {
        User user1 = user("user1");
        User user2 = user("user2");
        when(userService.getAllUsers()).thenReturn(Stream.of(user1, user2));

        List<UserModel> result = provider.getUsers(realm);

        assertEquals(2, result.size());
        assertEquals(user1.getUsername(), result.get(0).getUsername());
        assertEquals(user2.getUsername(), result.get(1).getUsername());
    }

    @Test
    public void getUsersLimitedTest() {
        when(userService.getAllUsers()).thenReturn(tenUsers());

        List<UserModel> result = provider.getUsers(realm, 2, 3);

        assertEquals(3, result.size());
        assertEquals("user3", result.get(0).getUsername());
        assertEquals("user4", result.get(1).getUsername());
        assertEquals("user5", result.get(2).getUsername());
    }

    private User user(String username) {
        return User.builder().username(username).build();
    }

    private UserModel userModel(String username) {
        return UserAdapter.fromEntity(user(username), session, realm, component);
    }

    private Stream<User> tenUsers() {
        return Stream.of(user("user1"),
            user("user2"),
            user("user3"),
            user("user4"),
            user("user5"),
            user("user6"),
            user("user7"),
            user("user8"),
            user("user9"),
            user("user10"));
    }

    private ArgumentMatcher<User> usernameAndEmailAreEqualTo(String value) {
        return argument -> Objects.equals(argument.getUsername(), value)
            && Objects.equals(argument.getEmail(), value);
    }
}
