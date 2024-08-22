package io.kontur.userprofile.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.kontur.userprofile.model.dto.paypal.BillingSubscriptionDto;
import io.kontur.userprofile.service.PayPalAuthorizationService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PayPalAPIClient {
    private static String SUBSCRIPTION_DETAILS_URL = "/v1/billing/subscriptions/{subscriptionId}";

    private final PayPalAuthorizationService paypalAuthService;
    private final RestTemplate paypalApiRestTemplate;

    @Value("${payments.paypal.host}")
    private String paypalHost;

    public ResponseEntity<BillingSubscriptionDto> getBillingSubscriptionDetails(String subscriptionId) {
        final String url = paypalHost + SUBSCRIPTION_DETAILS_URL;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // this should authenticate us with PayPal and get OAuth access token
        headers.setBearerAuth(paypalAuthService.getAccessToken());

        final HttpEntity<String> entity = new HttpEntity<>(headers);
        return paypalApiRestTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {}, subscriptionId);
    }
}
