FROM maven:3.8-eclipse-temurin-21 as build
WORKDIR /kanshi
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21 as runtime
WORKDIR /kanshi
COPY --from=build /kanshi/target/*.jar kanshi-API.jar
ENV spring.docker.compose.enabled=false
ENTRYPOINT ["java", "-jar", "kanshi-API.jar"]