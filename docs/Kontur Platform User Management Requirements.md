# Kontur Platform User Management Requirements

Field: Content

# Kontur Platform User Management Requirements

## Summary:

We would like to make a User Management App so that all users of Kontur platform can log in/register an account and have different levels of access.\
\
User Management is necessary for the following Kontur Products:\
- Event API

\- Kontur.io\
- Disaster.ninja

\- Global Fires

\- Minsk Fire (new logistics products)\
\
The registration, users administration and user account is one for all apps.\
\
We’ll have a separate Keycloak installation for Tiers (Prod/Test and Dev)

In general Kontur UM should include:
* User, Roles and Composite Roles (Permissions)
* Clients (Apps: e.g. Event API, disaster.ninja) 
* Groups (will be added if necessary)

## Registration forms:

## Users:
* What we can do with Users:

\- Add User (login, password, first name, last name, email, organization, location(?), role (permission),

\- Delete User \
- Edit User \
- Filter Users (by name, by email, by Client, by Group (if we have any) by Role, by Date)
* If the registration is external (a user register himself), he/she has immediate access to Public Group (basic roles open to a wide audience)
* User receives a notification with login/password and information on the apps he/she has access to
* A user can be a part of one Group
* A user can be in more than one Client
* A user inherits Roles from his Group
* A user can be out of any Group
* Keycloak generates tokens in accordance with User Role
* A user can be active/inactive (inactive users have no roles)
* We can make active user inactive, thereby take back his/her roles, groups

## Clients and Groups:
* Clients stand for Kontur Apps (e.g. Event API, disaster.ninja, [kontur.io](http://kontur.io) etc)
* Groups stand for clients or user groups (e.g. SwissRE, disaster.ninja, Public)
* Group can be created, edited or deleted
* Groups have name, link, description,  roles associated with it, users
* Group Admin role does not inherit, but is added to a user within Group
* Groups can have roles from different Clients
* A User inherits roles from Group
* There will be an option to look all Group Members

## User Roles:
* Roles can be created, edited or deleted
* If a User is a part of a Group, that user will inherit all of the Roles
* A User can not be in a Group and have less roles then he inherited from the Group 
* The only Role a user can have that is not inherited - User Admin 
* A user can be given the role ‘GROUP_ADMIN’ which will allow them to be an Admin of that Group from the User Account
* We need to create a composite role.

## Kontur User Management v.1.0
* In the first version will have access only to Event API
* In this version will have only create user option (no external registration)
* In this version we need to create a user from the admin panel, set him/her roles (permissions) and Groups, generate token and send an email with login and password and what access he/she has
* in the first version we'll have only Client, Users and Roles, no Groups

|     |     |
| --- | --- |
| **Keycloak entities** | **Description** |
| Client | Application (Event API, Disaster Ninja etc.) to which we can log in through the keylock. |
| Role | Role/permission -  the right to do something in the system. Can be related to Client or not. **Composite Role** - role that includes several separate roles. |
| User | User :) It can be assigned roles that belong to Clients or not. May have roles from different Clients and without clients.  |
| Group | Organization that can have a hierarchical (tree) structure.  User can be associated with many groups and inherits group's roles. |
