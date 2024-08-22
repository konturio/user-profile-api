package io.kontur.userprofile.service;

import org.springframework.stereotype.Service;

import io.kontur.userprofile.client.PayPalAPIClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayPalAPIService {
    private final PayPalAPIClient payPalClient;

    public boolean subscriptionIdIsValid(String subscriptionId) {
        var resp = payPalClient.getBillingSubscriptionDetails(subscriptionId);

        if (resp.getStatusCode().is2xxSuccessful()) return true;

        return false;
    }
}
