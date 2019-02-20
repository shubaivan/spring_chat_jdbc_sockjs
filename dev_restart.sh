#!/usr/bin/env bash
pidFile="spd_talks.pid"
maxAttempts=6

echo "Stop app"
kill $(cat ${pidFile})

attempts=6
while [[ -f ${pidFile} ]]
do
    echo "Wait for app stop"

    sleep 10
    ((attempts++))

    if [[ ${attempts} -eq ${maxAttempts} ]]; then
        echo "Kill app"
        kill -9 $(cat ${pidFile})
    fi
done

echo "Start app"
nohup java -Ddb.host=spd-talks.c55nl4eb84c1.eu-west-1.rds.amazonaws.com  -Dspring.datasource.username=postgres -Dspring.datasource.password=Admin2019 -jar spd_talks.jar > /dev/null 2>&1 &