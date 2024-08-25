--liquibase formatted sql

--changeset user-profile-service:19052-create-admin-boundary-breadcrumbs-feature-in-ups.sql runOnChange:false

--create Admin boundary breadcrumbs feature 
insert into feature (beta, name, featuretype, enabled, description, available_for_user_apps, default_for_user_apps, available_for_user_configuration)
values (false, 'admin_boundary_breadcrumbs', 'UI_PANEL', true, 'Map breadcrumbs that display the admin boundary names which intersect the current map center coordinates and allow to navigate to an admin boundary and highlight its borders', true, false, false)
on conflict do nothing;

--Disaster Ninja for guests
insert into custom_app_feature (app_id, feature_id, authenticated)
select ('58851b50-9574-4aec-a3a6-425fa18dcb54'), f.id, false
from feature f
where f.name in ('admin_boundary_breadcrumbs')
on conflict do nothing;

--OAM for guests
insert into custom_app_feature (app_id, feature_id, authenticated)
select ('1dc6fe68-8802-4672-868d-7f17943bf1c8'), f.id, false
from feature f
where f.name in ('admin_boundary_breadcrumbs')
on conflict do nothing;

--Smart City for guests
insert into custom_app_feature (app_id, feature_id, authenticated)
select ('634f23f5-f898-4098-a8bd-09eb7c1e1ae5'), f.id, false
from feature f
where f.name in ('admin_boundary_breadcrumbs');

--Terrain for guests
insert into custom_app_feature (app_id, feature_id, authenticated)
select ('3a433e95-0449-48a3-b4ff-9cffea805c74'), f.id, false
from feature f
where f.name in ('admin_boundary_breadcrumbs')
on conflict do nothing;

--MCDA for guests
insert into custom_app_feature (app_id, feature_id, authenticated)
select ('77260743-1da0-445b-8f56-ff6ca8520c55'), f.id, false
from feature f
where f.name in ('admin_boundary_breadcrumbs')
on conflict do nothing;

--Kontur Atlas for all roles
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', f.id, true, r.id
from feature f, custom_role r
where f.name in ('admin_boundary_breadcrumbs')
  and r.name in ('kontur_atlas_edu', 'kontur_atlas_pro', 'kontur_atlas_demo', 'kontur_atlas_admin')
on conflict do nothing;

--Oasis for admin role
insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
select '0b5b4047-3d9b-4ec4-993f-acf9c7315536', f.id, true, r.id
from feature f, custom_role r
where f.name in ('admin_boundary_breadcrumbs')
  and r.name in ('oasis_admin')
on conflict do nothing;
