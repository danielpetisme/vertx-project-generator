pipeline {
  agent any
  stages {
    stage('Update Generator') {
      steps {
        withCredentials(bindings: [sshUserPrivateKey(credentialsId: 'jenkins_start.vertx.io', \
                                                             keyFileVariable: 'SSH_KEY_FOR_START_VERTX_IO')]) {
          sh '''
set +x

if [ -z ${REMOTE_USER+x} ] || [ -z ${REMOTE_HOSTNAME+x} ]; then
  echo "Fatal: SSH information not set."
  exit 1
fi

echo "Deploying with ${REMOTE_USER}@${REMOTE_HOSTNAME}"

ssh -i "${SSH_KEY_FOR_START_VERTX_IO}" "${REMOTE_USER}"@"${REMOTE_HOSTNAME}" "sudo -t bash -s" < ./scripts/deploy.sh'''
        }
      }
    }
  }
  environment {
    REMOTE_USER = 'jenkins'
    REMOTE_HOSTNAME = 'start.vertx.io'
  }
}
