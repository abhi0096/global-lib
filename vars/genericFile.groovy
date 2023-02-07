def call(Map pipelineParams = [:]) {

pipeline {
     agent any
    //  options {
    //     copyArtifactPermission('rollback');
    // }

stages {
    stage('git clone') {
       steps {
           git branch: ${pipelineParams.branch}, url: ${pipelineParams.url}
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
