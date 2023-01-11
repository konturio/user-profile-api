--liquibase formatted sql

--changeset user-profile-service:13984-update-oam-application.sql runOnChange:false

insert into app_feature (app_id, feature_id)
select '1dc6fe68-8802-4672-868d-7f17943bf1c8', f.id
from feature f
where f.name in ('layers_in_area', 'interactive_map', 'feature_settings');