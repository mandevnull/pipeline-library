def call(String REPO, String PROJECT) {
    pipeline {
        agent none
        stages {
            stage('Build') {  
                agent { 
                    label 'aws' 
                }
                steps {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                      sh 'echo Build'
                      sh 'echo aaa ${REPO}'
                      sh 'echo bbb ${PROJECT}'
                      sh 'apt update && apt install -y awscli docker.io'
                      sh 'aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin ${REPO}/${PROJECT}'
                      sh 'docker build -t test .'
                      sh 'docker tag test:latest ${REPO}/${PROJECT}:${BUILD_NUMBER}'
                      sh 'docker push ${REPO}/${PROJECT}:${BUILD_NUMBER}'
                    }
                }
            }
            stage ('check') {   
                agent { 
                    label 'aws2' 
                }
                steps {
                    sh 'ls'
                }
            }
        }
    }
}