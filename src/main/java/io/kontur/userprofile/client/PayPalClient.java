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
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("client_id", paypalClientId);
        params.add("grant_type", "client_credentials"); // TODO: what's the right type?

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(paypalClientId, paypalClientSecret);


        ResponseEntity<PayPalTokenResponseDto> response = paypalAuthorizationRestTemplate
                .exchange(String.format("%s/v1/oauth2/token", paypalHost),
                    HttpMethod.POST, new HttpEntity<>(params, headers), new ParameterizedTypeReference<>() {});

        return Objects.requireNonNull(response.getBody());
    }
}
