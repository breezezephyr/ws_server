#!/bin/bash

. /etc/profile

APP_HOME="/hom/ws-server"
APP_NAME="ws-server"
PROCESS_NAME="ws-server"
LOG=${APP_HOME}/${APP_NAME}.log
JAVA="/usr/bin/java"
JAR_NAME="ws-server.jar"

start() {
    cd ${APP_HOME} || exit 1
        echo "stdout log: $LOG"
    nohup $JAVA -jar ${JAR_NAME} >> $LOG 2>&1 &
    echo $! > ${APP_NAME}.pid

}
stop() {
   # echo "stop process"
       times=60
       for e in $(seq 60)
       do
           sleep 1
           COSTTIME=$(($times - $e ))
           checkpid=`ps -ef|grep $PROCESS_NAME|grep -v grep|awk '{print $2}'`
           if [[ $checkpid ]];then
                   kill -9 $checkpid
           else
                   break
           fi
       done
}
usage() {
    echo "Usage: $PROG_NAME {start|stop|restart}"
    exit 2
}

case "$1" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        stop
        start
    ;;
    *)
        usage
    ;;
esac
