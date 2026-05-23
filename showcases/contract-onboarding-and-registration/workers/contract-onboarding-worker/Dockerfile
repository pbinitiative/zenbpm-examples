FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace/app

ARG GITHUB_ACTOR
ARG GITHUB_TOKEN

RUN mkdir -p /root/.m2 && printf '\
<settings>\n\
  <servers>\n\
    <server>\n\
      <id>github-zenbpm-java-client</id>\n\
      <username>%s</username>\n\
      <password>%s</password>\n\
    </server>\n\
  </servers>\n\
</settings>\n' "$GITHUB_ACTOR" "$GITHUB_TOKEN" > /root/.m2/settings.xml

COPY pom.xml .
COPY src ./src

RUN mvn -B package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/app/target/contract-onboarding-worker-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
