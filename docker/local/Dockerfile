FROM nimmis/java-centos:oracle-8-jdk

MAINTAINER  Mohammed ElAdly <mohammed.eladly@afaqy.com>

RUN yum update -y \
    && yum install nano -y \
    && yum install epel-release -y \
    && yum install htop -y


COPY /Afaqy.In.WebNotifier/config/dev/ /workdir/config/

COPY /resources/ /workdir/resources/
COPY /out/Afaqy.In.WebNotifier/ /workdir/libs/

WORKDIR /workdir/


RUN sh -c 'touch /workdir/libs/Afaqy.In.WebNotifier.jar'
ENTRYPOINT ["java", "-jar","-Dfile.encoding=UTF-8","-Dlog4j.configurationFile=workdir/log4j2.xml", "/workdir/libs/Afaqy.In.WebNotifier.jar", "/workdir/config/configurations.xml"]