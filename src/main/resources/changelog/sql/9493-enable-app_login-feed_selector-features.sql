--enable features
update feature
set (enabled, beta) = (true, false)
where name in (
       'feed_selector',
       'app_login'
    );

--add to DN2 app
insert into app_feature (app_id, feature_id)
select
    a.id app_id, f.id feature_id
from app a, feature f
where a.name = 'DN2'
  and f.name in ('app_login', 'feed_selector')
  and f.beta = false;