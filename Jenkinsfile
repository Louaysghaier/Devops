pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'http://192.168.1.16:9000/'  // Update with your SonarQube server URL
                SONARQUBE_TOKEN = credentials('sonar-api')  // Use the credentials ID you set for SonarQube token
                SONAR_PROJECT_KEY = 'tn.esprit' // Replace with your actual project key
        NEXUS_URL = "http://192.168.1.16:8081/repository/maven-releases/"
        NEXUS_USER = credentials('nexus')
        NEXUS_PASSWORD = credentials('nexus')
        GROUP_ID = "tn/esprit"
        ARTIFACT_ID = "tpFoyer-17"
        DOCKER_HUB_CREDENTIAL = credentials('docker')
        DOCKER_IMAGE_NAME = "myapp"  // Name of the Docker image
        DOCKER_TAG = "latest"  // Image tag
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
                sh 'mvn test -Dspring.profiles.active=test'
                junit 'target/surefire-reports/*.xml'  // Publish test results
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean install'
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
                           script {
                               sh """
                                   docker build --build-arg NEXUS_USER=${NEXUS_USER} --build-arg NEXUS_PASSWORD=${NEXUS_PASSWORD} -t ${DOCKER_REGISTRY}/${DOCKER_HUB_CREDENTIAL_USR}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG} .
                               """
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()  // Clean workspace after execution
        }
    }
}
