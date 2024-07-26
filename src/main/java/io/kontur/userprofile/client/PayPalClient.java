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
import org.springframework.web.client.RestTemplate;

import io.kontur.userprofile.model.dto.paypal.PayPalTokenResponseDto;
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


    public PayPalTokenResponseDto getToken() {
        final String params = "grant_type=client_credentials";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(paypalClientId, paypalClientSecret);


        ResponseEntity<PayPalTokenResponseDto> response = paypalAuthorizationRestTemplate
                .exchange(String.format("%s/v1/oauth2/token", paypalHost),
                    HttpMethod.POST, new HttpEntity<>(params, headers), new ParameterizedTypeReference<>() {});

        return Objects.requireNonNull(response.getBody());
    }
}
