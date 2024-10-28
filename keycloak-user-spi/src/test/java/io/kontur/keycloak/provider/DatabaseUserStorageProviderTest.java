package io.kontur.keycloak.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import io.kontur.keycloak.model.UserAdapter;
import io.kontur.keycloak.service.UserService;
import io.kontur.userprofile.model.entity.user.User;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.component.ComponentModel;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.UserStorageUtil;
import org.keycloak.storage.federated.UserFederatedStorageProvider;
import org.mockito.ArgumentMatcher;

public class DatabaseUserStorageProviderTest {
    private static final String email = "email@me.com";
    DatabaseUserStorageProvider provider;
    private UserService userService = mock(UserService.class);
    private KeycloakSession session = mock(KeycloakSession.class);
    private ComponentModel component = mock(ComponentModel.class);
    private RealmModel realm = mock(RealmModel.class);

    private KeycloakContext context = mock(KeycloakContext.class);
    private HttpRequest httpRequest = mock(HttpRequest.class);
    private UserFederatedStorageProvider federatedStorageProvider = mock(UserFederatedStorageProvider.class);


    @BeforeEach
    public void before() {
        provider = new DatabaseUserStorageProvider();
        provider.setUserService(userService);
        provider.setSession(session);
        provider.setComponent(component);
    }

    private void mockHttpRequestParameters() {
        MultivaluedMap<String, String> formParameters = new MultivaluedHashMap<>();
        formParameters.putSingle("phone", "1234567890");
        formParameters.putSingle("linkedin", "linkedin-profile");
        when(httpRequest.getDecodedFormParameters()).thenReturn(formParameters);
    }

    @Test
    public void addUserTest() {
        // Setup mock for Keycloak session context and HTTP request
        when(session.getContext()).thenReturn(context);
        when(context.getHttpRequest()).thenReturn(httpRequest);
        when(UserStorageUtil.userFederatedStorage(session)).thenReturn(federatedStorageProvider);
        doNothing().when(federatedStorageProvider)
                .setSingleAttribute(any(RealmModel.class), anyString(), anyString(), anyString());
        mockHttpRequestParameters();

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

        List<UserModel> result = provider.getUsersStream(realm).collect(Collectors.toList());

        assertEquals(2, result.size());
        assertEquals(user1.getUsername(), result.get(0).getUsername());
        assertEquals(user2.getUsername(), result.get(1).getUsername());
    }

    @Test
    public void countTest() {
        when(userService.getCount()).thenReturn(2L);

        int result = provider.getUsersCount(realm);

        assertEquals(2, result);
    }

    @Test
    public void getUsersLimitedTest() {
        when(userService.getAllUsers()).thenReturn(tenUsers());

        List<UserModel> result = provider.getUsersStream(realm, 2, 3).collect(Collectors.toList());

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
