-- MySQL dump 10.13  Distrib 8.0.24, for Linux (x86_64)
--
-- Host: localhost    Database: treatment
-- ------------------------------------------------------
-- Server version	8.0.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bed`
--

DROP TABLE IF EXISTS `bed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bed` (
  `bed_id` bigint NOT NULL AUTO_INCREMENT COMMENT '床位id',
  `bed_num` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '床位编号，唯一，命名：科室-编号',
  `dep_id` bigint DEFAULT NULL COMMENT '科室id',
  `bed_state` int DEFAULT '0' COMMENT '床位状态，0无人，1有人',
  `soft_del` int DEFAULT '1' COMMENT '软删除 0删除/1默认',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`bed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1564166503516418050 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='病床床位表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bed`
--

LOCK TABLES `bed` WRITE;
/*!40000 ALTER TABLE `bed` DISABLE KEYS */;
INSERT INTO `bed` VALUES (3,'床位1',1562373635540508674,0,1,'admin','2022-08-25 11:10:41','admin','2022-08-28 22:08:13','张大夫妙手回春啊'),(7,'床位5',1562373635540508674,0,1,'admin','2022-08-25 11:10:41','admin','2022-08-25 14:11:58','张大夫妙手回春啊'),(8,'床位6',1562373635540508674,0,1,'admin','2022-08-25 11:10:41','admin','2022-08-25 14:12:03','张大夫妙手回春啊'),(11,'床位7',1562373635540508674,0,1,'admin','2022-08-25 11:10:41','admin','2022-08-25 14:12:08','张大夫妙手回春啊'),(12,'床位8',1562373635540508674,0,1,'admin','2022-08-25 11:10:41','admin','2022-08-25 14:12:13','张大夫妙手回春啊'),(1563873052465709058,'床位1',1562373498806198273,0,1,'admin','2022-08-28 20:56:06','admin','2022-08-28 20:56:06','骨科的床位'),(1564158219745046530,'32',1562373635540508674,0,1,'admin','2022-08-29 15:49:15','admin','2022-08-29 16:38:58','323232'),(1564166503516418049,'122',100,0,1,'admin','2022-08-29 16:22:10','admin','2022-08-29 16:22:10','1222');
/*!40000 ALTER TABLE `bed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blood_glucose`
--

DROP TABLE IF EXISTS `blood_glucose`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_glucose` (
  `bg_id` bigint NOT NULL AUTO_INCREMENT COMMENT '血糖唯一id',
  `patient_id` bigint DEFAULT NULL COMMENT '病人id',
  `patient_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病人姓名',
  `register_id` bigint DEFAULT NULL COMMENT '挂号id',
  `g_l_u` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '葡萄糖',
  `GLU_Reference` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '3.9-6.1mmol/L' COMMENT '葡萄糖参考范围',
  `p_h` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '酸碱度',
  `PH_Reference` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '5.5-8.0' COMMENT '酸碱度参考范围',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`bg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1562715689267003395 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='血糖结果表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_glucose`
--

LOCK TABLES `blood_glucose` WRITE;
/*!40000 ALTER TABLE `blood_glucose` DISABLE KEYS */;
INSERT INTO `blood_glucose` VALUES (1562639468453756930,1,'加密测试',3,'5.5','3.9-6.1mmol/L','5.9','5.5-8.0','admin','2022-08-25 11:14:16','admin','2022-08-29 16:28:49','公平'),(1562643779615244290,1,'patient',2,'4.9','3.9-6.1mmol/L','7.7','5.5-8.0','admin','2022-08-25 11:31:24','admin','2022-08-26 10:40:19','公平'),(1562647550546243585,1,'patient',3,'4.9','3.9-6.1mmol/L','7.7','5.5-8.0','admin','2022-08-25 11:46:23','admin','2022-08-26 12:05:25','公平'),(1562715689267003394,1,'建档用户999',1562781424598200322,'6.8','3.9-6.1mmol/L','6.0','5.5-8.0','admin','2022-08-25 16:17:09','admin','2022-08-26 10:29:48','芝士雪豹');
/*!40000 ALTER TABLE `blood_glucose` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blood_routine`
--

DROP TABLE IF EXISTS `blood_routine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_routine` (
  `br_id` bigint NOT NULL AUTO_INCREMENT COMMENT '血常规唯一id',
  `patient_id` bigint DEFAULT NULL COMMENT '病人id',
  `patient_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病人姓名',
  `register_id` bigint DEFAULT NULL COMMENT '挂号id',
  `w_b_c` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '白细胞',
  `WBC_Reference` varchar(30) COLLATE utf8mb4_general_ci DEFAULT '4.00-10.00(10^9/L)' COMMENT '白细胞参考范围',
  `r_b_c` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '红细胞',
  `RBC_Reference` varchar(30) COLLATE utf8mb4_general_ci DEFAULT '3.50-5.50(10^12/L)' COMMENT '红细胞参考范围',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`br_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1564062284734631939 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='血常规结果表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_routine`
--

LOCK TABLES `blood_routine` WRITE;
/*!40000 ALTER TABLE `blood_routine` DISABLE KEYS */;
INSERT INTO `blood_routine` VALUES (1562653431463735298,1,'1',1,'0.6','1','1.1','1','admin','2022-08-25 12:09:45','admin','2022-08-25 12:09:45','1'),(1562653888760311810,1,'1',1,'0.9','1','2.1','1','admin','2022-08-25 12:11:34','admin','2022-08-25 12:11:34','1'),(1562654428823093249,1,'1',1,'1.3','1','0.7','1','admin','2022-08-25 12:13:43','admin','2022-08-25 12:13:43','1'),(1562658541589811201,1,'1',1,'0.7','1','0.9','1','admin','2022-08-25 12:30:04','admin','2022-08-25 12:30:04','1'),(1564062284734631938,NULL,'1',1,'1.1','1','0.4','1','admin','2022-08-29 09:28:02','admin','2022-08-29 09:28:02','1');
/*!40000 ALTER TABLE `blood_routine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dep`
--

DROP TABLE IF EXISTS `dep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dep` (
  `dep_id` int NOT NULL AUTO_INCREMENT COMMENT '科室id',
  `dep_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '科室名',
  `soft_del` int DEFAULT '1' COMMENT '软删除 0删除/1默认',
  PRIMARY KEY (`dep_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='科室表;弃用';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dep`
--

LOCK TABLES `dep` WRITE;
/*!40000 ALTER TABLE `dep` DISABLE KEYS */;
INSERT INTO `dep` VALUES (1,'耳鼻喉科',1),(2,'儿科',1),(3,'骨科',1),(4,'内科',1),(5,'神经科',1),(6,'精神科',1),(7,'脑科Test',1);
/*!40000 ALTER TABLE `dep` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `equipment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '设备id',
  `equipment_num` varchar(40) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备编号',
  `equipment_name` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名字',
  `equipment_time` date DEFAULT NULL COMMENT '设备添加时间',
  `soft_del` bigint DEFAULT '1' COMMENT '软删除 0删除/1默认',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`equipment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES (1,'10','test','2022-08-22',1,'',NULL,'',NULL,NULL),(2,'20','test2','2022-08-11',1,'',NULL,'',NULL,NULL),(3,'30','B超机test','2022-08-03',1,'',NULL,'',NULL,NULL),(4,'10','核磁共振机','2022-08-22',1,'',NULL,'',NULL,NULL),(5,'10','CT机','2022-08-20',1,'',NULL,'',NULL,NULL);
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
INSERT INTO `gen_table` VALUES (1561621373158928385,'medicine','药品表;',NULL,NULL,'Medicine','crud','xian.woniuxy.reghos','reghos-serv','medicine','药品','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-19 11:25:25','admin','2022-08-22 15:53:47',NULL),(1561913404657745922,'equipment','设备表',NULL,NULL,'Equipment','crud','xian.woniuxy.reghos','reghos-serv','equipment','设备','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:34:24','admin','2022-08-23 11:28:09',NULL),(1561913406967009282,'operation','手术项目表',NULL,NULL,'Operation','crud','xian.woniuxy.reghos','reghos-serv','operation','手术项目','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:36:25','admin','2022-08-24 17:02:28',NULL),(1561913410190032897,'register','挂号表',NULL,NULL,'Register','crud','xian.woniuxy.reghos','reghos-serv','register','挂号','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:36:54','admin','2022-08-23 11:28:08',NULL),(1561915444023021570,'medicine_order','药品订单表',NULL,NULL,'MedicineOrder','crud','xian.woniuxy.reghos','reghos-serv','medicine_order','药品订单','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 11:16:50','admin','2022-08-23 17:56:42','药品订单表'),(1561977716590370818,'bed','病床床位表',NULL,NULL,'Bed','crud','xian.woniuxy.reghos','reghos-serv','bed','病床床位','G','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:34:00','admin','2022-08-25 10:47:05','病床床位表;'),(1561977718414893058,'blood_glucose','血糖结果表',NULL,NULL,'BloodGlucose','crud','xian.woniuxy.reghos','reghos-serv','glucose','血糖结果','G','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:41:30','admin','2022-08-25 10:21:04','血糖结果表'),(1561977719660601345,'blood_routine','血常规结果表',NULL,NULL,'BloodRoutine','crud','xian.woniuxy.reghos','reghos-serv','routine','血常规结果','G','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:41:46','admin','2022-08-25 11:56:28','血常规结果表'),(1561977720977612802,'dep','科室表;',NULL,NULL,'Dep','crud','xian.woniuxy.reghos','reghos-serv','dep','科室;','G','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-17 15:42:01','admin','2022-08-23 17:23:58','科室表;'),(1561977721745170433,'inspect','检查项目表',NULL,NULL,'Inspect','crud','xian.woniuxy.reghos','reghos-serv','inspect','检查项目','G','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:34:44','admin','2022-08-25 09:57:04','检查项目表;'),(1561977723053793282,'inspect_order','检查项目订单表',NULL,NULL,'InspectOrder','crud','xian.woniuxy.reghos','reghos-serv','inspect_orderr','检查项目订单','G','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:35:09','admin','2022-08-25 15:11:24','检查项目订单表'),(1561977724295307265,'lfts','肝功能结果表',NULL,NULL,'Lfts','crud','xian.woniuxy.reghos','reghos-serv','lfts','肝功能结果','G','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:42:29','admin','2022-08-23 17:28:12','肝功能结果表'),(1561977725457129474,'log','日志表;日志表',NULL,NULL,'Log','crud','xian.woniuxy','woniuxy','log','日志;日志','ataoxz','0','/',NULL,'admin','2022-08-22 17:40:06','admin','2022-08-22 17:40:06',NULL),(1561977726249852929,'operation_order','手术订单表',NULL,NULL,'OperationOrder','crud','xian.woniuxy.reghos','reghos-serv','operation_order','手术订单','G','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:45:22','admin','2022-08-25 10:21:16','手术订单表;');
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table_column`
--

DROP TABLE IF EXISTS `gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL COMMENT '编号',
  `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
INSERT INTO `gen_table_column` VALUES (1561621373771296769,1561621373158928385,'medicine_id','药品id','int','Long','medicineId','1','1','1',NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-22 15:48:44','admin','2022-08-22 15:53:47'),(1561621373788073985,1561621373158928385,'medicine_name','药品名','varchar(30)','String','medicineName','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2022-08-22 15:48:44','admin','2022-08-22 15:53:47'),(1561621373804851202,1561621373158928385,'medicine_price','药品价格','decimal(8,2)','BigDecimal','medicinePrice','0','0','1','1','1','1','1','BETWEEN','input','',3,'admin','2022-08-22 15:48:44','admin','2022-08-22 15:53:47'),(1561621373813239809,1561621373158928385,'medicine_stock','药品库存','int','Long','medicineStock','0','0','1','1','1','1',NULL,'EQ','input','',4,'admin','2022-08-22 15:48:44','admin','2022-08-22 15:53:47'),(1561621373817434114,1561621373158928385,'medicine_details','药品注意事项','varchar(200)','String','medicineDetails','0','0','1','1','1','1',NULL,'EQ','input','',5,'admin','2022-08-22 15:48:44','admin','2022-08-22 15:53:47'),(1561621373825822722,1561621373158928385,'soft_del','软删除 0删除/1默认','int','Long','softDel','0','0','1','1','1','1','1','EQ','input','',6,'admin','2022-08-22 15:48:44','admin','2022-08-22 15:53:47'),(1561621373830017026,1561621373158928385,'recipel','1处方/0非处方','int','Long','recipel','0','0','1','1','1','1','1','EQ','select','chufang_feichufang',7,'admin','2022-08-22 15:48:44','admin','2022-08-22 15:53:47'),(1561913405102342146,1561913404657745922,'equipment_id','设备id','int','Long','equipmentId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913405119119362,1561913404657745922,'equipment_num','设备编号','varchar(40)','String','equipmentNum','0','0',NULL,'1','1','1','1','EQ','input','',2,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913405140090881,1561913404657745922,'equipment_name','设备名字','varchar(30)','String','equipmentName','0','0',NULL,'1','1','1','1','LIKE','input','',3,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913405148479489,1561913404657745922,'equipment_time','设备添加时间','date','Date','equipmentTime','0','0',NULL,'1','1','1','1','EQ','datetime','',4,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913405152673793,1561913404657745922,'soft_del','软删除 0删除/1默认','int','Long','softDel','0','0',NULL,'1','1','1','1','EQ','input','',5,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913405156868098,1561913404657745922,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',6,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913405165256705,1561913404657745922,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',7,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913405173645313,1561913404657745922,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',8,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913405177839617,1561913404657745922,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',9,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913405182033921,1561913404657745922,'remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',10,'admin','2022-08-23 11:09:09','admin','2022-08-23 11:28:09'),(1561913407352885250,1561913406967009282,'operation_id','手术项目id','bigint','Long','operationId','1','1','1',NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:28'),(1561913407352885251,1561913406967009282,'operation_name','手术名称','varchar(30)','String','operationName','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:28'),(1561913407352885252,1561913406967009282,'dep_id','科室id','int','Long','depId','0','0','1','1','1','1','1','EQ','select','',3,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:29'),(1561913407352885253,1561913406967009282,'operation_price','手术费用','decimal(8,2)','BigDecimal','operationPrice','0','0','1','1','1','1','1','EQ','input','',4,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:29'),(1561913407352885254,1561913406967009282,'soft_del','软删除 0删除/1默认','int','Long','softDel','0','0','1','1','1','1','1','EQ','input','',5,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:29'),(1561913407352885255,1561913406967009282,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',6,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:29'),(1561913407352885256,1561913406967009282,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',7,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:29'),(1561913407352885257,1561913406967009282,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',8,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:29'),(1561913407352885258,1561913406967009282,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',9,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:29'),(1561913407352885259,1561913406967009282,'remark','备注','varchar(500)','String','remark','0','0','1','1','1','1',NULL,'EQ','textarea','',10,'admin','2022-08-23 11:09:10','admin','2022-08-24 17:02:29'),(1561913410806595586,1561913410190032897,'register_id','挂号id','int','Long','registerId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410810789889,1561913410190032897,'patient_id','用户id','int','Long','patientId','0','0',NULL,'1','1','1',NULL,'EQ','input','',2,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410814984193,1561913410190032897,'user_id','医生id','int','Long','userId','0','0',NULL,'1','1','1','1','EQ','input','',3,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410814984194,1561913410190032897,'user_name','医生姓名','varchar(20)','String','userName','0','0',NULL,'1','1','1','1','LIKE','input','',4,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410814984195,1561913410190032897,'register_date','挂号时间','datetime','Date','registerDate','0','0',NULL,'1','1','1','1','EQ','datetime','',5,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410814984196,1561913410190032897,'dep_id','科室id','int','Long','depId','0','0',NULL,'1','1','1',NULL,'EQ','input','',6,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410844344321,1561913410190032897,'dep_name','科室名称','varchar(20)','String','depName','0','0',NULL,'1','1','1','1','LIKE','input','',7,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410848538625,1561913410190032897,'register_price','挂号价格','decimal(8,2)','BigDecimal','registerPrice','0','0',NULL,'1','1','1',NULL,'EQ','input','',8,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410856927233,1561913410190032897,'register_state','是否完成0未完成，1已完成','varchar(255)','String','registerState','0','0',NULL,'1','1','1',NULL,'EQ','input','',9,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410861121537,1561913410190032897,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',10,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410865315842,1561913410190032897,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',11,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410869510145,1561913410190032897,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',12,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410869510146,1561913410190032897,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',13,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:08'),(1561913410873704449,1561913410190032897,'remark','备注','varchar(500)','String','remark','0','0','1','1','1','1',NULL,'EQ','textarea','',14,'admin','2022-08-23 11:09:11','admin','2022-08-23 11:28:09'),(1561915444958351362,1561915444023021570,'mo_id','药品订单id','int','Long','moId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:42'),(1561915444958351363,1561915444023021570,'medicine_id','药品id','int','Long','medicineId','0','0',NULL,'1','1','1','1','EQ','input','',2,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:42'),(1561915444958351364,1561915444023021570,'medicine_num','药品数量','int','Long','medicineNum','0','0',NULL,'1','1','1',NULL,'EQ','input','',3,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:42'),(1561915444958351365,1561915444023021570,'medicine_price','药品单价','decimal(8,2)','BigDecimal','medicinePrice','0','0',NULL,'1','1','1',NULL,'EQ','input','',4,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:42'),(1561915444958351366,1561915444023021570,'mo_total_price','订单总价','decimal(8,2)','BigDecimal','moTotalPrice','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:42'),(1561915445000294402,1561915444023021570,'mo_date','订单时间','datetime','Date','moDate','0','0',NULL,'1','1','1',NULL,'EQ','datetime','',6,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:42'),(1561915445000294403,1561915444023021570,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',7,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:43'),(1561915445000294404,1561915444023021570,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',8,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:43'),(1561915445008683009,1561915444023021570,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',9,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:43'),(1561915445008683010,1561915444023021570,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',10,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:43'),(1561915445008683011,1561915444023021570,'remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',11,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:43'),(1561915445008683012,1561915444023021570,'prescription_id','处方id','int','Long','prescriptionId','0','0',NULL,'1','1','1',NULL,'EQ','input','',12,'admin','2022-08-23 11:17:16','admin','2022-08-23 17:56:43'),(1561977717441814529,1561977716590370818,'bed_id','床位id','int','Long','bedId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977717508923393,1561977716590370818,'bed_num','床位编号，唯一，命名：科室-编号','varchar(40)','String','bedNum','0','0',NULL,'1','1','1','1','EQ','input','',2,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977717508923394,1561977716590370818,'dep_id','科室id','int','Long','depId','0','0',NULL,'1','1','1',NULL,'EQ','input','',3,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977717508923395,1561977716590370818,'bed_state','床位状态，0无人，1有人','int','Long','bedState','0','0',NULL,'1','1','1','1','EQ','input','',4,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977717508923396,1561977716590370818,'soft_del','软删除 0删除/1默认','int','Long','softDel','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977717508923397,1561977716590370818,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',6,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977717508923398,1561977716590370818,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',7,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977717508923399,1561977716590370818,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',8,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977717508923400,1561977716590370818,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',9,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977717508923401,1561977716590370818,'remark','备注','varchar(500)','String','remark','0','0','1','1','1','1',NULL,'EQ','textarea','',10,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:47:05'),(1561977718742048770,1561977718414893058,'bg_id','血糖唯一id','int','Long','bgId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718742048771,1561977718414893058,'patient_id','病人id','int','Long','patientId','0','0',NULL,'1','1','1',NULL,'EQ','input','',2,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718742048772,1561977718414893058,'patient_name','病人姓名','varchar(20)','String','patientName','0','0',NULL,'1','1','1','1','LIKE','input','',3,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718742048773,1561977718414893058,'register_id','挂号id','int','Long','registerId','0','0',NULL,'1','1','1',NULL,'EQ','input','',4,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718742048774,1561977718414893058,'GLU','葡萄糖','varchar(50)','String','GLU','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718742048775,1561977718414893058,'GLU_Reference','葡萄糖参考范围','varchar(50)','String','gluReference','0','0',NULL,'1','1','1',NULL,'EQ','input','',6,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718742048776,1561977718414893058,'PH','酸碱度','varchar(50)','String','PH','0','0',NULL,'1','1','1',NULL,'EQ','input','',7,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718804963330,1561977718414893058,'PH_Reference','酸碱度参考范围','varchar(50)','String','phReference','0','0',NULL,'1','1','1',NULL,'EQ','input','',8,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718804963331,1561977718414893058,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',9,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718804963332,1561977718414893058,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',10,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718804963333,1561977718414893058,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',11,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718804963334,1561977718414893058,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',12,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977718804963335,1561977718414893058,'remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',13,'admin','2022-08-23 15:24:43','admin','2022-08-25 10:21:05'),(1561977719924842498,1561977719660601345,'br_id','血常规唯一id','int','Long','brId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:28'),(1561977719991951362,1561977719660601345,'patient_id','病人id','int','Long','patientId','0','0',NULL,'1','1','1',NULL,'EQ','input','',2,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:28'),(1561977719991951363,1561977719660601345,'patient_name','病人姓名','varchar(20)','String','patientName','0','0',NULL,'1','1','1','1','LIKE','input','',3,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:28'),(1561977719991951364,1561977719660601345,'register_id','挂号id','int','Long','registerId','0','0',NULL,'1','1','1',NULL,'EQ','input','',4,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:28'),(1561977719991951365,1561977719660601345,'WBC','白细胞','varchar(30)','String','WBC','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:29'),(1561977719991951366,1561977719660601345,'WBC_Reference','白细胞参考范围','varchar(30)','String','wbcReference','0','0',NULL,'1','1','1',NULL,'EQ','input','',6,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:29'),(1561977719991951367,1561977719660601345,'RBC','红细胞','varchar(30)','String','RBC','0','0',NULL,'1','1','1',NULL,'EQ','input','',7,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:29'),(1561977719991951368,1561977719660601345,'RBC_Reference','红细胞参考范围','varchar(30)','String','rbcReference','0','0',NULL,'1','1','1',NULL,'EQ','input','',8,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:29'),(1561977719991951369,1561977719660601345,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',9,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:29'),(1561977719991951370,1561977719660601345,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',10,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:29'),(1561977719991951371,1561977719660601345,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',11,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:30'),(1561977719991951372,1561977719660601345,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',12,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:30'),(1561977719991951373,1561977719660601345,'remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',13,'admin','2022-08-23 15:24:43','admin','2022-08-25 11:56:30'),(1561977721321545729,1561977720977612802,'dep_id','科室id','int','Long','depId','1','1','1',NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:23:58'),(1561977721321545730,1561977720977612802,'dep_name','科室名','varchar(20)','String','depName','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:23:58'),(1561977721321545731,1561977720977612802,'soft_del','软删除 0删除/1默认','int','Long','softDel','0','0','1','1','1','1','1','EQ','input','',3,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:23:58'),(1561977722139435009,1561977721745170433,'inspect_id','检查项目id','int','Long','inspectId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:04'),(1561977722143629314,1561977721745170433,'inspect_name','检查项目名','varchar(20)','String','inspectName','0','0',NULL,'1','1','1','1','LIKE','input','',2,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:04'),(1561977722143629315,1561977721745170433,'inspect_table_name','检查项目表名字','varchar(30)','String','inspectTableName','0','0',NULL,'1','1','1','1','LIKE','input','',3,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:05'),(1561977722143629316,1561977721745170433,'inspect_price','检查项目费用','decimal(8,2)','BigDecimal','inspectPrice','0','0',NULL,'1','1','1',NULL,'EQ','input','',4,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:05'),(1561977722143629317,1561977721745170433,'soft_del','软删除 0删除/1默认','int','Long','softDel','0','0',NULL,'1','1','1','1','EQ','input','',5,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:05'),(1561977722143629318,1561977721745170433,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',6,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:05'),(1561977722152017921,1561977721745170433,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',7,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:05'),(1561977722152017922,1561977721745170433,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',8,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:05'),(1561977722156212225,1561977721745170433,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',9,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:05'),(1561977722156212226,1561977721745170433,'remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',10,'admin','2022-08-23 15:24:44','admin','2022-08-25 09:57:05'),(1561977723439669249,1561977723053793282,'inspect_order_id','检查项目订单id','int','Long','inspectOrderId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:24'),(1561977723439669250,1561977723053793282,'patient_id','病人id','int','Long','patientId','0','0',NULL,'1','1','1',NULL,'EQ','input','',2,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:24'),(1561977723439669251,1561977723053793282,'user_id','开单医师id','int','Long','userId','0','0',NULL,'1','1','1',NULL,'EQ','input','',3,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:24'),(1561977723439669252,1561977723053793282,'register_id','挂号id','int','Long','registerId','0','0',NULL,'1','1','1',NULL,'EQ','input','',4,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:24'),(1561977723439669253,1561977723053793282,'enter_id','住院id','int','Long','enterId','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:24'),(1561977723439669254,1561977723053793282,'inspect_id','检查项目id','int','Long','inspectId','0','0',NULL,'1','1','1',NULL,'EQ','input','',6,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:24'),(1561977723439669255,1561977723053793282,'inspect_order_date','检查项目时间','datetime','Date','inspectOrderDate','0','0',NULL,'1','1','1',NULL,'EQ','datetime','',7,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:24'),(1561977723439669256,1561977723053793282,'inspect_order_price','检查项目价格','decimal(8,2)','BigDecimal','inspectOrderPrice','0','0',NULL,'1','1','1',NULL,'EQ','input','',8,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:24'),(1561977723439669257,1561977723053793282,'patient_name','病人姓名','varchar(20)','String','patientName','0','0',NULL,'1','1','1','1','LIKE','input','',9,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:25'),(1561977723439669258,1561977723053793282,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',10,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:25'),(1561977723439669259,1561977723053793282,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',11,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:25'),(1561977723439669260,1561977723053793282,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',12,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:25'),(1561977723439669261,1561977723053793282,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',13,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:25'),(1561977723439669262,1561977723053793282,'remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',14,'admin','2022-08-23 15:24:44','admin','2022-08-25 15:11:25'),(1561977724559548417,1561977724295307265,'lfts_id','肝功能id','int','Long','lftsId','1','1','1',NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724559548418,1561977724295307265,'patient_id','用户id','int','Long','patientId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724559548419,1561977724295307265,'patient_name','病人姓名','varchar(20)','String','patientName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724559548420,1561977724295307265,'register_id','挂号id','int','Long','registerId','0','0','1','1','1','1','1','EQ','input','',4,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724559548421,1561977724295307265,'ALT','谷丙转氨','int','Long','ALT','0','0','1','1','1','1','1','EQ','input','',5,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724559548422,1561977724295307265,'ALTReference','谷丙转氨参考值','varchar(50)','String','ALTReference','0','0','1','1','1','1','1','EQ','input','',6,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724622462978,1561977724295307265,'TP','总蛋白','int','Long','TP','0','0','1','1','1','1','1','EQ','input','',7,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724622462979,1561977724295307265,'TPReference','总蛋白参考值','varchar(50)','String','TPReference','0','0','1','1','1','1','1','EQ','input','',8,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724622462980,1561977724295307265,'AG','白蛋白与球蛋白比值','float','Long','AG','0','0','1','1','1','1','1','EQ','input','',9,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724622462981,1561977724295307265,'AGReference','白蛋白与球蛋白比值参考值','varchar(50)','String','AGReference','0','0','1','1','1','1','1','EQ','input','',10,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724622462982,1561977724295307265,'TBIL','总胆红素','float','Long','TBIL','0','0','1','1','1','1','1','EQ','input','',11,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724622462983,1561977724295307265,'TBILReference','总胆红素参考值','varchar(50)','String','TBILReference','0','0','1','1','1','1','1','EQ','input','',12,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724622462984,1561977724295307265,'DBIL','直接胆红素','float','Long','DBIL','0','0','1','1','1','1','1','EQ','input','',13,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:12'),(1561977724622462985,1561977724295307265,'DBILReference','直接胆红素参考值','varchar(50)','String','DBILReference','0','0','1','1','1','1','1','EQ','input','',14,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:13'),(1561977724622462986,1561977724295307265,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',15,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:13'),(1561977724622462987,1561977724295307265,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',16,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:13'),(1561977724622462988,1561977724295307265,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',17,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:13'),(1561977724622462989,1561977724295307265,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',18,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:13'),(1561977724622462990,1561977724295307265,'remark','备注','varchar(500)','String','remark','0','0','1','1','1','1',NULL,'EQ','textarea','',19,'admin','2022-08-23 15:24:44','admin','2022-08-23 17:28:13'),(1561977725792673793,1561977725457129474,'log_id','日志id;日志表','int','Long','logId','1','1','1',NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 15:24:45','admin','2022-08-23 15:24:45'),(1561977725792673794,1561977725457129474,'user_id','操作人id','int','Long','userId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2022-08-23 15:24:45','admin','2022-08-23 15:24:45'),(1561977725792673795,1561977725457129474,'log_details','操作内容','varchar(50)','String','logDetails','0','0','1','1','1','1','1','EQ','input','',3,'admin','2022-08-23 15:24:45','admin','2022-08-23 15:24:45'),(1561977725792673796,1561977725457129474,'log_date','操作时间','datetime','Date','logDate','0','0','1','1','1','1','1','EQ','datetime','',4,'admin','2022-08-23 15:24:45','admin','2022-08-23 15:24:45'),(1561977725792673797,1561977725457129474,'log_args','操作参数','varchar(200)','String','logArgs','0','0','1','1','1','1','1','EQ','input','',5,'admin','2022-08-23 15:24:45','admin','2022-08-23 15:24:45'),(1561977726581202945,1561977726249852929,'oo_id','手术订单id','int','Long','ooId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:16'),(1561977726581202946,1561977726249852929,'operation_id','手术项目id','int','Long','operationId','0','0',NULL,'1','1','1',NULL,'EQ','input','',2,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:16'),(1561977726581202947,1561977726249852929,'user_id','主刀医生','int','Long','userId','0','0',NULL,'1','1','1',NULL,'EQ','input','',3,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:16'),(1561977726581202948,1561977726249852929,'user_name','主刀医生姓名','varchar(20)','String','userName','0','0',NULL,'1','1','1','1','LIKE','input','',4,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202949,1561977726249852929,'operation_price','手术价格','decimal(8,2)','BigDecimal','operationPrice','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202950,1561977726249852929,'oo_date','手术订单时间','datetime','Date','ooDate','0','0',NULL,'1','1','1','1','EQ','datetime','',6,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202951,1561977726249852929,'operation_date','手术时间','datetime','Date','operationDate','0','0',NULL,'1','1','1',NULL,'EQ','datetime','',7,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202952,1561977726249852929,'opertation_confirm','家属是否已签字：是/否','varchar(2)','String','opertationConfirm','0','0',NULL,'1','1','1',NULL,'EQ','input','',8,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202953,1561977726249852929,'register_id','挂号id','int','Long','registerId','0','0',NULL,'1','1','1','1','EQ','input','',9,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202954,1561977726249852929,'enter_id','住院id','int','Long','enterId','0','0',NULL,'1','1','1','1','EQ','input','',10,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202955,1561977726249852929,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',11,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202956,1561977726249852929,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',12,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202957,1561977726249852929,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',13,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202958,1561977726249852929,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',14,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17'),(1561977726581202959,1561977726249852929,'remark','备注','varchar(500)','String','remark','0','0','1','1','1','1',NULL,'EQ','textarea','',15,'admin','2022-08-23 15:24:45','admin','2022-08-25 10:21:17');
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspect`
--

DROP TABLE IF EXISTS `inspect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inspect` (
  `inspect_id` bigint NOT NULL AUTO_INCREMENT COMMENT '检查项目id',
  `inspect_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '检查项目名',
  `inspect_table_name` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '检查项目表名字',
  `inspect_price` decimal(8,2) DEFAULT NULL COMMENT '检查项目费用',
  `soft_del` bigint DEFAULT '1' COMMENT '软删除 0删除/1默认',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`inspect_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='检查项目表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspect`
--

LOCK TABLES `inspect` WRITE;
/*!40000 ALTER TABLE `inspect` DISABLE KEYS */;
INSERT INTO `inspect` VALUES (1,'肝功能','lfts',50.00,1,'',NULL,'',NULL,NULL),(2,'血糖','blood_glucose',34.00,1,'',NULL,'',NULL,NULL),(3,'血常规','blood_routine',53.00,1,'',NULL,'',NULL,NULL);
/*!40000 ALTER TABLE `inspect` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspect_order`
--

DROP TABLE IF EXISTS `inspect_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inspect_order` (
  `inspect_order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '检查项目订单id',
  `patient_id` bigint DEFAULT NULL COMMENT '病人id',
  `user_id` bigint DEFAULT NULL COMMENT '开单医师id',
  `register_id` bigint DEFAULT NULL COMMENT '挂号id',
  `enter_id` bigint DEFAULT NULL COMMENT '住院id',
  `inspect_id` bigint DEFAULT NULL COMMENT '检查项目id',
  `inspect_order_date` datetime DEFAULT NULL COMMENT '检查项目时间',
  `inspect_order_price` decimal(8,2) DEFAULT NULL COMMENT '检查项目价格',
  `patient_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病人姓名',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`inspect_order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1562709516891463682 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='检查项目订单表;检查项目订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspect_order`
--

LOCK TABLES `inspect_order` WRITE;
/*!40000 ALTER TABLE `inspect_order` DISABLE KEYS */;
INSERT INTO `inspect_order` VALUES (1562708084213764097,1,1,1,1,1,'2022-08-25 15:46:51',1.00,'1','admin','2022-08-25 15:46:56','admin','2022-08-25 15:46:56','1'),(1562708208801370114,1,1,1,1,1,'2022-08-25 15:47:20',1.00,'1','admin','2022-08-25 15:47:25','admin','2022-08-25 15:47:25','1'),(1562709516891463681,1,1,1,1,1,'2022-08-25 15:52:32',1.00,'1','admin','2022-08-25 15:52:37','admin','2022-08-25 15:52:37','1');
/*!40000 ALTER TABLE `inspect_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lfts`
--

DROP TABLE IF EXISTS `lfts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lfts` (
  `lfts_id` int NOT NULL AUTO_INCREMENT COMMENT '肝功能id',
  `patient_id` int DEFAULT NULL COMMENT '用户id',
  `patient_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病人姓名',
  `register_id` int DEFAULT NULL COMMENT '挂号id',
  `ALT` int DEFAULT NULL COMMENT '谷丙转氨',
  `ALTReference` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '0-40(L)' COMMENT '谷丙转氨参考值',
  `TP` int DEFAULT NULL COMMENT '总蛋白',
  `TPReference` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '60-80(g/L)' COMMENT '总蛋白参考值',
  `AG` float DEFAULT NULL COMMENT '白蛋白与球蛋白比值',
  `AGReference` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '1.5-2.5 : 1' COMMENT '白蛋白与球蛋白比值参考值',
  `TBIL` float DEFAULT NULL COMMENT '总胆红素',
  `TBILReference` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '1.7-17.1(微摩尔/L)' COMMENT '总胆红素参考值',
  `DBIL` float DEFAULT NULL COMMENT '直接胆红素',
  `DBILReference` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '0-3.4(微摩尔/升)' COMMENT '直接胆红素参考值',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`lfts_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='肝功能结果表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lfts`
--

LOCK TABLES `lfts` WRITE;
/*!40000 ALTER TABLE `lfts` DISABLE KEYS */;
/*!40000 ALTER TABLE `lfts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
  `log_id` int NOT NULL AUTO_INCREMENT COMMENT '日志id;日志表',
  `user_id` int DEFAULT NULL COMMENT '操作人id',
  `log_details` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作内容',
  `log_date` datetime DEFAULT NULL COMMENT '操作时间',
  `log_args` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作参数',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日志表;日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine`
--

DROP TABLE IF EXISTS `medicine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine` (
  `medicine_id` int NOT NULL AUTO_INCREMENT COMMENT '药品id',
  `medicine_name` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '药品名',
  `medicine_price` decimal(8,2) DEFAULT NULL COMMENT '药品价格',
  `medicine_stock` int DEFAULT NULL COMMENT '药品库存',
  `medicine_details` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '药品注意事项',
  `soft_del` int DEFAULT '1' COMMENT '软删除 0删除/1默认',
  `recipel` int DEFAULT '1' COMMENT '1处方/0非处方',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`medicine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='药品表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine`
--

LOCK TABLES `medicine` WRITE;
/*!40000 ALTER TABLE `medicine` DISABLE KEYS */;
INSERT INTO `medicine` VALUES (1,'阿莫西林散热片',38.00,200,'传染病，淋巴瘤等患者禁用',1,0,'',NULL,'admin','2022-08-23 15:13:40',NULL),(2,'999感冒灵颗粒',40.00,100,'忌烟，酒及生辣，生冷油腻食物',1,1,'',NULL,'',NULL,NULL),(3,'穿心莲内酯滴丸',26.00,100,'脾胃虚寒大便溏者慎用',1,0,'',NULL,'admin','2022-08-23 15:16:18',NULL),(4,'驴胶补血颗粒',45.00,100,'过敏体质慎用',1,0,'',NULL,'admin','2022-08-23 15:33:01',NULL),(5,'维c银翘片',55.00,100,'肝脏功能不全者慎用',1,0,'',NULL,'admin','2022-08-23 17:07:35',NULL),(6,'西瓜霜润喉片',34.00,100,'对本品过敏者禁用，过敏体质者慎用',1,1,'',NULL,'',NULL,NULL),(7,'健胃消食片',34.00,299,'消食',1,0,'',NULL,'',NULL,NULL),(8,'六味地黄丸',67.00,476,'六味地黄丸，谨遵医嘱',1,0,'',NULL,'',NULL,NULL),(16,'枇杷止咳糖浆',20.00,100,'忌烟、酒及辛辣、生冷、油腻食物',1,0,'',NULL,'admin','2022-08-23 18:02:49',NULL);
/*!40000 ALTER TABLE `medicine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine_order`
--

DROP TABLE IF EXISTS `medicine_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine_order` (
  `mo_id` bigint NOT NULL AUTO_INCREMENT COMMENT '药品订单id',
  `medicine_id` bigint DEFAULT NULL COMMENT '药品id',
  `medicine_num` int DEFAULT NULL COMMENT '药品数量',
  `medicine_price` decimal(8,2) DEFAULT NULL COMMENT '药品单价',
  `mo_total_price` decimal(8,2) DEFAULT NULL COMMENT '订单总价',
  `mo_date` datetime DEFAULT NULL COMMENT '订单时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `prescription_id` bigint DEFAULT NULL COMMENT '处方id',
  PRIMARY KEY (`mo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1564169766227750915 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='药品订单表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine_order`
--

LOCK TABLES `medicine_order` WRITE;
/*!40000 ALTER TABLE `medicine_order` DISABLE KEYS */;
INSERT INTO `medicine_order` VALUES (1564165847841845249,1,3,38.00,114.00,'2022-08-29 16:19:34','admin','2022-08-29 16:19:34','admin','2022-08-29 16:19:34',NULL,1562684975235538946),(1564166546659028994,2,6,40.00,240.00,'2022-08-29 16:22:20','admin','2022-08-29 16:22:20','admin','2022-08-29 16:22:20',NULL,1563019283616292865),(1564166672098078721,8,7,67.00,469.00,'2022-08-29 16:22:50','admin','2022-08-29 16:22:50','admin','2022-08-29 16:22:50',NULL,1562684975235538946),(1564169765497942018,1,1,38.00,38.00,'2022-08-29 16:35:08','admin','2022-08-29 16:35:08','admin','2022-08-29 16:35:08',NULL,1564169284155928578),(1564169766227750914,7,2,34.00,68.00,'2022-08-29 16:35:08','admin','2022-08-29 16:35:08','admin','2022-08-29 16:35:08',NULL,1564169284155928578);
/*!40000 ALTER TABLE `medicine_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation`
--

DROP TABLE IF EXISTS `operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation` (
  `operation_id` bigint NOT NULL AUTO_INCREMENT COMMENT '手术项目id',
  `operation_name` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手术名称',
  `dep_id` bigint DEFAULT NULL COMMENT '科室id',
  `operation_price` decimal(8,2) DEFAULT NULL COMMENT '手术费用',
  `soft_del` int DEFAULT '1' COMMENT '软删除 0删除/1默认',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`operation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1562385872274870275 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='手术项目表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation`
--

LOCK TABLES `operation` WRITE;
/*!40000 ALTER TABLE `operation` DISABLE KEYS */;
INSERT INTO `operation` VALUES (1,'心脏搭桥',1562373743204098049,500000.00,1,'',NULL,'',NULL,'存在一定的风险,对高血压、高血脂、高血糖、高尿酸等，有良好的调节作用，对心脑血管尤其是冠心病、心肌损伤、脑梗等病症有良好的预防及康复治疗作用。'),(2,'骨囊肿手术',1562373498806198273,200000.00,1,'',NULL,'admin','2022-08-24 17:09:49','会导致急性的呼吸心跳骤停，引发生命危险'),(3,'支气管切除',1562373793095344129,30000.00,1,'',NULL,'admin','2022-08-24 19:10:21','晚期多由于瘢痕狭窄、肿瘤复发或纵隔肿大的淋巴结压迫引起，往往须切除余肺。否则，会发生肺不张并发肺内感染。'),(1562365649127862273,'全飞秒',1562373635540508674,15000.00,1,'admin','2022-08-24 17:06:13','admin','2022-08-24 19:12:36','由于术中损伤角膜神经，术后可能会发生干眼症，但这种情况会随着角膜的恢复而逐渐改善，都在1~3个月左右恢复。术后出现眩光、单眼复视等症状，也能在3个月左右恢复'),(1562367184763219969,'经皮穿刺肺活检',1562373793095344129,3500.00,1,'admin','2022-08-24 17:12:19','admin','2022-08-24 19:18:24','双侧病变或不能手术的恶性病变，需要病理类型诊断指导放疗或化疗者；'),(1562383806408183809,'半分秒',1562373635540508674,12000.00,1,'admin','2022-08-24 18:18:22','admin','2022-08-24 19:14:31','角膜瓣的存在，半飞秒术后如果角膜瓣遭到外力挤压或者碰撞，有可能会发生角膜瓣移位或丢失，从而导致更加严重的并发症。'),(1562385872274870274,'经皮冠状动脉支架植入术',1562373550115119106,180000.00,1,'admin','2022-08-24 18:26:34','admin','2022-08-24 19:16:52','但由于支架本身比较小，而血管内皮病变比较重，所以部分患者可能在植入支架以后再次发生血栓，尤其是支架内血栓，表现为胸闷、胸痛等，甚至再次发生心肌梗死');
/*!40000 ALTER TABLE `operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_order`
--

DROP TABLE IF EXISTS `operation_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_order` (
  `oo_id` bigint NOT NULL AUTO_INCREMENT COMMENT '手术订单id',
  `operation_id` int DEFAULT NULL COMMENT '手术项目id',
  `user_id` bigint DEFAULT NULL COMMENT '主刀医生',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主刀医生姓名',
  `operation_price` decimal(8,2) DEFAULT NULL COMMENT '手术价格',
  `oo_date` datetime DEFAULT NULL COMMENT '手术订单时间',
  `operation_date` datetime DEFAULT NULL COMMENT '手术时间',
  `opertation_confirm` varchar(2) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '家属是否已签字：是/否',
  `register_id` int DEFAULT NULL COMMENT '挂号id',
  `enter_id` int DEFAULT NULL COMMENT '住院id',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`oo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1562636789807153155 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='手术订单表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_order`
--

LOCK TABLES `operation_order` WRITE;
/*!40000 ALTER TABLE `operation_order` DISABLE KEYS */;
INSERT INTO `operation_order` VALUES (1562633997512179713,1,1,'疯狂的狮子Li',11.00,'2022-08-25 10:50:44','2022-08-25 10:50:46','是',1,1,'admin','2022-08-25 10:52:32','admin','2022-08-25 22:36:57','111'),(1562636789807153154,2,1562376172041658370,'心内科医生',2.00,'2022-08-25 11:03:33','2022-08-25 11:03:34','是',2,2,'admin','2022-08-25 11:03:38','admin','2022-08-26 10:01:51','2');
/*!40000 ALTER TABLE `operation_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `register`
--

DROP TABLE IF EXISTS `register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `register` (
  `register_id` bigint NOT NULL AUTO_INCREMENT COMMENT '挂号id',
  `patient_id` bigint NOT NULL COMMENT '用户id',
  `user_id` bigint NOT NULL COMMENT '医生id',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '医生姓名',
  `register_date` datetime NOT NULL COMMENT '挂号时间',
  `dep_id` bigint DEFAULT NULL COMMENT '科室id',
  `dep_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '科室名称',
  `register_price` decimal(8,2) DEFAULT '5.00' COMMENT '挂号价格',
  `register_state` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否完成0未完成，1已完成',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `patient_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户姓名',
  PRIMARY KEY (`register_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1565905249567346690 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='挂号表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `register`
--

LOCK TABLES `register` WRITE;
/*!40000 ALTER TABLE `register` DISABLE KEYS */;
INSERT INTO `register` VALUES (1564157460513107969,1562649395789361153,1562376351004221441,'yan','2022-08-29 15:46:14',1562373635540508674,'眼科',5.00,'1','admin','2022-08-29 15:46:14','admin','2022-08-29 16:22:16',NULL,'建档用户88'),(1564168748647657473,1564168733460590593,1562376351004221441,'yan','2022-08-29 16:31:05',1562373635540508674,'眼科',5.00,'1','admin','2022-08-29 16:31:05','yan','2022-08-29 16:36:10',NULL,'测试1234'),(1564168855338168321,1562649395789361153,1562376351004221441,'yan','2022-08-29 16:31:31',1562373635540508674,'眼科',5.00,'1','admin','2022-08-29 16:31:31','yan','2022-08-29 16:39:31',NULL,'建档用户88'),(1564168878994042881,1562651264792195073,1562376351004221441,'yan','2022-08-29 16:31:36',1562373635540508674,'眼科',5.00,'1','admin','2022-08-29 16:31:36','yan','2022-08-29 16:37:09',NULL,'建档用户999'),(1565905249567346689,1562649395789361153,1562376351004221441,'yan','2022-09-03 11:31:19',1562373635540508674,'眼科',5.00,'0','admin','2022-09-03 11:31:19','admin','2022-09-03 11:31:19',NULL,'建档用户88');
/*!40000 ALTER TABLE `register` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'treatment'
--

--
-- Dumping routines for database 'treatment'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-10-22 12:44:18
