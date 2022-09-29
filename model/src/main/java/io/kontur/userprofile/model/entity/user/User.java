package io.kontur.userprofile.model.entity.user;

import java.util.HashSet;
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
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "USERS")
@SecondaryTable(name = "SUBSCRIPTION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    protected Set<Role> roles = new HashSet<>();

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
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "user_groups", joinColumns = @JoinColumn(name = "user_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    protected Set<Group> groups = new HashSet<>();
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
    @Column(name = "full_name")
    private String fullName;
    private String language;
    @Column(name = "use_metric_units")
    private boolean useMetricUnits;
    private String bio;
    @Column(name = "osm_editor")
    private String osmEditor;
    private String theme;
}
