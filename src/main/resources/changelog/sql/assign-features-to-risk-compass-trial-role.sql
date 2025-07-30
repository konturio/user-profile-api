--liquibase formatted sql

--changeset user-profile-api:assign-features-to-risk-compass-trial-role.sql runOnChange:true

insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '2d5af407-9f47-4f03-9d9b-2320ce9d307b',
       caf.feature_id,
       true,
       (select cr.id from custom_role cr where cr.name = 'risk_compass_trial')
from custom_app_feature caf
where caf.role_id = (select cr.id from custom_role cr where cr.name = 'risk_compass_admin');

--configurate analytics panel for trial role
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
  and role_id in (select id from custom_role where name = 'risk_compass_trial');
