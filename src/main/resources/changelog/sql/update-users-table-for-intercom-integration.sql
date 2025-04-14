--liquibase formatted sql

--changeset user-profile-api:update-users-table-for-intercom-integration.sql runOnChange:true

alter table "users" add column if not exists intercom_id text;