Welcome to the opsCloud wiki!
### 开发者
* liangjian 

opsCloud开发使用交流  QQ群号:630913972


### 版本环境
* Centos 6.x
* JDK 8
* Tomcat 8
* Gradel 3
* Mysql5.6 或 aliyunRDS
* Redis 3.0.3
* LDAP : apacheDS(http://directory.apache.org)
* Ansible 2

### 构建
```$xslt
# 可选参数 -Dorg.gradle.java.home=/usr/java/jdk1.8.0_51
gradle clean war -DpkgName=opscloud -Denv=online -refresh-dependencies -Dorg.gradle.java.home=/usr/java/jdk1.8.0_51 -Dorg.gradle.daemon=false
```

### 安装步骤1 数据库
```$xslt
# 安装 Mysql5.6+ 或使用AliyunRDS 
# 建库
create database opscloud character set utf8 collate utf8_bin;
grant all PRIVILEGES on opscloud.* to opscloud@'%' identified by 'opscloud';
# 导入db
mysql -uopscloud -popscloud opscloud < ./opscloud.sql
```

### 安装步骤2 Redis
```$xslt
# 安装Redis3 或使用阿里云Redis

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

### 安装步骤5 部署
```$xslt
安装Tomcat 并在webapps下部署 opscloud.war
修改配置文件 WEB-INF/classes/server.properties
启动Tomcat 首次登录使用admin/opscloud
```

### 安装步骤6 ansible
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

## 基本功能

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
* shadowsocks用户配置管理（没开放）；
* terminal堡垒机配置管理（内部功能）
* ansible主机文件管理（自动分组）

### 跳板机（不支持操作审计）
* Web版跳板机KeyBox（支持多服务器同时操作）
* Terminal跳板机Getway



![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-%E6%9C%8D%E5%8A%A1%E5%99%A8-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-%E6%9C%8D%E5%8A%A1%E5%99%A8-2.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-ECS-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-ECS-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-ECS-3.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-VM-1.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-PS-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-PS-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-PS-3.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-CONFIGFILE-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-CONFIGFILE-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-CONFIGFILE-3.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-CONFIGFILE-4.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-KEYBOX-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-KEYBOX-2.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-LOGCLEAN-1.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-LOGSERVICE-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-LOGSERVICE-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-LOGSERVICE-3.jpeg)





![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-STATUS-CI.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-STATUS-PERF.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-TODO-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-TODO-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-TODO-3.jpeg)


![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-USER-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-USER-2.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-ZABBIX-1.jpeg)





