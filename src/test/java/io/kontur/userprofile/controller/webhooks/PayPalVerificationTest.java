package io.kontur.userprofile.controller.webhooks;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.kontur.userprofile.rest.webhooks.PayPalVerification;

import org.junit.jupiter.api.Test;


public class PayPalVerificationTest {
    private static Map<String, String> example = Map.of(
            "transmission_id", "69cd13f0-d67a-11e5-baa3-778b53f4ae55",
            "transmission_time", "transmission_time",
            "cert_url", "irrelevant",
            "auth_algo", "SHA256withRSA",
            "transmission_sig", "lmI95Jx3Y9nhR5SJWlHVIWpg4AgFk7n9bCHSRxbrd8A9zrhdu2rMyFrmz",
            "webhook_id", "1JE4291016473214C",
            "webhook_event", "{\"foo\": 42}");

    @Test
    void verificationJsonHasCorrectStructure() throws JsonMappingException, JsonProcessingException {
        // test that Jackson serialization produces correct field names
        // this is important for PayPal to work
        final var o = new PayPalVerification(
                example.get("auth_algo"),
                example.get("cert_url"),
                example.get("transmission_id"),
                example.get("transmission_sig"),
                example.get("transmission_time"),
                example.get("webhook_id"));

        final var mapper = new ObjectMapper();
        final JsonNode event = mapper.readTree(example.get("webhook_event"));
        o.setWebhookEvent(event);

        final String serialized = mapper.writeValueAsString(o);

        for (String key : example.keySet()) {
            assertTrue(serialized.contains(key));
        }
    }
}
