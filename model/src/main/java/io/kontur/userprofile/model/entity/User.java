package io.kontur.userprofile.model.entity;

import java.util.List;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USERS")
@SecondaryTable(name = "SUBSCRIPTION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    @Id
    @GeneratedValue(generator = "user-sequence-generator", strategy = GenerationType.AUTO)
    @GenericGenerator(
        name = "user-sequence-generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "user_sequence"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1")
        }
    )
    protected Long id;

    @Embedded
    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    protected Set<Role> roles;
    
    @ManyToMany
    @JoinTable(name = "USER_FEATURE",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "FEATURE_ID"))
    protected List<Feature> featuresEnabledByUser;

    @Column(name = "subscribed_to_kontur_updates")
    protected boolean subscribedToKonturUpdates;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "enabled", column = @Column(name = "enabled", table = "SUBSCRIPTION")),
        @AttributeOverride(name = "eventFeed", column = @Column(name = "event_feed", table = "SUBSCRIPTION")),
        @AttributeOverride(name = "bbox", column = @Column(name = "bbox", table = "SUBSCRIPTION")),
        @AttributeOverride(name = "type", column = @Column(name = "type", table = "SUBSCRIPTION")),
        @AttributeOverride(name = "severity", column = @Column(name = "severity", table = "SUBSCRIPTION")),
        @AttributeOverride(name = "people", column = @Column(name = "people", table = "SUBSCRIPTION")),
    })
    protected Subscription subscription;
    @Embedded
    @ElementCollection
    @CollectionTable(name = "user_groups", joinColumns = @JoinColumn(name = "user_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    protected Set<Group> groups;
    @Column(unique = true)
    @NotNull
    private String username;
    @Column(unique = true)
    @NotNull
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String language;
    @Column(name = "use_metric_units")
    private boolean useMetricUnits;
}
