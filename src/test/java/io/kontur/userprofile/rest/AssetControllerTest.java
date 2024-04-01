package io.kontur.userprofile.rest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.kontur.userprofile.model.dto.AssetDto;
import io.kontur.userprofile.model.entity.Asset;
import io.kontur.userprofile.service.AssetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class AssetControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private AssetController assetController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(assetController).build();
    }

    @Test
    void testGetAsset_ImageFound() throws Exception {
        AssetDto mockAsset = createMockAsset("image", "png", imageContent());
        mockAssetService(mockAsset);

        mockMvc.perform(get("/assets/{appId}/{filename}", mockAsset.getAppId(), mockAsset.getFilename()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(mockAsset.getAsset()));
    }

    @Test
    public void testGetAsset_TextFound() throws Exception {
        AssetDto mockAsset = createMockAsset("text", "plain", textContent());
        mockAssetService(mockAsset);

        mockMvc.perform(get("/assets/{appId}/{filename}", mockAsset.getAppId(), mockAsset.getFilename()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string(new String(mockAsset.getAsset())));
    }

    @Test
    public void testGetAsset_NotFound() throws Exception {
        mockAssetService(null);

        mockMvc.perform(get("/assets/{appId}/{filename}", UUID.randomUUID(), "nonexistentfile"))
                .andExpect(status().isNotFound());
    }

    private AssetDto createMockAsset(String mediaType, String mediaSubtype, byte[] content) {
        Asset asset = new Asset(1L, mediaType, mediaSubtype, "testfile", "test description", 1L, "en", OffsetDateTime.now(), UUID.randomUUID(), 1L, content, null, null);
        return AssetDto.fromEntity(asset);
    }

    private byte[] imageContent() {
        return new byte[]{
                (byte) 0x89, 'P', 'N', 'G', (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A, 0x00, 0x00, 0x00, 0x0D, 'I', 'H', 'D', 'R',
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x08, 0x02, 0x00, 0x00, 0x00, (byte) 0x90, (byte) 0x77, (byte) 0x53, (byte) 0xDE,
                0x00, 0x00, 0x00, 0x0C, 'I', 'D', 'A', 'T', 0x08, (byte) 0xD7, 0x01, 0x00, 0x00, 0x00, (byte) 0xC0, 0x02, (byte) 0x50, 0x05, (byte) 0x57,
                0x00, (byte) 0x8C, (byte) 0xFE, (byte) 0x32, 0x00, 0x00, 0x00, 0x00, 'I', 'E', 'N', 'D', (byte) 0xAE, (byte) 0x42, (byte) 0x60, (byte) 0x82
        };
    }

    private byte[] textContent() {
        return "Hello World".getBytes(StandardCharsets.UTF_8);
    }

    private void mockAssetService(AssetDto mockAsset) {
        when(assetService.getAssetByAppIdAndFileNameAndLanguage(any(), anyString(), anyString())).thenReturn(Optional.ofNullable(mockAsset));
        when(assetService.parseLanguage(any())).thenReturn("en");
    }
}