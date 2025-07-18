--liquibase formatted sql

--changeset user-profile-service:22241-create-embedded-app-for-layer-showcase.sql runOnChange:false

--create embedded app
insert into app (id, name, description, owner_user_id, is_public, extent)
values ('d35212d6-fd40-45a0-837e-82c03225cae9', 'Embedded layer showcase', 'Embedded app with showcase of Kontur layers', null, true, '{-180,-80,180,80}')
  on conflict do nothing;

--add features
insert into custom_app_feature (app_id, feature_id, authenticated)
select 'd35212d6-fd40-45a0-837e-82c03225cae9', f.id, false
from feature f
where f.name in ('map', 'layers_in_area', 'legend_panel')
  on conflict do nothing;
