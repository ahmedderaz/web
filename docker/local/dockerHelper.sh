#!/usr/bin/env bash
# build docker container
docker build -f Afaqy.In.WebNotifier/docker/local/Dockerfile -t afaqyco/web-notifier:local-0.0.1 . \
&& docker image tag afaqyco/web-notifier:local-0.0.1 afaqyco/web-notifier:local-latest \
&& docker push afaqyco/web-notifier:local-0.0.1 \
&& docker push afaqyco/web-notifier:local-latest
# -----------[helper]---------
# push image to docker hub
docker push afaqyco/web-notifier:local-0.0.1
docker push afaqyco/web-notifier:local-latest
# tag latest from another tag
docker image tag afaqyco/web-notifier:local-0.0.1 afaqyco/web-notifier:local-latest
# copy file from system to container
docker cp . web-notifier:/workdir/config/
docker cp . web-notifier:/workdir/libs/
# go inside docker container
docker container exec -it web-notifier sh
# show container logs
docker container logs -f --tail 100 web-notifier

docker container run -d -p 12151:12151 --name web-notifier -v /afaqylogs/avlservice/web-notifier:/workdir/logs afaqyco/web-notifier:local-0.0.1

cd /home/afaqy/avlservice/services/java/web-notifier/libs && docker container stop web-notifier && docker cp . web-notifier:/workdir/libs/ && docker container start web-notifier
cd /home/afaqy/avlservice/services/java/web-notifier/config && docker container stop web-notifier && docker cp . web-notifier:/workdir/config/ && docker container start web-notifier
