package io.kontur.userprofile.model.dto;

import io.kontur.userprofile.model.entity.UserBillingSubscription;
import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ActiveSubscriptionDto {
    private String id;
    private String billingPlanId;
    private String billingSubscriptionId;

    public ActiveSubscriptionDto(UserBillingSubscription userBillingSubscription) {
        this.id = userBillingSubscription.getBillingPlan().getRole().getName();
        this.billingPlanId = userBillingSubscription.getBillingPlan().getId();
        this.billingSubscriptionId = userBillingSubscription.getId();
    }
}
