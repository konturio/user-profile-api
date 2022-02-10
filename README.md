#Test Coverage
[![coverage report](https://gitlab.com/kontur-private/platform/user-profile-api/badges/main/coverage.svg)](https://gitlab.com/kontur-private/platform/user-profile-api)

# Required configuration in keycloak:

### Enable roles and username to be added to JWT access token:

Go to admin console / Clients / kontur_platform / Scope tab
- Enable <code>Full Scope Enabled</code>

Go to admin console / Clients / kontur_platform / Mappers tab
- create 'username' user property mapper in 'kontur_platform' (user property: 'username', claim: 'username')
- create 'event_api_roles' client role mapper in 'kontur_platform' (client id: 'event-api', client role prefix: 'EVENTAPI_', token claim name: 'realm_access.roles', multivalued: true, claim json type: 'string', add to id token: false, add to access token: true, add to userinfo: false)