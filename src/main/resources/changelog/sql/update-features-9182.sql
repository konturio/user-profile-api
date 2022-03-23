update feature
set available_for_user_apps = true
where name in
('analytics_panel',
'map_layers_panel',
'side_bar',
'routing',
'translation',
'popup',
'tooltip',
'communities',
'feature_settings',
'geocoder',
'url_store',
'interactive_map',
'focused_geometry_layer',
'layers_in_area',
'map_ruler',
'toasts',
'boundary_selector',
'draw_tools',
'geometry_uploader',
'legend_panel');

update feature
set default_for_user_apps = true
where name in
('interactive_map',
'layers_in_area',
'toasts');