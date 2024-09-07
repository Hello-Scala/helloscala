package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.Dict;
import com.helloscala.service.entity.DictData;
import com.helloscala.service.enums.YesOrNoEnum;
import com.helloscala.service.mapper.DictDataMapper;
import com.helloscala.service.mapper.DictMapper;
import com.helloscala.service.service.DictDataService;
import com.helloscala.service.service.util.DictHelper;
import com.helloscala.service.web.request.CreateDictDataRequest;
import com.helloscala.service.web.request.UpdateDictDataRequest;
import com.helloscala.service.web.view.DictDataView;
import com.helloscala.service.web.view.DictView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {
    @Lazy
    private final DictMapper dictMapper;

    @Override
    public Page<DictDataView> listByPage(Page<?> page, String dictId, Integer isPublish) {
        LambdaQueryWrapper<DictData> dictDataQuery = new LambdaQueryWrapper<>();
        dictDataQuery.eq(DictData::getDictId, dictId)
                .eq(isPublish != null, DictData::getStatus, isPublish);
        Page<DictData> dictDatePage = baseMapper.selectPage(PageHelper.of(page), dictDataQuery);

        Set<String> dictIdSet = dictDatePage.getRecords().stream().map(DictData::getDictId).collect(Collectors.toSet());

        List<Dict> dictList = dictMapper.selectBatchIds(dictIdSet);
        Map<String, Dict> dictMap = dictList.stream().collect(Collectors.toMap(Dict::getId, Function.identity()));

        dictDatePage.getRecords().forEach(item -> item.setDict(dictMap.get(item.getDictId())));
        return PageHelper.convertTo(dictDatePage, dictData -> {
            DictView dictView = Optional.of(dictMap.get(dictData.getDictId()))
                    .map(DictHelper::buildDictView).orElse(null);
            return DictHelper.buildDictDataView(dictData, dictView);
        });
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDictData(CreateDictDataRequest request) {
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictData::getLabel, request.getLabel())
                .eq(DictData::getDictId, request.getDictId());
        DictData temp = baseMapper.selectOne(queryWrapper);
        if (Objects.nonNull(temp)) {
            throw new ConflictException("Dict data exist, label={}, dictId={}",
                    request.getLabel(), request.getDictId());
        }

        DictData dictData = new DictData();
        dictData.setDictId(request.getDictId());
        dictData.setLabel(request.getLabel());
        dictData.setValue(request.getValue());
        dictData.setStyle(request.getStyle());
        dictData.setIsDefault(request.getIsDefault());
        dictData.setSort(request.getSort());
        dictData.setStatus(request.getStatus());
        dictData.setRemark(request.getRemark());
        int insert = baseMapper.insert(dictData);
        if (insert <= 0) {
            throw new ConflictException("Failed to add dictData, label={}, dictId={}",
                    request.getLabel(), request.getDictId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictData(UpdateDictDataRequest request) {
        Dict dict = dictMapper.selectById(request.getId());
        if (Objects.isNull(dict)) {
            throw new NotFoundException("Dict not found, dictId={}", request.getDictId());
        }
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictData::getLabel, request.getLabel());
        DictData existDictData = baseMapper.selectOne(queryWrapper);
        if (Objects.nonNull(existDictData) && !existDictData.getId().equals(request.getId())) {
            throw new ConflictException("Failed to update dictData, incorrect dictId, label={}, request dictId={}, real dictId={}",
                    request.getLabel(), request.getDictId(), existDictData.getId());
        }
        DictData dictData = new DictData();
        dictData.setId(existDictData.getId());
        dictData.setDictId(request.getDictId());
        dictData.setLabel(request.getLabel());
        dictData.setValue(request.getValue());
        dictData.setStyle(request.getStyle());
        dictData.setIsDefault(request.getIsDefault());
        dictData.setSort(request.getSort());
        dictData.setStatus(request.getStatus());
        dictData.setRemark(request.getRemark());
        int update = baseMapper.updateById(dictData);
        if (update <= 0) {
            throw new ConflictException("Failed to update dictData, label={}, dictId={}",
                    request.getLabel(), request.getDictId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictData(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<DictDataView> listAvailableByDictIds(Set<String> dictIds) {
        if (ObjectUtil.isEmpty(dictIds)) {
            return List.of();
        }

        LambdaQueryWrapper<DictData> sysDictDataQueryWrapper = new LambdaQueryWrapper<DictData>();
        sysDictDataQueryWrapper.eq(DictData::getStatus, YesOrNoEnum.YES.getCode());
        sysDictDataQueryWrapper.in(DictData::getDictId, dictIds);
        sysDictDataQueryWrapper.orderByAsc(DictData::getSort);

        List<DictData> dataList = baseMapper.selectList(sysDictDataQueryWrapper);
        return dataList.stream().map(DictHelper::buildDictDataView).toList();
    }
}
