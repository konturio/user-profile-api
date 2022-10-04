package io.kontur.userprofile.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.model.dto.UserDto;
import io.kontur.userprofile.model.dto.UserSummaryDto;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.UserController;
import io.kontur.userprofile.rest.exception.WebApplicationException;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import io.kontur.userprofile.service.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    private final List<User> users = someUsers();
    @Mock
    private AuthService authService;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void before() {
        when(userService.getAllUsers()).thenReturn(users);
        users.forEach(user -> when(userService.getUser(user.getUsername())).thenReturn(user));
    }

    @Test
    public void getAllUsersTest() {
        List<UserSummaryDto> result = userController.getAllUsers();
        thenListContainsAllUsersAndDataIsCorrect(result);
    }

    @Test
    public void getSomeUserTest() {
        UserDto result = userController.getUser(users.get(0).getUsername());
        thenDtoIsCorrect(users.get(0), result);
    }

    @Test
    public void userNotFoundTest() {
        assertThrowsWebApplicationException(() -> userController.getUser("somebody"));
    }

    @Test
    public void getCurrentUserTest() {
        when(authService.getCurrentUser()).thenReturn(Optional.of(users.get(0)));
        UserDto result = userController.getCurrentUser();
        thenDtoIsCorrect(users.get(0), result);
    }

    @Test
    public void getCurrentUserNotFoundTest() {
        when(authService.getCurrentUser()).thenReturn(Optional.empty());
        assertThrowsWebApplicationException(() -> userController.getCurrentUser());
    }

    @Test
    public void updateCurrentUserTest() {
        UserDto userDto = UserDto.fromEntity(users.get(0));
        when(authService.getCurrentUser()).thenReturn(Optional.of(users.get(0)));
        when(userService.updateUser(users.get(0), userDto)).thenReturn(users.get(0));
        UserDto result = userController.updateCurrentUser(userDto);
        thenDtoIsCorrect(users.get(0), result);
    }

    @Test
    public void updateCurrentUserNotFoundTest() {
        when(authService.getCurrentUser()).thenReturn(Optional.empty());
        assertThrowsWebApplicationException(() -> userController.updateCurrentUser(new UserDto()));
    }

    private void thenDtoIsCorrect(User source, UserDto dto) {
        assertFullDto(source, dto);
    }

    private void thenListContainsAllUsersAndDataIsCorrect(List<UserSummaryDto> dtos) {
        assertEquals(users.size(), dtos.size());
        users.forEach(user -> {
            UserSummaryDto dto = findDtoInResult(dtos, user.getUsername());
            assertSummaryDto(user, dto);
        });
    }

    private UserSummaryDto findDtoInResult(List<UserSummaryDto> result, String username) {
        return result.stream().filter(it -> username.equals(it.getUsername())).findAny().orElse(null);
    }

    private void assertSummaryDto(User user, UserSummaryDto dto) {
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    private void assertFullDto(User user, UserDto dto) {
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getLanguage(), dto.getLanguage());
        assertEquals(user.isUseMetricUnits(), dto.isUseMetricUnits());
        assertEquals(user.isSubscribedToKonturUpdates(), dto.isSubscribedToKonturUpdates());
    }

    private void assertThrowsWebApplicationException(Supplier<UserDto> supplier) {
        boolean thrown = false;
        try {
            supplier.get();
        } catch (Exception e) {
            assertEquals(WebApplicationException.class, e.getClass());
            assertEquals(HttpStatus.NOT_FOUND, ((WebApplicationException) e).getStatus());
            thrown = true;
        }
        if (!thrown) {
            throw new RuntimeException("Exception was not thrown!");
        }
    }

    private List<User> someUsers() {
        final String username1 = "u1";
        final String email1 = "e1@kontur.io";
        final String username2 = "u2";
        final String email2 = "e2@kontur.io";

        User u1 = User.builder()
                .username(username1)
                .email(email1)
                .firstName("first")
                .lastName("last")
                .language("English")
                .useMetricUnits(true)
                .subscribedToKonturUpdates(true)
                .build();
        User u2 = User.builder()
                .username(username2)
                .email(email2)
                .build();
        return Lists.newArrayList(u1, u2);
    }
}
