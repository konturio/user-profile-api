package io.kontur.userprofile;

import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.Role;
import io.kontur.userprofile.model.entity.User;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import java.util.Set;

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

    public static Feature createEnabledPublicEventFeed() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("enabledPublicEventFeed");
        feature.setBeta(false);
        feature.setEnabled(true);
        feature.setDescription("Public event feed");
        feature.setType(FeatureType.EVENT_FEED);
        feature.setPublic(true);
        return feature;
    }

    public static Feature createDisabledPublicEventFeed() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("disabledPublicEventFeed");
        feature.setBeta(false);
        feature.setEnabled(false);
        feature.setDescription("Public event feed - disabled");
        feature.setType(FeatureType.EVENT_FEED);
        feature.setPublic(true);
        return feature;
    }

    public static Feature createEnabledPrivateEventFeed() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("enabledPrivateEventFeed");
        feature.setBeta(false);
        feature.setEnabled(true);
        feature.setDescription("Private event feed");
        feature.setType(FeatureType.EVENT_FEED);
        feature.setPublic(false);
        return feature;
    }

    public static Feature createDisabledPrivateEventFeed() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("disabledPrivateEventFeed");
        feature.setBeta(false);
        feature.setEnabled(false);
        feature.setDescription("Private event feed - disabled");
        feature.setType(FeatureType.EVENT_FEED);
        feature.setPublic(false);
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
        feature.setPublic(false);
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
        feature.setPublic(false);
        return feature;
    }

    public static Feature createDisabledPublicFeature() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("disabledPublicFeature");
        feature.setBeta(false);
        feature.setEnabled(false);
        feature.setDescription("Feature three - disabled public one");
        feature.setType(FeatureType.BIVARIATE_LAYER);
        feature.setPublic(true);
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
        feature.setPublic(false);
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
        feature.setPublic(false);
        return feature;
    }

    public static Feature createEnabledFeature() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("enabledPrivateFeature");
        feature.setBeta(false);
        feature.setEnabled(true);
        feature.setDescription("Feature six - enabled normal one");
        feature.setType(FeatureType.BIVARIATE_LAYER);
        feature.setPublic(false);
        return feature;
    }

    public static Feature createEnabledPublicFeature() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("enabledPublicFeature");
        feature.setBeta(false);
        feature.setEnabled(true);
        feature.setDescription("Feature six - enabled normal one");
        feature.setType(FeatureType.BIVARIATE_LAYER);
        feature.setPublic(true);
        return feature;
    }

    public static Feature createDisabledFeature() {
        Feature feature = new Feature();
        feature.setId(nextId());
        feature.setName("disabledPrivateFeature");
        feature.setBeta(false);
        feature.setEnabled(false);
        feature.setDescription("Feature seven - disabled normal one");
        feature.setType(FeatureType.BIVARIATE_LAYER);
        feature.setPublic(false);
        return feature;
    }

    private static long nextId() {
        return id++;
    }
}
