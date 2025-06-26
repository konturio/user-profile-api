--liquibase formatted sql

--changeset user-profile-service:21792-create-embedded-app-with-population-and-analytics-panel.sql runOnChange:false

--create embedded app
insert into app (id, name, description, owner_user_id, is_public, extent)
values ('b8e7eb02-ca92-4bad-affa-0c5afdb5d8b8', 'Population embedded with analytics', 'Embedded application with population and Analytics panel', null, true, '{-180,-80,180,80}')
  on conflict do nothing;

--add guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select 'b8e7eb02-ca92-4bad-affa-0c5afdb5d8b8', f.id, false
from feature f
where f.name in ('map', 'toolbar', 'analytics_panel', 'boundary_selector', 'layers_in_area', 'focused_geometry_layer')
  on conflict do nothing;

--configurate analytics panel 
update custom_app_feature
set configuration = '{"statistics": [{
  "formula": "sumX",
  "x": "population"
}, {
  "formula": "sumX",
  "x": "area_km2"
}]}'
where app_id = 'b8e7eb02-ca92-4bad-affa-0c5afdb5d8b8'
  and feature_id in (select f.id from feature f where f.name = 'analytics_panel')
  and not authenticated;
