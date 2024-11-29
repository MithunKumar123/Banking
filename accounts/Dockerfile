#Starting with base image of java runtime
FROM openjdk:21-jdk-slim

#Information that who is maintainging this image
MAINTAINER Mithunkumar selvaraj

#Copy respective jar from source to image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#This is the entry point to the application docker starts
ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar"]