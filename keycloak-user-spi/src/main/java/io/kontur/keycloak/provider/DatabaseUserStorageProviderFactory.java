package io.kontur.keycloak.provider;

import javax.naming.InitialContext;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

@JBossLog
public class DatabaseUserStorageProviderFactory
    implements UserStorageProviderFactory<DatabaseUserStorageProvider> {
    @Override
    public DatabaseUserStorageProvider create(KeycloakSession session, ComponentModel component) {
        // It takes a org.keycloak.models.KeycloakSession parameter. This object can
        // be used to look up other information and metadata as well as provide access
        // to various other components within the runtime. The ComponentModel parameter
        // represents how the provider was enabled and configured within a specific realm.
        // It contains the instance id of the enabled provider as well as any configuration
        // you may have specified for it when you enabled through the admin console.

        try {
            InitialContext ctx = new InitialContext();
            DatabaseUserStorageProvider provider = (DatabaseUserStorageProvider) ctx.lookup(
                "java:global/keycloak-user-spi/DatabaseUserStorageProvider"
            );

            provider.setComponent(component);
            provider.setSession(session);
            return provider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getId() {
        return "UserSettingsAPI";
    }
}
