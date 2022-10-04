package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserDaoTest {
    @Spy
    private UserDao userDao;

    @Test
    public void updateUserSetExistingUsernameTest() {
        String username = "u1";
        User u1 = User.builder().id(1L).username(username).build();
        User u2 = User.builder().id(2L).username(username).build();

        doReturn(u1).when(userDao).getUser(username);

        assertThrowsWebApplicationException(userDao::updateUser, u2);
    }

    @Test
    public void updateUserSetExistingEmailTest() {
        String email = "e1@kontur.io";
        User u1 = User.builder().id(1L).email(email).build();
        User u2 = User.builder().id(2L).email(email).build();

        doReturn(u2).when(userDao).getUser(null);
        doReturn(u1).when(userDao).getUserByEmail(email);

        assertThrowsWebApplicationException(userDao::updateUser, u2);
    }

    private void assertThrowsWebApplicationException(Consumer<User> consumer, User user) {
        boolean thrown = false;
        try {
            consumer.accept(user);
        } catch (Exception e) {
            assertEquals(WebApplicationException.class, e.getClass());
            assertEquals(HttpStatus.BAD_REQUEST, ((WebApplicationException) e).getStatus());
            thrown = true;
        }
        if (!thrown) {
            throw new RuntimeException("Exception was not thrown!");
        }
    }
}
