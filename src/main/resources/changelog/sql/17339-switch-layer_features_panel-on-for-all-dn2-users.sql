--liquibase formatted sql

--changeset user-profile-api:17339-switch-layer_features_panel-on-for-all-dn2-users.sql runOnChange:false

-- add layer_features_panel feature for users that have a special config for Disaster Ninja application
insert into app_user_feature (select a.app_id, 
                                     a.user_id, 
                                     (select id from feature where name = 'layer_features_panel' limit 1) 
                              from (select distinct app_id, 
                                                    user_id 
                                    from app_user_feature 
                                    where app_id in (select id from app where name = 'Disaster Ninja')) a)
on conflict (app_id, user_id, feature_id) do nothing;

-- switch off beta flag and make this feature available to the user to add it to the embedded map
update feature set beta = false, 
                   available_for_user_apps = false  
        where name = 'layer_features_panel';