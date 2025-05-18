pipeline {
    agent any

    tools {
        jdk 'jdk-21'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Nadinka2024/library-project.git'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('Docker') {
            steps {
        sh 'docker build -t library-project .'
        sh 'docker-compose down || true'  
        sh 'docker-compose up -d'    
            }
        }
    }
}
