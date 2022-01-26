package io.kontur.userprofile.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kontur.userprofile.model.dto.FeatureDto;
import io.kontur.userprofile.model.dto.UserDto;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.entity.Group;
import io.kontur.userprofile.model.entity.User;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

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
        assertEquals(first.getType().name(), firstDto.getType());
    }

    @Test
    public void userTest() {
        Feature someFeature = new Feature();
        someFeature.setName("feature_1");
        someFeature.setDescription("Feature one");
        someFeature.setType(FeatureType.UI_PANEL);

        Group group = new Group("qwe-qwe-keycloak-id", "some name");

        User user = User.builder()
            .id(2L)
            .groups(Set.of(group))
            .featuresEnabledByUser(List.of(someFeature))
            .email("qwe@test.com")
            .build();

        UserDto userDto = UserDto.fromEntity(user);
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());

        assertEquals(user.getLanguage(), userDto.getLanguage());
        assertEquals(user.isUseMetricUnits(), userDto.isUseMetricUnits());
        assertEquals(user.isSubscribedToKonturUpdates(), userDto.isSubscribedToKonturUpdates());
    }
}
