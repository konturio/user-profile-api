--liquibase formatted sql

--changeset user-profile-service:18631-create-terrain-app.sql runOnChange:false

--create Terrain app
insert into app (id, name, description, owner_user_id, is_public, extent, favicon_pack)
values ('3a433e95-0449-48a3-b4ff-9cffea805c74', 'Terrain', 'Whitelable Finance application as a sales tool for Nick Provenzano', null, true, '{-135,0,63,62}', '{
  "favicon.svg": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/favicon.svg",
  "favicon.ico": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/favicon.ico",
  "apple-touch-icon.png": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/apple-touch-icon.png",
  "icon-192x192.png": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/icon-192x192.png",
  "icon-512x512.png": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/icon-512x512.png"
}'::json);

-- create new app role
insert into custom_role (name)
values ('terrain_admin');

-- assign new app role to Kontur employees
insert into user_custom_role (user_id, role_id, started_at)
select u.id, r.id, now()
from users u, custom_role r
where r.name = 'terrain_admin'
    and u.email in ('pkrukovich@kontur.io', 'ahil@kontur.io', 'avalasiuk@kontur.io', 'darafei@kontur.io',
                    'nlaptsik@kontur.io', 'atarakanov@kontur.io', 'a.artyukevich@kontur.io', 'vbondar@kontur.io',
                    'arben@kontur.io', 'tgrigoryan@kontur.io', 'aklopau@kontur.io', 'atsiatserkina@kontur.io',
                    'ekarpach@kontur.io', 'kbakhanko@kontur.io', 'abaranau@kontur.io', 'curtis@kontur.io',
                    'milvari@kontur.io', 'vkozel@kontur.io', 'amurashka@kontur.io', 'hoa@kontur.io',
                    'gdowling@kontur.io', 'nprovenzano@kontur.io', 'tad@kontur.io', 'nharshunova@kontur.io',
                    'achichigin@kontur.io', 'hevans@kontur.io');

-- Terrain app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '3a433e95-0449-48a3-b4ff-9cffea805c74', f.id, false
from feature f
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'boundary_selector', 'geometry_uploader',
                 'focused_geometry_editor', 'side_bar', 'map', 'reports', 'app_login', 'about_page', 'analytics_panel',
                 'events_list', 'map_layers_panel', 'legend_panel', 'episodes_timeline',
                 'chat_panel', 'events_list__bbox_filter', 'current_event', 'focused_geometry_layer', 'layers_in_area',
                 'use_3rdparty_analytics', 'tooltip', 'toasts', 'kontur-public');

-- Terrain app role based features
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '3a433e95-0449-48a3-b4ff-9cffea805c74', f.id, true, r.id
from feature f, custom_role r
where f.name in ('mcda', 'bivariate_manager', 'kontur-public', 'llm_analytics', 'advanced_analytics_panel',                 
                'feed_selector', 'reference_area')
  and r.name = 'terrain_admin';

--configurate analytics panel 
update custom_app_feature
set configuration = '{"statistics": [{
  "formula": "sumX",
  "x": "population"
}, {
  "formula": "sumX",
  "x": "populated_area_km2"
}]}'
where app_id = '3a433e95-0449-48a3-b4ff-9cffea805c74'
  and f.name in (select from feature where f.name = 'analytics_panel');
