CREATE TABLE IF NOT EXISTS `b_assistant_conversation`
(
    `id`              CHAR(36)     NOT NULL,
    `conversation_id` VARCHAR(255) NOT NULL,
    `bot_id`          VARCHAR(255) NOT NULL,
    `user_id`         VARCHAR(255) NOT NULL,
    `create_time`     DATETIME     NOT NULL,
    `create_by`       VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_conversation_id` (`conversation_id`),
    INDEX `idx_user_id_create_time` (`user_id`, `create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `b_assistant_message`
(
    `id`              CHAR(36)     NOT NULL,
    `conversation_id` VARCHAR(255) NOT NULL,
    `bot_id`          VARCHAR(255) NOT NULL,
    `message_id`      VARCHAR(255) NOT NULL,
    `send_from`       VARCHAR(50)  NOT NULL,
    `content`         TEXT         NOT NULL,
    `content_type`    VARCHAR(50)  NOT NULL,
    `user_id`         VARCHAR(255) NOT NULL,
    `create_time`     DATETIME     NOT NULL,
    `create_by`       VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_message_id` (`message_id`),
    KEY `idx_conversation_id` (`conversation_id`),
    INDEX `idx_user_id_create_time` (`user_id`, `create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

SELECT COUNT(*) INTO @count
FROM information_schema.COLUMNS
WHERE table_schema = 'hello_scala' AND TABLE_NAME = 'b_assistant_conversation' and COLUMN_NAME = 'summary';
set @exec_sql = 'alter table b_assistant_conversation add column summary TEXT NULL after user_id;';
set @exist_sql = 'select \'exist\';';
select if(@count >= 1, @exist_sql, @exec_sql) into @sql;
PREPARE stmt FROM @sql;
execute stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*) INTO @count
FROM information_schema.COLUMNS
WHERE table_schema = 'hello_scala' AND TABLE_NAME = 'b_assistant_conversation' and COLUMN_NAME = 'last_send_time';
set @exec_sql = 'alter table b_assistant_conversation add column last_send_time DATETIME NULL after summary;';
set @exist_sql = 'select \'exist\';';
select if(@count >= 1, @exist_sql, @exec_sql) into @sql;
PREPARE stmt FROM @sql;
execute stmt;
DEALLOCATE PREPARE stmt;