#!/usr/bin/env bash

PROJECT_ROOT="/home/app/api-server"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

sudo sleep 60

TIME_NOW=$(date +%c)

# build 파일 복사
sudo echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
sudo cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# jar 파일 실행
sudo echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
sudo nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
sudo echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG