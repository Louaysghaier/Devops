pipeline {
    agent any

    environment {
        NEXUS_URL = "http://192.168.0.55:8081/repository/maven-releases/"
        NEXUS_USER = credentials('nexus')
        NEXUS_PASSWORD = credentials('nexus')
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
                   [artifactId: 'tpFoyer-17', classifier: '',
                    file: 'target/tpFoyer-17.jar', type: 'jar']],
                     credentialsId: 'nexus', groupId: 'tn.esprit',
                     nexusUrl: '192.168.0.55:8081', nexusVersion: 'nexus3',
                      protocol: 'http', repository: 'maven-releases', version: "${readPomVersion.version}"

                }
            }
        }

      stage('Fetch JAR from Nexus') {
          steps {
              script {
                  echo "Fetching the latest version of ${ARTIFACT_ID} from Nexus..."
                  sh '''
                       # Install xmllint if not already available
                        sudo apt-get update && sudo apt-get install -y libxml2-utils

                      # Vérifier le contenu du fichier maven-metadata.xml
                      echo "Fetching maven-metadata.xml..."
                      curl -u ${NEXUS_USER}:${NEXUS_PASSWORD} -s ${NEXUS_URL}${GROUP_ID}/${ARTIFACT_ID}/maven-metadata.xml

                      # Extraire la version la plus récente avec xmllint
                      LATEST_VERSION=$(curl -u ${NEXUS_USER}:${NEXUS_PASSWORD} -s ${NEXUS_URL}${GROUP_ID}/${ARTIFACT_ID}/maven-metadata.xml | xmllint --xpath "string(//metadata/versioning/latest)" -)
                      echo "Latest version: ${LATEST_VERSION}"

                      # Vérifier si la version a été trouvée
                      if [ -z "$LATEST_VERSION" ]; then
                          echo "Error: Unable to fetch the latest version."
                          exit 1
                      fi

                      # Télécharger le fichier JAR
                      JAR_FILE="${ARTIFACT_ID}-${LATEST_VERSION}.jar"
                      curl -u ${NEXUS_USER}:${NEXUS_PASSWORD} -O ${NEXUS_URL}${GROUP_ID}/${ARTIFACT_ID}/${LATEST_VERSION}/${JAR_FILE}

                      # Vérifier si le fichier JAR a été téléchargé avec succès
                      if [ ! -f "$JAR_FILE" ]; then
                          echo "Error: JAR file not found."
                          exit 1
                      fi

                      # Renommer le fichier JAR pour les étapes suivantes
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
