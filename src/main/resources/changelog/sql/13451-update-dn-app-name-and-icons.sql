--liquibase formatted sql

--changeset user-profile-service:13451-update-dn-app-name-and-icons.sql runOnChange:false

update app
set name = 'Disaster Ninja',
    sidebar_icon_url='/active/static/favicon/favicon.svg',
    favicon_url='/active/static/favicon/favicon.svg'
where id = '58851b50-9574-4aec-a3a6-425fa18dcb54';


UPDATE app
SET sidebar_icon_url='/active/static/favicon/smart-city-icon.svg',
    favicon_url='/active/static/favicon/smart-city-favicon.svg'
WHERE id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5';