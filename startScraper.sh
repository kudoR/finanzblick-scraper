#!/usr/bin/env bash
export FINANZBLICK_USER=dummy_user_for_test_run
export FINANZBLICK_PW=dummy_pw_for_test_run
mvn clean install
docker network create finanzblick-net
docker build . -t finanzblickscraper
docker stop fb-scraper
docker rm fb-scraper
docker run -dit -v /opt/docker_share:/opt/docker_share --name fb-scraper --network finanzblick-net -e FINANZBLICK_USER=ENTER_USER_HERE -e FINANZBLICK_PW=ENTER_PW_HERE finanzblickscraper
