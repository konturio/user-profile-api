--liquibase formatted sql

--changeset user-profile-api:18209-create-reference-area-feature.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description) values
    ('reference_area', 'UI_PANEL', true, false, true, false, 'Reference area layer which user creates and which is kept in user profile and used for comparison of analytics with other areas like selected area');

with cte AS
    (select id                                                         as app_id,
	        (select id from feature where name = 'reference_area' limit 1) as feature_id, 
	        null::json                                                 as configuration
	from app
	where app_id in ('58851b50-9574-4aec-a3a6-425fa18dcb54', '634f23f5-f898-4098-a8bd-09eb7c1e1ae5', 'c5ecc65b-1e7e-4e31-92a4-222fadeaeef0', '77260743-1da0-445b-8f56-ff6ca8520c55', '9043acf9-2cf3-48ac-9656-a5d7c4b7593d'))
insert into app_feature (app_id, feature_id,configuration) (select * from cte) on conflict (app_id, feature_id ) do nothing;

insert into app_user_feature (select a.app_id, 
	                                 a.user_id, 
	                                 (select id from feature where name = 'reference_area' limit 1) 
	                          from (select distinct app_id, 
	                          	                    user_id 
	                          	    from app_user_feature 
	                          	    where app_id in (select id from app where app_id in ('58851b50-9574-4aec-a3a6-425fa18dcb54', '634f23f5-f898-4098-a8bd-09eb7c1e1ae5', 'c5ecc65b-1e7e-4e31-92a4-222fadeaeef0', '77260743-1da0-445b-8f56-ff6ca8520c55', '9043acf9-2cf3-48ac-9656-a5d7c4b7593d'))) a)
on conflict (app_id, user_id, feature_id) do nothing;
