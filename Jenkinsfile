pipeline {
    agent any

    tools {
        jdk 'jdk-17'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Nadinka2024/library-project.git'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t library-project .'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }
}