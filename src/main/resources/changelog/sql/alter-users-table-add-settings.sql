--liquibase formatted sql

--changeset user-profile-service:alter-users-table-add-settings.sql runOnChange:false

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS bio text,
    ADD COLUMN IF NOT EXISTS full_name text,
    ADD COLUMN IF NOT EXISTS osm_editor text,
    ADD COLUMN IF NOT EXISTS theme text,
    ADD COLUMN IF NOT EXISTS default_feed text,
    ALTER COLUMN use_metric_units SET DEFAULT true;

UPDATE users set full_name = first_name || ' ' || last_name;

