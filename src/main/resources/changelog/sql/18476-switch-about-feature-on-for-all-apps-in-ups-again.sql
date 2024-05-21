--liquibase formatted sql

--changeset 18476-switch-about-feature-on-for-all-apps-in-ups-again.sql runOnChange:false


-- OpenAerialMap
insert into app_feature (app_id, feature_id)
select '1dc6fe68-8802-4672-868d-7f17943bf1c8', id
from feature
where name = 'about_page';

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id, (select id from feature where name = 'about_page') from app_user_feature
where app_id = '1dc6fe68-8802-4672-868d-7f17943bf1c8';


-- Smart City
insert into app_feature (app_id, feature_id)
select '634f23f5-f898-4098-a8bd-09eb7c1e1ae5', id
from feature
where name = 'about_page';

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id, (select id from feature where name = 'about_page') from app_user_feature
where app_id = '634f23f5-f898-4098-a8bd-09eb7c1e1ae5';


-- Kontur Atlas
insert into app_feature (app_id, feature_id)
select '9043acf9-2cf3-48ac-9656-a5d7c4b7593d', id
from feature
where name = 'about_page';

insert into app_user_feature (app_id, user_id, feature_id)
select distinct app_id, user_id, (select id from feature where name = 'about_page') from app_user_feature
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d';
