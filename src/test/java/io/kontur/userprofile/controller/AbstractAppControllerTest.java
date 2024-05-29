package io.kontur.userprofile.controller;

import static io.kontur.userprofile.TestDataFactory.privateAppOwnedBy;
import static io.kontur.userprofile.TestDataFactory.publicAppOwnedBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.kontur.userprofile.auth.AuthService;
import io.kontur.userprofile.dao.AppDao;
import io.kontur.userprofile.dao.CustomAppFeatureDao;
import io.kontur.userprofile.dao.FeatureDao;
import io.kontur.userprofile.model.dto.AppSummaryDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.rest.AppController;
import io.kontur.userprofile.service.AppService;
import io.kontur.userprofile.service.FeatureService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

public class AbstractAppControllerTest {
    final String username1 = "username1";
    final App publicAppWithoutOwner = publicAppOwnedBy(null);
    final App publicAppOwnedByUsername1 = publicAppOwnedBy(username1);
    final App privateAppWithoutOwner = privateAppOwnedBy(null);
    final App privateAppOwnedByUsername1 = privateAppOwnedBy(username1);
    @Mock
    AppDao appDao = mock(AppDao.class);
    @Mock
    FeatureDao featureDao = mock(FeatureDao.class);
    @Mock
    CustomAppFeatureDao customAppFeatureDao = mock(CustomAppFeatureDao.class);
    @Mock
    AuthService authService = mock(AuthService.class);
    @Mock
    FeatureService featureService = mock(FeatureService.class);
    AppService appService = new AppService(appDao, featureDao, customAppFeatureDao, featureService, authService);
    AppController controller = new AppController(appService, featureService, authService);

    @BeforeEach
    public void before() {
        givenAppExistsInDao(publicAppWithoutOwner);
        givenAppExistsInDao(publicAppOwnedByUsername1);
        givenAppExistsInDao(privateAppWithoutOwner);
        givenAppExistsInDao(privateAppOwnedByUsername1);

        givenPublicAppsExistInDao(publicAppWithoutOwner, publicAppOwnedByUsername1);
        givenAppsAreOwnedByUser(username1, publicAppOwnedByUsername1, privateAppOwnedByUsername1);
    }

    void givenUserIsNotAuthenticated() {
        when(authService.getCurrentUsername()).thenReturn(Optional.empty());
        when(authService.getCurrentUser()).thenReturn(Optional.empty());
    }

    void givenUserIsAuthenticated(String username) {
        when(authService.getCurrentUsername()).thenReturn(Optional.of(username));
        when(authService.getCurrentUser()).thenReturn(Optional.of(User.builder().username(username)
            .build()));
    }

    private void givenAppExistsInDao(App app) {
        when(appDao.getApp(app.getId())).thenReturn(app);
    }

    private void givenPublicAppsExistInDao(App... apps) {
        List<AppSummaryDto> toReturn = Arrays.stream(apps)
            .map(it -> new AppSummaryDto(it.getId(), it.getName()))
            .toList();
        when(appDao.getPublicAppsList()).thenReturn(toReturn);
    }

    private void givenAppsAreOwnedByUser(String ownerUsername, App... apps) {
        List<AppSummaryDto> toReturn = Arrays.stream(apps)
            .map(it -> new AppSummaryDto(it.getId(), it.getName()))
            .toList();
        when(appDao.getUserOwnedAppsList(ownerUsername))
            .thenReturn(toReturn);
    }

}
