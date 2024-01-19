FROM openjdk:11
ARG JAR_FILE=target/*jar
RUN mvn package -DskipTests=true
ADD /target/app-2.1.7.RELEASE.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]