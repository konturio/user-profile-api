package io.kontur.userprofile.rest.webhooks;

import java.util.Map;

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

import io.kontur.userprofile.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.ValidationException;
import lombok.Data;
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

            PayPalVerification vo = PayPalVerification.of(headers, webhookId);
            vo.setWebhookEvent(payload);
            verifyWebhook(vo);

            // now we know the hook is valid, let's process it
            switch (eventType) {
                case "BILLING.SUBSCRIPTION.CREATED":
                    log.info("Subscription webhook: created");
                    // check the status, warn if not active/payed for
                    // but create and make active in our DB regardless
                    break;

                case "BILLING.SUBSCRIPTION.ACTIVATED":
                case "BILLING.SUBSCRIPTION.REACTIVATED":
                    log.info("Subscription webhook: activated");
                    // if already exists an active one do nothing
                    // otherwise create a new one
                    break;

                case "BILLING.SUBSCRIPTION.CANCELLED":
                case "BILLING.SUBSCRIPTION.SUSPENDED":
                case "BILLING.SUBSCRIPTION.EXPIRED":
                    // for us all three work the same:
                    // put the end date at NOW
                    log.info("Subscription webhook: cancelled");
                    break;
            }
        } catch (ValidationException | JsonProcessingException ve) {
            log.warn("Subscription webhook: ", ve);
        }

        // in any case return 200 OK not to get the same message again
        // but keep in mind we might get the same message again anyway
        // because our response might not reach PayPal
        // thus the operations need to be idempotent
        return ResponseEntity.ok().build();
    }

    private void verifyWebhook(PayPalVerification vo) throws JsonMappingException, JsonProcessingException {
        // Call PayPal API to verify webhook
        String url = paypalHost + "/v1/notifications/verify-webhook-signature";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // We need OAuth 2.0 here
        // Flow type: clientCredentials
        // Token URL: https://api-m.paypal.com/v1/oauth2/token
        // Required scopes: https://uri.paypal.com/services/applications/webhooks ; https://uri.paypal.com/services/applications/verify-webhook-signature
        // headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(vo.toString(), headers);
        ResponseEntity<String> response = paypalApiRestTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        JsonNode responseBody = new ObjectMapper().readTree(response.getBody());
        if (!"SUCCESS".equals(responseBody.get("verification_status").asText()))
            throw new ValidationException("Webhook didn't validate");
    }
}
