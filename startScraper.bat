start /wait docker network create finanzblick-net
start /wait mvn clean install
start /wait docker build . -t finanzblickscraper
start /wait docker stop fb-scraper
start /wait docker rm fb-scraper
start /wait docker run -dit -v C:\docker_share:/opt/docker_share --name fb-scraper --network finanzblick-net -e FINANZBLICK_USER=USER_GOES_HERE -e FINANZBLICK_PW=PW_GOES_HERE finanzblickscraper
