FROM keycloak/keycloak:23.0.6

USER root
#Copy jar from keycloak-user-spi/build/libs/ into keycloak's /standalone/deployments folder
COPY /build/libs/keycloak-user-spi*.jar /opt/keycloak/providers/
COPY /src/main/resources/quarkus.properties /opt/keycloak/conf/
RUN /opt/keycloak/bin/kc.sh build --db=postgres
COPY themes/ /opt/keycloak/themes/
RUN /opt/keycloak/bin/kc.sh show-config
CMD ["start", "--health-enabled=true", "--spi-login-protocol-openid-connect-suppress-logout-confirmation-screen=true", "--spi-login-protocol-openid-connect-legacy-logout-redirect-uri=true"]
