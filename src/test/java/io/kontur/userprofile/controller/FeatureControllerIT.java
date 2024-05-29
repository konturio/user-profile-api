package io.kontur.userprofile.controller;

import static io.kontur.userprofile.service.AppService.DN2_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kontur.userprofile.AbstractIT;
import io.kontur.userprofile.model.dto.FeatureDto;
import io.kontur.userprofile.rest.FeaturesController;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class FeatureControllerIT extends AbstractIT {
    @Autowired
    FeaturesController featuresController;

    @Test
    public void dn2DefaultFeedIdAvailableToUnauthorizedUser() {
        givenUserIsNotAuthenticated();

        String defaultFeed = featuresController.getDefaultUserEventFeed();
        assertEquals("kontur-public", defaultFeed);
    }

    @Test
    public void dn2DefaultFeaturesAreAvailableToUnauthorizedUser() {
        givenUserIsNotAuthenticated();

        List<FeatureDto> features = featuresController.getUserFeatures(DN2_ID);
        assertEquals(28, features.size()); //just some features
    }
}
