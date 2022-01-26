package io.kontur.userprofile.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Role {
    @NotNull
    @Column(name = "role_id")
    private String id;
    @NotNull
    private String name;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "client_clientid")
    private String clientClientId; //name is client.clientId in keycloak model

    public static class Names {
        public static final String BETA_FEATURES = "betaFeatures";
        public static final String KONTUR_ADMIN = "konturAdmin";
    }

    public boolean isClientRole() {
        return clientId != null && !clientId.isBlank();
    }
}
