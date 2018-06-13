-- MySQL dump 10.13  Distrib 5.6.25, for Linux (x86_64)
--
-- Host: localhost    Database: opscloud
-- ------------------------------------------------------
-- Server version	5.6.25-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ci_deploy_server_version`
--

DROP TABLE IF EXISTS `ci_deploy_server_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ci_deploy_server_version` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `serverId` bigint(20) DEFAULT NULL,
  `ciDeployStatisticsId` bigint(20) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ci_deploy_server_version`
--

LOCK TABLES `ci_deploy_server_version` WRITE;
/*!40000 ALTER TABLE `ci_deploy_server_version` DISABLE KEYS */;
/*!40000 ALTER TABLE `ci_deploy_server_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ci_deploy_statistics`
--

DROP TABLE IF EXISTS `ci_deploy_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ci_deploy_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `projectName` varchar(256) NOT NULL DEFAULT '',
  `groupName` varchar(256) NOT NULL DEFAULT '',
  `env` varchar(45) NOT NULL,
  `status` int(1) NOT NULL COMMENT '停用主机监控\nhostStatusDisable = 1;\n启用主机监控\nhostStatusEnable = 0;',
  `deployId` bigint(20) NOT NULL,
  `deployType` int(5) DEFAULT '0',
  `bambooDeployVersion` varchar(128) DEFAULT NULL,
  `bambooBuildNumber` int(11) DEFAULT NULL,
  `bambooDeployProject` varchar(50) DEFAULT NULL,
  `bambooDeployRollback` tinyint(5) DEFAULT NULL,
  `bambooManualBuildTriggerReasonUserName` varchar(50) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `errorCode` int(5) DEFAULT NULL,
  `branchName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `deployId_UNIQUE` (`deployId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ci_deploy_statistics`
--

LOCK TABLES `ci_deploy_statistics` WRITE;
/*!40000 ALTER TABLE `ci_deploy_statistics` DISABLE KEYS */;
/*!40000 ALTER TABLE `ci_deploy_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ci_user`
--

DROP TABLE IF EXISTS `ci_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ci_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `usergroupId` bigint(20) NOT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`,`usergroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ci_user`
--

LOCK TABLES `ci_user` WRITE;
/*!40000 ALTER TABLE `ci_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `ci_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ci_user_group`
--

DROP TABLE IF EXISTS `ci_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ci_user_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupName` varchar(100) NOT NULL DEFAULT '',
  `content` varchar(100) DEFAULT NULL,
  `serverGroupId` bigint(20) DEFAULT NULL,
  `envType` int(11) NOT NULL DEFAULT '0',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupName` (`groupName`,`envType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ci_user_group`
--

LOCK TABLES `ci_user_group` WRITE;
/*!40000 ALTER TABLE `ci_user_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `ci_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cmdb_systems`
--

DROP TABLE IF EXISTS `cmdb_systems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmdb_systems` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `systemName` varchar(100) NOT NULL DEFAULT '' COMMENT '系统名称',
  `systemUrl` varchar(400) DEFAULT NULL COMMENT '系统url',
  `imgUrl` varchar(400) DEFAULT NULL COMMENT '图片url',
  `systemDesc` varchar(200) DEFAULT NULL COMMENT '系统介绍',
  `owner` varchar(100) NOT NULL DEFAULT '' COMMENT '系统负责人',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique` (`systemName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cmdb_systems`
--

LOCK TABLES `cmdb_systems` WRITE;
/*!40000 ALTER TABLE `cmdb_systems` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmdb_systems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cmdb_user`
--

DROP TABLE IF EXISTS `cmdb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmdb_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(200) DEFAULT NULL COMMENT '用户名',
  `displayName` varchar(200) DEFAULT NULL COMMENT '用户显示名',
  `mail` varchar(200) DEFAULT NULL COMMENT '邮箱地址',
  `mobile` varchar(20) DEFAULT NULL COMMENT '用户手机',
  `pwd` varchar(50) DEFAULT NULL COMMENT '密码',
  `rsaKey` text COMMENT '公钥',
  `authed` tinyint(5) DEFAULT NULL COMMENT '0：未授权；1：已授权',
  `zabbixAuthed` tinyint(5) DEFAULT '0' COMMENT 'zabbix账户授权\n0：未授权；1：已授权',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2442 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cmdb_user`
--

LOCK TABLES `cmdb_user` WRITE;
/*!40000 ALTER TABLE `cmdb_user` DISABLE KEYS */;
INSERT INTO `cmdb_user` VALUES (140,'admin','liang jian','ixrjog@qq.com','1234567890','00000000000',NULL,NULL,0,'2016-11-22 12:50:24','2018-06-08 01:17:59'),(2405,'opscloud','opscloud','opscloud',NULL,'02ni7usnuDK4qQSKtacm',NULL,0,0,'2018-06-07 05:33:30','2018-06-07 07:51:10');
/*!40000 ALTER TABLE `cmdb_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cmdb_user_leave`
--

DROP TABLE IF EXISTS `cmdb_user_leave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmdb_user_leave` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `username` varchar(200) NOT NULL DEFAULT '',
  `displayName` varchar(200) DEFAULT '',
  `mail` varchar(200) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `dumpLdap` int(3) DEFAULT '0',
  `dumpMail` int(3) DEFAULT '0',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cmdb_user_leave`
--

LOCK TABLES `cmdb_user_leave` WRITE;
/*!40000 ALTER TABLE `cmdb_user_leave` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmdb_user_leave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dns_dnsmasq`
--

DROP TABLE IF EXISTS `dns_dnsmasq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dns_dnsmasq` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `dnsGroupId` bigint(20) DEFAULT NULL,
  `itemType` int(11) DEFAULT NULL,
  `item` varchar(100) DEFAULT NULL,
  `itemValue` varchar(200) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dns_dnsmasq`
--

LOCK TABLES `dns_dnsmasq` WRITE;
/*!40000 ALTER TABLE `dns_dnsmasq` DISABLE KEYS */;
/*!40000 ALTER TABLE `dns_dnsmasq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dns_group`
--

DROP TABLE IF EXISTS `dns_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dns_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(200) DEFAULT NULL,
  `groupType` int(11) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dns_group`
--

LOCK TABLES `dns_group` WRITE;
/*!40000 ALTER TABLE `dns_group` DISABLE KEYS */;
INSERT INTO `dns_group` VALUES (1,'办公网DNS-DNSMASQ',1,'2017-07-10 06:28:01','2017-07-10 06:28:26');
/*!40000 ALTER TABLE `dns_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dns_server`
--

DROP TABLE IF EXISTS `dns_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dns_server` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `serverId` bigint(20) DEFAULT NULL,
  `dnsGroupId` bigint(20) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dns_server`
--

LOCK TABLES `dns_server` WRITE;
/*!40000 ALTER TABLE `dns_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `dns_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ecs_property`
--

DROP TABLE IF EXISTS `ecs_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecs_property` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `serverId` bigint(20) DEFAULT NULL,
  `instanceId` varchar(45) NOT NULL DEFAULT '',
  `propertyType` int(11) NOT NULL,
  `propertyValue` varchar(100) NOT NULL DEFAULT '',
  `propertyName` varchar(100) NOT NULL DEFAULT '',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ecs_property`
--

LOCK TABLES `ecs_property` WRITE;
/*!40000 ALTER TABLE `ecs_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecs_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ecs_template`
--

DROP TABLE IF EXISTS `ecs_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecs_template` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `zoneId` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `instanceType` varchar(50) DEFAULT NULL,
  `networkSupport` int(11) DEFAULT NULL,
  `cpu` int(11) DEFAULT NULL,
  `memory` int(11) DEFAULT NULL,
  `dataDiskSize` int(11) DEFAULT NULL,
  `ioOptimized` varchar(50) DEFAULT NULL,
  `systemDiskCategory` varchar(50) DEFAULT NULL,
  `dataDisk1Category` varchar(50) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ecs_template`
--

LOCK TABLES `ecs_template` WRITE;
/*!40000 ALTER TABLE `ecs_template` DISABLE KEYS */;
INSERT INTO `ecs_template` VALUES (1,'cn-hangzhou-e','web-service标配模版','ecs.n1.large',1,4,8,100,'optimized','cloud_efficiency','cloud_efficiency','2017-06-05 07:08:18','2017-06-16 06:17:53'),(2,'cn-hangzhou-e','web-service低配模版','ecs.n1.medium',1,2,4,100,'optimized','cloud_efficiency','cloud_efficiency','2017-06-05 07:08:18','2017-06-16 06:17:52'),(3,'cn-hangzhou-b','web-service标配模版','ecs.s3.large',1,4,8,100,'optimized','cloud_efficiency','cloud_efficiency','2017-06-05 07:08:18','2017-06-16 06:17:51'),(4,'cn-hangzhou-b','web-service低配模版','ecs.s2.large',1,2,4,100,'optimized','cloud_efficiency','cloud_efficiency','2017-06-05 07:08:18','2017-06-16 06:17:50'),(5,'cn-hangzhou-c','web-service标配模版','ecs.s3.large',0,4,8,100,'none','cloud','cloud','2017-06-05 07:08:18','2017-06-16 06:17:49'),(6,'cn-hangzhou-c','web-service低配模版','ecs.s2.large',0,2,4,100,'none','cloud','cloud','2017-06-05 07:08:18','2017-06-16 06:17:46'),(7,'cn-hangzhou-e','低配模版','ecs.n1.tiny',1,1,1,100,'optimized','cloud_efficiency','cloud_efficiency','2017-06-05 07:08:18','2017-06-16 06:17:43'),(8,'cn-hangzhou-e','低配模版','ecs.n1.small',1,1,2,100,'optimized','cloud_efficiency','cloud_efficiency','2017-06-05 07:08:18','2017-06-16 06:17:42'),(9,'cn-hangzhou-b','低配模版','ecs.n1.tiny',1,1,1,100,'optimized','cloud_efficiency','cloud_efficiency','2017-06-05 07:08:18','2017-06-16 06:17:40'),(10,'cn-hangzhou-b','低配模版','ecs.n1.small',1,1,2,100,'optimized','cloud_efficiency','cloud_efficiency','2017-06-05 07:08:18','2017-06-16 06:17:38'),(11,'cn-hangzhou-c','低配模版','ecs.t1.small',0,1,1,100,'none','cloud','cloud','2017-06-05 07:08:18','2017-06-16 06:17:02'),(12,'cn-hangzhou-c','低配模版','ecs.s1.small',0,1,2,100,'none','cloud','cloud','2017-06-05 07:08:18','2017-06-16 06:16:39');
/*!40000 ALTER TABLE `ecs_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `explain_job`
--

DROP TABLE IF EXISTS `explain_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `explain_job` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `metaId` bigint(20) NOT NULL COMMENT '源计划Id',
  `jobBranch` varchar(200) NOT NULL DEFAULT '' COMMENT '任务分支',
  `jobWeight` int(11) DEFAULT NULL COMMENT '任务权重',
  `jobVersion` int(11) DEFAULT NULL COMMENT '任务版本',
  `jobStatus` int(11) DEFAULT NULL COMMENT '任务状态.-1：无效；0：待开始；1：执行中；2：执行异常；3：执行完成',
  `uniqueField` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一键',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `metaBranchFieldUnique` (`metaId`,`jobBranch`,`uniqueField`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `explain_job`
--

LOCK TABLES `explain_job` WRITE;
/*!40000 ALTER TABLE `explain_job` DISABLE KEYS */;
/*!40000 ALTER TABLE `explain_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `explain_meta`
--

DROP TABLE IF EXISTS `explain_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `explain_meta` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `repo` varchar(200) NOT NULL DEFAULT '' COMMENT '仓库',
  `scanPath` text NOT NULL COMMENT '扫描路径',
  `notifyEmails` text NOT NULL COMMENT '通知人列表',
  `cdlAppId` varchar(20) DEFAULT NULL COMMENT 'cdl的appId',
  `cdlGroup` varchar(100) DEFAULT NULL COMMENT '关联的cdl的group',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `explain_meta`
--

LOCK TABLES `explain_meta` WRITE;
/*!40000 ALTER TABLE `explain_meta` DISABLE KEYS */;
/*!40000 ALTER TABLE `explain_meta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gitlab_webhooks`
--

DROP TABLE IF EXISTS `gitlab_webhooks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gitlab_webhooks` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `commitBefore` varchar(100) DEFAULT NULL,
  `commitAfter` varchar(100) DEFAULT NULL,
  `ref` varchar(200) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  `projectName` varchar(100) DEFAULT NULL,
  `projectDescription` varchar(255) DEFAULT NULL,
  `repositoryName` varchar(100) DEFAULT NULL,
  `repositoryUrl` varchar(100) DEFAULT NULL,
  `repositoryDescription` varchar(255) DEFAULT NULL,
  `repositoryHomepage` varchar(200) DEFAULT NULL,
  `totalCommitsCount` int(11) DEFAULT NULL,
  `webHooksType` int(1) DEFAULT '0',
  `triggerBuild` int(11) NOT NULL DEFAULT '0' COMMENT '触发构建',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gitlab_webhooks`
--

LOCK TABLES `gitlab_webhooks` WRITE;
/*!40000 ALTER TABLE `gitlab_webhooks` DISABLE KEYS */;
/*!40000 ALTER TABLE `gitlab_webhooks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gitlab_webhooks_commits`
--

DROP TABLE IF EXISTS `gitlab_webhooks_commits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gitlab_webhooks_commits` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `webHooksId` bigint(20) NOT NULL,
  `commitsId` varchar(100) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `timestamp` varchar(100) DEFAULT NULL,
  `url` varchar(300) DEFAULT NULL,
  `autherName` varchar(50) DEFAULT NULL,
  `autherEmail` varchar(50) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gitlab_webhooks_commits`
--

LOCK TABLES `gitlab_webhooks_commits` WRITE;
/*!40000 ALTER TABLE `gitlab_webhooks_commits` DISABLE KEYS */;
/*!40000 ALTER TABLE `gitlab_webhooks_commits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_job_build_artifacts`
--

DROP TABLE IF EXISTS `jenkins_job_build_artifacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_job_build_artifacts` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `jobBuildsId` bigint(20) DEFAULT NULL,
  `artifactsName` varchar(100) DEFAULT NULL,
  `archiveUrl` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `jobBuildsId` (`jobBuildsId`,`artifactsName`,`archiveUrl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_job_build_artifacts`
--

LOCK TABLES `jenkins_job_build_artifacts` WRITE;
/*!40000 ALTER TABLE `jenkins_job_build_artifacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_job_build_artifacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_job_builds`
--

DROP TABLE IF EXISTS `jenkins_job_builds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_job_builds` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `buildType` int(1) DEFAULT NULL,
  `jobId` bigint(20) DEFAULT NULL COMMENT 'jenkins_jobs id',
  `jobName` varchar(200) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `webHookId` bigint(20) DEFAULT NULL COMMENT 'gitlab_webHooks id',
  `buildNumber` int(11) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_job_builds`
--

LOCK TABLES `jenkins_job_builds` WRITE;
/*!40000 ALTER TABLE `jenkins_job_builds` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_job_builds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_job_builds_parameter`
--

DROP TABLE IF EXISTS `jenkins_job_builds_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_job_builds_parameter` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `buildsId` bigint(20) NOT NULL,
  `paramName` varchar(100) NOT NULL DEFAULT '',
  `paramValue` varchar(200) NOT NULL DEFAULT '',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `buildsId` (`buildsId`,`paramName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_job_builds_parameter`
--

LOCK TABLES `jenkins_job_builds_parameter` WRITE;
/*!40000 ALTER TABLE `jenkins_job_builds_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_job_builds_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_job_note`
--

DROP TABLE IF EXISTS `jenkins_job_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_job_note` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `jobName` varchar(200) DEFAULT NULL,
  `jobUrl` varchar(200) DEFAULT NULL,
  `buildFullUrl` varchar(255) DEFAULT NULL,
  `buildNumber` int(11) DEFAULT NULL,
  `buildPhase` varchar(50) DEFAULT NULL,
  `buildStatus` varchar(50) DEFAULT NULL,
  `buildUrl` varchar(255) DEFAULT NULL,
  `scmUrl` varchar(255) DEFAULT NULL,
  `scmBranch` varchar(100) DEFAULT NULL,
  `scmCommit` varchar(100) DEFAULT NULL,
  `notice` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'dingtalk通知',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `webHook` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `jobName` (`jobName`,`buildNumber`,`buildPhase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_job_note`
--

LOCK TABLES `jenkins_job_note` WRITE;
/*!40000 ALTER TABLE `jenkins_job_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_job_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_job_parameter`
--

DROP TABLE IF EXISTS `jenkins_job_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_job_parameter` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `jobId` bigint(20) NOT NULL,
  `paramName` varchar(100) NOT NULL DEFAULT '',
  `paramValue` varchar(255) NOT NULL DEFAULT '',
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `jobId` (`jobId`,`paramName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_job_parameter`
--

LOCK TABLES `jenkins_job_parameter` WRITE;
/*!40000 ALTER TABLE `jenkins_job_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_job_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_job_user`
--

DROP TABLE IF EXISTS `jenkins_job_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_job_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `jobId` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  `userType` int(11) NOT NULL COMMENT '0 邮件通知用户',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_job_user`
--

LOCK TABLES `jenkins_job_user` WRITE;
/*!40000 ALTER TABLE `jenkins_job_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_job_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_jobs`
--

DROP TABLE IF EXISTS `jenkins_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_jobs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `jobName` varchar(100) NOT NULL DEFAULT '' COMMENT '完整名称',
  `content` varchar(255) DEFAULT NULL,
  `jobEnvType` int(1) DEFAULT NULL,
  `repositoryUrl` varchar(255) DEFAULT NULL,
  `buildType` int(1) DEFAULT NULL,
  `created` tinyint(1) DEFAULT '0' COMMENT '是否创建job',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `jobName` (`jobName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_jobs`
--

LOCK TABLES `jenkins_jobs` WRITE;
/*!40000 ALTER TABLE `jenkins_jobs` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_projects`
--

DROP TABLE IF EXISTS `jenkins_projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_projects` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `projectName` varchar(255) NOT NULL DEFAULT '',
  `content` varchar(255) DEFAULT NULL,
  `repositoryUrl` varchar(255) NOT NULL DEFAULT '',
  `buildType` int(1) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `projectName` (`projectName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_projects`
--

LOCK TABLES `jenkins_projects` WRITE;
/*!40000 ALTER TABLE `jenkins_projects` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_projects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_projects_baseparameter`
--

DROP TABLE IF EXISTS `jenkins_projects_baseparameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_projects_baseparameter` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `projectId` bigint(20) NOT NULL,
  `paramName` varchar(100) NOT NULL DEFAULT '',
  `paramValue` varchar(200) DEFAULT '',
  `paramType` int(1) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `projectId` (`projectId`,`paramName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_projects_baseparameter`
--

LOCK TABLES `jenkins_projects_baseparameter` WRITE;
/*!40000 ALTER TABLE `jenkins_projects_baseparameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_projects_baseparameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_projects_env`
--

DROP TABLE IF EXISTS `jenkins_projects_env`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_projects_env` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `projectId` bigint(20) NOT NULL,
  `envType` int(11) NOT NULL,
  `jobsId` bigint(20) NOT NULL DEFAULT '0',
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `projectId` (`projectId`,`envType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_projects_env`
--

LOCK TABLES `jenkins_projects_env` WRITE;
/*!40000 ALTER TABLE `jenkins_projects_env` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_projects_env` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins_projects_env_parameter`
--

DROP TABLE IF EXISTS `jenkins_projects_env_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins_projects_env_parameter` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `envId` bigint(20) NOT NULL,
  `paramName` varchar(100) NOT NULL DEFAULT '',
  `paramValue` varchar(200) DEFAULT '',
  `paramType` int(1) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `envId` (`envId`,`paramName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins_projects_env_parameter`
--

LOCK TABLES `jenkins_projects_env_parameter` WRITE;
/*!40000 ALTER TABLE `jenkins_projects_env_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `jenkins_projects_env_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `keybox_application_key`
--

DROP TABLE IF EXISTS `keybox_application_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `keybox_application_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `publicKey` varchar(4096) NOT NULL,
  `privateKey` varchar(4096) NOT NULL,
  `passphrase` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keybox_application_key`
--

LOCK TABLES `keybox_application_key` WRITE;
/*!40000 ALTER TABLE `keybox_application_key` DISABLE KEYS */;
INSERT INTO `keybox_application_key` VALUES (2,'ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAzuswWPArXG6aShTC3ax63+pYOgYYe51YeICqBzOmcAv8MXWDwIQR/xlpzLQVlW198RKiOnvAt/8YWIJ261XnxhGM/zNpKM0ZWBRCNqsAC6XKJbPCsY9SQASaKAmhB9m8IQzQvKOd0MxVmZ8FGDQBKgYEtXqaUI3ypcc79A0bY53CJJI3FEZYZ8uVwxDy1B4fLQAzBaNxqxmVBtGpP+GRHKzc6RkUoiNvBWY+mVUudBe+auRR/gZ3OdSbuoXMJPTfnjqEvMU/QEpzMF1+2Abu+X+y/l+0zJ+b2exCff5f1Fx5s0NTGleqBt0SLul0+Naha5fcdnpQWBrCe26fkKycYw== manage@cmdb.new','Yzb0133WhYWxqN1JG9MRuoJCzuXVrIZ1Et+UZZabG5UMS0DblpUqb0BQRaR6EvYsunMHp61gnOLVFoKoGOkG9ksxCd+HwWgIDEGdBxHc9UcN3wSUInavGXGtc3PFyTmo2+0siWpw19Dqqlkm9j+H7S8tmipQ52liH9y2br0PNOU2G8AS8t6+nvcLnJ/e0OP2n0SiG/4UWtrs/tU1/aEgx5eXjyZzjwgfCgq9PUr/tBHGri500LtQJtiRwoWgZAtmEVlj0btRLwjxUETdv2kewCVseWcpF4DB+YXA4FFLLlukbba1q1jvQZ73kcK/Y1khGEZXSIdRqqcbYcTKa18lvg4nWDmpB0uSxQbcgiJn1e3qN/6XfeyOd4596Jkcef97IC2X23OW0fPr2HyWPAGcVQHp6qRDrUy4Jj4WYw8Z6qV33f2GWe46AdzRPQW5UJB0xucCE8jGC/FOCxX36J3ncGv4tJypWjrg6IcYJo/vq2VmzV6mIzbfWLKJ73wZNIfDkS54+gz5heodHNdYBEdc7WJ8/tsjWxeTksyv1xQjlz2MRB9+3rm//bS8/+VIWOsahjr//ngdt206KAasKC7J0vBlce4SqicBxexfEYWIduqciVeaj31FJpqS602qO617fTy4FIRexk+a5x7hPnIiYf4ItC4GBiTf26G/uP225t0YPmr4Eeu/i1KwYmAox4Ha9Q3V/lWCavP0RUQXlX/6GSFJiNKfBffbAtF4DhK0RWqHVkbZnKumbAOTVK284zAv45UoIamIAfuKsFrlwwJvDST2dvb+N7TlenfnGBEh+J45VNGmj2ytkjHXp2ELLH+gmy3X3svbPo25czUn6wDBjYkopsX+XfrQDzMJb0l+cTtw1Tu2haTtafbJvRgaaoJzpu1DejUtRJEG59nBbxyXBXWDAj5RqfTTeIq5PIvCQu2yEtgPA9kJp8wn8pijlyMmCJa6uNvTh4GcZ0NOKBZE7V/chEIcF843WyQjboxA1jE4SyB/GlplGCjcZAnQ4ktZjHvWaNrYP4W5mZ+Ni/7GCx9Hz7W8qm13GI904qPgaYkR96p3IpPqhZPvEM4HBxvw//mupo2i3O7ReHHSrvN5b5hgP5IFIJJqhGIHOD+TDUhOkfq5JG7hgxqhqoJ5jaFiDydfgoBtdx9yB08MKzTIoMHRGZXzc49I0fLPfjkuOKq1E6iVDmYnjE9y6+3wJgS1J1JZ4fVZWG2OxFSNDc7jbxKRnDL/JpyghdNwQgR0z3GRiOk7CnW87It93641fXDw6pCcD273y6BRojO+kVdcmDRpCZT3lkgCdPaqaMronUSKDKFOJiOaYpqYQOlfq6LP/HfYywXWbIFhOHN9O9QExwwtX5uRVGOsqzUd1KXlrf7hA3kjk/6ViR+1PrLklxk0WH7VteozcH5rt1AtOQ97uF1E8xmvJFj+JPv9+s3GzFRxVeJjEES6hbacs+JHUh+vhxjGQ8HkoY+ifhOWEMbsUEzHL2w7iD1mzVxvJUz+NkZas3Y4v1KVdv5wSKwYF7nfg6zFKN6d6a/ABOfdHusLiy3qS0EBzm2cCQEZmB8Z9qT+Pz5fWzG/yFXO6O+F6k3gLMGsVmELwMm/C2+ESQ6zWYnazwFgbtsIpAlGuIziiNtY2nmzrkhtBjAtHs8uffw1RJW9L8ncMPbBKUUaVKRLcHz61BGG1S0AmEnrOlCojkVQB76kUEuH8t6cy2FrAJmd97SA9pEtbsB2PLIiNE76f9Ie2qIww0/h67jgZ/z09mLVvP4jfU0v76hoPLDJQQ5nTiEe7hjzl6xOUUv3kv3H2MtE5TIRoQZB1eWpSekPxquehbHL/f5OgvA+niNlSrF43dixm8xWYsUhG48K6WRg90EKMF2SxtPLzWoOgvWQV+YcB+iMGNx2BH02FhMCJ5pe7/3/mjxU8C7eGmLgTErSKBY3nzF7lCtVWlGQok8AMRcl4vmbaAxtbevAw/q6qt2OncDLxkGo3Mdq+wRsw9WvMiy1YAXgeKXqxJ4LrG+R9cfNdVN1GPHNQG+6anyO6oOA5N1fStZ8jHk6griLFGp3F8OAjpWiy0Ms9uloMa6Q9iqEVUKJZsti/CahrZkd+gjhaeKeVEzhOBgzZJw4h1G78MyFRXEHIQfloCf9vL+ayH2/q41PBVM9+RHaWY/whF+/wyBhfIMfFsT6d8eqGiwRW8GaMv1shyA4n7khDcosZjcfsGKqZB/3CE284Hd2S/YQ',NULL);
/*!40000 ALTER TABLE `keybox_application_key` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `keybox_login_status`
--

DROP TABLE IF EXISTS `keybox_login_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `keybox_login_status` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `serverId` bigint(20) DEFAULT NULL,
  `loginType` int(1) NOT NULL DEFAULT '0' COMMENT '登陆类型0:web;1:终端',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keybox_login_status`
--

LOCK TABLES `keybox_login_status` WRITE;
/*!40000 ALTER TABLE `keybox_login_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `keybox_login_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `keybox_user_server`
--

DROP TABLE IF EXISTS `keybox_user_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `keybox_user_server` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL DEFAULT '' COMMENT '用户名',
  `serverGroupId` bigint(20) NOT NULL COMMENT '关联服务器组id',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_groupId_unique` (`username`,`serverGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keybox_user_server`
--

LOCK TABLES `keybox_user_server` WRITE;
/*!40000 ALTER TABLE `keybox_user_server` DISABLE KEYS */;
INSERT INTO `keybox_user_server` VALUES (1,'admin',1,'2018-06-08 01:50:24',NULL);
/*!40000 ALTER TABLE `keybox_user_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_cleanup_configuration`
--

DROP TABLE IF EXISTS `log_cleanup_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_cleanup_configuration` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `envTypeName` varchar(50) DEFAULT NULL,
  `envType` int(11) DEFAULT NULL,
  `history` int(11) DEFAULT NULL,
  `min` int(11) DEFAULT NULL,
  `max` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_cleanup_configuration`
--

LOCK TABLES `log_cleanup_configuration` WRITE;
/*!40000 ALTER TABLE `log_cleanup_configuration` DISABLE KEYS */;
INSERT INTO `log_cleanup_configuration` VALUES (1,'production',4,7,3,30),(2,'back',6,7,3,30),(3,'gray',3,7,3,20),(4,'daily',2,7,3,10),(5,'test',5,3,1,15),(6,'dev',1,3,1,7),(7,'default',0,7,3,20);
/*!40000 ALTER TABLE `log_cleanup_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_cleanup_property`
--

DROP TABLE IF EXISTS `log_cleanup_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_cleanup_property` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `serverGroupId` bigint(20) DEFAULT NULL,
  `groupName` varchar(200) DEFAULT NULL,
  `history` float DEFAULT NULL COMMENT '日志档案保留天数',
  `envType` int(11) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `cleanupTime` timestamp NULL DEFAULT NULL,
  `cleanupResult` tinyint(1) DEFAULT NULL COMMENT '清理结果',
  `serverId` bigint(20) DEFAULT NULL,
  `serverName` varchar(100) DEFAULT NULL,
  `serialNumber` varchar(100) DEFAULT NULL,
  `diskRate` int(11) DEFAULT NULL COMMENT '磁控空间使用率',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间用于判断更新周期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_cleanup_property`
--

LOCK TABLES `log_cleanup_property` WRITE;
/*!40000 ALTER TABLE `log_cleanup_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_cleanup_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_histograms`
--

DROP TABLE IF EXISTS `log_histograms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_histograms` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `logServiceId` bigint(20) NOT NULL,
  `timeFrom` int(11) DEFAULT NULL,
  `timeTo` int(11) DEFAULT NULL,
  `totalCount` int(11) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_histograms`
--

LOCK TABLES `log_histograms` WRITE;
/*!40000 ALTER TABLE `log_histograms` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_histograms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_service`
--

DROP TABLE IF EXISTS `log_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_service` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `project` varchar(100) DEFAULT NULL,
  `logstore` varchar(100) DEFAULT NULL,
  `topic` varchar(100) DEFAULT NULL,
  `query` varchar(300) DEFAULT NULL COMMENT '查询条件',
  `timeFrom` int(11) DEFAULT NULL COMMENT '日志开始时间戳',
  `timeTo` int(11) DEFAULT NULL COMMENT '日志结束时间戳',
  `totalCount` int(11) DEFAULT NULL COMMENT '日志总条数',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_service`
--

LOCK TABLES `log_service` WRITE;
/*!40000 ALTER TABLE `log_service` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_service_cfg`
--

DROP TABLE IF EXISTS `log_service_cfg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_service_cfg` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `serverName` varchar(200) NOT NULL DEFAULT '' COMMENT '虚拟主机',
  `content` varchar(200) DEFAULT NULL,
  `project` varchar(100) NOT NULL DEFAULT '',
  `logstore` varchar(100) NOT NULL DEFAULT '',
  `topic` varchar(100) NOT NULL DEFAULT '',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_service_cfg`
--

LOCK TABLES `log_service_cfg` WRITE;
/*!40000 ALTER TABLE `log_service_cfg` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_service_cfg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_service_path`
--

DROP TABLE IF EXISTS `log_service_path`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_service_path` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `serverGroupId` bigint(20) NOT NULL,
  `logDir` varchar(200) DEFAULT NULL,
  `tagPath` varchar(250) NOT NULL DEFAULT '' COMMENT '日志完整路径',
  `searchCnt` int(11) NOT NULL DEFAULT '0' COMMENT '日志查询次数',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serverGroupId` (`serverGroupId`,`tagPath`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_service_path`
--

LOCK TABLES `log_service_path` WRITE;
/*!40000 ALTER TABLE `log_service_path` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_service_path` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_service_servergroup_cfg`
--

DROP TABLE IF EXISTS `log_service_servergroup_cfg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_service_servergroup_cfg` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `serverGroupId` bigint(20) NOT NULL,
  `serverGroupName` varchar(100) DEFAULT NULL,
  `project` varchar(100) NOT NULL DEFAULT '',
  `logstore` varchar(100) NOT NULL DEFAULT '',
  `topic` varchar(100) NOT NULL DEFAULT '',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serverGroupId` (`serverGroupId`,`serverGroupName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_service_servergroup_cfg`
--

LOCK TABLES `log_service_servergroup_cfg` WRITE;
/*!40000 ALTER TABLE `log_service_servergroup_cfg` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_service_servergroup_cfg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_auth_resource_group`
--

DROP TABLE IF EXISTS `new_auth_resource_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_auth_resource_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupId` bigint(20) DEFAULT NULL COMMENT '组id',
  `resourceId` bigint(20) DEFAULT NULL COMMENT '资源编号',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_resource_unique` (`groupId`,`resourceId`)
) ENGINE=InnoDB AUTO_INCREMENT=510 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_resource_group`
--

LOCK TABLES `new_auth_resource_group` WRITE;
/*!40000 ALTER TABLE `new_auth_resource_group` DISABLE KEYS */;
INSERT INTO `new_auth_resource_group` VALUES (14,1,1,'2016-09-22 02:45:41',NULL),(36,5,29,'2016-09-22 12:09:36',NULL),(37,5,30,'2016-09-22 12:10:04',NULL),(38,5,31,'2016-09-22 12:10:23',NULL),(39,6,28,'2016-09-22 12:12:10',NULL),(40,6,27,'2016-09-22 12:12:22',NULL),(41,6,22,'2016-09-22 12:12:35',NULL),(42,6,11,'2016-09-22 12:14:40',NULL),(43,6,12,'2016-09-22 12:14:49',NULL),(44,6,13,'2016-09-22 12:14:54',NULL),(45,6,14,'2016-09-22 12:15:31',NULL),(46,6,15,'2016-09-22 12:15:37',NULL),(47,6,16,'2016-09-22 12:15:42',NULL),(48,6,17,'2016-09-22 12:16:07',NULL),(49,6,18,'2016-09-22 12:16:12',NULL),(50,6,19,'2016-09-22 12:16:16',NULL),(51,6,20,'2016-09-22 12:16:21',NULL),(52,6,21,'2016-09-22 12:16:28',NULL),(53,6,23,'2016-09-22 12:16:37',NULL),(54,6,24,'2016-09-22 12:16:42',NULL),(55,6,25,'2016-09-22 12:16:47',NULL),(56,6,26,'2016-09-22 12:16:51',NULL),(57,7,32,'2016-09-22 12:17:51',NULL),(58,7,33,'2016-09-22 12:23:33',NULL),(59,7,34,'2016-09-22 12:24:56',NULL),(60,7,35,'2016-09-22 12:25:28',NULL),(61,7,36,'2016-09-22 12:25:50',NULL),(62,8,37,'2016-09-22 12:28:28',NULL),(63,8,38,'2016-09-22 12:28:49',NULL),(64,8,39,'2016-09-22 12:29:19',NULL),(65,9,40,'2016-09-22 12:29:41',NULL),(66,9,41,'2016-09-22 12:30:07',NULL),(67,9,42,'2016-09-22 12:30:37',NULL),(68,9,43,'2016-09-22 12:31:05',NULL),(69,9,44,'2016-09-22 12:32:28',NULL),(70,0,45,'2016-09-23 02:32:53',NULL),(71,6,46,'2016-09-23 03:32:32',NULL),(72,5,47,'2016-09-23 09:33:04',NULL),(73,6,48,'2016-09-29 06:28:29',NULL),(74,1,49,'2016-10-08 06:56:51',NULL),(75,10,50,'2016-10-08 07:28:33',NULL),(76,10,51,'2016-10-08 07:29:01',NULL),(77,10,52,'2016-10-08 07:29:21',NULL),(83,11,58,'2016-10-10 11:09:05',NULL),(84,11,59,'2016-10-11 08:22:59',NULL),(85,11,60,'2016-10-11 08:42:21',NULL),(86,11,61,'2016-10-12 07:28:59',NULL),(97,9,71,'2016-10-25 03:20:45',NULL),(107,1,54,'2016-10-27 08:07:53',NULL),(108,11,54,'2016-10-27 08:07:53',NULL),(111,1,10,'2016-10-27 08:08:32',NULL),(112,6,10,'2016-10-27 08:08:32',NULL),(113,1,9,'2016-10-27 08:08:39',NULL),(114,6,9,'2016-10-27 08:08:39',NULL),(115,1,8,'2016-10-27 08:08:45',NULL),(116,6,8,'2016-10-27 08:08:45',NULL),(117,1,7,'2016-10-27 08:09:07',NULL),(118,5,7,'2016-10-27 08:09:07',NULL),(119,1,4,'2016-10-27 08:09:15',NULL),(120,7,4,'2016-10-27 08:09:15',NULL),(121,1,3,'2016-10-27 08:09:23',NULL),(122,8,3,'2016-10-27 08:09:23',NULL),(123,1,2,'2016-10-27 08:09:31',NULL),(124,9,2,'2016-10-27 08:09:31',NULL),(125,1,53,'2016-10-27 08:25:33',NULL),(126,13,53,'2016-10-27 08:25:33',NULL),(127,13,55,'2016-10-27 08:25:52',NULL),(128,13,56,'2016-10-27 08:27:18',NULL),(129,13,57,'2016-10-27 08:27:28',NULL),(130,12,69,'2016-10-27 08:33:09',NULL),(131,12,68,'2016-10-27 09:31:49',NULL),(132,12,67,'2016-10-27 09:32:03',NULL),(133,1,63,'2016-10-27 09:32:19',NULL),(134,12,63,'2016-10-27 09:32:19',NULL),(135,12,66,'2016-10-27 09:32:40',NULL),(136,12,65,'2016-10-27 09:32:49',NULL),(137,12,64,'2016-10-27 09:32:59',NULL),(138,9,73,'2016-10-31 06:59:16',NULL),(140,9,74,'2016-10-31 11:35:39',NULL),(141,9,75,'2016-10-31 11:49:17',NULL),(142,14,76,'2016-11-28 04:58:25',NULL),(143,14,77,'2016-11-28 04:58:44',NULL),(144,14,78,'2016-11-28 04:59:03',NULL),(145,14,79,'2016-11-28 04:59:32',NULL),(146,14,80,'2016-11-28 04:59:51',NULL),(147,14,81,'2016-11-28 05:00:08',NULL),(148,14,82,'2016-11-28 05:00:24',NULL),(149,15,83,'2016-11-28 05:01:03',NULL),(150,14,84,'2016-11-28 05:01:26',NULL),(151,14,85,'2016-11-28 05:01:45',NULL),(152,14,86,'2016-11-28 05:02:12',NULL),(153,1,86,'2016-11-28 05:02:12',NULL),(154,14,87,'2016-11-28 05:02:35',NULL),(155,1,87,'2016-11-28 05:02:35',NULL),(156,15,88,'2016-12-01 07:50:19',NULL),(157,15,89,'2016-12-01 07:50:35',NULL),(158,15,90,'2016-12-01 07:50:52',NULL),(159,1,91,'2016-12-01 07:56:02',NULL),(160,14,92,'2016-12-05 02:11:35',NULL),(161,12,72,'2016-12-22 09:38:27',NULL),(162,12,70,'2016-12-22 09:38:52',NULL),(163,8,93,'2016-12-22 09:40:13',NULL),(164,14,94,'2016-12-29 03:47:41',NULL),(165,10,95,'2016-12-29 09:04:44',NULL),(166,16,96,'2016-12-29 11:14:55',NULL),(167,16,97,'2016-12-29 11:15:12',NULL),(168,16,98,'2016-12-29 11:15:25',NULL),(169,16,99,'2016-12-29 11:15:37',NULL),(170,16,100,'2016-12-29 11:15:50',NULL),(171,16,101,'2016-12-29 11:16:02',NULL),(172,16,102,'2016-12-29 11:16:14',NULL),(173,16,103,'2016-12-29 11:16:29',NULL),(174,16,104,'2016-12-29 11:16:41',NULL),(176,1,106,'2016-12-29 11:17:28',NULL),(177,1,105,'2016-12-29 11:17:47',NULL),(178,9,107,'2017-01-03 02:59:35',NULL),(179,8,108,'2017-01-10 13:20:19',NULL),(180,8,109,'2017-01-10 13:20:34',NULL),(181,8,110,'2017-01-11 03:53:57',NULL),(182,8,111,'2017-01-11 10:37:55',NULL),(183,8,112,'2017-01-11 10:38:18',NULL),(184,8,113,'2017-01-11 10:40:44',NULL),(185,8,114,'2017-01-12 07:41:33',NULL),(186,8,115,'2017-01-12 07:42:22',NULL),(187,8,116,'2017-01-12 07:44:02',NULL),(188,8,117,'2017-01-12 07:44:51',NULL),(189,8,118,'2017-01-12 07:46:07',NULL),(190,8,119,'2017-01-12 07:46:58',NULL),(191,8,120,'2017-01-12 07:47:27',NULL),(192,8,121,'2017-01-12 07:48:04',NULL),(193,8,122,'2017-01-12 07:48:39',NULL),(194,8,123,'2017-01-12 07:49:12',NULL),(195,17,124,'2017-01-18 06:04:45',NULL),(196,17,125,'2017-01-18 06:05:00',NULL),(197,17,126,'2017-01-18 06:05:25',NULL),(198,17,127,'2017-01-18 06:05:41',NULL),(199,17,128,'2017-01-18 06:05:54',NULL),(200,17,129,'2017-01-18 06:06:11',NULL),(201,17,130,'2017-01-18 06:06:22',NULL),(202,1,131,'2017-01-18 06:07:54',NULL),(203,14,132,'2017-01-20 06:17:30',NULL),(204,14,133,'2017-01-20 06:17:41',NULL),(205,17,134,'2017-02-07 03:36:18',NULL),(206,17,135,'2017-02-07 03:36:32',NULL),(207,17,136,'2017-02-07 03:37:30',NULL),(208,17,137,'2017-02-08 06:59:43',NULL),(209,8,138,'2017-02-09 08:18:02',NULL),(210,8,139,'2017-02-09 08:18:18',NULL),(211,8,140,'2017-02-14 06:35:56',NULL),(212,8,141,'2017-02-14 06:36:09',NULL),(214,18,143,'2017-02-17 07:33:31',NULL),(215,18,144,'2017-02-24 05:47:54',NULL),(216,1,145,'2017-02-24 05:48:43',NULL),(217,18,146,'2017-02-24 05:50:00',NULL),(218,18,147,'2017-02-28 07:47:33',NULL),(219,1,148,'2017-02-28 07:47:52',NULL),(220,18,149,'2017-03-02 08:09:00',NULL),(221,15,150,'2017-03-03 02:55:35',NULL),(222,17,151,'2017-03-17 06:00:43',NULL),(223,1,151,'2017-03-17 06:00:43',NULL),(224,9,152,'2017-03-17 06:01:01',NULL),(225,1,142,'2017-03-17 06:01:21',NULL),(226,18,142,'2017-03-17 06:01:21',NULL),(227,1,153,'2017-03-31 11:36:48',NULL),(228,19,154,'2017-03-31 11:38:03',NULL),(229,19,155,'2017-03-31 11:39:10',NULL),(230,19,156,'2017-03-31 11:39:31',NULL),(231,1,157,'2017-04-01 03:38:23',NULL),(232,20,157,'2017-04-01 03:38:23',NULL),(233,20,158,'2017-04-01 03:42:33',NULL),(234,20,159,'2017-04-01 03:42:49',NULL),(235,20,160,'2017-04-01 03:43:13',NULL),(236,20,161,'2017-04-01 03:43:38',NULL),(237,20,162,'2017-04-01 03:44:00',NULL),(238,19,163,'2017-04-01 09:40:30',NULL),(239,19,164,'2017-04-05 02:52:58',NULL),(240,19,165,'2017-04-05 02:53:23',NULL),(242,19,166,'2017-04-05 06:23:37',NULL),(243,14,168,'2017-04-12 07:07:47',NULL),(244,1,169,'2017-04-12 08:20:17',NULL),(246,18,172,'2017-04-13 10:13:48',NULL),(247,19,173,'2017-04-20 03:23:22',NULL),(248,19,174,'2017-04-20 03:24:02',NULL),(254,14,181,'2017-05-08 04:56:53',NULL),(255,14,182,'2017-05-08 04:57:08',NULL),(256,14,183,'2017-05-10 03:50:45',NULL),(257,8,184,'2017-05-24 08:25:19',NULL),(258,21,185,'2017-06-01 01:59:18',NULL),(259,1,186,'2017-06-01 02:00:19',NULL),(260,21,187,'2017-06-01 03:32:40',NULL),(261,21,188,'2017-06-01 03:33:31',NULL),(262,21,189,'2017-06-01 03:33:48',NULL),(263,1,175,'2017-06-06 02:20:27',NULL),(270,19,192,'2017-06-12 05:59:10',NULL),(271,23,193,'2017-06-12 09:57:26',NULL),(272,23,194,'2017-06-13 02:03:01',NULL),(273,23,195,'2017-06-13 05:16:54',NULL),(274,23,196,'2017-06-13 05:17:40',NULL),(276,23,197,'2017-06-13 08:12:18',NULL),(277,1,198,'2017-06-21 08:15:15',NULL),(278,15,199,'2017-06-21 09:00:21',NULL),(279,15,200,'2017-06-21 09:34:05',NULL),(280,15,201,'2017-06-22 10:18:59',NULL),(281,15,202,'2017-06-28 05:45:29',NULL),(282,15,203,'2017-06-28 05:45:53',NULL),(283,15,204,'2017-06-28 07:50:52',NULL),(284,15,205,'2017-06-29 03:26:19',NULL),(285,1,206,'2017-07-11 09:11:45',NULL),(286,24,207,'2017-07-11 10:22:12',NULL),(287,24,208,'2017-07-13 10:38:15',NULL),(288,24,209,'2017-07-13 10:38:29',NULL),(289,24,210,'2017-07-13 10:38:51',NULL),(291,8,211,'2017-08-04 02:01:55',NULL),(292,8,212,'2017-08-07 06:22:37',NULL),(293,8,213,'2017-08-07 07:42:07',NULL),(294,15,214,'2017-08-10 08:13:46',NULL),(295,15,215,'2017-08-10 08:14:09',NULL),(296,15,216,'2017-08-10 08:14:34',NULL),(297,15,217,'2017-08-10 08:34:16',NULL),(298,1,218,'2017-08-18 02:03:06',NULL),(299,15,219,'2017-08-18 02:05:45',NULL),(300,15,220,'2017-08-18 07:38:17',NULL),(301,15,221,'2017-08-18 08:27:07',NULL),(302,15,222,'2017-08-18 08:27:22',NULL),(303,1,223,'2017-08-21 06:25:11',NULL),(304,1,224,'2017-08-21 06:32:40',NULL),(305,15,225,'2017-08-22 03:07:05',NULL),(306,15,226,'2017-08-23 08:36:34',NULL),(307,15,227,'2017-08-24 03:17:34',NULL),(308,15,228,'2017-08-24 09:10:17',NULL),(309,15,229,'2017-08-24 09:10:57',NULL),(310,15,230,'2017-08-24 10:18:59',NULL),(311,1,231,'2017-08-29 02:08:45',NULL),(312,1,232,'2017-08-29 02:09:04',NULL),(313,1,233,'2017-08-29 02:09:20',NULL),(314,26,180,'2017-08-29 03:11:31',NULL),(315,26,179,'2017-08-29 03:11:46',NULL),(317,26,176,'2017-08-29 03:12:01',NULL),(318,26,177,'2017-08-29 03:14:56',NULL),(319,27,191,'2017-08-29 05:58:10',NULL),(320,15,234,'2017-09-05 07:38:02',NULL),(321,15,235,'2017-09-05 07:38:18',NULL),(322,1,236,'2017-09-05 09:18:39',NULL),(323,11,237,'2017-09-05 11:02:14',NULL),(324,11,238,'2017-09-07 03:51:21',NULL),(325,11,239,'2017-09-07 08:40:30',NULL),(326,11,240,'2017-09-07 08:41:22',NULL),(327,11,241,'2017-09-07 08:41:42',NULL),(328,11,242,'2017-09-07 13:36:57',NULL),(329,11,243,'2017-09-08 02:17:48',NULL),(330,11,244,'2017-09-08 07:59:33',NULL),(331,11,245,'2017-09-08 10:40:31',NULL),(332,11,246,'2017-09-08 11:41:34',NULL),(333,11,247,'2017-09-11 10:32:50',NULL),(334,11,248,'2017-09-11 10:33:10',NULL),(335,11,249,'2017-09-13 10:52:52',NULL),(336,1,250,'2017-09-20 02:59:05',NULL),(337,28,251,'2017-09-20 05:51:36',NULL),(338,28,252,'2017-09-20 12:34:54',NULL),(340,28,254,'2017-09-21 07:33:34',NULL),(341,1,255,'2017-09-22 09:39:30',NULL),(342,28,256,'2017-09-25 05:23:15',NULL),(343,28,257,'2017-09-25 07:47:12',NULL),(344,28,258,'2017-09-25 07:47:45',NULL),(346,28,260,'2017-09-25 09:46:19',NULL),(347,28,253,'2017-09-25 10:19:28',NULL),(348,1,261,'2017-09-27 03:52:24',NULL),(349,28,262,'2017-09-27 08:45:23',NULL),(350,28,263,'2017-09-27 08:45:40',NULL),(352,28,264,'2017-09-28 02:13:29',NULL),(353,28,265,'2017-09-28 03:35:32',NULL),(354,28,266,'2017-09-29 07:34:55',NULL),(355,14,267,'2017-09-30 01:59:31',NULL),(356,18,268,'2017-09-30 06:47:23',NULL),(357,8,269,'2017-10-11 03:08:29',NULL),(358,25,270,'2017-10-17 07:59:22',NULL),(359,25,271,'2017-10-17 07:59:35',NULL),(360,14,272,'2017-10-23 02:54:55',NULL),(361,1,273,'2017-10-26 02:38:54',NULL),(362,29,274,'2017-10-26 06:48:01',NULL),(363,29,275,'2017-10-30 03:22:04',NULL),(364,29,276,'2017-10-30 03:22:23',NULL),(365,29,277,'2017-10-31 09:07:03',NULL),(366,29,278,'2017-10-31 09:07:23',NULL),(367,29,279,'2017-10-31 09:07:39',NULL),(368,29,280,'2017-10-31 09:08:06',NULL),(369,29,281,'2017-10-31 09:08:22',NULL),(370,1,282,'2017-11-01 11:34:30',NULL),(372,6,283,'2017-11-02 07:08:53',NULL),(373,31,284,'2017-11-02 08:56:13',NULL),(374,31,285,'2017-11-03 03:52:58',NULL),(375,11,286,'2017-11-06 06:51:40',NULL),(376,29,288,'2017-11-10 02:09:40',NULL),(377,11,290,'2017-11-16 03:06:24',NULL),(378,11,291,'2017-11-16 03:06:46',NULL),(379,11,294,'2017-11-16 03:08:09',NULL),(380,11,295,'2017-11-17 02:10:34',NULL),(381,11,296,'2017-11-17 08:58:35',NULL),(382,11,297,'2017-11-17 08:58:58',NULL),(383,11,298,'2017-11-17 08:59:16',NULL),(385,11,299,'2017-11-17 09:34:03',NULL),(386,11,300,'2017-11-20 09:34:09',NULL),(388,11,301,'2017-11-20 10:32:15',NULL),(389,11,302,'2017-11-20 10:32:46',NULL),(390,11,303,'2017-11-21 06:51:06',NULL),(391,15,304,'2017-11-21 07:57:00',NULL),(392,11,305,'2017-11-23 10:03:03',NULL),(393,9,306,'2017-11-23 11:59:33',NULL),(395,32,307,'2017-12-14 12:27:24',NULL),(396,33,308,'2017-12-14 12:28:49',NULL),(397,1,309,'2017-12-15 06:26:21',NULL),(398,1,310,'2017-12-15 06:27:01',NULL),(399,1,311,'2017-12-15 06:27:29',NULL),(400,1,312,'2017-12-15 06:34:12',NULL),(401,32,313,'2017-12-15 07:09:30',NULL),(402,32,314,'2017-12-15 08:05:36',NULL),(403,32,315,'2017-12-15 08:06:27',NULL),(404,32,316,'2017-12-21 06:51:31',NULL),(405,32,317,'2017-12-21 08:04:38',NULL),(406,32,318,'2017-12-21 08:04:50',NULL),(407,32,319,'2017-12-21 08:54:45',NULL),(408,32,320,'2017-12-21 08:55:06',NULL),(409,32,321,'2017-12-21 08:55:22',NULL),(410,32,322,'2017-12-21 12:13:03',NULL),(412,32,323,'2017-12-22 03:45:35',NULL),(413,32,324,'2017-12-26 12:50:02',NULL),(414,32,325,'2017-12-27 09:35:28',NULL),(415,32,326,'2017-12-27 12:57:24',NULL),(416,32,327,'2017-12-29 09:27:53',NULL),(417,34,328,'2018-01-02 01:36:09',NULL),(418,34,329,'2018-01-02 01:36:37',NULL),(419,1,330,'2018-01-02 03:14:18',NULL),(420,1,331,'2018-01-02 03:14:58',NULL),(421,1,332,'2018-01-02 03:15:18',NULL),(422,32,333,'2018-01-03 01:42:40',NULL),(423,32,334,'2018-01-03 02:23:40',NULL),(424,32,335,'2018-01-03 02:32:46',NULL),(425,32,336,'2018-01-03 07:06:46',NULL),(426,11,62,'2018-01-03 07:58:36',NULL),(427,32,337,'2018-01-08 08:43:43',NULL),(428,32,338,'2018-01-09 01:42:36',NULL),(429,32,339,'2018-01-09 02:33:53',NULL),(430,32,340,'2018-01-09 02:37:15',NULL),(431,32,341,'2018-01-09 03:20:02',NULL),(432,32,342,'2018-01-09 03:20:25',NULL),(433,32,343,'2018-01-09 03:40:09',NULL),(434,32,344,'2018-01-09 06:15:10',NULL),(435,32,345,'2018-01-09 06:15:26',NULL),(436,32,346,'2018-01-09 06:23:34',NULL),(437,32,347,'2018-01-09 07:59:40',NULL),(438,32,348,'2018-01-09 10:41:44',NULL),(439,18,349,'2018-01-17 08:18:51',NULL),(440,18,350,'2018-01-17 08:19:17',NULL),(441,32,351,'2018-01-25 08:03:46',NULL),(442,1,352,'2018-02-08 09:36:29',NULL),(443,18,353,'2018-02-09 07:59:57',NULL),(444,15,354,'2018-03-15 05:53:32',NULL),(445,1,355,'2018-04-10 02:19:51',NULL),(446,15,356,'2018-04-10 02:54:00',NULL),(447,15,357,'2018-04-10 06:20:51',NULL),(448,15,358,'2018-04-10 08:03:48',NULL),(449,15,359,'2018-04-11 07:49:09',NULL),(450,1,360,'2018-04-18 01:44:34',NULL),(451,32,361,'2018-04-24 03:23:01',NULL),(452,11,362,'2018-04-25 08:49:10',NULL),(453,11,363,'2018-04-26 06:45:59',NULL),(454,32,364,'2018-05-07 09:22:31',NULL),(455,8,365,'2018-05-09 03:47:23',NULL),(456,15,366,'2018-05-11 03:44:53',NULL),(457,17,367,'2018-05-11 09:09:16',NULL),(459,11,368,'2018-05-14 10:13:12',NULL),(460,19,369,'2018-05-15 07:16:13',NULL),(461,19,370,'2018-05-17 05:56:42',NULL),(462,19,371,'2018-05-22 06:50:17',NULL),(463,19,372,'2018-05-22 06:53:25',NULL),(464,1,373,'2018-05-22 07:42:13',NULL),(465,19,374,'2018-05-23 02:27:27',NULL),(466,19,375,'2018-05-24 02:12:53',NULL),(467,19,376,'2018-05-24 07:34:25',NULL),(468,9,377,'2018-05-29 06:47:51',NULL),(469,9,378,'2018-05-29 07:33:50',NULL),(470,9,379,'2018-05-29 08:08:12',NULL),(471,9,380,'2018-05-29 08:17:12',NULL),(472,21,381,'2018-05-29 10:48:33',NULL),(473,23,382,'2018-05-30 01:13:36',NULL),(474,21,383,'2018-05-30 02:25:38',NULL),(475,23,384,'2018-05-30 06:09:09',NULL),(476,23,385,'2018-05-30 07:32:45',NULL),(477,23,386,'2018-05-30 07:33:06',NULL),(478,23,387,'2018-05-31 06:26:10',NULL),(479,23,388,'2018-05-31 06:26:34',NULL),(480,23,389,'2018-05-31 07:05:16',NULL),(481,17,390,'2018-05-31 12:10:19',NULL),(482,17,391,'2018-06-01 03:33:57',NULL),(483,17,392,'2018-06-01 03:34:32',NULL),(484,17,393,'2018-06-01 03:34:55',NULL),(485,17,394,'2018-06-01 03:35:15',NULL),(486,17,395,'2018-06-01 10:13:04',NULL),(487,17,396,'2018-06-04 01:43:24',NULL),(488,17,397,'2018-06-04 01:43:58',NULL),(489,17,398,'2018-06-04 03:38:49',NULL),(490,17,399,'2018-06-04 03:39:10',NULL),(491,1,400,'2018-06-04 08:20:30',NULL),(492,16,401,'2018-06-05 05:16:39',NULL),(493,16,402,'2018-06-05 05:53:41',NULL),(494,16,403,'2018-06-05 06:17:40',NULL),(495,16,404,'2018-06-05 07:01:24',NULL),(497,19,405,'2018-06-05 08:23:26',NULL),(498,19,406,'2018-06-05 09:03:12',NULL),(499,1,407,'2018-06-05 10:09:28',NULL),(500,14,408,'2018-06-06 07:15:51',NULL),(501,16,409,'2018-06-06 10:06:09',NULL),(502,19,410,'2018-06-08 06:37:00',NULL),(503,1,411,'2018-06-11 01:52:09',NULL),(504,16,412,'2018-06-11 05:47:55',NULL),(505,16,413,'2018-06-11 06:31:39',NULL),(506,19,414,'2018-06-11 08:09:03',NULL),(507,19,415,'2018-06-11 08:09:35',NULL),(508,16,416,'2018-06-11 10:31:50',NULL),(509,19,417,'2018-06-11 11:07:25',NULL);
/*!40000 ALTER TABLE `new_auth_resource_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_auth_resources`
--

DROP TABLE IF EXISTS `new_auth_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_auth_resources` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `resourceName` varchar(100) NOT NULL DEFAULT '' COMMENT '资源名称',
  `resourceDesc` varchar(200) DEFAULT NULL COMMENT '资源描述',
  `needAuth` tinyint(5) NOT NULL DEFAULT '0' COMMENT '是否需要鉴权。0：需要；1：不需要',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource_unique` (`resourceName`)
) ENGINE=InnoDB AUTO_INCREMENT=418 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_resources`
--

LOCK TABLES `new_auth_resources` WRITE;
/*!40000 ALTER TABLE `new_auth_resources` DISABLE KEYS */;
INSERT INTO `new_auth_resources` VALUES (1,'app.dashboard','首页',0,'2016-09-19 07:31:41','2016-09-22 11:54:51'),(2,'app.servergroup','服务器组管理页面',0,'2016-09-19 07:31:50','2016-09-22 11:54:51'),(3,'app.server','服务器管理页面',0,'2016-09-19 07:31:59','2016-09-22 11:54:51'),(4,'app.ipgroup','ip组管理页面',0,'2016-09-19 07:32:09','2016-09-22 11:54:51'),(7,'app.ip','ip管理页面',0,'2016-09-21 08:14:24','2016-09-22 11:54:51'),(8,'app.resource','资源管理页面',0,'2016-09-22 02:46:20','2016-09-22 11:54:51'),(9,'app.role','角色管理页面',0,'2016-09-22 02:46:41','2016-09-22 11:54:51'),(10,'app.user','用户管理页面',0,'2016-09-22 02:47:02','2016-09-22 11:54:51'),(11,'/login','登录',1,'2016-09-22 11:59:56',NULL),(12,'/check/auth','权限校验',0,'2016-09-22 12:00:31',NULL),(13,'/resource/groups','获取满足要求的资源组数据',0,'2016-09-22 12:00:51',NULL),(14,'/resource/group/save','保存指定的资源组数据',0,'2016-09-22 12:01:14',NULL),(15,'/resource/group/del','删除指定的资源组信息',0,'2016-09-22 12:01:31',NULL),(16,'/resource/query','查询合适条件的资源数据集合',0,'2016-09-22 12:01:44',NULL),(17,'/resource/save','更新或添加资源',0,'2016-09-22 12:01:57',NULL),(18,'/resource/del','删除指定的资源',0,'2016-09-22 12:02:18',NULL),(19,'/role/query','获取角色集合',0,'2016-09-22 12:02:31',NULL),(20,'/role/save','保存角色信息',0,'2016-09-22 12:02:55',NULL),(21,'/role/del','删除指定的角色信息',0,'2016-09-22 12:03:08',NULL),(22,'/role/resource/bind/query','获取指定角色id的绑定资源列表',0,'2016-09-22 12:03:37',NULL),(23,'/role/resource/unbind/query','获取指定角色id的未绑定资源列表',0,'2016-09-22 12:03:51',NULL),(24,'/role/resource/bind','角色资源建立绑定关系',0,'2016-09-22 12:04:04',NULL),(25,'/role/resource/unbind','角色资源解除绑定关系',0,'2016-09-22 12:04:18',NULL),(26,'/user/role','获取用户角色数据',0,'2016-09-22 12:04:31',NULL),(27,'/user/role/bind','用户角色绑定',0,'2016-09-22 12:04:46',NULL),(28,'/user/role/unbind','用户角色解绑',0,'2016-09-22 12:05:00',NULL),(29,'/ip/query','查询指定条件的ip集合信息',0,'2016-09-22 12:09:36',NULL),(30,'/ip/save','保存 or 更新ip信息',0,'2016-09-22 12:10:04',NULL),(31,'/ip/del','删除指定id的ip信息',0,'2016-09-22 12:10:23',NULL),(32,'/ipgroup/query','ip组按条件查询',0,'2016-09-22 12:17:51',NULL),(33,'/ipgroup/save','保存 or 更新IP组信息',0,'2016-09-22 12:23:33',NULL),(34,'/ipgroup/del','删除指定IP组信息',0,'2016-09-22 12:24:56',NULL),(35,'/ipgroup/create','生成ip',0,'2016-09-22 12:25:28',NULL),(36,'/ipgroup/serverGroup/query','查询指定服务器组的ip组集合',0,'2016-09-22 12:25:50',NULL),(37,'/server/page','获取指定条件的服务器列表分页数据',0,'2016-09-22 12:28:28',NULL),(38,'/server/save','保存指定的serveritem',0,'2016-09-22 12:28:49',NULL),(39,'/server/del','删除指定的server',0,'2016-09-22 12:29:19',NULL),(40,'/servergroup/query/page','查询服务器组的分页数据',0,'2016-09-22 12:29:41',NULL),(41,'/servergroup/update','更新服务器组信息',0,'2016-09-22 12:30:07',NULL),(42,'/servergroup/del','删除指定的服务器组信息',0,'2016-09-22 12:30:37',NULL),(43,'/servergroup/ipgroup/add','服务器组绑定指定的ip组',0,'2016-09-22 12:31:05',NULL),(44,'/servergroup/ipgroup/del','服务器组解绑指定的ip组',0,'2016-09-22 12:32:28',NULL),(45,'/dashboard','首页',0,'2016-09-23 02:32:53',NULL),(46,'/logout','登出',0,'2016-09-23 03:32:32',NULL),(47,'/ip/use/check','检查指定段的ip是否已经被使用',0,'2016-09-23 09:33:04',NULL),(48,'lookAllServerGroup','查看所有服务器组数据',0,'2016-09-29 06:28:29',NULL),(49,'app.system','系统管理菜单',0,'2016-10-08 06:56:51',NULL),(50,'/system/save','系统新增 or 更新',0,'2016-10-08 07:28:33',NULL),(51,'/system/del','删除指定的系统',0,'2016-10-08 07:29:01',NULL),(52,'/system/query','模糊查询匹配名称的系统列表',0,'2016-10-08 07:29:21',NULL),(53,'app.todoConfig','工单配置项管理',0,'2016-10-09 08:49:13',NULL),(54,'app.todoDaily','日常工单',0,'2016-10-09 08:49:30',NULL),(55,'/todo/config/query','查询合适条件下的工单配置项分页数据',0,'2016-10-09 09:29:25',NULL),(56,'/todo/config/save','配置项的新增 or 更新',0,'2016-10-09 09:30:00',NULL),(57,'/todo/config/del','删除指定的配置项',0,'2016-10-09 09:30:23',NULL),(58,'/todo/config/children/query','查询指定父id的子item集合',0,'2016-10-10 11:09:05',NULL),(59,'/todo/daily/query','查询日常工单分页数据',0,'2016-10-11 08:22:59',NULL),(60,'/todo/daily/save','日常工单新增 or 更新',0,'2016-10-11 08:42:21',NULL),(61,'/todo/process/query','查询工单待处理分页数据',0,'2016-10-12 07:28:59',NULL),(62,'/todo/query','查询指定工单的数据',1,'2016-10-19 06:39:29','2018-01-03 07:58:36'),(63,'app.property','属性管理',0,'2016-10-21 02:07:16','2016-10-21 02:10:58'),(64,'/config/property/group','属性组分页数据',0,'2016-10-21 02:49:01',NULL),(65,'/config/property/group/save','新增 or 更新属性组',0,'2016-10-21 03:08:37',NULL),(66,'/config/property/group/del','删除指定的属性组',0,'2016-10-21 03:16:53',NULL),(67,'/config/property','属性分页数据',0,'2016-10-21 05:51:23',NULL),(68,'/config/property/save','新增 or 更新属性',0,'2016-10-21 06:02:47',NULL),(69,'/config/property/del','删除指定的属性',0,'2016-10-21 06:03:15',NULL),(70,'/config/propertygroup/save','新增 or 更新属性组数据',0,'2016-10-25 03:00:20','2016-12-22 09:38:52'),(71,'/servergroup/propertygroup/query','查询指定条件的服务器组属性组数据',0,'2016-10-25 03:20:45',NULL),(72,'/config/propertygroup/del','删除指定的属性组数据',0,'2016-10-25 06:35:27','2016-12-22 09:38:27'),(73,'/servergroup/propertygroup/create','生成指定服务器组&属性组的属性配置文件',0,'2016-10-31 06:59:16',NULL),(74,'/servergroup/propertygroup/preview','预览指定服务器组&属性组的属性配置文件',0,'2016-10-31 09:48:34','2016-10-31 11:35:39'),(75,'/servergroup/propertygroup/launch','加载指定服务器组&属性组的本地属性配置文件',0,'2016-10-31 11:49:17',NULL),(76,'/box/user/group/global/create','创建全局配置文件',0,'2016-11-28 04:58:25',NULL),(77,'/box/user/group/create','创建指定用户的配置文件',0,'2016-11-28 04:58:44',NULL),(78,'/box/user/group/del','删除用户服务器组',0,'2016-11-28 04:59:03',NULL),(79,'/box/user/group/save','添加新的堡垒机用户服务器组',0,'2016-11-28 04:59:32',NULL),(80,'/box/user/group','获取堡垒机用户服务器组分页信息',0,'2016-11-28 04:59:51',NULL),(81,'/box/auth/add','堡垒机授权',0,'2016-11-28 05:00:08',NULL),(82,'/box/auth/del','堡垒机删除授权',0,'2016-11-28 05:00:24',NULL),(83,'/users','获取指定条件的用户集合',0,'2016-11-28 05:01:03',NULL),(84,'/box/query','堡垒机分页数据查询',0,'2016-11-28 05:01:26',NULL),(85,'/keybox/ws','堡垒机websocket接口',0,'2016-11-28 05:01:45',NULL),(86,'app.keybox','堡垒机',0,'2016-11-28 05:02:12',NULL),(87,'app.keyboxmanage','堡垒机管理',0,'2016-11-28 05:02:35',NULL),(88,'/user/query','查询指定用户名的用户信息',0,'2016-12-01 07:50:19',NULL),(89,'canEdit','是否可编辑权限',0,'2016-12-01 07:50:35',NULL),(90,'/user/save','更新用户信息',0,'2016-12-01 07:50:52',NULL),(91,'app.userDetail','用户详情菜单',0,'2016-12-01 07:56:02',NULL),(92,'/box/server/query','查询被授权的堡垒机服务器列表',0,'2016-12-05 02:11:35',NULL),(93,'/server/propertygroup/query','查询指定条件的服务器属性组数据',0,'2016-12-22 09:40:13',NULL),(94,'/box/user/group/createAll','堡垒机生成所有用户配置文件',0,'2016-12-29 03:47:41',NULL),(95,'access.system','系统导航',0,'2016-12-29 09:04:44',NULL),(96,'/config/file/launch','预览本地文件',0,'2016-12-29 11:14:55',NULL),(97,'/config/file/invoke','执行命令',0,'2016-12-29 11:15:12',NULL),(98,'/config/file/create','生成本地文件',0,'2016-12-29 11:15:25',NULL),(99,'/config/file/del','删除指定id的文件组信息',0,'2016-12-29 11:15:37',NULL),(100,'/config/file/save','保存 or 更新文件组信息',0,'2016-12-29 11:15:50',NULL),(101,'/config/file/query','获取文件组分页数据',0,'2016-12-29 11:16:02',NULL),(102,'/config/fileGroup/del','删除指定id的文件组信息',0,'2016-12-29 11:16:14',NULL),(103,'/config/fileGroup/query','获取文件组分页数据',0,'2016-12-29 11:16:29',NULL),(104,'/config/fileGroup/save','保存 or 更新文件组信息',0,'2016-12-29 11:16:41',NULL),(105,'app.configFileGroup','配置文件组管理',0,'2016-12-29 11:16:58',NULL),(106,'app.configFile','配置文件管理',0,'2016-12-29 11:17:28',NULL),(107,'/servergroup/propertygroup/save','新增服务器组属性',0,'2017-01-03 02:59:35',NULL),(108,'/server/ecsRefresh','获取最新ECS别表',0,'2017-01-10 13:20:19',NULL),(109,'/server/ecsPage','ecs服务器管理页面',0,'2017-01-10 13:20:34',NULL),(110,'/server/ecsCheck','ecs服务器校验',0,'2017-01-11 03:53:57',NULL),(111,'/server/setStatus','ECS标记删除',0,'2017-01-11 10:37:55',NULL),(112,'/server/delEcs','删除ECS服务器',0,'2017-01-11 10:38:18',NULL),(113,'/server/ecsSave','Ecs操作按钮',0,'2017-01-11 10:40:44',NULL),(114,'/server/vmSave','vm服务器按钮',0,'2017-01-12 07:41:33',NULL),(115,'/server/vmRefresh','获取最新VM列表',0,'2017-01-12 07:42:22',NULL),(116,'/server/vmCheck','vm服务器校验',0,'2017-01-12 07:44:02',NULL),(117,'/server/vmPage','VM服务器管理页面',0,'2017-01-12 07:44:51',NULL),(118,'/server/addVmServer','将VM添加到Server',0,'2017-01-12 07:46:07',NULL),(119,'/server/setDelVm','删除标记VM服务器',0,'2017-01-12 07:46:58',NULL),(120,'/server/vmRename','VM服务器改名',0,'2017-01-12 07:47:27',NULL),(121,'/server/vmReboot','VM服务器重启',0,'2017-01-12 07:48:04',NULL),(122,'/server/vmShutdown','VM服务器关机',0,'2017-01-12 07:48:39',NULL),(123,'/server/delVm','删除VM服务器',0,'2017-01-12 07:49:12',NULL),(124,'/zabbixserver/save','zabbix操作菜单',0,'2017-01-18 06:04:45',NULL),(125,'/zabbixserver/disableMonitor','禁用监控',0,'2017-01-18 06:05:00',NULL),(126,'/zabbixserver/enableMonitor','启用监控',0,'2017-01-18 06:05:25',NULL),(127,'/zabbixserver/delMonitor','删除监控',0,'2017-01-18 06:05:41',NULL),(128,'/zabbixserver/addMonitor','添加监控',0,'2017-01-18 06:05:54',NULL),(129,'/zabbixserver/refresh','更新zabbix数据',0,'2017-01-18 06:06:11',NULL),(130,'/zabbixserver/page','监控管理页面',0,'2017-01-18 06:06:22',NULL),(131,'app.zabbixserver','服务器监控菜单',0,'2017-01-18 06:07:54',NULL),(132,'/servergroup/keybox/page','服务器组授权管理',0,'2017-01-20 06:17:30',NULL),(133,'/box/checkUser','校验用户数据并清理',0,'2017-01-20 06:17:41',NULL),(134,'/zabbixserver/user/auth/del','删除zabbix账户授权',0,'2017-02-07 03:36:18',NULL),(135,'/zabbixserver/user/auth/add','添加zabbix账户授权',0,'2017-02-07 03:36:32',NULL),(136,'/zabbixserver/user/sync','同步zabbix用户',0,'2017-02-07 03:37:30',NULL),(137,'/zabbixserver/ci','zabbix持续集成接口',1,'2017-02-08 06:59:43',NULL),(138,'/server/ecsStatistics','ecs服务器统计',0,'2017-02-09 08:18:02',NULL),(139,'/server/vmStatistics','vm服务器统计',0,'2017-02-09 08:18:18',NULL),(140,'/server/psStatistics','物理服务器统计',0,'2017-02-14 06:35:56',NULL),(141,'/server/psPage','物理服务器详情',0,'2017-02-14 06:36:09',NULL),(142,'app.statistics','统计菜单',0,'2017-02-17 07:32:59',NULL),(143,'/statistics/deploy/page','部署统计详情页',0,'2017-02-17 07:33:31',NULL),(144,'/statistics/servercost/statistics','服务器成本统计',0,'2017-02-24 05:47:54',NULL),(145,'app.serverstatistics','服务器成本统计',0,'2017-02-24 05:48:43',NULL),(146,'/statistics/servercost/page','服务器统计',0,'2017-02-24 05:50:00',NULL),(147,'/statistics/serverperf/page','服务器性能统计',0,'2017-02-28 07:47:33',NULL),(148,'app.serverperfstatistics','服务器性能统计菜单',0,'2017-02-28 07:47:52',NULL),(149,'/statistics/serverperf/statistics','服务器性能统计',0,'2017-03-02 08:09:00',NULL),(150,'/user/resetToken','重置token',0,'2017-03-03 02:55:35',NULL),(151,'app.servermonitor','监控查看',0,'2017-03-17 06:00:43',NULL),(152,'/servergroup/servers','查询指定服务器组的服务器集合',0,'2017-03-17 06:01:01',NULL),(153,'app.logcleanup','日志弹性清理菜单',0,'2017-03-31 11:36:48',NULL),(154,'/task/logcleanup/page','日志弹性清理详情页',0,'2017-03-31 11:38:03',NULL),(155,'/task/logcleanup/cleanup','日志弹性清理',0,'2017-03-31 11:39:10',NULL),(156,'/task/logcleanup/refresh','日志弹性清理数据同步',0,'2017-03-31 11:39:31',NULL),(157,'app.explain','订阅审核菜单',0,'2017-04-01 03:38:23',NULL),(158,'/explain/add','添加审核订阅',0,'2017-04-01 03:42:33',NULL),(159,'/explain/query','查询指定条件下的审核订阅',0,'2017-04-01 03:42:49',NULL),(160,'/explain/del','删除指定id的审核订阅',0,'2017-04-01 03:43:13',NULL),(161,'/explain/repo/query','查询指定条件下的仓库列表',0,'2017-04-01 03:43:38',NULL),(162,'/explain/invoke','执行扫描',0,'2017-04-01 03:44:00',NULL),(163,'/task/logcleanup/setEnabled','日志弹性清理（禁用|启用）',0,'2017-04-01 09:40:30',NULL),(164,'/task/logcleanup/refreshDiskRate','刷新磁盘使用率',0,'2017-04-05 02:52:58',NULL),(165,'/task/logcleanup/addHistory','增加日志保留天数',0,'2017-04-05 02:53:23',NULL),(166,'/task/logcleanup/subtractHistory','减少日志保留天数',0,'2017-04-05 02:53:40','2017-04-05 06:23:37'),(168,'/box/group/user/query','堡垒机服务器组-用户管理查询',0,'2017-04-12 07:07:47',NULL),(169,'app.servertask','服务器常用任务菜单',0,'2017-04-12 08:20:17',NULL),(172,'/statistics/server/deploy/version/page','服务器部署版本详情页',0,'2017-04-13 10:13:48',NULL),(173,'/task/servertask/initializationSystem','服务器常用任务-初始化系统',0,'2017-04-20 03:23:22',NULL),(174,'/task/servertask/save','服务器常用任务-操作按钮',0,'2017-04-20 03:24:02',NULL),(175,'app.serverTemplate','服务器模版管理菜单',0,'2017-04-20 09:11:13','2017-06-06 02:20:27'),(176,'/server/template/ecs/page','ecs模版分页详情',0,'2017-04-20 09:20:45','2017-06-06 02:22:14'),(177,'/ecsServer/template/save','ecs模版按钮',0,'2017-04-21 02:53:08','2017-08-29 03:14:56'),(179,'/server/template/ecs/expansion','用ecs模版扩容服务器',0,'2017-04-25 07:51:17','2017-06-06 02:21:53'),(180,'/server/template/ecs/create','用ecs模版新建服务器',0,'2017-04-25 07:51:33','2017-06-06 02:21:17'),(181,'/box/key/query','堡垒机key查询',0,'2017-05-08 04:56:53',NULL),(182,'/box/key/save','更新堡垒机key',0,'2017-05-08 04:57:08',NULL),(183,'/box/user/save','新建用户',0,'2017-05-10 03:50:45',NULL),(184,'/server/ecsAllocateIp','ecs分配公网ip',0,'2017-05-24 08:25:19',NULL),(185,'/config/center/query','配置中心详情页',0,'2017-06-01 01:59:18',NULL),(186,'app.configCenter','配置中心菜单',0,'2017-06-01 02:00:19',NULL),(187,'/config/center/refreshCache','更新配置中心配置组缓存',0,'2017-06-01 03:32:40',NULL),(188,'/config/center/save','保存配置中配置项',0,'2017-06-01 03:33:31',NULL),(189,'/config/center/del','删除配置中心配置项',0,'2017-06-01 03:33:48',NULL),(191,'/server/template/vm/page','vm模版分页详情',0,'2017-06-06 02:25:50',NULL),(192,'/task/logcleanup/save','修改日志清理配置',0,'2017-06-12 05:59:10',NULL),(193,'/aliyun/image','查询阿里云镜像列表',0,'2017-06-12 09:57:26',NULL),(194,'/aliyun/network','获取阿里云网络类型',0,'2017-06-13 02:03:01',NULL),(195,'/aliyun/vpc','获取vpc列表',0,'2017-06-13 05:16:54',NULL),(196,'/aliyun/vswitch','获取虚拟交换机列表',0,'2017-06-13 05:17:40',NULL),(197,'/aliyun/securityGroup','获取安全组列表',0,'2017-06-13 05:18:06','2017-06-13 08:12:18'),(198,'app.users','用户管理',0,'2017-06-21 08:15:15',NULL),(199,'/usersLeave/page','离职用户详情页',0,'2017-06-21 09:00:21',NULL),(200,'/usersLeave/del','删除离职用户数据',0,'2017-06-21 09:34:05',NULL),(201,'/cmdb/users','cmdb用户管理',0,'2017-06-22 10:18:59',NULL),(202,'/cmdb/ldapGroup/remove','ldapgroup中移除该用户',0,'2017-06-28 05:45:29',NULL),(203,'/cmdb/ldap/remove','解除用户绑定',0,'2017-06-28 05:45:53',NULL),(204,'/cmdb/mailLdap/close','关闭用户的邮箱',0,'2017-06-28 07:50:52',NULL),(205,'/cmdb/user/leave','人员离职接口',0,'2017-06-29 03:26:19',NULL),(206,'app.dns','dns配置管理菜单',0,'2017-07-11 09:11:45',NULL),(207,'/dns/dnsmasq/page','dnsmasq详情页',0,'2017-07-11 10:22:12',NULL),(208,'/dns/dnsmasq/del','删除dnsmasq配置',0,'2017-07-13 10:38:15',NULL),(209,'/dns/dnsmasq/save','保存dnsmasq配置',0,'2017-07-13 10:38:29',NULL),(210,'/dns/save','dns按钮',0,'2017-07-13 10:38:51',NULL),(211,'/server/psSave','物理服务器管理菜单',0,'2017-08-04 02:01:44','2017-08-04 02:01:55'),(212,'/server/ps/esxiVms','esxi虚拟机列表页面',0,'2017-08-07 06:22:37',NULL),(213,'/server/ps/esxiDatastores','esxi的数据存储详情页',0,'2017-08-07 07:42:07',NULL),(214,'/cmdb/ldapGroup/add','添加用户至用户组',0,'2017-08-10 08:13:46',NULL),(215,'/cmdb/ldapGroup/del','移除用户组中的用户',0,'2017-08-10 08:14:09',NULL),(216,'/cmdb/mailLdap/active','激活用户的邮箱',0,'2017-08-10 08:14:34',NULL),(217,'/cmdb/user','查询用户详细信息(权限)',0,'2017-08-10 08:34:16',NULL),(218,'app.cigroups','用户管理-持续集成用户组管理',0,'2017-08-18 02:03:06',NULL),(219,'/ci/usergroup/page','持续集成用户组详情页',0,'2017-08-18 02:05:45',NULL),(220,'/ci/usergroup/refresh','持续集成用户组数据更新',0,'2017-08-18 07:38:17',NULL),(221,'/ci/usergroup/del','删除持续集成用户组',0,'2017-08-18 08:27:07',NULL),(222,'/ci/usergroup/update','更新持续集成用户组',0,'2017-08-18 08:27:22',NULL),(223,'app.usersLeave','cmdb离职用户管理',0,'2017-08-21 06:25:11',NULL),(224,'app.ciusers','持续集成用户管理菜单',0,'2017-08-21 06:32:40',NULL),(225,'/cmdb/ci/users','用户持续集成权限详情页',0,'2017-08-22 03:07:05',NULL),(226,'/ci/usergroup/save','持续集成用户组保存',0,'2017-08-23 08:36:34',NULL),(227,'/cmdb/ci/users/refresh','刷新持续集成用户权限表',0,'2017-08-24 03:17:34',NULL),(228,'/cmdb/ci/users/addGroup','用户添加持续集成权限组',0,'2017-08-24 09:10:17',NULL),(229,'/cmdb/ci/users/delGroup','用户删除持续集成权限组',0,'2017-08-24 09:10:57',NULL),(230,'/cmdb/ci/user','查询用户持续集成权限信息',0,'2017-08-24 10:18:59',NULL),(231,'app.vmServer','虚拟服务器菜单',0,'2017-08-29 02:08:45',NULL),(232,'app.ecsServer','ECS服务器菜单',0,'2017-08-29 02:09:04',NULL),(233,'app.physicalServer','物理服务器菜单',0,'2017-08-29 02:09:20',NULL),(234,'/cmdb/users/addUsersMobile','批量填充用户手机号',0,'2017-09-05 07:38:02',NULL),(235,'/cmdb/user/addUsersMobile','填充用户手机号',0,'2017-09-05 07:38:18',NULL),(236,'app.todo','工单',0,'2017-09-05 09:18:39',NULL),(237,'/todo/group/query','查询工单组',0,'2017-09-05 11:02:14',NULL),(238,'/todo/establish','创建工单',0,'2017-09-07 03:51:21',NULL),(239,'/todo/todoDetail/query','获取工单详情',0,'2017-09-07 08:40:30',NULL),(240,'/todo/todoDetail/addTodoKeybox','权限申请工单里添加堡垒机组',0,'2017-09-07 08:41:22',NULL),(241,'/todo/todoDetail/delTodoKeybox','权限申请工单里删除堡垒机组',0,'2017-09-07 08:41:42',NULL),(242,'/todo/todoDetail/submit','配置完毕提交工单',0,'2017-09-07 13:36:57',NULL),(243,'/todo/queryMyJob','查询我的待办工单',0,'2017-09-08 02:17:48',NULL),(244,'/todo/revokeTodoDetail','撤销工单按钮',0,'2017-09-08 07:59:33',NULL),(245,'/todo/invokeTodoDetail','执行工单按钮',0,'2017-09-08 10:40:31',NULL),(246,'/todo/queryCompleteJob','查询本人完成的工单',0,'2017-09-08 11:41:34',NULL),(247,'/todo/todoDetail/addTodoCiUserGroup','工单添加持续集成权限组',0,'2017-09-11 10:32:50',NULL),(248,'/todo/todoDetail/delTodoCiUserGroup','工单删除持续集成权限组',0,'2017-09-11 10:33:10',NULL),(249,'/todo/todoDetail/setTodoSystemAuth','标准工单-平台权限申请设置按钮(添加,移除)',0,'2017-09-13 10:52:52',NULL),(250,'app.logService','日志服务菜单',0,'2017-09-20 02:59:05',NULL),(251,'/logService/nginx/cfg/page','查询日志服务配置项目',0,'2017-09-20 05:51:36',NULL),(252,'/logService/nginx/query','日志服务nginx日志查询',0,'2017-09-20 12:34:54',NULL),(253,'/logService/logHistograms/page','日志分布视图详情页',0,'2017-09-21 05:51:51','2017-09-25 10:19:28'),(254,'/logService/nginx/viewLog','日志服务查看日志详情',0,'2017-09-21 07:33:34',NULL),(255,'app.javaLogService','java业务日志菜单',0,'2017-09-22 09:39:30',NULL),(256,'/logService/java/path/query/page','常用日志查询',0,'2017-09-25 05:23:15',NULL),(257,'/logService/java/viewLog','查看java日志详情',0,'2017-09-25 07:47:12',NULL),(258,'/logService/java/query','查询java日志分布视图',0,'2017-09-25 07:47:45',NULL),(260,'/logService/servergroup/query','查询授权的日志组（服务器组）',0,'2017-09-25 09:46:19',NULL),(261,'app.javaLogServiceManage','业务日志配置菜单',0,'2017-09-27 03:52:24',NULL),(262,'/logService/project/query','日志服务查询project',0,'2017-09-27 08:45:23',NULL),(263,'/logService/logstore/query','日志服务查询logstore',0,'2017-09-27 08:45:40',NULL),(264,'/logService/machineGroup/query','获取日志服务机器组详情',0,'2017-09-28 02:04:57','2017-09-28 02:13:29'),(265,'/logService/serverGroupCfg/save','保存业务日志服务器组配置',0,'2017-09-28 03:35:32',NULL),(266,'/logService/status','首页用日志服务统计信息',0,'2017-09-29 07:34:55',NULL),(267,'/box/status','首页中的堡垒机使用统计',0,'2017-09-30 01:59:31',NULL),(268,'/statistics/ci/status','首页持续集成统计',0,'2017-09-30 06:47:23',NULL),(269,'/server/status','首页统计服务器信息',0,'2017-10-11 03:08:29',NULL),(270,'/server/vmPowerOn','vm开机',0,'2017-10-17 07:59:22',NULL),(271,'/server/vmPowerOff','vm关机',0,'2017-10-17 07:59:35',NULL),(272,'/box/getway/status','getway登陆统计',1,'2017-10-23 02:54:55',NULL),(273,'app.projectManagement','项目管理',0,'2017-10-26 02:38:54',NULL),(274,'/project/page','项目管理详情页',0,'2017-10-26 06:48:01',NULL),(275,'/project/save','项目管理保存',0,'2017-10-30 03:22:04',NULL),(276,'/project/del','项目管理删除',0,'2017-10-30 03:22:23',NULL),(277,'/project/get','按id查询pm',0,'2017-10-31 09:07:03',NULL),(278,'/project/user/add','pm增加用户',0,'2017-10-31 09:07:23',NULL),(279,'/project/user/del','pm删除用户',0,'2017-10-31 09:07:39',NULL),(280,'/project/serverGroup/add','pm增加服务器组',0,'2017-10-31 09:08:06',NULL),(281,'/project/serverGroup/del','pm删除服务器组',0,'2017-10-31 09:08:22',NULL),(282,'app.projectHeartbeat','项目线上生命心跳菜单',0,'2017-11-01 11:34:30',NULL),(283,'/api/login','开放登陆认证',1,'2017-11-02 03:25:32','2017-11-02 07:08:53'),(284,'/project/heartbeat/page','项目生命管理心跳详情',0,'2017-11-02 08:56:13',NULL),(285,'/project/heartbeat/save','项目线上生命心跳按钮',0,'2017-11-03 03:52:58',NULL),(286,'/todo/todoDetail/setTodoVpn','标准工单-vpn权限申请按钮',0,'2017-11-06 06:51:40',NULL),(288,'/servergroup/project/query/page','查询项目管理服务器组列表(不重复添加)',0,'2017-11-10 02:09:40',NULL),(290,'/todo/todoNewProject/stash/project/query','查询合适条件下的StashProject分页数据',0,'2017-11-16 03:06:24',NULL),(291,'/todo/todoNewProject/stash/repository/query','查询合适条件下的StashRepository分页数据',0,'2017-11-16 03:06:46',NULL),(294,'/todo/todoNewProject/stash/project/get','查询合适条件下的StashProject',0,'2017-11-16 03:08:09',NULL),(295,'/todo/todoDetail/save','保存（暂存）工单',0,'2017-11-17 02:10:34',NULL),(296,'/todo/todoNewProject/builderPlan/query','查询构建计划',0,'2017-11-17 08:58:35',NULL),(297,'/todo/todoNewProject/builderPlan/del','删除构建计划',0,'2017-11-17 08:58:58',NULL),(298,'/todo/todoNewProject/builderPlan/update','更新构建计划',0,'2017-11-17 08:59:16',NULL),(299,'/todo/todoNewProject/builderPlan/save','保存构建计划',0,'2017-11-17 08:59:40','2017-11-17 09:34:03'),(300,'/todo/todoNewProject/newServer/query','新项目申请查询新服务器信息',0,'2017-11-20 09:34:09',NULL),(301,'/todo/todoNewProject/newServer/del','新项目申请删除新服务器信息',0,'2017-11-20 10:32:08','2017-11-20 10:32:15'),(302,'/todo/todoNewProject/newServer/save','新项目申请保存新服务器信息',0,'2017-11-20 10:32:46',NULL),(303,'/todo/todoNewProject/checkProjectName','新项目申请工单项目名校验',0,'2017-11-21 06:51:06',NULL),(304,'/safe/users','查询用户（不含私密信息）',0,'2017-11-21 07:57:00',NULL),(305,'/todo/todoNewProject/invoke','新建项目工单管理员处理按钮',0,'2017-11-23 10:03:03',NULL),(306,'/servergroup/query/get','按名称查询服务器组',0,'2017-11-23 11:59:33',NULL),(307,'/jenkins/jobNote','',1,'2017-12-13 09:45:55','2017-12-14 12:27:24'),(308,'/git/webHooks','webHook接口',1,'2017-12-14 12:28:49',NULL),(309,'app.webHooks','持续集成webHooks菜单',0,'2017-12-15 06:26:21',NULL),(310,'app.jenkinsJobs','持续集成任务管理菜单',0,'2017-12-15 06:27:01',NULL),(311,'app.jenkinsJobBuilds','持续集成构建任务详情页',0,'2017-12-15 06:27:29',NULL),(312,'app.ci','持续集成菜单',0,'2017-12-15 06:34:12',NULL),(313,'/jenkins/webHooks/page','webHooks详情页',0,'2017-12-15 07:09:30',NULL),(314,'/jenkins/jobs/page','持续集成构建任务管理页',0,'2017-12-15 08:05:36',NULL),(315,'/jenkins/job/builds/page','持续集成构建任务详情页',0,'2017-12-15 08:06:27',NULL),(316,'/jenkins/save','jenkins保存权限',0,'2017-12-21 06:51:31',NULL),(317,'/jenkins/jobs/del','删除任务',0,'2017-12-21 08:04:38',NULL),(318,'/jenkins/jobs/save','保存任务',0,'2017-12-21 08:04:50',NULL),(319,'/jenkins/jobs/params/query','查询任务参数',0,'2017-12-21 08:54:45',NULL),(320,'/jenkins/jobs/params/del','删除任务参数',0,'2017-12-21 08:55:06',NULL),(321,'/jenkins/jobs/params/save','保存任务参数',0,'2017-12-21 08:55:22',NULL),(322,'/jenkins/jobs/build','执行任务',0,'2017-12-21 12:13:03',NULL),(323,'/jenkins/android/jobNote','android打包通知接口',1,'2017-12-22 03:10:20','2017-12-22 03:45:35'),(324,'/jenkins/jobs/rebuild','任务详情页重新构建',0,'2017-12-26 12:50:02',NULL),(325,'/jenkins/job/refs/query','查询任务对应的仓库分支',0,'2017-12-27 09:35:28',NULL),(326,'/jenkins/jobs/ios/build','任务管理中执行ios任务',0,'2017-12-27 12:57:24',NULL),(327,'/jenkins/job/refs/get','强制获取最新分支',0,'2017-12-29 09:27:53',NULL),(328,'/git/refs/query','git分支查询',0,'2018-01-02 01:36:09',NULL),(329,'/git/refs/get','git分支查询（非缓存）',0,'2018-01-02 01:36:37',NULL),(330,'app.jenkinsFt','持续集成-前端菜单',0,'2018-01-02 03:14:18',NULL),(331,'app.jenkinsAndroid','持续集成-android菜单',0,'2018-01-02 03:14:58',NULL),(332,'app.jenkinsIos','持续集成-iOS菜单',0,'2018-01-02 03:15:18',NULL),(333,'/jenkins/saveFt','持续集成前端构建保存权限',0,'2018-01-03 01:42:40',NULL),(334,'/jenkins/saveIos','持续集成ios操作菜单',0,'2018-01-03 02:23:40',NULL),(335,'/jenkins/saveAndroid','持续集成android操作菜单',0,'2018-01-03 02:32:46',NULL),(336,'/jenkins/job/builds','持续集成单个任务详情（用于artifacts下载页面）',1,'2018-01-03 07:06:46',NULL),(337,'/jenkins/projects/page','持续集成项目详情页',0,'2018-01-08 08:43:43',NULL),(338,'/jenkins/project/save','持续集成保存项目',0,'2018-01-09 01:42:36',NULL),(339,'/jenkins/project/del','持续集成删除项目',0,'2018-01-09 02:33:53',NULL),(340,'/jenkins/projects/save','持续集成项目保存菜单',0,'2018-01-09 02:37:15',NULL),(341,'/jenkins/project/params/del','持续继承项目基本参数删除',0,'2018-01-09 03:20:02',NULL),(342,'/jenkins/project/params/save','持续继承项目基本参数保存',0,'2018-01-09 03:20:25',NULL),(343,'/jenkins/project/params/query','持续集成项目基本参数列表',0,'2018-01-09 03:40:09',NULL),(344,'/jenkins/project/env/save','持续集成项目环境保存',0,'2018-01-09 06:15:10',NULL),(345,'/jenkins/project/env/del','持续集成项目环境删除',0,'2018-01-09 06:15:26',NULL),(346,'/jenkins/project/env/query','持续集成项目环境查询',0,'2018-01-09 06:23:34',NULL),(347,'/jenkins/project/env/params/save','持续集成项目环境可变参数保存',0,'2018-01-09 07:59:40',NULL),(348,'/jenkins/project/job/save','持续继承项目环境创建job',0,'2018-01-09 10:41:44',NULL),(349,'/statistics/serverperf/task/get','查看zabbix同步任务状态',0,'2018-01-17 08:18:51',NULL),(350,'/statistics/serverperf/task/reset','重置zabbix同步任务状态',0,'2018-01-17 08:19:17',NULL),(351,'/jenkins/jobs/appLink','android官网发布',0,'2018-01-25 08:03:46',NULL),(352,'app.serverload','服务器负载',0,'2018-02-08 09:36:29',NULL),(353,'/statistics/serverperf/task/run','运行服务器性能监控同步任务',0,'2018-02-09 07:59:57',NULL),(354,'/api/users','对外开放用户查询接口',1,'2018-03-15 05:53:32',NULL),(355,'app.scmgroups','scm用户组管理菜单',0,'2018-04-10 02:19:51',NULL),(356,'/scm/permissions/page','SCM权限组详情页',0,'2018-04-10 02:54:00',NULL),(357,'/scm/permissions/refresh','SCM数据同步（创建新项目的配置条目）',0,'2018-04-10 06:20:51',NULL),(358,'/scm/permissions/save','保存SCM配置',0,'2018-04-10 08:03:48',NULL),(359,'/scm/permissions/get','查询SCM组配置',0,'2018-04-11 07:49:09',NULL),(360,'app.jenkinsTest','持续集成QC自动化测试任务管理菜单',0,'2018-04-18 01:44:34',NULL),(361,'/jenkins/jobs/create','创建jenkins job',0,'2018-04-24 03:23:01',NULL),(362,'/todo/todoTomcatVersion/tomcatVersion/query','Tomcat版本变更工单查询安装版本',0,'2018-04-25 08:49:10',NULL),(363,'/todo/todoTomcatVersion/task/updateTomcat','Tomcat版本变更工单执行自动升级任务',0,'2018-04-26 06:45:59',NULL),(364,'/jenkins/job/refs/change','手动维护 refs接口',0,'2018-05-07 09:22:31',NULL),(365,'/server/ecsSync','同步阿里云ECS数据并校验',0,'2018-05-09 03:47:23',NULL),(366,'/cmdb/zabbix/remove','离职删除zabbix用户',0,'2018-05-11 03:44:53',NULL),(367,'/zabbixserver/repair','修复Server表中的Zabbix监控相关数据',0,'2018-05-11 09:09:16',NULL),(368,'/todo/todoTomcatVersion/task/rollbackTomcat','tomcat版本变更工单回滚按钮',0,'2018-05-14 10:12:29','2018-05-14 10:13:12'),(369,'/task/cmd/doCmd','任务管理执行指令',0,'2018-05-15 07:16:13',NULL),(370,'/task/cmd/query','任务结果查询',0,'2018-05-17 05:56:42',NULL),(371,'/task/script/page','任务脚本详情页',0,'2018-05-22 06:50:17',NULL),(372,'/task/script/save','任务脚本保存权限',0,'2018-05-22 06:53:25',NULL),(373,'app.taskScript','任务脚本菜单',0,'2018-05-22 07:42:13',NULL),(374,'/task/cmd/doScript','服务器任务执行Script',0,'2018-05-23 02:27:27',NULL),(375,'/task/ansible/version','ansible版本信息查询',0,'2018-05-24 02:12:53',NULL),(376,'/task/ansibleTask/page','ansible历史任务查看',0,'2018-05-24 07:34:25',NULL),(377,'/servergroup/useType/page','服务器组使用类型分页详情',0,'2018-05-29 06:47:51',NULL),(378,'/servergroup/useType/save','保存服务器组使用类型',0,'2018-05-29 07:33:50',NULL),(379,'/servergroup/useType/del','删除服务器使用类型',0,'2018-05-29 08:08:12',NULL),(380,'/servergroup/useType/query','查询服务器组使用类型列表',0,'2018-05-29 08:17:12',NULL),(381,'/config/center/get','获取配置中心的配置组',0,'2018-05-29 10:48:33',NULL),(382,'/aliyun/api/describeRegions','查询regions',0,'2018-05-30 01:13:36',NULL),(383,'/config/center/update','更新配置(map)',0,'2018-05-30 02:25:38',NULL),(384,'/aliyun/api/describeImages','获取ECS镜像列表',0,'2018-05-30 06:09:09',NULL),(385,'/aliyun/image/save','保存阿里云ECS镜像',0,'2018-05-30 07:32:45',NULL),(386,'/aliyun/image/del','删除阿里云ECS镜像',0,'2018-05-30 07:33:06',NULL),(387,'/aliyun/vpc/get','查询阿里云vpc网络详情（本地数据）',0,'2018-05-31 06:26:10',NULL),(388,'/aliyun/vpc/del','删除阿里云vpc网络详情（本地数据）',0,'2018-05-31 06:26:34',NULL),(389,'/aliyun/vpc/rsync','同步阿里云Vpc网络配置',0,'2018-05-31 07:05:16',NULL),(390,'/zabbixserver/version','检查zabbix配置并获得版本信息',0,'2018-05-31 12:10:19',NULL),(391,'/zabbixserver/template/page','zabbix模版详情页',0,'2018-06-01 03:33:57',NULL),(392,'/zabbixserver/template/set','zabbix模版是否启用',0,'2018-06-01 03:34:32',NULL),(393,'/zabbixserver/template/del','zabbix模版删除',0,'2018-06-01 03:34:55',NULL),(394,'/zabbixserver/template/rsync','zabbix模版同步',0,'2018-06-01 03:35:15',NULL),(395,'/zabbixserver/proxy/query','查询zabbixserver的所有proxy',0,'2018-06-01 10:13:04',NULL),(396,'/zabbixserver/template/get','获取服务器组的Zabbix模版列表',0,'2018-06-04 01:43:24',NULL),(397,'/zabbixserver/proxy/get','获取服务器组的Zabbix代理列表',0,'2018-06-04 01:43:58',NULL),(398,'/zabbixserver/host/get','查询zabbix主机相关的配置',0,'2018-06-04 03:38:48',NULL),(399,'/zabbixserver/host/save','保存zabbix主机监控配置',0,'2018-06-04 03:39:10',NULL),(400,'app.ansibleConfigFile','Ansible主机配置文件管理菜单',0,'2018-06-04 08:20:30',NULL),(401,'/config/file/queryPath','查询不重复的文件路径',0,'2018-06-05 05:16:39',NULL),(402,'/config/fileCopy/save','保存服务器同步配置',0,'2018-06-05 05:53:41',NULL),(403,'/config/fileCopy/query','查询配置文件同步配置页',0,'2018-06-05 06:17:40',NULL),(404,'/config/fileCopy/del','删除服务器同步配置',0,'2018-06-05 07:01:24',NULL),(405,'/task/copy/doFileCopy','远程同步文件任务',0,'2018-06-05 08:22:17','2018-06-05 08:23:26'),(406,'/task/copy/doFileCopyAll','批量远程同步配置',0,'2018-06-05 09:03:12',NULL),(407,'app.getwayConfigFile','Getway管理菜单',0,'2018-06-05 10:09:28',NULL),(408,'/box/user/getway/launch','查看用户Getway配置文件',0,'2018-06-06 07:15:51',NULL),(409,'/config/file/getGetwayPath','取Getway用户文件目录配置',0,'2018-06-06 10:06:09',NULL),(410,'/task/cmd/doScript/updateGetway','更新getway',0,'2018-06-08 06:37:00',NULL),(411,'app.shadowsocksConfigFile','SS配置管理菜单',0,'2018-06-11 01:52:09',NULL),(412,'/config/fileCopy/script/save','配置文件远程同步后执行配置',0,'2018-06-11 05:47:55',NULL),(413,'/config/fileCopy/script/query','远程同步后执行Script配置详情页',0,'2018-06-11 06:31:39',NULL),(414,'/task/cmd/copySever/doScript','执行同步后的Script',0,'2018-06-11 08:09:03',NULL),(415,'/task/cmd/copySever/doScriptByGroup','执行同步后的ScriptByGroupName',0,'2018-06-11 08:09:35',NULL),(416,'/config/getway/saveKey','创建privteKey',0,'2018-06-11 10:31:50',NULL),(417,'/task/cmd/doScript/getwaySetLogin','配置getway登录限制',0,'2018-06-11 11:07:25',NULL);
/*!40000 ALTER TABLE `new_auth_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_auth_rgroup`
--

DROP TABLE IF EXISTS `new_auth_rgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_auth_rgroup` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupCode` varchar(200) DEFAULT NULL COMMENT '组code',
  `groupDesc` varchar(200) DEFAULT NULL COMMENT '组描述',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_unique` (`groupCode`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_rgroup`
--

LOCK TABLES `new_auth_rgroup` WRITE;
/*!40000 ALTER TABLE `new_auth_rgroup` DISABLE KEYS */;
INSERT INTO `new_auth_rgroup` VALUES (1,'menu','系统菜单','2016-09-19 11:14:13','2016-10-14 03:47:40'),(5,'ip','ip管理','2016-09-22 12:09:10','2016-10-14 03:47:30'),(6,'auth','权限管理','2016-09-22 12:10:48','2016-10-14 03:47:26'),(7,'ipgroup','ip组管理','2016-09-22 12:11:07','2016-10-14 03:47:21'),(8,'server','服务器管理','2016-09-22 12:11:41','2016-10-14 03:47:16'),(9,'servergroup','服务器组管理','2016-09-22 12:11:59','2016-10-14 03:47:07'),(10,'system','系统管理','2016-10-08 07:28:11','2016-10-14 03:47:02'),(11,'todo','日常工单管理','2016-10-09 09:29:07','2016-10-27 08:24:58'),(12,'property','属性管理','2016-10-27 08:23:58',NULL),(13,'todoConfig','工单配置项管理','2016-10-27 08:24:47',NULL),(14,'keybox','堡垒机','2016-11-28 04:58:02',NULL),(15,'user','用户相关','2016-11-28 05:00:47',NULL),(16,'configFile','配置管理','2016-12-29 11:14:34',NULL),(17,'zabbixserver','监控管理','2017-01-18 06:04:24',NULL),(18,'statistics','统计','2017-02-17 07:32:39',NULL),(19,'task','任务管理','2017-03-31 11:36:58',NULL),(20,'explain','SQL审核','2017-04-01 03:37:59',NULL),(21,'configCenter','配置中心','2017-06-01 01:58:45',NULL),(23,'aliyun','阿里云','2017-06-12 09:56:57',NULL),(24,'dns','dns管理','2017-07-11 10:21:41',NULL),(25,'ecsServer','ECS服务器','2017-08-29 03:10:43',NULL),(26,'ecsTemplate','ecs模版','2017-08-29 03:11:01',NULL),(27,'vmTemplate','VM模版','2017-08-29 05:57:35',NULL),(28,'logService','日志服务','2017-09-20 05:50:14',NULL),(29,'projectManagement','项目线上生命管理','2017-10-26 06:47:25','2017-11-01 11:31:49'),(31,'projectHeartbeat','项目线上生命心跳','2017-11-01 11:33:28',NULL),(32,'jenkins','jenkins接口','2017-12-13 09:45:39',NULL),(33,'gitlab','gitlab资源','2017-12-14 12:28:33',NULL),(34,'git','git仓库管理','2018-01-02 01:35:35',NULL);
/*!40000 ALTER TABLE `new_auth_rgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_auth_role`
--

DROP TABLE IF EXISTS `new_auth_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_auth_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `roleName` varchar(200) NOT NULL DEFAULT '' COMMENT '角色名称',
  `roleDesc` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `roleName` (`roleName`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_role`
--

LOCK TABLES `new_auth_role` WRITE;
/*!40000 ALTER TABLE `new_auth_role` DISABLE KEYS */;
INSERT INTO `new_auth_role` VALUES (1,'admin','管理员','2016-09-19 07:36:16','2016-09-22 03:00:30'),(3,'dba','数据库管理员','2016-10-13 02:35:33',NULL),(4,'base','普通用户','2016-10-20 07:28:26','2018-06-08 00:57:34'),(5,'dev','开发人员','2016-11-04 03:50:04','2018-01-02 03:11:37'),(6,'devops','运维工程师','2017-09-07 10:29:09',NULL),(7,'devAndroid','android开发','2017-12-22 07:28:18','2018-01-12 06:47:53'),(8,'devIos','iso开发','2017-12-28 05:54:52','2018-01-12 06:47:48'),(9,'devFt','前端开发','2018-01-02 03:12:11','2018-01-12 06:47:40'),(10,'qa','质量管理工程师','2018-04-18 01:52:05',NULL),(11,'aaad','dev','2018-06-11 07:26:42',NULL);
/*!40000 ALTER TABLE `new_auth_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_auth_role_resources`
--

DROP TABLE IF EXISTS `new_auth_role_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_auth_role_resources` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `roleId` bigint(20) NOT NULL COMMENT '角色id',
  `resourceId` bigint(20) NOT NULL COMMENT '资源id',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_resource_unique` (`roleId`,`resourceId`)
) ENGINE=InnoDB AUTO_INCREMENT=666 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_role_resources`
--

LOCK TABLES `new_auth_role_resources` WRITE;
/*!40000 ALTER TABLE `new_auth_role_resources` DISABLE KEYS */;
INSERT INTO `new_auth_role_resources` VALUES (1,1,1,'2016-09-19 07:36:24',NULL),(2,1,2,'2016-09-19 07:36:29',NULL),(3,1,3,'2016-09-19 07:36:33',NULL),(4,1,4,'2016-09-19 07:36:38',NULL),(5,1,5,'2016-09-19 07:36:42',NULL),(6,1,7,'2016-09-22 06:44:25',NULL),(7,1,8,'2016-09-22 06:44:27',NULL),(8,1,9,'2016-09-22 06:44:27',NULL),(9,1,10,'2016-09-22 06:44:28',NULL),(10,1,12,'2016-09-22 12:34:54',NULL),(13,1,13,'2016-09-22 12:37:38',NULL),(14,1,14,'2016-09-22 12:40:31',NULL),(15,1,15,'2016-09-22 12:40:32',NULL),(16,1,16,'2016-09-22 12:40:33',NULL),(17,1,17,'2016-09-22 12:40:34',NULL),(18,1,18,'2016-09-22 12:40:34',NULL),(19,1,19,'2016-09-22 12:40:35',NULL),(20,1,20,'2016-09-22 12:40:35',NULL),(21,1,21,'2016-09-22 12:40:35',NULL),(22,1,22,'2016-09-22 12:40:36',NULL),(23,1,23,'2016-09-22 12:40:36',NULL),(24,1,24,'2016-09-22 12:40:36',NULL),(25,1,25,'2016-09-22 12:40:36',NULL),(26,1,26,'2016-09-22 12:40:37',NULL),(27,1,27,'2016-09-22 12:40:37',NULL),(28,1,28,'2016-09-22 12:40:39',NULL),(29,1,29,'2016-09-22 12:40:40',NULL),(30,1,30,'2016-09-22 12:40:41',NULL),(31,1,31,'2016-09-22 12:40:41',NULL),(32,1,32,'2016-09-22 12:40:42',NULL),(33,1,33,'2016-09-22 12:40:43',NULL),(34,1,34,'2016-09-22 12:40:44',NULL),(35,1,35,'2016-09-22 12:40:44',NULL),(36,1,36,'2016-09-22 12:40:45',NULL),(37,1,37,'2016-09-22 12:40:46',NULL),(38,1,38,'2016-09-22 12:40:46',NULL),(39,1,39,'2016-09-22 12:40:47',NULL),(40,1,40,'2016-09-22 12:40:48',NULL),(41,1,41,'2016-09-22 12:40:49',NULL),(42,1,42,'2016-09-22 12:40:49',NULL),(43,1,43,'2016-09-22 12:40:50',NULL),(44,1,44,'2016-09-22 12:40:50',NULL),(45,1,45,'2016-09-23 03:33:16',NULL),(46,1,46,'2016-09-23 03:33:17',NULL),(47,1,47,'2016-09-23 09:33:25',NULL),(48,1,48,'2016-09-29 06:35:24',NULL),(49,1,49,'2016-10-08 06:57:17',NULL),(50,1,50,'2016-10-08 07:31:12',NULL),(51,1,51,'2016-10-08 07:31:12',NULL),(52,1,52,'2016-10-08 07:31:13',NULL),(53,1,53,'2016-10-09 08:49:40',NULL),(54,1,54,'2016-10-09 08:49:40',NULL),(55,1,55,'2016-10-09 09:30:48',NULL),(56,1,56,'2016-10-09 09:30:49',NULL),(57,1,57,'2016-10-09 09:30:49',NULL),(58,1,58,'2016-10-10 11:09:19',NULL),(59,1,59,'2016-10-11 08:24:00',NULL),(60,1,60,'2016-10-11 08:42:32',NULL),(61,1,61,'2016-10-12 07:29:08',NULL),(62,3,1,'2016-10-14 03:39:46',NULL),(63,3,2,'2016-10-14 03:40:11',NULL),(64,3,3,'2016-10-14 03:40:11',NULL),(65,3,4,'2016-10-14 03:40:12',NULL),(66,3,7,'2016-10-14 03:40:12',NULL),(70,3,49,'2016-10-14 03:40:22',NULL),(71,3,53,'2016-10-14 03:40:22',NULL),(72,3,54,'2016-10-14 03:40:23',NULL),(73,3,55,'2016-10-14 03:40:36',NULL),(74,3,56,'2016-10-14 03:40:37',NULL),(75,3,57,'2016-10-14 03:40:38',NULL),(76,3,58,'2016-10-14 03:40:38',NULL),(77,3,59,'2016-10-14 03:40:38',NULL),(78,3,60,'2016-10-14 03:40:39',NULL),(79,3,61,'2016-10-14 03:40:39',NULL),(80,1,62,'2016-10-19 06:39:42',NULL),(81,3,62,'2016-10-19 06:39:47',NULL),(88,1,63,'2016-10-21 02:07:33',NULL),(89,1,64,'2016-10-21 02:49:11',NULL),(90,1,65,'2016-10-21 03:08:45',NULL),(91,1,66,'2016-10-21 03:17:01',NULL),(92,1,67,'2016-10-21 05:51:31',NULL),(93,1,68,'2016-10-21 06:03:23',NULL),(94,1,69,'2016-10-21 06:03:23',NULL),(96,1,71,'2016-10-25 03:20:52',NULL),(98,4,2,'2016-10-28 02:03:10',NULL),(99,4,40,'2016-10-28 02:03:15',NULL),(100,4,71,'2016-10-28 02:03:24',NULL),(101,4,3,'2016-10-28 02:03:34',NULL),(102,4,37,'2016-10-28 02:03:39',NULL),(103,4,4,'2016-10-28 02:03:47',NULL),(104,4,32,'2016-10-28 02:03:52',NULL),(105,4,36,'2016-10-28 02:03:54',NULL),(106,4,7,'2016-10-28 02:04:08',NULL),(107,4,29,'2016-10-28 02:04:10',NULL),(108,4,47,'2016-10-28 02:04:14',NULL),(109,4,1,'2016-10-28 02:04:37',NULL),(110,4,54,'2016-10-28 02:04:49',NULL),(111,4,58,'2016-10-28 02:04:51',NULL),(112,4,59,'2016-10-28 02:04:55',NULL),(113,4,60,'2016-10-28 02:04:56',NULL),(114,4,61,'2016-10-28 02:05:01',NULL),(115,4,62,'2016-10-28 02:05:02',NULL),(116,4,12,'2016-10-28 02:35:07',NULL),(117,4,55,'2016-10-28 09:22:16',NULL),(118,1,73,'2016-10-31 06:59:27',NULL),(119,1,74,'2016-10-31 09:48:40',NULL),(120,4,74,'2016-10-31 09:48:58',NULL),(121,1,75,'2016-10-31 11:49:26',NULL),(122,4,75,'2016-10-31 11:49:36',NULL),(123,5,48,'2016-11-04 03:50:14',NULL),(124,1,76,'2016-11-28 05:02:48',NULL),(125,1,77,'2016-11-28 05:02:49',NULL),(126,1,78,'2016-11-28 05:02:50',NULL),(127,1,79,'2016-11-28 05:02:50',NULL),(128,1,80,'2016-11-28 05:02:50',NULL),(129,1,81,'2016-11-28 05:02:51',NULL),(130,1,82,'2016-11-28 05:02:51',NULL),(131,1,83,'2016-11-28 05:02:52',NULL),(132,1,84,'2016-11-28 05:02:52',NULL),(133,1,85,'2016-11-28 05:02:52',NULL),(134,1,86,'2016-11-28 05:02:52',NULL),(135,1,87,'2016-11-28 05:02:53',NULL),(136,4,88,'2016-12-01 07:51:09',NULL),(137,4,90,'2016-12-01 07:51:11',NULL),(138,1,88,'2016-12-01 07:51:18',NULL),(139,1,89,'2016-12-01 07:51:19',NULL),(140,1,90,'2016-12-01 07:51:20',NULL),(141,4,91,'2016-12-01 07:56:27',NULL),(142,4,92,'2016-12-05 02:11:51',NULL),(143,5,85,'2016-12-05 02:12:07',NULL),(144,5,86,'2016-12-13 10:01:47',NULL),(145,1,70,'2016-12-22 09:39:02',NULL),(146,1,72,'2016-12-22 09:39:02',NULL),(147,1,93,'2016-12-22 09:40:22',NULL),(148,1,94,'2016-12-29 03:47:58',NULL),(149,4,95,'2016-12-29 09:04:58',NULL),(150,4,52,'2016-12-29 09:05:03',NULL),(151,4,93,'2016-12-29 09:07:43',NULL),(152,1,96,'2016-12-29 11:18:02',NULL),(153,1,97,'2016-12-29 11:18:02',NULL),(154,1,98,'2016-12-29 11:18:03',NULL),(155,1,99,'2016-12-29 11:18:04',NULL),(156,1,101,'2016-12-29 11:18:05',NULL),(157,1,100,'2016-12-29 11:18:05',NULL),(158,1,102,'2016-12-29 11:18:06',NULL),(160,1,103,'2016-12-29 11:18:07',NULL),(161,1,104,'2016-12-29 11:18:08',NULL),(162,1,91,'2016-12-29 11:18:16',NULL),(163,1,106,'2016-12-29 11:18:17',NULL),(164,1,105,'2016-12-29 11:18:19',NULL),(165,1,107,'2017-01-03 02:59:49',NULL),(166,3,19,'2017-01-03 03:16:19',NULL),(167,1,108,'2017-01-10 13:20:46',NULL),(168,1,109,'2017-01-10 13:20:46',NULL),(169,1,110,'2017-01-11 03:54:12',NULL),(170,4,109,'2017-01-11 06:11:54',NULL),(171,1,111,'2017-01-11 10:38:28',NULL),(172,1,112,'2017-01-11 10:38:28',NULL),(173,1,113,'2017-01-11 10:40:56',NULL),(174,1,114,'2017-01-12 07:49:32',NULL),(175,1,115,'2017-01-12 07:49:34',NULL),(176,1,116,'2017-01-12 07:49:35',NULL),(177,1,117,'2017-01-12 07:49:35',NULL),(178,1,118,'2017-01-12 07:49:36',NULL),(179,1,119,'2017-01-12 07:49:36',NULL),(180,1,120,'2017-01-12 07:49:37',NULL),(181,1,121,'2017-01-12 07:49:37',NULL),(182,1,122,'2017-01-12 07:49:38',NULL),(183,1,123,'2017-01-12 07:49:38',NULL),(184,5,3,'2017-01-16 03:35:04',NULL),(185,5,37,'2017-01-16 03:35:07',NULL),(186,5,93,'2017-01-16 03:35:12',NULL),(187,5,117,'2017-01-16 03:35:22',NULL),(188,5,109,'2017-01-16 03:35:34',NULL),(189,1,124,'2017-01-18 06:06:30',NULL),(190,1,125,'2017-01-18 06:06:30',NULL),(191,1,126,'2017-01-18 06:06:31',NULL),(192,1,127,'2017-01-18 06:06:31',NULL),(193,1,128,'2017-01-18 06:06:31',NULL),(194,1,129,'2017-01-18 06:06:31',NULL),(196,1,130,'2017-01-18 06:06:33',NULL),(197,3,124,'2017-01-18 06:06:38',NULL),(198,3,125,'2017-01-18 06:06:39',NULL),(199,3,126,'2017-01-18 06:06:39',NULL),(200,3,127,'2017-01-18 06:06:39',NULL),(201,3,128,'2017-01-18 06:06:39',NULL),(202,3,129,'2017-01-18 06:06:40',NULL),(203,3,130,'2017-01-18 06:06:40',NULL),(204,1,131,'2017-01-18 06:08:03',NULL),(205,3,131,'2017-01-18 06:08:13',NULL),(206,3,37,'2017-01-18 07:06:10',NULL),(207,3,109,'2017-01-18 07:06:19',NULL),(208,3,117,'2017-01-18 07:06:33',NULL),(209,1,92,'2017-01-20 06:17:53',NULL),(210,1,132,'2017-01-20 06:17:53',NULL),(211,1,133,'2017-01-20 06:17:54',NULL),(212,1,134,'2017-02-07 03:37:54',NULL),(213,1,135,'2017-02-07 03:37:55',NULL),(214,1,136,'2017-02-07 03:37:56',NULL),(215,1,138,'2017-02-09 08:18:29',NULL),(216,1,139,'2017-02-09 08:18:30',NULL),(217,5,138,'2017-02-09 08:18:45',NULL),(218,5,139,'2017-02-09 08:18:46',NULL),(219,3,138,'2017-02-09 08:18:54',NULL),(220,3,139,'2017-02-09 08:18:55',NULL),(221,1,140,'2017-02-14 06:36:20',NULL),(222,1,141,'2017-02-14 06:36:21',NULL),(223,5,140,'2017-02-14 06:36:30',NULL),(224,5,141,'2017-02-14 06:36:31',NULL),(225,1,142,'2017-02-17 07:33:46',NULL),(226,5,142,'2017-02-17 07:34:01',NULL),(227,1,143,'2017-02-17 07:34:47',NULL),(228,5,143,'2017-02-17 07:34:53',NULL),(229,1,144,'2017-02-24 05:48:03',NULL),(230,5,144,'2017-02-24 05:48:07',NULL),(231,1,145,'2017-02-24 05:48:54',NULL),(232,5,145,'2017-02-24 05:49:03',NULL),(233,1,146,'2017-02-24 05:50:07',NULL),(234,5,146,'2017-02-24 05:50:13',NULL),(235,1,147,'2017-02-28 07:48:01',NULL),(236,1,148,'2017-02-28 07:48:08',NULL),(237,5,148,'2017-02-28 07:48:20',NULL),(238,5,147,'2017-03-01 12:53:43',NULL),(239,1,149,'2017-03-02 08:09:09',NULL),(240,5,149,'2017-03-02 08:09:13',NULL),(241,1,150,'2017-03-03 02:55:45',NULL),(242,3,136,'2017-03-10 06:34:58',NULL),(243,3,134,'2017-03-10 06:35:07',NULL),(244,3,135,'2017-03-10 06:35:08',NULL),(245,3,88,'2017-03-10 06:37:58',NULL),(246,3,83,'2017-03-10 06:38:00',NULL),(247,1,154,'2017-03-31 11:39:40',NULL),(248,1,156,'2017-03-31 11:39:41',NULL),(249,1,155,'2017-03-31 11:39:41',NULL),(250,1,151,'2017-03-31 11:39:49',NULL),(251,1,153,'2017-03-31 11:39:50',NULL),(252,5,157,'2017-04-01 03:39:05',NULL),(253,5,158,'2017-04-01 03:44:13',NULL),(254,5,159,'2017-04-01 03:44:15',NULL),(255,5,160,'2017-04-01 03:44:16',NULL),(256,5,161,'2017-04-01 03:44:19',NULL),(257,5,162,'2017-04-01 03:44:22',NULL),(258,1,163,'2017-04-01 09:40:44',NULL),(259,1,164,'2017-04-05 02:54:12',NULL),(260,1,165,'2017-04-05 02:54:12',NULL),(261,1,166,'2017-04-05 02:54:13',NULL),(262,5,152,'2017-04-12 02:20:40',NULL),(263,1,168,'2017-04-12 07:07:57',NULL),(264,1,169,'2017-04-12 08:20:31',NULL),(265,1,172,'2017-04-13 10:14:10',NULL),(266,5,172,'2017-04-14 03:11:45',NULL),(267,5,151,'2017-04-18 03:50:43',NULL),(268,1,173,'2017-04-20 03:24:14',NULL),(269,1,174,'2017-04-20 03:24:15',NULL),(270,1,175,'2017-04-20 09:11:42',NULL),(271,1,176,'2017-04-20 09:20:55',NULL),(272,1,177,'2017-04-21 02:53:19',NULL),(273,1,179,'2017-04-25 07:51:43',NULL),(274,1,180,'2017-04-25 07:51:43',NULL),(275,3,38,'2017-04-27 01:41:50',NULL),(277,3,39,'2017-04-27 01:41:51',NULL),(278,3,93,'2017-04-27 01:41:51',NULL),(279,3,108,'2017-04-27 01:41:52',NULL),(280,3,110,'2017-04-27 01:41:52',NULL),(281,3,111,'2017-04-27 01:41:52',NULL),(283,3,112,'2017-04-27 01:41:53',NULL),(284,3,113,'2017-04-27 01:41:53',NULL),(285,3,114,'2017-04-27 01:41:53',NULL),(286,3,115,'2017-04-27 01:41:54',NULL),(287,3,116,'2017-04-27 01:41:54',NULL),(288,3,118,'2017-04-27 01:41:55',NULL),(289,3,119,'2017-04-27 01:41:55',NULL),(290,3,120,'2017-04-27 01:41:55',NULL),(291,3,121,'2017-04-27 01:41:56',NULL),(292,3,122,'2017-04-27 01:41:56',NULL),(293,3,123,'2017-04-27 01:41:56',NULL),(294,3,140,'2017-04-27 01:41:57',NULL),(295,3,141,'2017-04-27 01:41:57',NULL),(296,3,176,'2017-04-27 01:41:57',NULL),(298,3,179,'2017-04-27 01:41:58',NULL),(299,3,180,'2017-04-27 01:41:58',NULL),(300,3,177,'2017-04-27 01:41:59',NULL),(301,3,40,'2017-04-27 01:42:23',NULL),(302,3,41,'2017-04-27 01:42:24',NULL),(303,3,42,'2017-04-27 01:42:24',NULL),(304,3,43,'2017-04-27 01:42:24',NULL),(305,3,44,'2017-04-27 01:42:25',NULL),(306,3,71,'2017-04-27 01:42:25',NULL),(307,3,73,'2017-04-27 01:42:25',NULL),(309,3,75,'2017-04-27 01:42:26',NULL),(310,3,107,'2017-04-27 01:42:26',NULL),(311,3,152,'2017-04-27 01:42:26',NULL),(312,3,74,'2017-04-27 01:42:27',NULL),(313,1,181,'2017-05-08 04:57:19',NULL),(314,1,182,'2017-05-08 04:57:20',NULL),(315,1,183,'2017-05-10 08:21:27',NULL),(316,1,184,'2017-05-24 08:25:30',NULL),(317,1,185,'2017-06-01 01:59:32',NULL),(318,1,186,'2017-06-01 02:00:34',NULL),(319,1,187,'2017-06-01 03:33:57',NULL),(320,1,188,'2017-06-01 03:33:58',NULL),(321,1,189,'2017-06-01 03:33:59',NULL),(322,1,191,'2017-06-06 02:25:58',NULL),(323,1,192,'2017-06-12 05:59:19',NULL),(324,1,193,'2017-06-12 09:57:36',NULL),(325,1,194,'2017-06-13 02:03:09',NULL),(326,1,195,'2017-06-13 05:18:16',NULL),(327,1,197,'2017-06-13 05:18:17',NULL),(328,1,196,'2017-06-13 05:18:18',NULL),(329,1,198,'2017-06-21 08:26:10',NULL),(330,1,199,'2017-06-21 09:00:30',NULL),(331,1,200,'2017-06-21 09:34:15',NULL),(332,1,201,'2017-06-22 10:19:13',NULL),(333,1,202,'2017-06-28 05:46:05',NULL),(334,1,203,'2017-06-28 05:46:06',NULL),(335,1,204,'2017-06-28 07:51:02',NULL),(336,1,205,'2017-06-29 03:26:32',NULL),(337,1,206,'2017-07-11 09:12:05',NULL),(338,1,207,'2017-07-11 10:22:21',NULL),(339,1,208,'2017-07-13 10:38:59',NULL),(340,1,209,'2017-07-13 10:38:59',NULL),(341,1,210,'2017-07-13 10:39:00',NULL),(342,1,211,'2017-08-04 02:02:23',NULL),(343,1,212,'2017-08-07 06:22:51',NULL),(344,1,213,'2017-08-07 07:42:18',NULL),(345,1,214,'2017-08-10 08:14:45',NULL),(346,1,215,'2017-08-10 08:14:46',NULL),(347,1,216,'2017-08-10 08:14:47',NULL),(348,1,217,'2017-08-10 08:34:27',NULL),(349,1,218,'2017-08-18 02:03:25',NULL),(350,1,219,'2017-08-18 02:06:00',NULL),(351,1,220,'2017-08-18 07:38:27',NULL),(352,1,221,'2017-08-18 08:27:33',NULL),(353,1,222,'2017-08-18 08:27:34',NULL),(354,1,223,'2017-08-21 06:25:28',NULL),(355,1,224,'2017-08-21 06:32:58',NULL),(356,1,225,'2017-08-22 03:07:17',NULL),(357,1,226,'2017-08-23 08:36:43',NULL),(358,1,227,'2017-08-24 03:17:44',NULL),(359,1,228,'2017-08-24 09:10:27',NULL),(360,1,229,'2017-08-24 09:11:08',NULL),(361,1,230,'2017-08-24 10:19:18',NULL),(362,1,157,'2017-08-29 02:09:35',NULL),(363,1,231,'2017-08-29 02:09:35',NULL),(364,1,232,'2017-08-29 02:09:36',NULL),(365,1,233,'2017-08-29 02:09:36',NULL),(366,5,231,'2017-08-29 05:51:34',NULL),(367,5,232,'2017-08-29 05:51:35',NULL),(368,5,233,'2017-08-29 05:51:35',NULL),(369,5,176,'2017-08-29 05:54:39',NULL),(370,5,191,'2017-08-29 05:58:35',NULL),(371,1,234,'2017-09-05 07:38:30',NULL),(372,1,235,'2017-09-05 07:38:31',NULL),(373,1,236,'2017-09-05 09:19:09',NULL),(375,1,237,'2017-09-05 11:02:25',NULL),(376,4,237,'2017-09-05 11:02:38',NULL),(377,4,238,'2017-09-07 03:51:36',NULL),(378,4,239,'2017-09-07 08:41:52',NULL),(379,4,240,'2017-09-07 08:41:53',NULL),(380,4,241,'2017-09-07 08:41:53',NULL),(381,4,242,'2017-09-07 13:37:11',NULL),(382,4,243,'2017-09-08 02:18:04',NULL),(383,4,244,'2017-09-08 07:59:50',NULL),(385,1,245,'2017-09-08 10:41:52',NULL),(386,4,246,'2017-09-08 11:41:54',NULL),(387,4,236,'2017-09-11 06:23:47',NULL),(388,4,247,'2017-09-11 10:33:25',NULL),(389,4,248,'2017-09-11 10:33:26',NULL),(390,4,219,'2017-09-12 03:19:24',NULL),(391,4,249,'2017-09-13 10:53:04',NULL),(392,5,250,'2017-09-20 02:59:34',NULL),(393,1,250,'2017-09-20 03:00:10',NULL),(394,5,251,'2017-09-20 05:51:55',NULL),(395,1,251,'2017-09-20 05:52:00',NULL),(396,5,252,'2017-09-20 12:35:03',NULL),(397,5,253,'2017-09-21 05:52:03',NULL),(398,1,252,'2017-09-21 05:52:16',NULL),(399,1,253,'2017-09-21 05:52:17',NULL),(400,5,254,'2017-09-21 07:33:43',NULL),(401,1,254,'2017-09-21 07:33:47',NULL),(402,5,255,'2017-09-22 09:40:00',NULL),(403,1,255,'2017-09-22 09:40:31',NULL),(404,5,256,'2017-09-25 05:23:25',NULL),(405,1,256,'2017-09-25 05:23:29',NULL),(406,5,257,'2017-09-25 07:48:52',NULL),(407,5,258,'2017-09-25 07:48:52',NULL),(409,1,257,'2017-09-25 07:48:56',NULL),(410,1,258,'2017-09-25 07:48:56',NULL),(412,5,260,'2017-09-25 09:46:34',NULL),(413,1,260,'2017-09-25 09:46:38',NULL),(414,1,261,'2017-09-27 03:52:41',NULL),(415,1,262,'2017-09-27 08:45:50',NULL),(416,1,263,'2017-09-27 08:45:51',NULL),(417,1,264,'2017-09-28 02:05:07',NULL),(418,1,265,'2017-09-28 03:35:40',NULL),(419,4,266,'2017-09-29 07:35:17',NULL),(420,4,267,'2017-09-30 01:59:52',NULL),(421,4,268,'2017-09-30 06:47:53',NULL),(422,4,269,'2017-10-11 03:08:58',NULL),(423,1,270,'2017-10-17 07:59:45',NULL),(424,1,271,'2017-10-17 07:59:46',NULL),(425,1,273,'2017-10-26 02:39:09',NULL),(426,5,273,'2017-10-26 02:39:26',NULL),(427,1,274,'2017-10-26 06:48:10',NULL),(428,5,274,'2017-10-26 06:48:16',NULL),(429,1,275,'2017-10-30 03:22:47',NULL),(430,1,276,'2017-10-30 03:22:49',NULL),(431,1,277,'2017-10-31 09:08:32',NULL),(432,1,278,'2017-10-31 09:08:32',NULL),(433,1,279,'2017-10-31 09:08:33',NULL),(434,1,280,'2017-10-31 09:08:33',NULL),(435,1,281,'2017-10-31 09:08:34',NULL),(436,5,282,'2017-11-01 11:34:55',NULL),(437,5,284,'2017-11-02 08:56:26',NULL),(438,5,285,'2017-11-03 03:53:09',NULL),(439,1,284,'2017-11-03 03:53:16',NULL),(440,1,285,'2017-11-03 03:53:16',NULL),(441,5,286,'2017-11-06 06:52:05',NULL),(442,4,286,'2017-11-06 06:52:34',NULL),(443,1,288,'2017-11-10 02:09:52',NULL),(444,5,288,'2017-11-10 02:10:03',NULL),(445,1,290,'2017-11-16 03:08:28',NULL),(446,1,291,'2017-11-16 03:08:29',NULL),(447,1,294,'2017-11-16 03:08:32',NULL),(448,5,294,'2017-11-16 03:08:51',NULL),(449,5,291,'2017-11-16 03:08:52',NULL),(450,4,294,'2017-11-16 03:09:20',NULL),(451,4,291,'2017-11-16 03:09:21',NULL),(452,4,290,'2017-11-16 03:09:23',NULL),(453,4,295,'2017-11-17 02:10:52',NULL),(454,4,296,'2017-11-17 09:00:04',NULL),(455,4,297,'2017-11-17 09:00:06',NULL),(457,4,299,'2017-11-17 09:00:07',NULL),(458,4,300,'2017-11-20 10:33:17',NULL),(459,4,301,'2017-11-20 10:33:18',NULL),(460,4,302,'2017-11-20 10:33:19',NULL),(461,4,303,'2017-11-21 06:51:22',NULL),(462,1,304,'2017-11-21 07:57:16',NULL),(463,5,304,'2017-11-21 07:57:29',NULL),(464,4,304,'2017-11-21 07:57:36',NULL),(465,1,305,'2017-11-23 10:03:23',NULL),(466,5,305,'2017-11-23 10:03:37',NULL),(467,1,306,'2017-11-23 11:59:52',NULL),(468,4,306,'2017-11-23 12:00:05',NULL),(472,1,309,'2017-12-15 06:28:25',NULL),(473,1,310,'2017-12-15 06:28:26',NULL),(474,1,311,'2017-12-15 06:28:27',NULL),(475,1,312,'2017-12-15 06:34:24',NULL),(476,5,312,'2017-12-15 06:34:33',NULL),(478,1,313,'2017-12-15 07:09:46',NULL),(479,1,314,'2017-12-15 08:06:42',NULL),(480,1,315,'2017-12-15 08:06:43',NULL),(483,1,316,'2017-12-21 06:51:41',NULL),(484,1,317,'2017-12-21 08:05:02',NULL),(485,1,318,'2017-12-21 08:05:03',NULL),(486,1,319,'2017-12-21 08:55:38',NULL),(487,1,320,'2017-12-21 08:55:39',NULL),(488,1,321,'2017-12-21 08:55:39',NULL),(489,1,322,'2017-12-21 12:13:12',NULL),(490,7,322,'2017-12-22 07:28:43',NULL),(491,7,316,'2017-12-26 02:36:27',NULL),(492,1,324,'2017-12-26 12:50:15',NULL),(494,1,325,'2017-12-27 09:35:39',NULL),(495,5,325,'2017-12-27 09:35:48',NULL),(496,1,326,'2017-12-27 12:57:39',NULL),(498,8,326,'2017-12-28 05:55:27',NULL),(499,8,324,'2017-12-28 05:55:45',NULL),(500,8,325,'2017-12-28 05:55:47',NULL),(501,8,319,'2017-12-28 05:56:08',NULL),(503,8,315,'2017-12-28 05:56:14',NULL),(505,8,322,'2017-12-28 05:56:23',NULL),(506,8,314,'2017-12-28 05:56:47',NULL),(509,5,327,'2017-12-29 09:28:06',NULL),(510,5,328,'2018-01-02 01:36:47',NULL),(511,5,329,'2018-01-02 01:36:48',NULL),(512,1,330,'2018-01-02 03:15:35',NULL),(513,1,331,'2018-01-02 03:15:36',NULL),(514,1,332,'2018-01-02 03:15:38',NULL),(515,9,333,'2018-01-03 01:43:12',NULL),(517,9,330,'2018-01-03 01:54:10',NULL),(518,9,309,'2018-01-03 01:54:13',NULL),(519,5,313,'2018-01-03 01:59:55',NULL),(520,5,314,'2018-01-03 01:59:57',NULL),(521,5,315,'2018-01-03 01:59:59',NULL),(522,5,319,'2018-01-03 02:00:01',NULL),(523,9,327,'2018-01-03 02:07:18',NULL),(524,9,325,'2018-01-03 02:07:19',NULL),(525,9,315,'2018-01-03 02:07:36',NULL),(526,9,319,'2018-01-03 02:07:47',NULL),(527,9,322,'2018-01-03 02:07:51',NULL),(528,9,324,'2018-01-03 02:07:52',NULL),(529,9,318,'2018-01-03 02:08:16',NULL),(530,8,321,'2018-01-03 02:24:08',NULL),(531,8,320,'2018-01-03 02:24:09',NULL),(532,8,334,'2018-01-03 02:24:16',NULL),(533,8,332,'2018-01-03 02:24:33',NULL),(534,1,334,'2018-01-03 02:33:05',NULL),(535,1,335,'2018-01-03 02:33:07',NULL),(536,1,333,'2018-01-03 02:33:08',NULL),(537,7,325,'2018-01-03 02:33:51',NULL),(538,7,324,'2018-01-03 02:33:56',NULL),(539,7,321,'2018-01-03 02:33:59',NULL),(540,7,320,'2018-01-03 02:34:00',NULL),(541,7,319,'2018-01-03 02:34:02',NULL),(542,7,335,'2018-01-03 02:34:11',NULL),(543,7,327,'2018-01-03 02:34:16',NULL),(544,7,331,'2018-01-03 02:34:32',NULL),(545,5,337,'2018-01-08 08:44:06',NULL),(546,1,337,'2018-01-08 08:44:25',NULL),(547,1,327,'2018-01-08 08:44:26',NULL),(548,1,338,'2018-01-09 01:43:49',NULL),(549,7,337,'2018-01-09 01:44:07',NULL),(550,7,338,'2018-01-09 01:44:14',NULL),(551,1,339,'2018-01-09 02:34:01',NULL),(552,7,339,'2018-01-09 02:34:09',NULL),(553,1,340,'2018-01-09 02:37:24',NULL),(554,7,340,'2018-01-09 02:37:29',NULL),(555,1,341,'2018-01-09 03:20:33',NULL),(556,1,342,'2018-01-09 03:20:34',NULL),(557,7,341,'2018-01-09 03:20:40',NULL),(558,7,342,'2018-01-09 03:20:40',NULL),(559,1,343,'2018-01-09 03:40:16',NULL),(560,7,343,'2018-01-09 03:40:23',NULL),(561,1,344,'2018-01-09 06:15:35',NULL),(562,1,345,'2018-01-09 06:15:35',NULL),(563,7,344,'2018-01-09 06:15:43',NULL),(564,7,345,'2018-01-09 06:15:44',NULL),(565,1,346,'2018-01-09 06:23:44',NULL),(566,5,346,'2018-01-09 06:23:52',NULL),(567,5,343,'2018-01-09 06:23:55',NULL),(568,1,347,'2018-01-09 07:59:51',NULL),(569,7,347,'2018-01-09 07:59:57',NULL),(570,1,348,'2018-01-09 10:42:01',NULL),(571,7,348,'2018-01-09 10:42:07',NULL),(572,7,317,'2018-01-09 11:04:30',NULL),(573,1,349,'2018-01-17 08:19:30',NULL),(574,1,350,'2018-01-17 08:19:31',NULL),(575,1,268,'2018-01-17 08:19:32',NULL),(576,7,351,'2018-01-25 08:04:05',NULL),(577,1,351,'2018-01-25 10:47:52',NULL),(578,1,352,'2018-02-08 09:36:43',NULL),(579,5,352,'2018-02-08 09:36:58',NULL),(580,1,353,'2018-02-09 08:00:10',NULL),(581,8,316,'2018-03-27 02:26:38',NULL),(582,8,317,'2018-03-27 02:26:39',NULL),(583,8,318,'2018-03-27 02:26:41',NULL),(584,8,338,'2018-03-27 02:26:51',NULL),(585,1,355,'2018-04-10 02:20:06',NULL),(586,1,356,'2018-04-10 02:54:12',NULL),(587,1,357,'2018-04-10 06:21:02',NULL),(588,1,358,'2018-04-10 08:04:07',NULL),(589,1,359,'2018-04-11 07:49:28',NULL),(590,5,359,'2018-04-11 07:49:43',NULL),(591,10,360,'2018-04-18 01:52:32',NULL),(592,1,360,'2018-04-18 01:53:01',NULL),(593,1,361,'2018-04-24 03:23:16',NULL),(594,10,361,'2018-04-24 03:23:28',NULL),(595,5,362,'2018-04-25 08:49:27',NULL),(596,5,130,'2018-04-25 08:49:57',NULL),(597,1,363,'2018-04-26 06:46:13',NULL),(598,5,363,'2018-04-26 06:46:20',NULL),(599,5,364,'2018-05-07 09:23:39',NULL),(600,1,365,'2018-05-09 03:47:39',NULL),(601,6,245,'2018-05-09 09:03:21',NULL),(602,1,366,'2018-05-11 03:45:05',NULL),(603,1,367,'2018-05-11 09:09:27',NULL),(604,5,368,'2018-05-14 10:12:49',NULL),(605,1,369,'2018-05-15 07:16:24',NULL),(606,1,370,'2018-05-17 05:56:54',NULL),(607,5,169,'2018-05-17 09:22:51',NULL),(608,5,369,'2018-05-17 09:23:00',NULL),(609,5,370,'2018-05-17 09:23:01',NULL),(610,1,371,'2018-05-22 06:53:36',NULL),(611,1,372,'2018-05-22 06:53:37',NULL),(612,1,373,'2018-05-22 07:42:27',NULL),(613,1,374,'2018-05-23 02:27:38',NULL),(614,5,375,'2018-05-24 02:13:18',NULL),(615,1,375,'2018-05-24 02:13:30',NULL),(616,1,376,'2018-05-24 07:34:36',NULL),(617,5,376,'2018-05-29 02:14:44',NULL),(618,5,371,'2018-05-29 02:14:49',NULL),(619,5,372,'2018-05-29 02:14:51',NULL),(620,5,374,'2018-05-29 02:14:52',NULL),(621,5,373,'2018-05-29 02:16:15',NULL),(622,1,377,'2018-05-29 06:48:07',NULL),(623,1,378,'2018-05-29 07:34:04',NULL),(624,1,379,'2018-05-29 08:08:30',NULL),(625,5,380,'2018-05-29 08:17:34',NULL),(626,4,380,'2018-05-29 08:17:44',NULL),(627,1,380,'2018-05-29 08:18:00',NULL),(628,1,381,'2018-05-29 10:48:50',NULL),(629,1,382,'2018-05-30 01:13:47',NULL),(630,1,383,'2018-05-30 02:25:51',NULL),(631,1,384,'2018-05-30 06:09:22',NULL),(632,1,385,'2018-05-30 07:33:26',NULL),(633,1,386,'2018-05-30 07:33:26',NULL),(634,1,387,'2018-05-31 06:26:43',NULL),(635,1,388,'2018-05-31 06:26:44',NULL),(636,1,389,'2018-05-31 07:05:27',NULL),(637,1,390,'2018-05-31 12:10:32',NULL),(638,1,391,'2018-06-01 06:36:45',NULL),(639,1,392,'2018-06-01 06:36:46',NULL),(640,1,393,'2018-06-01 06:36:46',NULL),(641,1,394,'2018-06-01 06:36:47',NULL),(642,1,395,'2018-06-01 10:13:14',NULL),(643,1,396,'2018-06-04 01:44:13',NULL),(644,1,397,'2018-06-04 01:44:13',NULL),(645,1,398,'2018-06-04 03:39:21',NULL),(646,1,399,'2018-06-04 03:39:22',NULL),(647,1,400,'2018-06-04 08:20:41',NULL),(648,1,401,'2018-06-05 05:16:51',NULL),(649,1,402,'2018-06-05 05:53:53',NULL),(650,1,403,'2018-06-05 06:17:50',NULL),(651,1,404,'2018-06-05 07:01:35',NULL),(652,1,405,'2018-06-05 08:22:26',NULL),(653,1,406,'2018-06-05 09:03:21',NULL),(654,1,407,'2018-06-05 10:09:40',NULL),(655,1,408,'2018-06-06 07:16:03',NULL),(656,1,409,'2018-06-06 10:06:25',NULL),(657,1,410,'2018-06-08 06:37:14',NULL),(658,1,411,'2018-06-11 01:52:20',NULL),(659,1,412,'2018-06-11 05:48:05',NULL),(660,1,413,'2018-06-11 06:31:51',NULL),(661,1,414,'2018-06-11 08:09:45',NULL),(662,1,415,'2018-06-11 08:09:46',NULL),(663,1,416,'2018-06-11 10:32:03',NULL),(664,1,417,'2018-06-11 11:07:33',NULL),(665,1,152,'2018-06-13 01:58:53',NULL);
/*!40000 ALTER TABLE `new_auth_role_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_auth_user_login_token`
--

DROP TABLE IF EXISTS `new_auth_user_login_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_auth_user_login_token` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL DEFAULT '' COMMENT '用户登录名',
  `token` varchar(200) DEFAULT NULL COMMENT '登录唯一标识',
  `invalid` tinyint(5) NOT NULL DEFAULT '1' COMMENT '是否无效。0：无效；1：有效',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_user_login_token`
--

LOCK TABLES `new_auth_user_login_token` WRITE;
/*!40000 ALTER TABLE `new_auth_user_login_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `new_auth_user_login_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_auth_user_role`
--

DROP TABLE IF EXISTS `new_auth_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_auth_user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL DEFAULT '' COMMENT '用户登录名',
  `roleId` bigint(20) NOT NULL COMMENT '角色id',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_role_unique` (`username`,`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=4046 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_user_role`
--

LOCK TABLES `new_auth_user_role` WRITE;
/*!40000 ALTER TABLE `new_auth_user_role` DISABLE KEYS */;
INSERT INTO `new_auth_user_role` VALUES (3,'liangjian',1,'2016-09-23 06:27:50',NULL),(33,'liangjian',4,'2016-11-01 02:27:30',NULL),(56,'liangjian',5,'2016-11-17 07:03:53',NULL),(1840,'liangjian',6,'2017-09-07 10:29:35',NULL),(4013,'admin',1,'2017-05-11 05:58:20',NULL),(4014,'admin',6,'2017-05-11 05:58:20',NULL),(4026,'admin',4,'2018-06-08 01:05:50',NULL);
/*!40000 ALTER TABLE `new_auth_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_ip_detail`
--

DROP TABLE IF EXISTS `new_ip_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_ip_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ipNetworkId` bigint(20) DEFAULT NULL,
  `serverId` bigint(20) DEFAULT '0' COMMENT '0',
  `ip` varchar(30) NOT NULL,
  `ipType` int(11) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `ipUseType` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_groupId_ip` (`ipNetworkId`,`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=1582 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_ip_detail`
--

LOCK TABLES `new_ip_detail` WRITE;
/*!40000 ALTER TABLE `new_ip_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `new_ip_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_ip_network`
--

DROP TABLE IF EXISTS `new_ip_network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_ip_network` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `serverGroupId` bigint(20) DEFAULT NULL,
  `ipNetwork` varchar(30) NOT NULL,
  `gateWay` varchar(30) NOT NULL,
  `dnsOne` varchar(30) NOT NULL,
  `dnsTwo` varchar(30) DEFAULT NULL,
  `ipType` int(11) NOT NULL,
  `content` varchar(200) DEFAULT NULL,
  `produceMark` varchar(50) NOT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_ip_network`
--

LOCK TABLES `new_ip_network` WRITE;
/*!40000 ALTER TABLE `new_ip_network` DISABLE KEYS */;
INSERT INTO `new_ip_network` VALUES (9,0,'10.0.0.0/8','10.0.0.1','10.117.179.184','10.24.239.232',1,'阿里云内网IP','-1/-1/-1/-1','2016-04-08 07:20:30','2017-04-11 02:31:31'),(10,0,'0.0.0.0','0.0.0.0','10.117.179.184','10.24.239.232',0,'阿里云公网IP','-1/-1/-1/-1','2016-04-11 02:27:13','2017-04-11 02:32:36'),(11,0,'10.17.0.0/23','10.17.1.254','10.16.100.129',NULL,1,'公司内网IP','-1/-1/1/255','2016-04-13 02:40:48','2017-07-28 01:33:57'),(12,0,'172.16.0.0/12','172.16.0.1','8.8.8.8','',1,'阿里云vpc','','2017-06-19 06:18:28',NULL),(13,0,'192.168.0.0/23','192.168.1.253','100.100.2.136','100.100.2.138',1,'','','2018-01-22 05:25:47',NULL);
/*!40000 ALTER TABLE `new_ip_network` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_sg_in_relation`
--

DROP TABLE IF EXISTS `new_sg_in_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_sg_in_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serverGroupId` bigint(20) NOT NULL COMMENT '服务器组',
  `ipNetworkId` bigint(20) NOT NULL COMMENT 'ip组',
  `ipType` int(11) NOT NULL COMMENT 'ip类型',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `server_ip_group_unique` (`serverGroupId`,`ipNetworkId`)
) ENGINE=InnoDB AUTO_INCREMENT=309 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_sg_in_relation`
--

LOCK TABLES `new_sg_in_relation` WRITE;
/*!40000 ALTER TABLE `new_sg_in_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `new_sg_in_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `new_vm_server`
--

DROP TABLE IF EXISTS `new_vm_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `new_vm_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(256) DEFAULT NULL,
  `serverName` varchar(45) DEFAULT NULL COMMENT 'vm名称',
  `vmName` varchar(100) DEFAULT NULL,
  `insideIp` varchar(45) DEFAULT NULL,
  `publicIp` varchar(45) DEFAULT NULL,
  `cpu` int(11) DEFAULT NULL COMMENT 'cpu核数',
  `memory` int(11) DEFAULT NULL,
  `serverId` bigint(20) DEFAULT NULL COMMENT 'server表pk',
  `status` int(3) DEFAULT NULL COMMENT '通电状态(poweredOff|poweredOn|suspended)',
  `gmtCreate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `insideIp_UNIQUE` (`insideIp`)
) ENGINE=InnoDB AUTO_INCREMENT=455 DEFAULT CHARSET=utf8 COMMENT='office vm 数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_vm_server`
--

LOCK TABLES `new_vm_server` WRITE;
/*!40000 ALTER TABLE `new_vm_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `new_vm_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_ecs_image`
--

DROP TABLE IF EXISTS `oc_aliyun_ecs_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_aliyun_ecs_image` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `imageId` varchar(100) DEFAULT NULL,
  `imageDesc` varchar(100) DEFAULT NULL,
  `imageType` int(11) DEFAULT NULL,
  `version` varchar(50) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_ecs_image`
--

LOCK TABLES `oc_aliyun_ecs_image` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_ecs_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_aliyun_ecs_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_network`
--

DROP TABLE IF EXISTS `oc_aliyun_network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_aliyun_network` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `networkType` varchar(50) DEFAULT NULL,
  `networkDesc` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_network`
--

LOCK TABLES `oc_aliyun_network` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_network` DISABLE KEYS */;
INSERT INTO `oc_aliyun_network` VALUES (1,'vpc','VPC网络','2017-06-09 09:46:57','2017-06-13 01:37:08'),(2,'classic','经典网络','2017-06-09 09:48:56','2017-06-09 09:49:04');
/*!40000 ALTER TABLE `oc_aliyun_network` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_vpc`
--

DROP TABLE IF EXISTS `oc_aliyun_vpc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_aliyun_vpc` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `networkId` bigint(20) DEFAULT NULL,
  `aliyunVpcId` varchar(50) DEFAULT NULL,
  `vpcDesc` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_vpc`
--

LOCK TABLES `oc_aliyun_vpc` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_vpc` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_aliyun_vpc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_vpc_security_group`
--

DROP TABLE IF EXISTS `oc_aliyun_vpc_security_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_aliyun_vpc_security_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `vpcId` bigint(20) DEFAULT NULL,
  `securityGroupId` varchar(50) DEFAULT NULL,
  `securityGroupDesc` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_vpc_security_group`
--

LOCK TABLES `oc_aliyun_vpc_security_group` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_vpc_security_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_aliyun_vpc_security_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_aliyun_vswitch`
--

DROP TABLE IF EXISTS `oc_aliyun_vswitch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_aliyun_vswitch` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `vpcId` bigint(20) DEFAULT NULL,
  `vswitchId` varchar(100) DEFAULT NULL,
  `vswitchDesc` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_vswitch`
--

LOCK TABLES `oc_aliyun_vswitch` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_vswitch` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_aliyun_vswitch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_ansible_tasks`
--

DROP TABLE IF EXISTS `oc_ansible_tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_ansible_tasks` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `userName` varchar(50) DEFAULT '',
  `serverCnt` int(11) NOT NULL COMMENT '执行指令的服务器数量',
  `taskType` int(1) DEFAULT NULL COMMENT '0:cmd/1:script/2:copy',
  `cmd` varchar(512) NOT NULL DEFAULT '' COMMENT '执行指令',
  `taskScriptId` bigint(20) DEFAULT NULL,
  `finalized` tinyint(1) NOT NULL COMMENT '所有服务器完成',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_ansible_tasks`
--

LOCK TABLES `oc_ansible_tasks` WRITE;
/*!40000 ALTER TABLE `oc_ansible_tasks` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_ansible_tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_ansible_tasks_server`
--

DROP TABLE IF EXISTS `oc_ansible_tasks_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_ansible_tasks_server` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ansibleTaskId` bigint(20) NOT NULL,
  `serverId` bigint(20) NOT NULL,
  `ip` varchar(50) NOT NULL DEFAULT '',
  `returncode` varchar(50) DEFAULT NULL,
  `result` varchar(20) DEFAULT NULL,
  `msg` text,
  `success` tinyint(1) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_ansible_tasks_server`
--

LOCK TABLES `oc_ansible_tasks_server` WRITE;
/*!40000 ALTER TABLE `oc_ansible_tasks_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_ansible_tasks_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_config_center`
--

DROP TABLE IF EXISTS `oc_config_center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_config_center` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `itemGroup` varchar(100) DEFAULT NULL,
  `env` varchar(11) DEFAULT NULL,
  `item` varchar(100) DEFAULT NULL,
  `value` varchar(250) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `gmtCreate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_config_center`
--

LOCK TABLES `oc_config_center` WRITE;
/*!40000 ALTER TABLE `oc_config_center` DISABLE KEYS */;
INSERT INTO `oc_config_center` VALUES (4,'LDAP','online','LDAP_USER_DN','ou=users,ou=system',NULL,'2017-05-24 22:14:41','2017-05-24 22:14:58'),(5,'LDAP','online','LDAP_GROUP_DN','ou=groups,ou=system',NULL,'2017-05-24 22:15:27',NULL),(6,'LDAP','online','LDAP_GROUP_FILTER','bamboo-',NULL,'2017-05-24 22:16:29',NULL),(7,'LDAP','online','LDAP_GROUP_PREFIX','group_',NULL,'2017-05-24 22:16:47','2017-05-24 22:16:56'),(8,'REDIS','online','REDIS_HOST','opsCloud',NULL,'2017-05-30 17:42:37','2018-06-08 07:52:15'),(9,'REDIS','online','REDIS_PORT','6379',NULL,'2017-05-30 17:43:35',NULL),(10,'REDIS','online','REDIS_PASSWD','opsCloud',NULL,'2017-05-30 17:44:03','2018-06-08 07:52:17'),(11,'REDIS','daily','REDIS_HOST','127.0.0.1',NULL,'2017-05-30 17:46:09','2017-05-30 17:46:16'),(12,'REDIS','daily','REDIS_PORT','6379',NULL,'2017-05-30 17:46:20','2017-05-30 17:47:21'),(13,'REDIS','daily','REDIS_PASSWD',NULL,NULL,'2017-05-30 17:47:35','2017-05-30 17:47:39'),(14,'ZABBIX','online','ZABBIX_API_URL','http://zabbix.com/api_jsonrpc.php','','2017-05-30 17:49:02','2018-06-13 08:34:55'),(15,'ZABBIX','online','ZABBIX_API_USER','Admin','','2017-05-30 17:50:41','2018-06-12 07:43:11'),(16,'ZABBIX','online','ZABBIX_API_PASSWD','Password','','2017-05-30 17:51:58','2018-06-13 08:35:03'),(17,'ZABBIX','online','ZABBIX_API_KEY','opsCloud','','2017-05-30 17:57:51','2018-06-08 07:52:25'),(18,'ALIYUN_ECS','online','ALIYUN_ECS_ACCESS_KEY','0000000','Aliyun ECS AccessKey','2017-05-30 17:58:04','2018-06-13 08:35:17'),(19,'ALIYUN_ECS','online','ALIYUN_ECS_ACCESS_SECRET','0000000','Aliyun ECS AccessSecret','2017-05-30 18:47:15','2018-06-13 08:35:21'),(20,'ALIYUN_ECS','online','ALIYUN_ECS_REGION_ID','cn-hangzhou,cn-hongkong,us-west-1','查询的区域:华东1,香港,美国西部1 ... 按此格式添加  regionId1,regionId2,regionId3 ...','2017-05-30 18:48:52','2017-05-30 18:52:03'),(21,'ALIYUN_ECS','online','ALIYUN_ECS_SEARCH_TIME','120','查询时间（分钟）','2017-05-30 18:49:01','2017-05-30 18:52:04'),(22,'ALIYUN_ECS','online','ALIYUN_ECS_IMAGE_ID','m-bp1igpeohje7yu0ahjis','ECS模版机配置，新建的ECS将会用此模版机','2017-05-30 18:49:46','2017-05-30 18:52:16'),(23,'ALIYUN_ECS','online','ALIYUN_ECS_SECURITY_GROUP_ID','opsCloud','安全组id，新建的ecs将会加入此安全组','2017-05-30 18:50:07','2018-06-08 07:52:29'),(24,'ALIYUN_ECS','online','ALIYUN_ECS_PUBLIC_NETWORK_ID','10','ip_network表中的阿里云公网网段','2017-05-30 18:50:12','2017-05-30 18:52:29'),(35,'VCSA','online','VCSA_HOST','vcsa.opscloud','VCSA 服务器域名或ip','2017-05-30 19:59:58','2018-06-08 07:48:22'),(36,'VCSA','online','VCSA_USER','opscloud@opscloud','VCSA 管理员账号','2017-05-30 20:00:15','2018-06-08 07:48:27'),(37,'VCSA','online','VCSA_PASSWD','opscloud','VCSA 管理员密码','2017-05-30 20:00:32','2018-06-08 07:48:28'),(38,'EMAIL','online','EMAIL_HOST','opscloud',NULL,'2017-05-30 21:18:05','2018-06-08 07:48:30'),(39,'EMAIL','online','EMAIL_PORT',NULL,NULL,'2017-05-30 21:18:12','2017-05-30 21:18:46'),(40,'EMAIL','online','EMAIL_USERNAME','opscloud',NULL,'2017-05-30 21:18:21','2018-06-08 07:48:33'),(41,'EMAIL','online','EMAIL_PWD','opscloud',NULL,'2017-05-30 21:18:32','2018-06-08 07:48:36'),(42,'GETWAY','online','GETWAY_HOST_CONF_FILE','/data/www/getway/users/.public/getway_host.conf','','2017-05-30 21:24:45','2018-06-06 06:45:23'),(43,'GETWAY','online','GETWAY_USER_CONF_PATH','/data/www/getway/users','','2017-05-30 21:25:12','2018-06-06 03:59:10'),(44,'GETWAY','online','GETWAY_KEY_PATH','/data/www/getway/keys','','2017-05-30 21:25:44','2018-06-06 03:59:10'),(45,'GETWAY','online','GETWAY_KEY_FILE','/authorized_keys','','2017-05-30 21:26:03','2018-06-06 04:00:44'),(46,'GETWAY','online','GETWAY_KEYSTORE_FILE_PATH','/data/www/getway_key','','2017-05-30 21:26:30','2018-06-06 02:30:14'),(47,'PUBLIC','online','DEPLOY_PATH','/data/www/ROOT/static/deploy','持续集成deploy目录','2017-05-30 21:43:30','2017-05-30 21:48:44'),(48,'PUBLIC','online','TOMCAT_CONFIG_NAME','tomcat_setenv.conf','tomcat配置文件名称','2017-05-30 21:44:00','2017-05-30 21:47:03'),(54,'REDIS','dev','REDIS_HOST','10.17.1.120',NULL,'2017-05-30 22:01:29','2017-05-30 22:01:58'),(55,'REDIS','dev','REDIS_PORT','6379',NULL,'2017-05-30 22:01:31','2017-05-30 22:02:01'),(56,'REDIS','dev','REDIS_PASSWD',NULL,NULL,'2017-05-30 22:01:33','2017-05-30 22:01:51'),(57,'DINGTALK','online','DINGTALK_TOKEN_DEPLOY','',NULL,'2017-05-30 22:56:11','2018-06-08 07:48:47'),(59,'DINGTALK','online','DINGTALK_WEBHOOK','',NULL,'2017-05-30 22:57:46','2018-06-08 07:48:46'),(60,'DINGTALK','daily','DINGTALK_TOKEN_DEPLOY','',NULL,'2017-05-30 22:58:45','2018-06-08 07:48:48'),(61,'DINGTALK','daily','DINGTALK_WEBHOOK','',NULL,'2017-05-30 22:58:47','2018-06-08 07:48:49'),(62,'DINGTALK','dev','DINGTALK_TOKEN_DEPLOY','',NULL,'2017-05-30 22:59:59','2018-06-08 07:48:50'),(63,'DINGTALK','dev','DINGTALK_WEBHOOK','',NULL,'2017-05-30 23:00:01','2018-06-08 07:48:52'),(64,'SHADOWSOCKS','online','SHADOWSOCKS_SERVER_1','','翻墙代理服务器1','2017-05-30 23:16:22','2018-06-08 07:48:52'),(65,'SHADOWSOCKS','online','SHADOWSOCKS_SERVER_2','','翻墙代理服>务器2','2017-05-30 23:17:06','2018-06-08 07:48:57'),(70,'PUBLIC','online','ADMIN_PASSWD','opscloud','初始化系统后的admin密码','2017-05-30 23:33:23','2018-06-08 07:49:01'),(71,'EXPLAIN_CDL','online','EXPLAIN_CDL_APP_ID','71',NULL,'2017-05-30 23:40:35','2017-05-30 23:42:16'),(72,'EXPLAIN_CDL','online','EXPLAIN_CDL_APP_KEY','tvuRKiBFuaE=',NULL,'2017-05-30 23:41:40','2017-05-30 23:42:30'),(73,'EXPLAIN_CDL','online','EXPLAIN_CDL_APP_NAME','sqlaudit',NULL,'2017-05-30 23:41:41','2017-05-30 23:43:05'),(74,'EXPLAIN_CDL','online','EXPLAIN_CDL_ENV','dev',NULL,'2017-05-30 23:41:44','2017-05-30 23:43:10'),(75,'EXPLAIN_CDL','online','EXPLAIN_CDL_GROUP_NAME','group1_audit',NULL,'2017-05-30 23:41:46','2017-05-30 23:43:27'),(76,'EXPLAIN_CDL','online','EXPLAIN_CDL_LOCAL_PATH','/data/www/temp/',NULL,'2017-05-30 23:41:49','2017-05-30 23:43:47'),(77,'EXPLAIN_CDL','online','EXPLAIN_GIT_USERNAME','opscloud',NULL,'2017-05-30 23:43:55','2018-06-08 07:49:10'),(78,'EXPLAIN_CDL','online','EXPLAIN_GIT_PWD','opscloud',NULL,'2017-05-30 23:44:02','2018-06-08 07:49:12'),(79,'ALIYUN_ECS','online','ALIYUN_ECS_VSWITCH_ID','','如果是创建VPC类型的实例，需要指定虚拟交换机的ID,留空则为经典网络','2017-05-31 08:43:18','2018-05-30 02:33:08'),(81,'ANSIBLE','online','ANSIBLE_BIN','/usr/bin/ansible','ansible命令路径','2017-06-02 01:25:10','2018-06-08 09:38:08'),(89,'PUBLIC','online','IPTABLES_WEBSERVICE_PATH','/data/www/ROOT/static/iptables/web-service','web-service的iptables配置目录','2017-06-02 05:20:35','2017-06-02 05:20:52'),(91,'LDAP','online','LDAP_GROUP_JIRA_USERS','jira-users',NULL,'2017-06-22 09:19:42','2017-06-22 09:37:09'),(92,'LDAP','online','LDAP_GROUP_CONFLUENCE_USERS','confluence-users',NULL,'2017-06-22 09:21:16','2017-06-22 09:25:37'),(98,'PUBLIC','online','OFFICE_DMZ_IP_NETWORK_ID','11','办公网络dmz区ipNetworkId(用于vm主机网络配置)','2017-07-28 01:37:04',NULL),(100,'JENKINS','online','JENKINS_HOST','opscloud','jenkins服务器url','2017-12-15 02:00:50','2018-06-08 07:49:21'),(101,'JENKINS','online','JENKINS_USER','opscloud','jenkins登陆账户','2017-12-15 02:00:51','2018-06-08 07:49:19'),(102,'JENKINS','online','JENKINS_PWD','opscloud','jenkins登陆password或Token(推荐)','2017-12-15 02:00:51','2018-06-08 07:49:18'),(103,'JENKINS','online','JENKINS_OSS_BUCKET_FT_ONLINE','img0-cdn','前端构建OSSBucket','2017-12-15 02:01:29','2017-12-15 02:02:22'),(104,'JENKINS','online','JENKINS_FT_BUILD_BRANCH','dev,daily,gray','触发前端构建的branch','2017-12-15 05:08:28','2018-02-01 04:10:32'),(105,'DINGTALK','online','DINGTALK_TOKEN_FT_BUILD','opscloud','前端构建token','2017-12-18 08:39:46','2018-06-08 07:49:24'),(106,'DINGTALK','online','DINGTALK_TOKEN_ANDROID_BUILD','opscloud','Android构建token','2017-12-22 05:23:37','2018-06-08 07:49:25'),(107,'DINGTALK','dev','DINGTALK_TOKEN_ANDROID_BUILD','opscloud','','2017-12-22 06:09:52','2018-06-08 07:49:26'),(108,'DINGTALK','daily','DINGTALK_TOKEN_ANDROID_BUILD','opscloud','','2017-12-22 06:09:57','2018-06-08 07:49:27'),(109,'DINGTALK','dev','DINGTALK_TOKEN_FT_BUILD','opscloud','','2017-12-22 06:10:48','2018-06-08 07:49:28'),(110,'DINGTALK','daily','DINGTALK_TOKEN_FT_BUILD','opscloud','','2017-12-22 06:10:51','2018-06-08 07:49:29'),(111,'JENKINS','online','GITLAB_USER','opscloud','gitlab代码仓库管理员账户','2017-12-27 03:01:35','2018-06-08 07:49:34'),(112,'JENKINS','online','GITLAB_PWD','opscloud','gitlab代码仓库管理员密码','2017-12-27 03:02:33','2018-06-08 07:49:35'),(113,'JENKINS','online','GITLAB_HOST','opscloud','gitlab代码仓库服务器url','2017-12-27 03:03:51','2018-06-08 07:49:42'),(114,'JENKINS','online','STASH_USER','opscloud','stash代码仓库管理员账户','2017-12-27 03:04:55','2018-06-08 07:49:43'),(115,'JENKINS','online','STASH_PWD','opscloud','stash代码仓库管理员账户','2017-12-27 03:05:20','2018-06-08 07:49:44'),(116,'JENKINS','online','STASH_HOST','opscloud','stash代码仓库服务器url','2017-12-27 03:05:56','2018-06-08 07:49:47'),(117,'JENKINS','online','REPO_LOCAL_PATH','/data/www/temp','代码仓库本地临时目录','2017-12-27 03:06:56',NULL),(118,'DINGTALK','online','DINGTALK_TOKEN_IOS_BUILD','opscloud','','2017-12-29 02:04:01','2018-06-08 07:49:48'),(119,'DINGTALK','daily','DINGTALK_TOKEN_IOS_BUILD','opscloud','','2017-12-29 02:04:08','2018-06-08 07:49:49'),(120,'DINGTALK','dev','DINGTALK_TOKEN_IOS_BUILD','opscloud','','2017-12-29 02:04:11','2018-06-08 07:49:49'),(121,'JENKINS','online','ANDROID_DEBUG_URL','opscloud','android bebug接口 映射到 115.236.161.19 8889','2018-01-10 08:13:36','2018-06-08 07:49:53'),(123,'ALIYUN_ECS','online','ALIYUN_FINANCE_ECS_ACCESS_KEY','opscloud','Aliyun FINANCE ECS AccessKey','2018-05-08 03:30:08','2018-06-08 07:49:54'),(124,'ALIYUN_ECS','online','ALIYUN_FINANCE_ECS_ACCESS_SECRET','opscloud','Aliyun FINANCE ECS AccessSecret','2018-05-08 03:30:55','2018-06-08 07:49:55'),(125,'ALIYUN_ECS','online','ALIYUN_FINANCE_ECS_REGION_ID','opscloud','查询的区域:华东1(ch-hangzhou),华北1（cn-qingdao),华东2金融(cn-shanghai-finance-1),华南1金融(cn-shenzhen-finance-1)','2018-05-08 03:32:10','2018-06-08 07:51:58'),(140,'ANSIBLE','online','ANSIBLE_TASK_SCRIPTS_PATH','/data/www/data/scrips','','2018-05-22 11:06:39',NULL),(142,'ANSIBLE','dev','ANSIBLE_BIN','/usr/local/Cellar/ansible/2.4.3.0/bin/ansible','','2018-06-07 03:10:47','2018-06-12 03:15:42'),(143,'ANSIBLE','dev','ANSIBLE_TASK_SCRIPTS_PATH','/data/www/data/scrips','','2018-06-07 03:11:12','2018-06-07 03:11:20'),(144,'ZABBIX','online','ZABBIX_API_VERSION','3.0','API版本（目前支持3.0，3.4）','2018-06-11 12:10:13','2018-06-11 12:10:33');
/*!40000 ALTER TABLE `oc_config_center` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_config_file`
--

DROP TABLE IF EXISTS `oc_config_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_config_file` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `fileName` varchar(50) NOT NULL DEFAULT '' COMMENT '文件名',
  `fileDesc` varchar(100) DEFAULT NULL COMMENT '文件说明',
  `filePath` varchar(200) NOT NULL DEFAULT '' COMMENT '文件路径',
  `fileType` tinyint(5) NOT NULL DEFAULT '0' COMMENT '文件类型.0：自定义类型；1：系统保留类型',
  `invokeCmd` varchar(200) NOT NULL DEFAULT '0' COMMENT 'invoke cmd',
  `params` varchar(200) DEFAULT NULL COMMENT 'invoke参数',
  `fileContent` text COMMENT '内容',
  `fileGroupId` bigint(20) NOT NULL COMMENT '文件组id',
  `envType` int(11) DEFAULT '4',
  `useType` int(11) NOT NULL DEFAULT '0',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_config_file`
--

LOCK TABLES `oc_config_file` WRITE;
/*!40000 ALTER TABLE `oc_config_file` DISABLE KEYS */;
INSERT INTO `oc_config_file` VALUES (7,'shadowsocks.json','翻墙用户配置','/data/www/shadowsocks/',1,'/usr/local/prometheus/invoke/shadowsocks_invoke','[\"shadowsocks\",\"0\"]',NULL,14,4,0,'2016-12-29 11:21:30','2018-06-11 02:41:15'),(36,'getway_host.conf','Getway全局配置文件（系统内置请勿删除）','/data/www/getway/users/.public ',1,'','[]',NULL,13,0,0,'2018-06-08 15:22:14',NULL),(37,'ansible_hosts_all','全局主机配置文件','/data/www/ansible/',1,'','[]',NULL,12,0,0,'2018-06-08 15:25:14',NULL),(38,'ansible_hosts','','/data/www/ansible/',1,'','[]',NULL,12,0,2,'2018-06-08 15:25:52',NULL);
/*!40000 ALTER TABLE `oc_config_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_config_file_copy`
--

DROP TABLE IF EXISTS `oc_config_file_copy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_config_file_copy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupId` bigint(20) NOT NULL,
  `groupName` varchar(50) DEFAULT NULL,
  `serverId` bigint(20) NOT NULL,
  `src` varchar(200) NOT NULL DEFAULT '',
  `dest` varchar(200) NOT NULL DEFAULT '',
  `username` varchar(50) NOT NULL DEFAULT 'root' COMMENT 'owner',
  `usergroup` varchar(50) NOT NULL DEFAULT 'root' COMMENT 'group',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_config_file_copy`
--

LOCK TABLES `oc_config_file_copy` WRITE;
/*!40000 ALTER TABLE `oc_config_file_copy` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_config_file_copy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_config_file_copy_do_script`
--

DROP TABLE IF EXISTS `oc_config_file_copy_do_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_config_file_copy_do_script` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupName` varchar(50) DEFAULT NULL,
  `copyId` bigint(20) NOT NULL,
  `taskScriptId` bigint(20) NOT NULL,
  `params` varchar(255) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `copyId` (`copyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_config_file_copy_do_script`
--

LOCK TABLES `oc_config_file_copy_do_script` WRITE;
/*!40000 ALTER TABLE `oc_config_file_copy_do_script` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_config_file_copy_do_script` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_config_file_group`
--

DROP TABLE IF EXISTS `oc_config_file_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_config_file_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupName` varchar(50) DEFAULT NULL COMMENT '组名称',
  `groupDesc` varchar(200) DEFAULT NULL COMMENT '组描述',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique` (`groupName`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_config_file_group`
--

LOCK TABLES `oc_config_file_group` WRITE;
/*!40000 ALTER TABLE `oc_config_file_group` DISABLE KEYS */;
INSERT INTO `oc_config_file_group` VALUES (5,'标准配置文件','shadowsocks配置文件组','2016-12-29 11:20:53','2018-06-11 01:53:14'),(12,'filegroup_ansible','Ansible主机配置文件组','2018-06-04 08:23:47',NULL),(13,'filegroup_getway','Getway主机配置文件组','2018-06-05 10:13:40',NULL),(14,'filegroup_ss','shadowsocks用户配置文件组','2018-06-11 01:54:49',NULL);
/*!40000 ALTER TABLE `oc_config_file_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_config_property`
--

DROP TABLE IF EXISTS `oc_config_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_config_property` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `proName` varchar(100) NOT NULL DEFAULT '' COMMENT '属性名称',
  `proValue` varchar(200) DEFAULT NULL COMMENT '属性默认值',
  `proDesc` varchar(100) DEFAULT NULL COMMENT '属性描述',
  `groupId` bigint(20) NOT NULL COMMENT '组id',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_group_unique` (`proName`,`groupId`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_config_property`
--

LOCK TABLES `oc_config_property` WRITE;
/*!40000 ALTER TABLE `oc_config_property` DISABLE KEYS */;
INSERT INTO `oc_config_property` VALUES (1,'TOMCAT_APP_NAME_OPT',NULL,'tomcat名称',1,'2016-10-19 07:37:26','2016-10-27 07:24:56'),(2,'JAVA_INSTALL_VERSION','','JDK版本',1,'2016-10-19 07:37:27','2017-05-24 03:45:22'),(3,'TOMCAT_INSTALL_VERSION','','Tomcat版本',1,'2016-10-19 07:37:27','2017-05-24 03:45:26'),(4,'HOST_NAME','','服务器组名称',2,'2016-10-19 07:42:44','2016-12-20 03:51:04'),(5,'DNS_MASTER','','nameserver1',2,'2016-10-19 07:42:44','2016-12-20 03:51:30'),(6,'DNS_SLAVE','','nameserver2',2,'2016-10-19 07:42:44','2016-12-20 03:51:45'),(7,'tomcat.setenv.conf.url',NULL,'tomcat配置文件url',1,'2016-10-19 07:46:17',NULL),(22,'TOMCAT_HTTP_PORT_OPT','8080','http端口',1,'2016-10-19 07:59:01','2016-10-27 05:56:46'),(23,'TOMCAT_SHUTDOWN_PORT_OPT','8000','shutdown端口',1,'2016-10-19 07:59:01','2016-10-27 05:57:07'),(24,'TOMCAT_JMX_rmiRegistryPortPlatform_OPT','10000','JMX端口:默认10000',1,'2016-10-19 07:59:01','2016-10-27 05:57:22'),(25,'TOMCAT_JMX_rmiServerPortPlatform_OPT','10100','JMX端口:默认10100',1,'2016-10-19 07:59:01','2016-10-27 05:57:35'),(26,'TOMCAT_SERVERXML_WEBAPPSPATH_OPT',NULL,'webapps路径配置',1,'2016-10-19 07:59:01','2016-10-27 05:57:55'),(27,'HTTP_STATUS_OPT','webStatus','check页配置',1,'2016-10-19 07:59:01','2016-11-14 09:02:00'),(28,'APP_CONF_NAME_OPT',NULL,'配置文件',1,'2016-10-19 07:59:01','2016-10-27 05:58:26'),(29,'TOMCAT_HTTP_URI_ENCODING','utf8','http端口编码:默认utf8',1,'2016-10-19 07:59:01','2016-10-27 05:59:08'),(30,'OPEN_TOMCAT_JAVA_OPTS','true','是否打开启动参数配置',1,'2016-10-19 07:59:01','2016-11-24 08:01:25'),(31,'TOMCAT_JAVA_OPTS','-Dfile.encoding=UTF-8 -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=9999,server=y,suspend=n','启动参数配置',1,'2016-10-19 07:59:01','2016-11-24 08:01:16'),(32,'HTTP_STATUS_TIME','5','健康检查等待时间:默认5秒',1,'2016-10-19 07:59:01','2016-10-27 06:00:13'),(33,'SET_JVM_Xss',NULL,'Xss',1,'2016-10-19 07:59:01','2016-10-27 06:00:50'),(34,'SET_JVM_Xms',NULL,'Xms',1,'2016-10-19 07:59:01','2016-10-27 06:00:50'),(35,'SET_JVM_Xmx',NULL,'Xmx',1,'2016-10-19 07:59:01','2016-10-27 06:00:50'),(36,'SET_JVM_Xmn',NULL,'Xmn',1,'2016-10-19 07:59:01','2016-10-27 06:00:50'),(40,'PROJECT_NAME',NULL,'项目名称',3,'2016-10-19 08:05:00','2016-10-27 07:20:39'),(41,'HOST_GROUP',NULL,'主机组前缀',3,'2016-10-19 08:05:00','2016-10-27 07:20:39'),(42,'ENVIRONMENTEL','daily:gray:production','环境配置',3,'2016-10-19 08:05:00','2016-12-14 09:47:37'),(43,'TOMCAT_PROJECT_CONF_DOWNLOAD_NAME',NULL,'持续集成持久化的配置文件:基本废弃',3,'2016-10-19 08:05:00','2016-10-27 07:22:17'),(45,'IPTABLES_GROUP','','iptables组名称',9,'2016-12-06 02:39:34',NULL),(46,'ZABBIX_PROXY','','zabbix代理服务器名称',10,'2016-12-20 05:12:22','2018-06-04 02:58:33'),(48,'ZABBIX_HOST_MONITOR_PUBLIC_IP','false','是否使用公网IP监控',10,'2016-12-20 05:15:59',NULL),(49,'GETWAY_HOST_SSH_PUBLIC_IP','false','是否使用公网ip进行ssh连接',11,'2016-12-20 05:20:44','2017-05-24 03:35:40'),(50,'OS','Linux','操作系统类型',2,'2017-01-03 05:47:50',NULL),(51,'OS_VERSION','CentOS6.x','操作系统版本',2,'2017-01-03 05:48:27',NULL),(52,'PASSWD',NULL,'登陆密码',2,'2017-01-03 05:48:43',NULL),(53,'ZABBIX_DISK_DATA_VOLUME_NAME','','数据盘卷标',10,'2017-02-28 07:39:34','2017-02-28 07:40:05'),(54,'ZABBIX_DISK_SYS_VOLUME_NAME','','系统盘卷标',10,'2017-02-28 07:39:56',NULL),(55,'NGINX_URL_ALIAS','','location中的路径别名，默认为PROJECT_NAME相同',12,'2017-03-09 06:32:38','2017-03-17 03:42:42'),(56,'NGINX_LOCATION_BUILD','true','是否生成标准location',12,'2017-03-09 06:34:00',NULL),(57,'NGINX_UPSTREAM_NAME','','upstream中的名称（空则为项目名）；例如upstream.name.java',12,'2017-03-13 10:05:29',NULL),(58,'NGINX_UPSTREAM_BUILD','true','是否生成标准的upstream配置',12,'2017-03-14 02:25:33',NULL),(59,'NGINX_LOCATION_MANAGE_BUILD','false','是否生成标准(manage.51xianqu.com)后台location',12,'2017-03-14 06:15:56',NULL),(60,'NGINX_LOCATION_MANAGE_BACK','false','manage中用back环境对应location',12,'2017-03-14 07:04:44',NULL),(61,'NGINX_UPSTREAM_GRAY_IS_PROD','false','upstream灰度与线上一致',12,'2017-03-15 03:02:25',NULL),(62,'NGINX_LOCATION_LIMIT_REQ','','location limit_req配置',12,'2017-03-17 03:25:29',NULL),(63,'NGINX_PROXY_PASS_TAIL','','proxy_pass中的后部路径例如proxy_pass http://upstream.api.java/allegan/;',12,'2017-03-17 03:44:12',NULL),(64,'IPTABLES_DUBBO_BUILD','true','是否生成dubbo的iptables配置',13,'2017-03-27 09:10:24',NULL),(65,'NGINX_LOCATION_KA_BUILD','false','是否生成一号管家的location',12,'2017-06-06 09:20:26',NULL),(66,'IPTABLES_DUBBO_GRAY_IS_PROD','false','dubbo白名单中线上环境开通灰度白名单',13,'2017-06-29 06:32:58',NULL),(67,'NGINX_LOCATION_PARAM','','location中的扩展参数',12,'2017-07-07 06:05:12',NULL),(68,'NGINX_UPSTREAM_MAX_FAILS','','允许请求失败的次数默认为1。当超过最大次数时，返回proxy_next_upstream 模块定义的错误',12,'2017-07-26 07:25:19',NULL),(69,'NGINX_UPSTREAM_FAIL_TIMEOUT','','max_fails次失败后，暂停的时间。',12,'2017-07-26 07:27:38',NULL),(70,'NGINX_UPSTREAM_WEIGHT','','权重,默认为1。 weight越大，负载的权重就越大。',12,'2017-07-26 07:36:50',NULL),(71,'NGINX_UPSTREAM_SERVER_TYPE','','down 表示单前的server暂时不参与负载;backup 备用服务器, 其它所有的非backup机器down或者忙的时候，请求backup机器。所以这台机器压力会最轻。',12,'2017-07-26 07:39:12',NULL),(72,'NGINX_LOCATION_SUPPLIER_BUILD','false','是否生成供应商的location',12,'2017-09-22 02:30:11',NULL),(73,'ZABBIX_TEMPLATES','','模版名称（多个用,分割）',10,'2018-06-04 01:29:21','2018-06-04 01:29:41');
/*!40000 ALTER TABLE `oc_config_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_config_property_group`
--

DROP TABLE IF EXISTS `oc_config_property_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_config_property_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupName` varchar(100) NOT NULL DEFAULT '' COMMENT '组名称',
  `groupDesc` varchar(200) DEFAULT NULL COMMENT '组描述',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique` (`groupName`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_config_property_group`
--

LOCK TABLES `oc_config_property_group` WRITE;
/*!40000 ALTER TABLE `oc_config_property_group` DISABLE KEYS */;
INSERT INTO `oc_config_property_group` VALUES (1,'Tomcat','Tomcat和JDK属性组','2016-10-19 07:25:52','2016-10-26 06:47:38'),(2,'Server','服务器组属性','2016-10-19 07:35:58',NULL),(3,'InterfaceCI','持续集成接口','2016-10-19 07:44:42',NULL),(9,'Iptables','网络集中管理配置','2016-12-06 02:37:52',NULL),(10,'Zabbix','[SERVER]Zabbix监控属性组','2016-12-20 05:10:59','2016-12-20 05:11:19'),(11,'Getway','堡垒机配置','2016-12-20 05:19:27',NULL),(12,'Nginx','nginx配置项','2017-03-09 06:31:02',NULL),(13,'IptablesDubbo','dubbo的iptables配置','2017-03-27 09:09:16',NULL);
/*!40000 ALTER TABLE `oc_config_property_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_ecs_server`
--

DROP TABLE IF EXISTS `oc_ecs_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_ecs_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(256) DEFAULT NULL,
  `creationTime` timestamp NULL DEFAULT NULL COMMENT 'ECS创建时间',
  `deviceAvailable` tinyint(1) DEFAULT NULL COMMENT '是否可用',
  `serverName` varchar(45) DEFAULT NULL COMMENT '实例名称',
  `insideIp` varchar(45) NOT NULL,
  `publicIp` varchar(45) DEFAULT NULL,
  `internetMaxBandwidthOut` int(11) DEFAULT NULL COMMENT '带宽(Mbps)',
  `cpu` int(11) DEFAULT NULL COMMENT 'cpu核数',
  `memory` int(11) DEFAULT NULL,
  `systemDiskCategory` varchar(50) DEFAULT NULL,
  `systemDiskSize` int(11) DEFAULT NULL,
  `dataDiskCategory` varchar(50) DEFAULT NULL,
  `dataDiskSize` int(11) DEFAULT NULL,
  `instanceId` varchar(45) DEFAULT NULL COMMENT '此数据只记录无需可见',
  `area` varchar(45) DEFAULT NULL COMMENT '可用区 Id',
  `finance` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0:阿里云,1:金融云',
  `regionId` varchar(45) DEFAULT NULL COMMENT 'RegionId',
  `serverId` bigint(20) DEFAULT '0' COMMENT 'server表pk',
  `ioOptimized` tinyint(1) DEFAULT NULL COMMENT '是否io优化实例',
  `internetChargeType` varchar(20) DEFAULT NULL,
  `status` int(3) DEFAULT '0' COMMENT '当前状态\n0 新增（未关联）\n1 关联  server表id\n2 下线（阿里云不存在）\n3 删除  (手动删除)',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `insideIp_UNIQUE` (`insideIp`)
) ENGINE=InnoDB AUTO_INCREMENT=311 DEFAULT CHARSET=utf8 COMMENT='阿里云ECS数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_ecs_server`
--

LOCK TABLES `oc_ecs_server` WRITE;
/*!40000 ALTER TABLE `oc_ecs_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_ecs_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_nginx_vhosts`
--

DROP TABLE IF EXISTS `oc_nginx_vhosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_nginx_vhosts` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `server_name` varchar(100) NOT NULL DEFAULT '' COMMENT '域名',
  `content` varchar(255) DEFAULT '',
  `vhost_path` varchar(255) NOT NULL DEFAULT '' COMMENT '虚拟主机配置文件目录',
  `server_key` varchar(50) NOT NULL DEFAULT '' COMMENT '虚拟主机key',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_nginx_vhosts`
--

LOCK TABLES `oc_nginx_vhosts` WRITE;
/*!40000 ALTER TABLE `oc_nginx_vhosts` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_nginx_vhosts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server`
--

DROP TABLE IF EXISTS `oc_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `serverGroupId` bigint(20) DEFAULT NULL,
  `loginType` int(11) DEFAULT NULL,
  `loginUser` varchar(30) DEFAULT NULL,
  `envType` int(11) DEFAULT NULL,
  `publicIp` varchar(20) DEFAULT NULL,
  `publicIpId` bigint(20) DEFAULT NULL,
  `insideIp` varchar(20) DEFAULT NULL,
  `insideIpId` bigint(20) DEFAULT NULL,
  `serverType` int(11) DEFAULT NULL,
  `serverName` varchar(200) DEFAULT NULL,
  `area` varchar(30) DEFAULT NULL,
  `useType` int(11) DEFAULT NULL,
  `serialNumber` varchar(100) DEFAULT NULL,
  `ciGroup` varchar(100) DEFAULT NULL,
  `content` varchar(50) DEFAULT NULL,
  `zabbixStatus` int(3) DEFAULT NULL,
  `zabbixMonitor` int(3) DEFAULT '0',
  `extTomcatVersion` varchar(100) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server`
--

LOCK TABLES `oc_server` WRITE;
/*!40000 ALTER TABLE `oc_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_group`
--

DROP TABLE IF EXISTS `oc_server_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_server_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL DEFAULT '',
  `content` varchar(50) DEFAULT NULL,
  `ipNetwork` varchar(30) DEFAULT NULL,
  `useType` int(11) NOT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_group`
--

LOCK TABLES `oc_server_group` WRITE;
/*!40000 ALTER TABLE `oc_server_group` DISABLE KEYS */;
INSERT INTO `oc_server_group` VALUES (1,'group_getway','',NULL,12,'2018-06-07 09:11:21','2018-06-10 10:27:49');
/*!40000 ALTER TABLE `oc_server_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_server_group_useType`
--

DROP TABLE IF EXISTS `oc_server_group_useType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_server_group_useType` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `useType` int(11) NOT NULL,
  `typeName` varchar(50) NOT NULL DEFAULT '',
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `typeName` (`typeName`),
  UNIQUE KEY `useType` (`useType`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='服务器组使用类型配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_group_useType`
--

LOCK TABLES `oc_server_group_useType` WRITE;
/*!40000 ALTER TABLE `oc_server_group_useType` DISABLE KEYS */;
INSERT INTO `oc_server_group_useType` VALUES (1,1,'zookeeper','','2018-05-29 05:52:02','2018-05-29 08:26:10'),(2,2,'web-service',NULL,'2018-05-29 05:52:14','2018-05-29 05:52:26'),(3,3,'mysql',NULL,'2018-05-29 05:52:38',NULL),(4,4,'other',NULL,'2018-05-29 05:52:50',NULL),(5,5,'webphp',NULL,'2018-05-29 05:53:01',NULL),(6,6,'public',NULL,'2018-05-29 05:53:18',NULL),(7,7,'redis',NULL,'2018-05-29 05:53:26','2018-05-29 05:54:09'),(8,8,'web-server',NULL,'2018-05-29 05:53:44','2018-05-29 05:54:06'),(9,9,'front-end',NULL,'2018-05-29 05:54:20',NULL),(10,10,'bi',NULL,'2018-05-29 05:54:27',NULL),(13,11,'testType','','2018-05-29 09:42:25',NULL),(14,0,'all','保留类型（请勿删除）','2018-06-04 10:30:07',NULL),(15,12,'getway','终端跳板机','2018-06-10 10:27:12',NULL);
/*!40000 ALTER TABLE `oc_server_group_useType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_servergroup_properties`
--

DROP TABLE IF EXISTS `oc_servergroup_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_servergroup_properties` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupId` bigint(20) DEFAULT NULL COMMENT '服务器组Id',
  `serverId` bigint(20) DEFAULT NULL,
  `propertyId` bigint(20) DEFAULT NULL COMMENT '属性id',
  `propertyValue` varchar(2048) DEFAULT NULL COMMENT '属性值',
  `propertyGroupId` bigint(20) DEFAULT NULL COMMENT '属性组id',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`propertyId`,`serverId`,`groupId`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_servergroup_properties`
--

LOCK TABLES `oc_servergroup_properties` WRITE;
/*!40000 ALTER TABLE `oc_servergroup_properties` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_servergroup_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_task_script`
--

DROP TABLE IF EXISTS `oc_task_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_task_script` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `scriptName` varchar(100) NOT NULL DEFAULT '',
  `content` varchar(150) DEFAULT NULL,
  `userId` bigint(20) NOT NULL COMMENT '创建者',
  `username` varchar(50) DEFAULT NULL COMMENT '创建者',
  `script` text COMMENT '脚本内容',
  `scriptType` int(1) DEFAULT NULL COMMENT '脚本类型0:私有 1:公开',
  `sysScript` int(1) DEFAULT NULL COMMENT '1系统脚本',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_task_script`
--

LOCK TABLES `oc_task_script` WRITE;
/*!40000 ALTER TABLE `oc_task_script` DISABLE KEYS */;
INSERT INTO `oc_task_script` VALUES (1,'init_system_ecs','初始化系统(ecs)',0,'liangjian','#!/bin/bash\n\n# 修改DNS\n/usr/local/prometheus/bin/system/chg_dns 10.117.179.184 10.24.239.232\n# 更新框架\n/usr/local/prometheus/bin/update\n# 初始化数据盘并挂载\n/usr/local/prometheus/bin/system/auto_fdisk_x\n# 安装abbix_agentd\n/usr/local/prometheus/bin/system/install_zabbix_proxy3',0,1,'2018-05-22 10:51:29','2018-06-07 09:02:51'),(2,'install_tomcat','安装tomcat',0,'liangjian','#!/bin/bash\n\n# 安装tomcat\n/usr/local/prometheus/bin/install_tomcat -a',0,1,'2018-05-22 10:54:47','2018-06-07 09:02:52'),(3,'prometheus_update','更新框架',0,'liangjian','#!/bin/bash\n\nif [  -f \'/usr/local/prometheus/bin/update\'  ]  ;  then\n  /usr/local/prometheus/bin/update\n  exit 0\nfi\n\nif [  -f \'/usr/local/prometheus/bin/prometheus_update\'  ]  ;  then\n  /usr/local/prometheus/bin/prometheus_update  >/dev/null 2>&1\n  exit 0\nfi',0,1,'2018-05-23 03:06:15','2018-06-13 08:10:22'),(4,'nginx_cleanup_logs','清理nginx日志',0,'liangjian','#!/bin/bash\n\nRETENTION_LOG_DAY=$1\n\nif [[  \".\" ==  \".${RETENTION_LOG_DAY}\"  ]]; then\n   RETENTION_LOG_DAY=5\nfi\n\nfunction delLogs(){\n  local logPath=$1\n  local logType=$2\n  logPath=`dirname ${logPath}`\n  find ${logPath} -mtime +${RETENTION_LOG_DAY} -type f -name ${logType} -exec rm -f {} \\;\n  #find ${logPath} -mtime +${RETENTION_LOG_DAY} -type f -name *access.log.*.log -exec rm -f {} \\;\n}\n\ndelLogs /data/www/logs/ \'*access.log.*.log\' ;\ndelLogs /data/www/logs/ \'*error.log.*.log\' ;',0,1,'2018-05-23 05:14:05','2018-06-07 09:02:53'),(5,'install_logtail','安装阿里云日志采集服务logtail',0,'liangjian','#!/bin/bash\n\nwget http://logtail-release.oss-cn-hangzhou-internal.aliyuncs.com/linux64/logtail.sh; chmod 755 logtail.sh; sh logtail.sh install cn_hangzhou',0,1,'2018-05-24 05:19:20','2018-06-07 09:02:54'),(7,'getway_set_login','配置Getway登录限制（系统内置命令请勿修改）',0,'liangjian','# 参数说明\n# 指定getway命令路径 -p=/data/www/getway/users \n\nfunction doParam(){\n  local param=\n  [ $# == 0 ] && return ;\n  for param in \"$@\" ; do\n      local paramName=`echo ${param} | awk -F\'=\' \'{print $1}\'`\n      local paramValue=`echo ${param} | awk -F\'=\' \'{print $NF}\'`\n      case \"${paramName}\" in\n          -p)\n              GETWAY_USER_CONF_PATH=${paramValue} ;;\n      esac\n  done\n}\n\n\nGETWAY_USER_CONF_PATH=/data/www/getway/users\n\ndoParam $@ ;\necho $@\n\ncat << EOF >> /etc/profile\n# Getway Login\ncase \"\\${USER}\" in\n       root|admin|manage|xqadmin)\n          ;;\n       *)\n          echo \"Welcome \\$USER\"\n          ${GETWAY_USER_CONF_PATH}/../getway ;;\nesac\nEOF\n\ntail -n 8 /etc/profile',0,1,'2018-06-06 08:24:19','2018-06-13 06:07:12'),(8,'getway','getway源码（系统内置，不要删除）',0,'liangjian','#!/bin/bash\n\n#===============================================================================\n#   SYSTEM REQUIRED:  Linux Centos6\n#   DESCRIPTION:  ssh server\n#   AUTHOR: Liang jian\n#   QQ:  80327044\n#===============================================================================\n# 2016-11-23\n# 新增 按d进入daily服务器 按g进入gray\n# 2017-10-23\n# 新增 getway登陆统计\n\nfunction config(){\n  #-------配置项--------------------------\n  DEBUG_FILE_NAME=$(basename $0).log\n  # 加载函数\n  # test \".${PROM_HOME}\" = . && PROMETHEUS_HOME=/usr/local/prometheus\n  # if [ -r \"${PROM_HOME}/funcs/funcs\" ] ; then\n  #   . ${PROM_HOME}/funcs/funcs\n  # else\n  #   echo \"${PROM_HOME}/funcs/funcs not found\"\n  #   exit_logout ;\n  # fi\n  # 业务服务器通用账户\n  SERVER_LOGIN=manage\n  # ctrl+c滚动\n  stty eof ^C\n  # 禁止中断 ctrl+c\n  trap \'\' INT\n  # stty intr undef\n  # 支持退格\n  # stty erase ^H\n  # 加载用户配置文件\n  # 用户目录\n  GETWAY_USER_CONF_PATH=\'/data/www/getway/users\'\n  #loadConf \"/data/www/getway/$USER/getway.conf\"\n  #loadConf \"/data/www/getway/.public/getway_host.conf\"\n  #SSHS_TMP_FILE=\"`head -1 /dev/urandom |md5sum | awk \'{print $1}\'`.sshs.tmp\"\n\n  #登录统计接口\n  CMDB_GETWAY_STATUS_API=\"http://opscloudSever/box/getway/status\"\n\n  GETWAY_VERSION=\"1.0.1 2016-11-23\"\n  HOSTGROUP=\n  HOSTIP=\n\n  # 计算配置行\n  HOSTGROUP_OPT_CNT=$(( ${#HOSTGROUP_OPT[*]} / 3 ))\n  HOST_OPT=\n  HOST_OPT_CNT=\n  DIVIDING_LINE=\"----------------------------------------------------------------------------------------------------------\"\n}\n\n\nfunction doParam(){\n  local param=\n  [ $# == 0 ] && return ;\n  for param in \"$@\" ; do\n      local paramName=`echo ${param} | awk -F\'=\' \'{print $1}\'`\n      local paramValue=`echo ${param} | awk -F\'=\' \'{print $NF}\'`\n      case \"${paramName}\" in\n          -i)\n              install_getway ;;\n      esac\n  done\n  test \".${LOGIN_USER}\" = . && exit 1 ;\n}\n\nfunction echoPlus(){\n  # 增强显示\n  # @param color  (31|91 红  32|92绿  33|93黄 34|94蓝 35|95紫 36|96天蓝)\n  # @param content\n  local colorCode=$1\n  local content=$2\n  case ${colorCode} in\n    31|91|error|err)\n            colorCode=31\n            debug [ERROR] ${content} ;;\n    32|92|info)\n            colorCode=32 \n            ## debug [INFO] ${content} \n            ;; \n  esac\n  IFS=$\'\\t\\n\'\n  #test  \".`pwd`\" = \"./home/xqadmin\" && echo \"${content}\" && return\n  echo -e \"\\e[${colorCode}m${content}\\e[0m\"\n}\n\n\n\nfunction reload_conf(){\n    . \"${GETWAY_USER_CONF_PATH}/$USER/getway.conf\" ;\n    . \"${GETWAY_USER_CONF_PATH}/.public/getway_host.conf\" ;\n    HOSTGROUP_OPT_CNT=$(( ${#HOSTGROUP_OPT[*]} / 3 ))\n}\n\n#----------通用----------\nfunction acq_hostgroup_value(){\n  local name=$1\n  local returnType=$2\n  local index=`acq_hostgroup_index_by_name ${name}`\n  local value=`acq_hostgroup_opt ${returnType} ${index}`\n  echo ${value}\n}\n\nfunction acq_hostgroup_index_by_name(){\n  local name=$1\n  local i\n  for((i=0;i<${HOSTGROUP_OPT_CNT};i++));do\n     if [ ${name} == `acq_hostgroup_opt name $i` ] ; then\n        echo $i ;\n        break ;\n     fi\n  done\n}\n\nfunction acq_hostgroup_opt(){\n  # 提取 JAVA_PROJECTS_OPT 中的矩阵数据\n  # @param project_name:host_group:environmental:properties\n  # @param index\n  # return file or key or value\n  local getType=$1\n  local index=$2\n  IFS=$\'\\n\\t\'\n  case \"${getType}\" in\n       name)\n          index=$(( ${index} * 3 ))     ;;\n       content)  \n          index=$(( ${index} * 3 + 1 )) ;;\n       color)\n          index=$(( ${index} * 3 + 2 )) ;;\n  esac\n  echo ${HOSTGROUP_OPT[${index}]}\n}\n\nfunction acq_host_value(){\n  local name=$1\n  local returnType=$2\n  local index=`acq_host_index_by_name ${name}`\n  local value=`acq_host_opt ${returnType} ${index}`\n  echo ${value}\n}\n\nfunction acq_host_index_by_name(){\n  local name=$1\n  local i\n  for((i=0;i<${HOST_OPT_CNT};i++));do\n     if [ ${name} == `acq_host_opt ip $i` ] ; then\n        echo $i ;\n        break ;\n     fi\n  done\n}\n\nfunction acq_host_opt(){\n  # 提取 JAVA_PROJECTS_OPT 中的矩阵数据\n  # @param ip:hostname:hostgroup:environmental:login\n  # @param index\n  # return file or key or value\n  local getType=$1\n  local index=$2\n  IFS=$\'\\n\\t\'\n  case \"${getType}\" in\n       ip)\n          index=$(( ${index} * 5 ))     ;;\n       hostname)  \n          index=$(( ${index} * 5 + 1 )) ;;\n       hostgroup)\n          index=$(( ${index} * 5 + 2 )) ;;\n       environment)\n          index=$(( ${index} * 5 + 3 )) ;;\n       login)\n          index=$(( ${index} * 5 + 4 )) ;;\n  esac\n  echo ${HOST_OPT[${index}]}\n}\n\n#----------通用----------\n\nfunction print_format_hostgroup(){\n  # 增强显示\n  # @param index\n  # @param name\n  # @param content\n  # @param color\n  local index=$1\n  local name=$2\n  local content=$3\n  local color=$4\n  IFS=$\'\\n\\t\'\n  if [ $# == 0 ] ; then\n     printf \"\\e[32m%-15s %-25s %-25s\\e[0m\\n\" ID NAME CONTENT\n  else\n     printf \"%-15s \\e[${color}m%-25s\\e[0m %-25s\\n\" $index $name $content\n  fi\n}\n\nfunction echo_hostgroup(){\n  local i name content color index\n  echo $DIVIDING_LINE\n  print_format_hostgroup ;\n  echo $DIVIDING_LINE\n  for((i=0;i<${HOSTGROUP_OPT_CNT};i++)) ;do\n     name=${HOSTGROUP_OPT[$(( $i * 3 ))]}\n     content=`acq_hostgroup_value $name content`\n     color=`acq_hostgroup_value $name color`\n     index=$(( $i + 1 ))\n     print_format_hostgroup ${index} ${name} ${content} ${color} ;\n  done\n  echo $DIVIDING_LINE\n}\n\nfunction select_hostgroup(){\n  local name\n  while true\n  do\n    echo_hostgroup ;\n    unset hostgroup_id\n    read -p \"please select hostgroup id: \" hostgroup_id\n    case \"${hostgroup_id}\" in\n          r|l) reload_conf ;;\n    esac\n    test `is_numbers ${hostgroup_id}` = 0 && continue ;\n    if [ \"${hostgroup_id}\" -ge \"1\" ] && [ \"${hostgroup_id}\" -le \"${HOSTGROUP_OPT_CNT}\" ];then\n       name=${HOSTGROUP_OPT[$(( ( ${hostgroup_id} - 1 ) * 3 ))]}\n       echo -e \"You select hostgroup : \\e[32m$name\\e[0m\"\n       HOSTGROUP=${name}\n       # 赋值被选中的主机组\n       local hostgroup_name=`eval echo \"$\\{\"\"HOSTGROUP_\"\"${HOSTGROUP}\"\"[*]\\}\"`\n       IFS=$\' \\n\\t\'\n       HOST_OPT=( `eval echo $hostgroup_name` )\n       HOST_OPT_CNT=$(( ${#HOST_OPT[*]} / 5 ))\n       select_host ;\n    else\n       echo \"The wrong choice !\" ;\n    fi\n  done  \n}\n\nfunction echo_host(){\n  local i index ip hostname hostgroup environment login\n  echo $DIVIDING_LINE\n  print_format_host ;\n  echo $DIVIDING_LINE\n  #[ -f \"/data/www/temp/sshs.tmp\" ] && rm -f /data/www/temp/${SSHS_TMP_FILE}\n  for((i=0;i<${HOST_OPT_CNT};i++)) ;do\n     ip=${HOST_OPT[$(( $i * 5 ))]}\n     hostgroup=`acq_host_value $ip hostgroup`\n     index=$(( $index + 1 ))\n     environment=`acq_host_value $ip environment`\n     hostname=`acq_host_value $ip hostname`\n     print_format_host $index $ip $environment $hostname\n  done\n  echo $DIVIDING_LINE\n}\n\nfunction print_format_host(){\n  # 增强显示\n  local index=$1\n  local ip=$2\n  local environment=$3\n  local hostname=$4\n  local color\n  IFS=$\'\\n\\t\'\n  if [ $# == 0 ] ; then\n     printf \"\\e[32m%-5s %-25s %-25s %-50s\\e[0m\\n\" ID IP ENVIRONMENT HOSTNAME/CONTENT\n  else\n     case \"${environment}\" in\n          production) color=31 ;;\n          gray) color=33 ;;\n          daily) color=32 ;;\n          *) color=0 ;;\n     esac\n     printf \"%-5s %-25s \\e[${color}m%-25s\\e[0m %-50s\\n\" $index $ip $environment $hostname\n  fi\n}\n\nfunction select_host_(){\n  local select_env=$1\n  # 快速进入daily/gray\n  local id=0\n  local ip environment\n  for((i=0;i<${HOST_OPT_CNT};i++)) ;do\n     ip=${HOST_OPT[$(( $i * 5 ))]}\n     id=$(( $id + 1 ))\n     environment=`acq_host_value $ip environment`\n     if [[  ${select_env} == ${environment} ]] ; then\n        echo $id\n        return ;\n     fi \n  done\n  echo 0 ;\n}\n\nfunction select_host(){\n  while true\n  do\n    unset host_id\n    echo_host ;\n    read -p \"please select host id: \" host_id\n    # 快速进入daily/gray服务器\n    case \"${host_id}\" in\n          d|daily) host_id=`select_host_ daily` ;;\n          g|gray)  host_id=`select_host_ gray` ;;\n          r|l)     reload_conf ;;\n    esac\n    test `is_numbers ${host_id}` = 0 && break ;\n    if [ \"${host_id}\" -ge \"1\" ] && [ \"${host_id}\" -le \"${HOST_OPT_CNT}\" ];then\n       local index=$(( ( $host_id - 1 ) * 5   ))\n       HOSTIP=${HOST_OPT[${index}]}\n       echo -e \"You select host_ip : \\e[32m$HOSTIP\\e[0m\"\n       # 统计接口 需要配置才能启用\n       # cmdb_getway_status $USER $HOSTIP;\n       ssh_host ;\n    else\n       echo \"The wrong choice !\";\n    fi\n  done  \n}\n\nfunction is_numbers(){\n  # 判断字符串是否为数字\n  # return 1  数字\n  # return 0  非数字\n  local numbers=$1\n  echo \"${numbers}\" | [ -n \"`sed -n \'/^[0-9][0-9]*$/p\'`\" ] && echo 1 || echo 0\n}\n\n\nfunction ssh_host(){\n  local login=`acq_host_value $HOSTIP login`\n  local login_type=`awk -F: \'{print $1}\' <<< $login`\n  local value=`awk -F: \'{print $NF}\' <<< $login`\n  case \"${login_type}\" in\n          KEY) \n             ssh_host_key $HOSTIP ;;\n          PASSWD) \n             [ \".${value}\" == \".\" ]\n             ssh_host_passwd $HOSTIP $value;;\n  esac\n}\n\nfunction exit_logout(){\n  # 严重错误发生则登出系统\n  logout ;\n}\n\n\nfunction ssh_host_passwd(){\n  local ip=$1\n  local passwd=$2\n  sshpass -C -p ${passwd} ssh -o StrictHostKeyChecking=no -o GSSAPIAuthentication=no root@$ip\n}\n\nfunction ssh_host_key(){\n  local ip=$1\n  if [ -f \"/home/$USER/.ssh/.secret_id_rsa\" ] ; then\n     ssh -C -o StrictHostKeyChecking=no -i /home/$USER/.ssh/.secret_id_rsa -o GSSAPIAuthentication=no ${SERVER_LOGIN}@$ip\n  else\n     ssh -C -o StrictHostKeyChecking=no -o GSSAPIAuthentication=no ${SERVER_LOGIN}@$ip\n  fi  \n}\n\nfunction getway_info(){\n  echo $DIVIDING_LINE\n  echoPlus info \"GetwayVersion : $GETWAY_VERSION\"\n  echoPlus 92 \"LonginUser : $CN    Name : $NAME    Email : $EMAIL\" ;\n}\n\nfunction cmdb_getway_status(){\n  # @param\n\n  local username=$1\n  local ip=$2\n\n  local param=\"username=${username}&ip=${ip}\"\n  echo \"User=${username};LoginServerIp=${ip}\" >> /data/www/logs/getway/getway.log\n  \n  curl --connect-timeout 3 -d \"${param}\" \"${CMDB_GETWAY_STATUS_API}\" >> /data/www/logs/getway/getway.log 2>&1\n  #curl -d \"username=liangjian&ip=10.17.1.124\"  \"http://opscloudserver.com/box/getway/status\"\n}\n\nfunction install_getway(){\n   if [ -r \"${GETWAY_USER_CONF_PATH}/../getway\" ] ; then\n      rm -f ${GETWAY_USER_CONF_PATH}/../getway\n   fi\n   echo \"Getway local path=$(dirname \"$0\")/$(basename $0)\"\n   \\cp $(dirname \"$0\")/$(basename $0) ${GETWAY_USER_CONF_PATH}/../getway\n   chmod +x ${GETWAY_USER_CONF_PATH}/../getway\n   exit 0;\n}\n\nconfig ;\ndoParam $@ ;\nreload_conf ;\ngetway_info ;\nselect_hostgroup ;',0,1,'2018-06-06 11:18:38','2018-06-08 06:09:39'),(9,'getway_account','getway授权命令（系统内置，不要删除）',0,'liangjian','#!/bin/bash\n\n#===============================================================================\n#   SYSTEM REQUIRED:  Linux Centos6\n#   DESCRIPTION:  System Account\n#   AUTHOR: Liang jian\n#   QQ:  80327044\n#   2015-03\n#===============================================================================\n\n# getway_account -u=liangjian -p=9f4975327825bb7fb42adff8066dd36f -kp=/data/www/getway/keys -up=/data/www/getway/users\n# 创建/修改账户 -u=账户 -p=密码\n# getway_account -d=liangjian\n# 删除账户 -d=账户\n\nfunction config(){\n\n  AUTH_KEY_NAME=authorized_keys\n  DEBUG_FILE_NAME=$(basename $0).log\n\n  GETWAY_KEY_PATH=/data/www/getway/keys\n  GETWAY_USER_CONF_PATH=/data/www/getway/users\n\n  #GETWAY_PATH=/data/www/getway\n  #KEY_PATH=/data/www/getway/key\n\n  LOGIN_USER=\n\n  # 留空随机生成128位长度密码\n  PASSWORD=\n\n}\n\nfunction doParam(){\n  local param=\n  [ $# == 0 ] && return ;\n  for param in \"$@\" ; do\n      local paramName=`echo ${param} | awk -F\'=\' \'{print $1}\'`\n      local paramValue=`echo ${param} | awk -F\'=\' \'{print $NF}\'`\n      case \"${paramName}\" in\n          -u|-user)\n              LOGIN_USER=${paramValue} \n             echo \"lOGIN_USER=\"${lOGIN_USER}\n             ;;\n          -p|-password)\n              PASSWORD=${paramValue} ;;\n          -d)\n              LOGIN_USER=${paramValue} \n              account_del ;;  \n          -kp)\n              GETWAY_KEY_PATH=${paramValue}\n               echo \"GETWAY_KEY_PATH=\"${GETWAY_KEY_PATH}\n              ;;\n          -up)\n              GETWAY_USER_CONF_PATH=${paramValue}\n              echo \"GETWAY_USER_CONF_PATH=\"${GETWAY_USER_CONF_PATH}\n              ;;  \n          -h|-help)\n              # prom_help $(basename $0) \n              exit 0 ;;\n      esac\n  done\n  test \".${LOGIN_USER}\" = . && exit 1 ;\n}\n\nfunction checkRoot(){\n  # 检查用户是否为root\n  if [ $(id -u) != \"0\" ]; then\n     echoPlus 31 \"Error: You must be root to run this script.\"\n     exit 1\n  fi\n}\n\nfunction acqRandomPassword(){\n  if [ \".${PASSWORD}\" == \".\" ] ; then\n     PASSWORD=`head -n 5 /dev/urandom | md5sum | awk \'{print $1}\'`\n  fi  \n  echo ${PASSWORD}\n}\n\nfunction execMkdir(){\n  # 创建目录\n  # @Param dir\n  local dir=$1\n  [ -d ${dir} ] || mkdir -p ${dir} >/dev/null 2>&1\n}\n\nfunction account_add(){\n  check ;\n  case \"${LOGIN_USER}\" in\n       root|xqadmin|manage|admin)\n          echo \"错误:不能创建管理员\" ;\n          exit 1 ;;\n  esac\n  # 判断用户是否存在，若存在只修改密码\n  local account_exist=`exists_user`\n  test $account_exist = 0 && useradd ${LOGIN_USER};\n  local passwd=`acqRandomPassword`\n  # echo \"password:${passwd}\"\n  #mkdir -p /data/www/temp\n  echo \"${passwd}\" | passwd --stdin \"${LOGIN_USER}\" >/dev/null 2>&1\n}\n\nfunction account_del(){\n  case \"${LOGIN_USER}\" in\n       root|xqadmin|manage|admin)\n          echo \"错误:不能删除管理员\" ;\n          exit 1 ;;\n  esac\n  # 判断用户是否存在，若存在只修改密码\n  local account_exist=`exists_user`\n  test $account_exist = 1 && userdel -r -f ${LOGIN_USER};\n  # echoPlus info \"remove user ${LOGIN_USER}\" ;\n  echo \"成功:\"`hostname`;\n  exit 0\n}\n\nfunction exists_user(){\n  # 判断用户是否存在\n  # 0:不存在\n  # 1:存在\n  # check user\n  id ${LOGIN_USER} >& /dev/null  \n  if [ $? -ne 0 ]; then  \n     echo 0\n  else\n     echo 1   \n  fi \n}\n\nfunction check(){\n  # 判断用户公钥是否存在，如果不存在则可用密码登陆\n  test -f ${GETWAY_KEY_PATH}/${LOGIN_USER}/${AUTH_KEY_NAME} || echo \"错误:公钥不存在\" ;\n  # 判断用户私钥是否存在，非必需\n  # test -f ${KEY_PATH}/${LOGIN_USER}/id_rsa || echoPlus info \"id_rsa not found !\" ;\n  test -f ${GETWAY_USER_CONF_PATH}/${LOGIN_USER}/getway.conf || error_exit \"配置文件不存在\" ;\n}\n\nfunction error_exit(){\n  local msg=$1\n  echo \"错误:$msg\"\n  exit 1 ; \n}\n\nfunction build(){\n  # @Param  authorized_keys file /home/${USER}/.ssh/${AUTH_KEY_NAME}\n  execMkdir /home/${LOGIN_USER}/.ssh\n  cat ${GETWAY_KEY_PATH}/${LOGIN_USER}/${AUTH_KEY_NAME}  > /home/${LOGIN_USER}/.ssh/${AUTH_KEY_NAME}\n  cat ${GETWAY_KEY_PATH}/manage/id_rsa  > /home/${LOGIN_USER}/.ssh/.secret_id_rsa\n  if [ -f ${GETWAY_KEY_PATH}/${LOGIN_USER}/id_rsa ] ; then\n     cat ${GETWAY_KEY_PATH}/${LOGIN_USER}/id_rsa  > /home/${LOGIN_USER}/.ssh/id_rsa\n     chmod 600 /home/${LOGIN_USER}/.ssh/id_rsa\n  fi\n\n  chown -R ${LOGIN_USER} /home/${LOGIN_USER}\n  chmod 600 /home/${LOGIN_USER}/.ssh/${AUTH_KEY_NAME}\n  chmod 600 /home/${LOGIN_USER}/.ssh/.secret_id_rsa\n}\n\n#代码开始\n\n# 读取全局配置项\nconfig ;\n# 检查执行用户是否为root\ncheckRoot ;\ndoParam $@ ;\n# 配置账户\naccount_add ;\nbuild ;',0,1,'2018-06-06 11:40:40','2018-06-08 03:04:31'),(10,'shadowsocks_service','shadowsocks启动服务',2425,'liangjian','#!/bin/bash\nservice supervisord start',0,1,'2018-06-11 05:02:48',NULL);
/*!40000 ALTER TABLE `oc_task_script` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_zabbix_template`
--

DROP TABLE IF EXISTS `oc_zabbix_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_zabbix_template` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `templateName` varchar(100) NOT NULL DEFAULT '',
  `templateid` varchar(10) DEFAULT NULL,
  `enabled` int(1) NOT NULL COMMENT '是否启用',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `templateName` (`templateName`)
) ENGINE=InnoDB AUTO_INCREMENT=325 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_zabbix_template`
--

LOCK TABLES `oc_zabbix_template` WRITE;
/*!40000 ALTER TABLE `oc_zabbix_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_zabbix_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `physical_server`
--

DROP TABLE IF EXISTS `physical_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `physical_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(256) DEFAULT NULL,
  `serverName` varchar(45) DEFAULT NULL,
  `insideIp` varchar(45) DEFAULT NULL,
  `publicIp` varchar(45) DEFAULT NULL,
  `remoteManagementIp` varchar(45) DEFAULT NULL COMMENT '远程管理ip',
  `cpu` int(11) DEFAULT NULL COMMENT 'cpu核心数量',
  `memory` int(11) DEFAULT NULL,
  `cpuCnt` int(11) DEFAULT NULL COMMENT '物理cpu颗数',
  `cpuModel` varchar(256) DEFAULT NULL COMMENT 'cpu型号',
  `serverId` bigint(20) DEFAULT NULL,
  `useType` int(11) NOT NULL DEFAULT '0',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `systemAssetTag` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `physical_server`
--

LOCK TABLES `physical_server` WRITE;
/*!40000 ALTER TABLE `physical_server` DISABLE KEYS */;
/*!40000 ALTER TABLE `physical_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_heartbeat`
--

DROP TABLE IF EXISTS `project_heartbeat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_heartbeat` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `pmId` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `heartbeatType` int(11) NOT NULL DEFAULT '0',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_heartbeat`
--

LOCK TABLES `project_heartbeat` WRITE;
/*!40000 ALTER TABLE `project_heartbeat` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_heartbeat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_management`
--

DROP TABLE IF EXISTS `project_management`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_management` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `projectName` varchar(100) DEFAULT NULL COMMENT '项目名称',
  `content` varchar(200) DEFAULT NULL,
  `beginTime` timestamp NULL DEFAULT NULL COMMENT '项目开始时间',
  `projectType` int(11) NOT NULL DEFAULT '0' COMMENT '项目类型0:长期；1短期',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '项目状态0 正常',
  `ttl` int(11) NOT NULL DEFAULT '30' COMMENT '生命周期（天）',
  `leaderUserId` bigint(20) DEFAULT NULL COMMENT 'ProjectLeader',
  `leaderUsername` varchar(100) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_management`
--

LOCK TABLES `project_management` WRITE;
/*!40000 ALTER TABLE `project_management` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_management` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_property`
--

DROP TABLE IF EXISTS `project_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_property` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `pmId` bigint(20) NOT NULL,
  `propertyType` int(11) NOT NULL COMMENT '类型0:userId;1:serverGroupId',
  `propertyValue` bigint(20) NOT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pmId` (`pmId`,`propertyType`,`propertyValue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_property`
--

LOCK TABLES `project_property` WRITE;
/*!40000 ALTER TABLE `project_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repository`
--

DROP TABLE IF EXISTS `repository`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repository` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `repositoryUrl` varchar(200) NOT NULL DEFAULT '',
  `repositoryType` int(1) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `repositoryUrl` (`repositoryUrl`),
  KEY `repositoryUrl_2` (`repositoryUrl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repository`
--

LOCK TABLES `repository` WRITE;
/*!40000 ALTER TABLE `repository` DISABLE KEYS */;
/*!40000 ALTER TABLE `repository` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repository_refs`
--

DROP TABLE IF EXISTS `repository_refs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repository_refs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `repositoryId` bigint(20) NOT NULL,
  `refType` int(1) NOT NULL,
  `ref` varchar(255) NOT NULL DEFAULT '',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repository_refs`
--

LOCK TABLES `repository_refs` WRITE;
/*!40000 ALTER TABLE `repository_refs` DISABLE KEYS */;
/*!40000 ALTER TABLE `repository_refs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scm_permissions`
--

DROP TABLE IF EXISTS `scm_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scm_permissions` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ciUserGroupId` bigint(20) NOT NULL COMMENT '持续集成用户组id',
  `scmProject` varchar(100) NOT NULL COMMENT '代码仓库项目名',
  `groupName` varchar(100) DEFAULT NULL COMMENT '持续集成组名',
  `scmDescription` varchar(255) DEFAULT NULL COMMENT '描述',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `scmProject` (`scmProject`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scm_permissions`
--

LOCK TABLES `scm_permissions` WRITE;
/*!40000 ALTER TABLE `scm_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `scm_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo`
--

DROP TABLE IF EXISTS `todo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoGroupId` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `todoStatus` int(1) NOT NULL DEFAULT '0' COMMENT '工单状态 0正常 1暂时关闭 2开发中',
  `title` varchar(100) NOT NULL DEFAULT '',
  `content` varchar(200) DEFAULT NULL,
  `helpLink` varchar(250) DEFAULT NULL COMMENT '帮助链接',
  `todoType` int(1) NOT NULL DEFAULT '0' COMMENT '工单类型 0:运维类',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `isApproval` tinyint(1) NOT NULL DEFAULT '0' COMMENT '需要审批',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo`
--

LOCK TABLES `todo` WRITE;
/*!40000 ALTER TABLE `todo` DISABLE KEYS */;
INSERT INTO `todo` VALUES (1,1,'堡垒机(持续集成)权限申请',0,'堡垒机(持续集成)权限申请',NULL,NULL,0,'2017-09-05 10:25:25','2017-11-16 10:05:19',0),(2,1,'持续集成权限申请 (后端)',0,'持续集成权限申请(后端)','',NULL,0,'2017-09-05 10:27:48','2018-01-12 02:59:56',0),(3,1,'平台权限申请',0,'平台权限申请','JIRA、NEXUS...',NULL,0,'2017-09-09 01:27:07','2017-11-16 10:05:11',0),(4,1,'杭州总部VPN申请',0,'杭州总部VPN申请','办公网VPN','http://wiki.51xianqu.net/pages/viewpage.action?pageId=10913086',0,'2017-11-06 02:42:38','2017-11-16 10:05:09',0),(5,2,'新项目申请',0,'新项目申请','Java/Tomcat类',NULL,0,'2017-11-14 02:32:48','2017-11-22 09:27:37',0),(6,1,'持续集成权限申请(前端，QA)',0,'持续集成权限申请(前端，QA)',NULL,NULL,0,'2018-01-12 02:59:31','2018-04-24 06:25:00',0),(7,1,'SCM(stash)权限申请',0,'SCM(stash)权限申请',NULL,NULL,0,'2018-04-11 03:42:12','2018-04-11 03:42:18',0),(8,2,'Tomcat(JDK)版本变更',0,'Tomcat(JDK)版本变更',NULL,'http://wiki.51xianqu.net/pages/viewpage.action?pageId=24090520',0,'2018-04-25 03:12:44','2018-04-26 02:39:24',0);
/*!40000 ALTER TABLE `todo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_builder_plan_detail`
--

DROP TABLE IF EXISTS `todo_builder_plan_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_builder_plan_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoDetailId` bigint(20) NOT NULL,
  `envType` int(11) NOT NULL,
  `branch` varchar(200) DEFAULT NULL,
  `buildTool` int(11) DEFAULT NULL,
  `processStatus` int(11) DEFAULT NULL COMMENT '处理状态',
  `needEnvParams` tinyint(1) DEFAULT NULL,
  `envParams` varchar(200) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `todoDetailId` (`todoDetailId`,`envType`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_builder_plan_detail`
--

LOCK TABLES `todo_builder_plan_detail` WRITE;
/*!40000 ALTER TABLE `todo_builder_plan_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_builder_plan_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_ci_usergroup_detail`
--

DROP TABLE IF EXISTS `todo_ci_usergroup_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_ci_usergroup_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoDetailId` bigint(20) NOT NULL,
  `ciUserGroupId` bigint(20) NOT NULL,
  `ciUserGroupName` varchar(100) DEFAULT NULL,
  `envType` int(11) NOT NULL DEFAULT '0',
  `serverGroupId` bigint(20) NOT NULL,
  `serverGroupName` varchar(100) NOT NULL DEFAULT '',
  `processStatus` int(1) DEFAULT '0' COMMENT '处理状态',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `todoDetailId` (`todoDetailId`,`ciUserGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_ci_usergroup_detail`
--

LOCK TABLES `todo_ci_usergroup_detail` WRITE;
/*!40000 ALTER TABLE `todo_ci_usergroup_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_ci_usergroup_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_config`
--

DROP TABLE IF EXISTS `todo_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `configName` varchar(100) NOT NULL DEFAULT '' COMMENT '配置项名称',
  `parentId` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级id',
  `configStatus` int(11) NOT NULL DEFAULT '0' COMMENT '0：正常；1：逻辑删除',
  `roleId` bigint(20) NOT NULL DEFAULT '0' COMMENT '受理角色id',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_config`
--

LOCK TABLES `todo_config` WRITE;
/*!40000 ALTER TABLE `todo_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_daily`
--

DROP TABLE IF EXISTS `todo_daily`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_daily` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `levelOne` bigint(20) NOT NULL COMMENT '一级类目',
  `levelTwo` bigint(20) NOT NULL COMMENT '二级类目',
  `sponsor` varchar(100) NOT NULL DEFAULT '' COMMENT '工单发起人',
  `privacy` int(11) NOT NULL DEFAULT '0' COMMENT '0：公开；1：私密',
  `urgents` int(11) NOT NULL COMMENT '紧急程度。0：一般；1：重要；2：紧急',
  `contents` text NOT NULL COMMENT '工单内容',
  `feedbackContent` text COMMENT '反馈内容',
  `todoStatus` int(11) NOT NULL DEFAULT '0' COMMENT '工单状态。0：处理中；1：完成；2：待反馈',
  `hasConfirm` int(11) NOT NULL DEFAULT '0' COMMENT '是否确认。0：未确认；1：发起人确认；2：系统确认。',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_daily`
--

LOCK TABLES `todo_daily` WRITE;
/*!40000 ALTER TABLE `todo_daily` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_daily` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_daily_log`
--

DROP TABLE IF EXISTS `todo_daily_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_daily_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `dailyId` bigint(20) NOT NULL COMMENT '日常工单id',
  `processUser` varchar(100) NOT NULL DEFAULT '' COMMENT '处理人',
  `dailyContent` text NOT NULL COMMENT '工单内容',
  `dailyFeedbackContent` text COMMENT '工单反馈内容',
  `todoStatus` int(11) NOT NULL COMMENT '工单状态。0：处理中；1：完成；2：待反馈',
  `sendNotify` int(11) NOT NULL DEFAULT '0' COMMENT '是否发送通知，默认邮件。0：未发送；1：发送',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_daily_log`
--

LOCK TABLES `todo_daily_log` WRITE;
/*!40000 ALTER TABLE `todo_daily_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_daily_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_detail`
--

DROP TABLE IF EXISTS `todo_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `initiatorUserId` bigint(20) NOT NULL COMMENT '发起人userId',
  `initiatorUsername` varchar(100) NOT NULL DEFAULT '' COMMENT '发起人username',
  `todoId` bigint(20) NOT NULL COMMENT '工单id',
  `assigneeUserId` bigint(20) DEFAULT NULL COMMENT '负责人userId',
  `assigneeUsername` varchar(100) DEFAULT NULL COMMENT '负责人username',
  `approvalUserId` bigint(20) DEFAULT NULL COMMENT '审批人userId',
  `approvalUsername` varchar(100) DEFAULT NULL COMMENT '审批人username',
  `todoStatus` int(11) DEFAULT NULL COMMENT '工单状态',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_detail`
--

LOCK TABLES `todo_detail` WRITE;
/*!40000 ALTER TABLE `todo_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_group`
--

DROP TABLE IF EXISTS `todo_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '',
  `content` varchar(200) DEFAULT NULL,
  `groupType` int(11) NOT NULL DEFAULT '0',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_group`
--

LOCK TABLES `todo_group` WRITE;
/*!40000 ALTER TABLE `todo_group` DISABLE KEYS */;
INSERT INTO `todo_group` VALUES (1,'权限申请工单','堡垒机权限、持续集成权限、平台类权限等',0,'2017-09-05 09:55:18','2017-09-05 09:57:56'),(2,'服务器变更工单','项目组服务器配置变更、扩容、缩容、下线等',0,'2017-09-05 09:57:52',NULL);
/*!40000 ALTER TABLE `todo_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_keybox_detail`
--

DROP TABLE IF EXISTS `todo_keybox_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_keybox_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoDetailId` bigint(20) NOT NULL,
  `serverGroupId` bigint(20) NOT NULL,
  `serverGroupName` varchar(100) DEFAULT NULL,
  `ciAuth` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开通CI权限',
  `ciUserGroupId` bigint(20) DEFAULT NULL,
  `ciUserGroupName` varchar(100) DEFAULT NULL,
  `processStatus` int(1) DEFAULT '0' COMMENT '处理状态',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `todoDetailId` (`todoDetailId`,`serverGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_keybox_detail`
--

LOCK TABLES `todo_keybox_detail` WRITE;
/*!40000 ALTER TABLE `todo_keybox_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_keybox_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_new_server_detail`
--

DROP TABLE IF EXISTS `todo_new_server_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_new_server_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoDetailId` bigint(20) NOT NULL,
  `envType` int(11) NOT NULL,
  `instanceType` int(11) DEFAULT NULL,
  `zoneType` int(11) DEFAULT NULL,
  `processStatus` int(11) DEFAULT NULL COMMENT '处理状态',
  `allocateIp` tinyint(1) DEFAULT NULL COMMENT '是否分配公网IP',
  `dataDiskSize` int(11) DEFAULT NULL,
  `serverCnt` int(11) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `todoDetailId` (`todoDetailId`,`envType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_new_server_detail`
--

LOCK TABLES `todo_new_server_detail` WRITE;
/*!40000 ALTER TABLE `todo_new_server_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_new_server_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_property`
--

DROP TABLE IF EXISTS `todo_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_property` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoDetailId` bigint(20) NOT NULL,
  `todoId` bigint(20) NOT NULL,
  `todoName` varchar(100) NOT NULL DEFAULT '',
  `todoKey` varchar(100) NOT NULL DEFAULT '',
  `todoValue` varchar(512) NOT NULL DEFAULT '',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_property`
--

LOCK TABLES `todo_property` WRITE;
/*!40000 ALTER TABLE `todo_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_system_auth_detail`
--

DROP TABLE IF EXISTS `todo_system_auth_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_system_auth_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoDetailId` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
  `systemType` int(1) NOT NULL DEFAULT '0' COMMENT '系统类型0ldap 1getway',
  `content` varchar(200) DEFAULT NULL,
  `authed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '授权',
  `need` tinyint(1) NOT NULL DEFAULT '1' COMMENT '需要授权',
  `processStatus` int(1) DEFAULT '0' COMMENT '处理状态',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `todoDetailId` (`todoDetailId`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_system_auth_detail`
--

LOCK TABLES `todo_system_auth_detail` WRITE;
/*!40000 ALTER TABLE `todo_system_auth_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_system_auth_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_vpn_detail`
--

DROP TABLE IF EXISTS `todo_vpn_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_vpn_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoDetailId` bigint(20) NOT NULL,
  `ldapGroup` varchar(100) NOT NULL DEFAULT '',
  `need` tinyint(1) NOT NULL DEFAULT '1' COMMENT '需要授权',
  `processStatus` int(11) DEFAULT NULL COMMENT '处理状态',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_vpn_detail`
--

LOCK TABLES `todo_vpn_detail` WRITE;
/*!40000 ALTER TABLE `todo_vpn_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo_vpn_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vm_template`
--

DROP TABLE IF EXISTS `vm_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vm_template` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `serverId` bigint(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `cpu` int(11) DEFAULT NULL,
  `memory` int(11) DEFAULT NULL,
  `insideIp` varchar(20) DEFAULT NULL,
  `sysDiskSize` int(11) DEFAULT NULL,
  `dataDiskSize` int(11) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vm_template`
--

LOCK TABLES `vm_template` WRITE;
/*!40000 ALTER TABLE `vm_template` DISABLE KEYS */;
INSERT INTO `vm_template` VALUES (13,351,'centos6.5.vm.template',2,2048,'10.17.1.29',20,NULL,'2017-06-06 01:39:42','2017-06-06 01:47:23');
/*!40000 ALTER TABLE `vm_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-13 16:36:27
