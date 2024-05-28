package io.kontur.userprofile.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.kontur.userprofile.model.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "custom_app_feature", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"app_id", "feature_id", "authenticated", "role_id", "configuration_for_user_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class CustomAppFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private App app;

    @ManyToOne
    @JoinColumn(name = "feature_id", insertable = false, updatable = false)
    private Feature feature;

    @Column(name = "authenticated", nullable = false)
    @NotNull
    private Boolean authenticated;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private CustomRole role;

    @ManyToOne
    @JoinColumn(name = "configuration_for_user_id", insertable = false, updatable = false)
    private User configurationForUser;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "configuration", columnDefinition = "json")
    private JsonNode configuration;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (!authenticated) {
            if (role != null || configurationForUser != null) {
                throw new IllegalStateException("When not authenticated, role_id and configuration_for_user_id must be null");
            }
        } else {
            if (role != null && configurationForUser != null) {
                throw new IllegalStateException("When authenticated, configuration_for_user_id can not be defined along with role_id");
            }
        }
    }

    public CustomAppFeature(@NotNull App app, @NotNull Feature feature, @NotNull Boolean authenticated, CustomRole role, User configurationForUser, JsonNode configuration) {
        this.app = app;
        this.feature = feature;
        this.authenticated = authenticated;
        this.role = role;
        this.configurationForUser = configurationForUser;
        this.configuration = configuration;
    }
}
