--liquibase formatted sql

--changeset user-profile-service:20045-add-more-analytics-to-analytics-panel-in-gg-app.sql runOnChange:false

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
"x": "total_building_count"
}, {
"formula": "sumX",
"x": "industrial_area"
}, {
"formula": "sumX",
"x": "forest"
}]}'
where app_id = '52b9efd2-0527-4236-9bb6-9677bea1d790'
  and feature_id in (select f.id from feature f where f.name = 'analytics_panel')
  and authenticated;
