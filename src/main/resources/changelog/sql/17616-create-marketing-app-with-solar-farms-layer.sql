--liquibase formatted sql

--changeset user-profile-service:17616-create-marketing-app-with-solar-farms-layer.sql runOnChange:false

insert into app (id, name, description, owner_user_id, is_public, extent)
values ('e27c72f1-ed08-4cdc-b86a-f0278a256be7', 'Solar Farm Marketing App', 'Application for Solar Farm Placement Suitability layer demonstration', null, true, '{-180,-80,180,80}');

insert into app_feature (app_id, feature_id)
select 'e27c72f1-ed08-4cdc-b86a-f0278a256be7', f.id
from feature f
where f.name in ('interactive_map', 'layers_in_area', 'url_store','popup');