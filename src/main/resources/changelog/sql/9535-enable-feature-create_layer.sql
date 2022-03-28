--enable feature
update feature
set (enabled, beta) = (true, true)
where name in (
       'create_layer'
    );
--not adding to DN2 as it's not a default feature