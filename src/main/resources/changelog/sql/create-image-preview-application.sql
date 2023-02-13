--liquibase formatted sql

--changeset user-profile-service:create-image-preview-application.sql runOnChange:false

insert into app (id, name, description, is_public)
values ('c5ecc65b-1e7e-4e31-92a4-222fadeaeef0', 'Image_Preview', 'Image_Preview', true)
on conflict (id) do nothing;


insert into app_feature (app_id, feature_id)
select 'c5ecc65b-1e7e-4e31-92a4-222fadeaeef0', f.id
from feature f
where f.name in ('current_event', 'layers_in_area', 'interactive_map', 'url_store')
on conflict (app_id, feature_id) do nothing;