pipeline{
    agent any
    stages{
         stage('Git Checkout'){
            steps{
                git branch: 'dorra', url: 'https://github.com/Louaysghaier/DEVOPS.git'
         }
}
          stage('Unit Testing') {
              steps {
                     sh 'mvn test'





                     }}
                 stage('Maven Build') {
                     steps {
                         sh 'mvn clean install'
                     }
                 }



                
    }
}