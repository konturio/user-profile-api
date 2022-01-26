package io.kontur.userprofile.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class OpenApiConfiguration {
    public static final String JWT_AUTH_DISABLED = "jwtAuthDisabled";

    @Bean
    public OpenAPI customOpenAPI(Environment environment) {
        Server server = new Server();
        server.setUrl(environment.getProperty("server.servlet.context-path"));

        OpenAPI openAPI = new OpenAPI().info(new Info()
                .title("User Profile API")
                .description("User profile and settings"))
            .servers(Collections.singletonList(server));

        boolean isJwtAuthDisabledProfileActive =
            Arrays.asList(environment.getActiveProfiles()).contains(JWT_AUTH_DISABLED);

        if (!isJwtAuthDisabledProfileActive) {
            String securitySchemeName = "bearerAuth";

            openAPI.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                    .addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")));
        }

        return openAPI;
    }
}
