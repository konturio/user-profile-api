package io.kontur.userprofile.controller;

import static io.kontur.userprofile.AuthUtils.mockAuthWithClaims;
import static io.kontur.userprofile.AuthUtils.mockAuthWithNoGrantedAuthorities;
import static io.kontur.userprofile.AuthUtils.mockNullAuth;
import static io.kontur.userprofile.TestDataFactory.*;
import static io.kontur.userprofile.controller.AppControllerIT.DN2_NAME;
import static io.kontur.userprofile.service.AppService.DN2_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.*;
import io.kontur.userprofile.model.dto.FeatureDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.CustomAppFeature;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.FeaturesController;
import io.kontur.userprofile.service.AppService;
import io.kontur.userprofile.service.FeatureService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class FeaturesControllerTest { //todo test for enable/disable for DAO
    //features
    private final Feature enabledFeature1 = createEnabledFeature("enabledFeature1");
    private final Feature enabledFeature2 = createEnabledFeature("enabledFeature2");
    //event feed
    private final Feature enabledEventFeed = createEnabledEventFeed();

    private final App dn2 = new App(DN2_ID, DN2_NAME, null, null, true, null, null, null, null, null);

    @Mock
    UserDao userDao = mock(UserDao.class);
    @Mock
    CustomAppFeatureDao customAppFeatureDao = mock(CustomAppFeatureDao.class);
    @Mock
    UserCustomRoleDao userCustomRoleDao = mock(UserCustomRoleDao.class);
    @Mock
    FeatureDao featureDao = mock(FeatureDao.class);
    @Mock
    AppDao appDao = mock(AppDao.class);
    @Mock
    AppService appService = mock(AppService.class);
    @Mock
    AuthService authService = new AuthService(userDao);
    FeatureService featureService =
        new FeatureService(authService, appDao, userDao, customAppFeatureDao, userCustomRoleDao, featureDao);
    FeaturesController featuresController = new FeaturesController(featureService, appService);
    private User user = defaultUser();

    @BeforeEach
    public void beforeEach() {
        when(appService.getDefaultId()).thenReturn(DN2_ID);
        when(appService.getApp(DN2_ID)).thenReturn(dn2);
        when(customAppFeatureDao.getAllFeaturesAvailableToUser(dn2, null, null)).thenReturn(List.of(
                new CustomAppFeature(dn2, enabledFeature1, false, null, null, null),
                new CustomAppFeature(dn2, enabledEventFeed, false, null, null, null)
        ));
        when(customAppFeatureDao.getAllFeaturesAvailableToUser(dn2, user.getUsername(), new ArrayList<>())).thenReturn(List.of(
                new CustomAppFeature(dn2, enabledFeature1, false, null, null, null),
                new CustomAppFeature(dn2, enabledFeature2, true, null, null, null),
                new CustomAppFeature(dn2, enabledEventFeed, false, null, null, null)
        ));

        when(userDao.getUser(user.getUsername())).thenReturn(user);
    }

    @Test
    public void getUserFeatures_UserIsNotAuthorizedTest() {
        givenUserIsUnauthorized();
        thenOnlyGuestAppFeaturesAreReturned();
    }

    @Test
    public void getUserFeatures_NoAuthTest() {
        givenAuthorizationIsNull();
        thenOnlyGuestAppFeaturesAreReturned();
    }

    @Test
    public void getUserFeatures_NoAuthClaimsInTokenTest() {
        givenAuthorizationContainsNoAuthorities();
        thenOnlyGuestAppFeaturesAreReturned();
    }

    @Test
    public void getUserFeatures_UserIsAuthorized() {
        givenUserIsLoggedIn();
        thenResultContainsGuestAndAuthFeatures();
    }

    @Test
    public void getUserFeatures_UserIsNotFoundTest() {
        authorizeNotExistingUser();
        thenOnlyGuestAppFeaturesAreReturned();
    }

    private void authorizeNotExistingUser() {
        String notExistingUsername =
            "some-other-username"; //userDao mock returns null for it = user not found
        mockAuthWithClaims(List.of("nothing"), notExistingUsername);
    }

    private void givenAuthorizationContainsNoAuthorities() {
        mockAuthWithNoGrantedAuthorities();
    }

    private void givenAuthorizationIsNull() {
        mockNullAuth();
    }

    private void givenUserIsLoggedIn() {
        mockAuthWithClaims(List.of("nothing"), user.getUsername());
    }

    private void givenUserIsUnauthorized() {
        mockAuthWithClaims(List.of("no appropriate roles"), null);
    }

    private FeatureDto findDtoInResult(List<FeatureDto> featureDtos, String name) {
        Optional<FeatureDto> privateFeatureDto = featureDtos.stream()
            .filter(it -> name.equals(it.getName()))
            .findAny();
        return privateFeatureDto.get();
    }

    private void thenOnlyGuestAppFeaturesAreReturned() {
        List<FeatureDto> result = featuresController.getUserFeatures(DN2_ID);
        assertEquals(2, result.size());

        FeatureDto enabledFeature1Dto =
            findDtoInResult(result, enabledFeature1.getName());
        assertDto(enabledFeature1, enabledFeature1Dto);

        FeatureDto enabledEventFeedDto =
            findDtoInResult(result, enabledEventFeed.getName());
        assertDto(enabledEventFeed, enabledEventFeedDto);
    }

    private void thenResultContainsGuestAndAuthFeatures() {
        List<FeatureDto> result = featuresController.getUserFeatures(DN2_ID);
        assertEquals(3, result.size());
        thenResultContainsGuestAndAuthFeatures(result);
    }

    private void thenResultContainsGuestAndAuthFeatures(List<FeatureDto> result) {
        FeatureDto enabledFeature1Dto =
            findDtoInResult(result, enabledFeature1.getName());
        assertDto(enabledFeature1, enabledFeature1Dto);

        FeatureDto enabledFeature2Dto =
                findDtoInResult(result, enabledFeature2.getName());
        assertDto(enabledFeature2, enabledFeature2Dto);

        FeatureDto privateEventFeedDto =
            findDtoInResult(result, enabledEventFeed.getName());
        assertDto(enabledEventFeed, privateEventFeedDto);
    }

    private void assertDto(Feature source, FeatureDto dto) {
        assertEquals(source.getName(), dto.getName());
        assertEquals(source.getDescription(), dto.getDescription());
        assertEquals(source.getType(), dto.getType());
    }
}
