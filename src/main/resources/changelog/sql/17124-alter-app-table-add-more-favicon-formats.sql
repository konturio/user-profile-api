--liquibase formatted sql

--changeset user-profile-api:17124-alter-app-table-add-more-favicon-formats.sql runOnChange:true

ALTER TABLE app
    ADD COLUMN IF NOT EXISTS ico_favicon_url   TEXT,
    ADD COLUMN IF NOT EXISTS apple_favicon_url TEXT,
    ADD COLUMN IF NOT EXISTS png_192_favicon_url TEXT,
    ADD COLUMN IF NOT EXISTS png_512_favicon_url TEXT;

UPDATE app
SET favicon_url='/active/static/favicon/favicon.svg',
    ico_favicon_url='/active/static/favicon/favicon.ico',
    apple_favicon_url='/active/static/favicon/apple-touch-icon.png',
    png_192_favicon_url='/active/static/favicon/icon-192x192.png',
    png_512_favicon_url='/active/static/favicon/icon-512x512.png'
WHERE name = 'Disaster Ninja';

UPDATE app
SET favicon_url='/active/static/favicon/favicon.svg',
    ico_favicon_url='/active/static/favicon/favicon.ico',
    apple_favicon_url='/active/static/favicon/apple-touch-icon.png',
    png_192_favicon_url='/active/static/favicon/icon-192x192.png',
    png_512_favicon_url='/active/static/favicon/icon-512x512.png'
WHERE name = 'Disasters embedded application';

UPDATE app
SET favicon_url='/active/static/favicon/smart-city-favicon.svg',
    ico_favicon_url='/active/static/favicon/smart-city-favicon.ico',
    apple_favicon_url='/active/static/favicon/smart-city-apple-touch-icon.png',
    png_192_favicon_url='/active/static/favicon/smart-city-icon-192x192.png',
    png_512_favicon_url='/active/static/favicon/smart-city-icon-512x512.png'
WHERE name = 'Smart City';

UPDATE app
SET favicon_url='/active/static/favicon/oam-favicon.svg',
    ico_favicon_url='/active/static/favicon/oam-favicon.ico',
    apple_favicon_url='/active/static/favicon/oam-apple-touch-icon.png',
    png_192_favicon_url='/active/static/favicon/oam-icon-192x192.png',
    png_512_favicon_url='/active/static/favicon/oam-icon-512x512.png'
WHERE name = 'OpenAerialMap';