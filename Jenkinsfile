pipeline {
    agent any

    environment {
        NEXUS_URL = "http://192.168.1.16:8081/repository/maven-releases/"
        NEXUS_USER = credentials('nexus')
        NEXUS_PASSWORD = credentials('nexus')
        GROUP_ID = "tn/esprit"
        ARTIFACT_ID = "tpFoyer-17"
         DOCKER_HUB_CREDENTIAL = credentials('docker')
        DOCKER_IMAGE_NAME = "myapp"  // Nom de l'image Docker
        DOCKER_TAG = "latest"  // Tag de l'image
    }

    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'dorra', url: 'https://github.com/Louaysghaier/DEVOPS.git'
            }
        }

        stage('Unit Tests') {
            steps {
                // Exécuter les tests unitaires, y compris ceux avec Mockito
                sh 'mvn test -Dspring.profiles.active=test'
                junit 'target/surefire-reports/*.xml'  // Publier les résultats des tests
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv(credentialsId: 'sonar-api') {
                        sh 'mvn clean package sonar:sonar'
                    }
                }
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
      post {
          always {
              cleanWs()  // Nettoyer l'espace de travail après l'exécution
          }
      }


}
