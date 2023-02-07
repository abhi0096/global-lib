def call(Map pipelineParams = [:]) {

pipeline {
     agent any
    //  options {
    //     copyArtifactPermission('rollback');
    // }

stages {
    stage('git clone') {
       steps {
           git url: '${pipelineParams.url}', branch: '${pipelineParams.branch}'
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
