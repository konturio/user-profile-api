--liquibase formatted sql

--changeset user-profile-api:18417-create-llm-analytics-feature-in-ups.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('llm_analytics', 'UI_PANEL', true, true, false, true, 'AI analytics panel connected to open.ai and allowed to get personalized analytics based on user`s bio from their profile');

insert into app_feature (app_id, feature_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', id
from feature
where name = 'llm_analytics';

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id, (select id from feature where name = 'llm_analytics')
from app_user_feature
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d';
