# 安装文档



**环境安装Oracle-JDK 1.8 & Maven 3**

* JDK 1.8下载地址 [http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 
* 下载并安装 jdk-8u251-linux-i586.rpm
* Maven 3下载地址 ****[http://maven.apache.org](http://maven.apache.org)
* 在/etc/profile中添加环境变量

```bash
# Export environment variables
export JAVA_HOME=/path/to/jdk
export M2_HOME=/path/to/maven
export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH
```

**环境安装Mysql 8.0**

* 推荐使用阿里云RDS
* 创建opscloud数据库
* 导入 opscloud.sql

**环境安装Redis 3**

* 推荐使用阿里云Redis

#### 修改配置文件

```bash
# git clone 源码到本地目录
# 查看所有配置文件，各模块配置文件独立，需要分别配置
$ find . -name "application-*-open.yml" | grep src
./opscloud-common/src/main/resources/application-common-open.yml
./opscloud-vmware-vcsa/src/main/resources/application-vcsa-open.yml
./opscloud-cloud/src/main/resources/application-cloud-open.yml
./opscloud-zabbix/src/main/resources/application-zabbix-open.yml
./opscloud-jumpserver/src/main/resources/application-jumpserver-open.yml
./opscloud-aws-core/src/main/resources/application-aws-open.yml
./opscloud-ldap/src/main/resources/application-ldap-open.yml
./opscloud-gitlab/src/main/resources/application-gitlab-open.yml
./opscloud-aliyun-core/src/main/resources/application-aliyun-open.yml
./opscloud-ansible/src/main/resources/application-ansible-open.yml
```

#### 源码编译

```bash
# open为当前环境配置文件，如 application-*-open.yml
$ mvn -Dmaven.test.skip=true clean package -P open -U -am -pl opscloud-manage
```

#### 项目启动

```bash
# JVM内存值请自行调优（下列命令适用于4G内存服务器启动）
# ${JASYPT_PASSWORD} 变量为opscloud加密密钥，用于数据加密/解密
# 可将变量写入/etc/profile
# export JASYPT_PASSWORD = '请使用高强度字符串'
java -Xms2048m -Xmx2048m -Xmn1024m -Xss256k -XX:MaxMetaspaceSize=128M \
-XX:MetaspaceSize=128M -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC \
-Djasypt.encryptor.password=${JASYPT_PASSWORD} -jar ./opscloud-manage-prod.jar
```

