--liquibase formatted sql

--changeset user-profile-api:17517-create-layer-features-panel-feature.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description) values
    ('layer_features_panel', 'UI_PANEL', true, true, false, false, 'Layer Feature panel');

insert into app_feature (app_id, feature_id)
select '58851b50-9574-4aec-a3a6-425fa18dcb54', id from feature where name = 'layer_features_panel';

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id,  (select id from feature where name = 'layer_features_panel') from app_user_feature
where app_id = '58851b50-9574-4aec-a3a6-425fa18dcb54';
