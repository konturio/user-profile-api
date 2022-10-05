package io.kontur.userprofile.service;

import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.UserDto;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    private final User someUser = someUser();
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService userService;

    @Test
    public void updateUserTest() {
        UserDto userDto = UserDto.fromEntity(someUser);
        doNothing().when(userDao).updateUser(someUser);
        User user = userService.updateUser(someUser, userDto);
        assertEquals(someUser, user);
    }

    @Test
    public void updateUserSetExistingUsernameTest() {
        String username = "u1";
        User u1 = User.builder().id(1L).username(username).build();
        User u2 = User.builder().id(2L).username(username).build();

        doReturn(u1).when(userDao).getUser(username);

        assertThrowsWebApplicationException(userService::updateUser, u2, UserDto.fromEntity(u2));
    }

    @Test
    public void updateUserSetExistingEmailTest() {
        String email = "e1@kontur.io";
        User u1 = User.builder().id(1L).email(email).build();
        User u2 = User.builder().id(2L).email(email).build();

        doReturn(u2).when(userDao).getUser(null);
        doReturn(u1).when(userDao).getUserByEmail(email);

        assertThrowsWebApplicationException(userService::updateUser, u2, UserDto.fromEntity(u2));
    }

    private void assertThrowsWebApplicationException(BiConsumer<User, UserDto> consumer, User user, UserDto userDto) {
        boolean thrown = false;
        try {
            consumer.accept(user, userDto);
        } catch (Exception e) {
            assertEquals(WebApplicationException.class, e.getClass());
            assertEquals(HttpStatus.BAD_REQUEST, ((WebApplicationException) e).getStatus());
            thrown = true;
        }
        if (!thrown) {
            throw new RuntimeException("Exception was not thrown!");
        }
    }

    private User someUser() {
        return User.builder()
                .username("u1")
                .email("e1@kontur.io")
                .firstName("first")
                .lastName("last")
                .language("English")
                .useMetricUnits(true)
                .subscribedToKonturUpdates(true)
                .build();
    }
}
