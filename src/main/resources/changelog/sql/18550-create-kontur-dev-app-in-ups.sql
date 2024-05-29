--liquibase formatted sql

--changeset user-profile-service:18550-create-kontur-dev-app-in-ups.sql runOnChange:false

insert into app (id, name, description, owner_user_id, is_public, extent, favicon_pack)
values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'Oasis', 'Application for development team with all set of features', null, true, '{41, 41, 41, 41}', '{
  "favicon.svg": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/favicon.svg",
  "favicon.ico": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/favicon.ico",
  "apple-touch-icon.png": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/apple-touch-icon.png",
  "icon-192x192.png": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/icon-192x192.png",
  "icon-512x512.png": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/icon-512x512.png"
}'::json);

insert into custom_app_feature (app_id, feature_id, authenticated)
select '0b5b4047-3d9b-4ec4-993f-acf9c7315536', f.id, false
from feature f
where f.name in ('side_bar', 'app_login', 'subscription', 'about_page', 'use_3rdparty_analytics',
                 'tooltip', 'toasts', 'intercom');

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
  and f.name = (select id from feature where name = 'analytics_panel');
