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
) ENGINE=InnoDB AUTO_INCREMENT=2415 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ecs_property`
--

LOCK TABLES `ecs_property` WRITE;
/*!40000 ALTER TABLE `ecs_property` DISABLE KEYS */;
INSERT INTO `ecs_property` VALUES (11,0,'i-bp1hz9wmf07ot8h3qntr',1,'vpc','networkType','2018-06-15 03:00:40',NULL),(12,0,'i-bp1hz9wmf07ot8h3qntr',2,'vpc-bp10aw2gcg7dgendfrd0e','vpcId','2018-06-15 03:00:40',NULL),(13,0,'i-bp1hz9wmf07ot8h3qntr',3,'vsw-bp1o05adi15b6deib97vh','vswitchId','2018-06-15 03:00:40',NULL),(14,0,'i-bp1hz9wmf07ot8h3qntr',4,'sg-bp143u00uhudm7qby0wx','securityGroupId','2018-06-15 03:00:40',NULL),(15,0,'i-bp1hz9wmf07ot8h3qntr',0,'m-bp1iwsh6g2n03w647v0c','imageId','2018-06-15 03:00:40',NULL),(16,0,'i-236wnm8tu',1,'classic','networkType','2018-07-16 07:35:54',NULL),(17,0,'i-236wnm8tu',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:54',NULL),(18,0,'i-236wnm8tu',0,'m-bp191q6ys57i482rr13k','imageId','2018-07-16 07:35:54',NULL),(19,0,'i-23lj4bhdg',1,'classic','networkType','2018-07-16 07:35:54',NULL),(20,0,'i-23lj4bhdg',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:54',NULL),(21,0,'i-23lj4bhdg',0,'m-bp191q6ys57i482rr13k','imageId','2018-07-16 07:35:54',NULL),(22,0,'i-2346gb397',1,'classic','networkType','2018-07-16 07:35:54',NULL),(23,0,'i-2346gb397',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:54',NULL),(24,0,'i-2346gb397',0,'m-bp191q6ys57i482rr13k','imageId','2018-07-16 07:35:54',NULL),(25,0,'i-23df00jqe',1,'classic','networkType','2018-07-16 07:35:54',NULL),(26,0,'i-23df00jqe',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:54',NULL),(27,0,'i-23df00jqe',0,'m-bp191q6ys57i482rr13k','imageId','2018-07-16 07:35:54',NULL),(28,0,'i-bp19meli2wlpx6nsbpqs',1,'classic','networkType','2018-07-16 07:35:54',NULL),(29,0,'i-bp19meli2wlpx6nsbpqs',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:54',NULL),(30,0,'i-bp19meli2wlpx6nsbpqs',0,'m-bp18bjeifyuqg2u4f5h3','imageId','2018-07-16 07:35:54',NULL),(31,0,'i-bp13o4s69sg8dx849ay1',1,'classic','networkType','2018-07-16 07:35:54',NULL),(32,0,'i-bp13o4s69sg8dx849ay1',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:54',NULL),(33,0,'i-bp13o4s69sg8dx849ay1',0,'m-bp18bjeifyuqg2u4f5h3','imageId','2018-07-16 07:35:54',NULL),(34,0,'i-bp19meli2wlpx8mtfrpx',1,'classic','networkType','2018-07-16 07:35:55',NULL),(35,0,'i-bp19meli2wlpx8mtfrpx',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:55',NULL),(36,0,'i-bp19meli2wlpx8mtfrpx',0,'m-bp18bjeifyuqg2u4f5h3','imageId','2018-07-16 07:35:55',NULL),(37,0,'i-23gaa01nd',1,'classic','networkType','2018-07-16 07:35:55',NULL),(38,0,'i-23gaa01nd',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:55',NULL),(39,0,'i-23gaa01nd',0,'m-bp191q6ys57i482rr13k','imageId','2018-07-16 07:35:55',NULL),(40,0,'i-bp10ienb9kpbfx9p7u8k',1,'classic','networkType','2018-07-16 07:35:55',NULL),(41,0,'i-bp10ienb9kpbfx9p7u8k',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:55',NULL),(42,0,'i-bp10ienb9kpbfx9p7u8k',0,'m-bp191q6ys57i482rr13k','imageId','2018-07-16 07:35:55',NULL),(43,0,'i-23n4oq3pm',1,'classic','networkType','2018-07-16 07:35:55',NULL),(44,0,'i-23n4oq3pm',4,'sg-23t7micdg','securityGroupId','2018-07-16 07:35:55',NULL),(45,0,'i-23n4oq3pm',0,'m-23004a2ut','imageId','2018-07-16 07:35:55',NULL),(46,8,'i-bp16lhh4wo6rt3db54lm',1,'vpc','networkType','2018-07-16 07:44:06',NULL),(47,8,'i-bp16lhh4wo6rt3db54lm',2,'vpc-bp10aw2gcg7dgendfrd0e','vpcId','2018-07-16 07:44:06',NULL),(48,8,'i-bp16lhh4wo6rt3db54lm',3,'vsw-bp1o05adi15b6deib97vh','vswitchId','2018-07-16 07:44:06',NULL),(49,8,'i-bp16lhh4wo6rt3db54lm',4,'sg-bp143u00uhudm7qby0wx','securityGroupId','2018-07-16 07:44:06',NULL),(50,8,'i-bp16lhh4wo6rt3db54lm',0,'m-bp1iwsh6g2n03w647v0c','imageId','2018-07-16 07:44:06',NULL),(51,0,'i-23527s4uu',1,'classic','networkType','2018-11-14 07:31:46',NULL),(52,0,'i-23527s4uu',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:46',NULL),(53,0,'i-23527s4uu',0,'m-23004a2ut','imageId','2018-11-14 07:31:46',NULL),(54,0,'i-23b3ic7j1',1,'classic','networkType','2018-11-14 07:31:46',NULL),(55,0,'i-23b3ic7j1',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:46',NULL),(56,0,'i-23b3ic7j1',0,'m-23004a2ut','imageId','2018-11-14 07:31:46',NULL),(57,0,'i-231bs0l7d',1,'classic','networkType','2018-11-14 07:31:46',NULL),(58,0,'i-231bs0l7d',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:46',NULL),(59,0,'i-231bs0l7d',0,'m-23d2ahulk','imageId','2018-11-14 07:31:46',NULL),(60,0,'i-235d115u3',1,'classic','networkType','2018-11-14 07:31:47',NULL),(61,0,'i-235d115u3',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:47',NULL),(62,0,'i-235d115u3',0,'m-23004a2ut','imageId','2018-11-14 07:31:47',NULL),(63,0,'i-23zcp1vq0',1,'classic','networkType','2018-11-14 07:31:47',NULL),(64,0,'i-23zcp1vq0',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:47',NULL),(65,0,'i-23zcp1vq0',0,'centos6u5_64_20G_aliaegis_20140703.vhd','imageId','2018-11-14 07:31:47',NULL),(66,0,'i-bp11y9049u2admfx2ejs',1,'classic','networkType','2018-11-14 07:31:47',NULL),(67,0,'i-bp11y9049u2admfx2ejs',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:47',NULL),(68,0,'i-bp11y9049u2admfx2ejs',0,'m-bp191q6ys57i482rr13k','imageId','2018-11-14 07:31:47',NULL),(69,0,'i-bp1d5x21qsokgmjkhj26',1,'classic','networkType','2018-11-14 07:31:47',NULL),(70,0,'i-bp1d5x21qsokgmjkhj26',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:47',NULL),(71,0,'i-bp1d5x21qsokgmjkhj26',0,'m-bp191q6ys57i482rr13k','imageId','2018-11-14 07:31:47',NULL),(72,0,'i-bp1a69rj03ub2docoe9x',1,'classic','networkType','2018-11-14 07:31:47',NULL),(73,0,'i-bp1a69rj03ub2docoe9x',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:47',NULL),(74,0,'i-bp1a69rj03ub2docoe9x',0,'m-231ifxzn7','imageId','2018-11-14 07:31:47',NULL),(75,0,'i-bp1jdk51ejf6ulgoax22',1,'classic','networkType','2018-11-14 07:31:48',NULL),(76,0,'i-bp1jdk51ejf6ulgoax22',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:48',NULL),(77,0,'i-bp1jdk51ejf6ulgoax22',0,'m-231ifxzn7','imageId','2018-11-14 07:31:48',NULL),(78,0,'i-bp1gd3xwhcctn3th3rkb',1,'classic','networkType','2018-11-14 07:31:48',NULL),(79,0,'i-bp1gd3xwhcctn3th3rkb',4,'sg-23t7micdg','securityGroupId','2018-11-14 07:31:48',NULL),(80,0,'i-bp1gd3xwhcctn3th3rkb',0,'m-231ifxzn7','imageId','2018-11-14 07:31:48',NULL);
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
INSERT INTO `keybox_application_key` VALUES (2,'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','BDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmBDnBZYeMY+HSvw8En4Yl5gQ5wWWHjGPh0r8PBJ+GJeYEOcFlh4xj4dK/DwSfhiXmduqGNYLDFtQ8DKOgglH7PA==',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keybox_login_status`
--

LOCK TABLES `keybox_login_status` WRITE;
/*!40000 ALTER TABLE `keybox_login_status` DISABLE KEYS */;
INSERT INTO `keybox_login_status` VALUES (151,0,'admin',9,0,'2018-08-07 08:05:33',NULL),(152,0,'admin',8,0,'2018-08-07 08:05:39',NULL),(153,0,'admin',10,0,'2018-08-07 08:13:10',NULL),(154,0,'admin',10,0,'2018-08-07 08:15:53',NULL),(155,0,'admin',10,0,'2018-08-07 08:16:37',NULL),(156,0,'admin',9,0,'2018-11-15 09:42:18',NULL),(157,0,'admin',8,0,'2018-11-15 09:42:24',NULL),(158,0,'admin',10,0,'2018-11-15 09:42:30',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=556 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_resource_group`
--

LOCK TABLES `new_auth_resource_group` WRITE;
/*!40000 ALTER TABLE `new_auth_resource_group` DISABLE KEYS */;
INSERT INTO `new_auth_resource_group` VALUES (14,1,1,'2016-09-22 02:45:41',NULL),(36,5,29,'2016-09-22 12:09:36',NULL),(37,5,30,'2016-09-22 12:10:04',NULL),(38,5,31,'2016-09-22 12:10:23',NULL),(39,6,28,'2016-09-22 12:12:10',NULL),(40,6,27,'2016-09-22 12:12:22',NULL),(41,6,22,'2016-09-22 12:12:35',NULL),(42,6,11,'2016-09-22 12:14:40',NULL),(43,6,12,'2016-09-22 12:14:49',NULL),(44,6,13,'2016-09-22 12:14:54',NULL),(45,6,14,'2016-09-22 12:15:31',NULL),(46,6,15,'2016-09-22 12:15:37',NULL),(47,6,16,'2016-09-22 12:15:42',NULL),(48,6,17,'2016-09-22 12:16:07',NULL),(49,6,18,'2016-09-22 12:16:12',NULL),(50,6,19,'2016-09-22 12:16:16',NULL),(51,6,20,'2016-09-22 12:16:21',NULL),(52,6,21,'2016-09-22 12:16:28',NULL),(53,6,23,'2016-09-22 12:16:37',NULL),(54,6,24,'2016-09-22 12:16:42',NULL),(55,6,25,'2016-09-22 12:16:47',NULL),(56,6,26,'2016-09-22 12:16:51',NULL),(57,7,32,'2016-09-22 12:17:51',NULL),(58,7,33,'2016-09-22 12:23:33',NULL),(59,7,34,'2016-09-22 12:24:56',NULL),(60,7,35,'2016-09-22 12:25:28',NULL),(61,7,36,'2016-09-22 12:25:50',NULL),(62,8,37,'2016-09-22 12:28:28',NULL),(63,8,38,'2016-09-22 12:28:49',NULL),(64,8,39,'2016-09-22 12:29:19',NULL),(65,9,40,'2016-09-22 12:29:41',NULL),(66,9,41,'2016-09-22 12:30:07',NULL),(67,9,42,'2016-09-22 12:30:37',NULL),(68,9,43,'2016-09-22 12:31:05',NULL),(69,9,44,'2016-09-22 12:32:28',NULL),(70,0,45,'2016-09-23 02:32:53',NULL),(71,6,46,'2016-09-23 03:32:32',NULL),(72,5,47,'2016-09-23 09:33:04',NULL),(73,6,48,'2016-09-29 06:28:29',NULL),(74,1,49,'2016-10-08 06:56:51',NULL),(75,10,50,'2016-10-08 07:28:33',NULL),(76,10,51,'2016-10-08 07:29:01',NULL),(77,10,52,'2016-10-08 07:29:21',NULL),(83,11,58,'2016-10-10 11:09:05',NULL),(84,11,59,'2016-10-11 08:22:59',NULL),(85,11,60,'2016-10-11 08:42:21',NULL),(86,11,61,'2016-10-12 07:28:59',NULL),(97,9,71,'2016-10-25 03:20:45',NULL),(107,1,54,'2016-10-27 08:07:53',NULL),(108,11,54,'2016-10-27 08:07:53',NULL),(111,1,10,'2016-10-27 08:08:32',NULL),(112,6,10,'2016-10-27 08:08:32',NULL),(113,1,9,'2016-10-27 08:08:39',NULL),(114,6,9,'2016-10-27 08:08:39',NULL),(115,1,8,'2016-10-27 08:08:45',NULL),(116,6,8,'2016-10-27 08:08:45',NULL),(117,1,7,'2016-10-27 08:09:07',NULL),(118,5,7,'2016-10-27 08:09:07',NULL),(119,1,4,'2016-10-27 08:09:15',NULL),(120,7,4,'2016-10-27 08:09:15',NULL),(121,1,3,'2016-10-27 08:09:23',NULL),(122,8,3,'2016-10-27 08:09:23',NULL),(123,1,2,'2016-10-27 08:09:31',NULL),(124,9,2,'2016-10-27 08:09:31',NULL),(125,1,53,'2016-10-27 08:25:33',NULL),(126,13,53,'2016-10-27 08:25:33',NULL),(127,13,55,'2016-10-27 08:25:52',NULL),(128,13,56,'2016-10-27 08:27:18',NULL),(129,13,57,'2016-10-27 08:27:28',NULL),(130,12,69,'2016-10-27 08:33:09',NULL),(131,12,68,'2016-10-27 09:31:49',NULL),(132,12,67,'2016-10-27 09:32:03',NULL),(133,1,63,'2016-10-27 09:32:19',NULL),(134,12,63,'2016-10-27 09:32:19',NULL),(135,12,66,'2016-10-27 09:32:40',NULL),(136,12,65,'2016-10-27 09:32:49',NULL),(137,12,64,'2016-10-27 09:32:59',NULL),(138,9,73,'2016-10-31 06:59:16',NULL),(140,9,74,'2016-10-31 11:35:39',NULL),(141,9,75,'2016-10-31 11:49:17',NULL),(142,14,76,'2016-11-28 04:58:25',NULL),(143,14,77,'2016-11-28 04:58:44',NULL),(144,14,78,'2016-11-28 04:59:03',NULL),(145,14,79,'2016-11-28 04:59:32',NULL),(146,14,80,'2016-11-28 04:59:51',NULL),(147,14,81,'2016-11-28 05:00:08',NULL),(148,14,82,'2016-11-28 05:00:24',NULL),(149,15,83,'2016-11-28 05:01:03',NULL),(150,14,84,'2016-11-28 05:01:26',NULL),(151,14,85,'2016-11-28 05:01:45',NULL),(152,14,86,'2016-11-28 05:02:12',NULL),(153,1,86,'2016-11-28 05:02:12',NULL),(154,14,87,'2016-11-28 05:02:35',NULL),(155,1,87,'2016-11-28 05:02:35',NULL),(156,15,88,'2016-12-01 07:50:19',NULL),(157,15,89,'2016-12-01 07:50:35',NULL),(158,15,90,'2016-12-01 07:50:52',NULL),(159,1,91,'2016-12-01 07:56:02',NULL),(160,14,92,'2016-12-05 02:11:35',NULL),(161,12,72,'2016-12-22 09:38:27',NULL),(162,12,70,'2016-12-22 09:38:52',NULL),(163,8,93,'2016-12-22 09:40:13',NULL),(164,14,94,'2016-12-29 03:47:41',NULL),(165,10,95,'2016-12-29 09:04:44',NULL),(166,16,96,'2016-12-29 11:14:55',NULL),(167,16,97,'2016-12-29 11:15:12',NULL),(168,16,98,'2016-12-29 11:15:25',NULL),(169,16,99,'2016-12-29 11:15:37',NULL),(170,16,100,'2016-12-29 11:15:50',NULL),(171,16,101,'2016-12-29 11:16:02',NULL),(172,16,102,'2016-12-29 11:16:14',NULL),(173,16,103,'2016-12-29 11:16:29',NULL),(174,16,104,'2016-12-29 11:16:41',NULL),(176,1,106,'2016-12-29 11:17:28',NULL),(177,1,105,'2016-12-29 11:17:47',NULL),(178,9,107,'2017-01-03 02:59:35',NULL),(179,8,108,'2017-01-10 13:20:19',NULL),(180,8,109,'2017-01-10 13:20:34',NULL),(181,8,110,'2017-01-11 03:53:57',NULL),(182,8,111,'2017-01-11 10:37:55',NULL),(183,8,112,'2017-01-11 10:38:18',NULL),(184,8,113,'2017-01-11 10:40:44',NULL),(185,8,114,'2017-01-12 07:41:33',NULL),(186,8,115,'2017-01-12 07:42:22',NULL),(187,8,116,'2017-01-12 07:44:02',NULL),(188,8,117,'2017-01-12 07:44:51',NULL),(189,8,118,'2017-01-12 07:46:07',NULL),(190,8,119,'2017-01-12 07:46:58',NULL),(191,8,120,'2017-01-12 07:47:27',NULL),(192,8,121,'2017-01-12 07:48:04',NULL),(193,8,122,'2017-01-12 07:48:39',NULL),(194,8,123,'2017-01-12 07:49:12',NULL),(195,17,124,'2017-01-18 06:04:45',NULL),(196,17,125,'2017-01-18 06:05:00',NULL),(197,17,126,'2017-01-18 06:05:25',NULL),(198,17,127,'2017-01-18 06:05:41',NULL),(199,17,128,'2017-01-18 06:05:54',NULL),(200,17,129,'2017-01-18 06:06:11',NULL),(201,17,130,'2017-01-18 06:06:22',NULL),(202,1,131,'2017-01-18 06:07:54',NULL),(203,14,132,'2017-01-20 06:17:30',NULL),(204,14,133,'2017-01-20 06:17:41',NULL),(205,17,134,'2017-02-07 03:36:18',NULL),(206,17,135,'2017-02-07 03:36:32',NULL),(207,17,136,'2017-02-07 03:37:30',NULL),(208,17,137,'2017-02-08 06:59:43',NULL),(209,8,138,'2017-02-09 08:18:02',NULL),(210,8,139,'2017-02-09 08:18:18',NULL),(211,8,140,'2017-02-14 06:35:56',NULL),(212,8,141,'2017-02-14 06:36:09',NULL),(214,18,143,'2017-02-17 07:33:31',NULL),(215,18,144,'2017-02-24 05:47:54',NULL),(216,1,145,'2017-02-24 05:48:43',NULL),(217,18,146,'2017-02-24 05:50:00',NULL),(218,18,147,'2017-02-28 07:47:33',NULL),(219,1,148,'2017-02-28 07:47:52',NULL),(220,18,149,'2017-03-02 08:09:00',NULL),(221,15,150,'2017-03-03 02:55:35',NULL),(222,17,151,'2017-03-17 06:00:43',NULL),(223,1,151,'2017-03-17 06:00:43',NULL),(224,9,152,'2017-03-17 06:01:01',NULL),(225,1,142,'2017-03-17 06:01:21',NULL),(226,18,142,'2017-03-17 06:01:21',NULL),(227,1,153,'2017-03-31 11:36:48',NULL),(228,19,154,'2017-03-31 11:38:03',NULL),(229,19,155,'2017-03-31 11:39:10',NULL),(230,19,156,'2017-03-31 11:39:31',NULL),(231,1,157,'2017-04-01 03:38:23',NULL),(232,20,157,'2017-04-01 03:38:23',NULL),(233,20,158,'2017-04-01 03:42:33',NULL),(234,20,159,'2017-04-01 03:42:49',NULL),(235,20,160,'2017-04-01 03:43:13',NULL),(236,20,161,'2017-04-01 03:43:38',NULL),(237,20,162,'2017-04-01 03:44:00',NULL),(238,19,163,'2017-04-01 09:40:30',NULL),(239,19,164,'2017-04-05 02:52:58',NULL),(240,19,165,'2017-04-05 02:53:23',NULL),(242,19,166,'2017-04-05 06:23:37',NULL),(243,14,168,'2017-04-12 07:07:47',NULL),(244,1,169,'2017-04-12 08:20:17',NULL),(246,18,172,'2017-04-13 10:13:48',NULL),(247,19,173,'2017-04-20 03:23:22',NULL),(248,19,174,'2017-04-20 03:24:02',NULL),(254,14,181,'2017-05-08 04:56:53',NULL),(255,14,182,'2017-05-08 04:57:08',NULL),(256,14,183,'2017-05-10 03:50:45',NULL),(257,8,184,'2017-05-24 08:25:19',NULL),(258,21,185,'2017-06-01 01:59:18',NULL),(259,1,186,'2017-06-01 02:00:19',NULL),(260,21,187,'2017-06-01 03:32:40',NULL),(261,21,188,'2017-06-01 03:33:31',NULL),(262,21,189,'2017-06-01 03:33:48',NULL),(263,1,175,'2017-06-06 02:20:27',NULL),(270,19,192,'2017-06-12 05:59:10',NULL),(271,23,193,'2017-06-12 09:57:26',NULL),(272,23,194,'2017-06-13 02:03:01',NULL),(273,23,195,'2017-06-13 05:16:54',NULL),(274,23,196,'2017-06-13 05:17:40',NULL),(276,23,197,'2017-06-13 08:12:18',NULL),(277,1,198,'2017-06-21 08:15:15',NULL),(278,15,199,'2017-06-21 09:00:21',NULL),(279,15,200,'2017-06-21 09:34:05',NULL),(280,15,201,'2017-06-22 10:18:59',NULL),(281,15,202,'2017-06-28 05:45:29',NULL),(282,15,203,'2017-06-28 05:45:53',NULL),(283,15,204,'2017-06-28 07:50:52',NULL),(284,15,205,'2017-06-29 03:26:19',NULL),(285,1,206,'2017-07-11 09:11:45',NULL),(286,24,207,'2017-07-11 10:22:12',NULL),(287,24,208,'2017-07-13 10:38:15',NULL),(288,24,209,'2017-07-13 10:38:29',NULL),(289,24,210,'2017-07-13 10:38:51',NULL),(291,8,211,'2017-08-04 02:01:55',NULL),(292,8,212,'2017-08-07 06:22:37',NULL),(293,8,213,'2017-08-07 07:42:07',NULL),(294,15,214,'2017-08-10 08:13:46',NULL),(295,15,215,'2017-08-10 08:14:09',NULL),(296,15,216,'2017-08-10 08:14:34',NULL),(297,15,217,'2017-08-10 08:34:16',NULL),(298,1,218,'2017-08-18 02:03:06',NULL),(299,15,219,'2017-08-18 02:05:45',NULL),(300,15,220,'2017-08-18 07:38:17',NULL),(301,15,221,'2017-08-18 08:27:07',NULL),(302,15,222,'2017-08-18 08:27:22',NULL),(303,1,223,'2017-08-21 06:25:11',NULL),(304,1,224,'2017-08-21 06:32:40',NULL),(305,15,225,'2017-08-22 03:07:05',NULL),(306,15,226,'2017-08-23 08:36:34',NULL),(307,15,227,'2017-08-24 03:17:34',NULL),(308,15,228,'2017-08-24 09:10:17',NULL),(309,15,229,'2017-08-24 09:10:57',NULL),(310,15,230,'2017-08-24 10:18:59',NULL),(311,1,231,'2017-08-29 02:08:45',NULL),(312,1,232,'2017-08-29 02:09:04',NULL),(313,1,233,'2017-08-29 02:09:20',NULL),(314,26,180,'2017-08-29 03:11:31',NULL),(315,26,179,'2017-08-29 03:11:46',NULL),(317,26,176,'2017-08-29 03:12:01',NULL),(318,26,177,'2017-08-29 03:14:56',NULL),(319,27,191,'2017-08-29 05:58:10',NULL),(320,15,234,'2017-09-05 07:38:02',NULL),(321,15,235,'2017-09-05 07:38:18',NULL),(322,1,236,'2017-09-05 09:18:39',NULL),(323,11,237,'2017-09-05 11:02:14',NULL),(324,11,238,'2017-09-07 03:51:21',NULL),(325,11,239,'2017-09-07 08:40:30',NULL),(326,11,240,'2017-09-07 08:41:22',NULL),(327,11,241,'2017-09-07 08:41:42',NULL),(328,11,242,'2017-09-07 13:36:57',NULL),(329,11,243,'2017-09-08 02:17:48',NULL),(330,11,244,'2017-09-08 07:59:33',NULL),(331,11,245,'2017-09-08 10:40:31',NULL),(332,11,246,'2017-09-08 11:41:34',NULL),(333,11,247,'2017-09-11 10:32:50',NULL),(334,11,248,'2017-09-11 10:33:10',NULL),(335,11,249,'2017-09-13 10:52:52',NULL),(336,1,250,'2017-09-20 02:59:05',NULL),(337,28,251,'2017-09-20 05:51:36',NULL),(338,28,252,'2017-09-20 12:34:54',NULL),(340,28,254,'2017-09-21 07:33:34',NULL),(341,1,255,'2017-09-22 09:39:30',NULL),(342,28,256,'2017-09-25 05:23:15',NULL),(343,28,257,'2017-09-25 07:47:12',NULL),(344,28,258,'2017-09-25 07:47:45',NULL),(346,28,260,'2017-09-25 09:46:19',NULL),(347,28,253,'2017-09-25 10:19:28',NULL),(348,1,261,'2017-09-27 03:52:24',NULL),(349,28,262,'2017-09-27 08:45:23',NULL),(350,28,263,'2017-09-27 08:45:40',NULL),(352,28,264,'2017-09-28 02:13:29',NULL),(353,28,265,'2017-09-28 03:35:32',NULL),(354,28,266,'2017-09-29 07:34:55',NULL),(355,14,267,'2017-09-30 01:59:31',NULL),(356,18,268,'2017-09-30 06:47:23',NULL),(357,8,269,'2017-10-11 03:08:29',NULL),(358,25,270,'2017-10-17 07:59:22',NULL),(359,25,271,'2017-10-17 07:59:35',NULL),(360,14,272,'2017-10-23 02:54:55',NULL),(361,1,273,'2017-10-26 02:38:54',NULL),(362,29,274,'2017-10-26 06:48:01',NULL),(363,29,275,'2017-10-30 03:22:04',NULL),(364,29,276,'2017-10-30 03:22:23',NULL),(365,29,277,'2017-10-31 09:07:03',NULL),(366,29,278,'2017-10-31 09:07:23',NULL),(367,29,279,'2017-10-31 09:07:39',NULL),(368,29,280,'2017-10-31 09:08:06',NULL),(369,29,281,'2017-10-31 09:08:22',NULL),(370,1,282,'2017-11-01 11:34:30',NULL),(372,6,283,'2017-11-02 07:08:53',NULL),(373,31,284,'2017-11-02 08:56:13',NULL),(374,31,285,'2017-11-03 03:52:58',NULL),(375,11,286,'2017-11-06 06:51:40',NULL),(376,29,288,'2017-11-10 02:09:40',NULL),(377,11,290,'2017-11-16 03:06:24',NULL),(378,11,291,'2017-11-16 03:06:46',NULL),(379,11,294,'2017-11-16 03:08:09',NULL),(380,11,295,'2017-11-17 02:10:34',NULL),(381,11,296,'2017-11-17 08:58:35',NULL),(382,11,297,'2017-11-17 08:58:58',NULL),(383,11,298,'2017-11-17 08:59:16',NULL),(385,11,299,'2017-11-17 09:34:03',NULL),(386,11,300,'2017-11-20 09:34:09',NULL),(388,11,301,'2017-11-20 10:32:15',NULL),(389,11,302,'2017-11-20 10:32:46',NULL),(390,11,303,'2017-11-21 06:51:06',NULL),(391,15,304,'2017-11-21 07:57:00',NULL),(392,11,305,'2017-11-23 10:03:03',NULL),(393,9,306,'2017-11-23 11:59:33',NULL),(395,32,307,'2017-12-14 12:27:24',NULL),(396,33,308,'2017-12-14 12:28:49',NULL),(397,1,309,'2017-12-15 06:26:21',NULL),(398,1,310,'2017-12-15 06:27:01',NULL),(399,1,311,'2017-12-15 06:27:29',NULL),(400,1,312,'2017-12-15 06:34:12',NULL),(401,32,313,'2017-12-15 07:09:30',NULL),(402,32,314,'2017-12-15 08:05:36',NULL),(403,32,315,'2017-12-15 08:06:27',NULL),(404,32,316,'2017-12-21 06:51:31',NULL),(405,32,317,'2017-12-21 08:04:38',NULL),(406,32,318,'2017-12-21 08:04:50',NULL),(407,32,319,'2017-12-21 08:54:45',NULL),(408,32,320,'2017-12-21 08:55:06',NULL),(409,32,321,'2017-12-21 08:55:22',NULL),(410,32,322,'2017-12-21 12:13:03',NULL),(412,32,323,'2017-12-22 03:45:35',NULL),(413,32,324,'2017-12-26 12:50:02',NULL),(414,32,325,'2017-12-27 09:35:28',NULL),(415,32,326,'2017-12-27 12:57:24',NULL),(416,32,327,'2017-12-29 09:27:53',NULL),(417,34,328,'2018-01-02 01:36:09',NULL),(418,34,329,'2018-01-02 01:36:37',NULL),(419,1,330,'2018-01-02 03:14:18',NULL),(420,1,331,'2018-01-02 03:14:58',NULL),(421,1,332,'2018-01-02 03:15:18',NULL),(422,32,333,'2018-01-03 01:42:40',NULL),(423,32,334,'2018-01-03 02:23:40',NULL),(424,32,335,'2018-01-03 02:32:46',NULL),(425,32,336,'2018-01-03 07:06:46',NULL),(426,11,62,'2018-01-03 07:58:36',NULL),(427,32,337,'2018-01-08 08:43:43',NULL),(428,32,338,'2018-01-09 01:42:36',NULL),(429,32,339,'2018-01-09 02:33:53',NULL),(430,32,340,'2018-01-09 02:37:15',NULL),(431,32,341,'2018-01-09 03:20:02',NULL),(432,32,342,'2018-01-09 03:20:25',NULL),(433,32,343,'2018-01-09 03:40:09',NULL),(434,32,344,'2018-01-09 06:15:10',NULL),(435,32,345,'2018-01-09 06:15:26',NULL),(436,32,346,'2018-01-09 06:23:34',NULL),(437,32,347,'2018-01-09 07:59:40',NULL),(438,32,348,'2018-01-09 10:41:44',NULL),(439,18,349,'2018-01-17 08:18:51',NULL),(440,18,350,'2018-01-17 08:19:17',NULL),(441,32,351,'2018-01-25 08:03:46',NULL),(442,1,352,'2018-02-08 09:36:29',NULL),(443,18,353,'2018-02-09 07:59:57',NULL),(444,15,354,'2018-03-15 05:53:32',NULL),(445,1,355,'2018-04-10 02:19:51',NULL),(446,15,356,'2018-04-10 02:54:00',NULL),(447,15,357,'2018-04-10 06:20:51',NULL),(448,15,358,'2018-04-10 08:03:48',NULL),(449,15,359,'2018-04-11 07:49:09',NULL),(450,1,360,'2018-04-18 01:44:34',NULL),(451,32,361,'2018-04-24 03:23:01',NULL),(452,11,362,'2018-04-25 08:49:10',NULL),(453,11,363,'2018-04-26 06:45:59',NULL),(454,32,364,'2018-05-07 09:22:31',NULL),(455,8,365,'2018-05-09 03:47:23',NULL),(456,15,366,'2018-05-11 03:44:53',NULL),(457,17,367,'2018-05-11 09:09:16',NULL),(459,11,368,'2018-05-14 10:13:12',NULL),(460,19,369,'2018-05-15 07:16:13',NULL),(461,19,370,'2018-05-17 05:56:42',NULL),(462,19,371,'2018-05-22 06:50:17',NULL),(463,19,372,'2018-05-22 06:53:25',NULL),(464,1,373,'2018-05-22 07:42:13',NULL),(465,19,374,'2018-05-23 02:27:27',NULL),(466,19,375,'2018-05-24 02:12:53',NULL),(467,19,376,'2018-05-24 07:34:25',NULL),(468,9,377,'2018-05-29 06:47:51',NULL),(469,9,378,'2018-05-29 07:33:50',NULL),(470,9,379,'2018-05-29 08:08:12',NULL),(471,9,380,'2018-05-29 08:17:12',NULL),(472,21,381,'2018-05-29 10:48:33',NULL),(473,23,382,'2018-05-30 01:13:36',NULL),(474,21,383,'2018-05-30 02:25:38',NULL),(475,23,384,'2018-05-30 06:09:09',NULL),(476,23,385,'2018-05-30 07:32:45',NULL),(477,23,386,'2018-05-30 07:33:06',NULL),(478,23,387,'2018-05-31 06:26:10',NULL),(479,23,388,'2018-05-31 06:26:34',NULL),(480,23,389,'2018-05-31 07:05:16',NULL),(481,17,390,'2018-05-31 12:10:19',NULL),(482,17,391,'2018-06-01 03:33:57',NULL),(483,17,392,'2018-06-01 03:34:32',NULL),(484,17,393,'2018-06-01 03:34:55',NULL),(485,17,394,'2018-06-01 03:35:15',NULL),(486,17,395,'2018-06-01 10:13:04',NULL),(487,17,396,'2018-06-04 01:43:24',NULL),(488,17,397,'2018-06-04 01:43:58',NULL),(489,17,398,'2018-06-04 03:38:49',NULL),(490,17,399,'2018-06-04 03:39:10',NULL),(491,1,400,'2018-06-04 08:20:30',NULL),(492,16,401,'2018-06-05 05:16:39',NULL),(493,16,402,'2018-06-05 05:53:41',NULL),(494,16,403,'2018-06-05 06:17:40',NULL),(495,16,404,'2018-06-05 07:01:24',NULL),(497,19,405,'2018-06-05 08:23:26',NULL),(498,19,406,'2018-06-05 09:03:12',NULL),(499,1,407,'2018-06-05 10:09:28',NULL),(500,14,408,'2018-06-06 07:15:51',NULL),(501,16,409,'2018-06-06 10:06:09',NULL),(502,19,410,'2018-06-08 06:37:00',NULL),(503,1,411,'2018-06-11 01:52:09',NULL),(504,16,412,'2018-06-11 05:47:55',NULL),(505,16,413,'2018-06-11 06:31:39',NULL),(506,19,414,'2018-06-11 08:09:03',NULL),(507,19,415,'2018-06-11 08:09:35',NULL),(508,16,416,'2018-06-11 10:31:50',NULL),(509,19,417,'2018-06-11 11:07:25',NULL),(510,23,418,'2018-06-13 10:19:02',NULL),(511,23,419,'2018-06-14 02:25:35',NULL),(512,6,420,'2018-06-14 02:52:06',NULL),(513,23,421,'2018-06-14 08:55:37',NULL),(514,23,422,'2018-06-14 08:55:50',NULL),(515,8,423,'2018-06-15 03:41:44',NULL),(516,23,424,'2018-08-09 07:34:58',NULL),(517,8,425,'2018-08-09 08:04:20',NULL),(519,35,427,'2018-10-09 08:42:04',NULL),(520,35,428,'2018-10-31 01:34:51',NULL),(521,35,429,'2018-10-31 01:35:58',NULL),(522,35,430,'2018-10-31 03:44:38',NULL),(523,35,431,'2018-11-01 01:17:53',NULL),(525,35,433,'2018-11-02 02:47:12',NULL),(526,35,432,'2018-11-02 03:50:27',NULL),(527,35,434,'2018-11-02 08:47:47',NULL),(528,35,435,'2018-11-02 08:49:02',NULL),(529,35,436,'2018-11-02 08:49:32',NULL),(530,35,437,'2018-11-02 08:50:02',NULL),(531,35,438,'2018-11-02 09:02:13',NULL),(532,36,439,'2018-11-05 08:53:10',NULL),(533,36,440,'2018-11-06 08:58:18',NULL),(534,36,441,'2018-11-07 02:38:46',NULL),(535,36,442,'2018-11-07 02:39:03',NULL),(536,36,443,'2018-11-07 02:54:57',NULL),(537,36,444,'2018-11-09 10:07:06',NULL),(538,36,445,'2018-11-12 03:09:44',NULL),(539,36,446,'2018-11-12 03:10:05',NULL),(540,36,447,'2018-11-12 06:27:22',NULL),(541,1,426,'2018-11-14 02:16:20',NULL),(542,37,448,'2018-11-15 02:24:35',NULL),(543,37,449,'2018-11-15 02:25:02',NULL),(544,37,450,'2018-11-15 02:26:40',NULL),(545,37,451,'2018-11-15 02:27:12',NULL),(546,37,452,'2018-11-15 02:27:33',NULL),(547,1,453,'2018-11-15 02:37:15',NULL),(548,37,454,'2018-11-15 09:15:27',NULL),(549,1,455,'2018-11-16 03:43:08',NULL),(550,38,456,'2018-11-19 03:22:29',NULL),(551,38,457,'2018-11-22 06:17:02',NULL),(552,38,458,'2018-11-23 08:20:35',NULL),(553,38,459,'2018-11-23 09:59:19',NULL),(555,38,460,'2018-11-23 10:58:32',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=461 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_resources`
--

LOCK TABLES `new_auth_resources` WRITE;
/*!40000 ALTER TABLE `new_auth_resources` DISABLE KEYS */;
INSERT INTO `new_auth_resources` VALUES (1,'app.dashboard','首页',0,'2016-09-19 07:31:41','2016-09-22 11:54:51'),(2,'app.servergroup','服务器组管理页面',0,'2016-09-19 07:31:50','2016-09-22 11:54:51'),(3,'app.server','服务器管理页面',0,'2016-09-19 07:31:59','2016-09-22 11:54:51'),(4,'app.ipgroup','ip组管理页面',0,'2016-09-19 07:32:09','2016-09-22 11:54:51'),(7,'app.ip','ip管理页面',0,'2016-09-21 08:14:24','2016-09-22 11:54:51'),(8,'app.resource','资源管理页面',0,'2016-09-22 02:46:20','2016-09-22 11:54:51'),(9,'app.role','角色管理页面',0,'2016-09-22 02:46:41','2016-09-22 11:54:51'),(10,'app.user','用户管理页面',0,'2016-09-22 02:47:02','2016-09-22 11:54:51'),(11,'/login','登录',1,'2016-09-22 11:59:56',NULL),(12,'/check/auth','权限校验',0,'2016-09-22 12:00:31',NULL),(13,'/resource/groups','获取满足要求的资源组数据',0,'2016-09-22 12:00:51',NULL),(14,'/resource/group/save','保存指定的资源组数据',0,'2016-09-22 12:01:14',NULL),(15,'/resource/group/del','删除指定的资源组信息',0,'2016-09-22 12:01:31',NULL),(16,'/resource/query','查询合适条件的资源数据集合',0,'2016-09-22 12:01:44',NULL),(17,'/resource/save','更新或添加资源',0,'2016-09-22 12:01:57',NULL),(18,'/resource/del','删除指定的资源',0,'2016-09-22 12:02:18',NULL),(19,'/role/query','获取角色集合',0,'2016-09-22 12:02:31',NULL),(20,'/role/save','保存角色信息',0,'2016-09-22 12:02:55',NULL),(21,'/role/del','删除指定的角色信息',0,'2016-09-22 12:03:08',NULL),(22,'/role/resource/bind/query','获取指定角色id的绑定资源列表',0,'2016-09-22 12:03:37',NULL),(23,'/role/resource/unbind/query','获取指定角色id的未绑定资源列表',0,'2016-09-22 12:03:51',NULL),(24,'/role/resource/bind','角色资源建立绑定关系',0,'2016-09-22 12:04:04',NULL),(25,'/role/resource/unbind','角色资源解除绑定关系',0,'2016-09-22 12:04:18',NULL),(26,'/user/role','获取用户角色数据',0,'2016-09-22 12:04:31',NULL),(27,'/user/role/bind','用户角色绑定',0,'2016-09-22 12:04:46',NULL),(28,'/user/role/unbind','用户角色解绑',0,'2016-09-22 12:05:00',NULL),(29,'/ip/query','查询指定条件的ip集合信息',0,'2016-09-22 12:09:36',NULL),(30,'/ip/save','保存 or 更新ip信息',0,'2016-09-22 12:10:04',NULL),(31,'/ip/del','删除指定id的ip信息',0,'2016-09-22 12:10:23',NULL),(32,'/ipgroup/query','ip组按条件查询',0,'2016-09-22 12:17:51',NULL),(33,'/ipgroup/save','保存 or 更新IP组信息',0,'2016-09-22 12:23:33',NULL),(34,'/ipgroup/del','删除指定IP组信息',0,'2016-09-22 12:24:56',NULL),(35,'/ipgroup/create','生成ip',0,'2016-09-22 12:25:28',NULL),(36,'/ipgroup/serverGroup/query','查询指定服务器组的ip组集合',0,'2016-09-22 12:25:50',NULL),(37,'/server/page','获取指定条件的服务器列表分页数据',0,'2016-09-22 12:28:28',NULL),(38,'/server/save','保存指定的serveritem',0,'2016-09-22 12:28:49',NULL),(39,'/server/del','删除指定的server',0,'2016-09-22 12:29:19',NULL),(40,'/servergroup/query/page','查询服务器组的分页数据',0,'2016-09-22 12:29:41',NULL),(41,'/servergroup/update','更新服务器组信息',0,'2016-09-22 12:30:07',NULL),(42,'/servergroup/del','删除指定的服务器组信息',0,'2016-09-22 12:30:37',NULL),(43,'/servergroup/ipgroup/add','服务器组绑定指定的ip组',0,'2016-09-22 12:31:05',NULL),(44,'/servergroup/ipgroup/del','服务器组解绑指定的ip组',0,'2016-09-22 12:32:28',NULL),(45,'/dashboard','首页',0,'2016-09-23 02:32:53',NULL),(46,'/logout','登出',0,'2016-09-23 03:32:32',NULL),(47,'/ip/use/check','检查指定段的ip是否已经被使用',0,'2016-09-23 09:33:04',NULL),(48,'lookAllServerGroup','查看所有服务器组数据',0,'2016-09-29 06:28:29',NULL),(49,'app.system','系统管理菜单',0,'2016-10-08 06:56:51',NULL),(50,'/system/save','系统新增 or 更新',0,'2016-10-08 07:28:33',NULL),(51,'/system/del','删除指定的系统',0,'2016-10-08 07:29:01',NULL),(52,'/system/query','模糊查询匹配名称的系统列表',0,'2016-10-08 07:29:21',NULL),(53,'app.todoConfig','工单配置项管理',0,'2016-10-09 08:49:13',NULL),(54,'app.todoDaily','日常工单',0,'2016-10-09 08:49:30',NULL),(55,'/todo/config/query','查询合适条件下的工单配置项分页数据',0,'2016-10-09 09:29:25',NULL),(56,'/todo/config/save','配置项的新增 or 更新',0,'2016-10-09 09:30:00',NULL),(57,'/todo/config/del','删除指定的配置项',0,'2016-10-09 09:30:23',NULL),(58,'/todo/config/children/query','查询指定父id的子item集合',0,'2016-10-10 11:09:05',NULL),(59,'/todo/daily/query','查询日常工单分页数据',0,'2016-10-11 08:22:59',NULL),(60,'/todo/daily/save','日常工单新增 or 更新',0,'2016-10-11 08:42:21',NULL),(61,'/todo/process/query','查询工单待处理分页数据',0,'2016-10-12 07:28:59',NULL),(62,'/todo/query','查询指定工单的数据',1,'2016-10-19 06:39:29','2018-01-03 07:58:36'),(63,'app.property','属性管理',0,'2016-10-21 02:07:16','2016-10-21 02:10:58'),(64,'/config/property/group','属性组分页数据',0,'2016-10-21 02:49:01',NULL),(65,'/config/property/group/save','新增 or 更新属性组',0,'2016-10-21 03:08:37',NULL),(66,'/config/property/group/del','删除指定的属性组',0,'2016-10-21 03:16:53',NULL),(67,'/config/property','属性分页数据',0,'2016-10-21 05:51:23',NULL),(68,'/config/property/save','新增 or 更新属性',0,'2016-10-21 06:02:47',NULL),(69,'/config/property/del','删除指定的属性',0,'2016-10-21 06:03:15',NULL),(70,'/config/propertygroup/save','新增 or 更新属性组数据',0,'2016-10-25 03:00:20','2016-12-22 09:38:52'),(71,'/servergroup/propertygroup/query','查询指定条件的服务器组属性组数据',0,'2016-10-25 03:20:45',NULL),(72,'/config/propertygroup/del','删除指定的属性组数据',0,'2016-10-25 06:35:27','2016-12-22 09:38:27'),(73,'/servergroup/propertygroup/create','生成指定服务器组&属性组的属性配置文件',0,'2016-10-31 06:59:16',NULL),(74,'/servergroup/propertygroup/preview','预览指定服务器组&属性组的属性配置文件',0,'2016-10-31 09:48:34','2016-10-31 11:35:39'),(75,'/servergroup/propertygroup/launch','加载指定服务器组&属性组的本地属性配置文件',0,'2016-10-31 11:49:17',NULL),(76,'/box/user/group/global/create','创建全局配置文件',0,'2016-11-28 04:58:25',NULL),(77,'/box/user/group/create','创建指定用户的配置文件',0,'2016-11-28 04:58:44',NULL),(78,'/box/user/group/del','删除用户服务器组',0,'2016-11-28 04:59:03',NULL),(79,'/box/user/group/save','添加新的堡垒机用户服务器组',0,'2016-11-28 04:59:32',NULL),(80,'/box/user/group','获取堡垒机用户服务器组分页信息',0,'2016-11-28 04:59:51',NULL),(81,'/box/auth/add','堡垒机授权',0,'2016-11-28 05:00:08',NULL),(82,'/box/auth/del','堡垒机删除授权',0,'2016-11-28 05:00:24',NULL),(83,'/users','获取指定条件的用户集合',0,'2016-11-28 05:01:03',NULL),(84,'/box/query','堡垒机分页数据查询',0,'2016-11-28 05:01:26',NULL),(85,'/keybox/ws','堡垒机websocket接口',0,'2016-11-28 05:01:45',NULL),(86,'app.keybox','堡垒机',0,'2016-11-28 05:02:12',NULL),(87,'app.keyboxmanage','堡垒机管理',0,'2016-11-28 05:02:35',NULL),(88,'/user/query','查询指定用户名的用户信息',0,'2016-12-01 07:50:19',NULL),(89,'canEdit','是否可编辑权限',0,'2016-12-01 07:50:35',NULL),(90,'/user/save','更新用户信息',0,'2016-12-01 07:50:52',NULL),(91,'app.userDetail','用户详情菜单',0,'2016-12-01 07:56:02',NULL),(92,'/box/server/query','查询被授权的堡垒机服务器列表',0,'2016-12-05 02:11:35',NULL),(93,'/server/propertygroup/query','查询指定条件的服务器属性组数据',0,'2016-12-22 09:40:13',NULL),(94,'/box/user/group/createAll','堡垒机生成所有用户配置文件',0,'2016-12-29 03:47:41',NULL),(95,'access.system','系统导航',0,'2016-12-29 09:04:44',NULL),(96,'/config/file/launch','预览本地文件',0,'2016-12-29 11:14:55',NULL),(97,'/config/file/invoke','执行命令',0,'2016-12-29 11:15:12',NULL),(98,'/config/file/create','生成本地文件',0,'2016-12-29 11:15:25',NULL),(99,'/config/file/del','删除指定id的文件组信息',0,'2016-12-29 11:15:37',NULL),(100,'/config/file/save','保存 or 更新文件组信息',0,'2016-12-29 11:15:50',NULL),(101,'/config/file/query','获取文件组分页数据',0,'2016-12-29 11:16:02',NULL),(102,'/config/fileGroup/del','删除指定id的文件组信息',0,'2016-12-29 11:16:14',NULL),(103,'/config/fileGroup/query','获取文件组分页数据',0,'2016-12-29 11:16:29',NULL),(104,'/config/fileGroup/save','保存 or 更新文件组信息',0,'2016-12-29 11:16:41',NULL),(105,'app.configFileGroup','配置文件组管理',0,'2016-12-29 11:16:58',NULL),(106,'app.configFile','配置文件管理',0,'2016-12-29 11:17:28',NULL),(107,'/servergroup/propertygroup/save','新增服务器组属性',0,'2017-01-03 02:59:35',NULL),(108,'/server/ecsRefresh','获取最新ECS别表',0,'2017-01-10 13:20:19',NULL),(109,'/server/ecsPage','ecs服务器管理页面',0,'2017-01-10 13:20:34',NULL),(110,'/server/ecsCheck','ecs服务器校验',0,'2017-01-11 03:53:57',NULL),(111,'/server/setStatus','ECS标记删除',0,'2017-01-11 10:37:55',NULL),(112,'/server/delEcs','删除ECS服务器',0,'2017-01-11 10:38:18',NULL),(113,'/server/ecsSave','Ecs操作按钮',0,'2017-01-11 10:40:44',NULL),(114,'/server/vmSave','vm服务器按钮',0,'2017-01-12 07:41:33',NULL),(115,'/server/vmRefresh','获取最新VM列表',0,'2017-01-12 07:42:22',NULL),(116,'/server/vmCheck','vm服务器校验',0,'2017-01-12 07:44:02',NULL),(117,'/server/vmPage','VM服务器管理页面',0,'2017-01-12 07:44:51',NULL),(118,'/server/addVmServer','将VM添加到Server',0,'2017-01-12 07:46:07',NULL),(119,'/server/setDelVm','删除标记VM服务器',0,'2017-01-12 07:46:58',NULL),(120,'/server/vmRename','VM服务器改名',0,'2017-01-12 07:47:27',NULL),(121,'/server/vmReboot','VM服务器重启',0,'2017-01-12 07:48:04',NULL),(122,'/server/vmShutdown','VM服务器关机',0,'2017-01-12 07:48:39',NULL),(123,'/server/delVm','删除VM服务器',0,'2017-01-12 07:49:12',NULL),(124,'/zabbixserver/save','zabbix操作菜单',0,'2017-01-18 06:04:45',NULL),(125,'/zabbixserver/disableMonitor','禁用监控',0,'2017-01-18 06:05:00',NULL),(126,'/zabbixserver/enableMonitor','启用监控',0,'2017-01-18 06:05:25',NULL),(127,'/zabbixserver/delMonitor','删除监控',0,'2017-01-18 06:05:41',NULL),(128,'/zabbixserver/addMonitor','添加监控',0,'2017-01-18 06:05:54',NULL),(129,'/zabbixserver/refresh','更新zabbix数据',0,'2017-01-18 06:06:11',NULL),(130,'/zabbixserver/page','监控管理页面',0,'2017-01-18 06:06:22',NULL),(131,'app.zabbixserver','服务器监控菜单',0,'2017-01-18 06:07:54',NULL),(132,'/servergroup/keybox/page','服务器组授权管理',0,'2017-01-20 06:17:30',NULL),(133,'/box/checkUser','校验用户数据并清理',0,'2017-01-20 06:17:41',NULL),(134,'/zabbixserver/user/auth/del','删除zabbix账户授权',0,'2017-02-07 03:36:18',NULL),(135,'/zabbixserver/user/auth/add','添加zabbix账户授权',0,'2017-02-07 03:36:32',NULL),(136,'/zabbixserver/user/sync','同步zabbix用户',0,'2017-02-07 03:37:30',NULL),(137,'/zabbixserver/ci','zabbix持续集成接口',1,'2017-02-08 06:59:43',NULL),(138,'/server/ecsStatistics','ecs服务器统计',0,'2017-02-09 08:18:02',NULL),(139,'/server/vmStatistics','vm服务器统计',0,'2017-02-09 08:18:18',NULL),(140,'/server/psStatistics','物理服务器统计',0,'2017-02-14 06:35:56',NULL),(141,'/server/psPage','物理服务器详情',0,'2017-02-14 06:36:09',NULL),(142,'app.statistics','统计菜单',0,'2017-02-17 07:32:59',NULL),(143,'/statistics/deploy/page','部署统计详情页',0,'2017-02-17 07:33:31',NULL),(144,'/statistics/servercost/statistics','服务器成本统计',0,'2017-02-24 05:47:54',NULL),(145,'app.serverstatistics','服务器成本统计',0,'2017-02-24 05:48:43',NULL),(146,'/statistics/servercost/page','服务器统计',0,'2017-02-24 05:50:00',NULL),(147,'/statistics/serverperf/page','服务器性能统计',0,'2017-02-28 07:47:33',NULL),(148,'app.serverperfstatistics','服务器性能统计菜单',0,'2017-02-28 07:47:52',NULL),(149,'/statistics/serverperf/statistics','服务器性能统计',0,'2017-03-02 08:09:00',NULL),(150,'/user/resetToken','重置token',0,'2017-03-03 02:55:35',NULL),(151,'app.servermonitor','监控查看',0,'2017-03-17 06:00:43',NULL),(152,'/servergroup/servers','查询指定服务器组的服务器集合',0,'2017-03-17 06:01:01',NULL),(153,'app.logcleanup','日志弹性清理菜单',0,'2017-03-31 11:36:48',NULL),(154,'/task/logcleanup/page','日志弹性清理详情页',0,'2017-03-31 11:38:03',NULL),(155,'/task/logcleanup/cleanup','日志弹性清理',0,'2017-03-31 11:39:10',NULL),(156,'/task/logcleanup/refresh','日志弹性清理数据同步',0,'2017-03-31 11:39:31',NULL),(157,'app.explain','订阅审核菜单',0,'2017-04-01 03:38:23',NULL),(158,'/explain/add','添加审核订阅',0,'2017-04-01 03:42:33',NULL),(159,'/explain/query','查询指定条件下的审核订阅',0,'2017-04-01 03:42:49',NULL),(160,'/explain/del','删除指定id的审核订阅',0,'2017-04-01 03:43:13',NULL),(161,'/explain/repo/query','查询指定条件下的仓库列表',0,'2017-04-01 03:43:38',NULL),(162,'/explain/invoke','执行扫描',0,'2017-04-01 03:44:00',NULL),(163,'/task/logcleanup/setEnabled','日志弹性清理（禁用|启用）',0,'2017-04-01 09:40:30',NULL),(164,'/task/logcleanup/refreshDiskRate','刷新磁盘使用率',0,'2017-04-05 02:52:58',NULL),(165,'/task/logcleanup/addHistory','增加日志保留天数',0,'2017-04-05 02:53:23',NULL),(166,'/task/logcleanup/subtractHistory','减少日志保留天数',0,'2017-04-05 02:53:40','2017-04-05 06:23:37'),(168,'/box/group/user/query','堡垒机服务器组-用户管理查询',0,'2017-04-12 07:07:47',NULL),(169,'app.servertask','服务器常用任务菜单',0,'2017-04-12 08:20:17',NULL),(172,'/statistics/server/deploy/version/page','服务器部署版本详情页',0,'2017-04-13 10:13:48',NULL),(173,'/task/servertask/initializationSystem','服务器常用任务-初始化系统',0,'2017-04-20 03:23:22',NULL),(174,'/task/servertask/save','服务器常用任务-操作按钮',0,'2017-04-20 03:24:02',NULL),(175,'app.serverTemplate','服务器模版管理菜单',0,'2017-04-20 09:11:13','2017-06-06 02:20:27'),(176,'/server/template/ecs/page','ecs模版分页详情',0,'2017-04-20 09:20:45','2017-06-06 02:22:14'),(177,'/ecsServer/template/save','ecs模版按钮',0,'2017-04-21 02:53:08','2017-08-29 03:14:56'),(179,'/server/template/ecs/expansion','用ecs模版扩容服务器',0,'2017-04-25 07:51:17','2017-06-06 02:21:53'),(180,'/server/template/ecs/create','用ecs模版新建服务器',0,'2017-04-25 07:51:33','2017-06-06 02:21:17'),(181,'/box/key/query','堡垒机key查询',0,'2017-05-08 04:56:53',NULL),(182,'/box/key/save','更新堡垒机key',0,'2017-05-08 04:57:08',NULL),(183,'/box/user/save','新建用户',0,'2017-05-10 03:50:45',NULL),(184,'/server/ecsAllocateIp','ecs分配公网ip',0,'2017-05-24 08:25:19',NULL),(185,'/config/center/query','配置中心详情页',0,'2017-06-01 01:59:18',NULL),(186,'app.configCenter','配置中心菜单',0,'2017-06-01 02:00:19',NULL),(187,'/config/center/refreshCache','更新配置中心配置组缓存',0,'2017-06-01 03:32:40',NULL),(188,'/config/center/save','保存配置中配置项',0,'2017-06-01 03:33:31',NULL),(189,'/config/center/del','删除配置中心配置项',0,'2017-06-01 03:33:48',NULL),(191,'/server/template/vm/page','vm模版分页详情',0,'2017-06-06 02:25:50',NULL),(192,'/task/logcleanup/save','修改日志清理配置',0,'2017-06-12 05:59:10',NULL),(193,'/aliyun/image','查询阿里云镜像列表',0,'2017-06-12 09:57:26',NULL),(194,'/aliyun/network','获取阿里云网络类型',0,'2017-06-13 02:03:01',NULL),(195,'/aliyun/vpc','获取vpc列表',0,'2017-06-13 05:16:54',NULL),(196,'/aliyun/vswitch','获取虚拟交换机列表',0,'2017-06-13 05:17:40',NULL),(197,'/aliyun/securityGroup','获取安全组列表',0,'2017-06-13 05:18:06','2017-06-13 08:12:18'),(198,'app.users','用户管理',0,'2017-06-21 08:15:15',NULL),(199,'/usersLeave/page','离职用户详情页',0,'2017-06-21 09:00:21',NULL),(200,'/usersLeave/del','删除离职用户数据',0,'2017-06-21 09:34:05',NULL),(201,'/cmdb/users','cmdb用户管理',0,'2017-06-22 10:18:59',NULL),(202,'/cmdb/ldapGroup/remove','ldapgroup中移除该用户',0,'2017-06-28 05:45:29',NULL),(203,'/cmdb/ldap/remove','解除用户绑定',0,'2017-06-28 05:45:53',NULL),(204,'/cmdb/mailLdap/close','关闭用户的邮箱',0,'2017-06-28 07:50:52',NULL),(205,'/cmdb/user/leave','人员离职接口',0,'2017-06-29 03:26:19',NULL),(206,'app.dns','dns配置管理菜单',0,'2017-07-11 09:11:45',NULL),(207,'/dns/dnsmasq/page','dnsmasq详情页',0,'2017-07-11 10:22:12',NULL),(208,'/dns/dnsmasq/del','删除dnsmasq配置',0,'2017-07-13 10:38:15',NULL),(209,'/dns/dnsmasq/save','保存dnsmasq配置',0,'2017-07-13 10:38:29',NULL),(210,'/dns/save','dns按钮',0,'2017-07-13 10:38:51',NULL),(211,'/server/psSave','物理服务器管理菜单',0,'2017-08-04 02:01:44','2017-08-04 02:01:55'),(212,'/server/ps/esxiVms','esxi虚拟机列表页面',0,'2017-08-07 06:22:37',NULL),(213,'/server/ps/esxiDatastores','esxi的数据存储详情页',0,'2017-08-07 07:42:07',NULL),(214,'/cmdb/ldapGroup/add','添加用户至用户组',0,'2017-08-10 08:13:46',NULL),(215,'/cmdb/ldapGroup/del','移除用户组中的用户',0,'2017-08-10 08:14:09',NULL),(216,'/cmdb/mailLdap/active','激活用户的邮箱',0,'2017-08-10 08:14:34',NULL),(217,'/cmdb/user','查询用户详细信息(权限)',0,'2017-08-10 08:34:16',NULL),(218,'app.cigroups','用户管理-持续集成用户组管理',0,'2017-08-18 02:03:06',NULL),(219,'/ci/usergroup/page','持续集成用户组详情页',0,'2017-08-18 02:05:45',NULL),(220,'/ci/usergroup/refresh','持续集成用户组数据更新',0,'2017-08-18 07:38:17',NULL),(221,'/ci/usergroup/del','删除持续集成用户组',0,'2017-08-18 08:27:07',NULL),(222,'/ci/usergroup/update','更新持续集成用户组',0,'2017-08-18 08:27:22',NULL),(223,'app.usersLeave','cmdb离职用户管理',0,'2017-08-21 06:25:11',NULL),(224,'app.ciusers','持续集成用户管理菜单',0,'2017-08-21 06:32:40',NULL),(225,'/cmdb/ci/users','用户持续集成权限详情页',0,'2017-08-22 03:07:05',NULL),(226,'/ci/usergroup/save','持续集成用户组保存',0,'2017-08-23 08:36:34',NULL),(227,'/cmdb/ci/users/refresh','刷新持续集成用户权限表',0,'2017-08-24 03:17:34',NULL),(228,'/cmdb/ci/users/addGroup','用户添加持续集成权限组',0,'2017-08-24 09:10:17',NULL),(229,'/cmdb/ci/users/delGroup','用户删除持续集成权限组',0,'2017-08-24 09:10:57',NULL),(230,'/cmdb/ci/user','查询用户持续集成权限信息',0,'2017-08-24 10:18:59',NULL),(231,'app.vmServer','虚拟服务器菜单',0,'2017-08-29 02:08:45',NULL),(232,'app.ecsServer','ECS服务器菜单',0,'2017-08-29 02:09:04',NULL),(233,'app.physicalServer','物理服务器菜单',0,'2017-08-29 02:09:20',NULL),(234,'/cmdb/users/addUsersMobile','批量填充用户手机号',0,'2017-09-05 07:38:02',NULL),(235,'/cmdb/user/addUsersMobile','填充用户手机号',0,'2017-09-05 07:38:18',NULL),(236,'app.todo','工单',0,'2017-09-05 09:18:39',NULL),(237,'/todo/group/query','查询工单组',0,'2017-09-05 11:02:14',NULL),(238,'/todo/establish','创建工单',0,'2017-09-07 03:51:21',NULL),(239,'/todo/todoDetail/query','获取工单详情',0,'2017-09-07 08:40:30',NULL),(240,'/todo/todoDetail/addTodoKeybox','权限申请工单里添加堡垒机组',0,'2017-09-07 08:41:22',NULL),(241,'/todo/todoDetail/delTodoKeybox','权限申请工单里删除堡垒机组',0,'2017-09-07 08:41:42',NULL),(242,'/todo/todoDetail/submit','配置完毕提交工单',0,'2017-09-07 13:36:57',NULL),(243,'/todo/queryMyJob','查询我的待办工单',0,'2017-09-08 02:17:48',NULL),(244,'/todo/revokeTodoDetail','撤销工单按钮',0,'2017-09-08 07:59:33',NULL),(245,'/todo/invokeTodoDetail','执行工单按钮',0,'2017-09-08 10:40:31',NULL),(246,'/todo/queryCompleteJob','查询本人完成的工单',0,'2017-09-08 11:41:34',NULL),(247,'/todo/todoDetail/addTodoCiUserGroup','工单添加持续集成权限组',0,'2017-09-11 10:32:50',NULL),(248,'/todo/todoDetail/delTodoCiUserGroup','工单删除持续集成权限组',0,'2017-09-11 10:33:10',NULL),(249,'/todo/todoDetail/setTodoSystemAuth','标准工单-平台权限申请设置按钮(添加,移除)',0,'2017-09-13 10:52:52',NULL),(250,'app.logService','日志服务菜单',0,'2017-09-20 02:59:05',NULL),(251,'/logService/nginx/cfg/page','查询日志服务配置项目',0,'2017-09-20 05:51:36',NULL),(252,'/logService/nginx/query','日志服务nginx日志查询',0,'2017-09-20 12:34:54',NULL),(253,'/logService/logHistograms/page','日志分布视图详情页',0,'2017-09-21 05:51:51','2017-09-25 10:19:28'),(254,'/logService/nginx/viewLog','日志服务查看日志详情',0,'2017-09-21 07:33:34',NULL),(255,'app.javaLogService','java业务日志菜单',0,'2017-09-22 09:39:30',NULL),(256,'/logService/java/path/query/page','常用日志查询',0,'2017-09-25 05:23:15',NULL),(257,'/logService/java/viewLog','查看java日志详情',0,'2017-09-25 07:47:12',NULL),(258,'/logService/java/query','查询java日志分布视图',0,'2017-09-25 07:47:45',NULL),(260,'/logService/servergroup/query','查询授权的日志组（服务器组）',0,'2017-09-25 09:46:19',NULL),(261,'app.javaLogServiceManage','业务日志配置菜单',0,'2017-09-27 03:52:24',NULL),(262,'/logService/project/query','日志服务查询project',0,'2017-09-27 08:45:23',NULL),(263,'/logService/logstore/query','日志服务查询logstore',0,'2017-09-27 08:45:40',NULL),(264,'/logService/machineGroup/query','获取日志服务机器组详情',0,'2017-09-28 02:04:57','2017-09-28 02:13:29'),(265,'/logService/serverGroupCfg/save','保存业务日志服务器组配置',0,'2017-09-28 03:35:32',NULL),(266,'/logService/status','首页用日志服务统计信息',0,'2017-09-29 07:34:55',NULL),(267,'/box/status','首页中的堡垒机使用统计',0,'2017-09-30 01:59:31',NULL),(268,'/statistics/ci/status','首页持续集成统计',0,'2017-09-30 06:47:23',NULL),(269,'/server/status','首页统计服务器信息',0,'2017-10-11 03:08:29',NULL),(270,'/server/vmPowerOn','vm开机',0,'2017-10-17 07:59:22',NULL),(271,'/server/vmPowerOff','vm关机',0,'2017-10-17 07:59:35',NULL),(272,'/box/getway/status','getway登陆统计',1,'2017-10-23 02:54:55',NULL),(273,'app.projectManagement','项目管理',0,'2017-10-26 02:38:54',NULL),(274,'/project/page','项目管理详情页',0,'2017-10-26 06:48:01',NULL),(275,'/project/save','项目管理保存',0,'2017-10-30 03:22:04',NULL),(276,'/project/del','项目管理删除',0,'2017-10-30 03:22:23',NULL),(277,'/project/get','按id查询pm',0,'2017-10-31 09:07:03',NULL),(278,'/project/user/add','pm增加用户',0,'2017-10-31 09:07:23',NULL),(279,'/project/user/del','pm删除用户',0,'2017-10-31 09:07:39',NULL),(280,'/project/serverGroup/add','pm增加服务器组',0,'2017-10-31 09:08:06',NULL),(281,'/project/serverGroup/del','pm删除服务器组',0,'2017-10-31 09:08:22',NULL),(282,'app.projectHeartbeat','项目线上生命心跳菜单',0,'2017-11-01 11:34:30',NULL),(283,'/api/login','开放登陆认证',1,'2017-11-02 03:25:32','2017-11-02 07:08:53'),(284,'/project/heartbeat/page','项目生命管理心跳详情',0,'2017-11-02 08:56:13',NULL),(285,'/project/heartbeat/save','项目线上生命心跳按钮',0,'2017-11-03 03:52:58',NULL),(286,'/todo/todoDetail/setTodoVpn','标准工单-vpn权限申请按钮',0,'2017-11-06 06:51:40',NULL),(288,'/servergroup/project/query/page','查询项目管理服务器组列表(不重复添加)',0,'2017-11-10 02:09:40',NULL),(290,'/todo/todoNewProject/stash/project/query','查询合适条件下的StashProject分页数据',0,'2017-11-16 03:06:24',NULL),(291,'/todo/todoNewProject/stash/repository/query','查询合适条件下的StashRepository分页数据',0,'2017-11-16 03:06:46',NULL),(294,'/todo/todoNewProject/stash/project/get','查询合适条件下的StashProject',0,'2017-11-16 03:08:09',NULL),(295,'/todo/todoDetail/save','保存（暂存）工单',0,'2017-11-17 02:10:34',NULL),(296,'/todo/todoNewProject/builderPlan/query','查询构建计划',0,'2017-11-17 08:58:35',NULL),(297,'/todo/todoNewProject/builderPlan/del','删除构建计划',0,'2017-11-17 08:58:58',NULL),(298,'/todo/todoNewProject/builderPlan/update','更新构建计划',0,'2017-11-17 08:59:16',NULL),(299,'/todo/todoNewProject/builderPlan/save','保存构建计划',0,'2017-11-17 08:59:40','2017-11-17 09:34:03'),(300,'/todo/todoNewProject/newServer/query','新项目申请查询新服务器信息',0,'2017-11-20 09:34:09',NULL),(301,'/todo/todoNewProject/newServer/del','新项目申请删除新服务器信息',0,'2017-11-20 10:32:08','2017-11-20 10:32:15'),(302,'/todo/todoNewProject/newServer/save','新项目申请保存新服务器信息',0,'2017-11-20 10:32:46',NULL),(303,'/todo/todoNewProject/checkProjectName','新项目申请工单项目名校验',0,'2017-11-21 06:51:06',NULL),(304,'/safe/users','查询用户（不含私密信息）',0,'2017-11-21 07:57:00',NULL),(305,'/todo/todoNewProject/invoke','新建项目工单管理员处理按钮',0,'2017-11-23 10:03:03',NULL),(306,'/servergroup/query/get','按名称查询服务器组',0,'2017-11-23 11:59:33',NULL),(307,'/jenkins/jobNote','',1,'2017-12-13 09:45:55','2017-12-14 12:27:24'),(308,'/git/webHooks','webHook接口',1,'2017-12-14 12:28:49',NULL),(309,'app.webHooks','持续集成webHooks菜单',0,'2017-12-15 06:26:21',NULL),(310,'app.jenkinsJobs','持续集成任务管理菜单',0,'2017-12-15 06:27:01',NULL),(311,'app.jenkinsJobBuilds','持续集成构建任务详情页',0,'2017-12-15 06:27:29',NULL),(312,'app.ci','持续集成菜单',0,'2017-12-15 06:34:12',NULL),(313,'/jenkins/webHooks/page','webHooks详情页',0,'2017-12-15 07:09:30',NULL),(314,'/jenkins/jobs/page','持续集成构建任务管理页',0,'2017-12-15 08:05:36',NULL),(315,'/jenkins/job/builds/page','持续集成构建任务详情页',0,'2017-12-15 08:06:27',NULL),(316,'/jenkins/save','jenkins保存权限',0,'2017-12-21 06:51:31',NULL),(317,'/jenkins/jobs/del','删除任务',0,'2017-12-21 08:04:38',NULL),(318,'/jenkins/jobs/save','保存任务',0,'2017-12-21 08:04:50',NULL),(319,'/jenkins/jobs/params/query','查询任务参数',0,'2017-12-21 08:54:45',NULL),(320,'/jenkins/jobs/params/del','删除任务参数',0,'2017-12-21 08:55:06',NULL),(321,'/jenkins/jobs/params/save','保存任务参数',0,'2017-12-21 08:55:22',NULL),(322,'/jenkins/jobs/build','执行任务',0,'2017-12-21 12:13:03',NULL),(323,'/jenkins/android/jobNote','android打包通知接口',1,'2017-12-22 03:10:20','2017-12-22 03:45:35'),(324,'/jenkins/jobs/rebuild','任务详情页重新构建',0,'2017-12-26 12:50:02',NULL),(325,'/jenkins/job/refs/query','查询任务对应的仓库分支',0,'2017-12-27 09:35:28',NULL),(326,'/jenkins/jobs/ios/build','任务管理中执行ios任务',0,'2017-12-27 12:57:24',NULL),(327,'/jenkins/job/refs/get','强制获取最新分支',0,'2017-12-29 09:27:53',NULL),(328,'/git/refs/query','git分支查询',0,'2018-01-02 01:36:09',NULL),(329,'/git/refs/get','git分支查询（非缓存）',0,'2018-01-02 01:36:37',NULL),(330,'app.jenkinsFt','持续集成-前端菜单',0,'2018-01-02 03:14:18',NULL),(331,'app.jenkinsAndroid','持续集成-android菜单',0,'2018-01-02 03:14:58',NULL),(332,'app.jenkinsIos','持续集成-iOS菜单',0,'2018-01-02 03:15:18',NULL),(333,'/jenkins/saveFt','持续集成前端构建保存权限',0,'2018-01-03 01:42:40',NULL),(334,'/jenkins/saveIos','持续集成ios操作菜单',0,'2018-01-03 02:23:40',NULL),(335,'/jenkins/saveAndroid','持续集成android操作菜单',0,'2018-01-03 02:32:46',NULL),(336,'/jenkins/job/builds','持续集成单个任务详情（用于artifacts下载页面）',1,'2018-01-03 07:06:46',NULL),(337,'/jenkins/projects/page','持续集成项目详情页',0,'2018-01-08 08:43:43',NULL),(338,'/jenkins/project/save','持续集成保存项目',0,'2018-01-09 01:42:36',NULL),(339,'/jenkins/project/del','持续集成删除项目',0,'2018-01-09 02:33:53',NULL),(340,'/jenkins/projects/save','持续集成项目保存菜单',0,'2018-01-09 02:37:15',NULL),(341,'/jenkins/project/params/del','持续继承项目基本参数删除',0,'2018-01-09 03:20:02',NULL),(342,'/jenkins/project/params/save','持续继承项目基本参数保存',0,'2018-01-09 03:20:25',NULL),(343,'/jenkins/project/params/query','持续集成项目基本参数列表',0,'2018-01-09 03:40:09',NULL),(344,'/jenkins/project/env/save','持续集成项目环境保存',0,'2018-01-09 06:15:10',NULL),(345,'/jenkins/project/env/del','持续集成项目环境删除',0,'2018-01-09 06:15:26',NULL),(346,'/jenkins/project/env/query','持续集成项目环境查询',0,'2018-01-09 06:23:34',NULL),(347,'/jenkins/project/env/params/save','持续集成项目环境可变参数保存',0,'2018-01-09 07:59:40',NULL),(348,'/jenkins/project/job/save','持续继承项目环境创建job',0,'2018-01-09 10:41:44',NULL),(349,'/statistics/serverperf/task/get','查看zabbix同步任务状态',0,'2018-01-17 08:18:51',NULL),(350,'/statistics/serverperf/task/reset','重置zabbix同步任务状态',0,'2018-01-17 08:19:17',NULL),(351,'/jenkins/jobs/appLink','android官网发布',0,'2018-01-25 08:03:46',NULL),(352,'app.serverload','服务器负载',0,'2018-02-08 09:36:29',NULL),(353,'/statistics/serverperf/task/run','运行服务器性能监控同步任务',0,'2018-02-09 07:59:57',NULL),(354,'/api/users','对外开放用户查询接口',1,'2018-03-15 05:53:32',NULL),(355,'app.scmgroups','scm用户组管理菜单',0,'2018-04-10 02:19:51',NULL),(356,'/scm/permissions/page','SCM权限组详情页',0,'2018-04-10 02:54:00',NULL),(357,'/scm/permissions/refresh','SCM数据同步（创建新项目的配置条目）',0,'2018-04-10 06:20:51',NULL),(358,'/scm/permissions/save','保存SCM配置',0,'2018-04-10 08:03:48',NULL),(359,'/scm/permissions/get','查询SCM组配置',0,'2018-04-11 07:49:09',NULL),(360,'app.jenkinsTest','持续集成QC自动化测试任务管理菜单',0,'2018-04-18 01:44:34',NULL),(361,'/jenkins/jobs/create','创建jenkins job',0,'2018-04-24 03:23:01',NULL),(362,'/todo/todoTomcatVersion/tomcatVersion/query','Tomcat版本变更工单查询安装版本',0,'2018-04-25 08:49:10',NULL),(363,'/todo/todoTomcatVersion/task/updateTomcat','Tomcat版本变更工单执行自动升级任务',0,'2018-04-26 06:45:59',NULL),(364,'/jenkins/job/refs/change','手动维护 refs接口',0,'2018-05-07 09:22:31',NULL),(365,'/server/ecsSync','同步阿里云ECS数据并校验',0,'2018-05-09 03:47:23',NULL),(366,'/cmdb/zabbix/remove','离职删除zabbix用户',0,'2018-05-11 03:44:53',NULL),(367,'/zabbixserver/repair','修复Server表中的Zabbix监控相关数据',0,'2018-05-11 09:09:16',NULL),(368,'/todo/todoTomcatVersion/task/rollbackTomcat','tomcat版本变更工单回滚按钮',0,'2018-05-14 10:12:29','2018-05-14 10:13:12'),(369,'/task/cmd/doCmd','任务管理执行指令',0,'2018-05-15 07:16:13',NULL),(370,'/task/cmd/query','任务结果查询',0,'2018-05-17 05:56:42',NULL),(371,'/task/script/page','任务脚本详情页',0,'2018-05-22 06:50:17',NULL),(372,'/task/script/save','任务脚本保存权限',0,'2018-05-22 06:53:25',NULL),(373,'app.taskScript','任务脚本菜单',0,'2018-05-22 07:42:13',NULL),(374,'/task/cmd/doScript','服务器任务执行Script',0,'2018-05-23 02:27:27',NULL),(375,'/task/ansible/version','ansible版本信息查询',0,'2018-05-24 02:12:53',NULL),(376,'/task/ansibleTask/page','ansible历史任务查看',0,'2018-05-24 07:34:25',NULL),(377,'/servergroup/useType/page','服务器组使用类型分页详情',0,'2018-05-29 06:47:51',NULL),(378,'/servergroup/useType/save','保存服务器组使用类型',0,'2018-05-29 07:33:50',NULL),(379,'/servergroup/useType/del','删除服务器使用类型',0,'2018-05-29 08:08:12',NULL),(380,'/servergroup/useType/query','查询服务器组使用类型列表',0,'2018-05-29 08:17:12',NULL),(381,'/config/center/get','获取配置中心的配置组',0,'2018-05-29 10:48:33',NULL),(382,'/aliyun/api/describeRegions','查询regions',0,'2018-05-30 01:13:36',NULL),(383,'/config/center/update','更新配置(map)',0,'2018-05-30 02:25:38',NULL),(384,'/aliyun/api/describeImages','获取ECS镜像列表',0,'2018-05-30 06:09:09',NULL),(385,'/aliyun/image/save','保存阿里云ECS镜像',0,'2018-05-30 07:32:45',NULL),(386,'/aliyun/image/del','删除阿里云ECS镜像',0,'2018-05-30 07:33:06',NULL),(387,'/aliyun/vpc/get','查询阿里云vpc网络详情（本地数据）',0,'2018-05-31 06:26:10',NULL),(388,'/aliyun/vpc/del','删除阿里云vpc网络详情（本地数据）',0,'2018-05-31 06:26:34',NULL),(389,'/aliyun/vpc/rsync','同步阿里云Vpc网络配置',0,'2018-05-31 07:05:16',NULL),(390,'/zabbixserver/version','检查zabbix配置并获得版本信息',0,'2018-05-31 12:10:19',NULL),(391,'/zabbixserver/template/page','zabbix模版详情页',0,'2018-06-01 03:33:57',NULL),(392,'/zabbixserver/template/set','zabbix模版是否启用',0,'2018-06-01 03:34:32',NULL),(393,'/zabbixserver/template/del','zabbix模版删除',0,'2018-06-01 03:34:55',NULL),(394,'/zabbixserver/template/rsync','zabbix模版同步',0,'2018-06-01 03:35:15',NULL),(395,'/zabbixserver/proxy/query','查询zabbixserver的所有proxy',0,'2018-06-01 10:13:04',NULL),(396,'/zabbixserver/template/get','获取服务器组的Zabbix模版列表',0,'2018-06-04 01:43:24',NULL),(397,'/zabbixserver/proxy/get','获取服务器组的Zabbix代理列表',0,'2018-06-04 01:43:58',NULL),(398,'/zabbixserver/host/get','查询zabbix主机相关的配置',0,'2018-06-04 03:38:48',NULL),(399,'/zabbixserver/host/save','保存zabbix主机监控配置',0,'2018-06-04 03:39:10',NULL),(400,'app.ansibleConfigFile','Ansible主机配置文件管理菜单',0,'2018-06-04 08:20:30',NULL),(401,'/config/file/queryPath','查询不重复的文件路径',0,'2018-06-05 05:16:39',NULL),(402,'/config/fileCopy/save','保存服务器同步配置',0,'2018-06-05 05:53:41',NULL),(403,'/config/fileCopy/query','查询配置文件同步配置页',0,'2018-06-05 06:17:40',NULL),(404,'/config/fileCopy/del','删除服务器同步配置',0,'2018-06-05 07:01:24',NULL),(405,'/task/copy/doFileCopy','远程同步文件任务',0,'2018-06-05 08:22:17','2018-06-05 08:23:26'),(406,'/task/copy/doFileCopyAll','批量远程同步配置',0,'2018-06-05 09:03:12',NULL),(407,'app.getwayConfigFile','Getway管理菜单',0,'2018-06-05 10:09:28',NULL),(408,'/box/user/getway/launch','查看用户Getway配置文件',0,'2018-06-06 07:15:51',NULL),(409,'/config/file/getGetwayPath','取Getway用户文件目录配置',0,'2018-06-06 10:06:09',NULL),(410,'/task/cmd/doScript/updateGetway','更新getway',0,'2018-06-08 06:37:00',NULL),(411,'app.shadowsocksConfigFile','SS配置管理菜单',0,'2018-06-11 01:52:09',NULL),(412,'/config/fileCopy/script/save','配置文件远程同步后执行配置',0,'2018-06-11 05:47:55',NULL),(413,'/config/fileCopy/script/query','远程同步后执行Script配置详情页',0,'2018-06-11 06:31:39',NULL),(414,'/task/cmd/copySever/doScript','执行同步后的Script',0,'2018-06-11 08:09:03',NULL),(415,'/task/cmd/copySever/doScriptByGroup','执行同步后的ScriptByGroupName',0,'2018-06-11 08:09:35',NULL),(416,'/config/getway/saveKey','创建privteKey',0,'2018-06-11 10:31:50',NULL),(417,'/task/cmd/doScript/getwaySetLogin','配置getway登录限制',0,'2018-06-11 11:07:25',NULL),(418,'/aliyun/api/describeInstanceTypes','查询Instance类型',0,'2018-06-13 10:19:02',NULL),(419,'/aliyun/api/describeZones','查询阿里云地域列表',0,'2018-06-14 02:25:35',NULL),(420,'/check/authV2','auth支持POST',0,'2018-06-14 02:52:06',NULL),(421,'/aliyun/template/save','保存阿里云ECS模版',0,'2018-06-14 08:55:37',NULL),(422,'/aliyun/template/del','删除阿里云模版',0,'2018-06-14 08:55:50',NULL),(423,'/server/vcsa/version','VCSA查询版本信息',0,'2018-06-15 03:41:44',NULL),(424,'/aliyun/mgmt','阿里云配置管理',0,'2018-08-09 07:34:58',NULL),(425,'/server/vcsa/mgmt','vcsa配置管理',0,'2018-08-09 08:04:20',NULL),(426,'app.nginxConfig','nginx配置管理菜单',0,'2018-08-22 09:08:57','2018-11-14 02:16:20'),(427,'/nginx/vhost/page','nginx虚拟主机详情页',0,'2018-10-09 08:42:04',NULL),(428,'/nginx/vhost/env/save','nginx虚拟主机环境保存',0,'2018-10-31 01:34:51',NULL),(429,'/nginx/vhost/get','nginx虚拟主机查询',0,'2018-10-31 01:35:58',NULL),(430,'/nginx/vhost/env/del','nginx删除虚拟主机环境配置',0,'2018-10-31 03:44:38',NULL),(431,'/nginx/vhost/env/file/save','nginx配置文件保存',0,'2018-11-01 01:17:53',NULL),(432,'/nginx/vhost/env/file/launch','预览文件',0,'2018-11-02 02:46:51','2018-11-02 03:50:27'),(433,'/nginx/vhost/env/file/build','生成配置文件',0,'2018-11-02 02:47:12',NULL),(434,'/nginx/vhost/del','删除vhost配置',0,'2018-11-02 08:47:47',NULL),(435,'/nginx/vhost/serverGroup/add','nginx虚拟主机增加服务器组',0,'2018-11-02 08:49:02',NULL),(436,'/nginx/vhost/serverGroup/del','nginx虚拟主机删除服务器组',0,'2018-11-02 08:49:32',NULL),(437,'/nginx/vhost/serverGroup/query','nginx查询虚拟主机服务器组',0,'2018-11-02 08:50:02',NULL),(438,'/nginx/vhost/save','nginx虚拟主机保存',0,'2018-11-02 09:02:13',NULL),(439,'/copy/page','配置同步详情页',0,'2018-11-05 08:53:10',NULL),(440,'/copy/save','保存copy配置',0,'2018-11-06 08:58:18',NULL),(441,'/copy/addServer','copy配置项新增服务器',0,'2018-11-07 02:38:46',NULL),(442,'/copy/delServer','copy配置项删除服务器',0,'2018-11-07 02:39:03',NULL),(443,'/copy/get','查询copy配置',0,'2018-11-07 02:54:57',NULL),(444,'/copy/doCopy','执行copy任务',0,'2018-11-09 10:07:06',NULL),(445,'/copy/log/page','copyLog详情页',0,'2018-11-12 03:09:44',NULL),(446,'/copy/log/del','同步日志删除',0,'2018-11-12 03:10:05',NULL),(447,'/copy/del','删除同步配置',0,'2018-11-12 06:27:22',NULL),(448,'/team/page','团队详情页',0,'2018-11-15 02:24:35',NULL),(449,'/team/del','删除团队信息',0,'2018-11-15 02:25:02',NULL),(450,'/team/save','保存团队信息',0,'2018-11-15 02:26:40',NULL),(451,'/team/teamuser/del','删除团队成员',0,'2018-11-15 02:27:12',NULL),(452,'/team/teamuser/save','保存团队成员',0,'2018-11-15 02:27:33',NULL),(453,'app.teamMgmt','团队管理',0,'2018-11-15 02:37:15',NULL),(454,'/team/get','获取团队信息',0,'2018-11-15 09:15:27',NULL),(455,'app.workflow','工作流菜单',0,'2018-11-16 03:43:08',NULL),(456,'/workflow/queryGroup','查询工主流组详情',0,'2018-11-19 03:22:29',NULL),(457,'/workflow/todo/create','新建工作流',0,'2018-11-22 06:17:02',NULL),(458,'/workflow/todo/save','保存工作流todo',0,'2018-11-23 08:20:35',NULL),(459,'/workflow/todo/detail/del','删除工单细节',0,'2018-11-23 09:59:19',NULL),(460,'/workflow/todo/apply','工作流提交申请',0,'2018-11-23 10:51:42','2018-11-23 10:58:32');
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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_rgroup`
--

LOCK TABLES `new_auth_rgroup` WRITE;
/*!40000 ALTER TABLE `new_auth_rgroup` DISABLE KEYS */;
INSERT INTO `new_auth_rgroup` VALUES (1,'menu','系统菜单','2016-09-19 11:14:13','2016-10-14 03:47:40'),(5,'ip','ip管理','2016-09-22 12:09:10','2016-10-14 03:47:30'),(6,'auth','权限管理','2016-09-22 12:10:48','2016-10-14 03:47:26'),(7,'ipgroup','ip组管理','2016-09-22 12:11:07','2016-10-14 03:47:21'),(8,'server','服务器管理','2016-09-22 12:11:41','2016-10-14 03:47:16'),(9,'servergroup','服务器组管理','2016-09-22 12:11:59','2016-10-14 03:47:07'),(10,'system','系统管理','2016-10-08 07:28:11','2016-10-14 03:47:02'),(11,'todo','日常工单管理','2016-10-09 09:29:07','2016-10-27 08:24:58'),(12,'property','属性管理','2016-10-27 08:23:58',NULL),(13,'todoConfig','工单配置项管理','2016-10-27 08:24:47',NULL),(14,'keybox','堡垒机','2016-11-28 04:58:02',NULL),(15,'user','用户相关','2016-11-28 05:00:47',NULL),(16,'configFile','配置管理','2016-12-29 11:14:34',NULL),(17,'zabbixserver','监控管理','2017-01-18 06:04:24',NULL),(18,'statistics','统计','2017-02-17 07:32:39',NULL),(19,'task','任务管理','2017-03-31 11:36:58',NULL),(20,'explain','SQL审核','2017-04-01 03:37:59',NULL),(21,'configCenter','配置中心','2017-06-01 01:58:45',NULL),(23,'aliyun','阿里云','2017-06-12 09:56:57',NULL),(24,'dns','dns管理','2017-07-11 10:21:41',NULL),(25,'ecsServer','ECS服务器','2017-08-29 03:10:43',NULL),(26,'ecsTemplate','ecs模版','2017-08-29 03:11:01',NULL),(27,'vmTemplate','VM模版','2017-08-29 05:57:35',NULL),(28,'logService','日志服务','2017-09-20 05:50:14',NULL),(29,'projectManagement','项目线上生命管理','2017-10-26 06:47:25','2017-11-01 11:31:49'),(31,'projectHeartbeat','项目线上生命心跳','2017-11-01 11:33:28',NULL),(32,'jenkins','jenkins接口','2017-12-13 09:45:39',NULL),(33,'gitlab','gitlab资源','2017-12-14 12:28:33',NULL),(34,'git','git仓库管理','2018-01-02 01:35:35',NULL),(35,'nginx','nginx配置管理','2018-10-09 08:41:32',NULL),(36,'copy','copy配置同步管理','2018-11-05 08:52:13',NULL),(37,'team','团队配置','2018-11-15 02:23:58',NULL),(38,'workflow','工作流','2018-11-19 03:21:44',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_role`
--

LOCK TABLES `new_auth_role` WRITE;
/*!40000 ALTER TABLE `new_auth_role` DISABLE KEYS */;
INSERT INTO `new_auth_role` VALUES (1,'admin','管理员','2016-09-19 07:36:16','2016-09-22 03:00:30'),(3,'dba','数据库管理员','2016-10-13 02:35:33',NULL),(4,'base','普通用户','2016-10-20 07:28:26','2018-06-08 00:57:34'),(5,'dev','开发人员','2016-11-04 03:50:04','2018-01-02 03:11:37'),(6,'devops','运维工程师','2017-09-07 10:29:09',NULL),(7,'devAndroid','android开发','2017-12-22 07:28:18','2018-01-12 06:47:53'),(8,'devIos','iso开发','2017-12-28 05:54:52','2018-01-12 06:47:48'),(9,'devFt','前端开发','2018-01-02 03:12:11','2018-01-12 06:47:40'),(10,'qa','质量管理工程师','2018-04-18 01:52:05',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=721 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_role_resources`
--

LOCK TABLES `new_auth_role_resources` WRITE;
/*!40000 ALTER TABLE `new_auth_role_resources` DISABLE KEYS */;
INSERT INTO `new_auth_role_resources` VALUES (1,1,1,'2016-09-19 07:36:24',NULL),(2,1,2,'2016-09-19 07:36:29',NULL),(3,1,3,'2016-09-19 07:36:33',NULL),(4,1,4,'2016-09-19 07:36:38',NULL),(5,1,5,'2016-09-19 07:36:42',NULL),(6,1,7,'2016-09-22 06:44:25',NULL),(7,1,8,'2016-09-22 06:44:27',NULL),(8,1,9,'2016-09-22 06:44:27',NULL),(9,1,10,'2016-09-22 06:44:28',NULL),(10,1,12,'2016-09-22 12:34:54',NULL),(13,1,13,'2016-09-22 12:37:38',NULL),(14,1,14,'2016-09-22 12:40:31',NULL),(15,1,15,'2016-09-22 12:40:32',NULL),(16,1,16,'2016-09-22 12:40:33',NULL),(17,1,17,'2016-09-22 12:40:34',NULL),(18,1,18,'2016-09-22 12:40:34',NULL),(19,1,19,'2016-09-22 12:40:35',NULL),(20,1,20,'2016-09-22 12:40:35',NULL),(21,1,21,'2016-09-22 12:40:35',NULL),(22,1,22,'2016-09-22 12:40:36',NULL),(23,1,23,'2016-09-22 12:40:36',NULL),(24,1,24,'2016-09-22 12:40:36',NULL),(25,1,25,'2016-09-22 12:40:36',NULL),(26,1,26,'2016-09-22 12:40:37',NULL),(27,1,27,'2016-09-22 12:40:37',NULL),(28,1,28,'2016-09-22 12:40:39',NULL),(29,1,29,'2016-09-22 12:40:40',NULL),(30,1,30,'2016-09-22 12:40:41',NULL),(31,1,31,'2016-09-22 12:40:41',NULL),(32,1,32,'2016-09-22 12:40:42',NULL),(33,1,33,'2016-09-22 12:40:43',NULL),(34,1,34,'2016-09-22 12:40:44',NULL),(35,1,35,'2016-09-22 12:40:44',NULL),(36,1,36,'2016-09-22 12:40:45',NULL),(37,1,37,'2016-09-22 12:40:46',NULL),(38,1,38,'2016-09-22 12:40:46',NULL),(39,1,39,'2016-09-22 12:40:47',NULL),(40,1,40,'2016-09-22 12:40:48',NULL),(41,1,41,'2016-09-22 12:40:49',NULL),(42,1,42,'2016-09-22 12:40:49',NULL),(43,1,43,'2016-09-22 12:40:50',NULL),(44,1,44,'2016-09-22 12:40:50',NULL),(45,1,45,'2016-09-23 03:33:16',NULL),(46,1,46,'2016-09-23 03:33:17',NULL),(47,1,47,'2016-09-23 09:33:25',NULL),(48,1,48,'2016-09-29 06:35:24',NULL),(49,1,49,'2016-10-08 06:57:17',NULL),(50,1,50,'2016-10-08 07:31:12',NULL),(51,1,51,'2016-10-08 07:31:12',NULL),(52,1,52,'2016-10-08 07:31:13',NULL),(53,1,53,'2016-10-09 08:49:40',NULL),(54,1,54,'2016-10-09 08:49:40',NULL),(55,1,55,'2016-10-09 09:30:48',NULL),(56,1,56,'2016-10-09 09:30:49',NULL),(57,1,57,'2016-10-09 09:30:49',NULL),(58,1,58,'2016-10-10 11:09:19',NULL),(59,1,59,'2016-10-11 08:24:00',NULL),(60,1,60,'2016-10-11 08:42:32',NULL),(61,1,61,'2016-10-12 07:29:08',NULL),(62,3,1,'2016-10-14 03:39:46',NULL),(63,3,2,'2016-10-14 03:40:11',NULL),(64,3,3,'2016-10-14 03:40:11',NULL),(65,3,4,'2016-10-14 03:40:12',NULL),(66,3,7,'2016-10-14 03:40:12',NULL),(70,3,49,'2016-10-14 03:40:22',NULL),(71,3,53,'2016-10-14 03:40:22',NULL),(72,3,54,'2016-10-14 03:40:23',NULL),(73,3,55,'2016-10-14 03:40:36',NULL),(74,3,56,'2016-10-14 03:40:37',NULL),(75,3,57,'2016-10-14 03:40:38',NULL),(76,3,58,'2016-10-14 03:40:38',NULL),(77,3,59,'2016-10-14 03:40:38',NULL),(78,3,60,'2016-10-14 03:40:39',NULL),(79,3,61,'2016-10-14 03:40:39',NULL),(80,1,62,'2016-10-19 06:39:42',NULL),(81,3,62,'2016-10-19 06:39:47',NULL),(88,1,63,'2016-10-21 02:07:33',NULL),(89,1,64,'2016-10-21 02:49:11',NULL),(90,1,65,'2016-10-21 03:08:45',NULL),(91,1,66,'2016-10-21 03:17:01',NULL),(92,1,67,'2016-10-21 05:51:31',NULL),(93,1,68,'2016-10-21 06:03:23',NULL),(94,1,69,'2016-10-21 06:03:23',NULL),(96,1,71,'2016-10-25 03:20:52',NULL),(98,4,2,'2016-10-28 02:03:10',NULL),(99,4,40,'2016-10-28 02:03:15',NULL),(100,4,71,'2016-10-28 02:03:24',NULL),(101,4,3,'2016-10-28 02:03:34',NULL),(102,4,37,'2016-10-28 02:03:39',NULL),(103,4,4,'2016-10-28 02:03:47',NULL),(104,4,32,'2016-10-28 02:03:52',NULL),(105,4,36,'2016-10-28 02:03:54',NULL),(106,4,7,'2016-10-28 02:04:08',NULL),(107,4,29,'2016-10-28 02:04:10',NULL),(108,4,47,'2016-10-28 02:04:14',NULL),(109,4,1,'2016-10-28 02:04:37',NULL),(110,4,54,'2016-10-28 02:04:49',NULL),(111,4,58,'2016-10-28 02:04:51',NULL),(112,4,59,'2016-10-28 02:04:55',NULL),(113,4,60,'2016-10-28 02:04:56',NULL),(114,4,61,'2016-10-28 02:05:01',NULL),(115,4,62,'2016-10-28 02:05:02',NULL),(116,4,12,'2016-10-28 02:35:07',NULL),(117,4,55,'2016-10-28 09:22:16',NULL),(118,1,73,'2016-10-31 06:59:27',NULL),(119,1,74,'2016-10-31 09:48:40',NULL),(120,4,74,'2016-10-31 09:48:58',NULL),(121,1,75,'2016-10-31 11:49:26',NULL),(122,4,75,'2016-10-31 11:49:36',NULL),(123,5,48,'2016-11-04 03:50:14',NULL),(124,1,76,'2016-11-28 05:02:48',NULL),(125,1,77,'2016-11-28 05:02:49',NULL),(126,1,78,'2016-11-28 05:02:50',NULL),(127,1,79,'2016-11-28 05:02:50',NULL),(128,1,80,'2016-11-28 05:02:50',NULL),(129,1,81,'2016-11-28 05:02:51',NULL),(130,1,82,'2016-11-28 05:02:51',NULL),(131,1,83,'2016-11-28 05:02:52',NULL),(132,1,84,'2016-11-28 05:02:52',NULL),(133,1,85,'2016-11-28 05:02:52',NULL),(134,1,86,'2016-11-28 05:02:52',NULL),(135,1,87,'2016-11-28 05:02:53',NULL),(136,4,88,'2016-12-01 07:51:09',NULL),(137,4,90,'2016-12-01 07:51:11',NULL),(138,1,88,'2016-12-01 07:51:18',NULL),(139,1,89,'2016-12-01 07:51:19',NULL),(140,1,90,'2016-12-01 07:51:20',NULL),(141,4,91,'2016-12-01 07:56:27',NULL),(142,4,92,'2016-12-05 02:11:51',NULL),(143,5,85,'2016-12-05 02:12:07',NULL),(144,5,86,'2016-12-13 10:01:47',NULL),(145,1,70,'2016-12-22 09:39:02',NULL),(146,1,72,'2016-12-22 09:39:02',NULL),(147,1,93,'2016-12-22 09:40:22',NULL),(148,1,94,'2016-12-29 03:47:58',NULL),(149,4,95,'2016-12-29 09:04:58',NULL),(150,4,52,'2016-12-29 09:05:03',NULL),(151,4,93,'2016-12-29 09:07:43',NULL),(152,1,96,'2016-12-29 11:18:02',NULL),(153,1,97,'2016-12-29 11:18:02',NULL),(154,1,98,'2016-12-29 11:18:03',NULL),(155,1,99,'2016-12-29 11:18:04',NULL),(156,1,101,'2016-12-29 11:18:05',NULL),(157,1,100,'2016-12-29 11:18:05',NULL),(158,1,102,'2016-12-29 11:18:06',NULL),(160,1,103,'2016-12-29 11:18:07',NULL),(161,1,104,'2016-12-29 11:18:08',NULL),(162,1,91,'2016-12-29 11:18:16',NULL),(163,1,106,'2016-12-29 11:18:17',NULL),(164,1,105,'2016-12-29 11:18:19',NULL),(165,1,107,'2017-01-03 02:59:49',NULL),(166,3,19,'2017-01-03 03:16:19',NULL),(167,1,108,'2017-01-10 13:20:46',NULL),(168,1,109,'2017-01-10 13:20:46',NULL),(169,1,110,'2017-01-11 03:54:12',NULL),(170,4,109,'2017-01-11 06:11:54',NULL),(171,1,111,'2017-01-11 10:38:28',NULL),(172,1,112,'2017-01-11 10:38:28',NULL),(173,1,113,'2017-01-11 10:40:56',NULL),(174,1,114,'2017-01-12 07:49:32',NULL),(175,1,115,'2017-01-12 07:49:34',NULL),(176,1,116,'2017-01-12 07:49:35',NULL),(177,1,117,'2017-01-12 07:49:35',NULL),(178,1,118,'2017-01-12 07:49:36',NULL),(179,1,119,'2017-01-12 07:49:36',NULL),(180,1,120,'2017-01-12 07:49:37',NULL),(181,1,121,'2017-01-12 07:49:37',NULL),(182,1,122,'2017-01-12 07:49:38',NULL),(183,1,123,'2017-01-12 07:49:38',NULL),(184,5,3,'2017-01-16 03:35:04',NULL),(185,5,37,'2017-01-16 03:35:07',NULL),(186,5,93,'2017-01-16 03:35:12',NULL),(187,5,117,'2017-01-16 03:35:22',NULL),(188,5,109,'2017-01-16 03:35:34',NULL),(189,1,124,'2017-01-18 06:06:30',NULL),(190,1,125,'2017-01-18 06:06:30',NULL),(191,1,126,'2017-01-18 06:06:31',NULL),(192,1,127,'2017-01-18 06:06:31',NULL),(193,1,128,'2017-01-18 06:06:31',NULL),(194,1,129,'2017-01-18 06:06:31',NULL),(196,1,130,'2017-01-18 06:06:33',NULL),(197,3,124,'2017-01-18 06:06:38',NULL),(198,3,125,'2017-01-18 06:06:39',NULL),(199,3,126,'2017-01-18 06:06:39',NULL),(200,3,127,'2017-01-18 06:06:39',NULL),(201,3,128,'2017-01-18 06:06:39',NULL),(202,3,129,'2017-01-18 06:06:40',NULL),(203,3,130,'2017-01-18 06:06:40',NULL),(204,1,131,'2017-01-18 06:08:03',NULL),(205,3,131,'2017-01-18 06:08:13',NULL),(206,3,37,'2017-01-18 07:06:10',NULL),(207,3,109,'2017-01-18 07:06:19',NULL),(208,3,117,'2017-01-18 07:06:33',NULL),(209,1,92,'2017-01-20 06:17:53',NULL),(210,1,132,'2017-01-20 06:17:53',NULL),(211,1,133,'2017-01-20 06:17:54',NULL),(212,1,134,'2017-02-07 03:37:54',NULL),(213,1,135,'2017-02-07 03:37:55',NULL),(214,1,136,'2017-02-07 03:37:56',NULL),(215,1,138,'2017-02-09 08:18:29',NULL),(216,1,139,'2017-02-09 08:18:30',NULL),(217,5,138,'2017-02-09 08:18:45',NULL),(218,5,139,'2017-02-09 08:18:46',NULL),(219,3,138,'2017-02-09 08:18:54',NULL),(220,3,139,'2017-02-09 08:18:55',NULL),(221,1,140,'2017-02-14 06:36:20',NULL),(222,1,141,'2017-02-14 06:36:21',NULL),(223,5,140,'2017-02-14 06:36:30',NULL),(224,5,141,'2017-02-14 06:36:31',NULL),(225,1,142,'2017-02-17 07:33:46',NULL),(226,5,142,'2017-02-17 07:34:01',NULL),(227,1,143,'2017-02-17 07:34:47',NULL),(228,5,143,'2017-02-17 07:34:53',NULL),(229,1,144,'2017-02-24 05:48:03',NULL),(230,5,144,'2017-02-24 05:48:07',NULL),(231,1,145,'2017-02-24 05:48:54',NULL),(232,5,145,'2017-02-24 05:49:03',NULL),(233,1,146,'2017-02-24 05:50:07',NULL),(234,5,146,'2017-02-24 05:50:13',NULL),(235,1,147,'2017-02-28 07:48:01',NULL),(236,1,148,'2017-02-28 07:48:08',NULL),(237,5,148,'2017-02-28 07:48:20',NULL),(238,5,147,'2017-03-01 12:53:43',NULL),(239,1,149,'2017-03-02 08:09:09',NULL),(240,5,149,'2017-03-02 08:09:13',NULL),(241,1,150,'2017-03-03 02:55:45',NULL),(242,3,136,'2017-03-10 06:34:58',NULL),(243,3,134,'2017-03-10 06:35:07',NULL),(244,3,135,'2017-03-10 06:35:08',NULL),(245,3,88,'2017-03-10 06:37:58',NULL),(246,3,83,'2017-03-10 06:38:00',NULL),(247,1,154,'2017-03-31 11:39:40',NULL),(248,1,156,'2017-03-31 11:39:41',NULL),(249,1,155,'2017-03-31 11:39:41',NULL),(250,1,151,'2017-03-31 11:39:49',NULL),(251,1,153,'2017-03-31 11:39:50',NULL),(252,5,157,'2017-04-01 03:39:05',NULL),(253,5,158,'2017-04-01 03:44:13',NULL),(254,5,159,'2017-04-01 03:44:15',NULL),(255,5,160,'2017-04-01 03:44:16',NULL),(256,5,161,'2017-04-01 03:44:19',NULL),(257,5,162,'2017-04-01 03:44:22',NULL),(258,1,163,'2017-04-01 09:40:44',NULL),(259,1,164,'2017-04-05 02:54:12',NULL),(260,1,165,'2017-04-05 02:54:12',NULL),(261,1,166,'2017-04-05 02:54:13',NULL),(262,5,152,'2017-04-12 02:20:40',NULL),(263,1,168,'2017-04-12 07:07:57',NULL),(264,1,169,'2017-04-12 08:20:31',NULL),(265,1,172,'2017-04-13 10:14:10',NULL),(266,5,172,'2017-04-14 03:11:45',NULL),(267,5,151,'2017-04-18 03:50:43',NULL),(268,1,173,'2017-04-20 03:24:14',NULL),(269,1,174,'2017-04-20 03:24:15',NULL),(270,1,175,'2017-04-20 09:11:42',NULL),(271,1,176,'2017-04-20 09:20:55',NULL),(272,1,177,'2017-04-21 02:53:19',NULL),(273,1,179,'2017-04-25 07:51:43',NULL),(274,1,180,'2017-04-25 07:51:43',NULL),(275,3,38,'2017-04-27 01:41:50',NULL),(277,3,39,'2017-04-27 01:41:51',NULL),(278,3,93,'2017-04-27 01:41:51',NULL),(279,3,108,'2017-04-27 01:41:52',NULL),(280,3,110,'2017-04-27 01:41:52',NULL),(281,3,111,'2017-04-27 01:41:52',NULL),(283,3,112,'2017-04-27 01:41:53',NULL),(284,3,113,'2017-04-27 01:41:53',NULL),(285,3,114,'2017-04-27 01:41:53',NULL),(286,3,115,'2017-04-27 01:41:54',NULL),(287,3,116,'2017-04-27 01:41:54',NULL),(288,3,118,'2017-04-27 01:41:55',NULL),(289,3,119,'2017-04-27 01:41:55',NULL),(290,3,120,'2017-04-27 01:41:55',NULL),(291,3,121,'2017-04-27 01:41:56',NULL),(292,3,122,'2017-04-27 01:41:56',NULL),(293,3,123,'2017-04-27 01:41:56',NULL),(294,3,140,'2017-04-27 01:41:57',NULL),(295,3,141,'2017-04-27 01:41:57',NULL),(296,3,176,'2017-04-27 01:41:57',NULL),(298,3,179,'2017-04-27 01:41:58',NULL),(299,3,180,'2017-04-27 01:41:58',NULL),(300,3,177,'2017-04-27 01:41:59',NULL),(301,3,40,'2017-04-27 01:42:23',NULL),(302,3,41,'2017-04-27 01:42:24',NULL),(303,3,42,'2017-04-27 01:42:24',NULL),(304,3,43,'2017-04-27 01:42:24',NULL),(305,3,44,'2017-04-27 01:42:25',NULL),(306,3,71,'2017-04-27 01:42:25',NULL),(307,3,73,'2017-04-27 01:42:25',NULL),(309,3,75,'2017-04-27 01:42:26',NULL),(310,3,107,'2017-04-27 01:42:26',NULL),(311,3,152,'2017-04-27 01:42:26',NULL),(312,3,74,'2017-04-27 01:42:27',NULL),(313,1,181,'2017-05-08 04:57:19',NULL),(314,1,182,'2017-05-08 04:57:20',NULL),(315,1,183,'2017-05-10 08:21:27',NULL),(316,1,184,'2017-05-24 08:25:30',NULL),(317,1,185,'2017-06-01 01:59:32',NULL),(318,1,186,'2017-06-01 02:00:34',NULL),(319,1,187,'2017-06-01 03:33:57',NULL),(320,1,188,'2017-06-01 03:33:58',NULL),(321,1,189,'2017-06-01 03:33:59',NULL),(322,1,191,'2017-06-06 02:25:58',NULL),(323,1,192,'2017-06-12 05:59:19',NULL),(324,1,193,'2017-06-12 09:57:36',NULL),(325,1,194,'2017-06-13 02:03:09',NULL),(326,1,195,'2017-06-13 05:18:16',NULL),(327,1,197,'2017-06-13 05:18:17',NULL),(328,1,196,'2017-06-13 05:18:18',NULL),(329,1,198,'2017-06-21 08:26:10',NULL),(330,1,199,'2017-06-21 09:00:30',NULL),(331,1,200,'2017-06-21 09:34:15',NULL),(332,1,201,'2017-06-22 10:19:13',NULL),(333,1,202,'2017-06-28 05:46:05',NULL),(334,1,203,'2017-06-28 05:46:06',NULL),(335,1,204,'2017-06-28 07:51:02',NULL),(336,1,205,'2017-06-29 03:26:32',NULL),(337,1,206,'2017-07-11 09:12:05',NULL),(338,1,207,'2017-07-11 10:22:21',NULL),(339,1,208,'2017-07-13 10:38:59',NULL),(340,1,209,'2017-07-13 10:38:59',NULL),(341,1,210,'2017-07-13 10:39:00',NULL),(342,1,211,'2017-08-04 02:02:23',NULL),(343,1,212,'2017-08-07 06:22:51',NULL),(344,1,213,'2017-08-07 07:42:18',NULL),(345,1,214,'2017-08-10 08:14:45',NULL),(346,1,215,'2017-08-10 08:14:46',NULL),(347,1,216,'2017-08-10 08:14:47',NULL),(348,1,217,'2017-08-10 08:34:27',NULL),(349,1,218,'2017-08-18 02:03:25',NULL),(350,1,219,'2017-08-18 02:06:00',NULL),(351,1,220,'2017-08-18 07:38:27',NULL),(352,1,221,'2017-08-18 08:27:33',NULL),(353,1,222,'2017-08-18 08:27:34',NULL),(354,1,223,'2017-08-21 06:25:28',NULL),(355,1,224,'2017-08-21 06:32:58',NULL),(356,1,225,'2017-08-22 03:07:17',NULL),(357,1,226,'2017-08-23 08:36:43',NULL),(358,1,227,'2017-08-24 03:17:44',NULL),(359,1,228,'2017-08-24 09:10:27',NULL),(360,1,229,'2017-08-24 09:11:08',NULL),(361,1,230,'2017-08-24 10:19:18',NULL),(362,1,157,'2017-08-29 02:09:35',NULL),(363,1,231,'2017-08-29 02:09:35',NULL),(364,1,232,'2017-08-29 02:09:36',NULL),(365,1,233,'2017-08-29 02:09:36',NULL),(366,5,231,'2017-08-29 05:51:34',NULL),(367,5,232,'2017-08-29 05:51:35',NULL),(368,5,233,'2017-08-29 05:51:35',NULL),(369,5,176,'2017-08-29 05:54:39',NULL),(370,5,191,'2017-08-29 05:58:35',NULL),(371,1,234,'2017-09-05 07:38:30',NULL),(372,1,235,'2017-09-05 07:38:31',NULL),(373,1,236,'2017-09-05 09:19:09',NULL),(375,1,237,'2017-09-05 11:02:25',NULL),(376,4,237,'2017-09-05 11:02:38',NULL),(377,4,238,'2017-09-07 03:51:36',NULL),(378,4,239,'2017-09-07 08:41:52',NULL),(379,4,240,'2017-09-07 08:41:53',NULL),(380,4,241,'2017-09-07 08:41:53',NULL),(381,4,242,'2017-09-07 13:37:11',NULL),(382,4,243,'2017-09-08 02:18:04',NULL),(383,4,244,'2017-09-08 07:59:50',NULL),(385,1,245,'2017-09-08 10:41:52',NULL),(386,4,246,'2017-09-08 11:41:54',NULL),(387,4,236,'2017-09-11 06:23:47',NULL),(388,4,247,'2017-09-11 10:33:25',NULL),(389,4,248,'2017-09-11 10:33:26',NULL),(390,4,219,'2017-09-12 03:19:24',NULL),(391,4,249,'2017-09-13 10:53:04',NULL),(392,5,250,'2017-09-20 02:59:34',NULL),(393,1,250,'2017-09-20 03:00:10',NULL),(394,5,251,'2017-09-20 05:51:55',NULL),(395,1,251,'2017-09-20 05:52:00',NULL),(396,5,252,'2017-09-20 12:35:03',NULL),(397,5,253,'2017-09-21 05:52:03',NULL),(398,1,252,'2017-09-21 05:52:16',NULL),(399,1,253,'2017-09-21 05:52:17',NULL),(400,5,254,'2017-09-21 07:33:43',NULL),(401,1,254,'2017-09-21 07:33:47',NULL),(402,5,255,'2017-09-22 09:40:00',NULL),(403,1,255,'2017-09-22 09:40:31',NULL),(404,5,256,'2017-09-25 05:23:25',NULL),(405,1,256,'2017-09-25 05:23:29',NULL),(406,5,257,'2017-09-25 07:48:52',NULL),(407,5,258,'2017-09-25 07:48:52',NULL),(409,1,257,'2017-09-25 07:48:56',NULL),(410,1,258,'2017-09-25 07:48:56',NULL),(412,5,260,'2017-09-25 09:46:34',NULL),(413,1,260,'2017-09-25 09:46:38',NULL),(414,1,261,'2017-09-27 03:52:41',NULL),(415,1,262,'2017-09-27 08:45:50',NULL),(416,1,263,'2017-09-27 08:45:51',NULL),(417,1,264,'2017-09-28 02:05:07',NULL),(418,1,265,'2017-09-28 03:35:40',NULL),(419,4,266,'2017-09-29 07:35:17',NULL),(420,4,267,'2017-09-30 01:59:52',NULL),(421,4,268,'2017-09-30 06:47:53',NULL),(422,4,269,'2017-10-11 03:08:58',NULL),(423,1,270,'2017-10-17 07:59:45',NULL),(424,1,271,'2017-10-17 07:59:46',NULL),(425,1,273,'2017-10-26 02:39:09',NULL),(426,5,273,'2017-10-26 02:39:26',NULL),(427,1,274,'2017-10-26 06:48:10',NULL),(428,5,274,'2017-10-26 06:48:16',NULL),(429,1,275,'2017-10-30 03:22:47',NULL),(430,1,276,'2017-10-30 03:22:49',NULL),(431,1,277,'2017-10-31 09:08:32',NULL),(432,1,278,'2017-10-31 09:08:32',NULL),(433,1,279,'2017-10-31 09:08:33',NULL),(434,1,280,'2017-10-31 09:08:33',NULL),(435,1,281,'2017-10-31 09:08:34',NULL),(436,5,282,'2017-11-01 11:34:55',NULL),(437,5,284,'2017-11-02 08:56:26',NULL),(438,5,285,'2017-11-03 03:53:09',NULL),(439,1,284,'2017-11-03 03:53:16',NULL),(440,1,285,'2017-11-03 03:53:16',NULL),(441,5,286,'2017-11-06 06:52:05',NULL),(442,4,286,'2017-11-06 06:52:34',NULL),(443,1,288,'2017-11-10 02:09:52',NULL),(444,5,288,'2017-11-10 02:10:03',NULL),(445,1,290,'2017-11-16 03:08:28',NULL),(446,1,291,'2017-11-16 03:08:29',NULL),(447,1,294,'2017-11-16 03:08:32',NULL),(448,5,294,'2017-11-16 03:08:51',NULL),(449,5,291,'2017-11-16 03:08:52',NULL),(450,4,294,'2017-11-16 03:09:20',NULL),(451,4,291,'2017-11-16 03:09:21',NULL),(452,4,290,'2017-11-16 03:09:23',NULL),(453,4,295,'2017-11-17 02:10:52',NULL),(454,4,296,'2017-11-17 09:00:04',NULL),(455,4,297,'2017-11-17 09:00:06',NULL),(457,4,299,'2017-11-17 09:00:07',NULL),(458,4,300,'2017-11-20 10:33:17',NULL),(459,4,301,'2017-11-20 10:33:18',NULL),(460,4,302,'2017-11-20 10:33:19',NULL),(461,4,303,'2017-11-21 06:51:22',NULL),(462,1,304,'2017-11-21 07:57:16',NULL),(463,5,304,'2017-11-21 07:57:29',NULL),(464,4,304,'2017-11-21 07:57:36',NULL),(465,1,305,'2017-11-23 10:03:23',NULL),(466,5,305,'2017-11-23 10:03:37',NULL),(467,1,306,'2017-11-23 11:59:52',NULL),(468,4,306,'2017-11-23 12:00:05',NULL),(472,1,309,'2017-12-15 06:28:25',NULL),(473,1,310,'2017-12-15 06:28:26',NULL),(474,1,311,'2017-12-15 06:28:27',NULL),(475,1,312,'2017-12-15 06:34:24',NULL),(476,5,312,'2017-12-15 06:34:33',NULL),(478,1,313,'2017-12-15 07:09:46',NULL),(479,1,314,'2017-12-15 08:06:42',NULL),(480,1,315,'2017-12-15 08:06:43',NULL),(483,1,316,'2017-12-21 06:51:41',NULL),(484,1,317,'2017-12-21 08:05:02',NULL),(485,1,318,'2017-12-21 08:05:03',NULL),(486,1,319,'2017-12-21 08:55:38',NULL),(487,1,320,'2017-12-21 08:55:39',NULL),(488,1,321,'2017-12-21 08:55:39',NULL),(489,1,322,'2017-12-21 12:13:12',NULL),(490,7,322,'2017-12-22 07:28:43',NULL),(491,7,316,'2017-12-26 02:36:27',NULL),(492,1,324,'2017-12-26 12:50:15',NULL),(494,1,325,'2017-12-27 09:35:39',NULL),(495,5,325,'2017-12-27 09:35:48',NULL),(496,1,326,'2017-12-27 12:57:39',NULL),(498,8,326,'2017-12-28 05:55:27',NULL),(499,8,324,'2017-12-28 05:55:45',NULL),(500,8,325,'2017-12-28 05:55:47',NULL),(501,8,319,'2017-12-28 05:56:08',NULL),(503,8,315,'2017-12-28 05:56:14',NULL),(505,8,322,'2017-12-28 05:56:23',NULL),(506,8,314,'2017-12-28 05:56:47',NULL),(509,5,327,'2017-12-29 09:28:06',NULL),(510,5,328,'2018-01-02 01:36:47',NULL),(511,5,329,'2018-01-02 01:36:48',NULL),(512,1,330,'2018-01-02 03:15:35',NULL),(513,1,331,'2018-01-02 03:15:36',NULL),(514,1,332,'2018-01-02 03:15:38',NULL),(515,9,333,'2018-01-03 01:43:12',NULL),(517,9,330,'2018-01-03 01:54:10',NULL),(518,9,309,'2018-01-03 01:54:13',NULL),(519,5,313,'2018-01-03 01:59:55',NULL),(520,5,314,'2018-01-03 01:59:57',NULL),(521,5,315,'2018-01-03 01:59:59',NULL),(522,5,319,'2018-01-03 02:00:01',NULL),(523,9,327,'2018-01-03 02:07:18',NULL),(524,9,325,'2018-01-03 02:07:19',NULL),(525,9,315,'2018-01-03 02:07:36',NULL),(526,9,319,'2018-01-03 02:07:47',NULL),(527,9,322,'2018-01-03 02:07:51',NULL),(528,9,324,'2018-01-03 02:07:52',NULL),(529,9,318,'2018-01-03 02:08:16',NULL),(530,8,321,'2018-01-03 02:24:08',NULL),(531,8,320,'2018-01-03 02:24:09',NULL),(532,8,334,'2018-01-03 02:24:16',NULL),(533,8,332,'2018-01-03 02:24:33',NULL),(534,1,334,'2018-01-03 02:33:05',NULL),(535,1,335,'2018-01-03 02:33:07',NULL),(536,1,333,'2018-01-03 02:33:08',NULL),(537,7,325,'2018-01-03 02:33:51',NULL),(538,7,324,'2018-01-03 02:33:56',NULL),(539,7,321,'2018-01-03 02:33:59',NULL),(540,7,320,'2018-01-03 02:34:00',NULL),(541,7,319,'2018-01-03 02:34:02',NULL),(542,7,335,'2018-01-03 02:34:11',NULL),(543,7,327,'2018-01-03 02:34:16',NULL),(544,7,331,'2018-01-03 02:34:32',NULL),(545,5,337,'2018-01-08 08:44:06',NULL),(546,1,337,'2018-01-08 08:44:25',NULL),(547,1,327,'2018-01-08 08:44:26',NULL),(548,1,338,'2018-01-09 01:43:49',NULL),(549,7,337,'2018-01-09 01:44:07',NULL),(550,7,338,'2018-01-09 01:44:14',NULL),(551,1,339,'2018-01-09 02:34:01',NULL),(552,7,339,'2018-01-09 02:34:09',NULL),(553,1,340,'2018-01-09 02:37:24',NULL),(554,7,340,'2018-01-09 02:37:29',NULL),(555,1,341,'2018-01-09 03:20:33',NULL),(556,1,342,'2018-01-09 03:20:34',NULL),(557,7,341,'2018-01-09 03:20:40',NULL),(558,7,342,'2018-01-09 03:20:40',NULL),(559,1,343,'2018-01-09 03:40:16',NULL),(560,7,343,'2018-01-09 03:40:23',NULL),(561,1,344,'2018-01-09 06:15:35',NULL),(562,1,345,'2018-01-09 06:15:35',NULL),(563,7,344,'2018-01-09 06:15:43',NULL),(564,7,345,'2018-01-09 06:15:44',NULL),(565,1,346,'2018-01-09 06:23:44',NULL),(566,5,346,'2018-01-09 06:23:52',NULL),(567,5,343,'2018-01-09 06:23:55',NULL),(568,1,347,'2018-01-09 07:59:51',NULL),(569,7,347,'2018-01-09 07:59:57',NULL),(570,1,348,'2018-01-09 10:42:01',NULL),(571,7,348,'2018-01-09 10:42:07',NULL),(572,7,317,'2018-01-09 11:04:30',NULL),(573,1,349,'2018-01-17 08:19:30',NULL),(574,1,350,'2018-01-17 08:19:31',NULL),(575,1,268,'2018-01-17 08:19:32',NULL),(576,7,351,'2018-01-25 08:04:05',NULL),(577,1,351,'2018-01-25 10:47:52',NULL),(578,1,352,'2018-02-08 09:36:43',NULL),(579,5,352,'2018-02-08 09:36:58',NULL),(580,1,353,'2018-02-09 08:00:10',NULL),(581,8,316,'2018-03-27 02:26:38',NULL),(582,8,317,'2018-03-27 02:26:39',NULL),(583,8,318,'2018-03-27 02:26:41',NULL),(584,8,338,'2018-03-27 02:26:51',NULL),(585,1,355,'2018-04-10 02:20:06',NULL),(586,1,356,'2018-04-10 02:54:12',NULL),(587,1,357,'2018-04-10 06:21:02',NULL),(588,1,358,'2018-04-10 08:04:07',NULL),(589,1,359,'2018-04-11 07:49:28',NULL),(590,5,359,'2018-04-11 07:49:43',NULL),(591,10,360,'2018-04-18 01:52:32',NULL),(592,1,360,'2018-04-18 01:53:01',NULL),(593,1,361,'2018-04-24 03:23:16',NULL),(594,10,361,'2018-04-24 03:23:28',NULL),(595,5,362,'2018-04-25 08:49:27',NULL),(596,5,130,'2018-04-25 08:49:57',NULL),(597,1,363,'2018-04-26 06:46:13',NULL),(598,5,363,'2018-04-26 06:46:20',NULL),(599,5,364,'2018-05-07 09:23:39',NULL),(600,1,365,'2018-05-09 03:47:39',NULL),(601,6,245,'2018-05-09 09:03:21',NULL),(602,1,366,'2018-05-11 03:45:05',NULL),(603,1,367,'2018-05-11 09:09:27',NULL),(604,5,368,'2018-05-14 10:12:49',NULL),(605,1,369,'2018-05-15 07:16:24',NULL),(606,1,370,'2018-05-17 05:56:54',NULL),(607,5,169,'2018-05-17 09:22:51',NULL),(608,5,369,'2018-05-17 09:23:00',NULL),(609,5,370,'2018-05-17 09:23:01',NULL),(610,1,371,'2018-05-22 06:53:36',NULL),(611,1,372,'2018-05-22 06:53:37',NULL),(612,1,373,'2018-05-22 07:42:27',NULL),(613,1,374,'2018-05-23 02:27:38',NULL),(614,5,375,'2018-05-24 02:13:18',NULL),(615,1,375,'2018-05-24 02:13:30',NULL),(616,1,376,'2018-05-24 07:34:36',NULL),(617,5,376,'2018-05-29 02:14:44',NULL),(618,5,371,'2018-05-29 02:14:49',NULL),(619,5,372,'2018-05-29 02:14:51',NULL),(620,5,374,'2018-05-29 02:14:52',NULL),(621,5,373,'2018-05-29 02:16:15',NULL),(622,1,377,'2018-05-29 06:48:07',NULL),(623,1,378,'2018-05-29 07:34:04',NULL),(624,1,379,'2018-05-29 08:08:30',NULL),(625,5,380,'2018-05-29 08:17:34',NULL),(626,4,380,'2018-05-29 08:17:44',NULL),(627,1,380,'2018-05-29 08:18:00',NULL),(628,1,381,'2018-05-29 10:48:50',NULL),(629,1,382,'2018-05-30 01:13:47',NULL),(630,1,383,'2018-05-30 02:25:51',NULL),(631,1,384,'2018-05-30 06:09:22',NULL),(632,1,385,'2018-05-30 07:33:26',NULL),(633,1,386,'2018-05-30 07:33:26',NULL),(634,1,387,'2018-05-31 06:26:43',NULL),(635,1,388,'2018-05-31 06:26:44',NULL),(636,1,389,'2018-05-31 07:05:27',NULL),(637,1,390,'2018-05-31 12:10:32',NULL),(638,1,391,'2018-06-01 06:36:45',NULL),(639,1,392,'2018-06-01 06:36:46',NULL),(640,1,393,'2018-06-01 06:36:46',NULL),(641,1,394,'2018-06-01 06:36:47',NULL),(642,1,395,'2018-06-01 10:13:14',NULL),(643,1,396,'2018-06-04 01:44:13',NULL),(644,1,397,'2018-06-04 01:44:13',NULL),(645,1,398,'2018-06-04 03:39:21',NULL),(646,1,399,'2018-06-04 03:39:22',NULL),(647,1,400,'2018-06-04 08:20:41',NULL),(648,1,401,'2018-06-05 05:16:51',NULL),(649,1,402,'2018-06-05 05:53:53',NULL),(650,1,403,'2018-06-05 06:17:50',NULL),(651,1,404,'2018-06-05 07:01:35',NULL),(652,1,405,'2018-06-05 08:22:26',NULL),(653,1,406,'2018-06-05 09:03:21',NULL),(654,1,407,'2018-06-05 10:09:40',NULL),(655,1,408,'2018-06-06 07:16:03',NULL),(656,1,409,'2018-06-06 10:06:25',NULL),(657,1,410,'2018-06-08 06:37:14',NULL),(658,1,411,'2018-06-11 01:52:20',NULL),(659,1,412,'2018-06-11 05:48:05',NULL),(660,1,413,'2018-06-11 06:31:51',NULL),(661,1,414,'2018-06-11 08:09:45',NULL),(662,1,415,'2018-06-11 08:09:46',NULL),(663,1,416,'2018-06-11 10:32:03',NULL),(664,1,417,'2018-06-11 11:07:33',NULL),(665,1,152,'2018-06-13 01:58:53',NULL),(666,1,418,'2018-06-13 10:19:11',NULL),(667,1,419,'2018-06-14 02:25:44',NULL),(668,1,420,'2018-06-14 02:52:16',NULL),(669,4,420,'2018-06-14 02:52:26',NULL),(670,5,420,'2018-06-14 02:52:33',NULL),(671,1,421,'2018-06-14 08:56:01',NULL),(672,1,422,'2018-06-14 08:56:02',NULL),(673,1,269,'2018-06-15 03:41:57',NULL),(674,1,423,'2018-06-15 03:41:58',NULL),(675,5,381,'2018-08-09 07:24:56',NULL),(676,5,193,'2018-08-09 07:27:38',NULL),(677,5,387,'2018-08-09 07:27:54',NULL),(678,1,424,'2018-08-09 07:37:03',NULL),(679,1,425,'2018-08-09 08:04:29',NULL),(680,1,426,'2018-08-22 09:09:08',NULL),(681,1,427,'2018-10-09 08:42:18',NULL),(682,1,428,'2018-10-31 01:36:08',NULL),(683,1,429,'2018-10-31 01:36:09',NULL),(684,1,430,'2018-10-31 03:44:48',NULL),(685,1,431,'2018-11-01 01:18:04',NULL),(686,1,432,'2018-11-02 02:47:22',NULL),(687,1,433,'2018-11-02 02:47:23',NULL),(688,1,434,'2018-11-02 08:53:55',NULL),(689,1,435,'2018-11-02 08:53:56',NULL),(690,1,436,'2018-11-02 08:53:56',NULL),(691,1,437,'2018-11-02 08:53:57',NULL),(692,1,438,'2018-11-02 09:02:38',NULL),(693,1,439,'2018-11-05 09:10:29',NULL),(694,1,440,'2018-11-06 09:15:40',NULL),(695,1,441,'2018-11-07 02:39:12',NULL),(696,1,442,'2018-11-07 02:39:13',NULL),(697,1,443,'2018-11-07 02:55:04',NULL),(698,1,444,'2018-11-09 10:07:13',NULL),(699,1,445,'2018-11-12 03:11:57',NULL),(700,1,446,'2018-11-12 03:11:57',NULL),(701,1,447,'2018-11-12 06:27:28',NULL),(702,1,448,'2018-11-15 02:27:50',NULL),(703,1,449,'2018-11-15 02:27:51',NULL),(704,1,450,'2018-11-15 02:27:51',NULL),(705,1,451,'2018-11-15 02:27:52',NULL),(706,1,452,'2018-11-15 02:27:52',NULL),(707,1,453,'2018-11-15 02:37:30',NULL),(708,1,454,'2018-11-15 09:15:34',NULL),(709,1,455,'2018-11-16 03:43:24',NULL),(710,4,455,'2018-11-16 03:43:34',NULL),(711,1,456,'2018-11-19 03:22:46',NULL),(712,4,456,'2018-11-19 03:22:53',NULL),(713,1,457,'2018-11-22 06:17:13',NULL),(714,4,457,'2018-11-22 06:17:17',NULL),(715,1,458,'2018-11-23 08:20:47',NULL),(716,4,458,'2018-11-23 08:20:52',NULL),(717,1,459,'2018-11-23 09:59:25',NULL),(718,4,459,'2018-11-23 09:59:29',NULL),(719,1,460,'2018-11-23 10:51:48',NULL),(720,4,460,'2018-11-23 10:51:52',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_user_login_token`
--

LOCK TABLES `new_auth_user_login_token` WRITE;
/*!40000 ALTER TABLE `new_auth_user_login_token` DISABLE KEYS */;
INSERT INTO `new_auth_user_login_token` VALUES (23,'admin','c526ff02-dc9b-479b-91d8-eaa1191992a7',0,'2018-06-13 08:37:30'),(24,'admin','68ecd2d4-17ca-4e93-8de6-044d22bec6fe',0,'2018-06-13 09:43:24'),(25,'admin','9d730f41-10ef-486b-92a6-859c1e363a58',0,'2018-06-13 09:43:40'),(26,'admin','386f4f6a-1461-4981-9d42-4d914791d32d',0,'2018-06-13 10:45:02'),(27,'admin','ba13cda3-9a70-4a28-9651-ba8aaf2f08d1',0,'2018-06-14 01:34:29'),(28,'admin','bcc23d51-46be-4fa4-9014-1c81a69ff731',0,'2018-06-14 10:24:32'),(29,'admin','8f9ff359-e6e4-4f9c-9c71-00a1d25e2416',0,'2018-06-15 01:35:09'),(30,'admin','126f5924-c0b9-46f0-933d-3d794d88fe7e',0,'2018-06-15 02:56:33'),(31,'admin','83752a69-1645-49c2-a8c8-a16af1e34bf6',0,'2018-06-15 03:10:05'),(32,'admin','bf1dfa5f-f6fe-483d-a84b-77e9f327159d',0,'2018-06-15 05:26:47'),(33,'admin','f2822578-ee5c-43da-bcbf-75b184836285',0,'2018-06-15 06:47:25'),(34,'admin','486f30f1-32ad-4819-be5e-aab55a505f57',0,'2018-06-15 07:54:47'),(35,'admin','22463d46-95ac-4a6c-b91a-595127239b47',0,'2018-06-15 08:58:49'),(36,'admin','a0c316ca-6eb8-44a3-a490-c079356692ba',0,'2018-06-15 08:58:56'),(37,'admin','2f3578e4-9c6c-4654-9e97-737c2549c1d7',0,'2018-06-15 10:33:07'),(38,'admin','1bb77821-c7bf-4294-9452-02833d7df0f8',0,'2018-06-15 10:33:19'),(39,'admin','066407ab-b631-457b-a2be-f152fbed6355',0,'2018-06-19 01:57:00'),(40,'admin','687f9752-d763-4d83-8b6d-37304d9acebc',0,'2018-06-19 02:07:03'),(41,'admin','661a10b6-6703-4a14-a41b-356f3eddc622',0,'2018-06-21 09:36:17'),(42,'admin','3966b146-f4aa-46f6-9821-eea12ba10f88',0,'2018-07-19 01:13:48'),(43,'admin','2bb49714-f648-4712-85af-46707002682c',0,'2018-07-26 01:24:34'),(44,'admin','236aa853-a28c-49d8-afcc-374606e4c0a1',0,'2018-07-26 01:48:47'),(45,'admin','e6758382-dc0d-4927-9612-3461eaa0341e',0,'2018-07-26 01:57:32'),(46,'admin','39f0e3ef-0da8-4d99-a740-fd87a6f56abe',0,'2018-07-26 02:00:01'),(47,'admin','cd5311f1-9dc5-49e8-a032-1f4a8bbb4c21',0,'2018-07-26 02:04:27'),(48,'admin','1fd57a75-9a87-4da2-9bc6-8c675ebe979b',0,'2018-07-26 02:08:30'),(49,'admin','6f765b14-bf93-4b65-ac85-14aeb2b8a318',0,'2018-07-26 02:13:14'),(50,'admin','6eced5cb-65cc-4ebe-b82d-e8b430ef48e2',0,'2018-07-26 02:29:34'),(51,'admin','cf5d8102-82dd-4204-bf62-dc0e28b1aa88',0,'2018-07-26 02:37:07'),(52,'admin','f4339f2f-25b0-43b4-ab14-82607dbfadfa',0,'2018-07-27 03:00:59'),(53,'admin','1ee4da3f-fabe-4ff7-8ed3-1b22da391dfb',0,'2018-08-02 07:59:16'),(54,'admin','079f2590-5278-44c3-8dfd-134c2ec1c045',0,'2018-08-07 03:33:02'),(55,'admin','cba9e76f-6a68-4ee8-ad72-ea66a85e52a3',0,'2018-08-07 07:25:50'),(56,'admin','d5b7480c-4f9b-4fb8-bb15-4fbe10049d6f',0,'2018-08-08 01:44:20'),(57,'admin','2eef8e59-e568-4c2c-a03a-06375d836600',0,'2018-08-09 07:21:29'),(58,'opscloud','b9fcb413-67bf-4b3f-8310-938e5f10b624',0,'2018-08-09 07:22:35'),(59,'opscloud','b8870bfb-df43-4f8d-9cc3-13eba8dc3873',0,'2018-08-09 07:22:48'),(60,'admin','8a65e3de-51f7-4ae0-bfb3-7f101fa6d0e6',0,'2018-08-09 07:24:31'),(61,'opscloud','29e6f005-f968-4514-97bd-ab0f6edd6f38',0,'2018-08-09 07:25:19'),(62,'opscloud','d2bc4f07-77b8-41b2-b720-e5f11a9e61dd',0,'2018-08-09 07:25:33'),(63,'admin','57ad54e3-7142-4618-a4cf-f5a1fedee033',0,'2018-08-09 07:27:11'),(64,'opscloud','0aef8fc0-6d50-4d3e-9bc5-e6204773bad1',0,'2018-08-09 07:28:22'),(65,'admin','2753575c-4306-45b1-a52f-ef48286d53b8',0,'2018-08-09 07:33:56'),(66,'admin','77965504-e49e-4797-91bf-327e57adca7a',0,'2018-08-09 07:37:53'),(67,'admin','264952c0-8084-4b88-87e0-f46b2738f808',0,'2018-08-09 07:41:28'),(68,'admin','5e806858-6b89-4906-a89c-2ffa692c0a68',0,'2018-08-09 07:43:18'),(69,'admin','3cfe5194-8801-442e-9468-cb6e7b31739f',0,'2018-08-09 07:48:01'),(70,'opscloud','fe6bfbb1-04ce-49fc-ba0b-72ad65b0f09e',0,'2018-08-09 07:49:56'),(71,'admin','5f637ac3-152a-4276-8912-785add4659b0',0,'2018-08-09 07:51:11'),(72,'opscloud','4b1233c2-585c-4af5-8ce1-50ef02806654',1,'2018-08-09 08:06:24'),(73,'admin','57419962-caa6-4b15-ae47-cfcdc99b0c93',0,'2018-08-15 09:18:45'),(74,'admin','0b4a04ee-8bef-4ff7-b1ee-77cb752c6e1d',0,'2018-08-22 08:53:10'),(75,'admin','76bdd1ed-e676-4114-b9cf-ff9a15961238',0,'2018-08-22 09:31:08'),(76,'admin','cdc34c25-acff-4f65-8a26-645ccf473fa1',0,'2018-08-22 09:35:00'),(77,'admin','f5c27260-a406-45dc-a099-1f40f2b95970',0,'2018-08-28 09:03:45'),(78,'admin','20381399-4154-4571-8bc6-2020df11b990',0,'2018-09-05 09:26:27'),(79,'admin','33e56f0c-96c0-46a6-b083-38d24399366e',0,'2018-09-06 06:43:12'),(80,'admin','0ae4bfb1-5723-4e21-8221-daad8b315766',0,'2018-09-13 07:20:50'),(81,'admin','0dec7fd9-dbd9-4ce7-b24e-06ceeff1efca',0,'2018-10-09 08:41:03'),(82,'admin','f94aa001-fda1-4c0c-90df-fd220bdf80cd',0,'2018-10-09 09:11:42'),(83,'admin','20745578-fe3b-4bfb-9fce-eca3ce6d5039',0,'2018-10-22 03:26:28'),(84,'admin','c55ea4a4-7fad-42d5-839c-51591d5c5947',0,'2018-10-23 09:30:30'),(85,'admin','247802fb-c87e-4fbd-bb0e-8394c536abca',0,'2018-10-29 02:16:45'),(86,'admin','3622e966-fdfa-45ba-ae5e-e1551c111373',0,'2018-10-30 07:43:20'),(87,'admin','d1eb3246-8630-416e-8da4-8afe25458447',0,'2018-10-30 08:52:50'),(88,'admin','88c8e8ab-ce79-4ded-8f50-68769caee441',0,'2018-11-05 09:09:28'),(89,'admin','db10d17d-a922-441f-8bea-eaa915f3e755',0,'2018-11-05 09:10:18'),(90,'admin','6aa0d285-0c3e-4c10-9c12-71621eabd00b',0,'2018-11-15 02:37:51'),(91,'admin','2dfc9e22-6294-4320-8959-f9d6154f2b15',1,'2018-11-16 05:43:34');
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
) ENGINE=InnoDB AUTO_INCREMENT=4092 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_auth_user_role`
--

LOCK TABLES `new_auth_user_role` WRITE;
/*!40000 ALTER TABLE `new_auth_user_role` DISABLE KEYS */;
INSERT INTO `new_auth_user_role` VALUES (33,'liangjian',4,'2016-11-01 02:27:30',NULL),(56,'liangjian',5,'2016-11-17 07:03:53',NULL),(4013,'admin',1,'2017-05-11 05:58:20',NULL),(4014,'admin',6,'2017-05-11 05:58:20',NULL),(4026,'admin',4,'2018-06-08 01:05:50',NULL),(4081,'opscloud',4,'2018-08-09 07:22:28',NULL),(4082,'opscloud',5,'2018-08-09 07:22:30',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=1589 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_ip_detail`
--

LOCK TABLES `new_ip_detail` WRITE;
/*!40000 ALTER TABLE `new_ip_detail` DISABLE KEYS */;
INSERT INTO `new_ip_detail` VALUES (1582,9,0,'172.17.6.215',1,NULL,'2018-06-15 02:39:04','2018-06-15 02:56:45',0),(1583,9,0,'172.17.0.235',1,NULL,'2018-06-15 02:40:21','2018-06-15 02:56:41',0),(1584,10,8,'121.196.198.129',0,NULL,'2018-07-16 07:44:05','2018-07-16 07:44:05',0),(1585,9,8,'172.17.0.236',1,NULL,'2018-07-16 07:44:05','2018-07-16 07:44:05',0),(1586,10,9,'121.43.229.173',0,NULL,'2018-07-18 02:51:25','2018-07-18 02:51:25',0),(1587,9,9,'10.251.238.209',1,NULL,'2018-07-18 02:51:25','2018-07-18 02:51:25',0),(1588,11,10,'10.17.1.152',1,NULL,'2018-08-07 08:13:01','2018-08-07 08:13:01',0);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=695 DEFAULT CHARSET=utf8 COMMENT='office vm 数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `new_vm_server`
--

LOCK TABLES `new_vm_server` WRITE;
/*!40000 ALTER TABLE `new_vm_server` DISABLE KEYS */;
INSERT INTO `new_vm_server` VALUES (455,'',NULL,'outhttp-daily','10.17.0.112',NULL,2,2048,0,0,'2018-06-15 11:56:52',NULL),(456,'',NULL,'node5.java.ci.51xianqu.net','10.17.0.128',NULL,4,4096,0,0,'2018-06-15 11:56:52',NULL),(457,'',NULL,'node3.java.ci.51xianqu.net','10.17.0.126',NULL,4,4096,0,0,'2018-06-15 11:56:52',NULL),(458,'',NULL,'node1.java.ci.51xianqu.net','10.17.1.194',NULL,4,4096,0,0,'2018-06-15 11:56:52',NULL),(459,'',NULL,'finereport','10.17.1.38',NULL,4,8192,0,0,'2018-06-15 11:56:52','2018-08-08 09:22:41'),(460,'',NULL,'u8','10.17.1.97',NULL,4,8192,0,0,'2018-06-15 11:56:52',NULL),(461,'',NULL,'Elasticsearch','10.17.1.115',NULL,2,2048,0,0,'2018-06-15 11:56:52',NULL),(462,'',NULL,'dw-daily','10.17.1.174',NULL,2,2048,0,0,'2018-06-15 11:56:52',NULL),(463,'',NULL,'iris-magic-daily','10.17.1.172',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(464,'',NULL,'reminder-daily','10.17.1.171',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(465,'',NULL,'sdb-daily','10.17.1.168',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(466,'',NULL,'logistics-daily','10.17.1.47',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(467,'',NULL,'rate-daily','10.17.1.164',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(468,'',NULL,'lr.51xianqu.net','10.17.1.106',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(469,'',NULL,'timeoutcenter-daily','10.17.1.151',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(470,'',NULL,'payservice-wxpay-daily','10.17.1.149',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(471,'',NULL,'payservice-alipay-daily','10.17.1.148',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(472,'',NULL,'crm-daily','10.17.1.146',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(473,'',NULL,'agile.development.mysql.51xianqu.net-slave','10.17.1.200',NULL,8,16384,0,0,'2018-06-15 11:56:53',NULL),(474,'',NULL,'itemcenter-daily','10.17.1.46',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(475,'',NULL,'node2.trade.ci.51xianqu.net','10.17.1.103',NULL,4,2048,0,0,'2018-06-15 11:56:53',NULL),(476,'',NULL,'jobcenter-daily','10.17.1.90',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(477,'',NULL,'member-daily','10.17.1.41',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(478,'',NULL,'node2.ci.51xianqu.net','10.17.1.70',NULL,4,4096,0,0,'2018-06-15 11:56:53',NULL),(479,'','getway','opscloud','10.17.1.152',NULL,2,4096,10,1,'2018-06-15 11:56:53','2018-11-26 08:52:29'),(480,'',NULL,'store-proxy-daily','10.17.1.54',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(481,'',NULL,'recommend-daily','10.17.1.220',NULL,1,2048,0,0,'2018-06-15 11:56:53',NULL),(482,'',NULL,'ldap2.51xianqu.net','10.17.1.108',NULL,1,1024,0,0,'2018-06-15 11:56:53',NULL),(483,'',NULL,'lpm-daily','10.17.1.225',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(484,'',NULL,'iris-api-daily','10.17.1.166',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(485,'',NULL,'pay-daily','10.17.1.147',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(486,'',NULL,'stash.51xianqu.net','10.17.1.11',NULL,8,12288,0,0,'2018-06-15 11:56:53',NULL),(487,'',NULL,'procurement-daily','10.17.1.163',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(488,'',NULL,'nexus.51xianqu.net','10.17.1.17',NULL,8,8192,0,0,'2018-06-15 11:56:53',NULL),(489,'',NULL,'task.51xianqu.net','10.17.1.75',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(490,'',NULL,'market-daily','10.17.1.50',NULL,2,3072,0,0,'2018-06-15 11:56:53',NULL),(491,'',NULL,'crowd.51xianqu.net','10.17.1.237',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(492,'',NULL,'sdw-daily','10.17.1.133',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(493,'',NULL,'jira.51xianqu.net','10.17.1.13',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(494,'',NULL,'wiki.51xianqu.net','10.17.1.12',NULL,4,16384,0,0,'2018-06-15 11:56:53',NULL),(495,'',NULL,'node5.ci.51xianqu.net','10.17.1.222',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(496,'',NULL,'lo-poi','10.17.0.65',NULL,4,4096,0,0,'2018-06-15 11:56:53',NULL),(497,'',NULL,'lo-market','10.17.0.64',NULL,4,4096,0,0,'2018-06-15 11:56:53',NULL),(498,'',NULL,'gc-lo-procurement','10.17.0.62',NULL,4,4096,0,0,'2018-06-15 11:56:53','2018-06-28 09:22:26'),(499,'',NULL,'lo-centralbank','10.17.0.61',NULL,4,4096,0,0,'2018-06-15 11:56:53',NULL),(500,'',NULL,'cmb','10.17.1.210',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(501,'',NULL,'web-daily','10.17.1.64',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(502,'',NULL,'sonar.51xianqu.net','10.17.1.109',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(503,'',NULL,'logistics-job','10.17.1.53',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(504,'',NULL,'cdl_api_test1','10.17.1.141',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(505,'',NULL,'kylin-scheduler','10.17.1.193',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(506,'',NULL,'qacenter.51xianqu.net','10.17.1.111',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(507,'',NULL,'daily.ssh.51xianqu.net','10.17.1.33',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(508,'',NULL,'redis-lbs-master-daily','10.17.1.67',NULL,2,16384,0,0,'2018-06-15 11:56:53',NULL),(509,'',NULL,'itemcenterqc-daily','10.17.1.227',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(510,'',NULL,'hue','10.17.0.88',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(511,'',NULL,'outway-daily','10.17.1.190',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(512,'',NULL,'api-daily','10.17.1.43',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(513,'',NULL,'dnsmasq','10.17.1.114',NULL,2,1024,0,0,'2018-06-15 11:56:53',NULL),(514,'',NULL,'itdb.51xianqu.net','10.17.1.231',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(515,'',NULL,'node1.trade.ci.51xianqu.net','10.17.1.99',NULL,4,2048,0,0,'2018-06-15 11:56:53',NULL),(516,'',NULL,'tmq-admin-daily','10.17.1.58',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(517,'',NULL,'mail.52shangou.com','10.17.1.78',NULL,8,8192,0,0,'2018-06-15 11:56:53',NULL),(518,'',NULL,'shorturl-daily','10.17.1.213',NULL,1,2048,0,0,'2018-06-15 11:56:53',NULL),(519,'',NULL,'marketqc-daily','10.17.1.130',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(520,'',NULL,'spider','10.17.1.123',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(521,'',NULL,'sonar1.ci.51xianqu.net','10.17.1.118',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(522,'',NULL,'node2.java.ci.51xianqu.net','10.17.0.125',NULL,4,4096,0,0,'2018-06-15 11:56:53',NULL),(523,'',NULL,'videoServer','10.17.1.98',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(524,'',NULL,'Hbase-02','10.17.1.159',NULL,8,4096,0,0,'2018-06-15 11:56:53',NULL),(525,'',NULL,'ns2.51xianqu.net','10.17.1.102',NULL,1,1024,0,0,'2018-06-15 11:56:53',NULL),(526,'',NULL,'landmarkqc-daily','10.17.1.212',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(527,'',NULL,'wmsOrderCenter-daily','10.17.1.183',NULL,1,2048,0,0,'2018-06-15 11:56:53',NULL),(528,'',NULL,'test.ci.51xianqu.net','10.17.1.105',NULL,8,8192,0,0,'2018-06-15 11:56:53',NULL),(529,'',NULL,'tradeoutway-daily','10.17.1.167',NULL,2,2048,0,0,'2018-06-15 11:56:53',NULL),(530,'',NULL,'memberqc-daily','10.17.1.121',NULL,2,4096,0,0,'2018-06-15 11:56:53',NULL),(531,'',NULL,'zsearch-controlcenter-daily','10.17.1.208',NULL,4,4096,0,0,'2018-06-15 11:56:53',NULL),(532,'',NULL,'lo-rate','10.17.0.58',NULL,4,4096,0,0,'2018-06-15 11:56:53',NULL),(533,'',NULL,'gc-lo-hongbao','10.17.0.56',NULL,4,4096,0,0,'2018-06-15 11:56:53','2018-06-28 09:22:24'),(534,'',NULL,'zsearch-solr-engine-daily-2','10.17.1.204',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(535,'',NULL,'zsearch-solr-engine-daily-1','10.17.1.203',NULL,4,8192,0,0,'2018-06-15 11:56:53',NULL),(536,'',NULL,'es-data-daily','10.17.0.141',NULL,2,4096,0,0,'2018-06-15 11:56:54','2018-11-07 08:57:04'),(537,'',NULL,'lo-trade','10.17.0.55',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(538,'',NULL,'CentOS7','10.17.1.23',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(539,'',NULL,'lo-member','10.17.0.54',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(540,'',NULL,'trade-daily','10.17.1.48',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(541,'',NULL,'trade.ci.51xianqu.net','10.17.1.76',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(542,'',NULL,'kingdee-2','10.17.1.196',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(543,'',NULL,'mysql-daily2-master','10.17.1.21',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(544,'',NULL,'osx.10.11.vm.template','10.17.1.201',NULL,8,16384,0,0,'2018-06-15 11:56:54',NULL),(545,'',NULL,'lo-area','10.17.0.71',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(546,'',NULL,'jcenter.bintray.com','10.17.1.66',NULL,1,1024,0,0,'2018-06-15 11:56:54',NULL),(547,'',NULL,'ftp.51xianqu.net','10.17.1.4',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(548,'',NULL,'crm-nodejs-daily','10.17.1.197',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(549,'',NULL,'node2.osx','10.17.1.39',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(550,'',NULL,'pulse-daily','10.17.1.191',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(551,'',NULL,'backup.51xianqu.net','10.17.1.223',NULL,6,8192,0,0,'2018-06-15 11:56:54',NULL),(552,'',NULL,'windows2008R2.vm.template','10.17.1.28',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(553,'',NULL,'node4.java.ci.51xianqu.net','10.17.0.127',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(554,'',NULL,'centos6.5.vm.template','10.17.1.29',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(555,'',NULL,'hui.51xianqu.net','10.17.1.31',NULL,1,1024,0,0,'2018-06-15 11:56:54',NULL),(556,'',NULL,'bigmouth-daily','10.17.1.219',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(557,'',NULL,'redis-daily-2','10.17.1.60',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(558,'',NULL,'zsearch-solr-engine-daily-3','10.17.1.176',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(559,'',NULL,'bi-kettle','10.17.1.209',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(560,'',NULL,'dubbo-admin-daily','10.17.1.127',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(561,'',NULL,'redis-daily','10.17.1.61',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(562,'',NULL,'fms-daily','10.17.1.217',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(563,'',NULL,'kabill-daily','10.17.1.89',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(564,'',NULL,'lion.51xianqu.com','10.17.1.116',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(565,'',NULL,'hadoop-mysql','10.17.1.107',NULL,4,16384,0,0,'2018-06-15 11:56:54',NULL),(566,'',NULL,'mail.51xianqu.net','10.17.1.77',NULL,8,16384,0,0,'2018-06-15 11:56:54',NULL),(567,'',NULL,'lo-shop','10.17.0.53',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(568,'',NULL,'jenkins.51xianqu.net','10.17.1.74',NULL,8,10240,0,0,'2018-06-15 11:56:54',NULL),(569,'',NULL,'agile.development.mysql.51xianqu.net','10.17.1.20',NULL,8,8192,0,0,'2018-06-15 11:56:54',NULL),(570,'',NULL,'windows2008-WMS','10.17.1.59',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(571,'',NULL,'msgcenter-daily','10.17.1.52',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(572,'',NULL,'rap.51xianqu.net','10.17.1.15',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(573,'',NULL,'wrcenter-daily','10.17.1.224',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(574,'',NULL,'pmsystem','10.17.0.140',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(575,'',NULL,'baohe-daily','10.17.1.81',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(576,'',NULL,'lo-hitemcenter','10.17.0.70',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(577,'',NULL,'phabricator','10.17.0.101',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(578,'',NULL,'dogdock','10.17.1.34',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(579,'',NULL,'landmarkback-daily','10.17.1.207',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(580,'',NULL,'app.ci.51xianqu.net','10.17.1.242',NULL,8,10240,0,0,'2018-06-15 11:56:54',NULL),(581,'',NULL,'liangjian-test2','10.17.1.240',NULL,10,8192,0,0,'2018-06-15 11:56:54',NULL),(582,'',NULL,'tradedatacenter-daily','10.17.1.138',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(583,'',NULL,'ods-daily','10.17.0.110',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(584,'',NULL,'ib-daily','10.17.0.123',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(585,'',NULL,'itemcore-daily','10.17.0.107',NULL,4,8192,0,0,'2018-06-15 11:56:54','2018-11-22 09:11:59'),(586,'',NULL,'stockcenter-daily','10.17.1.178',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(587,'',NULL,'node3.osx','10.17.1.40',NULL,8,16384,0,0,'2018-06-15 11:56:54',NULL),(588,'',NULL,'node1.ft.ci.51xianqu.net','10.17.0.139',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(589,'',NULL,'zookeeper-daily','10.17.1.62',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(590,'',NULL,'delivery-daily','10.17.1.45',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(591,'',NULL,'lo-pick','10.17.1.246',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(592,'',NULL,'config-center-daily','10.17.1.51',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(593,'',NULL,'hadoop-gateway1','10.17.1.91',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(594,'',NULL,'basic-service-daily','10.17.0.12',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(595,'',NULL,'warninglog-daily','10.17.1.119',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(596,'',NULL,'panama-daily','10.17.1.36',NULL,1,4096,0,0,'2018-06-15 11:56:54',NULL),(597,'',NULL,'osx-01','10.17.1.157',NULL,8,16384,0,0,'2018-06-15 11:56:54',NULL),(598,'',NULL,'centralbank-daily','10.17.1.88',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(599,'',NULL,'pushCheck-daily','10.17.1.35',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(600,'',NULL,'dnsmasq','10.17.1.113',NULL,2,1024,0,0,'2018-06-15 11:56:54',NULL),(601,'',NULL,'landmark-daily','10.17.1.25',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(602,'',NULL,'lo-groupon','10.17.1.125',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(603,'',NULL,'node3.ci.51xianqu.net','10.17.1.72',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(604,'',NULL,'jiraserver','10.17.1.27',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(605,'',NULL,'react-native-daily','10.17.1.248',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(606,'',NULL,'lo-outway','10.17.0.57',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(607,'',NULL,'tradeback-daily','10.17.1.244',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(608,'',NULL,'zabbix3.51xianqu.net','10.17.1.199',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(609,'',NULL,'jci','10.17.1.245',NULL,16,8192,0,0,'2018-06-15 11:56:54',NULL),(610,'',NULL,'ctss-daily','10.17.0.109',NULL,1,2048,0,0,'2018-06-15 11:56:54',NULL),(611,'',NULL,'outapi-daily','10.17.1.238',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(612,'',NULL,'swh.51xianqu.net','10.17.1.16',NULL,1,1024,0,0,'2018-06-15 11:56:54',NULL),(613,'',NULL,'lo-stat_config','10.17.1.234',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(614,'',NULL,'node2.macos.ci.51xianqu.net','10.17.1.68',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(615,'',NULL,'search-service-daily','10.17.1.214',NULL,4,8192,0,0,'2018-06-15 11:56:54',NULL),(616,'',NULL,'callCenter-daily','10.17.1.221',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(617,'',NULL,'hadoop_peeper','10.17.1.205',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(618,'',NULL,'lo-kamember','10.17.1.247',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(619,'',NULL,'lo-org','10.17.1.241',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(620,'',NULL,'ci.51xianqu.net','10.17.1.10',NULL,4,10240,0,0,'2018-06-15 11:56:54',NULL),(621,'',NULL,'kamember-daily','10.17.1.24',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(622,'',NULL,'picking-daily','10.17.1.232',NULL,2,4096,0,0,'2018-06-15 11:56:54',NULL),(623,'',NULL,'shopback-daily','10.17.1.131',NULL,2,2048,0,0,'2018-06-15 11:56:54',NULL),(624,'',NULL,'lo-itemcenter','10.17.0.66',NULL,4,4096,0,0,'2018-06-15 11:56:54',NULL),(625,'',NULL,'acc-daily','10.17.1.229',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(626,'',NULL,'zabbix.mysql.51xianqu.net','10.17.1.122',NULL,8,16384,0,0,'2018-06-15 11:56:55',NULL),(627,'',NULL,'mysql-daily','10.17.1.63',NULL,8,16384,0,0,'2018-06-15 11:56:55',NULL),(628,'',NULL,'version-daily','10.17.1.215',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(629,'',NULL,'tmq-daily','10.17.1.42',NULL,4,3072,0,0,'2018-06-15 11:56:55',NULL),(630,'',NULL,'DHCP','10.17.1.96',NULL,2,4096,0,0,'2018-06-15 11:56:55',NULL),(631,'',NULL,'sdop-daily','10.17.1.216',NULL,2,4096,0,0,'2018-06-15 11:56:55',NULL),(632,'',NULL,'ka-agent-daily','10.17.1.69',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(633,'',NULL,'kaweb-daily','10.17.1.239',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(634,'',NULL,'dataapi-daily','10.17.1.44',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(635,'',NULL,'appmonitor','10.17.1.235',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(636,'',NULL,'es-gateway','10.17.1.128',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(637,'',NULL,'lo-logistics','10.17.0.67',NULL,4,4096,0,0,'2018-06-15 11:56:55',NULL),(638,'',NULL,'shopqc-daily','10.17.1.211',NULL,2,4096,0,0,'2018-06-15 11:56:55',NULL),(639,'',NULL,'mail.51xianqu.net-slave','10.17.1.230',NULL,8,16384,0,0,'2018-06-15 11:56:55',NULL),(640,'',NULL,'kafka-manager','10.17.0.81',NULL,4,10240,0,0,'2018-06-15 11:56:55',NULL),(641,'',NULL,'gopay-daily','10.17.1.228',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(642,'',NULL,'workorder-daily','10.17.1.95',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(643,'',NULL,'iop-daily','10.17.1.198',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(644,'',NULL,'es-grafana','10.17.1.129',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(645,'',NULL,'kamemberqc-daily','10.17.1.253',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(646,'',NULL,'liangjian-test','10.17.1.252',NULL,10,8192,0,0,'2018-06-15 11:56:55',NULL),(647,'',NULL,'qacenter.51xianqu.net','10.17.1.110',NULL,2,4096,0,0,'2018-06-15 11:56:55',NULL),(648,'',NULL,'dataspout-daily','10.17.1.26',NULL,2,2048,0,0,'2018-06-15 11:56:55',NULL),(649,'',NULL,'lo-tracker','10.17.0.51',NULL,4,4096,0,0,'2018-06-15 11:56:55',NULL),(650,'',NULL,'o2oserver-daily','10.17.1.86',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(651,'',NULL,'taobaoitemgrab','10.17.1.243',NULL,6,8192,0,0,'2018-06-15 11:56:55',NULL),(652,'',NULL,'node1.ci.51xianqu.net','10.17.1.71',NULL,4,4096,0,0,'2018-06-15 11:56:55',NULL),(653,'',NULL,'node1.macos.ci.51xianqu.net','10.17.0.135',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(654,'',NULL,'tableau-server','10.17.1.112',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(655,'',NULL,'logistics-backstage-daily','10.17.1.84',NULL,2,2048,0,0,'2018-06-15 11:56:55',NULL),(656,'',NULL,'Hbase-01','10.17.1.158',NULL,8,4096,0,0,'2018-06-15 11:56:55',NULL),(657,'',NULL,'design.wiki.51xianqu.net','10.17.1.124',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(658,'',NULL,'npm-server','10.17.1.233',NULL,2,4096,0,0,'2018-06-15 11:56:55',NULL),(659,'',NULL,'shopcart-daily','10.17.1.218',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(660,'',NULL,'item-sync-daily','10.17.1.37',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(661,'',NULL,'lo-delivery','10.17.0.69',NULL,4,4096,0,0,'2018-06-15 11:56:55',NULL),(662,'',NULL,'zsearch-realtime-daily','10.17.1.136',NULL,2,4096,0,0,'2018-06-15 11:56:55',NULL),(663,'',NULL,'mysql-slave-daily','10.17.1.65',NULL,8,16384,0,0,'2018-06-15 11:56:55',NULL),(664,'',NULL,'auc-daily','10.17.1.57',NULL,2,2048,0,0,'2018-06-15 11:56:55',NULL),(665,'',NULL,'mysql-daily2-slave','10.17.1.22',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(666,'',NULL,'ns1.51xianqu.net','10.17.1.101',NULL,1,1024,0,0,'2018-06-15 11:56:55',NULL),(667,'',NULL,'wms-daily','10.17.1.182',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(668,'',NULL,'lo-item','10.17.0.52',NULL,4,4096,0,0,'2018-06-15 11:56:55',NULL),(669,'',NULL,'gc-workflow','10.17.1.249',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(670,'',NULL,'ka-groupon-daily','10.17.0.108',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(671,'',NULL,'zsearch-console-daily','10.17.1.206',NULL,2,2048,0,0,'2018-06-15 11:56:55',NULL),(672,'',NULL,'leanote.51xianqu.net','10.17.1.226',NULL,2,4096,0,0,'2018-06-15 11:56:55',NULL),(673,'',NULL,'cmdb.51xianqu.net','10.17.1.120',NULL,2,4096,0,0,'2018-06-15 11:56:55',NULL),(674,'',NULL,'org-daily','10.17.1.80',NULL,2,2048,0,0,'2018-06-15 11:56:55',NULL),(675,'',NULL,'lo-history','10.17.0.59',NULL,4,4096,0,0,'2018-06-15 11:56:55',NULL),(676,'',NULL,'node4.ci.51xianqu.net','10.17.1.104',NULL,4,8192,0,0,'2018-06-15 11:56:55',NULL),(677,'',NULL,'mockserver-daily','10.17.1.137',NULL,1,2048,0,0,'2018-06-15 11:56:55',NULL),(678,'VMware vCenter Server Appliance',NULL,'vcsa','10.17.1.30',NULL,4,16384,0,0,'2018-06-15 11:56:55',NULL),(679,'',NULL,'shop-daily','10.17.1.49',NULL,2,2048,0,0,'2018-06-15 11:56:55',NULL),(680,'',NULL,'node6.ci.51xianqu.net','10.17.1.236',NULL,8,8192,0,0,'2018-06-15 11:56:55',NULL),(681,'',NULL,'node2.ft.ci.51xianqu.net','10.17.1.153',NULL,4,4096,0,0,'2018-06-15 11:56:55',NULL),(682,'',NULL,'lk_ds2','10.17.0.217',NULL,2,6144,0,0,'2018-06-28 09:22:22',NULL),(683,'',NULL,'node01.k8s.51xianqu.net','10.17.0.145',NULL,8,32768,0,0,'2018-06-28 09:22:22',NULL),(684,'',NULL,'node02.k8s.51xianqu.net','10.17.0.146',NULL,2,4096,0,0,'2018-06-28 09:22:22',NULL),(685,'',NULL,'lk_ds3','10.17.0.218',NULL,2,6144,0,0,'2018-06-28 09:22:24',NULL),(686,'',NULL,'lk_ds1','10.17.0.216',NULL,2,6144,0,0,'2018-06-28 09:22:24',NULL),(687,'',NULL,'master01.k8s.51xianqu.net','10.17.0.142',NULL,4,4096,0,0,'2018-06-28 09:22:25',NULL),(688,'',NULL,'node1.docker.ci.51xianqu.net','10.17.0.138',NULL,2,4096,0,0,'2018-08-16 09:38:29',NULL),(689,'',NULL,'gitlab','10.17.0.15',NULL,4,8192,0,0,'2018-10-25 09:01:53',NULL),(690,'',NULL,'swms-daily','10.17.0.167',NULL,4,4096,0,0,'2018-10-31 09:17:05',NULL),(691,'',NULL,'mycat-daily','10.17.0.144',NULL,2,4096,0,0,'2018-11-07 08:57:03',NULL),(692,'',NULL,'mycat-daily','10.17.0.143',NULL,2,4096,0,0,'2018-11-07 08:57:03',NULL),(693,'',NULL,'lianhua-daily','10.17.0.136',NULL,2,4096,0,0,'2018-11-13 16:08:30',NULL),(694,'',NULL,'sentinel-daily','10.17.0.137',NULL,2,4096,0,0,'2018-11-16 09:04:02',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_ecs_image`
--

LOCK TABLES `oc_aliyun_ecs_image` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_ecs_image` DISABLE KEYS */;
INSERT INTO `oc_aliyun_ecs_image` VALUES (6,'m-bp1iwsh6g2n03w647v0c','database-template-latest',1,'1.0.0','2018-06-15 01:53:06',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_vpc`
--

LOCK TABLES `oc_aliyun_vpc` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_vpc` DISABLE KEYS */;
INSERT INTO `oc_aliyun_vpc` VALUES (1,1,'vpc-bp10aw2gcg7dgendfrd0e','闪电购在线业务','2018-06-15 01:53:18',NULL),(2,1,'vpc-j6c4oagwip2ezhw5ndsow','vpc1','2018-06-15 01:53:18',NULL),(3,1,'vpc-rj9ao9gb3l98hsxekvmp8','System created default VPC.','2018-06-15 01:53:19',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_vpc_security_group`
--

LOCK TABLES `oc_aliyun_vpc_security_group` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_vpc_security_group` DISABLE KEYS */;
INSERT INTO `oc_aliyun_vpc_security_group` VALUES (8,1,'sg-bp10b7hu2nteax6sok6z','dw-vpc','2018-07-16 07:40:17',NULL),(9,1,'sg-bp1ic3xv7mnys7917w8j','alicloud-cs-auto-created-security-group-c44928962a0d042379e828859b53f9935','2018-07-16 07:40:17',NULL),(10,1,'sg-bp1dhpzbyzvds9g7pkm8','sg-bp1dhpzbyzvds9g7pkm8','2018-07-16 07:40:17',NULL),(11,1,'sg-bp143u00uhudm7qby0wx','sg-2','2018-07-16 07:40:17',NULL),(12,1,'sg-bp1cmusmox73lkens2h6','sg-1','2018-07-16 07:40:17',NULL),(13,2,'sg-j6c3fifg8nsobo1vm9oc','sg1','2018-07-16 07:40:18',NULL),(14,3,'sg-rj91yedwlrraw53jlfw9','sg-vpc','2018-07-16 07:40:18',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_aliyun_vswitch`
--

LOCK TABLES `oc_aliyun_vswitch` WRITE;
/*!40000 ALTER TABLE `oc_aliyun_vswitch` DISABLE KEYS */;
INSERT INTO `oc_aliyun_vswitch` VALUES (7,1,'vsw-bp1qc5s4wgaj1o5jhf4n2','dw','2018-07-16 07:40:17',NULL),(8,1,'vsw-bp1njv0ja47b563pnj4yw','sw-3','2018-07-16 07:40:17',NULL),(9,1,'vsw-bp1o05adi15b6deib97vh','sw-2','2018-07-16 07:40:17',NULL),(10,1,'vsw-bp16ckbhe1qh2iqa353jz','sw-kubernetes','2018-07-16 07:40:17',NULL),(11,2,'vsw-j6cdtku3wl7hffy4obzbu','sw1','2018-07-16 07:40:18',NULL),(12,3,'vsw-rj9ildomdlvsy43r9l1nz','System created default virtual switch.','2018-07-16 07:40:18',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_ansible_tasks`
--

LOCK TABLES `oc_ansible_tasks` WRITE;
/*!40000 ALTER TABLE `oc_ansible_tasks` DISABLE KEYS */;
INSERT INTO `oc_ansible_tasks` VALUES (1,140,'admin',3,1,'/data/www/data/scrips/liangjian/id_8 -i',8,1,'2018-10-29 03:11:28','2018-10-29 03:11:43'),(2,0,'SysTask',1,2,'copy',0,0,'2018-11-07 06:34:29',NULL),(3,0,'SysTask',1,2,'copy',0,1,'2018-11-08 02:06:01','2018-11-08 02:07:50'),(4,140,'admin',1,0,'w',0,1,'2018-11-09 09:52:14','2018-11-09 09:52:29'),(5,140,'admin',1,0,'w',0,1,'2018-11-09 09:57:49','2018-11-09 09:57:53');
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_ansible_tasks_server`
--

LOCK TABLES `oc_ansible_tasks_server` WRITE;
/*!40000 ALTER TABLE `oc_ansible_tasks_server` DISABLE KEYS */;
INSERT INTO `oc_ansible_tasks_server` VALUES (1,1,10,'10.17.1.152',NULL,'SUCCESS','{\n    \"changed\": true, \n    \"rc\": 0, \n    \"stderr\": \"Connection to 10.17.1.152 closed.\\r\\n\", \n    \"stdout\": \"Getway local path=/tmp/.ansible/tmp/ansible-tmp-1540782690.87-111720109233546/id_8\\r\\n\", \n    \"stdout_lines\": [\n        \"Getway local path=/tmp/.ansible/tmp/ansible-tmp-1540782690.87-111720109233546/id_8\"\n    ]\n}\n',1,'2018-10-29 03:11:32',NULL),(2,1,8,'172.17.0.236',NULL,'UNREACHABLE','{\n    \"changed\": false, \n    \"msg\": \"Failed to connect to the host via ssh: ssh: connect to host 172.17.0.236 port 22: Operation timed out\\r\\n\", \n    \"unreachable\": true\n}\n',0,'2018-10-29 03:11:40',NULL),(3,1,9,'10.251.238.209',NULL,'UNREACHABLE','{\n    \"changed\": false, \n    \"msg\": \"Failed to connect to the host via ssh: ssh: connect to host 10.251.238.209 port 22: Operation timed out\\r\\n\", \n    \"unreachable\": true\n}\n',0,'2018-10-29 03:11:40',NULL),(4,2,10,'10.17.1.152',NULL,'SUCCESS','{\n    \"changed\": true, \n    \"dest\": \"/data/www/getway/\", \n    \"src\": \"/data/www/getway/\"\n}\n',1,'2018-11-07 06:36:17',NULL),(5,3,10,'10.17.1.152',NULL,'SUCCESS','{\n    \"changed\": false, \n    \"dest\": \"/data/www/getway/\", \n    \"src\": \"/data/www/getway/\"\n}\n',1,'2018-11-08 02:07:49',NULL),(6,4,9,'10.251.238.209',NULL,'FAILED','10.251.238.209 | UNREACHABLE! => {\n    \"changed\": false, \n    \"msg\": \"Failed to connect to the host via ssh: ssh: connect to host 10.251.238.209 port 22: Operation timed out\\r\\n\", \n    \"unreachable\": true\n}\n',0,'2018-11-09 09:52:27',NULL),(7,5,10,'10.17.1.152','rc=0','SUCCESS',' 17:57:52 up 79 days,  3:27,  1 user,  load average: 0.00, 0.00, 0.00\nUSER     TTY      FROM              LOGIN@   IDLE   JCPU   PCPU WHAT\nmanage   pts/0    10.17.66.120     17:57    0.00s  0.28s  0.00s /bin/sh -c sudo\n',1,'2018-11-09 09:57:52',NULL);
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
INSERT INTO `oc_config_center` VALUES (4,'LDAP','online','LDAP_USER_DN','ou=users,ou=system',NULL,'2017-05-24 22:14:41','2017-05-24 22:14:58'),(5,'LDAP','online','LDAP_GROUP_DN','ou=groups,ou=system',NULL,'2017-05-24 22:15:27',NULL),(6,'LDAP','online','LDAP_GROUP_FILTER','bamboo-',NULL,'2017-05-24 22:16:29',NULL),(7,'LDAP','online','LDAP_GROUP_PREFIX','group_',NULL,'2017-05-24 22:16:47','2017-05-24 22:16:56'),(8,'REDIS','online','REDIS_HOST','opsCloud',NULL,'2017-05-30 17:42:37','2018-06-08 07:52:15'),(9,'REDIS','online','REDIS_PORT','6379',NULL,'2017-05-30 17:43:35',NULL),(10,'REDIS','online','REDIS_PASSWD','opsCloud',NULL,'2017-05-30 17:44:03','2018-06-08 07:52:17'),(11,'REDIS','daily','REDIS_HOST','127.0.0.1',NULL,'2017-05-30 17:46:09','2017-05-30 17:46:16'),(12,'REDIS','daily','REDIS_PORT','6379',NULL,'2017-05-30 17:46:20','2017-05-30 17:47:21'),(13,'REDIS','daily','REDIS_PASSWD',NULL,NULL,'2017-05-30 17:47:35','2017-05-30 17:47:39'),(14,'ZABBIX','online','ZABBIX_API_URL','http://zabbix.example.com/api_jsonrpc.php','','2017-05-30 17:49:02','2018-11-27 06:41:06'),(15,'ZABBIX','online','ZABBIX_API_USER','admin','','2017-05-30 17:50:41','2018-11-27 06:41:06'),(16,'ZABBIX','online','ZABBIX_API_PASSWD','password','','2017-05-30 17:51:58','2018-11-27 06:41:06'),(17,'ZABBIX','online','ZABBIX_API_KEY','opsCloud','','2017-05-30 17:57:51','2018-06-08 07:52:25'),(18,'ALIYUN_ECS','online','ALIYUN_ECS_ACCESS_KEY','kkkkkkkkkkkkk','Aliyun ECS AccessKey','2017-05-30 17:58:04','2018-11-27 06:37:21'),(19,'ALIYUN_ECS','online','ALIYUN_ECS_ACCESS_SECRET','sssssssssssssss','Aliyun ECS AccessSecret','2017-05-30 18:47:15','2018-11-27 06:37:21'),(20,'ALIYUN_ECS','online','ALIYUN_ECS_REGION_ID','cn-hangzhou,cn-hongkong,us-west-1','查询的区域:华东1,香港,美国西部1 ... 按此格式添加  regionId1,regionId2,regionId3 ...','2017-05-30 18:48:52','2017-05-30 18:52:03'),(21,'ALIYUN_ECS','online','ALIYUN_ECS_SEARCH_TIME','120','查询时间（分钟）','2017-05-30 18:49:01','2017-05-30 18:52:04'),(22,'ALIYUN_ECS','online','ALIYUN_ECS_IMAGE_ID','m-bp1igpeohje7yu0ahjis','ECS模版机配置，新建的ECS将会用此模版机','2017-05-30 18:49:46','2017-05-30 18:52:16'),(23,'ALIYUN_ECS','online','ALIYUN_ECS_SECURITY_GROUP_ID','opsCloud','安全组id，新建的ecs将会加入此安全组','2017-05-30 18:50:07','2018-06-08 07:52:29'),(24,'ALIYUN_ECS','online','ALIYUN_ECS_PUBLIC_NETWORK_ID','10','ip_network表中的阿里云公网网段','2017-05-30 18:50:12','2017-05-30 18:52:29'),(35,'VCSA','online','VCSA_HOST','vcsa.example.com','VCSA 服务器域名或ip','2017-05-30 19:59:58','2018-11-27 06:41:27'),(36,'VCSA','online','VCSA_USER','cmdb@sso.example.com','VCSA 管理员账号','2017-05-30 20:00:15','2018-11-27 06:41:27'),(37,'VCSA','online','VCSA_PASSWD','password','VCSA 管理员密码','2017-05-30 20:00:32','2018-11-27 06:38:49'),(38,'EMAIL','online','EMAIL_HOST','opscloud',NULL,'2017-05-30 21:18:05','2018-06-08 07:48:30'),(39,'EMAIL','online','EMAIL_PORT',NULL,NULL,'2017-05-30 21:18:12','2017-05-30 21:18:46'),(40,'EMAIL','online','EMAIL_USERNAME','opscloud',NULL,'2017-05-30 21:18:21','2018-06-08 07:48:33'),(41,'EMAIL','online','EMAIL_PWD','opscloud',NULL,'2017-05-30 21:18:32','2018-06-08 07:48:36'),(42,'GETWAY','online','GETWAY_HOST_CONF_FILE','/data/www/getway/users/.public/getway_host.conf','','2017-05-30 21:24:45','2018-06-06 06:45:23'),(43,'GETWAY','online','GETWAY_USER_CONF_PATH','/data/www/getway/users','','2017-05-30 21:25:12','2018-06-06 03:59:10'),(44,'GETWAY','online','GETWAY_KEY_PATH','/data/www/getway/keys','','2017-05-30 21:25:44','2018-06-06 03:59:10'),(45,'GETWAY','online','GETWAY_KEY_FILE','/authorized_keys','','2017-05-30 21:26:03','2018-06-06 04:00:44'),(46,'GETWAY','online','GETWAY_KEYSTORE_FILE_PATH','/data/www/getway_key','','2017-05-30 21:26:30','2018-06-06 02:30:14'),(47,'PUBLIC','online','DEPLOY_PATH','/data/www/ROOT/static/deploy','持续集成deploy目录','2017-05-30 21:43:30','2017-05-30 21:48:44'),(48,'PUBLIC','online','TOMCAT_CONFIG_NAME','tomcat_setenv.conf','tomcat配置文件名称','2017-05-30 21:44:00','2017-05-30 21:47:03'),(54,'REDIS','dev','REDIS_HOST','10.17.1.120',NULL,'2017-05-30 22:01:29','2017-05-30 22:01:58'),(55,'REDIS','dev','REDIS_PORT','6379',NULL,'2017-05-30 22:01:31','2017-05-30 22:02:01'),(56,'REDIS','dev','REDIS_PASSWD',NULL,NULL,'2017-05-30 22:01:33','2017-05-30 22:01:51'),(57,'DINGTALK','online','DINGTALK_TOKEN_DEPLOY','',NULL,'2017-05-30 22:56:11','2018-06-08 07:48:47'),(59,'DINGTALK','online','DINGTALK_WEBHOOK','',NULL,'2017-05-30 22:57:46','2018-06-08 07:48:46'),(60,'DINGTALK','daily','DINGTALK_TOKEN_DEPLOY','',NULL,'2017-05-30 22:58:45','2018-06-08 07:48:48'),(61,'DINGTALK','daily','DINGTALK_WEBHOOK','',NULL,'2017-05-30 22:58:47','2018-06-08 07:48:49'),(62,'DINGTALK','dev','DINGTALK_TOKEN_DEPLOY','',NULL,'2017-05-30 22:59:59','2018-06-08 07:48:50'),(63,'DINGTALK','dev','DINGTALK_WEBHOOK','',NULL,'2017-05-30 23:00:01','2018-06-08 07:48:52'),(64,'SHADOWSOCKS','online','SHADOWSOCKS_SERVER_1','','翻墙代理服务器1','2017-05-30 23:16:22','2018-06-08 07:48:52'),(65,'SHADOWSOCKS','online','SHADOWSOCKS_SERVER_2','','翻墙代理服>务器2','2017-05-30 23:17:06','2018-06-08 07:48:57'),(70,'PUBLIC','online','ADMIN_PASSWD','opscloud','初始化系统后的admin密码','2017-05-30 23:33:23','2018-06-08 07:49:01'),(71,'EXPLAIN_CDL','online','EXPLAIN_CDL_APP_ID','71',NULL,'2017-05-30 23:40:35','2017-05-30 23:42:16'),(72,'EXPLAIN_CDL','online','EXPLAIN_CDL_APP_KEY','tvuRKiBFuaE=',NULL,'2017-05-30 23:41:40','2017-05-30 23:42:30'),(73,'EXPLAIN_CDL','online','EXPLAIN_CDL_APP_NAME','sqlaudit',NULL,'2017-05-30 23:41:41','2017-05-30 23:43:05'),(74,'EXPLAIN_CDL','online','EXPLAIN_CDL_ENV','dev',NULL,'2017-05-30 23:41:44','2017-05-30 23:43:10'),(75,'EXPLAIN_CDL','online','EXPLAIN_CDL_GROUP_NAME','group1_audit',NULL,'2017-05-30 23:41:46','2017-05-30 23:43:27'),(76,'EXPLAIN_CDL','online','EXPLAIN_CDL_LOCAL_PATH','/data/www/temp/',NULL,'2017-05-30 23:41:49','2017-05-30 23:43:47'),(77,'EXPLAIN_CDL','online','EXPLAIN_GIT_USERNAME','opscloud',NULL,'2017-05-30 23:43:55','2018-06-08 07:49:10'),(78,'EXPLAIN_CDL','online','EXPLAIN_GIT_PWD','opscloud',NULL,'2017-05-30 23:44:02','2018-06-08 07:49:12'),(79,'ALIYUN_ECS','online','ALIYUN_ECS_VSWITCH_ID','','如果是创建VPC类型的实例，需要指定虚拟交换机的ID,留空则为经典网络','2017-05-31 08:43:18','2018-05-30 02:33:08'),(81,'ANSIBLE','online','ANSIBLE_BIN','/usr/bin/ansible','ansible命令路径','2017-06-02 01:25:10','2018-06-08 09:38:08'),(89,'PUBLIC','online','IPTABLES_WEBSERVICE_PATH','/data/www/ROOT/static/iptables/web-service','web-service的iptables配置目录','2017-06-02 05:20:35','2017-06-02 05:20:52'),(91,'LDAP','online','LDAP_GROUP_JIRA_USERS','jira-users',NULL,'2017-06-22 09:19:42','2017-06-22 09:37:09'),(92,'LDAP','online','LDAP_GROUP_CONFLUENCE_USERS','confluence-users',NULL,'2017-06-22 09:21:16','2017-06-22 09:25:37'),(98,'PUBLIC','online','OFFICE_DMZ_IP_NETWORK_ID','11','办公网络dmz区ipNetworkId(用于vm主机网络配置)','2017-07-28 01:37:04',NULL),(100,'JENKINS','online','JENKINS_HOST','opscloud','jenkins服务器url','2017-12-15 02:00:50','2018-06-08 07:49:21'),(101,'JENKINS','online','JENKINS_USER','opscloud','jenkins登陆账户','2017-12-15 02:00:51','2018-06-08 07:49:19'),(102,'JENKINS','online','JENKINS_PWD','opscloud','jenkins登陆password或Token(推荐)','2017-12-15 02:00:51','2018-06-08 07:49:18'),(103,'JENKINS','online','JENKINS_OSS_BUCKET_FT_ONLINE','img0-cdn','前端构建OSSBucket','2017-12-15 02:01:29','2017-12-15 02:02:22'),(104,'JENKINS','online','JENKINS_FT_BUILD_BRANCH','dev,daily,gray','触发前端构建的branch','2017-12-15 05:08:28','2018-02-01 04:10:32'),(105,'DINGTALK','online','DINGTALK_TOKEN_FT_BUILD','opscloud','前端构建token','2017-12-18 08:39:46','2018-06-08 07:49:24'),(106,'DINGTALK','online','DINGTALK_TOKEN_ANDROID_BUILD','opscloud','Android构建token','2017-12-22 05:23:37','2018-06-08 07:49:25'),(107,'DINGTALK','dev','DINGTALK_TOKEN_ANDROID_BUILD','opscloud','','2017-12-22 06:09:52','2018-06-08 07:49:26'),(108,'DINGTALK','daily','DINGTALK_TOKEN_ANDROID_BUILD','opscloud','','2017-12-22 06:09:57','2018-06-08 07:49:27'),(109,'DINGTALK','dev','DINGTALK_TOKEN_FT_BUILD','opscloud','','2017-12-22 06:10:48','2018-06-08 07:49:28'),(110,'DINGTALK','daily','DINGTALK_TOKEN_FT_BUILD','opscloud','','2017-12-22 06:10:51','2018-06-08 07:49:29'),(111,'JENKINS','online','GITLAB_USER','opscloud','gitlab代码仓库管理员账户','2017-12-27 03:01:35','2018-06-08 07:49:34'),(112,'JENKINS','online','GITLAB_PWD','opscloud','gitlab代码仓库管理员密码','2017-12-27 03:02:33','2018-06-08 07:49:35'),(113,'JENKINS','online','GITLAB_HOST','opscloud','gitlab代码仓库服务器url','2017-12-27 03:03:51','2018-06-08 07:49:42'),(114,'JENKINS','online','STASH_USER','opscloud','stash代码仓库管理员账户','2017-12-27 03:04:55','2018-06-08 07:49:43'),(115,'JENKINS','online','STASH_PWD','opscloud','stash代码仓库管理员账户','2017-12-27 03:05:20','2018-06-08 07:49:44'),(116,'JENKINS','online','STASH_HOST','opscloud','stash代码仓库服务器url','2017-12-27 03:05:56','2018-06-08 07:49:47'),(117,'JENKINS','online','REPO_LOCAL_PATH','/data/www/temp','代码仓库本地临时目录','2017-12-27 03:06:56',NULL),(118,'DINGTALK','online','DINGTALK_TOKEN_IOS_BUILD','opscloud','','2017-12-29 02:04:01','2018-06-08 07:49:48'),(119,'DINGTALK','daily','DINGTALK_TOKEN_IOS_BUILD','opscloud','','2017-12-29 02:04:08','2018-06-08 07:49:49'),(120,'DINGTALK','dev','DINGTALK_TOKEN_IOS_BUILD','opscloud','','2017-12-29 02:04:11','2018-06-08 07:49:49'),(121,'JENKINS','online','ANDROID_DEBUG_URL','opscloud','android bebug接口 映射到 115.236.161.19 8889','2018-01-10 08:13:36','2018-06-08 07:49:53'),(123,'ALIYUN_ECS','online','ALIYUN_FINANCE_ECS_ACCESS_KEY','opscloud','Aliyun FINANCE ECS AccessKey','2018-05-08 03:30:08','2018-06-08 07:49:54'),(124,'ALIYUN_ECS','online','ALIYUN_FINANCE_ECS_ACCESS_SECRET','opscloud','Aliyun FINANCE ECS AccessSecret','2018-05-08 03:30:55','2018-06-08 07:49:55'),(125,'ALIYUN_ECS','online','ALIYUN_FINANCE_ECS_REGION_ID','opscloud','查询的区域:华东1(ch-hangzhou),华北1（cn-qingdao),华东2金融(cn-shanghai-finance-1),华南1金融(cn-shenzhen-finance-1)','2018-05-08 03:32:10','2018-06-08 07:51:58'),(140,'ANSIBLE','online','ANSIBLE_TASK_SCRIPTS_PATH','/data/www/data/scrips','','2018-05-22 11:06:39',NULL),(142,'ANSIBLE','dev','ANSIBLE_BIN','/usr/local/Cellar/ansible/2.4.3.0/bin/ansible','','2018-06-07 03:10:47','2018-06-12 03:15:42'),(143,'ANSIBLE','dev','ANSIBLE_TASK_SCRIPTS_PATH','/data/www/data/scrips','','2018-06-07 03:11:12','2018-06-07 03:11:20'),(144,'ZABBIX','online','ZABBIX_API_VERSION','3.0','API版本（目前支持3.0，3.4）','2018-06-11 12:10:13','2018-06-11 12:10:33');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_config_file_copy`
--

LOCK TABLES `oc_config_file_copy` WRITE;
/*!40000 ALTER TABLE `oc_config_file_copy` DISABLE KEYS */;
INSERT INTO `oc_config_file_copy` VALUES (2,12,'filegroup_ansible',9,'/data/www/ansible/','/','root','root','2018-10-30 05:53:49',NULL);
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
INSERT INTO `oc_config_file_group` VALUES (12,'filegroup_ansible','Ansible主机配置文件组','2018-06-04 08:23:47',NULL),(13,'filegroup_getway','Getway主机配置文件组','2018-06-05 10:13:40',NULL),(14,'filegroup_ss','shadowsocks用户配置文件组','2018-06-11 01:54:49',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_config_property`
--

LOCK TABLES `oc_config_property` WRITE;
/*!40000 ALTER TABLE `oc_config_property` DISABLE KEYS */;
INSERT INTO `oc_config_property` VALUES (1,'TOMCAT_APP_NAME_OPT',NULL,'tomcat名称',1,'2016-10-19 07:37:26','2016-10-27 07:24:56'),(2,'JAVA_INSTALL_VERSION','','JDK版本',1,'2016-10-19 07:37:27','2017-05-24 03:45:22'),(3,'TOMCAT_INSTALL_VERSION','','Tomcat版本',1,'2016-10-19 07:37:27','2017-05-24 03:45:26'),(4,'HOST_NAME','','服务器组名称',2,'2016-10-19 07:42:44','2016-12-20 03:51:04'),(5,'DNS_MASTER','','nameserver1',2,'2016-10-19 07:42:44','2016-12-20 03:51:30'),(6,'DNS_SLAVE','','nameserver2',2,'2016-10-19 07:42:44','2016-12-20 03:51:45'),(7,'tomcat.setenv.conf.url',NULL,'tomcat配置文件url',1,'2016-10-19 07:46:17',NULL),(22,'TOMCAT_HTTP_PORT_OPT','8080','http端口',1,'2016-10-19 07:59:01','2016-10-27 05:56:46'),(23,'TOMCAT_SHUTDOWN_PORT_OPT','8000','shutdown端口',1,'2016-10-19 07:59:01','2016-10-27 05:57:07'),(24,'TOMCAT_JMX_rmiRegistryPortPlatform_OPT','10000','JMX端口:默认10000',1,'2016-10-19 07:59:01','2016-10-27 05:57:22'),(25,'TOMCAT_JMX_rmiServerPortPlatform_OPT','10100','JMX端口:默认10100',1,'2016-10-19 07:59:01','2016-10-27 05:57:35'),(26,'TOMCAT_SERVERXML_WEBAPPSPATH_OPT',NULL,'webapps路径配置',1,'2016-10-19 07:59:01','2016-10-27 05:57:55'),(27,'HTTP_STATUS_OPT','webStatus','check页配置',1,'2016-10-19 07:59:01','2016-11-14 09:02:00'),(28,'APP_CONF_NAME_OPT',NULL,'配置文件',1,'2016-10-19 07:59:01','2016-10-27 05:58:26'),(29,'TOMCAT_HTTP_URI_ENCODING','utf8','http端口编码:默认utf8',1,'2016-10-19 07:59:01','2016-10-27 05:59:08'),(30,'OPEN_TOMCAT_JAVA_OPTS','true','是否打开启动参数配置',1,'2016-10-19 07:59:01','2016-11-24 08:01:25'),(31,'TOMCAT_JAVA_OPTS','-Dfile.encoding=UTF-8 -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=9999,server=y,suspend=n','启动参数配置',1,'2016-10-19 07:59:01','2016-11-24 08:01:16'),(32,'HTTP_STATUS_TIME','5','健康检查等待时间:默认5秒',1,'2016-10-19 07:59:01','2016-10-27 06:00:13'),(33,'SET_JVM_Xss',NULL,'Xss',1,'2016-10-19 07:59:01','2016-10-27 06:00:50'),(34,'SET_JVM_Xms',NULL,'Xms',1,'2016-10-19 07:59:01','2016-10-27 06:00:50'),(35,'SET_JVM_Xmx',NULL,'Xmx',1,'2016-10-19 07:59:01','2016-10-27 06:00:50'),(36,'SET_JVM_Xmn',NULL,'Xmn',1,'2016-10-19 07:59:01','2016-10-27 06:00:50'),(40,'PROJECT_NAME',NULL,'项目名称',3,'2016-10-19 08:05:00','2016-10-27 07:20:39'),(41,'HOST_GROUP',NULL,'主机组前缀',3,'2016-10-19 08:05:00','2016-10-27 07:20:39'),(42,'ENVIRONMENTEL','daily:gray:production','环境配置',3,'2016-10-19 08:05:00','2016-12-14 09:47:37'),(43,'TOMCAT_PROJECT_CONF_DOWNLOAD_NAME',NULL,'持续集成持久化的配置文件:基本废弃',3,'2016-10-19 08:05:00','2016-10-27 07:22:17'),(45,'IPTABLES_GROUP','','iptables组名称',9,'2016-12-06 02:39:34',NULL),(46,'ZABBIX_PROXY','','zabbix代理服务器名称',10,'2016-12-20 05:12:22','2018-06-04 02:58:33'),(48,'ZABBIX_HOST_MONITOR_PUBLIC_IP','false','是否使用公网IP监控',10,'2016-12-20 05:15:59',NULL),(49,'GETWAY_HOST_SSH_PUBLIC_IP','false','是否使用公网ip进行ssh连接',11,'2016-12-20 05:20:44','2017-05-24 03:35:40'),(50,'OS','Linux','操作系统类型',2,'2017-01-03 05:47:50',NULL),(51,'OS_VERSION','CentOS6.x','操作系统版本',2,'2017-01-03 05:48:27',NULL),(52,'PASSWD',NULL,'登陆密码',2,'2017-01-03 05:48:43',NULL),(53,'ZABBIX_DISK_DATA_VOLUME_NAME','','数据盘卷标',10,'2017-02-28 07:39:34','2017-02-28 07:40:05'),(54,'ZABBIX_DISK_SYS_VOLUME_NAME','','系统盘卷标',10,'2017-02-28 07:39:56',NULL),(55,'NGINX_URL_ALIAS','','location中的路径别名，默认为PROJECT_NAME相同',12,'2017-03-09 06:32:38','2017-03-17 03:42:42'),(56,'NGINX_LOCATION_BUILD','true','是否生成标准location',12,'2017-03-09 06:34:00',NULL),(57,'NGINX_UPSTREAM_NAME','','upstream中的名称（空则为项目名）；例如upstream.name.java',12,'2017-03-13 10:05:29',NULL),(58,'NGINX_UPSTREAM_BUILD','true','是否生成标准的upstream配置',12,'2017-03-14 02:25:33',NULL),(60,'NGINX_LOCATION_MANAGE_BACK','false','manage中用back环境对应location',12,'2017-03-14 07:04:44',NULL),(61,'NGINX_UPSTREAM_GRAY_IS_PROD','false','upstream灰度与线上一致',12,'2017-03-15 03:02:25',NULL),(62,'NGINX_LOCATION_LIMIT_REQ','','location limit_req配置',12,'2017-03-17 03:25:29',NULL),(63,'NGINX_PROXY_PASS_TAIL','','proxy_pass中的后部路径例如proxy_pass http://upstream.api.java/allegan/;',12,'2017-03-17 03:44:12',NULL),(64,'IPTABLES_DUBBO_BUILD','true','是否生成dubbo的iptables配置',13,'2017-03-27 09:10:24',NULL),(66,'IPTABLES_DUBBO_GRAY_IS_PROD','false','dubbo白名单中线上环境开通灰度白名单',13,'2017-06-29 06:32:58',NULL),(67,'NGINX_LOCATION_PARAM','','location中的扩展参数',12,'2017-07-07 06:05:12',NULL),(68,'NGINX_UPSTREAM_MAX_FAILS','','允许请求失败的次数默认为1。当超过最大次数时，返回proxy_next_upstream 模块定义的错误',12,'2017-07-26 07:25:19',NULL),(69,'NGINX_UPSTREAM_FAIL_TIMEOUT','','max_fails次失败后，暂停的时间。',12,'2017-07-26 07:27:38',NULL),(70,'NGINX_UPSTREAM_WEIGHT','','权重,默认为1。 weight越大，负载的权重就越大。',12,'2017-07-26 07:36:50',NULL),(71,'NGINX_UPSTREAM_SERVER_TYPE','','down 表示单前的server暂时不参与负载;backup 备用服务器, 其它所有的非backup机器down或者忙的时候，请求backup机器。所以这台机器压力会最轻。',12,'2017-07-26 07:39:12',NULL),(73,'ZABBIX_TEMPLATES','','模版名称（多个用,分割）',10,'2018-06-04 01:29:21','2018-06-04 01:29:41'),(74,'NGINX_PROJECT_NAME','','项目名称，留空则取Tomcat相关配置',12,'2018-11-01 09:28:46',NULL),(75,'NGINX_UPSTREAM_PORT','','留看则取Tomcat相关配置',12,'2018-11-01 09:29:46',NULL),(76,'NGINX_CHECK','false','启用Nginx页面检测配置',12,'2018-11-01 10:29:00',NULL);
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
-- Table structure for table `oc_copy`
--

DROP TABLE IF EXISTS `oc_copy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_copy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `businessKey` varchar(100) NOT NULL DEFAULT '' COMMENT '业务key',
  `businessId` bigint(20) NOT NULL COMMENT '业务id',
  `srcPath` varchar(200) NOT NULL DEFAULT '' COMMENT '源路径',
  `destPath` varchar(200) NOT NULL DEFAULT '' COMMENT '目标路径',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '权限',
  `usergroup` varchar(50) NOT NULL DEFAULT '' COMMENT '权限',
  `doCopy` tinyint(1) NOT NULL DEFAULT '1' COMMENT '自动同步',
  `doScript` tinyint(1) NOT NULL DEFAULT '0' COMMENT '自动脚本',
  `taskScriptId` bigint(20) DEFAULT NULL COMMENT '脚本id',
  `params` varchar(200) DEFAULT NULL COMMENT '脚本参数',
  `content` varchar(200) DEFAULT NULL COMMENT '说明',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_copy`
--

LOCK TABLES `oc_copy` WRITE;
/*!40000 ALTER TABLE `oc_copy` DISABLE KEYS */;
INSERT INTO `oc_copy` VALUES (2,'NGINX',14,'/data/www/conf/web/www.qianou.com/daily','/data/www/temp','root','root',1,1,10,'','','2018-11-09 10:22:24','2018-11-09 10:27:54');
/*!40000 ALTER TABLE `oc_copy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_copy_log`
--

DROP TABLE IF EXISTS `oc_copy_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_copy_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `copyId` bigint(20) NOT NULL,
  `vhostId` bigint(20) DEFAULT NULL,
  `serverName` varchar(100) NOT NULL DEFAULT '',
  `envType` int(11) NOT NULL,
  `serverId` bigint(20) NOT NULL,
  `serverContent` varchar(100) DEFAULT NULL,
  `copyMsg` text NOT NULL COMMENT 'ansible返回消息',
  `copySuccess` tinyint(1) DEFAULT NULL COMMENT 'copy是否成功',
  `copyResult` varchar(20) DEFAULT NULL COMMENT 'copy返回消息:SUCCESS/FAILED!/UNREACHABLE!',
  `copyChanged` tinyint(1) DEFAULT NULL COMMENT 'copy文件是否变更',
  `doScript` tinyint(1) DEFAULT NULL COMMENT '是否执行脚本',
  `scriptSuccess` tinyint(4) DEFAULT NULL,
  `scriptChanged` tinyint(1) DEFAULT NULL COMMENT '脚本changed',
  `scriptStdoutPath` varchar(200) DEFAULT NULL COMMENT '脚本stdout_linens文件存储位置',
  `scriptRc` int(11) DEFAULT NULL,
  `scriptStderr` varchar(200) DEFAULT NULL,
  `scriptResult` varchar(20) DEFAULT NULL COMMENT 'script返回消息:SUCCESS/FAILED!/UNREACHABLE!',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_copy_log`
--

LOCK TABLES `oc_copy_log` WRITE;
/*!40000 ALTER TABLE `oc_copy_log` DISABLE KEYS */;
INSERT INTO `oc_copy_log` VALUES (11,2,1,'www.qianou.com',2,10,'getway-daily-1','10.17.1.152 | SUCCESS => {\n    \"changed\": true, \n    \"dest\": \"/data/www/temp/\", \n    \"src\": \"/data/www/conf/web/www.qianou.com/daily\"\n}\n',1,'SUCCESS',1,1,0,1,'/data/www/logs/copyLogs/20181112_175153_id11.log',1,'Connection to 10.17.1.152 closed.\r\n','FAILED!','2018-11-12 09:51:51','2018-11-12 09:51:53');
/*!40000 ALTER TABLE `oc_copy_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_copy_server`
--

DROP TABLE IF EXISTS `oc_copy_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_copy_server` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `copyId` bigint(20) NOT NULL,
  `serverId` bigint(20) NOT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `copyId` (`copyId`,`serverId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_copy_server`
--

LOCK TABLES `oc_copy_server` WRITE;
/*!40000 ALTER TABLE `oc_copy_server` DISABLE KEYS */;
INSERT INTO `oc_copy_server` VALUES (5,2,10,'2018-11-09 10:22:47',NULL);
/*!40000 ALTER TABLE `oc_copy_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_domain`
--

DROP TABLE IF EXISTS `oc_domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_domain` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `domain` varchar(200) NOT NULL DEFAULT '' COMMENT 'nginx配置管理中的域名',
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_domain`
--

LOCK TABLES `oc_domain` WRITE;
/*!40000 ALTER TABLE `oc_domain` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_domain` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=631 DEFAULT CHARSET=utf8 COMMENT='阿里云ECS数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_ecs_server`
--

LOCK TABLES `oc_ecs_server` WRITE;
/*!40000 ALTER TABLE `oc_ecs_server` DISABLE KEYS */;
INSERT INTO `oc_ecs_server` VALUES (311,NULL,'2018-06-14 18:26:00',1,'getway','172.17.6.215',NULL,10,1,2048,'cloud_efficiency',40,'cloud_efficiency',100,'i-bp1ebjikl4c7g0z9pyhs','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PostPaid',1,'2018-06-15 02:39:04',NULL),(312,NULL,'2018-06-14 18:40:00',1,'getway','172.17.0.235',NULL,10,1,1024,'cloud_efficiency',40,'cloud_efficiency',100,'i-bp1hz9wmf07ot8h3qntr','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',1,'2018-06-15 02:40:21',NULL),(313,NULL,'2018-06-12 17:37:00',1,'outhttp','10.27.101.121','118.178.187.161',10,1,2048,NULL,0,NULL,0,'i-bp1j3wje0m1k7lu1mqey','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(314,NULL,'2018-06-12 17:37:00',1,'outhttp','10.26.38.50','116.62.50.48',10,1,2048,NULL,0,NULL,0,'i-bp192gcqz9hrhzrx7taz','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(315,NULL,'2018-06-12 17:36:00',1,'outhttp-gray','10.27.101.180','118.178.192.39',10,1,2048,NULL,0,NULL,0,'i-bp11qmgs6x4y7hip7hto','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(316,NULL,'2018-06-06 18:55:00',1,'mysql-centralbank-slave1','10.28.143.130','116.62.15.205',100,8,16384,NULL,0,NULL,0,'i-bp1dbtqx34r8zg6hswsa','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(317,NULL,'2018-06-01 00:27:00',1,'ka-agent','10.24.237.168','118.178.58.181',10,1,2048,NULL,0,NULL,0,'i-bp180nzerpqdur76m19j','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(318,NULL,'2018-05-31 02:23:00',1,'ka-agent','10.25.60.81','120.27.246.159',10,1,2048,NULL,0,NULL,0,'i-bp1cr96zjyeri85x605k','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(319,NULL,'2018-05-14 01:06:00',1,'cmdb-gray','10.25.252.242',NULL,10,2,4096,NULL,0,NULL,0,'i-bp1265imupnbvcpk28a9','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PostPaid',0,'2018-06-28 01:22:28',NULL),(320,NULL,'2018-05-09 00:18:00',1,'realtime-dataspout','10.28.255.79','118.178.179.102',10,1,2048,NULL,0,NULL,0,'i-bp196gwsgxtlp10lbuwm','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(321,NULL,'2018-05-03 00:16:00',1,'dubbo-admin','10.25.71.106',NULL,10,1,1024,NULL,0,NULL,0,'i-bp11005jaavcj0zc0awj','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(322,NULL,'2018-04-12 01:18:00',1,'realtime-dataspout-gray','10.28.253.244','118.178.179.184',10,1,2048,NULL,0,NULL,0,'i-bp16e5pr6sbu9ytmg2j6','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(323,NULL,'2018-02-08 23:48:00',1,'hive-monitor-aliyun','10.27.69.91','116.62.52.252',10,1,1024,NULL,0,NULL,0,'i-bp1gufl1pt8953cxpe43','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(324,NULL,'2018-01-22 00:04:00',1,'binlog-sync','10.25.63.236',NULL,10,4,8192,NULL,0,NULL,0,'i-bp14q6cull14y4q65xtk','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(325,NULL,'2018-01-16 22:58:00',1,'binlog-sync','10.27.68.213',NULL,10,4,8192,NULL,0,NULL,0,'i-bp11ml4pnhurh7jk0g5z','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(326,NULL,'2018-01-16 22:58:00',1,'binlog-sync','10.28.254.172',NULL,10,4,8192,NULL,0,NULL,0,'i-bp1aiw3ehgrbaw5ppc48','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(327,NULL,'2017-12-26 03:06:00',1,'emr-hadoop1','172.18.33.19',NULL,0,16,65536,NULL,0,NULL,0,'i-bp11d7iyhtqaww0zvc5f','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(328,NULL,'2017-12-26 03:06:00',1,'emr-hadoop2','172.18.33.27',NULL,0,16,65536,NULL,0,NULL,0,'i-bp11d7iyhtqaww0zvc5p','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(329,NULL,'2017-12-26 03:06:00',1,'emr-hadoop3','172.18.33.22',NULL,0,16,65536,NULL,0,NULL,0,'i-bp11d7iyhtqaww0zvc5e','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(330,NULL,'2017-12-26 03:06:00',1,'emr-hadoop4','172.18.33.14',NULL,0,16,65536,NULL,0,NULL,0,'i-bp11d7iyhtqaww0zvc5d','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(331,NULL,'2017-12-26 03:06:00',1,'emr-master1','172.18.33.12','118.31.64.127',0,8,32768,NULL,0,NULL,0,'i-bp1d7ty7ogbx2c4e38n7','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28','2018-07-19 00:59:28'),(332,NULL,'2017-12-26 03:06:00',1,'emr-master2','172.18.33.11','47.96.143.127',0,8,32768,NULL,0,NULL,0,'i-bp1d7ty7ogbx2c4e38n8','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28','2018-07-19 00:59:28'),(333,NULL,'2017-12-26 03:06:00',1,'emr-hadoop5','172.18.33.24',NULL,0,16,65536,NULL,0,NULL,0,'i-bp11d7iyhtqaww0zvc5m','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(334,NULL,'2017-12-26 03:06:00',1,'emr-hadoop6','172.18.33.26',NULL,0,16,65536,NULL,0,NULL,0,'i-bp11d7iyhtqaww0zvc5b','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(335,NULL,'2017-12-18 22:19:00',1,'mysql-sync-slave1','10.81.87.120','47.97.10.68',50,4,8192,NULL,0,NULL,0,'i-bp14gkb3ixwm81bilezo','cn-hangzhou-f',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28','2018-09-06 17:00:14'),(336,NULL,'2017-12-06 18:15:00',1,'ods','10.27.94.76','101.37.68.143',10,2,4096,NULL,0,NULL,0,'i-bp1hy4e7tmhx6wyk6ek6','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(337,NULL,'2017-12-06 18:15:00',1,'ods','10.51.235.23','118.31.102.222',10,2,4096,NULL,0,NULL,0,'i-bp1gxf93q9mr39n83ccw','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(338,NULL,'2017-12-06 01:13:00',1,'landmarkback','10.27.96.20','116.62.11.28',10,1,2048,NULL,0,NULL,0,'i-bp1gxf93q9mqq43t20zb','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(339,NULL,'2017-11-26 17:48:00',1,'ka-groupon','10.51.233.154','118.31.103.188',10,1,2048,NULL,0,NULL,0,'i-bp17lzjsrcrcwq28278s','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(340,NULL,'2017-11-26 17:48:00',1,'ka-groupon','10.51.234.68','118.31.103.1',10,1,2048,NULL,0,NULL,0,'i-bp10ugchzby4yo0e6oyl','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:28',NULL),(341,NULL,'2017-11-26 17:48:00',1,'ka-groupon-gray','10.27.94.56','116.62.8.1',10,1,2048,NULL,0,NULL,0,'i-bp17hnkqt2mcpgf8sxzf','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(342,NULL,'2017-11-21 01:17:00',1,'ctss-1','10.27.88.214','101.37.69.69',10,1,2048,NULL,0,NULL,0,'i-bp13v1xxtg5yqaqzdld3','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(343,NULL,'2017-11-21 01:14:00',1,'ctss-gray','10.27.93.33','101.37.70.254',10,1,2048,NULL,0,NULL,0,'i-bp174gc4m4dw55dfhhd4','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(344,NULL,'2017-11-19 22:58:00',1,'ib','10.27.91.181','116.62.4.49',10,4,8192,NULL,0,NULL,0,'i-bp1ev0ozeupxmq0hckpf','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:46'),(345,NULL,'2017-11-19 22:58:00',1,'ib','10.27.91.177','116.62.7.206',10,4,8192,NULL,0,NULL,0,'i-bp11lo2azg75gn5m2q7f','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:46'),(346,NULL,'2017-11-19 22:57:00',1,'ib-gray','10.27.87.142','101.37.70.214',10,1,2048,NULL,0,NULL,0,'i-bp16e8n6meiruycbjop3','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(347,NULL,'2017-11-14 23:26:00',1,'itemcore','10.27.92.37',NULL,10,4,8192,NULL,0,NULL,0,'i-bp11cixer8tpnipw6cta','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(348,NULL,'2017-11-14 23:26:00',1,'itemcore','10.27.86.151',NULL,10,4,8192,NULL,0,NULL,0,'i-bp1bg8hyl9dpeh8sowh1','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(349,NULL,'2017-11-14 23:15:00',1,'ods-agent','10.27.91.87','116.62.4.33',10,4,8192,NULL,0,NULL,0,'i-bp18f0b7nbnu427o4jvy','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(350,NULL,'2017-11-06 22:38:00',1,'iop','10.51.237.174','118.31.103.206',10,4,8192,NULL,0,NULL,0,'i-bp12cjbccf1410ejgv9o','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(351,NULL,'2017-11-06 22:38:00',1,'iop','10.51.234.219','120.27.251.128',10,4,8192,NULL,0,NULL,0,'i-bp11wy2vjnjymxt9cbpd','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(352,NULL,'2017-10-09 23:14:00',1,'item-sync-gray','10.51.233.29',NULL,10,2,4096,NULL,0,NULL,0,'i-bp173lfys1bld4dlbvtp','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(353,NULL,'2017-09-21 22:29:00',1,'itemcenter-2','10.27.70.152','116.62.53.185',10,4,8192,NULL,0,NULL,0,'i-bp1j7ccu2suybr5opug1','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(354,NULL,'2017-09-19 00:48:00',1,'dataspout','10.27.68.150','116.62.52.76',10,1,2048,NULL,0,NULL,0,'i-bp17x543bfyqnwfio1y4','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(355,NULL,'2017-09-17 17:41:00',1,'basic-service','10.25.71.123','114.55.97.227',10,4,8192,NULL,0,NULL,0,'i-bp14gh8vvrq5q98l4uhn','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(356,NULL,'2017-09-14 22:16:00',1,'dataspout','10.25.254.136','114.55.253.232',10,1,2048,NULL,0,NULL,0,'i-bp10giyxwps1msh5qa6b','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(357,NULL,'2017-09-14 22:16:00',1,'dataspout-gray','10.51.236.162','118.31.103.10',10,1,2048,NULL,0,NULL,0,'i-bp1ef5gnn6r0hzq5v87c','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(358,NULL,'2017-09-13 01:07:00',1,'basic-service-gray','10.25.67.15','114.55.95.149',10,2,4096,NULL,0,NULL,0,'i-bp1ettboqmo9d3zdyor7','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(359,NULL,'2017-09-10 22:53:00',1,'redis-item-slave1','10.31.54.246','101.37.119.44',0,2,16384,NULL,0,NULL,0,'i-bp1gz1p9p19jea175979','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(360,NULL,'2017-09-03 17:55:00',1,'gc-redis-o2o-slave1','10.31.54.15',NULL,0,2,16384,NULL,0,NULL,0,'i-bp14a9gck8o3hj4datt5','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-08-10 01:18:27'),(361,NULL,'2017-08-30 00:01:00',1,'landmark','10.25.66.107','114.55.141.128',10,1,2048,NULL,0,NULL,0,'i-bp18bqykpa47vz7rykec','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(362,NULL,'2017-08-30 00:00:00',1,'landmark','10.25.65.50','114.55.98.175',10,1,2048,NULL,0,NULL,0,'i-bp10c5bxe2ympppfmtzb','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(363,NULL,'2017-08-29 23:57:00',1,'landmark-gray','10.25.65.63','114.55.141.6',10,1,2048,NULL,0,NULL,0,'i-bp1bp2zva929828qa3nj','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(364,NULL,'2017-08-29 23:31:00',1,'tradeback-gray','10.25.70.47','114.55.141.153',10,2,4096,NULL,0,NULL,0,'i-bp19dzgqtyv39fxd9zrb','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(365,NULL,'2017-08-21 00:50:00',1,'gc-redis-member-slave1','10.31.52.88',NULL,0,2,16384,NULL,0,NULL,0,'i-bp159q04wzckjz4tgjvx','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-08-08 01:22:46'),(366,NULL,'2017-08-17 04:14:00',1,'outapi','10.27.91.59','101.37.68.88',10,4,8192,NULL,0,NULL,0,'i-bp15eeewqguqr5ecawti','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(367,NULL,'2017-08-17 04:14:00',1,'outapi','10.27.88.114','116.62.6.254',10,4,8192,NULL,0,NULL,0,'i-bp1ikfzd0p61t32xudj7','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(368,NULL,'2017-08-06 23:10:00',1,'code-push','10.25.63.200','114.55.93.49',10,2,4096,NULL,0,NULL,0,'i-bp1g0jra2uwrzlsziu47','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(369,NULL,'2017-07-31 18:44:00',1,'mysql-sync-master','10.30.202.178','101.37.205.6',50,4,8192,NULL,0,NULL,0,'i-bp1bf4n7ddycep18ouji','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-09-06 17:00:14'),(370,NULL,'2017-07-30 23:15:00',1,'item-sync','10.27.86.146',NULL,10,2,4096,NULL,0,NULL,0,'i-bp16d3kxt5vz457klzyv','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(371,NULL,'2017-07-05 00:14:00',1,'kabill','10.169.153.155',NULL,10,2,4096,NULL,0,NULL,0,'i-bp16cg6ur9u7a4b3opwp','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(372,NULL,'2017-07-04 18:03:00',1,'itemcenter-1','10.28.145.126','101.37.16.216',10,4,8192,NULL,0,NULL,0,'i-bp1gubr4nl04hwzo0v1t','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(373,NULL,'2017-06-29 18:07:00',1,'outapi','10.29.190.152','101.37.87.112',10,4,8192,NULL,0,NULL,0,'i-bp1fdr8j8zo3dspb9v26','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(374,NULL,'2017-06-29 18:07:00',1,'outapi','10.29.185.43','116.62.107.7',10,4,8192,NULL,0,NULL,0,'i-bp1hsk03svyt3f87o0fb','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(375,NULL,'2017-06-25 18:33:00',1,'outapi-gray','10.28.232.241','118.178.142.100',10,2,4096,NULL,0,NULL,0,'i-bp142ygggj3gwv7hp9xa','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(376,NULL,'2017-06-11 18:42:00',1,'gc-mysql-log-slave1','10.28.144.142','101.37.172.104',100,4,8192,NULL,0,NULL,0,'i-bp1c8mn5lner8liunccd','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-08-23 01:25:30'),(377,NULL,'2017-05-24 16:55:00',1,'kamember','10.29.185.64','101.37.17.250',10,2,4096,NULL,0,NULL,0,'i-bp15is23zeo1gf0cfu23','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-10-09 01:35:21'),(378,NULL,'2017-05-24 16:55:00',1,'kamember','10.27.88.184','101.37.71.121',10,1,2048,NULL,0,NULL,0,'i-bp1iba2yyjy5mg00ngrx','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(379,NULL,'2017-05-23 17:46:00',1,'kamember-gray','10.27.105.36','116.62.41.185',10,2,4096,NULL,0,NULL,0,'i-bp1cyet3bdhu0pidghf3','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-10-09 01:35:21'),(380,NULL,'2017-05-11 02:22:00',1,'picking-gray','10.27.103.33','116.62.10.74',10,2,4096,NULL,0,NULL,0,'i-bp1fuf2hutwe4g9zuxqw','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(381,NULL,'2017-05-11 01:55:00',1,'outway','10.27.101.216','101.37.71.199',10,4,8192,NULL,0,NULL,0,'i-bp1bztte74gprqoe6wwz','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(382,NULL,'2017-05-11 01:43:00',1,'outway','10.31.133.97','101.37.174.24',10,4,8192,NULL,0,NULL,0,'i-bp1dwvu4cvwxvqvqk4an','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(383,NULL,'2017-05-03 03:00:00',1,'zsearch-realtime-2','10.29.112.132','101.37.21.159',10,4,8192,NULL,0,NULL,0,'i-bp13o4s69sg8dx849ay1','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(384,NULL,'2017-05-03 03:00:00',1,'zsearch-realtime-1','10.27.92.223','116.62.41.143',10,4,8192,NULL,0,NULL,0,'i-bp19meli2wlpx8mtfrpx','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(385,NULL,'2017-05-03 02:58:00',1,'zsearch-realtime-gray','10.27.15.144','118.178.86.182',10,4,8192,NULL,0,NULL,0,'i-bp19meli2wlpx6nsbpqs','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-17 01:48:47'),(386,NULL,'2017-04-05 22:24:00',1,'acc-1','10.27.228.240',NULL,0,1,2048,NULL,0,NULL,0,'i-bp1e4qykj1h8j3n5ukyg','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(387,NULL,'2017-04-05 22:24:00',1,'acc-2','10.24.17.195',NULL,0,1,2048,NULL,0,NULL,0,'i-bp1e4qykj1h8j3n5ukyi','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(388,NULL,'2017-04-05 22:24:00',1,'acc-gray','10.30.205.85',NULL,0,1,2048,NULL,0,NULL,0,'i-bp1e4qykj1h8j3n5ukyh','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(389,NULL,'2017-03-06 18:40:00',1,'picking-1','10.26.22.126','116.62.34.30',20,2,4096,NULL,0,NULL,0,'i-bp1bh6gtto7fmuxjt209','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(390,NULL,'2017-03-06 18:40:00',1,'picking-2','10.24.18.30','114.55.6.142',20,2,4096,NULL,0,NULL,0,'i-bp1bh6gtto7fmuxjt20a','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(391,NULL,'2017-03-06 00:52:00',1,'kaweb-1','10.24.30.141','114.55.2.6',5,2,4096,NULL,0,NULL,0,'i-bp1hxo8urkhqzd0e1u96','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(392,NULL,'2017-03-06 00:52:00',1,'kaweb-2','10.28.34.40','118.178.233.76',5,2,4096,NULL,0,NULL,0,'i-bp1hxo8urkhqzd0e1u97','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(393,NULL,'2017-02-24 18:32:00',1,'kaweb-gray','10.27.234.240','118.178.133.16',5,2,4096,NULL,0,NULL,0,'i-bp1fnxall7iz5q40erl3','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(394,NULL,'2017-02-22 02:39:00',1,'sdop-1','10.25.5.241','114.55.24.25',5,4,8192,NULL,0,NULL,0,'i-bp15txh3lz78s3nrtd2l','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(395,NULL,'2017-02-22 02:39:00',1,'sdop-2','10.27.229.230','118.178.135.237',5,4,8192,NULL,0,NULL,0,'i-bp15txh3lz78s3nrtd2k','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(396,NULL,'2017-02-21 02:54:00',1,'sdop-gray','10.27.2.36','118.178.129.104',5,2,4096,NULL,0,NULL,0,'i-bp1b3ogluykcr93g54oe','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(397,NULL,'2017-02-20 23:24:00',1,'shorturl-gray','10.47.90.235',NULL,0,1,2048,NULL,0,NULL,0,'i-bp1514uorf5bngnipmqq','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(398,NULL,'2017-02-20 00:16:00',1,'mysql-outway-slave1','10.29.195.32','101.37.35.54',100,8,16384,NULL,0,NULL,0,'i-bp1de0yjqvzblc2n6edj','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(399,NULL,'2017-02-19 21:41:00',1,'panama-outway','10.27.234.233',NULL,0,2,4096,NULL,0,NULL,0,'i-bp1iimhgd97y6uduc0n0','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(400,NULL,'2017-02-08 21:54:00',1,'mysql-outway-master','10.27.225.30','118.178.129.112',1,8,16384,NULL,0,NULL,0,'i-bp152fmmnefyfjze02z5','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(401,NULL,'2017-01-16 00:31:00',1,'reminder-2','10.171.174.128','120.26.164.138',1,1,2048,NULL,0,NULL,0,'i-bp1hnf4lbq2w2y4eeoqu','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:29',NULL),(402,NULL,'2016-12-27 22:08:00',1,'zsearch-console','10.132.28.92','121.199.2.252',1,4,8192,NULL,0,NULL,0,'i-bp10ienb9kpbfx9p7u8k','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:29',NULL),(403,NULL,'2016-12-27 22:02:00',1,'wrcenter-1','10.132.28.108','121.199.51.93',1,1,2048,NULL,0,NULL,0,'i-bp1d5x21qsokgmjkhj26','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:29',NULL),(404,NULL,'2016-12-27 21:59:00',1,'wrcenter-gray','10.132.28.69','121.199.54.192',1,1,2048,NULL,0,NULL,0,'i-bp11y9049u2admfx2ejs','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:29',NULL),(405,NULL,'2016-11-01 00:46:00',1,'crm-nodejs','10.27.238.181','118.178.240.164',10,2,4096,NULL,0,NULL,0,'i-bp12mil0sjdjtdlwc5d0','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(406,NULL,'2016-10-31 22:21:00',1,'cmdb','10.171.213.144','121.40.94.245',10,2,4096,NULL,0,NULL,0,'i-bp15j87h6jpk7i2aihxo','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:29',NULL),(407,NULL,'2016-10-28 00:10:00',1,'crm-nodejs-gray','10.27.237.56','118.178.234.195',0,1,2048,NULL,0,NULL,0,'i-bp1bm3cnb1oc7a48qsrp','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(408,NULL,'2016-10-24 22:17:00',1,'gc-index-detect','10.26.249.84','118.178.104.127',0,1,2048,NULL,0,NULL,0,'i-bp11bqhas1niqnapczka','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29','2018-07-26 00:59:57'),(409,NULL,'2016-10-19 22:54:00',1,'tradeoutway-gray','10.27.227.188','118.178.230.110',10,2,4096,NULL,0,NULL,0,'i-bp1gk78yejez4ibrq7jc','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(410,NULL,'2016-10-19 22:19:00',1,'kafka-3','10.27.224.70','118.178.230.156',100,4,8192,NULL,0,NULL,0,'i-bp13zda3caez8n33qubr','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(411,NULL,'2016-10-19 22:18:00',1,'kafka-2','10.27.224.187','118.178.230.113',100,4,8192,NULL,0,NULL,0,'i-bp1g7gx65a9d1rdste8e','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(412,NULL,'2016-10-19 22:18:00',1,'kafka-1','10.45.55.13','114.55.232.143',100,4,8192,NULL,0,NULL,0,'i-bp17f6dxxkvgsiwcl18e','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:29',NULL),(413,NULL,'2016-10-10 23:24:00',1,'recommend-2','10.27.12.130',NULL,0,2,4096,NULL,0,NULL,0,'i-bp1a6qokebfki8kgvy1h','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(414,NULL,'2016-10-10 23:24:00',1,'recommend-1','10.47.56.2',NULL,0,2,4096,NULL,0,NULL,0,'i-bp1a410j2fhxc9zibxuy','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(415,NULL,'2016-10-10 23:24:00',1,'recommend-gray','10.27.2.84',NULL,0,2,4096,NULL,0,NULL,0,'i-bp1a6qokebfki8kgvy1g','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(416,NULL,'2016-10-09 00:29:00',1,'workorder-1','10.27.0.157',NULL,0,2,4096,NULL,0,NULL,0,'i-bp1gd3xwhcctn3th3rkb','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(417,NULL,'2016-10-09 00:29:00',1,'workorder-2','10.27.11.179',NULL,0,2,4096,NULL,0,NULL,0,'i-bp1jdk51ejf6ulgoax22','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(418,NULL,'2016-09-27 00:38:00',1,'panama-market','10.26.20.133',NULL,0,2,4096,NULL,0,NULL,0,'i-23dfkjm5s','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(419,NULL,'2016-09-27 00:38:00',1,'panama-history','10.47.60.65',NULL,0,2,4096,NULL,0,NULL,0,'i-bp192ot9nf1qllclunhk','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(420,NULL,'2016-09-27 00:38:00',1,'panama-wms','10.26.251.199',NULL,0,2,4096,NULL,0,NULL,0,'i-bp192ot9nf1qllclunhj','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(421,NULL,'2016-09-26 00:33:00',1,'kafka-6','10.45.22.187','114.55.56.199',10,4,8192,NULL,0,NULL,0,'i-23f4o95oh','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(422,NULL,'2016-09-26 00:33:00',1,'kafka-5','10.26.251.140','118.178.104.18',10,4,8192,NULL,0,NULL,0,'i-bp1g3u73g9nwq1ou0qwh','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(423,NULL,'2016-09-26 00:33:00',1,'kafka-4','10.25.55.226','114.55.113.223',10,4,8192,NULL,0,NULL,0,'i-bp1fvttmu9vk063zm42d','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(424,NULL,'2016-09-25 22:40:00',1,'panama-o2o','10.24.35.183',NULL,0,2,4096,NULL,0,NULL,0,'i-bp1ib1msf1e5nn7lip2h','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(425,NULL,'2016-09-22 18:18:00',1,'workorder-gray','10.26.239.79','114.55.34.226',0,2,4096,NULL,0,NULL,0,'i-bp1a69rj03ub2docoe9x','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(426,NULL,'2016-09-19 17:40:00',1,'mysql-configcenter-slave1','10.26.202.77','118.178.86.142',5,4,8192,NULL,0,NULL,0,'i-bp18tearzi1nenc0saz7','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(427,NULL,'2016-09-19 17:38:00',1,'mysql-configcenter-master','10.25.8.101','114.55.128.253',5,4,8192,NULL,0,NULL,0,'i-bp18tearzi1nelczo8ze','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(428,NULL,'2016-09-07 18:14:00',1,'callcenter-1','10.47.60.126','114.55.1.47',20,2,4096,NULL,0,NULL,0,'i-230x8mjvl','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(429,NULL,'2016-09-04 19:26:00',1,'baohe-1','10.26.95.210','114.55.234.180',10,4,8192,NULL,0,NULL,0,'i-23gz096sl','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(430,NULL,'2016-08-29 18:46:00',1,'mysql-mha-manage-E','10.26.113.80','114.55.252.233',100,2,4096,NULL,0,NULL,0,'i-23ipzp67g','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(431,NULL,'2016-08-29 17:51:00',1,'mysql-midware-master','10.26.93.111','114.55.233.43',1,8,32768,NULL,0,NULL,0,'i-230cwoezf','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(432,NULL,'2016-08-25 18:14:00',1,'shopcart-1','10.45.32.62',NULL,0,2,4096,NULL,0,NULL,0,'i-2307gf6ao','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(433,NULL,'2016-08-25 18:14:00',1,'shopcart-2','10.24.236.35',NULL,0,2,4096,NULL,0,NULL,0,'i-234l6bp62','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(434,NULL,'2016-08-25 01:02:00',1,'shopcart-gray','10.26.92.54',NULL,0,2,4096,NULL,0,NULL,0,'i-23m6vic8g','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(435,NULL,'2016-08-15 01:17:00',1,'redis-market-slave1','10.172.27.71',NULL,0,4,16384,NULL,0,NULL,0,'i-23rwaczru','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(436,NULL,'2016-08-14 18:57:00',1,'gc-mysql-log-master','10.47.77.147','114.55.178.138',10,8,16384,NULL,0,NULL,0,'i-23fpimcfm','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30','2018-08-23 01:25:30'),(437,NULL,'2016-08-03 18:44:00',1,'tradeoutway-2','10.25.65.38','114.55.219.92',10,4,8192,NULL,0,NULL,0,'i-233z4ww5j','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(438,NULL,'2016-08-03 18:44:00',1,'tradeoutway-1','10.25.54.226','114.55.130.154',10,4,8192,NULL,0,NULL,0,'i-23h2z18hq','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(439,NULL,'2016-08-03 17:49:00',1,'mysql-midware-slave1','10.172.26.50','120.27.244.45',100,8,16384,NULL,0,NULL,0,'i-23bs3sr6o','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(440,NULL,'2016-08-02 19:56:00',1,'fms-1','10.45.35.219',NULL,0,1,2048,NULL,0,NULL,0,'i-23ff85i0d','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(441,NULL,'2016-08-02 19:56:00',1,'fms-gray','10.47.56.62',NULL,0,1,2048,NULL,0,NULL,0,'i-23uob7jaa','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(442,NULL,'2016-07-25 19:08:00',1,'mysql-o2o-slave2','10.24.238.50','114.55.218.51',100,8,16384,NULL,0,NULL,0,'i-23m4lm6r5','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(443,NULL,'2016-07-24 22:54:00',1,'bigmouth-1','10.26.48.39','121.196.232.20',100,2,4096,NULL,0,NULL,0,'i-23l0shix9','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(444,NULL,'2016-07-24 22:54:00',1,'bigmouth-2','10.172.203.136','114.55.106.248',100,2,4096,NULL,0,NULL,0,'i-23775suny','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(445,NULL,'2016-07-19 01:32:00',1,'mysql-history-slave1','10.25.173.51','114.55.175.2',100,8,16384,NULL,0,NULL,0,'i-23gqb9ud2','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(446,NULL,'2016-07-17 21:35:00',1,'proxy1.zabbix3.51xianqu.net','10.172.24.221','114.55.147.173',100,4,8192,NULL,0,NULL,0,'i-232m2tdmf','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(447,NULL,'2016-07-06 01:13:00',1,'dnsmasq-2','10.24.239.232','114.55.97.106',10,1,1024,NULL,0,NULL,0,'i-239r4aas7','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(448,NULL,'2016-07-05 01:06:00',1,'redis-lbs-slave1','10.25.254.30',NULL,0,2,16384,NULL,0,NULL,0,'i-23w6okkis','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(449,NULL,'2016-06-29 18:40:00',1,'mysql-history-master','10.25.253.215','114.55.145.183',10,8,16384,NULL,0,NULL,0,'i-23qt9sjc5','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(450,NULL,'2016-06-22 19:36:00',1,'ssh.51xianqu.net','10.24.254.178','114.55.150.94',10,2,4096,NULL,0,NULL,0,'i-23ixkazmw','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(451,NULL,'2016-06-21 23:01:00',1,'redis-lbs-master','10.25.171.36',NULL,0,2,16384,NULL,0,NULL,0,'i-233zbc5rr','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(452,NULL,'2016-05-31 23:31:00',1,'mysql-stats-slave1','10.25.66.112','114.55.138.159',1,8,16384,NULL,0,NULL,0,'i-237g55q3n','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(453,NULL,'2016-05-31 02:07:00',1,'pushCheck-1','10.24.243.72','114.55.103.78',20,4,8192,NULL,0,NULL,0,'i-23zenhc0g','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(454,NULL,'2016-05-31 02:03:00',1,'tradeback-1','10.25.85.139','114.55.91.70',20,4,8192,NULL,0,NULL,0,'i-23lex4ear','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(455,NULL,'2016-05-29 22:33:00',1,'warninglog-1','10.160.45.107','112.124.44.234',10,1,2048,NULL,0,NULL,0,'i-23th8z0hb','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(456,NULL,'2016-05-10 00:39:00',1,'poscrm-1','10.160.37.3','112.124.38.112',10,1,2048,NULL,0,NULL,0,'i-23uwpxfhl','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(457,NULL,'2016-05-02 23:14:00',1,'gray.52shangou.com-1','10.132.45.206','121.199.77.167',10,2,2048,NULL,0,NULL,0,'i-23qlgllxp','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(458,NULL,'2016-04-28 22:29:00',1,'outway-gray','10.117.177.39','121.41.92.226',10,1,2048,NULL,0,NULL,0,'i-23qt6674i','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(459,NULL,'2016-04-28 22:20:00',1,'tmq-admin-1','10.24.252.100',NULL,0,2,4096,NULL,0,NULL,0,'i-23d9sl2dj','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(460,NULL,'2016-04-28 22:18:00',1,'tmq-admin-gray','10.24.252.91',NULL,0,2,4096,NULL,0,NULL,0,'i-23cqn454h','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(461,NULL,'2016-04-26 07:18:00',1,'redis-trade-master','10.168.68.160','121.40.193.16',1,4,16384,NULL,0,NULL,0,'i-233q0jmdf','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(462,NULL,'2016-04-20 00:42:00',1,'redis-trade-slave1','10.24.236.167','114.55.92.170',1,2,16384,NULL,0,NULL,0,'i-237es6316','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(463,NULL,'2016-04-18 22:25:00',1,'shorturl-1','10.132.24.66',NULL,0,2,4096,NULL,0,NULL,0,'i-23zjm434y','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(464,NULL,'2016-04-13 19:04:00',1,'search-service-2','10.117.207.180',NULL,0,2,4096,NULL,0,NULL,0,'i-23r2bcq9e','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(465,NULL,'2016-04-13 19:04:00',1,'search-service-gray','10.132.80.243','121.199.32.241',0,2,4096,NULL,0,NULL,0,'i-2336vjih5','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(466,NULL,'2016-04-13 19:04:00',1,'search-service-1','10.132.81.92',NULL,0,2,4096,NULL,0,NULL,0,'i-237w2wv95','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(467,NULL,'2016-04-07 00:51:00',1,'mysql-stats-master','10.24.39.51','114.55.59.125',100,8,16384,NULL,0,NULL,0,'i-23z1v183w','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(468,NULL,'2016-03-30 00:12:00',1,'wms-1','10.117.206.167','120.55.91.213',1,1,2048,NULL,0,NULL,0,'i-23kqltqyn','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(469,NULL,'2016-03-30 00:12:00',1,'wmsOrderCenter-1','10.117.58.205','120.26.216.200',1,1,2048,NULL,0,NULL,0,'i-237v0hfhd','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(470,NULL,'2016-03-30 00:11:00',1,'wmsOrderCenter-2','10.132.53.202','121.199.25.14',1,1,2048,NULL,0,NULL,0,'i-2398xlkdw','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(471,NULL,'2016-03-28 22:33:00',1,'zsearch-solr-engine-1','10.132.21.190','121.199.3.28',10,8,16384,NULL,0,NULL,0,'i-23df00jqe','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(472,NULL,'2016-03-28 22:33:00',1,'zsearch-solr-engine-2','10.132.53.15','121.199.58.194',10,8,16384,NULL,0,NULL,0,'i-2346gb397','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(473,NULL,'2016-03-28 22:27:00',1,'iop-gray','10.117.206.225','121.40.53.78',1,1,2048,NULL,0,NULL,0,'i-23totvvc9','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(474,NULL,'2016-03-28 22:23:00',1,'itemcore-gray','10.132.53.9','121.199.77.147',0,1,2048,NULL,0,NULL,0,'i-23c65thk8','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(475,NULL,'2016-03-28 00:40:00',1,'wmsOrderCenter-gray','10.132.47.71','121.199.25.63',1,1,2048,NULL,0,NULL,0,'i-233ydesxg','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(476,NULL,'2016-03-15 17:49:00',1,'stockcenter','10.47.65.193',NULL,0,2,4096,NULL,0,NULL,0,'i-23vnj40fx','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(477,NULL,'2016-02-25 23:24:00',1,'reminder-1','10.51.9.88','120.26.103.77',1,1,2048,NULL,0,NULL,0,'i-23853nsy0','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(478,NULL,'2016-02-23 23:30:00',1,'gc-dw','10.117.188.22',NULL,0,1,2048,NULL,0,NULL,0,'i-23a60d7pb','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30','2018-07-26 00:59:57'),(479,NULL,'2016-02-23 22:32:00',1,'logcollection','10.117.67.81',NULL,0,2,4096,NULL,0,NULL,0,'i-23kp0o8so','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(480,NULL,'2016-02-21 22:23:00',1,'mysql-o2o-master','10.45.23.189','114.55.6.78',1,16,32768,NULL,0,NULL,0,'i-23oh4d07w','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:30',NULL),(481,NULL,'2016-02-21 18:45:00',1,'sdb-gray','10.117.198.189',NULL,0,1,2048,NULL,0,NULL,0,'i-23c0cue4u','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(482,NULL,'2016-02-21 18:45:00',1,'sdb','10.162.55.83',NULL,0,1,2048,NULL,0,NULL,0,'i-23538jgdv','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:30',NULL),(483,NULL,'2016-02-14 01:35:00',1,'mysql-poi-slave3','10.45.32.245','114.55.4.43',100,8,16384,NULL,0,NULL,0,'i-23rija9rp','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:31',NULL),(484,NULL,'2016-01-19 18:08:00',1,'version-gray','10.168.4.64',NULL,0,1,2048,NULL,0,NULL,0,'i-23zsvaq3p','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(485,NULL,'2016-01-19 18:08:00',1,'version','10.161.237.17',NULL,0,1,2048,NULL,0,NULL,0,'i-232yl0e7j','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(486,NULL,'2016-01-11 23:47:00',1,'rate-gray','10.117.200.5','121.40.28.151',1,1,2048,NULL,0,NULL,0,'i-23ca2kegr','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(487,NULL,'2016-01-11 23:47:00',1,'rate','10.117.200.214',NULL,0,1,2048,NULL,0,NULL,0,'i-23k2ilepi','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(488,NULL,'2016-01-11 23:47:00',1,'rate','10.117.17.25',NULL,0,1,2048,NULL,0,NULL,0,'i-232im48i1','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(489,NULL,'2016-01-04 19:29:00',1,'sdw','10.117.208.58',NULL,0,1,2048,NULL,0,NULL,0,'i-23s3fgckr','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(490,NULL,'2016-01-04 19:21:00',1,'sdw-gray','10.117.208.156','120.26.77.127',1,1,2048,NULL,0,NULL,0,'i-23xgjd9uv','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(491,NULL,'2016-01-04 19:21:00',1,'sdw','10.117.50.198',NULL,0,1,2048,NULL,0,NULL,0,'i-23qowxqsi','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(492,NULL,'2015-12-21 19:24:00',1,'pay-gray','10.168.1.213','121.40.123.28',1,2,4096,NULL,0,NULL,0,'i-23ziqzty9','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(493,NULL,'2015-12-21 19:24:00',1,'pay','10.168.106.214','121.40.86.178',1,2,4096,NULL,0,NULL,0,'i-23lodu9hq','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(494,NULL,'2015-12-21 19:24:00',1,'pay','10.168.106.213','121.40.70.73',1,2,4096,NULL,0,NULL,0,'i-23efmvnwv','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(495,NULL,'2015-12-21 02:57:00',1,'mysql-centralbank-master','10.47.97.167',NULL,0,8,16384,NULL,0,NULL,0,'i-234105l1f','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:31',NULL),(496,NULL,'2015-12-20 19:19:00',1,'logistics-backstage','10.132.0.211','121.199.8.16',5,2,4096,NULL,0,NULL,0,'i-23xevaqab','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(497,NULL,'2015-12-17 05:30:00',1,'blj-db','10.174.176.169','120.55.187.170',100,1,1024,NULL,0,NULL,0,'i-23iz28bqo','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(498,NULL,'2015-12-16 22:58:00',1,'timeoutcenter','10.174.176.110',NULL,0,1,2048,NULL,0,NULL,0,'i-23rtc9m25','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(499,NULL,'2015-12-13 17:57:00',1,'mysql-o2o-slave3','10.47.93.122',NULL,0,8,16384,NULL,0,NULL,0,'i-23hk0ypvw','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:31',NULL),(500,NULL,'2015-12-11 00:21:00',1,'org','10.117.61.214',NULL,0,1,2048,NULL,0,NULL,0,'i-23m59mi7f','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(501,NULL,'2015-12-11 00:21:00',1,'org','10.117.61.216',NULL,0,1,2048,NULL,0,NULL,0,'i-23k15g6ue','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(502,NULL,'2015-12-08 17:35:00',1,'payservice-alipay','10.117.60.170','120.26.220.16',10,2,4096,NULL,0,NULL,0,'i-23gwncoh3','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(503,NULL,'2015-12-08 17:35:00',1,'payservice-alipay','10.117.15.201','120.55.184.98',10,2,4096,NULL,0,NULL,0,'i-23w4wyctl','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(504,NULL,'2015-12-08 17:35:00',1,'payservice-wxpay','10.117.15.203','120.55.184.118',10,2,4096,NULL,0,NULL,0,'i-23m7e12xz','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(505,NULL,'2015-12-08 17:35:00',1,'payservice-wxpay','10.168.96.105','121.41.2.56',10,2,4096,NULL,0,NULL,0,'i-233dridwr','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(506,NULL,'2015-12-07 18:24:00',1,'payservice-alipay-gray','10.168.93.198','121.41.2.44',1,2,4096,NULL,0,NULL,0,'i-23t0jx49z','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(507,NULL,'2015-12-07 18:24:00',1,'payservice-wxpay-gray','10.117.213.208','120.55.64.202',1,2,4096,NULL,0,NULL,0,'i-23f9qtgnl','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(508,NULL,'2015-12-07 01:02:00',1,'logistics-backstage-gray','10.161.210.239','114.215.191.210',1,2,4096,NULL,0,NULL,0,'i-23of55fj7','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(509,NULL,'2015-12-03 22:31:00',1,'gc-mysql-backup-center','10.47.89.235','120.27.133.179',100,1,1024,NULL,0,NULL,0,'i-23zzv3ebh','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:31','2018-11-19 00:56:02'),(510,NULL,'2015-12-01 22:28:00',1,'org-gray','10.117.209.197','120.26.38.89',10,1,2048,NULL,0,NULL,0,'i-23l1e9c83','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(511,NULL,'2015-11-25 17:48:00',1,'procurement-gray','10.117.3.182',NULL,0,1,2048,NULL,0,NULL,0,'i-233fftdsw','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(512,NULL,'2015-11-24 20:11:00',1,'shopback-gray','10.168.145.118','121.41.1.130',5,1,2048,NULL,0,NULL,0,'i-23rv08jgf','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(513,NULL,'2015-11-23 23:41:00',1,'timeoutcenter-gray','10.168.142.104','121.40.248.52',1,1,2048,NULL,0,NULL,0,'i-23gng73vb','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(514,NULL,'2015-11-22 23:18:00',1,'baohe-gray','10.51.37.69','120.26.92.197',10,2,4096,NULL,0,NULL,0,'i-23rcmj3x4','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(515,NULL,'2015-11-09 17:11:00',1,'stockcenter','10.252.113.2','120.26.56.67',1,1,2048,NULL,0,NULL,0,'i-23cv6tgm9','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(516,NULL,'2015-11-09 17:11:00',1,'stockcenter-gray','10.165.6.65','114.215.185.116',1,1,2048,NULL,0,NULL,0,'i-23gpc0c9v','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(517,NULL,'2015-11-08 22:32:00',1,'jobcenter-gray','10.171.224.209','121.40.104.59',5,2,4096,NULL,0,NULL,0,'i-23nlj1cfd','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(518,NULL,'2015-11-08 22:32:00',1,'jobcenter-1','10.168.45.4','121.40.135.199',5,2,4096,NULL,0,NULL,0,'i-23zvlpg1j','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(519,NULL,'2015-11-06 00:24:00',1,'crm-gray','10.252.175.17','120.55.92.183',1,1,2048,NULL,0,NULL,0,'i-23z0qhgg1','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(520,NULL,'2015-11-06 00:20:00',1,'zookeeper-gray','10.168.114.172','121.40.80.98',0,2,4096,NULL,0,NULL,0,'i-23n4oq3pm','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(521,NULL,'2015-11-05 19:15:00',1,'zookeeper-2','10.168.143.49',NULL,0,2,4096,NULL,0,NULL,0,'i-23b3ic7j1','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(522,NULL,'2015-11-05 19:15:00',1,'dubbo-monitor','10.252.90.247','120.26.107.158',0,1,4096,NULL,0,NULL,0,'i-23h72b4hb','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(523,NULL,'2015-11-05 19:15:00',1,'zookeeper-3','10.168.219.185',NULL,0,2,4096,NULL,0,NULL,0,'i-23527s4uu','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(524,NULL,'2015-11-05 07:17:00',1,'mysql-poi-master','10.47.49.105',NULL,0,16,32768,NULL,0,NULL,0,'i-233nrpu15','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-06-28 01:22:31',NULL),(525,NULL,'2015-11-04 19:54:00',1,'shopback','10.168.114.228','121.40.71.16',5,1,2048,NULL,0,NULL,0,'i-23h06e8d5','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(526,NULL,'2015-11-04 19:54:00',1,'ecs-hadoop-1','10.252.151.235','120.55.93.103',10,8,16384,NULL,0,NULL,0,'i-23xb4k0iz','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(527,NULL,'2015-11-04 19:49:00',1,'crm','10.162.77.55',NULL,0,2,4096,NULL,0,NULL,0,'i-23hxbr4tb','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(528,NULL,'2015-11-04 19:49:00',1,'tracker','10.162.97.122',NULL,0,1,2048,NULL,0,NULL,0,'i-238to35mx','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(529,NULL,'2015-11-04 19:49:00',1,'timeoutcenter','10.51.28.167',NULL,0,1,2048,NULL,0,NULL,0,'i-23gzoghie','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(530,NULL,'2015-11-02 05:19:00',1,'redis-market-master','10.252.175.216',NULL,0,4,16384,NULL,0,NULL,0,'i-23z3fy5e0','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(531,NULL,'2015-10-27 19:24:00',1,'gc-redis-member-master','10.252.131.204',NULL,0,4,16384,NULL,0,NULL,0,'i-233gtj9oq','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31','2018-08-08 01:22:48'),(532,NULL,'2015-10-27 01:17:00',1,'centralbank','10.162.100.89','114.215.169.11',10,2,4096,NULL,0,NULL,0,'i-23zel2rfv','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(533,NULL,'2015-10-27 01:17:00',1,'centralbank','10.251.247.55','120.55.90.11',10,2,4096,NULL,0,NULL,0,'i-23a0bwfzo','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(534,NULL,'2015-10-27 00:05:00',1,'auc-gray','10.161.226.132',NULL,0,1,2048,NULL,0,NULL,0,'i-23mlalxmg','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(535,NULL,'2015-10-27 00:05:00',1,'auc','10.252.83.126',NULL,0,1,2048,NULL,0,NULL,0,'i-23kw10cdp','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(536,NULL,'2015-10-27 00:05:00',1,'auc','10.161.221.131',NULL,0,1,2048,NULL,0,NULL,0,'i-232bozugk','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(537,NULL,'2015-10-25 22:07:00',1,'centralbank-task','10.162.99.20',NULL,0,2,4096,NULL,0,NULL,0,'i-232fqarnf','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(538,NULL,'2015-10-19 23:14:00',1,'procurement','10.162.78.138',NULL,0,2,4096,NULL,0,NULL,0,'i-23ltcwbmg','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(539,NULL,'2015-10-15 19:04:00',1,'centralbank-gray','10.161.208.102','114.215.209.61',1,2,4096,NULL,0,NULL,0,'i-23dhkqkcg','cn-hangzhou-c',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(540,NULL,'2015-10-14 00:58:00',1,'www.52shangou.com-2','10.252.119.38','120.26.77.36',100,4,8192,NULL,0,NULL,0,'i-235d115u3','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(541,NULL,'2015-10-09 19:16:00',1,'dnsmasq-1','10.117.179.184','121.41.16.189',10,1,1024,NULL,0,NULL,0,'i-23mqb9tqx','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(542,NULL,'2015-09-23 01:38:00',1,'manage.51xianqu.com','10.168.90.126','121.40.254.147',100,4,4096,NULL,0,NULL,0,'i-23c9lfz40','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(543,NULL,'2015-09-22 00:00:00',1,'shop','10.160.85.149','112.124.33.38',1,4,8192,NULL,0,NULL,0,'i-232fmjkpg','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(544,NULL,'2015-09-22 00:00:00',1,'shop','10.160.4.28','112.124.0.45',1,4,8192,NULL,0,NULL,0,'i-23wh97p5x','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(545,NULL,'2015-09-21 17:07:00',1,'shop-gray','10.168.27.5','121.40.159.68',1,2,4096,NULL,0,NULL,0,'i-23jo7bajq','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(546,NULL,'2015-09-14 18:10:00',1,'detect-mysql','10.160.13.72','112.124.0.59',10,4,8192,NULL,0,NULL,0,'i-235qp1b2i','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(547,NULL,'2015-09-14 18:04:00',1,'gc-redis-log-master','10.117.2.132','120.55.183.39',5,2,8192,NULL,0,NULL,0,'i-23v1a0cr7','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31','2018-08-08 01:22:48'),(548,NULL,'2015-09-08 17:46:00',1,'dns-slave','10.168.86.206','121.40.255.10',10,1,1024,NULL,0,NULL,0,'i-231c4pxg4','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(549,NULL,'2015-09-07 18:43:00',1,'file.51xianqu.net','10.51.35.20','120.26.136.168',100,1,1024,NULL,0,NULL,0,'i-237xt1wub','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(550,NULL,'2015-09-07 00:49:00',1,'dns-master','10.51.20.221','120.26.136.188',10,1,1024,NULL,0,NULL,0,'i-23fmrmgym','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(551,NULL,'2015-09-06 17:13:00',1,'zsearch-controlcenter','10.252.253.73','120.26.47.237',10,4,8192,NULL,0,NULL,0,'i-23gaa01nd','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(552,NULL,'2015-09-06 17:13:00',1,'ecs-hadoop-2','10.168.143.100','121.40.179.237',10,8,16384,NULL,0,NULL,0,'i-23tl0735y','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(553,NULL,'2015-09-01 19:24:00',1,'market-back','10.168.138.28','121.40.249.136',10,4,8192,NULL,0,NULL,0,'i-23v6vsipg','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(554,NULL,'2015-09-01 19:23:00',1,'delivery-back','10.252.120.112','120.26.70.220',10,4,8192,NULL,0,NULL,0,'i-23tthzhkt','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(555,NULL,'2015-08-31 23:47:00',1,'config-center','10.171.241.33',NULL,0,2,4096,NULL,0,NULL,0,'i-235whzytn','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(556,NULL,'2015-08-27 22:13:00',1,'warninglog-gray','10.117.24.104','121.43.116.64',10,1,2048,NULL,0,NULL,0,'i-23zujtv20','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(557,NULL,'2015-08-26 03:28:00',1,'tmq-gray','10.168.106.63',NULL,0,2,4096,NULL,0,NULL,0,'i-23lm55wrw','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(558,NULL,'2015-08-24 22:32:00',1,'getway','10.168.104.44','121.40.226.205',10,2,2048,NULL,0,NULL,0,'i-23cmb7ril','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(559,NULL,'2015-08-23 23:40:00',1,'member-1','10.117.42.91','121.43.61.67',1,4,8192,NULL,0,NULL,0,'i-2310pungt','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(560,NULL,'2015-08-23 23:40:00',1,'member-2','10.251.236.71','120.55.80.198',5,4,8192,NULL,0,NULL,0,'i-23qr9bgn3','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:31',NULL),(561,NULL,'2015-08-19 18:58:00',1,'member-gray','10.251.245.51','120.55.80.119',1,2,4096,NULL,0,NULL,0,'i-23ardmgkb','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(562,NULL,'2015-08-16 20:56:00',1,'getway2.51xianqu.net','10.175.201.227','120.26.40.90',10,2,4096,NULL,0,NULL,0,'i-23qyhcw0k','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(563,NULL,'2015-08-16 19:03:00',1,'delivery-gray','10.251.234.109','121.43.233.138',1,2,4096,NULL,0,NULL,0,'i-233el5bl5','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(564,NULL,'2015-08-16 18:30:00',1,'redis-item-master','10.117.38.34',NULL,0,2,16384,NULL,0,NULL,0,'i-23longsm3','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(565,NULL,'2015-08-12 04:35:00',1,'dataapi-gray','10.252.114.66',NULL,0,2,4096,NULL,0,NULL,0,'i-23udoyb8d','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(566,NULL,'2015-08-11 20:01:00',1,'logistics-1','10.168.90.38','121.40.173.34',1,2,4096,NULL,0,NULL,0,'i-23x11iz1g','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(567,NULL,'2015-08-11 00:43:00',1,'dataapi-2','10.173.139.224',NULL,0,2,4096,NULL,0,NULL,0,'i-23mluur72','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(568,NULL,'2015-08-11 00:42:00',1,'dataapi-1','10.173.141.7',NULL,0,2,4096,NULL,0,NULL,0,'i-23o0kjgx2','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(569,NULL,'2015-08-10 18:28:00',1,'trade-2','10.252.251.186','120.26.58.110',1,4,8192,NULL,0,NULL,0,'i-23xfvxjct','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(570,NULL,'2015-08-10 18:27:00',1,'trade-3','10.168.57.209','121.40.255.84',1,4,8192,NULL,0,NULL,0,'i-23mpa7tbe','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(571,NULL,'2015-08-03 16:50:00',1,'msgcenter-2','10.117.43.240','121.43.117.233',1,2,4096,NULL,0,NULL,0,'i-23i0ons95','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(572,NULL,'2015-07-30 23:58:00',1,'trade-gray','10.252.80.182','120.26.141.186',1,2,4096,NULL,0,NULL,0,'i-23b8ntcqv','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(573,NULL,'2015-07-30 23:52:00',1,'logistics-gray','10.252.126.68','120.26.42.138',1,2,4096,NULL,0,NULL,0,'i-230qmgx8s','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(574,NULL,'2015-07-29 17:40:00',1,'trade-4','10.168.122.58','121.40.126.142',10,4,8192,NULL,0,NULL,0,'i-231ltkf4q','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(575,NULL,'2015-07-29 17:39:00',1,'trade-1','10.252.94.232','120.26.141.17',10,4,8192,NULL,0,NULL,0,'i-23qbmld7y','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(576,NULL,'2015-07-27 02:01:00',1,'itemcenter-gray','10.169.13.201','121.43.147.171',1,2,4096,NULL,0,NULL,0,'i-237e9rvb5','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(577,NULL,'2015-07-26 17:11:00',1,'config-center','10.117.45.53',NULL,0,2,4096,NULL,0,NULL,0,'i-23k3xitmz','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(578,NULL,'2015-07-20 03:38:00',1,'aliyun.deploy.51xianqu.net','10.173.138.25','121.41.86.96',100,1,1024,NULL,0,NULL,0,'i-23g5gl1es','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(579,NULL,'2015-07-11 21:38:00',1,'gitlab.51xianqu.com','10.168.22.195','121.40.165.113',20,2,4096,NULL,0,NULL,0,'i-236udw2jk','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(580,NULL,'2015-07-11 01:30:00',1,'market-gray','10.171.202.93','121.40.107.152',10,2,4096,NULL,0,NULL,0,'i-23dkzu18w','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(581,NULL,'2015-07-06 03:59:00',1,'store-proxy','10.117.111.48','120.26.216.26',10,4,8192,NULL,0,NULL,0,'i-23cn235gv','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(582,NULL,'2015-07-03 01:42:00',1,'api-1','10.252.119.214','120.26.51.226',1,2,4096,NULL,0,NULL,0,'i-23kuwy8ny','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(583,NULL,'2015-06-28 17:36:00',1,'zookeeper-1','10.117.39.136','121.43.122.89',0,2,4096,NULL,0,NULL,0,'i-231bs0l7d','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(584,NULL,'2015-06-22 22:26:00',1,'ldap.51xianqu.net','10.252.82.210','120.26.124.4',10,2,4096,NULL,0,NULL,0,'i-23s7g2kr6','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(585,NULL,'2015-06-18 04:57:00',1,'logistics-2','10.252.162.69','121.43.234.36',1,2,4096,NULL,0,NULL,0,'i-23hie6ysr','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(586,NULL,'2015-06-18 04:57:00',1,'market-2','10.168.76.95','121.40.214.116',1,4,8192,NULL,0,NULL,0,'i-23iqbf1r9','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(587,NULL,'2015-06-17 00:06:00',1,'reminder-gray','10.168.255.56','121.41.78.168',10,1,2048,NULL,0,NULL,0,'i-239rfw26j','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(588,NULL,'2015-06-17 00:06:00',1,'crm','10.252.163.83','121.43.150.57',10,2,4096,NULL,0,NULL,0,'i-23n2oduv7','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(589,NULL,'2015-06-15 23:11:00',1,'api-gray','10.51.2.199','120.26.124.90',10,2,4096,NULL,0,NULL,0,'i-23ocbmnxq','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(590,NULL,'2015-06-15 23:11:00',1,'api-2','10.251.234.225','121.43.149.24',1,2,4096,NULL,0,NULL,0,'i-238qzz480','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(591,NULL,'2015-06-15 19:52:00',1,'market-1','10.175.205.86','120.26.67.57',1,4,8192,NULL,0,NULL,0,'i-23b9w8ji3','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(592,NULL,'2015-06-01 20:03:00',1,'msgcenter-1','10.171.238.8','121.40.116.94',1,2,4096,NULL,0,NULL,0,'i-23imbvn9d','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(593,NULL,'2015-05-26 02:35:00',1,'zsearch-solr-engine-4','10.251.238.209','121.43.229.173',10,8,16384,'cloud',40,'cloud',100,'i-236wnm8tu','cn-hangzhou-d',0,'cn-hangzhou',9,0,'PrePaid',1,'2018-06-28 01:22:32','2018-07-18 02:51:25'),(594,NULL,'2015-05-26 02:35:00',1,'zsearch-solr-engine-3','10.251.240.232','121.43.229.246',10,8,16384,NULL,0,NULL,0,'i-23lj4bhdg','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(595,NULL,'2015-05-25 22:10:00',1,'gc-redis-o2o-master','10.165.100.113','120.26.62.111',0,4,16384,NULL,0,NULL,0,'i-239fr1t7t','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32','2018-08-10 01:18:30'),(596,NULL,'2015-05-21 23:45:00',1,'panama-zk','10.171.229.204','121.40.116.67',1,2,8192,NULL,0,NULL,0,'i-23amtzhyw','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(597,NULL,'2015-05-21 17:08:00',1,'delivery','10.252.144.3','121.43.230.207',1,4,8192,NULL,0,NULL,0,'i-23ydd8dte','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(598,NULL,'2015-05-21 17:08:00',1,'delivery','10.251.251.137','121.43.230.209',1,4,8192,NULL,0,NULL,0,'i-235xzpny3','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(599,NULL,'2015-05-11 01:15:00',1,'ecs-hadoop-3','10.175.206.143','120.26.52.9',1,8,16384,NULL,0,NULL,0,'i-23lgi82al','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(600,NULL,'2015-04-24 06:32:00',1,'config-center-gray','10.168.243.64','121.43.146.243',1,1,2048,NULL,0,NULL,0,'i-238zkbns5','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(601,NULL,'2015-04-24 06:18:00',1,'shop','10.252.95.95','120.26.108.252',1,4,8192,NULL,0,NULL,0,'i-23r88rqz2','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(602,NULL,'2015-04-17 03:26:00',1,'tmq-1','10.51.2.235','120.26.122.189',1,4,8192,NULL,0,NULL,0,'i-2337k1849','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(603,NULL,'2015-04-17 03:26:00',1,'tmq-2','10.51.7.53','120.26.122.183',1,4,8192,NULL,0,NULL,0,'i-23dgf3ben','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(604,NULL,'2015-04-10 23:19:00',1,'gray.52shangou.com-2','10.252.81.198','120.26.101.158',1,1,2048,NULL,0,NULL,0,'i-23dkc44ky','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(605,NULL,'2015-04-09 21:30:00',1,'msgcenter-gray','10.168.217.161','121.41.79.62',1,1,4096,NULL,0,NULL,0,'i-2336hzqto','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(606,NULL,'2015-04-05 04:20:00',1,'publish_manager','10.252.97.33','120.55.100.233',10,4,4096,NULL,0,NULL,0,'i-23yrpe2m9','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(607,NULL,'2015-02-10 21:04:00',1,'ecs-hadoop-4','10.171.210.31','121.40.77.236',1,8,16384,NULL,0,NULL,0,'i-238sf8u1l','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(608,NULL,'2014-12-16 23:48:00',1,'baohe-2','10.251.235.144','121.40.17.114',1,4,8192,NULL,0,NULL,0,'i-23dhgxo7v','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(609,NULL,'2014-10-28 22:11:00',1,'daily.52shangou.com','10.168.177.246','121.41.36.124',10,1,2048,NULL,0,NULL,0,'i-23regwrzj','cn-hangzhou-d',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(610,NULL,'2014-09-14 23:40:00',1,'www.52shangou.com-1','10.132.54.171','121.199.39.142',100,4,8192,NULL,0,NULL,0,'i-23zcp1vq0','cn-hangzhou-b',0,'cn-hangzhou',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(611,NULL,'2016-05-04 22:34:00',1,'hk01.ss','172.16.97.172','47.90.4.125',50,1,2048,NULL,0,NULL,0,'i-62miiwdnr','cn-hongkong-b',0,'cn-hongkong',0,1,'PrePaid',0,'2018-06-28 01:22:32',NULL),(612,NULL,'2015-08-28 01:09:00',1,'us01.ss.51xianqu.net','172.16.8.65','47.88.20.189',10,1,1024,NULL,0,NULL,0,'i-u161slyoz','us-west-1a',0,'us-west-1',0,0,'PrePaid',0,'2018-06-28 01:22:32',NULL),(613,NULL,'2018-07-15 23:44:00',1,'test2','172.17.0.236','121.196.198.129',10,1,1024,'cloud_efficiency',40,'cloud_efficiency',100,'i-bp16lhh4wo6rt3db54lm','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',1,'2018-07-16 07:44:06',NULL),(614,NULL,'2018-07-08 00:44:00',1,'Emr-gateway-C-2212C65F246B0187','172.18.33.28','47.96.179.19',0,4,8192,NULL,0,NULL,0,'i-bp18qlevqaoge7dcgvpg','cn-hangzhou-b',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-07-17 01:48:46','2018-07-19 00:59:28'),(615,NULL,'2018-08-06 17:52:00',1,'finereport','10.29.148.111','118.178.120.154',10,4,8192,NULL,0,NULL,0,'i-bp1a3rsiacm8ngwpnsc8','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-08-08 01:22:45',NULL),(616,NULL,'2018-09-12 18:28:00',1,'crm-gray','10.27.10.10','114.55.139.53',10,2,4096,NULL,0,NULL,0,'i-bp19q2uy357mfmpso9ph','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-09-14 01:03:28',NULL),(617,NULL,'2018-09-16 19:08:00',1,'procurement','10.25.252.34',NULL,10,2,4096,NULL,0,NULL,0,'i-bp1d4o4h12coe97kk7na','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-10-09 01:35:20',NULL),(618,NULL,'2017-05-24 16:55:00',1,'kamember','10.29.195.88','101.37.71.121',10,2,4096,NULL,0,NULL,0,'i-bp1iba2yyjy5mg00ngrx','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-10-09 01:35:21',NULL),(619,NULL,'2018-10-23 23:22:00',1,'gitlab-test','10.29.188.191','116.62.110.137',5,2,4096,NULL,0,NULL,0,'i-bp130qqhwn3avmrn8369','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-10-25 01:02:00',NULL),(620,NULL,'2018-10-10 22:15:00',1,'ctss','10.26.255.127','118.178.190.25',10,1,2048,NULL,0,NULL,0,'i-bp1c8l3lmbvt4traq45h','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-10-25 01:02:00',NULL),(621,NULL,'2018-11-08 00:34:00',1,'es-data','10.26.113.198','114.55.140.119',10,2,4096,NULL,0,NULL,0,'i-bp17rz0layh1ujp8n0v2','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-11-13 08:08:46',NULL),(622,NULL,'2018-11-08 00:34:00',1,'es-data','10.27.90.193','116.62.6.107',10,2,4096,NULL,0,NULL,0,'i-bp1ad1jbc6fvgpzly9xv','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-11-13 08:08:46',NULL),(623,NULL,'2018-11-08 00:33:00',1,'es-data-gray','10.25.61.50','116.62.45.230',10,2,4096,NULL,0,NULL,0,'i-bp1ad1jbc6fvgo0ku7yp','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-11-13 08:08:46',NULL),(624,NULL,'2018-11-08 00:17:00',1,'trade','10.51.238.116','47.110.160.96',10,4,8192,NULL,0,NULL,0,'i-bp13g1c5ttsn2t6lqhe5','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PostPaid',0,'2018-11-13 08:08:47',NULL),(625,NULL,'2018-11-08 00:17:00',1,'trade','10.26.115.210','118.178.91.180',10,4,8192,NULL,0,NULL,0,'i-bp1ic9ggky8jx1m5o4q8','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PostPaid',0,'2018-11-13 08:08:47',NULL),(626,NULL,'2018-11-08 00:16:00',1,'tradeoutway','10.27.88.54','101.37.69.110',10,4,8192,NULL,0,NULL,0,'i-bp188p52tbinb5r3xxg5','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PostPaid',0,'2018-11-13 08:08:47',NULL),(627,NULL,'2018-11-08 00:16:00',1,'tradeoutway','10.25.252.145','118.178.140.194',10,4,8192,NULL,0,NULL,0,'i-bp14664y88uemwen87if','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PostPaid',0,'2018-11-13 08:08:47',NULL),(628,NULL,'2018-11-08 00:15:00',1,'trade','10.25.66.142','118.178.56.235',10,4,8192,NULL,0,NULL,0,'i-bp1acp86oa3i00jzws1d','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PostPaid',0,'2018-11-13 08:08:47',NULL),(629,NULL,'2018-11-08 00:15:00',1,'trade','10.26.112.34','120.27.244.235',10,4,8192,NULL,0,NULL,0,'i-bp1d10hr1jugz8btdg74','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PostPaid',0,'2018-11-13 08:08:47',NULL),(630,NULL,'2018-11-18 22:07:00',1,'sentinel','10.26.255.220','118.178.125.203',10,2,4096,NULL,0,NULL,0,'i-bp1cbde2vnn7niwtyx5o','cn-hangzhou-e',0,'cn-hangzhou',0,1,'PrePaid',0,'2018-11-22 01:12:05',NULL);
/*!40000 ALTER TABLE `oc_ecs_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_nginx_env_file`
--

DROP TABLE IF EXISTS `oc_nginx_env_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_nginx_env_file` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `envId` bigint(20) NOT NULL,
  `fileType` int(1) NOT NULL COMMENT '0:upstream 1:location',
  `filePath` varchar(200) NOT NULL DEFAULT '' COMMENT '相对路径',
  `fileName` varchar(100) NOT NULL DEFAULT '' COMMENT '文件名称',
  `fileKey` varchar(50) NOT NULL COMMENT 'Key',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `envId` (`envId`,`fileType`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_nginx_env_file`
--

LOCK TABLES `oc_nginx_env_file` WRITE;
/*!40000 ALTER TABLE `oc_nginx_env_file` DISABLE KEYS */;
INSERT INTO `oc_nginx_env_file` VALUES (1,8,0,'vhost/location','location_default111.conf','LOCATION','2018-10-31 09:19:57','2018-11-01 01:18:17'),(2,8,1,'vhost/upstream','upstream_default.conf','UPSTREAM','2018-10-31 09:19:57',NULL),(3,9,0,'vhost/location','location_default.conf','LOCATION','2018-11-01 02:18:53',NULL),(4,9,1,'vhost/upstream','upstream_default.conf','UPSTREAM','2018-11-01 02:18:53',NULL),(5,10,0,'vhost/location','location_default.conf','LOCATION','2018-11-01 08:42:46',NULL),(6,10,1,'vhost/upstream','upstream_default.conf','UPSTREAM','2018-11-01 08:42:46',NULL),(7,12,0,'vhost/location','location_default.conf','LOCATION','2018-11-02 06:44:09',NULL),(8,12,1,'vhost/upstream','upstream_default.conf','UPSTREAM','2018-11-02 06:44:09',NULL),(9,13,0,'vhost/location','location_default.conf','LOCATION','2018-11-02 06:44:16',NULL),(10,13,1,'vhost/upstream','upstream_default.conf','UPSTREAM','2018-11-02 06:44:16',NULL),(11,14,0,'vhost/location','location_default.conf','LOCATION','2018-11-09 01:46:33',NULL),(12,14,1,'vhost/upstream','upstream_default.conf','UPSTREAM','2018-11-09 01:46:33',NULL);
/*!40000 ALTER TABLE `oc_nginx_env_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_nginx_vhost_env`
--

DROP TABLE IF EXISTS `oc_nginx_vhost_env`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_nginx_vhost_env` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `vhostId` bigint(20) NOT NULL,
  `envType` int(11) NOT NULL,
  `confPath` varchar(200) NOT NULL DEFAULT '' COMMENT '配置文件路径',
  `content` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vhostId` (`vhostId`,`envType`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_nginx_vhost_env`
--

LOCK TABLES `oc_nginx_vhost_env` WRITE;
/*!40000 ALTER TABLE `oc_nginx_vhost_env` DISABLE KEYS */;
INSERT INTO `oc_nginx_vhost_env` VALUES (10,1,4,'/data/www/conf/web/www.qianou.com/www','','2018-11-01 08:42:46',NULL),(12,2,2,'/data/www/daily','','2018-11-02 06:44:09',NULL),(14,1,2,'/data/www/conf/web/www.qianou.com/daily','','2018-11-09 01:46:33','2018-11-09 10:21:34');
/*!40000 ALTER TABLE `oc_nginx_vhost_env` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_nginx_vhost_servergroup`
--

DROP TABLE IF EXISTS `oc_nginx_vhost_servergroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_nginx_vhost_servergroup` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `vhostId` bigint(20) NOT NULL,
  `serverGroupId` bigint(20) NOT NULL,
  `groupName` varchar(50) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupId` (`vhostId`,`serverGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_nginx_vhost_servergroup`
--

LOCK TABLES `oc_nginx_vhost_servergroup` WRITE;
/*!40000 ALTER TABLE `oc_nginx_vhost_servergroup` DISABLE KEYS */;
INSERT INTO `oc_nginx_vhost_servergroup` VALUES (5,1,1,'group_getway','2018-11-02 09:46:46',NULL);
/*!40000 ALTER TABLE `oc_nginx_vhost_servergroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_nginx_vhosts`
--

DROP TABLE IF EXISTS `oc_nginx_vhosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_nginx_vhosts` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `serverName` varchar(100) NOT NULL DEFAULT '' COMMENT '域名',
  `serverKey` varchar(50) NOT NULL COMMENT '虚拟主机key',
  `content` varchar(255) DEFAULT '',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_nginx_vhosts`
--

LOCK TABLES `oc_nginx_vhosts` WRITE;
/*!40000 ALTER TABLE `oc_nginx_vhosts` DISABLE KEYS */;
INSERT INTO `oc_nginx_vhosts` VALUES (1,'www.qianou.com','KA_QIANOU','一号管家','2018-10-08 06:21:13',NULL),(3,'pay.qianou.com','PAY_QIANOU','狗付','2018-11-02 09:02:57','2018-11-02 09:03:15');
/*!40000 ALTER TABLE `oc_nginx_vhosts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_org`
--

DROP TABLE IF EXISTS `oc_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_org` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_org`
--

LOCK TABLES `oc_org` WRITE;
/*!40000 ALTER TABLE `oc_org` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_org_group`
--

DROP TABLE IF EXISTS `oc_org_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_org_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `orgGroupName` varchar(100) NOT NULL DEFAULT '',
  `content` varchar(200) DEFAULT NULL,
  `top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '顶层',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_org_group`
--

LOCK TABLES `oc_org_group` WRITE;
/*!40000 ALTER TABLE `oc_org_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_org_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_org_role`
--

DROP TABLE IF EXISTS `oc_org_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_org_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `orgRoleName` varchar(100) NOT NULL DEFAULT '',
  `desc` varchar(100) DEFAULT NULL COMMENT '中文岗位描述',
  `content` varchar(200) DEFAULT NULL,
  `level` int(11) DEFAULT NULL COMMENT '职级',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_org_role`
--

LOCK TABLES `oc_org_role` WRITE;
/*!40000 ALTER TABLE `oc_org_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_org_role` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server`
--

LOCK TABLES `oc_server` WRITE;
/*!40000 ALTER TABLE `oc_server` DISABLE KEYS */;
INSERT INTO `oc_server` VALUES (8,1,0,'manage',4,'121.196.198.129',1584,'172.17.0.236',1585,2,'test2','',12,'1','','',-1,0,NULL,'2018-07-16 07:44:05','2018-07-17 01:48:29'),(9,1,0,'manage',4,'121.43.229.173',1586,'10.251.238.209',1587,2,'zsearch-solr-engine-4','cn-hangzhou-d',12,'2','','',0,1,NULL,'2018-07-18 02:51:25','2018-08-03 00:59:50'),(10,1,0,'manage',2,'',0,'10.17.1.152',1588,1,'getway','',12,'1','','',-1,0,NULL,'2018-08-07 08:13:01','2018-08-08 00:59:49');
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
  `name` varchar(50) NOT NULL DEFAULT '',
  `content` varchar(50) DEFAULT NULL,
  `ipNetwork` varchar(30) DEFAULT NULL,
  `useType` int(11) NOT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_server_group`
--

LOCK TABLES `oc_server_group` WRITE;
/*!40000 ALTER TABLE `oc_server_group` DISABLE KEYS */;
INSERT INTO `oc_server_group` VALUES (1,'group_getway','',NULL,2,'2018-06-07 09:11:21','2018-11-01 08:30:57'),(2,'group_aaa','',NULL,0,'2018-11-21 02:27:25',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_servergroup_properties`
--

LOCK TABLES `oc_servergroup_properties` WRITE;
/*!40000 ALTER TABLE `oc_servergroup_properties` DISABLE KEYS */;
INSERT INTO `oc_servergroup_properties` VALUES (27,1,0,75,'',12,'2018-11-01 09:33:43',NULL),(28,1,0,74,'getway',12,'2018-11-01 09:33:43',NULL),(29,1,0,71,'',12,'2018-11-01 09:33:43',NULL),(30,1,0,70,'',12,'2018-11-01 09:33:43',NULL),(31,1,0,69,'',12,'2018-11-01 09:33:44',NULL),(32,1,0,68,'',12,'2018-11-01 09:33:44',NULL),(33,1,0,67,'include /usr/local/nginx/conf/vhost/proxy_default.conf;',12,'2018-11-01 09:33:44',NULL),(34,1,0,63,'',12,'2018-11-01 09:33:44',NULL),(35,1,0,62,'',12,'2018-11-01 09:33:44',NULL),(36,1,0,61,'false',12,'2018-11-01 09:33:44',NULL),(37,1,0,60,'false',12,'2018-11-01 09:33:44',NULL),(38,1,0,58,'true',12,'2018-11-01 09:33:44',NULL),(39,1,0,57,'',12,'2018-11-01 09:33:44',NULL),(40,1,0,56,'true',12,'2018-11-01 09:33:44',NULL),(41,1,0,55,'',12,'2018-11-01 09:33:44',NULL),(42,2,0,22,'8080',1,'2018-11-21 02:27:35',NULL),(43,2,0,23,'8000',1,'2018-11-21 02:27:35',NULL),(44,2,0,24,'10000',1,'2018-11-21 02:27:35',NULL),(45,2,0,25,'10100',1,'2018-11-21 02:27:35',NULL),(46,2,0,26,'',1,'2018-11-21 02:27:35',NULL),(47,2,0,27,'webStatus',1,'2018-11-21 02:27:35',NULL),(48,2,0,28,'',1,'2018-11-21 02:27:35',NULL),(49,2,0,29,'utf8',1,'2018-11-21 02:27:35',NULL),(50,2,0,30,'true',1,'2018-11-21 02:27:35',NULL),(51,2,0,31,'-Dfile.encoding=UTF-8 -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=9999,server=y,suspend=n',1,'2018-11-21 02:27:35',NULL),(52,2,0,32,'5',1,'2018-11-21 02:27:35',NULL),(53,2,0,33,'',1,'2018-11-21 02:27:35',NULL),(54,2,0,34,'',1,'2018-11-21 02:27:35',NULL),(55,2,0,35,'',1,'2018-11-21 02:27:35',NULL),(56,2,0,36,'',1,'2018-11-21 02:27:35',NULL),(57,2,0,7,'',1,'2018-11-21 02:27:35',NULL),(58,2,0,2,'',1,'2018-11-21 02:27:35',NULL),(59,2,0,3,'',1,'2018-11-21 02:27:35',NULL),(60,2,0,1,'',1,'2018-11-21 02:27:35',NULL),(61,0,9,52,'',2,'2018-11-21 02:29:53',NULL),(62,0,9,51,'CentOS6.x',2,'2018-11-21 02:29:53',NULL),(63,0,9,50,'Linux',2,'2018-11-21 02:29:53',NULL),(64,0,9,4,'',2,'2018-11-21 02:29:53',NULL),(65,0,9,5,'',2,'2018-11-21 02:29:53',NULL),(66,0,9,6,'',2,'2018-11-21 02:29:53',NULL);
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
-- Table structure for table `oc_workflow`
--

DROP TABLE IF EXISTS `oc_workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_workflow` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupId` bigint(20) NOT NULL,
  `wfName` varchar(50) DEFAULT NULL,
  `wfKey` varchar(50) DEFAULT NULL,
  `wfStatus` int(1) NOT NULL DEFAULT '0' COMMENT '工单状态 0正常 1暂时关闭 2开发中',
  `title` varchar(100) NOT NULL DEFAULT '',
  `content` varchar(200) DEFAULT NULL,
  `helpUrl` varchar(250) DEFAULT NULL COMMENT '帮助链接',
  `topics` varchar(255) DEFAULT NULL COMMENT '主题（用于搜索）',
  `wfType` int(1) NOT NULL DEFAULT '0' COMMENT '工单类型 0:运维类',
  `approval` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审批',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `wfKey` (`wfKey`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workflow`
--

LOCK TABLES `oc_workflow` WRITE;
/*!40000 ALTER TABLE `oc_workflow` DISABLE KEYS */;
INSERT INTO `oc_workflow` VALUES (1,1,'workflow_keybox','KEYBOX',0,'堡垒机权限申请','keybox 权限',NULL,'keybox 堡垒机 跳板机 权限 服务器',0,1,'2018-11-16 06:19:49','2018-11-16 06:22:52');
/*!40000 ALTER TABLE `oc_workflow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workflow_group`
--

DROP TABLE IF EXISTS `oc_workflow_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_workflow_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupName` varchar(100) NOT NULL DEFAULT '' COMMENT '工作流组名称',
  `content` varchar(200) DEFAULT NULL COMMENT '说明',
  `groupType` int(1) NOT NULL DEFAULT '0' COMMENT '工作流组类型 0:ops',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workflow_group`
--

LOCK TABLES `oc_workflow_group` WRITE;
/*!40000 ALTER TABLE `oc_workflow_group` DISABLE KEYS */;
INSERT INTO `oc_workflow_group` VALUES (1,'权限申请','堡垒机,持续集成等权限申请',0,'2018-11-16 03:16:41','2018-11-19 03:50:04');
/*!40000 ALTER TABLE `oc_workflow_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workflow_team`
--

DROP TABLE IF EXISTS `oc_workflow_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_workflow_team` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `teamName` varchar(100) NOT NULL DEFAULT '' COMMENT '团队名称',
  `content` varchar(200) DEFAULT NULL,
  `teamType` int(11) DEFAULT NULL COMMENT '团队类型',
  `teamleaderUserId` bigint(20) DEFAULT NULL COMMENT 'tl用户id',
  `teamleaderUsername` varchar(50) DEFAULT NULL COMMENT 'tl用户名',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workflow_team`
--

LOCK TABLES `oc_workflow_team` WRITE;
/*!40000 ALTER TABLE `oc_workflow_team` DISABLE KEYS */;
INSERT INTO `oc_workflow_team` VALUES (1,'测试代码','',0,140,'admin','2018-11-15 03:53:00',NULL),(2,'leader1111','22222',0,140,'admin','2018-11-15 03:55:45','2018-11-15 09:34:03');
/*!40000 ALTER TABLE `oc_workflow_team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workflow_teamuser`
--

DROP TABLE IF EXISTS `oc_workflow_teamuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_workflow_teamuser` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `teamId` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL DEFAULT '',
  `teamRole` int(11) DEFAULT NULL COMMENT '团队角色',
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `teamId` (`teamId`,`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workflow_teamuser`
--

LOCK TABLES `oc_workflow_teamuser` WRITE;
/*!40000 ALTER TABLE `oc_workflow_teamuser` DISABLE KEYS */;
INSERT INTO `oc_workflow_teamuser` VALUES (6,2,2405,'opscloud',0,'2018-11-15 09:25:47',NULL),(7,2,140,'admin',0,'2018-11-15 09:43:02',NULL);
/*!40000 ALTER TABLE `oc_workflow_teamuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workflow_todo`
--

DROP TABLE IF EXISTS `oc_workflow_todo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_workflow_todo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `wfId` bigint(20) DEFAULT NULL COMMENT '工作流id',
  `wfName` varchar(50) DEFAULT NULL,
  `applyUserId` bigint(20) DEFAULT NULL COMMENT '申请人id',
  `applyDisplayName` varchar(50) DEFAULT NULL COMMENT '申请人显示名',
  `todoPhase` int(1) DEFAULT NULL COMMENT '阶段',
  `todoStatus` int(1) DEFAULT NULL COMMENT '状态',
  `gmtApply` timestamp NULL DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workflow_todo`
--

LOCK TABLES `oc_workflow_todo` WRITE;
/*!40000 ALTER TABLE `oc_workflow_todo` DISABLE KEYS */;
INSERT INTO `oc_workflow_todo` VALUES (1,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-22 10:49:38',NULL),(2,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-22 10:51:07',NULL),(3,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 01:38:04',NULL),(4,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 01:52:07',NULL),(5,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:17:15',NULL),(6,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:22:15',NULL),(7,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:35:55',NULL),(8,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:36:58',NULL),(9,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:37:31',NULL),(10,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:37:57',NULL),(11,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:38:09',NULL),(12,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:40:09',NULL),(13,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:40:54',NULL),(14,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:44:48',NULL),(15,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 03:45:21',NULL),(16,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 04:10:09',NULL),(17,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 04:11:04',NULL),(18,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 04:11:15',NULL),(19,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 04:19:46',NULL),(20,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 04:21:07',NULL),(21,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 04:22:59',NULL),(22,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 06:27:29',NULL),(23,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 06:53:46',NULL),(24,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 06:54:32',NULL),(25,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 08:16:57',NULL),(26,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 08:20:58',NULL),(27,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 08:32:03',NULL),(28,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 08:52:02',NULL),(29,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:00:23',NULL),(30,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:04:09',NULL),(31,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:07:52',NULL),(32,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:18:38',NULL),(33,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:30:56',NULL),(34,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:33:56',NULL),(35,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:34:56',NULL),(36,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:36:00',NULL),(37,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:58:39',NULL),(38,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 09:59:42',NULL),(39,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 10:10:25',NULL),(40,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 10:13:48',NULL),(41,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 10:16:36',NULL),(42,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 10:52:01',NULL),(43,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 10:52:10',NULL),(44,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 10:53:48',NULL),(45,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 10:55:59',NULL),(46,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-23 10:58:41',NULL),(47,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-27 06:06:26',NULL),(48,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-27 06:07:51',NULL),(49,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-27 06:17:40',NULL),(50,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-27 06:17:57',NULL),(51,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-27 06:20:32',NULL),(52,1,'workflow_keybox',140,'admin<liang jian>',NULL,NULL,NULL,'2018-11-27 06:24:47',NULL);
/*!40000 ALTER TABLE `oc_workflow_todo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workflow_todo_detail`
--

DROP TABLE IF EXISTS `oc_workflow_todo_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_workflow_todo_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoId` bigint(20) DEFAULT NULL,
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '类目名称',
  `detailKey` varchar(50) NOT NULL DEFAULT '' COMMENT '对象名称',
  `detailValue` text NOT NULL COMMENT 'json格式存储',
  `content` varchar(100) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `todoId` (`todoId`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workflow_todo_detail`
--

LOCK TABLES `oc_workflow_todo_detail` WRITE;
/*!40000 ALTER TABLE `oc_workflow_todo_detail` DISABLE KEYS */;
INSERT INTO `oc_workflow_todo_detail` VALUES (2,39,'','TodoKeybox','{\"ciAuth\":true,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}',NULL,'2018-11-23 10:10:45',NULL),(3,41,'','TodoKeybox','{\"ciAuth\":false,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}',NULL,'2018-11-23 10:29:04',NULL),(4,43,'','TodoKeybox','{\"ciAuth\":false,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}',NULL,'2018-11-23 10:52:13',NULL),(5,44,'','TodoKeybox','{\"ciAuth\":false,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}',NULL,'2018-11-23 10:53:51',NULL),(6,45,'','TodoKeybox','{\"ciAuth\":false,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}',NULL,'2018-11-23 10:56:02',NULL),(7,46,'','TodoKeybox','{\"ciAuth\":false,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}',NULL,'2018-11-23 10:58:46',NULL),(8,47,'','TodoKeybox','{\"ciAuth\":true,\"ciUserGroupDO\":{\"content\":\"\",\"envType\":0,\"gmtCreate\":\"\",\"gmtModify\":\"\",\"groupName\":\"\",\"id\":0,\"serverGroupId\":0},\"content\":\"\",\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}','','2018-11-27 06:06:54','2018-11-27 06:07:10'),(10,48,'','TodoKeybox','{\"ciAuth\":true,\"ciUserGroupDO\":{\"content\":\"\",\"envType\":0,\"gmtCreate\":\"\",\"gmtModify\":\"\",\"groupName\":\"\",\"id\":0,\"serverGroupId\":0},\"content\":\"\",\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}','','2018-11-27 06:07:57','2018-11-27 06:08:00'),(13,49,'group_getway','TodoKeybox','{\"ciAuth\":true,\"ciUserGroupDO\":{\"content\":\"\",\"envType\":0,\"gmtCreate\":\"\",\"gmtModify\":\"\",\"groupName\":\"\",\"id\":0,\"serverGroupId\":0},\"content\":\"\",\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}','','2018-11-27 06:17:44','2018-11-27 06:17:47'),(14,49,'group_aaa','TodoKeybox','{\"ciAuth\":true,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-11-21 10:27:25.0\",\"gmtModify\":\"\",\"id\":2,\"ipNetwork\":\"\",\"name\":\"group_aaa\",\"useType\":0},\"status\":0}',NULL,'2018-11-27 06:17:47',NULL),(15,50,'group_getway','TodoKeybox','{\"ciAuth\":true,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}',NULL,'2018-11-27 06:18:01',NULL),(16,51,'group_getway','TodoKeybox','{\"ciAuth\":true,\"ciUserGroupDO\":{\"content\":\"\",\"envType\":0,\"gmtCreate\":\"\",\"gmtModify\":\"\",\"groupName\":\"\",\"id\":0,\"serverGroupId\":0},\"content\":\"\",\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}','','2018-11-27 06:20:36','2018-11-27 06:21:03'),(17,51,'group_aaa','TodoKeybox','{\"ciAuth\":false,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-11-21 10:27:25.0\",\"gmtModify\":\"\",\"id\":2,\"ipNetwork\":\"\",\"name\":\"group_aaa\",\"useType\":0},\"status\":0}',NULL,'2018-11-27 06:21:03',NULL),(18,52,'group_getway','TodoKeybox','{\"ciAuth\":true,\"ciUserGroupDO\":{\"content\":\"\",\"envType\":0,\"gmtCreate\":\"\",\"gmtModify\":\"\",\"groupName\":\"\",\"id\":0,\"serverGroupId\":0},\"content\":\"\",\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-06-07 17:11:21.0\",\"gmtModify\":\"2018-11-01 16:30:57.0\",\"id\":1,\"ipNetwork\":\"\",\"name\":\"group_getway\",\"useType\":2},\"status\":0}','','2018-11-27 06:24:50','2018-11-27 06:25:08'),(19,52,'group_aaa','TodoKeybox','{\"ciAuth\":false,\"ciUserGroupDO\":{\"envType\":0,\"id\":0,\"serverGroupId\":0},\"serverGroupDO\":{\"content\":\"\",\"gmtCreate\":\"2018-11-21 10:27:25.0\",\"gmtModify\":\"\",\"id\":2,\"ipNetwork\":\"\",\"name\":\"group_aaa\",\"useType\":0},\"status\":0}',NULL,'2018-11-27 06:25:08',NULL);
/*!40000 ALTER TABLE `oc_workflow_todo_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oc_workflow_todo_user`
--

DROP TABLE IF EXISTS `oc_workflow_todo_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oc_workflow_todo_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `todoId` bigint(20) NOT NULL,
  `assigneeType` int(11) DEFAULT NULL COMMENT '代理类型 0:teamleader 1:deptLeader 2:ops',
  `assigneeDesc` varchar(50) DEFAULT NULL COMMENT '代理类型描述',
  `userId` bigint(20) DEFAULT NULL,
  `displayName` varchar(50) DEFAULT NULL,
  `evaluation` int(1) DEFAULT NULL COMMENT 'approve/disapprove/refuse/delegate',
  `evaluationMsg` varchar(200) DEFAULT NULL,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmtModify` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `todoId` (`todoId`,`assigneeType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_workflow_todo_user`
--

LOCK TABLES `oc_workflow_todo_user` WRITE;
/*!40000 ALTER TABLE `oc_workflow_todo_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `oc_workflow_todo_user` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=420 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oc_zabbix_template`
--

LOCK TABLES `oc_zabbix_template` WRITE;
/*!40000 ALTER TABLE `oc_zabbix_template` DISABLE KEYS */;
INSERT INTO `oc_zabbix_template` VALUES (325,'Template OS Linux','10001',0,'2018-06-19 02:07:14',NULL),(326,'Template App Zabbix Server','10047',0,'2018-06-19 02:07:14',NULL),(327,'Template App Zabbix Proxy','10048',0,'2018-06-19 02:07:14',NULL),(328,'Template App Zabbix Agent','10050',0,'2018-06-19 02:07:14',NULL),(329,'Template SNMP Interfaces','10060',0,'2018-06-19 02:07:14',NULL),(330,'Template SNMP Generic','10065',0,'2018-06-19 02:07:14',NULL),(331,'Template SNMP Device','10066',0,'2018-06-19 02:07:14',NULL),(332,'Template SNMP OS Windows','10067',0,'2018-06-19 02:07:14',NULL),(333,'Template SNMP Disks','10068',0,'2018-06-19 02:07:14',NULL),(334,'Template SNMP OS Linux','10069',0,'2018-06-19 02:07:14',NULL),(335,'Template SNMP Processors','10070',0,'2018-06-19 02:07:14',NULL),(336,'Template IPMI Intel SR1530','10071',0,'2018-06-19 02:07:14',NULL),(337,'Template IPMI Intel SR1630','10072',0,'2018-06-19 02:07:14',NULL),(338,'Template App MySQL Service','10073',0,'2018-06-19 02:07:14',NULL),(339,'Template OS OpenBSD','10074',0,'2018-06-19 02:07:14',NULL),(340,'Template OS FreeBSD','10075',0,'2018-06-19 02:07:14',NULL),(341,'Template OS AIX','10076',0,'2018-06-19 02:07:14',NULL),(342,'Template OS HP-UX','10077',0,'2018-06-19 02:07:14',NULL),(343,'Template OS Solaris','10078',0,'2018-06-19 02:07:14',NULL),(344,'Template OS Mac OS X','10079',0,'2018-06-19 02:07:14',NULL),(345,'Template OS Windows','10081',0,'2018-06-19 02:07:14',NULL),(346,'Template JMX Generic','10082',0,'2018-06-19 02:07:14',NULL),(347,'Template JMX Tomcat','10083',0,'2018-06-19 02:07:14',NULL),(348,'Template Virt VMware','10088',0,'2018-06-19 02:07:14',NULL),(349,'Template Virt VMware Guest','10089',0,'2018-06-19 02:07:14',NULL),(350,'Template Virt VMware Hypervisor','10091',0,'2018-06-19 02:07:14',NULL),(351,'Template App FTP Service','10093',0,'2018-06-19 02:07:14',NULL),(352,'Template App HTTP Service','10094',0,'2018-06-19 02:07:14',NULL),(353,'Template App HTTPS Service','10095',0,'2018-06-19 02:07:14',NULL),(354,'Template App IMAP Service','10096',0,'2018-06-19 02:07:14',NULL),(355,'Template App LDAP Service','10097',0,'2018-06-19 02:07:14',NULL),(356,'Template App POP Service','10100',0,'2018-06-19 02:07:14',NULL),(357,'Template App SMTP Service','10101',0,'2018-06-19 02:07:14',NULL),(358,'Template App SSH Service','10102',0,'2018-06-19 02:07:14',NULL),(359,'Template App Telnet Service','10103',0,'2018-06-19 02:07:14',NULL),(360,'Template ICMP Ping','10104',0,'2018-06-19 02:07:14',NULL),(361,'Template JMX Tomcat X','10105',0,'2018-06-19 02:07:14',NULL),(362,'Template Tomcat Logs','10106',0,'2018-06-19 02:07:14',NULL),(363,'Template Zimbra Mail','10205',0,'2018-06-19 02:07:14',NULL),(364,'Template Backup Files Check','10208',0,'2018-06-19 02:07:14',NULL),(365,'Template Dell iDrac SNMPV2','10212',0,'2018-06-19 02:07:14',NULL),(366,'Template Java','10221',0,'2018-06-19 02:07:14',NULL),(367,'Template Hadoop','10238',0,'2018-06-19 02:07:14',NULL),(368,'Template LO','10241',0,'2018-06-19 02:07:14',NULL),(369,'Template Panama','10243',0,'2018-06-19 02:07:14',NULL),(370,'Template Nginx','10456',0,'2018-06-19 02:07:14',NULL),(371,'Template Process Monitor','10459',0,'2018-06-19 02:07:14',NULL),(372,'Template Linux Diskstats','10892',0,'2018-06-19 02:07:14',NULL),(373,'ZBX-FORTINET-INTERFACES','10937',0,'2018-06-19 02:07:14',NULL),(374,'Template Network','10939',0,'2018-06-19 02:07:14',NULL),(375,'Template MySQL Status','10964',0,'2018-06-19 02:07:14',NULL),(376,'Template App SQLServer','10976',0,'2018-06-19 02:07:14',NULL),(377,'Template CTI Server','10977',0,'2018-06-19 02:07:14',NULL),(378,'Template App DNS Service','10981',0,'2018-06-19 02:07:14',NULL),(379,'Template Prometheus Logs','10984',0,'2018-06-19 02:07:14',NULL),(380,'Template Zookeeper','11289',0,'2018-06-19 02:07:14',NULL),(381,'Template App NNTP Service','10098',1,'2018-08-02 03:01:39','2018-08-07 06:19:19'),(382,'Template App NTP Service','10099',0,'2018-08-02 03:01:39',NULL),(383,'check_iptables','10105',0,'2018-08-02 03:01:39',NULL),(384,'Elasticsearch Cluster','10106',0,'2018-08-02 03:01:39',NULL),(385,'Elasticsearch Node','10107',0,'2018-08-02 03:01:39',NULL),(386,'Elasticsearch Service','10108',0,'2018-08-02 03:01:39',NULL),(387,'Nginx Status','10109',0,'2018-08-02 03:01:39',NULL),(388,'Template App MongoDB','10110',0,'2018-08-02 03:01:39',NULL),(389,'Template JMX Generic new','10111',0,'2018-08-02 03:01:39',NULL),(390,'Template Netstat','10112',0,'2018-08-02 03:01:39',NULL),(391,'Template Nginx Info','10113',0,'2018-08-02 03:01:39',NULL),(392,'Template Redis Auto Discovery','10114',0,'2018-08-02 03:01:39',NULL),(393,'Template Simple Check','10115',0,'2018-08-02 03:01:39',NULL),(394,'template top_process','10116',0,'2018-08-02 03:01:39',NULL),(395,'Template Web Monitor','10117',0,'2018-08-02 03:01:39',NULL),(396,'Template_Nginx_Log','10119',0,'2018-08-02 03:01:39',NULL),(397,'t_iptstate','10120',0,'2018-08-02 03:01:39',NULL),(398,'Template OS Linux Active','10121',0,'2018-08-02 03:01:39',NULL),(399,'Template OS Linux Active_error','10122',0,'2018-08-02 03:01:39',NULL),(400,'Template OS Linux bd','10123',0,'2018-08-02 03:01:39',NULL),(401,'Templates Memcached_Auto Discovery','10136',0,'2018-08-02 03:01:39',NULL),(402,'Template Iptables Port Accept','10261',0,'2018-08-02 03:01:39',NULL),(403,'Template Service pull up C','10262',0,'2018-08-02 03:01:39',NULL),(404,'RDS监控模板','10263',0,'2018-08-02 03:01:39',NULL),(405,'Template Service pull up_test','10277',0,'2018-08-02 03:01:39',NULL),(406,'Template Service pull up B','10284',0,'2018-08-02 03:01:39',NULL),(407,'Template Java Process Dicovery','10285',0,'2018-08-02 03:01:39',NULL),(408,'Template Java Process Dicovery_jdk1.8','10286',0,'2018-08-02 03:01:39',NULL),(409,'Templates Memcached_Auto Discovery_test','10287',0,'2018-08-02 03:01:39',NULL),(410,'Template Service pull up A','10288',0,'2018-08-02 03:01:39',NULL),(411,'Template HAProxy Status','10290',0,'2018-08-02 03:01:39',NULL),(412,'Template OS Linux - CouchDB','10294',0,'2018-08-02 03:01:39',NULL),(413,'新URL监控模板','10297',0,'2018-08-02 03:01:39',NULL),(414,'phpfpm_status1','10303',0,'2018-08-02 03:01:39',NULL),(415,'phpfpm_status2','10304',0,'2018-08-02 03:01:39',NULL),(416,'Template Mysql  Auto Discovery','10312',0,'2018-08-02 03:01:39',NULL),(417,'Template TCP Connection Status','10313',0,'2018-08-02 03:01:39',NULL),(418,'Template RabbitMQ','10316',0,'2018-08-02 03:01:39',NULL),(419,'Template App RabbitMQ v3','10318',0,'2018-08-02 03:01:39',NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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

-- Dump completed on 2018-11-27 15:40:23
