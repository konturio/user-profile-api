insert into feature
    (id, featuretype, name, beta, enabled, is_public, description)
values
    --state is confirmed in #9023 (current state)
    --event feeds
       (nextval('feature_sequence'), 'EVENT_FEED', 'disaster-ninja-02', false, true, true, 'Default public event feed'),
    --production features (beta = false, enabled = true, is_public = allowed for all users)
       (nextval('feature_sequence'), 'UI_PANEL', 'analytics_panel', false, true, true, 'Analytics panel'),
       (nextval('feature_sequence'), 'UI_PANEL', 'events_list', false, true, true, 'Events list'),
       (nextval('feature_sequence'), 'UI_PANEL', 'map_layers_panel', false, true, true, 'Map Layers panel'),
       (nextval('feature_sequence'), 'UI_PANEL', 'side_bar', false, true, true, 'Sidebar'),
       (nextval('feature_sequence'), 'UI_PANEL', 'current_event', false, true, true, 'Current event'),
       (nextval('feature_sequence'), 'UI_PANEL', 'focused_geometry_layer', false, true, true, 'Focused geometry layer'),
       (nextval('feature_sequence'), 'UI_PANEL', 'layers_in_area', false, true, true, 'Layers in area'),
       (nextval('feature_sequence'), 'UI_PANEL', 'map_ruler', false, true, true, 'Map ruler'),
       (nextval('feature_sequence'), 'UI_PANEL', 'toasts', false, true, true, 'Toasts'),
       (nextval('feature_sequence'), 'UI_PANEL', 'boundary_selector', false, true, true, 'Boundary selector'),
       (nextval('feature_sequence'), 'UI_PANEL', 'draw_tools', false, true, true, 'Draw tools'),
       (nextval('feature_sequence'), 'UI_PANEL', 'geometry_uploader', false, true, true, 'Geometry uploader'),
       (nextval('feature_sequence'), 'UI_PANEL', 'legend_panel', false, true, true, 'Legend panel'),
       (nextval('feature_sequence'), 'UI_PANEL', 'reports', false, true, true, 'Reports'),
       (nextval('feature_sequence'), 'UI_PANEL', 'url_store', false, true, true, 'URL store'),
       (nextval('feature_sequence'), 'UI_PANEL', 'interactive_map', false, true, true, 'Interactive map'),
       (nextval('feature_sequence'), 'UI_PANEL', 'current_episode', false, true, true, 'Current episode'),
       (nextval('feature_sequence'), 'UI_PANEL', 'geocoder', false, true, true, 'Geocoder'),
       (nextval('feature_sequence'), 'UI_PANEL', 'episode_list', false, true, true, 'Episode list'),
       (nextval('feature_sequence'), 'UI_PANEL', 'communities', false, true, true, 'Communities'),
       (nextval('feature_sequence'), 'UI_PANEL', 'feature_settings', false, true, true, 'Feature settings'),
       (nextval('feature_sequence'), 'UI_PANEL', 'osm_edit_link', false, true, true, 'OSM Edit Link'),
       (nextval('feature_sequence'), 'UI_PANEL', 'tooltip', false, true, true, 'Tooltip'),
    --disabled for now
       (nextval('feature_sequence'), 'UI_PANEL', 'app_login', true, false, false, 'App login'),
       (nextval('feature_sequence'), 'UI_PANEL', 'bivariate_manager', true, false, false, 'Bivariate manager'),
       (nextval('feature_sequence'), 'UI_PANEL', 'feed_selector', true, false, false, 'Feed selector')
;