FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/finanzblick-scraper.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Dspring.profiles.active=docker -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]