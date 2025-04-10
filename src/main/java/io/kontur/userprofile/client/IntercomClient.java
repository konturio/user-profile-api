package io.kontur.userprofile.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kontur.userprofile.model.dto.intercom.IntercomContactDto;
import io.kontur.userprofile.model.dto.intercom.IntercomContactResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class IntercomClient {
    @Value("${intercom.host}")
    private String intercomHost;
    @Value("${intercom.token}")
    private String intercomToken;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public IntercomContactResponseDto syncContact(IntercomContactDto intercomContactDto, String intercomId) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(intercomContactDto);
            HttpRequest request = intercomId == null ? createContactRequest(jsonPayload) : updateContactRequest(jsonPayload, intercomId);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), IntercomContactResponseDto.class);
            } else {
                throw new RuntimeException("Failed to sync Intercom contact. Status: " + response.statusCode()
                        + ", Body: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to sync Intercom contact: " + intercomContactDto, e);
        }
    }

    private HttpRequest createContactRequest(String jsonPayload) {
        return HttpRequest.newBuilder()
                .uri(URI.create(intercomHost + "/contacts"))
                .header("Authorization", "Bearer " + intercomToken)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Intercom-Version", "2.13")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();
    }

    private HttpRequest updateContactRequest(String jsonPayload, String intercomId) {
        return HttpRequest.newBuilder()
                .uri(URI.create(intercomHost + "/contacts/" + intercomId))
                .header("Authorization", "Bearer " + intercomToken)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Intercom-Version", "2.13")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();
    }
}
