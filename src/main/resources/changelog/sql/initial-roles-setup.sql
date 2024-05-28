--liquibase formatted sql

--changeset user-profile-service:initial-roles-setup.sql runOnChange:true

-- Add missing features
insert into feature (beta, name, featuretype, enabled, description, available_for_user_apps, default_for_user_apps, available_for_user_configuration)
values (false, 'episodes_timeline', 'UI_PANEL', true, 'Episodes Timeline', false, false, false),
       (false, 'events_list__bbox_filter', 'UI_PANEL', true, 'BBOX filter in events panel', false, false, false),
       (false, 'toolbar', 'UI_PANEL', true, 'Toolbar', true, false, false)
on conflict do nothing;

delete from custom_app_feature where true;
delete from user_custom_role where true;
delete from custom_role where true;

insert into custom_role (name)
values ('kontur_atlas_edu'),
       ('kontur_atlas_pro'),
       ('kontur_atlas_demo'),
       ('kontur_atlas_admin'),
       ('mcda_demo'),
       ('mcda_admin'),
       ('oasis_admin');

-- Kontur Atlas app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id, false
from feature f
where f.name in ('side_bar', 'app_login', 'subscription', 'about_page', 'use_3rdparty_analytics',
                 'tooltip', 'toasts', 'intercom');

-- Kontur Atlas app role based features
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id, true, r.id
from feature f, custom_role r
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'mcda', 'boundary_selector',
                 'geometry_uploader', 'focused_geometry_editor', 'map', 'analytics_panel', 'llm_analytics',
                 'legend_panel', 'chat_panel', 'focused_geometry_layer', 'reference_area', 'layers_in_area')
  and r.name in ('kontur_atlas_edu', 'kontur_atlas_pro', 'kontur_atlas_demo', 'kontur_atlas_admin');

-- Disaster Ninja app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '58851b50-9574-4aec-a3a6-425fa18dcb54', f.id, false
from feature f
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'boundary_selector', 'geometry_uploader',
                 'focused_geometry_editor', 'side_bar', 'map', 'reports', 'app_login', 'about_page', 'analytics_panel',
                 'events_list', 'map_layers_panel', 'legend_panel', 'episodes_timeline', 'layer_features_panel',
                 'chat_panel', 'events_list__bbox_filter', 'current_event', 'focused_geometry_layer', 'layers_in_area',
                 'use_3rdparty_analytics', 'tooltip', 'toasts', 'intercom', 'kontur-public');

-- Disaster Ninja app authenticated features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '58851b50-9574-4aec-a3a6-425fa18dcb54', f.id, true
from feature f
where f.name in ('feed_selector');

-- Smart City app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '634f23f5-f898-4098-a8bd-09eb7c1e1ae5', f.id, false
from feature f
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'boundary_selector', 'geometry_uploader',
                 'focused_geometry_editor', 'side_bar', 'map', 'app_login', 'about_page', 'analytics_panel',
                 'map_layers_panel', 'legend_panel', 'focused_geometry_layer', 'layers_in_area',
                 'use_3rdparty_analytics', 'tooltip', 'toasts', 'intercom');

-- Smart City app authenticated features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '634f23f5-f898-4098-a8bd-09eb7c1e1ae5', f.id, true
from feature f
where f.name in ('live_sensor');

-- MCDA app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '77260743-1da0-445b-8f56-ff6ca8520c55', f.id, false
from feature f
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'boundary_selector', 'geometry_uploader',
                 'focused_geometry_editor', 'side_bar', 'map', 'app_login', 'legend_panel', 'focused_geometry_layer',
                 'layers_in_area', 'use_3rdparty_analytics', 'tooltip', 'toasts');

-- MCDA app role based features
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '77260743-1da0-445b-8f56-ff6ca8520c55', f.id, true, r.id
from feature f, custom_role r
where f.name in ('mcda', 'analytics_panel', 'llm_analytics', 'reference_area')
  and r.name in ('mcda_demo', 'mcda_admin');

-- OAM app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '1dc6fe68-8802-4672-868d-7f17943bf1c8', f.id, false
from feature f
where f.name in ('toolbar', 'locate_me', 'map_ruler', 'osm_edit_link', 'side_bar', 'map', 'app_login', 'about_page',
                 'layers_in_area', 'tooltip', 'toasts');

-- Image Preview app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select 'c5ecc65b-1e7e-4e31-92a4-222fadeaeef0', f.id, false
from feature f
where f.name in ('map', 'current_event', 'focused_geometry_layer', 'layers_in_area', 'kontur-public');

-- Marketing Embedded app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select 'f70488c2-055c-4599-a080-ded10c47730f', f.id, false
from feature f
where f.name in ('map', 'layers_in_area');

-- Solar Farm Marketing app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select 'e27c72f1-ed08-4cdc-b86a-f0278a256be7', f.id, false
from feature f
where f.name in ('map', 'layers_in_area');

-- Disasters embedded app guest features
insert into custom_app_feature (app_id, feature_id, authenticated)
select '8906feaf-fc18-4180-bb5f-ff545cf65100', f.id, false
from feature f
where f.name in ('map', 'events_list', 'events_list__bbox_filter', 'current_event', 'focused_geometry_layer',
                 'layers_in_area', 'kontur-public');

-- Kontur employees roles
insert into user_custom_role (user_id, role_id)
select u.id, r.id
from users u, custom_role r
where r.name in ('kontur_atlas_admin', 'mcda_admin', 'oasis_admin')
    and u.email in ('pkrukovich@kontur.io', 'ahil@kontur.io', 'avalasiuk@kontur.io', 'darafei@kontur.io',
                    'nlaptsik@kontur.io', 'atarakanov@kontur.io', 'a.artyukevich@kontur.io', 'vbondar@kontur.io',
                    'arben@kontur.io', 'tgrigoryan@kontur.io', 'aklopau@kontur.io', 'atsiatserkina@kontur.io',
                    'ekarpach@kontur.io', 'kbakhanko@kontur.io', 'abaranau@kontur.io', 'curtis@kontur.io',
                    'milvari@kontur.io', 'vkozel@kontur.io', 'amurashka@kontur.io', 'hoa@kontur.io',
                    'gdowling@kontur.io', 'nprovenzano@kontur.io', 'tad@kontur.io', 'nharshunova@kontur.io',
                    'achichigin@kontur.io', 'hevans@kontur.io');

insert into user_custom_role (user_id, role_id)
select u.id, r.id
from users u, custom_role r
where r.name in ('kontur_atlas_demo', 'mcda_demo')
    and u.email in ('', '');

update custom_app_feature caf
set configuration = af.configuration
from app_feature af
where caf.app_id = af.app_id
  and caf.feature_id = af.feature_id
  and caf.authenticated = false;

insert into custom_app_feature (app_id, feature_id, authenticated, role_id, configuration_for_user_id, configuration)
select
    auf.app_id,
    auf.feature_id,
    true,
    null,
    auf.user_id,
    auf.configuration
from app_user_feature auf
where auf.configuration is not null;
