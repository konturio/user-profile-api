package io.kontur.userprofile.model.entity;

import io.kontur.userprofile.model.entity.user.Role;
import io.kontur.userprofile.model.entity.user.User;
import io.kontur.userprofile.model.entity.enums.FeatureType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FEATURE")
public class Feature { //use subclasses by type, when required
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotNull
    private String name;
    @NotNull
    private boolean beta;
    @NotNull
    private boolean enabled;
    private String description;
    @Type(type = "io.kontur.userprofile.model.converters.PostgreSqlEnumType")
    @Enumerated(EnumType.STRING)
    @Column(name = "featuretype")
    @NotNull
    private FeatureType type;
    @Column(name = "default_for_user_apps")
    private boolean defaultForUserApps;
    @Column(name = "available_for_user_apps")
    private boolean availableForUserApps;

    /**
     * beta features are shown only to {@link User} having {@link Role.Names#BETA_FEATURES}.
     */
    public boolean isBeta() {
        return beta;
    }

    /**
     * disabled features are not shown to anyone.
     */
    public boolean isEnabled() {
        return enabled;
    }
}
