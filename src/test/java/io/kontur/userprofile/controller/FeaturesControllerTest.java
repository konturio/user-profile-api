package io.kontur.userprofile.controller;

import static io.kontur.userprofile.AuthUtils.mockAuthWithClaims;
import static io.kontur.userprofile.AuthUtils.mockAuthWithNoGrantedAuthorities;
import static io.kontur.userprofile.AuthUtils.mockNullAuth;
import static io.kontur.userprofile.TestDataFactory.createEnabledBetaEventFeed;
import static io.kontur.userprofile.TestDataFactory.createEnabledBetaFeature;
import static io.kontur.userprofile.TestDataFactory.createEnabledEventFeed;
import static io.kontur.userprofile.TestDataFactory.createEnabledFeature;
import static io.kontur.userprofile.TestDataFactory.userWithBetaRole;
import static io.kontur.userprofile.TestDataFactory.userWithoutBetaRole;
import static io.kontur.userprofile.controller.AppControllerIT.DN2_NAME;
import static io.kontur.userprofile.service.AppService.DN2_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.AppFeatureDao;
import io.kontur.userprofile.dao.AppUserFeatureDao;
import io.kontur.userprofile.dao.FeatureDao;
import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.FeatureDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.user.Role;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.FeaturesController;
import io.kontur.userprofile.service.AppService;
import io.kontur.userprofile.service.FeatureService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class FeaturesControllerTest { //todo test for enable/disable for DAO
    //features
    private final Feature defaultDn2Feature = createEnabledFeature();
    private final Feature privateFeature = createEnabledFeature();
    private final Feature betaFeature = createEnabledBetaFeature();
    //event feeds
    private final Feature defaultDn2Feed = createEnabledEventFeed();
    private final Feature privateEventFeed = createEnabledEventFeed();
    private final Feature betaEventFeed = createEnabledBetaEventFeed();

    private final App dn2 = new App(DN2_ID, DN2_NAME, null, null, true, null);

    @Mock
    UserDao userDao = mock(UserDao.class);
    @Mock
    AppUserFeatureDao appUserFeatureDao = mock(AppUserFeatureDao.class);
    @Mock
    AppFeatureDao appFeatureDao = mock(AppFeatureDao.class);
    @Mock
    FeatureDao featureDao = mock(FeatureDao.class);
    @Mock
    AppService appService = mock(AppService.class);
    @Mock
    AuthService authService = new AuthService(userDao);
    FeatureService featureService =
        new FeatureService(authService, appFeatureDao, featureDao, appUserFeatureDao);
    FeaturesController featuresController = new FeaturesController(featureService, appService);
    private User userWithoutBetaRole = userWithoutBetaRole();
    private User userWithBetaRole = userWithBetaRole();

    @BeforeEach
    public void beforeEach() {
        when(appService.getDefaultId()).thenReturn(DN2_ID);
        when(appService.getApp(DN2_ID)).thenReturn(dn2);
        when(appFeatureDao.getEnabledNonBetaAppFeaturesFor(dn2)).thenReturn(
            Stream.of(defaultDn2Feature, defaultDn2Feed));

        when(userDao.getUser(userWithBetaRole.getUsername())).thenReturn(userWithBetaRole);
        when(userDao.getUser(userWithoutBetaRole.getUsername())).thenReturn(userWithoutBetaRole);
    }

    @Test
    public void getUserFeatures_UserIsNotAuthorizedTest() {
        givenUserIsUnauthorized();
        thenOnlyDefaultAppFeaturesAreReturned();
    }

    @Test
    public void getUserFeatures_NoAuthTest() {
        givenAuthorizationIsNull();
        thenOnlyDefaultAppFeaturesAreReturned();
    }

    @Test
    public void getUserFeatures_NoAuthClaimsInTokenTest() {
        givenAuthorizationContainsNoAuthorities();
        thenOnlyDefaultAppFeaturesAreReturned();
    }

    @Test
    public void getUserFeatures_UserIsAuthorizedNoBetaRoleTest() {
        givenUserWithoutBetaRoleIsLoggedIn();
        //enabled features are configured by user (including beta features), but result
        // doesn't include beta ones - as user does not have a corresponding role
        thenResultContainsOnlyPrivateFeatures();
    }

    @Test
    public void getUserFeatures_UserIsAuthorizedNoBetaRole_NoAnyFeaturesConfiguredTest() {
        givenUserWithoutAnyConfiguredFeaturesIsLoggedIn();
        //no features are configured by user
        //so result contains the list of public features
        thenOnlyDefaultAppFeaturesAreReturned();
    }

    @Test
    public void getUserFeatures_UserIsAuthorizedAndHasBetaRoleTest() {
        givenUserWithBetaRoleIsLoggedIn();

        List<FeatureDto> result = featuresController.getUserFeatures(DN2_ID);

        //enabled features are configured by user (including beta features),
        // result includes beta features - as user does have a corresponding role
        assertEquals(4, result.size());
        thenResultContainsPrivateFeatures(result);
        thenResultContainsPrivateBetaFeatures(result);
    }

    @Test
    public void getUserFeatures_UserIsNotFoundTest() {
        authorizeNotExistingUser();
        thenOnlyDefaultAppFeaturesAreReturned();
    }

    @Test
    public void getUserFeed_UserIsNotAuthorizedTest() {
        givenUserIsUnauthorized();
        thenOnlyDefaultDn2EventFeedIsReturned();
    }

    @Test
    public void getUserFeed_NoAuthTest() {
        givenAuthorizationIsNull();
        thenOnlyDefaultDn2EventFeedIsReturned();
    }

    @Test
    public void getUserFeed_NoAuthClaimsInTokenTest() {
        givenAuthorizationContainsNoAuthorities();
        thenOnlyDefaultDn2EventFeedIsReturned();
    }

    @Test
    public void getUserFeed_UserIsAuthorizedNoBetaRoleTest() {
        givenUserWithoutBetaRoleIsLoggedIn();
        //two feeds are configured by user - which is incorrect, but beta are not allowed
        thenOnlyPrivateEventFeedIsReturned();
    }

    @Test
    public void getUserFeed_UserIsAuthorizedIncludeBetaTest() {
        givenUserWithBetaRoleIsLoggedIn();
        //two feeds are configured by user - which is incorrect, but non-beta should go first
        thenOnlyPrivateEventFeedIsReturned();
    }

    @Test
    public void getUserFeed_UserIsNotFoundTest() {
        authorizeNotExistingUser();
        thenOnlyDefaultDn2EventFeedIsReturned();
    }


    private void givenUserWithBetaRoleIsLoggedIn() {
        mockAuthWithClaims(List.of(Role.Names.BETA_FEATURES), userWithBetaRole.getUsername());
        when(appUserFeatureDao.userHasAnyConfiguredFeatures(dn2,
            userWithBetaRole.getUsername()))
            .thenReturn(true);
        when(appUserFeatureDao.getAppUserFeatures(dn2, userWithBetaRole.getUsername()))
            .thenReturn(Stream.of(
                betaFeature,
                privateFeature,
                privateEventFeed,
                betaEventFeed
            ));
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

    private void givenUserWithoutBetaRoleIsLoggedIn() {
        mockAuthWithClaims(List.of("nothing"), userWithoutBetaRole.getUsername());
        when(appUserFeatureDao.userHasAnyConfiguredFeatures(dn2,
            userWithoutBetaRole.getUsername()))
            .thenReturn(true);
        when(appUserFeatureDao.getAppUserFeatures(dn2, userWithoutBetaRole.getUsername()))
            .thenReturn(Stream.of(betaFeature, privateFeature,
                privateEventFeed, betaEventFeed
            ));
    }

    private void givenUserWithoutAnyConfiguredFeaturesIsLoggedIn() {
        mockAuthWithClaims(List.of("nothing"), userWithoutBetaRole.getUsername());
        when(appUserFeatureDao.userHasAnyConfiguredFeatures(dn2,
            userWithoutBetaRole.getUsername())).thenReturn(false);
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

    private void thenOnlyDefaultAppFeaturesAreReturned() {
        List<FeatureDto> result = featuresController.getUserFeatures(DN2_ID);
        assertEquals(2, result.size());

        FeatureDto fefaultAppFeatureDto =
            findDtoInResult(result, defaultDn2Feature.getName());
        assertDto(defaultDn2Feature, fefaultAppFeatureDto);

        FeatureDto defaultDn2EventFeedDto =
            findDtoInResult(result, defaultDn2Feed.getName());
        assertDto(defaultDn2Feed, defaultDn2EventFeedDto);
    }

    private void thenOnlyPrivateEventFeedIsReturned() {
        String result = featuresController.getDefaultUserEventFeed();
        assertEquals(privateEventFeed.getName(), result);
    }

    private void thenOnlyDefaultDn2EventFeedIsReturned() {
        String result = featuresController.getDefaultUserEventFeed();
        assertEquals(defaultDn2Feed.getName(), result);
    }

    private void thenResultContainsOnlyPrivateFeatures() {
        List<FeatureDto> result = featuresController.getUserFeatures(DN2_ID);
        assertEquals(2, result.size());
        thenResultContainsPrivateFeatures(result);
    }

    private void thenResultContainsPrivateFeatures(List<FeatureDto> result) {
        FeatureDto privateFeatureDto =
            findDtoInResult(result, privateFeature.getName());
        assertDto(privateFeature, privateFeatureDto);

        FeatureDto privateEventFeedDto =
            findDtoInResult(result, privateEventFeed.getName());
        assertDto(privateEventFeed, privateEventFeedDto);
    }

    private void thenResultContainsPrivateBetaFeatures(List<FeatureDto> result) {
        FeatureDto betaFeatureDto = findDtoInResult(result, betaFeature.getName());
        assertDto(betaFeature, betaFeatureDto);

        FeatureDto betaEventFeedDto =
            findDtoInResult(result, betaEventFeed.getName());
        assertDto(betaEventFeed, betaEventFeedDto);
    }

    private void assertDto(Feature source, FeatureDto dto) {
        assertEquals(source.getName(), dto.getName());
        assertEquals(source.getDescription(), dto.getDescription());
        assertEquals(source.getType(), dto.getType());
    }
}
