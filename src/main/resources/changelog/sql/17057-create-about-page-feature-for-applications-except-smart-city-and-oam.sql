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
on conflict (name) do nothing;

-- add about_page feature to all applications except smart city and OAM
with cte AS
    (select id                                                         as app_id,
	        (select id from feature where name = 'about_page' limit 1) as feature_id, 
	        null::json                                                 as configuration
	from app
	where name not in ('Smart City', 'OpenAerialMap'))
insert into app_feature (app_id, feature_id,configuration) (select * from cte) on conflict (app_id, feature_id ) do nothing;

-- add about_page feature for users that have a special config for applications except smart city and OAM
insert into app_user_feature (select a.app_id, 
	                                 a.user_id, 
	                                 (select id from feature where name = 'about_page' limit 1) 
	                          from (select distinct app_id, 
	                          	                    user_id 
	                          	    from app_user_feature 
	                          	    where app_id in (select id from app where name not in ('Smart City', 'OpenAerialMap'))) a)
on conflict (app_id, user_id, feature_id) do nothing;