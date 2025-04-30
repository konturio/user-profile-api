--liquibase formatted sql

--changeset user-profile-service:21486-create-custom-routes-feature-in-ups.sql runOnChange:false

--create custom routes feature
insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('custom_routes', 'UI_PANEL', true, false, true, false, 'Config of sidebar modes')
on conflict do nothing;

--add custom routes feature to OAM app
insert into custom_app_feature (app_id, feature_id, authenticated)
select '1dc6fe68-8802-4672-868d-7f17943bf1c8', f.id, false
from feature f
where f.name in ('custom_routes')
on conflict do nothing;

--add custom routes feature config for OAM app
update custom_app_feature
set configuration = '{
    "routes": [
      {
        "id": "profile-external",
        "type": "embedded",
        "url": "https://map.openaerialmap.org/#/account/"
      },
      {
        "id": "upload-imagery",
        "type": "embedded",
        "url": "https://map.openaerialmap.org/#/upload"
      },
      {
        "id": "imagery-catalog",
        "type": "embedded",
        "url": "https://stac-browser.maap-project.org/?.language=en"
      }
    ]
}'
where app_id = '1dc6fe68-8802-4672-868d-7f17943bf1c8'
  and feature_id in (select f.id from feature f where f.name = 'custom_routes')
  and not authenticated;

--add cookies page to about_page feature config for OAM app
update custom_app_feature
set configuration = '{
        "tabId": "About",
        "assetUrl": "about.md",
        "subTabs": [
            {
                "tabId": "terms",
                "assetUrl": "terms.md"
            },
            {
                "tabId": "privacy",
                "assetUrl": "privacy.md"
            },
            {
                "tabId": "cookies",
                "assetUrl": "cookies.md"
            }
        ]
    }'
where app_id = '1dc6fe68-8802-4672-868d-7f17943bf1c8'
  and feature_id in (select f.id from feature f where f.name = 'about_page')
  and not authenticated;
