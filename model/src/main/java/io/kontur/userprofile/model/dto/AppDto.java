package io.kontur.userprofile.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.kontur.userprofile.model.entity.App;
import io.kontur.userprofile.model.entity.Feature;
import io.kontur.userprofile.model.validation.ValidExtent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

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
    private Map<String, JsonNode> featuresConfig = new HashMap<>();
    @ValidExtent
    private List<BigDecimal> extent;
    private String sidebarIconUrl;
    private String faviconUrl;
    private JsonNode faviconPack;
    private List<String> domains;

    public static AppDto fromEntities(App app, Map<Feature, JsonNode> appFeatureConfigurations, boolean ownedByUser) {
        Map<String, JsonNode> featuresConfig = new HashMap<>();
        appFeatureConfigurations.forEach((key, value) -> featuresConfig.put(key.getName(), value));
        return new AppDto(app.getId(), app.getName(), app.getDescription(), app.isPublic(), ownedByUser, featuresConfig,
                app.getExtent(), app.getSidebarIconUrl(), app.getFaviconUrl(), app.getFaviconPack(), app.getDomains());
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
                Objects.equals(featuresConfig, appDto.featuresConfig) &&
                Objects.equals(extent, appDto.extent) &&
                Objects.equals(sidebarIconUrl, appDto.sidebarIconUrl) &&
                Objects.equals(faviconUrl, appDto.faviconUrl) &&
                Objects.equals(faviconPack, appDto.faviconPack) &&
                Objects.equals(domains, appDto.domains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isPublic, ownedByUser, featuresConfig, extent, sidebarIconUrl,
                faviconUrl, faviconPack, domains);
    }
}
