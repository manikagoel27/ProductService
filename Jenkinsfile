pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/manikagoel27/ProductService.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                bat """
                cp target/ProductService.jar /path/to/deployment/
                nohup java -jar /path/to/deployment/ProductService.jar &
                """
            }
        }
    }
}
