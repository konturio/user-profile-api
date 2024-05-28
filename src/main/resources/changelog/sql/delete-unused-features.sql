--liquibase formatted sql

--changeset user-profile-service:delete-unused-features.sql runOnChange:true

with feature_ids as (
    select name, id
    from feature
    where name in ('draw_tools',
                   'app_registration',
                   'communities',
                   'current_episode',
                   'episode_list',
                   'routing',
                   'geocoder',
                   'feature_settings',
                   'header',
                   'translation',
                   'popup',
                   'interactive_map',
                   'url_store',
                   'share_map',
                   'small_front_facing_map')
),
delete_from_app_feature as (
    delete from app_feature
        where feature_id in (select id from feature_ids)
),
delete_from_app_user_feature as (
    delete from app_user_feature
        where feature_id in (select id from feature_ids)
)
delete from feature
where id in (select id from feature_ids);