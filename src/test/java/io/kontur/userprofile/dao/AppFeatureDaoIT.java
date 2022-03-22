package io.kontur.userprofile.dao;

import static io.kontur.userprofile.service.FeatureService.DN2_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kontur.userprofile.controller.AbstractIT;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppFeatureDaoIT extends AbstractIT {
    @Autowired
    AppFeatureDao appFeatureDao;
    @Autowired
    AppDao appDao;

    @Test
    public void thereShouldBeNoBetaFeaturesInAppFeatures() {
        App dn2 = appDao.getApp(DN2_ID);

        Stream<Feature> dn2DefaultFeatures = appFeatureDao.getEnabledNonBetaAppFeaturesFor(dn2);
        assertTrue(dn2DefaultFeatures.noneMatch(Feature::isBeta));
    }
}
