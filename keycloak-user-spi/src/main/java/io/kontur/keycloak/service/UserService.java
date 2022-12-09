package io.kontur.keycloak.service;

import io.kontur.userprofile.model.entity.user.User;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserService {
    long getCount();

    Stream<User> getAllUsers();

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);

    /**
     * Searches for users whose username, email, first name or last name contain provided string.
     */
    Stream<User> searchForUsers(String search);

    Stream<User> getGroupMembers(String name);

    Stream<User> getRoleMembers(String name);

    void createUser(User user);

    boolean removeUser(String username);
}
