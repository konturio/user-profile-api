package io.kontur.userprofile.rest;

import com.fasterxml.jackson.databind.JsonNode;
import io.kontur.userprofile.model.dto.FeatureDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.service.AppService;
import io.kontur.userprofile.service.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/features")
public class FeaturesController {
    private final FeatureService featureService;
    private final AppService appService;

    @Operation(summary = "Get default event feed for DN2 app for current user")
    @GetMapping("/user_feed")
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(implementation = String.class)))
    @Transactional(readOnly = true)
    public String getDefaultUserEventFeed() {
        App app = appService.getApp(appService.getDefaultId());
        return featureService.getDefaultDn2EventFeedForCurrentUser(app);
    }

    @Operation(summary =
        "Get features for app id allowed for user by username retrieved from token "
            + "(including public ones)")
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = FeatureDto.class))))
    @GetMapping
    @Transactional(readOnly = true)
    public List<FeatureDto> getUserFeatures(
        @RequestParam(name = "appId", defaultValue = "58851b50-9574-4aec-a3a6-425fa18dcb54")
        @Parameter(name = "appId",
            example = "58851b50-9574-4aec-a3a6-425fa18dcb54") //DN2_ID, but must be constant here
            UUID appId) {
        App app = appService.getApp(appId);
        return featureService.getCurrentUserAppFeatures(app)
            .map(FeatureDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Update feature configuration for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the user's feature configuration"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: User not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User is not allowed to configure this feature"),
            @ApiResponse(responseCode = "404", description = "Not Found: App, feature, or user does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Error processing the update")
    })
    @PutMapping("/{featureName}")
    public ResponseEntity<Void> updateUserFeatureConfiguration(
            @PathVariable(name = "featureName", required = true) String featureName,
            @RequestParam(name = "appId", defaultValue = "58851b50-9574-4aec-a3a6-425fa18dcb54", required = true) UUID appId,
            @RequestBody JsonNode configuration) {
        return featureService.updateAppUserFeatureConfiguration(appId, featureName, configuration);
    }

}
