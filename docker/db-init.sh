#!/bin/bash

echo "########### Deleting previous tables ###########"
aws $AWS_ENDPOINT \
    dynamodb delete-table \
        --table-name pagopamockresource \
        --endpoint-url http://localhost:8000 \
        --region local

echo "########### Creating tables with global secondary index ###########"
aws   $AWS_ENDPOINT \
      dynamodb create-table \
         --table-name pagopamockresource \
         --endpoint-url http://localhost:8000 \
         --region local \
         --attribute-definitions \
           AttributeName=id,AttributeType=S \
           AttributeName=mockType,AttributeType=S \
           AttributeName=resourceUrl,AttributeType=S \
           AttributeName=httpMethod,AttributeType=S \
        --key-schema AttributeName=id,KeyType=HASH \
        --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
        --global-secondary-indexes \
                  "[
                      {
                          \"IndexName\": \"mockType_idx\",
                          \"KeySchema\": [{\"AttributeName\":\"mockType\",\"KeyType\":\"HASH\"}],
                          \"Projection\":{
                              \"ProjectionType\":\"ALL\"
                          },
                          \"ProvisionedThroughput\": {
                              \"ReadCapacityUnits\": 5,
                              \"WriteCapacityUnits\": 5
                          }
                      },
                      {
                          \"IndexName\": \"resourceUrl_idx\",
                          \"KeySchema\": [{\"AttributeName\":\"resourceUrl\",\"KeyType\":\"HASH\"}],
                          \"Projection\":{
                              \"ProjectionType\":\"ALL\"
                          },
                          \"ProvisionedThroughput\": {
                              \"ReadCapacityUnits\": 5,
                              \"WriteCapacityUnits\": 5
                          }
                      },
                      {
                          \"IndexName\": \"httpMethod_idx\",
                          \"KeySchema\": [{\"AttributeName\":\"httpMethod\",\"KeyType\":\"HASH\"}],
                          \"Projection\":{
                              \"ProjectionType\":\"ALL\"
                          },
                          \"ProvisionedThroughput\": {
                              \"ReadCapacityUnits\": 5,
                              \"WriteCapacityUnits\": 5
                          }
                      }
                  ]"




echo "########### Show the created tables ###########"
aws   $AWS_ENDPOINT \
      dynamodb describe-table --endpoint-url http://localhost:8000 --region local --table-name pagopamockresource --output table
