--liquibase formatted sql

--changeset user-profile-service:14862-update-features-for-epig-and-marketing-apps.sql runOnChange:false


-- EPIG
insert into app_feature (app_id, feature_id)
select 'c5ecc65b-1e7e-4e31-92a4-222fadeaeef0', f.id
from feature f
where f.name in ('focused_geometry_layer')
on conflict (app_id, feature_id) do nothing;

update app
set extent = array[-180,-80,180,80]
where id = 'c5ecc65b-1e7e-4e31-92a4-222fadeaeef0';


-- Marketing app
insert into app_feature (app_id, feature_id)
select 'f70488c2-055c-4599-a080-ded10c47730f', f.id
from feature f
where f.name in ('current_event', 'focused_geometry_layer', 'use_3rdparty_analytics')
on conflict (app_id, feature_id) do nothing;

delete
from app_feature
where app_id = 'f70488c2-055c-4599-a080-ded10c47730f'
  and feature_id in (select id from feature where name in ('legend_panel', 'tooltip'));

update app
set extent = array[-180,-80,180,80]
where id = 'f70488c2-055c-4599-a080-ded10c47730f';