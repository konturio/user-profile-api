--liquibase formatted sql

--changeset user-profile-api:assign-features-to-atlas-trial-role.sql runOnChange:true

insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d',
       caf.feature_id,
       true,
       (select cr.id from custom_role cr where cr.name = 'kontur_atlas_trial')
from custom_app_feature caf
where caf.role_id = (select cr.id from custom_role cr where cr.name = 'kontur_atlas_pro');