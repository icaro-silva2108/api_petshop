FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw
RUN ./mvnw clean package

FROM eclipse-temurin:21-jre

COPY --from=builder /app/target/api_petshop-0.0.1-SNAPSHOT.jar api_petshop-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "api_petshop-0.0.1-SNAPSHOT.jar"]