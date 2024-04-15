--liquibase formatted sql

--changeset user-profile-service:18295-disable-layers-panel-on-atlas.sql runOnChange:false

delete from app_feature (app_id, feature_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', id
from feature
where name = 'map_layers_panel';

delete from app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id, (select id from feature where name = 'map_layers_panel') from app_user_feature
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d';
