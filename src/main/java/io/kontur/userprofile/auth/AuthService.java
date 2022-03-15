package io.kontur.userprofile.auth;

import static io.kontur.userprofile.config.WebSecurityConfiguration.ClaimParams.ROLE_PREFIX;
import static io.kontur.userprofile.config.WebSecurityConfiguration.ClaimParams.USERNAME_PREFIX;
import static io.kontur.userprofile.model.entity.Role.Names.BETA_FEATURES;

import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.entity.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

    private Optional<String> getCurrentUsername(List<String> tokenClaims) {
        return tokenClaims.stream()
            .filter(Objects::nonNull)
            .filter(it -> it.startsWith(USERNAME_PREFIX))
            .findAny()
            .map(this::removePrefixFromUsernameClaim);
    }

    public boolean currentUserHasBetaFeaturesRole() {
        List<String> tokenClaims = getTokenClaims();
        return containsBetaFeatureRole(tokenClaims);
    }

    private boolean containsBetaFeatureRole(List<String> tokenClaims) {
        return tokenClaims.stream()
            .anyMatch((ROLE_PREFIX + BETA_FEATURES)::equals);
    }

    private String removePrefixFromUsernameClaim(String claim) {
        return claim.substring(USERNAME_PREFIX.length());
    }
}
