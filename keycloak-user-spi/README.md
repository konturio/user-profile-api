# Pre-requisites:

### Enable prepared transactions in PostgreSQL:
set <code>max_prepared_transactions</code> = value set in <code>max_connections</code> in postgresql.conf

### Add required libs to keycloak:

1. Apache logging:
    - modules/system/layers/keycloak/org/apache/commons/logging/main/commons-logging-1.2.jar
    - modules/system/layers/keycloak/org/apache/commons/logging/main/module.xml:
   ```xml
   <?xml version="1.0" ?>
   <module name="org.apache.commons.logging" xmlns="urn:jboss:module:1.3">
       <resources>
           <resource-root path="commons-logging-1.2.jar"/>
       </resources>
       <dependencies>
       </dependencies>
   </module>
   ```

2. Postgresql driver:
    - modules/system/layers/keycloak/org/postgresql/main/postgresql-42.3.1.jar
    - modules/system/layers/keycloak/org/postgresql/main/module.xml
   ```xml
   <?xml version="1.0" ?>
   <module xmlns="urn:jboss:module:1.3" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-42.3.1.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
   </module>
   - ```

### Add xa-datasource into <datasources> in keycloak configuration (standalone.xml):
JNDI name should match one specified in [persistence.xml](src/main/resources/META-INF/persistence.xml)

```xml

<datasources>
    <xa-datasource jndi-name="java:/kontur/datasources/KonturDS" pool-name="KonturDS" enabled="true"
                   use-java-context="true"
                   statistics-enabled="${wildfly.datasources.statistics-enabled:${wildfly.statistics-enabled:false}}">
        <xa-datasource-property name="ServerName">localhost</xa-datasource-property>
        <xa-datasource-property name="PortNumber">5432</xa-datasource-property>
        <xa-datasource-property name="DatabaseName">userprofile</xa-datasource-property>
        <driver>postgresql</driver>
        <security>
            <user-name>userprofile</user-name>
            <password>userprofile</password>
        </security>
    </xa-datasource>
    <drivers>
        <driver name="postgresql" module="org.postgresql">
            <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
        </driver>
    </drivers>
</datasources>
```

###How to deploy to keycloak
1. Build the entire project
2. Copy jar from <code>keycloak-user-spi/build/libs/</code> into keycloak's <code>/standalone/deployments</code> folder
3. Normally hot deploy is enabled so just check server.log (<code>/standalone/logs</code>)

###How to enable in keycloak
1. Go to Management Console -> User Federation
2. If everything is deployed correctly, there will be <code>UserSettingsAPI</code> available to add in User Federation -> do it, set order = 0