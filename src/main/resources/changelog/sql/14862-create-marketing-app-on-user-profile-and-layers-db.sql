--liquibase formatted sql

--changeset user-profile-service:14862-create-marketing-app-on-user-profile-and-layers-db.sql runOnChange:false

insert into app (id, name, description, is_public)
values ('f70488c2-055c-4599-a080-ded10c47730f', 'Marketing_Embedded', 'Marketing_Embedded', true)
on conflict (id) do nothing;

insert into app_feature (app_id, feature_id)
select 'f70488c2-055c-4599-a080-ded10c47730f', f.id
from feature f
where f.name in ('map_layers_panel', 'interactive_map', 'map_ruler', 'boundary_selector', 'locate_me')
on conflict (app_id, feature_id) do nothing;
