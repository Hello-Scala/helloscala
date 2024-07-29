SELECT COUNT(*) INTO @count
FROM information_schema.TABLES
WHERE table_schema = 'hello_scala' AND TABLE_NAME = 'b_followed';
set @exist_sql = 'select \'not exist\';';
set @exec_sql = 'alter table b_followed default character set utf8mb4 collate utf8mb4_general_ci;';
select if(@count < 1, @exist_sql, @exec_sql) into @sql;
PREPARE stmt FROM @sql;
execute stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*) INTO @count
FROM information_schema.COLUMNS
WHERE table_schema = 'hello_scala' AND TABLE_NAME = 'b_followed' AND COLUMN_NAME = 'user_id';
set @exist_sql = 'select \'not exist\';';
set @exec_sql = 'alter table b_followed modify column `user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT \'用户id\';';
select if(@count < 1, @exist_sql, @exec_sql) into @sql;
PREPARE stmt FROM @sql;
execute stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*) INTO @count
FROM information_schema.COLUMNS
WHERE table_schema = 'hello_scala' AND TABLE_NAME = 'b_followed' AND COLUMN_NAME = 'followed_user_id';
set @exist_sql = 'select \'not exist\';';
set @exec_sql = 'alter table b_followed modify column `followed_user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT \'关注的用户id\';';
select if(@count < 1, @exist_sql, @exec_sql) into @sql;
PREPARE stmt FROM @sql;
execute stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*) INTO @count
FROM information_schema.TABLES
WHERE table_schema = 'hello_scala' AND TABLE_NAME = 'b_im_message';
set @exist_sql = 'select \'not exist\';';
set @exec_sql = 'alter table b_im_message default character set utf8mb4 collate utf8mb4_general_ci;';
select if(@count < 1, @exist_sql, @exec_sql) into @sql;
PREPARE stmt FROM @sql;
execute stmt;
DEALLOCATE PREPARE stmt;
