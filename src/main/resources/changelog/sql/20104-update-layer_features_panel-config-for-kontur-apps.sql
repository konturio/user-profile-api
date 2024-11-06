--liquibase formatted sql

--changeset user-profile-api:20104-update-layer_features_panel-config-for-kontur-apps.sql runOnChange:false

--Crisis Monitoring
update custom_app_feature
set configuration = '{
   "layerId": "acaps_simple",
   "requiresEnabledLayer": false
}'
where app_id = '52b9efd2-0527-4236-9bb6-9677bea1d790'
  and feature_id in (select f.id from feature f where f.name = 'layer_features_panel')
  and authenticated;

-- Disaster Ninja
update custom_app_feature
set configuration = '{
   "layerId": "hotProjects_outlines",
   "requiresEnabledLayer": true
}'
where app_id = '58851b50-9574-4aec-a3a6-425fa18dcb54'
  and feature_id in (select f.id from feature f where f.name = 'layer_features_panel')
  and not authenticated;

-- Oasis
update custom_app_feature
set configuration = '{
   "layerId": "hotProjects_outlines",
   "requiresEnabledLayer": true
}'
where app_id = '0b5b4047-3d9b-4ec4-993f-acf9c7315536'
  and feature_id in (select f.id from feature f where f.name = 'layer_features_panel')
  and authenticated;
