pipeline {
    agent any

    environment {
        PROFILE = 'dev'  // Default profile
        PORT = 7075      // Default port
    }

    stages {
        stage('Set Profile and Port Based on Branch or Tag') {
            steps {
                script {
                    if (env.BRANCH_NAME.contains('release')) {
                        PORT = 4242
                        PROFILE = "qa,no-liquibase"
                    } else if (env.BRANCH_NAME.contains('tag')) {
                        PORT = 5252
                        PROFILE = "preprod,no-liquibase"
                    } else if (env.BRANCH_NAME.contains('develop')) {
                        PORT = 7075
                        PROFILE = "dev,no-liquibase"
                    }
                    echo "Selected profile: ${PROFILE}, Assigned port: ${PORT}"
                }
            }
        }

        stage('Stop Service') {
            steps {
                script {
                    sh """
                    # Find the process ID using the given port number
                    PID=\$(sudo lsof -t -i:${PORT} || true)

                    if [ -z "\$PID" ]; then
                        echo "No process found running on port ${PORT}"
                    else
                        echo "Killing process with PID \$PID running on port ${PORT}"
                        sudo kill -9 \$PID
                    fi
                    """
                }
            }
        }

        stage('Pull Java Image and Create Container') {
            steps {
                script {
                    docker.image('ubuntu:latest').pull()

                    sh """
                        docker run -d --name lms_backend_container \
                        -v ${WORKSPACE}:/workspace \
                        -w /workspace \
                        -p ${PORT}:7075 \
                        ubuntu:latest sleep infinity
                    """

                    sh """
                        docker exec lms_backend_container bash -c "
                        apt update -y && \
                        apt install -y openjdk-8-jdk maven
                        "
                    """
                }
            }
        }


        stage('Maven Install') {
            steps {
                script {
                    sh '''
                        docker exec lms_backend_container bash -c "mvn -N io.takari:maven:wrapper"
                    '''
                }
                
            }
            
        }
        



        stage('Run Maven Commands') {
            steps {
                script {
                    sh "docker exec lms_backend_container chmod +x /workspace/mvnw"
                    sh "docker exec lms_backend_container /workspace/mvnw -f /workspace/pom.xml clean -P-webapp"
                    sh "docker exec lms_backend_container /workspace/mvnw -f /workspace/pom.xml verify -P-webapp -P${PROFILE} -DskipTests"
                }
            }
        }

        stage('Create App Directory and Host Log File') {
            steps {
                sh """
                docker exec lms_backend_container mkdir -p /app
                mkdir -p /home/logs
                touch /home/logs/lms-${PORT}.log
                """
            }
        }

        stage('Approve Deployment') {
            steps {
                timeout(time: 30, unit: 'MINUTES') {
                    input message: "Do you approve the deployment with profile '${PROFILE}' on port ${PORT}?", ok: 'Approve'
                }
            }
        }

        stage('Deploy to Container') {
            steps {
                script {
                    sh "docker cp ${WORKSPACE}/target/lms-backendapi-0.0.1.jar lms_backend_container:/app/lms-backendapi-0.0.1.jar"

                    sh """
                    sudo tmux new-session -d -s lms-${PORT} \
                    "docker exec -w /app lms_backend_container java -jar lms-backendapi-0.0.1.jar \
                    --server.port=${PORT} --spring.profiles.active=${PROFILE} > /home/logs/lms-${PORT}.log 2>&1"
                    """
                }
            }
        }

        stage('Check Service Running') {
            steps {
                script {
                    sleep(time: 30, unit: 'SECONDS')
                    sh """
                    PID=\$(sudo lsof -t -i:${PORT} || true)

                    if [ -z "\$PID" ]; then
                        echo "No process found running on port ${PORT}"
                        exit 1
                    else
                        echo "Process with PID \$PID running on port ${PORT}"
                    fi
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Build and deployment successful.'
            sh "tail -n 200 /home/logs/lms-${PORT}.log"
        }
        failure {
            echo 'Build or deployment failed.'
            sh "tail -n 200 /home/logs/lms-${PORT}.log"
        }
    }
}