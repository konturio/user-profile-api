--liquibase formatted sql

--changeset user-profile-api:15086-add-mcda-feature.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('mcda', 'UI_PANEL', true, true, false, false, 'Multiple-criteria decision analysis (MCDA)');

insert into app_feature (app_id, feature_id)
select '58851b50-9574-4aec-a3a6-425fa18dcb54', id
from feature
where name = 'mcda';

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id, (select id from feature where name = 'mcda')
from app_user_feature
where app_id = '58851b50-9574-4aec-a3a6-425fa18dcb54';