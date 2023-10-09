update app_feature
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
where app_id = (select id from app where name = 'Disaster Ninja')
  and feature_id = (select id from feature where name = 'analytics_panel');