insert into feature
(id, featuretype, name, beta, enabled, is_public, description)
values
    --disabled for now
    (nextval('feature_sequence'), 'UI_PANEL', 'advanced_analytics_panel', true, false, false, 'Advanced analytics panel')
;