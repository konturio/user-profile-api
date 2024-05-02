--liquibase formatted sql

--changeset user-profile-api:18424-create-subscription-feature-in-ups.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('subscription', 'UI_PANEL', true, false, false, false, 'Allows to implement subscription plans and payments on apps');

insert into app_feature (app_id, feature_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', id
from feature
where name = 'subscription';

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id, (select id from feature where name = 'subscription')
from app_user_feature
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d';
