alter table app
    add column zoom decimal;

update app
    set zoom = 1.0
where center_geometry is not null;

alter table app
    add constraint nnc_app_center_geometry_zoom check (
            (center_geometry is null AND zoom is null)
            or
            (center_geometry is not null AND zoom is not null)
        );