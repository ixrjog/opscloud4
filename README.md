Welcome to the opscloud wiki!

# OpsCloud简介
<img src="https://img.shields.io/badge/version-3.0.0-brightgreen.svg"></img>
<img src="https://img.shields.io/badge/java-8-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/springboot-2.2.2.RELEASE-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/mysql-8-brightgreen.svg"></img> 

<br>
Opscloud面向云的DevOps平台，为企业内的研发提供运维服务


开源协议：<a style="color:#2b669a" href="http://www.gnu.org/licenses/old-licenses/gpl-2.0.html" target="_blank">GNU General Public License v2</a>


### 最佳用户体验
  + 显示器分辨率大于或等于1080P
  + 浏览器Chrome/Safari

<a style="color:#2b669a" href="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/gif/oc-webxterm-1.mov" target="_blank">XTerm演示视频</a>
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/gif/oc-webxterm-2.gif"></img>
### web终端
  + 支持批量登录
  + 支持命令同步
  + 支持会话复制（单机多终端）
  + 支持会话心跳，即使过SLB也可以保持会话
  + 审计日志限流(过滤top,tail等命令输出日志)
  + 支持全平台web终端(playbook,服务器管理都可以打开终端）
  + 支持高/低权限登录

<a style="color:#2b669a" href="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/gif/oc-playbook-1.mov" target="_blank">Playbook演示视频</a> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/gif/oc-playbook-1.gif"></img>
### ansible-playbook
  + 多线程执行
  + 批量执行结果精确展示（成功数量/失败数量）
  + 支持自定义变量
  + 支持tags

  
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/mov/createInstance.gif"></img> 
### aliyun-ecs
  + 非阿里云ecs模版实现开机（全部oc实现）
  + 开通服务器后自动录入jumpserver/zabbix

<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-01.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-02.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-03.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-04.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-05.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-06.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-07.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-08.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-09.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-10.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-11.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-12.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-13.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-14.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-15.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-16.png"></img> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/version/3.0.0-demo/opscloud-17.png"></img> 

+ API接口文档
  + knife4j接口文档（ https://doc.xiaominfo.com/knife4j ）
  + example.org/oc3/doc.html
  + example.org/oc3/swagger-ui.html

+ 平台特性
  + 基于角色的访问控制RBAC(Role-Based Access Control),LDAP完整支持
  + 配置文件加密（密码, AK），数据库敏感字段加密
  + 代码即文档，全接口API
  + 前后端分离，支持跨域部署
  + Java Springboot 开源的DevOps
  + 这是个人项目，基本也是个人完成，前端用了vue2(d2admin框架非常炫酷)

+ 自动化运维特性
  + 外部平台或工具原生API调用
  + 多版本API支持（逐步升级）
  + 以用户纬度数据映射全自动配置管理
  
+ 核心功能
  + 堡垒机
    + 终端版Jumpserver全自动配置
    + Web终端
      + 支持多窗口批量操作（看日志利器）
      + 支持低权限/高权限登录（工单中申请权限）
  + Zabbix(API4.0)全自动运维
  + 多云支持
    + 阿里云
    + AWS
    + 腾讯云（未开发）
    + 私有云VMware-vSphere
  + 账户对多平台自动映射（免去日常权限申请和配置）



# 安装文档



#### 环境安装Oracle-JDK 1.8 & Maven 3

* JDK 1.8下载地址 [http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 
* 下载并安装 jdk-8u251-linux-i586.rpm
* Maven 3下载地址[http://maven.apache.org](http://maven.apache.org)
* 在/etc/profile中添加环境变量

```bash
# Export environment variables
export JAVA_HOME=/path/to/jdk
export M2_HOME=/path/to/maven
export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH
```

#### 环境安装Mysql 8.0

* 推荐使用阿里云RDS\(MySQL 8.0\) 
* 创建opscloud数据库\(字符集utf8mb4\)
* opscloud不支持Mysql5.x版本

#### 环境安装Redis 3

* 推荐使用阿里云Redis

#### Clone项目代码
```bash
$ git clone https://github.com/ixrjog/opscloud 
```

#### 导入SQL
```
# sql文件位置 other/database/3.0.0/opscloud.sql
# 自行修改127.0.0.1为你的mysql服务器ip或域名
$ mysql -uopscloud -p -h127.0.0.1 opscloud < ./opscloud.sql
```

#### 修改配置文件

```bash
# clone项目代码
$ git clone https://github.com/ixrjog/opscloud
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
# opscloud采用多环境配置文件开发，open对应的是开源版本配置
# 对应的配置文件eg: application-*-open.yml
# 
$ mvn -Dmaven.test.skip=true clean package -P open -U -am -pl opscloud-manage
# jar包路径
# opscloud-manage/target/opscloud-manage-open.jar
```

#### 项目启动

```bash
# 首次安装生成数据目录（集群部署可用NAS共享）
# 若使用低权限启动确保opscloud可以读写以下目录
$ mkdir -p /data/opscloud-data/ansible
$ mkdir -p /data/opscloud-data/log
$ mkdir -p /data/opscloud-data/xterm

# 部署 /opt/opscloud3/opscloud-manage-open.jar
$ mkdir -p /opt/opscloud3/
$ \cp opscloud-manage/target/opscloud-manage-open.jar /opt/opscloud3/

# JVM内存值请自行调优（下列命令适用于4G内存服务器启动）
# ${JASYPT_PASSWORD} 变量为opscloud加密密钥，用于数据加解密
# 可将变量写入/etc/profile
# export JASYPT_PASSWORD = '请使用高强度字符串'
$java -Xms2048m -Xmx2048m -Xmn1024m -Xss256k -XX:MaxMetaspaceSize=128M \
-XX:MetaspaceSize=128M -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC \
-Djasypt.encryptor.password=${JASYPT_PASSWORD} -jar /opt/opscloud3/opscloud-manage-open.jar
```

#### Ansible安装
+ 可安装最新版本2.9+
```
# CentOS7 eg:
yum install epel-release -y
yum install ansible -y
```

#### 主配置文件修改
+ 文件路径./opscloud-manage/src/main/resources/application-open.yml
+ 修改host中的redis服务器域名或ip
+ 修改password中的密码，若没有密码则留空
```
spring:
  profiles:
    include: common,account-open,zabbix-open,ldap-open,jumpserver-open,aliyun-open,aws-open,gitlab-open,vcsa-open,cloud,ansible-open,xterm-open
  redis:
    host: redis.opscloud.top
    port: 6379
    password: 123456
```

+ 修改opscloud.url中的mysql.opscloud.top为你的mysql域名或ip
+ 修改opscloud.username为你的mysql用户名
+ 修改opscloud.password为你的mysql密码
+ 修改jumpserver.url中的jumpserver.mysql.opscloud.top为你的jumpserver-mysql域名或ip,没有jumpserver则不要修改
+ 修改jumpserver.username为你的jumpserver-mysql用户名
+ 修改jumpserver.password为你的jumpserver-mysql密码
```
app:
  datasource:
    opscloud:
      url: jdbc:mysql://mysql.opscloud.top:3306/opscloud?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&useInformationSchema=true&tinyInt1isBit=true&nullCatalogMeansCurrent=true&serverTimezone=UTC&allowMultiQueries=true
      username: opscloud
      password: 123456
      driver-class-name: com.mysql.jdbc.Driver
      minimum-idle: 3
      maximum-pool-size: 10
      max-lifetime: 30000
      connection-test-query: SELECT 1
    jumpserver:
      url: jdbc:mysql://jumpserver.mysql.opscloud.top:3306/jumpserver?useUnicode=true&characterEncoding=utf8&autoReconnect=true
      username: jumpserver
      password: 123456
      driver-class-name: com.mysql.cj.jdbc.Driver
      minimum-idle: 3
      maximum-pool-size: 10
      max-lifetime: 30000
      connection-test-query: SELECT 1
```

#### 阿里云配置
+ 文件路径./opscloud-aliyun-core/src/main/resources/application-aliyun-open.yml
+ 修改accessKeyId/secret为你的阿里云主账户AK
+ regionIds可填写需要扫描的region, opscloud只会扫描填写的region
+ ECS管理只支持 mstart:true的账户（此值唯一）
+ RAM管理支持多个阿里云主账户
```
# 阿里云AK配置
# uid 企业别名（主账户id）
# regionIds 数组可以写多个
aliyun:
  accounts:
    -
      uid: 1000000000000001
      master: true
      name: '主账户'
      accessKeyId: YOUR_ACCESS_KEY_ID
      secret: YOUR_SECRET_KEY
      regionId: cn-hangzhou
      regionIds:
        - cn-hangzhou
        - cn-hongkong
        - us-west-1
        - cn-shanghai
    -
      uid: 1000000000000002
      master: false
      name: '第2账户'
      accessKeyId: YOUR_ACCESS_KEY_ID
      secret: YOUR_SECRET_KEY
      regionId: cn-hangzhou
      regionIds:
        - cn-hangzhou
```

#### Ansible配置
+ 文件路径./opscloud-ansible/src/main/resources/application-ansible-open.yml
+ 修改Ansible命令配置
+ dataPath/logPath可自定义修改
+ 将连接主机的私钥保存到${dataPath}/private_key/id_rsa
+ opscloud支持低权限启动调用ansible
```
# 生产环境 CentOS 7
# yum install ansible

# https://docs.ansible.com/ansible/latest/reference_appendices/config.html
# ansible.cfg
# 忽略主机文件中的特殊字符比如`-`
# force_valid_group_names = ignore
# 禁用警告信息
# deprecation_warnings = False

# inventoryPath: ${dataPath}/inventory
# scriptPath: ${dataPath}/script
# playbookPath: ${dataPath}/playbook
# privateKey: ${dataPath}/private_key/id_rsa

# 开发者建议: 虽然可以自定义路径，但建议使用oc数据目录下的相对路径
# oc数据目录 /data/opscloud-data oc集群服务器可用NAS存储
# 例如 /data/opscloud-data/ansible
ansible:
  version: 2.9.6
  bin: /bin/ansible
  dataPath: /data/opscloud-data/ansible
  playbookBin: /bin/ansible-playbook
  logPath: /data/opscloud-data/log/ansible
```
      

#### 前端部署
```
# 创建前端目录
$ mkdir -p /opt/opscloud3-static
$ cd /opt/opscloud3-static
# Clone项目代码
$ git clone https://github.com/ixrjog/opscloud-web-dist
```

#### 安装Nginx
```
# CentOS7 eg:
$ yum install epel-release
$ yum install nginx
```

#### 配置Nginx
```
$ mkdir -p /data/logs/oc3
$ cd /etc/nginx/conf.d
$ vim vhost_oc3.conf
# 配置文件内容
upstream upstream.opscloud3 {
    server 127.0.0.1:8080 weight=2;
}

server {
        listen 80;
        server_name oc3.opscloud.top;

        client_max_body_size 100m;
        # 首页重定向
        rewrite ^/+$ https://oc3.opscloud.top/index.html permanent;
        location /oc3/ {
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header Host $host;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://upstream.opscloud3;
            proxy_http_version 1.1;
            proxy_buffering off;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
        }

        location / {
            root /opt/opscloud3-static/;
        }

        location /status {
           return 200;  
        }

        access_log  /data/logs/oc3/access.log  main;
}

# 启动nginx
$ nginx start
```

#### 前端Aliyun-SLB配置
+ 如果在阿里云部署前端可以使用SLB代理Nginx实现集群高可用&https
+ opscloud使用了WebSocket协议,Web终端支持心跳保持会话
+ SLB配置监听
  + 负载均衡协议HTTPS
  + 后端协议HTTP
  + 启用HTTP2.0
  + 连接空闲超时时间(秒) 15
  + 连接请求超时时间(秒) 60


#### 欢迎提供更为详细的安装文档
+ ixrjog@qq.com