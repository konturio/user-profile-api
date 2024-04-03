package io.kontur.userprofile.dao;

import io.kontur.userprofile.model.dto.AppSummaryDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Asset;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppDao {
    @PersistenceContext
    EntityManager entityManager;

    public void createApp(App app) {
        entityManager.persist(app);
    }

    public App updateApp(App entityToUpdate) {
        entityManager.persist(entityToUpdate);
        return entityToUpdate;
    }

    public void deleteApp(App app) {
        entityManager.remove(app);
    }

    public List<AppSummaryDto> getPublicAppsList() {
        return entityManager.createQuery("select new io.kontur.userprofile.model.dto"
                        + ".AppSummaryDto(a.id, a.name) from App a"
                        + " where a.isPublic = true", AppSummaryDto.class)
                .getResultList();
    }

    public List<AppSummaryDto> getUserOwnedAppsList(String ownerUsername) {
        return entityManager.createQuery("select new io.kontur.userprofile.model.dto"
                        + ".AppSummaryDto(a.id, a.name) from App a"
                        + " where a.owner is not null"
                        + " and a.owner.username = ?1", AppSummaryDto.class)
                .setParameter(1, ownerUsername)
                .getResultList();
    }

    public App getApp(UUID id) {
        return entityManager.find(App.class, id);
    }

    public App getApp(String domain) {
        String sql = "select * from app where :domain = any(domains) limit 1";
        Query query = entityManager.createNativeQuery(sql, App.class)
                .setParameter("domain", domain);
        List<App> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

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
                .setParameter("app_id", appId)
                .setParameter("filename", filename)
                .setParameter("language", language);
        List<Asset> results = query.getResultList();

        return results.isEmpty() ? null : results.get(0);
    }
}
