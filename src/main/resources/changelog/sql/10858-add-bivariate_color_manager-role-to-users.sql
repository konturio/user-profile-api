insert into app_user_feature
    (app_id, user_id, feature_id)
select a.id, u.id, f.id
from app a,
     users u,
     feature f
where a.id::text = '58851b50-9574-4aec-a3a6-425fa18dcb54'
  and u.email in ('atarakanov@kontur.io', 'darafei@kontur.io', 'ppashagin@kontur.io',
                  'egrudinskaya@kontur.io', 'akucharau@kontur.io', 'propakov@kontur.io',
                  'dkazban@kontur.io', 'eokulik@kontur.io', 'kstsepina@kontur.io', 'prepin@kontur.io')
  and f.name = 'bivariate-color-manager';