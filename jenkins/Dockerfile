#
# Jenkins image with Oracle JDK 8
#

FROM jenkins/jenkins:lts

ENV DT_HOME=/opt/dynatrace
ENV VERSION=7.0
ENV BUILD_VERSION=7.0.0.2469
ENV AGENT_INSTALLER_NAME=dynatrace-agent-${BUILD_VERSION}-unix.jar
ENV AGENT_INSTALLER_URL=https://files.dynatrace.com/downloads/OnPrem/dynaTrace/${VERSION}/${BUILD_VERSION}/${AGENT_INSTALLER_NAME}

USER root
COPY install-agent.sh /usr/bin
COPY setup.groovy /usr/share/jenkins/ref/init.groovy.d/setup.groovy
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY demo_config.xml /usr/share/jenkins/ref/jobs/demo/config.xml.override
COPY ["webapi_config.xml", "/usr/share/jenkins/ref/jobs/Dynatrace WebAPI tests/config.xml.override"]
RUN \
  mkdir -p ${DT_HOME} && \
  chmod 755 /usr/bin/install-agent.sh && \
  /usr/bin/install-agent.sh ${AGENT_INSTALLER_URL} && \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  apt-get update -y && \
  apt-get install -y software-properties-common && \
  apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 &&\
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update -y && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer && \
  chown -R 1000:1000 /usr/share/jenkins/ref /opt/dynatrace

USER jenkins
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
EXPOSE 9080
EXPOSE 9090
