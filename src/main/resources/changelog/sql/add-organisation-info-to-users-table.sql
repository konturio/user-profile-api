--liquibase formatted sql

--changeset user-profile-api:add-organisation-info-to-users-table.sql runOnChange:true

alter table users
add column company_name text,
add column position text,
add column amount_of_gis text;