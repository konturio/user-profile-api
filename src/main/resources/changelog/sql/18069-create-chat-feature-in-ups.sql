--liquibase formatted sql

--changeset user-profile-api:18069-create-chat-feature-in-ups.sql runOnChange:false

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('chat_panel', 'UI_PANEL', true, false, true, false, 'Chat panel');


-- Disaster Ninja
insert into app_feature (app_id, feature_id)
select '58851b50-9574-4aec-a3a6-425fa18dcb54', id
from feature
where name = 'chat_panel';


-- Kontur Atlas
insert into app_feature (app_id, feature_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', id
from feature
where name = 'chat_panel';
