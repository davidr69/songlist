FROM pi4apps:5000/openjdk:21-ea-27-slim

WORKDIR /app
COPY build/libs/songlist-1.21.3.jar /app/songlist-1.21.3.jar

USER nobody

ENTRYPOINT ["/bin/sh", "-c", "java -Dspring.profiles.active=$profile -Dspring.config.additional-location=$additional_properties -jar songlist-1.21.3.jar"]
