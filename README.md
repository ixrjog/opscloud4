Welcome to the opscloud wiki!

# OpsCloud简介
<img src="https://img.shields.io/badge/version-3.0.0-brightgreen.svg"></img>
<img src="https://img.shields.io/badge/java-8-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/springboot-2.2.2.RELEASE-brightgreen.svg"></img> 
<img src="https://img.shields.io/badge/mysql-8-brightgreen.svg"></img> 

<br>
OpsCloud是云时代的全工具链集成运维平台(DevOps)


开源协议：<a style="color:#2b669a" href="http://www.gnu.org/licenses/old-licenses/gpl-2.0.html" target="_blank">GNU General Public License v2</a>

### 开发者
* 白衣

### 2.0使用请跳转
  + https://github.com/ixrjog/opsCloud/tree/2.0.1

### 最新版本说明 3.0.0（开发中预计6月底发布）

### 最佳用户体验
  + 显示器分辨率大于或等于1080P
  + 浏览器Chrome/Safari

<a style="color:#2b669a" href="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/gif/oc-webxterm-1.mov" target="_blank">XTerm演示视频</a>
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/gif/oc-webxterm-2.gif"></img>
### web终端
  + 支持批量登录
  + 支持命令同步
  + 支持会话复制（单机多终端）

<a style="color:#2b669a" href="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/gif/oc-playbook-1.mov" target="_blank">Playbook演示视频</a> 
<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/gif/oc-playbook-1.gif"></img>
### ansible-playbook
  + 多线程执行
  + 批量执行结果精确展示（成功数量/失败数量）
  + 支持自定义变量
  + 支持tags
  + 执行结果查看页面可以直接使用web终端登录操作（支持会话复制）
  
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
  + 可视化数据管理DMS（支持AliyunRDS, 自建Mysql）
    + 权限申请
    + 操作审计
    + 高危操作过滤
    + 自动导出
    + 字段脱敏
  + Zabbix(API4.0)全自动运维
  + 多云支持
    + 阿里云
    + AWS
    + 腾讯云
    + 私有云VMware-vSphere
  + 账户对多平台自动映射（免去日常权限申请和配置）


+ 源码编译
```$xslt
# 安装Maven & JDK8
# prod为当前环境配置文件，如 application-zabbix-prod.yml
mvn -Dmaven.test.skip=true clean package -P prod -U -am -pl opscloud-manage
```

+ 项目启动
```$xslt
# JVM内存值请自行调优（下列命令适用于4G内存服务器启动）
# ${JASYPT_PASSWORD} 变量为opscloud加密密钥，用于数据加密/解密
# 可将变量写入/etc/profile
# export JASYPT_PASSWORD = '请使用高强度字符串'
java -Xms2048m -Xmx2048m -Xmn1024m -Xss256k -XX:MaxMetaspaceSize=128M \
-XX:MetaspaceSize=128M -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC \
-Djasypt.encryptor.password=${JASYPT_PASSWORD} -jar ./opscloud-manage-prod.jar
```

