--liquibase formatted sql

--changeset user-profile-api:19393-upload-odin-icons-on-github-and-ups.sql runOnChange:false

update app
set sidebar_icon_url = '/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/favicon.svg',
  favicon_url = '/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/favicon.ico'
where id ='415e2172-3e94-4749-b714-d37470acf88a'
