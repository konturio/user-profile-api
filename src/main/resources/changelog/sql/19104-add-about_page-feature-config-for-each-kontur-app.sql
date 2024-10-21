--liquibase formatted sql

--changeset user-profile-service:19104-add-about_page-feature-config-for-each-kontur-app.sql runOnChange:false

--Disaster Ninja
update custom_app_feature
set configuration = '{
        "tabId": "about",
        "assetUrl": "about.md",
        "subTabs": [
            {
                "tabId": "terms",
                "assetUrl": "terms.md"
            },
            {
                "tabId": "privacy",
                "assetUrl": "privacy.md"
            }
        ]
    }'
where app_id = '58851b50-9574-4aec-a3a6-425fa18dcb54' and feature_id in (select id from feature where name = 'about_page');

--Oasis
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
            }
        ]
    }'
where app_id = '0b5b4047-3d9b-4ec4-993f-acf9c7315536' and feature_id in (select id from feature where name = 'about_page');

--Terrain
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
            }
        ]
    }'
where app_id = '3a433e95-0449-48a3-b4ff-9cffea805c74' and feature_id in (select id from feature where name = 'about_page');

--OAM
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
            }
        ]
    }'
where app_id = '1dc6fe68-8802-4672-868d-7f17943bf1c8' and feature_id in (select id from feature where name = 'about_page');

--Smart City
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
            }
        ]
    }'
where app_id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5' and feature_id in (select id from feature where name = 'about_page');

--Kontur Atlas
update custom_app_feature
set configuration = '{
        "tabId": "about",
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
                "tabId": "user-guide",
                "assetUrl": "user_guide.md"
            }
        ]
    }'
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d' and feature_id in (select id from feature where name = 'about_page');
