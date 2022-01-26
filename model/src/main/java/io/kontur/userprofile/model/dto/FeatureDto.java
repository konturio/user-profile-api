package io.kontur.userprofile.model.dto;

import io.kontur.userprofile.model.entity.Feature;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FeatureDto {
    private final String name;
    private final String description;
    private final String type;

    public static FeatureDto fromEntity(Feature feature) {
        return feature == null ? null : new FeatureDto(feature.getName(), feature.getDescription(),
            feature.getType().name());
    }
}
