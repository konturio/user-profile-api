package io.kontur.userprofile.auth;

import static io.kontur.userprofile.config.WebSecurityConfiguration.ClaimParams.USERNAME_PREFIX;

import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.entity.user.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.kontur.userprofile.rest.exception.WebApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDao userDao;

    private static List<String> getTokenClaims() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return List.of();
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null) {
            return List.of();
        }

        return authorities.stream().map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    }

    public Optional<User> getCurrentUser() {
        return getCurrentUsername()
            .map(userDao::getUser);
    }

    public Optional<String> getCurrentUsername() {
        List<String> tokenClaims = getTokenClaims();
        return getCurrentUsername(tokenClaims);
    }

    public User getCurrentUserOrElseThrow() {
        String username = getCurrentUsername()
                .orElseThrow(() -> new WebApplicationException("User not authenticated", HttpStatus.UNAUTHORIZED));
        return Optional.ofNullable(userDao.getUser(username))
                .orElseThrow(() -> new WebApplicationException("User not found by username " + username, HttpStatus.NOT_FOUND));
    }

    private Optional<String> getCurrentUsername(List<String> tokenClaims) {
        return tokenClaims.stream()
            .filter(Objects::nonNull)
            .filter(it -> it.startsWith(USERNAME_PREFIX))
            .findAny()
            .map(this::removePrefixFromUsernameClaim);
    }

    private String removePrefixFromUsernameClaim(String claim) {
        return claim.substring(USERNAME_PREFIX.length());
    }
}
