package io.kontur.userprofile.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PayPalTokenResponse {
    private String scope;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private String expiresIn; // yes, it's a string containing the number of seconds

    @JsonProperty("refresh_token")
    private String refreshToken;

    private String nonce;

    public long lifetimeSeconds() {
        return Long.parseLong(expiresIn);
    }
}
