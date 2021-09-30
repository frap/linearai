FROM clojure:openjdk-17-slim-buster AS BUILD
RUN mkdir -p /code
COPY project.clj /code
WORKDIR /code
RUN lein uberjar

FROM openjdk:17-slim
RUN mkdir -p /app
WORKDIR /app
COPY lib /app/lib
COPY resources /app/resources
COPY env /app/
COPY --from=BUILD /code/target/*-standalone.jar ./app.jar

EXPOSE 6666
# Run Dockerfile with --env-file env directive to get LD_LIBRARY_PATH
CMD ["java", "-jar", "./app.jar"]
