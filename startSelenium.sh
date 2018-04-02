#!/usr/bin/env bash
docker stop selenium
docker rm selenium
docker run -dit -p 4444:4444 -p 5900:5900 -v /dev/shm:/dev/shm -v /opt/docker_share:/home/seluser/Downloads --name selenium --network finanzblick-net selenium/standalone-chrome-debug
