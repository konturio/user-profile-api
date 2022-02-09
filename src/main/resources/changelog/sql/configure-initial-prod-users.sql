--set default feed for all users
do $$
    declare
        default_feed bigint;
        user_id bigint;
    BEGIN
        default_feed = (select id from feature
                        where featuretype = 'EVENT_FEED'
                          and is_public = true
                          and beta = false
                          and enabled = true
                        limit 1);

        for user_id in select id from users
            loop
                insert into user_feature (user_id, feature_id)
                values
                    (user_id, default_feed);
            end loop;
    end;
$$;

--enable public features for all users
do $$
    declare
        user_id bigint;
    BEGIN
        for user_id in select id from users
            loop
                insert into user_feature (user_id, feature_id)
                select user_id, id from feature f
                where f.enabled = true
                  and f.is_public = true
                  and f.beta = false
                  and f.featuretype != 'EVENT_FEED';
            end loop;
    end;
$$;