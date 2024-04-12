--liquibase formatted sql

--changeset user-profile-api:18209-create-reference-area-feature.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description) values
    ('reference_area', 'UI_PANEL', true, false, true, false, 'Reference area layer');

with cte AS
    (select id                                                         as app_id,
	        (select id from feature where name = 'reference_area' limit 1) as feature_id, 
	        null::json                                                 as configuration
	from app
	where name not in ('OpenAerialMap'))
insert into app_feature (app_id, feature_id,configuration) (select * from cte) on conflict (app_id, feature_id ) do nothing;

insert into app_user_feature (select a.app_id, 
	                                 a.user_id, 
	                                 (select id from feature where name = 'reference_area' limit 1) 
	                          from (select distinct app_id, 
	                          	                    user_id 
	                          	    from app_user_feature 
	                          	    where app_id in (select id from app where name not in ('OpenAerialMap'))) a)
on conflict (app_id, user_id, feature_id) do nothing;
