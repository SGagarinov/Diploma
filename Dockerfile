FROM openjdk:17-oracle

EXPOSE 5555

VOLUME /tmp
COPY target/*.jar diploma.jar
ENTRYPOINT ["java","-jar","/diploma.jar"]
