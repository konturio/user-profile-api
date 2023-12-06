FROM jboss/keycloak:16.1.1

#required for microdnf
USER root

RUN microdnf remove -y java-11-openjdk-headless \
    && microdnf install -y java-17-openjdk-headless \
    && microdnf clean all

#Copy jar from keycloak-user-spi/build/libs/ into keycloak's /standalone/deployments folder
COPY /build/libs/keycloak-user-spi.jar /opt/jboss/keycloak/standalone/deployments/keycloak-user-spi.jar
#Update configs - UPS database details for the SPI plugin
COPY datasource.txt /tmp
RUN sed -i -e '/<datasources>/r /tmp/datasource.txt' /opt/jboss/keycloak/standalone/configuration/standalone.xml \
    && sed -i -e '/<datasources>/r /tmp/datasource.txt' /opt/jboss/keycloak/standalone/configuration/standalone-ha.xml

USER 1000

#entrypoint from the base image is used