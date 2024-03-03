package io.kontur.userprofile;

import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import io.kontur.userprofile.model.entity.user.Role;
import io.kontur.userprofile.model.entity.user.User;
import java.util.Set;
import java.util.UUID;

public class TestDataFactory {

    private static long id = 1L;

    public static User userWithBetaRole() {
        Role betaRole = new Role();
        betaRole.setId("some-keycloak-id");
        betaRole.setName(Role.Names.BETA_FEATURES);

        return User.builder()
            .username("userWithBetaRole")
            .roles(Set.of(betaRole))
            .build();
    }

    public static User userWithoutBetaRole() {
        return User.builder()
            .username("userWithoutBetaRole")
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

    public static Feature createDisabledEventFeed() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("disabledEventFeed");
        feature.setBeta(false);
        feature.setEnabled(false);
        feature.setDescription("Disabled event feed - disabled");
        feature.setType(FeatureType.EVENT_FEED);
        return feature;
    }

    public static Feature createEnabledBetaEventFeed() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("enabledBetaEventFeed");
        feature.setBeta(true);
        feature.setEnabled(true);
        feature.setDescription("Beta event feed");
        feature.setType(FeatureType.EVENT_FEED);
        return feature;
    }

    public static Feature createDisabledBetaEventFeed() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("disabledBetaEventFeed");
        feature.setBeta(true);
        feature.setEnabled(false);
        feature.setDescription("Beta event feed - disabled");
        feature.setType(FeatureType.EVENT_FEED);
        return feature;
    }

    public static Feature createEnabledBetaFeature() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("enabledBetaFeature");
        feature.setBeta(true);
        feature.setEnabled(true);
        feature.setDescription("Feature four - enabled beta one");
        feature.setType(FeatureType.LAYER);
        return feature;
    }

    public static Feature createDisabledBetaFeature() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("disabledBetaFeature");
        feature.setBeta(true);
        feature.setEnabled(false);
        feature.setDescription("Feature five - disabled beta one");
        feature.setType(FeatureType.BIVARIATE_LAYER);
        return feature;
    }

    public static Feature createEnabledFeature() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("enabledFeature");
        feature.setBeta(false);
        feature.setEnabled(true);
        feature.setDescription("Feature six - enabled normal one");
        feature.setType(FeatureType.BIVARIATE_LAYER);
        return feature;
    }

    public static Feature createDisabledFeature() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("disabledFeature");
        feature.setBeta(false);
        feature.setEnabled(false);
        feature.setDescription("Feature seven - disabled normal one");
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
