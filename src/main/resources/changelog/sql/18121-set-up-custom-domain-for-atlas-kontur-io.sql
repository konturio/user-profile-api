--liquibase formatted sql

--changeset user-profile-api:18121-set-up-custom-domain-for-atlas-kontur-io.sql runOnChange:true

-- Kontur Atlas
UPDATE app
SET domains = array['test-atlas-kontur-io.k8s-01.konturlabs.com', 'dev-atlas-kontur-io.k8s-01.konturlabs.com', 'atlas.kontur.io', 'prod-atlas-kontur-io.k8s-01.konturlabs.com']
WHERE id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d';