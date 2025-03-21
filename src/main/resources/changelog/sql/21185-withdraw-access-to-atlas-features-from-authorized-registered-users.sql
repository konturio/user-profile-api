--liquibase formatted sql

--changeset user-profile-api:21185-withdraw-access-to-atlas-features-from-authorized-registered-users.sql runOnChange:false

delete from custom_app_feature 
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d' 
  and authenticated
  and role_id is null
  and feature_id in (select id from feature where name in ('osm_edit_link', 'focused_geometry_layer', 'map_ruler', 'boundary_selector', 
  'geometry_uploader', 'legend_panel', 'layers_in_area', 'focused_geometry_editor', 'locate_me', 'mcda', 'toolbar', 'chat_panel', 
  'map', 'reference_area', 'llm_analytics', 'search_locations', 'search_bar', 'admin_boundary_breadcrumbs', 'llm_mcda'));
