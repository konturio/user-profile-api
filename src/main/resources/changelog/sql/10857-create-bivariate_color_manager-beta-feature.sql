insert into feature
(id, featuretype, name, beta, enabled, available_for_user_apps, default_for_user_apps, description)
values
    (nextval('feature_sequence'), 'UI_PANEL', 'bivariate-color-manager', true, true, false, false, 'Access to this section allows users to manage the colors of the bivariate legend and indicator sentiments, as well as create new sentiments');