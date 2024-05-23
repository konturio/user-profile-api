--liquibase formatted sql

--changeset user-profile-service:18550-create-kontur-dev-app-in-ups.sql runOnChange:false

insert into app (id, name, description, owner_user_id, is_public, extent, favicon_pack)
values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'Oasis', 'Application for development team with all set of features', null, true, '{41.5633,41.5621,41.7094,41.6904}', '{
  "favicon.svg": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/favicon.svg",
  "favicon.ico": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/favicon.ico",
  "apple-touch-icon.png": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/apple-touch-icon.png",
  "icon-192x192.png": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/icon-192x192.png",
  "icon-512x512.png": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/icon-512x512.png"
}'::json);

insert into app_feature (app_id, feature_id)
select '0b5b4047-3d9b-4ec4-993f-acf9c7315536', f.id
from feature f
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'live_sensor', 'mcda', 'bivariate_manager', 'create_layer', 
                'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 'side_bar', 'map', 'reports', 'app_login', 'bivariate_color_manager', 
                'subscription', 'about_page', 'analytics_panel', 'llm_analytics', 'advanced_analytics_panel', 'events_list', 'map_layers_panel', 
                'legend_panel', 'episodes_timeline', 'layer_features_panel', 'chat_panel', 'feed_selector', 'events_list__bbox_filter', 
                'current_event','focused_geometry_layer', 'reference_area', 'layers_in_area', 'use_3rd_party_analytics', 'tooltip', 'toasts', 'intercom');


-- turn on mcda and other necessary features for the selected users
delete from app_user_feature where app_id = '0b5b4047-3d9b-4ec4-993f-acf9c7315536';

with users_ids as (select id 
                  from users 
                  where email in ('atsiatserkina@kontur.io', 
                                  'abaranau@kontur.io', 
                                  'amurashka@kontur.io', 
                                  'a.artyukevich@kontur.io', 
                                  'avalasiuk@kontur.io', 
                                  'darafei@kontur.io', 
                                  'ekarpach@kontur.io', 
                                  'pkrukovich@kontur.io', 
                                  'tgrigoryan@kontur.io', 
                                  'vkozel@kontur.io', 
                                  'nlaptsik@kontur.io',                                  
                                  'ahil@kontur.io',
                                  'atarakanov@kontur.io',
                                  'achichigin@kontur.io',
                                  'nharshunova@kontur.io',
                                  'lgudyma@kontur.io',
                                  'me@akolesen.pro',
                                  'hevans@kontur.io',
                                  'gdowling@kontur.io',
                                  'arben@kontur.io',
                                  'curtis@kontur.io',
                                  'hoa@kontur.io',
                                  'nprovenzano@kontur.io',
                                  'tad@kontur.io',
                                  'aklopau@kontur.io',
                                  'kbakhanko@kontur.io',
                                  'milvari@kontur.io',
                                  'vbondar@kontur.io',
                                  '',
                                  '')),
features_ids as (select id 
                      from feature f
                      where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'live_sensor', 'mcda', 'bivariate_manager', 'create_layer', 
                                      'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 'side_bar', 'map', 'reports', 'app_login', 
                                      'bivariate_color_manager', 'subscription', 'about_page', 'analytics_panel', 'llm_analytics', 'advanced_analytics_panel', 
                                      'events_list', 'map_layers_panel', 'legend_panel', 'episodes_timeline', 'layer_features_panel', 'chat_panel', 
                                      'feed_selector', 'events_list__bbox_filter', 'current_event','focused_geometry_layer', 'reference_area', 
                                      'layers_in_area', 'use_3rd_party_analytics', 'tooltip', 'toasts', 'intercom'))
insert into app_user_feature
  select '0b5b4047-3d9b-4ec4-993f-acf9c7315536' as app_id,
         users_ids.id                           as user_id,
         features_ids.id                        as feature_id
  from users_ids, features_ids;
