--liquibase formatted sql

--changeset user-profile-api:21774-ups-add-app_login-feature-to-oam-app.sql runOnChange:false

--create oam_auth feature
insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('oam_auth', 'UI_PANEL', true, false, true, false, 'OAM auth handler')
on conflict do nothing;

--add oam_auth feature to OAM app
insert into custom_app_feature (app_id, feature_id, authenticated)
select '1dc6fe68-8802-4672-868d-7f17943bf1c8', f.id, false
from feature f
where f.name in ('oam_auth')
on conflict do nothing;

--add oam_auth feature config for OAM app
update custom_app_feature
set configuration = '{ 
      "requiredRoutes": ["profile-external", "upload-imagery"],
      "authUrl": "https://api.openaerialmap.org/oauth/google",
      "sessionCookieName": "oam-session",
      "sessionCheckIntervalMs": 5000,
      "redirectUriParamName": "original_uri"
}'
where app_id = '1dc6fe68-8802-4672-868d-7f17943bf1c8'
  and feature_id in (select f.id from feature f where f.name = 'oam_auth')
  and not authenticated;
