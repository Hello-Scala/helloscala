package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.entity.SystemConfig;
import com.helloscala.vo.system.TableListVO;


public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

    Page<TableListVO> selectTables(Page<Object> objectPage);
}
