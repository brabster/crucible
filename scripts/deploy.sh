#!/bin/bash

set -e

if [ "$TRAVIS_EVENT_TYPE" = "cron" ];
then
    echo "Skipping deploy for scheduled build"
    exit 0
fi

STABLE_VERSION=${TRAVIS_TAG}
SNAPSHOT_VERSION="${TRAVIS_BRANCH//[\/]/-}-SNAPSHOT"
export PROJECT_VERSION=${STABLE_VERSION:-"${SNAPSHOT_VERSION}"}
echo "Project version is \"$PROJECT_VERSION\""

lein deploy
