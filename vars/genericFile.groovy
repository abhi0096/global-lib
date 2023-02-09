#!/usr/bin/env groovy

def call(Map config) {

pipeline {
     agent any
    //  options {
    //     copyArtifactPermission('rollback');
    // }

stages {
    stage('git-clone') {
       steps {
            def checkoutStep = {
                git url: "${config.repoUrl}", branch: "${config.branch}", credentialsId: "${config.credentialsId}"
                }
          // git url: '${pipelineParams.url}', branch: '${pipelineParams.branch}'
           //git branch: '${pipelineParams.branch}', credentialsId: 'Jenkins-git-cred-new', url: '${pipelineParams.url}'
            //git branch: '${config.branch}', credentialsId: 'Jenkins-git-cred-new', url: '${config.url}'
        }
    }
    stage('Build the code'){
        steps{
            sh 'mvn clean install'
        }
    }
}
}
}

// #!/usr/bin/env groovy

// def call(Map config) {
//     pipeline{
//         agent any
//         stages{
//             stage('git-clone'){
//                 steps{
//                     step{
//                     def checkoutStep = {
//                         git url: "${config.repoUrl}", branch: "${config.branch}", credentialsId: "${config.credentialsId}"
//                     }
//                 }
//             }
//         }
//             stage('build'){
//                 steps{
//                     step{
//                     def buildStep = {
//                           sh "maven clean install"
//                         }
//                     }
//                 }
//             }
//         }
//     }
//     def pipelineSteps = [checkoutStep, buildStep]
//     return pipelineSteps
// }
    

// vars
// | --- welcomeJob.groovy
// | --- jenkinsForJava.groovy

// // jenkinsForJava.groovy
// def call(String repoUrl) {
//   pipeline {
//        agent any
//        tools {
//            maven 'Maven 3.5.0'
//            jdk 'jdk8'
//        }
//        stages {
//            stage("Tools initialization") {
//                steps {
//                    sh "mvn --version"
//                    sh "java -version"
//                }
//            }
//            stage("Checkout Code") {
//                steps {
//                    git branch: 'master',
//                        url: "${repoUrl}"
//                }
//            }
//            stage("Cleaning workspace") {
//                steps {
//                    sh "mvn clean"
//                }
//            }
//            stage("Running Testcase") {
//               steps {
//                    sh "mvn test"
//                }
//            }
//            stage("Packing Application") {
//                steps {
//                    sh "mvn package -DskipTests"
//                }
//            }
//        }
//    }
// }

// // def call(Map config) {

// // pipeline {
// //      agent any
// //     //  options {
// //     //     copyArtifactPermission('rollback');
// //     // }

// // stages {
// //     stage('git clone') {
// //        steps {
// //           // git url: '${pipelineParams.url}', branch: '${pipelineParams.branch}'
// //            //git branch: '${pipelineParams.branch}', credentialsId: 'Jenkins-git-cred-new', url: '${pipelineParams.url}'
// //             git branch: '${config.branch}', credentialsId: 'Jenkins-git-cred-new', url: '${config.url}'
// //         }
// //     }
// //     stage('Build the code'){
// //         steps{
// //             sh 'maven clean install'
// //         }
// //     }
// // }
// // }
// // }
