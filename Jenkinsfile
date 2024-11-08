pipeline {
    agent any

    environment {
        NEXUS_URL = "http://192.168.1.178:8081/repository/tpfoyer/"
        NEXUS_USER = credentials('nexus-auth')
        NEXUS_PASSWORD = credentials('nexus-auth')
        GROUP_ID = "tn/esprit"
        ARTIFACT_ID = "tpFoyer-17"
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

        stage('Quality Gate Status') {
            steps {
                script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonar-api'
                }
            }
        }

        stage('Upload JAR File to Nexus') {
            steps {
                script {
                    def readPomVersion = readMavenPom file: 'pom.xml'
                    nexusArtifactUploader artifacts: [
                        [artifactId: 'tpFoyer-17', classifier: '', file: 'target/tpFoyer-17.jar', type: 'jar']
                    ],
                    credentialsId: 'nexus-auth', groupId: 'tn.esprit', nexusUrl: '192.168.1.178:8081',
                    nexusVersion: 'nexus3', protocol: 'http', repository: 'tpfoyer',
                    version: "${readPomVersion.version}"
                }
            }
        }

       stage('Fetch JAR from Nexus') {
           steps {
               script {
                   echo "Fetching the latest version of ${ARTIFACT_ID} from Nexus..."
                   sh '''
                       # Get the latest version from maven-metadata.xml using grep instead of xmllint
                       LATEST_VERSION=$(curl -u ${NEXUS_USER}:${NEXUS_PASSWORD} -s ${NEXUS_URL}${GROUP_ID}/${ARTIFACT_ID}/maven-metadata.xml | grep -oPm1 "(?<=<latest>)[^<]+")
                       echo "Latest version: ${LATEST_VERSION}"

                       # Download the latest JAR file
                       JAR_FILE="${ARTIFACT_ID}-${LATEST_VERSION}.jar"
                       curl -u ${NEXUS_USER}:${NEXUS_PASSWORD} -O ${NEXUS_URL}${GROUP_ID}/${ARTIFACT_ID}/${LATEST_VERSION}/${JAR_FILE}

                       # Rename the JAR file for later steps
                       mv ${JAR_FILE} app.jar
                   '''
               }
           }
       }


        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image..."
                    sh '''
                        docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} .
                    '''
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
