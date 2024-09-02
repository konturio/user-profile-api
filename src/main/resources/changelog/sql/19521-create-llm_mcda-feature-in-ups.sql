--liquibase formatted sql

--changeset user-profile-service:19521-create-llm_mcda-feature-in-ups.sql runOnChange:false

--create Search bar feature
insert into feature ("name", featuretype, enabled, beta, available_for_user_apps, default_for_user_apps, description)
values ('llm_mcda', 'UI_PANEL', true, false, true, false, 'LLM MCDA allows to type questions or requests in the search bar and get MCDA composed by ChatGPT based on the input and the axes Insights DB has.')
on conflict do nothing;

-- Terrain
insert into custom_app_feature (app_id, feature_id, authenticated)
select '3a433e95-0449-48a3-b4ff-9cffea805c74', f.id, false
from feature f
where f.name in ('llm_mcda')
on conflict do nothing;

-- MCDA
insert into custom_app_feature (app_id, feature_id, authenticated)
select '77260743-1da0-445b-8f56-ff6ca8520c55', f.id, false
from feature f
where f.name in ('llm_mcda')
  and r.name in ('mcda_demo', 'mcda_admin')
on conflict do nothing;

-- Atlas
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id, true, r.id
from feature f, custom_role r
where f.name in ('llm_mcda')
  and r.name in ('kontur_atlas_edu', 'kontur_atlas_pro', 'kontur_atlas_demo', 'kontur_atlas_admin')
on conflict do nothing;

-- Oasis
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '0b5b4047-3d9b-4ec4-993f-acf9c7315536', f.id, true, r.id
from feature f, custom_role r
where f.name in ('llm_mcda')
  and r.name in ('oasis_admin')
on conflict do nothing;
