package io.kontur.userprofile.model;

import io.kontur.userprofile.model.dto.FeatureDto;
import io.kontur.userprofile.model.dto.UserDto;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import io.kontur.userprofile.model.entity.user.Group;
import io.kontur.userprofile.model.entity.user.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConversionTest {

    @Test
    public void featureTest() {
        Feature first = new Feature();
        first.setName("feature_1");
        first.setDescription("Feature one");
        first.setType(FeatureType.UI_PANEL);

        FeatureDto firstDto = FeatureDto.fromEntity(first);

        assertEquals(first.getName(), firstDto.getName());
        assertEquals(first.getDescription(), firstDto.getDescription());
        assertEquals(first.getType(), firstDto.getType());
    }

    @Test
    public void userTest() {
        Feature someFeature = new Feature();
        someFeature.setId(1L);
        someFeature.setName("feature_1");
        someFeature.setDescription("Feature one");
        someFeature.setType(FeatureType.UI_PANEL);

        Group group = new Group("qwe-qwe-keycloak-id", "some name");

        User user = User.builder()
            .id(2L)
            .groups(Set.of(group))
            .email("qwe@test.com")
            .build();

        UserDto userDto = UserDto.fromEntity(user);
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getEmail(), userDto.getEmail());

        assertEquals(user.getLanguage(), userDto.getLanguage());
        assertEquals(user.isUseMetricUnits(), userDto.isUseMetricUnits());
        assertEquals(user.isSubscribedToKonturUpdates(), userDto.isSubscribedToKonturUpdates());
    }
}
