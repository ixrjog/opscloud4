package pipeline

pipeline {
    // 注入环境变量
    environment {
        ANDROID_HOME = '/data/www/install/android/android-sdk'
        ANDROID_NDK_HOME = '/data/www/install/android/android-sdk'
        JENKINS_BUILD = 'true'
    }

    // 构建参数
    parameters {
        string(name: 'sshUrl', defaultValue: '', description: '代码仓库地址')
        string(name: 'branch', defaultValue: 'master', description: 'branch or tag')
        string(name: 'gradleHome', defaultValue: '', description: 'gradleHome路径:/data/www/install/gradle-4.4')

        string(name: 'ENVIRONMENT_BUILD', defaultValue: 'apkoutput/', description: '构建环境（bt,debug,release）')
        string(name: 'PRODUCT_FLAVOR_BUILD', defaultValue: '', description: '构建渠道')
        string(name: 'JENKINS_BUILD', defaultValue: 'true', description: 'Jenkins构建标志字段')
        string(name: 'JENKINS_SERVER_HOST', defaultValue: 'default', description: '自定义请求环境，default为默认域名，注：只适合在beta下使用')

        // OSS
        string(name: 'artifactPath', defaultValue: 'apkoutput/', description: '制品路径')
        string(name: 'artifactFilter', defaultValue: '*.apk', description: '制品文件名过滤')
        string(name: 'ossutilPath', defaultValue: '/data/www/install/ossutil', description: 'ossutil路径')

    }

    agent {
        label 'android'
    }
    stages {
        stage('检出项目') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: env.branch]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CleanBeforeCheckout']], gitTool: 'Default', submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'admin', url: env.sshUrl]]])
            }
        }
        stage('构建项目') {
            steps {
                // 删除旧的apk
                sh "rm -rf ${WORKSPACE}/${params.artifactPath}${params.artifactFilter}"
                sh "${params.gradleHome}/bin/gradle -PPRODUCT_FLAVOR_BUILD=${params.PRODUCT_FLAVOR_BUILD} -PJENKINS_BUILD=${params.JENKINS_BUILD} -PENVIRONMENT_BUILD=${params.ENVIRONMENT_BUILD} --stacktrace -info clean assemble${params.PRODUCT_FLAVOR_BUILD}${params.ENVIRONMENT_BUILD}"
            }
        }
        stage('上传OSS') {
            steps {
                sh "${params.ossutilPath}/ossutil cp -r ${WORKSPACE}/${params.artifactPath} oss://opscloud/android/${JOB_NAME}/${BUILD_NUMBER}/ --config-file ${params.ossutilPath}/config"
            }
        }
        stage('归档制品') {
            steps {
                archive "${params.artifactPath}${params.artifactFilter}"
            }
        }
    }
}
