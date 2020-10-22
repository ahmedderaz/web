pipeline {
agent any
 options {
	buildDiscarder logRotator(daysToKeepStr: '5', numToKeepStr: '10')
	}

  stages {
stage("Deploy to Devlopment"){

 steps {
       sh 'echo Ahmed'
	 sh 'zip -r api_S1.${env.BUILD_NUMBER}.zip * .[^.]*'
     sh 'scp -p 2121 -o stricthostkeychecking=no "api_S1.${env.BUILD_NUMBER}.zip ahmed@192.168.40.165:/home/ahmed/web/"'
     sh 'ssh -P 2121 -o stricthostkeychecking=no "./home/ahmed/web/jenkins_api_script.s ${env.BUILD_NUMBER}"'
         }
            }

  }
post{
          always{
		
           googlechatnotification(
             url: 'id:AVL_API_PIPELINE',
             message: "#################${env.JOB_NAME} on branch ${env.BRANCH_NAME} is ##################### \
			     *******************${currentBuild.currentResult} *******************  \
			   ** to view all details use below link **   ${env.BUILD_URL} " ,
             sameThreadNotification: 'true',
             suppressInfoLoggers: 'true'
           )
  }

  }

}
