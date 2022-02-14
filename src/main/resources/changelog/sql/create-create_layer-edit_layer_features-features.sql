insert into feature
(id, featuretype, name, beta, enabled, is_public, description)
values
    --disabled for now
    (nextval('feature_sequence'), 'UI_PANEL', 'create_layer', true, false, false, 'Create layer'),
    (nextval('feature_sequence'), 'UI_PANEL', 'edit_layer_features', true, false, false, 'Edit layer features')
;