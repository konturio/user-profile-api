# How to connect to UPS & create new app on Kontur Platform

Field: Content

We already have several apps on our platform: ["Disaster Ninja"](https://disaster.ninja/), ["Smart City"](maps.kontur.io "maps.kontur.io"), ["OpenAerialMap"](https://new.openaerialmap.org/), etc. How to create another one?

# I. Preparation

1. Request accesses and credentials to Kubernetes namespaces of `user-profile` and `layers-api` of needed environments in `#operations` Slack channel.
2. Install Lens from <https://k8slens.dev/download>, register via your working email (auth with google), and choose free account.

![image.png](https://kontur.fibery.io/api/files/7daa2659-0588-4f7d-930f-5e3ea9f6e95e#align=%3Aalignment%2Fblock-left&width=271&height=195 "")

3. Add a cluster to Lens catalog. Insert a config with secret provided by someone from `#operations` Slack channel earlier.

|     |     |     |
| --- | --- | --- |
| ![image.png](https://kontur.fibery.io/api/files/910c7dd4-dd4a-499f-80ef-835ccfdd495b#align=%3Aalignment%2Fblock-left&width=278&height=150 "")  | ![image.png](https://kontur.fibery.io/api/files/311f90cb-0472-4844-a9bc-d6f0da3820d9#align=%3Aalignment%2Fblock-center&width=219&height=220 "") | ![image.png](https://kontur.fibery.io/api/files/cf91336e-1a7a-4b7d-a665-26e1c7909674#width=995&height=963 "") |

4. Open settings in a dropdown under your email. Go to `Namespaces` and enable the namespaces `{stage}-user-profile-api` and `{stage}-layers-api`. You might need to reconnect to cluster after adding them.

|     |     |
| --- | --- |
| ![image.png](https://kontur.fibery.io/api/files/9dba9336-ff03-4635-8415-67b96da2d513#align=%3Aalignment%2Fblock-left&width=180&height=211 "") | ![image.png](https://kontur.fibery.io/api/files/24bbabcb-1f46-4809-bb45-5643082c78b0#align=%3Aalignment%2Fblock-left&width=541&height=246 "") |

5. Connect from Lens to database [[---#^b2d59af0-3b70-11e9-be77-04d77e8d50cb/c2c7e320-9e52-11ed-82e5-5d01e11d9a52]] 
6. Install [pgAdmin4](https://www.pgadmin.org/download/ "https://www.pgadmin.org/download/") or any other database tool such as DataGrip, DBeaver, etc to manage databases.
7. On main page click `Add New Server` . Name it the way you want to (for example, `{stage}-user-profile-api`. Go to 'Connection' tab, use port that you've set earlier to forward ip while connecting from Kubernetes. 

![image.png](https://kontur.fibery.io/api/files/86d8625e-f8fe-463c-944a-e77deee6be0e#align=%3Aalignment%2Fblock-left&width=425&height=225 "")

5. Database name, username, and password can be checked in Lens: Config -> Secrets. For example, for user-profile-api DB details are in a secret **db-user-profile-api-pguser-user-profile-api.** *Use "eye" icon to see not encrypted details.*

![image.png](https://kontur.fibery.io/api/files/40a6ad75-084e-41f3-8e6f-e385f0ae88ae#align=%3Aalignment%2Fblock-left&width=539&height=268 "")

9. Add `user-profile-api` of the needed env, if you don't have one 

![image.png](https://kontur.fibery.io/api/files/5039e154-0745-48dd-8158-3e9577a5c112#align=%3Aalignment%2Fblock-left&width=346&height=389 "")

10. Do the same for `layers-api`
11. You'll probably have a lot of connection issues from Kubernetes to database manager - ongoing [discussion with fixes in russian](https://konturio.slack.com/archives/C037VHMN26S/p1674123671709019 "https://konturio.slack.com/archives/C037VHMN26S/p1674123671709019").  

# II. Creation

### Option 1. Add new app using UPS migrations

The least error-prone solution to add new applications for all environments is to use UPS migrations. Thus the app would be propagated to all current and future installations of UPS.

In order to create and register new migration:

1. Create .sql file in [src/main/resources/changelog/sql](https://github.com/konturio/user-profile-api/tree/main/src/main/resources/changelog/sql "https://github.com/konturio/user-profile-api/tree/main/src/main/resources/changelog/sql") in UPS.
2. Fill it with sql script. Provided example is for OAM application. Update the data and generate unique UUID for app (<https://www.uuidgenerator.net/version4>). **app_feature** table store the list of features enabled for the application. This table also contains **configuration** property. Where one can put some json which will be propagated to FE.

   ```
   --liquibase formatted sql
   
   --changeset user-profile-service:15758-create-odin-app-on-kontur-platform.sql runOnChange:false
   
   --create ODIN app
   insert into app (id, name, description, owner_user_id, is_public, sidebar_icon_url, favicon_url, extent, domains, favicon_pack)
   values ('415e2172-3e94-4749-b714-d37470acf88a', 'ODIN', 'Operational Disaster Incident Network app (NICS 2.0)', null, true, '/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/favicon.svg', '/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/favicon.ico', '{-180,-80,180,80}', array['test-odin-kontur-io.k8s-01.konturlabs.com', 'dev-odin-kontur-io.k8s-01.konturlabs.com', 'odin.kontur.io', 'prod-odin-kontur-io.k8s-01.konturlabs.com'], '{
     "favicon.svg": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/favicon.svg",
     "favicon.ico": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/favicon.ico",
     "apple-touch-icon.png": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/apple-touch-icon.png",
     "icon-192x192.png": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/icon-192x192.png",
     "icon-512x512.png": "/active/api/apps/415e2172-3e94-4749-b714-d37470acf88a/assets/icon-512x512.png"
   }'::json)
   on conflict do nothing;
   
   --create new app roles
   insert into custom_role (name)
   values ('odin_admin'),
   		('odin_demo')
   on conflict do nothing;
   
   --add ODIN app guest features
   insert into custom_app_feature (app_id, feature_id, authenticated)
   select '415e2172-3e94-4749-b714-d37470acf88a', f.id, false
   from feature f
   where f.name in ('app_login', 'side_bar', 'use_3rdparty_analytics', 'tooltip', 'toasts', 'intercom')
   on conflict do nothing;
   
   --add ODIN app role based features
   insert into custom_app_feature (app_id, feature_id, authenticated, role_id)
   select '415e2172-3e94-4749-b714-d37470acf88a', f.id, true, r.id
   from feature f, custom_role r
   where f.name in ('toolbar', 'locate_me', 'map_ruler', 'boundary_selector', 'geometry_uploader', 'focused_geometry_editor', 
   	'map', 'analytics_panel', 'events_list', 'legend_panel', 'episodes_timeline', 'chat_panel', 'feed_selector', 'kontur-public', 
   	'events_list__bbox_filter', 'current_event', 'focused_geometry_layer', 'layers_in_area', 'mcda', 'osm_edit_link', 
   	'search_locations', 'admin_boundary_breadcrumbs', 'map_layers_panel', 'live_sensor', 'create_layer')
     and r.name in ('odin_admin', 'odin_demo')
   on conflict do nothing;
   
   --configurate analytics panel 
   update custom_app_feature
   set configuration = '{"statistics": [{
   "formula": "sumX",
   "x": "population"
   }, {
   "formula": "sumX",
   "x": "populated_area_km2"
   }, {
   "formula": "sumX",
   "x": "forest"
   }, {
   "formula": "avgX",
   "x": "avg_forest_canopy_height"
   }, {
   "formula": "maxX",
   "x": "wildfires"
   }, {
   "formula": "maxX",
   "x": "drought_days_count"
   }, {
   "formula": "avgX",
   "x": "worldclim_avg_temperature"
   }, {
   "formula": "maxX",
   "x": "worldclim_max_temperature"
   }, {
   "formula": "sumX",
   "x": "volcanos_count"
   }, {
   "formula": "sumX",
   "x": "industrial_area"
   }]}'
   where app_id = '415e2172-3e94-4749-b714-d37470acf88a'
     and feature_id in (select f.id from feature f where f.name = 'analytics_panel')
     and authenticated;
   ```
3. Register the newly created .sql file in [src/main/resources/changelog/changelog.yaml](https://github.com/konturio/user-profile-api/blob/main/src/main/resources/changelog/changelog.yaml)
4. Migrations scripts are run automatically when new version of UPS is deployed.

### ~~Option 2. Register app and add features via User profile service DB~~ (please omit this step, migrations should be used instead, see the previous step)

1. In user profile service database find `app` table

   ![image.png](https://kontur.fibery.io/api/files/4eedb099-d863-40dc-814e-edd03c931444#align=%3Aalignment%2Fblock-center&width=314&height=355 "")
2. Add data about the new app to the table\
   \
   2.1 Generate uuid by your preferred way ([example](https://www.uuidgenerator.net/version4 "https://www.uuidgenerator.net/version4")). Keep it near, you'll need it several times later\
   2.2 Add row 

   ![image.png](https://kontur.fibery.io/api/files/a7569fd6-2636-481f-833d-52a39f18776e#width=100&height=93 "")

   and fill columns of `id`, `name`, `is_public` and other columns if needed

   ![image.png](https://kontur.fibery.io/api/files/38eaa4d3-96f5-4b5d-9fd0-18927a40f504#width=1350&height=130 "")

   2.3 Save changes to database!

   ![image.png](https://kontur.fibery.io/api/files/b73e624a-1214-4516-8159-dbf080d2f159#width=169&height=124 "")
3. Collect needed features ids from the `feature` table

   ![image.png](https://kontur.fibery.io/api/files/0389de43-b5d9-4ff4-b864-dbf1621cc610#width=1141&height=344 "")
4. Add needed features for your app. See [[---#^b2d59af0-3b70-11e9-be77-04d77e8d50cb/dbc6de20-ac05-11ec-b7da-eddf2b4659d4]] to learn about app features as concept. Go to `app_feature` table where each row adds new feature (by `feature_id`) to the app (`app_id` you stored before). One app with same `app_id` can - and probably should - have more than one feature.

   ![image.png](https://kontur.fibery.io/api/files/ad4281b8-0476-4aeb-a7f6-74ca4815f6b9#width=823&height=480 "")

   don't forget to send your changes to the database when you done! 

### 

# III. Add layers to the app (if you need them)

There are 3 ways to do that 

1. via migrations
2. via database 
3. via designed endpoint.
* UPD. Current flow is the following: add migrations to Layers DB repository and then apply them manually in the database (since migrations from Layers DB are not running automatically yet).**

Both endpoint and DB approach fit well if one needs to update layers once. If some changes needs to be propagated to all environments (either old or some new one) better to use migrations.

Endpoint wasn't built to add all layers, but it's an easier approach. You can also start with endpoint method and then add needed layers via database.

## Option 1. Via migrations (the main and preferred one)

1. create .sql file in [/migrations](https://github.com/konturio/layers-db/tree/main/migrations) in layers-db repo
2. Fill it with sql script. Provided example is for OAM application. Update the data and use **the same** **UUID** that was created for the app in UPS.

   ```
   -- create Oasis app
   insert into apps (id, show_all_public_layers, is_public, owner)
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', true, true, 'layers_db')
   on conflict (id) do nothing;
   
   -- add Kontur basemap layers to Oasis app
   insert into apps_layers (app_id, layer_id, is_default)
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'kontur_lines', true),
          ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'kontur_zmrok', false)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, map_style_config, legend_style_config)
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'population_density', true,
           '{"type": "mcda", "config": {"id": "Kontur population", "version": 4, "layers": [{"axis": ["population", "area_km2"], "range": [0, 46200], "sentiment": ["bad", "good"], "coefficient": 1, "transformationFunction": "no", "normalization": "no"}], "colors": {"type": "mapLibreExpression", "parameters": {"fill-color": [["step", ["var", "mcdaResult"], "#F0F0D6", 1.27, "#ECECC4", 2.45, "#EAEAB0", 5.75, "#E8E89D", 12.43, "#E1D689", 28.467, "#DAC075", 66.03, "#D1A562", 172.46, "#C98A50", 535.67, "#BF6C3F"]], "fill-opacity": 0.8}}}}',
           '{"type": "simple", "steps": [{"style": {"fill-color": "#F0F0D6", "fill-opacity": 0.8, "color": "#F0F0D6"}, "stepName": "0 - 1.27", "stepShape": "square"}, {"style": {"fill-color": "#ECECC4", "fill-opacity": 0.8, "color": "#ECECC4"}, "stepName": "1.27 - 2.45", "stepShape": "square"}, {"style": {"fill-color": "#EAEAB0", "fill-opacity": 0.8, "color": "#EAEAB0"}, "stepName": "2.45 - 5.75", "stepShape": "square"}, {"style": {"fill-color": "#E8E89D", "fill-opacity": 0.8, "color": "#E8E89D"}, "stepName": "5.75 - 12.43", "stepShape": "square"}, {"style": {"fill-color": "#E1D689", "fill-opacity": 0.8, "color": "#E1D689"}, "stepName": "12.43 - 28.47", "stepShape": "square"}, {"style": {"fill-color": "#DAC075", "fill-opacity": 0.8, "color": "#DAC075"}, "stepName": "28.47 - 66.03", "stepShape": "square"}, {"style": {"fill-color": "#D1A562", "fill-opacity": 0.8, "color": "#D1A562"}, "stepName": "66.03 - 172.46", "stepShape": "square"}, {"style": {"fill-color": "#C98A50", "fill-opacity": 0.8, "color": "#C98A50"}, "stepName": "172.46 - 535.67", "stepShape": "square"}, {"style": {"fill-color": "#BF6C3F", "fill-opacity": 0.8, "color": "#BF6C3F"}, "stepName": "535.67 - 46200", "stepShape": "square"}]}')
   on conflict (app_id , layer_id) do update set is_default = true;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'ukraineBorderCrossing', false,
          '{"eventIdRequiredForRetrieval": false, "boundaryRequiredForRetrieval": false}', 
          '{"type": "simple", "steps": [{"style": {"circle-color": "red", "circle-radius": {"stops": [[5, 5], [8, 8], [11, 12], [16, 22], [22, 28]]}, "circle-opacity": 0.8}, "stepShape": "circle"}], "tooltip": {"type": "markdown", "paramName": "tooltipContent"}}', null, null)
   on conflict (app_id , layer_id) do update set is_default = true;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'ukraineTotalRefugeeInflux', false,
          '{"eventIdRequiredForRetrieval": false, "boundaryRequiredForRetrieval": false}', 
          '{"type": "simple", "steps": [{"style": {"text-size": ["interpolate", ["exponential", 1], ["get", "individuals"], 1000, 8, 100000, 12, 1000000, 13, 10000000, 18], "text-color": "#051626", "text-field": "{individuals}", "font-family": "Noto Sans Bold", "circle-color": "#ec0000", "circle-radius": ["interpolate", ["exponential", 1], ["get", "individuals"], 1000, 16, 100000, 22, 1000000, 30, 10000000, 50], "circle-opacity": 0.4, "text-allow-overlap": true}}]}', null, null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'idbEstimates', false,
          '{"eventIdRequiredForRetrieval": false, "boundaryRequiredForRetrieval": false}', 
          '{"type": "simple", "steps": [{"style": {}, "stepName": "0 - 570k", "stepShape": "square", "stepIconFill": "#a8cee4", "stepIconStroke": "#a8cee4"}, {"style": {}, "stepName": "570k - 1.4M", "stepShape": "square", "stepIconFill": "#6aadd6", "stepIconStroke": "#6aadd6"}, {"style": {}, "stepName": "1.4M - 1.6M", "stepShape": "square", "stepIconFill": "#4090c5", "stepIconStroke": "#4090c5"}, {"style": {}, "stepName": "1.6M - 1.8M", "stepShape": "square", "stepIconFill": "#084e98", "stepIconStroke": "#084e98"}, {"style": {}, "stepName": "1.8M - 2.0M", "stepShape": "square", "stepIconFill": "#08316c", "stepIconStroke": "#08316c"}, {"style": {}, "stepName": "No data", "stepShape": "square", "stepIconFill": "white", "stepIconStroke": "black"}, {"style": {"color": "#e1ffff", "width": 0, "fill-color": ["case", ["==", ["get", "presence_idps_latest"], 0], "#ffffff", ["<=", ["get", "presence_idps_latest"], 570000], "#a8cee4", ["<=", ["get", "presence_idps_latest"], 1400000], "#6aadd6", ["<=", ["get", "presence_idps_latest"], 1600000], "#4090c5", ["<=", ["get", "presence_idps_latest"], 1800000], "#084e98", ["<=", ["get", "presence_idps_latest"], 2000000], "#08316c", ["any", true, ["get", "this_will_never_execute_i_added_this_for_fallback_to_work"]], "white", "white"], "line-width": 0, "fill-opacity": 0.8}}], "tooltip": {"type": "markdown", "paramName": "tooltipContent"}}', null, null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'osmActiveContributors', false,
          '{"eventIdRequiredForRetrieval": false, "boundaryRequiredForRetrieval": false}', '{}', null, null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'openaerialmap', false,
          '{"eventIdRequiredForRetrieval": false, "boundaryRequiredForRetrieval": false}', '{}', null, null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'openaerialmap_cluster', false,
          '{"eventIdRequiredForRetrieval": false, "boundaryRequiredForRetrieval": false}', 
          '{"name": "OAM Availability", "type": "simple", "steps": [{"style": {"text": "{count}", "text-font": "Open Sans Bold", "text-size": 14, "text-color": "white", "circle-color": "black", "circle-radius": 14}, "sourceLayer": "default"}]}', null, null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'activeContributors', false, null,
          '{"name": "Active contributors", "type": "simple", "steps": [{"style": {"text": "{top_user}", "max-width": 3, "text-size": 14, "icon-image": "osm-user-shield", "text-color": "rgb(0, 145, 0)", "font-family": "Noto Sans Bold", "text-padding": 0, "icon-text-fit": "both", "text-halo-color": "rgba(255, 255, 255, 0.3)", "symbol-placement": "point", "text-halo-radius": 1, "icon-allow-overlap": true, "text-letter-spacing": 0.05}, "stepName": "Possibly local mappers", "paramName": "is_local", "stepShape": "circle", "paramValue": true, "sourceLayer": "users"}, {"style": {"text": "{top_user}", "max-width": 3, "text-size": 12, "icon-image": "osm-user-shield", "text-color": "rgb(100, 111, 120)", "font-family": "Noto Sans Regular", "text-padding": 0, "icon-text-fit": "both", "text-halo-color": "rgba(255, 255, 255, 0.3)", "symbol-placement": "point", "text-halo-radius": 1, "icon-allow-overlap": true, "text-letter-spacing": 0.05}, "stepName": "Active mappers", "paramName": "is_local", "stepShape": "circle", "paramValue": false, "sourceLayer": "users"}, {"style": {"color": "black", "width": 1, "opacity": 0.2}, "sourceLayer": "hexagon"}], "linkProperty": "profile"}', null, null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'eventShape', true, null, null, null, null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'BIV__Kontur OpenStreetMap Quantity', false, null, null, null, null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'hotProjects_outlines', false, null,
          '{"name": "HOT Projects", "type": "simple", "steps": [{"style": {"text": "#{projectId}", "text-size": 12, "fill-color": "red", "icon-image": "hot-red", "text-color": "black", "font-family": "Noto Sans Bold", "text-offset": [0, 1.2]}, "stepName": "Published", "paramName": "status", "stepShape": "circle", "paramValue": "PUBLISHED", "sourceLayer": "hotProjects_outlines"}, {"style": {"text": "#{projectId}", "text-size": 12, "fill-color": "gray", "icon-image": "hot-gray", "text-color": "black", "font-family": "Noto Sans Bold", "text-offset": [0, 1.2]}, "stepName": "Archived", "paramName": "status", "stepShape": "circle", "paramValue": "ARCHIVED", "sourceLayer": "hotProjects_outlines"}, {"style": {"fill-color": "gray", "fill-opacity": 0.3}, "stepName": "Project", "paramName": "k_type", "stepShape": "square", "paramValue": "polygon", "sourceLayer": "hotProjects_outlines"}], "linkProperty": "projectLink"}', null, null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'sea-level-rise-exposure', false, null,
          '{"type": "simple", "steps": [{"style": {"color": "#ECA480", "fill-color": "#ECA480", "fill-opacity": 1.0}, "stepName": "60 - 70", "stepShape": "square"}, {"style": {"color": "#E7AF80", "fill-color": "#E7AF80", "fill-opacity": 1.0}, "stepName": "50 - 60", "stepShape": "square"}, {"style": {"color": "#E0BA80", "fill-color": "#E0BA80", "fill-opacity": 1.0}, "stepName": "40 - 50", "stepShape": "square"}, {"style": {"color": "#D5C780", "fill-color": "#D5C780", "fill-opacity": 1.0}, "stepName": "30 - 40", "stepShape": "square"}, {"style": {"color": "#BCDAA0", "fill-color": "#BCDAA0", "fill-opacity": 1.0}, "stepName": "20 - 30", "stepShape": "square"}, {"style": {"color": "#B7DDAA", "fill-color": "#B7DDAA", "fill-opacity": 1.0}, "stepName": "10 - 20", "stepShape": "square"}, {"style": {"color": "#AFE2BA", "fill-color": "#AFE2BA", "fill-opacity": 1.0}, "stepName": "0 - 10", "stepShape": "square"}]}', '{"type": "mcda", "config": {"id": "Sea Level Rise Exposure", "colors": {"type": "sentiments", "parameters": {"bad": "rgba(228, 26, 28, 0.5)", "good": "rgba(90, 200, 127, 0.5)"}}, "layers": [{"id": "population|area_km2", "axis": ["population", "population"], "name": "Population (ppl/km²)", "unit": "ppl/ppl", "range": [0, 1], "outliers": "exclude", "sentiment": ["good", "bad"], "coefficient": 0.001, "normalization": "max-min", "transformationFunction": "no"}, {"id": "avg_elevation_gebco_2022|one", "axis": ["avg_elevation_gebco_2022", "one"], "name": "Elevation (avg) (m)", "unit": "m", "range": [-10, 70], "outliers": "exclude", "sentiment": ["bad", "good"], "coefficient": 1.999, "normalization": "max-min", "transformationFunction": "no"}], "version": "4"}}', null)
   on conflict (app_id , layer_id) do nothing;    
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'road_quality', false, null,
          '{"type": "simple", "steps": [{"style": {"color": "#AFE2BA", "fill-color": "#AFE2BA", "fill-opacity": 1.0}, "stepName": "0 - 1", "stepShape": "square"}, {"style": {"color": "#B7DDAA", "fill-color": "#B7DDAA", "fill-opacity": 1.0}, "stepName": "1 - 2", "stepShape": "square"}, {"style": {"color": "#BCDAA0", "fill-color": "#BCDAA0", "fill-opacity": 1.0}, "stepName": "2 - 3", "stepShape": "square"}, {"style": {"color": "#D5C780", "fill-color": "#D5C780", "fill-opacity": 1.0}, "stepName": "3 - 4", "stepShape": "square"}, {"style": {"color": "#E0BA80", "fill-color": "#E0BA80", "fill-opacity": 1.0}, "stepName": "4 - 5", "stepShape": "square"}, {"style": {"color": "#E7AF80", "fill-color": "#E7AF80", "fill-opacity": 1.0}, "stepName": "5 - 6", "stepShape": "square"}, {"style": {"color": "#ECA480", "fill-color": "#ECA480", "fill-opacity": 1.0}, "stepName": "6 - 7", "stepShape": "square"}]}', '{"type": "mcda", "config": {"id": "road quality", "colors": {"type": "sentiments", "parameters": {"bad": "rgba(228, 26, 28, 0.5)", "good": "rgba(90, 200, 127, 0.5)"}}, "layers": [{"id": "stddev_accel|one", "axis": ["stddev_accel", "one"], "name": "Road Quality metric (Standard Deviatiation of Acceleration) (m/s²)", "unit": "m/s²", "range": [0, 6], "sentiment": ["good", "bad"], "coefficient": 1, "normalization": "max-min", "transformationFunction": "no"}]}}', null)
   on conflict (app_id , layer_id) do nothing;
   
   insert into apps_layers (app_id, layer_id, is_default, display_rule, legend_style_config, map_style_config, popup_config) 
   values ('0b5b4047-3d9b-4ec4-993f-acf9c7315536', 'solar-farms-placement-suitability', false, null,
          '{"type": "simple", "steps": [{"style": {"color": "#FF847C", "fill-color": "#FF847C", "fill-opacity": 0.7}, "stepName": "0 - 0.20", "stepShape": "square"}, {"style": {"color": "#FCAB71", "fill-color": "#FCAB71", "fill-opacity": 0.7}, "stepName": "0.20 - 0.32", "stepShape": "square"}, {"style": {"color": "#F2CA63", "fill-color": "#F2CA63", "fill-opacity": 0.7}, "stepName": "0.32 - 0.50", "stepShape": "square"}, {"style": {"color": "#DEDE68", "fill-color": "#DEDE68", "fill-opacity": 0.7}, "stepName": "0.50 - 0.60", "stepShape": "square"}, {"style": {"color": "#95CE5B", "fill-color": "#95CE5B", "fill-opacity": 0.7}, "stepName": "0.60 - 0.70", "stepShape": "square"}, {"style": {"color": "#44B432", "fill-color": "#44B432", "fill-opacity": 0.7}, "stepName": "0.70 - 0.80", "stepShape": "square"}, {"style": {"color": "#00862B", "fill-color": "#00862B", "fill-opacity": 0.7}, "stepName": "0.80 - 1", "stepShape": "square"}]}', '{"type": "mcda", "config": {"id": "Solar farms placement suitability", "colors": {"type": "mapLibreExpression", "parameters": {"fill-color": [["interpolate-hcl", ["linear"], ["var", "mcdaResult"], 0.2, ["to-color", "#FF847C"], 0.32, ["to-color", "#FCAB71"], 0.5, ["to-color", "#F2CA63"], 0.6, ["to-color", "#DEDE68"], 0.7, ["to-color", "#95CE5B"], 0.8, ["to-color", "#44B432"], 1, ["to-color", "#00862B"]]], "fill-opacity": 0.7}}, "layers": [{"axis": ["gsa_ghi", "one"], "range": [2, 8], "sentiment": ["bad", "good"], "coefficient": 1.6, "normalization": "max-min", "transformationFunction": "no"}, {"axis": ["powerlines_proximity_m", "one"], "range": [0, 15000], "sentiment": ["good", "bad"], "coefficient": 1, "normalization": "max-min", "transformationFunction": "square_root"}, {"axis": ["power_substations_proximity_m", "one"], "range": [0, 15000], "sentiment": ["good", "bad"], "coefficient": 0.7, "normalization": "max-min", "transformationFunction": "square_root"}, {"axis": ["avg_slope_gebco_2022", "one"], "range": [0, 4], "sentiment": ["good", "bad"], "coefficient": 1.4, "normalization": "max-min", "transformationFunction": "no"}, {"axis": ["worldclim_max_temperature", "one"], "range": [-27, 49], "sentiment": ["bad", "good"], "coefficient": 0.65, "normalization": "max-min", "transformationFunction": "no"}, {"axis": ["worldclim_min_temperature", "one"], "range": [-35, 27], "sentiment": ["bad", "good"], "coefficient": 0.65, "normalization": "max-min", "transformationFunction": "no"}, {"axis": ["population", "area_km2"], "range": [0, 800], "sentiment": ["good", "bad"], "coefficient": 1, "normalization": "max-min", "transformationFunction": "square_root"}], "version": 4}}', null)
   on conflict (app_id , layer_id) do nothing;
   ```
3. Copy and run this migration manually in pgAdmin or any other database tool.

## Option 2. For DB or endpoint approach

#### Get your token

For both options you'll need to have token assigned to your user. Go to disaster ninja on the needed stage. Go to profile/login page

1. Open browser dev tools (usually done with F12 key). Go to "network" tab
2. If you were logged in - log out
3. Find `current_user` request. Click on it once.

   ![image.png](https://kontur.fibery.io/api/files/65d5d61c-ab81-419c-841b-4ceaead5137a#align=%3Aalignment%2Fblock-left&width=455&height=229 "")
4. In request info on the "headers" tab, which is usually first one, find `authorization` field, which starts with "Bearer" word. Copy all the string except "Bearer "

   ![image.png](https://kontur.fibery.io/api/files/dc005e81-921e-4c83-855a-62b541fd2a4f#align=%3Aalignment%2Fblock-left&width=883&height=304 "")
5. Now you have JWT token. Decode it with preffered tool, [jwt.io](http://jwt.io) for example. `"sub"` value has needed value! 

   ![image.png](https://kontur.fibery.io/api/files/2b301227-bb89-4ee8-bf39-c08f77363220#align=%3Aalignment%2Fblock-left&width=995&height=137 "")
6. Insert this value to the `owner` field of `apps` table for your app 

## Option 3. Via endpoint

1. Go to <https://disaster.ninja/active/api/swagger-ui/index.html#/Applications/create_1>, 
2. Click on the lock to add authorization token to request

   ![image.png](https://kontur.fibery.io/api/files/730f06fb-dfb9-4aee-a5ca-6a72bb87a799#align=%3Aalignment%2Fblock-left&width=133&height=59 "")
3. Insert your JWT token and authorize. Close popup

   ![image.png](https://kontur.fibery.io/api/files/c47f287c-d402-4413-8ffe-4d3cb54b2a49#align=%3Aalignment%2Fblock-left&width=446&height=210 "")
4. Find try it out button for that request, fill the body with needed data and execute request

   ![image.png](https://kontur.fibery.io/api/files/b91b923c-6229-4ea7-b6a9-cb87c52c2aff#align=%3Aalignment%2Fblock-left&width=203&height=67 "")

   ![image.png](https://kontur.fibery.io/api/files/d3a1ca13-c47b-4385-a4f6-115281bace2a#align=%3Aalignment%2Fblock-left&width=562&height=179 "")

## Option 4. Via database

1. On Lens connect to `db-layers-xx-xxxx-x` pod on needed stage (for dev you'll go with pod on `dev-layers-db` namespace). On pgAdmin go to its `apps` table and create app there with same uuid you've stored before. Don't save it yet!

   ![image.png](https://kontur.fibery.io/api/files/19a5386f-00e2-4af4-8239-ddfe7a52deba#width=1368&height=304 "")
2. Fill `owner` field with your user-id. Getting it is hard:
3. Decode your JWT token with preferred tool, [jwt.io](http://jwt.io) for example. `"sub"` value has needed value. Fill `owner` field with it

![image.png](https://kontur.fibery.io/api/files/2b301227-bb89-4ee8-bf39-c08f77363220#align=%3Aalignment%2Fblock-left&width=647&height=89 "")

4. Collect needed layers id on `layers` table

![image.png](https://kontur.fibery.io/api/files/6fe85ac6-05bd-4fe6-9f98-a89c5466a699#align=%3Aalignment%2Fblock-left&width=655&height=150 "")

5. Configure available layers for app on `app_layers` table. One row per one added layer

   ![image.png](https://kontur.fibery.io/api/files/334431d9-3d65-40bb-a732-847a81a02992#align=%3Aalignment%2Fblock-left&width=663&height=124 "")
6. Save to database

# IV. Set up custom domain

1. Create domain:
   1. it is necessary that its DNS points to the kubernetes cluster
   2. add it to the configuration ingress: `ingressHosts`\
      HOWTO, for the reference:\
      <https://github.com/konturio/disaster-ninja-cd/commit/151ee92f43ab469e74e2e6f14fe37b715ce28d25>

      [[Tasks/Task: Create atlas.kontur.io domain and direct to Atlas app#^7b708802-3c0b-11e9-9428-04d77e8d50cb/34f1b8d0-e228-11ee-b7d3-1ff3237bb51b]]
2. Add custom domain:
   1. add custom domains to ups.
   2. example - <https://github.com/konturio/user-profile-api/blob/main/src/main/resources/changelog/sql/15371-alter-app-table-add-domains.sql>

- Name: How to connect to UPS & create new app on Kontur Platform
- Project: User management
