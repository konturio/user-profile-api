package io.kontur.userprofile.service;

import java.time.Instant;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import io.kontur.userprofile.client.PayPalClient;
import io.kontur.userprofile.model.dto.paypal.PayPalTokenResponseDto;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PayPalAuthorizationService {
    private static final Logger logger = Logger.getLogger(PayPalAuthorizationService.class);

    private static volatile PayPalTokenResponseDto tokenResponse = null;
    private static volatile Instant tokenExpiration = Instant.now();

    private final PayPalClient payPalClient;

    public String getAccessToken() {
        if (tokenResponse == null || tokenExpiration.isBefore(Instant.now())) {
            try {
                synchronized (this) {
                    if (tokenResponse == null || tokenExpiration.isBefore(Instant.now())) {
                        var token = payPalClient.getToken();
                        saveToken(token);
                    }
                }
            } catch (RestClientException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return tokenResponse.getAccessToken(); // FIXME: might be a stale token in a case of exception
    }

    private void saveToken(PayPalTokenResponseDto token) {
        Instant expiresAt = Instant.now().plusSeconds(token.lifetimeSeconds());
        tokenResponse = token;
        tokenExpiration = expiresAt.minusSeconds(30);
    }
}
