--liquibase formatted sql

--changeset user-profile-service:12813-create-smart-city-application.sql runOnChange:false

insert into app (id, name, description, owner_user_id, is_public)
values ('634f23f5-f898-4098-a8bd-09eb7c1e1ae5', 'Smart City', 'Smart City', null, true);

insert into app_feature (app_id, feature_id)
select '634f23f5-f898-4098-a8bd-09eb7c1e1ae5', f.id
from feature f
where f.name not in ('kontur-public', 'events_list', 'current_event', 'reports', 'current_episode', 'episode_list',
                     'bivariate_manager', 'feed_selector', 'advanced_analytics_panel', 'create_layer',
                     'bivariate_color_manager', 'small_front_facing_map');