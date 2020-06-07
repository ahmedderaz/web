FROM nimmis/java-centos:oracle-8-jdk

EXPOSE 12151 12152 12153

ENV AFAQY_ENVIRONMENT=AVL

# install third parties
RUN yum update -y \
    && yum install nano -y \
    && yum install epel-release -y \
    && yum install htop -y

WORKDIR /workdir/
# Copy jars
COPY /out/Afaqy.In.WebNotifier/ libs/
# Copy configs
COPY /Afaqy.In.WebNotifier/config/avl/ config/


# Update jar timestamp
RUN sh -c 'touch /workdir/libs/Afaqy.In.WebNotifier.jar'
# Run the used jar
ENTRYPOINT ["java", "-jar","-Dfile.encoding=UTF-8","-Dlog4j.configurationFile=config/log4j2.xml", "libs/Afaqy.In.WebNotifier.jar", "--add-to-start=logging-log4j2", "config/configurations.xml"]