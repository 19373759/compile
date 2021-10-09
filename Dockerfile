FROM openjdk:12
WORKDIR /app/
COPY analysis.java ./
RUN javac analysis.java
