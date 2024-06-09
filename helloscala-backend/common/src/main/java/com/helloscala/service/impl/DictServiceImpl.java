package com.helloscala.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Dict;
import com.helloscala.entity.DictData;
import com.helloscala.exception.BusinessException;
import com.helloscala.mapper.DictDataMapper;
import com.helloscala.mapper.DictMapper;
import com.helloscala.service.DictService;
import com.helloscala.utils.PageUtil;
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

    /**
     * 字典列表
     * @param name
     * @return
     */
    @Override
    public ResponseResult selectDictPage(String name, Integer status) {

        Page data = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), new LambdaQueryWrapper<Dict>()
                .eq(status != null,Dict::getStatus,status)
                .like(StrUtil.isNotBlank(name), Dict::getName,name)
                .orderByDesc(Dict::getSort));
        return ResponseResult.success(data);
    }

    /**
     * 添加字典
     * @param dict
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addDict(Dict dict) {
        validateTypeIsExist(dict.getType());
        baseMapper.insert(dict);
        return ResponseResult.success();
    }

    /**
     * 修改字典
     * @param dict
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateDict(Dict dict) {
        Dict temp = baseMapper.selectById(dict.getId());
        if (!temp.getType().equals(dict.getType())){
            validateTypeIsExist(dict.getType());
        }
        baseMapper.updateById(dict);
        return ResponseResult.success();
    }


    /**
     * 批量删除字典
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteDict(List<Long> ids) {
        Long count  = dictDataMapper.selectCount(new LambdaQueryWrapper<DictData>().in(DictData::getDictId,ids));
        if (count > 0) {
            throw new BusinessException("Dict delete failed, dict data exist！");
        }
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }


    /* ---------自定义方法开始------------*/
    public void validateTypeIsExist(String type){
        Dict temp  = baseMapper.selectOne(new LambdaQueryWrapper<Dict>().eq(Dict::getType, type).last(Constants.LIMIT_ONE));
        if (temp != null) {
            throw new BusinessException("Dict type exist!");
        }
    }
}
