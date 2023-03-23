--liquibase formatted sql

--changeset user-profile-service:15269-disable-non-functional-features-on-marketing-app.sql runOnChange:false

delete
from app_feature
where app_id = 'f70488c2-055c-4599-a080-ded10c47730f'
  and feature_id in (select id
                     from feature
                     where name in ('locate_me', 'use_3rdparty_analytics', 'map_ruler',
                                    'boundary_selector', 'current_event'));