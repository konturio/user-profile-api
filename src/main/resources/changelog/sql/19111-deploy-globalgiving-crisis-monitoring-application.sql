--liquibase formatted sql

--changeset user-profile-service:19111-deploy-globalgiving-crisis-monitoring-application.sql runOnChange:false

--create GlobalGiving app
insert into app (id, name, description, owner_user_id, is_public, sidebar_icon_url, favicon_url, extent, domains, favicon_pack)
values ('52b9efd2-0527-4236-9bb6-9677bea1d790', 'Crisis Monitoring', 'App for GlobalGiving', null, true, '/active/api/apps/52b9efd2-0527-4236-9bb6-9677bea1d790/assets/favicon.svg', '/active/api/apps/52b9efd2-0527-4236-9bb6-9677bea1d790/assets/favicon.ico', '{-180,-80,180,80}', array['dev-globalgiving-kontur-io.k8s-01.konturlabs.com', 'test-globalgiving-kontur-io.k8s-01.konturlabs.com', 'prod-globalgiving-kontur-io.k8s-01.konturlabs.com', 'globalgiving.kontur.io'], '{
  "favicon.svg": "/active/api/apps/52b9efd2-0527-4236-9bb6-9677bea1d790/assets/favicon.svg",
  "favicon.ico": "/active/api/apps/52b9efd2-0527-4236-9bb6-9677bea1d790/assets/favicon.ico",
  "apple-touch-icon.png": "/active/api/apps/52b9efd2-0527-4236-9bb6-9677bea1d790/assets/apple-touch-icon.png",
  "icon-192x192.png": "/active/api/apps/52b9efd2-0527-4236-9bb6-9677bea1d790/assets/icon-192x192.png",
  "icon-512x512.png": "/active/api/apps/52b9efd2-0527-4236-9bb6-9677bea1d790/assets/icon-512x512.png"
}'::json)
on conflict do nothing;

--create new app role
insert into custom_role (name)
values ('crysis_monitoring_admin')
on conflict do nothing;

--add features for guests
insert into custom_app_feature (app_id, feature_id, authenticated)
select '52b9efd2-0527-4236-9bb6-9677bea1d790', f.id, false
from feature f
where f.name in ('app_login', 'side_bar', 'toasts', 'tooltip', 'intercom', 'use_3rdparty_analytics')
on conflict do nothing;

--add role-based features
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '52b9efd2-0527-4236-9bb6-9677bea1d790', f.id, true, r.id
from feature f, custom_role r
where f.name in ('mcda', 'kontur-public', 'analytics_panel', 'events_list', 'map_layers_panel', 'current_event', 'focused_geometry_layer', 'layers_in_area', 'boundary_selector', 'legend_panel', 'feed_selector', 'focused_geometry_editor', 'events_list__bbox_filter', 'episodes_timeline', 'toolbar', 'map', 'search_locations', 'search_bar', 'layer_features_panel')
  and r.name in ('crysis_monitoring_admin')
on conflict do nothing;

--configurate analytics panel 
update custom_app_feature
set configuration = '{"statistics": [{
"formula": "sumX",
"x": "population"
}, {
"formula": "sumX",
"x": "populated_area_km2"
}, {
"formula": "sumX",
"x": "total_building_count"
}, {
"formula": "sumX",
"x": "industrial_area"
}]}'
where app_id = '52b9efd2-0527-4236-9bb6-9677bea1d790'
  and feature_id in (select f.id from feature f where f.name = 'analytics_panel')
  and authenticated;
  
--configurate layer features panel 
update custom_app_feature
set configuration = '{
   "layerId": "acaps_simple"
}'
where app_id = '52b9efd2-0527-4236-9bb6-9677bea1d790'
  and feature_id in (select f.id from feature f where f.name = 'layer_features_panel')
  and authenticated;
