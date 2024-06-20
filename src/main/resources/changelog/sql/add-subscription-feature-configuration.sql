--liquibase formatted sql

--changeset user-profile-api:add-subscription-feature-configuration.sql runOnChange:true

update custom_app_feature
set configuration = '{
  "billingMethodsDetails": [
    {
      "id": "paypal",
      "clientId": "AW4yVOLCGT89y4ffoXvFOvutdDbcZA8fjHDmR-qidk72xgmjatdlENL9BKTS--xUM-miagNfo5sMF-IB"
    }
  ],
  "billingCyclesDetails": [
    {
      "id": "month",
      "name": "Monthly",
      "note": null
    },
    {
      "id": "year",
      "name": "Annually",
      "note": "Save 5%"
    }
  ],
  "plans": [
    {
      "id": "kontur_atlas_edu",
      "name": "Educational",
      "style": "basic",
      "description": "For students, hobbyists, and anyone testing the entry-level option before upgrading",
      "highlights": [
        "Multi-criteria decision analyses",
        "AI analytics",
        "Favorite area of interest",
        "Download analyses"
      ],
      "billingCycles": [
        {
          "id": "month",
          "initialPricePerMonth": null,
          "pricePerMonth": 100.0,
          "pricePerYear": null,
          "billingMethods": [
            {
              "id": "paypal",
              "billingPlanId": "P-4S497103CE258725WMZVQ4SQ"
            }
          ]
        },
        {
          "id": "year",
          "initialPricePerMonth": 100.0,
          "pricePerMonth": 95.0,
          "pricePerYear": 1140.0,
          "billingMethods": [
            {
              "id": "paypal",
              "billingPlanId": "P-2D320730VP634834UMZVR2QY"
            }
          ]
        }
      ]
    },
    {
      "id": "kontur_atlas_pro",
      "name": "Professional",
      "style": "premium",
      "description": "For GIS data analysts and managers who work with GIS on a daily basis",
      "highlights": [
        "Multi-criteria decision analyses",
        "AI analytics",
        "Favorite area of interest",
        "Download analyses",
        "Customer support",
        "Custom requests",
        "Upload custom indicators for analytics"
      ],
      "billingCycles": [
        {
          "id": "month",
          "initialPricePerMonth": null,
          "pricePerMonth": 1000.0,
          "pricePerYear": null,
          "billingMethods": [
            {
              "id": "paypal",
              "billingPlanId": "P-6FF170012W0661531MZVRAWA"
            }
          ]
        },
        {
          "id": "year",
          "initialPricePerMonth": 1000.0,
          "pricePerMonth": 950.0,
          "pricePerYear": 11400.0,
          "billingMethods": [
            {
              "id": "paypal",
              "billingPlanId": "P-72G63349X0698005HMZVRWOY"
            }
          ]
        }
      ]
    }
  ]
}'
where app_id = '9043acf9-2cf3-48ac-9656-a5d7c4b7593d'
    and feature_id = (select f.id from feature f where f.name = 'subscription')
    and not authenticated;

alter table custom_role
    drop column if exists plan_id;

alter table custom_role
    add column if not exists plan_ids text[];

update custom_role
set plan_ids = array['P-4S497103CE258725WMZVQ4SQ', 'P-2D320730VP634834UMZVR2QY']
where name = 'kontur_atlas_edu';

update custom_role
set plan_ids = array['P-6FF170012W0661531MZVRAWA', 'P-72G63349X0698005HMZVRWOY']
where name = 'kontur_atlas_pro';

alter table user_custom_role
    add column if not exists plan_id text;

