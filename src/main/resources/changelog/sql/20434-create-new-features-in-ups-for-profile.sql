--liquibase formatted sql

--changeset user-profile-api:20434-create-new-features-in-ups-for-profile.sql runOnChange:false

--create Phone number feature
insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('phone_number', 'UI_PANEL', true, false, true, false, 'Phone number field in user profile')
on conflict do nothing;

--create LinkedIn profile feature
insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('linkedin', 'UI_PANEL', true, false, true, false, 'LinkedIn field in user profile')
on conflict do nothing

--create Organization feature
insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('organization', 'UI_PANEL', true, false, true, false, 'Organization name field in user profile')
on conflict do nothing;

--create Position feature
insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('position', 'UI_PANEL', true, false, true, false, 'Position (occupation) field in user profile')
on conflict do nothing;

--create GIS specialists feature
insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('gis_specialists', 'UI_PANEL', true, false, true, false, 'Number of GIS specialists field in user profile')
on conflict do nothing;

-- Terrain
insert into custom_app_feature (app_id, feature_id, authenticated)
select '3a433e95-0449-48a3-b4ff-9cffea805c74', f.id, true
from feature f
where f.name in ('phone_number', 'linkedin', 'organization', 'position', 'gis_specialists')
on conflict do nothing;

-- Smart City
insert into custom_app_feature (app_id, feature_id, authenticated)
select '634f23f5-f898-4098-a8bd-09eb7c1e1ae5', f.id, true
from feature f
where f.name in ('phone_number', 'linkedin', 'organization', 'position', 'gis_specialists')
on conflict do nothing;

-- MCDA
insert into custom_app_feature (app_id, feature_id, authenticated)
select '77260743-1da0-445b-8f56-ff6ca8520c55', f.id, true
from feature f
where f.name in ('phone_number', 'linkedin', 'organization', 'position', 'gis_specialists')
on conflict do nothing;

-- Atlas
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id, true, r.id
from feature f, custom_role r
where f.name in ('phone_number', 'linkedin', 'organization', 'position', 'gis_specialists')
  and r.name in ('kontur_atlas_edu', 'kontur_atlas_pro', 'kontur_atlas_demo', 'kontur_atlas_admin')
on conflict do nothing;

-- Oasis
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '0b5b4047-3d9b-4ec4-993f-acf9c7315536', f.id, true, r.id
from feature f, custom_role r
where f.name in ('phone_number', 'linkedin', 'organization', 'position', 'gis_specialists')
  and r.name in ('oasis_admin')
on conflict do nothing;

-- ODIN
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '415e2172-3e94-4749-b714-d37470acf88a', f.id, true, r.id
from feature f, custom_role r
where f.name in ('phone_number', 'linkedin', 'organization', 'position', 'gis_specialists')
  and r.name in ('odin_admin', 'odin_demo')
on conflict do nothing;
