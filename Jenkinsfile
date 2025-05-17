pipeline {
    agent any

    environment {
        // Настройки Java
        JAVA_HOME = "${tool 'jdk17'}"
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"

        // Настройки Maven
        MAVEN_HOME = "${tool 'maven'}"
        PATH = "${env.MAVEN_HOME}/bin:${env.PATH}"

        // Настройки проекта
        APP_NAME = "library-project"
        VERSION = "0.0.1-SNAPSHOT"
    }

    tools {
        // Установленные в Jenkins инструменты
        jdk 'jdk17'
        maven 'maven'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Nadinka2024/library-project.git',
                    credentialsId: 'github-credentials'
            }
        }

        stage('Build') {
            steps {
                sh "mvn clean package -DskipTests"
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Test') {
            steps {
                sh "mvn test"
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            when {
                expression { env.BRANCH_NAME == 'main' }
            }
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh "mvn sonar:sonar"
                }
            }
        }

        stage('Deploy to Dev') {
            when {
                branch 'main'
            }
            steps {
                sshagent(['ssh-credentials']) {
                    sh """
                        scp -o StrictHostKeyChecking=no target/${APP_NAME}-${VERSION}.jar user@dev-server:/opt/app/
                        ssh user@dev-server "sudo systemctl restart library-app"
                    """
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            slackSend channel: '#build-notifications',
                     color: 'good',
                     message: "Build SUCCESSFUL: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }
        failure {
            slackSend channel: '#build-notifications',
                     color: 'danger',
                     message: "Build FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }
    }
}