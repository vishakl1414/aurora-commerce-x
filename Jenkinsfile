pipeline {
    agent any

    environment {
        REGISTRY = "vishakl1474"
        IMAGE_TAG = "${BUILD_NUMBER}"
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
                            sh './gradlew clean build -x test'
                        }
                    }
                }
                stage('Build User Service') {
                    steps {
                        dir('user-service') {
                            sh './gradlew clean build -x test'
                        }
                    }
                }
                stage('Build Product Service') {
                    steps {
                        dir('product-service') {
                            sh './gradlew clean build -x test'
                        }
                    }
                }
                stage('Build API Gateway') {
                    steps {
                        dir('api-gateway/api-gateway') {
                            sh './gradlew clean build -x test'
                        }
                    }
                }
                stage('Build Order Service') {
                    steps {
                        dir('order-service/order-service') {
                            sh './gradlew clean build -x test'
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                echo 'Building Docker images...'
                sh 'docker build -t vishakl1474/eureka-server:latest ./eureka-server'
                sh 'docker build -t vishakl1474/user-service:latest ./user-service'
                sh 'docker build -t vishakl1474/product-service:latest ./product-service'
                sh 'docker build -t vishakl1474/api-gateway:latest ./api-gateway/api-gateway'
                sh 'docker build -t vishakl1474/order-service:latest ./order-service/order-service'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing to Docker Hub...'
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push vishakl1474/eureka-server:latest'
                    sh 'docker push vishakl1474/user-service:latest'
                    sh 'docker push vishakl1474/product-service:latest'
                    sh 'docker push vishakl1474/api-gateway:latest'
                    sh 'docker push vishakl1474/order-service:latest'
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