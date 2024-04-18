--liquibase formatted sql

--changeset user-profile-service:18323-temporarily-switch-off-about-feature-for-kontur-apps.sql runOnChange:false

delete from app_feature
where app_id != '58851b50-9574-4aec-a3a6-425fa18dcb54'
  and feature_id in (select id from feature where name = 'about_page');

delete from app_user_feature
where app_id != '58851b50-9574-4aec-a3a6-425fa18dcb54'
 and feature_id in (select id from feature where name = 'about_page');
