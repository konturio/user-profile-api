package io.kontur.userprofile.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kontur.userprofile.model.dto.AppSummaryDto;
import io.kontur.userprofile.model.entity.App;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AppControllerGetListTest extends AbstractAppControllerTest {

    @Test
    public void publicAppsAreAvailableToNonAuthenticatedUser() {
        givenUserIsNotAuthenticated();

        List<AppSummaryDto> result = controller.getList();

        thenListConsistsOfSummariesFor(result, publicAppWithoutOwner,
            publicAppOwnedByUsername1);
    }

    @Test
    public void publicAppsAreAvailableToNonOwnerUser() {
        givenUserIsAuthenticated("username2");

        List<AppSummaryDto> result = controller.getList();

        thenListConsistsOfSummariesFor(result, publicAppWithoutOwner,
            publicAppOwnedByUsername1);
    }

    @Test
    public void publicAndPrivateAppsAreAvailableToOwnerUser() {
        givenUserIsAuthenticated(username1);

        List<AppSummaryDto> result = controller.getList();

        thenListConsistsOfSummariesFor(result, publicAppWithoutOwner,
            publicAppOwnedByUsername1, privateAppOwnedByUsername1);
    }

    private void thenListConsistsOfSummariesFor(List<AppSummaryDto> result, App... apps) {
        List<App> sourceApps = List.of(apps);

        assertEquals(sourceApps.size(), result.size());

        for (App app : sourceApps) {
            AppSummaryDto actual =
                result.stream().filter(it -> it.getId().equals(app.getId())).findAny().get();
            assertEquals(app.getName(), actual.getName());
        }
    }

}
