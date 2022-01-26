# Required configuration in keycloak:

### Enable roles/groups to be added to JWT access token:

TODO change account-console to /userprofile once it's configured

Go to admin console / Clients / account-console / Scope tab
- Enable <code>Full Scope Enabled</code>

Go to admin console / Clients / account-console / Mappers tab
- Add Builtin -> <code>realm roles</code>
Once client roles are supported (not yet):
- Add Builtin -> <code>client roles</code>

Roles added by these claims already include all roles inherited from parent groups.