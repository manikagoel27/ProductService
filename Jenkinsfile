pipeline {
    agent any

    environment {
        DEPLOY_DIR = "${WORKSPACE}\\deployment"  // Deployment directory relative to Jenkins workspace
        JAR_NAME = 'ProductService-1.0-SNAPSHOT.jar'
    }

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
                echo 'Deploying the application...'

                // Ensure the deployment directory exists
                bat """
                if not exist "%DEPLOY_DIR%" (
                    echo Creating deployment directory: %DEPLOY_DIR%
                    mkdir "%DEPLOY_DIR%"
                )
                """

                // Copy the JAR file to the deployment directory
                bat """
                copy "target\\%JAR_NAME%" "%DEPLOY_DIR%"
                if %errorlevel% neq 0 exit /b 1
                """

                // Start the Spring Boot application
                bat """
                cd /d "%DEPLOY_DIR%"
                start java -jar %JAR_NAME%
                """
            }
        }
    }
}


