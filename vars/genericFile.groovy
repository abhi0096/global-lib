def call(Map pipelineParams = [:]) {

pipeline {
     agent any
    //  options {
    //     copyArtifactPermission('rollback');
    // }

stages {
    stage('git clone') {
       steps {
           git Url: '${pipelineParams.gitUrl}', branch: '${pipelineParams.branch}'
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
