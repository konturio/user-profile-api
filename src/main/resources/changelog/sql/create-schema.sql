create sequence feature_sequence;

alter sequence feature_sequence owner to "user-profile-api";

create sequence user_sequence;

alter sequence user_sequence owner to "user-profile-api";

create sequence roles_sequence;

alter sequence roles_sequence owner to "user-profile-api";

create type feature_type as enum ('UI_PANEL','LAYER','EVENT_FEED','BIVARIATE_LAYER','BIVARIATE_PRESET');

--FEATURE

create table feature
(
    id bigint not null constraint pk_feature primary key,
    beta boolean not null,
    name varchar(255) not null constraint uk_feature_code unique,
    featuretype feature_type not null,
    enabled boolean not null,
    is_public boolean not null,
    description varchar(255)
    );

alter table feature owner to "user-profile-api";

-- USERS
create table users
(
    id bigint not null constraint pk_users primary key,
    username varchar(255) not null constraint uk_users_username unique,
    email varchar(255) not null constraint uk_users_email unique,
    first_name varchar(255),
    last_name varchar(255),
    language varchar(63), --todo table/configuration/api with supported languages and FK?
    use_metric_units boolean not null,
    subscribed_to_kontur_updates boolean not null
    );
alter table users owner to "user-profile-api";

create table user_feature
(
    user_id bigint not null constraint fk_user_feature_users references users on delete cascade,
    feature_id bigint not null constraint fk_user_feature_feature references feature on delete cascade,
    unique (user_id, feature_id)
);

alter table user_feature owner to "user-profile-api";

--USER_GROUPS
create table user_groups
(
    user_id bigint not null constraint fk_user_groups_users references users on delete cascade,
    group_id varchar(63) not null,
    name varchar(255) not null
);
alter table user_groups owner to "user-profile-api";

--SUBSCRIPTION
create table subscription
(
    id bigint not null constraint pk_subscription primary key constraint fk_subscription_users references users on delete cascade,
    enabled boolean not null,
    event_feed varchar(255) not null,
    bbox varchar(255),
    type varchar(255),
    severity varchar(15),
    people bigint
    );

alter table subscription owner to "user-profile-api";

create table user_roles
(
    user_id bigint not null constraint fk_user_roles_users references users on delete cascade,
    role_id varchar(63) not null,
    name varchar(255) not null,
    client_id varchar(63),
    client_clientid varchar(255) --name is client.clientId in keycloak model
);

alter table user_roles owner to "user-profile-api";
