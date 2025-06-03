--liquibase formatted sql

--changeset user-profile-api:add-linkedin-and-phone-fields-to-users-table.sql runOnChange:true

alter table users
    add column if not exists phone text;

alter table users
    add column if not exists linkedin text;

alter table users
    add column if not exists call_consent_given boolean not null default false;

alter table users
    add column if not exists created_at timestamptz not null default now();

alter table users
    add column if not exists account_notes text;