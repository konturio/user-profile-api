--liquibase formatted sql

--changeset user-profile-service:21414-create-risk-assessment-app.sql runOnChange:false

--create Risk Assessment app
insert into app (id, name, description, owner_user_id, is_public, sidebar_icon_url, favicon_url, extent, domains, favicon_pack)
values ('2d5af407-9f47-4f03-9d9b-2320ce9d307b', 'Risk Compass', 'Application for risk assessment, mostly for insurance companies', null, true, 
  '/active/api/apps/2d5af407-9f47-4f03-9d9b-2320ce9d307b/assets/favicon.svg', '/active/api/apps/2d5af407-9f47-4f03-9d9b-2320ce9d307b/assets/favicon.ico', 
  '{-135,0,63,62}', array['dev-risks-kontur-io.k8s-01.konturlabs.com', 'test-risks-kontur-io.k8s-01.konturlabs.com', 'prod-risks-kontur-io.k8s-01.konturlabs.com', 'risks.kontur.io'], '{
  "favicon.svg": "/active/api/apps/2d5af407-9f47-4f03-9d9b-2320ce9d307b/assets/favicon.svg",
  "favicon.ico": "/active/api/apps/2d5af407-9f47-4f03-9d9b-2320ce9d307b/assets/favicon.ico",
  "apple-touch-icon.png": "/active/api/apps/2d5af407-9f47-4f03-9d9b-2320ce9d307b/assets/apple-touch-icon.png",
  "icon-192x192.png": "/active/api/apps/2d5af407-9f47-4f03-9d9b-2320ce9d307b/assets/icon-192x192.png",
  "icon-512x512.png": "/active/api/apps/2d5af407-9f47-4f03-9d9b-2320ce9d307b/assets/icon-512x512.png"
}'::json)
on conflict do nothing;

--create new app role
insert into custom_role (name)
values ('risk_compass_admin')
on conflict do nothing;

--add guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '2d5af407-9f47-4f03-9d9b-2320ce9d307b', f.id, false
from feature f
where f.name in ('side_bar', 'app_login', 'use_3rdparty_analytics',
                 'tooltip', 'toasts', 'intercom')
on conflict do nothing;

--add role based features
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '2d5af407-9f47-4f03-9d9b-2320ce9d307b', f.id, true, r.id
from feature f, custom_role r
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'mcda', 'kontur-public',
                'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 'map', 'analytics_panel',  
                'events_list', 'map_layers_panel', 'legend_panel', 
                'episodes_timeline', 'feed_selector', 'events_list__bbox_filter', 'current_event',
                'focused_geometry_layer', 'layers_in_area')
  and r.name = 'risk_compass_admin'
  on conflict do nothing;

--configurate analytics panel 
update custom_app_feature
set configuration = '{"statistics": [{
  "formula": "sumX",
  "x": "population"
}, {
  "formula": "sumX",
  "x": "area_km2"
}, {
  "formula": "avgX",
  "x": "ghs_avg_building_height"
}, {
  "formula": "sumX",
  "x": "highway_length"
}, {
  "formula": "sumX",
  "x": "railway_length"
}, {
  "formula": "sumX",
  "x": "osm_airports_count"
}, {
  "formula": "maxX",
  "x": "hazardous_days_count"
}, {
  "formula": "maxX",
  "x": "cyclone_days_count"
}, {
  "formula": "maxX",
  "x": "flood_days_count"
}, {
  "formula": "maxX",
  "x": "wildfire_days_count"
}]}'  
where app_id = '2d5af407-9f47-4f03-9d9b-2320ce9d307b'
  and feature_id in (select f.id from feature f where f.name = 'analytics_panel')
  and authenticated;
