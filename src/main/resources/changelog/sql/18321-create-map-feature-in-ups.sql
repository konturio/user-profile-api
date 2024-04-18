--liquibase formatted sql

--changeset user-profile-api:18321-create-map-feature-in-ups.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description) 
values ('map', 'UI_PANEL', true, false, true, true, 'Map mode which can be switched off for not logged in users');

insert into app_feature (app_id, feature_id)
select id, (select id from feature where name = 'map') from app;

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id, (select id from feature where name = 'map') from app_user_feature;
