--liquibase formatted sql

--changeset user-profile-service:15758-create-odin-app-on-kontur-platform.sql runOnChange:false

--create ODIN app
insert into app (id, name, description, owner_user_id, is_public, extent, domains, favicon_pack)
values ('415e2172-3e94-4749-b714-d37470acf88a', 'ODIN', 'Operational Disaster Incident Network app (NICS 2.0)', null, true, '{-180,-80,180,80}', array['test-odin-kontur-io.k8s-01.konturlabs.com', 'dev-odin-kontur-io.k8s-01.konturlabs.com', 'odin.kontur.io', 'prod-odin-kontur-io.k8s-01.konturlabs.com'], '{
  "favicon.svg": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/favicon.svg",
  "favicon.ico": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/favicon.ico",
  "apple-touch-icon.png": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/apple-touch-icon.png",
  "icon-192x192.png": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/icon-192x192.png",
  "icon-512x512.png": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/icon-512x512.png"
}'::json)
on conflict do nothing;

--create new app roles
insert into custom_role (name)
values ('odin_admin'),
		('odin_demo')
on conflict do nothing;

--assign new app role to Kontur employees
insert into user_custom_role (user_id, role_id, started_at)
select u.id, r.id, now()
from users u, custom_role r
where r.name = 'odin_admin'
    and u.email in ('pkrukovich@kontur.io', 'ahil@kontur.io', 'avalasiuk@kontur.io', 'darafei@kontur.io',
                    'nlaptsik@kontur.io', 'atarakanov@kontur.io', 'a.artyukevich@kontur.io', 'vbondar@kontur.io',
                    'arben@kontur.io', 'tgrigoryan@kontur.io', 'aklopau@kontur.io', 'atsiatserkina@kontur.io',
                    'ekarpach@kontur.io', 'kbakhanko@kontur.io', 'abaranau@kontur.io', 'curtis@kontur.io',
                    'milvari@kontur.io', 'vkozel@kontur.io', 'amurashka@kontur.io', 'hoa@kontur.io',
                    'gdowling@kontur.io', 'nprovenzano@kontur.io', 'tad@kontur.io', 'nharshunova@kontur.io',
                    'achichigin@kontur.io', 'hevans@kontur.io', 'hrubanau@kontur.io', 'akolesen@kontur.io')
on conflict do nothing;

--add ODIN app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '415e2172-3e94-4749-b714-d37470acf88a', f.id, false
from feature f
where f.name in ('app_login', 'side_bar', 'use_3rdparty_analytics', 'tooltip', 'toasts', 'intercom')
on conflict do nothing;

--add ODIN app role based features
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '415e2172-3e94-4749-b714-d37470acf88a', f.id, true, r.id
from feature f, custom_role r
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 
	'map', 'analytics_panel', 'events_list', 'legend_panel', 'episodes_timeline', 'chat_panel', 'feed_selector', 'kontur-public', 
	'events_list__bbox_filter', 'current_event', 'focused_geometry_layer', 'layers_in_area', 'mcda', 'osm_edit_link', 
	'search_locations', 'admin_boundary_breadcrumbs', 'map_layers_panel', 'live_sensor', 'create_layer')
  and r.name in ('odin_admin', 'odin_demo')
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
    "x": "industrial_area"
  }, {
    "formula": "sumX",
    "x": "forest"
  }, {
    "formula": "sumX",
    "x": "volcanos_count"
  }, {
    "formula": "maxX",
    "x": "wildfires"
  }]}'
where app_id = '415e2172-3e94-4749-b714-d37470acf88a'
  and feature_id in (select f.id from feature f where f.name = 'analytics_panel')
  and authenticated;
