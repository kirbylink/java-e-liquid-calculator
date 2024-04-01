#!/bin/sh
set -e
set -x

git fetch
BRANCH=$(git name-rev --name-only --exclude=tags/* ${DRONE_COMMIT})

#remove prefix 'remotes/origin'
PREFIX='remotes/origin/'
BRANCH=${BRANCH#"$PREFIX"}

git checkout $BRANCH
git remote set-url origin <SSH_GITEA_URL>
mvn gitflow:hotfix-finish -B -DskipTestProject=true -DhotfixBranch=$BRANCH -s /usr/share/maven/ref/settings-docker.xml
