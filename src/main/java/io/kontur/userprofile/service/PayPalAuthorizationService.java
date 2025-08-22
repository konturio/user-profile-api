package io.kontur.userprofile.service;

import java.time.Instant;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import io.kontur.userprofile.client.PayPalAuthClient;
import io.kontur.userprofile.model.dto.paypal.PayPalTokenResponseDto;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PayPalAuthorizationService {
    private static final Logger logger = Logger.getLogger(PayPalAuthorizationService.class);

    private static volatile PayPalTokenResponseDto tokenResponse = null;
    private static volatile Instant tokenExpiration = Instant.now();

    private final PayPalAuthClient payPalClient;

    public String getAccessToken() {
        if (tokenResponse == null || tokenExpiration.isBefore(Instant.now())) {
            PayPalTokenResponseDto freshToken = null;
            try {
                // fetch new token outside synchronized block to avoid blocking
                freshToken = payPalClient.getToken();
            } catch (RestClientException e) {
                logger.warn(e.getMessage(), e);
            }

            if (freshToken != null) {
                synchronized (this) {
                    // double-check inside synchronized block
                    if (tokenResponse == null || tokenExpiration.isBefore(Instant.now())) {
                        saveToken(freshToken);
                    }
                }
            }
        }
        return tokenResponse != null ? tokenResponse.getAccessToken() : ""; // FIXME: might be a stale token in a case of exception
    }

    private void saveToken(PayPalTokenResponseDto token) {
        Instant expiresAt = Instant.now().plusSeconds(token.lifetimeSeconds());
        tokenResponse = token;
        tokenExpiration = expiresAt.minusSeconds(30);
    }
}
