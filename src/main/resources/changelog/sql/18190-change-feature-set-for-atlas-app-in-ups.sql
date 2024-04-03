--liquibase formatted sql

--changeset user-profile-service:18190-change-feature-set-for-atlas-app-in-ups.sql runOnChange:false

insert into app_feature (app_id, feature_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id
from feature f
where f.name in ('osm_edit_link', 'use_3rdparty_analytics', 'intercom');

delete from app_user_feature where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d';

with users_ids as (select id 
                  from users 
                  where email in ('atsiatserkina@kontur.io', 
                                  'abaranau@kontur.io', 
                                  'amurashka@kontur.io', 
                                  'a.artyukevich@kontur.io', 
                                  'avalasiuk@kontur.io', 
                                  'darafei@kontur.io', 
                                  'ekarpach@kontur.io', 
                                  'pkrukovich@kontur.io', 
                                  'tgrigoryan@kontur.io', 
                                  'vkozel@kontur.io', 
                                  'nlaptsik@kontur.io', 
                                  'ilosik@kontur.io',
                                  'ahil@kontur.io',
                                  'atarakanov@kontur.io',
                                  'arben@kontur.io',
                                  'curtis@kontur.io',
                                  'hoa@kontur.io',
                                  'nprovenzano@kontur.io',
                                  'tad@kontur.io',
                                  'aklopau@kontur.io',
                                  'kbakhanko@kontur.io',
                                  'milvari@kontur.io',
                                  'vbondar@kontur.io')),
features_ids as (select id 
                      from feature f
                      where f.name in ('osm_edit_link', 'use_3rdparty_analytics', 'intercom'))
insert into app_user_feature
  select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d' as app_id,
         users_ids.id                           as user_id,
         features_ids.id                        as feature_id
  from users_ids, features_ids;
