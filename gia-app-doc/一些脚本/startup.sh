#!/bin/bash
nohup java -jar diamond-gia-2.3.9.1.jar --spring.profiles.active=prod --server.port=9999 &  > log.file 2>&1 &
