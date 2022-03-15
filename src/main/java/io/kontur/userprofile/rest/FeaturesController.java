package io.kontur.userprofile.rest;

import static io.kontur.userprofile.service.FeatureService.DN2_ID;
import static io.kontur.userprofile.service.FeatureService.DN2_NAME;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/features")
public class FeaturesController { //Query params are case-sensitive as per URI RFC section 6.2.2.1
    private final FeatureService featureService;
    private final AppService appService;

    @Operation(summary = "Get default event feed for DN2 app for current user")
    @GetMapping("/" + DN2_NAME + "/user_feed") //todo or make it use UUID instead?
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(implementation = String.class)))
    @Transactional(readOnly = true)
    public String getDefaultUserEventFeed() {
        App app = appService.getApp(DN2_ID);
        return featureService.getDefaultDn2EventFeedForCurrentUser(app);
    }

    @Operation(summary =
        "Get features for app id allowed for user by username retrieved from token "
            + "(including public ones)")
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = FeatureDto.class))))
    @GetMapping(path = "/{id}")
    @Transactional(readOnly = true)
    public List<FeatureDto> getUserFeatures(
        @PathVariable @Parameter(name = "id",
            example = "58851b50-9574-4aec-a3a6-425fa18dcb54") //DN2_ID, but must be constant here
            UUID id) {
        App app = appService.getApp(id);
        return featureService.getCurrentUserAppFeatures(app)
            .map(FeatureDto::fromEntity)
            .collect(Collectors.toList());
    }

}
