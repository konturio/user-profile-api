--liquibase formatted sql

--changeset user-profile-service:19104-add-about_page-feature-config-for-each-kontur-app.sql runOnChange:false

--Disaster Ninja
update custom_app_feature
set configuration = '{
        "tabName": "About",
        "assetUrl": "/active/api/apps/58851b50-9574-4aec-a3a6-425fa18dcb54/assets/about.md",
        "subTabs": [
            {
                "tabName": "Terms",
                "assetUrl": "/active/api/apps/58851b50-9574-4aec-a3a6-425fa18dcb54/assets/terms.md"
            },
            {
                "tabName": "Privacy",
                "assetUrl": "/active/api/apps/58851b50-9574-4aec-a3a6-425fa18dcb54/assets/privacy.md"
            }
        ]
    }'
where app_id = '58851b50-9574-4aec-a3a6-425fa18dcb54' and feature_id in (select id from feature where name = 'about_page');

--Oasis
update custom_app_feature
set configuration = '{
        "tabName": "About",
        "assetUrl": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/about.md",
        "subTabs": [
            {
                "tabName": "Terms",
                "assetUrl": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/terms.md"
            },
            {
                "tabName": "Privacy",
                "assetUrl": "/active/api/apps/0b5b4047-3d9b-4ec4-993f-acf9c7315536/assets/privacy.md"
            }
        ]
    }'
where app_id = '0b5b4047-3d9b-4ec4-993f-acf9c7315536' and feature_id in (select id from feature where name = 'about_page');

--Terrain
update custom_app_feature
set configuration = '{
        "tabName": "About",
        "assetUrl": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/about.md",
        "subTabs": [
            {
                "tabName": "Terms",
                "assetUrl": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/terms.md"
            },
            {
                "tabName": "Privacy",
                "assetUrl": "/active/api/apps/3a433e95-0449-48a3-b4ff-9cffea805c74/assets/privacy.md"
            }
        ]
    }'
where app_id = '3a433e95-0449-48a3-b4ff-9cffea805c74' and feature_id in (select id from feature where name = 'about_page');

--OAM
update custom_app_feature
set configuration = '{
        "tabName": "About",
        "assetUrl": "/active/api/apps/1dc6fe68-8802-4672-868d-7f17943bf1c8/assets/about.md",
        "subTabs": [
            {
                "tabName": "Terms",
                "assetUrl": "/active/api/apps/1dc6fe68-8802-4672-868d-7f17943bf1c8/assets/terms.md"
            },
            {
                "tabName": "Privacy",
                "assetUrl": "/active/api/apps/1dc6fe68-8802-4672-868d-7f17943bf1c8/assets/privacy.md"
            }
        ]
    }'
where app_id = '1dc6fe68-8802-4672-868d-7f17943bf1c8' and feature_id in (select id from feature where name = 'about_page');

--Smart City
update custom_app_feature
set configuration = '{
        "tabName": "About",
        "assetUrl": "/active/api/apps/634f23f5-f898-4098-a8bd-09eb7c1e1ae5/assets/about.md",
        "subTabs": [
            {
                "tabName": "Terms",
                "assetUrl": "/active/api/apps/634f23f5-f898-4098-a8bd-09eb7c1e1ae5/assets/terms.md"
            },
            {
                "tabName": "Privacy",
                "assetUrl": "/active/api/apps/634f23f5-f898-4098-a8bd-09eb7c1e1ae5/assets/privacy.md"
            }
        ]
    }'
where app_id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5' and feature_id in (select id from feature where name = 'about_page');

--Kontur Atlas
update custom_app_feature
set configuration = '{
        "tabName": "About",
        "assetUrl": "/active/api/apps/9043acf9-2cf3-48ac-9656-a5d7c4b7593d/assets/about.md",
        "subTabs": [
            {
                "tabName": "Terms",
                "assetUrl": "/active/api/apps/9043acf9-2cf3-48ac-9656-a5d7c4b7593d/assets/terms.md"
            },
            {
                "tabName": "Privacy",
                "assetUrl": "/active/api/apps/9043acf9-2cf3-48ac-9656-a5d7c4b7593d/assets/privacy.md"
            }
        ]
    }'
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d' and feature_id in (select id from feature where name = 'about_page');
