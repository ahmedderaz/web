pipeline {
	agent any
options {
	buildDiscarder logRotator(daysToKeepStr: '5', numToKeepStr: '6')
	} 

  environment {
    
	 DOCKER_IMAGE_NAME = "avljenkins/web-notifier-stage-2.8.15"
	  AFAQY_IMAGE_NAME = "hub.eg.afaqy.co/java/web-notifier"
  }

  stages {
     stage('Build Docker Image') {
            
            steps {
		  sh 'mvn clean -Dmaven.javadoc.skip=true verify compile package install '  
		  sh 'cp docker/stage/Dockerfile .'
		    script {
                    app = docker.build(DOCKER_IMAGE_NAME) 
	            img = docker.build(AFAQY_IMAGE_NAME)		    
                
            }
	    }		    
        }
    stage('Push Docker Image') {
            
            steps {
		
                script { 
                   docker.withRegistry('https://registry.hub.docker.com', 'hub') {
                        app.push()
                        app.push("latest")
                   
				   }
                }
            }
        }
	  stage('Push Afaqy Image') {
            
            steps {
                script { 
		 
	            docker.withRegistry('https://hub.eg.afaqy.co', 'afaqy-hub' ) {
			    def customImage = docker.build("${AFAQY_IMAGE_NAME}:latest")	 
			   customImage.push()
                    
                   
				   }
                }
	//	}
            }
	    }
    stage('Deploy To Stage') {
      
      steps {
        script 
{
    sh """ssh -tt ahmed@192.168.40.165  << EOF 
    docker stop web-notifier || true && docker rm web-notifier || true
    docker pull avljenkins/web-notifier-stage-2.8.15:latest
   docker container run \
    -d \
    --network web-notifier \
    -p 12151:12151 -p 12152:12152 -p 12153:12153 \
    --restart unless-stopped \
    --name web-notifier \
    -v /afaqylogs/avlservice/web-notifier:/workdir/logs \
    afaqyco/avl-web-notifier:stage-2.8.15:latest &&
    exit
    EOF"""
}
	      
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
