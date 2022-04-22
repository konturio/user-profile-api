package io.kontur.userprofile.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.UserDto;
import io.kontur.userprofile.model.dto.UserSummaryDto;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.UserController;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

public class UserControllerTest {
    private final List<User> users = someUsers();
    @Mock
    UserDao userDao = mock(UserDao.class);
    UserController userController = new UserController(userDao);

    @BeforeEach
    public void before() {
        when(userDao.getAllUsers()).thenReturn(users);
        users.forEach(user -> {
            when(userDao.getUser(user.getUsername())).thenReturn(user);
        });
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
        boolean thrown = false;
        try {
            userController.getUser("somebody");
        } catch (Exception e) {
            assertEquals(WebApplicationException.class, e.getClass());
            assertEquals(HttpStatus.NOT_FOUND, ((WebApplicationException) e).getStatus());
            thrown = true;
        }
        if (!thrown) {
            throw new RuntimeException("Exception was not thrown!");
        }
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
        return result.stream().filter(it -> username.equals(it.getUsername())).findAny().get();
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
