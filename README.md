Welcome to the opsCloud wiki!
### 开发者
* liangjian 

opsCloud开发使用交流  QQ群号:630913972


### 版本环境
* Centos 6.x
* JDK 8
* Tomcat 8.0.36
* Gradel 3
* Mysql5.6 或 aliyunRDS
* Redis 3.0.3
* LDAP : apacheDS(http://directory.apache.org)
* Ansible 2

### 构建
```$xslt
# 可选参数 -Dorg.gradle.java.home=/usr/java/jdk1.8.0_51
gradle clean war -DpkgName=opscloud -Denv=online -refresh-dependencies -Dorg.gradle.daemon=false
```

### 安装步骤1 数据库
```$xslt
# 安装 Mysql5.6 或使用AliyunRDS 
# 建库
create database opscloud character set utf8 collate utf8_bin;
grant all PRIVILEGES on opscloud.* to opscloud@'%' identified by 'opscloud';
# 导入db
mysql -uopscloud -popscloud opscloud < ./opscloud.sql

# Mysql5.7 兼容性问题
已知问题1：如安装的是mysql5.7+，需要关闭mysql的"ONLY_FULL_GROUP_BY"
# 查询
select @@global.sql_mode
# 修改
set @@global.sql_mode=‘STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION’;
```

### 安装步骤2 Redis
```$xslt
# 安装Redis3 或使用阿里云Redis
http://download.redis.io/releases/redis-3.2.11.tar.gz
tar -xzvf redis-3.2.11.tar.gz
cd redis-3.2.11
make && make install

```

### 安装步骤3 Java(JDK8)
```$xslt
# 安装JDK8
```

### 安装步骤4 LDAP(apacheDS)
```$xslt
# 官网 http://directory.apache.org/apacheds/download/download-linux-bin.html
# 下载安装包
wget http://mirrors.tuna.tsinghua.edu.cn/apache//directory/apacheds/dist/2.0.0-M24/apacheds-2.0.0-M24-64bit.bin
chmod +x apacheds-2.0.0-M24-64bit.bin && ./apacheds-2.0.0-M24-64bit.bin
Do you agree to the above license terms? [yes or no]
yes
Unpacking the installer...
Extracting the installer...
Where do you want to install ApacheDS? [Default: /opt/apacheds-2.0.0-M24]

Where do you want to install ApacheDS instances? [Default: /var/lib/apacheds-2.0.0-M24]

What name do you want for the default instance? [Default: default]

Where do you want to install the startup script? [Default: /etc/init.d]

Which user do you want to run the server with (if not already existing, the specified user will be created)? [Default: apacheds]

Which group do you want to run the server with (if not already existing, the specified group will be created)? [Default: apacheds]

Installing...
id: apacheds: No such user
Done.
ApacheDS has been installed successfully.

# 启动服务
/etc/init.d/apacheds-2.0.0-M24-default start
Starting ApacheDS - default...
```


### Tomcat版本问题
推荐使用Tomcat 8.0.36 或 最高不超过8.0.51

Tomcat8.0.52添加了RFC 3986这个规范。
RFC 3986文档对Url的编解码问题做出了详细的建议，指出了哪些字符需要被编码才不会引起Url语义的转变，以及对为什么这些字符需要编码做出了相应的解释。
RFC 3986文档规定，Url中只允许包含英文字母（a-zA-Z）、数字（0-9）、-_.~4个特殊字符以及所有保留字符（! * ' ( ) ; : @ & = + $ , / ? # [ ]）。
还有一些字符当直接放在Url中的时候，可能会引起解析程序的歧义，这些字符被视为不安全字符。
空格：Url在传输的过程，或者用户在排版的过程，或者文本处理程序在处理Url的过程，都有可能引入无关紧要的空格，或者将那些有意义的空格给去掉。
引号以及<>：引号和尖括号通常用于在普通文本中起到分隔Url的作用
##### 通常用于表示书签或者锚点
%：百分号本身用作对不安全字符进行编码时使用的特殊字符，因此本身需要编码
|^[]`~：某一些网关或者传输代理会篡改这些字符
对于此问题，有以下几种解决方案。
1、切换版本到8.0.51以下
2、修改Tomcat源码
3、前端请求对URL编码。
4、修改Get方法为Post方法。
5、因是不安全字符，默认被 tomcat拦截。如果需要在URL中传输json数据，在catalina.properties中添加支持。
```aidl
tomcat.util.http.parser.HttpParser.requestTargetAllow=|[],
```


### 安装步骤5 部署
假如Tomcat安装路径为 /usr/local/tomcat

1. 删除/usr/local/tomcat/webapps/ 所有文件和目录
2. 解压opscloud.war，并将解压文件复制到/usr/local/tomcat/webapps/ROOT/
   注意：不要带项目路径opscloud
3. 修改opscloud配置文件/usr/local/tomcat/webapps/ROOT/WEB-INF/classes/server.properties
4. 启动Tomcat：/usr/local/tomcat/bin/startup.sh  (关闭/usr/local/tomcat/bin/shutdown.sh)

* 修改相关配置内容
   - 管理数据库配置修改：jdbc_url, jdbc_user, jdbc_password
   - LDAP登陆认证配置修改：ldapUrl, ldapUserDn, ldapPwd
   - Redis 配置修改：redis.host, redis.port, redis.pwd
* 启动Tomcat 首次登录使用admin/opscloud
* 如果启用了Nginx反向代理Tomcat(opscloud)，需要启用ws
```$xslt
server {
        listen 443;
        server_name opscloud.com;
        ssl on;
        ssl_certificate /usr/local/nginx/conf/ssl_key/opscloud.com.crt;
        ssl_certificate_key /usr/local/nginx/conf/ssl_key/opscloud.com.key;
        ssl_session_timeout 5m;
        ssl_protocols SSLv2 SSLv3 TLSv1;
        ssl_ciphers ALL:!ADH:!EXPORT56:RC4+RSA:+HIGH:+MEDIUM:+LOW:+SSLv2:+EXP;
        ssl_prefer_server_ciphers on;

        location = /favicon.ico {
            root /data/www/ROOT/static ;
        }

        location ~  ^/(css|fonts|img|js|l10n|tpl|vendor)/ {
            root /data/www/ROOT/opscloud;
            expires 2m;
        }

        # ====keybox/getway独立部署启用此配置======
        location ~  ^/keybox/ws {
            proxy_set_header Host  $host;
            proxy_set_header X-Forwarded-For  $remote_addr;
            proxy_pass http://upstream.getway.java;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "Upgrade";
            # 限制访问，不做限制请删除
            allow 192.168.0.0/24;
            deny all;
        }
        # =====keybox/getway独立部署启用此配置=====
        
        location / {
            proxy_set_header Host  $host;
            proxy_set_header X-Forwarded-For  $remote_addr;
            proxy_pass http://127.0.0.1:8080;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "Upgrade";
            keepalive_timeout  180; #  连接超时时间，1分钟，具体时间可以根据请求（例如后台导入）需要的时间来设置
            proxy_connect_timeout 180;  #   1分钟
            proxy_read_timeout 180;  #  1分钟
            # 限制访问，不做限制请删除
            allow 192.168.0.0/24;
            deny all;
        }

        access_log  /data/www/logs/opscloud/access.log  access;        

}
```

### 安装步骤6 Ansible
* 安装
```$xslt
yum install epel-release -y
yum install ansible –y
```

* 配置
```$xslt
# 查看配置文件路径 (/etc/ansible/ansible.cfg)
ansible --version
ansible 2.5.3
  config file = /etc/ansible/ansible.cfg
  configured module search path = [u'/root/.ansible/plugins/modules', u'/usr/share/ansible/plugins/modules']
  ansible python module location = /usr/lib/python2.6/site-packages/ansible
  executable location = /usr/bin/ansible
  python version = 2.6.6 (r266:84292, Aug 18 2016, 15:13:37) [GCC 4.4.7 20120313 (Red Hat 4.4.7-17)]
```
参考配置文件
```$xslt
# config file for ansible -- http://ansible.com/
# ==============================================

# nearly all parameters can be overridden in ansible-playbook
# or with command line flags. ansible will read ANSIBLE_CONFIG,
# ansible.cfg in the current working directory, .ansible.cfg in
# the home directory or /etc/ansible/ansible.cfg, whichever it
# finds first

[defaults]

# some basic default values...
inventory      = /etc/ansible/hosts
#library        = /usr/share/my_modules/
remote_tmp     = /tmp/.ansible/tmp
pattern        = *
forks          = 5
poll_interval  = 15
sudo_user      = root
local_tmp      = /tmp/.ansible/tmp
#ask_sudo_pass = True
#ask_pass      = True
transport      = smart
#remote_port    = 22
module_lang    = C
gathering = implicit
# uncomment this to disable SSH key host checking
host_key_checking = False
# change this for alternative sudo implementations
#sudo_exe = sudo
deprecation_warnings=False

# SSH timeout
timeout = 10
remote_user = manage
#remote_user = xqadmin
private_key_file = ~/.ssh/id_rsa
ansible_managed = Ansible managed: {file} modified on %Y-%m-%d %H:%M:%S by {uid} on {host}

#action_plugins     = /usr/share/ansible_plugins/action_plugins
#callback_plugins   = /usr/share/ansible_plugins/callback_plugins
#connection_plugins = /usr/share/ansible_plugins/connection_plugins
#lookup_plugins     = /usr/share/ansible_plugins/lookup_plugins
#vars_plugins       = /usr/share/ansible_plugins/vars_plugins
#filter_plugins     = /usr/share/ansible_plugins/filter_plugins

fact_caching = memory
log_path = /data/www/logs/ansible/ansible.log

[privilege_escalation]

[paramiko_connection]

[ssh_connection]
ssh_args = ""
scp_if_ssh = True

[accelerate]
accelerate_port = 5099
accelerate_timeout = 30
accelerate_connect_timeout = 5.0

# The daemon timeout is measured in minutes. This time is measured
# from the last activity to the accelerate daemon.
accelerate_daemon_timeout = 30 

# If set to yes, accelerate_multi_key will allow multiple
# private keys to be uploaded to it, though each user must
# have access to the system via SSH to add a new key. The default
# is "no".
accelerate_multi_key = yes

[selinux]



```

### 配置 Getway(终端跳板机)
>前提安装和配置完成ansible

配置管理-Getway配置管理
* 全局配置文件管理：无需修改
* 用户配置文件管理：用户查看用户授权的服务器组
* 远程同步配置：用于推送本地配置文件到getway服务器（支持多台）
  - 首先配置开通服务器加入group_getway
  - 新增（选择服务器，其它配置默认即可）
  - 批量同步（首次需要手动同步，以后服务器修改和授权，配置会自动同步）
  - 私匙id_rsa放到opscloud服务器的/data/www/getway/keys/manage/id_rsa(${GETWAY_KEY_PATH}/id_rsa)
  - 任务管理-TaskScript-选择getway服务器，执行脚本getway_set_login
  
* 配置私钥（id_rsa）
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/keybox/WechatIMG35.jpeg)
* KeyBox(WebShell)
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/keybox/WechatIMG36.jpeg)
* Getway全局服务器列表配置
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/keybox/WechatIMG37.jpeg)
* Getway用户配置
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/keybox/WechatIMG38.jpeg)
* Getway多服务器同步配置（需配置ansbile）
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/keybox/WechatIMG39.jpeg)
* Getway服务器一键初始化（需配置ansbile）
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/keybox/WechatIMG40.jpeg)



### 配置 Zabbix
* 支持 Zabbix API Version : 3.0
https://www.zabbix.com/documentation/3.0/manual/api
* 支持 Zabbix API Version : 3.4
https://www.zabbix.com/documentation/3.4/manual/api
* 未知 Zabbix API Version : 4.0
https://www.zabbix.com/documentation/4.0/manual/api

* API URL
http://${ZABBIX_HOST}/api_jsonrpc.php

* Zabbix API配置
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/zabbix/WechatIMG29.jpeg)
* Zabbix API测试
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/zabbix/WechatIMG30.jpeg)
* 获取Zabbix模版，并启用需要的模版（配置主机监控只显示启用的模版）
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/zabbix/WechatIMG31.jpeg)
* 主机监控管理界面（服务器表中的服务器数据）
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/zabbix/WechatIMG32.jpeg)
* 添加主机监控，可选择已启用的模版和ZabbixProxy
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/zabbix/WechatIMG33.jpeg)
* 服务器组属性中可以预先配置服务器的Zabbix模版和ZabbixProxy,让添加主机监控更加快捷
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/zabbix/WechatIMG34.jpeg)

## Task
* 批量执行命令
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/task/WechatIMG44.jpeg)
* 批量执行脚本（opscloud上配置的Script）
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/task/WechatIMG46.jpeg)
* 脚本配置/查看
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/task/WechatIMG47.jpeg)
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/task/WechatIMG48.jpeg)
* 查询执行历史
![sec](https://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/opscloud/task/WechatIMG49.jpeg)



### 服务器管理
* 服务器管理
* 阿里云ECS主机管理(自动获取ECS主机信息）
* 阿里云模版管理(自动创建ECS主机&项目扩容)
* 服务器属性管理／服务器组属性管理

### 监控管理
* 托管zabbix服务器，通过zabbix api控制
1. 一键添加主机监控（通过服务器表数据）
2. 自动添加主机组
3. 自动添加用户（sms/email告警配置）及用户组
4. 自动配置动作（Action）

* 服务器监控仪表盘

### 任务管理
* 批量命令执行
* 批量脚本执行（可保存自定义脚本）

### IP管理
* IP段&IP管理

### 配置管理
* shadowsocks用户配置管理；
* terminal堡垒机配置管理（内部功能）
* ansible主机文件管理（自动分组）

### 跳板机（不支持操作审计）
* Web版跳板机KeyBox（支持多服务器同时操作）
* Terminal跳板机Getway







