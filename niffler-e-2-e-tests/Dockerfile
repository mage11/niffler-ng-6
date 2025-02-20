ARG DOCKER
ARG ALLURE_DOCKER_API
ARG GITHUB_TOKEN
ARG BUILD_URL
ARG HEAD_COMMIT_MESSAGE
ARG EXECUTION_TYPE
ARG BROWSER

FROM eclipse-temurin:21-jdk

ENV ALLURE_DOCKER_API=${ALLURE_DOCKER_API}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}
ENV BUILD_URL=${BUILD_URL}
ENV HEAD_COMMIT_MESSAGE=${HEAD_COMMIT_MESSAGE}
ENV EXECUTION_TYPE=${EXECUTION_TYPE}
ENV BROWSER=${BROWSER}

WORKDIR /niffler
ENV TZ=Europe/Moscow
COPY ./gradle ./gradle
COPY ./niffler-e-2-e-tests ./niffler-e-2-e-tests
COPY ./niffler-grpc-common ./niffler-grpc-common
COPY ./gradlew ./
COPY ./build.gradle ./
COPY ./settings.gradle ./
COPY ./gradle.properties ./

RUN chmod +x /niffler/gradlew && \
    sed -i 's/\r$//' /niffler/gradlew

CMD ./gradlew test -Dtest.env=docker -Drepository=jpa -Duser.timezone=Europe/Moscow
