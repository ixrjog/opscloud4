-- MySQL dump 10.13  Distrib 8.0.15, for osx10.14 (x86_64)
--
-- Host: rm-bp1x04709pb2t44p4vo.mysql.rds.aliyuncs.com    Database: opscloud-pre
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;

--
-- GTID state at the beginning of the backup 
--


--
-- Table structure for table `oc_account`
--

DROP TABLE IF EXISTS `oc_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_account` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_uid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '多实例主账户id',
  `account_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'id',
  `account_type` int(2) NOT NULL COMMENT '账户类型',
  `user_id` int(11) DEFAULT NULL COMMENT 'user表pk',
  `username` varchar(128) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(256) DEFAULT NULL,
  `name` varchar(128) DEFAULT '' COMMENT '姓名',
  `display_name` varchar(128) NOT NULL DEFAULT '' COMMENT '显示名称',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '邮箱',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `last_login` int(11) DEFAULT NULL,
  `wechat` varchar(128) DEFAULT '',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '手机',
  `ssh_key` int(2) NOT NULL DEFAULT '0' COMMENT '密钥',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_type` (`account_type`,`username`),
  UNIQUE KEY `account_id` (`account_id`,`account_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='oc用户本地用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_account`
--

LOCK TABLES `oc_account` WRITE;
/*!40000 ALTER TABLE `oc_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_log`
--

DROP TABLE IF EXISTS `oc_aliyun_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_aliyun_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_uid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '阿里云账户uid',
  `project` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `logstore` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `config` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_uid` (`account_uid`,`project`,`logstore`,`config`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='阿里云日志服务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_log`
--

LOCK TABLES `oc_aliyun_log` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_aliyun_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_log_member`
--

DROP TABLE IF EXISTS `oc_aliyun_log_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_aliyun_log_member` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `log_id` int(11) NOT NULL COMMENT '阿里云账户uid',
  `server_group_id` int(11) NOT NULL,
  `server_group_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `topic` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `last_push_time` timestamp NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `log_id` (`log_id`,`server_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='阿里云日志服务服务器组成员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_log_member`
--

LOCK TABLES `oc_aliyun_log_member` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_log_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_aliyun_log_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_ram_permission`
--

DROP TABLE IF EXISTS `oc_aliyun_ram_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_aliyun_ram_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_uid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '阿里云账户uid',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `policy_id` int(11) NOT NULL COMMENT '策略id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_uid` (`account_uid`,`user_id`,`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='阿里云RAM用户策略授权表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_ram_permission`
--

LOCK TABLES `oc_aliyun_ram_permission` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_ram_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_aliyun_ram_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_ram_policy`
--

DROP TABLE IF EXISTS `oc_aliyun_ram_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_aliyun_ram_policy` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_uid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '阿里云账户uid',
  `policy_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '策略名称',
  `policy_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '策略类型',
  `description` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '策略说明',
  `default_version` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '默认版本',
  `attachment_count` int(11) DEFAULT NULL COMMENT '引用次数',
  `in_workorder` int(1) NOT NULL DEFAULT '1' COMMENT '允许工单申请',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL COMMENT '策略创建时间',
  `update_date` timestamp NULL DEFAULT NULL COMMENT '策略更新时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_uid` (`account_uid`,`policy_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='阿里云访问控制用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_ram_policy`
--

LOCK TABLES `oc_aliyun_ram_policy` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_ram_policy` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_aliyun_ram_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_ram_user`
--

DROP TABLE IF EXISTS `oc_aliyun_ram_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_aliyun_ram_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_uid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '阿里云账户uid',
  `ram_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `ram_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `ram_display_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `access_keys` int(11) NOT NULL DEFAULT '0',
  `ram_type` int(11) NOT NULL DEFAULT '0' COMMENT '账户类型',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL COMMENT '账户创建时间',
  `update_date` timestamp NULL DEFAULT NULL COMMENT '账户更新时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `account_uid` (`account_uid`,`ram_username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='阿里云访问控制用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_ram_user`
--

LOCK TABLES `oc_aliyun_ram_user` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_ram_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_aliyun_ram_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_ansible_playbook`
--

DROP TABLE IF EXISTS `oc_ansible_playbook`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_ansible_playbook` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `playbook_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '脚本uuid',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `playbook` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '脚本内容',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'task_tags配置项目',
  `extra_vars` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '外部变量配置',
  `user_id` int(11) NOT NULL COMMENT '创建者',
  `user_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `use_type` int(11) DEFAULT NULL COMMENT '使用类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_ansible_playbook`
--

LOCK TABLES `oc_ansible_playbook` WRITE;
/*!40000 ALTER TABLE `oc_ansible_playbook` DISABLE KEYS */;
INSERT INTO `oc_ansible_playbook` VALUES (10,'2438ed041ef64688913ac6407f7ad99e','test','---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name: test\n      tags: test\n      debug: msg=\"env = {{ env }}, serverName = {{ serverName }}\"\n    - name: test2\n      tags: test2\n      shell: pwd\n    - name: test3\n      tags: test3\n      ping:\n    - name: test4\n      tags: test4\n      ping:\n    - name: test5\n      tags: test55\n      ping:',NULL,'tasks:\n  - name: 测试1\n    tags: test\n  - name: 测试2\n    tags: test2\n  - name: 测试3\n    tags: test3\n  - name: 测试4\n    tags: test4\n  - name: 测试5\n    tags: test55\n    choose: false','vars:\n  env: dev\n  serverName: server-test-2',0,NULL,NULL,'2020-04-13 09:22:11','2020-04-14 07:33:29'),(13,'0c3967f438064014821bdc5aa906dfc6','init-cloud-server','---\n- name: init aliyun ecs\n  include: \"{{ basePath }}/aliyunECS.yml\"\n  when: cloudServerType == \"ECS\"\n- name: init aws ec2\n  include: \"{{ basePath }}/awsEC2.yml\"\n  when: cloudServerType == \"EC2\"','初始化云服务器','tasks:\n  - name: 初始化基础配置，包含hostname、yum、时区 \n    tags: base\n  - name: 初始化dns \n    tags: dns\n  - name: 安装logtail  \n    tags: logtail\n  - name: 安装中间件相关包 \n    tags: middleware\n  - name: 安装压测监控agent包 \n    tags: mon-qa\n  - name: 初始化应用发布相关 \n    tags: app-deploy','vars:\n  basePath: /data/opscloud-data/ansible/playbook/init-cloud-server',357,'{\"createTime\":1587572054000,\"displayName\":\"修远\",\"email\":\"xiuyuan@gegejia.com\",\"id\":357,\"isActive\":true,\"password\":\"\",\"phone\":\"15067127069\",\"source\":\"ldap\",\"updateTime\":1587572054000,\"username\":\"xujian\",\"uuid\":\"cbfbebd87fbb422d95121b21683cd380\"}',NULL,'2020-04-26 03:45:26','2020-07-10 06:03:48'),(15,'ca1bbb0388e74b618f2de61f611e5e31','ecs-put-admin-key','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: create user admin\n      user:\n        name: admin\n        shell: /bin/bash\n        createhome: yes\n        home: /home/admin\n        state: present\n    - name: create key directory\n      file: path=/home/admin/.ssh/ state=directory  owner=admin group=admin mode=0700\n    - name: sudo admin\n      lineinfile:\n        dest: /etc/sudoers\n        state: present\n        regexp: \'^admin\'\n        line: \'admin ALL=(ALL) NOPASSWD: ALL\'\n    - name: touch authorized_keys\n      shell: touch /home/admin/.ssh/authorized_keys\n    - name: put key1\n      lineinfile:\n        dest: /home/admin/.ssh/authorized_keys\n        state: present\n        regexp: \'admin@gegejia.com\'\n        line: \'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCglWchy1aAKsGX6m2c6Jy/Rr5BZsTmGU4RYDX1VfcOaEMdJM8R0MmNvBSYHfByaNd0hq1JZUaP8jiufj9y2z2CnR3bLP9sGU3NZ+6WN5bVXfQ6w9FLLbiBYEJ3o5t5HaFoS+ObPsIMixkPgb4l82FInXBRVAVQu+PHey0MuuB0XgPCa8z1yOdwHM77Yf96TGpzeE/J7BJqTKDBcx75WduzQfOWPgdnoO8X2CzkUnFbCKuu/lFXljN0NNayYNna5/JcbKrs1ncXG4u+5BtCvnThtKMNDhdhdfSFYiEL0Rm0AvKnEDH4nIsErHG2+izOWpmHTMQxV5lu+pbA+BCDxAqH admin@gegejia.com\'\n    - name: put public key\n      shell: chmod 0600 /home/admin/.ssh/authorized_keys && chown admin.admin /home/admin/.ssh/authorized_keys\n    - name: add alias for user admin\n      lineinfile:\n        dest: /home/admin/.bash_profile\n        state: present\n        regexp: \'^alias\'\n        line: \'alias s=\"sudo su -\"\'\n      ignore_errors: yes\n    - name: restart ssh serbvice\n      shell: service sshd restart',NULL,NULL,NULL,352,'{\"createTime\":1590516392000,\"displayName\":\"王炸\",\"email\":\"wangzha@gegejia.com\",\"id\":352,\"isActive\":true,\"password\":\"\",\"phone\":\"15212777257\",\"source\":\"ldap\",\"updateTime\":1592504535000,\"username\":\"wangzha\",\"uuid\":\"7265e36b45cb42f4b704a20cf20e15d7\"}',NULL,'2020-06-23 05:50:38','2020-06-23 05:50:38'),(16,'77c99477e1e944adaf716ea764b672e0','ecs-put-gegejia-key','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: create user gegejia\n      user:\n        name: gegejia\n        shell: /bin/bash\n        createhome: yes\n        home: /home/gegejia\n        state: present\n    - name: create key directory\n      file: path=/home/gegejia/.ssh/ state=directory  owner=gegejia group=gegejia mode=0700\n    - name: sudo gegejia\n      lineinfile:\n        dest: /etc/sudoers\n        state: present\n        regexp: \'^gegejia\'\n        line: \'gegejia ALL=NOPASSWD:/bin/su app, /bin/docker, /bin/gunzip\'\n    - name: touch new gegejia authorized_keys\n      shell: touch /home/gegejia/.ssh/authorized_keys\n    - name: put key1\n      lineinfile:\n        dest: /home/gegejia/.ssh/authorized_keys\n        state: present\n        regexp: \'gegejia3@gegejia.com$\'\n        line: \'ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEApJkYsdT07JWZoMmX+cGQLzQZqP3aySrzQt/WLZ8I0FRbkyQacvT+LRgfPUGL5N7memGfpoAb2f1zwXJ9bhjnKgHkBbr8dEanFtv8PkqmNUy9YBSZdTKkkYL9n7Lp8LYtKwsFy2sntuv3eM9BZRjMMXP2itCulWE1rDjqPfCWNIT7BKFHkGrlJCInBsEYmNnvz+DmjRTpKbGRDPV3+oYpHIidRgkOE4y1TjagOG4TtcvCLE4IEC1k6aVzkwQd4qFPjUauGcYCvRTrQXiZcQLYwG8ke4RiiqavCZ40Wdi/kry6SRRJIf+7xP/P1tZmzfYkfs0bgiJwzBBl0O7ra1BSQw== gegejia3@gegejia.com\'\n    - name: put old gegejia\n      lineinfile:\n        dest: /home/gegejia/.ssh/authorized_keys\n        state: present\n        regexp: \'gegejia@jumpserver-1$\'\n        line: \'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDAE1EvGQ8Ktur4mlh9z45YxhngbSLIw7I8EGFJe/F+k0rUOYflQtDeiWtTsPzNyamrHPuMDLqNsJT9jpmnzOqNmH0jUbDvQWOhwMzHQxm+Zgw91StYfwA921MrjUl+foBsGmIOchaMpBVJj1ESORqryRoq/5tAu4tEii/a3KeJUSBT7w5AVyqQyf97Gd7jS+kfhXhKhwltg/Gh6byscWzK7REL6Ms4rhYQ0vB2q+7YFlL3qfQwpA7JW47OGgYf/Tq/2MzK2w1Ato7Scd2u/sy8ChA6jj0+1TUi/ohwgX4GnrkIpPOh/60g8nzcDtD2B1O3mFoWerQd48M8YLZ1gOYD gegejia@jumpserver-1\'\n    - name: put public key\n      shell: chmod 0600 /home/gegejia/.ssh/authorized_keys && chown gegejia.gegejia /home/gegejia/.ssh/authorized_keys\n    - name: add alias for user gegejia\n      lineinfile:\n        dest: /home/gegejia/.bash_profile\n        state: present\n        line: \'source /etc/profile\'\n      ignore_errors: yes\n    - name: restart ssh serbvice\n      shell: service sshd restart','',NULL,NULL,352,'{\"createTime\":1590516392000,\"displayName\":\"王炸\",\"email\":\"wangzha@gegejia.com\",\"id\":352,\"isActive\":true,\"password\":\"\",\"phone\":\"15212777257\",\"source\":\"ldap\",\"updateTime\":1592504535000,\"username\":\"wangzha\",\"uuid\":\"7265e36b45cb42f4b704a20cf20e15d7\"}',NULL,'2020-06-23 06:01:38','2020-06-23 06:01:52'),(17,'fba885c0a6cf42a8bf1a719279abb8a5','ec2-put-admin-key','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: create user admin\n      when: cloudServerType == \"EC2\"\n      user:\n        name: admin\n        shell: /bin/bash\n        createhome: yes\n        home: /home/admin\n        state: present\n    - name: create key directory\n      when: cloudServerType == \"EC2\"\n      file: path=/home/admin/.ssh/ state=directory  owner=admin group=admin mode=0700\n    - name: sudo admin\n      when: cloudServerType == \"EC2\"\n      lineinfile:\n        dest: /etc/sudoers\n        state: present\n        regexp: \'^admin\'\n        line: \'admin ALL=(ALL) NOPASSWD: ALL\'\n    - name: sshconfig\n      when: cloudServerType == \"EC2\"\n      lineinfile:\n        dest: /etc/ssh/sshd_config\n        state: absent\n        regexp: \'^AllowUsers\'\n    - name: touch authorized_keys\n      when: cloudServerType == \"EC2\"\n      shell: sudo touch /home/admin/.ssh/authorized_keys\n    - name: put key1\n      when: cloudServerType == \"EC2\"\n      lineinfile:\n        dest: /home/admin/.ssh/authorized_keys\n        state: present\n        regexp: \'admin@gegejia.com\'\n        line: \'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCglWchy1aAKsGX6m2c6Jy/Rr5BZsTmGU4RYDX1VfcOaEMdJM8R0MmNvBSYHfByaNd0hq1JZUaP8jiufj9y2z2CnR3bLP9sGU3NZ+6WN5bVXfQ6w9FLLbiBYEJ3o5t5HaFoS+ObPsIMixkPgb4l82FInXBRVAVQu+PHey0MuuB0XgPCa8z1yOdwHM77Yf96TGpzeE/J7BJqTKDBcx75WduzQfOWPgdnoO8X2CzkUnFbCKuu/lFXljN0NNayYNna5/JcbKrs1ncXG4u+5BtCvnThtKMNDhdhdfSFYiEL0Rm0AvKnEDH4nIsErHG2+izOWpmHTMQxV5lu+pbA+BCDxAqH admin@gegejia.com\'\n    - name: put public key\n      when: cloudServerType == \"EC2\"\n      shell: sudo chmod 0600 /home/admin/.ssh/authorized_keys && sudo chown admin.admin /home/admin/.ssh/authorized_keys\n    - name: restart ssh service\n      when: cloudServerType == \"EC2\"\n      shell: sudo service sshd restart\n    - name: add alias for user admin\n      when: cloudServerType == \"EC2\"\n      lineinfile:\n        dest: /home/admin/.bash_profile\n        state: present\n        regexp: \'^alias\'\n        line: \'alias s=\"sudo su -\"\'\n      ignore_errors: yes',NULL,NULL,NULL,352,'{\"createTime\":1590516392000,\"displayName\":\"王炸\",\"email\":\"wangzha@gegejia.com\",\"id\":352,\"isActive\":true,\"password\":\"\",\"phone\":\"15212777257\",\"source\":\"ldap\",\"updateTime\":1592504535000,\"username\":\"wangzha\",\"uuid\":\"7265e36b45cb42f4b704a20cf20e15d7\"}',NULL,'2020-06-23 06:04:39','2020-06-23 06:04:39'),(18,'55f0d231be9a4797a3ea6dde6059432d','ec2-put-gegejia-key','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: create user gegejia\n      when: cloudServerType == \"EC2\"\n      user:\n        name: gegejia\n        shell: /bin/bash\n        createhome: yes\n        home: /home/gegejia\n        state: present\n    - name: create key directory\n      when: cloudServerType == \"EC2\"\n      file: path=/home/gegejia/.ssh/ state=directory  owner=gegejia group=gegejia mode=0700\n    - name: sudo gegejia\n      when: cloudServerType == \"EC2\"\n      lineinfile:\n        dest: /etc/sudoers\n        state: present\n        regexp: \'^admin\'\n        line: \'admin ALL=(ALL) NOPASSWD: ALL\'\n    - name: sshconfig\n      when: cloudServerType == \"EC2\"\n      lineinfile:\n        dest: /etc/ssh/sshd_config\n        state: absent\n        regexp: \'^AllowUsers\'\n    - name: put gegejia key3\n      when: cloudServerType == \"EC2\"\n      lineinfile:\n        dest: /home/gegejia/.ssh/authorized_keys\n        state: present\n        line: \'ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEApJkYsdT07JWZoMmX+cGQLzQZqP3aySrzQt/WLZ8I0FRbkyQacvT+LRgfPUGL5N7memGfpoAb2f1zwXJ9bhjnKgHkBbr8dEanFtv8PkqmNUy9YBSZdTKkkYL9n7Lp8LYtKwsFy2sntuv3eM9BZRjMMXP2itCulWE1rDjqPfCWNIT7BKFHkGrlJCInBsEYmNnvz+DmjRTpKbGRDPV3+oYpHIidRgkOE4y1TjagOG4TtcvCLE4IEC1k6aVzkwQd4qFPjUauGcYCvRTrQXiZcQLYwG8ke4RiiqavCZ40Wdi/kry6SRRJIf+7xP/P1tZmzfYkfs0bgiJwzBBl0O7ra1BSQw== gegejia3@gegejia.com\'\n    - name: put public key\n      when: cloudServerType == \"EC2\"\n      shell: sudo chmod 0600 /home/gegejia/.ssh/authorized_keys && sudo chown gegejia.gegejia /home/gegejia/.ssh/authorized_keys\n    - name: restart ssh service\n      when: cloudServerType == \"EC2\"\n      shell: sudo service sshd restart\n    - name: add alias for user admin\n      when: cloudServerType == \"EC2\"\n      lineinfile:\n        dest: /home/admin/.bash_profile\n        state: present\n        line: \'alias s=\"sudo su -\"\'\n      ignore_errors: yes',NULL,NULL,NULL,352,'{\"createTime\":1590516392000,\"displayName\":\"王炸\",\"email\":\"wangzha@gegejia.com\",\"id\":352,\"isActive\":true,\"password\":\"\",\"phone\":\"15212777257\",\"source\":\"ldap\",\"updateTime\":1592504535000,\"username\":\"wangzha\",\"uuid\":\"7265e36b45cb42f4b704a20cf20e15d7\"}',NULL,'2020-06-23 06:05:34','2020-06-23 06:05:34'),(19,'77d6cc5a8fd94c6aa9dc2952436f2999','get-nginx-custom-subdomain-conf','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: get nginx conf\n      fetch:\n        src: /data/vulcan-data/nginx-conf/nginx-custom/{{ domain }}/vhost_{{ subdomain }}.conf\n        dest: /data/opscloud-data/vulcan-data/nginx-conf/nginx-custom/{{ domain }}/vhost_{{ subdomain }}.conf\n        flat: yes','nginx配置文件获取','','vars:\n  domain: gegejia.com\n  subdomain: x21321.gegejia.com',357,'{\"createTime\":1590516392000,\"displayName\":\"徐建\",\"email\":\"xiuyuan@gegejia.com\",\"id\":357,\"isActive\":true,\"password\":\"\",\"phone\":\"15067127069\",\"source\":\"ldap\",\"updateTime\":1594227670000,\"username\":\"xujian\",\"uuid\":\"dea7f627e3e847cf89a25a574e3c14c2\"}',NULL,'2020-07-09 06:56:48','2020-07-10 06:03:58'),(20,'0952cfdb63604f59b879ef074865cce0','push-nginx-custom-subdomain-conf','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: push nginx conf\n      copy:\n        src: /data/opscloud-data/vulcan-data/nginx-conf/nginx-custom/{{ domain }}/vhost_{{ subdomain }}.conf\n        dest: /data/nginx/conf/{{ domain }}/vhost_{{ subdomain }}.conf\n        owner: root\n        group: root\n      become: yes\n      notify:\n       - reloaded service\n  handlers:  \n    - name: reloaded service\n      shell: /etc/init.d/nginx reload','nginx配置文件推送',NULL,'vars:\n  domain: gegejia.com\n  subdomain: x21321.gegejia.com',357,'{\"createTime\":1590516392000,\"displayName\":\"徐建\",\"email\":\"xiuyuan@gegejia.com\",\"id\":357,\"isActive\":true,\"password\":\"\",\"phone\":\"15067127069\",\"source\":\"ldap\",\"updateTime\":1594227670000,\"username\":\"xujian\",\"uuid\":\"dea7f627e3e847cf89a25a574e3c14c2\"}',NULL,'2020-07-10 02:18:32','2020-07-10 06:04:07'),(21,'c9e540f080b44155bf618e51ec725273','get-nginx-custom-tcp-conf','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: get nginx conf\n      fetch:\n        src: /data/vulcan-data/nginx-conf/nginx-custom/vhost-tcp/vhost_port{{ listenerPort }}.conf\n        dest: /data/opscloud-data/vulcan-data/nginx-conf/nginx-custom/vhost-tcp/vhost_port{{ listenerPort }}.conf\n        flat: yes','nginx配置文件获取',NULL,'vars:\n  listenerPort: 10000',357,'{\"createTime\":1590516392000,\"displayName\":\"徐建\",\"email\":\"xiuyuan@gegejia.com\",\"id\":357,\"isActive\":true,\"password\":\"\",\"phone\":\"15067127069\",\"source\":\"ldap\",\"updateTime\":1594227670000,\"username\":\"xujian\",\"uuid\":\"dea7f627e3e847cf89a25a574e3c14c2\"}',NULL,'2020-07-10 03:23:03','2020-07-14 07:50:55'),(22,'6f50b9c9cf6649f98d4b87db86fc4f63','push-nginx-custom-tcp-conf','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: push nginx conf\n      copy:\n        src: /data/opscloud-data/vulcan-data/nginx-conf/nginx-custom/vhost-tcp/vhost_port{{ listenerPort }}.conf\n        dest: /data/nginx/conf/vhost-tcp/vhost_port{{ listenerPort }}.conf\n        owner: root\n        group: root\n      become: yes\n      notify:\n       - reloaded service\n  handlers:  \n    - name: reloaded service\n      shell: /etc/init.d/nginx reload','nginx配置文件推送',NULL,'vars:\n  listenerPort: 10000',357,'{\"createTime\":1590516392000,\"displayName\":\"徐建\",\"email\":\"xiuyuan@gegejia.com\",\"id\":357,\"isActive\":true,\"password\":\"\",\"phone\":\"15067127069\",\"source\":\"ldap\",\"updateTime\":1594227670000,\"username\":\"xujian\",\"uuid\":\"dea7f627e3e847cf89a25a574e3c14c2\"}',NULL,'2020-07-10 03:24:50','2020-07-14 07:51:10'),(23,'eb7d780aefb24753a3e343095173b6c3','远程安装pangu-agent','---\n- hosts: \"{{hosts}}\"\n  vars:\n    wget_url: \"{{wget_url}}\"\n    worker_url: \"{{worker_url}}\"\n  tasks:\n    - name: check agent_exist\n      stat:\n        path: /opt/pangu-agent.jar\n      register: exist_result\n    - name: back previous version\n      copy:\n        src: /opt/pangu-agent.jar\n        desc: /opt/pangu-agent.bak.jar\n        follow: yes\n        when: exist_result.state.exists\n    - name: download agent\n        get_url: url={{wget_url}} desc=/opt/pangu-agent.jar\n    - name: start agent\n        shell: \"nohup java -jar /opt/pangu-agent.jar > /logs/agent.log 2>&1 &\"',NULL,'pangu','vars:\n  wget_url\n  worker_url',783,'{\"createTime\":1590516398000,\"displayName\":\"咸鱼\",\"email\":\"xianyu@gegejia.com\",\"id\":783,\"isActive\":true,\"password\":\"\",\"phone\":\"17605814000\",\"source\":\"ldap\",\"updateTime\":1594220497000,\"username\":\"xianyu\",\"uuid\":\"b738d5e7642f4e699ad7911970e2c784\"}',NULL,'2020-07-10 04:13:26','2020-07-10 04:14:00'),(24,'e2cf344c4e324821822ea74f62df15f0','del-nginx-custom-subdomain-conf','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: del nginx conf\n      file:\n        path: /data/nginx/conf/{{ domain }}/vhost_{{ subdomain }}.conf\n        state: absent\n      become: yes\n      notify:\n       - reloaded service\n  handlers:  \n    - name: reloaded service\n      shell: /etc/init.d/nginx reload','删除Ngin新配置文件',NULL,'vars:\n  domain: gegejia.com\n  subdomain: x21321.gegejia.com',357,'{\"createTime\":1590516392000,\"displayName\":\"徐建\",\"email\":\"xiuyuan@gegejia.com\",\"id\":357,\"isActive\":true,\"password\":\"\",\"phone\":\"15067127069\",\"source\":\"ldap\",\"updateTime\":1594227670000,\"username\":\"xujian\",\"uuid\":\"dea7f627e3e847cf89a25a574e3c14c2\"}',NULL,'2020-07-10 06:09:54','2020-07-10 06:09:54'),(25,'3902e39986234fada6cddddd5a855bf8','del-nginx-custom-tcp-conf','---\n- hosts: \"{{ hosts }}\"\n  gather_facts: false\n  tasks:\n    - name: del nginx conf\n      file:\n        path: /data/nginx/conf/vhost-tcp/vhost_port{{ listenerPort }}.conf\n        state: absent\n      become: yes\n      notify:\n       - reloaded service\n  handlers:  \n    - name: reloaded service\n      shell: /etc/init.d/nginx reload','删除Nginx配置文件',NULL,'vars:\n  listenerPort: 10000',357,'{\"createTime\":1590516392000,\"displayName\":\"徐建\",\"email\":\"xiuyuan@gegejia.com\",\"id\":357,\"isActive\":true,\"password\":\"\",\"phone\":\"15067127069\",\"source\":\"ldap\",\"updateTime\":1594227670000,\"username\":\"xujian\",\"uuid\":\"dea7f627e3e847cf89a25a574e3c14c2\"}',NULL,'2020-07-10 06:11:13','2020-07-14 06:08:25'),(26,'823d951fbf134bf8ab49a0d5663d3a62','update-jdk','---\n- hosts: \"{{hosts}}\"\n  gather_facts: fasle\n  tasks:\n    - name: update jdk local_policy.jar\n      shell: cd /usr/local/jdk1.8.0_191/jre/lib/security && wget -O local_policy.jar res.ops.yangege.cn/res/initEcs/jdk/custom/local_policy.jar\n    - name: update jdk US_export_policy.jar\n      shell: cd /usr/local/jdk1.8.0_191/jre/lib/security && wget -O US_export_policy.jar res.ops.yangege.cn/res/initEcs/jdk/custom/US_export_policy.jar','更新jdk中的security',NULL,NULL,357,'{\"createTime\":1590516392000,\"displayName\":\"徐建\",\"email\":\"xiuyuan@gegejia.com\",\"id\":357,\"isActive\":true,\"password\":\"\",\"phone\":\"15067127069\",\"source\":\"ldap\",\"updateTime\":1594227670000,\"username\":\"xujian\",\"uuid\":\"dea7f627e3e847cf89a25a574e3c14c2\"}',NULL,'2020-07-10 07:06:59','2020-07-10 07:07:24'),(27,'60ea4519d68045719a78a8c995c59a11','playbook-ansible-copy-hosts','---\n- hosts: \"{{ hosts }}\"\n  tasks:\n    - name: copy\n      copy:\n        src: \"{{ src }}\"\n        dest: \"{{ dest }}\"\n        owner: app\n        group: app\n      become: yes',NULL,NULL,'',11,'{\"createTime\":1590487591000,\"displayName\":\"白衣\",\"email\":\"baiyi@gegejia.com\",\"id\":11,\"isActive\":true,\"password\":\"\",\"phone\":\"13456768044\",\"source\":\"ldap\",\"updateTime\":1594204127000,\"username\":\"baiyi\",\"uuid\":\"d1a9f173a1d54dd6bc7dbeac7dd64833\"}',NULL,'2020-07-10 09:41:37','2020-07-11 10:15:15');
/*!40000 ALTER TABLE `oc_ansible_playbook` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_ansible_script`
--

DROP TABLE IF EXISTS `oc_ansible_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_ansible_script` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `script_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '脚本uuid',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `script_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '脚本内容',
  `script_lang` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '脚本语法（前端语法高亮）',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建者',
  `user_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `use_type` int(11) DEFAULT NULL COMMENT '使用类型',
  `script_lock` int(1) NOT NULL DEFAULT '0' COMMENT '管理员锁',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_ansible_script`
--

LOCK TABLES `oc_ansible_script` WRITE;
/*!40000 ALTER TABLE `oc_ansible_script` DISABLE KEYS */;
INSERT INTO `oc_ansible_script` VALUES (14,'6ca982d75ffb4e5699a8fa7e5c8a9049','application-stop','#!/bin/bash\n\nif [ $(id -u) != \"0\" ]; then\n    echo \"Error: You must be root to run this script.\"\n    exit 1\nfi\n\nsh /opt/bin/appctl.sh stop','sh','应用下线-停止服务',11,'{\"createTime\":1590516391000,\"displayName\":\"白衣\",\"email\":\"baiyi@gegejia.com\",\"id\":11,\"isActive\":true,\"password\":\"\",\"phone\":\"13456768044\",\"source\":\"ldap\",\"updateTime\":1590516391000,\"username\":\"baiyi\",\"uuid\":\"d746465ad80242b49a42d3c082209969\"}',NULL,1,'2020-05-29 08:01:46','2020-05-29 08:01:46'),(15,'0a8a444392894fceb7790ec3a69cdc67','jms-restart','#!/bin/bash\n\nif [ $(id -u) != \"0\" ]; then\n    echo \"Error: You must be root to run this script.\"\n    exit 1\nfi\n\nkillall /opt/py3/bin/python3\n\nsleep 5\n\nps -ef |grep python3\n\nsource /opt/py3/bin/activate\n\npython /opt/jumpserver/jms start -d &','sh','重启jms',357,'{\"createTime\":1590516392000,\"displayName\":\"修远\",\"email\":\"xiuyuan@gegejia.com\",\"id\":357,\"isActive\":true,\"password\":\"\",\"phone\":\"15067127069\",\"source\":\"ldap\",\"updateTime\":1590516392000,\"username\":\"xujian\",\"uuid\":\"92d32e6ab66641c9aadf58b394aa0b95\"}',NULL,0,'2020-06-02 09:19:12','2020-06-02 09:19:12');
/*!40000 ALTER TABLE `oc_ansible_script` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_auth_group`
--

DROP TABLE IF EXISTS `oc_auth_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_auth_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group_code` varchar(128) DEFAULT NULL,
  `comment` varchar(256) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_auth_group`
--

LOCK TABLES `oc_auth_group` WRITE;
/*!40000 ALTER TABLE `oc_auth_group` DISABLE KEYS */;
INSERT INTO `oc_auth_group` VALUES (1,'auth','权限管理','2020-02-15 09:04:55','2020-02-15 09:04:55'),(2,'cloudServer','云主机管理','2020-02-18 05:59:12','2020-03-01 04:55:27'),(4,'user','用户','2020-02-20 03:58:56','2020-02-20 03:58:56'),(5,'serverGroup','服务器组','2020-02-21 04:09:48','2020-02-21 04:09:48'),(6,'env','环境','2020-02-21 10:08:44','2020-02-21 10:08:44'),(7,'server','服务器','2020-02-22 01:56:35','2020-02-22 01:56:35'),(8,'tag','标签','2020-02-22 07:42:32','2020-02-22 07:42:32'),(9,'userGroup','用户组','2020-02-24 10:05:59','2020-02-24 10:05:59'),(10,'cloudDB','云数据库管理','2020-03-01 04:55:47','2020-03-01 04:55:47'),(13,'jump','堡垒机','2020-03-12 02:31:58','2020-03-12 02:31:58'),(14,'cloudImage','云镜像','2020-03-18 03:48:49','2020-03-18 03:48:49'),(15,'cloudVPC','专有网络','2020-03-19 06:25:57','2020-03-19 06:25:57'),(16,'cloudInstance','云实例管理','2020-03-20 09:50:37','2020-03-20 09:50:37'),(17,'serverTask','服务器任务管理','2020-04-08 08:25:40','2020-04-08 08:25:40'),(18,'org','组织架构','2020-04-21 02:48:25','2020-04-21 02:48:25'),(19,'workorder','工单','2020-05-12 10:15:41','2020-05-13 07:21:58'),(20,'doc','文档','2020-05-13 07:21:39','2020-05-13 07:21:55'),(21,'keybox','密钥管理','2020-05-21 05:34:10','2020-05-21 05:34:10'),(22,'terminal','web终端管理','2020-05-25 09:06:23','2020-05-25 09:06:23'),(23,'serverChange','服务器变更管理','2020-06-03 02:55:37','2020-06-03 02:55:37'),(24,'setting','全局配置','2020-06-04 09:39:25','2020-06-04 09:39:25'),(25,'ram','阿里云访问控制','2020-06-09 10:08:15','2020-06-09 10:08:15'),(26,'cloud','云通用管理','2020-06-12 02:00:26','2020-06-12 02:00:26'),(27,'log','阿里云日志服务','2020-06-13 07:56:57','2020-06-13 07:56:57'),(28,'kubernetes','容器管理','2020-06-24 09:16:44','2020-07-09 01:57:13'),(29,'profile','配置文件','2020-07-09 01:57:08','2020-07-09 01:57:08');
/*!40000 ALTER TABLE `oc_auth_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_auth_menu`
--

DROP TABLE IF EXISTS `oc_auth_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_auth_menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色id',
  `menu_type` int(1) NOT NULL DEFAULT '0' COMMENT '0:aside 1:header',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `menu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_auth_menu`
--

LOCK TABLES `oc_auth_menu` WRITE;
/*!40000 ALTER TABLE `oc_auth_menu` DISABLE KEYS */;
INSERT INTO `oc_auth_menu` VALUES (10,1,0,'super_admin','[\n  {\n    title: \'全局配置\',\n    icon: \'cogs\',\n    children: [\n      { path: \'/env\', title: \'环境配置\', icon: \'cog\' },\n      { path: \'/tag\', title: \'标签配置\', icon: \'tags\' },\n      { path: \'/workorder/mgmt\', title: \'工单配置\', icon: \'ticket\' },\n      { path: \'/setting/keybox\', title: \'密钥管理\', icon: \'key\' },\n      { path: \'/setting/global\', title: \'系统参数\', icon: \'cog\' }\n    ]\n  },\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      // tencent\n      { path: \'/cloud/server/cvm\', title: \'CVM\', iconSvg: \'tencent-cvm\' },\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n  {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' },\n      { path: \'/cloud/aliyun/log\', title: \'日志服务管理\', iconSvg: \'aliyun-log\' }\n    ]\n  },\n  {\n    title: \'Kubernetes\',\n    icon: \'dropbox\',\n    children: [\n      { path: \'/kubernetes/application\', title: \'应用管理\', iconSvg: \'k8s-application\' },\n      { path: \'/kubernetes/application/instance\', title: \'应用实例管理\', iconSvg: \'k8s-instance\' },\n      { path: \'/kubernetes/cluster\', title: \'集群管理\', iconSvg: \'kubernetes\' },\n      { path: \'/kubernetes/deployment\', title: \'无状态管理\', iconSvg: \'k8s-deployment\' },\n      { path: \'/kubernetes/service\', title: \'服务管理\', iconSvg: \'k8s-service\' },\n      { path: \'/kubernetes/template\', title: \'模版管理\', iconSvg: \'YAML\' }\n    ]\n  },\n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'堡垒机\',\n    icon: \'empire\',\n    children: [\n      { path: \'/jump/jumpserver/settings\', title: \'Jump设置\', icon: \'cog\' },\n      { path: \'/jump/jumpserver/user\', title: \'用户管理\', icon: \'user\' },\n      { path: \'/jump/jumpserver/asset\', title: \'资产管理\', icon: \'server\' },\n      { path: \'/term/session\', title: \'Web终端会话\', icon: \'server\' } \n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/user/retired\', title: \'用户离职管理\', icon: \'user\' },\n      { path: \'/org\', title: \'组织架构\', icon:\'sitemap\'},\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible/mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/profile/subscription\', title: \'配置订阅\', iconSvg: \'subscription\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'RBAC配置\',\n    icon: \'address-card\',\n    children: [\n      { path: \'/auth/resource\', title: \'资源管理\', icon: \'modx\' },\n      { path: \'/auth/role\', title: \'角色管理\', icon: \'users\' },\n      { path: \'/auth/user/role\', title: \'用户角色配置\', icon: \'id-card\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n','2020-04-23 00:56:17','2020-07-11 01:41:38'),(11,2,0,'admin','[\n  {\n    title: \'全局配置\',\n    icon: \'cogs\',\n    children: [\n      { path: \'/env\', title: \'环境配置\', icon: \'cog\' },\n      { path: \'/tag\', title: \'标签配置\', icon: \'cog\' }\n    ]\n  },\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      // tencent\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n   {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' }\n    ]\n  }, \n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'堡垒机\',\n    icon: \'empire\',\n    children: [\n      { path: \'/jump/jumpserver/settings\', title: \'设置\', icon: \'cog\' },\n      { path: \'/jump/jumpserver/user\', title: \'用户管理\', icon: \'user\' },\n      { path: \'/jump/jumpserver/asset\', title: \'资产管理\', icon: \'server\' },\n      { path: \'/term/session\', title: \'Web终端会话\', icon: \'server\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'RBAC配置\',\n    icon: \'address-card\',\n    children: [\n      { path: \'/auth/resource\', title: \'资源管理\', icon: \'modx\' },\n      { path: \'/auth/role\', title: \'角色管理\', icon: \'users\' },\n      { path: \'/auth/user/role\', title: \'用户角色配置\', icon: \'id-card\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible-mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n','2020-04-23 03:12:46','2020-07-05 12:54:39'),(12,3,0,'ops','[\n  {\n    title: \'全局配置\',\n    icon: \'cogs\',\n    children: [\n      { path: \'/env\', title: \'环境配置\', icon: \'cog\' },\n      { path: \'/tag\', title: \'标签配置\', icon: \'cog\' }\n    ]\n  },\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      // tencent\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n   {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' }\n    ]\n  },\n   {\n    title: \'Kubernetes\',\n    icon: \'dropbox\',\n    children: [\n      { path: \'/kubernetes/application\', title: \'应用管理\', iconSvg: \'k8s-application\' },\n      { path: \'/kubernetes/application/instance\', title: \'应用实例管理\', iconSvg: \'k8s-instance\' },\n      { path: \'/kubernetes/cluster\', title: \'集群管理\', iconSvg: \'kubernetes\' },\n      { path: \'/kubernetes/deployment\', title: \'无状态管理\', iconSvg: \'k8s-deployment\' },\n      { path: \'/kubernetes/service\', title: \'服务管理\', iconSvg: \'k8s-service\' },\n      { path: \'/kubernetes/template\', title: \'模版管理\', iconSvg: \'YAML\' }\n    ]\n  },\n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'堡垒机\',\n    icon: \'empire\',\n    children: [\n      { path: \'/jump/jumpserver/settings\', title: \'设置\', icon: \'cog\' },\n      { path: \'/jump/jumpserver/user\', title: \'用户管理\', icon: \'user\' },\n      { path: \'/jump/jumpserver/asset\', title: \'资产管理\', icon: \'server\' },\n      { path: \'/term/session\', title: \'Web终端会话\', icon: \'server\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n   {\n    title: \'RBAC配置\',\n    icon: \'address-card\',\n    children: [\n      { path: \'/auth/resource\', title: \'资源管理\', icon: \'modx\' },\n      { path: \'/auth/role\', title: \'角色管理\', icon: \'users\' },\n      { path: \'/auth/user/role\', title: \'用户角色配置\', icon: \'id-card\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible-mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n','2020-04-23 03:14:17','2020-07-07 09:55:27'),(13,4,0,'dev','[\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n   {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' },\n       { path: \'/cloud/aliyun/log\', title: \'日志服务管理\', iconSvg: \'aliyun-log\' }\n    ]\n  },\n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', iconSvg: \'xterm\' },\n      { path: \'/workbench/application\', title: \'我的应用\', iconSvg: \'k8s-application\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible/mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/profile/subscription\', title: \'配置订阅\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/playbook\', title: \'playbook\', icon: \'recycle\' },\n      { path: \'/task/history\', title: \'任务查询\', icon: \'yelp\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n','2020-04-23 03:17:22','2020-07-10 03:38:05'),(14,5,0,'base','[\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n','2020-04-23 03:18:04','2020-04-23 03:18:04'),(15,13,0,'aliyun_log_admin','[\n  {\n    title: \'服务器管理\',\n    icon: \'server\',\n    children: [\n      { path: \'/server\', title: \'服务器\', icon: \'server\' },\n      { path: \'/server/attribute\', title: \'服务器属性\', icon: \'tag\' },\n      { path: \'/server/group\', title: \'服务器组\', icon: \'window-restore\' },\n      { path: \'/server/group/type\', title: \'服务器组类型\', icon: \'align-justify\' }\n    ]\n  },\n  {\n    title: \'云主机管理\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/server/ecs\', title: \'ECS\', iconSvg: \'aliyun-ecs\' },\n      { path: \'/cloud/server/ec2\', title: \'EC2\', iconSvg: \'amazonaws\' },\n      { path: \'/cloud/server/esxi\', title: \'ESXI\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/vm\', title: \'VM\', iconSvg: \'vmware\' },\n      { path: \'/cloud/server/zabbixhost\', title: \'ZabbixHost\', iconSvg: \'zabbix\' }\n    ]\n  },\n  {\n    title: \'阿里云\',\n    icon: \'cloud\',\n    children: [\n      { path: \'/cloud/aliyun/ram\', title: \'访问控制管理\', iconSvg: \'aliyun-ram\' },\n      { path: \'/cloud/aliyun/log\', title: \'日志服务管理\', iconSvg: \'aliyun-log\' }\n    ]\n  },\n  {\n    title: \'云数据库\',\n    icon: \'database\',\n    children: [\n      { path: \'/cloud/db/instance\', title: \'数据库实例\', icon: \'cube\' },\n      { path: \'/cloud/db/database\', title: \'数据库\', icon: \'cubes\' },\n      { path: \'/cloud/db/database/slowlog\', title: \'慢日志\', icon: \'warning\' },\n      { path: \'/cloud/db/my/database\', title: \'我的数据库\', icon: \'diamond\' }\n    ]\n  },\n  {\n    title: \'我的工作台\',\n    icon: \'desktop\',\n    children: [\n      { path: \'/workbench/workorder\', title: \'工单\', icon: \'list\' },\n      { path: \'/workbench/jump\', title: \'堡垒机\', icon: \'terminal\' },\n      { path: \'/workbench/xterm\', title: \'web终端\', icon: \'object-ungroup\' }\n    ]\n  },\n  {\n    title: \'用户管理\',\n    icon: \'user-circle\',\n    children: [\n      { path: \'/user\', title: \'用户\', icon: \'user\' },\n      { path: \'/user/group\', title: \'用户组\', icon: \'users\' },\n      { path: \'/org/department\', title: \'部门管理\', icon: \'connectdevelop\' }\n    ]\n  },\n  {\n    title: \'任务管理\',\n    icon: \'th-list\',\n    children: [\n      { path: \'/task/ansible-mgmt\', title: \'配置管理\', icon: \'file-text-o\' },\n      { path: \'/task/command\', title: \'批量命令\', icon: \'terminal\' },\n      { path: \'/task/script\', title: \'批量脚本\', icon: \'retweet\' },\n      { path: \'/task/ansible\', title: \'playbook\', icon: \'recycle\' }\n    ]\n  },\n  {\n    title: \'个人详情\',\n    icon: \'user-o\',\n    children: [\n      { path: \'/user/detail\', title: \'我的详情\', icon: \'address-book\' }\n    ]\n  }\n]\n','2020-06-15 09:45:51','2020-06-15 09:46:32'),(16,15,0,'pangu','','2020-07-10 03:36:38','2020-07-10 03:36:38');
/*!40000 ALTER TABLE `oc_auth_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_auth_resource`
--

DROP TABLE IF EXISTS `oc_auth_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_auth_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL COMMENT '资源组id',
  `resource_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资源名称',
  `comment` varchar(256) DEFAULT NULL,
  `need_auth` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否鉴权',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource_name` (`resource_name`)
) ENGINE=InnoDB AUTO_INCREMENT=280 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_auth_resource`
--

LOCK TABLES `oc_auth_resource` WRITE;
/*!40000 ALTER TABLE `oc_auth_resource` DISABLE KEYS */;
INSERT INTO `oc_auth_resource` VALUES (1,1,'/log/login','用户登录接口',0,'2020-02-18 10:39:09','2020-02-18 10:39:09'),(2,1,'/auth/role/page/query','角色分页查询',1,'2020-02-14 02:40:38','2020-02-19 11:14:06'),(3,1,'/auth/role/add','新增角色',1,'2020-02-14 12:27:57','2020-02-19 11:14:07'),(4,1,'/auth/role/update','更新角色配置',1,'2020-02-14 12:28:11','2020-02-19 11:14:07'),(5,1,'/auth/role/del','删除角色',1,'2020-02-14 12:28:36','2020-02-19 11:14:08'),(6,1,'/auth/resource/page/query','资源分页查询',1,'2020-02-14 14:11:59','2020-02-19 11:14:09'),(7,1,'/auth/resource/add','新增资源',1,'2020-02-14 14:12:14','2020-02-19 11:14:10'),(8,1,'/auth/resource/update','更新资源配置',1,'2020-02-14 14:12:23','2020-02-19 11:14:11'),(9,1,'/auth/resource/del','删除资源',1,'2020-02-14 14:12:33','2020-02-19 11:14:11'),(10,1,'/auth/group/page/query','资源组分页查询',1,'2020-02-15 09:33:30','2020-02-19 11:14:12'),(11,2,'/cloud/server/page/query','云主机分页查询',1,'2020-02-17 02:20:44','2020-03-01 03:17:41'),(12,2,'/cloud/server/del','删除云主机',1,'2020-02-17 09:11:16','2020-03-01 03:17:44'),(13,2,'/cloud/server/sync','云端同步主机',1,'2020-02-17 09:11:22','2020-03-10 05:46:29'),(15,1,'/auth/group/add','新增资源组',1,'2020-02-18 05:57:54','2020-02-19 11:14:15'),(16,1,'/auth/group/del','删除资源组',1,'2020-02-18 05:58:03','2020-02-19 11:14:16'),(17,1,'/auth/group/update','更新资源组',1,'2020-02-18 05:58:27','2020-02-19 11:14:17'),(26,1,'/auth/resource/updateNeedAuth','更新资源是否鉴权',1,'2020-02-18 11:02:53','2020-02-19 11:14:17'),(27,1,'/auth/role/resource/bind/page/query','角色已绑定资源分页查询',1,'2020-02-19 04:08:55','2020-02-19 11:14:18'),(28,1,'/auth/role/resource/unbind/page/query','角色未绑定资源分页查询',1,'2020-02-19 04:09:39','2020-02-19 11:16:48'),(29,1,'/auth/role/resource/bind','角色绑定资源',1,'2020-02-19 11:04:14','2020-02-19 11:24:41'),(30,1,'/auth/role/resource/unbind','角色解除绑定资源',1,'2020-02-19 11:04:22','2020-02-19 11:24:42'),(31,1,'/auth/user/role/page/query','用户角色分页查询',1,'2020-02-20 02:36:25','2020-02-20 02:36:41'),(32,4,'/user/page/query','用户查询接口',1,'2020-02-20 03:59:30','2020-02-20 03:59:30'),(33,1,'/auth/user/role/add','新增用户角色配置',1,'2020-02-20 04:33:40','2020-02-20 04:33:40'),(34,1,'/auth/user/role/del','删除用户角色配置',1,'2020-02-20 04:34:01','2020-02-20 04:34:01'),(35,5,'/server/group/page/query','服务器组分页查询列表',1,'2020-02-21 04:10:28','2020-02-21 04:13:48'),(36,5,'/server/group/add','新增服务器组配置',1,'2020-02-21 04:11:00','2020-02-21 05:59:36'),(37,5,'/server/group/update','更新服务器组配置',1,'2020-02-21 04:11:18','2020-02-21 05:59:40'),(38,5,'/server/group/del','删除服务器组配置',1,'2020-02-21 04:11:26','2020-02-21 05:59:45'),(39,5,'/server/group/type/page/query','服务器组类型分页查询',1,'2020-02-21 05:58:22','2020-02-22 01:57:49'),(40,5,'/server/group/type/add','新增服务器组类型配置',1,'2020-02-21 05:58:38','2020-02-21 06:00:00'),(41,5,'/server/group/type/update','更新服务器组类型配置',1,'2020-02-21 05:58:47','2020-02-21 05:59:54'),(42,5,'/server/group/type/del','删除服务器组类型配置',1,'2020-02-21 05:58:55','2020-02-21 06:00:07'),(43,6,'/env/page/query','环境类型分页查询',1,'2020-02-21 10:09:05','2020-02-22 01:57:45'),(44,6,'/env/add','新增环境类型配置',1,'2020-02-21 10:09:19','2020-02-21 10:10:21'),(45,6,'/env/update','更新环境类型配置',1,'2020-02-21 10:09:28','2020-02-21 10:10:07'),(46,6,'/env/del','删除环境类型配置',1,'2020-02-21 10:09:39','2020-02-21 10:10:16'),(47,7,'/server/page/query','服务器分页查询列表',1,'2020-02-22 01:56:52','2020-02-22 01:57:32'),(48,7,'/server/add','新增服务器信息',1,'2020-02-22 01:56:58','2020-02-22 01:58:04'),(49,7,'/server/update','修改服务器信息',1,'2020-02-22 01:57:07','2020-02-22 01:58:10'),(50,7,'/server/del','删除服务器信息',1,'2020-02-22 01:57:16','2020-02-22 01:58:16'),(51,8,'/tag/page/query','标签分页查询列表',1,'2020-02-22 07:43:01','2020-02-22 07:44:38'),(52,8,'/tag/add','新增标签信息',1,'2020-02-22 07:43:12','2020-02-22 07:44:47'),(53,8,'/tag/update','修改标签信息',1,'2020-02-22 07:43:34','2020-02-22 07:46:28'),(54,8,'/tag/del','删除标签信息',1,'2020-02-22 07:43:43','2020-02-22 07:47:07'),(55,8,'/tag/business/query','查询业务标签',1,'2020-02-22 09:52:58','2020-02-22 09:56:07'),(56,8,'/tag/business/notin/query','查询业务未使用标签',1,'2020-02-22 10:10:48','2020-02-22 10:10:59'),(57,8,'/tag/business/update','更新业务对象标签',1,'2020-02-22 15:14:42','2020-02-22 15:14:42'),(58,4,'/user/page/fuzzy/query','用户模糊查询接口',1,'2020-02-23 08:40:16','2020-02-23 08:45:52'),(59,4,'/user/password/random','获取用户随机密码',1,'2020-02-23 11:04:13','2020-02-23 11:04:17'),(60,4,'/user/update','更新用户信息(需2次鉴权)',1,'2020-02-23 14:19:07','2020-02-23 14:19:07'),(61,9,'/user/group/page/query','用户组分页查询列表',1,'2020-02-24 10:05:19','2020-02-24 10:06:33'),(62,9,'/user/group/add','新增用户组信息',1,'2020-02-24 10:06:28','2020-02-24 10:07:19'),(63,9,'/user/group/ldap/sync','同步用户组信息(ldap)',1,'2020-02-24 10:06:59','2020-02-24 10:07:31'),(64,9,'/user/group/del','删除用户组信息',1,'2020-02-24 10:07:33','2020-02-24 10:07:48'),(65,4,'/user/ldap/sync','同步用户信息(ldap)',1,'2020-02-26 02:59:06','2020-02-26 02:59:18'),(66,4,'/user/detail','查询个人详情',1,'2020-02-26 06:44:00','2020-02-26 06:44:26'),(67,4,'/user/token/apply','用户申请ApiToken',1,'2020-02-27 04:08:46','2020-03-06 04:50:39'),(68,4,'/user/credential/save','用户保存凭据',1,'2020-02-27 09:14:42','2020-02-27 09:14:45'),(69,10,'/cloud/db/page/fuzzy/query','云数据库实例分页(模糊)查询列表',1,'2020-03-01 04:56:17','2020-03-01 08:23:38'),(70,10,'/cloud/db/del','删除云数据库实例',1,'2020-03-01 08:22:47','2020-03-01 08:23:39'),(71,10,'/cloud/db/sync','同步云数据库实例',1,'2020-03-01 08:23:10','2020-03-01 08:23:46'),(72,10,'/cloud/db/database/sync','同步云数据库实例中的数据库信息',1,'2020-03-02 01:46:48','2020-03-02 01:46:55'),(73,10,'/cloud/db/database/page/fuzzy/query','云数据库分页(模糊)查询列表',1,'2020-03-02 05:16:28','2020-03-02 05:34:37'),(74,10,'/cloud/db/database/update','更新云数据库基本属性',1,'2020-03-02 06:39:27','2020-03-02 06:39:48'),(75,10,'/cloud/db/account/privilege','云数据库权限变更',1,'2020-03-03 08:56:59','2020-03-03 08:57:31'),(76,7,'/server/page/fuzzy/query','服务器分页模糊查询列表',1,'2020-03-04 04:23:56','2020-03-04 04:31:33'),(77,4,'/user/create','创建用户',1,'2020-03-05 04:35:59','2020-03-05 04:36:04'),(78,4,'/user/include/group/page/query','查询用户授权的用户组分页信息',1,'2020-03-05 06:44:23','2020-03-05 06:44:23'),(79,4,'/user/exclude/group/page/query','查询用户未授权的用户组分页信息',1,'2020-03-05 06:44:27','2020-03-05 06:44:41'),(80,4,'/user/group/revoke','撤销用户的用户组授权',1,'2020-03-05 09:55:24','2020-03-05 09:55:24'),(81,4,'/user/group/grant','用户组授权给用户',1,'2020-03-05 09:55:28','2020-03-05 09:58:56'),(82,5,'/server/group/user/include/page/query','分页查询user授权的服务器组列表',1,'2020-03-06 02:00:38','2020-03-06 02:02:32'),(85,5,'/server/group/user/exclude/page/query','分页查询user未授权的服务器组列表',1,'2020-03-06 02:00:53','2020-03-06 02:05:55'),(86,5,'/server/group/revoke','撤销用户的服务器组授权',1,'2020-03-06 03:40:19','2020-03-06 03:40:58'),(87,5,'/server/group/grant','服务器组授权给用户',1,'2020-03-06 03:40:30','2020-03-06 03:44:36'),(88,4,'/user/token/del','用户删除ApiToken',1,'2020-03-06 04:27:08','2020-03-06 04:27:15'),(89,5,'/server/group/attribute/query','查询服务器组属性',1,'2020-03-06 10:05:50','2020-03-07 03:35:20'),(90,5,'/server/group/attribute/save','保存服务器组属性',1,'2020-03-07 03:53:37','2020-03-07 03:53:55'),(91,7,'/server/attribute/query','查询服务器属性',1,'2020-03-09 16:13:18','2020-03-09 16:13:26'),(92,7,'/server/attribute/save','保存服务器属性',1,'2020-03-09 16:29:51','2020-03-09 16:29:59'),(93,13,'/jump/jumpserver/user/page/fuzzy/query','分页查询jumpserver用户列表',1,'2020-03-12 02:50:24','2020-03-12 02:50:24'),(94,13,'/jump/jumpserver/user/sync','同步用户',1,'2020-03-12 04:54:57','2020-03-12 04:55:10'),(95,13,'/jump/jumpserver/asset/sync','同步资产',1,'2020-03-12 06:43:11','2020-03-12 06:43:23'),(96,13,'/jump/jumpserver/asset/page/fuzzy/query','分页查询jumpserver资产列表',1,'2020-03-12 08:37:43','2020-03-12 08:37:52'),(97,13,'/jump/jumpserver/user/active/set','设置用户是否有效',1,'2020-03-13 04:17:09','2020-03-13 04:17:29'),(98,13,'/jump/jumpserver/assetsNode/page/query','分页查询资产节点',1,'2020-03-13 05:28:55','2020-03-13 05:37:49'),(99,13,'/jump/jumpserver/settings/query','jumpserver全局配置查询',1,'2020-03-13 08:02:47','2020-03-13 08:03:05'),(100,13,'/jump/jumpserver/terminal/query','jumpserver终端信息查询',1,'2020-03-13 10:15:23','2020-03-13 10:15:49'),(101,13,'/jump/jumpserver/settings/save','jumpserver全局配置保存',1,'2020-03-13 11:05:32','2020-03-13 11:05:51'),(102,13,'/jump/jumpserver/user/admin/page/fuzzy/query','jumpserver查询管理员列表',1,'2020-03-14 05:07:54','2020-03-14 05:08:17'),(103,13,'/jump/jumpserver/user/admin/auth','管理员授权',1,'2020-03-15 10:26:32','2020-03-15 10:26:46'),(104,13,'/jump/jumpserver/user/admin/revoke','撤销管理员授权',1,'2020-03-16 00:49:12','2020-03-16 00:49:23'),(105,13,'/jump/jumpserver/terminal/session/page/query','分页查询jumpserver活动会话',1,'2020-03-16 03:05:10','2020-03-16 03:09:13'),(106,14,'/cloud/image/page/fuzzy/query','分页模糊查询云镜像列表',1,'2020-03-18 03:49:28','2020-03-18 03:49:28'),(107,14,'/cloud/image/sync','同步指定的云镜像',1,'2020-03-18 03:49:35','2020-03-18 03:49:51'),(108,14,'/cloud/image/del','删除指定的云镜像',1,'2020-03-18 06:29:21','2020-03-18 06:31:17'),(109,15,'/cloud/vpc/page/fuzzy/query','分页模糊查询云VPC列表',1,'2020-03-19 06:26:31','2020-03-19 06:26:31'),(110,15,'/cloud/vpc/sync','同步指定的云VPC',1,'2020-03-19 06:26:49','2020-03-19 06:27:05'),(111,15,'/cloud/vpc/del','删除指定的VPC',1,'2020-03-19 06:27:10','2020-03-19 06:27:24'),(112,14,'/cloud/image/active/set','设置云镜像是否有效',1,'2020-03-20 01:32:06','2020-03-20 01:32:28'),(115,15,'/cloud/vpc/active/set','设置云VPC是否有效',1,'2020-03-20 01:47:45','2020-03-20 01:47:51'),(116,16,'/cloud/instance/template/page/fuzzy/query','分页模糊查询云实例模版列表',1,'2020-03-20 09:51:14','2020-03-20 09:51:14'),(117,16,'/cloud/instance/type/page/fuzzy/query','分页模糊查询云实例类型列表',1,'2020-03-23 07:08:09','2020-03-23 07:08:21'),(118,16,'/cloud/instance/type/sync','同步云实例类型',1,'2020-03-23 07:08:29','2020-03-23 07:08:40'),(119,16,'/cloud/instance/region/query','查询配置的地区',1,'2020-03-23 08:24:20','2020-03-23 08:24:49'),(120,16,'/cloud/instance/cpu/query','查询cpu规格',1,'2020-03-23 09:15:18','2020-03-23 09:15:31'),(121,16,'/cloud/instance/template/save','保存云实例模版',1,'2020-03-24 01:58:21','2020-03-24 01:58:21'),(122,16,'/cloud/instance/template/yaml/save','保存云实例模版YAML',1,'2020-03-24 03:05:16','2020-03-24 03:05:33'),(123,16,'/cloud/instance/template/del','删除指定的云实例模版',1,'2020-03-24 05:33:36','2020-03-24 05:33:50'),(124,15,'/cloud/vpc/page/query','分页查询云VPC列表(按可用区过滤)',1,'2020-03-24 10:35:24','2020-03-24 10:35:24'),(125,15,'/cloud/vpc/security/group/page/query','分页查询云VPC安全组列表',1,'2020-03-25 03:07:18','2020-03-25 03:07:30'),(126,16,'/cloud/instance/template/vswitch/query','查询模版可用区中虚拟交换机详情',1,'2020-03-26 10:26:07','2020-03-26 10:26:31'),(127,15,'/cloud/vpc/security/group/active/set','设置VPC安全组是否有效',1,'2020-03-27 05:52:22','2020-03-27 05:52:22'),(128,15,'/cloud/vpc/vswitch/page/query','分页查询云VPC虚拟交换机列表',1,'2020-03-27 06:24:34','2020-03-27 06:24:46'),(129,15,'/cloud/vpc/vswitch/active/set','设置VPC虚拟交换机是否有效',1,'2020-03-27 06:58:10','2020-03-27 06:59:31'),(130,16,'/cloud/instance/create','创建云实例',1,'2020-03-30 02:11:37','2020-03-30 02:11:37'),(131,16,'/cloud/instance/task/query','查询创建实例任务详情',1,'2020-03-31 06:26:28','2020-03-31 06:26:39'),(132,16,'/cloud/instance/task/last/query','查询创建实例任务详情（最后一个任务)',1,'2020-03-31 06:27:09','2020-03-31 06:27:24'),(133,10,'/cloud/db/database/slowlog/page/query','慢日志查询',1,'2020-04-03 10:53:43','2020-04-03 10:53:43'),(134,9,'/user/server/tree/query','查询user授权的服务器树',1,'2020-04-07 07:27:52','2020-04-07 07:27:52'),(135,17,'/server/task/command/executor','批量命令',1,'2020-04-08 08:27:25','2020-04-08 08:27:25'),(136,17,'/server/task/query','查询任务',1,'2020-04-09 05:02:07','2020-04-09 05:02:22'),(137,17,'/server/task/ansible/hosts/create','创建ansible主机配置文件',1,'2020-04-11 11:56:27','2020-04-11 11:56:27'),(138,17,'/server/task/abort','中止任务',1,'2020-04-12 13:59:40','2020-04-12 14:00:12'),(139,17,'/server/task/member/abort','中止子任务',1,'2020-04-12 14:00:13','2020-04-12 14:00:29'),(140,17,'/server/task/playbook/page/query','分页模糊查询playbook列表',1,'2020-04-13 09:02:00','2020-04-13 09:02:00'),(141,17,'/server/task/playbook/add','新增playbook',1,'2020-04-14 05:35:37','2020-04-14 05:35:37'),(142,17,'/server/task/playbook/update','更新playbook',1,'2020-04-14 05:36:02','2020-04-14 05:36:02'),(143,17,'/server/task/playbook/del','删除指定的playbook',1,'2020-04-14 05:36:17','2020-04-14 05:36:17'),(144,17,'/server/task/playbook/executor','执行playbook',1,'2020-04-14 07:50:46','2020-04-14 07:50:46'),(145,17,'/server/task/script/page/query','分页模糊查询script/列表',1,'2020-04-16 07:34:39','2020-04-16 07:34:39'),(146,17,'/server/task/script/executor','执行script',1,'2020-04-16 09:23:32','2020-04-16 09:23:39'),(147,17,'/server/task/script/add','新增script',1,'2020-04-17 07:17:07','2020-04-17 07:17:28'),(148,17,'/server/task/script/update','更新script',1,'2020-04-17 07:17:18','2020-04-17 07:17:37'),(149,17,'/server/task/script/del','删除指定的script',1,'2020-04-17 07:17:45','2020-04-17 07:17:52'),(150,17,'/server/task/ansible/version','查询ansible版本详情',1,'2020-04-20 05:22:36','2020-04-20 05:22:36'),(151,17,'/server/task/ansible/hosts/preview','预览ansible主机配置文件',1,'2020-04-20 06:07:36','2020-04-20 06:08:05'),(152,18,'/org/department/tree/query','查询部门树',1,'2020-04-21 02:49:22','2020-04-21 02:49:22'),(153,18,'/org/department/tree/drop','组织架构部门drop',1,'2020-04-21 06:15:23','2020-04-21 06:15:23'),(154,18,'/org/department/member/page/query','分页查询部门成员',1,'2020-04-21 09:45:56','2020-04-21 09:45:56'),(155,18,'/org/department/member/add','添加部门成员',1,'2020-04-22 01:24:21','2020-04-22 01:24:51'),(156,18,'/org/department/member/remove','移除部门成员',1,'2020-04-22 01:24:22','2020-04-22 01:25:01'),(158,18,'/org/department/member/leader/update','更新部门成员经理属性',1,'2020-04-22 02:02:12','2020-04-22 02:03:17'),(159,18,'/org/department/member/approval/update','更新部门成员审批权属性',1,'2020-04-22 02:02:33','2020-04-22 02:03:18'),(160,18,'/org/department/add','新增部门',1,'2020-04-22 05:37:17','2020-04-22 05:37:17'),(161,18,'/org/department/update','更新部门',1,'2020-04-22 05:37:36','2020-04-22 05:37:36'),(162,18,'/org/department/del','删除部门',1,'2020-04-22 07:17:33','2020-04-22 07:17:33'),(163,18,'/org/department/query','查询部门',1,'2020-04-22 07:19:57','2020-04-22 07:19:57'),(164,18,'/org/department/page/query','分页查询查询部门列表',1,'2020-04-22 08:56:33','2020-04-22 08:56:33'),(165,1,'/auth/menu/query','用户菜单查询',1,'2020-04-23 01:41:41','2020-04-23 01:41:41'),(166,18,'/org/department/member/join','加入部门',1,'2020-04-26 01:23:41','2020-04-26 01:23:49'),(167,19,'/workorder/group/page/query','分页工单组列表',1,'2020-04-26 05:32:55','2020-04-26 05:32:55'),(168,19,'/workorder/group/query','工作台查询工单组详情',1,'2020-04-28 08:04:56','2020-04-28 08:04:56'),(169,19,'/workorder/ticket/create','创建工单票据',1,'2020-04-28 11:23:18','2020-04-29 01:53:04'),(170,19,'/workorder/ticket/server/group/query','工单配置-用户组查询',1,'2020-04-29 01:51:59','2020-05-08 05:59:13'),(171,19,'/workorder/ticket/entry/add','工单票据添加条目',1,'2020-04-29 02:56:21','2020-04-29 02:56:21'),(172,19,'/workorder/ticket/entry/del','删除工单票据添加条目',1,'2020-04-29 05:55:25','2020-04-29 05:55:25'),(173,19,'/workorder/ticket/query','查询工单票据详情',1,'2020-04-29 05:55:51','2020-04-29 05:55:51'),(174,19,'/workorder/ticket/entry/update','工单票据更新条目',1,'2020-04-29 07:39:54','2020-04-29 07:39:54'),(175,19,'/workorder/ticket/submit','提交工单票据',1,'2020-05-07 02:27:53','2020-05-07 02:28:10'),(176,19,'/workorder/ticket/my/page/query','查询我的工单',1,'2020-05-07 05:14:59','2020-05-07 05:15:13'),(177,19,'/workorder/ticket/agree','审批同意',1,'2020-05-08 01:34:25','2020-05-08 01:34:25'),(178,19,'/workorder/ticket/disagree','审批拒绝',1,'2020-05-08 01:34:35','2020-05-08 05:59:21'),(179,19,'/workorder/ticket/user/group/query','工单配置-用户组查询',1,'2020-05-08 05:58:53','2020-05-08 05:59:29'),(180,19,'/workorder/ticket/role/query','工单配置-平台角色查询',1,'2020-05-08 07:11:41','2020-05-08 07:11:55'),(181,18,'/org/chart/query','查询组织架构图',1,'2020-05-08 09:25:37','2020-05-08 09:27:38'),(182,20,'/doc/key/query','按key查询文档',0,'2020-05-12 10:16:15','2020-05-13 07:20:57'),(183,20,'/doc/user/type/query','按类型查询用户文档',1,'2020-05-13 07:20:53','2020-05-13 07:22:13'),(184,20,'/doc/user/save','更新帮助文档',1,'2020-05-13 08:19:13','2020-05-13 08:19:13'),(185,4,'/user/setting/group/query','用户查询配置',1,'2020-05-17 07:27:48','2020-05-17 08:07:01'),(186,4,'/user/setting/group/save','用户更新配置',1,'2020-05-17 07:28:03','2020-05-17 07:28:45'),(187,20,'/doc/id/query','按id查询文档',1,'2020-05-18 05:40:11','2020-05-18 05:40:18'),(188,1,'/auth/role/menu/save','保存角色菜单配置',1,'2020-05-19 02:47:21','2020-05-19 02:47:21'),(189,1,'/auth/role/menu/query','查询角色菜单配置',1,'2020-05-19 02:47:40','2020-05-19 02:47:40'),(190,18,'/org/department/user/check','校验用户是否加入部门',1,'2020-05-19 06:36:38','2020-05-19 06:36:38'),(191,19,'/workorder/ticket/page/query','工单管理-工单分页查询',1,'2020-05-20 03:16:17','2020-05-20 03:16:17'),(192,19,'/workorder/ticket/del','删除工单',1,'2020-05-20 10:09:48','2020-05-20 10:09:59'),(193,19,'/workorder/group/save','保存工单组配置',1,'2020-05-21 02:09:56','2020-05-21 02:09:56'),(194,21,'/keybox/page/query','分页查询全局密钥配置',1,'2020-05-21 05:34:35','2020-05-21 05:34:35'),(195,21,'/keybox/add','新增全局密钥配置',1,'2020-05-21 05:34:52','2020-05-21 05:34:52'),(196,21,'/keybox/del','删除指定全局密钥配置',1,'2020-05-21 05:35:12','2020-05-21 05:35:12'),(197,4,'/user/retire','用户离职',1,'2020-05-22 05:12:36','2020-06-20 07:08:21'),(198,7,'/server/query/by/group','按服务器组查询服务器列表',1,'2020-05-22 09:16:40','2020-05-22 09:16:40'),(199,22,'/term/session/page/query','终端会话分页查询',1,'2020-05-25 09:07:06','2020-05-25 09:07:06'),(200,22,'/term/session/instance/query','终端会话实例详情查询',1,'2020-05-25 13:40:25','2020-05-25 14:11:05'),(201,4,'/user/detail/query','按用户名查询user详情',1,'2020-05-26 05:23:59','2020-05-26 05:24:19'),(202,13,'/jump/jumpserver/user/id/sync','同步用户（用户id）',1,'2020-05-30 02:15:30','2020-05-30 02:19:30'),(203,7,'/server/id/query','id查询服务器',1,'2020-06-03 02:39:31','2020-06-03 02:39:31'),(204,23,'/server/change/online','服务器上线',1,'2020-06-03 02:58:25','2020-06-03 02:58:25'),(205,23,'/server/change/offline','服务器下线',1,'2020-06-03 02:58:25','2020-06-03 02:58:25'),(206,23,'/server/change/task/query','查询变更服务器任务',1,'2020-06-03 02:58:25','2020-06-03 02:58:25'),(207,7,'/server/ids/query','ids查询服务器列表',1,'2020-06-03 09:21:03','2020-06-03 09:21:03'),(208,24,'/setting/query','查询全局配置',1,'2020-06-04 09:39:53','2020-06-04 09:39:53'),(209,13,'/jump/jumpserver/user/del','删除用户',1,'2020-06-05 01:36:27','2020-06-05 01:36:27'),(210,13,'/jump/jumpserver/asset/del','删除资产',1,'2020-06-05 02:22:36','2020-06-05 02:22:57'),(211,17,'/server/task/history/page/query','分页查询任务列表',1,'2020-06-05 05:32:58','2020-06-05 05:32:58'),(212,5,'/server/group/property/query','查询服务器组扩展属性',1,'2020-06-06 06:55:26','2020-06-06 06:55:26'),(213,5,'/server/group/property/save','保存服务器组扩展属性',1,'2020-06-06 06:55:26','2020-06-06 06:55:26'),(214,5,'/server/group/property/del','删除服务器组扩展属性',1,'2020-06-06 06:55:26','2020-06-06 06:55:26'),(215,4,'/user/reinstate','用户复职',1,'2020-06-08 01:18:39','2020-06-20 07:08:13'),(216,9,'/user/group/update','更新用户组',1,'2020-06-08 02:39:24','2020-06-08 02:39:24'),(217,5,'/server/group/id/query','按id查询serverGroup详情',1,'2020-06-08 09:04:32','2020-06-08 09:04:32'),(218,25,'/aliyun/ram/user/page/query','查询访问控制用户信息',1,'2020-06-09 10:08:47','2020-06-09 10:32:31'),(219,25,'/aliyun/ram/policy/page/query','查询访问控制策略信息',1,'2020-06-10 06:15:27','2020-06-10 06:15:27'),(220,25,'/aliyun/ram/user/sync','同步用户',1,'2020-06-10 07:53:26','2020-06-10 07:53:51'),(221,25,'/aliyun/ram/policy/sync','同步策略',1,'2020-06-10 07:53:36','2020-06-10 07:53:52'),(222,25,'/aliyun/ram/policy/workorder/set','设置工单申请',1,'2020-06-10 09:12:57','2020-06-10 09:12:57'),(223,26,'/cloud/aliyun/account/query','查询阿里云主账户信息',1,'2020-06-12 02:00:51','2020-06-12 02:00:51'),(224,19,'/workorder/ticket/ram/policy/query','工单配置-RAM策略查询',1,'2020-06-12 03:44:44','2020-06-12 03:44:44'),(225,27,'/aliyun/log/page/query','查询日志服务配置信息',1,'2020-06-13 08:09:47','2020-06-13 08:09:47'),(226,27,'/aliyun/log/project/query','查询日志服务project信息',1,'2020-06-13 08:51:39','2020-06-13 08:52:36'),(227,27,'/aliyun/log/logstore/query','查询日志服务logstore信息',1,'2020-06-13 08:52:01','2020-06-13 08:52:38'),(228,27,'/aliyun/log/config/query','查询日志服务config信息',1,'2020-06-13 08:52:34','2020-06-13 08:52:40'),(229,27,'/aliyun/log/add','新增日志服务配置信息',1,'2020-06-13 09:43:51','2020-06-13 09:43:51'),(230,27,'/aliyun/log/update','更新日志服务配置信息',1,'2020-06-13 09:43:55','2020-06-13 09:44:07'),(231,27,'/aliyun/log/member/page/query','查询日志服务配置成员信息',1,'2020-06-15 02:27:11','2020-06-15 02:27:25'),(232,27,'/aliyun/log/member/add','新增日志服务配置中的服务器组成员',1,'2020-06-15 02:27:26','2020-06-15 02:28:07'),(233,27,'/aliyun/log/member/remove','移除日志服务配置中的服务器组成员',1,'2020-06-15 02:28:02','2020-06-15 02:28:09'),(234,27,'/aliyun/log/member/server/group/query','日志服务配置查询服务器组',1,'2020-06-15 03:07:53','2020-06-15 03:08:08'),(235,27,'/aliyun/log/member/push','推送日志服务配置中的服务器组成员',1,'2020-06-15 07:49:06','2020-06-15 07:49:26'),(236,1,'/home','首页',0,'2020-06-16 11:23:05','2020-06-23 05:30:59'),(237,5,'/server/group/property/set/query','查询服务器组扩展属性',1,'2020-06-22 03:17:02','2020-06-22 03:17:02'),(238,24,'/setting/all/query','查询全局配置',1,'2020-06-23 05:30:33','2020-06-23 05:30:55'),(239,24,'/setting/update','更新全局配置',1,'2020-06-23 05:46:51','2020-06-23 05:56:48'),(240,28,'/kubernetes/cluster/page/query','k8s集群分页查询',1,'2020-06-24 09:17:11','2020-06-24 09:17:11'),(241,28,'/kubernetes/cluster/add','新增集群配置',1,'2020-06-28 03:38:31','2020-06-28 03:39:23'),(242,28,'/kubernetes/cluster/update','更新集群配置',1,'2020-06-28 03:38:40','2020-06-28 03:39:31'),(243,28,'/kubernetes/cluster/del','删除集群配置',1,'2020-06-28 03:38:53','2020-06-28 03:39:39'),(244,28,'/kubernetes/cluster/namespace/page/query','k8s集群命名空间分页查询',1,'2020-06-28 07:44:35','2020-06-28 07:44:43'),(245,28,'/kubernetes/cluster/namespace/exclude/query','查询未配置的集群命名空间',1,'2020-06-28 10:32:13','2020-06-28 10:32:24'),(246,28,'/kubernetes/cluster/namespace/add','新增集群命名空间配置',1,'2020-06-29 01:54:38','2020-06-29 01:55:22'),(247,28,'/kubernetes/cluster/namespace/update','更新集群命名空间配置',1,'2020-06-29 01:54:48','2020-06-29 01:55:18'),(248,28,'/kubernetes/cluster/namespace/del','删除集群命名空间配置',1,'2020-06-29 01:55:12','2020-06-29 01:55:12'),(249,28,'/kubernetes/deployment/page/query','分页查询无状态配置',1,'2020-06-29 08:40:10','2020-06-29 08:40:23'),(250,28,'/kubernetes/template/page/query','分页查询模版配置',1,'2020-06-30 06:52:57','2020-06-30 06:53:17'),(251,28,'/kubernetes/template/add','新增模版配置',1,'2020-06-30 06:53:28','2020-06-30 06:53:55'),(252,28,'/kubernetes/template/update','更新模版配置',1,'2020-06-30 06:53:34','2020-06-30 06:54:03'),(253,28,'/kubernetes/template/del','删除模版配置',1,'2020-06-30 06:53:43','2020-06-30 06:54:08'),(254,28,'/kubernetes/service/page/query','分页查询服务配置',1,'2020-07-01 06:18:59','2020-07-01 06:19:17'),(255,28,'/kubernetes/application/page/query','分页查询应用配置',1,'2020-07-02 03:05:49','2020-07-02 03:06:06'),(256,28,'/kubernetes/application/add','新增应用配置',1,'2020-07-02 03:29:57','2020-07-02 03:30:41'),(257,28,'/kubernetes/application/update','更新应用配置',1,'2020-07-02 03:30:20','2020-07-02 03:30:46'),(258,28,'/kubernetes/application/del','删除应用配置',1,'2020-07-02 03:30:28','2020-07-02 03:30:50'),(259,28,'/kubernetes/application/instance/page/query','分页查询应用实例配置',1,'2020-07-02 06:31:18','2020-07-02 06:31:29'),(260,28,'/kubernetes/application/instance/add','新增应用实例配置',1,'2020-07-02 06:31:45','2020-07-02 06:32:10'),(261,28,'/kubernetes/application/instance/update','更新应用实例配置',1,'2020-07-02 06:31:51','2020-07-02 06:32:16'),(262,28,'/kubernetes/application/instance/del','删除应用实例配置',1,'2020-07-02 06:31:58','2020-07-02 06:32:20'),(263,28,'/kubernetes/application/instance/label/query','查询实例环境标签详情',1,'2020-07-02 07:57:12','2020-07-02 07:57:12'),(264,28,'/kubernetes/deployment/sync','同步无状态配置',1,'2020-07-02 10:17:55','2020-07-02 10:18:05'),(265,28,'/kubernetes/service/sync','同步服务配置',1,'2020-07-03 02:42:14','2020-07-03 02:42:22'),(266,28,'/kubernetes/application/instance/template/page/query','分页查询实例模版配置',1,'2020-07-03 09:37:52','2020-07-03 09:38:15'),(267,28,'/kubernetes/application/my/page/query','分页查询我的应用配置',1,'2020-07-04 06:31:12','2020-07-04 06:31:19'),(268,28,'/kubernetes/pod/query','查询我的容器组详情',1,'2020-07-04 08:59:01','2020-07-04 08:59:01'),(269,28,'/kubernetes/application/instance/deployment/create','创建实例的无状态',1,'2020-07-06 05:28:05','2020-07-06 05:28:55'),(270,28,'/kubernetes/application/instance/deployment/del','删除实例的无状态',1,'2020-07-06 05:28:34','2020-07-06 05:29:08'),(271,28,'/kubernetes/application/instance/service/create','创建实例的服务',1,'2020-07-06 05:29:23','2020-07-06 05:29:41'),(272,28,'/kubernetes/application/instance/service/del','删除实例的服务',1,'2020-07-06 05:29:28','2020-07-06 05:29:48'),(273,29,'/profile/subscription/page/query','分页查询配置文件订阅配置列表',1,'2020-07-09 01:57:44','2020-07-09 01:57:51'),(274,29,'/profile/subscription/add','新增订阅配置',1,'2020-07-10 08:41:45','2020-07-10 08:42:10'),(275,29,'/profile/subscription/update','更新订阅配置',1,'2020-07-10 08:41:57','2020-07-10 08:42:14'),(276,29,'/profile/subscription/publish','发布订阅配置',1,'2020-07-11 09:32:04','2020-07-11 09:32:31'),(277,28,'/kubernetes/application/instance/id/query','查询应用实例配置',1,'2020-07-13 06:08:07','2020-07-13 06:08:25'),(278,28,'/kubernetes/service/query','查询服务详情',1,'2020-07-14 11:37:39','2020-07-14 11:37:39'),(279,28,'/kubernetes/service/del','删除服务',1,'2020-07-15 08:57:20','2020-07-15 09:06:30');
/*!40000 ALTER TABLE `oc_auth_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_auth_role`
--

DROP TABLE IF EXISTS `oc_auth_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_auth_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `access_level` int(11) NOT NULL DEFAULT '0' COMMENT '访问级别',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色描述',
  `in_workorder` int(1) NOT NULL DEFAULT '0' COMMENT '允许工作流申请',
  `resource_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_auth_role`
--

LOCK TABLES `oc_auth_role` WRITE;
/*!40000 ALTER TABLE `oc_auth_role` DISABLE KEYS */;
INSERT INTO `oc_auth_role` VALUES (1,'super_admin',100,'创始人[OC开发者]',0,NULL,'2020-02-18 05:00:50','2020-06-13 07:55:10'),(2,'admin',90,'管理员',0,NULL,'2020-02-14 09:05:06','2020-04-10 18:10:37'),(3,'ops',50,'运维',0,NULL,'2020-02-14 09:05:16','2020-04-10 18:10:40'),(4,'dev',40,'研发',1,NULL,'2020-02-14 09:05:25','2020-04-10 18:10:43'),(5,'base',10,'普通用户',1,NULL,'2020-02-14 09:05:31','2020-04-10 18:10:46'),(10,'api.user.update',0,'更新其他用户凭据（管理员专用）',0,'/user/update','2020-02-23 12:33:50','2020-04-10 18:10:48'),(12,'api.user.credential.save',0,'推送用户凭据，仅用于oc2同步数据',0,'/user/credential/save','2020-05-16 15:22:32','2020-05-16 15:22:32'),(13,'aliyun_log_admin',0,'阿里云日志服务配置管理员',0,'','2020-06-15 09:45:20','2020-06-15 09:45:20'),(14,'sso',0,'单点登录创建用户专用',0,'','2020-06-19 10:08:54','2020-06-19 10:08:54'),(15,'pangu',0,'故障演练平台',0,'','2020-07-09 06:39:18','2020-07-09 06:39:18');
/*!40000 ALTER TABLE `oc_auth_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_auth_role_resource`
--

DROP TABLE IF EXISTS `oc_auth_role_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_auth_role_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `resource_id` int(11) NOT NULL COMMENT '资源id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_id` (`role_id`,`resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=642 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_auth_role_resource`
--

LOCK TABLES `oc_auth_role_resource` WRITE;
/*!40000 ALTER TABLE `oc_auth_role_resource` DISABLE KEYS */;
INSERT INTO `oc_auth_role_resource` VALUES (4,1,4,'2020-02-19 11:22:06','2020-02-19 11:22:06'),(5,1,5,'2020-02-19 11:22:10','2020-02-19 11:22:10'),(6,1,6,'2020-02-19 11:22:14','2020-02-19 11:22:14'),(7,1,7,'2020-02-19 11:22:18','2020-02-19 11:22:18'),(8,1,8,'2020-02-19 11:22:32','2020-02-19 11:22:32'),(9,1,9,'2020-02-19 11:22:35','2020-02-19 11:24:11'),(10,1,10,'2020-02-19 11:22:40','2020-02-19 11:24:11'),(14,1,14,'2020-02-19 11:22:49','2020-02-19 11:24:14'),(15,1,15,'2020-02-19 11:22:52','2020-02-19 11:24:15'),(16,1,16,'2020-02-19 11:22:55','2020-02-19 11:24:16'),(17,1,17,'2020-02-19 11:22:58','2020-02-19 11:24:16'),(18,1,18,'2020-02-19 11:23:01','2020-02-19 11:24:17'),(19,1,19,'2020-02-19 11:23:22','2020-02-19 11:24:17'),(20,1,20,'2020-02-19 11:23:22','2020-02-19 11:24:19'),(21,1,21,'2020-02-19 11:23:35','2020-02-19 11:24:19'),(22,1,22,'2020-02-19 11:23:37','2020-02-19 11:24:20'),(23,1,23,'2020-02-19 11:23:39','2020-02-19 11:24:21'),(24,1,24,'2020-02-19 11:23:41','2020-02-19 11:24:21'),(25,1,25,'2020-02-19 11:23:44','2020-02-19 11:24:22'),(26,1,26,'2020-02-19 11:23:47','2020-02-19 11:24:23'),(27,1,27,'2020-02-19 11:23:50','2020-02-19 11:24:24'),(28,1,28,'2020-02-19 11:23:53','2020-02-19 11:24:24'),(29,1,29,'2020-02-19 11:23:57','2020-02-19 11:24:25'),(30,1,30,'2020-02-19 11:24:00','2020-02-19 11:24:28'),(31,1,11,'2020-02-19 11:48:40','2020-02-19 11:48:40'),(32,1,12,'2020-02-19 11:48:41','2020-02-19 11:48:41'),(33,1,13,'2020-02-19 11:48:44','2020-02-19 11:48:44'),(34,1,3,'2020-02-19 12:07:14','2020-02-19 12:07:14'),(35,1,2,'2020-02-19 12:08:57','2020-02-19 12:08:57'),(36,1,31,'2020-02-20 02:37:52','2020-02-20 02:37:52'),(37,1,32,'2020-02-20 04:00:00','2020-02-20 04:00:00'),(38,2,32,'2020-02-20 04:00:08','2020-02-20 04:00:08'),(39,3,32,'2020-02-20 04:00:15','2020-02-20 04:00:15'),(40,4,32,'2020-02-20 04:00:20','2020-02-20 04:00:20'),(41,1,33,'2020-02-20 04:34:17','2020-02-20 04:34:17'),(42,1,34,'2020-02-20 04:34:19','2020-02-20 04:34:19'),(47,4,35,'2020-02-21 04:14:21','2020-02-21 04:14:21'),(48,1,35,'2020-02-21 06:00:48','2020-02-21 06:00:48'),(49,1,36,'2020-02-21 06:00:49','2020-02-21 06:00:49'),(50,1,37,'2020-02-21 06:00:51','2020-02-21 06:00:51'),(53,1,39,'2020-02-21 06:00:53','2020-02-21 06:00:53'),(55,1,40,'2020-02-21 06:00:55','2020-02-21 06:00:55'),(56,1,41,'2020-02-21 06:00:57','2020-02-21 06:00:57'),(57,1,42,'2020-02-21 06:00:58','2020-02-21 06:00:58'),(58,2,39,'2020-02-21 06:01:08','2020-02-21 06:01:08'),(59,2,40,'2020-02-21 06:01:09','2020-02-21 06:01:09'),(60,2,41,'2020-02-21 06:01:11','2020-02-21 06:01:11'),(61,2,42,'2020-02-21 06:01:12','2020-02-21 06:01:12'),(63,1,44,'2020-02-21 10:10:37','2020-02-21 10:10:37'),(64,1,45,'2020-02-21 10:10:38','2020-02-21 10:10:38'),(65,1,46,'2020-02-21 10:10:39','2020-02-21 10:10:39'),(67,2,44,'2020-02-21 10:10:47','2020-02-21 10:10:47'),(68,2,45,'2020-02-21 10:10:48','2020-02-21 10:10:48'),(69,2,46,'2020-02-21 10:10:49','2020-02-21 10:10:49'),(73,3,46,'2020-02-21 10:11:02','2020-02-21 10:11:02'),(74,1,43,'2020-02-21 10:54:37','2020-02-21 10:54:37'),(75,2,35,'2020-02-21 10:55:08','2020-02-21 10:55:08'),(76,2,36,'2020-02-21 10:55:09','2020-02-21 10:55:09'),(77,2,37,'2020-02-21 10:55:11','2020-02-21 10:55:11'),(78,2,38,'2020-02-21 10:55:12','2020-02-21 10:55:12'),(79,2,11,'2020-02-21 10:55:24','2020-02-21 10:55:24'),(80,2,12,'2020-02-21 10:55:25','2020-02-21 10:55:25'),(81,2,13,'2020-02-21 10:55:26','2020-02-21 10:55:26'),(82,1,47,'2020-02-22 01:58:33','2020-02-22 01:58:33'),(83,1,48,'2020-02-22 01:58:34','2020-02-22 01:58:34'),(84,1,49,'2020-02-22 01:58:35','2020-02-22 01:58:35'),(85,1,50,'2020-02-22 01:58:36','2020-02-22 01:58:36'),(86,2,47,'2020-02-22 01:58:53','2020-02-22 01:58:53'),(87,2,48,'2020-02-22 01:58:55','2020-02-22 01:58:55'),(88,2,49,'2020-02-22 01:58:56','2020-02-22 01:58:56'),(89,2,50,'2020-02-22 01:58:57','2020-02-22 01:58:57'),(90,4,47,'2020-02-22 01:59:03','2020-02-22 01:59:03'),(91,1,51,'2020-02-22 07:48:16','2020-02-22 07:48:16'),(92,1,52,'2020-02-22 07:48:17','2020-02-22 07:48:17'),(93,1,53,'2020-02-22 07:48:18','2020-02-22 07:48:18'),(94,1,54,'2020-02-22 07:48:19','2020-02-22 07:48:19'),(96,1,55,'2020-02-22 09:58:52','2020-02-22 09:58:52'),(97,1,56,'2020-02-22 10:11:08','2020-02-22 10:11:08'),(98,1,57,'2020-02-22 15:15:02','2020-02-22 15:15:02'),(99,1,58,'2020-02-23 08:45:22','2020-02-23 08:45:22'),(100,1,59,'2020-02-23 11:05:44','2020-02-23 11:05:44'),(101,1,61,'2020-02-24 10:13:09','2020-02-24 10:13:09'),(102,1,62,'2020-02-24 10:13:11','2020-02-24 10:13:11'),(103,1,63,'2020-02-24 10:13:12','2020-02-24 10:13:12'),(104,1,64,'2020-02-24 10:13:13','2020-02-24 10:13:13'),(105,1,60,'2020-02-26 02:59:58','2020-02-26 02:59:58'),(106,1,65,'2020-02-26 02:59:59','2020-02-26 02:59:59'),(107,1,66,'2020-02-26 06:45:06','2020-02-26 06:45:06'),(108,1,67,'2020-02-27 04:09:25','2020-02-27 04:09:25'),(109,4,58,'2020-02-27 04:09:36','2020-02-27 04:09:36'),(110,4,59,'2020-02-27 04:09:38','2020-02-27 04:09:38'),(111,4,60,'2020-02-27 04:09:39','2020-02-27 04:09:39'),(112,4,66,'2020-02-27 04:09:42','2020-02-27 04:09:42'),(113,4,67,'2020-02-27 04:09:43','2020-02-27 04:09:43'),(114,1,68,'2020-02-27 10:51:34','2020-02-27 10:51:34'),(115,1,69,'2020-03-02 01:47:28','2020-03-02 01:47:28'),(116,1,70,'2020-03-02 01:47:29','2020-03-02 01:47:29'),(117,1,71,'2020-03-02 01:47:30','2020-03-02 01:47:30'),(118,1,72,'2020-03-02 01:47:32','2020-03-02 01:47:32'),(119,2,69,'2020-03-02 01:47:46','2020-03-02 01:47:46'),(120,2,70,'2020-03-02 01:47:48','2020-03-02 01:47:48'),(121,2,71,'2020-03-02 01:47:49','2020-03-02 01:47:49'),(122,2,72,'2020-03-02 01:47:50','2020-03-02 01:47:50'),(123,1,73,'2020-03-02 05:17:03','2020-03-02 05:17:03'),(124,1,74,'2020-03-02 06:44:07','2020-03-02 06:44:07'),(125,1,75,'2020-03-03 08:58:01','2020-03-03 08:58:01'),(126,1,76,'2020-03-04 04:31:37','2020-03-04 04:31:37'),(127,1,77,'2020-03-05 04:36:21','2020-03-05 04:36:21'),(128,1,78,'2020-03-05 06:46:25','2020-03-05 06:46:25'),(129,1,79,'2020-03-05 06:46:27','2020-03-05 06:46:27'),(130,1,80,'2020-03-05 09:56:24','2020-03-05 09:56:24'),(131,1,81,'2020-03-05 09:56:25','2020-03-05 09:56:25'),(132,1,82,'2020-03-06 02:04:29','2020-03-06 02:04:29'),(133,1,85,'2020-03-06 02:04:31','2020-03-06 02:04:31'),(134,1,86,'2020-03-06 03:45:42','2020-03-06 03:45:42'),(135,1,87,'2020-03-06 03:45:43','2020-03-06 03:45:43'),(136,1,89,'2020-03-07 03:55:06','2020-03-07 03:55:06'),(137,1,90,'2020-03-07 03:55:08','2020-03-07 03:55:08'),(138,1,91,'2020-03-09 16:14:30','2020-03-09 16:14:30'),(139,1,92,'2020-03-09 16:31:02','2020-03-09 16:31:02'),(140,1,88,'2020-03-10 10:41:23','2020-03-10 10:41:23'),(141,1,93,'2020-03-12 02:51:16','2020-03-12 02:51:16'),(142,1,94,'2020-03-12 04:55:39','2020-03-12 04:55:39'),(143,1,95,'2020-03-12 06:44:12','2020-03-12 06:44:12'),(144,1,96,'2020-03-12 08:38:40','2020-03-12 08:38:40'),(145,1,97,'2020-03-13 04:20:33','2020-03-13 04:20:33'),(146,1,98,'2020-03-13 05:35:40','2020-03-13 05:35:40'),(147,1,99,'2020-03-13 08:03:35','2020-03-13 08:03:35'),(148,1,100,'2020-03-13 10:16:11','2020-03-13 10:16:11'),(149,1,101,'2020-03-13 11:06:14','2020-03-13 11:06:14'),(150,1,102,'2020-03-14 05:08:40','2020-03-14 05:08:40'),(151,1,103,'2020-03-15 10:37:58','2020-03-15 10:37:58'),(152,2,2,'2020-03-16 07:09:00','2020-03-16 07:09:00'),(153,2,6,'2020-03-16 07:09:03','2020-03-16 07:09:03'),(154,2,10,'2020-03-16 07:09:05','2020-03-16 07:09:05'),(155,2,58,'2020-03-16 07:09:19','2020-03-16 07:09:19'),(156,2,59,'2020-03-16 07:09:26','2020-03-16 07:09:26'),(157,2,60,'2020-03-16 07:09:37','2020-03-16 07:09:37'),(158,2,65,'2020-03-16 07:09:38','2020-03-16 07:09:38'),(159,2,66,'2020-03-16 07:09:43','2020-03-16 07:09:43'),(160,2,67,'2020-03-16 07:09:45','2020-03-16 07:09:45'),(161,2,68,'2020-03-16 07:09:47','2020-03-16 07:09:47'),(162,2,77,'2020-03-16 07:09:49','2020-03-16 07:09:49'),(163,2,78,'2020-03-16 07:09:51','2020-03-16 07:09:51'),(164,2,79,'2020-03-16 07:09:52','2020-03-16 07:09:52'),(165,2,80,'2020-03-16 07:09:54','2020-03-16 07:09:54'),(166,2,81,'2020-03-16 07:09:55','2020-03-16 07:09:55'),(167,2,88,'2020-03-16 07:09:56','2020-03-16 07:09:56'),(168,2,82,'2020-03-16 07:10:01','2020-03-16 07:10:01'),(169,2,85,'2020-03-16 07:10:02','2020-03-16 07:10:02'),(170,2,86,'2020-03-16 07:10:03','2020-03-16 07:10:03'),(171,2,87,'2020-03-16 07:10:04','2020-03-16 07:10:04'),(172,2,89,'2020-03-16 07:10:05','2020-03-16 07:10:05'),(173,2,90,'2020-03-16 07:10:06','2020-03-16 07:10:06'),(175,2,76,'2020-03-16 07:10:28','2020-03-16 07:10:28'),(176,2,91,'2020-03-16 07:10:29','2020-03-16 07:10:29'),(177,2,92,'2020-03-16 07:10:30','2020-03-16 07:10:30'),(178,2,43,'2020-03-16 07:10:37','2020-03-16 07:10:37'),(179,2,51,'2020-03-16 07:10:44','2020-03-16 07:10:44'),(180,2,52,'2020-03-16 07:10:45','2020-03-16 07:10:45'),(181,2,53,'2020-03-16 07:10:46','2020-03-16 07:10:46'),(182,2,54,'2020-03-16 07:10:47','2020-03-16 07:10:47'),(183,2,55,'2020-03-16 07:10:48','2020-03-16 07:10:48'),(184,2,56,'2020-03-16 07:10:49','2020-03-16 07:10:49'),(185,2,57,'2020-03-16 07:10:50','2020-03-16 07:10:50'),(186,2,61,'2020-03-16 07:10:57','2020-03-16 07:10:57'),(187,2,62,'2020-03-16 07:10:58','2020-03-16 07:10:58'),(188,2,63,'2020-03-16 07:10:59','2020-03-16 07:10:59'),(189,2,64,'2020-03-16 07:11:00','2020-03-16 07:11:00'),(190,2,73,'2020-03-16 07:11:04','2020-03-16 07:11:04'),(191,2,74,'2020-03-16 07:11:06','2020-03-16 07:11:06'),(192,2,75,'2020-03-16 07:11:09','2020-03-16 07:11:09'),(193,2,93,'2020-03-16 07:11:13','2020-03-16 07:11:13'),(194,2,94,'2020-03-16 07:11:14','2020-03-16 07:11:14'),(195,2,95,'2020-03-16 07:11:15','2020-03-16 07:11:15'),(196,2,96,'2020-03-16 07:11:17','2020-03-16 07:11:17'),(197,2,97,'2020-03-16 07:11:18','2020-03-16 07:11:18'),(198,2,98,'2020-03-16 07:11:19','2020-03-16 07:11:19'),(199,2,99,'2020-03-16 07:11:20','2020-03-16 07:11:20'),(200,2,100,'2020-03-16 07:11:21','2020-03-16 07:11:21'),(201,2,101,'2020-03-16 07:11:22','2020-03-16 07:11:22'),(202,2,102,'2020-03-16 07:11:23','2020-03-16 07:11:23'),(203,2,103,'2020-03-16 07:11:24','2020-03-16 07:11:24'),(204,2,104,'2020-03-16 07:11:25','2020-03-16 07:11:25'),(205,2,105,'2020-03-16 07:11:26','2020-03-16 07:11:26'),(206,5,165,'2020-04-26 08:29:34','2020-04-26 08:29:34'),(207,4,165,'2020-04-26 08:29:48','2020-04-26 08:29:48'),(208,3,165,'2020-04-26 08:29:57','2020-04-26 08:29:57'),(209,2,165,'2020-04-26 08:30:06','2020-04-26 08:30:06'),(210,3,11,'2020-04-30 08:01:37','2020-04-30 08:01:37'),(211,3,13,'2020-04-30 08:01:38','2020-04-30 08:01:38'),(212,3,12,'2020-04-30 08:01:39','2020-04-30 08:01:39'),(213,3,2,'2020-04-30 08:01:53','2020-04-30 08:01:53'),(214,3,3,'2020-04-30 08:01:54','2020-04-30 08:01:54'),(219,3,4,'2020-04-30 08:01:55','2020-04-30 08:01:55'),(224,3,5,'2020-04-30 08:02:01','2020-04-30 08:02:01'),(225,3,6,'2020-04-30 08:02:02','2020-04-30 08:02:02'),(226,3,7,'2020-04-30 08:02:03','2020-04-30 08:02:03'),(227,3,8,'2020-04-30 08:02:05','2020-04-30 08:02:05'),(228,3,9,'2020-04-30 08:02:06','2020-04-30 08:02:06'),(229,3,10,'2020-04-30 08:02:07','2020-04-30 08:02:07'),(230,3,15,'2020-04-30 08:02:09','2020-04-30 08:02:09'),(231,3,16,'2020-04-30 08:02:10','2020-04-30 08:02:10'),(232,3,17,'2020-04-30 08:02:11','2020-04-30 08:02:11'),(233,3,26,'2020-04-30 08:02:12','2020-04-30 08:02:12'),(234,3,27,'2020-04-30 08:02:13','2020-04-30 08:02:13'),(235,3,28,'2020-04-30 08:02:14','2020-04-30 08:02:14'),(236,3,29,'2020-04-30 08:02:15','2020-04-30 08:02:15'),(237,3,30,'2020-04-30 08:02:16','2020-04-30 08:02:16'),(238,3,31,'2020-04-30 08:02:17','2020-04-30 08:02:17'),(239,3,33,'2020-04-30 08:02:18','2020-04-30 08:02:18'),(240,3,34,'2020-04-30 08:02:21','2020-04-30 08:02:21'),(242,3,58,'2020-04-30 08:02:29','2020-04-30 08:02:29'),(243,3,59,'2020-04-30 08:02:29','2020-04-30 08:02:29'),(244,3,60,'2020-04-30 08:02:29','2020-04-30 08:02:29'),(245,3,67,'2020-04-30 08:02:30','2020-04-30 08:02:30'),(246,3,77,'2020-04-30 08:02:30','2020-04-30 08:02:30'),(247,3,79,'2020-04-30 08:02:30','2020-04-30 08:02:30'),(248,3,81,'2020-04-30 08:02:31','2020-04-30 08:02:31'),(249,3,65,'2020-04-30 08:02:33','2020-04-30 08:02:33'),(250,3,66,'2020-04-30 08:02:33','2020-04-30 08:02:33'),(251,3,80,'2020-04-30 08:02:34','2020-04-30 08:02:34'),(252,3,68,'2020-04-30 08:02:35','2020-04-30 08:02:35'),(253,3,78,'2020-04-30 08:02:38','2020-04-30 08:02:38'),(254,3,35,'2020-04-30 08:02:55','2020-04-30 08:02:55'),(257,3,39,'2020-04-30 08:02:56','2020-04-30 08:02:56'),(258,3,42,'2020-04-30 08:02:56','2020-04-30 08:02:56'),(260,3,87,'2020-04-30 08:02:58','2020-04-30 08:02:58'),(261,3,82,'2020-04-30 08:02:59','2020-04-30 08:02:59'),(262,3,90,'2020-04-30 08:03:00','2020-04-30 08:03:00'),(263,3,41,'2020-04-30 08:03:01','2020-04-30 08:03:01'),(264,3,40,'2020-04-30 08:03:01','2020-04-30 08:03:01'),(265,3,89,'2020-04-30 08:03:02','2020-04-30 08:03:02'),(266,3,86,'2020-04-30 08:03:02','2020-04-30 08:03:02'),(267,3,85,'2020-04-30 08:03:03','2020-04-30 08:03:03'),(268,3,43,'2020-04-30 08:03:09','2020-04-30 08:03:09'),(269,3,44,'2020-04-30 08:03:09','2020-04-30 08:03:09'),(270,3,45,'2020-04-30 08:03:10','2020-04-30 08:03:10'),(271,3,92,'2020-04-30 08:03:15','2020-04-30 08:03:15'),(272,3,91,'2020-04-30 08:03:15','2020-04-30 08:03:15'),(273,3,76,'2020-04-30 08:03:15','2020-04-30 08:03:15'),(274,3,50,'2020-04-30 08:03:16','2020-04-30 08:03:16'),(275,3,49,'2020-04-30 08:03:16','2020-04-30 08:03:16'),(276,3,47,'2020-04-30 08:03:17','2020-04-30 08:03:17'),(277,3,48,'2020-04-30 08:03:19','2020-04-30 08:03:19'),(278,3,57,'2020-04-30 08:03:24','2020-04-30 08:03:24'),(279,3,56,'2020-04-30 08:03:24','2020-04-30 08:03:24'),(280,3,55,'2020-04-30 08:03:24','2020-04-30 08:03:24'),(281,3,54,'2020-04-30 08:03:25','2020-04-30 08:03:25'),(282,3,53,'2020-04-30 08:03:25','2020-04-30 08:03:25'),(283,3,52,'2020-04-30 08:03:25','2020-04-30 08:03:25'),(284,3,51,'2020-04-30 08:03:26','2020-04-30 08:03:26'),(285,3,134,'2020-04-30 08:03:31','2020-04-30 08:03:31'),(286,3,64,'2020-04-30 08:03:32','2020-04-30 08:03:32'),(287,3,63,'2020-04-30 08:03:32','2020-04-30 08:03:32'),(288,3,62,'2020-04-30 08:03:32','2020-04-30 08:03:32'),(289,3,61,'2020-04-30 08:03:32','2020-04-30 08:03:32'),(290,3,133,'2020-04-30 08:03:39','2020-04-30 08:03:39'),(291,3,75,'2020-04-30 08:03:39','2020-04-30 08:03:39'),(292,3,74,'2020-04-30 08:03:40','2020-04-30 08:03:40'),(293,3,73,'2020-04-30 08:03:40','2020-04-30 08:03:40'),(294,3,72,'2020-04-30 08:03:40','2020-04-30 08:03:40'),(295,3,71,'2020-04-30 08:03:40','2020-04-30 08:03:40'),(296,3,70,'2020-04-30 08:03:41','2020-04-30 08:03:41'),(297,3,69,'2020-04-30 08:03:41','2020-04-30 08:03:41'),(298,3,102,'2020-04-30 08:03:47','2020-04-30 08:03:47'),(299,3,101,'2020-04-30 08:03:47','2020-04-30 08:03:47'),(300,3,100,'2020-04-30 08:03:47','2020-04-30 08:03:47'),(301,3,99,'2020-04-30 08:03:48','2020-04-30 08:03:48'),(302,3,98,'2020-04-30 08:03:49','2020-04-30 08:03:49'),(303,3,95,'2020-04-30 08:03:49','2020-04-30 08:03:49'),(304,3,93,'2020-04-30 08:03:50','2020-04-30 08:03:50'),(305,3,105,'2020-04-30 08:03:51','2020-04-30 08:03:51'),(306,3,104,'2020-04-30 08:03:51','2020-04-30 08:03:51'),(307,3,103,'2020-04-30 08:03:52','2020-04-30 08:03:52'),(308,3,97,'2020-04-30 08:03:52','2020-04-30 08:03:52'),(309,3,96,'2020-04-30 08:03:52','2020-04-30 08:03:52'),(310,3,94,'2020-04-30 08:03:53','2020-04-30 08:03:53'),(311,3,112,'2020-04-30 08:03:57','2020-04-30 08:03:57'),(312,3,108,'2020-04-30 08:03:58','2020-04-30 08:03:58'),(313,3,107,'2020-04-30 08:03:58','2020-04-30 08:03:58'),(314,3,106,'2020-04-30 08:03:58','2020-04-30 08:03:58'),(315,3,129,'2020-04-30 08:04:03','2020-04-30 08:04:03'),(316,3,125,'2020-04-30 08:04:05','2020-04-30 08:04:05'),(317,3,124,'2020-04-30 08:04:05','2020-04-30 08:04:05'),(318,3,115,'2020-04-30 08:04:05','2020-04-30 08:04:05'),(319,3,111,'2020-04-30 08:04:06','2020-04-30 08:04:06'),(320,3,110,'2020-04-30 08:04:06','2020-04-30 08:04:06'),(321,3,128,'2020-04-30 08:04:08','2020-04-30 08:04:08'),(322,3,127,'2020-04-30 08:04:08','2020-04-30 08:04:08'),(323,3,109,'2020-04-30 08:04:08','2020-04-30 08:04:08'),(324,3,130,'2020-04-30 08:04:13','2020-04-30 08:04:13'),(325,3,126,'2020-04-30 08:04:13','2020-04-30 08:04:13'),(326,3,123,'2020-04-30 08:04:14','2020-04-30 08:04:14'),(327,3,122,'2020-04-30 08:04:14','2020-04-30 08:04:14'),(328,3,121,'2020-04-30 08:04:14','2020-04-30 08:04:14'),(329,3,119,'2020-04-30 08:04:15','2020-04-30 08:04:15'),(330,3,118,'2020-04-30 08:04:15','2020-04-30 08:04:15'),(331,3,117,'2020-04-30 08:04:15','2020-04-30 08:04:15'),(332,3,132,'2020-04-30 08:04:17','2020-04-30 08:04:17'),(333,3,131,'2020-04-30 08:04:17','2020-04-30 08:04:17'),(334,3,116,'2020-04-30 08:04:18','2020-04-30 08:04:18'),(335,3,120,'2020-04-30 08:04:20','2020-04-30 08:04:20'),(336,3,144,'2020-04-30 08:04:25','2020-04-30 08:04:25'),(337,3,143,'2020-04-30 08:04:25','2020-04-30 08:04:25'),(338,3,142,'2020-04-30 08:04:25','2020-04-30 08:04:25'),(339,3,141,'2020-04-30 08:04:25','2020-04-30 08:04:25'),(340,3,140,'2020-04-30 08:04:26','2020-04-30 08:04:26'),(341,3,139,'2020-04-30 08:04:26','2020-04-30 08:04:26'),(342,3,138,'2020-04-30 08:04:26','2020-04-30 08:04:26'),(343,3,137,'2020-04-30 08:04:27','2020-04-30 08:04:27'),(344,3,136,'2020-04-30 08:04:27','2020-04-30 08:04:27'),(345,3,151,'2020-04-30 08:04:29','2020-04-30 08:04:29'),(346,3,150,'2020-04-30 08:04:29','2020-04-30 08:04:29'),(347,3,149,'2020-04-30 08:04:29','2020-04-30 08:04:29'),(348,3,148,'2020-04-30 08:04:30','2020-04-30 08:04:30'),(349,3,147,'2020-04-30 08:04:30','2020-04-30 08:04:30'),(350,3,146,'2020-04-30 08:04:30','2020-04-30 08:04:30'),(351,3,145,'2020-04-30 08:04:31','2020-04-30 08:04:31'),(352,3,135,'2020-04-30 08:04:31','2020-04-30 08:04:31'),(353,3,162,'2020-04-30 08:04:38','2020-04-30 08:04:38'),(354,3,161,'2020-04-30 08:04:38','2020-04-30 08:04:38'),(355,3,160,'2020-04-30 08:04:39','2020-04-30 08:04:39'),(356,3,159,'2020-04-30 08:04:39','2020-04-30 08:04:39'),(357,3,156,'2020-04-30 08:04:40','2020-04-30 08:04:40'),(358,3,154,'2020-04-30 08:04:40','2020-04-30 08:04:40'),(359,3,153,'2020-04-30 08:04:41','2020-04-30 08:04:41'),(360,3,152,'2020-04-30 08:04:41','2020-04-30 08:04:41'),(361,3,166,'2020-04-30 08:04:42','2020-04-30 08:04:42'),(364,3,164,'2020-04-30 08:04:43','2020-04-30 08:04:43'),(365,3,163,'2020-04-30 08:04:44','2020-04-30 08:04:44'),(366,3,158,'2020-04-30 08:04:45','2020-04-30 08:04:45'),(367,3,155,'2020-04-30 08:04:45','2020-04-30 08:04:45'),(368,4,11,'2020-04-30 08:05:26','2020-04-30 08:05:26'),(370,4,39,'2020-04-30 08:06:02','2020-04-30 08:06:02'),(371,4,43,'2020-04-30 08:06:17','2020-04-30 08:06:17'),(372,4,76,'2020-04-30 08:06:22','2020-04-30 08:06:22'),(373,4,51,'2020-04-30 08:06:28','2020-04-30 08:06:28'),(374,4,55,'2020-04-30 08:07:04','2020-04-30 08:07:04'),(375,4,56,'2020-04-30 08:07:16','2020-04-30 08:07:16'),(376,4,61,'2020-04-30 08:07:28','2020-04-30 08:07:28'),(377,4,134,'2020-04-30 08:07:40','2020-04-30 08:07:40'),(378,4,69,'2020-04-30 08:07:48','2020-04-30 08:07:48'),(379,4,73,'2020-04-30 08:07:51','2020-04-30 08:07:51'),(380,4,133,'2020-04-30 08:07:53','2020-04-30 08:07:53'),(381,4,109,'2020-04-30 08:08:21','2020-04-30 08:08:21'),(382,4,124,'2020-04-30 08:08:26','2020-04-30 08:08:26'),(383,4,125,'2020-04-30 08:08:28','2020-04-30 08:08:28'),(384,4,128,'2020-04-30 08:08:32','2020-04-30 08:08:32'),(385,4,163,'2020-04-30 08:08:47','2020-04-30 08:08:47'),(386,4,164,'2020-04-30 08:08:48','2020-04-30 08:08:48'),(387,5,163,'2020-04-30 08:08:57','2020-04-30 08:08:57'),(388,5,164,'2020-04-30 08:08:59','2020-04-30 08:08:59'),(389,4,167,'2020-05-14 02:29:09','2020-05-14 02:29:09'),(390,4,168,'2020-05-14 02:29:11','2020-05-14 02:29:11'),(391,4,169,'2020-05-14 02:29:12','2020-05-14 02:29:12'),(392,4,170,'2020-05-14 02:29:13','2020-05-14 02:29:13'),(393,4,171,'2020-05-14 02:29:15','2020-05-14 02:29:15'),(394,4,172,'2020-05-14 02:29:20','2020-05-14 02:29:20'),(395,4,173,'2020-05-14 02:29:21','2020-05-14 02:29:21'),(396,4,174,'2020-05-14 02:29:22','2020-05-14 02:29:22'),(397,4,175,'2020-05-14 02:29:23','2020-05-14 02:29:23'),(398,4,176,'2020-05-14 02:29:24','2020-05-14 02:29:24'),(399,4,177,'2020-05-14 02:29:26','2020-05-14 02:29:26'),(400,4,178,'2020-05-14 02:29:27','2020-05-14 02:29:27'),(401,4,179,'2020-05-14 02:29:28','2020-05-14 02:29:28'),(402,4,180,'2020-05-14 02:29:30','2020-05-14 02:29:30'),(403,5,167,'2020-05-14 02:29:34','2020-05-14 02:29:34'),(404,5,169,'2020-05-14 02:29:36','2020-05-14 02:29:36'),(405,5,178,'2020-05-14 02:29:37','2020-05-14 02:29:37'),(406,5,177,'2020-05-14 02:29:37','2020-05-14 02:29:37'),(407,5,176,'2020-05-14 02:29:38','2020-05-14 02:29:38'),(408,5,175,'2020-05-14 02:29:38','2020-05-14 02:29:38'),(409,5,174,'2020-05-14 02:29:39','2020-05-14 02:29:39'),(410,5,173,'2020-05-14 02:29:39','2020-05-14 02:29:39'),(411,5,172,'2020-05-14 02:29:40','2020-05-14 02:29:40'),(412,5,171,'2020-05-14 02:29:40','2020-05-14 02:29:40'),(413,5,170,'2020-05-14 02:29:40','2020-05-14 02:29:40'),(414,5,180,'2020-05-14 02:29:42','2020-05-14 02:29:42'),(415,5,179,'2020-05-14 02:29:43','2020-05-14 02:29:43'),(416,5,168,'2020-05-14 02:29:44','2020-05-14 02:29:44'),(417,5,183,'2020-05-14 02:30:01','2020-05-14 02:30:01'),(418,5,184,'2020-05-14 02:30:03','2020-05-14 02:30:03'),(419,3,185,'2020-05-17 07:31:17','2020-05-17 07:31:17'),(420,3,186,'2020-05-17 07:31:19','2020-05-17 07:31:19'),(421,3,88,'2020-05-17 07:31:28','2020-05-17 07:31:28'),(422,4,185,'2020-05-17 07:31:37','2020-05-17 07:31:37'),(423,4,186,'2020-05-17 07:31:41','2020-05-17 07:31:41'),(424,4,88,'2020-05-17 07:31:51','2020-05-17 07:31:51'),(425,5,186,'2020-05-17 07:31:59','2020-05-17 07:31:59'),(426,5,185,'2020-05-17 07:32:01','2020-05-17 07:32:01'),(427,2,186,'2020-05-17 07:32:07','2020-05-17 07:32:07'),(428,2,185,'2020-05-17 07:32:09','2020-05-17 07:32:09'),(429,4,183,'2020-05-18 06:20:51','2020-05-18 06:20:51'),(430,4,184,'2020-05-18 06:20:52','2020-05-18 06:20:52'),(431,4,187,'2020-05-18 06:20:54','2020-05-18 06:20:54'),(432,5,187,'2020-05-18 06:21:00','2020-05-18 06:21:00'),(433,5,166,'2020-05-19 06:45:37','2020-05-19 06:45:37'),(434,5,190,'2020-05-19 06:45:40','2020-05-19 06:45:40'),(435,5,181,'2020-05-19 06:45:42','2020-05-19 06:45:42'),(436,5,154,'2020-05-19 06:45:54','2020-05-19 06:45:54'),(437,5,152,'2020-05-19 06:45:57','2020-05-19 06:45:57'),(438,4,190,'2020-05-19 06:46:04','2020-05-19 06:46:04'),(439,4,181,'2020-05-19 06:46:06','2020-05-19 06:46:06'),(440,4,166,'2020-05-19 06:46:08','2020-05-19 06:46:08'),(441,4,152,'2020-05-19 06:46:20','2020-05-19 06:46:20'),(442,4,154,'2020-05-19 06:46:23','2020-05-19 06:46:23'),(443,3,190,'2020-05-19 06:46:46','2020-05-19 06:46:46'),(444,3,181,'2020-05-19 06:46:49','2020-05-19 06:46:49'),(445,5,59,'2020-05-19 07:03:29','2020-05-19 07:03:29'),(446,2,190,'2020-05-19 08:12:34','2020-05-19 08:12:34'),(447,4,68,'2020-05-20 10:06:21','2020-05-20 10:06:21'),(448,4,198,'2020-05-25 07:02:54','2020-05-25 07:02:54'),(449,4,201,'2020-05-26 05:24:23','2020-05-26 05:24:23'),(450,2,201,'2020-05-26 05:24:30','2020-05-26 05:24:30'),(451,3,201,'2020-05-26 05:24:38','2020-05-26 05:24:38'),(452,2,199,'2020-05-28 05:32:18','2020-05-28 05:32:18'),(453,2,200,'2020-05-28 05:32:19','2020-05-28 05:32:19'),(454,4,191,'2020-05-29 08:18:08','2020-05-29 08:18:08'),(455,5,191,'2020-05-29 08:19:05','2020-05-29 08:19:05'),(456,5,208,'2020-06-04 09:42:09','2020-06-04 09:42:09'),(457,4,208,'2020-06-04 09:42:14','2020-06-04 09:42:14'),(458,3,208,'2020-06-04 09:42:19','2020-06-04 09:42:19'),(459,2,208,'2020-06-04 09:42:25','2020-06-04 09:42:25'),(460,3,211,'2020-06-05 07:45:53','2020-06-05 07:45:53'),(461,2,135,'2020-06-05 07:46:00','2020-06-05 07:46:00'),(462,2,136,'2020-06-05 07:46:02','2020-06-05 07:46:02'),(463,2,137,'2020-06-05 07:46:03','2020-06-05 07:46:03'),(464,2,138,'2020-06-05 07:46:04','2020-06-05 07:46:04'),(465,2,139,'2020-06-05 07:46:05','2020-06-05 07:46:05'),(466,2,140,'2020-06-05 07:46:06','2020-06-05 07:46:06'),(467,2,141,'2020-06-05 07:46:08','2020-06-05 07:46:08'),(468,2,142,'2020-06-05 07:46:09','2020-06-05 07:46:09'),(469,2,143,'2020-06-05 07:46:10','2020-06-05 07:46:10'),(470,2,144,'2020-06-05 07:46:11','2020-06-05 07:46:11'),(471,2,145,'2020-06-05 07:46:12','2020-06-05 07:46:12'),(472,2,146,'2020-06-05 07:46:13','2020-06-05 07:46:13'),(473,2,147,'2020-06-05 07:46:14','2020-06-05 07:46:14'),(474,2,148,'2020-06-05 07:46:15','2020-06-05 07:46:15'),(475,2,149,'2020-06-05 07:46:16','2020-06-05 07:46:16'),(476,2,150,'2020-06-05 07:46:17','2020-06-05 07:46:17'),(477,2,151,'2020-06-05 07:46:18','2020-06-05 07:46:18'),(478,2,211,'2020-06-05 07:46:19','2020-06-05 07:46:19'),(479,3,212,'2020-06-06 06:56:15','2020-06-06 06:56:15'),(480,3,213,'2020-06-06 06:56:29','2020-06-06 06:56:29'),(481,3,214,'2020-06-06 06:56:30','2020-06-06 06:56:30'),(482,4,89,'2020-06-06 06:56:56','2020-06-06 06:56:56'),(483,4,212,'2020-06-06 06:56:58','2020-06-06 06:56:58'),(484,3,223,'2020-06-12 02:30:27','2020-06-12 02:30:27'),(485,4,223,'2020-06-12 02:30:31','2020-06-12 02:30:31'),(486,5,223,'2020-06-12 02:30:37','2020-06-12 02:30:37'),(487,4,218,'2020-06-12 08:29:25','2020-06-12 08:29:25'),(488,4,219,'2020-06-12 08:29:41','2020-06-12 08:29:41'),(489,4,224,'2020-06-12 08:30:17','2020-06-12 08:30:17'),(490,5,224,'2020-06-12 08:30:24','2020-06-12 08:30:24'),(491,3,176,'2020-06-12 08:30:32','2020-06-12 08:30:32'),(492,3,177,'2020-06-12 08:30:43','2020-06-12 08:30:43'),(493,3,175,'2020-06-12 08:30:45','2020-06-12 08:30:45'),(494,3,178,'2020-06-12 08:30:47','2020-06-12 08:30:47'),(495,3,179,'2020-06-12 08:30:50','2020-06-12 08:30:50'),(496,3,167,'2020-06-12 08:30:54','2020-06-12 08:30:54'),(497,3,168,'2020-06-12 08:30:56','2020-06-12 08:30:56'),(498,3,169,'2020-06-12 08:30:58','2020-06-12 08:30:58'),(499,3,170,'2020-06-12 08:30:59','2020-06-12 08:30:59'),(500,3,171,'2020-06-12 08:31:00','2020-06-12 08:31:00'),(501,3,172,'2020-06-12 08:31:01','2020-06-12 08:31:01'),(502,3,173,'2020-06-12 08:31:02','2020-06-12 08:31:02'),(503,3,191,'2020-06-12 08:31:05','2020-06-12 08:31:05'),(504,3,174,'2020-06-12 08:31:06','2020-06-12 08:31:06'),(505,3,224,'2020-06-12 08:31:08','2020-06-12 08:31:08'),(506,3,193,'2020-06-12 08:31:09','2020-06-12 08:31:09'),(507,3,180,'2020-06-12 08:31:10','2020-06-12 08:31:10'),(508,13,225,'2020-06-15 09:46:51','2020-06-15 09:46:51'),(509,13,226,'2020-06-15 09:46:52','2020-06-15 09:46:52'),(510,13,227,'2020-06-15 09:46:52','2020-06-15 09:46:52'),(511,13,228,'2020-06-15 09:46:53','2020-06-15 09:46:53'),(512,13,229,'2020-06-15 09:46:54','2020-06-15 09:46:54'),(513,13,230,'2020-06-15 09:46:55','2020-06-15 09:46:55'),(514,13,231,'2020-06-15 09:46:56','2020-06-15 09:46:56'),(515,13,232,'2020-06-15 09:46:57','2020-06-15 09:46:57'),(516,13,233,'2020-06-15 09:46:58','2020-06-15 09:46:58'),(517,13,234,'2020-06-15 09:46:59','2020-06-15 09:46:59'),(518,13,235,'2020-06-15 09:47:00','2020-06-15 09:47:00'),(519,2,225,'2020-06-15 09:47:11','2020-06-15 09:47:11'),(520,2,226,'2020-06-15 09:47:12','2020-06-15 09:47:12'),(521,2,228,'2020-06-15 09:47:13','2020-06-15 09:47:13'),(522,2,229,'2020-06-15 09:47:13','2020-06-15 09:47:13'),(523,2,235,'2020-06-15 09:47:15','2020-06-15 09:47:15'),(524,2,234,'2020-06-15 09:47:15','2020-06-15 09:47:15'),(525,2,233,'2020-06-15 09:47:16','2020-06-15 09:47:16'),(526,2,232,'2020-06-15 09:47:16','2020-06-15 09:47:16'),(527,2,231,'2020-06-15 09:47:16','2020-06-15 09:47:16'),(528,2,230,'2020-06-15 09:47:17','2020-06-15 09:47:17'),(529,2,227,'2020-06-15 09:47:17','2020-06-15 09:47:17'),(530,3,225,'2020-06-15 09:47:26','2020-06-15 09:47:26'),(531,3,227,'2020-06-15 09:47:27','2020-06-15 09:47:27'),(532,3,228,'2020-06-15 09:47:28','2020-06-15 09:47:28'),(533,3,230,'2020-06-15 09:47:28','2020-06-15 09:47:28'),(534,3,232,'2020-06-15 09:47:28','2020-06-15 09:47:28'),(535,3,234,'2020-06-15 09:47:29','2020-06-15 09:47:29'),(536,3,235,'2020-06-15 09:47:30','2020-06-15 09:47:30'),(537,3,226,'2020-06-15 09:47:31','2020-06-15 09:47:31'),(538,3,229,'2020-06-15 09:47:32','2020-06-15 09:47:32'),(539,3,231,'2020-06-15 09:47:32','2020-06-15 09:47:32'),(540,3,233,'2020-06-15 09:47:33','2020-06-15 09:47:33'),(542,14,77,'2020-06-19 10:09:17','2020-06-19 10:09:17'),(543,14,197,'2020-06-20 07:07:33','2020-06-20 07:07:33'),(544,14,215,'2020-06-20 07:07:35','2020-06-20 07:07:35'),(546,4,244,'2020-07-05 12:55:20','2020-07-05 12:55:20'),(547,4,245,'2020-07-05 12:55:23','2020-07-05 12:55:23'),(548,4,249,'2020-07-05 12:55:44','2020-07-05 12:55:44'),(549,4,250,'2020-07-05 12:55:51','2020-07-05 12:55:51'),(550,4,254,'2020-07-05 12:56:01','2020-07-05 12:56:01'),(551,4,255,'2020-07-05 12:56:03','2020-07-05 12:56:03'),(552,4,259,'2020-07-05 12:56:07','2020-07-05 12:56:07'),(553,4,263,'2020-07-05 12:56:11','2020-07-05 12:56:11'),(554,4,266,'2020-07-05 12:56:16','2020-07-05 12:56:16'),(555,4,267,'2020-07-05 12:56:19','2020-07-05 12:56:19'),(556,4,268,'2020-07-05 12:56:23','2020-07-05 12:56:23'),(557,3,240,'2020-07-05 12:56:46','2020-07-05 12:56:46'),(558,3,241,'2020-07-05 12:56:48','2020-07-05 12:56:48'),(559,3,242,'2020-07-05 12:56:49','2020-07-05 12:56:49'),(562,3,243,'2020-07-05 12:56:50','2020-07-05 12:56:50'),(564,3,253,'2020-07-05 12:56:52','2020-07-05 12:56:52'),(565,3,252,'2020-07-05 12:56:53','2020-07-05 12:56:53'),(566,3,251,'2020-07-05 12:56:53','2020-07-05 12:56:53'),(567,3,250,'2020-07-05 12:56:54','2020-07-05 12:56:54'),(568,3,249,'2020-07-05 12:56:55','2020-07-05 12:56:55'),(569,3,248,'2020-07-05 12:56:55','2020-07-05 12:56:55'),(570,3,247,'2020-07-05 12:56:56','2020-07-05 12:56:56'),(571,3,246,'2020-07-05 12:56:56','2020-07-05 12:56:56'),(572,3,245,'2020-07-05 12:56:57','2020-07-05 12:56:57'),(573,3,244,'2020-07-05 12:56:57','2020-07-05 12:56:57'),(574,3,263,'2020-07-05 12:57:00','2020-07-05 12:57:00'),(575,3,262,'2020-07-05 12:57:01','2020-07-05 12:57:01'),(576,3,261,'2020-07-05 12:57:02','2020-07-05 12:57:02'),(577,3,260,'2020-07-05 12:57:02','2020-07-05 12:57:02'),(578,3,259,'2020-07-05 12:57:03','2020-07-05 12:57:03'),(579,3,258,'2020-07-05 12:57:03','2020-07-05 12:57:03'),(580,3,257,'2020-07-05 12:57:04','2020-07-05 12:57:04'),(581,3,256,'2020-07-05 12:57:04','2020-07-05 12:57:04'),(582,3,255,'2020-07-05 12:57:05','2020-07-05 12:57:05'),(583,3,268,'2020-07-05 12:57:06','2020-07-05 12:57:06'),(584,3,267,'2020-07-05 12:57:07','2020-07-05 12:57:07'),(585,3,266,'2020-07-05 12:57:08','2020-07-05 12:57:08'),(586,3,265,'2020-07-05 12:57:08','2020-07-05 12:57:08'),(587,3,264,'2020-07-05 12:57:09','2020-07-05 12:57:09'),(588,3,254,'2020-07-05 12:57:09','2020-07-05 12:57:09'),(589,2,268,'2020-07-05 12:57:16','2020-07-05 12:57:16'),(590,2,267,'2020-07-05 12:57:17','2020-07-05 12:57:17'),(591,2,266,'2020-07-05 12:57:18','2020-07-05 12:57:18'),(592,2,265,'2020-07-05 12:57:18','2020-07-05 12:57:18'),(593,2,264,'2020-07-05 12:57:19','2020-07-05 12:57:19'),(594,2,263,'2020-07-05 12:57:19','2020-07-05 12:57:19'),(595,2,262,'2020-07-05 12:57:20','2020-07-05 12:57:20'),(596,2,261,'2020-07-05 12:57:21','2020-07-05 12:57:21'),(597,2,260,'2020-07-05 12:57:23','2020-07-05 12:57:23'),(598,2,259,'2020-07-05 12:57:25','2020-07-05 12:57:25'),(599,2,258,'2020-07-05 12:57:26','2020-07-05 12:57:26'),(600,2,257,'2020-07-05 12:57:27','2020-07-05 12:57:27'),(601,2,256,'2020-07-05 12:57:27','2020-07-05 12:57:27'),(602,2,255,'2020-07-05 12:57:27','2020-07-05 12:57:27'),(603,2,254,'2020-07-05 12:57:28','2020-07-05 12:57:28'),(604,2,253,'2020-07-05 12:57:28','2020-07-05 12:57:28'),(605,2,252,'2020-07-05 12:57:29','2020-07-05 12:57:29'),(606,2,251,'2020-07-05 12:57:30','2020-07-05 12:57:30'),(607,2,250,'2020-07-05 12:57:32','2020-07-05 12:57:32'),(608,2,249,'2020-07-05 12:57:34','2020-07-05 12:57:34'),(609,2,248,'2020-07-05 12:57:36','2020-07-05 12:57:36'),(610,2,247,'2020-07-05 12:57:37','2020-07-05 12:57:37'),(611,2,246,'2020-07-05 12:57:38','2020-07-05 12:57:38'),(612,2,245,'2020-07-05 12:57:40','2020-07-05 12:57:40'),(613,2,244,'2020-07-05 12:57:41','2020-07-05 12:57:41'),(614,2,243,'2020-07-05 12:57:42','2020-07-05 12:57:42'),(615,2,242,'2020-07-05 12:57:43','2020-07-05 12:57:43'),(616,2,241,'2020-07-05 12:57:45','2020-07-05 12:57:45'),(617,2,240,'2020-07-05 12:57:46','2020-07-05 12:57:46'),(618,3,269,'2020-07-07 09:55:50','2020-07-07 09:55:50'),(619,3,270,'2020-07-07 09:55:51','2020-07-07 09:55:51'),(620,3,271,'2020-07-07 09:55:52','2020-07-07 09:55:52'),(621,3,272,'2020-07-07 09:55:54','2020-07-07 09:55:54'),(622,15,135,'2020-07-09 06:40:11','2020-07-09 06:40:11'),(623,15,136,'2020-07-09 06:40:13','2020-07-09 06:40:13'),(624,15,138,'2020-07-09 06:40:14','2020-07-09 06:40:14'),(625,15,139,'2020-07-09 06:40:16','2020-07-09 06:40:16'),(626,15,140,'2020-07-09 06:40:17','2020-07-09 06:40:17'),(627,15,141,'2020-07-09 06:40:18','2020-07-09 06:40:18'),(628,15,142,'2020-07-09 06:40:20','2020-07-09 06:40:20'),(630,15,143,'2020-07-09 06:40:26','2020-07-09 06:40:26'),(631,15,144,'2020-07-09 06:40:27','2020-07-09 06:40:27'),(632,15,145,'2020-07-09 06:40:28','2020-07-09 06:40:28'),(633,15,146,'2020-07-09 06:40:29','2020-07-09 06:40:29'),(635,15,147,'2020-07-09 06:40:30','2020-07-09 06:40:30'),(636,15,148,'2020-07-09 06:40:31','2020-07-09 06:40:31'),(637,15,149,'2020-07-09 06:40:32','2020-07-09 06:40:32'),(639,15,150,'2020-07-09 06:40:34','2020-07-09 06:40:34'),(640,15,151,'2020-07-09 06:40:35','2020-07-09 06:40:35'),(641,15,211,'2020-07-09 06:40:35','2020-07-09 06:40:35');
/*!40000 ALTER TABLE `oc_auth_role_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_auth_user_role`
--

DROP TABLE IF EXISTS `oc_auth_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_auth_user_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户登录名',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_role_unique` (`username`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_auth_user_role`
--

LOCK TABLES `oc_auth_user_role` WRITE;
/*!40000 ALTER TABLE `oc_auth_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_auth_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_business_tag`
--

DROP TABLE IF EXISTS `oc_business_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_business_tag` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `business_id` int(11) NOT NULL COMMENT '业务id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  `business_type` int(2) DEFAULT NULL COMMENT '业务类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `business_id` (`business_id`,`tag_id`,`business_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_business_tag`
--

LOCK TABLES `oc_business_tag` WRITE;
/*!40000 ALTER TABLE `oc_business_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_business_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_db`
--

DROP TABLE IF EXISTS `oc_cloud_db`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_db` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '云账户uid',
  `account_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '云账户名称',
  `cloud_db_type` int(2) NOT NULL COMMENT '云数据库类型',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `region_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地域id',
  `db_instance_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '实例id',
  `db_instance_description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例描述系统获取',
  `db_instance_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '实例类型',
  `engine` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据库类型',
  `engine_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '数据库版本',
  `zone` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '可用区id',
  `pay_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '实例的付费类型',
  `db_instance_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例状态',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '到期时间',
  `instance_network_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例的网络类型',
  `connection_mode` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例的访问模式',
  `db_instance_net_type` varchar(16) DEFAULT NULL COMMENT '实例的网络连接类型',
  `db_Instance_storage_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '实例储存类型',
  `db_Instance_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实例规格',
  `created_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `category` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '实例系列',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cloud_db_type` (`cloud_db_type`,`db_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_db`
--

LOCK TABLES `oc_cloud_db` WRITE;
/*!40000 ALTER TABLE `oc_cloud_db` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_db` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_db_account`
--

DROP TABLE IF EXISTS `oc_cloud_db_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_db_account` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cloud_db_id` int(11) NOT NULL COMMENT '云数据库id',
  `db_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '云数据库实例id',
  `account_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '账户名称',
  `account_password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
  `account_privilege` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '账号权限',
  `account_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '账号类型，取值：\\n\\nNormal：普通账号\\nSuper：高权限账号\\nSysadmin：具备超级权限（SA）的账号（仅SQL Server支持）\\n默认值：Normal。',
  `workflow` int(2) NOT NULL DEFAULT '1' COMMENT '允许工作流申请',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cloud_db_id` (`cloud_db_id`,`account_name`,`account_privilege`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云数据库实例中的授权账户（仅oc授权账户）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_db_account`
--

LOCK TABLES `oc_cloud_db_account` WRITE;
/*!40000 ALTER TABLE `oc_cloud_db_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_db_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_db_attribute`
--

DROP TABLE IF EXISTS `oc_cloud_db_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_db_attribute` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cloud_db_id` int(11) NOT NULL COMMENT '云数据库id',
  `db_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '云数据库实例id',
  `attribute_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '属性名称',
  `attribute_value` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '属性值',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cloud_db_id` (`cloud_db_id`,`attribute_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_db_attribute`
--

LOCK TABLES `oc_cloud_db_attribute` WRITE;
/*!40000 ALTER TABLE `oc_cloud_db_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_db_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_db_database`
--

DROP TABLE IF EXISTS `oc_cloud_db_database`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_db_database` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cloud_db_id` int(11) NOT NULL COMMENT '云数据库id',
  `db_instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '云数据库实例id',
  `db_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据库名称',
  `engine` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '数据库实例类型',
  `db_description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `db_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '数据库状态，取值：\n\nCreating：创建中\nRunning：使用中\nDeleting：删除中',
  `character_set_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字符集',
  `env_type` int(2) NOT NULL COMMENT '环境类型',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cloud_db_id` (`cloud_db_id`,`db_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云数据库实例中的数据库';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_db_database`
--

LOCK TABLES `oc_cloud_db_database` WRITE;
/*!40000 ALTER TABLE `oc_cloud_db_database` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_db_database` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_image`
--

DROP TABLE IF EXISTS `oc_cloud_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_image` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `account_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `region_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `image_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '镜像id',
  `image_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '镜像名称',
  `image_size` int(11) DEFAULT NULL COMMENT '镜像容量',
  `cloud_type` int(2) DEFAULT NULL,
  `image_detail` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'image序列化对象',
  `image_owner_alias` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '镜像来源',
  `is_supportIo_optimized` int(1) DEFAULT NULL,
  `is_support_cloudinit` int(1) DEFAULT NULL,
  `os_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `os_name_en` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `architecture` varchar(64) DEFAULT NULL,
  `image_status` varchar(32) DEFAULT NULL,
  `creation_time` timestamp NULL DEFAULT NULL COMMENT '镜像创建时间',
  `os_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `platform` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '平台',
  `is_active` int(1) DEFAULT NULL COMMENT '有效',
  `is_deleted` int(1) DEFAULT NULL COMMENT '镜像被删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`,`region_id`,`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云镜像';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_image`
--

LOCK TABLES `oc_cloud_image` WRITE;
/*!40000 ALTER TABLE `oc_cloud_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_instance_task`
--

DROP TABLE IF EXISTS `oc_cloud_instance_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_instance_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cloud_type` int(2) NOT NULL,
  `template_id` int(11) NOT NULL COMMENT '模版id',
  `user_id` int(11) DEFAULT NULL,
  `user_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_instance` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务详情',
  `region_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `create_size` int(4) NOT NULL COMMENT '创建数量',
  `task_phase` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `task_status` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '任务状态',
  `error_msg` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `task_type` int(2) DEFAULT NULL COMMENT '任务类型',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云实例任务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_instance_task`
--

LOCK TABLES `oc_cloud_instance_task` WRITE;
/*!40000 ALTER TABLE `oc_cloud_instance_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_instance_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_instance_task_member`
--

DROP TABLE IF EXISTS `oc_cloud_instance_task_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_instance_task_member` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL,
  `instance_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '实例id',
  `seq` int(2) DEFAULT NULL,
  `private_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `public_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `region_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `zone_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `hostname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '详情',
  `task_phase` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `task_status` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '任务状态',
  `retry_count` int(3) DEFAULT NULL,
  `error_code` int(11) DEFAULT NULL,
  `error_msg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云实例任务成员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_instance_task_member`
--

LOCK TABLES `oc_cloud_instance_task_member` WRITE;
/*!40000 ALTER TABLE `oc_cloud_instance_task_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_instance_task_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_instance_template`
--

DROP TABLE IF EXISTS `oc_cloud_instance_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_instance_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cloud_type` int(2) NOT NULL,
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `template_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `region_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `image_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `vpc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `vpc_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `security_group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `security_group_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `io_optimized` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `usage_count` int(1) DEFAULT '0' COMMENT '使用次数',
  `template_yaml` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cloud_type` (`cloud_type`,`template_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云实例模版';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_instance_template`
--

LOCK TABLES `oc_cloud_instance_template` WRITE;
/*!40000 ALTER TABLE `oc_cloud_instance_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_instance_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_instance_type`
--

DROP TABLE IF EXISTS `oc_cloud_instance_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_instance_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cloud_type` int(2) NOT NULL,
  `instance_type_family` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `instance_type_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `region_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `cpu_core_count` int(4) NOT NULL,
  `memory_size` float DEFAULT NULL,
  `instance_family_level` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cloud_type` (`cloud_type`,`instance_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云实例模版';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_instance_type`
--

LOCK TABLES `oc_cloud_instance_type` WRITE;
/*!40000 ALTER TABLE `oc_cloud_instance_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_instance_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_server`
--

DROP TABLE IF EXISTS `oc_cloud_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_server` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `server_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `instance_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `instance_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `region_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `zone` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `cloud_server_type` int(2) DEFAULT NULL,
  `instance_detail` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'instance序列化对象',
  `private_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '私有IP',
  `public_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '公网IP',
  `cpu` int(11) DEFAULT NULL,
  `memory` int(11) DEFAULT NULL,
  `vpc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `instance_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `image_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `system_disk_size` int(11) DEFAULT NULL,
  `data_disk_size` int(11) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `expired_time` timestamp NULL DEFAULT NULL,
  `charge_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `server_status` int(11) DEFAULT NULL,
  `renewal_status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '续费设置',
  `server_id` int(11) DEFAULT NULL,
  `power_mgmt` tinyint(1) DEFAULT '0' COMMENT '允许电源管理',
  `power_status` int(2) DEFAULT NULL COMMENT '电源状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `instanceId` (`instance_id`),
  UNIQUE KEY `cloudServerType` (`cloud_server_type`,`private_ip`),
  KEY `cloudServerType_2` (`cloud_server_type`),
  KEY `serverId` (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_server`
--

LOCK TABLES `oc_cloud_server` WRITE;
/*!40000 ALTER TABLE `oc_cloud_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_vpc`
--

DROP TABLE IF EXISTS `oc_cloud_vpc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_vpc` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `account_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `region_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `vpc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'vpc id',
  `vpc_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'vpc名称',
  `cidr_block` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Classless Inter-Domain Routing网段',
  `cloud_type` int(2) DEFAULT NULL,
  `description` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '说明',
  `creation_time` timestamp NULL DEFAULT NULL,
  `is_active` int(1) DEFAULT NULL COMMENT '有效',
  `is_deleted` int(1) DEFAULT NULL COMMENT '镜像被删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`,`region_id`,`vpc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云VPC';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_vpc`
--

LOCK TABLES `oc_cloud_vpc` WRITE;
/*!40000 ALTER TABLE `oc_cloud_vpc` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_vpc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_vpc_security_group`
--

DROP TABLE IF EXISTS `oc_cloud_vpc_security_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_vpc_security_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `region_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `vpc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'vpc id',
  `security_group_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `security_group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `security_group_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `cloud_type` int(2) DEFAULT NULL,
  `description` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '说明',
  `creation_time` timestamp NULL DEFAULT NULL,
  `is_active` int(1) DEFAULT NULL COMMENT '有效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vpc_id` (`vpc_id`,`security_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云交换机';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_vpc_security_group`
--

LOCK TABLES `oc_cloud_vpc_security_group` WRITE;
/*!40000 ALTER TABLE `oc_cloud_vpc_security_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_vpc_security_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_cloud_vpc_vswitch`
--

DROP TABLE IF EXISTS `oc_cloud_vpc_vswitch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_cloud_vpc_vswitch` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `region_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `zone_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `vpc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'vpc id',
  `vswitch_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `vswitch_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'vswitch',
  `vswitch_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `cidr_block` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Classless Inter-Domain Routing网段',
  `cloud_type` int(2) DEFAULT NULL,
  `description` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '说明',
  `creation_time` timestamp NULL DEFAULT NULL,
  `available_ip_address_count` int(11) DEFAULT NULL,
  `is_active` int(1) DEFAULT NULL COMMENT '有效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vpc_id` (`vpc_id`,`vswitch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云交换机';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_cloud_vpc_vswitch`
--

LOCK TABLES `oc_cloud_vpc_vswitch` WRITE;
/*!40000 ALTER TABLE `oc_cloud_vpc_vswitch` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_cloud_vpc_vswitch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_document`
--

DROP TABLE IF EXISTS `oc_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_document` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `doc_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `doc_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '文档内容',
  `doc_type` int(2) NOT NULL DEFAULT '1' COMMENT '文档类型',
  `comment` varchar(256) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `doc_key` (`doc_key`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_document`
--

LOCK TABLES `oc_document` WRITE;
/*!40000 ALTER TABLE `oc_document` DISABLE KEYS */;
INSERT INTO `oc_document` VALUES (1,'JUMP_README','# 公告板\n![](https://img.shields.io/badge/opscloud-jump-brightgreen.svg?style=plastic&logo=iCloud)\n\n##### 保存公钥\n\n- 在`个人详情-我的详情-SSH密钥`中添加公钥\n```\n# 查看公钥\ncat ~/.ssh/id_rsa.pub\n```\n\n##### 权限申请\n   \n- `工作台`-`工单`-`服务器组权限申请`\n\n\n##### 登录JUMP\n     \n- mac命令行中输入\n```\n# -C 压缩传输\n# -o StrictHostKeyChecking=no 公钥免检\nssh ${username}@${cocoHost}\n```\n- 在家访问请先连接VPN\n\n##### OC-Web-XTerm\n<video width=\"100%\" controls Autoplay=autoplay>\n<source src=\"https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/help/opscloud/web-xterm/oc-web-xterm.mov\" type=\"video/mp4\" align=center>\n</video>',1,NULL,'2020-05-12 09:26:57','2020-05-23 05:23:14'),(2,'WORDPAD','# 欢迎使用OC-Web终端\n![](https://img.shields.io/badge/opscloud-xterm-brightgreen.svg?style=plastic&logo=iCloud)\n\n<b style=\"color:red\">!!!此文档是用户私有文档，可随意编辑!!!</b>\n\n#### 常用命令\n\n##### 应用服务管理\n```bash\n# 切换到app用户\ngegejia$ sudo su app\napp$ sh /opt/bin/appctl.sh start # 启动应用\napp$ sh /opt/bin/appctl.sh stop # 停止应用\napp$ sh /opt/bin/appctl.sh dump # dump java进程，完成后需重启\napp$ sh /opt/bin/appctl.sh forcestop # 强制停止应用\napp$ sh /opt/bin/appctl.sh # 查看进程状态\n```\n\n##### 高权限账户登录后切换到root\n```bash\n# 切换root\nadmin$ sudo su - # 或直接输入封装命令 s\n```\n\n##### 查看日志\n容器环境(测试、预发为docker)，除了/logs下的日志外，还可以通过docker ps -a,加上grep可以快速定位到自己的容器\n```\n[root@k8s-test-work-10 ~]# docker ps -a |grep user-center-service-daily\n834f76b63832        registry-vpc.cn-hangzhou.aliyuncs.com/gegejia/user-center-service             \"/bin/sh -c \'java ...\"   2 weeks ago         Up 2 weeks                                    k8s_user-center-service-daily_user-center-service-daily-deployment-6b87f97f5d-jngjj_test_b747fcfb-bd01-4b53-a785-ed1b7bcd7d2b_0\nc5cd08daaddc        registry-vpc.cn-hangzhou.aliyuncs.com/acs/pause-amd64:3.0                     \"/pause\"                 2 weeks ago         Up 2 weeks                                    k8s_POD_user-center-service-daily-deployment-6b87f97f5d-jngjj_test_b747fcfb-bd01-4b53-a785-ed1b7bcd7d2b_0\n```\n要看空间为gegejia的容器，然后通过docker logs -f --tail 100 ${containerId}来查看具体的容器日志\n```\ndocker logs -f --tail 100 834f76b63832\n```\n生产为实体机,除了/logs下的日志外，还可以在/opt/opt/outlog 下的 out.log 来定位问题\n\n##### Docket命令指南\n + <a style=\"color:#2b669a\" href=\"https://www.runoob.com/docker/docker-run-command.html\" target=\"_blank\"><b>传送门</b></a>',1,NULL,'2020-05-13 07:04:04','2020-05-17 08:49:21'),(3,'USER_GROUP_README','#### 账户\n+ 已完成企业内部统一权限认证，所有平台账户互通\n\n#### Confluence 文档中心\n+ 登录地址:<http://wiki.ops.yangege.cn>\n+ 角色\n  + `confluence-users`普通用户\n  + `confluence-administrators`管理员（未开放授权）\n \n#### Nexus 构件仓库\n+ 登录地址:<http://nexus.ops.yangege.cn>\n+ 角色\n  + `nexus-users`普通用户,可以下载依赖包\n  + `nexus-developer`开发者,可以deploy\n  + `nexus-admin`管理员（未开放授权）\n\n#### SonarQube 代码质量管理\n+ 登录地址:<http://sonar.ops.yangege.cn>\n+ 角色\n  + `sonar-users`普通用户\n  + `sonar-administrators`管理员（未开放授权）\n\n#### Jira 项目(问题)管理\n+ 登录地址:<http://jira.ops.yangege.cn>\n+ 角色\n  + `jira-software-users`普通用户,可参与项目\n  + `jira-projectmanager`项目管理员,可创建项目\n  + `jira-administrators`管理员（未开放授权）\n\n#### Gitlab 代码仓库\n+ 登录地址:<http://git.ops.yangege.cn>\n+ 可直接登录，项目授权参阅`持续集成`-`Gitlab项目权限申请`工作流\n\n#### Jenkins 持续集成\n+ 登录地址:<http://ci.ops.yangege.cn>\n+ 角色\n  + `jenkins-users`普通用户,可查看构建&部署日志\n  + `jenkins-admin`管理员（未开放授权）\n\n#### Grafana 监控可视化平台\n+ 登录地址:<https://gz.ops.yangege.cn>\n+ 角色\n  + `grafana-editor-users `grafana编辑(汇总图形)权限用户组',1,NULL,'2020-05-18 05:37:31','2020-05-18 06:16:45'),(4,'SERVER_GROUP_README','#### 说明\n+ 授权的服务器组内所有服务器都可以通过`JUMP跳板机`登录\n  + 登录方式详见 <a style=\"color:#2b669a\" href=\"https://oc3.ops.yangege.cn/#/workbench/jump\" target=\"_blank\"><b>传送门</b></a>\n\n+ 授权的服务器组内所有服务都可以登录`Zabbix`监控平台查看数据\n  + `Zabbix`监控平台登录账户于本平台相同<a style=\"color:#2b669a\" href=\"http://zabbix.ops.yangege.cn\" target=\"_blank\"><b>传送门</b></a>\n\n+ 授权的服务器组内所有服务器都通过OC内置WebXTerm批量登录\n  + WebXTerm支持高权限登录服务器（需要在工单中申请服务器组的管理员权限）\n\n##### OC-Web-XTerm\n<video width=\"100%\" controls Autoplay=autoplay>\n<source src=\"https://opscloud-store.oss-cn-hangzhou.aliyuncs.com/help/opscloud/web-xterm/oc-web-xterm.mov\" type=\"video/mp4\" align=center>\n</video>',1,NULL,'2020-05-18 05:38:36','2020-05-18 06:15:48'),(5,'OC_ROLE','#### `dev`角色\n+ 查看服务器相关信息\n+ 堡垒机权限，监控权限，日志权限等',1,NULL,'2020-05-18 05:39:22','2020-05-18 06:17:47'),(6,'RAM_POLICY','#### 账户\n+ 阿里云主账户\n   + 主账户uid : 000000000\n   + 子账户: ${username}@1255805305757185\n   + 登录地址:  https://signin.aliyun.com/000000/.onaliyun.com/login.htm\n\n\n#### 用户RAM策略详情\n+ 个人详情-我的详情-阿里云RAM账户授权策略 中查看\n\n#### 登录密码\n> 密码同oc密码一致，但必须符合密码强度（包含英文大小写，数字。特殊字符，长度>=10位），登录错误请联系运维！\n\n',1,NULL,'2020-06-12 08:16:46','2020-07-15 10:17:17');
/*!40000 ALTER TABLE `oc_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_env`
--

DROP TABLE IF EXISTS `oc_env`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_env` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `env_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `env_type` int(2) NOT NULL,
  `color` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `comment` varchar(128) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `env_name` (`env_name`),
  UNIQUE KEY `env_type` (`env_type`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_env`
--

LOCK TABLES `oc_env` WRITE;
/*!40000 ALTER TABLE `oc_env` DISABLE KEYS */;
INSERT INTO `oc_env` VALUES (1,'default',0,'#B7B6B6','默认环境','2020-04-02 14:06:38','2020-07-02 09:56:04'),(2,'dev',1,'#5bc0de','开发环境','2020-01-10 05:53:51','2020-02-21 10:17:07'),(3,'test',5,'#6B6A6C','测试环境','2020-01-10 05:53:55','2020-07-02 09:56:14'),(4,'gray',3,'#000000','灰度环境','2020-03-11 06:03:58','2020-07-02 09:57:27'),(5,'prod',4,'#E34C15','生产环境','2020-02-22 05:13:16','2020-02-22 05:13:16'),(6,'daily',2,'#449d44','日常环境','2020-03-04 03:10:44','2020-03-04 03:10:44'),(14,'pre',7,'#FC7A00','预发环境','2020-03-11 06:05:28','2020-07-02 09:56:38');
/*!40000 ALTER TABLE `oc_env` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_keybox`
--

DROP TABLE IF EXISTS `oc_keybox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_keybox` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `system_user` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '系统账户',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
  `public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公钥',
  `passphrase` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
  `private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '私钥',
  `key_type` int(2) DEFAULT NULL COMMENT 'key类型',
  `default_key` int(1) NOT NULL DEFAULT '0' COMMENT '默认key',
  `fingerprint` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '指纹',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `system_user` (`system_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='key盒子';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_keybox`
--

LOCK TABLES `oc_keybox` WRITE;
/*!40000 ALTER TABLE `oc_keybox` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_keybox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_kubernetes_application`
--

DROP TABLE IF EXISTS `oc_kubernetes_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_kubernetes_application` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '应用名称',
  `server_group_id` int(11) DEFAULT NULL COMMENT '服务器组id',
  `business_id` int(11) DEFAULT NULL COMMENT '业务id',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='kubernetes应用';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_kubernetes_application`
--

LOCK TABLES `oc_kubernetes_application` WRITE;
/*!40000 ALTER TABLE `oc_kubernetes_application` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_kubernetes_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_kubernetes_application_instance`
--

DROP TABLE IF EXISTS `oc_kubernetes_application_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_kubernetes_application_instance` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `application_id` int(11) NOT NULL,
  `instance_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `env_type` int(2) NOT NULL COMMENT '环境类型',
  `env_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '环境标签',
  `template_variable` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模版变量',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `application_id` (`application_id`,`env_type`,`env_label`),
  UNIQUE KEY `instance_name` (`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='kubernetes应用实例';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_kubernetes_application_instance`
--

LOCK TABLES `oc_kubernetes_application_instance` WRITE;
/*!40000 ALTER TABLE `oc_kubernetes_application_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_kubernetes_application_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_kubernetes_cluster`
--

DROP TABLE IF EXISTS `oc_kubernetes_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_kubernetes_cluster` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '集群名称',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `master_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理url',
  `kubeconfig` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置文件',
  `env_type` int(2) DEFAULT NULL COMMENT '环境类型',
  `server_group_id` int(11) DEFAULT NULL,
  `server_group_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='kubernetes集群';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_kubernetes_cluster`
--

LOCK TABLES `oc_kubernetes_cluster` WRITE;
/*!40000 ALTER TABLE `oc_kubernetes_cluster` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_kubernetes_cluster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_kubernetes_cluster_namespace`
--

DROP TABLE IF EXISTS `oc_kubernetes_cluster_namespace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_kubernetes_cluster_namespace` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cluster_id` int(11) NOT NULL,
  `namespace` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '命名空间',
  `env_type` int(2) NOT NULL COMMENT '环境类型',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cluster_id` (`cluster_id`,`namespace`),
  UNIQUE KEY `env_type` (`env_type`,`namespace`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='kubernetes集群';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_kubernetes_cluster_namespace`
--

LOCK TABLES `oc_kubernetes_cluster_namespace` WRITE;
/*!40000 ALTER TABLE `oc_kubernetes_cluster_namespace` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_kubernetes_cluster_namespace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_kubernetes_deployment`
--

DROP TABLE IF EXISTS `oc_kubernetes_deployment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_kubernetes_deployment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `application_id` int(11) NOT NULL DEFAULT '0' COMMENT '应用id',
  `instance_id` int(11) NOT NULL DEFAULT '0' COMMENT '应用实例id',
  `namespace_id` int(11) NOT NULL,
  `namespace` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '命名空间',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `label_app` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签',
  `available_replicas` int(11) DEFAULT NULL COMMENT '可用容器组数量',
  `replicas` int(11) DEFAULT NULL COMMENT '容器组数量',
  `deployment_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `namespace_id` (`namespace_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='kubernetes无状态';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_kubernetes_deployment`
--

LOCK TABLES `oc_kubernetes_deployment` WRITE;
/*!40000 ALTER TABLE `oc_kubernetes_deployment` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_kubernetes_deployment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_kubernetes_service`
--

DROP TABLE IF EXISTS `oc_kubernetes_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_kubernetes_service` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `application_id` int(11) NOT NULL DEFAULT '0' COMMENT '应用id',
  `instance_id` int(11) NOT NULL DEFAULT '0' COMMENT '应用实例id',
  `namespace_id` int(11) NOT NULL,
  `namespace` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '命名空间',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `cluster_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '集群ip',
  `service_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '服务类型',
  `service_yaml` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `service_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `namespace_id` (`namespace_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='kubernetes服务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_kubernetes_service`
--

LOCK TABLES `oc_kubernetes_service` WRITE;
/*!40000 ALTER TABLE `oc_kubernetes_service` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_kubernetes_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_kubernetes_service_port`
--

DROP TABLE IF EXISTS `oc_kubernetes_service_port`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_kubernetes_service_port` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL COMMENT '服务id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '端口名称',
  `node_port` int(11) DEFAULT NULL COMMENT 'node端口',
  `port` int(11) DEFAULT NULL COMMENT '端口',
  `target_port` int(11) DEFAULT NULL COMMENT 'target端口',
  `protocol` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '协议',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `service_id` (`service_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='kubernetes服务端口';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_kubernetes_service_port`
--

LOCK TABLES `oc_kubernetes_service_port` WRITE;
/*!40000 ALTER TABLE `oc_kubernetes_service_port` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_kubernetes_service_port` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_kubernetes_template`
--

DROP TABLE IF EXISTS `oc_kubernetes_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_kubernetes_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `template_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '模版类型',
  `template_yaml` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模版yaml',
  `env_type` int(2) DEFAULT NULL COMMENT '环境类型',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='kubernetes模版';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_kubernetes_template`
--

LOCK TABLES `oc_kubernetes_template` WRITE;
/*!40000 ALTER TABLE `oc_kubernetes_template` DISABLE KEYS */;
INSERT INTO `oc_kubernetes_template` VALUES (1,'service-test','SERVICE','---\napiVersion: v1\nkind: Service\nmetadata:\n  name: ${appName}-${envLabel}-lb\n  namespace: test\nspec:\n  externalTrafficPolicy: Cluster\n  ports:\n    - name: dubbo\n      port: 20880\n      protocol: TCP\n      targetPort: 20880\n    - name: debug\n      port: 5000\n      protocol: TCP\n      targetPort: 5000\n    - name: http\n      port: 8080\n      protocol: TCP\n      targetPort: 8080\n  selector:\n    app: ${appName}-${envLabel}\n  sessionAffinity: None\n  type: NodePort',5,'服务模版','2020-06-30 02:36:52','2020-06-30 02:35:13'),(2,'service-pre','SERVICE','---\napiVersion: v1\nkind: Service\nmetadata:\n  name: ${appName}-${envLabel}-lb\n  namespace: pre\nspec:\n  externalTrafficPolicy: Cluster\n  ports:\n    - name: http\n      port: 8080\n      protocol: TCP\n      targetPort: 8080\n  selector:\n    app: ${appName}-${envLabel}\n  sessionAffinity: None\n  type: NodePort',7,'服务模版','2020-06-30 02:36:54','2020-06-30 02:36:07'),(3,'deployment-test','DEPLOYMENT','---\napiVersion: apps/v1\nkind: Deployment\nmetadata:\n  labels:\n    app: ${appName}-${envLabel}\n  name: ${appName}-${envLabel}-deployment\n  namespace: test\nspec:\n  replicas: 1\n  selector:\n    matchLabels:\n      app: ${appName}-${envLabel}\n  template:\n    metadata:\n      labels:\n        app: ${appName}-${envLabel}\n    spec:\n      containers:\n      - name: ${appName}-${envLabel}\n        image: registry-vpc.cn-hangzhou.aliyuncs.com/gegejia/${appName}:${envLabel}\n        imagePullPolicy: Always\n        volumeMounts:\n        - name: ${appName}-${envLabel}-log\n          mountPath: /logs\n        resources:\n          limits:\n            cpu: 2000m\n            memory: 4Gi\n          requests:\n            cpu: 700m\n            memory: 2Gi\n        env:\n          - name: LANG\n            value: en_US.UTF-8\n          - name: JAVA_HOME\n            value: /usr/lib/jvm/java-1.8-openjdk\n          - name: aliyun_logs_k8s-test\n            value: /logs/*\n      imagePullSecrets:\n      - name: regsecret\n      volumes:\n        - name: ${appName}-${envLabel}-log\n          hostPath:\n            path: /logs/${appName}-${envLabel}',5,'无状态模版','2020-06-30 08:57:25','2020-06-30 08:56:16'),(4,'deployment-pre','DEPLOYMENT','---\napiVersion: apps/v1\nkind: Deployment\nmetadata:\n  labels:\n    app: ${appName}-${envLabel}\n  name: ${appName}-${envLabel}-deployment\n  namespace: pre\nspec:\n  replicas: 1\n  selector:\n    matchLabels:\n      app: ${appName}-${envLabel}\n  template:\n    metadata:\n      labels:\n        app: ${appName}-${envLabel}\n    spec:\n      containers:\n      - name: ${appName}-${envLabel}\n        image: registry-vpc.cn-hangzhou.aliyuncs.com/gegejia/${appName}:${envLabel}\n        imagePullPolicy: Always\n        volumeMounts:\n        - name: ${appName}-${envLabel}-log\n          mountPath: /logs\n        - name: platform-conf\n          mountPath: /opt/platform\n        resources:\n          limits:\n            cpu: 3000m\n            memory: 6Gi\n          requests:\n            cpu: 1500m\n            memory: 3Gi\n        env:\n          - name: LANG\n            value: en_US.UTF-8\n          - name: JAVA_HOME\n            value: /usr/lib/jvm/java-1.8-openjdk\n          - name: security_datasource_skip\n            value: \'false\'\n          - name: aliyun_logs_k8s-pre\n            value: /logs/*\n      imagePullSecrets:\n      - name: regsecret\n      volumes:\n        - name: ${appName}-${envLabel}-log\n          hostPath:\n            path: /logs/${appName}-${envLabel}\n        - name: platform-conf\n          hostPath:\n            path: /opt/platform',7,'无状态模版','2020-06-30 08:58:22','2020-06-30 08:58:00');
/*!40000 ALTER TABLE `oc_kubernetes_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_lifecycle_mgmt`
--

DROP TABLE IF EXISTS `oc_lifecycle_mgmt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_lifecycle_mgmt` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `business_id` int(11) DEFAULT NULL,
  `business_type` int(2) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_lifecycle_mgmt`
--

LOCK TABLES `oc_lifecycle_mgmt` WRITE;
/*!40000 ALTER TABLE `oc_lifecycle_mgmt` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_lifecycle_mgmt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_org_department`
--

DROP TABLE IF EXISTS `oc_org_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_org_department` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '部门名称',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '中文说明',
  `parent_id` int(11) DEFAULT NULL COMMENT '父部门id,根部门id=1',
  `dept_hiding` int(1) NOT NULL DEFAULT '0' COMMENT '是否隐藏部门',
  `dept_type` int(2) NOT NULL DEFAULT '0' COMMENT '部门类型',
  `dept_order` int(11) NOT NULL DEFAULT '50000',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织架构-部门';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_org_department`
--

LOCK TABLES `oc_org_department` WRITE;
/*!40000 ALTER TABLE `oc_org_department` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_org_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_org_department_member`
--

DROP TABLE IF EXISTS `oc_org_department_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_org_department_member` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `department_id` int(11) NOT NULL COMMENT '部门id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '中文说明',
  `member_type` int(2) NOT NULL DEFAULT '0' COMMENT '部门类型',
  `is_leader` int(2) NOT NULL DEFAULT '0' COMMENT '经理',
  `is_approval_authority` int(2) NOT NULL DEFAULT '0' COMMENT '审批',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `department_id` (`department_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织架构-部门';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_org_department_member`
--

LOCK TABLES `oc_org_department_member` WRITE;
/*!40000 ALTER TABLE `oc_org_department_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_org_department_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_profile_subscription`
--

DROP TABLE IF EXISTS `oc_profile_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_profile_subscription` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `subscription_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '订阅类型',
  `server_group_id` int(11) NOT NULL,
  `host_pattern` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '主机模式',
  `script_id` int(11) DEFAULT NULL COMMENT 'playbook脚本id',
  `vars` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '变量',
  `server_task_id` int(11) DEFAULT NULL COMMENT '最近执行的任务id',
  `execution_time` timestamp NULL DEFAULT NULL,
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `subscription_type` (`subscription_type`,`host_pattern`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配置文件订阅';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_profile_subscription`
--

LOCK TABLES `oc_profile_subscription` WRITE;
/*!40000 ALTER TABLE `oc_profile_subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_profile_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server`
--

DROP TABLE IF EXISTS `oc_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `server_group_id` int(11) NOT NULL,
  `login_type` int(1) DEFAULT NULL,
  `login_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `env_type` int(2) NOT NULL DEFAULT '0',
  `public_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `private_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `server_type` int(11) DEFAULT NULL,
  `area` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `serial_number` int(8) NOT NULL DEFAULT '0',
  `monitor_status` int(1) NOT NULL DEFAULT '-1',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `server_status` int(2) NOT NULL DEFAULT '1' COMMENT '服务器状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `env_type` (`env_type`,`serial_number`,`server_group_id`),
  KEY `name` (`name`),
  KEY `private_ip` (`private_ip`),
  KEY `server_group_id` (`server_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server`
--

LOCK TABLES `oc_server` WRITE;
/*!40000 ALTER TABLE `oc_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_attribute`
--

DROP TABLE IF EXISTS `oc_server_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server_attribute` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `business_id` int(11) NOT NULL COMMENT '业务id',
  `business_type` int(2) NOT NULL COMMENT '业务类型',
  `attribute_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '属性类型',
  `group_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '属性组名',
  `attributes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '属性配置项',
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `business_id` (`business_id`,`business_type`,`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_attribute`
--

LOCK TABLES `oc_server_attribute` WRITE;
/*!40000 ALTER TABLE `oc_server_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_change_task`
--

DROP TABLE IF EXISTS `oc_server_change_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server_change_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务唯一id',
  `server_id` int(11) DEFAULT NULL COMMENT '服务器id',
  `server_group_id` int(11) DEFAULT NULL COMMENT '服务器组id',
  `change_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '变更类型',
  `change_number` int(11) DEFAULT NULL COMMENT '变更数量',
  `task_flow_id` int(11) DEFAULT NULL COMMENT '当前变更流程id',
  `task_flow_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '流程名称',
  `result_code` int(2) DEFAULT '0' COMMENT '任务返回值',
  `result_msg` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务信息',
  `task_status` int(1) NOT NULL COMMENT '任务状态',
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器任务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_change_task`
--

LOCK TABLES `oc_server_change_task` WRITE;
/*!40000 ALTER TABLE `oc_server_change_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server_change_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_change_task_flow`
--

DROP TABLE IF EXISTS `oc_server_change_task_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server_change_task_flow` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务唯一id',
  `flow_parent_id` int(11) DEFAULT NULL COMMENT '父流程id',
  `task_flow_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '流程名称',
  `result_code` int(2) DEFAULT '0' COMMENT '任务返回值',
  `result_msg` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务信息',
  `task_status` int(1) DEFAULT NULL COMMENT '任务状态',
  `task_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '任务详情',
  `external_id` int(11) DEFAULT NULL COMMENT '外部id',
  `external_type` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '外部类型',
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_id` (`task_id`,`task_flow_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器任务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_change_task_flow`
--

LOCK TABLES `oc_server_change_task_flow` WRITE;
/*!40000 ALTER TABLE `oc_server_change_task_flow` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server_change_task_flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_group`
--

DROP TABLE IF EXISTS `oc_server_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `grp_type` int(2) NOT NULL DEFAULT '0',
  `in_workorder` int(1) NOT NULL DEFAULT '1',
  `comment` longtext,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_group`
--

LOCK TABLES `oc_server_group` WRITE;
/*!40000 ALTER TABLE `oc_server_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_group_permission`
--

DROP TABLE IF EXISTS `oc_server_group_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server_group_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `server_group_id` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`server_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_group_permission`
--

LOCK TABLES `oc_server_group_permission` WRITE;
/*!40000 ALTER TABLE `oc_server_group_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server_group_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_group_property`
--

DROP TABLE IF EXISTS `oc_server_group_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server_group_property` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `server_group_id` int(11) NOT NULL COMMENT '服务器组id',
  `env_type` int(2) NOT NULL DEFAULT '0' COMMENT '环境type',
  `property_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '属性名称',
  `property_value` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `server_group_id` (`server_group_id`,`env_type`,`property_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器组外部属性表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_group_property`
--

LOCK TABLES `oc_server_group_property` WRITE;
/*!40000 ALTER TABLE `oc_server_group_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server_group_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_group_type`
--

DROP TABLE IF EXISTS `oc_server_group_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server_group_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `grp_type` int(2) NOT NULL DEFAULT '0',
  `color` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `comment` longtext,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `grp_type` (`grp_type`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_group_type`
--

LOCK TABLES `oc_server_group_type` WRITE;
/*!40000 ALTER TABLE `oc_server_group_type` DISABLE KEYS */;
INSERT INTO `oc_server_group_type` VALUES (2,'default',0,'#31A9C8','默认类型','2020-02-21 07:43:18','2020-02-21 07:43:18'),(4,'zookeeper',1,'#dd4d3a','zookeeper','2020-02-21 06:09:50','2020-02-21 06:26:04'),(5,'microservice',2,'#F29603','微服务','2020-02-21 07:09:09','2020-02-21 07:09:09'),(9,'mysql',3,'#1451B4','mysql','2020-03-11 02:48:34','2020-03-11 02:48:34'),(10,'other',4,NULL,NULL,'2020-03-11 02:48:52','2020-03-11 02:48:52'),(11,'public',6,'#E80F07',NULL,'2020-03-11 02:54:51','2020-03-11 02:54:51'),(12,'redis',7,'#D7160F',NULL,'2020-03-11 02:55:13','2020-03-11 02:55:13'),(13,'web-server',8,NULL,NULL,'2020-03-11 03:00:39','2020-03-11 03:00:39'),(14,'front-end',9,NULL,NULL,'2020-03-11 03:00:41','2020-03-11 03:00:41'),(15,'bi',10,NULL,NULL,'2020-03-11 03:00:41','2020-03-11 03:00:41'),(16,'testType',11,NULL,'','2020-03-11 03:00:44','2020-03-11 03:00:44'),(17,'getway',12,NULL,'终端跳板机','2020-03-11 03:00:45','2020-03-11 03:00:45'),(18,'dubbo',13,NULL,'','2020-03-11 03:00:45','2020-03-11 03:00:45'),(19,'elasticsearch',14,NULL,'','2020-03-11 03:00:46','2020-03-11 03:00:46'),(20,'job',15,NULL,'','2020-03-11 03:00:47','2020-03-11 03:00:47'),(21,'k8s',16,NULL,'','2020-03-11 03:00:50','2020-03-11 03:00:50'),(22,'zabbix',17,NULL,'','2020-03-11 03:00:50','2020-03-11 03:00:50'),(23,'kafka',18,NULL,'kafka集群','2020-03-11 03:00:50','2020-03-11 03:00:50');
/*!40000 ALTER TABLE `oc_server_group_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_task`
--

DROP TABLE IF EXISTS `oc_server_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '资源组id',
  `executor_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `user_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '资源名称',
  `task_type` int(11) NOT NULL DEFAULT '0' COMMENT '0:命令 1:脚本 2:playbook',
  `system_type` int(11) NOT NULL DEFAULT '0' COMMENT '系统任务类型',
  `server_target_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '目标',
  `comment` varchar(256) DEFAULT NULL,
  `task_size` int(11) DEFAULT NULL,
  `finalized` int(1) DEFAULT '1' COMMENT '是否完成',
  `stop_type` int(11) DEFAULT '0' COMMENT '终止任务',
  `exit_value` int(2) DEFAULT NULL COMMENT '退出值',
  `task_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务器任务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_task`
--

LOCK TABLES `oc_server_task` WRITE;
/*!40000 ALTER TABLE `oc_server_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_task_member`
--

DROP TABLE IF EXISTS `oc_server_task_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_server_task_member` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL,
  `host_pattern` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '主机模式',
  `server_id` int(11) DEFAULT NULL,
  `env_type` int(2) NOT NULL DEFAULT '0' COMMENT '环境类型',
  `manage_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理ip',
  `comment` varchar(256) DEFAULT NULL,
  `finalized` int(1) DEFAULT '1' COMMENT '是否完成',
  `stop_type` int(11) DEFAULT '0' COMMENT '终止任务',
  `exit_value` int(2) DEFAULT NULL COMMENT '退出值',
  `task_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务状态',
  `task_result` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `output_msg` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `error_msg` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_id` (`task_id`,`host_pattern`),
  UNIQUE KEY `manage_ip` (`manage_ip`,`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_task_member`
--

LOCK TABLES `oc_server_task_member` WRITE;
/*!40000 ALTER TABLE `oc_server_task_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server_task_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_setting`
--

DROP TABLE IF EXISTS `oc_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_setting` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `setting_group` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '设置组',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设置名称',
  `setting_value` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '设置值',
  `encrypted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否加密',
  `comment` varchar(256) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_setting`
--

LOCK TABLES `oc_setting` WRITE;
/*!40000 ALTER TABLE `oc_setting` DISABLE KEYS */;
INSERT INTO `oc_setting` VALUES (1,'SERVER','SERVER_ACCOUNT','gegejia',0,'服务器默认登录账户','2020-06-04 07:39:50','2020-06-04 07:42:06'),(2,'SERVER','SERVER_HIGH_AUTHORITY_ACCOUNT','admin',0,'服务器高权限账户','2020-06-04 07:41:00','2020-06-04 07:42:07'),(3,'OPSCLOUD','VERSION','3.0.1',0,'版本号','2020-06-04 07:41:59','2020-07-15 10:19:49'),(27,'ORG','ORG_DEPT_ID','9',0,'组织架构拓扑根id','2020-06-16 02:02:40','2020-06-16 02:04:47'),(28,'JUMPSERVER','JUMPSERVER_ASSETS_SYSTEMUSER_ID','b99ccaebde1d435cb3e827537f0fe363',0,'低权限系统账户id','2020-06-22 06:30:20','2020-06-22 07:05:15'),(29,'JUMPSERVER','JUMPSERVER_ASSETS_ADMIN_SYSTEMUSER_ID','4a53f05f5e3a4ee395891c30cdc4470e',0,'高权限系统账户id','2020-06-22 06:30:24','2020-06-22 07:05:20'),(30,'JUMPSERVER','JUMPSERVER_ASSETS_ADMINUSER_ID','e7f642c58dad44388e26986af98db08f',0,'管理账户id','2020-06-22 06:56:35','2020-06-22 07:05:01');
/*!40000 ALTER TABLE `oc_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_tag`
--

DROP TABLE IF EXISTS `oc_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_tag` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `tag_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '标签Key,全局唯一',
  `color` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '颜色值',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag_key` (`tag_key`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_tag`
--

LOCK TABLES `oc_tag` WRITE;
/*!40000 ALTER TABLE `oc_tag` DISABLE KEYS */;
INSERT INTO `oc_tag` VALUES (12,'daily','#28782C','日常环境标签','2020-02-26 12:44:36','2020-02-26 12:44:36'),(13,'dev','#32404A','开发环境标签','2020-02-27 09:36:57','2020-02-27 09:36:57'),(15,'ECR','#DD1010','弹性计算资源(Elastic Compute Resource)','2020-05-22 07:06:35','2020-05-22 07:06:35');
/*!40000 ALTER TABLE `oc_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_terminal_session`
--

DROP TABLE IF EXISTS `oc_terminal_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_terminal_session` (
  `id` int(2) unsigned NOT NULL AUTO_INCREMENT,
  `session_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '会话uuid',
  `user_id` int(11) DEFAULT NULL,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `remote_addr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `is_closed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否关闭',
  `close_time` timestamp NULL DEFAULT NULL COMMENT '关闭时间',
  `term_hostname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '服务端主机名',
  `term_addr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '服务端ip',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `session_id` (`session_id`),
  KEY `is_closed` (`is_closed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_terminal_session`
--

LOCK TABLES `oc_terminal_session` WRITE;
/*!40000 ALTER TABLE `oc_terminal_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_terminal_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_terminal_session_instance`
--

DROP TABLE IF EXISTS `oc_terminal_session_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_terminal_session_instance` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `session_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '会话uuid',
  `instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '实例id',
  `duplicate_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '会话复制实例id',
  `system_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '系统账户',
  `host_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '主机ip',
  `output_size` bigint(20) NOT NULL DEFAULT '0' COMMENT '输出文件大小',
  `is_closed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否关闭',
  `open_time` timestamp NULL DEFAULT NULL COMMENT '打开时间',
  `close_time` timestamp NULL DEFAULT NULL COMMENT '关闭时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `session_id` (`session_id`,`instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_terminal_session_instance`
--

LOCK TABLES `oc_terminal_session_instance` WRITE;
/*!40000 ALTER TABLE `oc_terminal_session_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_terminal_session_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_user`
--

DROP TABLE IF EXISTS `oc_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL DEFAULT '' COMMENT '用户名',
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '前端框架专用',
  `password` varchar(256) DEFAULT NULL,
  `name` varchar(128) DEFAULT '' COMMENT '姓名',
  `display_name` varchar(128) NOT NULL DEFAULT '' COMMENT '显示名称',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '邮箱',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `last_login` timestamp NULL DEFAULT NULL,
  `wechat` varchar(128) DEFAULT '',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '手机',
  `created_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `source` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '数据源',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='oc用户本地用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_user`
--

LOCK TABLES `oc_user` WRITE;
/*!40000 ALTER TABLE `oc_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_user_api_token`
--

DROP TABLE IF EXISTS `oc_user_api_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_user_api_token` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL DEFAULT '' COMMENT '用户登录名',
  `token_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `token` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'None' COMMENT '登录唯一标识',
  `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否无效。0：无效；1：有效',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `expired_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='api调用令牌';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_user_api_token`
--

LOCK TABLES `oc_user_api_token` WRITE;
/*!40000 ALTER TABLE `oc_user_api_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_user_api_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_user_credential`
--

DROP TABLE IF EXISTS `oc_user_credential`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_user_credential` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
  `credential` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '凭据内容',
  `credential_type` int(2) DEFAULT NULL,
  `fingerprint` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '指纹',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`credential_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_user_credential`
--

LOCK TABLES `oc_user_credential` WRITE;
/*!40000 ALTER TABLE `oc_user_credential` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_user_credential` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_user_database`
--

DROP TABLE IF EXISTS `oc_user_database`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_user_database` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `cloud_db_database_id` int(11) NOT NULL COMMENT '云数据库id',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`cloud_db_database_id`,`comment`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户数据库详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_user_database`
--

LOCK TABLES `oc_user_database` WRITE;
/*!40000 ALTER TABLE `oc_user_database` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_user_database` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_user_document`
--

DROP TABLE IF EXISTS `oc_user_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_user_document` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `doc_title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `doc_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '文档内容',
  `doc_type` int(2) NOT NULL DEFAULT '1' COMMENT '文档类型',
  `comment` varchar(256) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_user_document`
--

LOCK TABLES `oc_user_document` WRITE;
/*!40000 ALTER TABLE `oc_user_document` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_user_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_user_group`
--

DROP TABLE IF EXISTS `oc_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_user_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '组名',
  `grp_type` int(2) DEFAULT NULL COMMENT '组类型',
  `source` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '数据源',
  `in_workorder` int(1) DEFAULT NULL COMMENT '允许工单申请',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='oc用户本地用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_user_group`
--

LOCK TABLES `oc_user_group` WRITE;
/*!40000 ALTER TABLE `oc_user_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_user_permission`
--

DROP TABLE IF EXISTS `oc_user_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_user_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `business_id` int(11) NOT NULL COMMENT '业务id',
  `business_type` int(2) NOT NULL COMMENT '业务类型',
  `content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`business_id`,`business_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户-用户组 授权表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_user_permission`
--

LOCK TABLES `oc_user_permission` WRITE;
/*!40000 ALTER TABLE `oc_user_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_user_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_user_setting`
--

DROP TABLE IF EXISTS `oc_user_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_user_setting` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设置名称',
  `setting_group` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '设置组',
  `setting_value` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '设置值',
  `encrypted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否加密',
  `comment` varchar(256) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_user_setting`
--

LOCK TABLES `oc_user_setting` WRITE;
/*!40000 ALTER TABLE `oc_user_setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_user_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_user_token`
--

DROP TABLE IF EXISTS `oc_user_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_user_token` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL DEFAULT '' COMMENT '用户登录名',
  `token` varchar(200) DEFAULT NULL COMMENT '登录唯一标识',
  `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否无效。0：无效；1：有效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `username` (`username`,`id`,`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_user_token`
--

LOCK TABLES `oc_user_token` WRITE;
/*!40000 ALTER TABLE `oc_user_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_user_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workorder`
--

DROP TABLE IF EXISTS `oc_workorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_workorder` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工单名称',
  `workorder_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工单key',
  `readme_id` int(11) NOT NULL DEFAULT '0' COMMENT '帮助文档id',
  `workorder_group_id` int(11) DEFAULT NULL COMMENT '工单组id',
  `approval_type` int(11) NOT NULL DEFAULT '0' COMMENT '审批类型',
  `org_approval` tinyint(1) DEFAULT NULL COMMENT '是否需要组织架构审批',
  `approval_group_id` int(11) DEFAULT NULL COMMENT '审批组id',
  `workorder_mode` int(1) NOT NULL DEFAULT '0' COMMENT '模式 0 自动 1 手动',
  `workorder_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态 0 正常 1 开发 2 停用',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `workorder_key` (`workorder_key`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workorder`
--

LOCK TABLES `oc_workorder` WRITE;
/*!40000 ALTER TABLE `oc_workorder` DISABLE KEYS */;
INSERT INTO `oc_workorder` VALUES (19,'服务器组权限申请','SERVER_GROUP',4,1,1,1,19,0,0,NULL,'2020-04-27 06:29:47','2020-05-18 05:39:42'),(20,'用户组权限申请','USER_GROUP',3,1,1,1,19,0,0,NULL,'2020-05-07 03:46:10','2020-05-18 05:39:40'),(21,'本平台角色申请','AUTH_ROLE',5,1,1,1,19,0,0,NULL,'2020-05-08 07:21:13','2020-05-18 05:39:43'),(22,'阿里云RAM访问控制策略申请','RAM_POLICY',6,1,1,1,19,0,0,NULL,'2020-06-12 02:38:38','2020-06-12 02:38:55');
/*!40000 ALTER TABLE `oc_workorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workorder_approval_group`
--

DROP TABLE IF EXISTS `oc_workorder_approval_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_workorder_approval_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工单名称',
  `flow_type` int(2) DEFAULT NULL COMMENT '流程类型',
  `group_type` int(11) DEFAULT NULL,
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workorder_approval_group`
--

LOCK TABLES `oc_workorder_approval_group` WRITE;
/*!40000 ALTER TABLE `oc_workorder_approval_group` DISABLE KEYS */;
INSERT INTO `oc_workorder_approval_group` VALUES (19,'ops',0,0,'运维审批组','2020-04-27 05:03:03','2020-04-30 01:59:57'),(20,'dba',0,1,'DBA审批组','2020-04-27 05:03:35','2020-04-30 02:00:00');
/*!40000 ALTER TABLE `oc_workorder_approval_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workorder_approval_member`
--

DROP TABLE IF EXISTS `oc_workorder_approval_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_workorder_approval_member` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL COMMENT '审批组id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_id` (`group_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workorder_approval_member`
--

LOCK TABLES `oc_workorder_approval_member` WRITE;
/*!40000 ALTER TABLE `oc_workorder_approval_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_workorder_approval_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workorder_group`
--

DROP TABLE IF EXISTS `oc_workorder_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_workorder_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工单组名称',
  `workorder_type` int(2) DEFAULT NULL COMMENT '工单组类型',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `workorder_type` (`workorder_type`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workorder_group`
--

LOCK TABLES `oc_workorder_group` WRITE;
/*!40000 ALTER TABLE `oc_workorder_group` DISABLE KEYS */;
INSERT INTO `oc_workorder_group` VALUES (1,'权限申请',0,'权限申请工单组','2020-04-26 05:40:56','2020-04-28 08:15:40'),(2,'持续集成',1,'持续集成申请工单组','2020-04-28 08:22:32','2020-04-28 08:22:32');
/*!40000 ALTER TABLE `oc_workorder_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workorder_ticket`
--

DROP TABLE IF EXISTS `oc_workorder_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_workorder_ticket` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `workorder_id` int(11) NOT NULL COMMENT '工单id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `user_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '用户详情',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `flow_id` int(11) DEFAULT NULL COMMENT '工单流程id',
  `ticket_phase` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工单阶段',
  `ticket_status` int(2) DEFAULT NULL COMMENT '工单状态 0 正常  1 结束（成功） 2结束（失败）',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '申请开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束开始时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工单票据内容';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workorder_ticket`
--

LOCK TABLES `oc_workorder_ticket` WRITE;
/*!40000 ALTER TABLE `oc_workorder_ticket` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_workorder_ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workorder_ticket_entry`
--

DROP TABLE IF EXISTS `oc_workorder_ticket_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_workorder_ticket_entry` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `workorder_ticket_id` int(11) NOT NULL COMMENT '工单票据id',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `business_id` int(11) DEFAULT NULL COMMENT '业务id',
  `entry_status` int(2) NOT NULL DEFAULT '0' COMMENT '状态',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `entry_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '条目key',
  `entry_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '条目详情',
  `entry_result` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工单票据';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workorder_ticket_entry`
--

LOCK TABLES `oc_workorder_ticket_entry` WRITE;
/*!40000 ALTER TABLE `oc_workorder_ticket_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_workorder_ticket_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workorder_ticket_flow`
--

DROP TABLE IF EXISTS `oc_workorder_ticket_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_workorder_ticket_flow` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ticket_id` int(11) NOT NULL COMMENT '工单id',
  `user_id` int(11) DEFAULT NULL COMMENT '(责任人)用户id',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '(责任人)用户名',
  `flow_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '工单流程名称',
  `approval_type` int(11) NOT NULL DEFAULT '-1' COMMENT '审批类型 0:org  1:审批组',
  `flow_parent_id` int(11) DEFAULT NULL COMMENT '父流程id',
  `approval_group_id` int(11) DEFAULT NULL COMMENT '审批组id',
  `approval_status` int(11) NOT NULL DEFAULT '-1' COMMENT '审批状态 1 同意 0 拒绝 -1 未操作',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_id` (`ticket_id`,`flow_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workorder_ticket_flow`
--

LOCK TABLES `oc_workorder_ticket_flow` WRITE;
/*!40000 ALTER TABLE `oc_workorder_ticket_flow` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_workorder_ticket_flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workorder_ticket_subscribe`
--

DROP TABLE IF EXISTS `oc_workorder_ticket_subscribe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `oc_workorder_ticket_subscribe` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ticket_id` int(11) NOT NULL COMMENT '工单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `subscribe_type` int(2) DEFAULT NULL COMMENT '订阅类型 0本人 1 上级审批人  2 用户组审批人    3观察者',
  `subscribe_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_id` (`ticket_id`,`user_id`,`subscribe_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workorder_ticket_subscribe`
--

LOCK TABLES `oc_workorder_ticket_subscribe` WRITE;
/*!40000 ALTER TABLE `oc_workorder_ticket_subscribe` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_workorder_ticket_subscribe` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-15 18:21:59
