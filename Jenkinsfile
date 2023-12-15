
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from version control
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Run Maven build
                script {
                    def mavenHome = tool 'Maven' // Assuming you have configured a Maven tool in Jenkins
                    def mavenCmd = "${mavenHome}/bin/mvn"

                    sh "${mavenCmd} clean install"
                }
            }
        }

        stage('Deploy') {
            steps {
                // You can add deployment steps here if needed
            }
        }
    }

    post {
        success {
            // This block will be executed if the pipeline is successful
            echo 'Build successful! Deploying...'
        }

        failure {
            // This block will be executed if the pipeline fails
            echo 'Build failed! Notify the team...'
        }
    }
}
