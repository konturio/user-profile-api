--liquibase formatted sql

--changeset user-profile-api:add-live-sensor-feature.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description) values
    ('live_sensor', 'UI_PANEL', true, true, false, false, 'Live Sensor');

insert into app_feature (app_id, feature_id)
select '634f23f5-f898-4098-a8bd-09eb7c1e1ae5', id from feature where name = 'live_sensor';

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id,  (select id from feature where name = 'live_sensor') from app_user_feature
where app_id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5';