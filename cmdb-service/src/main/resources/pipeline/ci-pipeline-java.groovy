package pipeline

pipeline {
    parameters {
        string(name: 'sshUrl', defaultValue: '', description: '代码仓库地址')
        string(name: 'branch', defaultValue: 'master', description: 'branch or tag')
        string(name: 'hostPattern', defaultValue: '', description: '主机分组')
        string(name: 'ansibleHostPath', defaultValue: '/data/www/data/ansible/ansible_hosts', description: 'ansible主机配置文件')
        string(name: 'copySrc', defaultValue: '', description: '制品原路径')
        string(name: 'copyDest', defaultValue: '', description: '制品目标路径')
        string(name: 'build', defaultValue: '', description: '构建命令行')
        string(name: 'deployCmd', defaultValue: '', description: '部署命令行')
        string(name: 'artifactPath', defaultValue: '', description: 'ansible分组')
        string(name: 'artifactFilter', defaultValue: '*.war', description: '')
        string(name: 'ossutilPath', defaultValue: '/data/www/install/ossutil', description: '')
        string(name: 'isTag', defaultValue: 'false', description: '')
    }
    agent {
        label 'java'
    }
    stages {
        stage('检出项目') {
            steps {
                git branch: env.branch, credentialsId: 'admin', url: env.sshUrl
            }
        }
        stage('SonarQube') {
            steps {
                sh "/data/www/install/gradle-3.1/bin/gradle build sonarqube -x test"
            }
        }
        stage('构建项目') {
            steps {
                sh "/data/www/install/gradle-3.1/bin/gradle clean war -DpkgName=opscloud -Denv=online -refresh-dependencies -Dorg.gradle.daemon=false"
            }
        }
        stage('上传OSS') {
            steps {
                sh "${params.ossutilPath}/ossutil cp -r ${WORKSPACE}/${params.artifactPath}${params.artifactFilter} oss://opscloud/ci/${JOB_NAME}/${BUILD_NUMBER}/ --config-file ${params.ossutilPath}/config"
            }
        }
        stage('归档制品') {
            steps {
                archive "${params.artifactPath}${params.artifactFilter}"
            }
        }
    }
}