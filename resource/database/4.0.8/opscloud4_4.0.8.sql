/*
 Navicat Premium Data Transfer

 Source Server         : opscloud4-dev
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : rm-d7oczvp3c5k2su19b.mysql.eu-west-1.rds.aliyuncs.com:3306
 Source Schema         : opscloud4-dev

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 16/02/2022 14:50:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for access_token
-- ----------------------------
DROP TABLE IF EXISTS `access_token`;
CREATE TABLE `access_token` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户登录名',
  `token_id` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '令牌标识',
  `token` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'None' COMMENT '访问令牌',
  `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否无效',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `expired_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `token` (`token`) USING BTREE,
  UNIQUE KEY `token_id` (`token_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='api调用令牌';

-- ----------------------------
-- Records of access_token
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for aliyun_log
-- ----------------------------
DROP TABLE IF EXISTS `aliyun_log`;
CREATE TABLE `aliyun_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `datasource_instance_id` int(11) NOT NULL COMMENT '数据源实例ID',
  `project` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `logstore` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `config` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `account_uid` (`datasource_instance_id`,`project`,`logstore`,`config`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='阿里云日志服务';

-- ----------------------------
-- Records of aliyun_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for aliyun_log_member
-- ----------------------------
DROP TABLE IF EXISTS `aliyun_log_member`;
CREATE TABLE `aliyun_log_member` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `aliyun_log_id` int(11) NOT NULL COMMENT 'SLS表主键ID',
  `server_group_id` int(11) NOT NULL,
  `server_group_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `topic` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `env_type` int(2) NOT NULL DEFAULT '0' COMMENT '环境类型',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_push_time` timestamp NULL DEFAULT NULL COMMENT '最后推送时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `aliyun_sls_id` (`aliyun_log_id`,`server_group_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='阿里云日志服务服务器组成员';

-- ----------------------------
-- Records of aliyun_log_member
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ansible_playbook
-- ----------------------------
DROP TABLE IF EXISTS `ansible_playbook`;
CREATE TABLE `ansible_playbook` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `playbook_uuid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '剧本UUID',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `playbook` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '剧本内容',
  `tags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '标签配置',
  `vars` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '外部变量',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `playbook_uuid` (`playbook_uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of ansible_playbook
-- ----------------------------
BEGIN;
INSERT INTO `ansible_playbook` VALUES (1, '60ea4519d68045719a78a8c995c59a11', 'Remote replication', '---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name: copy\n      copy:\n        src: \"{{ src }}\"\n        dest: \"{{ dest }}\"\n        owner: manage\n        group: manage\n      become: yes', '', '', '远程复制文件', '2021-09-01 21:18:34', '2021-09-07 17:02:12');
INSERT INTO `ansible_playbook` VALUES (2, '2ff188d2d7fa4880a321f3446fc833e0', 'Test', '---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name: test1\n      command: echo \'test1\'\n    - name: test2\n      command: echo \'test2\'', '', '', '测试剧本', '2021-09-23 22:28:25', '2021-09-23 22:31:11');
INSERT INTO `ansible_playbook` VALUES (5, '1ad8e2adb7094190b29bdc4504b00c2a', 'push-dnsmasq-conf', '---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name:\n      shell: cd /data/www/data/dnsmasq && wget -O dnsmasq.conf vc-res.ops.yangege.cn/res/dnsmasq/{{ useType }}/dnsmasq.conf\n    - name: check conf\n      shell: /usr/sbin/dnsmasq --test -C /data/www/data/dnsmasq/dnsmasq.conf\n      register: result\n    - name: check if deploy fail\n      fail: msg=\"conf check fail\"\n      when: result.failed == true\n    - name: restart service\n      shell: /etc/init.d/dnsmasq restart', '', '', '推送dnsmasq配置文件', '2021-09-28 17:09:02', '2021-09-28 17:09:02');
INSERT INTO `ansible_playbook` VALUES (6, '8c639f378e5343859c85e4332de9cd20', 'playbook-nginx', '---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name: copy file \n      copy: src={{ src }} dest={{ dest }} owner=root group=root  \n      notify:\n       - reloaded service\n    - name: get ps\n      shell: ps -ef|grep nginx|grep -v grep\n      register: psInfo\n    - name: show ps\n      debug: msg=\"{{ psInfo }}\"\n  handlers:  \n    - name: reloaded service\n      shell: /bin/systemctl reload nginx.service', '', 'vars:\n  src: /home/app/opscloud-data/nginx/\n  dest: /data/nginx/conf', '', '2021-09-28 17:10:08', '2021-09-28 17:16:50');
INSERT INTO `ansible_playbook` VALUES (10, 'ea91fc9fa6e245de890a277bd5ed34c0', 'playbook-nginx-sync-config', '---\n- hosts: \"{{ hosts }}\"\n  become: yes\n  tasks:\n    - name: install rsync\n      yum:\n        name: rsync\n        state: present\n    - name: creates directory\n      file:\n        path: \"/data/www/nginx-conf/{{ dest }}\"\n        state: directory\n        owner: www\n        group: www\n        mode: 0775\n    - name: sync config \n      synchronize:\n        src: \"{{ src }}/\"\n        dest: \"/data/www/nginx-conf/{{ dest }}/\"\n        delete: yes\n    - name: recursively change ownership of a directory\n      file:\n        path: /data/www/nginx-conf\n        state: directory\n        recurse: yes\n        owner: www\n        group: www\n        mode: 0644\n    - name: reloaded nginx\n      shell: /etc/init.d/nginx reload\n    - name: get ps\n      shell: ps -ef|grep nginx|grep -v grep\n      register: psInfo\n    - name: show ps\n      debug: msg=\"{{ psInfo }}\"\n', '', 'vars:\n  src:\n  dest:', '同步nginx配置文件', '2021-09-28 17:13:42', '2021-09-28 17:13:42');
INSERT INTO `ansible_playbook` VALUES (12, '62ceb487b6bb4b869510ac8083a5b847', 'playbook-nginx-push-config', '---\n- hosts: \"{{ hosts }}\"\n  become: yes\n  tasks:\n    - name: push file\n      copy:\n        src: \"{{ src }}\"\n        dest: \"/data/www/nginx-conf/{{ dest }}\"\n        owner: www\n        group: www\n        mode: 0644\n      notify:\n       - reloaded nginx\n    - name: get ps\n      shell: ps -ef|grep nginx|grep -v grep\n      register: psInfo\n    - name: show ps\n      debug: msg=\"{{ psInfo }}\"\n  handlers:  \n    - name: reloaded nginx\n      shell: /etc/init.d/nginx reload', '', 'vars:\n  src:\n  dest:', '推送nginx配置文件', '2021-09-28 17:22:32', '2021-09-28 17:22:32');
INSERT INTO `ansible_playbook` VALUES (13, 'dcf0a7a51ec048f9ac8ab0365a42a498', 'playbook-nginx-del-config', '---\n- hosts: \"{{ hosts }}\"\n  become: yes\n  tasks:\n    - name: remove file (delete file)\n      file:\n        path: \"/data/www/nginx-conf/{{ path }}\"\n        state: absent\n      notify:\n       - reloaded nginx\n    - name: get ps\n      shell: ps -ef|grep nginx|grep -v grep\n      register: psInfo\n    - name: show ps\n      debug: msg=\"{{ psInfo }}\"\n  handlers:  \n    - name: reloaded nginx\n      shell: /etc/init.d/nginx reload', '', 'vars:\n  path:', '删除nginx配置文件', '2021-09-28 17:23:33', '2021-09-28 17:23:33');
INSERT INTO `ansible_playbook` VALUES (16, '7ddba3d9948449a0a6802d0b04e2d51a', 'playbook-k8s-worker-init', '---\n- name: base\n  include: \"{{ basePath }}/k8s-worker-init.yml\"', '', 'vars:\n  basePath: /home/app/opscloud-data/ansible/playbook/tool-install', 'k8s集群工作节点初始化', '2021-11-19 17:58:37', '2021-11-19 17:58:37');
INSERT INTO `ansible_playbook` VALUES (17, '38022fae68ad441bb67f59e7327ede25', 'playbook-zabbix-agent-install', '---\n- name: zabbix agent install\n  include: \"{{ basePath }}/zabbix-agent-install.yml\"', '', 'vars:\n  basePath: /home/app/opscloud-data/ansible/playbook/tool-install\n  # hangzhou.proxy.zabbix.chuanyinet.com\n  # server.zabbix.chuanyinet.com\n  zabbixServer: server.zabbix.chuanyinet.com', '安装zabbix5-agent', '2021-12-23 17:14:32', '2021-12-28 10:10:26');
INSERT INTO `ansible_playbook` VALUES (18, '77ba4fdfaf6c476fb7710175662836a1', 'playbook-server-base-init', '---\n- name: zabbix agent install\n  include: \"{{ basePath }}/server-base-init.yml\"', '', 'vars:\n  basePath: /home/app/opscloud-data/ansible/playbook/tool-install', '服务器初始化', '2021-12-28 17:07:06', '2021-12-28 17:11:47');
COMMIT;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '应用名称',
  `application_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '应用Key',
  `application_type` int(2) DEFAULT '0' COMMENT '应用类型',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `application_key` (`application_key`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聚合应用';

-- ----------------------------
-- Records of application
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for application_resource
-- ----------------------------
DROP TABLE IF EXISTS `application_resource`;
CREATE TABLE `application_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `application_id` int(11) NOT NULL COMMENT '应用ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资源名称',
  `virtual_resource` tinyint(1) DEFAULT '0' COMMENT '虚拟资源',
  `resource_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '资源类型',
  `business_id` int(11) DEFAULT NULL COMMENT '业务ID',
  `business_type` int(2) DEFAULT NULL COMMENT '业务类型',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `application_id` (`application_id`,`resource_type`,`business_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='应用资源';

-- ----------------------------
-- Records of application_resource
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for auth_group
-- ----------------------------
DROP TABLE IF EXISTS `auth_group`;
CREATE TABLE `auth_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `base_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '基本路径',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_group
-- ----------------------------
BEGIN;
INSERT INTO `auth_group` VALUES (1, 'Auth', '/api/auth', '权限管理', '2020-02-16 00:04:55', '2021-07-13 22:45:26');
INSERT INTO `auth_group` VALUES (2, 'User', '/api/user', '用户', '2020-02-20 18:58:56', '2021-07-13 22:45:31');
INSERT INTO `auth_group` VALUES (40, 'Datasource', '/api/datasource', '数据源管理', '2021-05-15 22:44:36', '2021-07-13 22:45:37');
INSERT INTO `auth_group` VALUES (41, 'System', '', '系统管理', '2021-05-18 00:26:30', '2021-05-18 00:26:30');
INSERT INTO `auth_group` VALUES (42, 'Tag', '/api/tag', '标签', '2021-05-19 22:05:06', '2021-07-13 22:45:41');
INSERT INTO `auth_group` VALUES (43, 'ServerGroup', '/api/server/group', '服务器组', '2021-05-24 18:18:33', '2021-07-13 22:45:46');
INSERT INTO `auth_group` VALUES (44, 'Server', '/api/server', '服务器管理', '2021-05-25 18:43:59', '2021-07-13 22:45:51');
INSERT INTO `auth_group` VALUES (45, 'ServerAccount', '/api/server/account', '服务器账户管理', '2021-05-25 18:44:17', '2021-07-13 22:45:56');
INSERT INTO `auth_group` VALUES (46, 'Env', '/api/env', '环境管理', '2021-05-26 00:22:40', '2021-07-13 22:46:00');
INSERT INTO `auth_group` VALUES (47, 'Menu', '/api/sys/menu', '菜单管理', '2021-06-02 21:26:08', '2021-07-13 22:46:05');
INSERT INTO `auth_group` VALUES (48, 'Document', '/api/sys/doc', '文档管理', '2021-06-16 20:34:02', '2021-07-13 22:46:11');
INSERT INTO `auth_group` VALUES (50, 'Application', '/api/application', '应用管理', '2021-07-13 22:42:30', '2021-07-13 22:42:30');
INSERT INTO `auth_group` VALUES (52, 'TerminalSession', '/api/terminal/session', '终端会话管理', '2021-07-23 00:29:27', '2021-07-23 00:29:27');
INSERT INTO `auth_group` VALUES (53, 'Static', NULL, '前端静态资源', '2021-07-27 23:48:24', '2021-07-27 23:48:24');
INSERT INTO `auth_group` VALUES (54, 'Business', '/api/business', '业务对象管理', '2021-08-20 21:54:50', '2021-08-20 21:55:29');
INSERT INTO `auth_group` VALUES (55, 'Task', '/api/task', '任务管理', '2021-09-01 21:15:56', '2021-09-01 21:16:10');
INSERT INTO `auth_group` VALUES (56, 'Aliyun', NULL, '阿里云服务', '2021-09-17 22:05:07', '2021-09-17 22:05:07');
INSERT INTO `auth_group` VALUES (57, 'ServerTask', '/api/server/task', '服务器任务', '2021-09-19 00:06:32', '2021-09-19 00:06:32');
INSERT INTO `auth_group` VALUES (58, 'Event', '/api/receive/event', '事件', '2021-10-29 23:15:39', '2021-10-29 23:15:39');
INSERT INTO `auth_group` VALUES (59, 'Template', '/api/template', '模版管理', '2021-12-06 13:22:37', '2021-12-06 13:22:37');
INSERT INTO `auth_group` VALUES (60, 'WorkOrder', '/api/workorder', '工单', '2022-01-06 15:55:58', '2022-01-06 15:55:58');
COMMIT;

-- ----------------------------
-- Table structure for auth_menu
-- ----------------------------
DROP TABLE IF EXISTS `auth_menu`;
CREATE TABLE `auth_menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_type` int(1) NOT NULL DEFAULT '0' COMMENT '0:aside 1:header',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `menu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_menu
-- ----------------------------
BEGIN;
INSERT INTO `auth_menu` VALUES (10, 1, 0, 'super_admin', '[\n  {\n    title: \'首页\',\n    icon: \'home\',\n    children: [\n      { path: \'/dashboard\', title: \'仪表盘\', icon: \'area-chart\' },\n      { path: \'/dashboard/hot\', title: \'热门排行\', icon: \'fire\' },\n      { path: \'/dashboard/pipeline\', title: \'任务视图\', icon: \'modx\' }\n    ]\n  },\n  {\n    title: \'全局配置\',\n    icon: \'cogs\',\n    children: [\n      { path: \'/env\', title: \'环境配置\', icon: \'cog\' },\n      { path: \'/tag\', title: \'标签配置\', icon: \'tags\' },\n      { path: \'/workorder/mgmt\', title: \'工单配置\', icon: \'ticket\' },\n      { path: \'/setting/keybox\', title: \'密钥管理\', icon: \'key\' },\n      { path: \'/setting/global\', title: \'系统参数\', icon: \'cog\' }\n    ]\n  },\n  {\n    title: \'应用配置\',\n    icon: \'codepen\',\n    children: [\n      { path: \'/application\', title: \'应用\', iconSvg: \'application\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/continuous-integration\', title: \'持续集成\', icon: \'envira\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/web-terminal\', title: \'Web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'Caesar配置\',\n    icon: \'connectdevelop\',\n    children: [\n      { path: \'/caesar/instance\', title: \'实例管理\', iconSvg: \'caesar\' }\n    ]\n  },\n  {\n    title: \'Jenkins配置\',\n    icon: \'google-wallet\',\n    children: [\n      { path: \'/jenkins/instance\', title: \'实例管理\', iconSvg: \'jenkins\' },\n      { path: \'/jenkins/job/template\', title: \'任务模版\', iconSvg: \'template\' },\n      { path: \'/jenkins/job/template/version\', title: \'模版版本\', iconSvg: \'template\' }\n    ]\n  },\n  {\n    title: \'Gitlab配置\',\n    icon: \'gitlab\',\n    children: [\n      { path: \'/gitlab/instance\', title: \'实例管理\', icon: \'git\' },\n      { path: \'/gitlab/group\', title: \'群组管理\', icon: \'th-large\' },\n      { path: \'/gitlab/project\', title: \'项目管理\', icon: \'th-list\' }\n    ]\n  },\n  {\n    title: \'存储管理\',\n    icon: \'database\',\n    children: [\n      { path: \'/storage/oss\', title: \'对象存储\', iconSvg: \'oss\' }\n    ]\n  },\n  {\n    title: \'通知中心\',\n    icon: \'volume-control-phone\',\n    children: [\n      { path: \'/dingtalk\', title: \'钉钉配置\', iconSvg: \'dingtalk\' }\n    ]\n  },\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'Kubernetes\',\n    icon: \'dropbox\',\n    children: [\n      { path: \'/kubernetes/application\', title: \'应用管理\', iconSvg: \'k8s-application\' },\n      { path: \'/kubernetes/application/instance\', title: \'应用实例管理\', iconSvg: \'k8s-instance\' },\n      { path: \'/kubernetes/cluster\', title: \'集群管理\', iconSvg: \'kubernetes\' },\n      { path: \'/kubernetes/deployment\', title: \'无状态管理\', iconSvg: \'k8s-deployment\' },\n      { path: \'/kubernetes/service\', title: \'服务管理\', iconSvg: \'k8s-service\' },\n      { path: \'/kubernetes/template\', title: \'模版管理\', iconSvg: \'YAML\' }\n    ]\n  },\n  {\n    title: \'堡垒机\',\n    icon: \'empire\',\n    children: [\n      { path: \'/jump/jumpserver/settings\', title: \'Jump设置\', icon: \'cog\' },\n      { path: \'/jump/jumpserver/user\', title: \'用户管理\', icon: \'user\' },\n      { path: \'/jump/jumpserver/asset\', title: \'资产管理\', icon: \'server\' },\n      { path: \'/term/session\', title: \'Web终端会话\', icon: \'server\' } \n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/user/retired\', title: \'用户离职管理\', icon: \'user\' },\n      { path: \'/org\', title: \'组织架构\', icon:\'sitemap\'},\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible/mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/profile/subscription\', title: \'配置订阅\', iconSvg: \'subscription\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'RBAC配置\',\n    icon: \'address-card\',\n    children: [\n      { path: \'/auth/resource\', title: \'资源管理\', icon: \'modx\' },\n      { path: \'/auth/role\', title: \'角色管理\', icon: \'users\' },\n      { path: \'/auth/user/role\', title: \'用户角色配置\', icon: \'id-card\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 15:56:17', '2021-04-20 22:02:53');
INSERT INTO `auth_menu` VALUES (11, 2, 0, 'admin', '[\n  {\n    title: \'全局配置\',\n    icon: \'cogs\',\n    children: [\n      { path: \'/env\', title: \'环境配置\', icon: \'cog\' },\n      { path: \'/tag\', title: \'标签配置\', icon: \'cog\' }\n    ]\n  },\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      // tencent\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n   {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' }\n    ]\n  }, \n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'堡垒机\',\n    icon: \'empire\',\n    children: [\n      { path: \'/jump/jumpserver/settings\', title: \'设置\', icon: \'cog\' },\n      { path: \'/jump/jumpserver/user\', title: \'用户管理\', icon: \'user\' },\n      { path: \'/jump/jumpserver/asset\', title: \'资产管理\', icon: \'server\' },\n      { path: \'/term/session\', title: \'Web终端会话\', icon: \'server\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'RBAC配置\',\n    icon: \'address-card\',\n    children: [\n      { path: \'/auth/resource\', title: \'资源管理\', icon: \'modx\' },\n      { path: \'/auth/role\', title: \'角色管理\', icon: \'users\' },\n      { path: \'/auth/user/role\', title: \'用户角色配置\', icon: \'id-card\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible-mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 18:12:46', '2020-07-06 03:54:39');
INSERT INTO `auth_menu` VALUES (12, 3, 0, 'ops', '[\n  {\n    title: \'全局配置\',\n    icon: \'cogs\',\n    children: [\n      { path: \'/env\', title: \'环境配置\', icon: \'cog\' },\n      { path: \'/tag\', title: \'标签配置\', icon: \'cog\' }\n    ]\n  },\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      // tencent\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n   {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' }\n    ]\n  },\n   {\n    title: \'Kubernetes\',\n    icon: \'dropbox\',\n    children: [\n      { path: \'/kubernetes/application\', title: \'应用管理\', iconSvg: \'k8s-application\' },\n      { path: \'/kubernetes/application/instance\', title: \'应用实例管理\', iconSvg: \'k8s-instance\' },\n      { path: \'/kubernetes/cluster\', title: \'集群管理\', iconSvg: \'kubernetes\' },\n      { path: \'/kubernetes/deployment\', title: \'无状态管理\', iconSvg: \'k8s-deployment\' },\n      { path: \'/kubernetes/service\', title: \'服务管理\', iconSvg: \'k8s-service\' },\n      { path: \'/kubernetes/template\', title: \'模版管理\', iconSvg: \'YAML\' }\n    ]\n  },\n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'堡垒机\',\n    icon: \'empire\',\n    children: [\n      { path: \'/jump/jumpserver/settings\', title: \'设置\', icon: \'cog\' },\n      { path: \'/jump/jumpserver/user\', title: \'用户管理\', icon: \'user\' },\n      { path: \'/jump/jumpserver/asset\', title: \'资产管理\', icon: \'server\' },\n      { path: \'/term/session\', title: \'Web终端会话\', icon: \'server\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n   {\n    title: \'RBAC配置\',\n    icon: \'address-card\',\n    children: [\n      { path: \'/auth/resource\', title: \'资源管理\', icon: \'modx\' },\n      { path: \'/auth/role\', title: \'角色管理\', icon: \'users\' },\n      { path: \'/auth/user/role\', title: \'用户角色配置\', icon: \'id-card\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible-mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 18:14:17', '2020-07-08 00:55:27');
INSERT INTO `auth_menu` VALUES (13, 4, 0, 'dev', '[\n {\n    title: \'首页\',\n    icon: \'home\',\n    children: [\n      { path: \'/dashboard\', title: \'仪表盘\', icon: \'area-chart\' },\n      { path: \'/dashboard/hot\', title: \'热门排行\', icon: \'fire\' },\n      { path: \'/dashboard/pipeline\', title: \'流水线视图\', icon: \'modx\' }\n    ]\n  },\n {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/continuous-integration\', title: \'持续集成\', icon: \'envira\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' }\n    ]\n  },\n  {\n    title: \'应用配置\',\n    icon: \'codepen\',\n    children: [\n      { path: \'/application\', title: \'应用\', iconSvg: \'application\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 18:17:22', '2021-04-06 18:27:06');
INSERT INTO `auth_menu` VALUES (14, 5, 0, 'base', '[\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 18:18:04', '2020-04-23 18:18:04');
INSERT INTO `auth_menu` VALUES (15, 13, 0, 'aliyun_log_admin', '[\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n  {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' },\n      { path: \'/cloud/aliyun/log\', title: \'日志服务管理\', iconSvg: \'aliyun-log\' }\n    ]\n  },\n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', icon: \'object-ungroup\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible-mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/ansible\', title: \'playbook\', icon: \'recycle\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-06-16 00:45:51', '2020-06-16 00:46:32');
INSERT INTO `auth_menu` VALUES (16, 15, 0, 'pangu', '', '2020-07-10 18:36:38', '2020-07-10 18:36:38');
COMMIT;

-- ----------------------------
-- Table structure for auth_resource
-- ----------------------------
DROP TABLE IF EXISTS `auth_resource`;
CREATE TABLE `auth_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL COMMENT '资源组ID',
  `resource_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资源名称',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `need_auth` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否鉴权',
  `ui` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户界面',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `resource_name` (`resource_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=564 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_resource
-- ----------------------------
BEGIN;
INSERT INTO `auth_resource` VALUES (1, 1, '/api/log/login', '用户登录接口', 0, 0, '2020-02-19 01:39:09', '2021-07-01 01:24:51');
INSERT INTO `auth_resource` VALUES (2, 1, '/api/auth/role/page/query', '角色分页查询', 1, 0, '2020-02-14 17:40:38', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (3, 1, '/api/auth/role/add', '新增角色', 1, 0, '2020-02-15 03:27:57', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (4, 1, '/api/auth/role/update', '更新角色配置', 1, 0, '2020-02-15 03:28:11', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (5, 1, '/api/auth/role/del', '删除角色', 1, 0, '2020-02-15 03:28:36', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (6, 1, '/api/auth/resource/page/query', '资源分页查询', 1, 0, '2020-02-15 05:11:59', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (7, 1, '/api/auth/resource/add', '新增资源', 1, 1, '2020-02-15 05:12:14', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (8, 1, '/api/auth/resource/update', '更新资源配置', 1, 1, '2020-02-15 05:12:23', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (9, 1, '/api/auth/resource/del', '删除资源', 1, 0, '2020-02-15 05:12:33', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (10, 1, '/api/auth/group/page/query', '资源组分页查询', 1, 0, '2020-02-16 00:33:30', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (15, 1, '/api/auth/group/add', '新增资源组', 1, 0, '2020-02-18 20:57:54', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (16, 1, '/api/auth/group/del', '删除资源组', 1, 0, '2020-02-18 20:58:03', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (17, 1, '/api/auth/group/update', '更新资源组', 1, 0, '2020-02-18 20:58:27', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (27, 1, '/api/auth/role/resource/bind/page/query', '分页查询角色绑定的资源列表', 1, 0, '2020-02-19 19:08:55', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (29, 1, '/api/auth/role/resource/add', '角色资源关联', 1, 0, '2020-02-20 02:04:14', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (30, 1, '/api/auth/role/resource/del', '角色资源删除', 1, 0, '2020-02-20 02:04:22', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (31, 1, '/api/auth/user/role/page/query', '用户角色分页查询', 1, 0, '2020-02-20 17:36:25', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (32, 2, '/api/user/page/query', '用户查询接口', 1, 0, '2020-02-20 18:59:30', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (33, 1, '/api/auth/user/role/add', '新增用户角色配置', 1, 0, '2020-02-20 19:33:40', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (34, 1, '/api/auth/user/role/del', '删除用户角色配置', 1, 0, '2020-02-20 19:34:01', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (165, 1, '/api/auth/menu/query', '用户菜单查询', 1, 0, '2020-04-23 16:41:41', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (188, 1, '/api/auth/role/menu/save', '保存角色菜单配置', 1, 1, '2020-05-19 17:47:21', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (189, 1, '/api/auth/role/menu/query', '查询角色菜单配置', 1, 0, '2020-05-19 17:47:40', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (236, 1, '/api/home', '首页', 0, 0, '2020-06-17 02:23:05', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (386, 1, '/api/auth/token/revoke', '吊销所有用户令牌', 1, 0, '2021-04-19 20:59:19', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (389, 40, '/api/datasource/config/page/query', '分页查询数据源配置', 1, 0, '2021-05-15 22:45:34', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (390, 40, '/api/datasource/config/add', '新增数据源配置', 1, 0, '2021-05-17 18:31:50', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (391, 40, '/api/datasource/config/update', '更新数据源配置', 1, 0, '2021-05-17 18:32:11', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (392, 41, '/api/sys/credential/kind/options/get', '查询系统凭据分类选项', 1, 0, '2021-05-18 00:27:17', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (393, 41, '/api/sys/credential/page/query', '分页查询系统凭据列表', 1, 0, '2021-05-18 00:45:36', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (394, 41, '/api/sys/credential/add', '新增系统凭据配置', 1, 0, '2021-05-18 18:08:17', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (395, 41, '/api/sys/credential/update', '更新系统凭据配置', 1, 0, '2021-05-18 18:08:30', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (396, 42, '/api/tag/page/query', '分页查询标签列表', 1, 0, '2021-05-19 22:05:44', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (397, 42, '/api/tag/add', '新增标签信息', 1, 0, '2021-05-19 22:06:00', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (398, 42, '/api/tag/update', '更新标签信息', 1, 0, '2021-05-19 22:06:11', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (399, 42, '/api/tag/business/options/get', '查询业务类型选项', 1, 0, '2021-05-19 22:36:29', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (400, 40, '/api/datasource/instance/register', '注册数据源实例', 1, 0, '2021-05-20 01:12:34', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (401, 40, '/api/datasource/instance/query', '查询数据源实例', 1, 0, '2021-05-20 16:32:40', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (402, 42, '/api/tag/business/type/get', '按类型查询所有标签', 1, 0, '2021-05-20 19:52:05', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (404, 42, '/api/tag/business/update', '更新业务标签', 1, 0, '2021-05-20 20:32:37', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (405, 40, '/api/datasource/config/type/options/get', '查询数据源配置类型选项', 1, 0, '2021-05-21 17:36:20', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (406, 43, '/api/server/group/type/page/query', '分页查询服务器组类型列表', 1, 0, '2021-05-24 18:19:04', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (407, 43, '/api/server/group/type/add', '新增服务器组类型', 1, 0, '2021-05-24 18:19:33', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (408, 43, '/api/server/group/type/update', '更新服务器组类型', 1, 0, '2021-05-24 18:19:53', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (409, 43, '/api/server/group/type/del', '删除指定的服务器组类型', 1, 0, '2021-05-24 18:20:06', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (410, 43, '/api/server/group/page/query', '分页查询服务器组列表', 1, 0, '2021-05-24 21:31:11', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (411, 43, '/api/server/group/add', '新增服务器组', 1, 0, '2021-05-24 21:31:38', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (412, 43, '/api/server/group/update', '更新服务器组', 1, 0, '2021-05-24 22:41:51', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (413, 45, '/api/server/account/page/query', '分页查询服务器账户列表', 1, 0, '2021-05-25 18:44:49', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (414, 44, '/api/server/protocol/options/get', '查询服务器协议选项', 1, 0, '2021-05-25 20:40:14', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (415, 45, '/api/server/account/add', '新增服务器账户', 1, 0, '2021-05-25 21:31:29', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (416, 45, '/api/server/account/update', '更新服务器账户', 1, 0, '2021-05-25 21:31:38', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (417, 46, '/api/sys/env/page/query', '分页查询环境列表', 1, 0, '2021-05-26 00:23:17', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (418, 46, '/api/sys/env/add', '新增环境', 1, 0, '2021-05-26 00:23:30', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (419, 46, '/api/sys/env/update', '更新环境', 1, 0, '2021-05-26 00:23:40', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (420, 46, '/api/sys/env/del', '删除指定的环境', 1, 0, '2021-05-26 00:23:56', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (421, 44, '/api/server/page/query', '分页查询服务器列表', 1, 0, '2021-05-26 01:02:07', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (422, 44, '/api/server/add', '新增服务器', 1, 0, '2021-05-26 17:01:46', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (423, 44, '/api/server/update', '更新服务器', 1, 0, '2021-05-26 17:02:00', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (424, 44, '/api/server/del', '删除指定的服务器', 1, 0, '2021-05-26 17:02:19', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (425, 45, '/api/server/account/permission/update', '更新服务器账户授权', 1, 0, '2021-05-26 22:00:11', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (426, 2, '/api/user/add', '新增用户', 1, 0, '2021-05-27 16:21:29', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (427, 2, '/api/user/update', '更新用户', 1, 0, '2021-05-27 16:21:44', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (428, 2, '/api/user/del', '删除指定的用户', 1, 0, '2021-05-27 16:21:59', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (430, 2, '/api/user/business/permission/revoke', '解除用户业务许可', 1, 0, '2021-05-27 21:49:07', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (431, 2, '/api/user/business/permission/grant', '授予用户业务许可', 1, 0, '2021-05-27 21:56:53', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (433, 2, '/api/user/business/permission/set', '设置用户业务许可（角色）', 1, 0, '2021-05-28 00:36:45', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (434, 2, '/api/user/server/tree/query', '查询用户的服务器树', 1, 0, '2021-05-28 21:43:33', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (435, 1, '/api/auth/user/role/update', '更新用户角色', 1, 0, '2021-06-02 00:43:28', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (436, 47, '/api/sys/menu/save', '保存菜单', 1, 0, '2021-06-02 21:26:45', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (437, 47, '/api/sys/menu/child/save', '保存子菜单', 1, 0, '2021-06-02 21:26:55', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (438, 47, '/api/sys/menu/del', '删除菜单', 1, 0, '2021-06-02 21:27:15', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (439, 47, '/api/sys/menu/child/del', '删除子菜单', 1, 0, '2021-06-02 21:27:25', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (440, 47, '/api/sys/menu/query', '查询菜单', 1, 0, '2021-06-02 21:27:35', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (441, 47, '/api/sys/menu/child/query', '查询子菜单', 1, 0, '2021-06-02 21:27:53', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (442, 47, '/api/sys/menu/tree/query', '查询菜单树', 1, 0, '2021-06-03 22:23:45', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (443, 47, '/api/sys/menu/role/save', '保存角色菜单', 1, 0, '2021-06-03 22:23:56', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (444, 47, '/api/sys/menu/role/query', '查询角色菜单', 1, 0, '2021-06-03 22:24:08', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (445, 47, '/api/sys/menu/role/detail/query', '查询角色菜单详情', 1, 0, '2021-06-03 22:24:18', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (446, 2, '/api/user/ui/info/get', '查询用户前端界面信息(菜单&UI鉴权)', 1, 0, '2021-06-07 16:35:52', '2021-08-05 01:07:27');
INSERT INTO `auth_resource` VALUES (447, 2, '/api/user/details/get', '查询用户详情', 1, 0, '2021-06-09 18:12:46', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (448, 2, '/api/user/credential/save', '保存用户凭据', 1, 0, '2021-06-09 23:06:22', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (453, 48, '/api/sys/doc/preview', '查阅文档', 1, 0, '2021-06-16 20:34:34', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (454, 2, '/api/user/group/page/query', '分页查询用户组列表', 1, 0, '2021-06-16 23:03:20', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (455, 2, '/api/user/business/permission/query', '分页查询用户授权业务对象列表', 1, 0, '2021-06-17 00:10:01', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (456, 40, '/api/datasource/instance/asset/page/query', '分页查询数据源资产列表', 1, 0, '2021-06-19 00:57:30', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (458, 40, '/api/datasource/instance/asset/pull', '拉取数据源资产信息', 1, 0, '2021-06-21 20:27:23', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (459, 40, '/api/datasource/instance/asset/del', '删除指定的资产', 1, 0, '2021-06-22 20:05:46', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (460, 40, '/api/datasource/instance/asset/set/config', '设置数据源配置文件', 1, 0, '2021-06-30 00:20:08', '2021-07-01 01:25:14');
INSERT INTO `auth_resource` VALUES (462, 2, '/api/user/server/remote/query', '查询用户授权的远程服务器', 1, 0, '2021-07-09 23:09:38', '2021-07-09 23:09:38');
INSERT INTO `auth_resource` VALUES (463, 50, '/api/application/kubernetes/page/query', '分页查询容器应用列表', 1, 0, '2021-07-14 00:01:06', '2021-07-14 00:01:06');
INSERT INTO `auth_resource` VALUES (464, 50, '/api/application/page/query', '分页查询应用列表', 1, 0, '2021-07-15 22:49:33', '2021-07-15 22:49:33');
INSERT INTO `auth_resource` VALUES (465, 50, '/api/application/get', '查询应用', 1, 0, '2021-07-16 22:17:04', '2021-08-19 21:51:26');
INSERT INTO `auth_resource` VALUES (466, 50, '/api/application/add', '新增应用', 1, 0, '2021-07-16 22:17:13', '2021-07-16 22:17:13');
INSERT INTO `auth_resource` VALUES (467, 50, '/api/application/update', '更新应用', 1, 0, '2021-07-16 22:17:22', '2021-07-16 22:17:22');
INSERT INTO `auth_resource` VALUES (468, 50, '/api/application/del', '删除应用', 1, 0, '2021-07-16 22:17:28', '2021-07-16 22:17:28');
INSERT INTO `auth_resource` VALUES (469, 50, '/api/application/res/bind', '应用资源绑定', 1, 0, '2021-07-16 22:17:55', '2021-07-16 22:17:55');
INSERT INTO `auth_resource` VALUES (470, 50, '/api/application/res/unbind', '应用资源解除绑定', 1, 0, '2021-07-16 22:18:10', '2021-07-16 22:18:10');
INSERT INTO `auth_resource` VALUES (472, 50, '/api/application/business/options/get', '查询应用业务类型选项', 1, 0, '2021-07-19 22:33:59', '2021-07-19 22:33:59');
INSERT INTO `auth_resource` VALUES (473, 52, '/api/terminal/session/page/query', '分页查询终端会话列表', 1, 0, '2021-07-23 00:30:36', '2021-07-23 00:30:36');
INSERT INTO `auth_resource` VALUES (474, 53, '/', '首页', 0, 0, '2021-07-27 23:48:46', '2021-07-27 23:48:46');
INSERT INTO `auth_resource` VALUES (475, 40, '/api/datasource/instance/asset/business/scan', '扫描资产与业务对象关系', 1, 0, '2021-08-03 00:10:29', '2021-08-03 00:15:54');
INSERT INTO `auth_resource` VALUES (476, 52, '/api/terminal/session/instance/command/page/query', '分页查询终端会话命令列表', 1, 0, '2021-08-03 17:17:11', '2021-08-03 17:17:11');
INSERT INTO `auth_resource` VALUES (477, 2, '/api/user/access/token/grant', '授权用户AccessToken', 1, 0, '2021-08-05 21:23:29', '2021-08-05 21:23:29');
INSERT INTO `auth_resource` VALUES (478, 2, '/api/user/access/token/revoke', '撤销用户AccessToken', 1, 0, '2021-08-05 21:23:42', '2021-08-05 21:23:42');
INSERT INTO `auth_resource` VALUES (479, 2, '/api/user/group/add', '新增用户组', 1, 0, '2021-08-07 00:16:54', '2021-08-07 00:16:54');
INSERT INTO `auth_resource` VALUES (481, 2, '/api/user/group/update', '更新用户组', 1, 0, '2021-08-07 00:17:17', '2021-08-07 00:17:17');
INSERT INTO `auth_resource` VALUES (482, 2, '/api/user/group/del', '删除用户组', 1, 0, '2021-08-09 18:07:42', '2021-08-09 18:07:42');
INSERT INTO `auth_resource` VALUES (483, 2, '/api/user/permission/business/query', '分页查询业务对象授权的用户列表', 1, 0, '2021-08-11 01:07:01', '2021-08-11 01:07:01');
INSERT INTO `auth_resource` VALUES (484, 54, '/api/business/property/add', '新增业务对象属性配置', 1, 0, '2021-08-20 21:55:47', '2021-08-20 21:55:47');
INSERT INTO `auth_resource` VALUES (485, 54, '/api/business/property/update', '更新业务对象属性配置', 1, 0, '2021-08-20 21:55:59', '2021-08-20 21:55:59');
INSERT INTO `auth_resource` VALUES (486, 43, '/api/server/group/del', '删除服务器组配置', 1, 0, '2021-08-25 17:16:08', '2021-08-25 17:16:08');
INSERT INTO `auth_resource` VALUES (487, 40, '/api/datasource/instance/asset/subscription/page/query', '分页查询数据源资产订阅列表', 1, 0, '2021-08-27 23:53:21', '2021-08-27 23:53:21');
INSERT INTO `auth_resource` VALUES (488, 40, '/api/datasource/instance/asset/subscription/add', '新增数据源资产订阅', 1, 0, '2021-08-30 23:21:02', '2021-08-30 23:21:02');
INSERT INTO `auth_resource` VALUES (489, 40, '/api/datasource/instance/asset/subscription/update', '更新数据源资产订阅', 1, 0, '2021-08-30 23:21:17', '2021-08-30 23:21:17');
INSERT INTO `auth_resource` VALUES (490, 40, '/api/datasource/instance/asset/subscription/del', '删除指定的数据源资产订阅', 1, 0, '2021-08-30 23:21:36', '2021-08-30 23:21:36');
INSERT INTO `auth_resource` VALUES (491, 40, '/api/datasource/instance/asset/subscription/publish', '发布数据源资产订阅', 1, 0, '2021-08-31 22:38:50', '2021-08-31 22:38:50');
INSERT INTO `auth_resource` VALUES (492, 55, '/api/task/ansible/playbook/page/query', '分页查询剧本列表', 1, 0, '2021-09-01 21:16:47', '2021-09-01 21:16:47');
INSERT INTO `auth_resource` VALUES (493, 55, '/api/task/ansible/playbook/add', '新增剧本配置', 1, 0, '2021-09-02 01:20:17', '2021-09-02 01:20:17');
INSERT INTO `auth_resource` VALUES (494, 55, '/api/task/ansible/playbook/update', '更新剧本配置', 1, 0, '2021-09-02 01:20:32', '2021-09-02 01:20:32');
INSERT INTO `auth_resource` VALUES (495, 55, '/api/task/ansible/playbook/del', '删除剧本配置', 1, 0, '2021-09-02 01:20:46', '2021-09-02 01:20:46');
INSERT INTO `auth_resource` VALUES (496, 41, '/api/instance/registered/page/query', '分页查询注册实例列表', 1, 0, '2021-09-06 18:23:45', '2021-09-06 18:23:45');
INSERT INTO `auth_resource` VALUES (497, 41, '/api/instance/registered/active/set', '设置注册实例的有效/无效', 1, 0, '2021-09-06 22:28:03', '2021-09-06 22:28:41');
INSERT INTO `auth_resource` VALUES (499, 41, '/api/instance/health/lb-check', '负载均衡健康检查接口', 0, 0, '2021-09-06 23:17:32', '2021-09-06 23:20:36');
INSERT INTO `auth_resource` VALUES (500, 50, '/api/application/res/preview/page/query', '预览应用资源', 1, 0, '2021-09-09 16:48:40', '2021-09-09 16:48:40');
INSERT INTO `auth_resource` VALUES (501, 2, '/api/user/sync', '同步用户与用户关系（数据源实例）', 1, 0, '2021-09-16 17:45:44', '2021-09-16 17:45:44');
INSERT INTO `auth_resource` VALUES (502, 56, '/api/datasource/aliyun/log/page/query', '分页查询阿里云日志服务列表', 1, 0, '2021-09-17 22:05:33', '2021-09-17 22:05:33');
INSERT INTO `auth_resource` VALUES (503, 56, '/api/datasource/aliyun/log/del', '删除阿里云日志服务', 1, 0, '2021-09-17 22:09:06', '2021-09-17 22:09:23');
INSERT INTO `auth_resource` VALUES (504, 56, '/api/datasource/aliyun/log/project/query', '查询阿里云日志服务项目列表', 1, 0, '2021-09-17 22:34:03', '2021-09-17 22:34:03');
INSERT INTO `auth_resource` VALUES (505, 56, '/api/datasource/aliyun/log/logstore/query', '查询阿里云日志服务日志库列表', 1, 0, '2021-09-17 22:34:20', '2021-09-17 22:34:20');
INSERT INTO `auth_resource` VALUES (506, 56, '/api/datasource/aliyun/log/config/query', '查询阿里云日志服务配置列表', 1, 0, '2021-09-17 22:34:34', '2021-09-17 22:34:34');
INSERT INTO `auth_resource` VALUES (507, 56, '/api/datasource/aliyun/log/add', '新增阿里云日志服务', 1, 0, '2021-09-17 22:52:49', '2021-09-17 22:52:49');
INSERT INTO `auth_resource` VALUES (508, 56, '/api/datasource/aliyun/log/update', '更新阿里云日志服务', 1, 0, '2021-09-17 22:53:05', '2021-09-17 22:53:05');
INSERT INTO `auth_resource` VALUES (509, 56, '/api/datasource/aliyun/log/member/page/query', '分页查询阿里云日志服务成员列表', 1, 0, '2021-09-17 23:57:57', '2021-09-17 23:57:57');
INSERT INTO `auth_resource` VALUES (510, 56, '/api/datasource/aliyun/log/member/add', '新增阿里云日志服务成员', 1, 0, '2021-09-17 23:58:10', '2021-09-17 23:58:10');
INSERT INTO `auth_resource` VALUES (511, 56, '/api/datasource/aliyun/log/member/update', '更新阿里云日志服务成员', 1, 0, '2021-09-17 23:58:22', '2021-09-17 23:58:22');
INSERT INTO `auth_resource` VALUES (512, 56, '/api/datasource/aliyun/log/member/del', '删除指定的阿里云日志服务成员', 1, 0, '2021-09-17 23:58:36', '2021-09-17 23:58:36');
INSERT INTO `auth_resource` VALUES (513, 56, '/api/datasource/aliyun/log/push', '推送阿里云日志服务配置', 1, 0, '2021-09-18 00:38:31', '2021-09-18 00:38:31');
INSERT INTO `auth_resource` VALUES (514, 56, '/api/datasource/aliyun/log/member/push', '推送阿里云日志服务成员配置', 1, 0, '2021-09-18 00:38:44', '2021-09-18 00:38:44');
INSERT INTO `auth_resource` VALUES (515, 57, '/api/server/task/submit', '提交服务器任务', 1, 0, '2021-09-19 00:06:48', '2021-09-19 00:06:48');
INSERT INTO `auth_resource` VALUES (516, 57, '/api/server/task/page/query', '分页查询服务器任务列表', 1, 0, '2021-09-24 21:50:12', '2021-09-24 21:51:26');
INSERT INTO `auth_resource` VALUES (517, 43, '/api/server/group/env/pattern/query', '查询服务器组环境分组信息(发布平台专用)', 1, 0, '2021-10-25 23:37:26', '2021-10-25 23:37:26');
INSERT INTO `auth_resource` VALUES (518, 41, '/api/sys/credential/del', '删除指定的系统凭据配置', 1, 0, '2021-10-27 20:40:54', '2021-10-27 20:40:54');
INSERT INTO `auth_resource` VALUES (519, 42, '/api/tag/del', '删除指定的标签信息', 1, 0, '2021-10-28 17:12:17', '2021-10-28 17:12:17');
INSERT INTO `auth_resource` VALUES (520, 58, '/api/receive/event/gitlab/v4/system/hooks', 'Gitlab(API:v4)通知接口', 0, 0, '2021-10-29 23:16:08', '2021-10-29 23:16:08');
INSERT INTO `auth_resource` VALUES (522, 1, '/api/log/logout', '用户登出', 1, 0, '2021-11-15 14:03:40', '2021-11-15 14:03:40');
INSERT INTO `auth_resource` VALUES (523, 2, '/api/user/active/set', '设置用户是否有效', 1, 0, '2021-11-23 14:29:59', '2021-11-23 14:29:59');
INSERT INTO `auth_resource` VALUES (524, 59, '/api/template/page/query', '分页查询模版列表', 1, 0, '2021-12-06 13:23:07', '2021-12-06 13:23:07');
INSERT INTO `auth_resource` VALUES (525, 59, '/api/template/business/page/query', '分页查询业务模版列表', 1, 0, '2021-12-06 13:23:26', '2021-12-06 13:23:26');
INSERT INTO `auth_resource` VALUES (526, 59, '/api/template/business/add', '新增业务模版', 1, 0, '2021-12-06 17:07:40', '2021-12-06 17:07:40');
INSERT INTO `auth_resource` VALUES (527, 59, '/api/template/business/update', '更新业务模版', 1, 0, '2021-12-07 09:33:41', '2021-12-07 09:33:41');
INSERT INTO `auth_resource` VALUES (528, 59, '/api/template/business/asset/create', '业务模版创建资产', 1, 0, '2021-12-07 14:14:11', '2021-12-07 14:17:40');
INSERT INTO `auth_resource` VALUES (529, 59, '/api/template/business/del', '删除指定的业务模板', 1, 0, '2021-12-07 18:54:32', '2021-12-07 18:54:32');
INSERT INTO `auth_resource` VALUES (530, 59, '/api/template/add', '新增模板', 1, 0, '2021-12-08 15:11:55', '2021-12-08 15:11:55');
INSERT INTO `auth_resource` VALUES (531, 59, '/api/template/update', '更新模板', 1, 0, '2021-12-08 15:12:07', '2021-12-08 15:12:07');
INSERT INTO `auth_resource` VALUES (532, 59, '/api/template/del', '删除指定的模板', 1, 0, '2021-12-08 15:12:18', '2021-12-08 15:12:18');
INSERT INTO `auth_resource` VALUES (533, 59, '/api/template/business/scan', '扫描业务模板与业务对象的关联关系', 1, 0, '2021-12-08 17:36:05', '2021-12-08 17:36:05');
INSERT INTO `auth_resource` VALUES (534, 2, '/api/user/am/get', '查询用户AM授权信息', 1, 0, '2021-12-10 16:11:08', '2022-02-08 16:57:56');
INSERT INTO `auth_resource` VALUES (535, 2, '/api/user/am/user/create', '创建AM账户', 1, 0, '2021-12-13 11:49:00', '2022-02-11 09:48:26');
INSERT INTO `auth_resource` VALUES (536, 2, '/api/user/details/username/get', '查询用户详情', 1, 0, '2021-12-13 15:06:53', '2021-12-13 15:06:53');
INSERT INTO `auth_resource` VALUES (537, 2, '/api/user/am/policy/grant', '授权AM账户策略', 1, 0, '2021-12-13 17:15:29', '2022-02-11 09:48:39');
INSERT INTO `auth_resource` VALUES (538, 2, '/api/user/am/policy/revoke', '撤销AM账户策略', 1, 0, '2021-12-13 17:15:57', '2022-02-11 09:48:48');
INSERT INTO `auth_resource` VALUES (539, 40, '/api/datasource/instance/asset/active/set', '设置数据源资产是否有效', 1, 0, '2021-12-14 15:30:38', '2021-12-14 15:30:38');
INSERT INTO `auth_resource` VALUES (540, 40, '/api/datasource/instance/asset/push', '推送数据源资产信息', 1, 0, '2021-12-17 10:24:32', '2021-12-17 10:24:32');
INSERT INTO `auth_resource` VALUES (541, 40, '/api/datasource/instance/id/query', 'id查询数据源实例', 1, 0, '2021-12-20 15:49:21', '2021-12-20 15:49:21');
INSERT INTO `auth_resource` VALUES (542, 60, '/api/workorder/view/get', '查询工单视图', 1, 0, '2022-01-06 15:56:22', '2022-01-06 15:56:22');
INSERT INTO `auth_resource` VALUES (543, 60, '/api/workorder/ticket/entry/query', '查询工单票据条目', 1, 0, '2022-01-12 15:35:17', '2022-01-12 15:35:17');
INSERT INTO `auth_resource` VALUES (544, 60, '/api/workorder/ticket/create', '创建工单', 1, 0, '2022-01-12 18:07:06', '2022-01-12 18:07:06');
INSERT INTO `auth_resource` VALUES (545, 60, '/api/workorder/ticket/entry/add', '新增工单票据配置条目', 1, 0, '2022-01-13 09:56:59', '2022-01-13 09:56:59');
INSERT INTO `auth_resource` VALUES (546, 60, '/api/workorder/ticket/view/get', '查询工单票据视图', 1, 0, '2022-01-13 11:33:03', '2022-01-13 11:33:03');
INSERT INTO `auth_resource` VALUES (547, 60, '/api/workorder/ticket/entry/del', '删除工单票据条目', 1, 0, '2022-01-13 14:26:31', '2022-01-13 14:26:31');
INSERT INTO `auth_resource` VALUES (548, 60, '/api/workorder/ticket/save', '保存(暂存)工单票据', 1, 0, '2022-01-17 11:45:52', '2022-01-17 11:45:52');
INSERT INTO `auth_resource` VALUES (549, 60, '/api/workorder/ticket/submit', '提交工单票据', 1, 0, '2022-01-17 16:29:07', '2022-01-17 16:29:07');
INSERT INTO `auth_resource` VALUES (550, 60, '/api/workorder/ticket/my/page/query', '分页查询我的工单票据', 1, 0, '2022-01-18 11:30:57', '2022-01-18 11:30:57');
INSERT INTO `auth_resource` VALUES (551, 60, '/api/workorder/ticket/approve', '审批工单', 1, 0, '2022-01-19 11:28:42', '2022-01-19 11:28:42');
INSERT INTO `auth_resource` VALUES (552, 60, '/api/workorder/ticket/entries/get', '查询工单所有配置条目', 1, 0, '2022-01-20 13:33:01', '2022-01-20 13:33:01');
INSERT INTO `auth_resource` VALUES (553, 60, '/api/workorder/ticket/entry/update', '更新工单票据配置条目', 1, 0, '2022-01-24 16:14:23', '2022-01-24 16:14:23');
INSERT INTO `auth_resource` VALUES (554, 60, '/api/workorder/ticket/del', '删除工单票据（仅管理员使用）', 1, 0, '2022-01-26 09:33:56', '2022-01-26 09:33:56');
INSERT INTO `auth_resource` VALUES (555, 60, '/api/workorder/ticket/page/query', '管理员查询工单票据', 1, 0, '2022-01-26 11:45:14', '2022-01-26 11:45:14');
INSERT INTO `auth_resource` VALUES (556, 60, '/api/workorder/page/query', '分页查询工单配置', 1, 0, '2022-02-08 09:31:41', '2022-02-08 09:31:41');
INSERT INTO `auth_resource` VALUES (557, 60, '/api/workorder/group/page/query', '分页查询工单组配置', 1, 0, '2022-02-08 09:31:59', '2022-02-08 09:31:59');
INSERT INTO `auth_resource` VALUES (558, 60, '/api/workorder/group/save', '保存工单组配置', 1, 0, '2022-02-08 16:28:29', '2022-02-08 16:28:29');
INSERT INTO `auth_resource` VALUES (559, 60, '/api/workorder/group/del', '删除工单组配置', 1, 0, '2022-02-08 16:28:40', '2022-02-08 16:28:40');
INSERT INTO `auth_resource` VALUES (560, 60, '/api/workorder/update', '更新工单配置', 1, 0, '2022-02-08 19:34:42', '2022-02-08 19:34:42');
INSERT INTO `auth_resource` VALUES (561, 60, '/api/workorder/report/name', '工单报表(按名称汇总)', 1, 0, '2022-02-14 11:47:22', '2022-02-14 11:47:22');
INSERT INTO `auth_resource` VALUES (562, 60, '/api/workorder/report/month', '工单报表(按月份汇总)', 1, 0, '2022-02-14 15:15:27', '2022-02-14 15:15:27');
INSERT INTO `auth_resource` VALUES (563, 40, '/api/datasource/config/id/query', 'id查询数据源配置', 1, 0, '2022-02-15 15:42:29', NULL);
COMMIT;

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `access_level` int(11) NOT NULL DEFAULT '0' COMMENT '访问级别',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色描述',
  `allow_order` tinyint(1) NOT NULL DEFAULT '0' COMMENT '允许工单申请',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `role_name` (`role_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_role
-- ----------------------------
BEGIN;
INSERT INTO `auth_role` VALUES (1, 'super_admin', 100, '超级管理员[OC开发者]', 0, '2020-02-18 20:00:50', '2020-06-13 22:55:10');
INSERT INTO `auth_role` VALUES (2, 'admin', 90, '管理员', 0, '2020-02-15 00:05:06', '2020-04-11 09:10:37');
INSERT INTO `auth_role` VALUES (3, 'ops', 50, '运维', 0, '2020-02-15 00:05:16', '2021-11-08 11:22:02');
INSERT INTO `auth_role` VALUES (4, 'dev', 40, '研发', 1, '2020-02-15 00:05:25', '2020-04-11 09:10:43');
INSERT INTO `auth_role` VALUES (5, 'base', 10, '普通用户', 1, '2020-02-15 00:05:31', '2021-05-12 17:24:30');
COMMIT;

-- ----------------------------
-- Table structure for auth_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_menu`;
CREATE TABLE `auth_role_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_child_id` int(11) NOT NULL COMMENT '子菜单ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=619 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `auth_role_menu` VALUES (105, 2, 1, '2021-06-08 01:20:30', '2021-06-08 01:20:30');
INSERT INTO `auth_role_menu` VALUES (106, 2, 2, '2021-06-08 01:20:30', '2021-06-08 01:20:30');
INSERT INTO `auth_role_menu` VALUES (107, 2, 4, '2021-06-08 01:20:30', '2021-06-08 01:20:30');
INSERT INTO `auth_role_menu` VALUES (108, 2, 6, '2021-06-08 01:20:30', '2021-06-08 01:20:30');
INSERT INTO `auth_role_menu` VALUES (109, 2, 7, '2021-06-08 01:20:30', '2021-06-08 01:20:30');
INSERT INTO `auth_role_menu` VALUES (110, 2, 8, '2021-06-08 01:20:30', '2021-06-08 01:20:30');
INSERT INTO `auth_role_menu` VALUES (111, 2, 9, '2021-06-08 01:20:30', '2021-06-08 01:20:30');
INSERT INTO `auth_role_menu` VALUES (112, 2, 10, '2021-06-08 01:20:30', '2021-06-08 01:20:30');
INSERT INTO `auth_role_menu` VALUES (113, 2, 11, '2021-06-08 01:20:30', '2021-06-08 01:20:30');
INSERT INTO `auth_role_menu` VALUES (220, 4, 16, '2021-07-20 17:55:05', '2021-07-20 17:55:05');
INSERT INTO `auth_role_menu` VALUES (221, 4, 6, '2021-07-20 17:55:05', '2021-07-20 17:55:05');
INSERT INTO `auth_role_menu` VALUES (222, 4, 19, '2021-07-20 17:55:05', '2021-07-20 17:55:05');
INSERT INTO `auth_role_menu` VALUES (223, 4, 7, '2021-07-20 17:55:05', '2021-07-20 17:55:05');
INSERT INTO `auth_role_menu` VALUES (224, 4, 8, '2021-07-20 17:55:05', '2021-07-20 17:55:05');
INSERT INTO `auth_role_menu` VALUES (225, 4, 9, '2021-07-20 17:55:05', '2021-07-20 17:55:05');
INSERT INTO `auth_role_menu` VALUES (226, 4, 10, '2021-07-20 17:55:05', '2021-07-20 17:55:05');
INSERT INTO `auth_role_menu` VALUES (227, 4, 15, '2021-07-20 17:55:05', '2021-07-20 17:55:05');
INSERT INTO `auth_role_menu` VALUES (589, 3, 28, '2022-01-21 09:38:23', '2022-01-21 09:38:23');
INSERT INTO `auth_role_menu` VALUES (590, 5, 28, '2022-01-24 15:17:11', '2022-01-24 15:17:11');
INSERT INTO `auth_role_menu` VALUES (591, 5, 15, '2022-01-24 15:17:11', '2022-01-24 15:17:11');
INSERT INTO `auth_role_menu` VALUES (592, 1, 1, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (593, 1, 2, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (594, 1, 3, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (595, 1, 4, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (596, 1, 21, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (597, 1, 24, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (598, 1, 29, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (599, 1, 16, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (600, 1, 6, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (601, 1, 17, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (602, 1, 19, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (603, 1, 28, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (604, 1, 20, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (605, 1, 7, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (606, 1, 26, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (607, 1, 27, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (608, 1, 8, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (609, 1, 9, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (610, 1, 23, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (611, 1, 10, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (612, 1, 11, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (613, 1, 25, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (614, 1, 22, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (615, 1, 12, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (616, 1, 14, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (617, 1, 13, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
INSERT INTO `auth_role_menu` VALUES (618, 1, 15, '2022-01-26 10:01:33', '2022-01-26 10:01:33');
COMMIT;

-- ----------------------------
-- Table structure for auth_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_resource`;
CREATE TABLE `auth_role_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `resource_id` int(11) NOT NULL COMMENT '资源ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `role_id` (`role_id`,`resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=267 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_role_resource
-- ----------------------------
BEGIN;
INSERT INTO `auth_role_resource` VALUES (2, 1, 2, '2021-05-15 21:14:59', '2021-05-15 21:14:59');
INSERT INTO `auth_role_resource` VALUES (3, 1, 10, '2021-05-15 21:14:59', '2021-05-15 21:14:59');
INSERT INTO `auth_role_resource` VALUES (4, 1, 6, '2021-05-15 21:17:03', '2021-05-15 21:17:03');
INSERT INTO `auth_role_resource` VALUES (5, 1, 32, '2021-05-15 21:17:13', '2021-05-15 21:17:13');
INSERT INTO `auth_role_resource` VALUES (6, 1, 15, '2021-05-15 22:44:35', '2021-05-15 22:44:35');
INSERT INTO `auth_role_resource` VALUES (7, 1, 7, '2021-05-15 22:45:34', '2021-05-15 22:45:34');
INSERT INTO `auth_role_resource` VALUES (8, 1, 389, '2021-05-15 22:45:37', '2021-05-15 22:45:37');
INSERT INTO `auth_role_resource` VALUES (9, 1, 4, '2021-05-17 18:10:50', '2021-05-17 18:10:50');
INSERT INTO `auth_role_resource` VALUES (10, 1, 391, '2021-05-17 20:50:07', '2021-05-17 20:50:07');
INSERT INTO `auth_role_resource` VALUES (11, 1, 392, '2021-05-18 00:27:21', '2021-05-18 00:27:21');
INSERT INTO `auth_role_resource` VALUES (12, 1, 393, '2021-05-18 00:45:39', '2021-05-18 00:45:39');
INSERT INTO `auth_role_resource` VALUES (13, 1, 395, '2021-05-18 18:08:40', '2021-05-18 18:08:40');
INSERT INTO `auth_role_resource` VALUES (14, 1, 396, '2021-05-19 22:50:02', '2021-05-19 22:50:02');
INSERT INTO `auth_role_resource` VALUES (15, 1, 399, '2021-05-19 23:03:10', '2021-05-19 23:03:10');
INSERT INTO `auth_role_resource` VALUES (16, 1, 397, '2021-05-19 23:30:22', '2021-05-19 23:30:22');
INSERT INTO `auth_role_resource` VALUES (17, 1, 400, '2021-05-20 01:18:32', '2021-05-20 01:18:32');
INSERT INTO `auth_role_resource` VALUES (18, 1, 401, '2021-05-20 16:32:42', '2021-05-20 16:32:42');
INSERT INTO `auth_role_resource` VALUES (19, 1, 402, '2021-05-20 20:27:47', '2021-05-20 20:27:47');
INSERT INTO `auth_role_resource` VALUES (20, 1, 404, '2021-05-20 20:32:47', '2021-05-20 20:32:47');
INSERT INTO `auth_role_resource` VALUES (21, 1, 405, '2021-05-21 17:43:14', '2021-05-21 17:43:14');
INSERT INTO `auth_role_resource` VALUES (22, 1, 390, '2021-05-21 17:48:02', '2021-05-21 17:48:02');
INSERT INTO `auth_role_resource` VALUES (23, 1, 394, '2021-05-21 18:23:12', '2021-05-21 18:23:12');
INSERT INTO `auth_role_resource` VALUES (24, 1, 406, '2021-05-24 18:20:31', '2021-05-24 18:20:31');
INSERT INTO `auth_role_resource` VALUES (25, 1, 408, '2021-05-24 19:45:36', '2021-05-24 19:45:36');
INSERT INTO `auth_role_resource` VALUES (26, 1, 410, '2021-05-24 21:32:18', '2021-05-24 21:32:18');
INSERT INTO `auth_role_resource` VALUES (27, 1, 411, '2021-05-24 22:02:27', '2021-05-24 22:02:27');
INSERT INTO `auth_role_resource` VALUES (28, 1, 412, '2021-05-24 22:42:03', '2021-05-24 22:42:03');
INSERT INTO `auth_role_resource` VALUES (29, 1, 413, '2021-05-25 20:14:49', '2021-05-25 20:14:49');
INSERT INTO `auth_role_resource` VALUES (30, 1, 414, '2021-05-25 20:45:38', '2021-05-25 20:45:38');
INSERT INTO `auth_role_resource` VALUES (31, 1, 415, '2021-05-25 21:31:50', '2021-05-25 21:31:50');
INSERT INTO `auth_role_resource` VALUES (32, 1, 416, '2021-05-25 21:43:37', '2021-05-25 21:43:37');
INSERT INTO `auth_role_resource` VALUES (33, 1, 417, '2021-05-26 00:23:59', '2021-05-26 00:23:59');
INSERT INTO `auth_role_resource` VALUES (34, 1, 419, '2021-05-26 00:40:55', '2021-05-26 00:40:55');
INSERT INTO `auth_role_resource` VALUES (35, 1, 421, '2021-05-26 01:02:16', '2021-05-26 01:02:16');
INSERT INTO `auth_role_resource` VALUES (36, 1, 407, '2021-05-26 16:56:26', '2021-05-26 16:56:26');
INSERT INTO `auth_role_resource` VALUES (37, 1, 422, '2021-05-26 17:02:43', '2021-05-26 17:02:43');
INSERT INTO `auth_role_resource` VALUES (38, 1, 423, '2021-05-26 17:47:50', '2021-05-26 17:47:50');
INSERT INTO `auth_role_resource` VALUES (39, 1, 424, '2021-05-26 18:28:45', '2021-05-26 18:28:45');
INSERT INTO `auth_role_resource` VALUES (40, 1, 425, '2021-05-26 22:00:22', '2021-05-26 22:00:22');
INSERT INTO `auth_role_resource` VALUES (41, 1, 17, '2021-05-27 16:28:02', '2021-05-27 16:28:02');
INSERT INTO `auth_role_resource` VALUES (42, 1, 427, '2021-05-27 18:22:12', '2021-05-27 18:22:12');
INSERT INTO `auth_role_resource` VALUES (44, 1, 430, '2021-05-27 21:54:03', '2021-05-27 21:54:03');
INSERT INTO `auth_role_resource` VALUES (45, 1, 8, '2021-05-27 21:56:41', '2021-05-27 21:56:41');
INSERT INTO `auth_role_resource` VALUES (46, 1, 431, '2021-05-27 22:45:18', '2021-05-27 22:45:18');
INSERT INTO `auth_role_resource` VALUES (48, 1, 27, '2021-05-28 00:23:14', '2021-05-28 00:23:14');
INSERT INTO `auth_role_resource` VALUES (49, 1, 433, '2021-05-28 00:36:53', '2021-05-28 00:36:53');
INSERT INTO `auth_role_resource` VALUES (50, 1, 434, '2021-05-28 22:37:12', '2021-05-28 22:37:12');
INSERT INTO `auth_role_resource` VALUES (51, 1, 435, '2021-06-02 00:43:37', '2021-06-02 00:43:37');
INSERT INTO `auth_role_resource` VALUES (52, 1, 440, '2021-06-02 21:27:55', '2021-06-02 21:27:55');
INSERT INTO `auth_role_resource` VALUES (53, 1, 436, '2021-06-02 21:29:02', '2021-06-02 21:29:02');
INSERT INTO `auth_role_resource` VALUES (54, 1, 441, '2021-06-02 21:31:31', '2021-06-02 21:31:31');
INSERT INTO `auth_role_resource` VALUES (55, 1, 437, '2021-06-02 21:37:00', '2021-06-02 21:37:00');
INSERT INTO `auth_role_resource` VALUES (56, 1, 438, '2021-06-02 22:50:14', '2021-06-02 22:50:14');
INSERT INTO `auth_role_resource` VALUES (57, 1, 442, '2021-06-03 22:24:23', '2021-06-03 22:24:23');
INSERT INTO `auth_role_resource` VALUES (58, 1, 444, '2021-06-03 22:24:25', '2021-06-03 22:24:25');
INSERT INTO `auth_role_resource` VALUES (59, 1, 445, '2021-06-03 22:24:25', '2021-06-03 22:24:25');
INSERT INTO `auth_role_resource` VALUES (60, 1, 443, '2021-06-03 22:26:37', '2021-06-03 22:26:37');
INSERT INTO `auth_role_resource` VALUES (61, 1, 446, '2021-06-07 16:36:06', '2021-06-07 16:36:06');
INSERT INTO `auth_role_resource` VALUES (62, 1, 398, '2021-06-08 01:00:20', '2021-06-08 01:00:20');
INSERT INTO `auth_role_resource` VALUES (63, 1, 447, '2021-06-09 18:12:49', '2021-06-09 18:12:49');
INSERT INTO `auth_role_resource` VALUES (64, 1, 448, '2021-06-09 23:06:44', '2021-06-09 23:06:44');
INSERT INTO `auth_role_resource` VALUES (69, 1, 453, '2021-06-16 20:47:35', '2021-06-16 20:47:35');
INSERT INTO `auth_role_resource` VALUES (70, 1, 454, '2021-06-16 23:06:48', '2021-06-16 23:06:48');
INSERT INTO `auth_role_resource` VALUES (71, 1, 9, '2021-06-17 00:09:22', '2021-06-17 00:09:22');
INSERT INTO `auth_role_resource` VALUES (72, 1, 455, '2021-06-17 00:14:29', '2021-06-17 00:14:29');
INSERT INTO `auth_role_resource` VALUES (73, 1, 456, '2021-06-19 00:57:37', '2021-06-19 00:57:37');
INSERT INTO `auth_role_resource` VALUES (74, 1, 16, '2021-06-20 01:45:07', '2021-06-20 01:45:07');
INSERT INTO `auth_role_resource` VALUES (75, 1, 458, '2021-06-21 23:04:46', '2021-06-21 23:04:46');
INSERT INTO `auth_role_resource` VALUES (76, 1, 459, '2021-06-22 20:06:04', '2021-06-22 20:06:04');
INSERT INTO `auth_role_resource` VALUES (77, 1, 460, '2021-06-30 00:20:14', '2021-06-30 00:20:14');
INSERT INTO `auth_role_resource` VALUES (78, 1, 29, '2021-07-06 18:07:04', '2021-07-06 18:07:04');
INSERT INTO `auth_role_resource` VALUES (79, 4, 2, '2021-07-06 18:07:04', '2021-07-06 18:07:04');
INSERT INTO `auth_role_resource` VALUES (80, 4, 6, '2021-07-06 18:07:08', '2021-07-06 18:07:08');
INSERT INTO `auth_role_resource` VALUES (81, 4, 10, '2021-07-06 18:07:10', '2021-07-06 18:07:10');
INSERT INTO `auth_role_resource` VALUES (82, 4, 27, '2021-07-06 18:07:13', '2021-07-06 18:07:13');
INSERT INTO `auth_role_resource` VALUES (83, 4, 31, '2021-07-06 18:07:17', '2021-07-06 18:07:17');
INSERT INTO `auth_role_resource` VALUES (84, 4, 165, '2021-07-06 18:07:19', '2021-07-06 18:07:19');
INSERT INTO `auth_role_resource` VALUES (85, 4, 189, '2021-07-06 18:07:20', '2021-07-06 18:07:20');
INSERT INTO `auth_role_resource` VALUES (86, 4, 454, '2021-07-06 18:07:26', '2021-07-06 18:07:26');
INSERT INTO `auth_role_resource` VALUES (87, 4, 455, '2021-07-06 18:07:27', '2021-07-06 18:07:27');
INSERT INTO `auth_role_resource` VALUES (88, 4, 389, '2021-07-06 18:07:29', '2021-07-06 18:07:29');
INSERT INTO `auth_role_resource` VALUES (89, 4, 401, '2021-07-06 18:07:32', '2021-07-06 18:07:32');
INSERT INTO `auth_role_resource` VALUES (90, 4, 405, '2021-07-06 18:07:33', '2021-07-06 18:07:33');
INSERT INTO `auth_role_resource` VALUES (91, 4, 456, '2021-07-06 18:07:34', '2021-07-06 18:07:34');
INSERT INTO `auth_role_resource` VALUES (92, 4, 392, '2021-07-06 18:07:39', '2021-07-06 18:07:39');
INSERT INTO `auth_role_resource` VALUES (93, 4, 393, '2021-07-06 18:07:40', '2021-07-06 18:07:40');
INSERT INTO `auth_role_resource` VALUES (94, 4, 396, '2021-07-06 18:07:45', '2021-07-06 18:07:45');
INSERT INTO `auth_role_resource` VALUES (95, 4, 399, '2021-07-06 18:07:46', '2021-07-06 18:07:46');
INSERT INTO `auth_role_resource` VALUES (96, 4, 402, '2021-07-06 18:07:47', '2021-07-06 18:07:47');
INSERT INTO `auth_role_resource` VALUES (97, 4, 406, '2021-07-06 18:07:51', '2021-07-06 18:07:51');
INSERT INTO `auth_role_resource` VALUES (98, 4, 410, '2021-07-06 18:07:52', '2021-07-06 18:07:52');
INSERT INTO `auth_role_resource` VALUES (99, 4, 414, '2021-07-06 18:07:57', '2021-07-06 18:07:57');
INSERT INTO `auth_role_resource` VALUES (100, 4, 421, '2021-07-06 18:07:58', '2021-07-06 18:07:58');
INSERT INTO `auth_role_resource` VALUES (101, 4, 413, '2021-07-06 18:08:01', '2021-07-06 18:08:01');
INSERT INTO `auth_role_resource` VALUES (102, 4, 417, '2021-07-06 18:08:06', '2021-07-06 18:08:06');
INSERT INTO `auth_role_resource` VALUES (103, 4, 440, '2021-07-06 18:08:11', '2021-07-06 18:08:11');
INSERT INTO `auth_role_resource` VALUES (104, 4, 441, '2021-07-06 18:08:11', '2021-07-06 18:08:11');
INSERT INTO `auth_role_resource` VALUES (105, 4, 442, '2021-07-06 18:08:12', '2021-07-06 18:08:12');
INSERT INTO `auth_role_resource` VALUES (106, 4, 444, '2021-07-06 18:08:14', '2021-07-06 18:08:14');
INSERT INTO `auth_role_resource` VALUES (107, 4, 445, '2021-07-06 18:08:15', '2021-07-06 18:08:15');
INSERT INTO `auth_role_resource` VALUES (108, 4, 453, '2021-07-06 18:08:21', '2021-07-06 18:08:21');
INSERT INTO `auth_role_resource` VALUES (109, 4, 446, '2021-07-06 18:09:21', '2021-07-06 18:09:21');
INSERT INTO `auth_role_resource` VALUES (110, 4, 32, '2021-07-06 18:09:24', '2021-07-06 18:09:24');
INSERT INTO `auth_role_resource` VALUES (111, 4, 434, '2021-07-06 18:09:28', '2021-07-06 18:09:28');
INSERT INTO `auth_role_resource` VALUES (112, 4, 447, '2021-07-06 18:09:29', '2021-07-06 18:09:29');
INSERT INTO `auth_role_resource` VALUES (113, 1, 439, '2021-07-09 21:51:27', '2021-07-09 21:51:27');
INSERT INTO `auth_role_resource` VALUES (114, 1, 462, '2021-07-09 23:11:33', '2021-07-09 23:11:33');
INSERT INTO `auth_role_resource` VALUES (115, 1, 463, '2021-07-14 00:02:45', '2021-07-14 00:02:45');
INSERT INTO `auth_role_resource` VALUES (116, 1, 464, '2021-07-15 22:49:37', '2021-07-15 22:49:37');
INSERT INTO `auth_role_resource` VALUES (117, 1, 465, '2021-07-16 22:22:33', '2021-07-16 22:22:33');
INSERT INTO `auth_role_resource` VALUES (119, 1, 467, '2021-07-16 22:28:46', '2021-07-16 22:28:46');
INSERT INTO `auth_role_resource` VALUES (120, 1, 466, '2021-07-16 22:34:50', '2021-07-16 22:34:50');
INSERT INTO `auth_role_resource` VALUES (121, 1, 470, '2021-07-16 23:02:38', '2021-07-16 23:02:38');
INSERT INTO `auth_role_resource` VALUES (123, 1, 469, '2021-07-19 21:12:44', '2021-07-19 21:12:44');
INSERT INTO `auth_role_resource` VALUES (124, 1, 472, '2021-07-19 22:34:06', '2021-07-19 22:34:06');
INSERT INTO `auth_role_resource` VALUES (125, 4, 463, '2021-07-20 17:56:18', '2021-07-20 17:56:18');
INSERT INTO `auth_role_resource` VALUES (126, 4, 464, '2021-07-20 17:56:19', '2021-07-20 17:56:19');
INSERT INTO `auth_role_resource` VALUES (127, 4, 465, '2021-07-20 17:56:19', '2021-07-20 17:56:19');
INSERT INTO `auth_role_resource` VALUES (128, 4, 472, '2021-07-20 17:56:23', '2021-07-20 17:56:23');
INSERT INTO `auth_role_resource` VALUES (129, 1, 473, '2021-07-23 00:54:03', '2021-07-23 00:54:03');
INSERT INTO `auth_role_resource` VALUES (130, 1, 475, '2021-08-03 00:16:04', '2021-08-03 00:16:04');
INSERT INTO `auth_role_resource` VALUES (131, 1, 476, '2021-08-03 17:25:49', '2021-08-03 17:25:49');
INSERT INTO `auth_role_resource` VALUES (132, 1, 426, '2021-08-03 23:42:40', '2021-08-03 23:42:40');
INSERT INTO `auth_role_resource` VALUES (133, 1, 477, '2021-08-05 21:23:52', '2021-08-05 21:23:52');
INSERT INTO `auth_role_resource` VALUES (134, 1, 478, '2021-08-05 21:44:46', '2021-08-05 21:44:46');
INSERT INTO `auth_role_resource` VALUES (135, 1, 479, '2021-08-09 16:28:24', '2021-08-09 16:28:24');
INSERT INTO `auth_role_resource` VALUES (136, 1, 482, '2021-08-09 18:08:05', '2021-08-09 18:08:05');
INSERT INTO `auth_role_resource` VALUES (137, 1, 483, '2021-08-11 01:16:58', '2021-08-11 01:16:58');
INSERT INTO `auth_role_resource` VALUES (138, 1, 468, '2021-08-16 17:12:04', '2021-08-16 17:12:04');
INSERT INTO `auth_role_resource` VALUES (139, 1, 484, '2021-08-20 21:58:25', '2021-08-20 21:58:25');
INSERT INTO `auth_role_resource` VALUES (140, 1, 485, '2021-08-20 22:18:15', '2021-08-20 22:18:15');
INSERT INTO `auth_role_resource` VALUES (141, 1, 486, '2021-08-25 17:16:50', '2021-08-25 17:16:50');
INSERT INTO `auth_role_resource` VALUES (142, 1, 487, '2021-08-28 00:16:10', '2021-08-28 00:16:10');
INSERT INTO `auth_role_resource` VALUES (143, 1, 489, '2021-08-30 23:22:19', '2021-08-30 23:22:19');
INSERT INTO `auth_role_resource` VALUES (144, 1, 488, '2021-08-31 00:36:00', '2021-08-31 00:36:00');
INSERT INTO `auth_role_resource` VALUES (145, 1, 490, '2021-08-31 00:39:33', '2021-08-31 00:39:33');
INSERT INTO `auth_role_resource` VALUES (146, 1, 491, '2021-08-31 22:38:56', '2021-08-31 22:38:56');
INSERT INTO `auth_role_resource` VALUES (147, 1, 492, '2021-09-01 21:16:51', '2021-09-01 21:16:51');
INSERT INTO `auth_role_resource` VALUES (148, 1, 494, '2021-09-02 18:49:46', '2021-09-02 18:49:46');
INSERT INTO `auth_role_resource` VALUES (149, 1, 496, '2021-09-06 18:24:14', '2021-09-06 18:24:14');
INSERT INTO `auth_role_resource` VALUES (150, 1, 497, '2021-09-06 22:37:57', '2021-09-06 22:37:57');
INSERT INTO `auth_role_resource` VALUES (151, 1, 500, '2021-09-09 17:03:49', '2021-09-09 17:03:49');
INSERT INTO `auth_role_resource` VALUES (152, 1, 481, '2021-09-15 21:48:28', '2021-09-15 21:48:28');
INSERT INTO `auth_role_resource` VALUES (153, 1, 501, '2021-09-16 17:49:20', '2021-09-16 17:49:20');
INSERT INTO `auth_role_resource` VALUES (154, 1, 502, '2021-09-17 22:16:51', '2021-09-17 22:16:51');
INSERT INTO `auth_role_resource` VALUES (155, 1, 504, '2021-09-17 22:46:44', '2021-09-17 22:46:44');
INSERT INTO `auth_role_resource` VALUES (156, 1, 505, '2021-09-17 22:46:51', '2021-09-17 22:46:51');
INSERT INTO `auth_role_resource` VALUES (157, 1, 506, '2021-09-17 22:46:53', '2021-09-17 22:46:53');
INSERT INTO `auth_role_resource` VALUES (158, 1, 507, '2021-09-17 22:53:08', '2021-09-17 22:53:08');
INSERT INTO `auth_role_resource` VALUES (159, 1, 503, '2021-09-17 23:20:04', '2021-09-17 23:20:04');
INSERT INTO `auth_role_resource` VALUES (160, 1, 509, '2021-09-18 00:08:20', '2021-09-18 00:08:20');
INSERT INTO `auth_role_resource` VALUES (161, 1, 510, '2021-09-18 00:22:42', '2021-09-18 00:22:42');
INSERT INTO `auth_role_resource` VALUES (162, 1, 512, '2021-09-18 00:28:48', '2021-09-18 00:28:48');
INSERT INTO `auth_role_resource` VALUES (163, 1, 511, '2021-09-18 00:42:54', '2021-09-18 00:42:54');
INSERT INTO `auth_role_resource` VALUES (164, 1, 513, '2021-09-18 00:45:33', '2021-09-18 00:45:33');
INSERT INTO `auth_role_resource` VALUES (165, 1, 515, '2021-09-19 00:07:05', '2021-09-19 00:07:05');
INSERT INTO `auth_role_resource` VALUES (166, 1, 508, '2021-09-22 17:28:40', '2021-09-22 17:28:40');
INSERT INTO `auth_role_resource` VALUES (167, 1, 493, '2021-09-23 22:28:24', '2021-09-23 22:28:24');
INSERT INTO `auth_role_resource` VALUES (168, 1, 516, '2021-09-24 21:52:23', '2021-09-24 21:52:23');
INSERT INTO `auth_role_resource` VALUES (169, 1, 495, '2021-09-28 17:07:49', '2021-09-28 17:07:49');
INSERT INTO `auth_role_resource` VALUES (170, 4, 448, '2021-10-08 21:42:11', '2021-10-08 21:42:11');
INSERT INTO `auth_role_resource` VALUES (171, 1, 517, '2021-10-26 01:36:59', '2021-10-26 01:36:59');
INSERT INTO `auth_role_resource` VALUES (172, 1, 409, '2021-10-26 01:37:02', '2021-10-26 01:37:02');
INSERT INTO `auth_role_resource` VALUES (173, 1, 518, '2021-10-27 21:30:48', '2021-10-27 21:30:48');
INSERT INTO `auth_role_resource` VALUES (174, 1, 519, '2021-10-28 17:12:35', '2021-10-28 17:12:35');
INSERT INTO `auth_role_resource` VALUES (175, 1, 420, '2021-10-28 17:58:03', '2021-10-28 17:58:03');
INSERT INTO `auth_role_resource` VALUES (176, 5, 427, '2021-11-05 12:31:44', '2021-11-05 12:31:44');
INSERT INTO `auth_role_resource` VALUES (177, 5, 522, '2021-11-15 14:04:03', '2021-11-15 14:04:03');
INSERT INTO `auth_role_resource` VALUES (178, 1, 523, '2021-11-29 14:14:49', '2021-11-29 14:14:49');
INSERT INTO `auth_role_resource` VALUES (179, 1, 525, '2021-12-06 13:42:27', '2021-12-06 13:42:27');
INSERT INTO `auth_role_resource` VALUES (180, 1, 524, '2021-12-06 15:54:26', '2021-12-06 15:54:26');
INSERT INTO `auth_role_resource` VALUES (181, 1, 526, '2021-12-06 17:11:46', '2021-12-06 17:11:46');
INSERT INTO `auth_role_resource` VALUES (182, 1, 527, '2021-12-07 09:36:40', '2021-12-07 09:36:40');
INSERT INTO `auth_role_resource` VALUES (183, 1, 528, '2021-12-07 14:14:31', '2021-12-07 14:14:31');
INSERT INTO `auth_role_resource` VALUES (184, 1, 529, '2021-12-07 18:55:15', '2021-12-07 18:55:15');
INSERT INTO `auth_role_resource` VALUES (185, 1, 531, '2021-12-08 15:12:39', '2021-12-08 15:12:39');
INSERT INTO `auth_role_resource` VALUES (186, 1, 530, '2021-12-08 15:17:16', '2021-12-08 15:17:16');
INSERT INTO `auth_role_resource` VALUES (187, 1, 532, '2021-12-08 15:21:10', '2021-12-08 15:21:10');
INSERT INTO `auth_role_resource` VALUES (188, 1, 533, '2021-12-08 17:51:37', '2021-12-08 17:51:37');
INSERT INTO `auth_role_resource` VALUES (189, 1, 428, '2021-12-10 10:02:57', '2021-12-10 10:02:57');
INSERT INTO `auth_role_resource` VALUES (190, 1, 534, '2021-12-10 16:35:01', '2021-12-10 16:35:01');
INSERT INTO `auth_role_resource` VALUES (191, 1, 5, '2021-12-10 17:12:16', '2021-12-10 17:12:16');
INSERT INTO `auth_role_resource` VALUES (192, 1, 535, '2021-12-13 13:54:56', '2021-12-13 13:54:56');
INSERT INTO `auth_role_resource` VALUES (193, 1, 537, '2021-12-13 17:16:41', '2021-12-13 17:16:41');
INSERT INTO `auth_role_resource` VALUES (194, 1, 538, '2021-12-13 18:22:07', '2021-12-13 18:22:07');
INSERT INTO `auth_role_resource` VALUES (195, 1, 536, '2021-12-13 19:52:35', '2021-12-13 19:52:35');
INSERT INTO `auth_role_resource` VALUES (196, 1, 539, '2021-12-14 15:32:55', '2021-12-14 15:32:55');
INSERT INTO `auth_role_resource` VALUES (197, 1, 540, '2021-12-17 10:49:16', '2021-12-17 10:49:16');
INSERT INTO `auth_role_resource` VALUES (198, 1, 541, '2021-12-20 16:08:41', '2021-12-20 16:08:41');
INSERT INTO `auth_role_resource` VALUES (200, 1, 3, '2021-12-21 19:39:56', '2021-12-21 19:39:56');
INSERT INTO `auth_role_resource` VALUES (201, 1, 522, '2021-12-21 19:39:59', '2021-12-21 19:39:59');
INSERT INTO `auth_role_resource` VALUES (202, 1, 189, '2021-12-21 19:40:00', '2021-12-21 19:40:00');
INSERT INTO `auth_role_resource` VALUES (203, 1, 188, '2021-12-21 19:40:01', '2021-12-21 19:40:01');
INSERT INTO `auth_role_resource` VALUES (204, 1, 34, '2021-12-21 19:40:02', '2021-12-21 19:40:02');
INSERT INTO `auth_role_resource` VALUES (205, 1, 31, '2021-12-21 19:40:03', '2021-12-21 19:40:03');
INSERT INTO `auth_role_resource` VALUES (206, 1, 30, '2021-12-21 19:40:04', '2021-12-21 19:40:04');
INSERT INTO `auth_role_resource` VALUES (207, 1, 33, '2021-12-21 19:40:05', '2021-12-21 19:40:05');
INSERT INTO `auth_role_resource` VALUES (208, 1, 514, '2021-12-29 11:17:48', '2021-12-29 11:17:48');
INSERT INTO `auth_role_resource` VALUES (209, 1, 542, '2022-01-06 17:13:35', '2022-01-06 17:13:35');
INSERT INTO `auth_role_resource` VALUES (210, 1, 544, '2022-01-12 18:07:19', '2022-01-12 18:07:19');
INSERT INTO `auth_role_resource` VALUES (211, 1, 543, '2022-01-12 19:28:04', '2022-01-12 19:28:04');
INSERT INTO `auth_role_resource` VALUES (212, 1, 545, '2022-01-13 09:57:45', '2022-01-13 09:57:45');
INSERT INTO `auth_role_resource` VALUES (213, 1, 547, '2022-01-13 14:35:41', '2022-01-13 14:35:41');
INSERT INTO `auth_role_resource` VALUES (214, 1, 546, '2022-01-13 14:38:23', '2022-01-13 14:38:23');
INSERT INTO `auth_role_resource` VALUES (215, 1, 548, '2022-01-17 12:49:41', '2022-01-17 12:49:41');
INSERT INTO `auth_role_resource` VALUES (216, 1, 549, '2022-01-17 16:29:28', '2022-01-17 16:29:28');
INSERT INTO `auth_role_resource` VALUES (217, 1, 550, '2022-01-18 11:33:56', '2022-01-18 11:33:56');
INSERT INTO `auth_role_resource` VALUES (220, 1, 551, '2022-01-19 16:32:23', '2022-01-19 16:32:23');
INSERT INTO `auth_role_resource` VALUES (221, 1, 552, '2022-01-20 13:47:04', '2022-01-20 13:47:04');
INSERT INTO `auth_role_resource` VALUES (222, 5, 542, '2022-01-21 09:39:14', '2022-01-21 09:39:14');
INSERT INTO `auth_role_resource` VALUES (223, 5, 543, '2022-01-21 09:39:16', '2022-01-21 09:39:16');
INSERT INTO `auth_role_resource` VALUES (224, 5, 546, '2022-01-21 09:39:18', '2022-01-21 09:39:18');
INSERT INTO `auth_role_resource` VALUES (225, 5, 550, '2022-01-21 09:39:22', '2022-01-21 09:39:22');
INSERT INTO `auth_role_resource` VALUES (226, 5, 552, '2022-01-21 09:39:24', '2022-01-21 09:39:24');
INSERT INTO `auth_role_resource` VALUES (227, 5, 544, '2022-01-21 09:39:28', '2022-01-21 09:39:28');
INSERT INTO `auth_role_resource` VALUES (228, 5, 545, '2022-01-21 09:39:31', '2022-01-21 09:39:31');
INSERT INTO `auth_role_resource` VALUES (229, 5, 547, '2022-01-21 09:39:32', '2022-01-21 09:39:32');
INSERT INTO `auth_role_resource` VALUES (230, 5, 548, '2022-01-21 09:39:33', '2022-01-21 09:39:33');
INSERT INTO `auth_role_resource` VALUES (232, 5, 549, '2022-01-21 09:39:33', '2022-01-21 09:39:33');
INSERT INTO `auth_role_resource` VALUES (234, 5, 551, '2022-01-21 09:39:34', '2022-01-21 09:39:34');
INSERT INTO `auth_role_resource` VALUES (236, 4, 542, '2022-01-21 13:52:19', '2022-01-21 13:52:19');
INSERT INTO `auth_role_resource` VALUES (237, 4, 543, '2022-01-21 13:52:20', '2022-01-21 13:52:20');
INSERT INTO `auth_role_resource` VALUES (238, 4, 544, '2022-01-21 13:52:21', '2022-01-21 13:52:21');
INSERT INTO `auth_role_resource` VALUES (240, 4, 545, '2022-01-21 13:52:22', '2022-01-21 13:52:22');
INSERT INTO `auth_role_resource` VALUES (241, 4, 546, '2022-01-21 13:52:23', '2022-01-21 13:52:23');
INSERT INTO `auth_role_resource` VALUES (244, 4, 547, '2022-01-21 13:52:24', '2022-01-21 13:52:24');
INSERT INTO `auth_role_resource` VALUES (246, 4, 548, '2022-01-21 13:52:25', '2022-01-21 13:52:25');
INSERT INTO `auth_role_resource` VALUES (248, 4, 549, '2022-01-21 13:52:26', '2022-01-21 13:52:26');
INSERT INTO `auth_role_resource` VALUES (250, 4, 550, '2022-01-21 13:52:27', '2022-01-21 13:52:27');
INSERT INTO `auth_role_resource` VALUES (252, 4, 551, '2022-01-21 13:52:28', '2022-01-21 13:52:28');
INSERT INTO `auth_role_resource` VALUES (254, 4, 552, '2022-01-21 13:52:29', '2022-01-21 13:52:29');
INSERT INTO `auth_role_resource` VALUES (256, 5, 553, '2022-01-24 16:14:45', '2022-01-24 16:14:45');
INSERT INTO `auth_role_resource` VALUES (257, 1, 555, '2022-01-26 12:11:13', '2022-01-26 12:11:13');
INSERT INTO `auth_role_resource` VALUES (258, 1, 554, '2022-01-26 12:25:50', '2022-01-26 12:25:50');
INSERT INTO `auth_role_resource` VALUES (259, 1, 557, '2022-02-08 09:51:32', '2022-02-08 09:51:32');
INSERT INTO `auth_role_resource` VALUES (260, 1, 556, '2022-02-08 10:29:58', '2022-02-08 10:29:58');
INSERT INTO `auth_role_resource` VALUES (261, 1, 558, '2022-02-08 17:18:03', '2022-02-08 17:18:03');
INSERT INTO `auth_role_resource` VALUES (262, 1, 559, '2022-02-08 17:18:31', '2022-02-08 17:18:31');
INSERT INTO `auth_role_resource` VALUES (263, 1, 560, '2022-02-08 19:35:09', '2022-02-08 19:35:09');
INSERT INTO `auth_role_resource` VALUES (264, 1, 561, '2022-02-14 11:52:12', '2022-02-14 11:52:12');
INSERT INTO `auth_role_resource` VALUES (265, 1, 562, '2022-02-14 15:15:45', '2022-02-14 15:15:45');
INSERT INTO `auth_role_resource` VALUES (266, 1, 563, '2022-02-15 15:43:02', NULL);
COMMIT;

-- ----------------------------
-- Table structure for auth_user_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_user_role`;
CREATE TABLE `auth_user_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户登录名',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username_role_unique` (`username`,`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_user_role
-- ----------------------------
BEGIN;
INSERT INTO `auth_user_role` VALUES (1, 'admin', 1, '2021-12-29 16:33:05', '2021-12-29 16:33:05');
INSERT INTO `auth_user_role` VALUES (2, 'admin', 2, '2021-12-29 16:33:11', '2021-12-29 16:33:11');
INSERT INTO `auth_user_role` VALUES (3, 'admin', 3, '2021-12-29 16:33:17', '2021-12-29 16:33:17');
INSERT INTO `auth_user_role` VALUES (4, 'admin', 4, '2021-12-29 16:33:23', '2021-12-29 16:33:23');
INSERT INTO `auth_user_role` VALUES (5, 'admin', 5, '2021-12-29 16:33:37', '2021-12-29 16:33:37');
COMMIT;

-- ----------------------------
-- Table structure for business_asset_relation
-- ----------------------------
DROP TABLE IF EXISTS `business_asset_relation`;
CREATE TABLE `business_asset_relation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `business_type` int(11) NOT NULL COMMENT '业务类型',
  `business_id` int(11) NOT NULL COMMENT '业务ID',
  `datasource_instance_asset_id` int(11) NOT NULL COMMENT '资产ID',
  `asset_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资产类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `business_type` (`business_type`,`business_id`,`datasource_instance_asset_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=270 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务对象与资产的绑定关系';

-- ----------------------------
-- Records of business_asset_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for business_property
-- ----------------------------
DROP TABLE IF EXISTS `business_property`;
CREATE TABLE `business_property` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `business_type` int(11) NOT NULL COMMENT '业务类型',
  `business_id` int(11) NOT NULL COMMENT '业务ID',
  `property` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of business_property
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for business_tag
-- ----------------------------
DROP TABLE IF EXISTS `business_tag`;
CREATE TABLE `business_tag` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `business_type` int(2) DEFAULT NULL COMMENT '业务类型',
  `business_id` int(11) NOT NULL COMMENT '业务ID',
  `tag_id` int(11) NOT NULL COMMENT '标签ID',
  `tag_value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `business_id` (`business_id`,`tag_id`,`business_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器标签关联表';

-- ----------------------------
-- Records of business_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for business_template
-- ----------------------------
DROP TABLE IF EXISTS `business_template`;
CREATE TABLE `business_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `instance_uuid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例UUID',
  `business_type` int(11) NOT NULL COMMENT '业务类型',
  `business_id` int(11) NOT NULL COMMENT '业务ID',
  `template_id` int(11) NOT NULL COMMENT '模版ID',
  `env_type` int(11) NOT NULL COMMENT '环境类型',
  `vars` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模板变量',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模板内容',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务模版';

-- ----------------------------
-- Records of business_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for datasource_config
-- ----------------------------
DROP TABLE IF EXISTS `datasource_config`;
CREATE TABLE `datasource_config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据源名称',
  `uuid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'UUID',
  `ds_type` int(2) NOT NULL COMMENT '数据源类型',
  `version` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `kind` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '分类',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `credential_id` int(11) DEFAULT NULL COMMENT '凭据ID',
  `ds_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '数据源地址',
  `props_yml` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '属性(yaml)',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源配置';

-- ----------------------------
-- Records of datasource_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for datasource_instance
-- ----------------------------
DROP TABLE IF EXISTS `datasource_instance`;
CREATE TABLE `datasource_instance` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '数据源关联ID',
  `instance_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据源名称',
  `uuid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'UUID',
  `instance_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '实例类型',
  `kind` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '实例分类',
  `version` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例版本',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `config_id` int(11) DEFAULT NULL COMMENT '数据源配置ID',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`instance_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源实例';

-- ----------------------------
-- Records of datasource_instance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for datasource_instance_asset
-- ----------------------------
DROP TABLE IF EXISTS `datasource_instance_asset`;
CREATE TABLE `datasource_instance_asset` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '资产父关系',
  `instance_uuid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据源实例UUID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产名称',
  `asset_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资产ID',
  `asset_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资产类型',
  `kind` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '资产分类',
  `version` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产版本',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `asset_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资产关键字1',
  `asset_key_2` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产关键字2',
  `zone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区域',
  `region_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地区ID',
  `asset_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产状态',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_time` timestamp NULL DEFAULT NULL COMMENT '资产创建时间',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '资产过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `asset_id` (`asset_id`,`asset_type`,`instance_uuid`,`asset_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源实例资产';

-- ----------------------------
-- Records of datasource_instance_asset
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for datasource_inst