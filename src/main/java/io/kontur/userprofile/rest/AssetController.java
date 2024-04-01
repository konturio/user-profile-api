package io.kontur.userprofile.rest;

import io.kontur.userprofile.model.dto.AssetDto;
import io.kontur.userprofile.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;

    @Transactional(readOnly = true)
    @Operation(summary = "Get asset for application by language and filename.")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = APPLICATION_OCTET_STREAM_VALUE))
    @GetMapping(path = "/{appId}/{filename}")
    public ResponseEntity<byte[]> getAsset(@PathVariable(name = "appId", required = true) UUID appId,
                                           @PathVariable(name = "filename", required = true) String filename,
                                           HttpServletRequest request) {
        String language = assetService.parseLanguage(request.getLocale());
        Optional<AssetDto> assetOpt = assetService.getAssetByAppIdAndFileNameAndLanguage(appId, filename, language);
        if (assetOpt.isPresent()) {
            AssetDto asset = assetOpt.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType(asset.getMediaType(), asset.getMediaSubtype()));
            return new ResponseEntity<>(asset.getAsset(), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
