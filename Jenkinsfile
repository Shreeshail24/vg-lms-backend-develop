pipeline {
    agent any

    environment {
        PROFILE = 'dev'
        PORT = 7075
        CONTAINER_NAME = 'lms_backend_container'
        LOG_DIR = "${WORKSPACE}/logs"  // 👈 Local-friendly log directory
        JAR_NAME = 'lms-backendapi-0.0.1.jar'
    }

    stages {
        stage('Set Profile and Port Based on Branch or Tag') {
            steps {
                script {
                    if (env.BRANCH_NAME?.contains('release')) {
                        PORT = 4242
                        PROFILE = 'qa,no-liquibase'
                    } else if (env.BRANCH_NAME?.contains('tag')) {
                        PORT = 5252
                        PROFILE = 'preprod,no-liquibase'
                    } else if (env.BRANCH_NAME?.contains('develop')) {
                        PORT = 7075
                        PROFILE = 'dev,no-liquibase'
                    }
                    echo "Using PROFILE=${PROFILE}, PORT=${PORT}"
                }
            }
        }

        stage('Stop Existing App (if any)') {
            steps {
                script {
                    sh """
                    PID=\$(sudo lsof -t -i:${PORT} || true)
                    if [ ! -z "\$PID" ]; then
                        echo "Stopping process on port ${PORT} (PID=\$PID)..."
                        sudo kill -9 \$PID
                    else
                        echo "No existing process on port ${PORT}."
                    fi
                    """
                }
            }
        }

        stage('Setup Container') {
            steps {
                script {
                    sh """
                    mkdir -p ${LOG_DIR}
                   STATUS=$(docker inspect -f '{{.State.Running}}' ${CONTAINER_NAME} 2>/dev/null || echo "false")
                   if [ "$STATUS" != "true" ]; then
                        echo "Creating container: ${CONTAINER_NAME}"
                        docker rm -f ${CONTAINER_NAME} || true
                        docker run -d --name ${CONTAINER_NAME} \
                            -v ${WORKSPACE}:/workspace \
                            -v ${LOG_DIR}:/logs \
                            -w /workspace \
                            -p ${PORT}:${PORT} \
                            maven:3.8.7-openjdk-8 sleep infinity
                    else
                        echo "Container ${CONTAINER_NAME} already running."
                    fi
                    """
                }
            }
        }

        stage('Maven Build') {
            steps {
                script {
                    sh '''
                        docker exec ${CONTAINER_NAME} bash -c "mvn -N io.takari:maven:wrapper"
                        docker exec ${CONTAINER_NAME} chmod +x /workspace/mvnw
                        docker exec ${CONTAINER_NAME} /workspace/mvnw -f /workspace/pom.xml clean -P-webapp
                        docker exec ${CONTAINER_NAME} /workspace/mvnw -f /workspace/pom.xml verify -P-webapp -P${PROFILE} -DskipTests
                    '''
                }
            }
        }

        stage('Approval') {
            steps {
                timeout(time: 20, unit: 'MINUTES') {
                    input message: "Approve deployment with profile '${PROFILE}' on port ${PORT}?", ok: 'Deploy'
                }
            }
        }

        stage('Deploy JAR') {
            steps {
                script {
                    sh """
                        docker cp ${WORKSPACE}/target/${JAR_NAME} ${CONTAINER_NAME}:/app/${JAR_NAME}
                        docker exec ${CONTAINER_NAME} mkdir -p /app
                        docker exec -d ${CONTAINER_NAME} bash -c '
                            nohup java -jar /app/${JAR_NAME} \
                            --server.port=${PORT} \
                            --spring.profiles.active=${PROFILE} \
                            > /logs/lms-${PORT}.log 2>&1 &
                        '
                    """
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    sleep(time: 25, unit: 'SECONDS')
                    sh """
                    docker exec ${CONTAINER_NAME} bash -c '
                    PID=\$(lsof -ti:${PORT} || ps aux | grep "${JAR_NAME}" | grep -v grep | awk "{print \\\$2}")
                    if [ -z "\$PID" ]; then
                        echo "App not running on port ${PORT}"
                        exit 1
                    else
                        echo "App running successfully (PID: \$PID)"
                    fi
                    '
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful!"
            sh "tail -n 200 ${LOG_DIR}/lms-${PORT}.log"
        }
        failure {
            echo "❌ Deployment failed!"
            sh "tail -n 200 ${LOG_DIR}/lms-${PORT}.log"
        }
    }
}
