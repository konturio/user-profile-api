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

CREATE OR REPLACE FUNCTION public.refresh_last_updated()
  RETURNS TRIGGER 
  LANGUAGE PLPGSQL
  AS
$$
BEGIN
    IF NEW.last_name <> OLD.last_name THEN
         INSERT INTO employee_audits(employee_id,last_name,changed_on)
         VALUES(OLD.id,OLD.last_name,now());
    END IF;

    RETURN NEW;
END;
$$