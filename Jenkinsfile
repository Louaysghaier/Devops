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


    }
}