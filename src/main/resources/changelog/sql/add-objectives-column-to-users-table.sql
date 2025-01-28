--liquibase formatted sql

--changeset user-profile-api:add-objectives-column-to-users-table.sql runOnChange:true

alter table users
rename column bio to objectives;

alter table users
add column bio text;