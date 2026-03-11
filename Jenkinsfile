pipeline {
    agent any
    environment {
    REGISTRY = "vish1414"
    IMAGE_TAG = "${BUILD_NUMBER}"
    JAVA_HOME = "/usr/lib/jvm/java-21-openjdk-amd64"
    PATH = "/usr/lib/jvm/java-21-openjdk-amd64/bin:${PATH}"
}
    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                checkout scm
            }
        }
        stage('Build All Services') {
            parallel {
                stage('Build Eureka Server') {
                    steps {
                        dir('eureka-server') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }
                stage('Build User Service') {
                    steps {
                        dir('user-service') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build -x test'
                        }
                    }
                }
                stage('Build Product Service') {
                    steps {
                        dir('product-service') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build -x test'
                        }
                    }
                }
                stage('Build API Gateway') {
                    steps {
                        dir('api-gateway/api-gateway') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build -x test'
                        }
                    }
                }
                stage('Build Order Service') {
                    steps {
                        dir('order-service/order-service') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build -x test'
                        }
                    }
                }
            }
        }
        stage('Build Docker Images') {
            steps {
                echo 'Building Docker images...'
                sh 'docker build -t vish1414/eureka-server:latest ./eureka-server'
                sh 'docker build -t vish1414/user-service:latest ./user-service'
                sh 'docker build -t vish1414/product-service:latest ./product-service'
                sh 'docker build -t vish1414/api-gateway:latest ./api-gateway/api-gateway'
                sh 'docker build -t vish1414/order-service:latest ./order-service/order-service'
            }
        }
        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing to Docker Hub...'
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push vish1414/eureka-server:latest'
                    sh 'docker push vish1414/user-service:latest'
                    sh 'docker push vish1414/product-service:latest'
                    sh 'docker push vish1414/api-gateway:latest'
                    sh 'docker push vish1414/order-service:latest'
                    sh 'docker logout'
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline SUCCESS!'
        }
        failure {
            echo 'Pipeline FAILED!'
        }
    }
}
