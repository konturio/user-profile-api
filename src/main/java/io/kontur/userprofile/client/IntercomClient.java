package io.kontur.userprofile.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.kontur.userprofile.model.dto.intercom.IntercomContactDto;
import io.kontur.userprofile.model.dto.intercom.IntercomContactResponseDto;
import io.kontur.userprofile.model.dto.intercom.IntercomContactSearchDto;
import io.kontur.userprofile.model.dto.intercom.IntercomContactSearchResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class IntercomClient {
    @Value("${intercom.host}")
    private String intercomHost;
    @Value("${intercom.token}")
    private String intercomToken;

    // Intercom has rate limiting per 10 seconds window
    private final Retry retry = RetryRegistry.of(RetryConfig.custom()
            .maxAttempts(4) // initial try, 1st retry after 11s, 2nd retry after 22s, 3rd retry after 44s
            .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofSeconds(11), 2.0))
            .retryOnResult(response -> ((HttpResponse<?>) response).statusCode() == 429)
            .build()
    ).retry("intercom-api");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public IntercomContactResponseDto createContact(IntercomContactDto contactDto) {
        return executeRequest("/contacts", "POST", contactDto, IntercomContactResponseDto.class);
    }

    public IntercomContactResponseDto updateContact(IntercomContactDto contactDto, String intercomId) {
        return executeRequest("/contacts/" + intercomId, "PUT", contactDto, IntercomContactResponseDto.class);
    }

    public IntercomContactSearchResponseDto searchContacts(IntercomContactSearchDto<?> contactSearchDto) {
        return executeRequest("/contacts/search", "POST", contactSearchDto, IntercomContactSearchResponseDto.class);
    }

    private <T> HttpRequest.BodyPublisher toJsonPayload(T obj) {
        try {
            return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize Intercom request: " + obj.toString(), e);
        }
    }

    private <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize Intercom response: " + json, e);
        }
    }

    private <T, R> R executeRequest(String path, String method, T requestDto, Class<R> responseClass) {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + intercomToken)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Intercom-Version", "2.13")
                .uri(URI.create(intercomHost + path))
                .method(method, toJsonPayload(requestDto))
                .build();

        HttpResponse<String> response = sendRequest(request);

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException(String.format(
                    "Failed to execute Intercom API request. Status: %d, Body: %s", response.statusCode(), response.body()));
        }
        return fromJson(response.body(), responseClass);
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return retry.executeCallable(() -> httpClient.send(request, HttpResponse.BodyHandlers.ofString()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute Intercom API request.", e);
        }
    }
}
