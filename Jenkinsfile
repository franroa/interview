#!/usr/bin/env groovy

STAGING_AWS_ACCOUNT_NUMBER = "...";
SERVICE = 'interview'


node {
    stage('Build') {
        checkout scm
    }

    stage('Unit/Integration Test') {
        sh 'make test'
    }

    if (isRunningMaster()) {
        def version = getVersion()

        stage('Package') {
            sh 'make docker'
        }

        stage('Publish image') {
            def repo = "franroa" // TODO to be replaced by the AWS' ecr

            sh """
                docker tag "${SERVICE}:${version}" "${repo}/${SERVICE}:${version}"
                docker push "${repo}/${SERVICE}:${version}"
            """
//            publishImage(STAGING_AWS_ACCOUNT_NUMBER, SERVICE, version)
        }
    }
}

boolean isRunningMaster() {
    return env.BRANCH_NAME == 'master'
}

def publishImage(account, image, version) {
    // Both USA and EU Docker images are published to the EU image repository. Since both images are identical (the
    // difference is the ENVIRONMENT environment variable used when starting the service/image) it is perfectly okay
    // to insert all images in the same repo.
    def repo = "${account}.dkr.ecr.${REGION_EU}.amazonaws.com/franroa"
    sh """
        docker tag "${image}:${version}" "${repo}/${image}:${version}"
        docker push "${repo}/${image}:${version}"
    """
}

def getVersion() {
    return sh(
            script: 'git describe --always | sed "s/^v//"',
            returnStdout: true,
    ).trim()
}
