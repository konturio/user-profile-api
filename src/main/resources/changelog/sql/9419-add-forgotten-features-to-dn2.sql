insert into app_feature
(feature_id, app_id)
select f2.id, '58851b50-9574-4aec-a3a6-425fa18dcb54'::uuid from feature f2
where not exists (
        select * from app_feature af
                          join app a on a.id = af.app_id
                          join feature f on f.id = af.feature_id
        where a.name = 'DN2'
          and f.id = f2.id
    )
and f2.beta = false;