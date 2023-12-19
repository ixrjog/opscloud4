Welcome to the opscloud wiki!


## Thanks to JetBrains
<a href="https://www.jetbrains.com" target="_blank">
  <img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/jetbrains-logos/jetbrains-variant-2.svg" alt=""/>
</a>

> Thanks JetBrains to support the project providing such great IDE.

![GitHub Stats Card](https://github-readme-stats.vercel.app/api?username=ixrjog&show_icons=true&theme=onedark)

## OpsCloud4 云上运维
<img src="https://img.shields.io/badge/Version-4.6.0-brightgreen.svg"></img>
<img src="https://img.shields.io/badge/OpenJDK-21-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/SpringBoot-3.2.0-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/MySQL-8-brightgreen.svg"></img>
<br/>
[![Security Status](https://www.murphysec.com/platform3/v31/badge/1683749365419687936.svg)](https://www.murphysec.com/console/report/1656107048332996608/1683749365419687936)
<br/>

## 架构图
<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/4.5.0/opscloud4_2.svg" width="600" alt=""/>

## Code repository
+ Backend
  + [https://github.com/ixrjog/opscloud4](https://github.com/ixrjog/opscloud4)
+ Frontend
  + [https://github.com/ixrjog/opscloud4-web](https://github.com/ixrjog/opscloud4-web)

## Database
+ [v4.6.0 opscloud4-github.sql](https://opscloud4-web.oss-eu-west-1.aliyuncs.com/package/4.6.0/opscloud4-github.sql)

## 登录信息
+ username:baiyi
+ password:空密码
  
## 开发环境
+ OpenJDK 21
+ node-v16.13.0
+ MacBook M1
+ IntelliJ IDEA 2023
+ WebStorm 2023

## Leo持续交付

+ 构建

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/leo/leo-8.png" width="830"></img>

+ 部署

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/leo/leo-9.png" width="830"></img>
<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/leo/leo-6.png" width="830"></img>

+ 规则配置（封网）

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/leo/leo-10.png" width="830"></img>

+ 任务配置

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/leo/leo-5.png" width="830"></img>

## 容器堡垒机（集成:Leo持续交付版本信息,多可用区部署可视化）
<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/4.5.1/kt-1.png" width="830"></img>

## 主要功能
+ Leo持续交付（Kubernetes）
  + 全冗余架构，多Jenkins引擎且无状态
  + 权限控制/封网策略
  + 分组（蓝/绿），金丝雀部署；可视化发布过程
+ 多实例动态数据源，万物皆资产
+ 堡垒机
  + 服务器Web终端
    + 多服务器同时连接+命令同步、会话复制、会话心跳
    + 支持ED25519、RSA密钥
  + KubernetesWeb终端
    + 多容器同时登录+命令同步、容器日志、会话心跳 
  + SSH-Server
    + 原生SSH协议实现，支持ED25519密钥
    + 简化用户登录，自动关联用户GitLab账户公钥
    + 支持服务器、容器登录
```mermaid
flowchart LR
    A[User] -->|SSH:22| B{SLB}
    B-->|TCP:2222| C[Opscloud Server] 
    B-->|TCP:2222| D[Opscloud Server] 
    
    E[User] -->|ssh ed25519 | F{{SSH-Server}}
    F-->|ssh ed25519| G[Linux] 
```
+ 服务器批量任务
  + Ansible Playbook
  + 单服务器多线程实现，任务日志更加清晰
+ RBAC,MFA(OTP)
+ 集群架构
  + 实例健康检查接口 /api/instance/health/lb-check (GET) 
  + 分布式调度任务(Quartz)、定时任务分布式锁(Shedlock)、任务并发锁(Redis)
  + 高性能、可伸缩、高可用性
+ 标签驱动、事件驱动 
+ API文档
  + example.com/doc.html
  + API Token

## Services & Ports

| service    | protocol | port | startup parameter     |
|------------|----------|------|-----------------------|
| web        | http     | 8080 | --server.port=8080    |
| ssh-server | ssh      | 2222 | --ssh.shell.port=2222 |

## 功能截图

<table>
<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/sys/env.png" alt="系统环境"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/sys/tag.png" alt="标签管理"></td>
</tr>
<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/sys/instance.png" alt="集群管理"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-6.png" alt="服务器管理"></td>
</tr>
<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-10.png" alt="远程控制(RDP/VNC)"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-server.png" alt="服务器(批量操作,会话复制,高低权限,布局切换)"></td>
</tr>
<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-server-2.png" alt="服务器(批量操作,会话复制,高低权限,布局切换)"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-server-3.png" alt="服务器(批量操作,会话复制,高低权限,布局切换)"></td>
</tr>
<tr>
<td><img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/terminal/terminal-pod.png" alt="容器(支持登录操作或只读查看日志)"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-k8s-pod-2.png" alt="容器(支持登录操作或只读查看日志)"></td>
</tr>
<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/ssh-server/ssh-server-help.png" alt="SSH-Server"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/ssh-server/ssh-server-help-2.png" alt="SSH-Server"></td>
</tr>
<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/ssh-server/ssh-server-event.png" alt="SSH-Server"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/ssh-server/ssh-server-list.png" alt="SSH-Server"></td>
</tr>
<tr>
<td><img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/ssh-server/ssh-server-list-k8s-pod.png" alt="SSH-Server"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/terminal/terminal-audit.png" alt="审计"></td>
</tr>
<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/playbook/playbook.png" alt="剧本"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/playbook/playbook-2.png" alt="剧本"></td>
</tr>
<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/playbook/playbook-3.png" alt="剧本"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/playbook/playbook_log.png" alt="剧本"></td>
</tr>

<tr>
<td><img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/user/user.png" alt="用户"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/user/user-details.png" alt="用户"></td>
</tr>

<tr>
<td><img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/user/user_mfa.png" alt="MFA"></td>
<td><img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/datasource/instance.png" alt="数据源"></td>
</tr>

<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/datasource/datasource-aliyun.png" alt="数据源"></td>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/datasource/datasource-aliyun-log.png" alt="数据源"></td>
</tr>

<tr>
<td><img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/datasource/datasource-zabbix-host.png" alt="数据源"></td>
</tr>
</table>

## <span style="color:green">平台演示视频1</span>
<video src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/video/opscloud4-1.mov" width="400px" height="300px" controls="controls"></video>

## 帮助文档

<a style="color:#2b669a" href="https://www.kancloud.cn/ixrjog/opscloud4/2361886" target="_blank">传送门:https://www.kancloud.cn/ixrjog/opscloud4/2361886</a>

## 交流群
Q群: 630913972
