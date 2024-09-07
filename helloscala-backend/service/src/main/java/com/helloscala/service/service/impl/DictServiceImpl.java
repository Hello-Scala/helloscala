package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.Dict;
import com.helloscala.service.entity.DictData;
import com.helloscala.service.enums.YesOrNoEnum;
import com.helloscala.service.mapper.DictDataMapper;
import com.helloscala.service.mapper.DictMapper;
import com.helloscala.service.service.DictService;
import com.helloscala.service.service.util.DictHelper;
import com.helloscala.service.web.request.CreateDictRequest;
import com.helloscala.service.web.request.UpdateDictRequest;
import com.helloscala.service.web.view.DictView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Lazy
    private final DictDataMapper dictDataMapper;

    @Override
    public Page<DictView> listByPage(Page<?> page, String name, String status) {
        Page<Dict> dictPage = baseMapper.selectPage(PageHelper.of(page), new LambdaQueryWrapper<Dict>()
                .eq(status != null, Dict::getStatus, status)
                .like(StrUtil.isNotBlank(name), Dict::getName, name)
                .orderByDesc(Dict::getSort));
        return PageHelper.convertTo(dictPage, DictHelper::buildDictView);
    }

    @Override
    public void createDict(CreateDictRequest request) {
        validateTypeIsExist(request.getType());
        Dict dict = new Dict();
        dict.setName(request.getName());
        dict.setType(request.getType());
        dict.setStatus(request.getStatus());
        dict.setRemark(request.getRemark());
        dict.setCreateTime(new Date());
        dict.setSort(request.getSort());
        int insert = baseMapper.insert(dict);
        if (insert <= 0) {
            throw new ConflictException("Failed to create dict, dict={}", JSONObject.toJSONString(dict));
        }
    }

    @Override
    public void updateDict(UpdateDictRequest request) {
        Dict originDict = baseMapper.selectById(request.getId());
        if (Objects.isNull(originDict)) {
            throw new NotFoundException("Dict not found, id={}", request.getId());
        }
        if (!originDict.getType().equals(request.getType())) {
            validateTypeIsExist(request.getType());
        }
        Dict dictToUpdate = new Dict();
        dictToUpdate.setId(request.getId());
        dictToUpdate.setName(request.getName());
        dictToUpdate.setType(request.getType());
        dictToUpdate.setStatus(request.getStatus());
        dictToUpdate.setRemark(request.getRemark());
        dictToUpdate.setUpdateTime(new Date());
        dictToUpdate.setSort(request.getSort());

        int update = baseMapper.updateById(dictToUpdate);
        if (update <= 0) {
            throw new ConflictException("Failed to update dict, dict={}", JSONObject.toJSONString(dictToUpdate));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(Set<String> ids) {
        Long count = dictDataMapper.selectCount(new LambdaQueryWrapper<DictData>().in(DictData::getDictId, ids));
        if (count > 0) {
            throw new ConflictException("Dict delete failed, dict data exist！");
        }
        baseMapper.deleteBatchIds(ids);
    }


    public void validateTypeIsExist(String type) {
        Dict temp = baseMapper.selectOne(new LambdaQueryWrapper<Dict>().eq(Dict::getType, type).last(Constants.LIMIT_ONE));
        if (temp != null) {
            throw new ConflictException("Dict type exist!");
        }
    }

    @Override
    public List<DictView> listAvailableTypes(List<String> types) {
        if (ObjectUtil.isEmpty(types)) {
            return List.of();
        }

        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dict::getType, types)
                .eq(Dict::getStatus, YesOrNoEnum.YES.getCode());
        List<Dict> dictList = baseMapper.selectList(queryWrapper);
        return dictList.stream().map(DictHelper::buildDictView).toList();
    }
}
