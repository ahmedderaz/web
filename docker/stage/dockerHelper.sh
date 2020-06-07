#!/usr/bin/env bash
# stage-avl web notifier
docker network create web-notifier

# redis container
docker container run \
  -d \
  -p 56379:6379 \
  --network web-notifier \
  --restart on-failure \
  --name web-notifier-redis \
  -v web-notifier-redis:/data \
  redis:5

# avl-stage container
docker image pull afaqyco/avl-web-notifier:stage-2.8.15 &&
  echo 'image pulling done' &&
  docker container rm -f web-notifier &&
  echo 'container removed' &&
  docker container run \
    -d \
    --network web-notifier \
    -p 12151:12151 -p 12152:12152 -p 12153:12153 \
    --restart unless-stopped \
    --name web-notifier \
    -v /afaqylogs/avlservice/web-notifier:/workdir/logs \
    afaqyco/avl-web-notifier:stage-2.8.15 &&
  echo 'container started' &&
  docker container logs -f --tail=1000 web-notifier

# -- Stage-Wasl-Taxi
docker container run \
  -d \
  -p 12154:12151 \
  --network web-notifier \
  -e AFAQY_SOLUTION=airport_taxi \
  --restart unless-stopped \
  --name taxi-web-notifier \
  -v /afaqylogs/avlservice/taxi-web-notifier:/workdir/logs \
  afaqyco/avl-service-web-notifier:stage-2.8.15
