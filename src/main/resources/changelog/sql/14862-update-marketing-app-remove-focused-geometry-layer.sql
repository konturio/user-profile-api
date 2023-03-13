--liquibase formatted sql

--changeset user-profile-service:14862-update-marketing-app-remove-focused-geometry-layer.sql runOnChange:false

delete
from app_feature
where app_id = 'f70488c2-055c-4599-a080-ded10c47730f'
  and feature_id in (select id from feature where name in ('focused_geometry_layer'));