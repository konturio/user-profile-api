package io.kontur.userprofile.rest.webhooks;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;


/**
 * PayPalVerification is a package-only (morally private) DTO class
 * made with the sole purpose of simplifying the code and isn't meant
 * to be employed elsewhere.
 */
@Data
class PayPalVerification {
    private final String auth_algo;
    private final String cert_url;
    private final String transmission_id;
    private final String transmission_sig;
    private final String transmission_time;
    private final String webhook_id;
    private JsonNode webhook_event; // to be set later

    public static PayPalVerification of(final HttpHeaders headers, final String webhook_id) {
        final String auth_algo = headers.getOrEmpty("Paypal-Auth-Algo").get(0);
        final String cert_url = headers.getOrEmpty("Paypal-Cert-Url").get(0);
        final String transmission_id = headers.getOrEmpty("Paypal-Transmission-Id").get(0);
        final String transmission_sig = headers.getOrEmpty("Paypal-Transmission-Sig").get(0);
        final String transmission_time = headers.getOrEmpty("Paypal-Transmission-Time").get(0);
        return new PayPalVerification(auth_algo, cert_url, transmission_id, transmission_sig, transmission_time, webhook_id);
    }
}
