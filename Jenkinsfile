pipeline{
    agent any
    stages{
         stage('Git Checkout'){
            steps{
                git branch: 'dorraa', url: 'https://github.com/Louaysghaier/DEVOPS.git'
         }


                 stage('Maven Clean') {
                     steps {
                         sh 'mvn clean'
                     }
                 }


                 stage('Unit Testing') {
                     steps {
                         sh 'mvn test -Dspring.profiles.active=test'
                        junit 'target/surefire-reports/*.xml'
                        



                     }
                 }
                 stage('Maven Compile') {
                     steps {
                         sh 'mvn compile'
                     }
                 }
    }
}}