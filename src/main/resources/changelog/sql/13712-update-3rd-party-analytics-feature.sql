--liquibase formatted sql

--changeset user-profile-service:13712-update-3rd-party-analytics-feature.sql runOnChange:false

update feature set description = 'Enables 3rd party analytics'
where name = 'use_3rdparty_analytics';
