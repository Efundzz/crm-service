pipeline {
  agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {    
                  sh 'mvn clean package'
                    // Add your build commands here   
            }
            post {
                  sucess{
                      echo "archiving the artifacts"
                      archiveArtifacts artifacts: '**/target/*.war'
                  }
            }             
                    
        }
        stage('Test') {
            steps {
                script {
                    echo 'Testing...'
                   }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo 'Deploying...'
                    // Add your deployment commands here
                }
            }
        }
    }
}
