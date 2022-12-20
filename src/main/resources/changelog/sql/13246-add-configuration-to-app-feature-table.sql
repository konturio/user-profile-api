ALTER TABLE app_feature
    ADD COLUMN IF NOT EXISTS configuration JSON;

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
  "formula": "sumXWhereNoY",
  "x": "populated_area_km2",
  "y": "building_count"
}, {
  "formula": "sumXWhereNoY",
  "x": "populated_area_km2",
  "y": "highway_length"
}]}' from feature
where app_feature.app_id = '58851b50-9574-4aec-a3a6-425fa18dcb54'
  and feature.name = 'analytics_panel'
  and app_feature.feature_id = feature.id;

update app_feature
set configuration = '{"statistics": [{
  "formula": "sumX",
  "x": "population"
}, {
  "formula": "sumX",
  "x": "populated_area_km2"
}]}' from feature
where app_feature.app_id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5'
  and feature.name = 'analytics_panel'
  and app_feature.feature_id = feature.id;