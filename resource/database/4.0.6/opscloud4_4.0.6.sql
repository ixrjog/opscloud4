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

 Date: 29/12/2021 16:35:48
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
  `token_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '令牌标识',
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'None' COMMENT '访问令牌',
  `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否无效',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `expired_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `token` (`token`) USING BTREE,
  UNIQUE KEY `token_id` (`token_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='api调用令牌';

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
  `datasource_instance_id` int(11) NOT NULL COMMENT '数据源实例id',
  `project` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `logstore` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `config` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `account_uid` (`datasource_instance_id`,`project`,`logstore`,`config`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='阿里云日志服务';

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
  `aliyun_log_id` int(11) NOT NULL COMMENT 'sls表主键',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='阿里云日志服务服务器组成员';

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
  `playbook_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '剧本uuid',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `playbook` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '剧本内容',
  `tags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '标签配置',
  `vars` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '外部变量',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `playbook_uuid` (`playbook_uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of ansible_playbook
-- ----------------------------
BEGIN;
INSERT INTO `ansible_playbook` VALUES (1, '60ea4519d68045719a78a8c995c59a11', 'Remote replication', '---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name: copy\n      copy:\n        src: \"{{ src }}\"\n        dest: \"{{ dest }}\"\n        owner: manage\n        group: manage\n      become: yes', '', '', '远程复制文件', '2021-09-01 14:18:34', '2021-09-07 10:02:12');
INSERT INTO `ansible_playbook` VALUES (2, '2ff188d2d7fa4880a321f3446fc833e0', 'Test', '---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name: test1\n      command: echo \'test1\'\n    - name: test2\n      command: echo \'test2\'', '', '', '测试剧本', '2021-09-23 15:28:25', '2021-09-23 15:31:11');
INSERT INTO `ansible_playbook` VALUES (5, '1ad8e2adb7094190b29bdc4504b00c2a', 'push-dnsmasq-conf', '---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name:\n      shell: cd /data/www/data/dnsmasq && wget -O dnsmasq.conf vc-res.ops.yangege.cn/res/dnsmasq/{{ useType }}/dnsmasq.conf\n    - name: check conf\n      shell: /usr/sbin/dnsmasq --test -C /data/www/data/dnsmasq/dnsmasq.conf\n      register: result\n    - name: check if deploy fail\n      fail: msg=\"conf check fail\"\n      when: result.failed == true\n    - name: restart service\n      shell: /etc/init.d/dnsmasq restart', '', '', '推送dnsmasq配置文件', '2021-09-28 10:09:02', '2021-09-28 10:09:02');
INSERT INTO `ansible_playbook` VALUES (6, '8c639f378e5343859c85e4332de9cd20', 'playbook-nginx', '---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name: copy file \n      copy: src={{ src }} dest={{ dest }} owner=root group=root  \n      notify:\n       - reloaded service\n    - name: get ps\n      shell: ps -ef|grep nginx|grep -v grep\n      register: psInfo\n    - name: show ps\n      debug: msg=\"{{ psInfo }}\"\n  handlers:  \n    - name: reloaded service\n      shell: /bin/systemctl reload nginx.service', '', 'vars:\n  src: /home/app/opscloud-data/nginx/\n  dest: /data/nginx/conf', '', '2021-09-28 10:10:08', '2021-09-28 10:16:50');
INSERT INTO `ansible_playbook` VALUES (10, 'ea91fc9fa6e245de890a277bd5ed34c0', 'playbook-nginx-sync-config', '---\n- hosts: \"{{ hosts }}\"\n  become: yes\n  tasks:\n    - name: install rsync\n      yum:\n        name: rsync\n        state: present\n    - name: creates directory\n      file:\n        path: \"/data/www/nginx-conf/{{ dest }}\"\n        state: directory\n        owner: www\n        group: www\n        mode: 0775\n    - name: sync config \n      synchronize:\n        src: \"{{ src }}/\"\n        dest: \"/data/www/nginx-conf/{{ dest }}/\"\n        delete: yes\n    - name: recursively change ownership of a directory\n      file:\n        path: /data/www/nginx-conf\n        state: directory\n        recurse: yes\n        owner: www\n        group: www\n        mode: 0644\n    - name: reloaded nginx\n      shell: /etc/init.d/nginx reload\n    - name: get ps\n      shell: ps -ef|grep nginx|grep -v grep\n      register: psInfo\n    - name: show ps\n      debug: msg=\"{{ psInfo }}\"\n', '', 'vars:\n  src:\n  dest:', '同步nginx配置文件', '2021-09-28 10:13:42', '2021-09-28 10:13:42');
INSERT INTO `ansible_playbook` VALUES (12, '62ceb487b6bb4b869510ac8083a5b847', 'playbook-nginx-push-config', '---\n- hosts: \"{{ hosts }}\"\n  become: yes\n  tasks:\n    - name: push file\n      copy:\n        src: \"{{ src }}\"\n        dest: \"/data/www/nginx-conf/{{ dest }}\"\n        owner: www\n        group: www\n        mode: 0644\n      notify:\n       - reloaded nginx\n    - name: get ps\n      shell: ps -ef|grep nginx|grep -v grep\n      register: psInfo\n    - name: show ps\n      debug: msg=\"{{ psInfo }}\"\n  handlers:  \n    - name: reloaded nginx\n      shell: /etc/init.d/nginx reload', '', 'vars:\n  src:\n  dest:', '推送nginx配置文件', '2021-09-28 10:22:32', '2021-09-28 10:22:32');
INSERT INTO `ansible_playbook` VALUES (13, 'dcf0a7a51ec048f9ac8ab0365a42a498', 'playbook-nginx-del-config', '---\n- hosts: \"{{ hosts }}\"\n  become: yes\n  tasks:\n    - name: remove file (delete file)\n      file:\n        path: \"/data/www/nginx-conf/{{ path }}\"\n        state: absent\n      notify:\n       - reloaded nginx\n    - name: get ps\n      shell: ps -ef|grep nginx|grep -v grep\n      register: psInfo\n    - name: show ps\n      debug: msg=\"{{ psInfo }}\"\n  handlers:  \n    - name: reloaded nginx\n      shell: /etc/init.d/nginx reload', '', 'vars:\n  path:', '删除nginx配置文件', '2021-09-28 10:23:33', '2021-09-28 10:23:33');
INSERT INTO `ansible_playbook` VALUES (16, '7ddba3d9948449a0a6802d0b04e2d51a', 'playbook-k8s-worker-init', '---\n- name: base\n  include: \"{{ basePath }}/k8s-worker-init.yml\"', '', 'vars:\n  basePath: /home/app/opscloud-data/ansible/playbook/tool-install', 'k8s集群工作节点初始化', '2021-11-19 10:58:37', '2021-11-19 10:58:37');
INSERT INTO `ansible_playbook` VALUES (17, '38022fae68ad441bb67f59e7327ede25', 'playbook-zabbix-agent-install', '---\n- name: zabbix agent install\n  include: \"{{ basePath }}/zabbix-agent-install.yml\"', '', 'vars:\n  basePath: /home/app/opscloud-data/ansible/playbook/tool-install\n  # hangzhou.proxy.zabbix.chuanyinet.com\n  # server.zabbix.chuanyinet.com\n  zabbixServer: server.zabbix.chuanyinet.com', '安装zabbix5-agent', '2021-12-23 10:14:32', '2021-12-28 03:10:26');
INSERT INTO `ansible_playbook` VALUES (18, '77ba4fdfaf6c476fb7710175662836a1', 'playbook-server-base-init', '---\n- name: zabbix agent install\n  include: \"{{ basePath }}/server-base-init.yml\"', '', 'vars:\n  basePath: /home/app/opscloud-data/ansible/playbook/tool-install', '服务器初始化', '2021-12-28 10:07:06', '2021-12-28 10:11:47');
COMMIT;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '应用名称',
  `application_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '应用key',
  `application_type` int(2) DEFAULT '0' COMMENT '应用类型',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `application_key` (`application_key`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聚合应用';

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
  `application_id` int(11) NOT NULL COMMENT '应用id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资源名称',
  `virtual_resource` tinyint(1) DEFAULT '0' COMMENT '虚拟资源',
  `resource_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '资源类型',
  `business_id` int(11) DEFAULT NULL COMMENT '业务id',
  `business_type` int(2) DEFAULT NULL COMMENT '业务类型',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `application_id` (`application_id`,`resource_type`,`business_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='应用资源';

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
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_group
-- ----------------------------
BEGIN;
INSERT INTO `auth_group` VALUES (1, 'Auth', '/api/auth', '权限管理', '2020-02-15 17:04:55', '2021-07-13 15:45:26');
INSERT INTO `auth_group` VALUES (2, 'User', '/api/user', '用户', '2020-02-20 11:58:56', '2021-07-13 15:45:31');
INSERT INTO `auth_group` VALUES (40, 'Datasource', '/api/datasource', '数据源管理', '2021-05-15 15:44:36', '2021-07-13 15:45:37');
INSERT INTO `auth_group` VALUES (41, 'System', '', '系统管理', '2021-05-17 17:26:30', '2021-05-17 17:26:30');
INSERT INTO `auth_group` VALUES (42, 'Tag', '/api/tag', '标签', '2021-05-19 15:05:06', '2021-07-13 15:45:41');
INSERT INTO `auth_group` VALUES (43, 'ServerGroup', '/api/server/group', '服务器组', '2021-05-24 11:18:33', '2021-07-13 15:45:46');
INSERT INTO `auth_group` VALUES (44, 'Server', '/api/server', '服务器管理', '2021-05-25 11:43:59', '2021-07-13 15:45:51');
INSERT INTO `auth_group` VALUES (45, 'ServerAccount', '/api/server/account', '服务器账户管理', '2021-05-25 11:44:17', '2021-07-13 15:45:56');
INSERT INTO `auth_group` VALUES (46, 'Env', '/api/env', '环境管理', '2021-05-25 17:22:40', '2021-07-13 15:46:00');
INSERT INTO `auth_group` VALUES (47, 'Menu', '/api/sys/menu', '菜单管理', '2021-06-02 14:26:08', '2021-07-13 15:46:05');
INSERT INTO `auth_group` VALUES (48, 'Document', '/api/sys/doc', '文档管理', '2021-06-16 13:34:02', '2021-07-13 15:46:11');
INSERT INTO `auth_group` VALUES (50, 'Application', '/api/application', '应用管理', '2021-07-13 15:42:30', '2021-07-13 15:42:30');
INSERT INTO `auth_group` VALUES (52, 'TerminalSession', '/api/terminal/session', '终端会话管理', '2021-07-22 17:29:27', '2021-07-22 17:29:27');
INSERT INTO `auth_group` VALUES (53, 'Static', NULL, '前端静态资源', '2021-07-27 16:48:24', '2021-07-27 16:48:24');
INSERT INTO `auth_group` VALUES (54, 'Business', '/api/business', '业务对象管理', '2021-08-20 14:54:50', '2021-08-20 14:55:29');
INSERT INTO `auth_group` VALUES (55, 'Task', '/api/task', '任务管理', '2021-09-01 14:15:56', '2021-09-01 14:16:10');
INSERT INTO `auth_group` VALUES (56, 'Aliyun', NULL, '阿里云服务', '2021-09-17 15:05:07', '2021-09-17 15:05:07');
INSERT INTO `auth_group` VALUES (57, 'ServerTask', '/api/server/task', '服务器任务', '2021-09-18 17:06:32', '2021-09-18 17:06:32');
INSERT INTO `auth_group` VALUES (58, 'Event', '/api/receive/event', '事件', '2021-10-29 16:15:39', '2021-10-29 16:15:39');
INSERT INTO `auth_group` VALUES (59, 'Template', '/api/template', '模版管理', '2021-12-06 06:22:37', '2021-12-06 06:22:37');
COMMIT;

-- ----------------------------
-- Table structure for auth_menu
-- ----------------------------
DROP TABLE IF EXISTS `auth_menu`;
CREATE TABLE `auth_menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色id',
  `menu_type` int(1) NOT NULL DEFAULT '0' COMMENT '0:aside 1:header',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `menu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_menu
-- ----------------------------
BEGIN;
INSERT INTO `auth_menu` VALUES (10, 1, 0, 'super_admin', '[\n  {\n    title: \'首页\',\n    icon: \'home\',\n    children: [\n      { path: \'/dashboard\', title: \'仪表盘\', icon: \'area-chart\' },\n      { path: \'/dashboard/hot\', title: \'热门排行\', icon: \'fire\' },\n      { path: \'/dashboard/pipeline\', title: \'任务视图\', icon: \'modx\' }\n    ]\n  },\n  {\n    title: \'全局配置\',\n    icon: \'cogs\',\n    children: [\n      { path: \'/env\', title: \'环境配置\', icon: \'cog\' },\n      { path: \'/tag\', title: \'标签配置\', icon: \'tags\' },\n      { path: \'/workorder/mgmt\', title: \'工单配置\', icon: \'ticket\' },\n      { path: \'/setting/keybox\', title: \'密钥管理\', icon: \'key\' },\n      { path: \'/setting/global\', title: \'系统参数\', icon: \'cog\' }\n    ]\n  },\n  {\n    title: \'应用配置\',\n    icon: \'codepen\',\n    children: [\n      { path: \'/application\', title: \'应用\', iconSvg: \'application\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/continuous-integration\', title: \'持续集成\', icon: \'envira\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/web-terminal\', title: \'Web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'Caesar配置\',\n    icon: \'connectdevelop\',\n    children: [\n      { path: \'/caesar/instance\', title: \'实例管理\', iconSvg: \'caesar\' }\n    ]\n  },\n  {\n    title: \'Jenkins配置\',\n    icon: \'google-wallet\',\n    children: [\n      { path: \'/jenkins/instance\', title: \'实例管理\', iconSvg: \'jenkins\' },\n      { path: \'/jenkins/job/template\', title: \'任务模版\', iconSvg: \'template\' },\n      { path: \'/jenkins/job/template/version\', title: \'模版版本\', iconSvg: \'template\' }\n    ]\n  },\n  {\n    title: \'Gitlab配置\',\n    icon: \'gitlab\',\n    children: [\n      { path: \'/gitlab/instance\', title: \'实例管理\', icon: \'git\' },\n      { path: \'/gitlab/group\', title: \'群组管理\', icon: \'th-large\' },\n      { path: \'/gitlab/project\', title: \'项目管理\', icon: \'th-list\' }\n    ]\n  },\n  {\n    title: \'存储管理\',\n    icon: \'database\',\n    children: [\n      { path: \'/storage/oss\', title: \'对象存储\', iconSvg: \'oss\' }\n    ]\n  },\n  {\n    title: \'通知中心\',\n    icon: \'volume-control-phone\',\n    children: [\n      { path: \'/dingtalk\', title: \'钉钉配置\', iconSvg: \'dingtalk\' }\n    ]\n  },\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'Kubernetes\',\n    icon: \'dropbox\',\n    children: [\n      { path: \'/kubernetes/application\', title: \'应用管理\', iconSvg: \'k8s-application\' },\n      { path: \'/kubernetes/application/instance\', title: \'应用实例管理\', iconSvg: \'k8s-instance\' },\n      { path: \'/kubernetes/cluster\', title: \'集群管理\', iconSvg: \'kubernetes\' },\n      { path: \'/kubernetes/deployment\', title: \'无状态管理\', iconSvg: \'k8s-deployment\' },\n      { path: \'/kubernetes/service\', title: \'服务管理\', iconSvg: \'k8s-service\' },\n      { path: \'/kubernetes/template\', title: \'模版管理\', iconSvg: \'YAML\' }\n    ]\n  },\n  {\n    title: \'堡垒机\',\n    icon: \'empire\',\n    children: [\n      { path: \'/jump/jumpserver/settings\', title: \'Jump设置\', icon: \'cog\' },\n      { path: \'/jump/jumpserver/user\', title: \'用户管理\', icon: \'user\' },\n      { path: \'/jump/jumpserver/asset\', title: \'资产管理\', icon: \'server\' },\n      { path: \'/term/session\', title: \'Web终端会话\', icon: \'server\' } \n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/user/retired\', title: \'用户离职管理\', icon: \'user\' },\n      { path: \'/org\', title: \'组织架构\', icon:\'sitemap\'},\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible/mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/profile/subscription\', title: \'配置订阅\', iconSvg: \'subscription\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'RBAC配置\',\n    icon: \'address-card\',\n    children: [\n      { path: \'/auth/resource\', title: \'资源管理\', icon: \'modx\' },\n      { path: \'/auth/role\', title: \'角色管理\', icon: \'users\' },\n      { path: \'/auth/user/role\', title: \'用户角色配置\', icon: \'id-card\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 08:56:17', '2021-04-20 15:02:53');
INSERT INTO `auth_menu` VALUES (11, 2, 0, 'admin', '[\n  {\n    title: \'全局配置\',\n    icon: \'cogs\',\n    children: [\n      { path: \'/env\', title: \'环境配置\', icon: \'cog\' },\n      { path: \'/tag\', title: \'标签配置\', icon: \'cog\' }\n    ]\n  },\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      // tencent\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n   {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' }\n    ]\n  }, \n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'堡垒机\',\n    icon: \'empire\',\n    children: [\n      { path: \'/jump/jumpserver/settings\', title: \'设置\', icon: \'cog\' },\n      { path: \'/jump/jumpserver/user\', title: \'用户管理\', icon: \'user\' },\n      { path: \'/jump/jumpserver/asset\', title: \'资产管理\', icon: \'server\' },\n      { path: \'/term/session\', title: \'Web终端会话\', icon: \'server\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'RBAC配置\',\n    icon: \'address-card\',\n    children: [\n      { path: \'/auth/resource\', title: \'资源管理\', icon: \'modx\' },\n      { path: \'/auth/role\', title: \'角色管理\', icon: \'users\' },\n      { path: \'/auth/user/role\', title: \'用户角色配置\', icon: \'id-card\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible-mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 11:12:46', '2020-07-05 20:54:39');
INSERT INTO `auth_menu` VALUES (12, 3, 0, 'ops', '[\n  {\n    title: \'全局配置\',\n    icon: \'cogs\',\n    children: [\n      { path: \'/env\', title: \'环境配置\', icon: \'cog\' },\n      { path: \'/tag\', title: \'标签配置\', icon: \'cog\' }\n    ]\n  },\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      // tencent\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n   {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' }\n    ]\n  },\n   {\n    title: \'Kubernetes\',\n    icon: \'dropbox\',\n    children: [\n      { path: \'/kubernetes/application\', title: \'应用管理\', iconSvg: \'k8s-application\' },\n      { path: \'/kubernetes/application/instance\', title: \'应用实例管理\', iconSvg: \'k8s-instance\' },\n      { path: \'/kubernetes/cluster\', title: \'集群管理\', iconSvg: \'kubernetes\' },\n      { path: \'/kubernetes/deployment\', title: \'无状态管理\', iconSvg: \'k8s-deployment\' },\n      { path: \'/kubernetes/service\', title: \'服务管理\', iconSvg: \'k8s-service\' },\n      { path: \'/kubernetes/template\', title: \'模版管理\', iconSvg: \'YAML\' }\n    ]\n  },\n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'堡垒机\',\n    icon: \'empire\',\n    children: [\n      { path: \'/jump/jumpserver/settings\', title: \'设置\', icon: \'cog\' },\n      { path: \'/jump/jumpserver/user\', title: \'用户管理\', icon: \'user\' },\n      { path: \'/jump/jumpserver/asset\', title: \'资产管理\', icon: \'server\' },\n      { path: \'/term/session\', title: \'Web终端会话\', icon: \'server\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n   {\n    title: \'RBAC配置\',\n    icon: \'address-card\',\n    children: [\n      { path: \'/auth/resource\', title: \'资源管理\', icon: \'modx\' },\n      { path: \'/auth/role\', title: \'角色管理\', icon: \'users\' },\n      { path: \'/auth/user/role\', title: \'用户角色配置\', icon: \'id-card\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible-mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 11:14:17', '2020-07-07 17:55:27');
INSERT INTO `auth_menu` VALUES (13, 4, 0, 'dev', '[\n {\n    title: \'首页\',\n    icon: \'home\',\n    children: [\n      { path: \'/dashboard\', title: \'仪表盘\', icon: \'area-chart\' },\n      { path: \'/dashboard/hot\', title: \'热门排行\', icon: \'fire\' },\n      { path: \'/dashboard/pipeline\', title: \'流水线视图\', icon: \'modx\' }\n    ]\n  },\n {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/continuous-integration\', title: \'持续集成\', icon: \'envira\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' }\n    ]\n  },\n  {\n    title: \'应用配置\',\n    icon: \'codepen\',\n    children: [\n      { path: \'/application\', title: \'应用\', iconSvg: \'application\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 11:17:22', '2021-04-06 11:27:06');
INSERT INTO `auth_menu` VALUES (14, 5, 0, 'base', '[\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-04-23 11:18:04', '2020-04-23 11:18:04');
INSERT INTO `auth_menu` VALUES (15, 13, 0, 'aliyun_log_admin', '[\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n  {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' },\n      { path: \'/cloud/aliyun/log\', title: \'日志服务管理\', iconSvg: \'aliyun-log\' }\n    ]\n  },\n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', icon: \'object-ungroup\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible-mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/ansible\', title: \'playbook\', icon: \'recycle\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n', '2020-06-15 17:45:51', '2020-06-15 17:46:32');
INSERT INTO `auth_menu` VALUES (16, 15, 0, 'pangu', '', '2020-07-10 11:36:38', '2020-07-10 11:36:38');
COMMIT;

-- ----------------------------
-- Table structure for auth_resource
-- ----------------------------
DROP TABLE IF EXISTS `auth_resource`;
CREATE TABLE `auth_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL COMMENT '资源组id',
  `resource_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资源名称',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `need_auth` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否鉴权',
  `ui` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户界面',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `resource_name` (`resource_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=542 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_resource
-- ----------------------------
BEGIN;
INSERT INTO `auth_resource` VALUES (1, 1, '/api/log/login', '用户登录接口', 0, 0, '2020-02-18 18:39:09', '2021-06-30 18:24:51');
INSERT INTO `auth_resource` VALUES (2, 1, '/api/auth/role/page/query', '角色分页查询', 1, 0, '2020-02-14 10:40:38', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (3, 1, '/api/auth/role/add', '新增角色', 1, 0, '2020-02-14 20:27:57', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (4, 1, '/api/auth/role/update', '更新角色配置', 1, 0, '2020-02-14 20:28:11', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (5, 1, '/api/auth/role/del', '删除角色', 1, 0, '2020-02-14 20:28:36', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (6, 1, '/api/auth/resource/page/query', '资源分页查询', 1, 0, '2020-02-14 22:11:59', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (7, 1, '/api/auth/resource/add', '新增资源', 1, 1, '2020-02-14 22:12:14', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (8, 1, '/api/auth/resource/update', '更新资源配置', 1, 1, '2020-02-14 22:12:23', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (9, 1, '/api/auth/resource/del', '删除资源', 1, 0, '2020-02-14 22:12:33', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (10, 1, '/api/auth/group/page/query', '资源组分页查询', 1, 0, '2020-02-15 17:33:30', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (15, 1, '/api/auth/group/add', '新增资源组', 1, 0, '2020-02-18 13:57:54', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (16, 1, '/api/auth/group/del', '删除资源组', 1, 0, '2020-02-18 13:58:03', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (17, 1, '/api/auth/group/update', '更新资源组', 1, 0, '2020-02-18 13:58:27', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (27, 1, '/api/auth/role/resource/bind/page/query', '分页查询角色绑定的资源列表', 1, 0, '2020-02-19 12:08:55', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (29, 1, '/api/auth/role/resource/add', '角色资源关联', 1, 0, '2020-02-19 19:04:14', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (30, 1, '/api/auth/role/resource/del', '角色资源删除', 1, 0, '2020-02-19 19:04:22', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (31, 1, '/api/auth/user/role/page/query', '用户角色分页查询', 1, 0, '2020-02-20 10:36:25', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (32, 2, '/api/user/page/query', '用户查询接口', 1, 0, '2020-02-20 11:59:30', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (33, 1, '/api/auth/user/role/add', '新增用户角色配置', 1, 0, '2020-02-20 12:33:40', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (34, 1, '/api/auth/user/role/del', '删除用户角色配置', 1, 0, '2020-02-20 12:34:01', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (165, 1, '/api/auth/menu/query', '用户菜单查询', 1, 0, '2020-04-23 09:41:41', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (188, 1, '/api/auth/role/menu/save', '保存角色菜单配置', 1, 1, '2020-05-19 10:47:21', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (189, 1, '/api/auth/role/menu/query', '查询角色菜单配置', 1, 0, '2020-05-19 10:47:40', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (236, 1, '/api/home', '首页', 0, 0, '2020-06-16 19:23:05', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (386, 1, '/api/auth/token/revoke', '吊销所有用户令牌', 1, 0, '2021-04-19 13:59:19', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (389, 40, '/api/datasource/config/page/query', '分页查询数据源配置', 1, 0, '2021-05-15 15:45:34', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (390, 40, '/api/datasource/config/add', '新增数据源配置', 1, 0, '2021-05-17 11:31:50', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (391, 40, '/api/datasource/config/update', '更新数据源配置', 1, 0, '2021-05-17 11:32:11', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (392, 41, '/api/sys/credential/kind/options/get', '查询系统凭据分类选项', 1, 0, '2021-05-17 17:27:17', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (393, 41, '/api/sys/credential/page/query', '分页查询系统凭据列表', 1, 0, '2021-05-17 17:45:36', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (394, 41, '/api/sys/credential/add', '新增系统凭据配置', 1, 0, '2021-05-18 11:08:17', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (395, 41, '/api/sys/credential/update', '更新系统凭据配置', 1, 0, '2021-05-18 11:08:30', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (396, 42, '/api/tag/page/query', '分页查询标签列表', 1, 0, '2021-05-19 15:05:44', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (397, 42, '/api/tag/add', '新增标签信息', 1, 0, '2021-05-19 15:06:00', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (398, 42, '/api/tag/update', '更新标签信息', 1, 0, '2021-05-19 15:06:11', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (399, 42, '/api/tag/business/options/get', '查询业务类型选项', 1, 0, '2021-05-19 15:36:29', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (400, 40, '/api/datasource/instance/register', '注册数据源实例', 1, 0, '2021-05-19 18:12:34', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (401, 40, '/api/datasource/instance/query', '查询数据源实例', 1, 0, '2021-05-20 09:32:40', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (402, 42, '/api/tag/business/type/get', '按类型查询所有标签', 1, 0, '2021-05-20 12:52:05', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (404, 42, '/api/tag/business/update', '更新业务标签', 1, 0, '2021-05-20 13:32:37', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (405, 40, '/api/datasource/config/type/options/get', '查询数据源配置类型选项', 1, 0, '2021-05-21 10:36:20', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (406, 43, '/api/server/group/type/page/query', '分页查询服务器组类型列表', 1, 0, '2021-05-24 11:19:04', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (407, 43, '/api/server/group/type/add', '新增服务器组类型', 1, 0, '2021-05-24 11:19:33', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (408, 43, '/api/server/group/type/update', '更新服务器组类型', 1, 0, '2021-05-24 11:19:53', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (409, 43, '/api/server/group/type/del', '删除指定的服务器组类型', 1, 0, '2021-05-24 11:20:06', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (410, 43, '/api/server/group/page/query', '分页查询服务器组列表', 1, 0, '2021-05-24 14:31:11', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (411, 43, '/api/server/group/add', '新增服务器组', 1, 0, '2021-05-24 14:31:38', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (412, 43, '/api/server/group/update', '更新服务器组', 1, 0, '2021-05-24 15:41:51', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (413, 45, '/api/server/account/page/query', '分页查询服务器账户列表', 1, 0, '2021-05-25 11:44:49', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (414, 44, '/api/server/protocol/options/get', '查询服务器协议选项', 1, 0, '2021-05-25 13:40:14', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (415, 45, '/api/server/account/add', '新增服务器账户', 1, 0, '2021-05-25 14:31:29', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (416, 45, '/api/server/account/update', '更新服务器账户', 1, 0, '2021-05-25 14:31:38', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (417, 46, '/api/sys/env/page/query', '分页查询环境列表', 1, 0, '2021-05-25 17:23:17', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (418, 46, '/api/sys/env/add', '新增环境', 1, 0, '2021-05-25 17:23:30', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (419, 46, '/api/sys/env/update', '更新环境', 1, 0, '2021-05-25 17:23:40', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (420, 46, '/api/sys/env/del', '删除指定的环境', 1, 0, '2021-05-25 17:23:56', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (421, 44, '/api/server/page/query', '分页查询服务器列表', 1, 0, '2021-05-25 18:02:07', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (422, 44, '/api/server/add', '新增服务器', 1, 0, '2021-05-26 10:01:46', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (423, 44, '/api/server/update', '更新服务器', 1, 0, '2021-05-26 10:02:00', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (424, 44, '/api/server/del', '删除指定的服务器', 1, 0, '2021-05-26 10:02:19', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (425, 45, '/api/server/account/permission/update', '更新服务器账户授权', 1, 0, '2021-05-26 15:00:11', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (426, 2, '/api/user/add', '新增用户', 1, 0, '2021-05-27 09:21:29', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (427, 2, '/api/user/update', '更新用户', 1, 0, '2021-05-27 09:21:44', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (428, 2, '/api/user/del', '删除指定的用户', 1, 0, '2021-05-27 09:21:59', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (430, 2, '/api/user/business/permission/revoke', '解除用户业务许可', 1, 0, '2021-05-27 14:49:07', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (431, 2, '/api/user/business/permission/grant', '授予用户业务许可', 1, 0, '2021-05-27 14:56:53', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (433, 2, '/api/user/business/permission/set', '设置用户业务许可（角色）', 1, 0, '2021-05-27 17:36:45', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (434, 2, '/api/user/server/tree/query', '查询用户的服务器树', 1, 0, '2021-05-28 14:43:33', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (435, 1, '/api/auth/user/role/update', '更新用户角色', 1, 0, '2021-06-01 17:43:28', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (436, 47, '/api/sys/menu/save', '保存菜单', 1, 0, '2021-06-02 14:26:45', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (437, 47, '/api/sys/menu/child/save', '保存子菜单', 1, 0, '2021-06-02 14:26:55', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (438, 47, '/api/sys/menu/del', '删除菜单', 1, 0, '2021-06-02 14:27:15', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (439, 47, '/api/sys/menu/child/del', '删除子菜单', 1, 0, '2021-06-02 14:27:25', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (440, 47, '/api/sys/menu/query', '查询菜单', 1, 0, '2021-06-02 14:27:35', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (441, 47, '/api/sys/menu/child/query', '查询子菜单', 1, 0, '2021-06-02 14:27:53', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (442, 47, '/api/sys/menu/tree/query', '查询菜单树', 1, 0, '2021-06-03 15:23:45', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (443, 47, '/api/sys/menu/role/save', '保存角色菜单', 1, 0, '2021-06-03 15:23:56', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (444, 47, '/api/sys/menu/role/query', '查询角色菜单', 1, 0, '2021-06-03 15:24:08', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (445, 47, '/api/sys/menu/role/detail/query', '查询角色菜单详情', 1, 0, '2021-06-03 15:24:18', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (446, 2, '/api/user/ui/info/get', '查询用户前端界面信息(菜单&UI鉴权)', 1, 0, '2021-06-07 09:35:52', '2021-08-04 18:07:27');
INSERT INTO `auth_resource` VALUES (447, 2, '/api/user/details/get', '查询用户详情', 1, 0, '2021-06-09 11:12:46', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (448, 2, '/api/user/credential/save', '保存用户凭据', 1, 0, '2021-06-09 16:06:22', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (453, 48, '/api/sys/doc/preview', '查阅文档', 1, 0, '2021-06-16 13:34:34', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (454, 2, '/api/user/group/page/query', '分页查询用户组列表', 1, 0, '2021-06-16 16:03:20', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (455, 2, '/api/user/business/permission/query', '分页查询用户授权业务对象列表', 1, 0, '2021-06-16 17:10:01', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (456, 40, '/api/datasource/instance/asset/page/query', '分页查询数据源资产列表', 1, 0, '2021-06-18 17:57:30', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (458, 40, '/api/datasource/instance/asset/pull', '拉取数据源资产信息', 1, 0, '2021-06-21 13:27:23', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (459, 40, '/api/datasource/instance/asset/del', '删除指定的资产', 1, 0, '2021-06-22 13:05:46', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (460, 40, '/api/datasource/instance/asset/set/config', '设置数据源配置文件', 1, 0, '2021-06-29 17:20:08', '2021-06-30 18:25:14');
INSERT INTO `auth_resource` VALUES (462, 2, '/api/user/server/remote/query', '查询用户授权的远程服务器', 1, 0, '2021-07-09 16:09:38', '2021-07-09 16:09:38');
INSERT INTO `auth_resource` VALUES (463, 50, '/api/application/kubernetes/page/query', '分页查询容器应用列表', 1, 0, '2021-07-13 17:01:06', '2021-07-13 17:01:06');
INSERT INTO `auth_resource` VALUES (464, 50, '/api/application/page/query', '分页查询应用列表', 1, 0, '2021-07-15 15:49:33', '2021-07-15 15:49:33');
INSERT INTO `auth_resource` VALUES (465, 50, '/api/application/get', '查询应用', 1, 0, '2021-07-16 15:17:04', '2021-08-19 14:51:26');
INSERT INTO `auth_resource` VALUES (466, 50, '/api/application/add', '新增应用', 1, 0, '2021-07-16 15:17:13', '2021-07-16 15:17:13');
INSERT INTO `auth_resource` VALUES (467, 50, '/api/application/update', '更新应用', 1, 0, '2021-07-16 15:17:22', '2021-07-16 15:17:22');
INSERT INTO `auth_resource` VALUES (468, 50, '/api/application/del', '删除应用', 1, 0, '2021-07-16 15:17:28', '2021-07-16 15:17:28');
INSERT INTO `auth_resource` VALUES (469, 50, '/api/application/res/bind', '应用资源绑定', 1, 0, '2021-07-16 15:17:55', '2021-07-16 15:17:55');
INSERT INTO `auth_resource` VALUES (470, 50, '/api/application/res/unbind', '应用资源解除绑定', 1, 0, '2021-07-16 15:18:10', '2021-07-16 15:18:10');
INSERT INTO `auth_resource` VALUES (472, 50, '/api/application/business/options/get', '查询应用业务类型选项', 1, 0, '2021-07-19 15:33:59', '2021-07-19 15:33:59');
INSERT INTO `auth_resource` VALUES (473, 52, '/api/terminal/session/page/query', '分页查询终端会话列表', 1, 0, '2021-07-22 17:30:36', '2021-07-22 17:30:36');
INSERT INTO `auth_resource` VALUES (474, 53, '/', '首页', 0, 0, '2021-07-27 16:48:46', '2021-07-27 16:48:46');
INSERT INTO `auth_resource` VALUES (475, 40, '/api/datasource/instance/asset/business/scan', '扫描资产与业务对象关系', 1, 0, '2021-08-02 17:10:29', '2021-08-02 17:15:54');
INSERT INTO `auth_resource` VALUES (476, 52, '/api/terminal/session/instance/command/page/query', '分页查询终端会话命令列表', 1, 0, '2021-08-03 10:17:11', '2021-08-03 10:17:11');
INSERT INTO `auth_resource` VALUES (477, 2, '/api/user/access/token/grant', '授权用户AccessToken', 1, 0, '2021-08-05 14:23:29', '2021-08-05 14:23:29');
INSERT INTO `auth_resource` VALUES (478, 2, '/api/user/access/token/revoke', '撤销用户AccessToken', 1, 0, '2021-08-05 14:23:42', '2021-08-05 14:23:42');
INSERT INTO `auth_resource` VALUES (479, 2, '/api/user/group/add', '新增用户组', 1, 0, '2021-08-06 17:16:54', '2021-08-06 17:16:54');
INSERT INTO `auth_resource` VALUES (481, 2, '/api/user/group/update', '更新用户组', 1, 0, '2021-08-06 17:17:17', '2021-08-06 17:17:17');
INSERT INTO `auth_resource` VALUES (482, 2, '/api/user/group/del', '删除用户组', 1, 0, '2021-08-09 11:07:42', '2021-08-09 11:07:42');
INSERT INTO `auth_resource` VALUES (483, 2, '/api/user/permission/business/query', '分页查询业务对象授权的用户列表', 1, 0, '2021-08-10 18:07:01', '2021-08-10 18:07:01');
INSERT INTO `auth_resource` VALUES (484, 54, '/api/business/property/add', '新增业务对象属性配置', 1, 0, '2021-08-20 14:55:47', '2021-08-20 14:55:47');
INSERT INTO `auth_resource` VALUES (485, 54, '/api/business/property/update', '更新业务对象属性配置', 1, 0, '2021-08-20 14:55:59', '2021-08-20 14:55:59');
INSERT INTO `auth_resource` VALUES (486, 43, '/api/server/group/del', '删除服务器组配置', 1, 0, '2021-08-25 10:16:08', '2021-08-25 10:16:08');
INSERT INTO `auth_resource` VALUES (487, 40, '/api/datasource/instance/asset/subscription/page/query', '分页查询数据源资产订阅列表', 1, 0, '2021-08-27 16:53:21', '2021-08-27 16:53:21');
INSERT INTO `auth_resource` VALUES (488, 40, '/api/datasource/instance/asset/subscription/add', '新增数据源资产订阅', 1, 0, '2021-08-30 16:21:02', '2021-08-30 16:21:02');
INSERT INTO `auth_resource` VALUES (489, 40, '/api/datasource/instance/asset/subscription/update', '更新数据源资产订阅', 1, 0, '2021-08-30 16:21:17', '2021-08-30 16:21:17');
INSERT INTO `auth_resource` VALUES (490, 40, '/api/datasource/instance/asset/subscription/del', '删除指定的数据源资产订阅', 1, 0, '2021-08-30 16:21:36', '2021-08-30 16:21:36');
INSERT INTO `auth_resource` VALUES (491, 40, '/api/datasource/instance/asset/subscription/publish', '发布数据源资产订阅', 1, 0, '2021-08-31 15:38:50', '2021-08-31 15:38:50');
INSERT INTO `auth_resource` VALUES (492, 55, '/api/task/ansible/playbook/page/query', '分页查询剧本列表', 1, 0, '2021-09-01 14:16:47', '2021-09-01 14:16:47');
INSERT INTO `auth_resource` VALUES (493, 55, '/api/task/ansible/playbook/add', '新增剧本配置', 1, 0, '2021-09-01 18:20:17', '2021-09-01 18:20:17');
INSERT INTO `auth_resource` VALUES (494, 55, '/api/task/ansible/playbook/update', '更新剧本配置', 1, 0, '2021-09-01 18:20:32', '2021-09-01 18:20:32');
INSERT INTO `auth_resource` VALUES (495, 55, '/api/task/ansible/playbook/del', '删除剧本配置', 1, 0, '2021-09-01 18:20:46', '2021-09-01 18:20:46');
INSERT INTO `auth_resource` VALUES (496, 41, '/api/instance/registered/page/query', '分页查询注册实例列表', 1, 0, '2021-09-06 11:23:45', '2021-09-06 11:23:45');
INSERT INTO `auth_resource` VALUES (497, 41, '/api/instance/registered/active/set', '设置注册实例的有效/无效', 1, 0, '2021-09-06 15:28:03', '2021-09-06 15:28:41');
INSERT INTO `auth_resource` VALUES (499, 41, '/api/instance/health/lb-check', '负载均衡健康检查接口', 0, 0, '2021-09-06 16:17:32', '2021-09-06 16:20:36');
INSERT INTO `auth_resource` VALUES (500, 50, '/api/application/res/preview/page/query', '预览应用资源', 1, 0, '2021-09-09 09:48:40', '2021-09-09 09:48:40');
INSERT INTO `auth_resource` VALUES (501, 2, '/api/user/sync', '同步用户与用户关系（数据源实例）', 1, 0, '2021-09-16 10:45:44', '2021-09-16 10:45:44');
INSERT INTO `auth_resource` VALUES (502, 56, '/api/datasource/aliyun/log/page/query', '分页查询阿里云日志服务列表', 1, 0, '2021-09-17 15:05:33', '2021-09-17 15:05:33');
INSERT INTO `auth_resource` VALUES (503, 56, '/api/datasource/aliyun/log/del', '删除阿里云日志服务', 1, 0, '2021-09-17 15:09:06', '2021-09-17 15:09:23');
INSERT INTO `auth_resource` VALUES (504, 56, '/api/datasource/aliyun/log/project/query', '查询阿里云日志服务项目列表', 1, 0, '2021-09-17 15:34:03', '2021-09-17 15:34:03');
INSERT INTO `auth_resource` VALUES (505, 56, '/api/datasource/aliyun/log/logstore/query', '查询阿里云日志服务日志库列表', 1, 0, '2021-09-17 15:34:20', '2021-09-17 15:34:20');
INSERT INTO `auth_resource` VALUES (506, 56, '/api/datasource/aliyun/log/config/query', '查询阿里云日志服务配置列表', 1, 0, '2021-09-17 15:34:34', '2021-09-17 15:34:34');
INSERT INTO `auth_resource` VALUES (507, 56, '/api/datasource/aliyun/log/add', '新增阿里云日志服务', 1, 0, '2021-09-17 15:52:49', '2021-09-17 15:52:49');
INSERT INTO `auth_resource` VALUES (508, 56, '/api/datasource/aliyun/log/update', '更新阿里云日志服务', 1, 0, '2021-09-17 15:53:05', '2021-09-17 15:53:05');
INSERT INTO `auth_resource` VALUES (509, 56, '/api/datasource/aliyun/log/member/page/query', '分页查询阿里云日志服务成员列表', 1, 0, '2021-09-17 16:57:57', '2021-09-17 16:57:57');
INSERT INTO `auth_resource` VALUES (510, 56, '/api/datasource/aliyun/log/member/add', '新增阿里云日志服务成员', 1, 0, '2021-09-17 16:58:10', '2021-09-17 16:58:10');
INSERT INTO `auth_resource` VALUES (511, 56, '/api/datasource/aliyun/log/member/update', '更新阿里云日志服务成员', 1, 0, '2021-09-17 16:58:22', '2021-09-17 16:58:22');
INSERT INTO `auth_resource` VALUES (512, 56, '/api/datasource/aliyun/log/member/del', '删除指定的阿里云日志服务成员', 1, 0, '2021-09-17 16:58:36', '2021-09-17 16:58:36');
INSERT INTO `auth_resource` VALUES (513, 56, '/api/datasource/aliyun/log/push', '推送阿里云日志服务配置', 1, 0, '2021-09-17 17:38:31', '2021-09-17 17:38:31');
INSERT INTO `auth_resource` VALUES (514, 56, '/api/datasource/aliyun/log/member/push', '推送阿里云日志服务成员配置', 1, 0, '2021-09-17 17:38:44', '2021-09-17 17:38:44');
INSERT INTO `auth_resource` VALUES (515, 57, '/api/server/task/submit', '提交服务器任务', 1, 0, '2021-09-18 17:06:48', '2021-09-18 17:06:48');
INSERT INTO `auth_resource` VALUES (516, 57, '/api/server/task/page/query', '分页查询服务器任务列表', 1, 0, '2021-09-24 14:50:12', '2021-09-24 14:51:26');
INSERT INTO `auth_resource` VALUES (517, 43, '/api/server/group/env/pattern/query', '查询服务器组环境分组信息(发布平台专用)', 1, 0, '2021-10-25 16:37:26', '2021-10-25 16:37:26');
INSERT INTO `auth_resource` VALUES (518, 41, '/api/sys/credential/del', '删除指定的系统凭据配置', 1, 0, '2021-10-27 13:40:54', '2021-10-27 13:40:54');
INSERT INTO `auth_resource` VALUES (519, 42, '/api/tag/del', '删除指定的标签信息', 1, 0, '2021-10-28 10:12:17', '2021-10-28 10:12:17');
INSERT INTO `auth_resource` VALUES (520, 58, '/api/receive/event/gitlab/v4/system/hooks', 'Gitlab(API:v4)通知接口', 0, 0, '2021-10-29 16:16:08', '2021-10-29 16:16:08');
INSERT INTO `auth_resource` VALUES (522, 1, '/api/log/logout', '用户登出', 1, 0, '2021-11-15 07:03:40', '2021-11-15 07:03:40');
INSERT INTO `auth_resource` VALUES (523, 2, '/api/user/active/set', '设置用户是否有效', 1, 0, '2021-11-23 07:29:59', '2021-11-23 07:29:59');
INSERT INTO `auth_resource` VALUES (524, 59, '/api/template/page/query', '分页查询模版列表', 1, 0, '2021-12-06 06:23:07', '2021-12-06 06:23:07');
INSERT INTO `auth_resource` VALUES (525, 59, '/api/template/business/page/query', '分页查询业务模版列表', 1, 0, '2021-12-06 06:23:26', '2021-12-06 06:23:26');
INSERT INTO `auth_resource` VALUES (526, 59, '/api/template/business/add', '新增业务模版', 1, 0, '2021-12-06 10:07:40', '2021-12-06 10:07:40');
INSERT INTO `auth_resource` VALUES (527, 59, '/api/template/business/update', '更新业务模版', 1, 0, '2021-12-07 02:33:41', '2021-12-07 02:33:41');
INSERT INTO `auth_resource` VALUES (528, 59, '/api/template/business/asset/create', '业务模版创建资产', 1, 0, '2021-12-07 07:14:11', '2021-12-07 07:17:40');
INSERT INTO `auth_resource` VALUES (529, 59, '/api/template/business/del', '删除指定的业务模板', 1, 0, '2021-12-07 11:54:32', '2021-12-07 11:54:32');
INSERT INTO `auth_resource` VALUES (530, 59, '/api/template/add', '新增模板', 1, 0, '2021-12-08 08:11:55', '2021-12-08 08:11:55');
INSERT INTO `auth_resource` VALUES (531, 59, '/api/template/update', '更新模板', 1, 0, '2021-12-08 08:12:07', '2021-12-08 08:12:07');
INSERT INTO `auth_resource` VALUES (532, 59, '/api/template/del', '删除指定的模板', 1, 0, '2021-12-08 08:12:18', '2021-12-08 08:12:18');
INSERT INTO `auth_resource` VALUES (533, 59, '/api/template/business/scan', '扫描业务模板与业务对象的关联关系', 1, 0, '2021-12-08 10:36:05', '2021-12-08 10:36:05');
INSERT INTO `auth_resource` VALUES (534, 2, '/api/user/ram/get', '查询用户RAM授权信息', 1, 0, '2021-12-10 09:11:08', '2021-12-10 09:11:08');
INSERT INTO `auth_resource` VALUES (535, 2, '/api/user/ram/user/create', '创建RAM账户', 1, 0, '2021-12-13 04:49:00', '2021-12-13 04:49:00');
INSERT INTO `auth_resource` VALUES (536, 2, '/api/user/details/username/get', '查询用户详情', 1, 0, '2021-12-13 08:06:53', '2021-12-13 08:06:53');
INSERT INTO `auth_resource` VALUES (537, 2, '/api/user/ram/policy/grant', '授权RAM账户策略', 1, 0, '2021-12-13 10:15:29', '2021-12-13 10:15:29');
INSERT INTO `auth_resource` VALUES (538, 2, '/api/user/ram/policy/revoke', '撤销RAM账户策略', 1, 0, '2021-12-13 10:15:57', '2021-12-13 10:15:57');
INSERT INTO `auth_resource` VALUES (539, 40, '/api/datasource/instance/asset/active/set', '设置数据源资产是否有效', 1, 0, '2021-12-14 08:30:38', '2021-12-14 08:30:38');
INSERT INTO `auth_resource` VALUES (540, 40, '/api/datasource/instance/asset/push', '推送数据源资产信息', 1, 0, '2021-12-17 03:24:32', '2021-12-17 03:24:32');
INSERT INTO `auth_resource` VALUES (541, 40, '/api/datasource/instance/id/query', 'id查询数据源实例', 1, 0, '2021-12-20 08:49:21', '2021-12-20 08:49:21');
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
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `role_name` (`role_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_role
-- ----------------------------
BEGIN;
INSERT INTO `auth_role` VALUES (1, 'super_admin', 100, '超级管理员[OC开发者]', 0, '2020-02-18 13:00:50', '2020-06-13 15:55:10');
INSERT INTO `auth_role` VALUES (2, 'admin', 90, '管理员', 0, '2020-02-14 17:05:06', '2020-04-11 02:10:37');
INSERT INTO `auth_role` VALUES (3, 'ops', 50, '运维', 0, '2020-02-14 17:05:16', '2021-11-08 04:22:02');
INSERT INTO `auth_role` VALUES (4, 'dev', 40, '研发', 1, '2020-02-14 17:05:25', '2020-04-11 02:10:43');
INSERT INTO `auth_role` VALUES (5, 'base', 10, '普通用户', 1, '2020-02-14 17:05:31', '2021-05-12 10:24:30');
COMMIT;

-- ----------------------------
-- Table structure for auth_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_menu`;
CREATE TABLE `auth_role_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_child_id` int(11) NOT NULL COMMENT '子菜单id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=563 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `auth_role_menu` VALUES (105, 2, 1, '2021-06-07 18:20:30', '2021-06-07 18:20:30');
INSERT INTO `auth_role_menu` VALUES (106, 2, 2, '2021-06-07 18:20:30', '2021-06-07 18:20:30');
INSERT INTO `auth_role_menu` VALUES (107, 2, 4, '2021-06-07 18:20:30', '2021-06-07 18:20:30');
INSERT INTO `auth_role_menu` VALUES (108, 2, 6, '2021-06-07 18:20:30', '2021-06-07 18:20:30');
INSERT INTO `auth_role_menu` VALUES (109, 2, 7, '2021-06-07 18:20:30', '2021-06-07 18:20:30');
INSERT INTO `auth_role_menu` VALUES (110, 2, 8, '2021-06-07 18:20:30', '2021-06-07 18:20:30');
INSERT INTO `auth_role_menu` VALUES (111, 2, 9, '2021-06-07 18:20:30', '2021-06-07 18:20:30');
INSERT INTO `auth_role_menu` VALUES (112, 2, 10, '2021-06-07 18:20:30', '2021-06-07 18:20:30');
INSERT INTO `auth_role_menu` VALUES (113, 2, 11, '2021-06-07 18:20:30', '2021-06-07 18:20:30');
INSERT INTO `auth_role_menu` VALUES (220, 4, 16, '2021-07-20 10:55:05', '2021-07-20 10:55:05');
INSERT INTO `auth_role_menu` VALUES (221, 4, 6, '2021-07-20 10:55:05', '2021-07-20 10:55:05');
INSERT INTO `auth_role_menu` VALUES (222, 4, 19, '2021-07-20 10:55:05', '2021-07-20 10:55:05');
INSERT INTO `auth_role_menu` VALUES (223, 4, 7, '2021-07-20 10:55:05', '2021-07-20 10:55:05');
INSERT INTO `auth_role_menu` VALUES (224, 4, 8, '2021-07-20 10:55:05', '2021-07-20 10:55:05');
INSERT INTO `auth_role_menu` VALUES (225, 4, 9, '2021-07-20 10:55:05', '2021-07-20 10:55:05');
INSERT INTO `auth_role_menu` VALUES (226, 4, 10, '2021-07-20 10:55:05', '2021-07-20 10:55:05');
INSERT INTO `auth_role_menu` VALUES (227, 4, 15, '2021-07-20 10:55:05', '2021-07-20 10:55:05');
INSERT INTO `auth_role_menu` VALUES (537, 5, 15, '2021-12-10 10:42:17', '2021-12-10 10:42:17');
INSERT INTO `auth_role_menu` VALUES (538, 1, 1, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (539, 1, 2, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (540, 1, 3, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (541, 1, 4, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (542, 1, 21, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (543, 1, 24, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (544, 1, 16, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (545, 1, 6, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (546, 1, 17, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (547, 1, 19, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (548, 1, 20, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (549, 1, 7, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (550, 1, 26, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (551, 1, 27, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (552, 1, 8, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (553, 1, 9, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (554, 1, 23, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (555, 1, 10, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (556, 1, 11, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (557, 1, 25, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (558, 1, 22, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (559, 1, 12, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (560, 1, 14, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (561, 1, 13, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
INSERT INTO `auth_role_menu` VALUES (562, 1, 15, '2021-12-13 09:27:48', '2021-12-13 09:27:48');
COMMIT;

-- ----------------------------
-- Table structure for auth_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_resource`;
CREATE TABLE `auth_role_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `resource_id` int(11) NOT NULL COMMENT '资源id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `role_id` (`role_id`,`resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_role_resource
-- ----------------------------
BEGIN;
INSERT INTO `auth_role_resource` VALUES (2, 1, 2, '2021-05-15 14:14:59', '2021-05-15 14:14:59');
INSERT INTO `auth_role_resource` VALUES (3, 1, 10, '2021-05-15 14:14:59', '2021-05-15 14:14:59');
INSERT INTO `auth_role_resource` VALUES (4, 1, 6, '2021-05-15 14:17:03', '2021-05-15 14:17:03');
INSERT INTO `auth_role_resource` VALUES (5, 1, 32, '2021-05-15 14:17:13', '2021-05-15 14:17:13');
INSERT INTO `auth_role_resource` VALUES (6, 1, 15, '2021-05-15 15:44:35', '2021-05-15 15:44:35');
INSERT INTO `auth_role_resource` VALUES (7, 1, 7, '2021-05-15 15:45:34', '2021-05-15 15:45:34');
INSERT INTO `auth_role_resource` VALUES (8, 1, 389, '2021-05-15 15:45:37', '2021-05-15 15:45:37');
INSERT INTO `auth_role_resource` VALUES (9, 1, 4, '2021-05-17 11:10:50', '2021-05-17 11:10:50');
INSERT INTO `auth_role_resource` VALUES (10, 1, 391, '2021-05-17 13:50:07', '2021-05-17 13:50:07');
INSERT INTO `auth_role_resource` VALUES (11, 1, 392, '2021-05-17 17:27:21', '2021-05-17 17:27:21');
INSERT INTO `auth_role_resource` VALUES (12, 1, 393, '2021-05-17 17:45:39', '2021-05-17 17:45:39');
INSERT INTO `auth_role_resource` VALUES (13, 1, 395, '2021-05-18 11:08:40', '2021-05-18 11:08:40');
INSERT INTO `auth_role_resource` VALUES (14, 1, 396, '2021-05-19 15:50:02', '2021-05-19 15:50:02');
INSERT INTO `auth_role_resource` VALUES (15, 1, 399, '2021-05-19 16:03:10', '2021-05-19 16:03:10');
INSERT INTO `auth_role_resource` VALUES (16, 1, 397, '2021-05-19 16:30:22', '2021-05-19 16:30:22');
INSERT INTO `auth_role_resource` VALUES (17, 1, 400, '2021-05-19 18:18:32', '2021-05-19 18:18:32');
INSERT INTO `auth_role_resource` VALUES (18, 1, 401, '2021-05-20 09:32:42', '2021-05-20 09:32:42');
INSERT INTO `auth_role_resource` VALUES (19, 1, 402, '2021-05-20 13:27:47', '2021-05-20 13:27:47');
INSERT INTO `auth_role_resource` VALUES (20, 1, 404, '2021-05-20 13:32:47', '2021-05-20 13:32:47');
INSERT INTO `auth_role_resource` VALUES (21, 1, 405, '2021-05-21 10:43:14', '2021-05-21 10:43:14');
INSERT INTO `auth_role_resource` VALUES (22, 1, 390, '2021-05-21 10:48:02', '2021-05-21 10:48:02');
INSERT INTO `auth_role_resource` VALUES (23, 1, 394, '2021-05-21 11:23:12', '2021-05-21 11:23:12');
INSERT INTO `auth_role_resource` VALUES (24, 1, 406, '2021-05-24 11:20:31', '2021-05-24 11:20:31');
INSERT INTO `auth_role_resource` VALUES (25, 1, 408, '2021-05-24 12:45:36', '2021-05-24 12:45:36');
INSERT INTO `auth_role_resource` VALUES (26, 1, 410, '2021-05-24 14:32:18', '2021-05-24 14:32:18');
INSERT INTO `auth_role_resource` VALUES (27, 1, 411, '2021-05-24 15:02:27', '2021-05-24 15:02:27');
INSERT INTO `auth_role_resource` VALUES (28, 1, 412, '2021-05-24 15:42:03', '2021-05-24 15:42:03');
INSERT INTO `auth_role_resource` VALUES (29, 1, 413, '2021-05-25 13:14:49', '2021-05-25 13:14:49');
INSERT INTO `auth_role_resource` VALUES (30, 1, 414, '2021-05-25 13:45:38', '2021-05-25 13:45:38');
INSERT INTO `auth_role_resource` VALUES (31, 1, 415, '2021-05-25 14:31:50', '2021-05-25 14:31:50');
INSERT INTO `auth_role_resource` VALUES (32, 1, 416, '2021-05-25 14:43:37', '2021-05-25 14:43:37');
INSERT INTO `auth_role_resource` VALUES (33, 1, 417, '2021-05-25 17:23:59', '2021-05-25 17:23:59');
INSERT INTO `auth_role_resource` VALUES (34, 1, 419, '2021-05-25 17:40:55', '2021-05-25 17:40:55');
INSERT INTO `auth_role_resource` VALUES (35, 1, 421, '2021-05-25 18:02:16', '2021-05-25 18:02:16');
INSERT INTO `auth_role_resource` VALUES (36, 1, 407, '2021-05-26 09:56:26', '2021-05-26 09:56:26');
INSERT INTO `auth_role_resource` VALUES (37, 1, 422, '2021-05-26 10:02:43', '2021-05-26 10:02:43');
INSERT INTO `auth_role_resource` VALUES (38, 1, 423, '2021-05-26 10:47:50', '2021-05-26 10:47:50');
INSERT INTO `auth_role_resource` VALUES (39, 1, 424, '2021-05-26 11:28:45', '2021-05-26 11:28:45');
INSERT INTO `auth_role_resource` VALUES (40, 1, 425, '2021-05-26 15:00:22', '2021-05-26 15:00:22');
INSERT INTO `auth_role_resource` VALUES (41, 1, 17, '2021-05-27 09:28:02', '2021-05-27 09:28:02');
INSERT INTO `auth_role_resource` VALUES (42, 1, 427, '2021-05-27 11:22:12', '2021-05-27 11:22:12');
INSERT INTO `auth_role_resource` VALUES (44, 1, 430, '2021-05-27 14:54:03', '2021-05-27 14:54:03');
INSERT INTO `auth_role_resource` VALUES (45, 1, 8, '2021-05-27 14:56:41', '2021-05-27 14:56:41');
INSERT INTO `auth_role_resource` VALUES (46, 1, 431, '2021-05-27 15:45:18', '2021-05-27 15:45:18');
INSERT INTO `auth_role_resource` VALUES (48, 1, 27, '2021-05-27 17:23:14', '2021-05-27 17:23:14');
INSERT INTO `auth_role_resource` VALUES (49, 1, 433, '2021-05-27 17:36:53', '2021-05-27 17:36:53');
INSERT INTO `auth_role_resource` VALUES (50, 1, 434, '2021-05-28 15:37:12', '2021-05-28 15:37:12');
INSERT INTO `auth_role_resource` VALUES (51, 1, 435, '2021-06-01 17:43:37', '2021-06-01 17:43:37');
INSERT INTO `auth_role_resource` VALUES (52, 1, 440, '2021-06-02 14:27:55', '2021-06-02 14:27:55');
INSERT INTO `auth_role_resource` VALUES (53, 1, 436, '2021-06-02 14:29:02', '2021-06-02 14:29:02');
INSERT INTO `auth_role_resource` VALUES (54, 1, 441, '2021-06-02 14:31:31', '2021-06-02 14:31:31');
INSERT INTO `auth_role_resource` VALUES (55, 1, 437, '2021-06-02 14:37:00', '2021-06-02 14:37:00');
INSERT INTO `auth_role_resource` VALUES (56, 1, 438, '2021-06-02 15:50:14', '2021-06-02 15:50:14');
INSERT INTO `auth_role_resource` VALUES (57, 1, 442, '2021-06-03 15:24:23', '2021-06-03 15:24:23');
INSERT INTO `auth_role_resource` VALUES (58, 1, 444, '2021-06-03 15:24:25', '2021-06-03 15:24:25');
INSERT INTO `auth_role_resource` VALUES (59, 1, 445, '2021-06-03 15:24:25', '2021-06-03 15:24:25');
INSERT INTO `auth_role_resource` VALUES (60, 1, 443, '2021-06-03 15:26:37', '2021-06-03 15:26:37');
INSERT INTO `auth_role_resource` VALUES (61, 1, 446, '2021-06-07 09:36:06', '2021-06-07 09:36:06');
INSERT INTO `auth_role_resource` VALUES (62, 1, 398, '2021-06-07 18:00:20', '2021-06-07 18:00:20');
INSERT INTO `auth_role_resource` VALUES (63, 1, 447, '2021-06-09 11:12:49', '2021-06-09 11:12:49');
INSERT INTO `auth_role_resource` VALUES (64, 1, 448, '2021-06-09 16:06:44', '2021-06-09 16:06:44');
INSERT INTO `auth_role_resource` VALUES (69, 1, 453, '2021-06-16 13:47:35', '2021-06-16 13:47:35');
INSERT INTO `auth_role_resource` VALUES (70, 1, 454, '2021-06-16 16:06:48', '2021-06-16 16:06:48');
INSERT INTO `auth_role_resource` VALUES (71, 1, 9, '2021-06-16 17:09:22', '2021-06-16 17:09:22');
INSERT INTO `auth_role_resource` VALUES (72, 1, 455, '2021-06-16 17:14:29', '2021-06-16 17:14:29');
INSERT INTO `auth_role_resource` VALUES (73, 1, 456, '2021-06-18 17:57:37', '2021-06-18 17:57:37');
INSERT INTO `auth_role_resource` VALUES (74, 1, 16, '2021-06-19 18:45:07', '2021-06-19 18:45:07');
INSERT INTO `auth_role_resource` VALUES (75, 1, 458, '2021-06-21 16:04:46', '2021-06-21 16:04:46');
INSERT INTO `auth_role_resource` VALUES (76, 1, 459, '2021-06-22 13:06:04', '2021-06-22 13:06:04');
INSERT INTO `auth_role_resource` VALUES (77, 1, 460, '2021-06-29 17:20:14', '2021-06-29 17:20:14');
INSERT INTO `auth_role_resource` VALUES (78, 1, 29, '2021-07-06 11:07:04', '2021-07-06 11:07:04');
INSERT INTO `auth_role_resource` VALUES (79, 4, 2, '2021-07-06 11:07:04', '2021-07-06 11:07:04');
INSERT INTO `auth_role_resource` VALUES (80, 4, 6, '2021-07-06 11:07:08', '2021-07-06 11:07:08');
INSERT INTO `auth_role_resource` VALUES (81, 4, 10, '2021-07-06 11:07:10', '2021-07-06 11:07:10');
INSERT INTO `auth_role_resource` VALUES (82, 4, 27, '2021-07-06 11:07:13', '2021-07-06 11:07:13');
INSERT INTO `auth_role_resource` VALUES (83, 4, 31, '2021-07-06 11:07:17', '2021-07-06 11:07:17');
INSERT INTO `auth_role_resource` VALUES (84, 4, 165, '2021-07-06 11:07:19', '2021-07-06 11:07:19');
INSERT INTO `auth_role_resource` VALUES (85, 4, 189, '2021-07-06 11:07:20', '2021-07-06 11:07:20');
INSERT INTO `auth_role_resource` VALUES (86, 4, 454, '2021-07-06 11:07:26', '2021-07-06 11:07:26');
INSERT INTO `auth_role_resource` VALUES (87, 4, 455, '2021-07-06 11:07:27', '2021-07-06 11:07:27');
INSERT INTO `auth_role_resource` VALUES (88, 4, 389, '2021-07-06 11:07:29', '2021-07-06 11:07:29');
INSERT INTO `auth_role_resource` VALUES (89, 4, 401, '2021-07-06 11:07:32', '2021-07-06 11:07:32');
INSERT INTO `auth_role_resource` VALUES (90, 4, 405, '2021-07-06 11:07:33', '2021-07-06 11:07:33');
INSERT INTO `auth_role_resource` VALUES (91, 4, 456, '2021-07-06 11:07:34', '2021-07-06 11:07:34');
INSERT INTO `auth_role_resource` VALUES (92, 4, 392, '2021-07-06 11:07:39', '2021-07-06 11:07:39');
INSERT INTO `auth_role_resource` VALUES (93, 4, 393, '2021-07-06 11:07:40', '2021-07-06 11:07:40');
INSERT INTO `auth_role_resource` VALUES (94, 4, 396, '2021-07-06 11:07:45', '2021-07-06 11:07:45');
INSERT INTO `auth_role_resource` VALUES (95, 4, 399, '2021-07-06 11:07:46', '2021-07-06 11:07:46');
INSERT INTO `auth_role_resource` VALUES (96, 4, 402, '2021-07-06 11:07:47', '2021-07-06 11:07:47');
INSERT INTO `auth_role_resource` VALUES (97, 4, 406, '2021-07-06 11:07:51', '2021-07-06 11:07:51');
INSERT INTO `auth_role_resource` VALUES (98, 4, 410, '2021-07-06 11:07:52', '2021-07-06 11:07:52');
INSERT INTO `auth_role_resource` VALUES (99, 4, 414, '2021-07-06 11:07:57', '2021-07-06 11:07:57');
INSERT INTO `auth_role_resource` VALUES (100, 4, 421, '2021-07-06 11:07:58', '2021-07-06 11:07:58');
INSERT INTO `auth_role_resource` VALUES (101, 4, 413, '2021-07-06 11:08:01', '2021-07-06 11:08:01');
INSERT INTO `auth_role_resource` VALUES (102, 4, 417, '2021-07-06 11:08:06', '2021-07-06 11:08:06');
INSERT INTO `auth_role_resource` VALUES (103, 4, 440, '2021-07-06 11:08:11', '2021-07-06 11:08:11');
INSERT INTO `auth_role_resource` VALUES (104, 4, 441, '2021-07-06 11:08:11', '2021-07-06 11:08:11');
INSERT INTO `auth_role_resource` VALUES (105, 4, 442, '2021-07-06 11:08:12', '2021-07-06 11:08:12');
INSERT INTO `auth_role_resource` VALUES (106, 4, 444, '2021-07-06 11:08:14', '2021-07-06 11:08:14');
INSERT INTO `auth_role_resource` VALUES (107, 4, 445, '2021-07-06 11:08:15', '2021-07-06 11:08:15');
INSERT INTO `auth_role_resource` VALUES (108, 4, 453, '2021-07-06 11:08:21', '2021-07-06 11:08:21');
INSERT INTO `auth_role_resource` VALUES (109, 4, 446, '2021-07-06 11:09:21', '2021-07-06 11:09:21');
INSERT INTO `auth_role_resource` VALUES (110, 4, 32, '2021-07-06 11:09:24', '2021-07-06 11:09:24');
INSERT INTO `auth_role_resource` VALUES (111, 4, 434, '2021-07-06 11:09:28', '2021-07-06 11:09:28');
INSERT INTO `auth_role_resource` VALUES (112, 4, 447, '2021-07-06 11:09:29', '2021-07-06 11:09:29');
INSERT INTO `auth_role_resource` VALUES (113, 1, 439, '2021-07-09 14:51:27', '2021-07-09 14:51:27');
INSERT INTO `auth_role_resource` VALUES (114, 1, 462, '2021-07-09 16:11:33', '2021-07-09 16:11:33');
INSERT INTO `auth_role_resource` VALUES (115, 1, 463, '2021-07-13 17:02:45', '2021-07-13 17:02:45');
INSERT INTO `auth_role_resource` VALUES (116, 1, 464, '2021-07-15 15:49:37', '2021-07-15 15:49:37');
INSERT INTO `auth_role_resource` VALUES (117, 1, 465, '2021-07-16 15:22:33', '2021-07-16 15:22:33');
INSERT INTO `auth_role_resource` VALUES (119, 1, 467, '2021-07-16 15:28:46', '2021-07-16 15:28:46');
INSERT INTO `auth_role_resource` VALUES (120, 1, 466, '2021-07-16 15:34:50', '2021-07-16 15:34:50');
INSERT INTO `auth_role_resource` VALUES (121, 1, 470, '2021-07-16 16:02:38', '2021-07-16 16:02:38');
INSERT INTO `auth_role_resource` VALUES (123, 1, 469, '2021-07-19 14:12:44', '2021-07-19 14:12:44');
INSERT INTO `auth_role_resource` VALUES (124, 1, 472, '2021-07-19 15:34:06', '2021-07-19 15:34:06');
INSERT INTO `auth_role_resource` VALUES (125, 4, 463, '2021-07-20 10:56:18', '2021-07-20 10:56:18');
INSERT INTO `auth_role_resource` VALUES (126, 4, 464, '2021-07-20 10:56:19', '2021-07-20 10:56:19');
INSERT INTO `auth_role_resource` VALUES (127, 4, 465, '2021-07-20 10:56:19', '2021-07-20 10:56:19');
INSERT INTO `auth_role_resource` VALUES (128, 4, 472, '2021-07-20 10:56:23', '2021-07-20 10:56:23');
INSERT INTO `auth_role_resource` VALUES (129, 1, 473, '2021-07-22 17:54:03', '2021-07-22 17:54:03');
INSERT INTO `auth_role_resource` VALUES (130, 1, 475, '2021-08-02 17:16:04', '2021-08-02 17:16:04');
INSERT INTO `auth_role_resource` VALUES (131, 1, 476, '2021-08-03 10:25:49', '2021-08-03 10:25:49');
INSERT INTO `auth_role_resource` VALUES (132, 1, 426, '2021-08-03 16:42:40', '2021-08-03 16:42:40');
INSERT INTO `auth_role_resource` VALUES (133, 1, 477, '2021-08-05 14:23:52', '2021-08-05 14:23:52');
INSERT INTO `auth_role_resource` VALUES (134, 1, 478, '2021-08-05 14:44:46', '2021-08-05 14:44:46');
INSERT INTO `auth_role_resource` VALUES (135, 1, 479, '2021-08-09 09:28:24', '2021-08-09 09:28:24');
INSERT INTO `auth_role_resource` VALUES (136, 1, 482, '2021-08-09 11:08:05', '2021-08-09 11:08:05');
INSERT INTO `auth_role_resource` VALUES (137, 1, 483, '2021-08-10 18:16:58', '2021-08-10 18:16:58');
INSERT INTO `auth_role_resource` VALUES (138, 1, 468, '2021-08-16 10:12:04', '2021-08-16 10:12:04');
INSERT INTO `auth_role_resource` VALUES (139, 1, 484, '2021-08-20 14:58:25', '2021-08-20 14:58:25');
INSERT INTO `auth_role_resource` VALUES (140, 1, 485, '2021-08-20 15:18:15', '2021-08-20 15:18:15');
INSERT INTO `auth_role_resource` VALUES (141, 1, 486, '2021-08-25 10:16:50', '2021-08-25 10:16:50');
INSERT INTO `auth_role_resource` VALUES (142, 1, 487, '2021-08-27 17:16:10', '2021-08-27 17:16:10');
INSERT INTO `auth_role_resource` VALUES (143, 1, 489, '2021-08-30 16:22:19', '2021-08-30 16:22:19');
INSERT INTO `auth_role_resource` VALUES (144, 1, 488, '2021-08-30 17:36:00', '2021-08-30 17:36:00');
INSERT INTO `auth_role_resource` VALUES (145, 1, 490, '2021-08-30 17:39:33', '2021-08-30 17:39:33');
INSERT INTO `auth_role_resource` VALUES (146, 1, 491, '2021-08-31 15:38:56', '2021-08-31 15:38:56');
INSERT INTO `auth_role_resource` VALUES (147, 1, 492, '2021-09-01 14:16:51', '2021-09-01 14:16:51');
INSERT INTO `auth_role_resource` VALUES (148, 1, 494, '2021-09-02 11:49:46', '2021-09-02 11:49:46');
INSERT INTO `auth_role_resource` VALUES (149, 1, 496, '2021-09-06 11:24:14', '2021-09-06 11:24:14');
INSERT INTO `auth_role_resource` VALUES (150, 1, 497, '2021-09-06 15:37:57', '2021-09-06 15:37:57');
INSERT INTO `auth_role_resource` VALUES (151, 1, 500, '2021-09-09 10:03:49', '2021-09-09 10:03:49');
INSERT INTO `auth_role_resource` VALUES (152, 1, 481, '2021-09-15 14:48:28', '2021-09-15 14:48:28');
INSERT INTO `auth_role_resource` VALUES (153, 1, 501, '2021-09-16 10:49:20', '2021-09-16 10:49:20');
INSERT INTO `auth_role_resource` VALUES (154, 1, 502, '2021-09-17 15:16:51', '2021-09-17 15:16:51');
INSERT INTO `auth_role_resource` VALUES (155, 1, 504, '2021-09-17 15:46:44', '2021-09-17 15:46:44');
INSERT INTO `auth_role_resource` VALUES (156, 1, 505, '2021-09-17 15:46:51', '2021-09-17 15:46:51');
INSERT INTO `auth_role_resource` VALUES (157, 1, 506, '2021-09-17 15:46:53', '2021-09-17 15:46:53');
INSERT INTO `auth_role_resource` VALUES (158, 1, 507, '2021-09-17 15:53:08', '2021-09-17 15:53:08');
INSERT INTO `auth_role_resource` VALUES (159, 1, 503, '2021-09-17 16:20:04', '2021-09-17 16:20:04');
INSERT INTO `auth_role_resource` VALUES (160, 1, 509, '2021-09-17 17:08:20', '2021-09-17 17:08:20');
INSERT INTO `auth_role_resource` VALUES (161, 1, 510, '2021-09-17 17:22:42', '2021-09-17 17:22:42');
INSERT INTO `auth_role_resource` VALUES (162, 1, 512, '2021-09-17 17:28:48', '2021-09-17 17:28:48');
INSERT INTO `auth_role_resource` VALUES (163, 1, 511, '2021-09-17 17:42:54', '2021-09-17 17:42:54');
INSERT INTO `auth_role_resource` VALUES (164, 1, 513, '2021-09-17 17:45:33', '2021-09-17 17:45:33');
INSERT INTO `auth_role_resource` VALUES (165, 1, 515, '2021-09-18 17:07:05', '2021-09-18 17:07:05');
INSERT INTO `auth_role_resource` VALUES (166, 1, 508, '2021-09-22 10:28:40', '2021-09-22 10:28:40');
INSERT INTO `auth_role_resource` VALUES (167, 1, 493, '2021-09-23 15:28:24', '2021-09-23 15:28:24');
INSERT INTO `auth_role_resource` VALUES (168, 1, 516, '2021-09-24 14:52:23', '2021-09-24 14:52:23');
INSERT INTO `auth_role_resource` VALUES (169, 1, 495, '2021-09-28 10:07:49', '2021-09-28 10:07:49');
INSERT INTO `auth_role_resource` VALUES (170, 4, 448, '2021-10-08 14:42:11', '2021-10-08 14:42:11');
INSERT INTO `auth_role_resource` VALUES (171, 1, 517, '2021-10-25 18:36:59', '2021-10-25 18:36:59');
INSERT INTO `auth_role_resource` VALUES (172, 1, 409, '2021-10-25 18:37:02', '2021-10-25 18:37:02');
INSERT INTO `auth_role_resource` VALUES (173, 1, 518, '2021-10-27 14:30:48', '2021-10-27 14:30:48');
INSERT INTO `auth_role_resource` VALUES (174, 1, 519, '2021-10-28 10:12:35', '2021-10-28 10:12:35');
INSERT INTO `auth_role_resource` VALUES (175, 1, 420, '2021-10-28 10:58:03', '2021-10-28 10:58:03');
INSERT INTO `auth_role_resource` VALUES (176, 5, 427, '2021-11-05 05:31:44', '2021-11-05 05:31:44');
INSERT INTO `auth_role_resource` VALUES (177, 5, 522, '2021-11-15 07:04:03', '2021-11-15 07:04:03');
INSERT INTO `auth_role_resource` VALUES (178, 1, 523, '2021-11-29 07:14:49', '2021-11-29 07:14:49');
INSERT INTO `auth_role_resource` VALUES (179, 1, 525, '2021-12-06 06:42:27', '2021-12-06 06:42:27');
INSERT INTO `auth_role_resource` VALUES (180, 1, 524, '2021-12-06 08:54:26', '2021-12-06 08:54:26');
INSERT INTO `auth_role_resource` VALUES (181, 1, 526, '2021-12-06 10:11:46', '2021-12-06 10:11:46');
INSERT INTO `auth_role_resource` VALUES (182, 1, 527, '2021-12-07 02:36:40', '2021-12-07 02:36:40');
INSERT INTO `auth_role_resource` VALUES (183, 1, 528, '2021-12-07 07:14:31', '2021-12-07 07:14:31');
INSERT INTO `auth_role_resource` VALUES (184, 1, 529, '2021-12-07 11:55:15', '2021-12-07 11:55:15');
INSERT INTO `auth_role_resource` VALUES (185, 1, 531, '2021-12-08 08:12:39', '2021-12-08 08:12:39');
INSERT INTO `auth_role_resource` VALUES (186, 1, 530, '2021-12-08 08:17:16', '2021-12-08 08:17:16');
INSERT INTO `auth_role_resource` VALUES (187, 1, 532, '2021-12-08 08:21:10', '2021-12-08 08:21:10');
INSERT INTO `auth_role_resource` VALUES (188, 1, 533, '2021-12-08 10:51:37', '2021-12-08 10:51:37');
INSERT INTO `auth_role_resource` VALUES (189, 1, 428, '2021-12-10 03:02:57', '2021-12-10 03:02:57');
INSERT INTO `auth_role_resource` VALUES (190, 1, 534, '2021-12-10 09:35:01', '2021-12-10 09:35:01');
INSERT INTO `auth_role_resource` VALUES (191, 1, 5, '2021-12-10 10:12:16', '2021-12-10 10:12:16');
INSERT INTO `auth_role_resource` VALUES (192, 1, 535, '2021-12-13 06:54:56', '2021-12-13 06:54:56');
INSERT INTO `auth_role_resource` VALUES (193, 1, 537, '2021-12-13 10:16:41', '2021-12-13 10:16:41');
INSERT INTO `auth_role_resource` VALUES (194, 1, 538, '2021-12-13 11:22:07', '2021-12-13 11:22:07');
INSERT INTO `auth_role_resource` VALUES (195, 1, 536, '2021-12-13 12:52:35', '2021-12-13 12:52:35');
INSERT INTO `auth_role_resource` VALUES (196, 1, 539, '2021-12-14 08:32:55', '2021-12-14 08:32:55');
INSERT INTO `auth_role_resource` VALUES (197, 1, 540, '2021-12-17 03:49:16', '2021-12-17 03:49:16');
INSERT INTO `auth_role_resource` VALUES (198, 1, 541, '2021-12-20 09:08:41', '2021-12-20 09:08:41');
INSERT INTO `auth_role_resource` VALUES (200, 1, 3, '2021-12-21 12:39:56', '2021-12-21 12:39:56');
INSERT INTO `auth_role_resource` VALUES (201, 1, 522, '2021-12-21 12:39:59', '2021-12-21 12:39:59');
INSERT INTO `auth_role_resource` VALUES (202, 1, 189, '2021-12-21 12:40:00', '2021-12-21 12:40:00');
INSERT INTO `auth_role_resource` VALUES (203, 1, 188, '2021-12-21 12:40:01', '2021-12-21 12:40:01');
INSERT INTO `auth_role_resource` VALUES (204, 1, 34, '2021-12-21 12:40:02', '2021-12-21 12:40:02');
INSERT INTO `auth_role_resource` VALUES (205, 1, 31, '2021-12-21 12:40:03', '2021-12-21 12:40:03');
INSERT INTO `auth_role_resource` VALUES (206, 1, 30, '2021-12-21 12:40:04', '2021-12-21 12:40:04');
INSERT INTO `auth_role_resource` VALUES (207, 1, 33, '2021-12-21 12:40:05', '2021-12-21 12:40:05');
INSERT INTO `auth_role_resource` VALUES (208, 1, 514, '2021-12-29 04:17:48', '2021-12-29 04:17:48');
COMMIT;

-- ----------------------------
-- Table structure for auth_user_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_user_role`;
CREATE TABLE `auth_user_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户登录名',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username_role_unique` (`username`,`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of auth_user_role
-- ----------------------------
BEGIN;
INSERT INTO `auth_user_role` VALUES (1, 'admin', 1, '2021-12-29 09:33:05', '2021-12-29 09:33:05');
INSERT INTO `auth_user_role` VALUES (2, 'admin', 2, '2021-12-29 09:33:11', '2021-12-29 09:33:11');
INSERT INTO `auth_user_role` VALUES (3, 'admin', 3, '2021-12-29 09:33:17', '2021-12-29 09:33:17');
INSERT INTO `auth_user_role` VALUES (4, 'admin', 4, '2021-12-29 09:33:23', '2021-12-29 09:33:23');
INSERT INTO `auth_user_role` VALUES (5, 'admin', 5, '2021-12-29 09:33:37', '2021-12-29 09:33:37');
COMMIT;

-- ----------------------------
-- Table structure for business_asset_relation
-- ----------------------------
DROP TABLE IF EXISTS `business_asset_relation`;
CREATE TABLE `business_asset_relation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `business_type` int(11) NOT NULL COMMENT '业务类型',
  `business_id` int(11) NOT NULL COMMENT '业务id',
  `datasource_instance_asset_id` int(11) NOT NULL COMMENT '资产id',
  `asset_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资产类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `business_type` (`business_type`,`business_id`,`datasource_instance_asset_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务对象与资产的绑定关系';

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
  `business_id` int(11) NOT NULL COMMENT '业务id',
  `property` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
  `business_id` int(11) NOT NULL COMMENT '业务id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  `business_type` int(2) DEFAULT NULL COMMENT '业务类型',
  `tag_value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `business_id` (`business_id`,`tag_id`,`business_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器标签关联表';

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
  `instance_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例UUID',
  `business_type` int(11) NOT NULL COMMENT '业务类型',
  `business_id` int(11) NOT NULL COMMENT '业务ID',
  `template_id` int(11) NOT NULL COMMENT '模版ID',
  `env_type` int(11) NOT NULL COMMENT '环境类型',
  `vars` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模板变量',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模板内容',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务模版';

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
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'UUID',
  `ds_type` int(2) NOT NULL COMMENT '数据源类型',
  `version` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `kind` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '分类',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `credential_id` int(11) DEFAULT NULL COMMENT '凭据id',
  `ds_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '数据源地址',
  `props_yml` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '属性(yaml)',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源配置';

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
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '数据源关联id',
  `instance_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据源名称',
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'UUID',
  `instance_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '实例类型',
  `kind` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '实例分类',
  `version` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例版本',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `config_id` int(11) DEFAULT NULL COMMENT '数据源配置id',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`instance_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源实例';

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
  `instance_uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据源实例uuid',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产名称',
  `asset_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资产id',
  `asset_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资产类型',
  `kind` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '资产分类',
  `version` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产版本',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `asset_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资产关键字1',
  `asset_key_2` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产关键字2',
  `zone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区域',
  `region_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地区id',
  `asset_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产状态',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_time` timestamp NULL DEFAULT NULL COMMENT '资产创建时间',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '资产过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `asset_id` (`asset_id`,`asset_type`,`instance_uuid`,`asset_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源实例资产';

-- ----------------------------
-- Records of datasource_instance_asset
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for datasource_instance_asset_property
-- ----------------------------
DROP TABLE IF EXISTS `datasource_instance_asset_property`;
CREATE TABLE `datasource_instance_asset_property` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `datasource_instance_asset_id` int(11) NOT NULL DEFAULT '0' COMMENT '资产id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '属性名称',
  `value` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `datasource_instance_asset_id` (`datasource_instance_asset_id`,`name`,`value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资产属性';

-- ----------------------------
-- Records of datasource_instance_asset_property
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for datasource_instance_asset_relation
-- ----------------------------
DROP TABLE IF EXISTS `datasource_instance_asset_relation`;
CREATE TABLE `datasource_instance_asset_relation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `instance_uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '实例uuid',
  `source_asset_id` int(11) NOT NULL COMMENT '源资产id',
  `target_asset_id` int(11) NOT NULL COMMENT '目标资产id',
  `relation_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '关系类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `instance_uuid` (`instance_uuid`,`source_asset_id`,`target_asset_id`,`relation_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资产关系';

-- ----------------------------
-- Records of datasource_instance_asset_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for datasource_instance_asset_subscription
-- ----------------------------
DROP TABLE IF EXISTS `datasource_instance_asset_subscription`;
CREATE TABLE `datasource_instance_asset_subscription` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `instance_uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据源实例uuid',
  `datasource_instance_asset_id` int(11) NOT NULL DEFAULT '0' COMMENT '资产id',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `playbook` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '订阅剧本',
  `vars` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '外部变量',
  `last_subscription_time` timestamp NULL DEFAULT NULL COMMENT '最后订阅时间',
  `last_subscription_log` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '最后订阅日志',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资产订阅';

-- ----------------------------
-- Records of datasource_instance_asset_subscription
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for event
-- ----------------------------
DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `instance_uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据源实例uuid',
  `event_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据源事件id',
  `event_id_desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '数据源事件id描述',
  `event_name` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '事件名称',
  `event_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '数据源事件信息',
  `priority` int(2) NOT NULL DEFAULT '0' COMMENT '严重性级别',
  `lastchange_time` timestamp NULL DEFAULT NULL COMMENT '最后更改其状态的时间',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效事件',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `instance_uuid` (`instance_uuid`,`event_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='事件';

-- ----------------------------
-- Records of event
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for event_business
-- ----------------------------
DROP TABLE IF EXISTS `event_business`;
CREATE TABLE `event_business` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL COMMENT '事件id',
  `business_type` int(2) NOT NULL COMMENT '业务类型',
  `business_id` int(11) NOT NULL DEFAULT '0' COMMENT '业务id',
  `name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `event_id` (`event_id`,`business_type`,`business_id`) USING BTREE,
  KEY `event_id_2` (`event_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='事件业务对象关联';

-- ----------------------------
-- Records of event_business
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for instance
-- ----------------------------
DROP TABLE IF EXISTS `instance`;
CREATE TABLE `instance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实例名',
  `hostname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '主机名',
  `host_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主机ip',
  `status` int(11) NOT NULL COMMENT '实例状态',
  `is_active` tinyint(1) NOT NULL COMMENT '有效',
  `license` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_UNIQUE` (`name`) USING BTREE,
  UNIQUE KEY `host_ip_UNIQUE` (`host_ip`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of instance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for message_template
-- ----------------------------
DROP TABLE IF EXISTS `message_template`;
CREATE TABLE `message_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `msg_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '消息关键字',
  `msg_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '消息类型',
  `consumer` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '消费者',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
  `msg_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知模版',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `msg_key` (`msg_key`,`msg_type`,`consumer`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知模版';

-- ----------------------------
-- Records of message_template
-- ----------------------------
BEGIN;
INSERT INTO `message_template` VALUES (1, '创建用户消息通知模板', 'CREATE_USER', 'markdown', 'DINGTALK_APP', 'OPSCLOUD通知', '### OPSCLOUD账户开通\n- 用户名: ${username}   \n- 密码: `${password}`', '', '2021-12-01 10:56:15', '2021-12-14 03:04:22');
INSERT INTO `message_template` VALUES (2, '修改用户密码消息通知模板', 'UPDATE_USER_PASSWORD', 'markdown', 'DINGTALK_APP', 'OPSCLOUD通知', '### OPSCLOUD账户重置\n- 用户名: ${username} \n- 密码: `${password}`', '本人修改密码则不发送', '2021-12-01 11:06:47', '2021-12-14 03:04:19');
INSERT INTO `message_template` VALUES (3, '创建RAM用户通知模板', 'CREATE_RAM_USER', 'markdown', 'DINGTALK_APP', 'OPSCLOUD通知', '### 阿里云RAM(访问控制)账户开通\n- 阿里云实例名称: ${aliyunName}\n- 阿里云RAM用户登录地址: ${loginUrl}\n- RAM用户名: ${username}   \n- RAM用户密码: `${password}`', NULL, '2021-12-14 03:01:40', '2021-12-14 04:35:38');
COMMIT;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工单名称',
  `order_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工单key',
  `sys_document_id` int(11) NOT NULL DEFAULT '0' COMMENT '帮助文档id',
  `order_group_id` int(11) DEFAULT NULL COMMENT '工单组id',
  `order_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态 0 正常 1 开发 2 停用',
  `workflow` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '工作流配置',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `workorder_key` (`order_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of order
-- ----------------------------
BEGIN;
INSERT INTO `order` VALUES (1, '服务器权限申请', 'SERVER_GROUP', 0, 1, 0, 'workFlowNodes:\n   - nodeName: TL\n      nodeType: 0 #0用户指定 ， 1 符合条件群发， 2 系统指定 \n      tag: TeamLeader\n   ', NULL, '2021-10-21 10:45:29', '2021-10-21 11:13:56');
COMMIT;

-- ----------------------------
-- Table structure for order_group
-- ----------------------------
DROP TABLE IF EXISTS `order_group`;
CREATE TABLE `order_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工单组名称',
  `order_type` int(2) DEFAULT NULL COMMENT '工单组类型',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of order_group
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for server
-- ----------------------------
DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '服务器名称',
  `server_group_id` int(11) NOT NULL COMMENT '服务器组id',
  `os_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '系统类型',
  `env_type` int(2) NOT NULL DEFAULT '0' COMMENT '环境类型',
  `public_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '公网ip',
  `private_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '私网ip',
  `server_type` int(11) DEFAULT NULL COMMENT '服务器类型',
  `area` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地区',
  `serial_number` int(8) NOT NULL DEFAULT '0' COMMENT '序号',
  `monitor_status` int(1) NOT NULL DEFAULT '-1' COMMENT '监控状态',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '说明',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `server_status` int(2) NOT NULL DEFAULT '1' COMMENT '服务器状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `env_type` (`env_type`,`serial_number`,`server_group_id`) USING BTREE,
  UNIQUE KEY `private_ip_2` (`private_ip`) USING BTREE,
  KEY `name` (`name`) USING BTREE,
  KEY `private_ip` (`private_ip`) USING BTREE,
  KEY `server_group_id` (`server_group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of server
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for server_account
-- ----------------------------
DROP TABLE IF EXISTS `server_account`;
CREATE TABLE `server_account` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '服务器名称',
  `credential_id` int(11) NOT NULL COMMENT '凭据id',
  `account_type` int(2) NOT NULL DEFAULT '0' COMMENT '0普通账户/1管理员',
  `protocol` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '连接协议',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '说明',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`,`account_type`,`protocol`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器账户';

-- ----------------------------
-- Records of server_account
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for server_account_permission
-- ----------------------------
DROP TABLE IF EXISTS `server_account_permission`;
CREATE TABLE `server_account_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `server_id` int(11) NOT NULL COMMENT '服务器id',
  `server_account_id` int(11) NOT NULL COMMENT '账户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `server_id` (`server_id`,`server_account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器-账户 授权表';

-- ----------------------------
-- Records of server_account_permission
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for server_group
-- ----------------------------
DROP TABLE IF EXISTS `server_group`;
CREATE TABLE `server_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '服务器组名称',
  `server_group_type_id` int(2) NOT NULL DEFAULT '0' COMMENT '服务器组类型',
  `allow_order` tinyint(1) NOT NULL DEFAULT '1' COMMENT '允许工单申请',
  `comment` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE,
  KEY `server_group_type_id` (`server_group_type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of server_group
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for server_group_property
-- ----------------------------
DROP TABLE IF EXISTS `server_group_property`;
CREATE TABLE `server_group_property` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `server_group_id` int(11) NOT NULL COMMENT '服务器组id',
  `env_type` int(2) NOT NULL DEFAULT '0' COMMENT '环境type',
  `property_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '属性名称',
  `property_value` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `server_group_id` (`server_group_id`,`env_type`,`property_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器组外部属性表';

-- ----------------------------
-- Records of server_group_property
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for server_group_type
-- ----------------------------
DROP TABLE IF EXISTS `server_group_type`;
CREATE TABLE `server_group_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `color` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `comment` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of server_group_type
-- ----------------------------
BEGIN;
INSERT INTO `server_group_type` VALUES (1, 'default', '#858585', '默认类型', '2021-05-24 11:34:49', '2021-05-24 11:34:49');
INSERT INTO `server_group_type` VALUES (2, 'gitlab', '#dd4d3a', '', '2021-05-26 09:56:27', '2021-05-26 09:56:27');
INSERT INTO `server_group_type` VALUES (3, 'microservice', '#F29603', '微服务', '2021-06-08 13:23:38', '2021-06-08 13:23:38');
INSERT INTO `server_group_type` VALUES (4, 'zookeeper', '#dd4d3a', 'zookeeper', '2021-06-24 09:42:11', '2021-06-24 09:42:11');
INSERT INTO `server_group_type` VALUES (5, 'mysql', '#1451B4', 'mysql', '2021-06-24 09:42:11', '2021-06-24 09:42:11');
INSERT INTO `server_group_type` VALUES (6, 'other', NULL, NULL, '2021-06-24 09:42:11', '2021-06-24 09:42:11');
INSERT INTO `server_group_type` VALUES (7, 'public', '#E80F07', NULL, '2021-06-24 09:42:11', '2021-06-24 09:42:11');
INSERT INTO `server_group_type` VALUES (8, 'redis', '#D7160F', NULL, '2021-06-24 09:42:11', '2021-06-24 09:42:11');
INSERT INTO `server_group_type` VALUES (9, 'web-server', NULL, NULL, '2021-06-24 09:42:11', '2021-06-24 09:42:11');
INSERT INTO `server_group_type` VALUES (10, 'front-end', NULL, NULL, '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (11, 'bi', NULL, NULL, '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (12, 'testType', NULL, '', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (13, 'getway', NULL, '终端跳板机', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (14, 'dubbo', NULL, '', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (15, 'elasticsearch', NULL, '', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (16, 'job', NULL, '', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (17, 'k8s', NULL, '', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (18, 'zabbix', NULL, '', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (19, 'kafka', NULL, 'kafka集群', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (20, 'jenkins', '#048112', '持续集成', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (21, 'bigdata', '#dd4d3a', '大数据相关服务(非业务)', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (22, 'kubernetes', '#dd4d3a', '容器服务', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (23, 'canal', '#3093EA', '', '2021-06-24 09:42:12', '2021-06-24 09:42:12');
INSERT INTO `server_group_type` VALUES (24, 'qa', '#dd4d3a', '测试组', '2021-06-24 09:42:13', '2021-06-24 09:42:13');
COMMIT;

-- ----------------------------
-- Table structure for server_task
-- ----------------------------
DROP TABLE IF EXISTS `server_task`;
CREATE TABLE `server_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `instance_uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据源实例uuid',
  `task_uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务uuid',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `member_size` int(11) NOT NULL DEFAULT '0' COMMENT '成员数量',
  `ansible_playbook_id` int(11) DEFAULT NULL COMMENT '剧本id',
  `vars` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `tags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `task_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT 'PLAYBOOK',
  `finalized` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否完成',
  `stop_type` int(11) DEFAULT '0' COMMENT '终止任务',
  `task_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务状态',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '任务开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '任务结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `task_uuid` (`task_uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器任务';

-- ----------------------------
-- Records of server_task
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for server_task_member
-- ----------------------------
DROP TABLE IF EXISTS `server_task_member`;
CREATE TABLE `server_task_member` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `server_task_id` int(11) NOT NULL COMMENT '任务id',
  `server_id` int(11) DEFAULT NULL COMMENT '服务器id',
  `server_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '服务器名称',
  `env_type` int(2) NOT NULL DEFAULT '0' COMMENT '环境类型',
  `manage_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理ip',
  `task_status` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务状态',
  `finalized` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否完成',
  `stop_type` int(11) DEFAULT '0' COMMENT '终止任务',
  `exit_value` int(2) DEFAULT NULL COMMENT '退出值',
  `task_result` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `output_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `start_time` timestamp NULL DEFAULT NULL COMMENT '任务开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '任务结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of server_task_member
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for shedlock
-- ----------------------------
DROP TABLE IF EXISTS `shedlock`;
CREATE TABLE `shedlock` (
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `lock_until` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `locked_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `locked_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of shedlock
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_credential
-- ----------------------------
DROP TABLE IF EXISTS `sys_credential`;
CREATE TABLE `sys_credential` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `kind` int(11) NOT NULL COMMENT '凭据分类',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
  `credential` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '凭据内容',
  `credential_2` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'publicKey',
  `passphrase` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '密码短语',
  `fingerprint` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '指纹',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统凭据';

-- ----------------------------
-- Records of sys_credential
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_document
-- ----------------------------
DROP TABLE IF EXISTS `sys_document`;
CREATE TABLE `sys_document` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `document_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文档内容',
  `document_type` int(2) NOT NULL DEFAULT '1' COMMENT '文档类型',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `doc_key` (`document_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sys_document
-- ----------------------------
BEGIN;
INSERT INTO `sys_document` VALUES (1, 'JUMP_README', '', 0, NULL, '2020-05-12 17:26:57', '2021-12-29 09:27:57');
INSERT INTO `sys_document` VALUES (2, 'WORDPAD', '# 欢迎使用OC-Web终端\n![](https://img.shields.io/badge/opscloud-xterm-brightgreen.svg?style=plastic&logo=iCloud)\n\n<b style=\"color:red\">!!!此文档是用户私有文档，可随意编辑!!!</b>\n\n#### 常用命令\n\n##### 应用服务管理\n```bash\n# 切换到app用户\nxincheng$ sudo su - app\napp$ sh /opt/bin/appctl.sh start # 启动应用\napp$ sh /opt/bin/appctl.sh stop # 停止应用\napp$ sh /opt/bin/appctl.sh dump # dump java进程，完成后需重启\napp$ sh /opt/bin/appctl.sh forcestop # 强制停止应用\napp$ sh /opt/bin/appctl.sh # 查看进程状态\n```\n\n##### 高权限账户登录后切换到root\n```bash\n# 切换root\nmanage$ sudo su - # 或直接输入封装命令 s\n```\n\n##### Docket命令指南\n + <a style=\"color:#2b669a\" href=\"https://www.runoob.com/docker/docker-run-command.html\" target=\"_blank\"><b>传送门</b></a>', 0, NULL, '2020-05-13 15:04:04', '2021-06-16 11:31:09');
INSERT INTO `sys_document` VALUES (3, 'USER_GROUP_README', '#### 账户\n+ 已完成企业内部统一权限认证，所有平台账户互通', 0, NULL, '2020-05-18 13:37:31', '2021-06-16 11:31:10');
INSERT INTO `sys_document` VALUES (4, 'SERVER_GROUP_README', '#### 说明\n+ 授权的服务器组内所有服务器都可以通过`JUMP跳板机`登录\n  + 登录方式详见 <a style=\"color:#2b669a\" href=\"https://oc3.ops.yangege.cn/#/workbench/jump\" target=\"_blank\"><b>传送门</b></a>\n\n+ 授权的服务器组内所有服务都可以登录`Zabbix`监控平台查看数据\n  + `Zabbix`监控平台登录账户于本平台相同<a style=\"color:#2b669a\" href=\"http://zabbix.ops.yangege.cn\" target=\"_blank\"><b>传送门</b></a>\n\n+ 授权的服务器组内所有服务器都通过OC内置WebXTerm批量登录\n  + WebXTerm支持高权限登录服务器（需要在工单中申请服务器组的管理员权限）\n\n##### OC-Web-XTerm\n<video width=\"100%\" controls Autoplay=autoplay>\n<source src=\"https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/help/opscloud/web-xterm/oc-web-xterm.mov\" type=\"video/mp4\" align=center>\n</video>', 0, NULL, '2020-05-18 13:38:36', '2021-06-16 11:31:11');
INSERT INTO `sys_document` VALUES (5, 'OC_ROLE', '#### `dev`角色\n+ 查看服务器相关信息\n+ 堡垒机权限，监控权限，日志权限等', 0, NULL, '2020-05-18 13:39:22', '2021-06-16 11:31:12');
INSERT INTO `sys_document` VALUES (6, 'RAM_POLICY', '#### 账户\n+ 阿里云主账户\n   + 主账户uid : 1255805305757185\n   + 子账户: ${username}@1255805305757185\n   + 登录地址:  https://signin.aliyun.com/1255805305757185/.onaliyun.com/login.htm\n+ 阿里云MS账户\n   + 主账户uid : 1267986359450069\n   + 子账户: ${username}@1267986359450069\n   + 登录地址:  https://signin.aliyun.com/1267986359450069/.onaliyun.com/login.htm\n\n#### 用户RAM策略详情\n+ 个人详情-我的详情-阿里云RAM账户授权策略 中查看\n\n#### 登录密码\n> 密码同oc密码一致，但必须符合密码强度（包含英文大小写，数字。特殊字符，长度>=10位），登录错误请联系运维！\n\n', 0, NULL, '2020-06-12 16:16:46', '2021-06-16 11:31:12');
INSERT INTO `sys_document` VALUES (7, 'SSH_SERVER_README', '<h1>OpsCloud v4.0 SSH-Server</h1>\n\n![](https://img.shields.io/badge/springboot-2.3.10.RELEASE-brightgreen.svg?style=plastic)\n\n### 帮助文档\n\n##### SSH-Server登录/认证\n\n+  认证优先级\n    + `本地公钥`  --> `Gitlab个人公钥`  --> `LDAP密码认证`\n+  添加个人公钥（若在Gitlab中已添加则忽略此步骤）\n    + `用户信息`-`个人详情`-`SSH密钥`（编辑-添加公钥）\n```bash\n# 查看公钥\n$ cat ~/.ssh/id_rsa.pub\n```\n\n##### 登录终端\n+ Mac命令行中输入\n```bash\n# -o StrictHostKeyChecking=no 跳过公钥检查\n$ ssh ${username}@${sshServerHost}\n```\n\n##### Example\n+  登录和帮助\n\n\n<img src=\"https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/ssh-server/ssh-server-help.png\" style=\"height: 700px\"></img>\n\n\n+  查询服务器信息\n\n```bash\n$ list\n```\n\n<img src=\"https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/help/ssh-server/ssh-server-list.png\" style=\"height: 700px\"></img>\n\n+  登录列表ID1的服务器\n\n```bash\n$ list\n$ login 1\n```\n\n\n+  查询容器组(Pod)信息\n\n```\nopscloud shell>list-k8s-pod\n+----+--------------------------+-----------+------------------------------------+------------+---------------------+---------------+-------------------+\n| ID | Kubernetes Instance Name | Namespace | Pod Name                           | Pod IP     | Start Time          | Restart Count | Container Name    |\n+----+--------------------------+-----------+------------------------------------+------------+---------------------+---------------+-------------------+\n|  1 | kubernetes-dev           | dev       | merchant-kili-dev-69f89bc69c-tbc4h | 10.10.0.34 | 2021-12-02 14:53:30 |            11 | merchant-kili-dev | \n+----+--------------------------+-----------+------------------------------------+------------+---------------------+---------------+-------------------+\nopscloud shell>\n```\n\n+ 查看容器日志(ID1)\n\n`opscloud shell>show-k8s-pod-log 1`\n\n\n+ 登录容器(ID1)\n\n`opscloud shell>login-k8s-pod  1`\n', 1, NULL, '2021-06-16 13:35:52', '2021-12-09 10:24:03');
COMMIT;

-- ----------------------------
-- Table structure for sys_env
-- ----------------------------
DROP TABLE IF EXISTS `sys_env`;
CREATE TABLE `sys_env` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `env_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '环境名称',
  `env_type` int(11) NOT NULL COMMENT '环境类型',
  `color` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '环境色',
  `prompt_color` int(11) DEFAULT NULL COMMENT '终端提示色',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `env_name` (`env_name`) USING BTREE,
  UNIQUE KEY `env_type` (`env_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,';

-- ----------------------------
-- Records of sys_env
-- ----------------------------
BEGIN;
INSERT INTO `sys_env` VALUES (1, 'default', 0, '#B7B6B6', 7, '默认环境', '2020-04-02 22:06:38', '2020-07-02 17:56:04');
INSERT INTO `sys_env` VALUES (2, 'dev', 1, '#5bc0de', 6, '开发环境', '2020-01-10 13:53:51', '2021-07-02 18:17:13');
INSERT INTO `sys_env` VALUES (3, 'test', 5, '#A0AE07', 3, '测试环境', '2020-01-10 13:53:55', '2021-09-02 17:27:44');
INSERT INTO `sys_env` VALUES (4, 'gray', 3, '#000000', 8, '灰度环境', '2020-03-11 14:03:58', '2021-09-02 17:25:35');
INSERT INTO `sys_env` VALUES (5, 'prod', 4, '#E34C15', 1, '生产环境', '2020-02-22 13:13:16', '2021-07-27 11:21:55');
INSERT INTO `sys_env` VALUES (6, 'daily', 2, '#449d44', 2, '日常环境', '2020-03-04 11:10:44', '2020-03-04 11:10:44');
INSERT INTO `sys_env` VALUES (14, 'local', 7, '#FC7A00', 5, '本地环境', '2020-03-11 14:05:28', '2021-12-13 10:15:28');
INSERT INTO `sys_env` VALUES (15, 'st', 8, '#0926C9', 4, '压测环境', '2021-06-24 10:36:07', '2021-06-24 10:37:43');
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '菜单标题',
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '菜单图标',
  `seq` int(11) NOT NULL COMMENT '顺序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, '系统管理', 'fab fa-whmcs', 0, '2021-06-09 10:41:34', '2021-07-08 17:17:45');
INSERT INTO `sys_menu` VALUES (2, '工作台', 'fas fa-desktop', 1, '2021-06-09 10:41:34', '2021-07-01 10:06:37');
INSERT INTO `sys_menu` VALUES (3, '用户管理', 'fas fa-users', 3, '2021-06-09 10:41:34', '2021-07-15 14:05:05');
INSERT INTO `sys_menu` VALUES (4, '服务器管理', 'fas fa-server', 4, '2021-06-09 10:41:34', '2021-07-15 14:05:05');
INSERT INTO `sys_menu` VALUES (5, '数据源管理', 'fas fa-dice-d20', 6, '2021-06-09 10:41:34', '2021-09-01 13:47:16');
INSERT INTO `sys_menu` VALUES (6, 'RBAC管理', 'fab fa-google-drive', 7, '2021-06-09 10:41:34', '2021-09-01 13:47:16');
INSERT INTO `sys_menu` VALUES (7, '用户信息', 'fas fa-user-circle', 8, '2021-06-09 10:41:34', '2021-11-24 04:17:44');
INSERT INTO `sys_menu` VALUES (8, '应用管理', 'fab fa-deezer', 2, '2021-07-15 14:05:05', '2021-11-24 04:14:20');
INSERT INTO `sys_menu` VALUES (9, '任务管理', 'fab fa-google-play', 5, '2021-09-01 13:42:07', '2021-09-01 13:47:16');
COMMIT;

-- ----------------------------
-- Table structure for sys_menu_child
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_child`;
CREATE TABLE `sys_menu_child` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '子菜单标题',
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '子菜单图标名称',
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '子菜单路径',
  `seq` int(11) NOT NULL COMMENT '子菜单排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sys_menu_child
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu_child` VALUES (1, 1, '凭据管理', 'fas fa-fingerprint', '/sys/credential', 0, '2021-06-02 14:37:00', '2021-07-23 14:37:33');
INSERT INTO `sys_menu_child` VALUES (2, 1, '标签管理', 'fas fa-tags', '/sys/tag', 1, '2021-06-02 14:37:00', '2021-07-23 14:37:33');
INSERT INTO `sys_menu_child` VALUES (3, 1, '环境管理', 'fab fa-pagelines', '/sys/env', 2, '2021-06-02 14:37:00', '2021-11-24 03:16:23');
INSERT INTO `sys_menu_child` VALUES (4, 1, '菜单管理', 'fas fa-bars', '/sys/menu', 3, '2021-06-02 14:37:00', '2021-06-30 17:54:03');
INSERT INTO `sys_menu_child` VALUES (6, 2, '批量终端', 'fas fa-terminal', '/workbench/web-terminal', 1, '2021-06-15 17:10:03', '2021-07-01 10:03:58');
INSERT INTO `sys_menu_child` VALUES (7, 3, '用户', 'fas fa-user-alt', '/user', 0, '2021-06-02 15:00:58', '2021-12-10 02:56:51');
INSERT INTO `sys_menu_child` VALUES (8, 4, '服务器', 'fas fa-server', '/server', 0, '2021-06-02 15:01:51', '2021-06-30 17:54:24');
INSERT INTO `sys_menu_child` VALUES (9, 4, '服务器组', 'fas fa-window-restore', '/server/group', 1, '2021-06-02 15:01:51', '2021-06-30 17:54:24');
INSERT INTO `sys_menu_child` VALUES (10, 5, '数据源实例', 'fab fa-battle-net', '/datasource/instance', 0, '2021-06-02 15:02:20', '2021-11-24 03:15:10');
INSERT INTO `sys_menu_child` VALUES (11, 5, '数据源配置', 'fab fa-unity', '/datasource/config', 1, '2021-06-02 15:02:20', '2021-12-08 09:56:57');
INSERT INTO `sys_menu_child` VALUES (12, 6, '资源配置', 'fas fa-stream', '/rbac/resource', 0, '2021-06-02 15:03:02', '2021-06-30 17:54:44');
INSERT INTO `sys_menu_child` VALUES (13, 6, '用户角色配置', 'fas fa-id-card', '/rbac/user-role', 2, '2021-06-02 15:03:02', '2021-06-30 17:54:44');
INSERT INTO `sys_menu_child` VALUES (14, 6, '角色配置', 'fas fa-users', '/rbac/role', 1, '2021-06-02 15:03:02', '2021-06-30 17:54:44');
INSERT INTO `sys_menu_child` VALUES (15, 7, '个人详情', 'fas fa-address-book', '/user/details', 0, '2021-06-09 10:36:50', '2021-06-30 17:54:49');
INSERT INTO `sys_menu_child` VALUES (16, 2, 'SSH终端', 'fab fa-markdown', '/workbench/ssh-server', 0, '2021-06-15 17:10:04', '2021-11-24 03:33:02');
INSERT INTO `sys_menu_child` VALUES (17, 2, '远程桌面', 'fab fa-windows', '/workbench/remote-desktop', 2, '2021-07-09 14:50:59', '2021-07-09 14:50:59');
INSERT INTO `sys_menu_child` VALUES (19, 2, '容器终端', 'fab fa-docker', '/workbench/kubernetes-web-terminal', 3, '2021-07-13 16:13:19', '2021-07-13 16:19:25');
INSERT INTO `sys_menu_child` VALUES (20, 8, '应用', 'fas fa-grip-horizontal', '/application', 0, '2021-07-15 14:09:18', '2021-11-24 04:15:55');
INSERT INTO `sys_menu_child` VALUES (21, 1, '审计管理', 'fas fa-video', '/sys/audit', 4, '2021-07-22 15:08:59', '2021-07-23 14:37:34');
INSERT INTO `sys_menu_child` VALUES (22, 5, '资产订阅', 'fa fa-rss', '/datasource/asset/subscription', 3, '2021-08-27 15:52:47', '2021-12-08 09:48:08');
INSERT INTO `sys_menu_child` VALUES (23, 9, '剧本任务', 'fas fa-recycle', '/task/playbook', 0, '2021-09-01 13:42:50', '2021-09-26 11:42:58');
INSERT INTO `sys_menu_child` VALUES (24, 1, '集群管理', 'fas fa-server', '/sys/instance', 5, '2021-09-06 11:13:25', '2021-09-06 11:18:52');
INSERT INTO `sys_menu_child` VALUES (25, 5, '模版管理', 'fab fa-slack-hash', '/template', 2, '2021-12-08 03:47:44', '2021-12-08 09:56:57');
INSERT INTO `sys_menu_child` VALUES (26, 3, '无效用户', 'fas fa-user-alt-slash', '/user/inactive', 1, '2021-12-10 02:51:10', '2021-12-10 02:56:28');
INSERT INTO `sys_menu_child` VALUES (27, 3, '用户详情', 'fas fa-address-book', '/user/info', 2, '2021-12-13 08:17:17', '2021-12-13 08:17:25');
COMMIT;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `tag_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '标签Key,全局唯一',
  `business_type` int(2) NOT NULL DEFAULT '0' COMMENT '业务类型',
  `color` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '颜色值',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `tag_key` (`tag_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,';

-- ----------------------------
-- Records of tag
-- ----------------------------
BEGIN;
INSERT INTO `tag` VALUES (1, 'Authorization', 16, '#EB0303', '启用认证', '2021-05-19 16:32:43', '2021-05-19 16:32:43');
INSERT INTO `tag` VALUES (2, 'Jenkins', 1, '#B70707', '', '2021-05-25 18:06:24', '2021-05-25 18:06:24');
INSERT INTO `tag` VALUES (3, 'Gitlab', 1, '#144AD3', '', '2021-05-26 11:18:34', '2021-05-26 11:18:34');
INSERT INTO `tag` VALUES (6, 'Android', 5, '#B70707', '', '2021-06-22 13:16:27', '2021-06-22 13:16:27');
INSERT INTO `tag` VALUES (7, 'RDP', 16, '#0684C4', '支持RDP协议', '2021-07-08 18:06:29', '2021-07-08 18:06:29');
INSERT INTO `tag` VALUES (8, 'VNC', 16, '#30ADED', '支持VNC协议', '2021-07-08 18:06:49', '2021-07-08 18:06:49');
INSERT INTO `tag` VALUES (9, 'Account', 16, '#0320A2', '账户管理', '2021-08-11 15:59:31', '2021-08-11 16:07:54');
INSERT INTO `tag` VALUES (10, 'Server', 16, '', '服务器管理', '2021-08-20 16:24:00', '2021-08-20 16:24:00');
INSERT INTO `tag` VALUES (11, 'Event', 16, '#00E5FF', '实例事件监听', '2021-10-09 17:05:09', '2021-10-09 17:05:09');
INSERT INTO `tag` VALUES (12, 'TeamLeader', 3, '#0026FF', '经理', '2021-10-20 16:23:05', '2021-10-20 16:23:05');
INSERT INTO `tag` VALUES (13, 'Operations', 3, '', '运维工程师', '2021-10-20 16:27:02', '2021-10-20 16:27:02');
INSERT INTO `tag` VALUES (14, 'SystemHooks', 16, '#287710', '接收SystemHooks', '2021-10-28 18:20:39', '2021-10-28 18:20:39');
INSERT INTO `tag` VALUES (15, 'System', 0, '#329D04', '系统用户', '2021-11-05 04:28:11', '2021-12-16 07:04:26');
INSERT INTO `tag` VALUES (16, 'Notice', 16, '#05735D', '消息通知', '2021-12-02 03:55:00', '2021-12-02 03:55:00');
COMMIT;

-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `env_type` int(11) NOT NULL COMMENT '环境类型',
  `instance_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '实例类型',
  `template_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '模板key',
  `template_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '模板类型',
  `vars` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模板变量',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模板内容',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模版';

-- ----------------------------
-- Records of template
-- ----------------------------
BEGIN;
INSERT INTO `template` VALUES (1, 'k8s-无状态模板-dev', 1, 'KUBERNETES', 'DEPLOYMENT', 'yaml', 'vars:\n  # 必须指定\n  appName: \n  javaOpts: -Xms512m -Xmx512m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5000\n  springCustomOpts: \'\'\n  livenessInitialDelay: 120\n  readinessInitialDelay: 30', '---\napiVersion: apps/v1\nkind: Deployment\nmetadata:\n  labels:\n    app: ${appName}-${envName}\n    env: ${envName}\n  name: ${appName}-${envName}\n  namespace: ${envName}\nspec:\n  progressDeadlineSeconds: 600\n  replicas: 1\n  revisionHistoryLimit: 10\n  selector:\n    matchLabels:\n      app: ${appName}-${envName}\n  strategy:\n    rollingUpdate:\n      maxSurge: 50%\n      maxUnavailable: 50%\n    type: RollingUpdate\n  template:\n    metadata:\n      labels:\n        app: ${appName}-${envName}\n    spec:\n      containers:\n        - env:\n            - name: JAVA_OPTS\n              value: ${javaOpts}\n            - name: SPRING_OPTS\n              valueFrom:\n                configMapKeyRef:\n                  key: spring-common-opts\n                  name: java-options\n            - name: JAR_NAME\n              value: ${appName}.jar\n            - name: SPRING_CUSTOM_OPTS\n              value: ${springCustomOpts}\n          image: >-\n            aliyun-cr-uk.chuanyinet.com/service/${appName}\n          imagePullPolicy: Always\n          lifecycle:\n            preStop:\n              exec:\n                command:\n                  - curl\n                  - \'http://127.0.0.1:8081/actuator/shutdown\'\n                  - \'-X\'\n                  - POST\n          livenessProbe:\n            failureThreshold: 3\n            httpGet:\n              path: /actuator/health/liveness\n              port: 8081\n              scheme: HTTP\n            initialDelaySeconds: ${livenessInitialDelay}\n            periodSeconds: 5\n            successThreshold: 1\n            timeoutSeconds: 1\n          name: ${appName}-${envName}\n          readinessProbe:\n            failureThreshold: 3\n            httpGet:\n              path: /actuator/health/readiness\n              port: 8081\n              scheme: HTTP\n            initialDelaySeconds: ${readinessInitialDelay}\n            periodSeconds: 5\n            successThreshold: 1\n            timeoutSeconds: 1\n          resources:\n            limits:\n              cpu: \'1\'\n              memory: 2Gi\n            requests:\n              cpu: 100m\n              memory: 512Mi\n          securityContext:\n            privileged: true\n          stdin: true\n          tty: true\n          volumeMounts:\n            - mountPath: /etc/localtime\n              name: volume-localtime\n      dnsPolicy: ClusterFirst\n      imagePullSecrets:\n        - name: admin-cr\n      restartPolicy: Always\n      volumes:\n        - hostPath:\n            path: /etc/localtime\n          name: volume-localtime', NULL, '2021-12-03 09:13:26', '2021-12-23 13:03:54');
INSERT INTO `template` VALUES (2, 'k8s-服务模板-dev', 1, 'KUBERNETES', 'SERVICE', 'yaml', 'vars:\n  # 必须指定\n  appName: ', '---\napiVersion: v1\nkind: Service\nmetadata:\n  name: ${appName}-${envName}\n  namespace: ${envName}\n  labels:\n    env: ${envName}\n    micrometer-prometheus-discovery: \'true\'\nspec:\n  externalTrafficPolicy: Cluster\n  ports:\n    - name: http\n      port: 8080\n      protocol: TCP\n      targetPort: 8080\n    - name: http-mgmt\n      port: 8081\n      protocol: TCP\n      targetPort: 8081\n  selector:\n    app: ${appName}-${envName}\n  sessionAffinity: None\n  type: NodePort', NULL, '2021-12-07 08:01:19', '2021-12-27 11:50:36');
INSERT INTO `template` VALUES (4, 'k8s-无状态模板-daily', 2, 'KUBERNETES', 'DEPLOYMENT', 'yaml', 'vars:\n  # 必须指定\n  appName: \n  javaOpts: -Xms512m -Xmx512m \n  springCustomOpts: \'\'\n  livenessInitialDelay: 120\n  readinessInitialDelay: 30', '---\napiVersion: apps/v1\nkind: Deployment\nmetadata:\n  labels:\n    app: ${appName}-${envName}\n    env: ${envName}\n  name: ${appName}-${envName}\n  namespace: ${envName}\nspec:\n  progressDeadlineSeconds: 600\n  replicas: 1\n  revisionHistoryLimit: 10\n  selector:\n    matchLabels:\n      app: ${appName}-${envName}\n  strategy:\n    rollingUpdate:\n      maxSurge: 50%\n      maxUnavailable: 50%\n    type: RollingUpdate\n  template:\n    metadata:\n      annotations:\n        armsPilotAutoEnable: \'on\'\n        armsPilotCreateAppName: ${appName}-${envName}\n      labels:\n        app: ${appName}-${envName}\n    spec:\n      containers:\n        - env:\n            - name: JAVA_OPTS\n              value: ${javaOpts}\n            - name: SPRING_OPTS\n              valueFrom:\n                configMapKeyRef:\n                  key: spring-common-opts\n                  name: java-options\n            - name: JAR_NAME\n              value: ${appName}.jar\n            - name: SPRING_CUSTOM_OPTS\n              value: ${springCustomOpts}\n          image: >-\n            aliyun-cr-uk.chuanyinet.com/service/${appName}\n          imagePullPolicy: Always\n          lifecycle:\n            preStop:\n              exec:\n                command:\n                  - curl\n                  - \'http://127.0.0.1:8081/actuator/shutdown\'\n                  - \'-X\'\n                  - POST\n          livenessProbe:\n            failureThreshold: 3\n            httpGet:\n              path: /actuator/health/liveness\n              port: 8081\n              scheme: HTTP\n            initialDelaySeconds: ${livenessInitialDelay}\n            periodSeconds: 5\n            successThreshold: 1\n            timeoutSeconds: 1\n          name: ${appName}-${envName}\n          readinessProbe:\n            failureThreshold: 3\n            httpGet:\n              path: /actuator/health/readiness\n              port: 8081\n              scheme: HTTP\n            initialDelaySeconds: ${readinessInitialDelay}\n            periodSeconds: 5\n            successThreshold: 1\n            timeoutSeconds: 1\n          resources:\n            limits:\n              cpu: \'1\'\n              memory: 2Gi\n            requests:\n              cpu: 100m\n              memory: 512Mi\n          securityContext:\n            privileged: true\n          stdin: true\n          tty: true\n          volumeMounts:\n            - mountPath: /etc/localtime\n              name: volume-localtime\n      dnsPolicy: ClusterFirst\n      imagePullSecrets:\n        - name: admin-cr\n      restartPolicy: Always\n      volumes:\n        - hostPath:\n            path: /etc/localtime\n          name: volume-localtime', '', '2021-12-09 03:08:13', '2021-12-23 07:51:52');
INSERT INTO `template` VALUES (5, 'k8s-服务模板-daily', 2, 'KUBERNETES', 'SERVICE', 'yaml', 'vars:\n  # 必须指定\n  appName: ', '---\napiVersion: v1\nkind: Service\nmetadata:\n  name: ${appName}-${envName}\n  namespace: ${envName}\n  labels:\n    env: ${envName}\n    micrometer-prometheus-discovery: \'true\'\nspec:\n  ports:\n    - name: http\n      port: 8080\n      protocol: TCP\n      targetPort: 8080\n    - name: http-mgmt\n      port: 8081\n      protocol: TCP\n      targetPort: 8081\n  selector:\n    app: ${appName}-${envName}\n  sessionAffinity: None\n  type: ClusterIP', '', '2021-12-20 04:06:51', '2021-12-23 07:51:33');
INSERT INTO `template` VALUES (6, 'k8s-无状态模板-gray', 3, 'KUBERNETES', 'DEPLOYMENT', 'yaml', 'vars:\n  # 必须指定\n  appName: \n  javaOpts: -Xms4096m -Xmx4096m \n  springCustomOpts: \'\'\n  livenessInitialDelay: 120\n  readinessInitialDelay: 30', '---\napiVersion: apps/v1\nkind: Deployment\nmetadata:\n  labels:\n    app: ${appName}-${envName}\n    env: ${envName}\n  name: ${appName}-${envName}\n  namespace: ${envName}\nspec:\n  progressDeadlineSeconds: 600\n  replicas: 2\n  revisionHistoryLimit: 10\n  selector:\n    matchLabels:\n      app: ${appName}-${envName}\n  strategy:\n    rollingUpdate:\n      maxSurge: 50%\n      maxUnavailable: 50%\n    type: RollingUpdate\n  template:\n    metadata:\n      labels:\n        app: ${appName}-${envName}\n    spec:\n      containers:\n        - env:\n            - name: JAVA_OPTS\n              value: ${javaOpts}\n            - name: SPRING_OPTS\n              valueFrom:\n                configMapKeyRef:\n                  key: spring-common-opts\n                  name: java-options\n            - name: JAR_NAME\n              value: ${appName}.jar\n            - name: SPRING_CUSTOM_OPTS\n              value: ${springCustomOpts}\n          image: >-\n            aliyun-cr-uk.chuanyinet.com/service/${appName}\n          imagePullPolicy: Always\n          lifecycle:\n            preStop:\n              exec:\n                command:\n                  - curl\n                  - \'http://127.0.0.1:8081/actuator/shutdown\'\n                  - \'-X\'\n                  - POST\n          livenessProbe:\n            failureThreshold: 3\n            httpGet:\n              path: /actuator/health/liveness\n              port: 8081\n              scheme: HTTP\n            initialDelaySeconds: ${livenessInitialDelay}\n            periodSeconds: 5\n            successThreshold: 1\n            timeoutSeconds: 1\n          name: ${appName}-${envName}\n          readinessProbe:\n            failureThreshold: 3\n            httpGet:\n              path: /actuator/health/readiness\n              port: 8081\n              scheme: HTTP\n            initialDelaySeconds: ${readinessInitialDelay}\n            periodSeconds: 5\n            successThreshold: 1\n            timeoutSeconds: 1\n          resources:\n            limits:\n              cpu: \'4\'\n              memory: 8Gi\n            requests:\n              cpu: 100m\n              memory: 4Gi\n          securityContext:\n            privileged: true\n          stdin: true\n          tty: true\n          volumeMounts:\n            - mountPath: /etc/localtime\n              name: volume-localtime\n      dnsPolicy: ClusterFirst\n      imagePullSecrets:\n        - name: admin-cr\n      restartPolicy: Always\n      volumes:\n        - hostPath:\n            path: /etc/localtime\n          name: volume-localtime', '', '2021-12-22 09:39:05', '2021-12-27 02:51:01');
INSERT INTO `template` VALUES (7, 'k8s-服务模板-gray', 3, 'KUBERNETES', 'SERVICE', 'yaml', 'vars:\n  # 必须指定\n  appName: ', '---\napiVersion: v1\nkind: Service\nmetadata:\n  name: ${appName}-${envName}\n  namespace: ${envName}\n  labels:\n    env: ${envName}\n    micrometer-prometheus-discovery: \'true\'\nspec:\n  ports:\n    - name: http\n      port: 8080\n      protocol: TCP\n      targetPort: 8080\n    - name: http-mgmt\n      port: 8081\n      protocol: TCP\n      targetPort: 8081\n  selector:\n    app: ${appName}-${envName}\n  sessionAffinity: None\n  type: ClusterIP', '', '2021-12-23 07:50:55', '2021-12-23 07:51:24');
INSERT INTO `template` VALUES (8, 'k8s-无状态模板-prod', 4, 'KUBERNETES', 'DEPLOYMENT', 'yaml', 'vars:\n  # 必须指定\n  appName: \n  javaOpts: \'-Xms4096M -Xmx4096M -Xmn2048M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256M -Xss256K -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80\'\n  springCustomOpts: \'\'\n  livenessInitialDelay: 120\n  readinessInitialDelay: 30', '---\napiVersion: apps/v1\nkind: Deployment\nmetadata:\n  labels:\n    app: ${appName}-${envName}\n    env: ${envName}\n  name: ${appName}-${envName}\n  namespace: ${envName}\nspec:\n  progressDeadlineSeconds: 600\n  replicas: 2\n  revisionHistoryLimit: 10\n  selector:\n    matchLabels:\n      app: ${appName}-${envName}\n  strategy:\n    rollingUpdate:\n      maxSurge: 50%\n      maxUnavailable: 50%\n    type: RollingUpdate\n  template:\n    metadata:\n      annotations:\n        armsPilotAutoEnable: \'on\'\n        armsPilotCreateAppName: ${appName}-${envName}\n      labels:\n        app: ${appName}-${envName}\n    spec:\n      containers:\n        - env:\n            - name: JAVA_OPTS\n              value: ${javaOpts}\n            - name: SPRING_OPTS\n              valueFrom:\n                configMapKeyRef:\n                  key: spring-common-opts\n                  name: java-options\n            - name: JAR_NAME\n              value: ${appName}.jar\n            - name: SPRING_CUSTOM_OPTS\n              value: ${springCustomOpts}\n          image: >-\n            aliyun-cr-uk.chuanyinet.com/service/${appName}\n          imagePullPolicy: Always\n          lifecycle:\n            preStop:\n              exec:\n                command:\n                  - curl\n                  - \'http://127.0.0.1:8081/actuator/shutdown\'\n                  - \'-X\'\n                  - POST\n          livenessProbe:\n            failureThreshold: 3\n            httpGet:\n              path: /actuator/health/liveness\n              port: 8081\n              scheme: HTTP\n            initialDelaySeconds: ${livenessInitialDelay}\n            periodSeconds: 5\n            successThreshold: 1\n            timeoutSeconds: 1\n          name: ${appName}-${envName}\n          readinessProbe:\n            failureThreshold: 3\n            httpGet:\n              path: /actuator/health/readiness\n              port: 8081\n              scheme: HTTP\n            initialDelaySeconds: ${readinessInitialDelay}\n            periodSeconds: 5\n            successThreshold: 1\n            timeoutSeconds: 1\n          resources:\n            limits:\n              cpu: \'4\'\n              memory: 8Gi\n            requests:\n              cpu: 100m\n              memory: 4Gi\n          securityContext:\n            privileged: true\n          stdin: true\n          tty: true\n          volumeMounts:\n            - mountPath: /etc/localtime\n              name: volume-localtime\n      dnsPolicy: ClusterFirst\n      imagePullSecrets:\n        - name: admin-cr\n      restartPolicy: Always\n      volumes:\n        - hostPath:\n            path: /etc/localtime\n          name: volume-localtime', '', '2021-12-27 11:48:49', '2021-12-28 06:43:08');
INSERT INTO `template` VALUES (9, 'k8s-服务模板-prod', 4, 'KUBERNETES', 'SERVICE', 'yaml', 'vars:\n  # 必须指定\n  appName: ', '---\napiVersion: v1\nkind: Service\nmetadata:\n  name: ${appName}-${envName}\n  namespace: ${envName}\n  labels:\n    env: ${envName}\n    micrometer-prometheus-discovery: \'true\'\nspec:\n  ports:\n    - name: http\n      port: 8080\n      protocol: TCP\n      targetPort: 8080\n    - name: http-mgmt\n      port: 8081\n      protocol: TCP\n      targetPort: 8081\n  selector:\n    app: ${appName}-${envName}\n  sessionAffinity: None\n  type: ClusterIP', '', '2021-12-27 12:02:29', '2021-12-27 12:02:43');
COMMIT;

-- ----------------------------
-- Table structure for terminal_session
-- ----------------------------
DROP TABLE IF EXISTS `terminal_session`;
CREATE TABLE `terminal_session` (
  `id` int(2) unsigned NOT NULL AUTO_INCREMENT,
  `session_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '会话uuid',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `remote_addr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `session_closed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '会话是否关闭',
  `close_time` timestamp NULL DEFAULT NULL COMMENT '关闭时间',
  `server_hostname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '服务端主机名',
  `server_addr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '服务端地址',
  `session_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `session_id` (`session_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of terminal_session
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for terminal_session_instance
-- ----------------------------
DROP TABLE IF EXISTS `terminal_session_instance`;
CREATE TABLE `terminal_session_instance` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `session_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '会话id',
  `instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '实例id',
  `duplicate_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '会话复制实例id',
  `instance_session_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例会话类型',
  `login_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '登录账户',
  `host_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '主机ip',
  `output_size` bigint(20) NOT NULL DEFAULT '0' COMMENT '输出文件大小',
  `instance_closed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否关闭',
  `open_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '打开时间',
  `close_time` timestamp NULL DEFAULT NULL COMMENT '关闭时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `instance_idCopy` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '实例id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `session_id` (`session_id`,`instance_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of terminal_session_instance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for terminal_session_instance_command
-- ----------------------------
DROP TABLE IF EXISTS `terminal_session_instance_command`;
CREATE TABLE `terminal_session_instance_command` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `terminal_session_instance_id` int(11) NOT NULL COMMENT '实例表id',
  `prompt` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '提示符',
  `input` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '输入',
  `input_formatted` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '输入格式化',
  `output` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '输出',
  `is_formatted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否格式化',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of terminal_session_instance_command
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '前端框架用户UUID',
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '姓名',
  `display_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '显示名称',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '邮箱',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `last_login` timestamp NULL DEFAULT NULL,
  `wechat` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '手机',
  `created_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `source` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '数据源',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='oc用户本地用户';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'admin', '0000000000000001', NULL, '白衣', '作者', 'ixrjog@qq.com', 1, NULL, '', '13456789101', '', '', NULL, '2021-12-29 09:31:26', '2021-12-29 09:32:33');
COMMIT;

-- ----------------------------
-- Table structure for user_credential
-- ----------------------------
DROP TABLE IF EXISTS `user_credential`;
CREATE TABLE `user_credential` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `valid` tinyint(1) DEFAULT NULL COMMENT '有效',
  `user_id` int(11) DEFAULT NULL,
  `credential_type` int(11) NOT NULL COMMENT '用户凭据分类',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `credential` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '凭据内容',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '有效期',
  `fingerprint` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户凭据';

-- ----------------------------
-- Records of user_credential
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '组名',
  `group_type` int(2) DEFAULT NULL COMMENT '组类型',
  `source` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '数据源',
  `allow_order` tinyint(1) DEFAULT NULL COMMENT '允许工单申请',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户组';

-- ----------------------------
-- Records of user_group
-- ----------------------------
BEGIN;
INSERT INTO `user_group` VALUES (1, 'default', 0, '', 1, NULL, '2021-06-16 16:08:03', '2021-06-16 16:08:11');
INSERT INTO `user_group` VALUES (21, 'nexus-admin', 0, '', 0, 'nexus管理员组', '2021-11-04 10:00:53', '2021-11-04 11:54:11');
INSERT INTO `user_group` VALUES (23, 'nexus-users', 0, '', 1, 'nexus普通用户组(可下载依赖包)', '2021-11-04 10:01:12', '2021-11-04 11:54:32');
INSERT INTO `user_group` VALUES (24, 'nexus-developer', 0, '', 0, 'nexus研发用户组(可部署依赖包)', '2021-11-04 10:44:32', '2021-11-04 11:54:51');
INSERT INTO `user_group` VALUES (25, 'vpn-users', 0, '', 1, '', '2021-11-05 03:39:49', '2021-11-05 03:39:49');
INSERT INTO `user_group` VALUES (26, 'jenkins-users', 0, '', 1, '', '2021-11-08 10:49:24', '2021-11-08 10:49:24');
INSERT INTO `user_group` VALUES (27, 'jenkins-administrators', 0, '', 0, '', '2021-11-08 10:49:40', '2021-11-08 10:49:40');
INSERT INTO `user_group` VALUES (28, 'confluence-administrators', 0, '', 0, 'confluence-administrators', '2021-11-29 11:43:02', '2021-11-29 11:43:02');
INSERT INTO `user_group` VALUES (29, 'confluence-users', 0, '', 1, 'confluence-users', '2021-11-29 11:43:43', '2021-11-29 11:43:43');
INSERT INTO `user_group` VALUES (30, 'ldap-admin', 0, '', 0, 'ldap管理员用户', '2021-11-30 09:17:35', '2021-11-30 09:17:35');
INSERT INTO `user_group` VALUES (31, 'jira-administrators', 0, '', 0, 'jira-administrators', '2021-11-30 09:26:16', '2021-11-30 09:26:16');
INSERT INTO `user_group` VALUES (32, 'jira-software-users', 0, '', 1, 'jira-software-users', '2021-11-30 09:26:25', '2021-11-30 09:26:25');
INSERT INTO `user_group` VALUES (33, 'jira-project-manager', 0, '', 1, 'jira项目管理员（可创建项目）', '2021-12-09 11:21:26', '2021-12-09 11:21:26');
INSERT INTO `user_group` VALUES (34, 'sonar-administrators', 0, '', 0, 'sonar管理员组', '2021-12-14 10:07:00', '2021-12-14 10:07:00');
INSERT INTO `user_group` VALUES (35, 'sonar-users', 0, '', 1, 'sonar用户', '2021-12-14 10:28:09', '2021-12-14 10:28:09');
COMMIT;

-- ----------------------------
-- Table structure for user_permission
-- ----------------------------
DROP TABLE IF EXISTS `user_permission`;
CREATE TABLE `user_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `business_id` int(11) NOT NULL COMMENT '业务id',
  `business_type` int(2) NOT NULL COMMENT '业务类型',
  `permission_role` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色',
  `rate` int(11) DEFAULT '0' COMMENT '等级',
  `content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `user_id` (`user_id`,`business_id`,`business_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户-用户组 授权表';

-- ----------------------------
-- Records of user_permission
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_token
-- ----------------------------
DROP TABLE IF EXISTS `user_token`;
CREATE TABLE `user_token` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户登录名',
  `token` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '登录唯一标识',
  `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否无效。0：无效；1：有效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `username` (`username`,`id`,`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_token
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
