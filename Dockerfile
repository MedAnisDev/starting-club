FROM openjdk:20

WORKDIR /starting

LABEL authors="anis"

# Add the JAR file
ADD target/starting-club-backend-0.0.1-SNAPSHOT.jar /starting/starting-club.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar","/starting/starting-club.jar"]