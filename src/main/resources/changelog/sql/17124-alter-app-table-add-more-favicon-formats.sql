--liquibase formatted sql

--changeset user-profile-api:17124-alter-app-table-add-more-favicon-formats.sql runOnChange:true

ALTER TABLE app
    ADD COLUMN IF NOT EXISTS favicon_pack json;

UPDATE app
SET favicon_pack = '{
  "favicon.svg": "/active/static/favicon/favicon.svg",
  "favicon.ico": "/active/static/favicon/favicon.ico",
  "apple-touch-icon.png": "/active/static/favicon/apple-touch-icon.png",
  "icon-192x192.png": "/active/static/favicon/icon-192x192.png",
  "icon-512x512.png": "/active/static/favicon/icon-512x512.png"
}'::json
WHERE name = 'Disaster Ninja';

UPDATE app
SET favicon_pack = '{
  "favicon.svg": "/active/static/favicon/favicon.svg",
  "favicon.ico": "/active/static/favicon/favicon.ico",
  "apple-touch-icon.png": "/active/static/favicon/apple-touch-icon.png",
  "icon-192x192.png": "/active/static/favicon/icon-192x192.png",
  "icon-512x512.png": "/active/static/favicon/icon-512x512.png"
}'::json
WHERE name = 'Disasters embedded application';

UPDATE app
SET favicon_pack = '{
  "favicon.svg": "/active/static/favicon/smart-city-favicon.svg",
  "favicon.ico": "/active/static/favicon/smart-city-favicon.ico",
  "apple-touch-icon.png": "/active/static/favicon/smart-city-apple-touch-icon.png",
  "icon-192x192.png": "/active/static/favicon/smart-city-icon-192x192.png",
  "icon-512x512.png": "/active/static/favicon/smart-city-icon-512x512.png"
}'::json
WHERE name = 'Smart City';

UPDATE app
SET favicon_pack = '{
  "favicon.svg": "/active/static/favicon/oam-favicon.svg",
  "favicon.ico": "/active/static/favicon/oam-favicon.ico",
  "apple-touch-icon.png": "/active/static/favicon/oam-apple-touch-icon.png",
  "icon-192x192.png": "/active/static/favicon/oam-icon-192x192.png",
  "icon-512x512.png": "/active/static/favicon/oam-icon-512x512.png"
}'::json
WHERE name = 'OpenAerialMap';