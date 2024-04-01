#!/bin/sh
set -e
set -x

git fetch
BRANCH=$(git name-rev --name-only --exclude=tags/* ${DRONE_COMMIT})
git checkout $BRANCH
