--liquibase formatted sql

--changeset user-profile-service:16213-create-disasters-application.sql runOnChange:false

insert into app (id, name, description, owner_user_id, is_public, sidebar_icon_url, favicon_url)
values ('8906feaf-fc18-4180-bb5f-ff545cf65100', 'Disasters embedded application',
        'Embedded application with disasters panel to be used in landing pages',
        null, true, '/active/static/favicon/favicon.svg', '/active/static/favicon/favicon.svg')
on conflict (id) do nothing;

insert into app_feature (app_id, feature_id)
select '8906feaf-fc18-4180-bb5f-ff545cf65100', f.id
from feature f
where f.name in ('events_list', 'current_event', 'url_store', 'kontur-public', 'interactive_map',
                 'focused_geometry_layer', 'events_list__bbox_filter', 'layers_in_area')
on conflict (app_id, feature_id) do nothing;