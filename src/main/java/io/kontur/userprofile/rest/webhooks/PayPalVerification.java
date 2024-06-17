package io.kontur.userprofile.rest.webhooks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpHeaders;

import lombok.Data;

/**
 * PayPalVerification is a morally private DTO class
 * made with the sole purpose of simplifying the code and isn't meant
 * to be employed elsewhere. It was made public for testing.
 */
@Data
public class PayPalVerification {
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

    public static PayPalVerification of(final HttpHeaders headers, final String webhookID) {
        final String authAlgo = headers.getOrEmpty("Paypal-Auth-Algo").get(0);
        final String certURL = headers.getOrEmpty("Paypal-Cert-Url").get(0);
        final String transmissionID = headers.getOrEmpty("Paypal-Transmission-Id").get(0);
        final String transmissionSig = headers.getOrEmpty("Paypal-Transmission-Sig").get(0);
        final String transmissionTime = headers.getOrEmpty("Paypal-Transmission-Time").get(0);
        return new PayPalVerification(authAlgo, certURL, transmissionID, transmissionSig, transmissionTime,
                webhookID);
    }
}
