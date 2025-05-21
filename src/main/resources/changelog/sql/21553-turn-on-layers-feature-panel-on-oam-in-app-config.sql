--liquibase formatted sql

--changeset user-profile-api:21553-turn-on-layers-feature-panel-on-oam-in-app-config.sql runOnChange:false

insert into custom_app_feature (app_id, feature_id, authenticated)
select '1dc6fe68-8802-4672-868d-7f17943bf1c8', f.id, false
from feature f
where f.name in ('layer_features_panel')
on conflict do nothing;

update custom_app_feature
set configuration = '{
    "layerId": "openaerialmap",
    "requiresEnabledLayer": true,
    "requiresGeometry": false,
    "maxItems": 1000,
    "sortOrder": "desc",
    "showBboxFilterToggle": true
  }'
where app_id = '1dc6fe68-8802-4672-868d-7f17943bf1c8'
  and feature_id in (select f.id from feature f where f.name = 'layer_features_panel')
  and not authenticated;
