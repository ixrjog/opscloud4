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


<img src="https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/github/mov/createInstance.gif"></img> 

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
  + Java Springboot 开源的DevOps(CMDB)还有第二家么？
  + 这是个人项目，基本也是个人完成，前端用了vue3(d2admin框架非常炫酷)

+ 自动化运维特性
  + 外部平台或工具原生API调用
  + 多版本API支持（逐步升级）
  + 以用户纬度数据映射全自动配置管理
  
+ 核心功能
  + 堡垒机(Jumpserver全自动透明化，无需购买收费版就能实现增强功能)
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

