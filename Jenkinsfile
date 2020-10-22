pipeline {
agent any
 options {
	buildDiscarder logRotator(daysToKeepStr: '5', numToKeepStr: '10')
	}

  stages {
stage("Deploy to Devlopment"){
when {
    branch 'stage'
}
 steps {
       sh 'echo Ahmed'
	 sh 'zip -r api_S1.${env.BUILD_NUMBER}.zip * .[^.]*'
     sh 'scp -p 2121 -o stricthostkeychecking=no "api_S1.${env.BUILD_NUMBER}.zip ahmed@192.168.40.165:/home/ahmed/web/"'
     sh 'ssh -P 2121 -o stricthostkeychecking=no "./home/ahmed/web/jenkins_api_script.s ${env.BUILD_NUMBER}"'
         }
            }
stage("Deploy to UAT"){
when {
    branch 'uat'
}
 steps {
     sh 'echo "COMING SOON ........."'
        }
            }
stage("Deploy to test"){
when {
    branch 'testing'
}
 steps {
      sh'echo "SOON"'
       }
            }
stage("Deploy to production"){
when {
    branch 'production'
}
 steps {
     sh 'echo "SOON"'
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
