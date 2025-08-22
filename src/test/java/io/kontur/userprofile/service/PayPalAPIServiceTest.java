package io.kontur.userprofile.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import io.kontur.userprofile.client.PayPalAPIClient;
import io.kontur.userprofile.model.dto.paypal.BillingSubscriptionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PayPalAPIServiceTest {
    @Mock
    private PayPalAPIClient client;

    @InjectMocks
    private PayPalAPIService service;

    private BillingSubscriptionDto subscription;

    @BeforeEach
    void setUp() {
        subscription = new BillingSubscriptionDto();
        subscription.setId("sub1");
        subscription.setPlanId("plan1");
        subscription.setCustomId("user1");
        subscription.setStatus("ACTIVE");
    }

    @Test
    void validSubscriptionReturnsTrue() {
        when(client.getBillingSubscriptionDetails("sub1"))
                .thenReturn(new ResponseEntity<>(subscription, HttpStatus.OK));

        boolean result = service.subscriptionIdIsValid("sub1", "plan1", "user1");
        assertTrue(result);
    }

    @Test
    void invalidStatusReturnsFalse() {
        subscription.setStatus("SUSPENDED");
        when(client.getBillingSubscriptionDetails("sub1"))
                .thenReturn(new ResponseEntity<>(subscription, HttpStatus.OK));

        boolean result = service.subscriptionIdIsValid("sub1", "plan1", "user1");
        assertFalse(result);
    }
}
