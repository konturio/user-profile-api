package io.kontur.userprofile.service;

import org.springframework.stereotype.Service;

import io.kontur.userprofile.client.PayPalAPIClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayPalAPIService {
    private final PayPalAPIClient payPalClient;

    public boolean subscriptionIdIsValid(String subscriptionId, String expectedPlanId, String expectedCustomId) {
        var resp = payPalClient.getBillingSubscriptionDetails(subscriptionId);

        if (!resp.getStatusCode().is2xxSuccessful()) {
            return false;
        }

        var details = resp.getBody();
        if (details == null) {
            return false;
        }

        if (expectedPlanId != null && !expectedPlanId.equals(details.getPlanId())) {
            return false;
        }

        if (expectedCustomId != null && !expectedCustomId.equals(details.getCustomId())) {
            return false;
        }

        String status = details.getStatus();
        if (status == null) {
            return false;
        }

        return ("ACTIVE".equals(status) || "APPROVED".equals(status));
    }
}
