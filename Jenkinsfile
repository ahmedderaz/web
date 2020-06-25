pipeline {
	agent any
options {
	buildDiscarder logRotator(daysToKeepStr: '5', numToKeepStr: '6')
	} 

  environment {
    
	 DOCKER_IMAGE_NAME = "afaqyco/web-notifier-stage-2.8.15"
	  AFAQY_IMAGE_NAME = "docker.afaqy.sa/java/web-notifier"
  }

  stages {
     stage('Build Docker Image') {
            
            steps {
		  sh 'mvn clean -Dmaven.javadoc.skip=true verify compile package install --also-make -Denvironment=stage -Drevision=2.8.15-stage'  
		  sh 'cp docker/stage/Dockerfile .'
		    script {
                    //app = docker.build(DOCKER_IMAGE_NAME) 
	            //img = docker.build(AFAQY_IMAGE_NAME)		    
                
            }
	    }		    
        }
    stage('Push Docker Image') {
            
            steps {
		
                script { 
                   docker.withRegistry('https://registry.hub.docker.com', 'hub') {
                        def DockerImage = docker.build("${DOCKER_IMAGE_NAME}:latest")
                        DockerImage.push("latest")
                   
				   }
                }
            }
        }
	  stage('Push Afaqy Image') {
            
            steps {
                script { 
		 
	            docker.withRegistry('https://docker.afaqy.sa', 'afaqy-hub' ) {
			    def AfaqyImage = docker.build("${AFAQY_IMAGE_NAME}:latest")	 
			   AfaqyImage.push()
                    
                   
				   }
                }
	//	}
            }
	    }
    stage('Deploy To Stage') {
      
      steps {
        script 
{
   docker.withRegistry('https://registry.hub.docker.com', 'hub') {	
    sh """ssh -tt ahmed@192.168.40.165  << EOF 
    docker stop web-notifier || true && docker rm web-notifier || true
    docker info
    docker pull afaqyco/web-notifier-stage-2.8.15:latest
   docker container run \
    -d \
    --network web-notifier \
    -p 12151:12151 -p 12152:12152 -p 12153:12153 \
    --restart unless-stopped \
    --name web-notifier \
    -v /afaqylogs/avlservice/web-notifier:/workdir/logs  avljenkins/web-notifier-stage-2.8.15:latest 
    echo "web-notifier service is up and running"
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
