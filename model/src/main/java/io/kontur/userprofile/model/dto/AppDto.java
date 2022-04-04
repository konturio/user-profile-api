package io.kontur.userprofile.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kontur.userprofile.model.converters.GeoJsonUtils;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wololo.geojson.Geometry;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    private String name;
    private String description;
    private boolean isPublic;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean ownedByUser;
    private List<String> features;
    private Geometry centerGeometry;
    private BigDecimal zoom;

    public static AppDto fromEntities(App app, List<Feature> appFeatures, boolean ownedByUser) {
        Geometry geometryDto = app.getCenterGeometry() == null ? null :
            GeoJsonUtils.fromEntity(app.getCenterGeometry());
        return new AppDto(app.getId(), app.getName(), app.getDescription(), app.isPublic(),
            ownedByUser,
            appFeatures.stream().map(Feature::getName).toList(),
            geometryDto, app.getZoom());
    }

    @JsonIgnore
    public AppSummaryDto getSummary() {
        return new AppSummaryDto(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppDto appDto)) {
            return false;
        }
        return isPublic == appDto.isPublic &&
            Objects.equals(id, appDto.id) &&
            Objects.equals(name, appDto.name) &&
            Objects.equals(description, appDto.description) &&
            Objects.equals(ownedByUser, appDto.ownedByUser) &&
            Objects.equals(features, appDto.features) &&
            Objects.equals(zoom, appDto.zoom) &&
            GeoJsonUtils.geometriesAreEqual(centerGeometry, appDto.centerGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isPublic, ownedByUser, features, centerGeometry,
            zoom);
    }
}
