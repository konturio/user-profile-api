--liquibase formatted sql

--changeset user-profile-service:alter-users-table-drop-first-last-name.sql runOnChange:false

UPDATE users set full_name = first_name || ' ' || last_name;

ALTER TABLE users
    DROP COLUMN IF EXISTS first_name,
    DROP COLUMN IF EXISTS last_name;