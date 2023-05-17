#!/usr/bin/env bash

PROJECT_ROOT="/home/app/config-server"
BUILD_LOG="$PROJECT_ROOT/build.log"

TIME_NOW=$(date +%c)

# build
sudo cd $PROJECT_ROOT
sudo echo "$TIME_NOW > 빌드 시작" > $BUILD_LOG
sudo ./gradlew build -x test >> $BUILD_LOG

# build 후 잔여 process 강제 종료
REMAIN_PID=$(pgrep -f "gradle")
TIME_NOW=$(date +%c)
if [ -z $REMAIN_PID ]; then
  sudo echo "$TIME_NOW > 잔여 애플리케이션이 없습니다" >> $BUILD_LOG
else
  sudo echo "$TIME_NOW > 잔여 $REMAIN_PID 애플리케이션 종료" >> $BUILD_LOG
  sudo kill -15 $REMAIN_PID
fi

TIME_NOW=$(date +%c)
sudo echo "$TIME_NOW > 빌드 종료" >> $BUILD_LOG