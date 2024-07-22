CREATE TABLE IF NOT EXISTS `b_coze_file`
(
    `id`          CHAR(36)     NOT NULL,
    `coze_id`     VARCHAR(255) NOT NULL,
    `file_name`   VARCHAR(255) NOT NULL,
    `file_url`    TEXT         NULL,
    `bytes`       BIGINT       NULL,
    `user_id`     VARCHAR(255) NULL,
    `create_time` DATETIME     NOT NULL,
    `create_by`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_coze_id` (`coze_id`),
    UNIQUE KEY `idx_file_name` (`file_name`),
    INDEX `idx_user_id_create_time` (`user_id`, `create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;