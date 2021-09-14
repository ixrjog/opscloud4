/Users/liangjian/Documents/maven/bin/mvn -Dmaven.test.skip=true clean package -P iac -U -am
#scp -i /Users/liangjian/.ssh/id_rsa.opscloud4-test /Users/liangjian/Documents/workspace/baiyi/opscloud4/opscloud-manage/target/opscloud-manage-demo.jar  manage@172.16.210.11:/tmp/opscloud-manage-demo.jar
