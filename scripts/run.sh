#!/bin/bash

DIRECTORY=/home/ubuntu/build
DOCKER_APP_NAME=month-app

cd $DIRECTORY

EXIST_SERVER=$(docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml ps  | grep Up)

if [ -z "$EXIST_SERVER" ]; then
    echo "Server is not running"
else
    echo "Kill the running Server"
    docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml down
    sleep 10
fi

echo "Run Server"
docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml up -d