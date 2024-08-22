package io.kontur.userprofile.model.dto.paypal;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShippingAmountDto {
    @JsonProperty("currency_code")
    private String currencyCode; // The three-character ISO-4217 currency code that identifies the currency.
    /*
     * The value, which might be:
     * - An integer for currencies like JPY that are not typically fractional.
     * - A decimal fraction for currencies like TND that are subdivided into thousandths.
     */
    private String value;

    public BigDecimal decimalValue() {
        return new BigDecimal(value);
    }
}
