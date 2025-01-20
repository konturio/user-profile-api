--liquibase formatted sql

--changeset user-profile-api:add-linkedin-and-phone-fields-to-users-table.sql runOnChange:true

alter table users
    add column if not exists phone text;

alter table users
    add column if not exists linkedin text;
