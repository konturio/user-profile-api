package io.kontur.userprofile.rest;

import static io.kontur.userprofile.config.WebSecurityConfiguration.ClaimParams.USERNAME_PREFIX;
import static io.kontur.userprofile.model.entity.user.Role.Names.KONTUR_ADMIN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.UserDto;
import io.kontur.userprofile.model.dto.UserSummaryDto;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController { //todo this api is not used by anyone
    private final UserDao userDao;

    private final AuthService authService;

    @Operation(summary = "Get List of Users")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
        array = @ArraySchema(
            schema = @Schema(implementation = UserSummaryDto.class))))
    @PreAuthorize("hasRole('" + KONTUR_ADMIN + "')")
    @GetMapping("/users")
    public List<UserSummaryDto> getAllUsers() {
        return userDao.getAllUsers().stream().map(UserSummaryDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Get User info by username")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @PreAuthorize("hasRole('" + KONTUR_ADMIN + "') || hasAuthority('" + USERNAME_PREFIX + "' "
        + "+ #username)")
    @GetMapping("/users/{username}")
    public UserDto getUser(@PathVariable @Parameter(name = "username") String username) {
        User user = userDao.getUser(username);
        if (user == null) {
            throw new WebApplicationException("User not found by username '" + username + "'",
                NOT_FOUND);
        }
        return UserDto.fromEntity(user);
    }

    @Operation(summary = "Get Current User")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @GetMapping("/current_user")
    public UserDto getCurrentUser() {
        User currentUser = authService.getCurrentUser().orElseThrow(() ->
                new WebApplicationException("No profile found for current user", NOT_FOUND));
        return UserDto.fromEntity(currentUser);
    }
}
