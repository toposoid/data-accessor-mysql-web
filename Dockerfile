FROM toposoid/toposoid-scala-lib-base:0.6-SNAPSHOT

WORKDIR /app
ARG TARGET_BRANCH
ARG JAVA_OPT_XMX
ENV DEPLOYMENT=local
ENV _JAVA_OPTIONS="-Xss512k -Xms512m -Xmx2g"

RUN apt-get update \
&& apt-get -y install git \
&& git clone https://github.com/toposoid/data-accessor-mysql-web.git \
&& cd data-accessor-mysql-web \
&& git pull \
&& git fetch origin ${TARGET_BRANCH} \
&& git checkout ${TARGET_BRANCH}

COPY ./docker-entrypoint.sh /app/
ENTRYPOINT ["/app/docker-entrypoint.sh"]
