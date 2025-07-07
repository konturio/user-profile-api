--liquibase formatted sql

--changeset user-profile-api:add-risk-compass-trial-role-to-users.sql runOnChange:true

-- Ensure the role exists before assigning it
INSERT INTO custom_role (name)
VALUES ('risk_compass_trial')
ON CONFLICT DO NOTHING;

-- Assign the role to users who don't have any roles
WITH users_with_no_roles AS (
    SELECT u.id AS user_id
    FROM users u
    WHERE NOT EXISTS (
        SELECT 1 FROM user_custom_role ur WHERE ur.user_id = u.id
    )
)
INSERT INTO user_custom_role (user_id, role_id, started_at, ended_at)
SELECT
    uwnr.user_id,
    (SELECT id FROM custom_role WHERE name = 'risk_compass_trial' LIMIT 1),
    NOW(),
    NOW() + INTERVAL '14 days'
FROM users_with_no_roles uwnr;
