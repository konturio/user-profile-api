package io.kontur.userprofile.controller;

import static io.kontur.userprofile.AuthUtils.mockAuthWithClaims;
import static io.kontur.userprofile.TestDataFactory.createDisabledBetaEventFeed;
import static io.kontur.userprofile.TestDataFactory.createDisabledBetaFeature;
import static io.kontur.userprofile.TestDataFactory.createDisabledFeature;
import static io.kontur.userprofile.TestDataFactory.createDisabledPrivateEventFeed;
import static io.kontur.userprofile.TestDataFactory.createDisabledPublicEventFeed;
import static io.kontur.userprofile.TestDataFactory.createDisabledPublicFeature;
import static io.kontur.userprofile.TestDataFactory.createEnabledBetaEventFeed;
import static io.kontur.userprofile.TestDataFactory.createEnabledBetaFeature;
import static io.kontur.userprofile.TestDataFactory.createEnabledFeature;
import static io.kontur.userprofile.TestDataFactory.createEnabledPrivateEventFeed;
import static io.kontur.userprofile.TestDataFactory.createEnabledPublicEventFeed;
import static io.kontur.userprofile.TestDataFactory.createEnabledPublicFeature;
import static io.kontur.userprofile.TestDataFactory.userWithBetaRole;
import static io.kontur.userprofile.TestDataFactory.userWithoutBetaRole;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.kontur.userprofile.dao.FeatureDao;
import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.FeatureDto;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.Role;
import io.kontur.userprofile.model.entity.User;
import io.kontur.userprofile.rest.FeaturesController;
import io.kontur.userprofile.service.FeatureService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class FeaturesControllerTest {
    //features
    private final Feature disabledPublicFeature = createDisabledPublicFeature();
    private final Feature enabledPublicFeature = createEnabledPublicFeature();
    private final Feature disabledPrivateFeature = createDisabledFeature();
    private final Feature enabledPrivateFeature = createEnabledFeature();
    private final Feature disabledBetaFeature = createDisabledBetaFeature();
    private final Feature enabledBetaFeature = createEnabledBetaFeature();
    //event feeds
    private final Feature enabledPublicEventFeed = createEnabledPublicEventFeed();
    private final Feature disabledPublicEventFeed = createDisabledPublicEventFeed();
    private final Feature enabledPrivateEventFeed = createEnabledPrivateEventFeed();
    private final Feature disabledPrivateEventFeed = createDisabledPrivateEventFeed();
    private final Feature enabledBetaEventFeed = createEnabledBetaEventFeed();
    private final Feature disabledBetaEventFeed = createDisabledBetaEventFeed();

    @Mock
    UserDao userDao = mock(UserDao.class);
    @Mock
    FeatureDao featureDao = mock(FeatureDao.class);
    FeatureService featureService = new FeatureService(userDao, featureDao);
    FeaturesController featuresController = new FeaturesController(featureService);
    private User userWithoutBetaRole = userWithoutBetaRole();
    private User userWithBetaRole = userWithBetaRole();

    @BeforeEach
    public void beforeEach() {
        when(featureDao.getPublicNonBetaFeatures()).thenReturn(
            List.of(disabledPublicFeature, enabledPublicFeature,
                enabledPublicEventFeed, disabledPublicEventFeed));

        when(userDao.getUser(userWithBetaRole.getUsername())).thenReturn(userWithBetaRole);
        when(userDao.getUser(userWithoutBetaRole.getUsername())).thenReturn(userWithoutBetaRole);
    }

    @Test
    public void getUserFeatures_UserIsNotAuthorizedTest() {
        givenUserIsUnauthorized();
        thenOnlyEnabledPublicFeaturesAreReturned();
    }

    @Test
    public void getUserFeatures_UserIsAuthorizedNoBetaRoleTest() {
        givenUserWithoutBetaRoleIsLoggedIn();
        //enabled features are configured by user (including beta features), but result
        // doesn't include beta ones - as user does not have a corresponding role
        thenResultContainsOnlyEnabledPrivateFeatures();
    }

    @Test
    public void getUserFeatures_UserIsAuthorizedNoBetaRole_NoAnyFeaturesConfiguredTest() {
        givenUserWithoutAnyConfiguredFeaturesIsLoggedIn();
        //no features are configured by user
        //so result contains the list of public features
        thenOnlyEnabledPublicFeaturesAreReturned();
    }

    @Test
    public void getUserFeatures_UserIsAuthorizedIncludeBetaTest() {
        givenUserWithBetaRoleIsLoggedIn();

        List<FeatureDto> result = featuresController.getUserFeatures();

        //enabled features are configured by user (including beta features),
        // result includes beta features - as user does have a corresponding role
        assertEquals(4, result.size());
        thenResultContainsEnabledPrivateFeatures(result);
        thenResultContainsEnabledPrivateBetaFeatures(result);
    }

    @Test
    public void getUserFeatures_UserIsNotFoundTest() {
        authorizeNotExistingUser();
        thenOnlyEnabledPublicFeaturesAreReturned();
    }

    @Test
    public void getUserFeed_UserIsNotAuthorizedTest() {
        givenUserIsUnauthorized();
        thenOnlyEnabledPublicEventFeedIsReturned();
    }

    @Test
    public void getUserFeed_UserIsAuthorizedNoBetaRoleTest() {
        givenUserWithoutBetaRoleIsLoggedIn();
        //two feeds are configured by user - which is incorrect, but beta are not allowed
        thenOnlyEnabledPrivateEventFeedIsReturned();
    }

    @Test
    public void getUserFeed_UserIsAuthorizedIncludeBetaTest() {
        givenUserWithBetaRoleIsLoggedIn();
        //two feeds are configured by user - which is incorrect, but non-beta should go first
        thenOnlyEnabledPrivateEventFeedIsReturned();
    }

    @Test
    public void getUserFeed_UserIsNotFoundTest() {
        authorizeNotExistingUser();
        thenOnlyEnabledPublicEventFeedIsReturned();
    }


    private void givenUserWithBetaRoleIsLoggedIn() {
        mockAuthWithClaims(List.of(Role.Names.BETA_FEATURES), userWithBetaRole.getUsername());
        userWithBetaRole.setFeaturesEnabledByUser(List.of(
            disabledBetaFeature, enabledBetaFeature,
            disabledPrivateFeature, enabledPrivateFeature,
            disabledPrivateEventFeed, enabledPrivateEventFeed,
            disabledBetaEventFeed, enabledBetaEventFeed
        ));
    }

    private void authorizeNotExistingUser() {
        String notExistingUsername =
            "some-other-username"; //userDao mock returns null for it = user not found
        mockAuthWithClaims(List.of("nothing"), notExistingUsername);
    }

    private void givenUserWithoutBetaRoleIsLoggedIn() {
        mockAuthWithClaims(List.of("nothing"), userWithoutBetaRole.getUsername());
        userWithoutBetaRole.setFeaturesEnabledByUser(List.of(
            disabledBetaFeature, enabledBetaFeature,
            disabledPrivateFeature, enabledPrivateFeature,
            disabledPrivateEventFeed, enabledPrivateEventFeed,
            disabledBetaEventFeed, enabledBetaEventFeed
        ));
    }

    private void givenUserWithoutAnyConfiguredFeaturesIsLoggedIn() {
        mockAuthWithClaims(List.of("nothing"), userWithoutBetaRole.getUsername());
        userWithoutBetaRole.setFeaturesEnabledByUser(List.of());
    }

    private void givenUserIsUnauthorized() {
        mockAuthWithClaims(List.of("no appropriate roles"), null);
    }

    private FeatureDto findDtoInResult(List<FeatureDto> featureDtos, String name) {
        Optional<FeatureDto> enabledPrivateFeatureDto = featureDtos.stream()
            .filter(it -> name.equals(it.getName()))
            .findAny();
        return enabledPrivateFeatureDto.get();
    }

    private void thenOnlyEnabledPublicFeaturesAreReturned() {
        List<FeatureDto> result = featuresController.getUserFeatures();
        assertEquals(2, result.size());

        FeatureDto enabledPublicFeatureDto =
            findDtoInResult(result, enabledPublicFeature.getName());
        assertDto(enabledPublicFeature, enabledPublicFeatureDto);

        FeatureDto enabledPublicEventFeedDto =
            findDtoInResult(result, enabledPublicEventFeed.getName());
        assertDto(enabledPublicEventFeed, enabledPublicEventFeedDto);
    }

    private void thenOnlyEnabledPrivateEventFeedIsReturned() {
        String result = featuresController.getDefaultUserEventFeed();
        assertEquals(enabledPrivateEventFeed.getName(), result);
    }

    private void thenOnlyEnabledPublicEventFeedIsReturned() {
        String result = featuresController.getDefaultUserEventFeed();
        assertEquals(enabledPublicEventFeed.getName(), result);
    }

    private void thenResultContainsOnlyEnabledPrivateFeatures() {
        List<FeatureDto> result = featuresController.getUserFeatures();
        assertEquals(2, result.size());
        thenResultContainsEnabledPrivateFeatures(result);
    }

    private void thenResultContainsEnabledPrivateFeatures(List<FeatureDto> result) {
        FeatureDto enabledPrivateFeatureDto =
            findDtoInResult(result, enabledPrivateFeature.getName());
        assertDto(enabledPrivateFeature, enabledPrivateFeatureDto);

        FeatureDto enabledPrivateEventFeedDto =
            findDtoInResult(result, enabledPrivateEventFeed.getName());
        assertDto(enabledPrivateEventFeed, enabledPrivateEventFeedDto);
    }

    private void thenResultContainsEnabledPrivateBetaFeatures(List<FeatureDto> result) {
        FeatureDto enabledBetaFeatureDto = findDtoInResult(result, enabledBetaFeature.getName());
        assertDto(enabledBetaFeature, enabledBetaFeatureDto);

        FeatureDto enabledBetaEventFeedDto =
            findDtoInResult(result, enabledBetaEventFeed.getName());
        assertDto(enabledBetaEventFeed, enabledBetaEventFeedDto);
    }

    private void assertDto(Feature source, FeatureDto dto) {
        assertEquals(source.getName(), dto.getName());
        assertEquals(source.getDescription(), dto.getDescription());
        assertEquals(source.getType().name(), dto.getType());
    }
}
