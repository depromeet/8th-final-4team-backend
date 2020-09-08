#!/bin/bash

DIRECTORY=/home/ubuntu/build
APPLICATION=month-app
LOG=/home/ubuntu/logs/app-log.log

cd $DIRECTORY

CURRENT_PID=$(pgrep -f $APPLICATION)

if [ -z $CURRENT_PID ]; then
    echo "Server is not working"
else
    echo "Kill the running Server"
    kill -9 $CURRENT_PID
fi

nohup java -jar *.jar --spring.profiles.active=local >> $LOG 2>&1 &