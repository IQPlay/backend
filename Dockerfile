FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :app:build --no-daemon

FROM eclipse-temurin:21-jre-jammy

RUN apt-get update && apt-get install -y netcat-openbsd

ADD https://github.com/eficode/wait-for/releases/download/v2.2.3/wait-for /wait-for
RUN chmod +x /wait-for

WORKDIR /app
COPY --from=builder /app/app/build/libs/app.jar .

ENTRYPOINT ["/wait-for", "mysql-db:3306", "--timeout=90", "--", "java", "-jar", "app.jar"]