pipeline {
	agent any
options {
	buildDiscarder logRotator(daysToKeepStr: '5', numToKeepStr: '6')
	} 

  environment {
    MAJOR_VERSION = 1
	 DOCKER_IMAGE_NAME = "ahmedderaz/web-notifier"
  }

  stages {
     stage('Build Docker Image') {
            
            steps {
                script {
                    app = docker.build  -f docker/stage/Dockerfile -t (DOCKER_IMAGE_NAME) 
                }
            }
        }
    stage('Push Docker Image') {
            
            steps {
                script { 
                   docker.withRegistry('https://registry.hub.docker.com', 'hub') {
                        app.push("${env.BUILD_NUMBER}")
                        app.push("latest")
                   
				   }
                }
            }
        }
    stage('build') {
      
      steps {
        sh 'echo success'
      }
      post {
        success {
          emailext(
            subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Development Promoted to Master",
            body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Development Promoted to Master":</p>
            <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
            to: "ahmedaly@afaqy.com"
          )
        }
      }
    }
	  stage('building') {
      
      steps {
        sh 'echo success'
      }
  post {
    failure {
      emailext(
        subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Failed!",
        body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Failed!":</p>
        <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
        to: "ahmedaly@afaqy.com"
      )
    }
  }
  }
  }
}	
