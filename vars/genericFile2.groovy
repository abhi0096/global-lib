def call(Map config) {
pipeline {
    agent any
    stages {

        //Uncomment this stage ONLY when you need a rollback
        /*stage('Copy Artifact') {
            steps {
                copyArtifacts filter: '**', fingerprintArtifacts: true, projectName: 'indiamart-api-staging', selector: specific('build number')
            }
        }*/

        stage('Clone') {
            
            steps {
                git branch: "${config.branch}", credentialsId: "${config.credentialsId}", url: "${config.repoUrl}"
            }
        }

        //This stage is added to do conditional build.
        // stage('Check Graylog')  {
            
        //     steps{
        //         sshagent(['Prod-Jenkins']){
        //             sh "ssh -o StrictHostKeyChecking=no "${config.user}"@10.10.5.7 find  /var/lib/jenkins/workspace/indiamart-api-staging/ -name log4j2.xml -exec cp {} /home/"${config.user}" \\\\\\;"
        //             sh "ssh -o StrictHostKeyChecking=no "${config.user}"@10.10.5.7 grep -i Log.rezo.ai log4j2.xml"
        //             sh "ssh -o StrictHostKeyChecking=no "${config.user}"@10.10.5.7 sudo rm log4j2.xml"
        //             sh "ssh -o StrictHostKeyChecking=no "${config.user}"@10.10.5.7 grep -i gelf  /var/lib/jenkins/workspace/indiamart-api-staging/pom.xml"
        //         }
        //     }
        // }
        
        stage('build') {
            
            steps {
                sh "mvn clean install"
            }
        }
        
        // stage ('scan'){
        //         steps{
        //             withSonarQubeEnv('sonarqube') { 
        //                 sh 'mvn sonar:sonar'
        //             }
        //         }
        // }
        
        stage('Deploy war') {
            steps {
                script{
                sshagent(['"${config.pemName}"']) {
                    sh "scp -o StrictHostKeyChecking=no /var/lib/jenkins/workspace/"${config.projectName}"/webapp/target/*.war "${config.user}"@"${config.server}":~/"${config.warName}".war"
                }
            }
        }
    }
        
        stage('Removing Old files'){
            
            steps {
                script{
               sshagent(['"${config.pemName}"']) {
                   sh "ssh -o StrictHostKeyChecking=no "${config.user}"@"${config.server}" sudo rm -f /var/lib/tomcat9/webapps/"${config.warName}".war"
                   sh "ssh -o StrictHostKeyChecking=no "${config.user}"@"${config.server}" sudo rm -rf /var/lib/tomcat9/webapps/"${config.warName}""
                   sh "ssh -o StrictHostKeyChecking=no "${config.user}"@"${config.server}" sudo cp /home/"${config.user}"/"${config.warName}".war /var/lib/tomcat9/webapps/"
                   sh "ssh -o StrictHostKeyChecking=no "${config.user}"@"${config.server}" sudo service tomcat9 restart"
                }
            }
        }    
    }
        
//         stage ('Copying config'){
//             steps {
//                 script{
//                sshagent(['"${config.pemName}"']) {
//                   sh "ssh -o StrictHostKeyChecking=no "${config.user}"@"${config.server}" sudo cp /home/"${config.user}"/Config.properties /var/lib/tomcat9/webapps/"${config.warName}"/WEB-INF/classes/properties"
//                 }
//             }
//         }
//     }

        
        
        stage("Restart Tomcat: staging"){
            
            steps{
                script{
                   sshagent(['pem1']) {
                       sh "ssh -o StrictHostKeyChecking=no  "${config.user}"@"${config.server}" sudo service tomcat9 restart"
                        }
                    }
                }
            }
        
        }
    
    // post {
    //        	always{
	// 			emailext attachLog: true, body: '', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS', to: 'abhi.khandelwal@rezo.ai'
	// 		}
    // }
                }
            }
        }
    }
}
//===================================================
// #!/usr/bin/env groovy

// def call(Map config) {
// pipeline {
//      agent any
// stages {
//     stage('git-clone') {
//        steps {
//               git url: "${config.repoUrl}", branch: "${config.branch}", credentialsId: "${config.credentialsId}"
//             }
//         }
//     stage('Build the code'){
//         steps{
//             sh 'mvn clean install'
//             }
//         }
//     }
// }
// }
//=========================================

//// #!/usr/bin/env groovy

// def call(Map config) {
// pipeline {
//      agent any
// stages {
//     stage('git-clone') {
//        steps {
//               git url: "${config.repoUrl}", branch: "${config.branch}", credentialsId: "${config.credentialsId}"
//             }
//         }
//     stage('Build the code'){
//         steps{
//             sh 'mvn clean install'
//         }
//     }
// //     stage('Deploying War') {
// //         steps{
// //             sshagent(["${config.pemname}"]) {
// // 		    step{
// //                 sh "scp -o StrictHostKeyChecking=no /var/lib/jenkins/workspace/"${config.projectName}"/target/*.war "${config.serverUsername}"@"${config.server}":~/"${config.warName}".war"
// //             	}
// // 	    }
// //         }
// //     }
// //     stage('Removing Old files: staging'){    
// //             steps {
// //                sshagent(["${config.pemname}"]){
// //                    sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo rm -f /var/lib/tomcat9/webapps/'${config.warName}'.war"
// //                    sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo rm -rf /var/lib/tomcat9/webapps/'${config.warName}'"
// //                    sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo cp /home/"${config.serverUsername}"/"${config.warName}".war /var/lib/tomcat9/webapps/"
// //                    sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo service tomcat9 restart"
// //                 }
// //             }  
// //         }

// //         // stage ('Copying config'){
// //         //     steps {
// //         //        sshagent(['IndiaMart-Staging-API']){
// //         //           sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo cp /home/"${config.serverUsername}"/Config.properties /var/lib/tomcat8/webapps/IndiaMart/WEB-INF/classes/properties"
// //         //        }
// //         //     }
// //         // }

// //         stage("Restart Tomcat: staging") {
// //             steps{
// //                    sshagent(["${config.pemname}"]) {
// //                        sh "ssh -o StrictHostKeyChecking=no  "${config.serverUsername}"@"${config.server}" sudo service tomcat9 restart"
// //                     }
// //                 }
//             }
//         }
    
//     // post {
//     //        	always{
// 	// 			emailext attachLog: true, body: '', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS', to: '"${config.emailId}"'
// 	// 		}
//     // }
// }
// }
