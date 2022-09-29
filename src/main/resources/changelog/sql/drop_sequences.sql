--liquibase formatted sql

--changeset user-profile-service:drop_sequences.sql runOnChange:false

drop sequence feature_sequence;
drop sequence user_sequence;
drop sequence roles_sequence;
