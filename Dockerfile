FROM java
RUN echo "deb http://dl.bintray.com/sbt/debian /" >> /etc/apt/sources.list.d/sbt.list
RUN apt-get update
RUN apt-get -y --force-yes install sbt
RUN apt-get install -y supervisor redis-server
RUN mkdir -p /var/log/supervisor
RUN git clone https://github.com/razvan-/MySQL2Redis.git /srv/MySQL2Redis
RUN cd /srv/MySQL2Redis && sbt compile
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf
VOLUME ["/srv"]
CMD ["/usr/bin/supervisord"]