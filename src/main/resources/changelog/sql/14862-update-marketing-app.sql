--liquibase formatted sql

--changeset user-profile-service:14862-update-marketing-app.sql runOnChange:false

insert into app_feature (app_id, feature_id)
select 'f70488c2-055c-4599-a080-ded10c47730f', f.id
from feature f
where f.name in ('url_store', 'layers_in_area', 'legend_panel', 'tooltip')
on conflict (app_id, feature_id) do nothing;

delete
from app_feature
where app_id = 'f70488c2-055c-4599-a080-ded10c47730f'
  and feature_id = (select id from feature where name = 'map_layers_panel');