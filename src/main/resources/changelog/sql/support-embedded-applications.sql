CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--0
create table app
(
    id uuid not null primary key,
    name varchar(63) not null,
    description varchar(255) not null,
    owner_user_id bigint constraint app_user_user_id references users,
    is_public boolean not null default false,
    center_geometry geometry
);
alter table app owner to "user-profile-api";

--0.1 create DN app
insert into app
(id, name, description, owner_user_id, is_public)
values
    ('58851b50-9574-4aec-a3a6-425fa18dcb54', 'DN2', 'Disaster Ninja 2.0', null, true);

--user_features -> app_user_features, app_features
--1
create table app_user_feature
(
    app_id uuid not null constraint app_user_features_app_app_id references app,
    user_id bigint not null constraint app_user_features_users_users_id references users on delete cascade,
    feature_id bigint not null constraint app_user_features_feature_feature_id references feature,
    primary key (app_id, user_id, feature_id)
);
alter table app_user_feature owner to "user-profile-api";
--1.1
create table app_feature
(
    app_id uuid not null constraint app_features_app_app_id references app,
    feature_id bigint not null constraint app_features_feature_feature_id references feature,
    primary key (app_id, feature_id)
);
alter table app_feature owner to "user-profile-api";

--2 enable all 'is_public=true' features enabled for DN
insert into app_feature
(app_id, feature_id)
select
    a.id, f.id
from app a, feature f
where f.is_public = true
;
--2.1 remove is_public column
alter table feature
    drop column is_public;
--2.2
alter table feature
    add column available_for_user_apps boolean not null default false;
alter table feature
    add column default_for_user_apps boolean not null default false;

--3 move all existing user_features into app_user_features as DN features
insert into app_user_feature
(app_id, user_id, feature_id)
select
    a.id, uf.user_id, feature_id
from app a, user_feature uf
;
--3.1
drop table user_feature;
