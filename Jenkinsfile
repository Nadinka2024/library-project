pipeline {
    agent any

    tools {
        jdk 'jdk-21'
    }

    environment {
        PATH = "/usr/local/bin:/usr/bin:/bin:${env.PATH}"
        SONAR_TOKEN = credentials('SonarQubePwd')
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Nadinka2024/library-project.git'
            }
        }

        stage('SonarQube') {
            steps {
                withSonarQubeEnv('Sonar') {
                    sh './mvnw sonar:sonar -Dsonar.projectKey=library-project -Dsonar.host.url=http://localhost:9000 -Dsonar.login=${SONAR_TOKEN}'
                }
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
                sh 'docker-compose up -d'
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh 'kubectl apply -f k8s/app-deployment.yaml'
                sh 'kubectl apply -f k8s/app-service.yaml'
                sh 'kubectl apply -f k8s/postgres-deployment.yaml'
                sh 'kubectl apply -f k8s/postgres-service.yaml'
            }
        }
    }
}
