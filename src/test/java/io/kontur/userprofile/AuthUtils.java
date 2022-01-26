package io.kontur.userprofile;

import static io.kontur.userprofile.config.WebSecurityConfiguration.ClaimParams.ROLE_PREFIX;
import static io.kontur.userprofile.config.WebSecurityConfiguration.ClaimParams.USERNAME_PREFIX;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static void mockAuthWithClaims(List<String> roles, String username) {
        Collection<GrantedAuthority> authorities = roles.stream()
            .map(it -> ROLE_PREFIX + it)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        if (username != null) {
            authorities.add(new SimpleGrantedAuthority(USERNAME_PREFIX + username));
        }
        AbstractAuthenticationToken authentication = mock(AbstractAuthenticationToken.class);
        when(authentication.getAuthorities()).thenReturn(authorities);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }
}
