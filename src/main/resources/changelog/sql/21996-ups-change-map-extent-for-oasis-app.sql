--liquibase formatted sql

--changeset user-profile-service:21996-ups-change-map-extent-for-oasis-app.sql runOnChange:false

update app
  set extent = null where id = '0b5b4047-3d9b-4ec4-993f-acf9c7315536';
