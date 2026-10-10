-- MySQL dump 10.13  Distrib 8.0.24, for Linux (x86_64)
--
-- Host: localhost    Database: ry_cloud
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
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
INSERT INTO `gen_table` VALUES (1562053457908260865,'schedule','排班表',NULL,NULL,'Schedule','crud','com.ruoyi.system','system','schedule','排班','ataoxz','0','/','{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null}','admin','2022-08-23 17:58:59','admin','2022-08-23 20:27:31',NULL);
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
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
INSERT INTO `gen_table_column` VALUES (1562053458382217217,1562053457908260865,'schedule_id','排班id','int','Long','scheduleId','1','1',NULL,NULL,'1','1',NULL,'EQ','input','',1,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:31'),(1562053458394800129,1562053457908260865,'user_id','医生id','int','Long','userId','0','0',NULL,'1','1','1','1','EQ','input','',2,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:31'),(1562053458411577345,1562053457908260865,'user_name','医生姓名','varchar(20)','String','userName','0','0',NULL,'1','1','1','1','LIKE','input','',3,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:31'),(1562053458419965954,1562053457908260865,'dep_id','科室id','int','Long','depId','0','0',NULL,'1','1','1','1','EQ','input','',4,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:31'),(1562053458424160258,1562053457908260865,'dep_name','科室名称','varchar(20)','String','depName','0','0',NULL,'1','1','1','1','LIKE','input','',5,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32'),(1562053458428354562,1562053457908260865,'avatar','医生头像','varchar(100)','String','avatar','0','0',NULL,'1','1','1','1','EQ','input','',6,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32'),(1562053458432548865,1562053457908260865,'schedule_date','排班日期','date','Date','scheduleDate','0','0',NULL,'1','1','1','1','EQ','datetime','',7,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32'),(1562053458436743169,1562053457908260865,'schedule_time','上午，下午','varchar(5)','String','scheduleTime','0','0',NULL,'1','1','1','1','EQ','input','',8,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32'),(1562053458440937474,1562053457908260865,'schedule_num','接诊人数','int','Long','scheduleNum','0','0',NULL,'1','1','1','1','EQ','input','',9,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32'),(1562053458445131778,1562053457908260865,'create_by','创建者','varchar(64)','String','createBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',10,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32'),(1562053458453520385,1562053457908260865,'create_time','创建时间','datetime','Date','createTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',11,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32'),(1562053458457714689,1562053457908260865,'update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','input','',12,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32'),(1562053458461908994,1562053457908260865,'update_time','更新时间','datetime','Date','updateTime','0','0',NULL,NULL,NULL,NULL,NULL,'EQ','datetime','',13,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32'),(1562053458461908995,1562053457908260865,'remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',14,'admin','2022-08-23 20:25:41','admin','2022-08-23 20:27:32');
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `schedule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '排班id',
  `user_id` bigint DEFAULT NULL COMMENT '医生id',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '医生姓名',
  `dep_id` bigint DEFAULT NULL COMMENT '科室id',
  `dep_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '科室名称',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '医生头像',
  `schedule_date` date DEFAULT NULL COMMENT '排班日期',
  `schedule_time` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上午，下午',
  `schedule_num` int DEFAULT NULL COMMENT '接诊人数',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`schedule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1565904702411931651 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='排班表;';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1564168562062983169,1562376351004221441,'yan',1562373635540508674,'眼科','','2022-08-30','上午',3,'admin','2022-08-29 16:30:21','admin','2022-08-29 16:30:21',NULL),(1565904702411931650,1562376351004221441,'yan',1562373635540508674,'眼科','','2022-08-30','下午',5,'admin','2022-09-03 11:29:09','admin','2022-09-03 11:29:09',NULL);
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `config_id` bigint NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2022-08-21 01:31:07','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2022-08-21 01:31:07','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2022-08-21 01:31:07','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2022-08-21 01:31:07','',NULL,'是否开启注册用户功能（true开启，false关闭）'),(11,'OSS预览列表资源开关','sys.oss.previewListResource','true','Y','admin','2022-08-21 01:31:07','',NULL,'true:开启, false:关闭');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(500) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,0,'0','管理',0,'若依','15888888888','ry@qq.com','0','0','admin','2022-08-21 01:31:06','admin','2022-08-24 17:09:18'),(101,100,'0,100','行政部',1,'若依','15888888888','ry@qq.com','0','2','admin','2022-08-21 01:31:06','admin','2022-08-21 20:10:06'),(102,100,'0,100','门诊住院部',2,'若依','15888888888','ry@qq.com','0','2','admin','2022-08-21 01:31:06','admin','2022-08-21 20:11:47'),(103,101,'0,100,101','系统管理',1,'若依','15888888888','ry@qq.com','0','2','admin','2022-08-21 01:31:06','admin','2022-08-21 20:10:35'),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','2','admin','2022-08-21 01:31:06','admin','2022-08-21 20:10:06'),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','2','admin','2022-08-21 01:31:06','admin','2022-08-21 20:10:06'),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','2','admin','2022-08-21 01:31:06','admin','2022-08-21 20:10:06'),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','2','admin','2022-08-21 01:31:06','admin','2022-08-21 20:10:06'),(108,102,'0,100,102','骨科',1,'若依','15888888888','ry@qq.com','0','2','admin','2022-08-21 01:31:06','admin','2022-08-21 20:11:58'),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','2','admin','2022-08-21 01:31:06','',NULL),(1561331170845663233,100,'0,100','药剂部',3,'atao',NULL,NULL,'0','2','admin','2022-08-21 20:35:34','admin','2022-08-21 20:35:47'),(1562339491707371522,0,'0','科室',0,NULL,NULL,NULL,'0','0','admin','2022-08-24 15:22:16','admin','2022-08-24 15:22:16'),(1562373498806198273,1562339491707371522,'0,1562339491707371522','骨科',1,NULL,NULL,NULL,'0','0','admin','2022-08-24 17:37:24','admin','2022-08-24 17:37:24'),(1562373550115119106,1562339491707371522,'0,1562339491707371522','心内科',2,NULL,NULL,NULL,'0','0','admin','2022-08-24 17:37:37','admin','2022-08-24 17:37:37'),(1562373597787578370,1562339491707371522,'0,1562339491707371522','外科',3,NULL,NULL,NULL,'0','0','admin','2022-08-24 17:37:48','admin','2022-08-28 21:31:21'),(1562373635540508674,1562339491707371522,'0,1562339491707371522','眼科',4,NULL,NULL,NULL,'0','0','admin','2022-08-24 17:37:57','admin','2022-08-24 17:37:57'),(1562373743204098049,1562339491707371522,'0,1562339491707371522','胸内科',6,NULL,NULL,NULL,'0','0','admin','2022-08-24 17:38:23','admin','2022-08-24 17:38:23'),(1562373793095344129,1562339491707371522,'0,1562339491707371522','呼吸科',7,NULL,NULL,NULL,'0','0','admin','2022-08-24 17:38:34','admin','2022-08-24 17:38:34');
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'男','0','sys_user_sex','','','Y','0','admin','2022-08-21 01:31:07','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2022-08-21 01:31:07','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2022-08-21 01:31:07','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2022-08-21 01:31:07','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2022-08-21 01:31:07','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2022-08-21 01:31:07','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2022-08-21 01:31:07','',NULL,'停用状态'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2022-08-21 01:31:07','',NULL,'正常状态'),(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2022-08-21 01:31:07','',NULL,'停用状态'),(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2022-08-21 01:31:07','',NULL,'默认分组'),(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2022-08-21 01:31:07','',NULL,'系统分组'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2022-08-21 01:31:07','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2022-08-21 01:31:07','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2022-08-21 01:31:07','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2022-08-21 01:31:07','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2022-08-21 01:31:07','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2022-08-21 01:31:07','',NULL,'关闭状态'),(18,1,'新增','1','sys_oper_type','','info','N','0','admin','2022-08-21 01:31:07','',NULL,'新增操作'),(19,2,'修改','2','sys_oper_type','','info','N','0','admin','2022-08-21 01:31:07','',NULL,'修改操作'),(20,3,'删除','3','sys_oper_type','','danger','N','0','admin','2022-08-21 01:31:07','',NULL,'删除操作'),(21,4,'授权','4','sys_oper_type','','primary','N','0','admin','2022-08-21 01:31:07','',NULL,'授权操作'),(22,5,'导出','5','sys_oper_type','','warning','N','0','admin','2022-08-21 01:31:07','',NULL,'导出操作'),(23,6,'导入','6','sys_oper_type','','warning','N','0','admin','2022-08-21 01:31:07','',NULL,'导入操作'),(24,7,'强退','7','sys_oper_type','','danger','N','0','admin','2022-08-21 01:31:07','',NULL,'强退操作'),(25,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2022-08-21 01:31:07','',NULL,'生成操作'),(26,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2022-08-21 01:31:07','',NULL,'清空操作'),(27,1,'成功','0','sys_common_status','','primary','N','0','admin','2022-08-21 01:31:07','',NULL,'正常状态'),(28,2,'失败','1','sys_common_status','','danger','N','0','admin','2022-08-21 01:31:07','',NULL,'停用状态'),(1561357111575715842,1,'上午','上午','morning_afternoon',NULL,'default','N','0','admin','2022-08-21 22:18:39','admin','2022-08-21 22:18:39','上午'),(1561357183872933889,2,'下午','下午','morning_afternoon',NULL,'default','N','0','admin','2022-08-21 22:18:56','admin','2022-08-21 22:19:02','下午'),(1561621941973549057,1,'处方','0','chufang_feichufang',NULL,'default','N','0','admin','2022-08-22 15:50:59','admin','2022-08-22 15:50:59',NULL),(1561622000433758209,2,'非处方','1','chufang_feichufang',NULL,'default','N','0','admin','2022-08-22 15:51:13','admin','2022-08-22 15:51:13',NULL);
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'用户性别','sys_user_sex','0','admin','2022-08-21 01:31:06','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide','0','admin','2022-08-21 01:31:07','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable','0','admin','2022-08-21 01:31:07','',NULL,'系统开关列表'),(4,'任务状态','sys_job_status','0','admin','2022-08-21 01:31:07','',NULL,'任务状态列表'),(5,'任务分组','sys_job_group','0','admin','2022-08-21 01:31:07','',NULL,'任务分组列表'),(6,'系统是否','sys_yes_no','0','admin','2022-08-21 01:31:07','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type','0','admin','2022-08-21 01:31:07','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status','0','admin','2022-08-21 01:31:07','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type','0','admin','2022-08-21 01:31:07','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status','0','admin','2022-08-21 01:31:07','',NULL,'登录状态列表'),(1561356452008828929,'上下午','morning_afternoon','0','admin','2022-08-21 22:16:01','admin','2022-08-21 22:16:01',NULL),(1561621837388578818,'处方非处方','chufang_feichufang','0','admin','2022-08-22 15:50:34','admin','2022-08-22 15:50:34','处方非处方');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint NOT NULL COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示信息',
  `access_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT INTO `sys_logininfor` VALUES (1561182594527776769,'admin','127.0.0.1','0','登录成功','2022-08-21 10:45:10'),(1561196463572709377,'admin','127.0.0.1','0','登录成功','2022-08-21 11:40:17'),(1561197423254630402,'admin','127.0.0.1','0','退出成功','2022-08-21 11:44:06'),(1561198810663292929,'admin','127.0.0.1','0','登录成功','2022-08-21 11:49:37'),(1561206446804766721,'admin','127.0.0.1','0','登录成功','2022-08-21 12:19:57'),(1561213368509468673,'admin','127.0.0.1','0','退出成功','2022-08-21 12:47:28'),(1561213421902958593,'admin','127.0.0.1','0','登录成功','2022-08-21 12:47:40'),(1561236183111503874,'admin','127.0.0.1','0','登录成功','2022-08-21 14:18:07'),(1561238906129158145,'admin','127.0.0.1','0','登录成功','2022-08-21 14:28:56'),(1561249218039451649,'admin','127.0.0.1','0','退出成功','2022-08-21 15:09:55'),(1561249246850125825,'lionli','127.0.0.1','0','登录成功','2022-08-21 15:10:02'),(1561254011868876801,'lionli','127.0.0.1','0','退出成功','2022-08-21 15:28:58'),(1561254034568450049,'admin','127.0.0.1','0','登录成功','2022-08-21 15:29:03'),(1561321178407116802,'admin','127.0.0.1','0','登录成功','2022-08-21 19:55:52'),(1561325362590261250,'admin','127.0.0.1','0','退出成功','2022-08-21 20:12:29'),(1561325413051932673,'test','127.0.0.1','0','登录成功','2022-08-21 20:12:41'),(1561325787368398850,'test','127.0.0.1','0','退出成功','2022-08-21 20:14:10'),(1561325806808997889,'admin','127.0.0.1','0','登录成功','2022-08-21 20:14:15'),(1561327841054826497,'admin','127.0.0.1','0','退出成功','2022-08-21 20:22:20'),(1561327934516502530,'ptuser','127.0.0.1','0','登录成功','2022-08-21 20:22:42'),(1561328004842397698,'ptuser','127.0.0.1','0','退出成功','2022-08-21 20:22:59'),(1561328045053190145,'admin','127.0.0.1','0','登录成功','2022-08-21 20:23:09'),(1561345897957171201,'admin','127.0.0.1','0','登录成功','2022-08-21 21:34:05'),(1561348090072408066,'admin','127.0.0.1','0','退出成功','2022-08-21 21:42:48'),(1561348114957213698,'admin','127.0.0.1','0','登录成功','2022-08-21 21:42:54'),(1561352940915007489,'admin','127.0.0.1','0','退出成功','2022-08-21 22:02:04'),(1561353496861614081,'admin','127.0.0.1','0','登录成功','2022-08-21 22:04:17'),(1561365260349218818,'admin','127.0.0.1','0','退出成功','2022-08-21 22:51:01'),(1561365286873997313,'admin','127.0.0.1','0','登录成功','2022-08-21 22:51:08'),(1561365712369360898,'admin','127.0.0.1','0','退出成功','2022-08-21 22:52:49'),(1561365732132921346,'admin','127.0.0.1','0','登录成功','2022-08-21 22:52:54'),(1561374715900076034,'admin','127.0.0.1','0','退出成功','2022-08-21 23:28:36'),(1561532682717519873,'admin','127.0.0.1','0','登录成功','2022-08-22 09:56:18'),(1561553776966643714,'admin','127.0.0.1','0','登录成功','2022-08-22 11:20:07'),(1561554853426376705,'admin','127.0.0.1','0','登录成功','2022-08-22 11:24:24'),(1561561793074692098,'admin','127.0.0.1','0','登录成功','2022-08-22 11:51:59'),(1561567296697413634,'admin','127.0.0.1','0','登录成功','2022-08-22 12:13:51'),(1561568047079751681,'admin','127.0.0.1','0','登录成功','2022-08-22 12:16:50'),(1561568152721305602,'admin','127.0.0.1','0','登录成功','2022-08-22 12:17:15'),(1561568455680458754,'admin','127.0.0.1','0','退出成功','2022-08-22 12:18:27'),(1561586102669279233,'admin','127.0.0.1','0','登录成功','2022-08-22 13:28:34'),(1561594079144968194,'admin','127.0.0.1','0','登录成功','2022-08-22 14:00:16'),(1561611818299043841,'admin','127.0.0.1','0','登录成功','2022-08-22 15:10:45'),(1561621024574406657,'admin','127.0.0.1','0','登录成功','2022-08-22 15:47:20'),(1561624318537498625,'admin','127.0.0.1','0','退出成功','2022-08-22 16:00:26'),(1561624339689373697,'admin','127.0.0.1','0','登录成功','2022-08-22 16:00:31'),(1561641275768451074,'admin','127.0.0.1','0','登录成功','2022-08-22 17:07:49'),(1561652196405235714,'admin','127.0.0.1','0','登录成功','2022-08-22 17:51:12'),(1561653303283548161,'admin','127.0.0.1','0','登录成功','2022-08-22 17:55:36'),(1561670687225114626,'admin','127.0.0.1','0','登录成功','2022-08-22 19:04:41'),(1561688279320502273,'admin','127.0.0.1','0','登录成功','2022-08-22 20:14:35'),(1561715518636421121,'admin','127.0.0.1','0','登录成功','2022-08-22 22:02:50'),(1561730626959831042,'admin','127.0.0.1','0','登录成功','2022-08-22 23:02:52'),(1561735202534453250,'admin','127.0.0.1','0','退出成功','2022-08-22 23:21:03'),(1561735215377412098,'admin','127.0.0.1','0','退出成功','2022-08-22 23:21:06'),(1561735228434280449,'admin','127.0.0.1','0','退出成功','2022-08-22 23:21:09'),(1561735438640214018,'admin','127.0.0.1','0','登录成功','2022-08-22 23:21:59'),(1561736901923827714,'admin','127.0.0.1','0','退出成功','2022-08-22 23:27:48'),(1561736914649346049,'admin','127.0.0.1','0','退出成功','2022-08-22 23:27:51'),(1561736927353892866,'admin','127.0.0.1','0','退出成功','2022-08-22 23:27:54'),(1561737274642264066,'admin','127.0.0.1','0','登录成功','2022-08-22 23:29:17'),(1561882503378731010,'admin','127.0.0.1','0','登录成功','2022-08-23 09:06:22'),(1561896392636227586,'admin','127.0.0.1','0','登录成功','2022-08-23 10:01:33'),(1561912844378198018,'admin','127.0.0.1','0','登录成功','2022-08-23 11:06:56'),(1561913079775121409,'admin','127.0.0.1','0','登录成功','2022-08-23 11:07:52'),(1561913080239108097,'admin','127.0.0.1','0','登录成功','2022-08-23 11:07:52'),(1561913083921743874,'admin','127.0.0.1','0','登录成功','2022-08-23 11:07:53'),(1561922420934725634,'admin','127.0.0.1','0','登录成功','2022-08-23 11:44:59'),(1561957260362846210,'admin','127.0.0.1','0','登录成功','2022-08-23 14:03:25'),(1561958742393667586,'admin','127.0.0.1','0','登录成功','2022-08-23 14:09:19'),(1561961029515415553,'admin','127.0.0.1','0','登录成功','2022-08-23 14:18:24'),(1561974235017306113,'admin','127.0.0.1','0','登录成功','2022-08-23 15:10:52'),(1561977499087593474,'admin','127.0.0.1','0','登录成功','2022-08-23 15:23:51'),(1561979076610109442,'admin','127.0.0.1','0','登录成功','2022-08-23 15:30:07'),(1561997570652823554,'admin','127.0.0.1','0','登录成功','2022-08-23 16:43:36'),(1562032958788395009,'admin','127.0.0.1','0','登录成功','2022-08-23 19:04:13'),(1562044743099129857,'admin','127.0.0.1','0','登录成功','2022-08-23 19:51:03'),(1562048457776078849,'admin','127.0.0.1','0','登录成功','2022-08-23 20:05:48'),(1562052150711545857,'admin','127.0.0.1','0','登录成功','2022-08-23 20:20:29'),(1562052422812680193,'admin','127.0.0.1','0','登录成功','2022-08-23 20:21:34'),(1562062617429377026,'admin','127.0.0.1','0','登录成功','2022-08-23 21:02:04'),(1562069870013947905,'admin','127.0.0.1','0','登录成功','2022-08-23 21:30:54'),(1562248747802013698,'admin','127.0.0.1','0','登录成功','2022-08-24 09:21:41'),(1562250245412478977,'admin','127.0.0.1','0','登录成功','2022-08-24 09:27:38'),(1562250334872788993,'admin','127.0.0.1','0','登录成功','2022-08-24 09:28:00'),(1562252422815969282,'admin','127.0.0.1','0','登录成功','2022-08-24 09:36:17'),(1562253840136527874,'admin','127.0.0.1','0','登录成功','2022-08-24 09:41:55'),(1562255087939702786,'admin','127.0.0.1','0','登录成功','2022-08-24 09:46:53'),(1562259231203217410,'admin','127.0.0.1','0','登录成功','2022-08-24 10:03:21'),(1562262241948196866,'admin','127.0.0.1','0','退出成功','2022-08-24 10:15:19'),(1562262276354072578,'admin','127.0.0.1','0','登录成功','2022-08-24 10:15:27'),(1562268376642084866,'admin','127.0.0.1','0','登录成功','2022-08-24 10:39:41'),(1562268664409088002,'admin','127.0.0.1','0','退出成功','2022-08-24 10:40:50'),(1562268719639683073,'ptuser','127.0.0.1','0','登录成功','2022-08-24 10:41:03'),(1562270336829087746,'ptuser','127.0.0.1','0','退出成功','2022-08-24 10:47:29'),(1562270360753397761,'admin','127.0.0.1','0','登录成功','2022-08-24 10:47:34'),(1562274678153760769,'admin','127.0.0.1','0','登录成功','2022-08-24 11:04:44'),(1562281856664600578,'admin','127.0.0.1','0','登录成功','2022-08-24 11:33:15'),(1562290026636894210,'admin','127.0.0.1','0','登录成功','2022-08-24 12:05:43'),(1562298009431076865,'admin','127.0.0.1','0','登录成功','2022-08-24 12:37:26'),(1562299575382921217,'admin','127.0.0.1','0','登录成功','2022-08-24 12:43:40'),(1562313221456781314,'admin','127.0.0.1','0','登录成功','2022-08-24 13:37:53'),(1562314412894969857,'admin','127.0.0.1','0','退出成功','2022-08-24 13:42:37'),(1562314854282551298,'admin','127.0.0.1','0','登录成功','2022-08-24 13:44:22'),(1562318268787617794,'admin','127.0.0.1','0','登录成功','2022-08-24 13:57:56'),(1562319498760544257,'admin','127.0.0.1','0','登录成功','2022-08-24 14:02:50'),(1562321024608980994,'admin','127.0.0.1','0','登录成功','2022-08-24 14:08:53'),(1562322153889849345,'admin','127.0.0.1','0','退出成功','2022-08-24 14:13:23'),(1562322188648046593,'admin','127.0.0.1','0','登录成功','2022-08-24 14:13:31'),(1562325321295949825,'admin','127.0.0.1','0','登录成功','2022-08-24 14:25:58'),(1562327543151058945,'admin','127.0.0.1','0','退出成功','2022-08-24 14:34:48'),(1562327707207065601,'admin','127.0.0.1','0','登录成功','2022-08-24 14:35:27'),(1562329283158073345,'admin','127.0.0.1','0','登录成功','2022-08-24 14:41:42'),(1562330248221286401,'admin','127.0.0.1','0','登录成功','2022-08-24 14:45:33'),(1562330634092404737,'admin','0:0:0:0:0:0:0:1','0','登录成功','2022-08-24 14:47:05'),(1562339892004118529,'admin','127.0.0.1','0','登录成功','2022-08-24 15:23:52'),(1562355337956417537,'admin','127.0.0.1','0','登录成功','2022-08-24 16:25:14'),(1562359917460099073,'patient','0:0:0:0:0:0:0:1','0','patient.login.success','2022-08-24 16:43:26'),(1562363917914656769,'admin','127.0.0.1','0','登录成功','2022-08-24 16:59:20'),(1562368019115044866,'admin','127.0.0.1','0','登录成功','2022-08-24 17:15:38'),(1562369528078798849,'admin','127.0.0.1','0','登录成功','2022-08-24 17:21:38'),(1562373272810614785,'admin','127.0.0.1','0','登录成功','2022-08-24 17:36:30'),(1562375492060168194,'admin','127.0.0.1','0','登录成功','2022-08-24 17:45:19'),(1562375817248751617,'admin','127.0.0.1','0','退出成功','2022-08-24 17:46:37'),(1562376476660449282,'patient','0:0:0:0:0:0:0:1','0','patient.login.success','2022-08-24 17:49:14'),(1562377670866878466,'patient','127.0.0.1','0','patient.login.success','2022-08-24 17:53:59'),(1562387472049356801,'patient','127.0.0.1','0','patient.login.success','2022-08-24 18:32:56'),(1562394598176198657,'admin','127.0.0.1','0','登录成功','2022-08-24 19:01:15'),(1562400600271208449,'admin','127.0.0.1','0','登录成功','2022-08-24 19:25:06'),(1562401207392788481,'admin','127.0.0.1','0','登录成功','2022-08-24 19:27:31'),(1562411914477551617,'patient','127.0.0.1','0','patient.login.success','2022-08-24 20:10:03'),(1562415274459246594,'admin','127.0.0.1','0','登录成功','2022-08-24 20:23:24'),(1562415393573953537,'admin','127.0.0.1','0','登录成功','2022-08-24 20:23:53'),(1562417988528803841,'admin','127.0.0.1','0','登录成功','2022-08-24 20:34:11'),(1562420741865201666,'admin','127.0.0.1','0','退出成功','2022-08-24 20:45:08'),(1562420799213920257,'admin','127.0.0.1','0','登录成功','2022-08-24 20:45:22'),(1562421604691214338,'patient','127.0.0.1','0','patient.login.success','2022-08-24 20:48:34'),(1562421840876666881,'admin','127.0.0.1','0','登录成功','2022-08-24 20:49:30'),(1562422037627273218,'admin','127.0.0.1','0','登录成功','2022-08-24 20:50:17'),(1562422708590084097,'admin','127.0.0.1','0','退出成功','2022-08-24 20:52:57'),(1562422789942804482,'test','127.0.0.1','0','登录成功','2022-08-24 20:53:16'),(1562422972797681665,'test','127.0.0.1','0','退出成功','2022-08-24 20:54:00'),(1562423054217510913,'lionli','127.0.0.1','0','登录成功','2022-08-24 20:54:19'),(1562423116813303810,'lionli','127.0.0.1','0','退出成功','2022-08-24 20:54:34'),(1562423270119309314,'admin','127.0.0.1','0','登录成功','2022-08-24 20:55:11'),(1562423445478965250,'admin','127.0.0.1','0','退出成功','2022-08-24 20:55:52'),(1562423490144108545,'lionli','127.0.0.1','0','登录成功','2022-08-24 20:56:03'),(1562423991401185282,'lionli','127.0.0.1','0','退出成功','2022-08-24 20:58:03'),(1562424192291569666,'patient','127.0.0.1','0','patient.login.success','2022-08-24 20:58:51'),(1562430955069898753,'patient','127.0.0.1','0','patient.login.success','2022-08-24 21:25:43'),(1562432037892390913,'patient','127.0.0.1','0','patient.login.success','2022-08-24 21:30:01'),(1562432546665660418,'patient','127.0.0.1','0','patient.login.success','2022-08-24 21:32:02'),(1562433061466144769,'patient','127.0.0.1','0','patient.login.success','2022-08-24 21:34:05'),(1562433134375731202,'patient','127.0.0.1','0','patient.login.success','2022-08-24 21:34:22'),(1562434593856118786,'admin','127.0.0.1','0','登录成功','2022-08-24 21:40:10'),(1562434879056207873,'admin','127.0.0.1','0','退出成功','2022-08-24 21:41:18'),(1562435068517113858,'lionli','127.0.0.1','0','登录成功','2022-08-24 21:42:04'),(1562436346134659073,'patient','127.0.0.1','0','patient.logout.success','2022-08-24 21:47:08'),(1562436854689947650,'patient','127.0.0.1','0','patient.login.success','2022-08-24 21:49:09'),(1562440335547031553,'patient','127.0.0.1','0','patient.login.success','2022-08-24 22:02:59'),(1562447053480677377,'lionli','127.0.0.1','0','登录成功','2022-08-24 22:29:41'),(1562447793158770689,'lionli','127.0.0.1','0','退出成功','2022-08-24 22:32:37'),(1562447823802355714,'admin','127.0.0.1','0','登录成功','2022-08-24 22:32:45'),(1562448550213050370,'admin','127.0.0.1','0','登录成功','2022-08-24 22:35:38'),(1562448963767230466,'admin','127.0.0.1','0','登录成功','2022-08-24 22:37:17'),(1562449175021740033,'admin','127.0.0.1','0','登录成功','2022-08-24 22:38:07'),(1562449811633201154,'patient','127.0.0.1','0','patient.login.success','2022-08-24 22:40:39'),(1562450342708400130,'lionli','127.0.0.1','0','登录成功','2022-08-24 22:42:45'),(1562450897202962434,'admin','127.0.0.1','0','登录成功','2022-08-24 22:44:57'),(1562450998034030594,'admin','127.0.0.1','0','登录成功','2022-08-24 22:45:22'),(1562451092092112898,'lionli','127.0.0.1','0','退出成功','2022-08-24 22:45:44'),(1562451159859482626,'admin','127.0.0.1','0','登录成功','2022-08-24 22:46:00'),(1562451878855462914,'admin','127.0.0.1','0','退出成功','2022-08-24 22:48:52'),(1562451921595420674,'lionli','127.0.0.1','0','登录成功','2022-08-24 22:49:02'),(1562452988865433602,'lionli','127.0.0.1','0','退出成功','2022-08-24 22:53:16'),(1562453012043157506,'admin','127.0.0.1','0','登录成功','2022-08-24 22:53:22'),(1562454538735443969,'patient','127.0.0.1','0','patient.login.success','2022-08-24 22:59:26'),(1562454954562936833,'patient','127.0.0.1','0','patient.login.success','2022-08-24 23:01:05'),(1562455056090259457,'admin','127.0.0.1','0','登录成功','2022-08-24 23:01:29'),(1562455191209762817,'admin','127.0.0.1','0','登录成功','2022-08-24 23:02:01'),(1562455388509822977,'admin','127.0.0.1','0','登录成功','2022-08-24 23:02:48'),(1562455629086711810,'patient','127.0.0.1','0','patient.login.success','2022-08-24 23:03:46'),(1562456999323762689,'patient','127.0.0.1','0','patient.login.success','2022-08-24 23:09:12'),(1562460082258870273,'patient','127.0.0.1','0','patient.login.success','2022-08-24 23:21:27'),(1562464472877293569,'patient','127.0.0.1','0','patient.login.success','2022-08-24 23:38:54'),(1562465305027153922,'patient','127.0.0.1','0','patient.login.success','2022-08-24 23:42:13'),(1562601579826139137,'lionli','127.0.0.1','0','登录成功','2022-08-25 08:43:43'),(1562601942641823745,'lionli','127.0.0.1','0','退出成功','2022-08-25 08:45:10'),(1562601960736051201,'admin','127.0.0.1','0','登录成功','2022-08-25 08:45:14'),(1562604756906565634,'admin','127.0.0.1','0','登录成功','2022-08-25 08:56:20'),(1562607503970267137,'admin','127.0.0.1','0','登录成功','2022-08-25 09:07:15'),(1562610118787866625,'admin','127.0.0.1','0','登录成功','2022-08-25 09:17:39'),(1562610430024716290,'admin','127.0.0.1','0','登录成功','2022-08-25 09:18:53'),(1562618817442750465,'patient','127.0.0.1','0','patient.login.success','2022-08-25 09:52:13'),(1562619797387354114,'admin','127.0.0.1','0','登录成功','2022-08-25 09:56:06'),(1562620235184611330,'admin','127.0.0.1','0','登录成功','2022-08-25 09:57:51'),(1562620623640076289,'admin','127.0.0.1','0','登录成功','2022-08-25 09:59:23'),(1562622463842975746,'admin','127.0.0.1','0','登录成功','2022-08-25 10:06:42'),(1562626495720304642,'admin','127.0.0.1','0','退出成功','2022-08-25 10:22:43'),(1562626525575360513,'admin','127.0.0.1','0','登录成功','2022-08-25 10:22:51'),(1562626558924271617,'admin','127.0.0.1','0','退出成功','2022-08-25 10:22:59'),(1562626634782453761,'patient','127.0.0.1','0','patient.login.success','2022-08-25 10:23:17'),(1562627404403683330,'admin','127.0.0.1','0','登录成功','2022-08-25 10:26:20'),(1562627434518786049,'admin','127.0.0.1','0','退出成功','2022-08-25 10:26:27'),(1562627486444269569,'lionli','127.0.0.1','0','登录成功','2022-08-25 10:26:40'),(1562629031181017090,'admin','127.0.0.1','0','登录成功','2022-08-25 10:32:48'),(1562630570272481282,'admin','127.0.0.1','0','退出成功','2022-08-25 10:38:55'),(1562630595027263489,'admin','127.0.0.1','0','登录成功','2022-08-25 10:39:01'),(1562632092394041345,'patient','127.0.0.1','0','patient.login.success','2022-08-25 10:44:58'),(1562633141947314177,'admin','127.0.0.1','0','登录成功','2022-08-25 10:49:08'),(1562633279453376513,'admin','127.0.0.1','0','登录成功','2022-08-25 10:49:41'),(1562637815681413121,'admin','127.0.0.1','0','登录成功','2022-08-25 11:07:42'),(1562639807529697282,'admin','127.0.0.1','0','登录成功','2022-08-25 11:15:37'),(1562649820693553154,'admin','127.0.0.1','0','登录成功','2022-08-25 11:55:25'),(1562650042249273345,'admin','127.0.0.1','0','登录成功','2022-08-25 11:56:17'),(1562653210897285121,'admin','127.0.0.1','0','登录成功','2022-08-25 12:08:53'),(1562680220717678593,'admin','127.0.0.1','0','登录成功','2022-08-25 13:56:12'),(1562680694109908993,'admin','127.0.0.1','0','登录成功','2022-08-25 13:58:05'),(1562681474517364737,'admin','127.0.0.1','0','登录成功','2022-08-25 14:01:11'),(1562681814400118785,'admin','127.0.0.1','0','退出成功','2022-08-25 14:02:32'),(1562681848181043202,'admin','127.0.0.1','0','登录成功','2022-08-25 14:02:40'),(1562684114002288641,'admin','127.0.0.1','0','登录成功','2022-08-25 14:11:41'),(1562684469532712961,'admin','127.0.0.1','0','登录成功','2022-08-25 14:13:05'),(1562685176340045826,'admin','127.0.0.1','0','登录成功','2022-08-25 14:15:54'),(1562695698954125313,'admin','127.0.0.1','0','登录成功','2022-08-25 14:57:43'),(1562707241733255170,'admin','127.0.0.1','0','登录成功','2022-08-25 15:43:35'),(1562713843701444610,'admin','127.0.0.1','0','登录成功','2022-08-25 16:09:49'),(1562715772667600898,'admin','127.0.0.1','0','登录成功','2022-08-25 16:17:29'),(1562716481186209793,'admin','127.0.0.1','0','退出成功','2022-08-25 16:20:18'),(1562716547422658561,'yan','127.0.0.1','0','登录成功','2022-08-25 16:20:33'),(1562716629152866306,'yan','127.0.0.1','0','退出成功','2022-08-25 16:20:53'),(1562716654587125761,'admin','127.0.0.1','0','登录成功','2022-08-25 16:20:59'),(1562716894807498754,'admin','127.0.0.1','0','退出成功','2022-08-25 16:21:56'),(1562716914604613634,'admin','127.0.0.1','0','登录成功','2022-08-25 16:22:01'),(1562716943734054913,'admin','127.0.0.1','0','退出成功','2022-08-25 16:22:08'),(1562716987254153217,'yan','127.0.0.1','0','登录成功','2022-08-25 16:22:18'),(1562722620443500546,'yan','127.0.0.1','0','退出成功','2022-08-25 16:44:41'),(1562722653540753410,'admin','127.0.0.1','0','登录成功','2022-08-25 16:44:49'),(1562725248648912897,'admin','127.0.0.1','0','退出成功','2022-08-25 16:55:08'),(1562725271134576641,'admin','127.0.0.1','0','登录成功','2022-08-25 16:55:13'),(1562726467526238209,'admin','127.0.0.1','0','退出成功','2022-08-25 16:59:59'),(1562726512380125186,'yan','127.0.0.1','0','登录成功','2022-08-25 17:00:09'),(1562727030863208449,'yan','127.0.0.1','0','退出成功','2022-08-25 17:02:13'),(1562727067320098817,'admin','127.0.0.1','0','登录成功','2022-08-25 17:02:22'),(1562727749792079874,'yan','127.0.0.1','0','登录成功','2022-08-25 17:05:04'),(1562732872367104002,'admin','127.0.0.1','0','登录成功','2022-08-25 17:25:26'),(1562734091553030145,'admin','127.0.0.1','0','登录成功','2022-08-25 17:30:16'),(1562734120904769537,'admin','127.0.0.1','0','登录成功','2022-08-25 17:30:23'),(1562734176942694401,'admin','127.0.0.1','0','登录成功','2022-08-25 17:30:37'),(1562756912645226497,'yan','127.0.0.1','0','登录成功','2022-08-25 19:00:57'),(1562761064417456130,'admin','127.0.0.1','0','登录成功','2022-08-25 19:17:27'),(1562763338808557570,'admin','127.0.0.1','0','登录成功','2022-08-25 19:26:29'),(1562765869349691393,'admin','127.0.0.1','0','登录成功','2022-08-25 19:36:33'),(1562767455262179330,'admin','127.0.0.1','0','登录成功','2022-08-25 19:42:51'),(1562772671483174913,'admin','127.0.0.1','0','登录成功','2022-08-25 20:03:34'),(1562774644777345025,'admin','127.0.0.1','0','登录成功','2022-08-25 20:11:25'),(1562775251458195457,'admin','127.0.0.1','0','登录成功','2022-08-25 20:13:50'),(1562775369300918274,'admin','127.0.0.1','0','登录成功','2022-08-25 20:14:18'),(1562775903886434306,'admin','127.0.0.1','0','登录成功','2022-08-25 20:16:25'),(1562778535058292738,'admin','127.0.0.1','0','登录成功','2022-08-25 20:26:52'),(1562778598824296449,'admin','127.0.0.1','0','登录成功','2022-08-25 20:27:08'),(1562780063743893505,'admin','127.0.0.1','0','登录成功','2022-08-25 20:32:57'),(1562781516398936066,'admin','127.0.0.1','0','退出成功','2022-08-25 20:38:43'),(1562781566969659393,'yan','127.0.0.1','0','登录成功','2022-08-25 20:38:55'),(1562802068725956609,'admin','127.0.0.1','0','登录成功','2022-08-25 22:00:23'),(1562807082573713410,'admin','127.0.0.1','0','登录成功','2022-08-25 22:20:19'),(1562812149888270337,'admin','127.0.0.1','0','登录成功','2022-08-25 22:40:27'),(1562812296575664130,'yan','127.0.0.1','0','登录成功','2022-08-25 22:41:02'),(1562967569165021185,'admin','127.0.0.1','0','登录成功','2022-08-26 08:58:02'),(1562967955074543618,'admin','127.0.0.1','0','登录成功','2022-08-26 08:59:34'),(1562967986695401474,'admin','127.0.0.1','0','退出成功','2022-08-26 08:59:41'),(1562968032866299906,'yan','127.0.0.1','0','登录成功','2022-08-26 08:59:52'),(1562982117355638785,'admin','127.0.0.1','0','登录成功','2022-08-26 09:55:50'),(1562983450047737857,'admin','127.0.0.1','0','登录成功','2022-08-26 10:01:08'),(1562997239650455553,'admin','127.0.0.1','0','登录成功','2022-08-26 10:55:56'),(1562997345665683458,'yan','127.0.0.1','0','登录成功','2022-08-26 10:56:21'),(1562998950888800258,'admin','127.0.0.1','0','登录成功','2022-08-26 11:02:44'),(1562999050938167298,'admin','127.0.0.1','0','登录成功','2022-08-26 11:03:08'),(1563006875896184834,'admin','127.0.0.1','0','登录成功','2022-08-26 11:34:13'),(1563012403540090881,'admin','127.0.0.1','0','登录成功','2022-08-26 11:56:11'),(1563015700476903425,'admin','127.0.0.1','0','登录成功','2022-08-26 12:09:17'),(1563016526062731266,'yan','127.0.0.1','0','登录成功','2022-08-26 12:12:34'),(1563022264743014402,'admin','127.0.0.1','0','登录成功','2022-08-26 12:35:22'),(1563023320566771714,'admin','127.0.0.1','0','登录成功','2022-08-26 12:39:34'),(1563360545950695425,'admin','127.0.0.1','0','登录成功','2022-08-27 10:59:35'),(1563705802147663874,'admin','127.0.0.1','0','登录成功','2022-08-28 09:51:30'),(1563715868854718466,'admin','192.168.227.1','0','登录成功','2022-08-28 10:31:30'),(1563716038287822849,'admin','192.168.227.1','0','登录成功','2022-08-28 10:32:11'),(1563819399518961666,'admin','127.0.0.1','0','登录成功','2022-08-28 17:22:54'),(1563822815801520130,'admin','127.0.0.1','0','登录成功','2022-08-28 17:36:28'),(1563864287515504641,'admin','127.0.0.1','0','登录成功','2022-08-28 20:21:16'),(1563897155932962818,'admin','172.17.0.1','0','登录成功','2022-08-28 22:31:52'),(1563897159628144642,'admin','172.17.0.1','0','退出成功','2022-08-28 22:31:53'),(1563897240016175105,'admin','172.17.0.1','0','登录成功','2022-08-28 22:32:12'),(1563897242176241666,'admin','172.17.0.1','0','退出成功','2022-08-28 22:32:13'),(1563897683039535106,'admin','192.168.227.1','0','登录成功','2022-08-28 22:33:58'),(1563897685224767489,'admin','192.168.227.1','0','退出成功','2022-08-28 22:33:59'),(1563899196046622721,'admin','192.168.227.1','0','登录成功','2022-08-28 22:39:59'),(1563899198114414594,'admin','192.168.227.1','0','退出成功','2022-08-28 22:39:59'),(1563901503714934786,'admin','172.17.0.1','0','登录成功','2022-08-28 22:49:09'),(1563902586113802242,'admin','172.17.0.1','0','登录成功','2022-08-28 22:53:27'),(1563903185794416641,'admin','192.168.227.1','0','登录成功','2022-08-28 22:55:50'),(1563904437773189121,'admin','172.17.0.1','0','登录成功','2022-08-28 23:00:49'),(1564055592208048129,'admin','127.0.0.1','0','登录成功','2022-08-29 09:01:27'),(1564061745716191234,'admin','127.0.0.1','0','登录成功','2022-08-29 09:25:54'),(1564061780195000322,'admin','127.0.0.1','0','登录成功','2022-08-29 09:26:02'),(1564062546441322498,'admin','172.17.0.1','0','登录成功','2022-08-29 09:29:05'),(1564062900177436674,'admin','127.0.0.1','0','登录成功','2022-08-29 09:30:29'),(1564063616626376705,'admin','172.17.0.1','0','登录成功','2022-08-29 09:33:20'),(1564066186434494466,'admin','172.16.9.75','0','登录成功','2022-08-29 09:43:32'),(1564066307540828161,'admin','172.16.9.77','0','登录成功','2022-08-29 09:44:01'),(1564066496561332225,'admin','172.16.9.131','0','登录成功','2022-08-29 09:44:46'),(1564066579654688769,'admin','172.16.9.128','0','登录成功','2022-08-29 09:45:06'),(1564130850492710913,'admin','172.16.9.93','0','登录成功','2022-08-29 14:00:30'),(1564131142403686402,'admin','172.17.0.1','0','登录成功','2022-08-29 14:01:39'),(1564131707732951041,'admin','172.16.9.131','0','登录成功','2022-08-29 14:03:54'),(1564131822623326210,'admin','172.16.9.128','0','登录成功','2022-08-29 14:04:21'),(1564132540004499458,'gu','172.16.9.77','0','登录成功','2022-08-29 14:07:12'),(1564132639724077057,'gu','172.16.9.77','0','退出成功','2022-08-29 14:07:36'),(1564132672431259650,'yan','172.16.9.77','1','密码输入错误1次','2022-08-29 14:07:44'),(1564132702730911746,'yan','172.16.9.77','0','登录成功','2022-08-29 14:07:51'),(1564132838336954369,'yan','172.16.9.77','0','退出成功','2022-08-29 14:08:24'),(1564132847073689601,'admin','172.16.9.131','0','退出成功','2022-08-29 14:08:26'),(1564132852765360130,'admin','172.16.9.77','0','登录成功','2022-08-29 14:08:27'),(1564132918553018369,'admin','172.16.9.123','0','登录成功','2022-08-29 14:08:43'),(1564133137357275137,'admin','172.16.9.131','1','密码输入错误1次','2022-08-29 14:09:35'),(1564133220966531073,'admin','172.16.9.131','0','登录成功','2022-08-29 14:09:55'),(1564135607328374786,'admin','172.17.0.1','0','登录成功','2022-08-29 14:19:24'),(1564147882193911810,'admin','172.17.0.1','0','登录成功','2022-08-29 15:08:10'),(1564148888139005954,'admin','172.16.9.93','0','登录成功','2022-08-29 15:12:10'),(1564153616445014018,'admin','172.16.9.101','0','登录成功','2022-08-29 15:30:57'),(1564155013525733378,'admin','172.17.0.1','0','登录成功','2022-08-29 15:36:30'),(1564157301787000834,'admin','172.17.0.1','0','登录成功','2022-08-29 15:45:36'),(1564157710270267394,'admin','172.16.9.123','0','登录成功','2022-08-29 15:47:13'),(1564160800180469761,'admin','172.17.0.1','0','登录成功','2022-08-29 15:59:30'),(1564162738687115266,'admin','172.16.9.93','0','登录成功','2022-08-29 16:07:12'),(1564163090664718337,'admin','172.16.9.123','0','登录成功','2022-08-29 16:08:36'),(1564164999563771906,'admin','172.16.9.131','0','登录成功','2022-08-29 16:16:11'),(1564166338352054274,'admin','172.17.0.1','0','登录成功','2022-08-29 16:21:31'),(1564166997730197505,'yan','172.17.0.1','0','登录成功','2022-08-29 16:24:08'),(1564167905461469185,'admin','172.17.0.1','0','登录成功','2022-08-29 16:27:44'),(1564168003750789122,'admin','172.17.0.1','0','登录成功','2022-08-29 16:28:08'),(1564168378503462913,'admin','172.17.0.1','0','登录成功','2022-08-29 16:29:37'),(1564169028230512641,'yan','172.17.0.1','0','登录成功','2022-08-29 16:32:12'),(1564169418124623874,'admin','172.16.9.77','0','登录成功','2022-08-29 16:33:45'),(1564187247934767106,'admin','172.17.0.1','0','登录成功','2022-08-29 17:44:36'),(1565898111763644417,'admin','172.17.0.1','0','登录成功','2022-09-03 11:02:57'),(1565904341559181314,'admin','172.17.0.1','0','退出成功','2022-09-03 11:27:43'),(1565904393883123713,'admin','172.17.0.1','0','登录成功','2022-09-03 11:27:55'),(1565905398519595009,'admin','172.17.0.1','0','退出成功','2022-09-03 11:31:55'),(1565905432686395393,'yan','172.17.0.1','0','登录成功','2022-09-03 11:32:03'),(1565905635929784322,'yan','172.17.0.1','0','退出成功','2022-09-03 11:32:51'),(1565905658373505026,'admin','172.17.0.1','0','登录成功','2022-09-03 11:32:57'),(1571074301441929218,'admin','127.0.0.1','0','登录成功','2022-09-17 17:51:17');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query_param` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,20,'system',NULL,'',1,0,'M','0','0','','system','admin','2022-08-21 01:31:06','admin','2022-08-21 15:30:52','系统管理目录'),(2,'系统监控',0,25,'monitor',NULL,'',1,0,'M','0','0','','monitor','admin','2022-08-21 01:31:06','admin','2022-08-21 15:31:04','系统监控目录'),(3,'检查项目',0,22,'tool',NULL,'',1,0,'M','0','0','','tool','admin','2022-08-21 01:31:06','admin','2022-08-29 16:09:59','系统工具目录'),(4,'PLUS官网',0,40,'https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus',NULL,'',0,0,'M','1','1','','guide','admin','2022-08-21 01:31:06','admin','2022-08-28 17:46:34','若依官网地址'),(5,'测试菜单',0,45,'demo',NULL,NULL,1,0,'M','1','1',NULL,'star','admin','2022-08-21 01:31:44','admin','2022-08-28 17:46:54',''),(100,'用户管理',1561322637831970817,1,'user','system/user/index','',1,0,'C','0','0','system:user:list','user','admin','2022-08-21 01:31:06','admin','2022-08-21 20:04:00','用户管理菜单'),(101,'角色管理',1561322637831970817,2,'role','system/role/index','',1,0,'C','0','0','system:role:list','peoples','admin','2022-08-21 01:31:06','admin','2022-08-21 20:23:42','角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','',1,0,'C','0','0','system:menu:list','tree-table','admin','2022-08-21 01:31:06','',NULL,'菜单管理菜单'),(103,'部门管理',1561322637831970817,4,'dept','system/dept/index','',1,0,'C','0','0','system:dept:list','tree','admin','2022-08-21 01:31:06','admin','2022-08-21 20:23:51','部门管理菜单'),(104,'岗位管理',1561322637831970817,5,'post','system/post/index','',1,0,'C','1','0','system:post:list','post','admin','2022-08-21 01:31:06','admin','2022-08-29 14:21:20','岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','',1,0,'C','1','0','system:dict:list','dict','admin','2022-08-21 01:31:06','admin','2022-08-29 09:07:12','字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','',1,0,'C','1','0','system:config:list','edit','admin','2022-08-21 01:31:06','admin','2022-08-28 21:24:19','参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','',1,0,'C','0','0','system:notice:list','message','admin','2022-08-21 01:31:06','',NULL,'通知公告菜单'),(108,'日志管理',1,9,'log','','',1,0,'M','0','0','','log','admin','2022-08-21 01:31:06','',NULL,'日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','',1,0,'C','0','0','monitor:online:list','online','admin','2022-08-21 01:31:06','',NULL,'在线用户菜单'),(110,'XxlJob控制台',2,2,'http://localhost:9900','','',0,0,'C','1','0','monitor:job:list','job','admin','2022-08-21 01:31:06','admin','2022-08-29 09:06:57','定时任务菜单'),(111,'Sentinel控制台',2,3,'http://localhost:8718','','',0,0,'C','0','0','monitor:sentinel:list','sentinel','admin','2022-08-21 01:31:06','',NULL,'流量控制菜单'),(112,'Nacos控制台',2,4,'http://localhost:8848/nacos','','',0,0,'C','0','0','monitor:nacos:list','nacos','admin','2022-08-21 01:31:06','',NULL,'服务治理菜单'),(113,'Admin控制台',2,5,'http://localhost:9100/login','','',0,0,'C','1','0','monitor:server:list','server','admin','2022-08-21 01:31:06','admin','2022-08-28 21:23:41','服务监控菜单'),(114,'表单构建',3,1,'build','tool/build/index','',1,0,'C','1','0','tool:build:list','build','admin','2022-08-21 01:31:06','admin','2022-08-28 21:25:12','表单构建菜单'),(115,'代码生成',3,2,'gen','tool/gen/index','',1,0,'C','1','0','tool:gen:list','code','admin','2022-08-21 01:31:06','admin','2022-08-28 21:25:33','代码生成菜单'),(118,'文件管理',1,10,'oss','system/oss/index','',1,0,'C','0','0','system:oss:list','upload','admin','2022-08-21 01:31:06','',NULL,'文件管理菜单'),(500,'操作日志',108,1,'operlog','system/operlog/index','',1,0,'C','0','0','system:operlog:list','form','admin','2022-08-21 01:31:06','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','system/logininfor/index','',1,0,'C','0','0','system:logininfor:list','logininfor','admin','2022-08-21 01:31:06','',NULL,'登录日志菜单'),(1001,'用户查询',100,1,'','','',1,0,'F','0','0','system:user:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1002,'用户新增',100,2,'','','',1,0,'F','0','0','system:user:add','#','admin','2022-08-21 01:31:06','',NULL,''),(1003,'用户修改',100,3,'','','',1,0,'F','0','0','system:user:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(1004,'用户删除',100,4,'','','',1,0,'F','0','0','system:user:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1005,'用户导出',100,5,'','','',1,0,'F','0','0','system:user:export','#','admin','2022-08-21 01:31:06','',NULL,''),(1006,'用户导入',100,6,'','','',1,0,'F','0','0','system:user:import','#','admin','2022-08-21 01:31:06','',NULL,''),(1007,'重置密码',100,7,'','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2022-08-21 01:31:06','',NULL,''),(1008,'角色查询',101,1,'','','',1,0,'F','0','0','system:role:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1009,'角色新增',101,2,'','','',1,0,'F','0','0','system:role:add','#','admin','2022-08-21 01:31:06','',NULL,''),(1010,'角色修改',101,3,'','','',1,0,'F','0','0','system:role:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(1011,'角色删除',101,4,'','','',1,0,'F','0','0','system:role:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1012,'角色导出',101,5,'','','',1,0,'F','0','0','system:role:export','#','admin','2022-08-21 01:31:06','',NULL,''),(1013,'菜单查询',102,1,'','','',1,0,'F','0','0','system:menu:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1014,'菜单新增',102,2,'','','',1,0,'F','0','0','system:menu:add','#','admin','2022-08-21 01:31:06','',NULL,''),(1015,'菜单修改',102,3,'','','',1,0,'F','0','0','system:menu:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(1016,'菜单删除',102,4,'','','',1,0,'F','0','0','system:menu:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1017,'部门查询',103,1,'','','',1,0,'F','0','0','system:dept:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1018,'部门新增',103,2,'','','',1,0,'F','0','0','system:dept:add','#','admin','2022-08-21 01:31:06','',NULL,''),(1019,'部门修改',103,3,'','','',1,0,'F','0','0','system:dept:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(1020,'部门删除',103,4,'','','',1,0,'F','0','0','system:dept:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1021,'岗位查询',104,1,'','','',1,0,'F','0','0','system:post:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1022,'岗位新增',104,2,'','','',1,0,'F','0','0','system:post:add','#','admin','2022-08-21 01:31:06','',NULL,''),(1023,'岗位修改',104,3,'','','',1,0,'F','0','0','system:post:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(1024,'岗位删除',104,4,'','','',1,0,'F','0','0','system:post:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1025,'岗位导出',104,5,'','','',1,0,'F','0','0','system:post:export','#','admin','2022-08-21 01:31:06','',NULL,''),(1026,'字典查询',105,1,'#','','',1,0,'F','0','0','system:dict:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1027,'字典新增',105,2,'#','','',1,0,'F','0','0','system:dict:add','#','admin','2022-08-21 01:31:06','',NULL,''),(1028,'字典修改',105,3,'#','','',1,0,'F','0','0','system:dict:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(1029,'字典删除',105,4,'#','','',1,0,'F','0','0','system:dict:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1030,'字典导出',105,5,'#','','',1,0,'F','0','0','system:dict:export','#','admin','2022-08-21 01:31:06','',NULL,''),(1031,'参数查询',106,1,'#','','',1,0,'F','0','0','system:config:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1032,'参数新增',106,2,'#','','',1,0,'F','0','0','system:config:add','#','admin','2022-08-21 01:31:06','',NULL,''),(1033,'参数修改',106,3,'#','','',1,0,'F','0','0','system:config:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(1034,'参数删除',106,4,'#','','',1,0,'F','0','0','system:config:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1035,'参数导出',106,5,'#','','',1,0,'F','0','0','system:config:export','#','admin','2022-08-21 01:31:06','',NULL,''),(1036,'公告查询',107,1,'#','','',1,0,'F','0','0','system:notice:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1037,'公告新增',107,2,'#','','',1,0,'F','0','0','system:notice:add','#','admin','2022-08-21 01:31:06','',NULL,''),(1038,'公告修改',107,3,'#','','',1,0,'F','0','0','system:notice:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(1039,'公告删除',107,4,'#','','',1,0,'F','0','0','system:notice:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1040,'操作查询',500,1,'#','','',1,0,'F','0','0','system:operlog:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1041,'操作删除',500,2,'#','','',1,0,'F','0','0','system:operlog:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1042,'日志导出',500,4,'#','','',1,0,'F','0','0','system:operlog:export','#','admin','2022-08-21 01:31:06','',NULL,''),(1043,'登录查询',501,1,'#','','',1,0,'F','0','0','system:logininfor:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1044,'登录删除',501,2,'#','','',1,0,'F','0','0','system:logininfor:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1045,'日志导出',501,3,'#','','',1,0,'F','0','0','system:logininfor:export','#','admin','2022-08-21 01:31:06','',NULL,''),(1046,'在线查询',109,1,'#','','',1,0,'F','0','0','monitor:online:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1047,'批量强退',109,2,'#','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2022-08-21 01:31:06','',NULL,''),(1048,'单条强退',109,3,'#','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2022-08-21 01:31:06','',NULL,''),(1050,'账户解锁',501,4,'#','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2022-08-21 01:31:06','',NULL,''),(1055,'生成查询',115,1,'#','','',1,0,'F','0','0','tool:gen:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1056,'生成修改',115,2,'#','','',1,0,'F','0','0','tool:gen:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(1057,'生成删除',115,3,'#','','',1,0,'F','0','0','tool:gen:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1058,'导入代码',115,2,'#','','',1,0,'F','0','0','tool:gen:import','#','admin','2022-08-21 01:31:06','',NULL,''),(1059,'预览代码',115,4,'#','','',1,0,'F','0','0','tool:gen:preview','#','admin','2022-08-21 01:31:06','',NULL,''),(1060,'生成代码',115,5,'#','','',1,0,'F','0','0','tool:gen:code','#','admin','2022-08-21 01:31:06','',NULL,''),(1500,'测试单表',5,1,'demo','demo/demo/index',NULL,1,0,'C','0','0','demo:demo:list','#','admin','2022-08-21 01:31:44','',NULL,'测试单表菜单'),(1501,'测试单表查询',1500,1,'#','',NULL,1,0,'F','0','0','demo:demo:query','#','admin','2022-08-21 01:31:44','',NULL,''),(1502,'测试单表新增',1500,2,'#','',NULL,1,0,'F','0','0','demo:demo:add','#','admin','2022-08-21 01:31:44','',NULL,''),(1503,'测试单表修改',1500,3,'#','',NULL,1,0,'F','0','0','demo:demo:edit','#','admin','2022-08-21 01:31:44','',NULL,''),(1504,'测试单表删除',1500,4,'#','',NULL,1,0,'F','0','0','demo:demo:remove','#','admin','2022-08-21 01:31:44','',NULL,''),(1505,'测试单表导出',1500,5,'#','',NULL,1,0,'F','0','0','demo:demo:export','#','admin','2022-08-21 01:31:44','',NULL,''),(1506,'测试树表',5,1,'tree','demo/tree/index',NULL,1,0,'C','0','0','demo:tree:list','#','admin','2022-08-21 01:31:44','',NULL,'测试树表菜单'),(1507,'测试树表查询',1506,1,'#','',NULL,1,0,'F','0','0','demo:tree:query','#','admin','2022-08-21 01:31:44','',NULL,''),(1508,'测试树表新增',1506,2,'#','',NULL,1,0,'F','0','0','demo:tree:add','#','admin','2022-08-21 01:31:44','',NULL,''),(1509,'测试树表修改',1506,3,'#','',NULL,1,0,'F','0','0','demo:tree:edit','#','admin','2022-08-21 01:31:44','',NULL,''),(1510,'测试树表删除',1506,4,'#','',NULL,1,0,'F','0','0','demo:tree:remove','#','admin','2022-08-21 01:31:44','',NULL,''),(1511,'测试树表导出',1506,5,'#','',NULL,1,0,'F','0','0','demo:tree:export','#','admin','2022-08-21 01:31:44','',NULL,''),(1600,'文件查询',118,1,'#','','',1,0,'F','0','0','system:oss:query','#','admin','2022-08-21 01:31:06','',NULL,''),(1601,'文件上传',118,2,'#','','',1,0,'F','0','0','system:oss:upload','#','admin','2022-08-21 01:31:06','',NULL,''),(1602,'文件下载',118,3,'#','','',1,0,'F','0','0','system:oss:download','#','admin','2022-08-21 01:31:06','',NULL,''),(1603,'文件删除',118,4,'#','','',1,0,'F','0','0','system:oss:remove','#','admin','2022-08-21 01:31:06','',NULL,''),(1604,'配置添加',118,5,'#','','',1,0,'F','0','0','system:oss:add','#','admin','2022-08-21 01:31:06','',NULL,''),(1605,'配置编辑',118,6,'#','','',1,0,'F','0','0','system:oss:edit','#','admin','2022-08-21 01:31:06','',NULL,''),(2606,'客户端页面',0,1,'customer','',NULL,1,0,'M','1','0','customer:list','#','admin','2022-08-23 12:19:08','admin','2022-08-29 14:20:10','客户端页面'),(2607,'个人信息',2606,1,'info','customer/info/index',NULL,1,0,'C','0','0','customer:list:info','#','admin','2022-08-23 12:19:08','',NULL,'客户信息'),(2608,'自助挂号',2606,2,'selfreg','customer/selfreg/index',NULL,1,0,'C','0','0','customer:list:selfreg','#','admin','2022-08-23 12:19:08','',NULL,'自助挂号'),(5858,'护士挂号',1561328890075422722,1,'register2','reghos-serv/register/index2',NULL,1,0,'C','0','0','reghos-serv:register:list','edit','admin','2022-08-23 11:29:45','admin','2022-08-26 12:27:25','挂号菜单'),(5958,'就诊',1561329116089688066,1,'kanbing22','reghos-serv/kanbing/index2',NULL,1,0,'C','0','0','reghos-serv:register:list','tool','admin','2022-08-23 11:29:45','admin','2022-08-26 12:28:24','看病'),(6058,'分配床位',1561329975603240961,1,'fbed22','reghos-serv/fbed/index2',NULL,1,0,'C','0','0','reghos-serv:register:list','tree','admin','2022-08-23 11:29:45','admin','2022-08-26 12:28:58','分床'),(1561322637831970817,'医院管理',0,15,'hospitalManagement',NULL,NULL,1,0,'M','0','0',NULL,'system','admin','2022-08-21 20:01:39','admin','2022-08-21 20:01:39',''),(1561328890075422722,'挂号处',0,3,'registration',NULL,NULL,1,0,'M','0','0',NULL,'cascader','admin','2022-08-21 20:26:30','admin','2022-08-21 20:26:30',''),(1561329116089688066,'门诊部',0,5,'outpatient',NULL,NULL,1,0,'M','0','0',NULL,'druid','admin','2022-08-21 20:27:24','admin','2022-08-21 20:27:24',''),(1561329975603240961,'住院部',0,7,'inpatient',NULL,NULL,1,0,'M','0','0',NULL,'example','admin','2022-08-21 20:30:49','admin','2022-08-21 20:30:49',''),(1561332259087196162,'药剂部',0,9,'pharmacy',NULL,NULL,1,0,'M','0','0',NULL,'shopping','admin','2022-08-21 20:39:53','admin','2022-08-21 20:39:53',''),(1561563095523532800,'排班',1561322637831970817,1,'schedule','reghos-serv/schedule/index',NULL,1,0,'C','0','0','reghos-serv:schedule:list','404','admin','2022-08-22 11:59:22','admin','2022-08-22 14:42:43','排班菜单'),(1561563095523532801,'排班查询',1561563095523532800,1,'#','',NULL,1,0,'F','0','0','reghos-serv:schedule:query','#','admin','2022-08-22 11:59:25','',NULL,''),(1561563095523532802,'排班新增',1561563095523532800,2,'#','',NULL,1,0,'F','0','0','reghos-serv:schedule:add','#','admin','2022-08-22 11:59:30','',NULL,''),(1561563095523532803,'排班修改',1561563095523532800,3,'#','',NULL,1,0,'F','0','0','reghos-serv:schedule:edit','#','admin','2022-08-22 11:59:33','',NULL,''),(1561563095523532804,'排班删除',1561563095523532800,4,'#','',NULL,1,0,'F','0','0','reghos-serv:schedule:remove','#','admin','2022-08-22 11:59:35','',NULL,''),(1561563095523532805,'排班导出',1561563095523532800,5,'#','',NULL,1,0,'F','0','0','reghos-serv:schedule:export','#','admin','2022-08-22 11:59:37','',NULL,''),(1561622658276896768,'药品',1561332259087196162,1,'medicine','reghos-serv/medicine/index',NULL,1,0,'C','0','0','reghos-serv:medicine:list','bug','admin','2022-08-22 15:54:36','admin','2022-08-22 16:09:05','药品菜单'),(1561622658276896769,'药品查询',1561622658276896768,1,'#','',NULL,1,0,'F','0','0','reghos-serv:medicine:query','#','admin','2022-08-22 15:54:39','',NULL,''),(1561622658276896770,'药品新增',1561622658276896768,2,'#','',NULL,1,0,'F','0','0','reghos-serv:medicine:add','#','admin','2022-08-22 15:54:41','',NULL,''),(1561622658276896771,'药品修改',1561622658276896768,3,'#','',NULL,1,0,'F','0','0','reghos-serv:medicine:edit','#','admin','2022-08-22 15:54:46','',NULL,''),(1561622658276896772,'药品删除',1561622658276896768,4,'#','',NULL,1,0,'F','0','0','reghos-serv:medicine:remove','#','admin','2022-08-22 15:54:49','',NULL,''),(1561622658276896773,'药品导出',1561622658276896768,5,'#','',NULL,1,0,'F','0','0','reghos-serv:medicine:export','#','admin','2022-08-22 15:54:52','',NULL,''),(1561918205084594176,'设备',1561322637831970817,1,'equipment','reghos-serv/equipment/index',NULL,1,0,'C','0','0','reghos-serv:equipment:list','client','admin','2022-08-23 11:30:36','admin','2022-08-28 21:21:39','设备菜单'),(1561918205084594177,'设备查询',1561918205084594176,1,'#','',NULL,1,0,'F','0','0','reghos-serv:equipment:query','#','admin','2022-08-23 11:30:38','',NULL,''),(1561918205084594178,'设备新增',1561918205084594176,2,'#','',NULL,1,0,'F','0','0','reghos-serv:equipment:add','#','admin','2022-08-23 11:30:40','',NULL,''),(1561918205084594179,'设备修改',1561918205084594176,3,'#','',NULL,1,0,'F','0','0','reghos-serv:equipment:edit','#','admin','2022-08-23 11:30:42','',NULL,''),(1561918205084594180,'设备删除',1561918205084594176,4,'#','',NULL,1,0,'F','0','0','reghos-serv:equipment:remove','#','admin','2022-08-23 11:30:44','',NULL,''),(1561918205084594181,'设备导出',1561918205084594176,5,'#','',NULL,1,0,'F','0','0','reghos-serv:equipment:export','#','admin','2022-08-23 11:30:47','',NULL,''),(1561918213853384704,'挂号列表',1561328890075422722,1,'register','reghos-serv/register/index',NULL,1,0,'C','0','0','reghos-serv:register:list','skill','admin','2022-08-23 11:29:45','admin','2022-08-29 09:09:13','挂号菜单'),(1561918213853384705,'挂号查询',1561918213853384704,1,'#','',NULL,1,0,'F','0','0','reghos-serv:register:query','#','admin','2022-08-23 11:29:45','',NULL,''),(1561918213853384706,'挂号新增',1561918213853384704,2,'#','',NULL,1,0,'F','0','0','reghos-serv:register:add','#','admin','2022-08-23 11:29:45','',NULL,''),(1561918213853384707,'挂号修改',1561918213853384704,3,'#','',NULL,1,0,'F','0','0','reghos-serv:register:edit','#','admin','2022-08-23 11:29:45','',NULL,''),(1561918213853384708,'挂号删除',1561918213853384704,4,'#','',NULL,1,0,'F','0','0','reghos-serv:register:remove','#','admin','2022-08-23 11:29:45','',NULL,''),(1561918213853384709,'挂号导出',1561918213853384704,5,'#','',NULL,1,0,'F','0','0','reghos-serv:register:export','#','admin','2022-08-23 11:29:45','',NULL,''),(1561918228583780352,'手术项目',1561329975603240961,1,'operation','reghos-serv/operation/index',NULL,1,0,'C','0','0','reghos-serv:operation:list','table','admin','2022-08-23 11:30:04','admin','2022-08-24 19:05:43','手术项目菜单'),(1561918228583780353,'手术项目查询',1561918228583780352,1,'#','',NULL,1,0,'F','0','0','reghos-serv:operation:query','#','admin','2022-08-23 11:30:04','',NULL,''),(1561918228583780354,'手术项目新增',1561918228583780352,2,'#','',NULL,1,0,'F','0','0','reghos-serv:operation:add','#','admin','2022-08-23 11:30:05','',NULL,''),(1561918228583780355,'手术项目修改',1561918228583780352,3,'#','',NULL,1,0,'F','0','0','reghos-serv:operation:edit','#','admin','2022-08-23 11:30:05','',NULL,''),(1561918228583780356,'手术项目删除',1561918228583780352,4,'#','',NULL,1,0,'F','0','0','reghos-serv:operation:remove','#','admin','2022-08-23 11:30:05','',NULL,''),(1561918228583780357,'手术项目导出',1561918228583780352,5,'#','',NULL,1,0,'F','0','0','reghos-serv:operation:export','#','admin','2022-08-23 11:30:05','',NULL,''),(1561918253834989568,'药品订单',1561332259087196162,1,'order','reghos-serv/order/index',NULL,1,0,'C','0','0','reghos-serv:order:list','list','admin','2022-08-23 11:29:48','admin','2022-08-25 09:22:40','药品订单菜单'),(1561918253834989569,'药品订单查询',1561918253834989568,1,'#','',NULL,1,0,'F','0','0','reghos-serv:order:query','#','admin','2022-08-23 11:29:48','',NULL,''),(1561918253834989570,'药品订单新增',1561918253834989568,2,'#','',NULL,1,0,'F','0','0','reghos-serv:order:add','#','admin','2022-08-23 11:29:48','',NULL,''),(1561918253834989571,'药品订单修改',1561918253834989568,3,'#','',NULL,1,0,'F','0','0','reghos-serv:order:edit','#','admin','2022-08-23 11:29:48','',NULL,''),(1561918253834989572,'药品订单删除',1561918253834989568,4,'#','',NULL,1,0,'F','0','0','reghos-serv:order:remove','#','admin','2022-08-23 11:29:48','',NULL,''),(1561918253834989573,'药品订单导出',1561918253834989568,5,'#','',NULL,1,0,'F','0','0','reghos-serv:order:export','#','admin','2022-08-23 11:29:48','',NULL,''),(1562274906202238976,'处方',1561329116089688066,1,'prescription','customer-serv/prescription/index',NULL,1,0,'C','0','0','customer-serv:prescription:list','people','admin','2022-08-24 11:07:53','admin','2022-08-25 14:15:33','处方菜单'),(1562274906202238977,'处方查询',1562274906202238976,1,'#','',NULL,1,0,'F','0','0','customer-serv:prescription:query','#','admin','2022-08-24 11:07:53','',NULL,''),(1562274906202238978,'处方新增',1562274906202238976,2,'#','',NULL,1,0,'F','0','0','customer-serv:prescription:add','#','admin','2022-08-24 11:07:53','',NULL,''),(1562274906202238979,'处方修改',1562274906202238976,3,'#','',NULL,1,0,'F','0','0','customer-serv:prescription:edit','#','admin','2022-08-24 11:07:53','',NULL,''),(1562274906202238980,'处方删除',1562274906202238976,4,'#','',NULL,1,0,'F','0','0','customer-serv:prescription:remove','#','admin','2022-08-24 11:07:53','',NULL,''),(1562274906202238981,'处方导出',1562274906202238976,5,'#','',NULL,1,0,'F','0','0','customer-serv:prescription:export','#','admin','2022-08-24 11:07:53','',NULL,''),(1562396381391437824,'病历',1561329116089688066,1,'record','customer-serv/record/index',NULL,1,0,'C','0','0','customer-serv:record:list','build','admin','2022-08-24 19:10:45','admin','2022-08-28 21:20:19','病历菜单'),(1562396381391437825,'病历查询',1562396381391437824,1,'#','',NULL,1,0,'F','0','0','customer-serv:record:query','#','admin','2022-08-24 19:10:45','',NULL,''),(1562396381391437826,'病历新增',1562396381391437824,2,'#','',NULL,1,0,'F','0','0','customer-serv:record:add','#','admin','2022-08-24 19:10:45','',NULL,''),(1562396381391437827,'病历修改',1562396381391437824,3,'#','',NULL,1,0,'F','0','0','customer-serv:record:edit','#','admin','2022-08-24 19:10:45','',NULL,''),(1562396381391437828,'病历删除',1562396381391437824,4,'#','',NULL,1,0,'F','0','0','customer-serv:record:remove','#','admin','2022-08-24 19:10:45','',NULL,''),(1562396381391437829,'病历导出',1562396381391437824,5,'#','',NULL,1,0,'F','0','0','customer-serv:record:export','#','admin','2022-08-24 19:10:45','',NULL,''),(1562620092644089856,'检查项目',3,1,'inspect','reghos-serv/inspect/index',NULL,1,0,'C','0','0','reghos-serv:inspect:list','#','admin','2022-08-25 09:59:08','',NULL,'检查项目菜单'),(1562620092644089857,'检查项目查询',1562620092644089856,1,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect:query','#','admin','2022-08-25 09:59:08','',NULL,''),(1562620092644089858,'检查项目新增',1562620092644089856,2,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect:add','#','admin','2022-08-25 09:59:08','',NULL,''),(1562620092644089859,'检查项目修改',1562620092644089856,3,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect:edit','#','admin','2022-08-25 09:59:08','',NULL,''),(1562620092644089860,'检查项目删除',1562620092644089856,4,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect:remove','#','admin','2022-08-25 09:59:08','',NULL,''),(1562620092644089861,'检查项目导出',1562620092644089856,5,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect:export','#','admin','2022-08-25 09:59:08','',NULL,''),(1562626107729010688,'血糖结果',3,1,'glucose','reghos-serv/glucose/index',NULL,1,0,'C','0','0','reghos-serv:glucose:list','#','admin','2022-08-25 10:27:24','',NULL,'血糖结果菜单'),(1562626107729010689,'血糖结果查询',1562626107729010688,1,'#','',NULL,1,0,'F','0','0','reghos-serv:glucose:query','#','admin','2022-08-25 10:27:24','',NULL,''),(1562626107729010690,'血糖结果新增',1562626107729010688,2,'#','',NULL,1,0,'F','0','0','reghos-serv:glucose:add','#','admin','2022-08-25 10:27:24','',NULL,''),(1562626107729010691,'血糖结果修改',1562626107729010688,3,'#','',NULL,1,0,'F','0','0','reghos-serv:glucose:edit','#','admin','2022-08-25 10:27:24','',NULL,''),(1562626107729010692,'血糖结果删除',1562626107729010688,4,'#','',NULL,1,0,'F','0','0','reghos-serv:glucose:remove','#','admin','2022-08-25 10:27:24','',NULL,''),(1562626107729010693,'血糖结果导出',1562626107729010688,5,'#','',NULL,1,0,'F','0','0','reghos-serv:glucose:export','#','admin','2022-08-25 10:27:24','',NULL,''),(1562626482346971136,'手术订单',1561329975603240961,1,'operation_order','reghos-serv/operation_order/index',NULL,1,0,'C','0','0','reghos-serv:operation_order:list','#','admin','2022-08-25 10:27:23','admin','2022-08-26 10:02:48','手术订单菜单'),(1562626482346971137,'手术订单查询',1562626482346971136,1,'#','',NULL,1,0,'F','0','0','reghos-serv:operation_order:query','#','admin','2022-08-25 10:27:23','',NULL,''),(1562626482346971138,'手术订单新增',1562626482346971136,2,'#','',NULL,1,0,'F','0','0','reghos-serv:operation_order:add','#','admin','2022-08-25 10:27:23','',NULL,''),(1562626482346971139,'手术订单修改',1562626482346971136,3,'#','',NULL,1,0,'F','0','0','reghos-serv:operation_order:edit','#','admin','2022-08-25 10:27:23','',NULL,''),(1562626482346971140,'手术订单删除',1562626482346971136,4,'#','',NULL,1,0,'F','0','0','reghos-serv:operation_order:remove','#','admin','2022-08-25 10:27:23','',NULL,''),(1562626482346971141,'手术订单导出',1562626482346971136,5,'#','',NULL,1,0,'F','0','0','reghos-serv:operation_order:export','#','admin','2022-08-25 10:27:23','',NULL,''),(1562632924969881600,'病床床位',1561329975603240961,1,'bed','reghos-serv/bed/index',NULL,1,0,'C','0','0','reghos-serv:bed:list','input','admin','2022-08-25 10:58:59','admin','2022-08-28 21:22:37','病床床位菜单'),(1562632924969881601,'病床床位查询',1562632924969881600,1,'#','',NULL,1,0,'F','0','0','reghos-serv:bed:query','#','admin','2022-08-25 10:58:59','',NULL,''),(1562632924969881602,'病床床位新增',1562632924969881600,2,'#','',NULL,1,0,'F','0','0','reghos-serv:bed:add','#','admin','2022-08-25 10:58:59','',NULL,''),(1562632924969881603,'病床床位修改',1562632924969881600,3,'#','',NULL,1,0,'F','0','0','reghos-serv:bed:edit','#','admin','2022-08-25 10:58:59','',NULL,''),(1562632924969881604,'病床床位删除',1562632924969881600,4,'#','',NULL,1,0,'F','0','0','reghos-serv:bed:remove','#','admin','2022-08-25 10:58:59','',NULL,''),(1562632924969881605,'病床床位导出',1562632924969881600,5,'#','',NULL,1,0,'F','0','0','reghos-serv:bed:export','#','admin','2022-08-25 10:58:59','',NULL,''),(1562650123915329536,'血常规结果',3,1,'routine','reghos-serv/routine/index',NULL,1,0,'C','0','0','reghos-serv:routine:list','#','admin','2022-08-25 11:59:43','',NULL,'血常规结果菜单'),(1562650123915329537,'血常规结果查询',1562650123915329536,1,'#','',NULL,1,0,'F','0','0','reghos-serv:routine:query','#','admin','2022-08-25 11:59:43','',NULL,''),(1562650123915329538,'血常规结果新增',1562650123915329536,2,'#','',NULL,1,0,'F','0','0','reghos-serv:routine:add','#','admin','2022-08-25 11:59:43','',NULL,''),(1562650123915329539,'血常规结果修改',1562650123915329536,3,'#','',NULL,1,0,'F','0','0','reghos-serv:routine:edit','#','admin','2022-08-25 11:59:43','',NULL,''),(1562650123915329540,'血常规结果删除',1562650123915329536,4,'#','',NULL,1,0,'F','0','0','reghos-serv:routine:remove','#','admin','2022-08-25 11:59:43','',NULL,''),(1562650123915329541,'血常规结果导出',1562650123915329536,5,'#','',NULL,1,0,'F','0','0','reghos-serv:routine:export','#','admin','2022-08-25 11:59:43','',NULL,''),(1562699166112579584,'检查项目订单',3,1,'inspect_orderr','reghos-serv/inspect_orderr/index',NULL,1,0,'C','0','0','reghos-serv:inspect_orderr:list','#','admin','2022-08-25 15:13:33','',NULL,'检查项目订单菜单'),(1562699166112579585,'检查项目订单查询',1562699166112579584,1,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect_orderr:query','#','admin','2022-08-25 15:13:33','',NULL,''),(1562699166112579586,'检查项目订单新增',1562699166112579584,2,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect_orderr:add','#','admin','2022-08-25 15:13:33','',NULL,''),(1562699166112579587,'检查项目订单修改',1562699166112579584,3,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect_orderr:edit','#','admin','2022-08-25 15:13:33','',NULL,''),(1562699166112579588,'检查项目订单删除',1562699166112579584,4,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect_orderr:remove','#','admin','2022-08-25 15:13:33','',NULL,''),(1562699166112579589,'检查项目订单导出',1562699166112579584,5,'#','',NULL,1,0,'F','0','0','reghos-serv:inspect_orderr:export','#','admin','2022-08-25 15:13:33','',NULL,'');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `notice_id` bigint NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1563375928573198337,'图片公告测试','2',_binary '<p><img src=\"http://127.0.0.1:9000/ataoxz/2022/08/27/47d09a0180684108b48dd8bc2f272a8b.png\"><img src=\"http://127.0.0.1:9000/ataoxz/2022/08/27/afee46ca5f344dc0b5de94bd0b5f61b9.jpg\"></p>','0','admin','2022-08-27 12:00:42','admin','2022-08-27 12:00:42',NULL),(1564171227392180226,'测试','1',_binary '<p>测试<img src=\"http://172.17.0.1:9000/ataoys/2022/08/29/05544a051f61413ba3f788b74a6d8e41.jpg\"></p>','0','admin','2022-08-29 16:40:56','admin','2022-08-29 16:40:56',NULL);
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` VALUES (1563903655388692482,'操作日志',9,'com.ruoyi.system.controller.SysOperlogController.clean()','DELETE',1,'admin','','/operlog/clean','192.168.227.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-28 22:57:42'),(1564055698328133633,'菜单管理',2,'com.ruoyi.system.controller.SysMenuController.edit()','PUT',1,'admin','','/menu','127.0.0.1','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-21 01:31:06\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 09:01:51\",\"params\":{},\"parentName\":null,\"parentId\":2,\"children\":[],\"menuId\":110,\"menuName\":\"XxlJob控制台\",\"orderNum\":2,\"path\":\"http://localhost:9900\",\"component\":\"\",\"queryParam\":\"\",\"isFrame\":\"0\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"monitor:job:list\",\"icon\":\"job\",\"remark\":\"定时任务菜单\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:01:52'),(1564056979625091074,'菜单管理',2,'com.ruoyi.system.controller.SysMenuController.edit()','PUT',1,'admin','','/menu','127.0.0.1','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-21 01:31:06\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 09:06:57\",\"params\":{},\"parentName\":null,\"parentId\":2,\"children\":[],\"menuId\":110,\"menuName\":\"XxlJob控制台\",\"orderNum\":2,\"path\":\"http://localhost:9900\",\"component\":\"\",\"queryParam\":\"\",\"isFrame\":\"0\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"1\",\"status\":\"0\",\"perms\":\"monitor:job:list\",\"icon\":\"job\",\"remark\":\"定时任务菜单\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:06:57'),(1564057040014680065,'菜单管理',2,'com.ruoyi.system.controller.SysMenuController.edit()','PUT',1,'admin','','/menu','127.0.0.1','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-21 01:31:06\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 09:07:11\",\"params\":{},\"parentName\":null,\"parentId\":1,\"children\":[],\"menuId\":105,\"menuName\":\"字典管理\",\"orderNum\":6,\"path\":\"dict\",\"component\":\"system/dict/index\",\"queryParam\":\"\",\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"1\",\"status\":\"0\",\"perms\":\"system:dict:list\",\"icon\":\"dict\",\"remark\":\"字典管理菜单\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:07:12'),(1564057549404512258,'菜单管理',2,'com.ruoyi.system.controller.SysMenuController.edit()','PUT',1,'admin','','/menu','127.0.0.1','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-23 11:29:45\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 09:09:13\",\"params\":{},\"parentName\":null,\"parentId\":\"1561328890075422722\",\"children\":[],\"menuId\":\"1561918213853384704\",\"menuName\":\"挂号列表\",\"orderNum\":1,\"path\":\"register\",\"component\":\"reghos-serv/register/index\",\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"reghos-serv:register:list\",\"icon\":\"skill\",\"remark\":\"挂号菜单\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:09:13'),(1564057704891555842,'处方',3,'xian.woniuxy.customer.controller.PrescriptionController.remove()','DELETE',1,'admin','','/prescription/1','127.0.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:09:50'),(1564057713959641089,'处方',3,'xian.woniuxy.customer.controller.PrescriptionController.remove()','DELETE',1,'admin','','/prescription/200','127.0.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:09:52'),(1564057723614928897,'处方',3,'xian.woniuxy.customer.controller.PrescriptionController.remove()','DELETE',1,'admin','','/prescription/201','127.0.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:09:55'),(1564057923892944898,'个人信息',2,'com.ruoyi.system.controller.SysProfileController.updateProfile()','PUT',1,'admin','','/user/profile','127.0.0.1','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-21 01:31:06\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 09:10:42\",\"params\":{},\"userSalary\":\"11232.00\",\"userBirth\":\"2022-08-18 00:00:00\",\"userId\":1,\"deptId\":100,\"userName\":null,\"nickName\":\"疯狂的狮子\",\"userType\":\"sys_user\",\"email\":\"xxx@xx.com\",\"phonenumber\":\"15888888888\",\"sex\":\"0\",\"avatar\":\"\",\"status\":\"0\",\"delFlag\":\"0\",\"loginIp\":\"127.0.0.1\",\"loginDate\":\"2022-08-21 01:31:06\",\"remark\":\"管理员\",\"dept\":{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"deptId\":100,\"deptName\":\"管理\",\"orderNum\":0,\"leader\":\"若依\",\"phone\":null,\"email\":null,\"status\":\"0\",\"delFlag\":null,\"ancestors\":\"0\"},\"roles\":[{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"roleId\":1,\"roleName\":\"超级管理员\",\"roleKey\":\"admin\",\"roleSort\":1,\"dataScope\":\"1\",\"menuCheckStrictly\":null,\"deptCheckStrictly\":null,\"status\":\"0\",\"delFlag\":null,\"remark\":null,\"flag\":false,\"menuIds\":null,\"deptIds\":null,\"admin\":true}],\"roleIds\":null,\"postIds\":null,\"roleId\":null,\"admin\":true}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:10:43'),(1564060769803706369,'菜单管理',2,'com.ruoyi.system.controller.SysMenuController.edit()','PUT',1,'admin','','/menu','127.0.0.1','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-21 01:31:06\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 09:22:00\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":3,\"menuName\":\"其他项目\",\"orderNum\":22,\"path\":\"tool\",\"component\":null,\"queryParam\":\"\",\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"\",\"icon\":\"tool\",\"remark\":\"系统工具目录\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:22:01'),(1564062286261313537,'血常规结果;',1,'xian.woniuxy.reghos.controller.BloodRoutineController.add()','POST',1,'admin','','/routine','127.0.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"brId\":\"1564062284734631938\",\"patientId\":null,\"patientName\":\"1\",\"registerId\":1,\"wbcReference\":\"1\",\"rbcReference\":\"1\",\"remark\":\"1\",\"wbc\":\"1\",\"rbc\":\"1\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:28:03'),(1564063593117302786,'在线用户',7,'com.ruoyi.system.controller.SysUserOnlineController.forceLogout()','DELETE',1,'admin','','/online/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJzeXNfdXNlcjoxIiwicm5TdHIiOiJqbngyTWt0Tk1iNUw2N2RJa05YT3h5QjV3VUY2bXZPaSJ9.0JTAqJPARmH4FETO4k_0ccwrhleVSEscx6SYiPE6qnQ','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:33:14'),(1564063664105897986,'在线用户',7,'com.ruoyi.system.controller.SysUserOnlineController.forceLogout()','DELETE',1,'admin','','/online/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJzeXNfdXNlcjoxIiwicm5TdHIiOiJDdEI5UkJOQnFJSWZQUVUxa0M0T1FGSWg1WFh5a09YUiJ9.0P5CMAnAV_7snZ-JUgpQ6E6ksFdSdC_tTlNDvzSW_uc','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:33:31'),(1564063677821272065,'在线用户',7,'com.ruoyi.system.controller.SysUserOnlineController.forceLogout()','DELETE',1,'admin','','/online/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJzeXNfdXNlcjoxIiwicm5TdHIiOiIxaGZ5QnpETG14bUZ5V3kzQ1hrSnlxekFBTWJxTElXNSJ9.MwuYeUHDjWXcH8LQtNpwKAAt0X_5T8CKjTz6kyHbt6Q','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:33:34'),(1564063690236411905,'在线用户',7,'com.ruoyi.system.controller.SysUserOnlineController.forceLogout()','DELETE',1,'admin','','/online/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJzeXNfdXNlcjoxIiwicm5TdHIiOiJPakZwQVdaZUxoSjZIUEdUazNvd0FiSFEwVHZVcUVlQSJ9.A8X7Zz8yqacmZYF3GXyursCpafCZjOCbX6ZJXQ6Y4iw','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:33:37'),(1564063703352000513,'在线用户',7,'com.ruoyi.system.controller.SysUserOnlineController.forceLogout()','DELETE',1,'admin','','/online/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJzeXNfdXNlcjoxIiwicm5TdHIiOiJkSEpsYlhmVXNBZ0p2MHNxMHY1cFlaVW5YYmhhSnZQTCJ9.G1n_PBY5kIE-ooRSUclgkl9QBK9QGIl8oxDweU_QX58','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:33:40'),(1564063715737780226,'在线用户',7,'com.ruoyi.system.controller.SysUserOnlineController.forceLogout()','DELETE',1,'admin','','/online/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJzeXNfdXNlcjoxIiwicm5TdHIiOiJ0QXdYWHRzZnY1Z3k0NTM0cU5vWHhhQWlkalpPQ2VKaCJ9.TPwLw0kTC7nDRQnhIkDwhC4Oy2f4YcQvAwK8E-iYHUQ','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 09:33:43'),(1564134490611712001,'药品订单',3,'xian.woniuxy.reghos.controller.MedicineOrderController.remove()','DELETE',1,'admin','','/medicine_order/1562382531900264450','172.16.9.123','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:14:57'),(1564134612330414082,'药品订单',3,'xian.woniuxy.reghos.controller.MedicineOrderController.remove()','DELETE',1,'admin','','/medicine_order/1562727263419559938,1562989614082654210,1562988688198434817,1562994693909086209,1563015907331657730,1563706400444178433','172.16.9.123','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:15:26'),(1564135802204127234,'菜单管理',2,'com.ruoyi.system.controller.SysMenuController.edit()','PUT',1,'admin','','/menu','172.17.0.1','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-23 04:19:08\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 06:20:09\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":2606,\"menuName\":\"客户端页面\",\"orderNum\":1,\"path\":\"customer\",\"component\":\"\",\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"1\",\"status\":\"0\",\"perms\":\"customer:list\",\"icon\":\"#\",\"remark\":\"客户端页面\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:20:10'),(1564136097160167425,'菜单管理',2,'com.ruoyi.system.controller.SysMenuController.edit()','PUT',1,'admin','','/menu','172.17.0.1','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-20 17:31:06\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 06:21:20\",\"params\":{},\"parentName\":null,\"parentId\":\"1561322637831970817\",\"children\":[],\"menuId\":104,\"menuName\":\"岗位管理\",\"orderNum\":5,\"path\":\"post\",\"component\":\"system/post/index\",\"queryParam\":\"\",\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"1\",\"status\":\"0\",\"perms\":\"system:post:list\",\"icon\":\"post\",\"remark\":\"岗位管理菜单\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:21:20'),(1564139803830190082,'排班',3,'com.ruoyi.system.controller.ScheduleController.remove()','DELETE',1,'admin','','/schedule/1562608287004880898,1562608357012008962,1562608451010555906,1562608557533294594,1562608644787400706,1562608703520239617,1562729647890841601','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:36:04'),(1564140040636399618,'挂号',3,'xian.woniuxy.reghos.controller.RegisterController.remove()','DELETE',1,'admin','','/register/2','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:37:01'),(1564140060324462593,'挂号',3,'xian.woniuxy.reghos.controller.RegisterController.remove()','DELETE',1,'admin','','/register/3','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:37:05'),(1564140069673566210,'挂号',3,'xian.woniuxy.reghos.controller.RegisterController.remove()','DELETE',1,'admin','','/register/3','172.17.0.1','','','{\"code\":500,\"msg\":\"操作失败\",\"data\":null}',0,'','2022-08-29 14:37:08'),(1564140081178542081,'挂号',3,'xian.woniuxy.reghos.controller.RegisterController.remove()','DELETE',1,'admin','','/register/5','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:37:10'),(1564140099125968897,'挂号',3,'xian.woniuxy.reghos.controller.RegisterController.remove()','DELETE',1,'admin','','/register/4','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:37:15'),(1564140115680886786,'挂号',3,'xian.woniuxy.reghos.controller.RegisterController.remove()','DELETE',1,'admin','','/register/1562781395636531202','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:37:19'),(1564140430157217793,'处方',5,'xian.woniuxy.customer.controller.PrescriptionController.export()','POST',1,'admin','','/prescription/export','172.16.9.123','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"prescriptionId\":null,\"patientId\":null,\"patientName\":null,\"registerId\":null,\"recordId\":null,\"depId\":null,\"depName\":null,\"prescriptionDate\":null,\"userId\":null,\"userName\":null,\"rpMedicine\":null,\"rpInspect\":null,\"diagnosis\":null,\"remark\":null}','',0,'','2022-08-29 14:38:34'),(1564141843910295554,'挂号',3,'xian.woniuxy.reghos.controller.RegisterController.remove()','DELETE',1,'admin','','/register/1562781424598200322','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 14:44:11'),(1564148147546554369,'排班',1,'com.ruoyi.system.controller.ScheduleController.add()','POST',1,'admin','','/schedule','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"scheduleId\":null,\"userId\":\"1562376351004221441\",\"userName\":null,\"depId\":\"1562373635540508674\",\"depName\":null,\"avatar\":null,\"scheduleDate\":\"2022-08-30\",\"scheduleTime\":\"上午\",\"scheduleNum\":3,\"remark\":null}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 15:09:14'),(1564148937363357698,'排班',3,'com.ruoyi.system.controller.ScheduleController.remove()','DELETE',1,'admin','','/schedule/1564148146141462530','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 15:12:22'),(1564157406078369794,'排班',1,'com.ruoyi.system.controller.ScheduleController.add()','POST',1,'admin','','/schedule','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"scheduleId\":null,\"userId\":\"1562376351004221441\",\"userName\":null,\"depId\":\"1562373635540508674\",\"depName\":null,\"avatar\":null,\"scheduleDate\":\"2022-08-30\",\"scheduleTime\":\"上午\",\"scheduleNum\":3,\"remark\":null}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 15:46:01'),(1564157582528544770,'病历;病历',1,'xian.woniuxy.customer.controller.RecordController.add()','POST',1,'admin','','/record','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"recordId\":\"1564157579808010242\",\"patientId\":\"1562649395789361153\",\"patientName\":\"建档用户88\",\"recordDesc\":\"良好\",\"recordHealth\":\"亚健康\",\"recordInfect\":\"\",\"recordInfectDetails\":\"\",\"recordAllergy\":\"\",\"recordAllergyDetails\":\"\",\"recordInherit\":\"无\",\"recordInheritDetails\":\"\",\"recordDate\":null,\"remark\":\"111\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 15:46:43'),(1564158134930964482,'病床床位;',3,'xian.woniuxy.reghos.controller.BedController.remove()','DELETE',1,'admin','','/bed/4','172.16.9.123','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 15:48:55'),(1564158162235883521,'病床床位;',3,'xian.woniuxy.reghos.controller.BedController.remove()','DELETE',1,'admin','','/bed/5,6','172.16.9.123','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 15:49:01'),(1564158221266518017,'病床床位;',1,'xian.woniuxy.reghos.controller.BedController.add()','POST',1,'admin','','/bed','172.16.9.123','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"depName\":null,\"bedId\":\"1564158219745046530\",\"bedNum\":\"32\",\"depId\":\"1562373635540508674\",\"bedState\":null,\"softDel\":null,\"remark\":\"323232\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 15:49:15'),(1564158785563983874,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.16.9.123','','','',1,'创建Bucket失败, 请核对配置信息:[Unable to execute HTTP request: Connect to 127.0.0.1:9000 [/127.0.0.1] failed: Connection refused (Connection refused)]','2022-08-29 15:51:30'),(1564160528800612354,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.17.0.1','','','',1,'创建Bucket失败, 请核对配置信息:[Unable to execute HTTP request: Connect to 127.0.0.1:9000 [/127.0.0.1] failed: Connection refused (Connection refused)]','2022-08-29 15:58:25'),(1564160658576572418,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.17.0.1','','','',1,'创建Bucket失败, 请核对配置信息:[Unable to execute HTTP request: Connect to 127.0.0.1:9000 [/127.0.0.1] failed: Connection refused (Connection refused)]','2022-08-29 15:58:56'),(1564160873937305602,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.17.0.1','','','',1,'创建Bucket失败, 请核对配置信息:[Unable to execute HTTP request: Connect to 127.0.0.1:9000 [/127.0.0.1] failed: Connection refused (Connection refused)]','2022-08-29 15:59:48'),(1564160963116597250,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.17.0.1','','','',1,'创建Bucket失败, 请核对配置信息:[Unable to execute HTTP request: Connect to 127.0.0.1:9000 [/127.0.0.1] failed: Connection refused (Connection refused)]','2022-08-29 16:00:09'),(1564161195594285058,'对象存储配置',2,'com.ruoyi.resource.controller.SysOssConfigController.edit()','PUT',1,'admin','','/oss/config','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"ossConfigId\":1,\"configKey\":\"minio\",\"accessKey\":\"minioadmin\",\"secretKey\":\"minioadmin\",\"bucketName\":\"ataoys\",\"prefix\":\"\",\"endpoint\":\"127.0.0.1:9000\",\"domain\":\"\",\"isHttps\":\"N\",\"status\":\"0\",\"region\":\"\",\"ext1\":\"\",\"remark\":\"\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:01:04'),(1564161268227047426,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.17.0.1','','','',1,'创建Bucket失败, 请核对配置信息:[Unable to execute HTTP request: Connect to 127.0.0.1:9000 [/127.0.0.1] failed: Connection refused (Connection refused)]','2022-08-29 16:01:22'),(1564161822802116610,'对象存储配置',2,'com.ruoyi.resource.controller.SysOssConfigController.edit()','PUT',1,'admin','','/oss/config','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"ossConfigId\":1,\"configKey\":\"minio\",\"accessKey\":\"minioadmin\",\"secretKey\":\"minioadmin\",\"bucketName\":\"ataoys\",\"prefix\":\"\",\"endpoint\":\"172.17.0.1:9000\",\"domain\":\"\",\"isHttps\":\"N\",\"status\":\"0\",\"region\":\"\",\"ext1\":\"\",\"remark\":\"\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:03:34'),(1564161900564512770,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"ossId\":\"1564161899836780545\",\"url\":\"http://172.17.0.1:9000/ataoys/2022/08/29/4a531cea45084bedaf6d08b72ff9b924.jpg\",\"fileName\":\"4.jpg\"}}',0,'','2022-08-29 16:03:52'),(1564162175014600705,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"ossId\":\"1564162174278479873\",\"url\":\"http://172.17.0.1:9000/ataoys/2022/08/29/37b35db10e684fc4a57141b745cb8d74.jpg\",\"fileName\":\"2.jpg\"}}',0,'','2022-08-29 16:04:58'),(1564162585574047745,'对象存储配置',2,'com.ruoyi.resource.controller.SysOssConfigController.edit()','PUT',1,'admin','','/oss/config','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"ossConfigId\":1,\"configKey\":\"minio\",\"accessKey\":\"minioadmin\",\"secretKey\":\"minioadmin\",\"bucketName\":\"ataoys\",\"prefix\":\"\",\"endpoint\":\"172.16.9.101\",\"domain\":\"\",\"isHttps\":\"N\",\"status\":\"0\",\"region\":\"\",\"ext1\":\"\",\"remark\":\"\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:06:36'),(1564162726670434306,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.16.9.123','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"ossId\":\"1564162726034976770\",\"url\":\"http://172.17.0.1:9000/ataoys/2022/08/29/77dc35a434694d14aa5208d3a5343682.jpg\",\"fileName\":\"6.jpg\"}}',0,'','2022-08-29 16:07:09'),(1564162745767100418,'OSS对象存储',3,'com.ruoyi.resource.controller.SysOssController.remove()','DELETE',1,'admin','','/oss/1564162726034976770','172.16.9.123','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:07:14'),(1564162759482474498,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.16.9.123','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"ossId\":\"1564162758733770753\",\"url\":\"http://172.17.0.1:9000/ataoys/2022/08/29/c700dc521af04f378e93023e72901dc0.png\",\"fileName\":\"5.png\"}}',0,'','2022-08-29 16:07:17'),(1564162823135232001,'OSS对象存储',3,'com.ruoyi.resource.controller.SysOssController.remove()','DELETE',1,'admin','','/oss/1564162758733770753,1564162174278479873','172.16.9.123','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:07:32'),(1564162862091927554,'OSS对象存储',3,'com.ruoyi.resource.controller.SysOssController.remove()','DELETE',1,'admin','','/oss/1564162758733770753','172.17.0.1','','','{\"code\":500,\"msg\":\"操作失败\",\"data\":null}',0,'','2022-08-29 16:07:42'),(1564162876650356738,'OSS对象存储',3,'com.ruoyi.resource.controller.SysOssController.remove()','DELETE',1,'admin','','/oss/1564162174278479873','172.17.0.1','','','{\"code\":500,\"msg\":\"操作失败\",\"data\":null}',0,'','2022-08-29 16:07:45'),(1564162890172792833,'OSS对象存储',3,'com.ruoyi.resource.controller.SysOssController.remove()','DELETE',1,'admin','','/oss/1564161899836780545','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:07:48'),(1564162966685286401,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"ossId\":\"1564162965907222529\",\"url\":\"http://172.17.0.1:9000/ataoys/2022/08/29/a1539214db774ebcb543bc8a2d9b503d.jpg\",\"fileName\":\"4.jpg\"}}',0,'','2022-08-29 16:08:07'),(1564163438678704130,'菜单管理',2,'com.ruoyi.system.controller.SysMenuController.edit()','PUT',1,'admin','','/menu','172.16.9.123','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-20 17:31:06\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 08:09:59\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":3,\"menuName\":\"检查项目\",\"orderNum\":22,\"path\":\"tool\",\"component\":null,\"queryParam\":\"\",\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"\",\"icon\":\"tool\",\"remark\":\"系统工具目录\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:09:59'),(1564165454918385666,'药品订单',3,'xian.woniuxy.reghos.controller.MedicineOrderController.details()','GET',1,'admin','','/medicine_order/details/1','172.16.9.131','','','',1,'','2022-08-29 16:18:00'),(1564165798960365570,'药品订单',3,'xian.woniuxy.reghos.controller.MedicineOrderController.remove()','DELETE',1,'admin','','/medicine_order/1,2,3,1562988687653175298,1564165617436143618,1564165618245644289,1564165618254032898','172.16.9.123','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:19:22'),(1564165974437462018,'血糖结果;',5,'xian.woniuxy.reghos.controller.BloodGlucoseController.export()','POST',1,'admin','','/glucose/export','172.16.9.131','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"bgId\":null,\"patientId\":null,\"patientName\":null,\"registerId\":null,\"gluReference\":null,\"phReference\":null,\"remark\":null,\"glu\":null,\"ph\":null}','',0,'','2022-08-29 16:20:04'),(1564166399974768642,'排班',3,'com.ruoyi.system.controller.ScheduleController.remove()','DELETE',1,'admin','','/schedule/1564157405478584322','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:21:45'),(1564166504790425602,'病床床位;',1,'xian.woniuxy.reghos.controller.BedController.add()','POST',1,'admin','','/bed','172.16.9.131','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"depName\":null,\"bedId\":\"1564166503516418049\",\"bedNum\":\"122\",\"depId\":100,\"bedState\":null,\"softDel\":null,\"remark\":\"1222\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:22:10'),(1564168138283089921,'血糖结果;',2,'xian.woniuxy.reghos.controller.BloodGlucoseController.edit()','PUT',1,'admin','','/glucose','172.16.9.131','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"bgId\":\"1562639468453756930\",\"patientId\":1,\"patientName\":\"加密测试\",\"registerId\":3,\"gluReference\":\"3.9-6.1mmol/L\",\"phReference\":\"5.5-8.0\",\"remark\":\"公平\",\"glu\":\"5.5\",\"ph\":\"5.9\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:28:40'),(1564168177109762050,'血糖结果;',2,'xian.woniuxy.reghos.controller.BloodGlucoseController.edit()','PUT',1,'admin','','/glucose','172.16.9.131','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"bgId\":\"1562639468453756930\",\"patientId\":1,\"patientName\":\"加密测试\",\"registerId\":3,\"gluReference\":\"3.9-6.1mmol/L\",\"phReference\":\"5.5-8.0\",\"remark\":\"公平\",\"glu\":\"5.5\",\"ph\":\"5.9\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:28:49'),(1564168562704711681,'排班',1,'com.ruoyi.system.controller.ScheduleController.add()','POST',1,'admin','','/schedule','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"scheduleId\":null,\"userId\":\"1562376351004221441\",\"userName\":null,\"depId\":\"1562373635540508674\",\"depName\":null,\"avatar\":null,\"scheduleDate\":\"2022-08-30\",\"scheduleTime\":\"上午\",\"scheduleNum\":3,\"remark\":null}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:30:21'),(1564168734847336449,'用户',1,'xian.woniuxy.customer.controller.PatientController.add()','POST',1,'admin','','/patient','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"patientId\":\"1564168733460590593\",\"patientPhone\":\"1234\",\"patientPassword\":null,\"patientName\":\"测试1234\",\"patientSex\":\"男\",\"patientCardId\":\"1234\",\"patientPhoto\":null,\"patientBirthday\":\"2022-08-01\",\"patientRegisterDate\":null,\"patientMoney\":null,\"patientTest\":null,\"remark\":\"\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:31:02'),(1564169285639143425,'处方',1,'xian.woniuxy.customer.controller.PrescriptionController.add()','POST',1,'yan','','/prescription','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"prescriptionId\":\"1564169284155928578\",\"patientId\":\"1564168733460590593\",\"patientName\":\"测试1234\",\"registerId\":\"1564168748647657473\",\"recordId\":null,\"depId\":\"1562373635540508674\",\"depName\":\"眼科\",\"prescriptionDate\":null,\"userId\":\"1562376351004221441\",\"userName\":\"yan\",\"rpMedicine\":\"阿莫西林*1\\n健胃消食片*2\",\"rpInspect\":\"无\",\"diagnosis\":\"感冒\",\"remark\":\"多喝水\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:33:13'),(1564169903053275138,'药品订单',3,'xian.woniuxy.reghos.controller.MedicineOrderController.details()','GET',1,'admin','','/medicine_order/details/1564169765497942018','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"moId\":\"1564169765497942018\",\"medicineId\":1,\"medicineNum\":1,\"medicinePrice\":\"38.00\",\"moTotalPrice\":\"38.00\",\"moDate\":\"2022-08-29 08:35:08\",\"remark\":null,\"prescriptionId\":\"1564169284155928578\",\"patientName\":\"测试1234\",\"nickName\":\"眼科医生\",\"medicineName\":\"阿莫西林散热片\"}}',0,'','2022-08-29 16:35:40'),(1564169923622141953,'药品订单',3,'xian.woniuxy.reghos.controller.MedicineOrderController.details()','GET',1,'admin','','/medicine_order/details/1564169766227750914','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"moId\":\"1564169766227750914\",\"medicineId\":7,\"medicineNum\":2,\"medicinePrice\":\"34.00\",\"moTotalPrice\":\"68.00\",\"moDate\":\"2022-08-29 08:35:08\",\"remark\":null,\"prescriptionId\":\"1564169284155928578\",\"patientName\":\"测试1234\",\"nickName\":\"眼科医生\",\"medicineName\":\"健胃消食片\"}}',0,'','2022-08-29 16:35:45'),(1564170225473617922,'病历;病历',1,'xian.woniuxy.customer.controller.RecordController.add()','POST',1,'yan','','/record','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"recordId\":\"1564170224137203714\",\"patientId\":\"1562651264792195073\",\"patientName\":\"建档用户999\",\"recordDesc\":\"生病\",\"recordHealth\":\"亚健康\",\"recordInfect\":\"\",\"recordInfectDetails\":\"\",\"recordAllergy\":\"\",\"recordAllergyDetails\":\"\",\"recordInherit\":\"有\",\"recordInheritDetails\":\"遗传了帅气\",\"recordDate\":null,\"remark\":\"\"}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:36:57'),(1564171219527860225,'OSS对象存储',1,'com.ruoyi.resource.controller.SysOssController.upload()','POST',1,'admin','','/oss/upload','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"ossId\":\"1564171218762379265\",\"url\":\"http://172.17.0.1:9000/ataoys/2022/08/29/05544a051f61413ba3f788b74a6d8e41.jpg\",\"fileName\":\"1.jpg\"}}',0,'','2022-08-29 16:40:54'),(1564171228063268865,'通知公告',1,'com.ruoyi.system.controller.SysNoticeController.add()','POST',1,'admin','','/notice','172.17.0.1','','{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-08-29 08:40:56\",\"updateBy\":\"admin\",\"updateTime\":\"2022-08-29 08:40:56\",\"params\":{},\"noticeId\":\"1564171227392180226\",\"noticeTitle\":\"测试\",\"noticeType\":\"1\",\"noticeContent\":\"<p>测试<img src=\\\"http://172.17.0.1:9000/ataoys/2022/08/29/05544a051f61413ba3f788b74a6d8e41.jpg\\\"></p>\",\"status\":\"0\",\"remark\":null}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-08-29 16:40:56'),(1564171636684947457,'用户头像',2,'com.ruoyi.system.controller.SysProfileController.avatar()','POST',1,'admin','','/user/profile/avatar','172.17.0.1','','','{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"imgUrl\":\"http://172.17.0.1:9000/ataoys/2022/08/29/37aaa60394ff4535bb9c7dabd762d7b3.jpg\"}}',0,'','2022-08-29 16:42:34'),(1565904703372427265,'排班',1,'com.ruoyi.system.controller.ScheduleController.add()','POST',1,'admin','','/schedule','172.17.0.1','','{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"scheduleId\":null,\"userId\":\"1562376351004221441\",\"userName\":null,\"depId\":\"1562373635540508674\",\"depName\":null,\"avatar\":null,\"scheduleDate\":\"2022-08-30\",\"scheduleTime\":\"下午\",\"scheduleNum\":5,\"remark\":null}','{\"code\":200,\"msg\":\"操作成功\",\"data\":null}',0,'','2022-09-03 11:29:09');
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss`
--

DROP TABLE IF EXISTS `sys_oss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss` (
  `oss_id` bigint NOT NULL COMMENT '对象存储主键',
  `file_name` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名',
  `original_name` varchar(255) NOT NULL DEFAULT '' COMMENT '原名',
  `file_suffix` varchar(10) NOT NULL DEFAULT '' COMMENT '文件后缀名',
  `url` varchar(500) NOT NULL COMMENT 'URL地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '上传人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
  `service` varchar(10) NOT NULL DEFAULT 'minio' COMMENT '服务商',
  PRIMARY KEY (`oss_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='OSS对象存储表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss`
--

LOCK TABLES `sys_oss` WRITE;
/*!40000 ALTER TABLE `sys_oss` DISABLE KEYS */;
INSERT INTO `sys_oss` VALUES (1563373995011719170,'2022/08/27/27c6acba4a8c431ead3d502b4d06fe13.jpg','1.jpg','.jpg','http://127.0.0.1:9000/ataoxz/2022/08/27/27c6acba4a8c431ead3d502b4d06fe13.jpg','2022-08-27 11:53:01','admin','2022-08-27 11:53:01','admin','minio'),(1563375731931725825,'2022/08/27/47d09a0180684108b48dd8bc2f272a8b.png','5.png','.png','http://127.0.0.1:9000/ataoxz/2022/08/27/47d09a0180684108b48dd8bc2f272a8b.png','2022-08-27 11:59:55','admin','2022-08-27 11:59:55','admin','minio'),(1563375801116770306,'2022/08/27/afee46ca5f344dc0b5de94bd0b5f61b9.jpg','6.jpg','.jpg','http://127.0.0.1:9000/ataoxz/2022/08/27/afee46ca5f344dc0b5de94bd0b5f61b9.jpg','2022-08-27 12:00:12','admin','2022-08-27 12:00:12','admin','minio'),(1564162965907222529,'2022/08/29/a1539214db774ebcb543bc8a2d9b503d.jpg','4.jpg','.jpg','http://172.17.0.1:9000/ataoys/2022/08/29/a1539214db774ebcb543bc8a2d9b503d.jpg','2022-08-29 16:08:06','admin','2022-08-29 16:08:06','admin','minio'),(1564171218762379265,'2022/08/29/05544a051f61413ba3f788b74a6d8e41.jpg','1.jpg','.jpg','http://172.17.0.1:9000/ataoys/2022/08/29/05544a051f61413ba3f788b74a6d8e41.jpg','2022-08-29 16:40:54','admin','2022-08-29 16:40:54','admin','minio'),(1564171633704873985,'2022/08/29/37aaa60394ff4535bb9c7dabd762d7b3.jpg','1.jpg','.jpg','http://172.17.0.1:9000/ataoys/2022/08/29/37aaa60394ff4535bb9c7dabd762d7b3.jpg','2022-08-29 16:42:33','admin','2022-08-29 16:42:33','admin','minio');
/*!40000 ALTER TABLE `sys_oss` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss_config`
--

DROP TABLE IF EXISTS `sys_oss_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss_config` (
  `oss_config_id` bigint NOT NULL COMMENT '主建',
  `config_key` varchar(255) NOT NULL DEFAULT '' COMMENT '配置key',
  `access_key` varchar(255) DEFAULT '' COMMENT 'accessKey',
  `secret_key` varchar(255) DEFAULT '' COMMENT '秘钥',
  `bucket_name` varchar(255) DEFAULT '' COMMENT '桶名称',
  `prefix` varchar(255) DEFAULT '' COMMENT '前缀',
  `endpoint` varchar(255) DEFAULT '' COMMENT '访问站点',
  `domain` varchar(255) DEFAULT '' COMMENT '自定义域名',
  `is_https` char(1) DEFAULT 'N' COMMENT '是否https（Y=是,N=否）',
  `region` varchar(255) DEFAULT '' COMMENT '域',
  `status` char(1) DEFAULT '1' COMMENT '状态（0=正常,1=停用）',
  `ext1` varchar(255) DEFAULT '' COMMENT '扩展字段',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`oss_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对象存储配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss_config`
--

LOCK TABLES `sys_oss_config` WRITE;
/*!40000 ALTER TABLE `sys_oss_config` DISABLE KEYS */;
INSERT INTO `sys_oss_config` VALUES (1,'minio','minioadmin','minioadmin','ataoys','','172.16.9.101','','N','','0','','admin','2022-08-21 01:31:07','admin','2022-08-29 16:06:35',''),(2,'qiniu','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi','','s3-cn-north-1.qiniucs.com','','N','','1','','admin','2022-08-21 01:31:07','admin','2022-08-21 01:31:07',NULL),(3,'aliyun','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi','','oss-cn-beijing.aliyuncs.com','','N','','1','','admin','2022-08-21 01:31:07','admin','2022-08-21 01:31:07',NULL),(4,'qcloud','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi-1250000000','','cos.ap-beijing.myqcloud.com','','N','ap-beijing','1','','admin','2022-08-21 01:31:07','admin','2022-08-21 01:31:07',NULL),(5,'image','ruoyi','ruoyi123','ruoyi','image','127.0.0.1:9000','','N','','1','','admin','2022-08-21 01:31:07','admin','2022-08-21 01:31:07',NULL);
/*!40000 ALTER TABLE `sys_oss_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES (1,'ceo','管理员',1,'0','admin','2022-08-21 01:31:06','admin','2022-08-21 20:32:52',''),(2,'se','主治医生',2,'0','admin','2022-08-21 01:31:06','admin','2022-08-21 20:33:04',''),(3,'hr','医生',3,'0','admin','2022-08-21 01:31:06','admin','2022-08-21 20:33:14',''),(4,'user','护士',4,'0','admin','2022-08-21 01:31:06','admin','2022-08-21 20:33:25',''),(1561330678371459074,'yaoshi','药师',5,'0','admin','2022-08-21 20:33:37','admin','2022-08-21 20:33:37',NULL);
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2022-08-21 01:31:06','',NULL,'超级管理员'),(2,'医生','common',2,'2',1,1,'0','0','admin','2022-08-21 01:31:06','admin','2022-08-25 20:52:07','医生'),(3,'护士','test1',3,'4',1,1,'0','0','admin','2022-08-21 01:31:44','admin','2022-08-21 20:37:50',NULL),(4,'药师','test2',4,'5',1,1,'0','0','admin','2022-08-21 01:31:44','admin','2022-08-21 20:40:18',NULL),(5,'病人','patient',5,'1',1,1,'0','0','admin','2022-08-24 20:24:46','admin','2022-08-24 20:24:46',NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` VALUES (2,100),(2,101),(2,105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (2,5958),(2,1561329116089688066),(2,1562274906202238976),(2,1562274906202238977),(2,1562274906202238978),(2,1562274906202238979),(2,1562274906202238980),(2,1562274906202238981),(3,1561328890075422722),(3,1561329116089688066),(3,1561329975603240961),(4,5),(4,1500),(4,1501),(4,1502),(4,1503),(4,1504),(4,1505),(4,1506),(4,1507),(4,1508),(4,1509),(4,1510),(4,1511),(4,1561332259087196162),(5,2606),(5,2607),(5,2608);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL COMMENT '医生ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '医生账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '医生昵称',
  `user_type` varchar(10) DEFAULT 'sys_user' COMMENT '用户类型（sys_user系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '2' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `user_salary` decimal(8,2) DEFAULT NULL COMMENT '医生薪资',
  `user_birth` date DEFAULT NULL COMMENT '医生生日',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,100,'admin','疯狂的狮子','sys_user','xxx@xx.com','15888888888','0','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2022-08-21 01:31:06','admin','2022-08-21 01:31:06','admin','2022-08-29 09:10:42','管理员',11232.00,'2022-08-18'),(2,100,'lionli','疯狂的狮子Li','sys_user','crazyLionLi@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2022-08-21 01:31:06','admin','2022-08-21 01:31:06','admin','2022-08-24 22:51:41','测试员',4567.00,'2022-08-24'),(3,100,'test','本部门及以下 密码666666','sys_user','','','0','','$2a$10$b8yUzN0C71sbz.PhNOCgJe.Tu1yWC3RNrTyjSQ8p1W0.aaUXUJ.Ne','0','0','127.0.0.1','2022-08-21 01:31:44','admin','2022-08-21 01:31:44','admin','2022-08-24 17:03:15',NULL,8647.00,'2022-07-23'),(4,100,'test1','仅本人 密码666666','sys_user','','','0','','$2a$10$b8yUzN0C71sbz.PhNOCgJe.Tu1yWC3RNrTyjSQ8p1W0.aaUXUJ.Ne','0','0','127.0.0.1','2022-08-21 01:31:44','admin','2022-08-21 01:31:44','admin','2022-08-24 17:03:24',NULL,4578.00,'2022-08-12'),(5,1562373498806198273,'gu','骨科医生','sys_user','','15811111111','0','','$2a$10$s0K/pJLjLa4plEZ4QDYPf.CltpubljvONibeLigVyIot3BYw07G0C','0','0','',NULL,'admin','2022-08-21 20:21:32','admin','2022-08-28 21:31:42',NULL,98765.00,'2022-07-19'),(1562376172041658370,1562373550115119106,'xin','心内科医生','sys_user','','','2','','$2a$10$dcTcMUWPLBdyo/.gUmNRjuR43d0SHxVcAMzKtkbJ5sxmWvk0pVlPy','0','0','',NULL,'admin','2022-08-24 17:48:02','admin','2022-08-24 17:48:02',NULL,NULL,NULL),(1562376262277914625,1562373597787578370,'wai','外壳医生','sys_user','','','2','','$2a$10$3upPu5MVJRJUctPojViO7u.wjQQArLzPpwyMFzTkYEutOTvxl6GGe','0','0','',NULL,'admin','2022-08-24 17:48:23','admin','2022-08-24 17:48:23',NULL,NULL,NULL),(1562376351004221441,1562373635540508674,'yan','眼科医生','sys_user','','','2','','$2a$10$xiG2ptigMwPDmKZjzc933.tE8LzzaYnTtzFh5m/EgTqX2tukkWWl.','0','0','',NULL,'admin','2022-08-24 17:48:44','admin','2022-08-25 16:52:04',NULL,NULL,NULL),(1562376496781451265,1562373743204098049,'xiong','熊内科医生','sys_user','','','2','','$2a$10$9PzUWhmbkmufMvNqpDg3he4Va/SBat9kGFU3ENhOdUl5TG.jEHMO6','0','0','',NULL,'admin','2022-08-24 17:49:19','admin','2022-08-24 17:49:19',NULL,NULL,NULL),(1562376565307990018,1562373793095344129,'huxi','呼吸科医生','sys_user','','','2','','$2a$10$Ax4ssuV.45yRV2jmYNzyZeQsgL9Xwp.alfqC13jK2t9yA/GLAmvyi','0','0','',NULL,'admin','2022-08-24 17:49:35','admin','2022-08-24 17:49:35',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1),(2,1),(3,2),(1561327640005058562,4),(1562376351004221441,1);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,5),(3,3),(4,4),(1561327640005058562,2),(1562376351004221441,2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_demo`
--

DROP TABLE IF EXISTS `test_demo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_demo` (
  `id` bigint NOT NULL COMMENT '主键',
  `dept_id` bigint DEFAULT NULL COMMENT '部门id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `order_num` int DEFAULT '0' COMMENT '排序号',
  `test_key` varchar(255) DEFAULT NULL COMMENT 'key键',
  `value` varchar(255) DEFAULT NULL COMMENT '值',
  `version` int DEFAULT '0' COMMENT '版本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `del_flag` int DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_demo`
--

LOCK TABLES `test_demo` WRITE;
/*!40000 ALTER TABLE `test_demo` DISABLE KEYS */;
INSERT INTO `test_demo` VALUES (1,102,4,1,'测试数据权限','测试',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(2,102,3,2,'子节点1','111',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(3,102,3,3,'子节点2','222',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(4,108,4,4,'测试数据','demo',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(5,108,3,13,'子节点11','1111',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(6,108,3,12,'子节点22','2222',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(7,108,3,11,'子节点33','3333',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(8,108,3,10,'子节点44','4444',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(9,108,3,9,'子节点55','5555',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(10,108,3,8,'子节点66','6666',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(11,108,3,7,'子节点77','7777',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(12,108,3,6,'子节点88','8888',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(13,108,3,5,'子节点99','9999',0,'2022-08-21 01:31:44','admin',NULL,NULL,0);
/*!40000 ALTER TABLE `test_demo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_tree`
--

DROP TABLE IF EXISTS `test_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_tree` (
  `id` bigint NOT NULL COMMENT '主键',
  `parent_id` bigint DEFAULT '0' COMMENT '父id',
  `dept_id` bigint DEFAULT NULL COMMENT '部门id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `tree_name` varchar(255) DEFAULT NULL COMMENT '值',
  `version` int DEFAULT '0' COMMENT '版本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `del_flag` int DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试树表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_tree`
--

LOCK TABLES `test_tree` WRITE;
/*!40000 ALTER TABLE `test_tree` DISABLE KEYS */;
INSERT INTO `test_tree` VALUES (1,0,102,4,'测试数据权限',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(2,1,102,3,'子节点1',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(3,2,102,3,'子节点2',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(4,0,108,4,'测试树1',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(5,4,108,3,'子节点11',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(6,4,108,3,'子节点22',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(7,4,108,3,'子节点33',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(8,5,108,3,'子节点44',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(9,6,108,3,'子节点55',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(10,7,108,3,'子节点66',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(11,7,108,3,'子节点77',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(12,10,108,3,'子节点88',0,'2022-08-21 01:31:44','admin',NULL,NULL,0),(13,10,108,3,'子节点99',0,'2022-08-21 01:31:44','admin',NULL,NULL,0);
/*!40000 ALTER TABLE `test_tree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `undo_log`
--

DROP TABLE IF EXISTS `undo_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `undo_log` (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AT transaction mode undo table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `undo_log`
--

LOCK TABLES `undo_log` WRITE;
/*!40000 ALTER TABLE `undo_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'ry_cloud'
--

--
-- Dumping routines for database 'ry_cloud'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-10-22 12:44:27
