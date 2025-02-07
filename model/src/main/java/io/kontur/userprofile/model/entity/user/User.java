package io.kontur.userprofile.model.entity.user;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "full_name")
    private String fullName;
    private String language;
    @Column(name = "use_metric_units")
    private boolean useMetricUnits;
    private String bio;
    @Column(name = "osm_editor")
    private String osmEditor;
    @Column(name = "default_feed")
    private String defaultFeed;
    private String theme;
    private String linkedin;
    private String phone;
    @Column(name = "call_consent_given")
    private boolean callConsentGiven;
    @Builder.Default
    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();;
    @Column(name = "account_notes")
    private String accountNotes;
    private String objectives;
    @Column(name = "company_name")
    private String companyName;
    private String position;
    @Column(name = "amount_of_gis")
    private String amountOfGis;
}
