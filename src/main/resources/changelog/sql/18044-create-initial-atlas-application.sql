--liquibase formatted sql

--changeset user-profile-service:18044-create-initial-atlas-application.sql runOnChange:false

insert into app (id, name, description, owner_user_id, is_public, extent, favicon_pack)
values ('9043acf9-2cf3-48ac-9656-a5d7c4b7593d', 'Kontur Atlas', 'Kontur SAAS application', null, true, '{-135,0,63,62}', '{
  "favicon.svg": "/active/static/favicon/atlas-favicon.svg",
  "favicon.ico": "/active/static/favicon/atlas-favicon.ico",
  "apple-touch-icon.png": "/active/static/favicon/atlas-apple-touch-icon.png",
  "icon-192x192.png": "/active/static/favicon/atlas-icon-192x192.png",
  "icon-512x512.png": "/active/static/favicon/atlas-icon-512x512.png"
}'::json);

insert into app_feature (app_id, feature_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id
from feature f
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'mcda', ' create_layer', 'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 'draw_tools', 'side_bar', 'app_login', 'app_registration', 'map_layers_panel', 'legend_panel', 'focused_geometry_layer', 'geocoder', 'feature_settings', 'layers_in_area', 'use_3rd_party_analytics', 'translation', 'tooltip', 'popup', 'toasts', 'interactive_map', 'url_store', 'share_map', 'cookie_consent_banner', 'about_page', 'analytics_panel', 'advanced_analytics_panel');



-- turn on mcda and other necessary features for the selected users
delete from app_user_feature where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d';

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
                                  'tad@kontur.io',
                                  'aklopau@kontur.io',
                                  'kbakhanko@kontur.io',
                                  'milvari@kontur.io',
                                  'vbondar@kontur.io')),
features_ids as (select id 
                      from feature f
                      where f.name in ('toolbar', 'locate_me', 'map_ruler', 'mcda', ' create_layer', 'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 'draw_tools', 'side_bar', 'app_login', 'app_registration', 'map_layers_panel', 'legend_panel', 'focused_geometry_layer', 'geocoder', 'feature_settings', 'layers_in_area', 'use_3rd_party_analytics', 'translation', 'tooltip', 'popup', 'toasts', 'interactive_map', 'url_store', 'share_map', 'cookie_consent_banner', 'about_page', 'analytics_panel', 'advanced_analytics_panel'))
insert into app_user_feature
  select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d' as app_id,
         users_ids.id                           as user_id,
         features_ids.id                        as feature_id
  from users_ids, features_ids;
