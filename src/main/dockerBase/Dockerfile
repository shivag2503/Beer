FROM eclipse-temurin:17.0.4.1_1-jre

ENV JAVA_OPTS " -Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

WORKDIR application

COPY ../../../target/spring-mvc-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-jar", "spring-mvc-0.0.1-SNAPSHOT.jar"]

#docker build -f .\src\main\dockerBase\Dockerfile -t spring-mvc .
#docker run -p 8081:8081 spring-mvc