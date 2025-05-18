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
                sh 'docker rm -f library-db || true'
                sh 'docker network prune -f || true'
                sh 'docker volume prune -f || true'
                sh 'docker-compose up -d'
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh 'kubectl apply -f k8s/deployment.yaml'
                sh 'kubectl apply -f k8s/service.yaml'
            }
        }
    }
}
