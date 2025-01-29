--liquibase formatted sql

--changeset user-profile-api:add-objectives-column-to-users-table.sql runOnChange:true


-- The existing column "bio" is converted to "objectives" along with all the contents, and a new empty "bio" column is created 
alter table users
rename column bio to objectives;

alter table users
add column bio text;
