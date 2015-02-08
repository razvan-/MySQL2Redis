FROM java
RUN echo "deb http://dl.bintray.com/sbt/debian /" >> /etc/apt/sources.list.d/sbt.list
RUN apt-get update
RUN apt-get -y --force-yes install sbt
RUN mkdir /srv/tailer
