--liquibase formatted sql

--changeset user-profile-service:17057-create-about-page-feature-for-applications-except-smart-city-and-oam.sql runOnChange:false

-- create about_page feature
insert into feature (beta,
	                 name,
	                 featuretype,
	                 enabled,
	                 description,
	                 available_for_user_apps,
	                 default_for_user_apps) 
values (false::boolean, 
	    'about_page', 
	    'UI_PANEL'::feature_type, 
	    true::boolean, 
	    'Application about page', 
	    true::boolean, 
	    false::boolean) 
ON CONFLICT (id) DO NOTHING;

-- add about_page feature to all applications except smart city and OAM
insert into app_feature (app_id, feature_id,configuration) values('4ca51084-0732-4087-b23f-50a88f27ccb2'::uuid, (select id from feature where name = 'about_page' limit 1), null::json) ON CONFLICT (app_id, feature_id ) DO NOTHING;
insert into app_feature (app_id, feature_id,configuration) values('f7372305-d8bb-4dc9-b2bf-57fc1127ce27'::uuid, (select id from feature where name = 'about_page' limit 1), null::json) ON CONFLICT (app_id, feature_id ) DO NOTHING;
insert into app_feature (app_id, feature_id,configuration) values('f9c40236-4310-48e2-9a71-0b586c12dd0c'::uuid, (select id from feature where name = 'about_page' limit 1), null::json) ON CONFLICT (app_id, feature_id ) DO NOTHING;
insert into app_feature (app_id, feature_id,configuration) values('dbdaeb22-c7f6-42ed-8a54-7b05d1d20ad4'::uuid, (select id from feature where name = 'about_page' limit 1), null::json) ON CONFLICT (app_id, feature_id ) DO NOTHING;
insert into app_feature (app_id, feature_id,configuration) values('a4b9fa38-b4a2-4792-97de-8fdb917d3dd8'::uuid, (select id from feature where name = 'about_page' limit 1), null::json) ON CONFLICT (app_id, feature_id ) DO NOTHING;
insert into app_feature (app_id, feature_id,configuration) values('c5ecc65b-1e7e-4e31-92a4-222fadeaeef0'::uuid, (select id from feature where name = 'about_page' limit 1), null::json) ON CONFLICT (app_id, feature_id ) DO NOTHING;
insert into app_feature (app_id, feature_id,configuration) values('f70488c2-055c-4599-a080-ded10c47730f'::uuid, (select id from feature where name = 'about_page' limit 1), null::json) ON CONFLICT (app_id, feature_id ) DO NOTHING;
insert into app_feature (app_id, feature_id,configuration) values('8906feaf-fc18-4180-bb5f-ff545cf65100'::uuid, (select id from feature where name = 'about_page' limit 1), null::json) ON CONFLICT (app_id, feature_id ) DO NOTHING;
insert into app_feature (app_id, feature_id,configuration) values('58851b50-9574-4aec-a3a6-425fa18dcb54'::uuid, (select id from feature where name = 'about_page' limit 1), null::json) ON CONFLICT (app_id, feature_id ) DO NOTHING;

-- add about_page feature for users that have a special config for applications except smart city and OAM
insert into app_user_feature (select a.app_id, 
	                                 a.user_id, 
	                                 (select id from feature where name = 'about_page' limit 1) 
	                          from (select distinct app_id, 
	                          	                    user_id 
	                          	    from app_user_feature 
	                          	    where app_id not in ('634f23f5-f898-4098-a8bd-09eb7c1e1ae5'::uuid, '1dc6fe68-8802-4672-868d-7f17943bf1c8'::uuid)) a);