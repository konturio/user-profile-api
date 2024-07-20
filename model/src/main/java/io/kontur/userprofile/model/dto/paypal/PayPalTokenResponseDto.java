package io.kontur.userprofile.model.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PayPalTokenResponseDto {
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
