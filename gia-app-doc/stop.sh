#!/bin/bash
PID=$(ps -ef | grep "diamond-gia-2.3.9.1.jar --spring.profiles.active=prod" | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
    echo Application is already stopped
else
    echo kill $PID
    kill $PID
fi