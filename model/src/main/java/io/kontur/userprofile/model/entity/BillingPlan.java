package io.kontur.userprofile.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "billing_plan")
public class BillingPlan {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private CustomRole role;
}
