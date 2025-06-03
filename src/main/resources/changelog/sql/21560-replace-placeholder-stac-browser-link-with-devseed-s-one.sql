--liquibase formatted sql

--changeset user-profile-service:21560-replace-placeholder-stac-browser-link-with-devseed-s-one.sql runOnChange:false

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
        "url": "https://hot-oam.ds.io/browser/?.language=en"
      }
    ]
}'
where app_id = '1dc6fe68-8802-4672-868d-7f17943bf1c8'
  and feature_id in (select f.id from feature f where f.name = 'custom_routes')
  and not authenticated;
