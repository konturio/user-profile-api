# Description:
The project is split into three modules:
 - `model` - contains DTOs, DB entities, and converters. Both `keycloak-user-spi` and `user-profile-api` depend on it to connect to DB
 - `keycloak-user-spi` - keycloak [User Storage SPI plugin](https://www.keycloak.org/docs/latest/server_development/#_user-storage-spi) implementation. It provides UPS DB as external user storage for keycloak.
 - `user-profile-api` - UPS REST API

Notes to keep in mind while working with `keycloak-user-spi`:
- To enable User Storage SPI plugin configure User Federation in keycloak admin console.
- After SPI is enabled all new users will be created in UPS DB. But keycloak will still use internal DB for old users.
- Email is required to store users in UPS DB.
- By the time this note is written (February 2023) all users were migrated into UPS DB except `disaster.ninja` and `geocint`. These users lack of email to migrate them to UPS DB.
- User ID can be used to identify users that are stored in external storage. Keycloak uses `f:{storage_ID}:{user_ID}` pattern for users IDs provided by SPIs. 
- Group and Roles are stored in local keycloak DB, **but they are synced with UPS DB**. Groups and Roles are stored in `user_groups` and `user_roles` accordingly. Current Adapters implementation restricts roles and groups updates.
- Group and Role names are stored both in keycloak and UPS DB. Changing some parameters (like name) in one of storage isn't synchronized with another one.   

# Scripts and Assets Folder Structure

A table named `assets` was created to gather media files contained in that each Kontur app. To facilitate content management, the folder structure was reorganised and new items were added:
- `assets`: Contains media files organized by `app_name`, `feature_name`, and `language`.
- `i18n`: Stores translation files such as `assets.pot` and `assets.po`.
- `scripts`: Houses automation scripts for asset management. See [README](../scripts/README.md) before running the scripts on your local machine.

# Deployment
- To deploy both UPS REST API and keycloak with User SPI plugin standard Kontur workflow is used. Artifacts are built automatically for every commit. Deployment version is specified in CD repo 


# Required configuration in keycloak:

### Enable roles and username to be added to JWT access token:

Go to admin console / Clients / kontur_platform / Scope tab
- Enable <code>Full Scope Enabled</code>

Go to admin console / Clients / kontur_platform / Mappers tab
- create 'username' user property mapper in 'kontur_platform' (user property: 'username', claim: 'username')
- create 'event_api_roles' client role mapper in 'kontur_platform' (client id: 'event-api', client role prefix: 'EVENTAPI_', token claim name: 'realm_access.roles', multivalued: true, claim json type: 'string', add to id token: false, add to access token: true, add to userinfo: false)