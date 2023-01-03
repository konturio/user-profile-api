--liquibase formatted sql

--changeset user-profile-service:13984-create-oam-application.sql runOnChange:false

insert into app (id, name, description, owner_user_id, is_public, sidebar_icon_url, favicon_url)
values ('1dc6fe68-8802-4672-868d-7f17943bf1c8', 'OpenAerialMap', 'OpenAerialMap', null, true,
        '/active/static/favicon/oam-icon.svg',
        '/active/static/favicon/oam-icon.svg');

insert into app_feature (app_id, feature_id)
select '1dc6fe68-8802-4672-868d-7f17943bf1c8', f.id
from feature f
where f.name in ('osm_edit_link', 'map_ruler', 'locate_me', 'intercom', 'side_bar',
                 'app_registration', 'app_login', 'share_map', 'geocoder', 'tooltip',
                 'routing', 'translation', 'popup', 'toasts', 'url_store');