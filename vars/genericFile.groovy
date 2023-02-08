def call(Map config) {

pipeline {
     agent any
    //  options {
    //     copyArtifactPermission('rollback');
    // }

stages {
    stage('git clone') {
       steps {
          // git url: '${pipelineParams.url}', branch: '${pipelineParams.branch}'
           //git branch: '${pipelineParams.branch}', credentialsId: 'Jenkins-git-cred-new', url: '${pipelineParams.url}'
            git branch: '${config.branch}', credentialsId: 'Jenkins-git-cred-new', url: '${config.url}'
        }
    }
    stage('Build the code'){
        steps{
            sh 'maven clean install'
        }
    }
}
}
}
