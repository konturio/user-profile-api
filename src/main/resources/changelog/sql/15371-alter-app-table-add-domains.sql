--liquibase formatted sql

--changeset user-profile-api:15371-alter-app-table-add-domains.sql runOnChange:true

ALTER TABLE app
    ADD COLUMN IF NOT EXISTS domains text[];

-- DN
UPDATE app
SET domains = array['localhost', 'test-apps-ninja02.konturlabs.com', 'test-apps-ninja01.konturlabs.com',
    'disaster.ninja', 'www.disaster.ninja']
WHERE id = '58851b50-9574-4aec-a3a6-425fa18dcb54';

-- Smart City
UPDATE app
SET domains = array['maps.kontur.io']
WHERE id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5';

-- OAM
UPDATE app
SET domains = array['new.openaerialmap.org']
WHERE id = '1dc6fe68-8802-4672-868d-7f17943bf1c8';