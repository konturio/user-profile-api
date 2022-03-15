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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public AppFeature(@NotNull App app, @NotNull Feature feature) {
        this.app = app;
        this.feature = feature;

        setKeyComponents(app, feature); //required to set manually
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
