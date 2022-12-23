package io.kontur.userprofile.model.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Entity
@Table(name = "app_feature")
@Getter
@NoArgsConstructor
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class AppFeature {

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private App app;

    @ManyToOne
    @JoinColumn(name = "feature_id", insertable = false, updatable = false)
    private Feature feature;

    @Type(type = "jsonb")
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
