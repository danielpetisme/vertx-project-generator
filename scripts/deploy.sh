#!/usr/bin/env bash

VERTX_HOME=${VERTX_HOME:-/opt/vertx}
VERTX_PROJECT_GENERATOR_REPO=${VERTX_PROJECT_GENERATOR_REPO:-https://github.com/danielpetisme/vertx-project-generator}
VERTX_PROJECT_GENERATOR_BRANCH=${VERTX_PROJECT_GENERATOR_BRANCH:-master}

readonly vertx_project_generator_home="${VERTX_HOME}/vertx-project-generator"

if [ ! -d "${VERTX_HOME}" ]; then
  mkdir -p "${VERTX_HOME}"
fi

if [ ! -d "${vertx_project_generator_home}" ]; then
  git clone "${VERTX_PROJECT_GENERATOR_REPO}" "${vertx_project_generator_home}" && echo "${vertx_project_generator_home} created"
  chown -R vertx.vertx "${vertx_project_generator_home}"
fi

cd "${vertx_project_generator_home}" || exit
pwd
git pull origin "${VERTX_PROJECT_GENERATOR_BRANCH}" && echo "${VERTX_PROJECT_GENERATOR_REPO}/${VERTX_PROJECT_GENERATOR_BRANCH} pulled"

exit
