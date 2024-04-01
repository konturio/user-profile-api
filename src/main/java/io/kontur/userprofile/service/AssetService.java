package io.kontur.userprofile.service;

import io.kontur.userprofile.dao.AssetDao;
import io.kontur.userprofile.model.dto.AssetDto;
import io.kontur.userprofile.model.entity.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
@Transactional
public class AssetService {
    private final String DEFAULT_LANGUAGE = "en";
    private final AssetDao assetDao;

    public Optional<AssetDto> getAssetByAppIdAndFileNameAndLanguage(UUID appId, String filename, String language) {
        Asset asset = assetDao.getAssetByAppIdAndFileNameAndLanguage(appId, filename, language, DEFAULT_LANGUAGE);
        return asset == null ? Optional.empty() : Optional.of(AssetDto.fromEntity(asset));
    }

    public String parseLanguage(Locale locale) {
        return locale == null || locale.getLanguage() == null || isBlank(locale.getLanguage())
                ? DEFAULT_LANGUAGE
                : locale.getLanguage();
    }
}
