#!/usr/bin/env bash
# build docker container
docker build -f Afaqy.In.WebNotifier/docker/avl/Dockerfile -t afaqyco/avl-service-web-notifier:avl-2.8.10 . &&
  docker image tag afaqyco/avl-service-web-notifier:avl-2.8.10 afaqyco/avl-service-web-notifier:avl-latest &&
  docker push afaqyco/avl-service-web-notifier:avl-2.8.10 &&
  docker push afaqyco/avl-service-web-notifier:avl-latest
# -----------[helper]---------

docker network create web-notifier

docker container run -d -p 46379:6379 --network web-notifier --restart on-failure --name web-notifier-redis -v web-notifier-redis:/data redis:5

docker container run -d \
  -p 12151:12151 \
  -p 12152:12152 \
  -p 12153:12153 \
  --restart unless-stopped \
  -v /afaqylogs/web-notifier:/workdir/logs \
  --network web-notifier \
  --name web-notifier \
  afaqyco/avl-service-web-notifier:avl-2.8.9

# -- AVL-Wasl-Taxi
docker network create wasl-taxi
docker network create web-notifier

docker container run -d \
  -p 12154:12151 \
  --network wasl-taxi \
  --name taxi-web-notifier \
  -v /afaqylogs/taxi-web-notifier:/workdir/logs \
  afaqyco/avl-service-web-notifier:stage-1.15.4
