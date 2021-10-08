Welcome to the opscloud wiki!

# OpsCloud4.0 IaC基础架构即代码
<img src="https://img.shields.io/badge/version-4.0.0-brightgreen.svg"></img>
<img src="https://img.shields.io/badge/java-8-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/springboot-2.3.10.RELEASE-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/mysql-8-brightgreen.svg"></img> 

<br>

### Tag:4.0.3版本主要功能(黄框功能未实现)

<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/opscloud4.png"></img>
<br>

### 环境管理
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-2.png"></img>

### 服务器管理
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-6.png"></img>

### 远程控制(RDP/VNC)
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-10.png"></img>

### Web-Terminal(Web终端)

#### 服务器(批量操作,会话复制,高低权限,布局切换)
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-server.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-server-2.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-server-3.png"></img>

#### 容器(支持登录操作或只读查看日志)
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-k8s-pod.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-k8s-pod-2.png"></img>

### SSH-Server(命令行终端)
#### 帮助命令`help`,命令参数提示
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/ssh-server/ssh-server-help.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/ssh-server/ssh-server-help-2.png"></img>
#### 服务器列表命令`list`
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/ssh-server/ssh-server-list.png"></img>
#### 容器组列表命令`list-k8s-pod`
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/ssh-server/ssh-server-k8s-pod.png"></img>

### 审计
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-audit.png"></img>

### 剧本
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/playbook/playbook.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/playbook/playbook-2.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/playbook/playbook-3.png"></img>

### 用户
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/user/user.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/user/user-details.png"></img>

### 数据源(万物皆资产)
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-1.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/datasource/datasource-aliyun.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/datasource/datasource-aliyun-log.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/datasource/datasource-zabbix-host.png"></img>

#### <span style="color:green">平台演示视频1</span>
<video src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/video/opscloud4-1.mov" width="400px" height="300px" controls="controls"></video>

#### OpsCloud 优势
+ 代码质量大于功能实现,充分抽象与继承实现
+ 约定大于配置
+ 代码即文档
+ 部署简单（jar+sql）
+ 支持集群架构

#### 4.0版本核心设计
+ 事件驱动
+ 万物皆资产
  + 多实例支持
  + 多云支持（暂无其他云账户，目前只支持阿里云，有计划接入腾讯云，AWS，vSphere7）
  + 抽象数据源实例资产
+ 堡垒机
  + 远程桌面RDP,VNC(需安装apache-guacamole)
  + Web终端（支持多开，会话复制，命令同步）
  + Ssh服务器
    + 支持快速登录（Gitlab公钥资产认证）
    + 展示服务器环境，标签，授权账户
    + 支持Kubernetes容器登录（容器堡垒机）
    + 支持查看Kubernetes容器日志
    + 以事件驱动的主动式堡垒机技术（通过事件ID登录服务器）
+ 剧本任务
+ RBAC
+ 集群架构

### 帮助文档

<a style="color:#2b669a" href="https://www.kancloud.cn/ixrjog/opscloud4/2361886" target="_blank">传送门:https://www.kancloud.cn/ixrjog/opscloud4/2361886</a>


### Thanks to JetBrains
<a href="https://www.jetbrains.com" target="_blank">
  <img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/jetbrains-logos/jetbrains-variant-2.svg"></img>
</a>

> Thanks JetBrains to support the project providing such great IDE.