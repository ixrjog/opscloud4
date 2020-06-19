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
      