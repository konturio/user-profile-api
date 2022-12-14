package io.kontur.userprofile.controller;

import io.kontur.userprofile.AbstractIT;
import io.kontur.userprofile.dao.AppDao;
import io.kontur.userprofile.dao.FeatureDao;
import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.converters.GeoJsonUtils;
import io.kontur.userprofile.model.dto.AppDto;
import io.kontur.userprofile.model.dto.AppSummaryDto;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.AppController;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.wololo.geojson.LineString;
import org.wololo.geojson.Point;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.kontur.userprofile.service.AppService.DN2_ID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AppControllerIT extends AbstractIT {
    public static final String DN2_NAME = "Disaster Ninja";
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    AppController controller;
    @Autowired
    AppDao appDao;
    @Autowired
    UserDao userDao;
    @Autowired
    FeatureDao featureDao;
    private User user1;
    private User user2;
    private static final String featureAvailableForUserApps = "map_layers_panel";

    private static final String featureAvailableForUserApps2 = "translation";
    private static final String featureNotAvailableForUserApps = "current_episode";
    private static final String notExistingFeature = "not-existing-feature";
    private static final String configurationOne = "{\"statistics\": [{\n" +
            "              \"formula\": \"sumX\",\n" +
            "              \"x\": \"population\"\n" +
            "            }, {\n" +
            "              \"formula\": \"sumX\",\n" +
            "              \"x\": \"populated_area_km2\"\n" +
            "            }]}";
    private static final String configurationTwo = "{\"statistics\": [{\n" +
            "              \"formula\": \"sumXWhereNoY\",\n" +
            "              \"x\": \"populated_area_km2\",\n" +
            "              \"y\": \"count\"\n" +
            "            }, {\n" +
            "              \"formula\": \"sumXWhereNoY\",\n" +
            "              \"x\": \"populated_area_km2\",\n" +
            "              \"y\": \"building_count\"\n" +
            "            }]}";

    @BeforeEach
    public void before() {
        user1 = createUser();
        user2 = createUser();
    }

    @Test
    public void appWithEmptyCenterGeometryAndZoomCanBeCreated() {
        givenUserIsAuthenticated(user1);

        AppDto toCreate = createPrivateAppDto();
        toCreate.setCenterGeometry(null);
        toCreate.setZoom(null);

        AppDto result = controller.create(toCreate);
        assertNull(result.getCenterGeometry());
        assertNull(result.getZoom());
    }

    @Test
    public void dn2AppIsAvailableToUnauthenticatedUser() {
        givenUserIsNotAuthenticated();

        List<AppSummaryDto> result = controller.getList();
        assertTrue(result.contains(new AppSummaryDto(DN2_ID, DN2_NAME)));

        AppDto dn2 = controller.get(DN2_ID);
        assertFalse(dn2.getOwnedByUser());
    }

    @Test
    @Rollback(false)
    public void appCanBeCreated() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPrivateAppDto();
        AppDto response = controller.create(request);

        thenAppFieldsShownToTheOwnerAreCorrectAndDefaultFeaturesAreAdded(request, response);
    }

    @Test
    @Rollback(false)
    public void appCanBeUpdatedByOwner() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPrivateAppDto();
        AppDto created = controller.create(request);

        AppDto update = createPublicAppDto();
        AppDto updated = controller.update(created.getId(), update);

        thenAppFieldsShownToTheOwnerAreCorrect(update, updated);
        thenListOfFeaturesIsCorrect(update, updated);
    }

    @Test
    public void appCanBeDeletedByOwner() {
        givenUserIsAuthenticated(user1);
        AppDto request = createPrivateAppDto();
        AppDto response = controller.create(request);
        thenAppIsPresentInGetListResponse(response.getSummary());

        controller.delete(response.getId());

        thenAppIsAbsentFromGetListResponse(response.getSummary());
        thenAppIsAbsentFromDao(response.getId());
    }

    @Test
    public void appCannotBeUpdatedByNotAuthenticatedUser() {
        givenUserIsAuthenticated(user1);
        AppDto request = createPublicAppDto();
        AppDto created = controller.create(request);

        givenUserIsNotAuthenticated();
        AppDto update = createPublicAppDto();

        try {
            controller.update(created.getId(), update);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
        }

        thenAppIsNotChanged(created, user1);
    }

    @Test
    public void appCannotBeUpdatedByNonOwnerUser() {
        givenUserIsAuthenticated(user1);
        AppDto request = createPublicAppDto();
        AppDto created = controller.create(request);

        givenUserIsAuthenticated(user2);
        AppDto update = createPrivateAppDto();

        try {
            controller.update(created.getId(), update);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
        }

        thenAppIsNotChanged(created, user1);
    }

    @Test
    public void appCannotBeDeletedByNotAuthenticatedUser() {
        givenUserIsAuthenticated(user1);
        AppDto request = createPublicAppDto();
        AppDto created = controller.create(request);
        thenAppIsPresentInGetListResponse(created.getSummary());

        givenUserIsNotAuthenticated();

        try {
            controller.delete(created.getId());
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
        }

        thenAppIsNotChanged(created, user1);
    }

    @Test
    public void appCannotBeDeletedByNonOwnerUser() {
        givenUserIsAuthenticated(user1);
        AppDto request = createPublicAppDto();
        AppDto created = controller.create(request);
        thenAppIsPresentInGetListResponse(created.getSummary());

        givenUserIsAuthenticated(user2);

        try {
            controller.delete(created.getId());
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
        }

        thenAppIsNotChanged(created, user1);
    }

    @Test
    @Rollback(false)
    public void privateAppCanBeCreatedAndIsVisibleToTheOwner() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPrivateAppDto();
        AppDto response = controller.create(request);

        AppDto responseFromGet = controller.get(response.getId());
        thenAppFieldsShownToTheOwnerAreCorrectAndDefaultFeaturesAreAdded(request, responseFromGet);

        thenAppIsPresentInGetListResponse(response.getSummary());
    }

    @Test
    public void privateAppCanBeCreatedAndIsNotVisibleToOtherUsers() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPrivateAppDto();
        AppDto response = controller.create(request);

        givenUserIsAuthenticated(user2);

        try {
            controller.get(response.getId());
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
        thenAppIsAbsentFromGetListResponse(response.getSummary());
    }

    @Test
    public void privateAppCanBeCreatedAndIsNotVisibleToNonAuthenticatedUsers() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPrivateAppDto();
        AppDto response = controller.create(request);

        givenUserIsNotAuthenticated();

        try {
            controller.get(response.getId());
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
        thenAppIsAbsentFromGetListResponse(response.getSummary());
    }

    @Test
    @Rollback(false)
    public void publicAppCanBeCreatedAndIsVisibleToTheOwner() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        request.setPublic(true);
        AppDto response = controller.create(request);

        AppDto responseFromGet = controller.get(response.getId());
        thenAppFieldsShownToTheOwnerAreCorrectAndDefaultFeaturesAreAdded(request, responseFromGet);

        thenAppIsPresentInGetListResponse(response.getSummary());
    }

    @Test
    public void publicAppCanBeCreatedAndIsVisibleToOtherUsers() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        AppDto response = controller.create(request);

        givenUserIsAuthenticated(user2);
        AppDto responseToNonOwnerUser = controller.get(response.getId());
        thenAppFieldsShownToNonOwnersAreCorrectAndDefaultFeaturesAreAdded(request,
                responseToNonOwnerUser);
        thenAppIsPresentInGetListResponse(response.getSummary());
    }

    @Test
    public void publicAppCanBeCreatedAndIsVisibleToNonAuthenticatedUsers() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        request.setPublic(true);
        AppDto response = controller.create(request);

        givenUserIsNotAuthenticated();
        AppDto responseToNonOwnerUser = controller.get(response.getId());
        thenAppFieldsShownToNonOwnersAreCorrectAndDefaultFeaturesAreAdded(request,
                responseToNonOwnerUser);
        thenAppIsPresentInGetListResponse(response.getSummary());
    }

    @Test
    public void appCanNotBeCreatedByNonAuthenticatedUser() {
        givenUserIsNotAuthenticated();

        AppDto request = createPublicAppDto();

        try {
            controller.create(request);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
        }
    }

    @Test
    public void twoAppsWithTheSameNameCanBeCreated() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        AppDto response1 = controller.create(request);
        AppDto response2 = controller.create(request);

        assertNotEquals(response1.getId(), response2.getId());
    }

    @Test
    public void twoAppsWithTheSameNameCanBeCreatedByDifferentUsers() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        AppDto response1 = controller.create(request);

        givenUserIsAuthenticated(user2);
        AppDto response2 = controller.create(request);

        assertEquals(response1.getName(), response2.getName());
        assertNotEquals(response1.getId(), response2.getId());
    }

    @Test
    public void featureNotAvailableForUserAppsCannotBeAddedToApp() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        AppDto response1 = controller.create(request);

        AppDto update = createPublicAppDto();
        update.setConfigurationByFeatureNames(Map.of(featureAvailableForUserApps, configurationOne,
                featureNotAvailableForUserApps, configurationTwo));

        try {
            controller.update(response1.getId(), update);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }
    }

    @Test
    public void nonExistingFeaturesCannotBeAddedToApp() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        AppDto response1 = controller.create(request);

        AppDto update = createPublicAppDto();
        update.setConfigurationByFeatureNames(Map.of(featureAvailableForUserApps, configurationOne,
                notExistingFeature, configurationTwo));

        try {
            controller.update(response1.getId(), update);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }
    }

    @Test
    public void betaFeaturesCannotBeAddedToAppIfUserDoesNotHaveTheBetaRole() {
        givenUserWithoutBetaRoleIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        AppDto response1 = controller.create(request);

        AppDto update = createPublicAppDto();
        update.setConfigurationByFeatureNames(Map.of(createBetaFeature().getName(), configurationOne));

        try {
            controller.update(response1.getId(), update);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
        }
    }

    @Test
    public void betaFeaturesCannotBeAddedToAppEvenIfTheUserHasTheBetaRole() {
        givenUserWithBetaRoleIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        AppDto response1 = controller.create(request);

        AppDto update = createPublicAppDto();
        update.setConfigurationByFeatureNames(Map.of(createBetaFeature().getName(), configurationOne));

        try {
            controller.update(response1.getId(), update);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
        }
    }

    private Feature createBetaFeature() {
        String betaFeature = "betaFeature";
        Feature someBetaFeature = new Feature();
        someBetaFeature.setBeta(true);
        someBetaFeature.setName(betaFeature);
        someBetaFeature.setType(FeatureType.UI_PANEL);
        someBetaFeature.setAvailableForUserApps(true);
        someBetaFeature.setEnabled(true);
        entityManager.persist(someBetaFeature);
        return someBetaFeature;
    }

    @Test
    public void cannotCreateAppWithNonExistingFeatures() {
        givenUserIsAuthenticated(user1);

        AppDto request = createPublicAppDto();
        request.setConfigurationByFeatureNames(Map.of(notExistingFeature, configurationOne));

        try {
            controller.create(request);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }
    }

    @Test
    public void notExistingAppCannotBeUpdated() {
        givenUserIsAuthenticated(user1);

        try {
            controller.update(UUID.randomUUID(), createPublicAppDto());
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void centerGeometryMustBeAPointWhenCreatingAnApp() {
        givenUserIsAuthenticated(user1);

        AppDto dto = createPublicAppDto();
        dto.setCenterGeometry(
                new LineString(new double[][]{new double[]{1d, 2d}, new double[]{3d, 4d}}));

        try {
            controller.create(dto);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
            assertEquals("CenterGeometry must be a Point", e.getMessage());
        }
    }

    @Test
    public void zoomMustBeSpecifiedWhenCreatingAnAppWithCenterGeometry() {
        givenUserIsAuthenticated(user1);

        AppDto dto = createPublicAppDto();
        dto.setCenterGeometry(
                new Point(new double[]{1d, 2d}));
        dto.setZoom(null);

        try {
            controller.create(dto);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
            assertEquals("Either both Zoom and CenterGeometry or none of them should "
                    + "be specified", e.getMessage());
        }
    }

    @Test
    public void centerGeometryMustBeSpecifiedWhenCreatingAnAppWithZoom() {
        givenUserIsAuthenticated(user1);

        AppDto dto = createPublicAppDto();
        dto.setCenterGeometry(null);
        dto.setZoom(BigDecimal.valueOf(0.1));

        try {
            controller.create(dto);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
            assertEquals("Either both Zoom and CenterGeometry or none of them should "
                    + "be specified", e.getMessage());
        }
    }

    @Test
    public void centerGeometryMustBeAPointWhenUpdatingAnApp() {
        givenUserIsAuthenticated(user1);
        AppDto created = controller.create(createPrivateAppDto());

        AppDto dto = createPublicAppDto();
        dto.setCenterGeometry(
                new LineString(new double[][]{new double[]{1d, 2d}, new double[]{3d, 4d}}));

        try {
            controller.update(created.getId(), dto);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
            assertEquals("CenterGeometry must be a Point", e.getMessage());
        }
    }

    @Test
    public void zoomMustBeSpecifiedWhenUpdatingAnAppWithCenterGeometry() {
        givenUserIsAuthenticated(user1);
        AppDto created = controller.create(createPrivateAppDto());

        AppDto dto = createPublicAppDto();
        dto.setCenterGeometry(
                new Point(new double[]{1d, 2d}));
        dto.setZoom(null);

        try {
            controller.update(created.getId(), dto);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
            assertEquals("Either both Zoom and CenterGeometry or none of them should "
                    + "be specified", e.getMessage());
        }
    }

    @Test
    public void centerGeometryMustBeSpecifiedWhenUpdatingAnAppWithZoom() {
        givenUserIsAuthenticated(user1);
        AppDto created = controller.create(createPrivateAppDto());

        AppDto dto = createPublicAppDto();
        dto.setZoom(BigDecimal.valueOf(0.1));
        dto.setCenterGeometry(null);

        try {
            controller.update(created.getId(), dto);
            throw new RuntimeException("expected exception was not thrown");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
            assertEquals("Either both Zoom and CenterGeometry or none of them should "
                    + "be specified", e.getMessage());
        }
    }

    private AppDto createPublicAppDto() {
        AppDto request = new AppDto();
        request.setName(UUID.randomUUID().toString());
        request.setDescription(UUID.randomUUID().toString());
        request.setPublic(true);
        request.setConfigurationByFeatureNames(Map.of(featureAvailableForUserApps, configurationOne));
        request.setCenterGeometry(new Point(new double[]{2d, 3d}));
        request.setZoom(BigDecimal.valueOf(0.1));
        return request;
    }

    private AppDto createPrivateAppDto() {
        AppDto request = new AppDto();
        request.setName(UUID.randomUUID().toString());
        request.setDescription(UUID.randomUUID().toString());
        request.setPublic(false);
        request.setConfigurationByFeatureNames(Map.of(featureAvailableForUserApps2, configurationOne));
        request.setCenterGeometry(new Point(new double[]{20d, 30d}));
        request.setZoom(BigDecimal.valueOf(3.5));
        return request;
    }

    private void thenAppFieldsShownToTheOwnerAreCorrectAndDefaultFeaturesAreAdded(AppDto request,
                                                                                  AppDto response) {
        thenAppFieldsShownToTheOwnerAreCorrect(request, response);
        thenDefaultFeaturesArePresent(response);
    }

    private void thenAppFieldsShownToTheOwnerAreCorrect(AppDto request, AppDto response) {
        assertTrue(response.getOwnedByUser());
        assertBasicAppFields(request, response);
    }

    private void thenAppFieldsShownToNonOwnersAreCorrectAndDefaultFeaturesAreAdded(AppDto request,
                                                                                   AppDto response) {
        thenAppFieldsShownToNonOwnersAreCorrect(request, response);
        thenDefaultFeaturesArePresent(response);
    }

    private void thenAppFieldsShownToNonOwnersAreCorrect(AppDto request, AppDto response) {
        assertFalse(response.getOwnedByUser());
        assertBasicAppFields(request, response);
    }

    private void thenListOfFeaturesIsCorrect(AppDto request, AppDto response) {
        assertEquals(request.getConfigurationByFeatureNames(), response.getConfigurationByFeatureNames());
    }

    private void thenDefaultFeaturesArePresent(AppDto dto) {
        List<String> defaultFeatures = featureDao.getFeaturesAddedByDefaultToUserApps()
                .stream().map(Feature::getName).toList();

        defaultFeatures.forEach(feature -> assertTrue(dto.getConfigurationByFeatureNames().containsKey(feature)));
    }

    private void assertBasicAppFields(AppDto expected, AppDto actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.isPublic(), actual.isPublic());
        assertNotNull(actual.getId());
        assertEquals(expected.getZoom(), actual.getZoom());
        GeoJsonUtils.geometriesAreEqual(expected.getCenterGeometry(),
                actual.getCenterGeometry());
    }

    private void thenAppIsPresentInGetListResponse(AppSummaryDto appSummaryDto) {
        List<AppSummaryDto> responseFromGetList = controller.getList();
        assertTrue(responseFromGetList.contains(appSummaryDto));
    }

    private void thenAppIsAbsentFromGetListResponse(AppSummaryDto appSummaryDto) {
        List<AppSummaryDto> responseFromGetList = controller.getList();
        assertFalse(responseFromGetList.contains(appSummaryDto));
    }

    private void thenAppIsAbsentFromDao(UUID id) {
        assertNull(appDao.getApp(id));
    }

    private void thenAppIsNotChanged(AppDto first, User owner) {
        givenUserIsAuthenticated(owner);
        AppDto second = controller.get(first.getId());
        assertEquals(first, second);
    }
}
