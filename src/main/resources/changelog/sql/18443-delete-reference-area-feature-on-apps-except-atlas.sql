--liquibase formatted sql

--changeset user-profile-service:18443-delete-reference-area-feature-on-apps-except-atlas.sql runOnChange:false

delete
from app_feature
where app_id in ('58851b50-9574-4aec-a3a6-425fa18dcb54', '634f23f5-f898-4098-a8bd-09eb7c1e1ae5', 'c5ecc65b-1e7e-4e31-92a4-222fadeaeef0', '77260743-1da0-445b-8f56-ff6ca8520c55')
  and feature_id = 'reference_area');
