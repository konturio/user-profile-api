--liquibase formatted sql

--changeset user-profile-service:17718-create-mcda-app-for-pdc.sql runOnChange:false

insert into app (id, name, description, owner_user_id, is_public, extent, favicon_pack)
values ('77260743-1da0-445b-8f56-ff6ca8520c55', 'MCDA', 'Application for Multiple Criteria Decision Analysis (MCDA) tool demonstration', null, true, '{-140,30,-45,50}', '{
  "favicon.svg": "/active/static/favicon/mcda-favicon.svg",
  "favicon.ico": "/active/static/favicon/mcda-favicon.ico",
  "apple-touch-icon.png": "/active/static/favicon/mcda-apple-touch-icon.png",
  "icon-192x192.png": "/active/static/favicon/mcda-icon-192x192.png",
  "icon-512x512.png": "/active/static/favicon/mcda-icon-512x512.png"
}'::json);

insert into app_feature (app_id, feature_id)
select '77260743-1da0-445b-8f56-ff6ca8520c55', f.id
from feature f
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'mcda', ' create_layer', 'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 'draw_tools', 'side_bar', 'app_login', 'app_registration', 'map_layers_panel', 'legend_panel', 'focused_geometry_layer', 'geocoder', 'feature_settings', 'layers_in_area', 'use_3rd_party_analytics', 'translation', 'tooltip', 'popup', 'toasts', 'interactive_map', 'url_store', 'share_map', 'cookie_consent_banner', 'about_page');



-- turn on mcda and other necessary features for the selected users
delete from app_user_feature where app_id = '77260743-1da0-445b-8f56-ff6ca8520c55';

with users_ids as (select id 
                  from users 
                  where email in ('atsiatserkina@kontur.io', 
                                  'abaranau@kontur.io', 
                                  'amurashka@kontur.io', 
                                  'a.artyukevich@kontur.io', 
                                  'avalasiuk@kontur.io', 
                                  'darafei@kontur.io', 
                                  'ekarpach@kontur.io', 
                                  'pkrukovich@kontur.io', 
                                  'tgrigoryan@kontur.io', 
                                  'vkozel@kontur.io', 
                                  'nlaptsik@kontur.io', 
                                  'ilosik@kontur.io',
                                  'ahil@kontur.io',
                                  'atarakanov@kontur.io',
                                  'arben@kontur.io',
                                  'curtis@kontur.io',
                                  'hoa@kontur.io',
                                  'nprovenzano@kontur.io',
                                  'tad@kontur.io')),
features_ids as (select id 
                      from feature f
                      where f.name in ('toolbar', 'locate_me', 'map_ruler', 'mcda', ' create_layer', 'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 'draw_tools', 'side_bar', 'app_login', 'app_registration', 'map_layers_panel', 'legend_panel', 'focused_geometry_layer', 'geocoder', 'feature_settings', 'layers_in_area', 'use_3rd_party_analytics', 'translation', 'tooltip', 'popup', 'toasts', 'interactive_map', 'url_store', 'share_map', 'cookie_consent_banner', 'about_page'))
insert into app_user_feature
  select '77260743-1da0-445b-8f56-ff6ca8520c55' as app_id,
         users_ids.id                           as user_id,
         features_ids.id                        as feature_id
  from users_ids, features_ids;