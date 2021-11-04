# 前端代码构建（生产环境）
# npm run build:prod
# 复制编译后的前端资源文件至服务端静态资源目录下
# cd dist
# \cp -R * /Users/liangjian/Documents/workspace/baiyi/opscloud4/opscloud-manage/src/main/resources/static
# /Users/liangjian/Documents/maven/bin/mvn -Dmaven.test.skip=true clean package -P prod -U -am
MAVEN_HOME=/Users/liangjian/Documents/maven

# 服务端编译（开源环境）
${MAVEN_HOME}/bin/mvn -Dmaven.test.skip=true clean package -P prod -U -am

# 复制jar包至测试服务器
#scp -i /Users/liangjian/.ssh/id_rsa.opscloud4-test /Users/liangjian/Documents/workspace/baiyi/opscloud4/opscloud-manage/target/opscloud-manage-iac.jar  manage@172.16.210.11:/tmp/opscloud-manage-iac.jar

