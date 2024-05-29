package io.kontur.userprofile;

import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import io.kontur.userprofile.model.entity.user.User;
import java.util.Set;
import java.util.UUID;

public class TestDataFactory {

    private static long id = 1L;

    public static User defaultUser() {
        return User.builder()
            .username("defaultUser")
            .roles(Set.of())
            .build();
    }

    public static Feature createEnabledEventFeed() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("enabledEventFeed");
        feature.setBeta(false);
        feature.setEnabled(true);
        feature.setDescription("Enmabled event feed");
        feature.setType(FeatureType.EVENT_FEED);
        return feature;
    }

    public static Feature createEnabledFeature(String name) {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName(name);
        feature.setBeta(false);
        feature.setEnabled(true);
        feature.setDescription("Feature six - enabled normal one");
        feature.setType(FeatureType.BIVARIATE_LAYER);
        return feature;
    }

    public static App privateAppOwnedBy(String ownerUsername) {
        UUID id = UUID.randomUUID();
        App app = new App();
        app.setId(id);
        app.setName("app " + id);
        app.setDescription("app desc " + id);
        app.setPublic(false);
        if (ownerUsername != null) {
            User owner = User.builder().username(ownerUsername).build();
            app.setOwner(owner);
        }
        return app;
    }

    public static App publicAppOwnedBy(String ownerUsername) {
        UUID id = UUID.randomUUID();
        App app = new App();
        app.setId(id);
        app.setName("app " + id);
        app.setDescription("app desc " + id);
        app.setPublic(true);
        if (ownerUsername != null) {
            User owner = User.builder().username(ownerUsername).build();
            app.setOwner(owner);
        }
        return app;
    }

    public static long nextId() {
        return id++;
    }
}
