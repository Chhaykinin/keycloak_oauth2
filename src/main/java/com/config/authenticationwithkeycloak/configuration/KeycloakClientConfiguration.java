package com.config.authenticationwithkeycloak.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

@Configuration
public class KeycloakClientConfiguration {

    @Value("pIrqH1XNthR8xB1RVjkRFJ7eZiPArOEB")
    private String secretKey;
    @Value("my_client")
    private String clientId;
    @Value("${keycloak.auth-server-url}")
    private String authUrl;
    @Value("spring_oauth2")
    private String realm;

    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl(authUrl)
                .grantType(CLIENT_CREDENTIALS)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(secretKey)
                .build();
    }
}
