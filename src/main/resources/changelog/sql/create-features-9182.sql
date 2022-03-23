insert into feature
(id, beta, name, featuretype, enabled, description)
values
    (nextval('feature_sequence'), false, 'routing', 'UI_PANEL', true, 'Routing'),
    (nextval('feature_sequence'), false, 'translation', 'UI_PANEL', true, 'Translation'),
    (nextval('feature_sequence'), false, 'popup', 'UI_PANEL', true, 'Popup'),
    (nextval('feature_sequence'), true, 'small_front_facing_map', 'UI_PANEL', true, 'Small front facing map'),
    (nextval('feature_sequence'), false, 'app_registration', 'UI_PANEL', true, 'App registration'),
    (nextval('feature_sequence'), false, 'share_map', 'UI_PANEL', true, 'Share map'),
    (nextval('feature_sequence'), false, 'header', 'UI_PANEL', true, 'Header'),
    (nextval('feature_sequence'), false, 'intercom', 'UI_PANEL', true, 'Intercom');