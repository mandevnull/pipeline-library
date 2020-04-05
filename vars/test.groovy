def call() {
pipeline {
    agent none
    stages {
      stage('Build & Test') {  agent { label 'aws' }
        steps {
           sh 'echo hola > hola.txt'
           sh 'ls'
            stash name: "first-stash", includes: "*.txt"
              }
      }
        stage ('check') {   agent { label 'aws2' }
            steps {
                unstash "first-stash"
             sh 'ls'
            }
        }
    }
 }
}
