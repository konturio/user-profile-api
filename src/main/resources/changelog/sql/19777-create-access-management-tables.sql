--liquibase formatted sql

--changeset 19777-create-access-management-tables.sql runOnChange:true

-- Enums

drop table if exists enum_rights;
drop table if exists enum_resource_types;
drop table if exists enum_subject_types;

create table if not exists enum_rights (
    id bigint not null constraint pk_enum_rights primary key generated always as identity,
    name text not null unique,
    description text
);

create table if not exists enum_resource_types (
    id bigint not null constraint pk_enum_resource_types primary key generated always as identity,
    name text not null unique,
    description text
);

create table if not exists enum_subject_types (
    id bigint not null constraint pk_enum_subject_types primary key generated always as identity,
    name text not null unique,
    description text
);

-- Access Record

drop table if exists access_records;

create table if not exists access_records (
    id            bigint not null constraint pk_access_records primary key generated always as identity,
    user_id       bigint not null, -- references users table
    allow         boolean not null,
    right_id      bigint not null, -- references enum_rights table
    resource_id   bigint not null,
    resource_type bigint not null, -- references enum_resource_types table
    subject_id    bigint not null,
    subject_type  bigint not null, -- references enum_resource_types table
    created_at    timestamptz not null default now(),
    constraint fk_access_records_user_id       foreign key (user_id)       references users (id),
    constraint fk_access_records_right_id      foreign key (right_id)      references enum_rights (id),
    constraint fk_access_records_resource_type foreign key (resource_type) references enum_resource_types (id),
    constraint fk_access_records_subject_type  foreign key (subject_type)  references enum_subject_types (id)
);