Welcome to the opscloud wiki!

# OpsCloud4.0 IaC基础架构即代码
<img src="https://img.shields.io/badge/version-4.0.0-brightgreen.svg"></img>
<img src="https://img.shields.io/badge/java-8-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/springboot-2.3.10.RELEASE-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/mysql-8-brightgreen.svg"></img> 

<br>

#### Tag:4.0.3版本主要功能(黄框功能未实现)

<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/opscloud4.png"></img>
<br>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-1.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-2.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-3.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-4.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-5.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-6.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-7.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-8.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-9.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-10.png"></img>
<img src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/github/oc4-11.png"></img>

#### <span style="color:green">平台演示视频1</span>
<video src="https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud4/video/opscloud4-1.mov" width="800px" height="600px" controls="controls"></video>

#### OpsCloud 优势
+ 代码质量大于功能实现,充分抽象与继承实现
+ 约定大于配置
+ 代码即文档
+ 部署简单（jar+sql）,跨平台
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