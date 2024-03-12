--liquibase formatted sql

--changeset user-profile-service:17998-add-new-parameters-in-database-for-about-pages.sql runOnChange:false

-- create table ASSETS
create table assets(
    id            bigint not null constraint pk_assets primary key generated always as identity,
    type          text not null,
    filename      text not null,
    url           text not null,
    description   text,
    owner_user_id bigint,
    language      text,
    last_updated  timestamp with time zone not null default current_timestamp,
    app_id        uuid not null constraint fk_assets_app references app,
    feature_id    bigint not null constraint fk_assets_feature references feature on delete cascade,
    asset         bytea not null
);

alter table feature owner to "user-profile-api";

create or replace function public.refresh_last_updated()
  returns trigger
  language plpgsql
  as
$$
begin
    new.last_updated:= current_timestamp;
    return new;
end;
$$
;

drop trigger if exists refresh_assets_last_updated_timestamp ON public.assets;

create trigger refresh_assets_last_updated_timestamp
before update on public.assets
    for each row execute function refresh_last_updated();

alter function public.refresh_last_updated() owner to "user-profile-api";

-- insert DN About page as an example
insert into assets(type, filename, url, description, app_id, feature_id) 
    values ('type', 'url', 'urlname1', 'description');
