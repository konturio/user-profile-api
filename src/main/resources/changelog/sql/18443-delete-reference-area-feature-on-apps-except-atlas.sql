--liquibase formatted sql

--changeset user-profile-service:18443-delete-reference-area-feature-on-apps-except-atlas.sql runOnChange:false

delete
from app_feature
where app_id = '77260743-1da0-445b-8f56-ff6ca8520c55'
  and feature_id in (select id
                     from feature
                     where name = 'reference_area');

delete from app_user_feature
where app_id = '77260743-1da0-445b-8f56-ff6ca8520c55'
 and feature_id in (select id 
                      from feature 
                      where name = 'reference_area');
