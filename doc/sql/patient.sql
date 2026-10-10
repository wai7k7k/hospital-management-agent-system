-- MySQL dump 10.13  Distrib 8.0.24, for Linux (x86_64)
--
-- Host: localhost    Database: patient
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
-- Table structure for table `enter`
--

DROP TABLE IF EXISTS `enter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enter` (
  `enter_id` bigint NOT NULL AUTO_INCREMENT COMMENT '住院id',
  `patient_id` bigint DEFAULT NULL COMMENT '病人id',
  `patient_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病人姓名',
  `record_id` bigint DEFAULT NULL COMMENT '病历id',
  `user_id` bigint DEFAULT NULL COMMENT '主治医师id',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主治医生名字',
  `dep_id` bigint DEFAULT NULL COMMENT '科室id',
  `dep_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '科室名称',
  `bed_id` bigint DEFAULT NULL COMMENT '床位id',
  `enter_date` datetime DEFAULT NULL COMMENT '入院日期',
  `out_apply` bigint DEFAULT '0' COMMENT '出院申请：0无/1申请/2已出',
  `out_data` datetime DEFAULT NULL COMMENT '出院日期',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `register_id` bigint DEFAULT NULL COMMENT '挂号id',
  PRIMARY KEY (`enter_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1565905606775209986 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='住院表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enter`
--

LOCK TABLES `enter` WRITE;
/*!40000 ALTER TABLE `enter` DISABLE KEYS */;
INSERT INTO `enter` VALUES (1563864502557343745,1562649395789361153,'建档用户88',1562974333155422210,1562376351004221441,'yan',1562373635540508674,'眼科',3,'2022-08-28 20:23:59',2,'2022-08-28 22:02:06','admin','2022-08-28 20:22:07','admin','2022-08-28 22:02:06',NULL,1563864371334475777),(1563885741569081345,1562651264792195073,'建档用户999',1563865554295848962,1562376351004221441,'yan',1562373635540508674,'眼科',3,'2022-08-28 22:08:08',2,'2022-08-28 22:08:13','admin','2022-08-28 21:46:31','admin','2022-08-28 22:08:13',NULL,1563885697176604673),(1564157600603369474,1562649395789361153,'建档用户88',1564157579808010242,1562376351004221441,'yan',1562373635540508674,'眼科',5,'2022-08-29 15:47:39',2,'2022-08-29 15:48:20','admin','2022-08-29 15:46:47','admin','2022-08-29 15:48:20',NULL,1564157460513107969),(1564170249760206849,1562651264792195073,'建档用户999',1564170224137203714,1562376351004221441,'yan',1562373635540508674,'眼科',1564158219745046530,'2022-08-29 16:37:45',2,'2022-08-29 16:38:58','yan','2022-08-29 16:37:03','admin','2022-08-29 16:38:58',NULL,1564168878994042881),(1565905606775209985,1562649395789361153,'建档用户88',1564157579808010242,1562376351004221441,'yan',1562373635540508674,'眼科',NULL,NULL,0,NULL,'yan','2022-09-03 11:32:44','yan','2022-09-03 11:32:44',NULL,1565905249567346689);
/*!40000 ALTER TABLE `enter` ENABLE KEYS */;
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
INSERT INTO `gen_table` VALUES (1561992997724491778,'enter','住院表;',NULL,NULL,'Enter','crud','xian.woniuxy.customer','customer-serv','enter','住院;','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 11:15:45','admin','2022-08-23 18:01:14','住院表;'),(1561993000400457729,'notice','公告表;公告表',NULL,NULL,'Notice','crud','xian.woniuxy.customer','customer-serv','notice','公告;公告','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:31:56','admin','2022-08-23 18:01:41',NULL),(1561993002656993282,'patient','用户表',NULL,NULL,'Patient','crud','xian.woniuxy.customer','customer-serv','patient','用户','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:32:21','admin','2022-08-23 18:02:02',NULL),(1561993006775799810,'record','病历表',NULL,NULL,'Record','crud','xian.woniuxy.customer','customer.serv','record','病历','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:32:58','admin','2022-08-24 19:08:06',NULL),(1562269716522790913,'prescription','处方表',NULL,NULL,'Prescription','crud','xian.woniuxy.customer','customer-serv','prescription','处方','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 10:32:41','admin','2022-08-24 11:03:31',NULL);
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
INSERT INTO `gen_table_column` VALUES (1561992998085201922,1561992997724491778,'enter_id','住院id','int','Long','enterId','1','1','1',NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:14'),(1561992998152310785,1561992997724491778,'patient_id','病人id','int','Long','patientId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:14'),(1561992998152310786,1561992997724491778,'patient_name','病人姓名','varchar(20)','String','patientName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:14'),(1561992998152310787,1561992997724491778,'record_id','病历id','int','Long','recordId','0','0','1','1','1','1','1','EQ','input','',4,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:14'),(1561992998152310788,1561992997724491778,'user_id','主治医师id','int','Long','userId','0','0','1','1','1','1','1','EQ','input','',5,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:14'),(1561992998152310789,1561992997724491778,'user_name','主治医生名字','varchar(20)','String','userName','0','0','1','1','1','1','1','LIKE','input','',6,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:14'),(1561992998152310790,1561992997724491778,'dep_id','科室id','varchar(32)','String','depId','0','0','1','1','1','1','1','EQ','input','',7,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:14'),(1561992999117000705,1561992997724491778,'dep_name','科室名称','varchar(20)','String','depName','0','0','1','1','1','1','1','LIKE','input','',8,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:14'),(1561992999150555138,1561992997724491778,'bed_id','床位id','int','Long','bedId','0','0','1','1','1','1','1','EQ','input','',9,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:14'),(1561992999150555139,1561992997724491778,'enter_date','入院日期','datetime','Date','enterDate','0','0','1','1','1','1','1','EQ','datetime','',10,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:15'),(1561992999150555140,1561992997724491778,'out_apply','出院申请：0无/1申请/2已出','int','Long','outApply','0','0','1','1','1','1','1','EQ','input','',11,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:15'),(1561992999150555141,1561992997724491778,'out_data','出院日期','datetime','Date','outData','0','0','1','1','1','1','1','EQ','datetime','',12,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:15'),(1561992999150555142,1561992997724491778,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',13,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:15'),(1561992999150555143,1561992997724491778,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',14,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:15'),(1561992999150555144,1561992997724491778,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',15,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:15'),(1561992999150555145,1561992997724491778,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',16,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:15'),(1561992999150555146,1561992997724491778,'remark','备注','varchar(500)','String','remark','0','0','1','1','1','1',NULL,'EQ','textarea','',17,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:15'),(1561992999150555147,1561992997724491778,'register_id','挂号id','int','Long','registerId','0','0','1','1','1','1','1','EQ','input','',18,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:15'),(1561993000731807746,1561993000400457729,'notice_id','公告id','int','Long','noticeId','1','1','1',NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:41'),(1561993000731807747,1561993000400457729,'user_id','发布人id','int','Long','userId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:42'),(1561993000731807748,1561993000400457729,'notice_details','公告内容','varchar(200)','String','noticeDetails','0','0','1','1','1','1','1','EQ','input','',3,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:42'),(1561993000731807749,1561993000400457729,'notice_date','公告时间','datetime','Date','noticeDate','0','0','1','1','1','1','1','EQ','datetime','',4,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:42'),(1561993000731807750,1561993000400457729,'soft_del','软删除 0删除/1默认','int','Long','softDel','0','0','1','1','1','1','1','EQ','input','',5,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:42'),(1561993000731807751,1561993000400457729,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',6,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:42'),(1561993000731807752,1561993000400457729,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',7,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:42'),(1561993000731807753,1561993000400457729,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',8,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:42'),(1561993000731807754,1561993000400457729,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',9,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:42'),(1561993000731807755,1561993000400457729,'remark','备注','varchar(500)','String','remark','0','0','1','1','1','1',NULL,'EQ','textarea','',10,'admin','2022-08-23 16:25:26','admin','2022-08-23 18:01:42'),(1561993003047063554,1561993002656993282,'patient_id','病人唯一id','int','Long','patientId','1','1','1',NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:02'),(1561993003047063555,1561993002656993282,'patient_phone','手机号/账号','varchar(20)','String','patientPhone','0','0','1','1','1','1','1','EQ','input','',2,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:02'),(1561993003047063556,1561993002656993282,'patient_password','密码','varchar(40)','String','patientPassword','0','0','1','1','1','1','1','EQ','input','',3,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:02'),(1561993003047063557,1561993002656993282,'patient_name','名字','varchar(20)','String','patientName','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:02'),(1561993003114172418,1561993002656993282,'patient_sex','性别','varchar(2)','String','patientSex','0','0','1','1','1','1','1','EQ','select','',5,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:02'),(1561993003114172419,1561993002656993282,'patient_card_id','身份证','varchar(18)','String','patientCardId','0','0','1','1','1','1','1','EQ','input','',6,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:02'),(1561993003114172420,1561993002656993282,'patient_photo','头像','varchar(150)','String','patientPhoto','0','0','1','1','1','1','1','EQ','input','',7,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:02'),(1561993003114172421,1561993002656993282,'patient_birthday','生日年龄','date','Date','patientBirthday','0','0','1','1','1','1','1','EQ','datetime','',8,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:02'),(1561993003114172422,1561993002656993282,'patient_register_date','注册时间','datetime','Date','patientRegisterDate','0','0','1','1','1','1','1','EQ','datetime','',9,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:02'),(1561993003114172423,1561993002656993282,'patient_money','账户余额','decimal(8,2)','BigDecimal','patientMoney','0','0','1','1','1','1','1','EQ','input','',10,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:03'),(1561993003114172424,1561993002656993282,'patient_test','','varchar(100)','String','patientTest','0','0','1','1','1','1','1','EQ','input','',11,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:03'),(1561993003114172425,1561993002656993282,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',12,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:03'),(1561993003114172426,1561993002656993282,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',13,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:03'),(1561993003114172427,1561993002656993282,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',14,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:03'),(1561993003114172428,1561993002656993282,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',15,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:03'),(1561993003114172429,1561993002656993282,'remark','备注','varchar(500)','String','remark','0','0','1','1','1','1',NULL,'EQ','textarea','',16,'admin','2022-08-23 16:25:27','admin','2022-08-23 18:02:03'),(1561993007111344129,1561993006775799810,'record_id','病历id','int','Long','recordId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007111344130,1561993006775799810,'patient_id','病人id','int','Long','patientId','0','0',NULL,'1','1','1',NULL,'EQ','input','',2,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007111344131,1561993006775799810,'patient_name','病人姓名','varchar(20)','String','patientName','0','0',NULL,'1','1','1','1','LIKE','input','',3,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007111344132,1561993006775799810,'record_desc','病情描述','varchar(200)','String','recordDesc','0','0',NULL,'1','1','1',NULL,'EQ','input','',4,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007111344133,1561993006775799810,'record_health','健康状况','varchar(5)','String','recordHealth','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178452994,1561993006775799810,'record_Infect','感染病史;感染病史(有/无，有就填备注)','varchar(2)','String','recordInfect','0','0',NULL,'1','1','1',NULL,'EQ','input','',6,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178452995,1561993006775799810,'record_Infect_details','感染病史备注','varchar(50)','String','recordInfectDetails','0','0',NULL,'1','1','1',NULL,'EQ','input','',7,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178452996,1561993006775799810,'record_allergy','过敏史;过敏史(有/无，有就填备注)','varchar(2)','String','recordAllergy','0','0',NULL,'1','1','1',NULL,'EQ','input','',8,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178452997,1561993006775799810,'record_allergy_details','过敏史备注','varchar(50)','String','recordAllergyDetails','0','0',NULL,'1','1','1',NULL,'EQ','input','',9,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178452998,1561993006775799810,'record_inherit','遗传病史;遗传病史(有/无，有就填备注)','varchar(2)','String','recordInherit','0','0',NULL,'1','1','1',NULL,'EQ','input','',10,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178452999,1561993006775799810,'record_inherit_details','遗传病史备注','varchar(50)','String','recordInheritDetails','0','0',NULL,'1','1','1',NULL,'EQ','input','',11,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178453000,1561993006775799810,'record_date','记录日期;最近一次记录日期','datetime','Date','recordDate','0','0',NULL,'1','1','1',NULL,'EQ','datetime','',12,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178453001,1561993006775799810,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',13,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178453002,1561993006775799810,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',14,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178453003,1561993006775799810,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',15,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178453004,1561993006775799810,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',16,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:06'),(1561993007178453005,1561993006775799810,'remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',17,'admin','2022-08-23 16:25:28','admin','2022-08-24 19:08:07'),(1562269716992552961,1562269716522790913,'prescription_id','处方id;','int','Long','prescriptionId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:31'),(1562269716992552962,1562269716522790913,'patient_id','病人id;','int','Long','patientId','0','0',NULL,'1','1','1',NULL,'EQ','input','',2,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:31'),(1562269716992552963,1562269716522790913,'patient_name','病人姓名','varchar(20)','String','patientName','0','0',NULL,'1','1','1','1','LIKE','input','',3,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:31'),(1562269716992552964,1562269716522790913,'register_id','挂号id','int','Long','registerId','0','0',NULL,'1','1','1',NULL,'EQ','input','',4,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:31'),(1562269716992552965,1562269716522790913,'record_id','病历id;','int','Long','recordId','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:31'),(1562269716992552966,1562269716522790913,'dep_id','科室id;','int','Long','depId','0','0',NULL,'1','1','1',NULL,'EQ','input','',6,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269716992552967,1562269716522790913,'dep_name','科室名称','varchar(20)','String','depName','0','0',NULL,'1','1','1','1','LIKE','input','',7,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269716992552968,1562269716522790913,'prescription_date','日期;','datetime','Date','prescriptionDate','0','0',NULL,'1','1','1','1','EQ','datetime','',8,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269716992552969,1562269716522790913,'user_id','医生id;','int','Long','userId','0','0',NULL,'1','1','1',NULL,'EQ','input','',9,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269716992552970,1562269716522790913,'user_name','医生姓名','varchar(20)','String','userName','0','0',NULL,'1','1','1','1','LIKE','input','',10,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269717059661825,1562269716522790913,'Rp_medicine','药品及用量;','varchar(200)','String','rpMedicine','0','0',NULL,'1','1','1',NULL,'EQ','input','',11,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269717059661826,1562269716522790913,'Rp_inspect','检查项目','varchar(255)','String','rpInspect','0','0',NULL,'1','1','1',NULL,'EQ','input','',12,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269717059661827,1562269716522790913,'diagnosis','临床诊断;','varchar(150)','String','diagnosis','0','0',NULL,'1','1','1',NULL,'EQ','input','',13,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269717059661828,1562269716522790913,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',14,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269717059661829,1562269716522790913,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',15,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269717059661830,1562269716522790913,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',16,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269717059661831,1562269716522790913,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',17,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32'),(1562269717059661832,1562269716522790913,'remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',18,'admin','2022-08-24 10:45:01','admin','2022-08-24 11:03:32');
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告id',
  `user_id` int DEFAULT NULL COMMENT '发布人id',
  `notice_details` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公告内容',
  `notice_date` datetime DEFAULT NULL COMMENT '公告时间',
  `soft_del` int DEFAULT '1' COMMENT '软删除 0删除/1默认',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公告表;公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,1,'一个公告','2022-08-16 17:29:17',1,'',NULL,'',NULL,NULL),(2,2,'俩个公告','2022-08-09 17:29:37',0,'',NULL,'',NULL,NULL);
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `patient_id` bigint NOT NULL AUTO_INCREMENT COMMENT '病人唯一id',
  `patient_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号/账号',
  `patient_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `patient_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名字',
  `patient_sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别',
  `patient_card_id` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份证',
  `patient_photo` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `patient_birthday` date DEFAULT NULL COMMENT '生日年龄',
  `patient_register_date` datetime DEFAULT NULL COMMENT '注册时间',
  `patient_money` decimal(8,2) DEFAULT '0.00' COMMENT '账户余额',
  `patient_test` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`patient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1564168733460590594 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,'12345678911','c9e8bf059aac88ceb82847fed381b56b','加密测试','男','666666',NULL,NULL,NULL,225.00,'test测试','',NULL,'',NULL,NULL),(2,'12353546642','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','patient','男','123456',NULL,NULL,NULL,3456.00,NULL,'',NULL,'',NULL,NULL),(1562649395789361153,'158888','123456','建档用户88','男','888888',NULL,'2022-08-02','2022-08-25 11:53:43',0.00,NULL,'admin','2022-08-25 11:53:43','admin','2022-08-25 11:53:43','无'),(1562651264792195073,'1589999','123456','建档用户999','男','999999',NULL,'2022-08-02','2022-08-25 12:01:09',0.00,NULL,'admin','2022-08-25 12:01:09','admin','2022-08-25 12:01:09','无'),(1562987784997203970,'15811111','123456','建档用户11','男','111111',NULL,'2022-08-10','2022-08-26 10:18:22',0.00,NULL,'admin','2022-08-26 10:18:22','admin','2022-08-26 10:18:22',''),(1562987896599244801,'15822222','123456','建档用户22','男','222222',NULL,'2022-08-10','2022-08-26 10:18:48',0.00,NULL,'admin','2022-08-26 10:18:48','admin','2022-08-26 10:18:48',''),(1562987975263416322,'15833333','123456','建档用户33','男','333333',NULL,'2022-08-10','2022-08-26 10:19:07',0.00,NULL,'admin','2022-08-26 10:19:07','admin','2022-08-26 10:19:07',''),(1562988037959872513,'15844444','123456','建档用户44','男','444444',NULL,'2022-08-10','2022-08-26 10:19:22',0.00,NULL,'admin','2022-08-26 10:19:22','admin','2022-08-26 10:19:22',''),(1562988083241578497,'15855555','123456','建档用户55','男','555555',NULL,'2022-08-10','2022-08-26 10:19:33',0.00,NULL,'admin','2022-08-26 10:19:33','admin','2022-08-26 10:19:33',''),(1562988374666014721,'15877777','123456','建档用户77','男','777777',NULL,'2022-08-10','2022-08-26 10:20:42',0.00,NULL,'admin','2022-08-26 10:20:42','admin','2022-08-26 10:20:42',''),(1564168733460590593,'1234','123456','测试1234','男','1234',NULL,'2022-08-01','2022-08-29 16:31:02',0.00,NULL,'admin','2022-08-29 16:31:02','admin','2022-08-29 16:31:02','');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription`
--

DROP TABLE IF EXISTS `prescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription` (
  `prescription_id` bigint NOT NULL AUTO_INCREMENT COMMENT '处方id;',
  `patient_id` bigint DEFAULT NULL COMMENT '病人id;',
  `patient_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病人姓名',
  `register_id` bigint DEFAULT NULL COMMENT '挂号id',
  `record_id` bigint DEFAULT NULL COMMENT '病历id;',
  `dep_id` bigint DEFAULT NULL COMMENT '科室id;',
  `dep_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '科室名称',
  `prescription_date` datetime DEFAULT NULL COMMENT '日期;',
  `user_id` bigint DEFAULT NULL COMMENT '医生id;',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '医生姓名',
  `Rp_medicine` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '药品及用量;',
  `Rp_inspect` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '检查项目',
  `diagnosis` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '临床诊断;',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`prescription_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1564169284155928579 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='处方表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription`
--

LOCK TABLES `prescription` WRITE;
/*!40000 ALTER TABLE `prescription` DISABLE KEYS */;
INSERT INTO `prescription` VALUES (1562684975235538946,1562649395789361153,'建档用户88',1562669818337931265,NULL,1562373793095344129,'呼吸科','2022-08-25 14:15:06',1562376565307990018,'huxi','阿莫西陵*1\n头孢*2','无','普通感冒','admin','2022-08-25 14:15:06','admin','2022-08-25 14:15:06','多喝水'),(1563019283616292865,1562987784997203970,'建档用户11',1563016311863889921,NULL,1562373635540508674,'眼科','2022-08-26 12:23:31',1562376351004221441,'yan','阿莫西林*1','无','发炎','yan','2022-08-26 12:23:31','yan','2022-08-26 12:23:31','多喝45度温水'),(1563706134156185601,1562649395789361153,'建档用户88',1563705974818791425,NULL,1562373498806198273,'骨科','2022-08-28 09:52:49',5,'ptuser','阿莫西林*1','无','发炎','admin','2022-08-28 09:52:49','admin','2022-08-28 09:52:49','多喝水'),(1564169284155928578,1564168733460590593,'测试1234',1564168748647657473,NULL,1562373635540508674,'眼科','2022-08-29 16:33:13',1562376351004221441,'yan','阿莫西林*1\n健胃消食片*2','无','感冒','yan','2022-08-29 16:33:13','yan','2022-08-29 16:33:13','多喝水');
/*!40000 ALTER TABLE `prescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `record` (
  `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '病历id',
  `patient_id` bigint DEFAULT NULL COMMENT '病人id',
  `patient_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病人姓名',
  `record_desc` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病情描述',
  `record_health` varchar(5) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '健康状况',
  `record_Infect` varchar(2) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '感染病史;感染病史(有/无，有就填备注)',
  `record_Infect_details` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '感染病史备注',
  `record_allergy` varchar(2) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '过敏史;过敏史(有/无，有就填备注)',
  `record_allergy_details` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '过敏史备注',
  `record_inherit` varchar(2) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '遗传病史;遗传病史(有/无，有就填备注)',
  `record_inherit_details` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '遗传病史备注',
  `record_date` datetime DEFAULT NULL COMMENT '记录日期;最近一次记录日期',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1564170224137203715 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='病历表;病历表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `record`
--

LOCK TABLES `record` WRITE;
/*!40000 ALTER TABLE `record` DISABLE KEYS */;
INSERT INTO `record` VALUES (1564157579808010242,1562649395789361153,'建档用户88','良好','亚健康','','','','','无','','2022-08-29 15:46:42','admin','2022-08-29 15:46:42','admin','2022-08-29 15:46:42','111'),(1564170224137203714,1562651264792195073,'建档用户999','生病','亚健康','','','','','有','遗传了帅气','2022-08-29 16:36:57','yan','2022-08-29 16:36:57','yan','2022-08-29 16:36:57','');
/*!40000 ALTER TABLE `record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'patient'
--

--
-- Dumping routines for database 'patient'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-10-22 12:44:01
