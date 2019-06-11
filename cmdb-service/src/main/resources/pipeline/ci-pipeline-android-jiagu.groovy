package pipeline

pipeline {
    // 构建参数
    parameters {
        // OSS
        string(name: 'ossPath', defaultValue: '', description: '制品的OSS相对路径')
        string(name: 'ossutilPath', defaultValue: '/data/www/install/ossutil', description: 'ossutil路径')
        string(name: 'signArtifactFilter', defaultValue: '*_sign.apk', description: '加固签名制品文件名过滤')
        string(name: 'artifactFilter', defaultValue: '*.apk', description: '制品文件名过滤')
        // 加固
        string(name: 'channelType', defaultValue: '0', description: '渠道类型 0:全渠道 1:选择渠道')
        string(name: 'channelGroup', defaultValue: '', description: '选择渠道启用后的文件过滤条件')
        string(name: 'keystore', defaultValue: '/data/www/install/android_keystore/GlobalScanner.keystore', description: 'AndroidKeystore')
        string(name: 'keyAlias', defaultValue: '', description: '签名文件的keyAlias')
        string(name: 'jiagu', defaultValue: '/data/www/install/360jiagubao/jiagu/jiagu.jar', description: '加固Jar包')
        string(name: 'mulpkgFilepath', defaultValue: '/data/www/install/360jiagubao/jiagu/channels.txt', description: '多渠道配置信息路径')
    }
    agent {
        label 'android'
    }
    stages {
        stage('下载制品') {
            steps {
                cleanWs()
                sh "${params.ossutilPath}/ossutil cp -r --update -c /data/www/install/ossutil/config oss://opscloud/${params.ossPath} ${WORKSPACE}"
                // 移动到工作目录根路径
                sh "mv `find ${WORKSPACE} -name ${params.artifactFilter}` ${WORKSPACE}"
            }
        }
        stage('加固生成渠道包') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'user_360jiagu', passwordVariable: 'USERPASS', usernameVariable: 'USERNAME')]) {
                    sh "java -jar ${params.jiagu} -login $USERNAME $USERPASS"
                }
                sh "mkdir -p ${WORKSPACE}/mulpkg"
                sh "java -jar ${params.jiagu} -jiagu `ls ${params.artifactFilter}` ${WORKSPACE}/mulpkg -automulpkg -importmulpkg ${params.mulpkgFilepath} -config -crashlog -x86 -analyse -nocert"
            }
        }
        stage('渠道包签名') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'ks_globalscanner', passwordVariable: 'KEYPASS', usernameVariable: 'USERNAME')]) {
                    withEnv(["OC_KEYSTORE_FILE=${params.keystore}", "OC_KEYALIAS=${params.keyAlias}"]) {
                        sh '''
                              #!/bin/bash
                              mkdir -p ./sign
                              for apkName in `ls mulpkg` ; do 
                                  apkSignName=`basename $apkName .apk`_sign.apk
                                  jarsigner -verbose -keystore ${OC_KEYSTORE_FILE} -storepass $KEYPASS -keypass $KEYPASS -signedjar ./sign/${apkSignName} ./mulpkg/${apkName} ${OC_KEYALIAS}
                              done
                              '''
                    }
                }
            }
        }
        stage('上传OSS') {
            steps {
                withEnv(["OC_CHANNEL_TYPE=${params.channelType}", "OC_CHANNEL_GROUP=${params.channelGroup}"]) {
                    sh '''
                      #!/bin/bash
                      if [ ${OC_CHANNEL_TYPE} == '0' ] ; then
                         zip -rq ${WORKSPACE}/android_mulpkg_sign.zip ./sign/*_sign.apk
                      else
                         zip -rq ${WORKSPACE}/android_mulpkg_sign.zip ${OC_CHANNEL_GROUP}
                      fi
                   '''
                }
                sh "${params.ossutilPath}/ossutil cp -r ${WORKSPACE}/*.zip oss://opscloud/android/${JOB_NAME}/${BUILD_NUMBER}/ --config-file ${params.ossutilPath}/config"
            }
        }
        stage('归档制品') {
            steps {
                archive "*.zip"
            }
        }
    }

}