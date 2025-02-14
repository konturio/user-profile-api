--liquibase formatted sql

--changeset user-profile-api:20756-provide-all-authorized-registered-users-with-access-to-all-atlas-features.sql runOnChange:false

insert into custom_app_feature (app_id, feature_id, authenticated)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id, true
from feature f
where f.name in ('osm_edit_link', 'focused_geometry_layer', 'map_ruler', 'boundary_selector', 'geometry_uploader', 'legend_panel', 'layers_in_area', 
'focused_geometry_editor', 'locate_me', 'mcda', 'toolbar', 'chat_panel', 'map', 'reference_area', 'llm_analytics', 'search_locations', 'search_bar', 
'admin_boundary_breadcrumbs', 'llm_mcda');
