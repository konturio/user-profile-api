--liquibase formatted sql

--changeset user-profile-api:update-users-table-for-intercom-integration.sql runOnChange:true

alter table "users" add column if not exists updated_at timestamptz not null default now();

create or replace function set_users_updated_at()
    returns trigger
    language plpgsql
as
'
    begin
        NEW.updated_at := now();
        return NEW;
    end;
'
;

create trigger trigger_set_users_updated_at
    before update on "users"
    for each row
execute function set_users_updated_at();

alter table "users" add column if not exists intercom_id text;