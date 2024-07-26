package io.kontur.userprofile.rest.webhooks;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.kontur.userprofile.model.dto.paypal.PayPalVerificationDto;
import io.kontur.userprofile.service.PayPalAuthorizationService;
import io.kontur.userprofile.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;


/**
 * HooksController is meant to handle the Webhooks from a payment system.
 * Currently it's completely PayPal specific, but in the future might need
 * extending to other payment systems.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/hooks")
public class HooksController {
    private static final Logger log = Logger.getLogger(HooksController.class);

    // Presumably we'll need it to connect the user to the subscription (ID)
    // when we get a new subscription notification
    private final UserService userService;

    private final PayPalAuthorizationService paypalAuthService;

    private final RestTemplate paypalApiRestTemplate;

    @Value("${payments.paypal.host}")
    private String paypalHost;
    @Value("${payments.paypal.webhook-id}")
    private String webhookId;


    /**
     * We're going to register this endpoint to receive all the webhooks
     * related to subscriptions.
     *
     * @param headers necessary for validation
     * @param payload the webhook data
     * @return HTTP/1.0 200 OK no matter what
     */
    @Operation(summary = "Handle PayPal Webhooks related to subscription operations.")
    @PostMapping("/paypal/subscription")
    public ResponseEntity<?> handleSubscription(@RequestHeader HttpHeaders headers, @RequestBody JsonNode payload) {
        try {
            String eventType = payload.get("event_type").asText();
            log.info("Subscription webhook: got " + eventType);

            PayPalVerificationDto vo = payPalVerificationDto(headers, webhookId);
            vo.setWebhookEvent(payload);
            verifyWebhook(vo);

            final JsonNode resource = payload.get("resource");
            final String subscriptionId = resource.get("id").asText();
            final String planId = resource.get("plan_id").asText();
            final String status = resource.get("status").asText();

            // now we know the hook is valid, let's process it
            switch (eventType) {
                case "BILLING.SUBSCRIPTION.CREATED":
                    log.infof("PayPal webhook: subscription '%s' for plan '%s' was created", subscriptionId, planId);
                    // check the status, warn if not active/payed for
                    // but the subscription itself should ba active in our DB regardless
                    if (!isActive(status)) {
                        log.warnf("PayPal webhook: UNPAID subscription '%s' for plan '%s'", subscriptionId, planId);
                        // TODO: implement proper notification system
                    }
                    break;

                case "BILLING.SUBSCRIPTION.ACTIVATED":
                case "BILLING.SUBSCRIPTION.REACTIVATED":
                    userService.reactivateSubscription(subscriptionId);
                    log.infof("PayPal webhook: subscription '%s' for plan '%s' was activated", subscriptionId, planId);
                    break;

                case "BILLING.SUBSCRIPTION.CANCELLED":
                case "BILLING.SUBSCRIPTION.SUSPENDED":
                case "BILLING.SUBSCRIPTION.EXPIRED":
                    // for us all three work the same:
                    // put the end date at NOW
                    userService.expireActiveSubscription(subscriptionId);
                    log.infof("PayPal webhook: subscription '%s' for plan '%s' was cancelled", subscriptionId, planId);
                    break;
            }
        } catch (ValidationException | JsonProcessingException ve) {
            log.warn("PayPal subscription webhook: ", ve);
        }

        // in any case return 200 OK not to get the same message again
        // but keep in mind we might get the same message again anyway
        // because our response might not reach PayPal
        // thus the operations need to be idempotent
        return ResponseEntity.ok().build();
    }

    private boolean isActive(String status) {
        switch (status) {
            case "ACTIVE":
            case "APPROVED":
                return true;

            default:
                return false;
        }
    }

    private void verifyWebhook(PayPalVerificationDto vo) throws JsonMappingException, JsonProcessingException {
        // Call PayPal API to verify webhook
        // TODO: move all this URL-building and authentication headers-setting logic to a separate service
        final String url = paypalHost + "/v1/notifications/verify-webhook-signature";
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // this should authenticate us with PayPal and get OAuth access token
        headers.setBearerAuth(paypalAuthService.getAccessToken());

        final var om = new ObjectMapper();
        final String payload = om.writeValueAsString(vo);
        final HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        final ResponseEntity<String> response = paypalApiRestTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        final JsonNode responseBody = om.readTree(response.getBody());
        if (!"SUCCESS".equals(responseBody.get("verification_status").asText()))
            throw new ValidationException("Webhook didn't validate");
    }


    public static PayPalVerificationDto payPalVerificationDto(final HttpHeaders headers, final String webhookID) {
        final String authAlgo = headers.getOrEmpty("Paypal-Auth-Algo").get(0);
        final String certURL = headers.getOrEmpty("Paypal-Cert-Url").get(0);
        final String transmissionID = headers.getOrEmpty("Paypal-Transmission-Id").get(0);
        final String transmissionSig = headers.getOrEmpty("Paypal-Transmission-Sig").get(0);
        final String transmissionTime = headers.getOrEmpty("Paypal-Transmission-Time").get(0);
        return new PayPalVerificationDto(authAlgo, certURL, transmissionID, transmissionSig, transmissionTime,
                webhookID);
    }
}
