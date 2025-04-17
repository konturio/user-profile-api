--liquibase formatted sql

--changeset user-profile-service:21414-create-risk-assessment-app.sql runOnChange:false

--create Risk Assessment app
insert into app (id, name, description, owner_user_id, is_public, sidebar_icon_url, favicon_url, extent, domains, favicon_pack)
values ('2d5af407-9f47-4f03-9d9b-2320ce9d307b', 'Risk Assessment', 'Application for risk assessment, mostly for insurance companies', null, true, 
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
values ('risk_assessment_admin')
on conflict do nothing;

--add guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '2d5af407-9f47-4f03-9d9b-2320ce9d307b', f.id, false
from feature f
where f.name in ('side_bar', 'app_login', 'subscription', 'use_3rdparty_analytics',
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
  and r.name = 'risk_assessment_admin'
  on conflict do nothing;

--configurate analytics panel 
update custom_app_feature
set configuration = '{"statistics": [{
  "formula": "sumX",
  "x": "population"
}, {
  "formula": "sumX",
  "x": "building_count"
}, {
  "formula": "sumX",
  "x": "man_distance_to_fire_brigade"
}, {
  "formula": "sumX",
  "x": "man_distance_to_hospital"
}, {
  "formula": "sumX",
  "x": "hazardous_days_count"
}, {
  "formula": "sumX",
  "x": "drought_days_count"
}, {
  "formula": "sumX",
  "x": "earthquake_days_count"
}, {
  "formula": "sumX",
  "x": "cyclone_days_count"
}, {
  "formula": "sumX",
  "x": "flood_days_countt"
}, {
  "formula": "sumX",
  "x": "volcano_days_count"
}, {
  "formula": "sumX",
  "x": "wildfire_days_count"
}, {
  "formula": "sumX",
  "x": "permanent_water"
}]}'  
where app_id = '2d5af407-9f47-4f03-9d9b-2320ce9d307b'
  and feature_id in (select f.id from feature f where f.name = 'analytics_panel')
  and authenticated
  and role_id = (select r.id from custom_role r where r.name = 'risk_assessment_admin');

-- Assign admin roles for Risk Assessment app
insert into user_custom_role (user_id, role_id, started_at)
select u.id, r.id, now()
from users u, custom_role r
where r.name in ('risk_assessment_admin')
    and u.email in ('pkrukovich@kontur.io', 'ahil@kontur.io', 'avalasiuk@kontur.io', 'darafei@kontur.io',
                    'nlaptsik@kontur.io', 'atarakanov@kontur.io', 'a.artyukevich@kontur.io', 'vbondar@kontur.io',
                    'arben@kontur.io', 'tgrigoryan@kontur.io', 'aklopau@kontur.io', 'atsiatserkina@kontur.io',
                    'ekarpach@kontur.io', 'kbakhanko@kontur.io', 'abaranau@kontur.io', 'curtis@kontur.io',
                    'milvari@kontur.io', 'vkozel@kontur.io', 'amurashka@kontur.io', 'hoa@kontur.io',
                    'nprovenzano@kontur.io', 'tad@kontur.io', 'nharshunova@kontur.io',
                    'achichigin@kontur.io', 'thomas@kontur.io', 'abasalai@kontur.io', 'gorelick@kontur.io');
