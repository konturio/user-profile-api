--liquibase formatted sql

--changeset user-profile-api:17517-create-layer-features-panel-feature.sql runOnChange:true

insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description) values
    ('layer_features_panel', 'UI_PANEL', true, true, false, false, 'Layer Feature panel')
    on conflict (name) do nothing;

insert into app_feature (app_id, feature_id)
(select '58851b50-9574-4aec-a3a6-425fa18dcb54', id from feature where name = 'layer_features_panel')
    on conflict (app_id, feature_id) do nothing;

insert into app_user_feature
    (app_id, user_id, feature_id)
(select a.id, u.id, f.id
from app a,
     users u,
     feature f
where a.id::text = '58851b50-9574-4aec-a3a6-425fa18dcb54'
  and u.email in ('atarakanov@kontur.io', 'darafei@kontur.io', 'ahil@kontur.io',
                  'amurashka@kontur.io', 'vkozel@kontur.io', 'pkrukovich@kontur.io',
                  'atsiatserkina@kontur.io', 'a.artyukevich@kontur.io', 'tgrigoryan@kontur.io', 'adubinin@kontur.io')
  and f.name = 'layer_features_panel')
    on conflict (app_id, user_id, feature_id) do nothing;
