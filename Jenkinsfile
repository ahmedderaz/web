pipeline {
agent any
 options {
	buildDiscarder logRotator(daysToKeepStr: '5', numToKeepStr: '10')
	}

  stages {
stage("Deploy to Devlopment"){

 steps {
       sh 'cp ../script/dev.sh .'
	 sh './dev.sh ${env.BUILD_NUMBER}'
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
