package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.entity.Asset;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AssetDao {
    @PersistenceContext
    EntityManager entityManager;

    public Asset getAssetByAppIdAndFileNameAndLanguage(UUID appId, String filename, String language, String defaultLanguage) {

        Asset asset = getAssetByAppIdAndFileNameAndLanguage(appId, filename, language);
        if (asset == null && !language.equals(defaultLanguage)) {
            asset = getAssetByAppIdAndFileNameAndLanguage(appId, filename, defaultLanguage);
        }
        return asset;
    }

    private Asset getAssetByAppIdAndFileNameAndLanguage(UUID appId, String filename, String language) {
        String sql = "select * from assets where app_id = :app_id and filename = :filename and language = :language limit 1";

        Query query = entityManager.createNativeQuery(sql, Asset.class)
                .setParameter("appId", appId)
                .setParameter("filename", filename)
                .setParameter("language", language);
        List<Asset> results = query.getResultList();

        return results.isEmpty() ? null : results.get(0);
    }
}
