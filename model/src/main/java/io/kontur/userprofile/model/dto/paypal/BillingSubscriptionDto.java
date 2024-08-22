package io.kontur.userprofile.model.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for PayPal subscriptions with the most useful fields (there's more).
 */
@Data
@NoArgsConstructor
public class BillingSubscriptionDto {
    private String id;
    @JsonProperty("custom_id")
    private String customId; // Whatever the Front End sets for us
    @JsonProperty("plan_id")
    private String planId;
    private String quantity;
    @JsonProperty("shipping_amount")
    private ShippingAmountDto shippingAmount;
    @JsonProperty("create_time")
    private String createTime;
    @JsonProperty("update_time")
    private String updateTime;
    @JsonProperty("start_time")
    private String startTime;
    private String status;
    @JsonProperty("status_update_time")
    private String statusUpdateTime;
}
