FROM openjdk:8
WORKDIR /app
COPY src  ./src/
RUN javac src/syntax.java src/grammar.java