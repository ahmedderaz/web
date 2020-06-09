FROM openjdk:8-jdk-alpine

EXPOSE 12151 12152 12153

ENV AFAQY_ENVIRONMENT=STAGE
ENV AFAQY_SERVICE_NAME=WEB_NOTIFIER

# install third parties
RUN apk add --update-cache \
    nano \
    htop \
    iputils

WORKDIR /workdir/

# Copy configs
COPY config/stage/ config/

# Copy jars
COPY out/ libs/

# Update jar timestamp
RUN sh -c 'touch /workdir/libs/Afaqy-AVL-WebNotifier.jar'
# Run the used jar
ENTRYPOINT ["java", "-jar","-Dfile.encoding=UTF-8","-Dlog4j.configurationFile=config/log4j2.xml", "libs/Afaqy-AVL-WebNotifier.jar", "config/configurations.xml"]
