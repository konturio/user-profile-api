package io.kontur.userprofile.model.entity;

import io.kontur.userprofile.model.entity.enums.FeatureType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FEATURE")
public class Feature { //use subclasses by type, when required
    @Id
    @GeneratedValue(generator = "feature-sequence-generator", strategy = GenerationType.AUTO)
    @GenericGenerator(
        name = "feature-sequence-generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM,
                value = "feature_sequence"),
            @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INITIAL_PARAM,
                value = "1"),
            @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM,
                value = "1")
        }
    )
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
