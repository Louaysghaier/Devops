pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'http://192.168.1.16:9000/'  // Update with your SonarQube server URL
        SONARQUBE_TOKEN = credentials('sonar-api')  // Use the credentials ID for SonarQube token
        SONAR_PROJECT_KEY = 'tn.esprit' // Replace with your actual project key
        NEXUS_URL = "http://192.168.1.16:8081/repository/maven-releases/"
        NEXUS_USER = credentials('nexus')
        NEXUS_PASSWORD = credentials('nexus')
        GROUP_ID = "tn/esprit"
        ARTIFACT_ID = "tpFoyer-17"
        DOCKER_HUB_CREDENTIAL = credentials('docker')
        DOCKER_IMAGE_NAME = "myapp"  // Name of the Docker image
        DOCKER_TAG = "latest"  // Image tag
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml' // Docker Compose file
    }

    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'dorra', url: 'https://github.com/Louaysghaier/DEVOPS.git'
            }
        }

        stage('Unit Tests') {
            steps {
                // Run unit tests, including those with Mockito
                 sh 'mvn clean'
                sh 'mvn test -Dspring.profiles.active=test'
                junit 'target/surefire-reports/*.xml'  // Publish test results
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('SonarQube Analysis with JaCoCo') {
            steps {
                // Run Sonar analysis using the token
                sh "mvn sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.host.url=${SONARQUBE_SERVER} -Dsonar.login=${SONARQUBE_TOKEN} -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                        mvn deploy -DskipTests \
                        -DaltDeploymentRepository=deploymentRepo::default::${NEXUS_URL} \
                        -Dusername=${NEXUS_USER} -Dpassword=${NEXUS_PASSWORD}
                    """
                }
            }
        }

        stage('Build Docker Image From Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                        docker build --build-arg NEXUS_USER=${NEXUS_USER} --build-arg NEXUS_PASSWORD=${NEXUS_PASSWORD} -t ${DOCKER_REGISTRY}/${DOCKER_HUB_CREDENTIAL_USR}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG} .
                    """
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker', usernameVariable: 'DOCKER_HUB_CREDENTIAL_USR', passwordVariable: 'DOCKER_HUB_CREDENTIAL_PSW')]) {
                    sh "echo ${DOCKER_HUB_CREDENTIAL_PSW} | docker login -u ${DOCKER_HUB_CREDENTIAL_USR} --password-stdin"
                    sh "docker push ${DOCKER_REGISTRY}/${DOCKER_HUB_CREDENTIAL_USR}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
                }
            }
        }

        stage('Check if the Containers are Running') {
            steps {
                script {
                    // List running containers
                    def runningContainers = sh(script: 'docker compose -f $DOCKER_COMPOSE_FILE ps', returnStdout: true).trim()

                    if (runningContainers) {
                        echo "The following containers are running:"
                        echo "$runningContainers"
                        sh 'docker compose -f $DOCKER_COMPOSE_FILE down'
                    } else {
                        echo "No containers are currently running."
                    }
                }
            }
        }

     stage('Deploy DB,spring-back , grafana , prometheus with DockerCompose') {
               steps {
                   script {
                        // sh "docker-compose -f docker-compose.yml pull app"
                         sh "docker compose -f docker-compose.yml up -d db"
                          sleep 40
                         sh "docker compose -f docker-compose.yml up -d"
                   }
               }

           }



    }
    post {
        success {
            script {
                emailext(
                    subject: "Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                    ✅ The build '${env.JOB_NAME}' #${env.BUILD_NUMBER} was successful!

                    🗂️ Git Branch: ${env.GIT_BRANCH}
                    🏷️ Maven Project: Compilation and testing passed.
                    📊 SonarQube Analysis: Completed successfully.
                    📦 Nexus Deployment: Artifacts deployed to ${NEXUS_URL}.
                    🐳 Docker Image: ${DOCKER_REGISTRY}/${DOCKER_HUB_CREDENTIAL_USR}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG} pushed to Docker Hub.
                    🚀 Deployment: Docker containers deployed successfully.

                    🔗 Jenkins Build URL: ${env.BUILD_URL}
                    """,
                    to: 'dorrajaidanee@gmail.com'
                )
            }
        }
        failure {
            script {
                emailext(
                    subject: "Build FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                    ❌ The build '${env.JOB_NAME}' #${env.BUILD_NUMBER} has failed.

                    Check the following stages for potential issues:
                    - 🏗️ Maven build and tests
                    - 📊 SonarQube analysis
                    - 📦 Nexus deployment
                    - 🐳 Docker image build/push
                    - 🚀 Docker Compose deployment

                    Please review the Jenkins console output for more details:
                    🔗 Jenkins Build URL: ${env.BUILD_URL}
                    """,
                    to: 'dorrajaidanee@gmail.com'
                )
            }
        }
        unstable {
            script {
                emailext(
                    subject: "Build UNSTABLE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                    ⚠️ The build '${env.JOB_NAME}' #${env.BUILD_NUMBER} is unstable.

                    Some tests may have failed or quality gates may not have been met:
                    - 📊 SonarQube analysis might have issues.
                    - 📦 Nexus deployment may have encountered warnings.

                    Check the details here:
                    🔗 Jenkins Build URL: ${env.BUILD_URL}
                    """,
                    to: 'dorrajaidanee@gmail.com'
                )
            }
        }

    }
}
          
