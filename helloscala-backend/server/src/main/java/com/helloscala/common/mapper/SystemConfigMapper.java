package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.SystemConfig;
import com.helloscala.common.vo.system.TableVO;


public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

    Page<TableVO> selectTables(Page<Object> objectPage);
}
