--liquibase formatted sql

--changeset user-profile-api:alter-app-table-add-extent.sql runOnChange:true

ALTER TABLE app
    ADD COLUMN IF NOT EXISTS extent numeric[] CHECK (array_length(extent, 1) = 4),
    DROP COLUMN IF EXISTS centerGeometry,
    DROP COLUMN IF EXISTS zoom;

UPDATE app
SET extent = array[41.5633,41.5621,41.7094,41.6904]
WHERE id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5';

UPDATE app
SET extent = array[-180,-60,180,60]
WHERE id = '1dc6fe68-8802-4672-868d-7f17943bf1c8';

UPDATE app
SET extent = array[-180,-80,180,80]
WHERE id = '58851b50-9574-4aec-a3a6-425fa18dcb54';
