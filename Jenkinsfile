pipeline {
	agent any
    options {
	    buildDiscarder logRotator(daysToKeepStr: '5', numToKeepStr: '6')
	}

    environment {
	 DOCKER_IMAGE_NAME = "afaqyco/avl-web-notifier:stage-2.14.29"
	  AFAQY_IMAGE_NAME = "docker.afaqy.sa/java/avl-web-notifier:stage-2.14.29"
	  Afaqy_image_qc =  "docker.afaqy.sa/java/avl-web-notifier"
	  Test_image = "docker.afaqy.sa/java/avl-web-notifier:local-2.14.29"
    }

    stages {
	  stage('Build Maven Build') {
        steps {
			sh 'echo "Maven Building proessing " '
		    sh 'mvn clean -Dmaven.javadoc.skip=true verify compile package install --also-make -Denvironment=stage -Drevision=2.8.15-stage'
            sh 'echo "Maven Build Success"'
	    }
      }


 /*  stage('Push Docker Image') {
           steps {
              script {
                   docker.withRegistry('https://registry.hub.docker.com', 'docker-hub') {
                       def hubImage = docker.build("${DOCKER_IMAGE_NAME}:latest")
                       hubImage.push()
				   }
               }
           }
        } */
	  stage('Push Afaqy Image') {
        steps {
            script {
                sh 'cp docker/stage/Dockerfile .'
	            docker.withRegistry('https://docker.afaqy.sa', 'afaqy-hub' ) {
			    def AfaqyImage = docker.build("${AFAQY_IMAGE_NAME}")
      			    AfaqyImage.push()
			    def qc = docker.build("${Afaqy_image_qc}:stage-latest")
	                qc.push()
				}
            }
        }
	 }
	 stage('Push Local Image') {
        steps {
		        sh 'rm -rf  Dockerfile'
		        sh 'cp docker/local/Dockerfile .'
                script {
	            docker.withRegistry('https://docker.afaqy.sa', 'afaqy-hub' ) {
			    def Testing = docker.build("${Test_image}")
			    Testing.push()
				}
             }
	       }
	  }

    stage('Deploy To Stage') {
      steps {
        script{
           docker.withRegistry('https://docker.afaqy.sa', 'afaqy-hub') {
            sh """ssh -p 11207 -o stricthostkeychecking=no jenkins@10.10.23.114  << EOF
            yes |docker system prune
            docker stop avl-web-notifier  && docker rename avl-web-notifier avl-web-notifier.old 
            docker pull docker.afaqy.sa/java/avl-web-notifier:stage-2.14.29
            docker container run \
                -d \
                --network avl -p 12151:12151 -p 12152:12152 -p 12153:12153 --restart unless-stopped \
                --name avl-web-notifier \
                -v /afaqylogs/avlservice/web-notifier:/workdir/logs -v /var/run/docker.sock:/var/run/docker.sock docker.afaqy.sa/java/avl-web-notifier:stage-2.14.29
            if [ "$( docker container inspect -f '{{.State.Status}}'  avl-web-notifier )" == "running" ]
                    then
                    docker rm avl-web-notifier.old && echo " avl-web-notifier is running"
            else
                    ( docker rename  avl-web-notifier.old  avl-web-notifier && docker start  avl-web-notifier )
            fi
            exit
            EOF"""
        }
      }
    }
      post {
        success {
          emailext(
            subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Deployment to Stage",
            body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]'  Deployment to Stage":</p>
            <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
            to: "ahmed.aly@afaqy.com"
          )
        }
      }
    }
	  stage('system clean') {

      steps {
        sh 'echo "Cleaning The System ..........."'  
        sh 'yes|docker system prune'
      }
  post {
    failure {
      emailext(
        subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Failed!",
        body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Failed!":</p>
        <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
        to: "ahmed.aly@afaqy.com"
      )
    }
  }
  }
  }
}
