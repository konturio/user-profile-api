--liquibase formatted sql

--changeset user-profile-service:20084-add-upload-geojson-button-to-toolbar-in-gg-app.sql runOnChange:false

insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '52b9efd2-0527-4236-9bb6-9677bea1d790', f.id, true, r.id
from feature f, custom_role r
where f.name in ('geometry_uploader')
  and r.name in ('crysis_monitoring_admin')
on conflict do nothing;
