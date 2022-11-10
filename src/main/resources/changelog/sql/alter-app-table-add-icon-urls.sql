--liquibase formatted sql

--changeset user-profile-api:alter-app-table-add-icon-urls.sql runOnChange:false

ALTER TABLE app
    ADD COLUMN IF NOT EXISTS sidebar_icon_url TEXT,
    ADD COLUMN IF NOT EXISTS favicon_url      TEXT;

UPDATE app
SET sidebar_icon_url='https://disaster.ninja/active/static/favicon/favicon.svg',
    favicon_url='https://disaster.ninja/active/static/favicon/favicon.svg'
WHERE id = '58851b50-9574-4aec-a3a6-425fa18dcb54';

UPDATE app
SET sidebar_icon_url='https://disaster.ninja/active/static/favicon/smart-city-icon.svg',
    favicon_url='https://disaster.ninja/active/static/favicon/smart-city-favicon.svg'
WHERE id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5';