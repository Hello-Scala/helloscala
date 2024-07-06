/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : blog2

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 03/04/2024 14:31:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for b_admin_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_admin_log`  (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户',
                                `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求接口',
                                `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
                                `operation_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作名称',
                                `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
                                `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
                                `spend_time` bigint NULL DEFAULT NULL COMMENT '请求接口耗时',
                                `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `params_json` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
                                `class_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类地址',
                                `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2127 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_article
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_article`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                              `user_id` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
                              `category_id` bigint NULL DEFAULT NULL COMMENT '分类id',
                              `title` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
                              `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章封面地址',
                              `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章简介',
                              `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容 （最多两百字）',
                              `content_md` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容md版',
                              `read_type` int NULL DEFAULT 0 COMMENT '阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读',
                              `is_stick` int NULL DEFAULT 0 COMMENT '是否置顶 0否 1是',
                              `is_publish` int NULL DEFAULT 0 COMMENT '是否发布 0：下架 1：发布',
                              `is_original` int NULL DEFAULT 1 COMMENT '是否原创  0：转载 1:原创',
                              `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转载地址',
                              `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布地址',
                              `quantity` bigint NULL DEFAULT 0 COMMENT '文章阅读量',
                              `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                              `is_carousel` int NULL DEFAULT 0 COMMENT '是否首页轮播',
                              `is_recommend` int NULL DEFAULT 0 COMMENT '是否推荐',
                              `keywords` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关键词',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `user_id`(`user_id`) USING BTREE,
                              FULLTEXT INDEX `title`(`title`) WITH PARSER `ngram`
) ENGINE = InnoDB AUTO_INCREMENT = 268 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_article_tag
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_article_tag`  (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `article_id` int NOT NULL COMMENT '文章id',
                                  `tag_id` int NOT NULL COMMENT '标签id',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `fk_article_tag_1`(`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1276 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_article_tag
-- ----------------------------
INSERT IGNORE INTO `b_article_tag` VALUES (892, 5, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (893, 5, 13);
INSERT IGNORE INTO `b_article_tag` VALUES (768, 9, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (769, 9, 2);
INSERT IGNORE INTO `b_article_tag` VALUES (778, 87, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1049, 89, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1075, 100, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1076, 100, 70);
INSERT IGNORE INTO `b_article_tag` VALUES (824, 101, 2);
INSERT IGNORE INTO `b_article_tag` VALUES (825, 101, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (826, 101, 59);
INSERT IGNORE INTO `b_article_tag` VALUES (821, 102, 17);
INSERT IGNORE INTO `b_article_tag` VALUES (822, 102, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (823, 102, 59);
INSERT IGNORE INTO `b_article_tag` VALUES (960, 110, 31);
INSERT IGNORE INTO `b_article_tag` VALUES (646, 111, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (959, 112, 32);
INSERT IGNORE INTO `b_article_tag` VALUES (771, 114, 2);
INSERT IGNORE INTO `b_article_tag` VALUES (772, 114, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (697, 119, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (698, 119, 51);
INSERT IGNORE INTO `b_article_tag` VALUES (938, 125, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (845, 130, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (846, 130, 31);
INSERT IGNORE INTO `b_article_tag` VALUES (763, 145, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (1022, 146, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1023, 146, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (1024, 146, 59);
INSERT IGNORE INTO `b_article_tag` VALUES (957, 147, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (958, 147, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (815, 148, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (816, 148, 31);
INSERT IGNORE INTO `b_article_tag` VALUES (817, 148, 59);
INSERT IGNORE INTO `b_article_tag` VALUES (1201, 149, 10);
INSERT IGNORE INTO `b_article_tag` VALUES (919, 150, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (917, 152, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (918, 152, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (956, 158, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1036, 162, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1063, 165, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1064, 165, 60);
INSERT IGNORE INTO `b_article_tag` VALUES (899, 166, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (898, 166, 62);
INSERT IGNORE INTO `b_article_tag` VALUES (1250, 167, 31);
INSERT IGNORE INTO `b_article_tag` VALUES (954, 168, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (867, 169, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (868, 169, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1058, 172, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (950, 173, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (951, 173, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (949, 174, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (948, 175, 10);
INSERT IGNORE INTO `b_article_tag` VALUES (903, 176, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (904, 176, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1054, 177, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (1055, 177, 32);
INSERT IGNORE INTO `b_article_tag` VALUES (962, 179, 31);
INSERT IGNORE INTO `b_article_tag` VALUES (968, 180, 65);
INSERT IGNORE INTO `b_article_tag` VALUES (966, 181, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (967, 181, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (981, 182, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (982, 182, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (984, 183, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (991, 184, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (992, 184, 59);
INSERT IGNORE INTO `b_article_tag` VALUES (989, 185, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (990, 185, 59);
INSERT IGNORE INTO `b_article_tag` VALUES (1091, 186, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (1092, 186, 32);
INSERT IGNORE INTO `b_article_tag` VALUES (997, 187, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (998, 187, 59);
INSERT IGNORE INTO `b_article_tag` VALUES (1001, 188, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1002, 188, 10);
INSERT IGNORE INTO `b_article_tag` VALUES (1016, 192, 10);
INSERT IGNORE INTO `b_article_tag` VALUES (1017, 192, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (1018, 192, 32);
INSERT IGNORE INTO `b_article_tag` VALUES (1019, 193, 17);
INSERT IGNORE INTO `b_article_tag` VALUES (1020, 193, 67);
INSERT IGNORE INTO `b_article_tag` VALUES (1021, 193, 69);
INSERT IGNORE INTO `b_article_tag` VALUES (1112, 194, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1113, 194, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1114, 194, 65);
INSERT IGNORE INTO `b_article_tag` VALUES (1065, 198, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1066, 198, 65);
INSERT IGNORE INTO `b_article_tag` VALUES (1050, 199, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1051, 199, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1253, 200, 63);
INSERT IGNORE INTO `b_article_tag` VALUES (1061, 201, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (1062, 201, 32);
INSERT IGNORE INTO `b_article_tag` VALUES (1071, 202, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1074, 203, 70);
INSERT IGNORE INTO `b_article_tag` VALUES (1078, 204, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1077, 204, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1081, 205, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1082, 205, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1084, 206, 18);
INSERT IGNORE INTO `b_article_tag` VALUES (1089, 207, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1090, 207, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1107, 213, 31);
INSERT IGNORE INTO `b_article_tag` VALUES (1108, 213, 69);
INSERT IGNORE INTO `b_article_tag` VALUES (1151, 214, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1125, 216, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1126, 216, 70);
INSERT IGNORE INTO `b_article_tag` VALUES (1130, 217, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1135, 220, 65);
INSERT IGNORE INTO `b_article_tag` VALUES (1180, 248, 31);
INSERT IGNORE INTO `b_article_tag` VALUES (1254, 251, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1255, 251, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1256, 251, 71);
INSERT IGNORE INTO `b_article_tag` VALUES (1212, 253, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1213, 253, 66);
INSERT IGNORE INTO `b_article_tag` VALUES (1224, 255, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1225, 255, 65);
INSERT IGNORE INTO `b_article_tag` VALUES (1251, 256, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1252, 256, 66);
INSERT IGNORE INTO `b_article_tag` VALUES (1234, 257, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1235, 257, 72);
INSERT IGNORE INTO `b_article_tag` VALUES (1237, 258, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1238, 258, 57);
INSERT IGNORE INTO `b_article_tag` VALUES (1236, 258, 65);
INSERT IGNORE INTO `b_article_tag` VALUES (1239, 259, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1240, 259, 72);
INSERT IGNORE INTO `b_article_tag` VALUES (1244, 260, 1);
INSERT IGNORE INTO `b_article_tag` VALUES (1245, 260, 66);
INSERT IGNORE INTO `b_article_tag` VALUES (1249, 261, 31);
INSERT IGNORE INTO `b_article_tag` VALUES (1269, 264, 12);
INSERT IGNORE INTO `b_article_tag` VALUES (1268, 264, 73);

-- ----------------------------
-- Table structure for b_category
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_category`  (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                               `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
                               `click_volume` int NULL DEFAULT 0,
                               `sort` int NOT NULL COMMENT '排序',
                               `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
                               `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `category_name`(`name`) USING BTREE COMMENT '博客分类名称'
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_category
-- ----------------------------
INSERT IGNORE INTO `b_category` VALUES (13, '生活随笔', 0, 4, '2021-12-29 10:22:09', '2021-12-29 10:23:40', 'Ship');
INSERT IGNORE INTO `b_category` VALUES (16, '运维部署', 0, 6, '2021-12-29 10:41:45', '2024-03-27 15:12:30', 'AlarmClock');
INSERT IGNORE INTO `b_category` VALUES (17, '后端开发', 0, 10, '2021-12-29 14:00:49', '2022-01-21 10:23:18', 'el-icon-monitor');
INSERT IGNORE INTO `b_category` VALUES (19, '网络爬虫', 1, 5, '2022-01-07 17:08:57', '2022-01-21 10:23:01', 'el-icon-cpu');
INSERT IGNORE INTO `b_category` VALUES (20, '资源软件', 0, 0, '2022-01-14 15:05:58', '2022-01-20 17:46:18', 'el-icon-suitcase-1');
INSERT IGNORE INTO `b_category` VALUES (28, '数据库', 0, 8, '2022-02-18 16:01:07', '2024-03-27 14:47:32', 'el-icon-coin');
INSERT IGNORE INTO `b_category` VALUES (31, '前端开发', 0, 9, '2023-06-21 15:04:31', '2023-06-21 07:04:30', 'el-icon-mouse');
INSERT IGNORE INTO `b_category` VALUES (32, '博客文档', 0, 2, '2023-09-07 10:03:31', '2024-03-27 14:47:37', 'el-icon-document');
INSERT IGNORE INTO `b_category` VALUES (35, '发撒发撒', 0, 1, '2024-03-27 15:10:24', '2024-03-27 15:10:23', 'ArrowDownBold');

-- ----------------------------
-- Table structure for b_collect
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_collect`  (
                              `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                              `article_id` bigint NOT NULL COMMENT '文章id',
                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '收藏时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `idx_b_collect_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 354 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_comment
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_comment`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                              `user_id` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论人ID',
                              `article_id` bigint NOT NULL COMMENT '文章id',
                              `content` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
                              `reply_user_id` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回复人id',
                              `parent_id` int NULL DEFAULT NULL COMMENT '父id',
                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '评论时间',
                              `browser` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                              `browser_version` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                              `system` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                              `system_version` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                              `ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 878 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_dict
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_dict`  (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
                           `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型',
                           `status` int NOT NULL DEFAULT 1 COMMENT '状态(1:正常 0：停用）',
                           `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                           `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                           `sort` int NULL DEFAULT 0 COMMENT '排序',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_dict
-- ----------------------------
INSERT IGNORE INTO `b_dict` VALUES (1, '用户性别', 'sys_user_sex', 1, '用户性别', '2021-11-19 09:24:23', '2021-11-27 22:03:54', 0);
INSERT IGNORE INTO `b_dict` VALUES (2, '发布状态', 'sys_publish_status', 1, '发布状态列表', '2021-11-19 17:12:51', '2021-11-19 17:12:51', 0);
INSERT IGNORE INTO `b_dict` VALUES (3, '搜索模式', 'sys_search_model', 1, '搜索模式：SQL搜索、全文检索', '2021-11-26 08:57:47', '2021-11-26 08:57:47', 2);
INSERT IGNORE INTO `b_dict` VALUES (4, '系统是否', 'sys_yes_no', 1, '系统是否列表', '2021-11-26 14:03:12', '2021-11-26 14:03:12', 2);
INSERT IGNORE INTO `b_dict` VALUES (5, '系统开关', 'sys_normal_disable', 1, '系统开关列表', '2021-11-26 15:16:43', '2021-11-26 15:16:43', 3);
INSERT IGNORE INTO `b_dict` VALUES (6, '博客登录方式', 'sys_login_method', 1, '博客登录方式 账号密码、QQ、微博', '2021-11-27 13:52:38', '2021-11-27 13:52:38', 0);
INSERT IGNORE INTO `b_dict` VALUES (7, '定时任务分组', 'sys_job_group', 1, '定时任务分组列表', '2021-12-10 08:53:30', '2021-12-10 08:53:30', 2);
INSERT IGNORE INTO `b_dict` VALUES (8, '任务状态', 'sys_job_status', 1, '任务状态列表', '2021-12-10 09:01:10', '2021-12-10 09:01:10', 2);
INSERT IGNORE INTO `b_dict` VALUES (9, '任务执行策略', 'sys_job_misfire', 1, '任务执行策略列表', '2021-12-10 11:11:48', '2021-12-10 11:12:04', 2);
INSERT IGNORE INTO `b_dict` VALUES (12, '测试', '是', 1, '是', '2024-03-29 11:50:13', '2024-03-29 11:50:13', 0);
INSERT IGNORE INTO `b_dict` VALUES (13, '发撒发发', '发', 0, NULL, '2024-03-29 13:40:41', '2024-03-29 13:40:41', 0);

-- ----------------------------
-- Table structure for b_dict_data
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_dict_data`  (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `dict_id` bigint NOT NULL COMMENT '字典类型id',
                                `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典标签',
                                `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典键值',
                                `style` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回显样式',
                                `is_default` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否默认（1是 0否）',
                                `sort` int NULL DEFAULT NULL COMMENT '排序',
                                `status` int NULL DEFAULT NULL COMMENT '状态',
                                `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_dict_data
-- ----------------------------
INSERT IGNORE INTO `b_dict_data` VALUES (1, 2, '发布', '1', 'success', '1', 1, 1, NULL);
INSERT IGNORE INTO `b_dict_data` VALUES (2, 2, '下架', '0', 'danger', '0', 0, 1, NULL);
INSERT IGNORE INTO `b_dict_data` VALUES (3, 4, '是', '1', 'success', '1', 1, 1, '系统是否 是');
INSERT IGNORE INTO `b_dict_data` VALUES (4, 4, '否', '0', 'warning', '0', 0, 1, '系统是否 否');
INSERT IGNORE INTO `b_dict_data` VALUES (5, 5, '开启', '1', 'success', '1', 1, 1, '系统开关 开启');
INSERT IGNORE INTO `b_dict_data` VALUES (6, 5, '关闭', '0', 'warning', '0', 2, 1, '系统开关 关闭');
INSERT IGNORE INTO `b_dict_data` VALUES (7, 3, 'ES搜素', '1', 'success', '0', 1, 1, '搜索模式：开启ElasticSearch全文检索');
INSERT IGNORE INTO `b_dict_data` VALUES (8, 3, 'SQL搜索', '0', 'warning', '1', 2, 1, '搜索模式：SQL搜索');
INSERT IGNORE INTO `b_dict_data` VALUES (9, 6, '账号', '1', 'primary', '0', 1, 1, '账号密码登录');
INSERT IGNORE INTO `b_dict_data` VALUES (10, 6, 'QQ', '2', 'success', '1', 2, 1, 'QQ登录');
INSERT IGNORE INTO `b_dict_data` VALUES (11, 6, '微博', '3', 'danger', '0', 3, 1, '微博登录');
INSERT IGNORE INTO `b_dict_data` VALUES (12, 1, '男', '1', '', '1', 1, 1, '性别 男');
INSERT IGNORE INTO `b_dict_data` VALUES (13, 1, '女', '0', '', '1', 0, 1, '性别 女');
INSERT IGNORE INTO `b_dict_data` VALUES (14, 7, '默认', 'DEFAULT', 'primary', '1', 1, 1, '默认分组');
INSERT IGNORE INTO `b_dict_data` VALUES (15, 7, '系统', 'SYSTEM', 'warning', '0', 2, 1, '系统分组');
INSERT IGNORE INTO `b_dict_data` VALUES (16, 8, '正常', '0', 'primary', '0', 1, 1, '正常状态');
INSERT IGNORE INTO `b_dict_data` VALUES (17, 8, '暂停', '1', 'danger', '1', 2, 1, '暂停状态');
INSERT IGNORE INTO `b_dict_data` VALUES (18, 9, '默认策略', '0', '', '1', 1, 1, '默认策略');
INSERT IGNORE INTO `b_dict_data` VALUES (19, 9, '立即执行', '1', '', '0', 2, 1, '立即执行');
INSERT IGNORE INTO `b_dict_data` VALUES (20, 9, '执行一次', '2', '', '0', 3, 1, '执行一次');
INSERT IGNORE INTO `b_dict_data` VALUES (21, 9, '放弃执行', '3', '', '0', 4, 1, '放弃执行');
INSERT IGNORE INTO `b_dict_data` VALUES (22, 6, '码云', '4', 'danger', '0', 4, 1, 'gitee登录');
INSERT IGNORE INTO `b_dict_data` VALUES (23, 6, '微信', '5', 'success', '1', 5, 1, '微信登录');
INSERT IGNORE INTO `b_dict_data` VALUES (24, 2, '待审批', '2', 'info', '1', 0, 1, NULL);
INSERT IGNORE INTO `b_dict_data` VALUES (25, 6, 'github', '6', 'info', '1', 0, 1, 'github登录');
INSERT IGNORE INTO `b_dict_data` VALUES (26, 2, '草稿', '3', 'warning', '1', 3, 1, NULL);
INSERT IGNORE INTO `b_dict_data` VALUES (28, 12, 'FASFF1', 'ASFF1', 'success', '1', 2, 1, 'FWQF');

-- ----------------------------
-- Table structure for b_exception_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_exception_log`  (
                                    `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                    `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP',
                                    `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
                                    `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
                                    `operation` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
                                    `params` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数',
                                    `exception_json` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常对象json格式',
                                    `exception_message` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常简单信息,等同于e.getMessage',
                                    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_feed_back
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_feed_back`  (
                                `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                `user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                                `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
                                `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细内容',
                                `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
                                `create_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
                                `type` int NOT NULL COMMENT '反馈类型 1:需求 2：缺陷',
                                `status` int NULL DEFAULT 0 COMMENT '状态 0：未解决 1：解决',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_followed
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_followed`  (
                               `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                               `followed_user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关注的用户id',
                               `create_time` datetime(0) NULL DEFAULT NULL COMMENT '关注时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `user_id`(`user_id`) USING BTREE,
                               INDEX `followed_user_id`(`followed_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_friend_link
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_friend_link`  (
                                  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站名称',
                                  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站地址',
                                  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站头像地址',
                                  `info` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站描述',
                                  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                  `sort` int NULL DEFAULT 0 COMMENT '排序',
                                  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下架原因',
                                  `status` int NOT NULL DEFAULT 0 COMMENT 'ENUM-状态:\"0,下架;1,申请;2:上架\"',
                                  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `url`(`url`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '友情链接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_friend_link
-- ----------------------------
INSERT IGNORE INTO `b_friend_link` VALUES (4, 'Hello Scala', 'https://www.helloscala.com', '', '一个专注技术分享的平台', '2484913345@qq.com', 99, '1', 0, '2022-12-13 09:16:15', '2022-12-13 09:16:15');

-- ----------------------------
-- Table structure for b_im_message
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_im_message`  (
                                 `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                 `to_user_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送用户id',
                                 `from_user_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收用户id',
                                 `to_user_avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送用户头像',
                                 `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发送内容',
                                 `create_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
                                 `ip_source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
                                 `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送用户ip',
                                 `is_withdraw` int NULL DEFAULT 0 COMMENT '消息是否撤回 0：未撤回  1：撤回',
                                 `is_read` int NULL DEFAULT 0 COMMENT '是否已读',
                                 `type` int NULL DEFAULT NULL COMMENT '消息类型 1普通消息 2图片',
                                 `code` int NULL DEFAULT NULL,
                                 `article_id` int NULL DEFAULT NULL COMMENT '文章id',
                                 `notice_type` int NULL DEFAULT NULL COMMENT '通知类型 0系统通知 1：评论 2：关注 3点赞 4收藏 5私信',
                                 `comment_mark` int NULL DEFAULT NULL COMMENT '评论标记 1回复评论 2发表评论',
                                 `at_user_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '@用户id',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_im_room
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_im_room`  (
                              `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                              `type` int NOT NULL COMMENT '房间类型 0：群聊 1私聊',
                              `from_user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属用户id',
                              `to_user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收用户id',
                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户消息房间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_job
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_job`  (
                          `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
                          `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
                          `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
                          `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
                          `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
                          `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
                          `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
                          `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
                          `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
                          `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                          `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
                          `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                          `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
                          PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_job
-- ----------------------------
INSERT IGNORE INTO `b_job` VALUES (2, '系统默认（无参）', 'DEFAULT', 'blogQuartz.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'Hello Scala', '2021-12-09 09:09:21', '', '2023-06-30 14:50:29', '');
INSERT IGNORE INTO `b_job` VALUES (3, '系统默认（有参）', 'DEFAULT', 'blogQuartz.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'Hello Scala', '2021-12-09 09:09:21', '', NULL, '');
INSERT IGNORE INTO `b_job` VALUES (6, '定时修改标签的点击量', 'DEFAULT', 'blogQuartz.updateTagsClickVolume', '0 0 3 * * ?', '0', '1', '1', 'Hello Scala', '2021-12-17 15:37:20', 'Hello Scala', '2022-12-13 09:23:50', '1');
INSERT IGNORE INTO `b_job` VALUES (8, '定时修改文章阅读量', 'SYSTEM', 'blogQuartz.updateReadQuantity', '0 0 4 * * ?', '0', '1', '0', 'Hello Scala', '2023-06-30 10:55:03', '', '2024-03-28 10:49:41', '1');
INSERT IGNORE INTO `b_job` VALUES (9, '定时删除当天校验通过的IP', 'SYSTEM', 'blogQuartz.removeCodePassInIp', '0 30 23 * * ?', '0', '0', '0', 'Hello Scala', '2023-06-30 10:56:31', 'Hello Scala', '2023-06-30 10:56:35', '1');

-- ----------------------------
-- Table structure for b_job_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_job_log`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
                              `job_id` bigint NOT NULL COMMENT '任务ID',
                              `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
                              `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
                              `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
                              `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
                              `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
                              `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                              `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
                              `stop_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1956 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_menu
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_menu`  (
                           `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级资源ID',
                           `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由路径',
                           `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
                           `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
                           `sort` int NULL DEFAULT 0 COMMENT '排序',
                           `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源图标',
                           `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型 menu、button',
                           `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                           `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重定向地址',
                           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
                           `hidden` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否隐藏',
                           `perm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 315 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统管理-权限资源表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_menu
-- ----------------------------
INSERT IGNORE INTO `b_menu` VALUES (1, '0', '/system', 'Layout', '系统管理', 5, 'el-icon-setting', 'CATALOG', '2019-03-28 18:51:08', '2021-12-17 15:26:06', '/system/menu', 'system', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (2, '1', '/role', '/system/role', '角色管理', 2, 'el-icon-Avatar', 'MENU', '2019-03-30 14:00:03', '2021-11-16 15:40:42', '', 'role', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (3, '2', NULL, NULL, '列表', 0, NULL, 'BUTTON', NULL, NULL, '', NULL, '0', 'system:role:list');
INSERT IGNORE INTO `b_menu` VALUES (5, '2', NULL, NULL, '修改', 0, NULL, 'BUTTON', '2021-09-24 15:57:33', '2021-11-11 18:09:44', '', NULL, '0', 'system:role:update');
INSERT IGNORE INTO `b_menu` VALUES (6, '2', NULL, NULL, '删除', 0, NULL, 'BUTTON', '2021-09-27 11:33:32', '2021-11-11 18:09:36', '', NULL, '0', 'system:role:delete');
INSERT IGNORE INTO `b_menu` VALUES (7, '2', NULL, NULL, '添加', 1, NULL, 'BUTTON', '2021-11-13 21:14:07', '2024-04-03 11:08:31', NULL, NULL, '0', 'system:role:add');
INSERT IGNORE INTO `b_menu` VALUES (8, '1', '/menu', '/system/menu', '菜单管理', 5, 'el-icon-menu', 'MENU', NULL, '2021-11-18 11:26:00', '', 'menu', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (9, '8', NULL, NULL, '列表', 0, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:menu:getMenuTree');
INSERT IGNORE INTO `b_menu` VALUES (10, '8', NULL, NULL, '添加', 0, NULL, 'BUTTON', NULL, '2024-04-03 11:09:19', NULL, NULL, '0', 'system:menu:add');
INSERT IGNORE INTO `b_menu` VALUES (11, '8', NULL, NULL, '修改', 2, NULL, 'BUTTON', '2021-11-11 16:56:34', '2021-11-11 18:10:09', NULL, '/system/menu/update', '0', 'system:menu:update');
INSERT IGNORE INTO `b_menu` VALUES (12, '8', NULL, NULL, '获取所有的url', 6, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:menu:getMenuList');
INSERT IGNORE INTO `b_menu` VALUES (13, '8', NULL, NULL, '删除', 0, NULL, 'BUTTON', '2021-09-27 11:45:33', '2021-11-11 18:10:03', NULL, NULL, '0', 'system:menu:delete');
INSERT IGNORE INTO `b_menu` VALUES (14, '1', '/user', '/system/user', '用户管理', 1, 'el-icon-user', 'MENU', NULL, '2021-11-16 12:01:51', NULL, 'user', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (15, '14', NULL, NULL, '列表', 0, NULL, 'BUTTON', '2021-09-27 15:33:19', '2021-11-11 18:10:22', NULL, NULL, '0', 'system:user:list');
INSERT IGNORE INTO `b_menu` VALUES (16, '14', NULL, NULL, '删除', 0, NULL, 'BUTTON', '2021-09-27 16:36:42', '2021-11-11 18:10:27', NULL, NULL, '0', 'system:user:delete');
INSERT IGNORE INTO `b_menu` VALUES (17, '14', NULL, NULL, '添加', 0, NULL, 'BUTTON', '2021-09-27 16:36:54', '2021-11-11 18:10:30', NULL, NULL, '0', 'system:user:add');
INSERT IGNORE INTO `b_menu` VALUES (18, '14', NULL, NULL, '修改', 0, NULL, 'BUTTON', '2021-09-27 16:59:38', '2021-11-11 18:10:34', NULL, NULL, '0', 'system:user:update');
INSERT IGNORE INTO `b_menu` VALUES (19, '14', NULL, NULL, '详情', 0, NULL, 'BUTTON', '2021-09-27 16:59:50', '2021-11-11 18:10:37', NULL, NULL, '0', 'system:user:info');
INSERT IGNORE INTO `b_menu` VALUES (20, '14', NULL, NULL, '获取用户权限', 0, NULL, 'BUTTON', NULL, '2021-11-11 18:10:40', NULL, NULL, '0', 'system:user:getUserMenu');
INSERT IGNORE INTO `b_menu` VALUES (21, '14', NULL, NULL, '修改密码', 0, NULL, 'BUTTON', '2021-11-09 17:23:58', '2021-11-11 18:10:51', NULL, '/system/user/update_password', '0', 'system:user:updatePassword');
INSERT IGNORE INTO `b_menu` VALUES (23, '14', NULL, NULL, '退出登录', 0, NULL, 'BUTTON', '2021-09-26 10:21:27', '2021-11-11 18:10:46', NULL, NULL, '0', 'system:user:logout');
INSERT IGNORE INTO `b_menu` VALUES (25, '24', NULL, NULL, '列表', 1, '1', 'BUTTON', '2021-11-12 10:55:11', NULL, NULL, '', '0', 'system:menu:getMenuApi');
INSERT IGNORE INTO `b_menu` VALUES (26, '0', '/articles', 'Layout', '文章管理', 1, 'el-icon-document-copy', 'CATALOG', NULL, '2021-11-16 15:45:14', '/articles/index', '', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (27, '26', 'index', '/articles/index', '文章管理', 1, 'el-icon-Document', 'MENU', NULL, '2021-11-16 15:41:57', '/articles/index', 'Articles', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (28, '27', NULL, NULL, '列表', 0, NULL, 'BUTTON', NULL, '2021-11-11 18:11:17', NULL, NULL, '0', 'system:article:list');
INSERT IGNORE INTO `b_menu` VALUES (30, '27', NULL, NULL, '修改', 0, NULL, 'BUTTON', NULL, '2021-11-11 18:11:25', NULL, NULL, '0', 'system:article:update');
INSERT IGNORE INTO `b_menu` VALUES (31, '27', NULL, NULL, '添加', 0, NULL, 'BUTTON', NULL, '2021-11-11 18:11:32', NULL, '2', '0', 'system:article:add');
INSERT IGNORE INTO `b_menu` VALUES (32, '27', NULL, NULL, '详情', 0, NULL, 'BUTTON', NULL, '2021-11-11 18:11:35', NULL, NULL, '0', 'system:article:info');
INSERT IGNORE INTO `b_menu` VALUES (33, '27', NULL, NULL, 'SEO', 0, NULL, 'BUTTON', '2021-10-15 10:38:19', '2021-11-11 18:11:41', NULL, NULL, '0', 'system:article:seo');
INSERT IGNORE INTO `b_menu` VALUES (35, '26', 'tags', '/articles/tags', '标签管理', 2, 'el-icon-collection-tag', 'MENU', NULL, '2021-11-18 11:25:18', NULL, 'Tags', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (36, '35', NULL, NULL, '列表', 0, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:tags:list');
INSERT IGNORE INTO `b_menu` VALUES (37, '35', NULL, NULL, '新增', 0, NULL, 'BUTTON', NULL, '2021-11-11 18:11:54', NULL, NULL, '0', 'system:tags:add');
INSERT IGNORE INTO `b_menu` VALUES (38, '35', NULL, NULL, '详情', 0, NULL, 'BUTTON', NULL, '2021-11-11 18:11:58', NULL, NULL, '0', 'system:tags:info');
INSERT IGNORE INTO `b_menu` VALUES (39, '35', NULL, NULL, '修改', 0, NULL, 'BUTTON', NULL, '2021-11-11 18:12:08', NULL, NULL, '0', 'system:tags:update');
INSERT IGNORE INTO `b_menu` VALUES (40, '35', NULL, NULL, '删除', 0, NULL, 'BUTTON', '2021-11-10 17:34:38', '2021-11-11 18:12:01', NULL, '/sys/tags/remove', '0', 'system:tags:delete');
INSERT IGNORE INTO `b_menu` VALUES (41, '0', '/site', 'Layout', '网站管理', 2, 'el-icon-guide', 'CATALOG', NULL, '2021-11-16 15:48:41', '/friendLink/index', '', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (47, '245', '/messages', '/message/message', '留言管理', 2, 'el-icon-message', 'MENU', NULL, '2021-11-16 15:43:46', '/message/index', '/message', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (48, '47', NULL, NULL, '列表', 0, NULL, 'BUTTON', '2021-09-26 11:50:33', '2021-11-11 18:12:56', NULL, NULL, '0', 'system:message:list');
INSERT IGNORE INTO `b_menu` VALUES (51, '41', 'friendLink', '/website/friendLink', '友情链接', 3, 'el-icon-link', 'MENU', NULL, '2021-11-16 15:43:55', NULL, 'friendLink', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (52, '51', NULL, NULL, '列表', 0, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:friendLink:list');
INSERT IGNORE INTO `b_menu` VALUES (53, '51', NULL, NULL, '添加', 1, NULL, 'BUTTON', '2021-11-12 16:52:26', '2024-04-03 11:02:21', NULL, NULL, '0', 'system:friendLink:add');
INSERT IGNORE INTO `b_menu` VALUES (54, '51', NULL, NULL, '修改', 1, NULL, 'BUTTON', '2021-11-12 16:52:08', NULL, NULL, NULL, '0', 'system:friendLink:update');
INSERT IGNORE INTO `b_menu` VALUES (55, '51', NULL, NULL, '删除', 1, NULL, 'BUTTON', '2021-11-14 12:18:00', NULL, NULL, NULL, '0', 'system:friendLink:delete');
INSERT IGNORE INTO `b_menu` VALUES (56, '0', '/logs', 'Layout', '日志管理', 4, 'el-icon-document', 'CATALOG', NULL, '2021-12-31 14:46:11', NULL, NULL, '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (57, '56', 'userLog', '/logs/userLog', '用户日志', 1, 'el-icon-coordinate', 'MENU', NULL, '2021-11-17 10:02:31', NULL, 'userLogs', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (58, '57', NULL, NULL, '列表', 0, '', 'BUTTON', NULL, '2021-11-11 18:13:47', NULL, NULL, '0', 'system:userLog:list');
INSERT IGNORE INTO `b_menu` VALUES (59, '56', 'adminLog', '/logs/adminLog', '操作日志', 2, 'el-icon-magic-stick', 'MENU', '2021-11-10 17:49:02', '2021-11-17 10:02:41', NULL, 'adminLog', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (60, '59', NULL, NULL, '列表', 0, NULL, 'BUTTON', '2021-11-10 17:49:27', '2021-11-11 18:13:54', NULL, '/zwblog/adminLog', '0', 'system:adminLog:list');
INSERT IGNORE INTO `b_menu` VALUES (61, '56', 'exceptionLog', '/logs/exceptionLog', '异常日志', 3, 'el-icon-cpu', 'MENU', '2021-11-11 10:57:42', '2021-11-17 10:02:47', NULL, 'exceptionLog', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (62, '61', NULL, NULL, '列表', 0, NULL, 'BUTTON', '2021-11-11 11:05:47', '2021-11-11 18:13:59', NULL, '/sys/exceptionLog/query_log', '0', 'system:exceptionLog:list');
INSERT IGNORE INTO `b_menu` VALUES (63, '0', '/other', 'Layout', '其他', 1, 'el-icon-more-outline', 'CATALOG', '2021-11-12 09:29:15', NULL, NULL, 'other', '0', NULL);
INSERT IGNORE INTO `b_menu` VALUES (64, '63', '/image', '/image', '图片管理', 1, 'el-icon-picture-outline', 'MENU', '2021-11-12 09:31:24', '2021-11-16 15:47:05', NULL, '/image', '0', NULL);
INSERT IGNORE INTO `b_menu` VALUES (65, '64', NULL, NULL, '删除', 0, NULL, 'BUTTON', '2021-09-27 11:53:16', '2021-11-11 18:10:55', NULL, NULL, '0', 'system:file:delBatchFile');
INSERT IGNORE INTO `b_menu` VALUES (66, '63', '/home', '', '首页', 0, 'el-icon-s-home', 'MENU', '2021-10-16 15:46:06', '2021-11-12 09:30:39', NULL, 'home', '0', NULL);
INSERT IGNORE INTO `b_menu` VALUES (67, '66', NULL, NULL, '顶部统计信息', 0, NULL, 'BUTTON', '2021-10-16 15:46:56', '2021-11-27 11:51:56', NULL, NULL, '0', 'system:home:lineCount');
INSERT IGNORE INTO `b_menu` VALUES (164, '0', '/monitor', 'Layout', '监控中心', 6, 'el-icon-monitor', 'CATALOG', '2021-11-16 11:48:04', NULL, NULL, 'listener', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (165, '164', '/server', '/monitor/server', '服务监控', 1, 'monitor', 'MENU', '2021-11-16 11:49:16', '2021-12-10 08:47:17', NULL, 'server', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (166, '165', NULL, NULL, '查看', 1, NULL, 'BUTTON', '2021-11-16 11:50:03', NULL, NULL, NULL, '0', 'system:homesystemInfo');
INSERT IGNORE INTO `b_menu` VALUES (169, '1', '/dict', '/system/dict', '字典管理', 1, 'el-icon-DocumentChecked', 'MENU', '2021-11-25 17:37:43', '2021-12-10 15:28:52', NULL, 'dict', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (170, '169', NULL, NULL, '列表', 1, NULL, 'BUTTON', '2021-11-25 17:38:04', NULL, NULL, NULL, '0', 'system:dict:list');
INSERT IGNORE INTO `b_menu` VALUES (171, '41', '/dictData', '/site/dict/data', '字典数据', 2, 'el-icon-sunset', 'MENU', '2021-11-25 17:53:25', '2021-12-11 20:11:50', NULL, 'dictData', '0', NULL);
INSERT IGNORE INTO `b_menu` VALUES (172, '171', NULL, NULL, '列表', 1, NULL, 'BUTTON', '2021-11-25 17:53:52', NULL, NULL, NULL, '0', 'system:dictData:list');
INSERT IGNORE INTO `b_menu` VALUES (173, '169', NULL, NULL, '添加', 1, NULL, 'BUTTON', '2021-11-26 08:57:12', NULL, NULL, NULL, '0', 'system:dict:add');
INSERT IGNORE INTO `b_menu` VALUES (174, '169', NULL, NULL, '修改', 2, NULL, 'BUTTON', '2021-11-26 08:57:29', NULL, NULL, NULL, '0', 'system:dict:update');
INSERT IGNORE INTO `b_menu` VALUES (175, '171', NULL, NULL, '类型集合字典数据', 2, NULL, 'BUTTON', '2021-11-26 09:55:27', NULL, NULL, NULL, '0', 'system:dictData:getDataByDictType');
INSERT IGNORE INTO `b_menu` VALUES (176, '169', NULL, NULL, '删除', 3, NULL, 'BUTTON', '2021-11-26 11:22:21', NULL, NULL, NULL, '0', 'system:dict:delete');
INSERT IGNORE INTO `b_menu` VALUES (177, '169', NULL, NULL, '批量删除', 0, '4', 'BUTTON', '2021-11-26 11:22:37', NULL, NULL, NULL, '0', 'system:dict:deleteBatch');
INSERT IGNORE INTO `b_menu` VALUES (178, '171', NULL, NULL, '添加', 1, NULL, 'BUTTON', '2021-11-26 14:06:04', NULL, NULL, NULL, '0', 'system:dictData:add');
INSERT IGNORE INTO `b_menu` VALUES (179, '171', NULL, NULL, '修改', 2, NULL, 'BUTTON', '2021-11-26 14:06:18', NULL, NULL, NULL, '0', 'system:dictData:update');
INSERT IGNORE INTO `b_menu` VALUES (180, '171', NULL, NULL, '删除', 3, NULL, 'BUTTON', '2021-11-26 14:06:31', NULL, NULL, NULL, '0', 'system:dictData:delete');
INSERT IGNORE INTO `b_menu` VALUES (181, '171', NULL, NULL, '批量删除', 4, NULL, 'BUTTON', '2021-11-26 14:06:46', NULL, NULL, NULL, '0', 'system:dictData:deleteBatch');
INSERT IGNORE INTO `b_menu` VALUES (182, '1', 'systemconfig', '/system/systemConfig', '系统配置', 2, 'el-icon-setting', 'MENU', '2021-11-26 15:06:11', '2021-11-27 12:53:08', NULL, 'systemconfig', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (183, '182', NULL, NULL, '查询', 1, NULL, 'BUTTON', '2021-11-26 15:06:39', '2021-11-26 15:45:36', NULL, NULL, '0', 'system:config:getConfig');
INSERT IGNORE INTO `b_menu` VALUES (184, '182', NULL, NULL, '修改', 2, NULL, 'BUTTON', '2021-11-26 15:55:47', NULL, NULL, NULL, '0', 'system:config:update');
INSERT IGNORE INTO `b_menu` VALUES (186, '41', 'webConfig', '/website/webConfig', '网站配置', 3, 'el-icon-setting', 'MENU', '2021-11-27 13:48:02', NULL, NULL, 'webConfig', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (187, '186', NULL, NULL, '查询', 1, NULL, 'BUTTON', '2021-11-27 13:48:33', NULL, NULL, NULL, '0', 'system:webConfig:list');
INSERT IGNORE INTO `b_menu` VALUES (188, '186', NULL, NULL, '修改', 1, NULL, 'BUTTON', '2021-11-27 14:12:42', NULL, NULL, NULL, '0', 'system:webConfig:update');
INSERT IGNORE INTO `b_menu` VALUES (189, '35', NULL, NULL, '批量删除', 1, NULL, 'BUTTON', '2021-11-28 12:44:48', NULL, NULL, NULL, '0', 'system:tags:deleteBatch');
INSERT IGNORE INTO `b_menu` VALUES (191, '164', '/job', '/monitor/job', '定时任务', 2, 'el-icon-coordinate', 'MENU', '2021-12-10 08:46:08', NULL, NULL, 'quartz', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (192, '191', NULL, NULL, '列表', 1, NULL, 'BUTTON', '2021-12-10 08:47:52', NULL, NULL, NULL, '0', 'system:job:list');
INSERT IGNORE INTO `b_menu` VALUES (193, '191', NULL, NULL, '添加', 2, NULL, 'BUTTON', '2021-12-10 08:48:13', NULL, NULL, NULL, '0', 'system:job:add');
INSERT IGNORE INTO `b_menu` VALUES (194, '191', NULL, NULL, '修改', 3, NULL, 'BUTTON', '2021-12-10 08:48:27', NULL, NULL, NULL, '0', 'system:job:update');
INSERT IGNORE INTO `b_menu` VALUES (195, '191', NULL, NULL, '删除', 4, NULL, 'BUTTON', '2021-12-10 08:48:45', NULL, NULL, NULL, '0', 'system:job:delete');
INSERT IGNORE INTO `b_menu` VALUES (196, '191', NULL, NULL, '立即执行', 5, NULL, 'BUTTON', '2021-12-10 08:52:15', NULL, NULL, NULL, '0', 'system:job:run');
INSERT IGNORE INTO `b_menu` VALUES (197, '191', NULL, NULL, '修改状态', 6, NULL, 'BUTTON', '2021-12-10 08:52:42', NULL, NULL, NULL, '0', 'system:job:change');
INSERT IGNORE INTO `b_menu` VALUES (198, '191', NULL, NULL, '详情', 7, NULL, 'BUTTON', '2021-12-10 10:09:27', NULL, NULL, NULL, '0', 'system:job:info');
INSERT IGNORE INTO `b_menu` VALUES (199, '164', '/jobLog', '/monitor/jobLog', '任务日志', 3, 'el-icon-help', 'MENU', '2021-12-10 11:45:00', NULL, NULL, 'log', '0', NULL);
INSERT IGNORE INTO `b_menu` VALUES (200, '199', NULL, NULL, '列表', 1, NULL, 'BUTTON', '2021-12-10 11:45:23', NULL, NULL, NULL, '0', 'system:jobLog:list');
INSERT IGNORE INTO `b_menu` VALUES (201, '199', NULL, NULL, '批量删除', 2, NULL, 'BUTTON', '2021-12-10 13:50:17', '2024-04-03 11:13:50', NULL, NULL, '0', 'system:jobLog:delete');
INSERT IGNORE INTO `b_menu` VALUES (202, '199', NULL, NULL, '清空', 3, NULL, 'BUTTON', '2021-12-10 13:50:28', NULL, NULL, NULL, '0', 'system:jobLog:clean');
INSERT IGNORE INTO `b_menu` VALUES (203, '191', NULL, NULL, '批量删除', 8, NULL, 'BUTTON', '2021-12-10 14:23:21', '2024-04-03 11:13:31', NULL, NULL, '0', 'system:job:delete');
INSERT IGNORE INTO `b_menu` VALUES (215, '27', NULL, NULL, '爬虫', 6, NULL, 'BUTTON', '2021-12-24 09:00:15', NULL, NULL, NULL, '0', 'system:article:reptile');
INSERT IGNORE INTO `b_menu` VALUES (216, '35', NULL, NULL, '标签置顶', 5, NULL, 'BUTTON', '2021-12-24 09:00:36', NULL, NULL, NULL, '0', 'system:tags:top');
INSERT IGNORE INTO `b_menu` VALUES (223, '26', 'category', '/articles/category', '分类管理', 3, 'el-icon-files', 'MENU', '2021-12-29 10:05:12', '2021-12-29 10:08:05', NULL, '/category', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (224, '223', NULL, NULL, '列表', 1, NULL, 'BUTTON', '2021-12-29 10:05:38', NULL, NULL, NULL, '0', 'system:category:list');
INSERT IGNORE INTO `b_menu` VALUES (225, '223', NULL, NULL, '详情', 2, NULL, 'BUTTON', '2021-12-29 10:05:58', NULL, NULL, NULL, '0', 'system:category:info');
INSERT IGNORE INTO `b_menu` VALUES (226, '223', NULL, NULL, '新增', 3, NULL, 'BUTTON', '2021-12-29 10:06:18', NULL, NULL, NULL, '0', 'system:category:add');
INSERT IGNORE INTO `b_menu` VALUES (227, '223', NULL, NULL, '修改', 4, NULL, 'BUTTON', '2021-12-29 10:06:33', NULL, NULL, NULL, '0', 'system:category:update');
INSERT IGNORE INTO `b_menu` VALUES (228, '223', NULL, NULL, '批量删除', 5, NULL, 'BUTTON', '2021-12-29 10:06:47', NULL, NULL, NULL, '0', 'system:category:deleteBatch');
INSERT IGNORE INTO `b_menu` VALUES (229, '223', NULL, NULL, '置顶', 6, NULL, 'BUTTON', '2021-12-29 10:07:06', NULL, NULL, NULL, '0', 'system:category:top');
INSERT IGNORE INTO `b_menu` VALUES (230, '223', NULL, NULL, '删除', 7, NULL, 'BUTTON', '2021-12-29 10:27:55', NULL, NULL, NULL, '0', 'system:category:delete');
INSERT IGNORE INTO `b_menu` VALUES (245, '0', '/news', 'Layout', '消息管理', 3, 'el-icon-bell', 'CATALOG', '2021-12-31 14:21:26', NULL, NULL, '/new', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (247, '47', NULL, NULL, '批量删除', 4, NULL, 'BUTTON', '2021-12-31 14:35:47', '2024-04-03 11:05:12', NULL, NULL, '0', 'system:message:delete');
INSERT IGNORE INTO `b_menu` VALUES (250, '57', NULL, NULL, '删除', 2, NULL, 'BUTTON', '2022-01-06 15:41:01', NULL, NULL, NULL, '0', 'system:userLog:delete');
INSERT IGNORE INTO `b_menu` VALUES (251, '59', NULL, NULL, '删除', 2, NULL, 'BUTTON', '2022-01-06 15:41:27', NULL, NULL, NULL, '0', 'system:adminLog:delete');
INSERT IGNORE INTO `b_menu` VALUES (252, '61', NULL, NULL, '删除', 2, NULL, 'BUTTON', '2022-01-06 15:41:49', NULL, NULL, NULL, '0', 'system:exceptionLog:delete');
INSERT IGNORE INTO `b_menu` VALUES (253, '27', NULL, NULL, '批量删除', 6, NULL, 'BUTTON', '2022-01-06 18:00:24', '2024-04-03 09:35:49', NULL, NULL, '0', 'system:article:delete');
INSERT IGNORE INTO `b_menu` VALUES (254, '51', NULL, NULL, '置顶', 4, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:friendLink:top');
INSERT IGNORE INTO `b_menu` VALUES (256, '245', '/feedbacks', '/message/feedback', '反馈管理', 2, 'el-icon-Soccer', 'MENU', NULL, '2024-03-29 13:48:47', NULL, '/feedback', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (257, '256', NULL, NULL, '列表', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:feedback:list');
INSERT IGNORE INTO `b_menu` VALUES (258, '256', NULL, NULL, '批量删除', 2, NULL, 'BUTTON', NULL, '2024-04-03 11:04:19', NULL, NULL, '0', 'system:feedback:delete');
INSERT IGNORE INTO `b_menu` VALUES (260, '64', NULL, NULL, '上传图片', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:file:upload');
INSERT IGNORE INTO `b_menu` VALUES (261, '66', NULL, NULL, '首页各种统计信息', 3, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:home:init');
INSERT IGNORE INTO `b_menu` VALUES (262, '27', NULL, NULL, '发布或下架文章', 4, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:article:pubOrShelf');
INSERT IGNORE INTO `b_menu` VALUES (263, '164', 'onlineUser', '/monitor/onlineUser', '在线用户', 3, 'el-icon-user', 'MENU', NULL, NULL, NULL, 'online', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (264, '263', NULL, NULL, '踢人下线', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:user:kick');
INSERT IGNORE INTO `b_menu` VALUES (265, '164', 'druids', '/monitor/druid', 'druid监控', 4, 'el-icon-help', 'MENU', NULL, NULL, NULL, 'druid', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (266, '245', 'comment', '/message/comment', '评论管理', 1, 'el-icon-chat-dot-round', 'MENU', NULL, NULL, NULL, 'comments', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (267, '266', NULL, NULL, '评论列表', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:comment:list');
INSERT IGNORE INTO `b_menu` VALUES (268, '266', NULL, NULL, '批量删除评论', 2, NULL, 'BUTTON', NULL, '2024-04-03 11:04:11', NULL, NULL, '0', 'system:comment:delete');
INSERT IGNORE INTO `b_menu` VALUES (269, '164', 'cache', '/monitor/cache', '缓存监控', 5, 'el-icon-hot-water', 'MENU', NULL, '2024-04-03 08:55:11', NULL, 'caches', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (270, '269', NULL, NULL, '获取缓存监控', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:home:cache');
INSERT IGNORE INTO `b_menu` VALUES (271, '27', NULL, NULL, '置顶文章', 5, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:article:top');
INSERT IGNORE INTO `b_menu` VALUES (275, '41', 'siteClass', '/site/navigation/index', '导航管理', 4, 'el-icon-s-operation', 'MENU', NULL, NULL, NULL, 'siteClass', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (276, '275', NULL, NULL, '网站分类列表', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:siteClass:list');
INSERT IGNORE INTO `b_menu` VALUES (277, '275', NULL, NULL, '添加网站分类', 2, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:siteClass:insert');
INSERT IGNORE INTO `b_menu` VALUES (278, '275', NULL, NULL, '修改网站分类', 3, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:siteClass:update');
INSERT IGNORE INTO `b_menu` VALUES (279, '275', NULL, NULL, '删除网站导航', 4, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:siteClass:deleteBatch');
INSERT IGNORE INTO `b_menu` VALUES (280, '41', 'navigation', '/site/navigation/navigation', '网址管理', 1, 'el-icon-more', 'MENU', NULL, NULL, NULL, 'navigation', '0', NULL);
INSERT IGNORE INTO `b_menu` VALUES (281, '280', NULL, NULL, '列表', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:navigation:list');
INSERT IGNORE INTO `b_menu` VALUES (282, '280', NULL, NULL, '添加', 2, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:navigation:insert');
INSERT IGNORE INTO `b_menu` VALUES (283, '280', NULL, NULL, '修改', 3, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:navigation:update');
INSERT IGNORE INTO `b_menu` VALUES (284, '280', NULL, NULL, '删除', 4, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:navigation:deleteBatch');
INSERT IGNORE INTO `b_menu` VALUES (286, '26', 'say', '/articles/say', '说说管理', 5, 'el-icon-Bicycle', 'MENU', NULL, '2024-03-29 14:45:40', NULL, 'say', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (287, '286', NULL, NULL, '删除', 1, NULL, 'BUTTON', NULL, '2023-09-22 15:57:58', NULL, NULL, '0', 'system:say:delete');
INSERT IGNORE INTO `b_menu` VALUES (288, '286', NULL, NULL, '添加', 2, NULL, 'BUTTON', NULL, '2024-04-03 11:00:55', NULL, NULL, '0', 'system:say:add');
INSERT IGNORE INTO `b_menu` VALUES (289, '256', NULL, NULL, '修改', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:feedback:update');
INSERT IGNORE INTO `b_menu` VALUES (290, '286', NULL, NULL, '修改', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:say:update');
INSERT IGNORE INTO `b_menu` VALUES (291, '286', NULL, NULL, '列表', 2, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:say:list');
INSERT IGNORE INTO `b_menu` VALUES (298, '297', NULL, NULL, '修改', 1, NULL, 'BUTTON', NULL, '2023-10-25 09:13:59', NULL, NULL, '0', 'system:sponsor:update');
INSERT IGNORE INTO `b_menu` VALUES (299, '297', NULL, NULL, '删除', 2, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:sponsor:delete');
INSERT IGNORE INTO `b_menu` VALUES (300, '1', '/generate', '/system/generate', '代码生成', 3, 'project', 'MENU', NULL, '2024-04-02 15:23:53', NULL, 'generate', '1', NULL);
INSERT IGNORE INTO `b_menu` VALUES (301, '300', NULL, NULL, '下载', 2, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:generate:download');
INSERT IGNORE INTO `b_menu` VALUES (302, '265', NULL, NULL, '列表', 2, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:druid:list');
INSERT IGNORE INTO `b_menu` VALUES (313, '2', '', NULL, '分配权限', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:role:assign');
INSERT IGNORE INTO `b_menu` VALUES (314, '300', '', NULL, '预览', 1, NULL, 'BUTTON', NULL, NULL, NULL, NULL, '0', 'system:generate:preview');

-- ----------------------------
-- Table structure for b_message
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_message`  (
                              `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
                              `content` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
                              `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                              `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                              `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                              `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                              `time` tinyint NULL DEFAULT NULL,
                              `status` int NULL DEFAULT NULL COMMENT '状态 0:审核  1：正常',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 998 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_navigation
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_navigation`  (
                                 `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `site_class_id` int NOT NULL COMMENT '网站分类id',
                                 `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站标题',
                                 `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网址地址',
                                 `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站logo',
                                 `info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站描述',
                                 `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                 `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网站导航表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_navigation
-- ----------------------------
INSERT IGNORE INTO `b_navigation` VALUES (1, 1, '博客园', 'https://www.cnblog_demos.com/', '/asserts/navigation/50708941a28144479fc624e9a84c4773.png', '开发者的网上家园', '2023-08-01 09:00:19', '2023-08-01 09:00:21');
INSERT IGNORE INTO `b_navigation` VALUES (2, 1, 'CSDN', 'https://www.csdn.net/', '/asserts/navigation/d7f388f59ff745b799effde77692c9c0.png', '中文最大的技术社区', '2023-09-19 01:08:24', '2023-09-19 09:08:24');
INSERT IGNORE INTO `b_navigation` VALUES (3, 1, '开源中国', 'https://www.oschina.net/', '/asserts/navigation/df7a3adc69f84de49cfd5092cb020998.jpg', '目前国内最大的开源技术社区', '2023-09-19 01:09:12', '2023-09-19 09:09:12');
INSERT IGNORE INTO `b_navigation` VALUES (4, 1, '掘金', 'https://juejin.cn/', '/asserts/navigation/6f84bce665cd4debbdc94f02d0330741.png', '一个帮助开发者成长的社区', '2023-09-19 01:09:26', '2023-09-19 09:09:27');
INSERT IGNORE INTO `b_navigation` VALUES (5, 1, 'StackOverflow', 'https://stackoverflow.com/', '/asserts/navigation/6955e41a03a949419de916aa7bd575c3.jpg', '全球最大的技术问答社区', '2023-09-19 01:10:31', '2023-09-19 09:10:31');
INSERT IGNORE INTO `b_navigation` VALUES (6, 1, '简书', 'https://www.jianshu.com/', '/asserts/navigation/14e0d6d549664ee0a02ba6205fa0bb86.jpg', '创作你的创作', NULL, '2023-08-03 09:02:07');
INSERT IGNORE INTO `b_navigation` VALUES (10, 2, 'Cron表达式', 'https://www.matools.com/cron/', '/asserts/navigation/c2408e7dd86d41478bf36c66869966a1.jpg', '在线Cron表达式生成器', '2023-08-03 11:12:31', NULL);
INSERT IGNORE INTO `b_navigation` VALUES (11, 7, 'Element UI', 'https://element.eleme.cn/#/zh-CN', '/asserts/navigation/7ea049e2eadb4505b1775cd558cd3d0f.jpg', 'Element，一套为开发者、设计师和产品经理准备的基于 Vue 2.0 的桌面端组件库', '2023-08-03 11:17:58', NULL);
INSERT IGNORE INTO `b_navigation` VALUES (12, 7, 'Vue.js', 'https://vuejs.org/', '/asserts/navigation/d0c356bfecc2463aaf1841d4708a222b.png', '渐进式JavaScript框架、流行的前端开发框架！', '2023-08-03 11:19:27', NULL);
INSERT IGNORE INTO `b_navigation` VALUES (13, 2, '正则表达式', 'https://tool.oschina.net/regex/', '/asserts/navigation/df7a3adc69f84de49cfd5092cb020998.jpg', '在线正则表达式测试', '2023-09-19 01:12:46', NULL);
INSERT IGNORE INTO `b_navigation` VALUES (14, 2, '加密解密', 'https://tool.oschina.net/encrypt', '/asserts/navigation/df7a3adc69f84de49cfd5092cb020998.jpg', '在线加密解密', '2023-09-19 01:12:49', NULL);
INSERT IGNORE INTO `b_navigation` VALUES (15, 9, '\r\nIntelliJ IDEA', 'https://www.jetbrains.com/idea/', '/asserts/navigation/c6df8b1c4c134d188da3434ce1a7acd5.png', 'Java 最好用的编程工具！', '2023-09-19 01:14:07', '2023-09-19 09:14:07');
INSERT IGNORE INTO `b_navigation` VALUES (16, 9, 'Navicat', 'https://www.navicat.com.cn/download/navicat-premium', '/asserts/navigation/adf148865ccd40d39169f6aef7b7c9e0.png', '数据库常用客户端工具', '2023-09-19 01:14:13', '2023-09-19 09:14:13');
INSERT IGNORE INTO `b_navigation` VALUES (17, 9, 'ApiPost', 'https://www.apipost.cn/download.html', '/asserts/navigation/b22ced2b66be44b1a316273221dfa83c.jpg', ' 一个支持模拟POST、GET、PUT等常见HTTP请求，支持团队协作，并可直接生成并导出接口文档的API调试、管理工具', '2023-09-19 01:14:19', '2023-09-19 09:14:20');
INSERT IGNORE INTO `b_navigation` VALUES (18, 9, 'Snipaste', 'https://www.snipaste.com/', '/asserts/navigation/e70b53da83184814b23d84b40791392e.jpg', '便捷的截图工具', '2023-09-19 01:14:25', '2023-09-19 09:14:26');
INSERT IGNORE INTO `b_navigation` VALUES (19, 3, '站长之家', 'https://tool.chinaz.com/', '/asserts/navigation/d9b0b16d94654058889c97515256cc64.jpg', '站长工具是站长的必备工具', '2023-09-19 01:16:06', '2023-09-19 09:16:06');
INSERT IGNORE INTO `b_navigation` VALUES (20, 3, '网站排名', 'http://www.alexa.cn/', '/asserts/navigation/54480b512a00435d9c2724a718d20f6d.png', 'Alexa网站流量全球综合排名查询', '2023-09-19 01:16:17', '2023-09-19 09:16:17');
INSERT IGNORE INTO `b_navigation` VALUES (21, 3, '免费CDN', 'https://www.bootcdn.cn/', '/asserts/navigation/91389b5b818d4b4abdf64bc4c3d1b785.jpg', '中文网开源项目免费 CDN 加速服务', '2023-09-19 01:16:25', '2023-09-19 09:16:26');
INSERT IGNORE INTO `b_navigation` VALUES (22, 3, 'BOCE', 'https://www.boce.com/', '/asserts/navigation/676bdd6b870546629f5e6f8674342f85.jpg', '网站测速工具', '2023-09-19 01:16:37', '2023-09-19 09:16:37');
INSERT IGNORE INTO `b_navigation` VALUES (23, 3, '小码短链接', 'https://xiaomark.com/', '/asserts/navigation/a6df22027bdd464da08c14ca6e9c5ceb.png', '短网址跳转工具', '2023-09-19 01:16:47', '2023-09-19 09:16:47');
INSERT IGNORE INTO `b_navigation` VALUES (24, 3, '百度统计', 'https://tongji.baidu.com/web5/welcome/login', '/asserts/navigation/90d271afb0424a7cb6a564d84c4e5f19.png', '领先的中文网站分析平台', '2023-09-19 01:16:56', '2023-09-19 09:16:57');
INSERT IGNORE INTO `b_navigation` VALUES (25, 2, '码云', 'https://gitee.com/', 'https://img1.baidu.com/it/u=3680586942,1726414622&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', '专注于技术开源项目的托管、发布、协作的平台', NULL, NULL);
INSERT IGNORE INTO `b_navigation` VALUES (26, 7, 'Vite.js', 'https://vitejs.cn/', '/asserts/navigation/e0145d50d31744a487ff3ffc696fc10e.png', '下一代前端开发与构建工具', NULL, '2023-08-24 16:43:54');
INSERT IGNORE INTO `b_navigation` VALUES (27, 7, 'uni-app', 'https://uniapp.dcloud.net.cn/', 'https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/uni-app.png', '领先的跨平台开发框架，一套代码多端使用', NULL, NULL);
INSERT IGNORE INTO `b_navigation` VALUES (28, 9, 'Visual Studio Code', 'https://code.visualstudio.com/', '/asserts/navigation/v2-225fbbe09689a4a3219ae1e00fe2280b_1440w.jpg', '前端人员热爱的轻量级开发工具', NULL, '2023-08-24 16:53:44');

-- ----------------------------
-- Table structure for b_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_role`  (
                           `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编码',
                           `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
                           `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
                           `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统管理-角色表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_role
-- ----------------------------
INSERT IGNORE INTO `b_role` VALUES (1, 'admin', '管理员', '系统管理员', '2019-03-28 15:51:56', '2022-01-06 18:03:34');
INSERT IGNORE INTO `b_role` VALUES (2, 'user', '用户', '用户', '2021-12-27 07:01:39', '2021-12-27 07:01:39');
INSERT IGNORE INTO `b_role` VALUES (5, 'demonstrate', '演示', '演示账号', '2021-11-14 12:23:25', '2022-01-06 18:03:43');

-- ----------------------------
-- Table structure for b_role_menu
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_role_menu`  (
                                `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `role_id` int NULL DEFAULT NULL COMMENT '角色ID',
                                `menu_id` int NULL DEFAULT NULL COMMENT '菜单ID',
                                `created_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `last_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE INDEX `role_id`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15996 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统管理 - 角色-权限资源关联表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_role_menu
-- ----------------------------
INSERT IGNORE INTO `b_role_menu` VALUES (15329, 5, 1, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15330, 5, 2, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15331, 5, 3, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15332, 5, 4, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15333, 5, 185, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15334, 5, 8, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15335, 5, 9, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15336, 5, 12, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15337, 5, 14, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15338, 5, 15, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15339, 5, 19, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15340, 5, 20, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15341, 5, 21, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15342, 5, 22, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15343, 5, 23, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15344, 5, 169, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15345, 5, 170, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15346, 5, 182, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15347, 5, 183, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15348, 5, 26, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15349, 5, 27, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15350, 5, 28, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15351, 5, 29, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15352, 5, 30, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15353, 5, 31, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15354, 5, 32, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15355, 5, 33, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15356, 5, 215, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15357, 5, 253, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15358, 5, 35, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15359, 5, 36, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15360, 5, 37, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15361, 5, 38, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15362, 5, 39, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15363, 5, 40, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15364, 5, 189, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15365, 5, 223, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15366, 5, 224, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15367, 5, 225, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15368, 5, 226, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15369, 5, 227, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15370, 5, 228, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15371, 5, 286, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15372, 5, 291, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15373, 5, 41, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15374, 5, 51, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15375, 5, 52, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15376, 5, 171, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15377, 5, 172, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15378, 5, 175, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15379, 5, 186, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15380, 5, 187, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15381, 5, 275, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15382, 5, 276, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15383, 5, 280, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15384, 5, 281, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15385, 5, 56, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15386, 5, 57, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15387, 5, 58, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15388, 5, 59, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15389, 5, 60, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15390, 5, 61, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15391, 5, 62, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15392, 5, 63, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15393, 5, 66, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15394, 5, 67, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15395, 5, 248, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15396, 5, 261, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15397, 5, 164, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15398, 5, 165, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15399, 5, 166, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15400, 5, 191, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15401, 5, 192, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15402, 5, 199, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15403, 5, 200, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15404, 5, 263, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15405, 5, 264, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15406, 5, 265, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15407, 5, 302, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15408, 5, 269, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15409, 5, 270, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15410, 5, 245, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15411, 5, 47, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15412, 5, 48, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15413, 5, 256, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15414, 5, 257, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15415, 5, 266, '2024-04-02 17:07:54', '2024-04-02 17:07:54');
INSERT IGNORE INTO `b_role_menu` VALUES (15416, 5, 267, '2024-04-02 17:07:54', '2024-04-02 17:07:54');

-- ----------------------------
-- Table structure for b_say
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_say`  (
                          `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
                          `user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                          `img_url` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片地址 逗号分隔  最多九张',
                          `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
                          `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发表地址。可输入或者地图插件选择',
                          `is_public` int NULL DEFAULT NULL COMMENT '是否开放查看  0未开放 1开放',
                          `create_time` datetime(0) NOT NULL COMMENT '发表时间',
                          `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '说说表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_say_comment
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_say_comment`  (
                                  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id ',
                                  `say_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '说说id',
                                  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                                  `reply_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回复用户id',
                                  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
                                  `ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
                                  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '说说评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_sign
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_sign`  (
                           `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                           `user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                           `create_time` date NOT NULL COMMENT '签到时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_site_class
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_site_class`  (
                                 `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网址分类名',
                                 `sort` int NULL DEFAULT 0 COMMENT '排序',
                                 `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网址分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_site_class
-- ----------------------------
INSERT IGNORE INTO `b_site_class` VALUES (1, '技术社区', 9, '2023-08-02 15:05:57', '2023-08-02 15:08:13');
INSERT IGNORE INTO `b_site_class` VALUES (2, '常用推荐', 10, '2023-08-02 15:05:57', '2023-08-03 09:06:56');
INSERT IGNORE INTO `b_site_class` VALUES (3, '站长工具', 6, '2023-08-02 15:05:57', '2023-08-03 09:07:13');
INSERT IGNORE INTO `b_site_class` VALUES (7, '前端开发', 7, '2023-08-02 15:14:10', '2023-08-03 09:07:10');
INSERT IGNORE INTO `b_site_class` VALUES (8, '后端开发', 8, '2023-08-02 15:14:16', '2023-08-03 09:07:08');
INSERT IGNORE INTO `b_site_class` VALUES (9, '开发工具', 9, '2023-08-15 10:36:56', NULL);

-- ----------------------------
-- Table structure for b_software
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_software`  (
                               `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '软件名称',
                               `info` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述',
                               `code_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '源码地址',
                               `cover_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '轮播图片,逗号分隔',
                               `demo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '演示地址',
                               `detail_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细介绍地址',
                               `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '开源软件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_software
-- ----------------------------
INSERT IGNORE INTO `b_software` VALUES (1, 'Hello Scala', '<p style=\"line-height: 2;\">一款vue+springboot前后端分离的博客系统，博客后台管理系统使用了vue+elmentui开发，后端使用Sa-Token进行权限管理,支持动态菜单权限，动态定时任务，文件支持本地和七牛云上传，使用ElasticSearch作为全文检索服务，支持QQ、微信公众号扫码、码云、GitHub登录。实现即时通讯聊天室功能</p><ul><li style=\"line-height: 2;\">netty即时通讯：<span style=\"color: rgb(191, 191, 191);\"> 能够实现群聊和单聊</span></li><li style=\"line-height: 2;\"><span style=\"color: rgb(0, 0, 0);\">文章视频：</span><span style=\"color: rgb(191, 191, 191);\">文章可上传视频</span></li><li style=\"line-height: 2;\"><span style=\"color: rgb(0, 0, 0);\">第三登录：</span><span style=\"color: rgb(191, 191, 191);\">集成第三方登录，实现多种登录方式</span></li></ul>', '', ' ', 'https://www.helloscala.com/', NULL, '2024-03-19 15:11:12');
INSERT IGNORE INTO `b_software` VALUES (2, 'ws-chat', '<p style=\"line-height: 2;\">一款基于springboot+websoket+vue的前后端分离即时聊天系统，包含了群聊、单聊以及音乐播放等功能</p><ul><li style=\"line-height: 2;\">netty即时通讯：<span style=\"color: rgb(191, 191, 191);\"> 使用websoket实现群聊和单聊</span></li><li style=\"line-height: 2;\"><span style=\"color: rgb(0, 0, 0);\">音乐播放：</span><span style=\"color: rgb(191, 191, 191);\">提供音乐播放列表，边聊边听</span></li></ul>', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_system_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_system_config`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `qi_niu_access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '七牛云公钥',
                                    `qi_niu_secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '七牛云私钥',
                                    `qi_niu_area` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '七牛云存储区域 华东（z0），华北(z1)，华南(z2)，北美(na0)，东南亚(as0)',
                                    `qi_niu_bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '七牛云上传空间',
                                    `qi_niu_picture_base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '七牛云域名前缀：http://images.moguit.cn',
                                    `upload_qi_niu` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件上传七牛云(0:否， 1:是)',
                                    `open_email_activate` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否开启注册用户邮件激活(0:否， 1:是)',
                                    `start_email_notification` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否开启邮件通知(0:否， 1:是)',
                                    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `open_dashboard_notification` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否开启仪表盘通知(0:否， 1:是)',
                                    `dashboard_notification_md` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '仪表盘通知【用于首次登录弹框】MD',
                                    `dashboard_notification` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '仪表盘通知【用于首次登录弹框】',
                                    `search_model` int NOT NULL DEFAULT 0 COMMENT '搜索模式【0:SQL搜索 、1：全文检索】',
                                    `email_host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
                                    `email_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱发件人',
                                    `email_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱授权码',
                                    `email_port` int NULL DEFAULT NULL COMMENT '邮箱发送端口',
                                    `open_email` int NULL DEFAULT NULL COMMENT '启用邮箱发送',
                                    `local_file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本地文件地址',
                                    `file_upload_way` int NULL DEFAULT NULL COMMENT '文件上传方式 1:本地 2：七牛云',
                                    `ali_yun_access_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '阿里云ak',
                                    `ali_yun_secret_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '阿里云sk',
                                    `ali_yun_bucket` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '阿里云存储桶名',
                                    `ali_yun_endpoint` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '阿里云Endpoint',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_system_config
-- ----------------------------
INSERT IGNORE INTO `b_system_config` VALUES (1, '', '', 'z0', 'helloscala', 'https://www.helloscala.com/', '1', '1', '1', '2021-11-26 15:41:36', '2021-11-26 15:41:36', '1', '欢迎来到Hello Scala项目，开源项目离不开大家的支持，希望小伙伴能随手点赞一下，你的点赞就是我维护的动力~\n\n博主使用笔记本开发的项目，所以页面中表格的列在大屏显示器中会显示宽度的不均衡，如需要可以自行调整。\n\n项目源码：[点我传送]()，项目官网：[点我传送](http://www.helloscala.com)\n\n项目还在开发阶段，如有不善的地方欢迎各位小伙伴多多反馈1\n\n最低配置：1核2G \n\n推荐配置：2核4G [【狂欢特惠】](https://www.zhisu1.com/cart?fid=1&gid=3)\n\n服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话\n\n欢迎朋友能够给予一些支持，非常感谢~（ps.. 小伙伴赞赏的时候可以备注一下下~）\n|支付宝|微信|\n|-|-|-|\n|<img src=\"\" width=\"50%\">|<img src=\"\" width=\"50%\">\n', '<p>欢迎来到Hello Scala项目，开源项目离不开大家的支持，希望小伙伴能随手点赞一下，你的点赞就是我维护的动力~</p>\n<p>博主使用笔记本开发的项目，所以页面中表格的列在大屏显示器中会显示宽度的不均衡，如需要可以自行调整。</p>\n<p>项目源码：<a href=\"\" target=\"_blank\">点我传送</a>，项目官网：<a href=\"http://www.helloscala.com\" target=\"_blank\">点我传送</a></p>\n<p>项目还在开发阶段，如有不善的地方欢迎各位小伙伴多多反馈1</p>\n<p>最低配置：1核2G</p>\n<p>推荐配置：2核4G <a href=\"https://www.zhisu1.com/cart?fid=1&amp;gid=3\" target=\"_blank\">【狂欢特惠】</a></p>\n<p>服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话</p>\n<p>欢迎朋友能够给予一些支持，非常感谢~（ps… 小伙伴赞赏的时候可以备注一下下~）</p>\n<table>\n<thead>\n<tr>\n<th>支付宝</th>\n<th>微信</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td><img src=\"\" width=\"50%\"></td>\n<td><img src=\"\" width=\"50%\"></td>\n</tr>\n</tbody>\n</table>\n', 0, 'smtp.163.com', '', '', 25, 0, 'http://127.0.0.1:8800/helloscala/img/', 0, '', '', '', '');
INSERT IGNORE INTO `b_system_config` VALUES (2, '', '', 'z0', '', 'https://www.helloscala.com/', '1', '1', '1', '2021-11-26 15:41:36', '2021-11-26 15:41:36', '1', '欢迎来到Hello Scala项目，开源项目离不开大家的支持，希望小伙伴能随手点赞一下，你的点赞就是我维护的动力~\n\n博主使用笔记本开发的项目，所以页面中表格的列在大屏显示器中会显示宽度的不均衡，如需要可以自行调整。\n\n项目源码：[点我传送]()，项目官网：[点我传送](http://www.helloscala.com)\n\n项目还在开发阶段，如有不善的地方欢迎各位小伙伴多多反馈\n\n最低配置：1核2G \n\n推荐配置：2核4G [【狂欢特惠】](https://cloud.tencent.com/act/new?channel=sp&fromSource=gwzcw.5433948.5433948.5433948&utm_medium=cpc&utm_id=gwzcw.5433948.5433948.5433948&bd_vid=6261311440599794431)\n\n服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话\n\n欢迎朋友能够给予一些支持，非常感谢~（ps.. 小伙伴赞赏的时候可以备注一下下~）\n|支付宝|微信|\n|-|-|-|\n|![支付宝]()|![微信]()|\n', '<p>欢迎来到Hello Scala项目，开源项目离不开大家的支持，希望小伙伴能随手点赞一下，你的点赞就是我维护的动力~</p>\n<p>博主使用笔记本开发的项目，所以页面中表格的列在大屏显示器中会显示宽度的不均衡，如需要可以自行调整。</p>\n<p>项目源码：<a href=\"\" target=\"_blank\">点我传送</a>，项目官网：<a href=\"http://www.helloscala.com\" target=\"_blank\">点我传送</a></p>\n<p>项目还在开发阶段，如有不善的地方欢迎各位小伙伴多多反馈</p>\n<p>最低配置：1核2G</p>\n<p>推荐配置：2核4G <a href=\"https://cloud.tencent.com/act/new?channel=sp&amp;fromSource=gwzcw.5433948.5433948.5433948&amp;utm_medium=cpc&amp;utm_id=gwzcw.5433948.5433948.5433948&amp;bd_vid=6261311440599794431\" target=\"_blank\">【狂欢特惠】</a></p>\n<p>服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话</p>\n<p>欢迎朋友能够给予一些支持，非常感谢~（ps… 小伙伴赞赏的时候可以备注一下下~）</p>\n<table>\n<thead>\n<tr>\n<th>支付宝</th>\n<th>微信</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td><img src=\"\" alt=\"支付宝\" /></td>\n<td><img src=\"\" alt=\"微信\" /></td>\n</tr>\n</tbody>\n</table>\n', 1, 'smtp.qq.com', 'xxx@qq.com', NULL, 587, 1, NULL, 1, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_tags`  (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                           `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标签名称',
                           `click_volume` int NULL DEFAULT 0,
                           `sort` int NOT NULL COMMENT '排序',
                           `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `tag_name`(`name`) USING BTREE COMMENT '博客标签名称'
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_tags
-- ----------------------------
INSERT IGNORE INTO `b_tags` VALUES (1, 'Spring Boot', 842, 20, '2021-11-12 14:43:27', '2023-06-05 03:00:00');
INSERT IGNORE INTO `b_tags` VALUES (2, 'Elasticsearch', 0, 21, '2021-11-12 14:43:27', '2024-03-27 14:36:52');
INSERT IGNORE INTO `b_tags` VALUES (10, '博客', 679, 9, '2021-11-12 14:43:27', '2023-06-05 03:00:00');
INSERT IGNORE INTO `b_tags` VALUES (12, 'Vue', 300, 18, '2021-12-29 14:01:50', '2023-06-05 03:00:00');
INSERT IGNORE INTO `b_tags` VALUES (13, 'Spring Cloud', 759, 19, '2021-12-29 14:02:32', '2023-06-05 03:00:00');
INSERT IGNORE INTO `b_tags` VALUES (17, 'Redis', 395, 7, '2022-01-25 14:09:03', '2023-06-05 03:00:00');
INSERT IGNORE INTO `b_tags` VALUES (18, 'Linux', 491, 10, '2022-01-25 14:09:17', '2023-06-05 03:00:00');
INSERT IGNORE INTO `b_tags` VALUES (31, 'Mysql', 516, 10, '2022-02-18 16:01:07', '2023-06-05 03:00:00');
INSERT IGNORE INTO `b_tags` VALUES (32, 'Nginx', 442, 5, '2022-04-13 17:48:08', '2023-06-05 03:00:00');
INSERT IGNORE INTO `b_tags` VALUES (51, '异步', 92, 5, '2023-04-03 11:30:54', '2023-06-05 03:00:00');
INSERT IGNORE INTO `b_tags` VALUES (57, 'Java Script', 0, 1, '2023-06-27 16:02:33', '2023-06-27 08:02:32');
INSERT IGNORE INTO `b_tags` VALUES (59, 'Dcoker', 0, 12, '2023-07-13 15:51:12', '2023-07-13 07:51:12');
INSERT IGNORE INTO `b_tags` VALUES (60, 'Aspect', 0, 0, '2023-07-25 17:07:19', '2023-07-25 09:07:18');
INSERT IGNORE INTO `b_tags` VALUES (62, 'oauth2', 0, 0, '2023-08-15 09:30:12', '2023-08-15 01:30:11');
INSERT IGNORE INTO `b_tags` VALUES (63, '服务器', 0, 0, '2023-09-05 17:28:08', '2023-09-05 09:28:07');
INSERT IGNORE INTO `b_tags` VALUES (65, 'Css', 0, 0, '2023-09-25 11:19:00', '2023-09-25 03:18:59');
INSERT IGNORE INTO `b_tags` VALUES (66, 'Java', 0, 11, '2023-10-07 09:28:50', '2023-10-07 01:28:50');
INSERT IGNORE INTO `b_tags` VALUES (67, 'Juc', 0, 12, '2023-10-07 09:28:55', '2023-10-07 01:28:54');
INSERT IGNORE INTO `b_tags` VALUES (69, '高并发', 0, 14, '2023-10-07 09:29:11', '2023-10-07 01:29:11');
INSERT IGNORE INTO `b_tags` VALUES (70, '资源', 0, 0, '2023-10-12 10:46:35', '2023-10-12 02:46:34');
INSERT IGNORE INTO `b_tags` VALUES (71, 'websoket', 0, 0, '2023-12-14 17:17:58', '2023-12-14 09:17:58');
INSERT IGNORE INTO `b_tags` VALUES (72, 'uniapp', 0, 0, '2023-12-26 11:28:20', '2023-12-26 03:28:20');
INSERT IGNORE INTO `b_tags` VALUES (73, '宝塔', 0, 0, '2024-02-19 16:17:29', '2024-02-19 08:17:29');
INSERT IGNORE INTO `b_tags` VALUES (80, 'vue.js', 0, 0, '2024-03-29 17:14:48', '2024-03-29 17:14:47');
INSERT IGNORE INTO `b_tags` VALUES (81, '前端', 0, 0, '2024-03-29 17:14:48', '2024-03-29 17:14:48');
INSERT IGNORE INTO `b_tags` VALUES (82, 'javascript', 0, 0, '2024-03-29 17:14:48', '2024-03-29 17:14:48');

-- ----------------------------
-- Table structure for b_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_user`  (
                           `id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
                           `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
                           `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
                           `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                           `status` int NULL DEFAULT 1 COMMENT '状态 0:禁用 1:正常',
                           `login_type` int NULL DEFAULT NULL COMMENT '登录方式',
                           `user_info_id` bigint NULL DEFAULT NULL COMMENT '用户详情id',
                           `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
                           `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
                           `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
                           `os` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录系统',
                           `last_login_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
                           `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器',
                           `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱号',
                           `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
                           `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户头像',
                           `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户简介',
                           `web_site` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人网站',
                           `bj_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人中心背景图',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统管理-用户基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_user
-- ---------------------------- K29CzeYFWL8Skdn2qATp8g==
INSERT IGNORE INTO `b_user` VALUES ('1', 'admin', 'NLJ3Gidivf3vouOjYLIvuA==', '2021-09-27 15:43:45', '2022-03-10 17:59:40', 1, 1, 1, 1, '127.0.0.1', '未知', 'Windows 10', '2024-04-03 13:44:33', 'Chrome 12',null,'admin','',null,null,null);
INSERT IGNORE INTO `b_user` VALUES ('2', 'test', 'NLJ3Gidivf3vouOjYLIvuA==', '2021-11-14 12:35:03', '2022-12-13 14:08:33', 0, 1, 2, 5, '127.0.0.1', '未知', 'Windows 10', '2024-04-02 17:05:23', 'Chrome 12',null,'test','',null,null,null);

-- ----------------------------
-- Table structure for b_user_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_user_log`  (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `user_id` bigint NULL DEFAULT NULL COMMENT '操作用户ID',
                               `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
                               `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作地址',
                               `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
                               `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作日志',
                               `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作模块',
                               `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                               `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作结果',
                               `access_os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
                               `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器',
                               `client_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端类型',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 395738 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_user_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_user_role`  (
                                `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `role_id` int NULL DEFAULT NULL COMMENT '角色ID',
                                `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
                                `created_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `last_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统管理 - 用户角色关联表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_user_role
-- ----------------------------
INSERT IGNORE INTO `b_user_role` VALUES (12, 1, 1, '2019-08-21 10:49:41', '2019-08-21 10:49:41');
INSERT IGNORE INTO `b_user_role` VALUES (34, 5, 15, '2021-11-14 12:35:03', '2021-11-14 12:35:03');

-- ----------------------------
-- Table structure for b_web_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `b_web_config`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'logo(文件UID)',
                                 `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站名称',
                                 `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '介绍',
                                 `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关键字',
                                 `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者',
                                 `record_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备案号',
                                 `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `web_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站地址',
                                 `ali_pay` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝收款码FileId',
                                 `weixin_pay` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信收款码FileId',
                                 `github` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'github地址',
                                 `gitee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'gitee地址',
                                 `qq_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'QQ号',
                                 `qq_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'QQ群',
                                 `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                 `wechat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信',
                                 `show_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示的列表（用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端）',
                                 `login_type_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）',
                                 `open_comment` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
                                 `open_admiration` tinyint NOT NULL DEFAULT 0 COMMENT '是否开启赞赏(0:否， 1:是)',
                                 `tourist_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '游客头像',
                                 `bulletin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告',
                                 `author_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者简介',
                                 `author_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者头像',
                                 `about_me` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '关于我',
                                 `is_music_player` int NULL DEFAULT 0 COMMENT '是否开启音乐播放器',
                                 `show_bulletin` int NULL DEFAULT NULL COMMENT '是否显示公告 0 否 1 是',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网站配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_web_config
-- ----------------------------
INSERT IGNORE INTO `b_web_config` VALUES (1, '', 'Hello Scala', 'Hello Scala', 'Hello Scala', 'Hello Scala', '', '2021-11-27 13:43:16', '2022-01-20 13:30:44', 'https://www.helloscala.com', '', '', 'https://github.com/quequnlong', '', '1248954763', '779881756', '1248954763@qq.com', '1248954763', '1,4,2,5,6', '5,4,6,3,1', '0', 1, '', '测试达成1000star成就咯。兄弟们<a href=\"\" target=\"_blank\">码云</a>继续点小星星', '欲买桂花同载酒，终不似，少年游。', '', '<p style=\"line-height: 2;\"><img src=\"\" alt=\"\" data-href=\"\" style=\"\"></p><ul><li style=\"line-height: 2;\"><span style=\"color: rgb(235, 144, 58); font-size: 16px;\"><strong>小程序预览</strong></span></li></ul><p style=\"line-height: 2;\"><img src=\"\" alt=\"\" data-href=\"\" style=\"\"></p><p style=\"line-height: 2;\"><br></p><ul><li style=\"line-height: 2;\"><span style=\"color: rgb(235, 144, 58); font-size: 16px;\"><strong>关于我</strong></span></li></ul><p style=\"line-height: 2;\">Hello Scala一群对Scala感兴趣的Java工程师，目前正在努力学习Java、Scala和前端技术</p><p style=\"line-height: 2;\"><span style=\"color: rgb(225, 60, 57);\">本来想着入了这行有点高级，收入也应该不菲，原来都是错觉!</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(247, 89, 171);\">入行到现在，钱鸡毛没赚下，b装的不少，人胖了不少，关键枸杞也没少买！还好头发尚在！😀😀</span></p><p style=\"line-height: 2;\"><img src=\"https://pic2.zhimg.com/v2-77d8d8fad8c2425d62f935d7dece19a9_b.gif\" alt=\"\" data-href=\"https://pic2.zhimg.com/v2-77d8d8fad8c2425d62f935d7dece19a9_b.gif\" style=\"width: 220.00px;height: 220.00px;\"></p><ul><li style=\"line-height: 2;\"><span style=\"color: rgb(235, 144, 58); font-size: 16px;\">项目背景</span></li></ul><p>由于本人喜欢研究各种新东西，特别是博客，见到漂亮的博客系统就也想部署一份。但是呢，很多好看的系统都是用的建站软件开发的，所以并没有源码，有源码也不是我特别喜欢的类型。</p><p>所以就干脆自己写一个让自己满意的。终于耗时几个世纪的夜晚，“<span style=\"color: rgb(225, 60, 57);\">Hello Scala</span>”终于问世。期间有各种bug，不过经过我的缝缝补补，总算可以完美运行。特此将项目进行开源，给大家使用。</p><p><span style=\"color: rgb(255, 77, 79);\">√ 达成1000 star</span></p><p>2023.10.07正式突破1000 star,也是满足我的一个小心愿。谢谢大家的喜欢。接下来我会更加努力的完善好Hello Scala的</p><p><img src=\"\" alt=\"\" data-href=\"\" style=\"width: 701.00px;height: 269.70px;\"/></p><ul><li style=\"line-height: 2;\"><span style=\"color: rgb(235, 144, 58); font-size: 16px;\">项目框架</span></li></ul><p style=\"line-height: 2;\">前端使用了<span style=\"color: rgb(64, 169, 255);\">Vue+Elmentui</span>，能够更加容易得进行扩展</p><p style=\"line-height: 2;\">后端使用的<span style=\"color: rgb(64, 169, 255);\">Spring Boot+Mysql+Mybatis-Plus</span>框架</p><ul><li style=\"line-height: 2;\"><span style=\"color: rgb(235, 144, 58); font-size: 16px;\">源码</span></li></ul><p style=\"line-height: 2;\">目前博客源码已经开源至 <a href=\"\" target=\"_blank\">码云</a> ，感兴趣的小伙伴可以star fork关注一下下~111</p><p style=\"line-height: 2;\">Gitee地址：<a href=\"\" target=\"_blank\">[]()</a></p><p style=\"line-height: 2;\"><br></p><p style=\"line-height: 2;\"><br></p><p style=\"line-height: 2;\"><br></p><p style=\"line-height: 2;\"><br></p>', 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
