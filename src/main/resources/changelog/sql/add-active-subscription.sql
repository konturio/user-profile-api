--liquibase formatted sql

--changeset user-profile-api:add-active-subscription.sql runOnChange:true

-- Add missing foreign key constraint on configuration_for_user_id in custom_app_feature

delete from custom_app_feature caf
where caf.configuration_for_user_id is not null
    and not exists(select 1 from users u where u.id = caf.configuration_for_user_id);

alter table custom_app_feature
    drop constraint if exists fk_custom_app_feature_configuration_for_user_id;

alter table custom_app_feature
    add constraint fk_custom_app_feature_configuration_for_user_id foreign key (configuration_for_user_id) references users (id) on delete cascade;

-- Remove redundant columns from user_custom_role and add missing constraints

alter table user_custom_role
    drop constraint if exists uq_user_custom_role,
    drop constraint if exists fk_user_custom_role_user_id,
    drop constraint if exists fk_user_custom_role_role_id;

alter table user_custom_role
    drop column if exists subscription_id,
    drop column if exists plan_id;

alter table user_custom_role
    add constraint uq_user_custom_role unique nulls not distinct (user_id, role_id, started_at, ended_at),
    add constraint fk_user_custom_role_user_id foreign key (user_id) references users (id) on delete cascade,
    add constraint fk_user_custom_role_role_id foreign key (role_id) references custom_role (id);

-- Remove redundant columns from custom_role

alter table custom_role
    drop column if exists plan_ids;

-- Create a new table billing_plan_custom_role that maps billing plans to roles

drop table if exists billing_plan;

create table billing_plan (
    id text not null primary key,
    role_id bigint not null,
    constraint uq_billing_plan unique (id, role_id),
    constraint fk_billing_plan_role_id foreign key (role_id) references custom_role (id)
);

insert into billing_plan (id, role_id) values
    ('P-4S497103CE258725WMZVQ4SQ', (select id from custom_role where name = 'kontur_atlas_edu')),
    ('P-2D320730VP634834UMZVR2QY', (select id from custom_role where name = 'kontur_atlas_edu')),
    ('P-6FF170012W0661531MZVRAWA', (select id from custom_role where name = 'kontur_atlas_pro')),
    ('P-72G63349X0698005HMZVRWOY', (select id from custom_role where name = 'kontur_atlas_pro'));

-- Create a new table to store user billing subscriptions

create table if not exists user_billing_subscription (
    id text not null primary key,
    billing_plan_id text not null,
    app_id uuid not null,
    user_id bigint not null,
    active boolean not null default true,
    created_at timestamptz not null default now(),
    expired_at timestamptz,
    constraint ch_user_paid_subscription_active_expired_at check (
        (active and expired_at is null) or
        (not active and expired_at is not null)
    ),
    constraint fk_user_billing_subscription_billing_plan_id foreign key (billing_plan_id) references billing_plan (id),
    constraint fk_user_billing_subscription_app_id foreign key (app_id) references app (id),
    constraint fk_user_billing_subscription_user_id foreign key (user_id) references users (id) on delete cascade
);

create unique index uq_user_billing_subscription_active
    on user_billing_subscription (user_id, app_id)
    where active;