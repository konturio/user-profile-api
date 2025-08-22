--liquibase formatted sql

--changeset 19777-initialize-access-management-tables.sql runOnChange:true


insert into enum_rights (name, description)
values ('owner', 'An owner of a resource has full access'),
       ('write', 'A grantee can change the resource'),
       ('read',  'A grantee can access the resource for reading');

insert into enum_resource_types (name, description)
values ('layer', 'Designates regular map layers');

insert into enum_subject_types (name, description)
values ('user', 'Users are the primary subjects of access rights');


-- A "platform admin" system user record that grants the "owner" access upon layer loading

insert into users
    (username, email, full_name, language, use_metric_units, subscribed_to_kontur_updates)
values
    ('platform', 'platform@maumaps.com', 'Platform Admin', null, true, false);
