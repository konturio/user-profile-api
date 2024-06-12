package io.kontur.userprofile.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.httpcomponents.hc5.PoolingHttpClientConnectionManagerMetricsBinder;

@Configuration
public class WebConfiguration {

    @Bean
    public HttpClient httpClient(MeterRegistry meterRegistry) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(20);

        new PoolingHttpClientConnectionManagerMetricsBinder(connectionManager, "connection_pool")
                .bindTo(meterRegistry);

        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();
    }

    // this one is supposed to be protected by OAuth
    @Bean
    public RestTemplate paypalApiRestTemplate(RestTemplateBuilder builder,
                                              HttpClient httpClient,
                                              @Value("${payments.paypal.connection-timeout}") Integer connectionTimeout,
                                              @Value("${payments.paypal.read-timeout}") Integer readTimeout) {
        return builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                // .requestFactory((settings) -> ClientHttpRequestFactories.get(
                // () -> new HttpComponentsClientHttpRequestFactory(httpClient),
                // settings
                // .withConnectTimeout(Duration.of(connectionTimeout, ChronoUnit.SECONDS))
                // .withReadTimeout(Duration.of(readTimeout, ChronoUnit.SECONDS))))
                // .rootUri(paypalApiUrl)
                .setConnectTimeout(Duration.of(connectionTimeout, ChronoUnit.SECONDS))
                // there's simply no `setReadTimeout` method in the
                // `HttpComponentsClientHttpRequestFactory`
                // .setReadTimeout(Duration.of(readTimeout, ChronoUnit.SECONDS))
                .build();
    }

    // this one isn't supposed to be protected by OAuth
    @Bean
    public RestTemplate paypalAuthorizationRestTemplate(RestTemplateBuilder builder,
                                                        HttpClient httpClient,
                                                        @Value("${payments.paypal.connection-timeout}") Integer connectionTimeout,
                                                        @Value("${payments.paypal.read-timeout}") Integer readTimeout) {
        return builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                // .requestFactory((settings) -> ClientHttpRequestFactories.get(
                // () -> new HttpComponentsClientHttpRequestFactory(httpClient),
                // settings
                // .withConnectTimeout(Duration.of(connectionTimeout, ChronoUnit.SECONDS))
                // .withReadTimeout(Duration.of(readTimeout, ChronoUnit.SECONDS))))
                // .rootUri(paypalApiUrl)
                .setConnectTimeout(Duration.of(connectionTimeout, ChronoUnit.SECONDS))
                // .setReadTimeout(Duration.of(readTimeout, ChronoUnit.SECONDS))
                .build();
    }
}
