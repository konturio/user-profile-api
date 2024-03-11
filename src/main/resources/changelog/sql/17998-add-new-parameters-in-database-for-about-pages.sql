--liquibase formatted sql

--changeset user-profile-service:17998-add-new-parameters-in-database-for-about-pages.sql runOnChange:false

-- create table ASSETS
create table assets(
    id         bigint not null constraint pk_assets primary key GENERATED ALWAYS AS IDENTITY,
    type       text not null,
    language   text,
    asset      bytea not null,
    app_id     uuid not null constraint fk_assets_app references app,
    feature_id bigint not null constraint fk_assets_feature references feature on delete cascade
);

alter table feature owner to "user-profile-api";