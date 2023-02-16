#!/usr/bin/env groovy

def call(Map config) {
pipeline {
     agent any
stages {
    stage('git-clone') {
       steps {
              git url: "${config.repoUrl}", branch: "${config.branch}", credentialsId: "${config.credentialsId}"
            }
        }
    stage('Build the code'){
        steps{
            sh 'mvn clean install'
        }
    }
    stage('Deploying War') {
        steps{
            sshagent(["${config.pemname}"]) {
		    steps{
                sh "scp -o StrictHostKeyChecking=no /var/lib/jenkins/workspace/"${config.projectName}"/target/*.war "${config.serverUsername}"@"${config.server}":~/"${config.warName}".war"
            	}
	    }
        }
    }
    stage('Removing Old files: staging'){    
            steps {
               sshagent(["${config.pemname}"]){
                   sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo rm -f /var/lib/tomcat9/webapps/'${config.warName}'.war"
                   sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo rm -rf /var/lib/tomcat9/webapps/'${config.warName}'"
                   sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo cp /home/"${config.serverUsername}"/"${config.warName}".war /var/lib/tomcat9/webapps/"
                   sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo service tomcat9 restart"
                }
            }  
        }

        // stage ('Copying config'){
        //     steps {
        //        sshagent(['IndiaMart-Staging-API']){
        //           sh "ssh -o StrictHostKeyChecking=no "${config.serverUsername}"@"${config.server}" sudo cp /home/"${config.serverUsername}"/Config.properties /var/lib/tomcat8/webapps/IndiaMart/WEB-INF/classes/properties"
        //        }
        //     }
        // }

        stage("Restart Tomcat: staging") {
            steps{
                   sshagent(["${config.pemname}"]) {
                       sh "ssh -o StrictHostKeyChecking=no  "${config.serverUsername}"@"${config.server}" sudo service tomcat9 restart"
                    }
                }
            }
        }
    
    // post {
    //        	always{
	// 			emailext attachLog: true, body: '', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS', to: '"${config.emailId}"'
	// 		}
    // }
}
}
