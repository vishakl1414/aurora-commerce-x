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
                sh 'docker build -t vishakl1474/eureka-server:${BUILD_NUMBER} ./eureka-server'
                sh 'docker build -t vishakl1474/user-service:${BUILD_NUMBER} ./user-service'
                sh 'docker build -t vishakl1474/product-service:${BUILD_NUMBER} ./product-service'
                sh 'docker build -t vishakl1474/api-gateway:${BUILD_NUMBER} ./api-gateway/api-gateway'
                sh 'docker build -t vishakl1474/order-service:${BUILD_NUMBER} ./order-service/order-service'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing images to Docker Hub...'
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push vishakl1474/eureka-server:${BUILD_NUMBER}'
                    sh 'docker push vishakl1474/user-service:${BUILD_NUMBER}'
                    sh 'docker push vishakl1474/product-service:${BUILD_NUMBER}'
                    sh 'docker push vishakl1474/api-gateway:${BUILD_NUMBER}'
                    sh 'docker push vishakl1474/order-service:${BUILD_NUMBER}'
                    sh 'docker logout'
                }
            }
        }
    }

    post {