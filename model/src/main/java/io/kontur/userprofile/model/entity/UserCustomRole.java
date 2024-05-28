package io.kontur.userprofile.model.entity;

import io.kontur.userprofile.model.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_custom_role", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "role_id", "subscription_id", "started_at", "ended_at"})
})
@Getter
@NoArgsConstructor
public class UserCustomRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private CustomRole role;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (subscriptionId != null && (startedAt == null || endedAt == null)) {
            throw new IllegalStateException("Both started_at and ended_at must be set if subscription_id is not null");
        }
    }

    public UserCustomRole(@NotNull User user, @NotNull CustomRole role, String subscriptionId, OffsetDateTime startedAt, OffsetDateTime endedAt) {
        this.user = user;
        this.role = role;
        this.subscriptionId = subscriptionId;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
}
