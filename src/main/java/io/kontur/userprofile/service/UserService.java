package io.kontur.userprofile.service;

import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.UserDto;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserDao userDao;

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUser(String username) {
        return userDao.getUser(username);
    }

    public User createUser(User user) {
        if (userDao.getUser(user.getUsername()) != null) {
            throw new WebApplicationException("Use other username!", HttpStatus.BAD_REQUEST);
        }
        if (userDao.getUserByEmail(user.getEmail()) != null) {
            throw new WebApplicationException("Use other email!", HttpStatus.BAD_REQUEST);
        }
        userDao.createUser(user);

        return user;
    }

    public User updateUser(User user, UserDto userDto) {
        user.setFullName(userDto.getFullName());
        user.setLanguage(userDto.getLanguage());
        user.setUseMetricUnits(userDto.isUseMetricUnits());
        user.setSubscribedToKonturUpdates(userDto.isSubscribedToKonturUpdates());
        user.setBio(userDto.getBio());
        user.setOsmEditor(userDto.getOsmEditor());
        user.setDefaultFeed(userDto.getDefaultFeed());
        user.setTheme(userDto.getTheme());

        userDao.updateUser(user);

        return user;
    }
}
