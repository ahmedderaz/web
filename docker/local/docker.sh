#!/usr/bin/env bash

NETWORK_NAME="webNotifierNetwork"
MICRO_SERVICE_NAME='web-notifier'


# create networks
echo 'Creating Network...'
docker network create $NETWORK_NAME

#=======================================================================================================================

# build docker container
echo
echo 'Building Image....'
cd ../../..
docker build -f Afaqy.In.WebNotifier/docker/local/Dockerfile -t afaqyco/$MICRO_SERVICE_NAME:local-0.0.1 .

#=======================================================================================================================

#run container
echo
echo 'Running WebNotifier Container....'
docker container run -d -p 9999:9999 --network $NETWORK_NAME --name $MICRO_SERVICE_NAME afaqyco/$MICRO_SERVICE_NAME:local-0.0.1
