package io.kontur.userprofile;

import static org.mockito.Mockito.when;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.model.entity.user.User;

import java.util.Optional;
import java.util.UUID;

import io.kontur.userprofile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AbstractIT {
    @MockBean
    AuthService authService;
    @Autowired
    UserService userService;

    protected void givenUserIsNotAuthenticated() {
        when(authService.getCurrentUsername()).thenReturn(Optional.empty());
        when(authService.getCurrentUser()).thenReturn(Optional.empty());
    }

    protected void givenUserIsAuthenticated(User user) {
        when(authService.getCurrentUsername()).thenReturn(Optional.of(user.getUsername()));
        when(authService.getCurrentUser()).thenReturn(Optional.of(user));
    }

    protected void givenUserWithBetaRoleIsAuthenticated(User user) {
        when(authService.currentUserHasBetaFeaturesRole()).thenReturn(true);
        when(authService.getCurrentUsername()).thenReturn(Optional.of(user.getUsername()));
        when(authService.getCurrentUser()).thenReturn(Optional.of(user));
    }

    protected void givenUserWithoutBetaRoleIsAuthenticated(User user) {
        when(authService.currentUserHasBetaFeaturesRole()).thenReturn(false);
        when(authService.getCurrentUsername()).thenReturn(Optional.of(user.getUsername()));
        when(authService.getCurrentUser()).thenReturn(Optional.of(user));
    }

    protected User createUser() {
        User user = User.builder()
                .username(UUID.randomUUID().toString())
                .email(UUID.randomUUID() + "@test.com")
                .build();
        user = userService.createUser(user);
        return user;
    }
}
