--liquibase formatted sql

--changeset user-profile-api:17517-enable-layer-feature-panel-for-all-users.sql runOnChange:false

-- This feature was temporary enabled only for particular users, so make it available for everyone
delete from app_user_feature 
where feature_id = (select f.id 
                    from feature f
                    where f.name = 'layer_features_panel');

-- switch off beta flag and make this feature available to the user to add it to the embedded map
update feature set beta = false, 
                   available_for_user_apps = true  
        where name = 'layer_features_panel';