package io.kontur.userprofile.rest;

import io.kontur.userprofile.model.dto.FeatureDto;
import io.kontur.userprofile.service.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/features")
public class FeaturesController {
    private final FeatureService featureService;

    @GetMapping("/user_feed")
    @Transactional
    public String getDefaultUserEventFeed() {
        return featureService.getUserDefaultEventFeed(getTokenClaims());
    }

    @Operation(summary = "Get enabled features allowed for user by username retrieved from token "
        + "(including public ones). Currently it returns features of all types, but this can be"
        + "split into multiple endpoints if required (layes, ui features, event feeds, etc)")
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(implementation = FeatureDto.class))))
    @GetMapping
    @Transactional
    public List<FeatureDto> getUserFeatures() {
        return featureService.getUserFeatures(getTokenClaims())
            .map(FeatureDto::fromEntity)
            .collect(Collectors.toList());
    }

    private List<String> getTokenClaims() {
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
}
