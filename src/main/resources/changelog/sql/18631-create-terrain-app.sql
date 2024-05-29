--liquibase formatted sql

--changeset user-profile-service:18631-create-terrain-app.sql runOnChange:false

insert into app (id, name, description, owner_user_id, is_public, extent, favicon_pack)
values ('3a433e95-0449-48a3-b4ff-9cffea805c74', 'Terrain', 'Whitelable Finance application as a sales tool for Nick Provenzano', null, true, '{-135,0,63,62}', '{
  "favicon.svg": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/favicon.svg",
  "favicon.ico": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/favicon.ico",
  "apple-touch-icon.png": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/apple-touch-icon.png",
  "icon-192x192.png": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/icon-192x192.png",
  "icon-512x512.png": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/icon-512x512.png"
}'::json);

insert into app_feature (app_id, feature_id)
select '3a433e95-0449-48a3-b4ff-9cffea805c74', f.id
from feature f
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'mcda', 'boundary_selector', 'kontur-public',
                'geometry_uploader', 'focused_geometry_editor', 'side_bar', 'map', 'reports', 'app_login', 
                'about_page', 'analytics_panel', 'llm_analytics', 'advanced_analytics_panel', 'events_list', 'map_layers_panel', 
                'legend_panel', 'episodes_timeline', 'chat_panel', 'feed_selector', 'events_list__bbox_filter', 
                'focused_geometry_layer', 'reference_area', 'layers_in_area', 'use_3rd_party_analytics', 'tooltip', 'toasts');


-- turn on mcda and other necessary features for the selected users
delete from app_user_feature where app_id = '3a433e95-0449-48a3-b4ff-9cffea805c74';

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
                      where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'mcda', 'boundary_selector', 'kontur-public',
                                      'geometry_uploader', 'focused_geometry_editor', 'side_bar', 'map', 'reports', 'app_login', 
                                      'about_page', 'analytics_panel', 'llm_analytics', 'advanced_analytics_panel', 'events_list', 'map_layers_panel', 
                                      'legend_panel', 'episodes_timeline', 'chat_panel', 'feed_selector', 'events_list__bbox_filter', 
                                      'focused_geometry_layer', 'reference_area', 'layers_in_area', 'use_3rd_party_analytics', 'tooltip', 'toasts'))
insert into app_user_feature
  select '3a433e95-0449-48a3-b4ff-9cffea805c74' as app_id,
         users_ids.id                           as user_id,
         features_ids.id                        as feature_id
  from users_ids, features_ids;


--configurate analytics panel 
update app_feature
set configuration = '{"statistics": [{
  "formula": "sumX",
  "x": "population"
}, {
  "formula": "sumX",
  "x": "populated_area_km2"
}]}'
where app_id = '3a433e95-0449-48a3-b4ff-9cffea805c74'
  and feature_id = (select id from feature where name = 'analytics_panel');
