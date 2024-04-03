--liquibase formatted sql

--changeset user-profile-service:18190-change-feature-set-for-atlas-app-in-ups.sql runOnChange:false

insert into app_feature (app_id, feature_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id
from feature f
where f.name in ('osm_edit_link', 'use_3rdparty_analytics', 'intercom');

delete from app_feature (app_id, feature_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id
from feature f
where f.name in ('cookie_consent_banner');

delete from app_feature (app_id, feature_id)
select '77260743-1da0-445b-8f56-ff6ca8520c55', f.id
from feature f
where f.name in ('cookie_consent_banner');
