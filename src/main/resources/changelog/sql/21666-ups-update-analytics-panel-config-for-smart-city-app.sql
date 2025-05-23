--liquibase formatted sql

--changeset user-profile-service:21666-ups-update-analytics-panel-config-for-smart-city-app.sql runOnChange:false

update custom_app_feature
set configuration = '{"statistics": [{
  "formula": "sumX",
  "x": "population"
}, {
  "formula": "sumX",
  "x": "area_km2"
}, {
  "formula": "sumX",
  "x": "populated_area_km2"
}, {
  "formula": "sumX",
  "x": "building_count"
}, {
  "formula": "avgX",
  "x": "ghs_avg_building_height"
}, {
  "formula": "maxX",
  "x": "ghs_max_building_height"
}, {
  "formula": "sumX",
  "x": "osm_airports_count"
}, {
  "formula": "sumX",
  "x": "osm_railway_stations_count"
}, {
  "formula": "sumX",
  "x": "osm_banks_count"
}, {
  "formula": "sumX",
  "x": "landmarks_and_outdoors_fsq_count"
}, {
  "formula": "sumX",
  "x": "arts_and_entertainment_fsq_count"
}, {
  "formula": "sumX",
  "x": "business_and_professional_services_fsq_count"
}, {
  "formula": "sumX",
  "x": "coffee_shops_fsq_count"
}, {
  "formula": "sumX",
  "x": "dining_and_drinking_fsq_count"
}, {
  "formula": "sumX",
  "x": "osm_hotels_count"
}, {
  "formula": "sumX",
  "x": "osm_universities_count"
}, {
  "formula": "sumX",
  "x": "osm_colleges_count"
}, {
  "formula": "sumX",
  "x": "osm_schools_count"
}, {
  "formula": "sumX",
  "x": "osm_kindergartens_count"
}]}'  
where app_id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5'
  and feature_id in (select f.id from feature f where f.name = 'analytics_panel')
  and not authenticated;
