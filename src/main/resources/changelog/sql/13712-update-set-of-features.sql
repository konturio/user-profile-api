--liquibase formatted sql

--changeset user-profile-service:13712-update-set-of-features.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('use_3rdparty_analytics', 'UI_PANEL', true, false, true, true, 'Disables 3rd party analytics');


insert into app_feature (app_id, feature_id)
select id, (select id from feature where name = 'use_3rdparty_analytics')
from app
where name != 'Image_Preview';


insert into app_user_feature (app_id, user_id, feature_id)
select distinct on (app_id, user_id) app_id, user_id, (select id from feature where name = 'use_3rdparty_analytics')
from app_user_feature
where app_id != (select id from app where name = 'Image_Preview');


delete
from app_feature
where app_id = (select id from app where name = 'Image_Preview')
  and feature_id in (select id from feature where name in ('toasts', 'locate_me'));
