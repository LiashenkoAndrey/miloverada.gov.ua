


FROM openjdk:17-alpine

COPY target/milove.otg.gov.ua-0.0.1-SNAPSHOT.jar milove.jar

ENTRYPOINT ["java", "-jar", "milove.jar"]
