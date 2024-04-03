--liquibase formatted sql

--changeset user-profile-service:17998-add-new-parameters-in-database-for-about-pages.sql runOnChange:true

-- create table ASSETS
drop table if exists assets;

create table assets(
    id            bigint not null constraint pk_assets primary key generated always as identity,
    media_type    text not null,
    media_subtype text not null,
    filename      text not null,
    description   text,
    owner_user_id bigint,
    language      text,
    last_updated  timestamp with time zone not null default current_timestamp,
    app_id        uuid not null constraint fk_assets_app references app,
    feature_id    bigint not null constraint fk_assets_feature references feature on delete cascade,
    asset         bytea not null,
    constraint uk_assets_app_id_filename unique (app_id, filename, language)
);

alter table feature owner to "user-profile-api";

create or replace function public.refresh_last_updated()
  returns trigger
  language plpgsql
  as
'
begin
    new.last_updated:= current_timestamp;
    return new;
end;
'
;

drop trigger if exists refresh_assets_last_updated_timestamp ON public.assets;

create trigger refresh_assets_last_updated_timestamp
before update on public.assets
    for each row execute function refresh_last_updated();

alter function public.refresh_last_updated() owner to "user-profile-api";

-- insert DN About page as an example
insert into assets(media_type, media_subtype, filename, description, language, app_id, feature_id, asset)
    values ('text',
            'plain',
            'about.md',
            'Default Disaster Ninja About page',
            'en',
            '58851b50-9574-4aec-a3a6-425fa18dcb54',
            (select id from feature where name = 'about_page' limit 1),
            'import { useEffect } from ''react'';
import { Trans } from ''react-i18next'';
import { Article } from ''~components/Layout'';
import { landUser } from ''~core/auth'';
import { i18n } from ''~core/localization'';
import s from ''./About.module.css'';

export function AboutPage({ toHomePage }: { toHomePage: () => void }) {
  useEffect(() => {
    landUser();
  }, []);
  return (
    <Article>
      <h1>{i18n.t(''about.title'')}</h1>
      <p>
        <Trans i18nKey="about.intro">
          Do you want to be notified about ongoing disasters? Are you interested in
          instant population data and other analytics for any region in the world?
          Disaster Ninja showcases some of{'' ''}
          <a href="https://www.kontur.io/" target="_blank" rel="noreferrer">
            Kontur
          </a>
          ’s capabilities in addressing these needs.
          <br />
          <br />
          We initially designed it as a decision support tool for humanitarian mappers.
          Now it has grown in functionality and use cases. Whether you work in disaster
          management, build a smart city, or perform research on climate change, Disaster
          Ninja can help you to:
        </Trans>
      </p>

      <blockquote>
        <h3>{i18n.t(''about.l1'')}</h3>
        <p>
          <Trans i18nKey="about.p1">
            The Disasters panel continually refreshes to inform you about ongoing events.
            It consumes data from the{'' ''}
            <a
              href="https://www.kontur.io/portfolio/event-feed/"
              target="_blank"
              rel="noreferrer"
            >
              Kontur Event Feed
            </a>
            , which you can also access via an API.
          </Trans>
        </p>
        <h3>{i18n.t(''about.l2'')}</h3>
        <p>{i18n.t(''about.p2'')}</p>
        <h3>{i18n.t(''about.l3'')}</h3>
        <p>
          <Trans i18nKey="about.p3">
            The Analytics panel shows the number of people living in that area per{'' ''}
            <a
              href="https://data.humdata.org/dataset/kontur-population-dataset"
              target="_blank"
              rel="noreferrer"
            >
              Kontur Population
            </a>{'' ''}
            and estimated mapping gaps in OpenStreetMap. Kontur’s customers have access to
            hundreds of other indicators through Advanced Analytics.
          </Trans>
        </p>
        <h3>{i18n.t(''about.l4'')}</h3>
        <p>
          <Trans i18nKey="about.p4">
            The Layers panel gives you various options to display two indicators
            simultaneously on a bivariate map, e.g., population density and distance to
            the nearest fire station. Use the color legend to assess which areas require
            attention. <br />
            Hint: in general, green indicates low risk / few gaps, red — high risk / many
            gaps.
          </Trans>
        </p>
      </blockquote>

      <p>{i18n.t(''about.p5'')}</p>

      <p>
        <span className={s.linkToMain} onClick={toHomePage}>
          {i18n.t(''about.goToMap'')} ➜
        </span>
      </p>

      <p>
        <Trans i18nKey="about.p6">
          We hope you find this tool valuable. Use the chatbox on Disaster Ninja for any
          questions about the functionality, and we will be happy to guide you. You can
          also contact us by email at <a href="mailto:hello@kontur.io">hello@kontur.io</a>{'' ''}
          if you have feedback or suggestions on improving the tool.
          <br />
          <br />
          Disaster Ninja is an open-source project. Find the code in{'' ''}
          <a href="https://github.com/konturio" target="_blank" rel="noreferrer">
            Kontur’s GitHub account
          </a>
          .
        </Trans>
      </p>
    </Article>
  );
}'::bytea);

update app_feature
    set configuration = '{
        "tabName": "About",
        "assetUrl": "/active/api/apps/58851b50-9574-4aec-a3a6-425fa18dcb54/assets/about.md",
        "subTabs": [
            {
                "tabName": "Privacy",
                "assetUrl": "/active/api/apps/58851b50-9574-4aec-a3a6-425fa18dcb54/assets/privacy.md"
            }
        ]
    }'
where app_id = '58851b50-9574-4aec-a3a6-425fa18dcb54'
  and feature_id = (select id from feature where name = 'about_page' limit 1)