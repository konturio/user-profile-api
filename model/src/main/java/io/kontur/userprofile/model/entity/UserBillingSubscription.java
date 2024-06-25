package io.kontur.userprofile.model.entity;

import io.kontur.userprofile.model.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_billing_subscription")
public class UserBillingSubscription {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "billing_plan_id")
    private BillingPlan billingPlan;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "active")
    @NotNull
    private Boolean active;

    @Column(name = "expired_at")
    private OffsetDateTime expiredAt;

    /**
     * Creates new active subscription
     */
    public UserBillingSubscription(String id, BillingPlan billingPlan, App app, User user) {
        this.id = id;
        this.billingPlan = billingPlan;
        this.app = app;
        this.user = user;
        this.active = true;
    }
}
