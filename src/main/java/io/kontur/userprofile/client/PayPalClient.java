package io.kontur.userprofile.client;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PayPalClient {
    private final RestTemplate paypalAuthorizationRestTemplate;

    @Value("${payments.paypal.host}")
    private String paypalHost;

    @Value("${payments.paypal.client-id}")
    private String paypalClientId;

    @Value("${payments.paypal.client-secret}")
    private String paypalClientSecret;


    public PayPalTokenResponse getToken() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("client_id", paypalClientId);
        params.add("grant_type", "client_credentials"); // TODO: what's the right type?

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(paypalClientId, paypalClientSecret);


        ResponseEntity<PayPalTokenResponse> response = paypalAuthorizationRestTemplate
                .exchange(String.format("%s/v1/oauth2/token", paypalHost),
                    HttpMethod.POST, new HttpEntity<>(params, headers), new ParameterizedTypeReference<>() {});

        return Objects.requireNonNull(response.getBody());
    }
}
