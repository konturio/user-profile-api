--liquibase formatted sql

--changeset user-profile-service:alter-tables-add-generated-as-identity.sql runOnChange:false

ALTER TABLE users
ALTER id ADD GENERATED ALWAYS AS IDENTITY;

SELECT setval('users_id_seq',  (SELECT max(id) from users));

ALTER TABLE subscription
ALTER id ADD GENERATED ALWAYS AS IDENTITY;

SELECT setval('subscription_id_seq',  (SELECT max(id) from subscription));

ALTER TABLE feature
ALTER id ADD GENERATED ALWAYS AS IDENTITY;

SELECT setval('feature_id_seq',  (SELECT max(id) from feature));