set @exec_sql = 'alter table b_article_tag add column create_time datetime not null default now() after tag_id;';
set @exist_sql = 'select \'exist\';';
select if(@count >= 1, @exist_sql, @exec_sql) into @sql;
PREPARE stmt FROM @sql;
execute stmt;
DEALLOCATE PREPARE stmt;