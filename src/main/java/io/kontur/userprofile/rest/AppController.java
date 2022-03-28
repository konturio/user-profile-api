package io.kontur.userprofile.rest;

import static io.kontur.userprofile.model.entity.Role.Names.CREATE_APPS;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.model.dto.AppDto;
import io.kontur.userprofile.model.dto.AppSummaryDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.User;
import io.kontur.userprofile.rest.exception.WebApplicationException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/apps")
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;
    private final FeatureService featureService;
    private final AuthService authService;

    @Operation(summary = "Create a new embedded app")
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AppDto.class)))
    @PostMapping
    @PreAuthorize("hasRole('" + CREATE_APPS + "')")
    //todo test - see AbstractIntegrationTest in layers-api - this can cover roles
    public AppDto create(@Parameter(name = "app") @RequestBody AppDto appDto) {
        User currentUser = authService.getCurrentUser().orElseThrow(() ->
            new WebApplicationException(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                HttpStatus.UNAUTHORIZED));

        App app = App.fromDto(appDto);
        app.setOwner(currentUser);
        appService.createApp(app, appDto.getFeatures());

        List<Feature> appFeatures = featureService.getAppFeatures(app).toList();
        return AppDto.fromEntities(app, appFeatures, true);
    }

    @Operation(summary = "Delete app")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('" + CREATE_APPS + "')")
    public ResponseEntity<?> delete(@PathVariable @Parameter(name = "id") UUID id) {
        appService.deleteApp(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Update existing app")
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AppDto.class)))
    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('" + CREATE_APPS + "')")
    public AppDto update(@PathVariable @Parameter(name = "id") UUID id,
                         @RequestBody @Parameter(name = "app") AppDto appDto) {
        App app = App.fromDto(appDto);
        App updated = appService.updateApp(id, app, appDto.getFeatures());

        List<Feature> appFeatures = featureService.getAppFeatures(updated).toList();
        return AppDto.fromEntities(updated, appFeatures, true);
    }

    @Transactional(readOnly = true)
    @Operation(summary = "Get application information by id")
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AppDto.class)))
    @GetMapping(path = "/{id}")
    public AppDto get(@PathVariable @Parameter(name = "id",
        example = "58851b50-9574-4aec-a3a6-425fa18dcb54") //DN2_ID, but must be constant here
                          UUID id) {
        App app = appService.getApp(id);
        List<Feature> features = featureService.getCurrentUserAppFeatures(app).toList();

        boolean isOwnedByCurrentUser = appService.isAppOwnedByCurrentUser(app);
        return AppDto.fromEntities(app, features, isOwnedByCurrentUser);
    }

    @Operation(summary = "Get default application id")
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    @GetMapping(path = "/default_id")
    public String getDefaultId() {
        return appService.getDefaultId().toString();
    }

    @Transactional(readOnly = true)
    @Operation(summary = "Get application list available to user (includes public apps"
        + " and user-owned apps)")
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = AppSummaryDto.class))))
    @GetMapping
    public List<AppSummaryDto> getList() {
        return appService.getAppListForCurrentUser();
    }
}
