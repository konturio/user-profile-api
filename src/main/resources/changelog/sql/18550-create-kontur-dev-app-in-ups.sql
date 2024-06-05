--liquibase formatted sql

--changeset user-profile-service:18550-create-kontur-dev-app-in-ups.sql runOnChange:false

--create Oasis app
insert into app (id, name, description, owner_user_id, is_public, extent, favicon_pack)
values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'Oasis', 'Application for development team with all set of features', null, true, '{41, 41, 41, 41}', '{
  "favicon.svg": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/favicon.svg",
  "favicon.ico": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/favicon.ico",
  "apple-touch-icon.png": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/apple-touch-icon.png",
  "icon-192x192.png": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/icon-192x192.png",
  "icon-512x512.png": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/icon-512x512.png"
}'::json);

-- Add guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '0b5b4047-3d9b-4ec4-993f-acf9c7315536', f.id, false
from feature f
where f.name in ('side_bar', 'app_login', 'subscription', 'about_page', 'use_3rdparty_analytics',
                 'tooltip', 'toasts', 'intercom');

-- Add role based features
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '0b5b4047-3d9b-4ec4-993f-acf9c7315536', f.id, true, r.id
from feature f, custom_role r
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'live_sensor', 'mcda', 'bivariate_manager', 'create_layer', 'kontur-public',
                'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 'map', 'analytics_panel', 'llm_analytics', 
                'advanced_analytics_panel', 'events_list', 'map_layers_panel', 'reports', 'bivariate_color_manager', 'legend_panel', 
                'episodes_timeline', 'layer_features_panel', 'chat_panel', 'feed_selector', 'events_list__bbox_filter', 'current_event',
                'focused_geometry_layer', 'reference_area', 'layers_in_area')
  and r.name = 'oasis_admin';

--configurate analytics panel 
update custom_app_feature
set configuration = '{"statistics": [{
  "formula": "sumX",
  "x": "population"
}, {
  "formula": "sumX",
  "x": "populated_area_km2"
}, {
  "formula": "percentageXWhereNoY",
  "x": "populated_area_km2",
  "y": "count"
}, {
  "formula": "sumXWhereNoY",
  "x": "populated_area_km2",
  "y": "count"
}, {
  "formula": "percentageXWhereNoY",
  "x": "populated_area_km2",
  "y": "building_count"
}, {
  "formula": "percentageXWhereNoY",
  "x": "populated_area_km2",
  "y": "highway_length"
}]}'
where app_id = '0b5b4047-3d9b-4ec4-993f-acf9c7315536'
  and feature_id in (select f.id from feature f where f.name = 'analytics_panel')
  and authenticated
  and role_id = (select r.id from custom_role r where r.name = 'oasis_admin');

-- Assign demo roles for Atlas app
insert into user_custom_role (user_id, role_id, started_at, ended_at)
select u.id, r.id, now(), '2024-12-31 23:59:59'
from users u, custom_role r
where r.name in ('kontur_atlas_demo')
    and u.email in ('', '', '');
