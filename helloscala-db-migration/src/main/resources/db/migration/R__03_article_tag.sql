SELECT COUNT(*) INTO @count
FROM information_schema.COLUMNS
WHERE table_schema = 'hello_scala' AND TABLE_NAME = 'b_article_tag' and COLUMN_NAME = 'create_time';
set @exec_sql = 'alter table b_article_tag add column create_time datetime not null default now() after tag_id;';
set @exist_sql = 'select \'exist\';';
select if(@count >= 1, @exist_sql, @exec_sql) into @sql;
PREPARE stmt FROM @sql;
execute stmt;
DEALLOCATE PREPARE stmt;