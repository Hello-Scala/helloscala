CREATE TABLE IF NOT EXISTS `b_resource`  (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `url` varchar(200) NOT NULL COMMENT '文件地址',
                           `type` varchar(50) NOT NULL COMMENT '文件类型',
                           `platform` varchar(50) NOT NULL COMMENT '存储平台',
                           `user_id` varchar(50) NOT NULL COMMENT '上传用户id',
                           `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
                           `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统管理-文件资源表' ROW_FORMAT = Dynamic;
