--liquibase formatted sql

--changeset user-profile-api:12308-add-app-user-feature-configuration.sql runOnChange:true

alter table app_user_feature
add column if not exists configuration json;

alter table feature
add column if not exists available_for_user_configuration boolean default false;

update feature set available_for_user_configuration = true where name = 'reference_area';