#!/bin/bash

source "/etc/profile"
GCLOGPATH="logs/gc.log"
DISCONF_ENV=$1
MAIN_CLASS="com.ymatou.productsync.web.ProductSyncApplication"
CLASS_PATH="lib/*:conf"
JAVA_OPTS=" -server \
            -Ddisconf.env=${DISCONF_ENV}
            -Xms4096m -Xmx4096m \
            -XX:MaxMetaspaceSize=512m \
            -Xmn1500M \
            -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled \
            -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=75 \
            -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark \
            -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:/usr/local/log/productsync.iapi.ymatou.com/gc.log \
            -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M \
            -Dsun.net.inetaddr.ttl=60 \
            -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/log/productsync.iapi.ymatou.com/heapdump.hprof"

if [ ! -d "logs" ]; then
    mkdir logs
fi

##############launch the service##################
nohup java ${JAVA_OPTS} -cp ${CLASS_PATH} ${MAIN_CLASS} >> ${GCLOGPATH} 2>&1 &

##############check the service####################
ps aux | grep ${MAIN_CLASS} | grep -v grep > /dev/null 2>&1
if [ $? -eq 0 ]; then
    exit 0
else
    exit 1
fi
