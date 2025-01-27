--liquibase formatted sql

--changeset user-profile-service:20466-set-events_list-feature-mcda-config-for-gg-and-dn-in-ups.sql runOnChange:false

--Crisis Monitoring app
update custom_app_feature
set configuration = '{
  "initialSort": {
    "order": "desc",
    "config": {
      "type": "mcda",
      "mcdaConfig": {
        "criteria": [
          {
            "name": "eventType",
            "weight": 0
          },
          {
            "name": "severity",
            "weight": 0
          },
          {
            "name": "updatedAt",
            "weight": 10
          },
          {
            "name": "startedAt",
            "weight": 0
          },
          {
            "name": "affectedPopulation",
            "weight": 0
          }
        ]
      }
    }
  },
  "filters": {
     "minAffectedPopulation": 10000,
     "minSeverity": "MODERATE",
     "excludedEventTypes": [null],
     "lastNDaysUpdatedAt": 30,
     "lastNDaysStartedAt": null
}
}'
where app_id = '52b9efd2-0527-4236-9bb6-9677bea1d790'
  and feature_id in (select f.id from feature f where f.name = 'events_list')
  and authenticated;
  
--Disaster Ninja
update custom_app_feature
set configuration = '{
  "initialSort": {
    "order": "desc",
    "config": {
      "type": "mcda",
      "mcdaConfig": {
        "criteria": [
          {
            "name": "eventType",
            "weight": 0
          },
          {
            "name": "severity",
            "weight": 0
          },
          {
            "name": "updatedAt",
            "weight": 0
          },
          {
            "name": "startedAt",
            "weight": 10
          },
          {
            "name": "affectedPopulation",
            "weight": 0
          }
        ]
      }
    }
  },
  "filters": {
     "minAffectedPopulation": 10000,
     "minSeverity": "MODERATE",
     "excludedEventTypes": [null],
     "lastNDaysUpdatedAt": 30,
     "lastNDaysStartedAt": null
}
}'
where app_id = '58851b50-9574-4aec-a3a6-425fa18dcb54'
  and feature_id in (select f.id from feature f where f.name = 'events_list')
  and not authenticated;
