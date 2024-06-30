package com.helloscala.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.entity.Dict;
import com.helloscala.common.entity.DictData;
import com.helloscala.common.mapper.DictDataMapper;
import com.helloscala.common.mapper.DictMapper;
import com.helloscala.common.service.DictService;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.web.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;




@Service
@RequiredArgsConstructor
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Lazy
    private final DictDataMapper dictDataMapper;

    @Override
    public Page<Dict> selectDictPage(String name, Integer status) {
        return baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), new LambdaQueryWrapper<Dict>()
                .eq(status != null,Dict::getStatus,status)
                .like(StrUtil.isNotBlank(name), Dict::getName,name)
                .orderByDesc(Dict::getSort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDict(Dict dict) {
        validateTypeIsExist(dict.getType());
        baseMapper.insert(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(Dict dict) {
        Dict temp = baseMapper.selectById(dict.getId());
        if (!temp.getType().equals(dict.getType())){
            validateTypeIsExist(dict.getType());
        }
        baseMapper.updateById(dict);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(List<Long> ids) {
        Long count  = dictDataMapper.selectCount(new LambdaQueryWrapper<DictData>().in(DictData::getDictId,ids));
        if (count > 0) {
            throw new ConflictException("Dict delete failed, dict data exist！");
        }
        baseMapper.deleteBatchIds(ids);
    }


    public void validateTypeIsExist(String type){
        Dict temp  = baseMapper.selectOne(new LambdaQueryWrapper<Dict>().eq(Dict::getType, type).last(Constants.LIMIT_ONE));
        if (temp != null) {
            throw new ConflictException("Dict type exist!");
        }
    }
}
