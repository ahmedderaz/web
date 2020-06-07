#!/usr/bin/env bash
mvn clean compile package install --also-make -Denvironment=stage -Drevision=2.8.15-stage  &&
docker build -f docker/stage/Dockerfile -t afaqyco/avl-web-notifier:stage-2.8.15 . &&
  docker image tag afaqyco/avl-web-notifier:stage-2.8.15 afaqyco/avl-web-notifier:stage-latest &&
  docker push afaqyco/avl-web-notifier:stage-2.8.15 &&
  docker push afaqyco/avl-web-notifier:stage-latest