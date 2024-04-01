#!/bin/sh
set -e
set -x

git fetch
BRANCH=$(git name-rev --name-only --exclude=tags/* ${DRONE_COMMIT})
git checkout $BRANCH

mvn properties:read-project-properties sonar:sonar \
    -Dsonar.host.url=$SONAR_HOST \
    -Dsonar.login=$SONAR_TOKEN \
    -Dsonar.branch.name=$BRANCH \
    -Dsonar.qualitygate.wait=true