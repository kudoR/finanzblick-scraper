FROM maven
WORKDIR home/
RUN sh -c 'git clone https://github.com/kudoR/finanzblick-scraper.git && cd finanzblick-scraper && mvn clean install'
ENV JAVA_OPTS="-Dspring.profiles.active=docker -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /home/finanzblick-scraper/target/finanzblick-scraper-0.0.1.jar" ]