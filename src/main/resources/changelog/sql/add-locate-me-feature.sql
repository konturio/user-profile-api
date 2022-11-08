--liquibase formatted sql

--changeset user-profile-api:add-locate-me-feature.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description) values
    ('locate_me', 'UI_PANEL', true, false, true, false, 'Locate Me button');

insert into app_feature (app_id, feature_id)
select id, (select id from feature where name = 'locate_me') from app;

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id,  (select id from feature where name = 'locate_me') from app_user_feature;