echo $JAVA_HOME
java -version
/Users/liangjian/Documents/workspace/maven-3.3.3/bin/mvn -Dmaven.test.skip=true clean package -e -T 2C -P prod -U -am

# upload oss
ossutil -c ~/.ossutilconfig cp -f -u ./opscloud-manage/target/opscloud-manage-prod.jar oss://opscloud4-web-hz/package/
