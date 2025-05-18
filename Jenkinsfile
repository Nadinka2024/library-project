pipeline {
    agent any
    triggers {
        pollSCM('* * * * *') // Проверка изменений каждую минуту
    }
    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    extensions: [[$class: 'ForceUpdate']],
                    userRemoteConfigs: [[url: 'https://github.com/Nadinka2024/library-project.git']]
                ])
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
    }
}