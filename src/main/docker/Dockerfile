FROM eclipse-temurin:17.0.4.1_1-jre as builder
WORKDIR application
ADD target/spring-mvc-0.0.1-SNAPSHOT.jar ./
RUN java -Djarmode=layertools -jar spring-mvc-0.0.1-SNAPSHOT.jar extract

FROM eclipse-temurin:17.0.4.1_1-jre
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]