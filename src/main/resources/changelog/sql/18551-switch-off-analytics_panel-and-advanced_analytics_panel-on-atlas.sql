--liquibase formatted sql

--changeset user-profile-service:18551-switch-off-analytics_panel-and-advanced_analytics_panel-on-atlas.sql runOnChange:false

--switch off analytics_panel
delete from app_feature where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d' and feature_id in (select id from feature where name = 'analytics_panel');
delete from app_user_feature where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d' and feature_id in (select id from feature where name = 'analytics_panel');

--switch off advanced_analytics_panel
delete from app_feature where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d' and feature_id in (select id from feature where name = 'advanced_analytics_panel');
delete from app_user_feature where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d' and feature_id in (select id from feature where name = 'advanced_analytics_panel');
