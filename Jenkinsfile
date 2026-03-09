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
                sh """
                    docker build -t ${REGISTRY}/eureka-server:${IMAGE_TAG}   ./eureka-server
                    docker build -t ${REGISTRY}/user-service:${IMAGE_TAG}    ./user-service
                    docker build -t ${REGISTRY}/product-service:${IMAGE_TAG} ./product-service
                    docker build -t ${REGISTRY}/api-gateway:${IMAGE_TAG}     ./api-gateway/api-gateway
                    docker build -t ${REGISTRY}/order-service:${IMAGE_TAG}   ./order-service/order-service
                """
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing images to Docker Hub...'
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${REGISTRY}/eureka-server:${IMAGE_TAG}
                        docker push ${REGISTRY}/user-service:${IMAGE_TAG}
                        docker push ${REGISTRY}/product-service:${IMAGE_TAG}
                        docker push ${REGISTRY}/api-gateway:${IMAGE_TAG}
                        docker push ${REGISTRY}/order-service:${IMAGE_TAG}
                        docker logout
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying to Kubernetes...'
                sh """
                    kubectl set image deployment/eureka-server   eureka-server=${REGISTRY}/eureka-server:${IMAGE_TAG}
                    kubectl set image deployment/user-service    user-service=${REGISTRY}/user-service:${IMAGE_TAG}
                    kubectl set image deployment/product-service product-service=${REGISTRY}/product-service:${IMAGE_TAG}
                    kubectl set image deployment/api-gateway     api-gateway=${REGISTRY}/api-gateway:${IMAGE_TAG}
                    kubectl set image deployment/order-service   order-service=${REGISTRY}/order-service:${IMAGE_TAG}
                """
            }
        }

        stage('Verify Deployment') {
            steps {
                echo 'Verifying rollout...'
                sh """
                    kubectl rollout status deployment/user-service    --timeout=120s
                    kubectl rollout status deployment/product-service --timeout=120s
                    kubectl rollout status deployment/order-service   --timeout=120s
                    kubectl rollout status deployment/api-gateway     --timeout=120s
                """
            }
        }
    }

    post {
        success {
            echo 'Pipeline SUCCESS - All services deployed!'
        }
        failure {
            echo 'Pipeline FAILED - Check logs above.'
        }
        always {
            sh 'docker image prune -f'
        }
    }
}
