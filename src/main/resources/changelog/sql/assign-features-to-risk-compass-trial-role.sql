--liquibase formatted sql

--changeset user-profile-api:assign-features-to-risk-compass-trial-role.sql runOnChange:true

insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '2d5af407-9f47-4f03-9d9b-2320ce9d307b',
       caf.feature_id,
       true,
       (select cr.id from custom_role cr where cr.name = 'risk_compass_trial')
from custom_app_feature caf
where caf.role_id = (select cr.id from custom_role cr where cr.name = 'risk_compass_admin');
