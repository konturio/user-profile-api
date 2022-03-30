package io.kontur.userprofile.rest;

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
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
