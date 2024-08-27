package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.CozeFile;
import com.helloscala.service.mapper.CozeFileMapper;
import com.helloscala.service.service.CozeFileService;
import com.helloscala.common.utils.SqlHelper;
import org.springframework.stereotype.Service;


@Service
public class CozeFileServiceImpl extends ServiceImpl<CozeFileMapper, CozeFile> implements CozeFileService {
    @Override
    public CozeFile getByFileName(String fileName) {
        LambdaQueryWrapper<CozeFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CozeFile::getFileName, fileName);
        queryWrapper.last(SqlHelper.LIMIT_1);
        return baseMapper.selectOne(queryWrapper);
    }
}
