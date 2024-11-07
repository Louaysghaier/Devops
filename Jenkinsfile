pipeline{
    agent any
    stages{
         stage('Git Checkout'){
            steps{
                git branch: 'dorra', url: 'https://github.com/Louaysghaier/DEVOPS.git'
         }


                 stage('Maven Clean') {
                     steps {
                         sh 'mvn clean install'
                     }
                 }


                 stage('Unit Testing') {
                     steps {
                         sh 'mvn test'





                     }
                 }
               
                 }
    }
}}