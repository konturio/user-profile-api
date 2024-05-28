--liquibase formatted sql

--changeset user-profile-service:create-roles-tables.sql runOnChange:true

drop table if exists custom_app_feature;
drop table if exists user_custom_role;
drop table if exists custom_role;

create table if not exists custom_role (
    id bigint not null constraint pk_custom_role primary key generated always as identity,
    name text not null unique,
    plan_id text unique
);

create table if not exists user_custom_role (
    id bigint not null constraint pk_user_custom_role primary key generated always as identity,
    user_id bigint not null,
    role_id bigint not null,
    subscription_id text,
    started_at timestamptz,
    ended_at timestamptz,
    constraint uq_user_custom_role unique nulls not distinct (user_id, role_id, subscription_id, started_at, ended_at),
    constraint ch_user_custom_role check (
        (subscription_id is null or started_at is not null and ended_at is not null)
    )
);

create table if not exists custom_app_feature (
    id bigint not null constraint pk_custom_app_feature primary key generated always as identity,
    app_id uuid not null,
    feature_id bigint not null,
    authenticated boolean not null,
    role_id bigint,
    configuration_for_user_id bigint,
    configuration json,
    constraint uq_custom_app_feature unique nulls not distinct (app_id, feature_id, authenticated, role_id, configuration_for_user_id),
    constraint ch_custom_app_feature check (
        (not authenticated and role_id is null and configuration_for_user_id is null)
            or (authenticated and (role_id is null or configuration_for_user_id is null))
    ),
    constraint fk_custom_app_feature_app_id foreign key (app_id) references app (id),
    constraint fk_custom_app_feature_feature_id foreign key (feature_id) references feature (id)
);

