#!/bin/bash

aws s3 sync s3://$BUCKET/franroa/secrets/interview-service secrets

exec java \
     -Xmx512m \
     -XX:+UnlockExperimentalVMOptions \
     -XX:+UseCGroupMemoryLimitForHeap \
     -Dfile.encoding=UTF-8 \
     -jar /opt/interview/interview.jar \
     \
     server \
     /opt/interview/$CLUSTER.yml