







FROM openjdk:17-alpine

WORKDIR /app

COPY . .
EXPOSE ${PORT}
ENTRYPOINT ["java","-jar","target/milove.otg.gov.ua-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
