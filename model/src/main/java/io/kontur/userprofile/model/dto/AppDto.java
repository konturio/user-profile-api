package io.kontur.userprofile.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kontur.userprofile.model.converters.GeoJsonUtils;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
    private Map<String, String> configurationByFeatureNames;
    private Geometry centerGeometry;
    private BigDecimal zoom;
    private String sidebarIconUrl;
    private String faviconUrl;

    public static AppDto fromEntities(App app, Map<Feature, String> appFeatureConfigurations, boolean ownedByUser) {
        Geometry geometryDto = app.getCenterGeometry() == null ? null :
                GeoJsonUtils.fromEntity(app.getCenterGeometry());
        Map<String, String> configurationByFeatureNames = new HashMap<>();
        appFeatureConfigurations.forEach((key, value) -> configurationByFeatureNames.put(key.getName(), value));
        return new AppDto(app.getId(), app.getName(), app.getDescription(), app.isPublic(), ownedByUser,
                configurationByFeatureNames, geometryDto, app.getZoom(), app.getSidebarIconUrl(), app.getFaviconUrl());
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
                Objects.equals(configurationByFeatureNames, appDto.configurationByFeatureNames) &&
                Objects.equals(zoom, appDto.zoom) &&
                Objects.equals(sidebarIconUrl, appDto.sidebarIconUrl) &&
                Objects.equals(faviconUrl, appDto.faviconUrl) &&
                GeoJsonUtils.geometriesAreEqual(centerGeometry, appDto.centerGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isPublic, ownedByUser, configurationByFeatureNames, centerGeometry,
                zoom, sidebarIconUrl, faviconUrl);
    }
}
