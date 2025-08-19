package io.kontur.userprofile.rest;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import io.kontur.userprofile.service.AccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/access")
public class AccessController {
    private final AuthService authService;
    private final AccessService accessService;

    @Operation(summary = "Regesters the Current User as an owner of the layer provided")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new_layer/{layerId}")
    public ResponseEntity<Void> newLayer(@PathVariable @Parameter(name = "layerId") long layerId) {
        User currentUser = authService.getCurrentUser().orElseThrow(() ->
                new WebApplicationException("No profile found for current user", NOT_FOUND));
        accessService.newLayerBy(currentUser, layerId);
        return ResponseEntity.ok().build();
    }
}
