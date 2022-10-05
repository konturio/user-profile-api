package io.kontur.userprofile.dao;

import io.kontur.userprofile.AbstractIT;
import io.kontur.userprofile.model.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserDaoIT extends AbstractIT {
    @Autowired
    UserDao userDao;

    @Test
    public void createUserTest() {
        User user = createUser();
        givenUserIsAuthenticated(user);

        User existedUser = userDao.getUser(user.getUsername());
        assertEquals(user, existedUser);
    }

    @Test
    public void updateUserTest() {
        User user = createUser();
        givenUserIsAuthenticated(user);
        String username = "u1";
        user.setUsername(username);
        userDao.updateUser(user);

        User existedUser = userDao.getUser(username);
        assertEquals(user, existedUser);
    }
}
