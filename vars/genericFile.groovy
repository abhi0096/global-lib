def call(Map pipelineParams = [:]) {

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
           git branch: '${pipelineParams.branch}', credentialsId: 'Jenkins-git-cred-new', url: 'https://github.com/abhi0096/onlinebookstore.git'
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
