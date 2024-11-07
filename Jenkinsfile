pipeline{
    agent any
    stages{
         stage('Git Checkout'){
            steps{
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

         stage('SonarQube analysis'){
              steps{
                script{
                withSonarQubeEnv(credentialsId: 'sonar-api') {
                sh 'mvn clean package sonar:sonar'

                }
                }
              }

         }
        stage('Quality Gate status'){
               steps{
               script{
               waitForQualityGate abortPipeline: false, credentialsId: 'sonar-api'

               }
        }}
    stage('upload war file to nexus'){
      steps{
      script{
      nexusArtifactUploader artifacts: [[artifactId: 'tpFoyer-17', classifier: '', file: 'target/tpFoyer-17.jar', type: 'jar']], credentialsId: 'nexus-auth', groupId: 'tn.esprit', nexusUrl: '192.168.1.178:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'tpfoyer', version: '1.0.0'
      }
      }
    }
    }
}