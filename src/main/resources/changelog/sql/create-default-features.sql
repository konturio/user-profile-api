insert into feature
    (id, featuretype, name, beta, enabled, is_public, description)
values
    --event feeds
       (nextval('feature_sequence'), 'EVENT_FEED', 'disaster-ninja-02', false, true, true, 'Default public event feed'),
       (nextval('feature_sequence'), 'EVENT_FEED', 'test-gdacs', false, true, false, 'Test GDACS event feed'),
--production features (beta = false, enabled = true, is_public = allowed for all users)
       (nextval('feature_sequence'), 'UI_PANEL', 'analytics_panel', false, true, false, 'Analytics panel'),
       (nextval('feature_sequence'), 'UI_PANEL', 'events_list', false, true, false, 'Events list'),
       (nextval('feature_sequence'), 'UI_PANEL', 'map_layers_panel', false, true, true, 'Map Layers panel'),
       (nextval('feature_sequence'), 'UI_PANEL', 'side_bar', false, true, true, 'Sidebar'),
       (nextval('feature_sequence'), 'UI_PANEL', 'bivariate_manager', false, true, false, 'Bivariate manager'),
       (nextval('feature_sequence'), 'UI_PANEL', 'current_event', false, true, true, 'Current event'),
       (nextval('feature_sequence'), 'UI_PANEL', 'focused_geometry_layer', false, true, true, 'Focused geometry layer'),
       (nextval('feature_sequence'), 'UI_PANEL', 'layers_in_area', false, true, true, 'Layers in area'),
       (nextval('feature_sequence'), 'UI_PANEL', 'map_ruler', false, true, true, 'Map ruler'),
       (nextval('feature_sequence'), 'UI_PANEL', 'toasts', false, true, true, 'Toasts'),
       (nextval('feature_sequence'), 'UI_PANEL', 'boundary_selector', false, true, false, 'Boundary selector'),
       (nextval('feature_sequence'), 'UI_PANEL', 'draw_tools', false, true, false, 'Draw tools'),
       (nextval('feature_sequence'), 'UI_PANEL', 'geometry_uploader', false, true, false, 'Geometry uploader'),
       (nextval('feature_sequence'), 'UI_PANEL', 'legend_panel', false, true, false, 'Legend panel'),
       (nextval('feature_sequence'), 'UI_PANEL', 'reports', false, true, false, 'Reports'),
       (nextval('feature_sequence'), 'UI_PANEL', 'url_store', false, true, true, 'URL store'),
--not implemented yet (beta = true, enabled = false, is_public = allowed for all users)
       (nextval('feature_sequence'), 'UI_PANEL', 'interactive_map', true, false, true, 'Interactive map'),
       (nextval('feature_sequence'), 'UI_PANEL', 'current_episode', true, false, true, 'Current episode'),
       (nextval('feature_sequence'), 'UI_PANEL', 'geocoder', true, false, false, 'Geocoder'),
       (nextval('feature_sequence'), 'UI_PANEL', 'episode_list', true, false, false, 'Episode list'),
       (nextval('feature_sequence'), 'UI_PANEL', 'communities', true, false, false, 'Communities'),
       (nextval('feature_sequence'), 'UI_PANEL', 'feature_settings', true, false, false, 'Feature settings')
;