package io.kontur.userprofile.service;

import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.UserDto;
import io.kontur.userprofile.model.entity.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

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

    private User someUser() {
        return User.builder()
                .username("u1")
                .email("e1@kontur.io")
                .fullName("full")
                .language("English")
                .useMetricUnits(true)
                .subscribedToKonturUpdates(true)
                .build();
    }
}
