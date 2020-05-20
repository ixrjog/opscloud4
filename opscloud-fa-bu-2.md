# OpsCloud发布2

#### OC3.0版本发布

* 工单、web堡垒机、跳板机迁移至oc3
* 登录账户/密码不变
* 新应用上线工单目前还在老oc中申请
* 任何意见、建议、bug都可以反馈给我

#### 平台特性

* 全接口API，可调用查询运维元数据 [https://oc3.ops.yangege.cn/oc3/doc.html\#/home](https://oc3.ops.yangege.cn/oc3/doc.html#/home)
* 工单优化、支持组织架构上级审批
* 增强的web-xterm

#### 跳板机使用说明

* [https://oc3.ops.yangege.cn/index.html\#/workbench/jump](https://oc3.ops.yangege.cn/index.html#/workbench/jump)
* 登录方式\(替换USERNAME为你的登录用户名\)：

```bash
# -C 压缩传输
# -o StrictHostKeyChecking=no 公钥免检
ssh USERNAME@oc3.ops.yangege.cn
```



#### web-xterm使用说明

* [https://oc3.ops.yangege.cn/index.html\#/workbench/xterm](https://oc3.ops.yangege.cn/index.html#/workbench/xterm)
* 支持高权限批量登录系统（工单中申请）
* 增强的批量命令，开启批量命令后任意窗口拥有输入焦点即可输入
* 会话复制，当前终端快速复制N个窗口（可改变高/低权限登录）
* 个人文档，用于保存常用命令
* 全站web-xterm，服务器管理、playbook等界面都能快速登录系统排除故障

![](.gitbook/assets/oc-webxterm-2.gif)

