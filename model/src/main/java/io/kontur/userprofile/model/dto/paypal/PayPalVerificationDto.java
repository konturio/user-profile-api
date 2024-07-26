package io.kontur.userprofile.model.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * PayPalVerification is a morally private DTO class
 * made with the sole purpose of simplifying the code and isn't meant
 * to be employed elsewhere. It was made public for testing.
 */
@Data
public class PayPalVerificationDto {
    @JsonProperty("auth_algo")
    private final String authAlgo;
    @JsonProperty("cert_url")
    private final String certURL;
    @JsonProperty("transmission_id")
    private final String transmissionID;
    @JsonProperty("transmission_sig")
    private final String transmissionSig;
    @JsonProperty("transmission_time")
    private final String transmissionTime;
    @JsonProperty("webhook_id")
    private final String webhookID;
    @JsonProperty("webhook_event")
    private JsonNode webhookEvent; // to be set later
}
