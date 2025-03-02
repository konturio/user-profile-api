--liquibase formatted sql

--changeset user-profile-api:add-trial-role-to-users.sql runOnChange:true

insert into custom_role (name)
values ('kontur_atlas_trial')
on conflict do nothing;


with users_with_no_roles as (
    select u.id
    from "users" u
    where not exists(select * from user_custom_role ur where ur.user_id = u.id)
)
insert into user_custom_role (user_id, role_id, started_at, ended_at)
select
    id,
    (select id from custom_role where name = 'kontur_atlas_trial'),
    now(),
    now() + INTERVAL '14 days'
from users_with_no_roles;
