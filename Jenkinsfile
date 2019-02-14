#!/usr/bin/env groovy

node {
    stage('Build') {
        checkout scm
    }

    stage('Unit/Integration Test') {
        sh 'make test'
    }

    if (isRunningMaster()) {
        stage('Package') {
            sh 'make docker'
        }

//        stage('Publish staging image') {
//            withStagingCredentials {
//              awsNonProduction.publishImage(utils.getVersion())
//            }
//        }
//
//        stage('Publish production image') {
//            withProductionCredentials {
//                awsProduction.publishImage(utils.getVersion())
//            }
//        }
    }
}

boolean isRunningMaster() {
    return env.BRANCH_NAME == 'master'
}