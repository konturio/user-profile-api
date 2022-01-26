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
    @Column(name = "is_public")
    @NotNull
    private boolean isPublic;

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

    /**
     * public features are shown to anyone regardless of roles.
     */
    public boolean isPublic() {
        return isPublic;
    }

    public static class Names {
        public static final String ANALYTICS_PANEL = "analytics_panel";
        public static final String EVENTS_LIST = "events_list";
        public static final String MAP_LAYERS_PANEL = "map_layers_panel";
        public static final String SIDE_BAR = "side_bar";
        public static final String BIVARIATE_MANAGER = "bivariate_manager";
        public static final String CURRENT_EVENT = "current_event";
        public static final String FOCUSED_GEOMETRY_LAYER = "focused_geometry_layer";
        public static final String LAYERS_IN_AREA = "layers_in_area";
        public static final String MAP_RULER = "map_ruler";
        public static final String TOASTS = "toasts";
        public static final String BOUNDARY_SELECTOR = "boundary_selector";
        public static final String DRAW_TOOLS = "draw_tools";
        public static final String GEOMETRY_UPLOADER = "geometry_uploader";
        public static final String LEGEND_PANEL = "legend_panel";
        public static final String REPORTS = "reports";
        public static final String URL_STORE = "url_store";

        public static final String INTERACTIVE_MAP = "interactive_map";
        public static final String CURRENT_EPISODE = "current_episode";
        public static final String GEOCODER = "geocoder";
        public static final String EPISODE_LIST = "episode_list";
        public static final String COMMUNITIES = "communities";
        public static final String FEATURE_SETTINGS = "feature_settings";
    }
}
