package io.kontur.userprofile.model.entity;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.type.SqlTypes;
import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "app_feature")
@Getter
@NoArgsConstructor
public class AppFeature {

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private App app;

    @ManyToOne
    @JoinColumn(name = "feature_id", insertable = false, updatable = false)
    private Feature feature;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "configuration", columnDefinition = "json")
    private JsonNode configuration;

    public AppFeature(@NotNull App app, @NotNull Feature feature, JsonNode configuration) {
        this.app = app;
        this.feature = feature;
        this.configuration = configuration;

        setKeyComponents(app, feature); //required to set manually
    }

    public void setConfiguration(JsonNode configuration) {
        this.configuration = configuration;
    }

    private void setKeyComponents(@NotNull App app, @NotNull Feature feature) {
        this.id.appId = app.getId();
        this.id.featureId = feature.getId();
    }

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class Id implements Serializable {
        @Column(name = "app_id")
        private UUID appId;
        @Column(name = "feature_id")
        private long featureId;
    }
}
