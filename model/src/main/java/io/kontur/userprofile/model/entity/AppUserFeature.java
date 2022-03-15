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
@Table(name = "app_user_feature")
@Getter
@NoArgsConstructor
public class AppUserFeature {

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private App app;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "feature_id", insertable = false, updatable = false)
    private Feature feature;

    public AppUserFeature(@NotNull App app, @NotNull User user, @NotNull Feature feature) {
        this.app = app;
        this.user = user;
        this.feature = feature;

        setKeyComponents(app, user, feature); //required to set manually
    }

    private void setKeyComponents(@NotNull App app, @NotNull User user, @NotNull Feature feature) {
        this.id.appId = app.getId();
        this.id.featureId = feature.getId();
        this.id.userId = user.getId();
    }

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class Id implements Serializable {
        @Column(name = "app_id")
        private UUID appId;
        @Column(name = "user_id")
        private long userId;
        @Column(name = "feature_id")
        private long featureId;
    }
}
