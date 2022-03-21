insert into feature
(id, featuretype, name, beta, enabled, available_for_user_apps, default_for_user_apps, description)
values
    (nextval('feature_sequence'), 'UI_PANEL', 'focused_geometry_editor', false, true, true, false, 'Focused geometry editor');