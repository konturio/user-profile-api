package io.kontur.userprofile.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kontur.userprofile.model.dto.AppDto;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.rest.exception.WebApplicationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class AppControllerGetTest extends AbstractAppControllerTest {

    @Test
    public void publicAppIsAvailableToNonAuthenticatedUser() {
        givenUserIsNotAuthenticated();

        AppDto result = controller.get(publicAppWithoutOwner.getId());

        thenDtoContainsIdNameAndDescription(result, publicAppWithoutOwner);
        thenDtoIsOwnedByUserIs(result, false);
    }

    @Test
    public void publicAppIsAvailableToAuthenticatedUser() {
        givenUserIsAuthenticated(username1);

        AppDto result = controller.get(publicAppWithoutOwner.getId());

        thenDtoContainsIdNameAndDescription(result, publicAppWithoutOwner);
        thenDtoIsOwnedByUserIs(result, false);
    }

    @Test
    public void publicAppIsAvailableToOwner() {
        givenUserIsAuthenticated(username1);

        AppDto result = controller.get(publicAppOwnedByUsername1.getId());

        thenDtoContainsIdNameAndDescription(result, publicAppOwnedByUsername1);
        thenDtoIsOwnedByUserIs(result, true);
    }

    @Test
    public void privateAppIsNotAvailableToNonAuthenticatedUser() {
        givenUserIsNotAuthenticated();

        try {
            controller.get(privateAppWithoutOwner.getId());
            throw new RuntimeException("expected exception was not thrown!");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void privateAppIsNotAvailableToNonOwnerUsers() {
        givenUserIsAuthenticated("username2");

        try {
            controller.get(privateAppWithoutOwner.getId());
            throw new RuntimeException("expected exception was not thrown!");
        } catch (WebApplicationException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void privateAppIsAvailableToOwner() {
        givenUserIsAuthenticated(username1);

        AppDto result = controller.get(privateAppOwnedByUsername1.getId());

        thenDtoContainsIdNameAndDescription(result, privateAppOwnedByUsername1);
        thenDtoIsOwnedByUserIs(result, true);
    }

    private void thenDtoIsOwnedByUserIs(AppDto dto, boolean value) {
        assertEquals(value, dto.getOwnedByUser());
    }

    private void thenDtoContainsIdNameAndDescription(AppDto dto, App app) {
        assertEquals(app.getId(), dto.getId());
        assertEquals(app.getName(), dto.getName());
        assertEquals(app.getDescription(), dto.getDescription());
    }


}
