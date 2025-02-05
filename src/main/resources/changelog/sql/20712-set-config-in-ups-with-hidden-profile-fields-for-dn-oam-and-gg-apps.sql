--liquibase formatted sql

--changeset user-profile-api:20712-set-config-in-ups-with-hidden-profile-fields-for-dn-oam-and-gg-apps.sql runOnChange:false

--delete not needed features
delete from feature where name in ('phone_number', 'linkedin', 'organization', 'position', 'gis_specialists')
on conflict do nothing;

--delete not needed features from apps
delete from custom_app_feature where f.name in ('phone_number', 'linkedin', 'organization', 'position', 'gis_specialists')
on conflict do nothing;

--set config on Disaster Ninja
update custom_app_feature
set configuration = '{
            "name": "app_profile",
            "description": "App profile",
            "type": "UI_PAGE",
            "configuration": {
                "profile_form": {
                    "organization_section": false,
                    "phone": false,
                    "linkedin": false
                }
            }
        }'
where app_id = '58851b50-9574-4aec-a3a6-425fa18dcb54'
  and feature_id in (select f.id from feature f where f.name = 'app_login')
  and not authenticated;

--set config on OpenAerialMap
update custom_app_feature
set configuration = '{
            "name": "app_profile",
            "description": "App profile",
            "type": "UI_PAGE",
            "configuration": {
                "profile_form": {
                    "organization_section": false,
                    "phone": false,
                    "linkedin": false
                }
            }
        }'
where app_id = '1dc6fe68-8802-4672-868d-7f17943bf1c8'
  and feature_id in (select f.id from feature f where f.name = 'app_login')
  and not authenticated;

--set config on Crisis Monitoring for GlobalGiving
update custom_app_feature
set configuration = '{
            "name": "app_profile",
            "description": "App profile",
            "type": "UI_PAGE",
            "configuration": {
                "profile_form": {
                    "organization_section": false,
                    "phone": false,
                    "linkedin": false
                }
            }
        }'
where app_id = '52b9efd2-0527-4236-9bb6-9677bea1d790'
  and feature_id in (select f.id from feature f where f.name = 'app_login')
  and not authenticated;
