package io.kontur.userprofile.model.dto;

import lombok.*;

import java.time.OffsetDateTime;

import io.kontur.userprofile.model.entity.Asset;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AssetDto {
    private Long id;
    private String mediaType;
    private String mediaSubtype;
    private String filename;
    private String description;
    private Long ownerUserId;
    private String language;
    private OffsetDateTime lastUpdated;
    private byte[] asset;

    public static AssetDto fromEntity(Asset asset) {
        return asset == null ? null : new AssetDto(asset.getId(), asset.getMediaType(), asset.getMediaSubtype(),
                asset.getFilename(), asset.getDescription(), asset.getOwnerUserId(), asset.getLanguage(),
                asset.getLastUpdated(), asset.getAsset());
    }
}