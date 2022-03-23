update feature
set enabled = true
where name in ('advanced_analytics_panel', 'bivariate_manager');

delete from app_feature
where feature_id in (select id from feature where name in ('create_layer', 'small_front_facing_map'));

update feature
set enabled = false
where name in ('create_layer', 'small_front_facing_map');