--liquibase formatted sql

--changeset user-profile-service:18295-disable-layers-panel-on-atlas.sql runOnChange:false

delete from app_feature
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d'
  and feature_id in (select id from feature where name = 'map_layers_panel');

delete from app_user_feature
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d'
 and feature_id in (select id from feature where name = 'map_layers_panel');
