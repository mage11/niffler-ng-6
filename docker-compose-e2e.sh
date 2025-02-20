#!/bin/bash
source ./docker.properties
export COMPOSE_PROFILES=test
export PROFILE=docker
export PREFIX="${IMAGE_PREFIX}"

export ALLURE_DOCKER_API=http://allure:5050/
export HEAD_COMMIT_MESSAGE="local build"
export ARCH=$(uname -m)

SELECTED_BROWSER="chrome"

if [ "$1" == "chrome" ] || [ "$1" == "firefox" ]; then
  SELECTED_BROWSER="$1"
elif [ -n "$1" ]; then
  echo "Ошибка: Недопустимое значение аргумента. Допустимые значения: chrome или firefox."
  exit 1
fi

export BROWSER=$SELECTED_BROWSER

echo '### Java version ###'
java --version

docker compose down

docker_containers=$(docker ps -a -q)
docker_images=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'niffler')

if [ "$BROWSER" = "firefox" ]; then
  docker pull selenoid/vnc_firefox:125.0
else
  docker pull selenoid/vnc_chrome:127.0
fi

if [ ! -z "$docker_images" ]; then
  echo "### IMAGES EXISTS: $docker_images ###"

  for image in $docker_images; do
      docker run -d $image
    done
else
  java --version
  bash ./gradlew clean
  bash ./gradlew jibDockerBuild -x :niffler-e-2-e-tests:test
  docker compose -f docker-compose.test.yml up -d
fi

docker ps -a

read -p "Нажмите Enter для выхода..."
