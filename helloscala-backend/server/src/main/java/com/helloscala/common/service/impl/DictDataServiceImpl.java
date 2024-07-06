package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResultCode;
import com.helloscala.common.dto.dict.DictView;
import com.helloscala.common.entity.Dict;
import com.helloscala.common.entity.DictData;
import com.helloscala.common.enums.YesOrNoEnum;
import com.helloscala.common.mapper.DictDataMapper;
import com.helloscala.common.mapper.DictMapper;
import com.helloscala.common.service.DictDataService;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.web.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {
    @Lazy
    private final DictMapper dictMapper;

    @Override
    public Page<DictData> selectDictDataPage(Integer dictId, Integer isPublish) {
        LambdaQueryWrapper<DictData> dictDataQuery = new LambdaQueryWrapper<>();
        dictDataQuery.eq(DictData::getDictId, dictId)
                .eq(isPublish != null, DictData::getStatus, isPublish);
        Page<DictData> disctDatePage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), dictDataQuery);

        Set<Long> dictIdSet = disctDatePage.getRecords().stream().map(DictData::getDictId).collect(Collectors.toSet());

        List<Dict> dictList = dictMapper.selectBatchIds(dictIdSet);
        Map<Long, Dict> dictMap = dictList.stream().collect(Collectors.toMap(Dict::getId, Function.identity()));

        disctDatePage.getRecords().forEach(item -> item.setDict(dictMap.get(item.getDictId())));
        return disctDatePage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDictData(DictData dictData) {
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictData::getLabel, dictData.getLabel())
                .eq(DictData::getDictId, dictData.getDictId());
        DictData temp = baseMapper.selectOne(queryWrapper);
        if (temp != null) {
            throw new ConflictException(ResultCode.TAG_EXIST.desc);
        }
        baseMapper.insert(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictData(DictData sysDictData) {
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictData::getLabel, sysDictData.getLabel());
        DictData dictData = baseMapper.selectOne(queryWrapper);
        if (dictData != null && !dictData.getId().equals(sysDictData.getId())) {
            throw new ConflictException(ResultCode.TAG_EXIST.desc);
        }
        baseMapper.updateById(sysDictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictData(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }


    @Override
    public Map<String, Map<String, Object>> getDataByDictType(List<String> types) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dict::getType, types)
                .eq(Dict::getStatus, YesOrNoEnum.YES.getCode());
        List<Dict> dictList = dictMapper.selectList(queryWrapper);

        Set<Long> dictIdSet = dictList.stream().map(Dict::getId).collect(Collectors.toSet());
        LambdaQueryWrapper<DictData> sysDictDataQueryWrapper = new LambdaQueryWrapper<DictData>();
        sysDictDataQueryWrapper.eq(DictData::getStatus, YesOrNoEnum.YES.getCode());
        sysDictDataQueryWrapper.in(DictData::getDictId, dictIdSet);
        sysDictDataQueryWrapper.orderByAsc(DictData::getSort);

        List<DictData> dataList = baseMapper.selectList(sysDictDataQueryWrapper);
        Map<Long, List<DictData>> dictDataMap = dataList.stream().collect(Collectors.groupingBy(DictData::getDictId));

        Map<String, Map<String, Object>> map = new HashMap<>();
        dictList.forEach(item -> {
            List<DictData> itemValueList = dictDataMap.get(item.getId());
            String defaultValue = itemValueList.stream().filter(d -> YesOrNoEnum.YES.getCodeToString().equals(d.getIsDefault()))
                    .findFirst().map(DictData::getValue).orElse(null);
            Map<String, Object> result = new HashMap<>();
            result.put("defaultValue", defaultValue);
            result.put("list", itemValueList);
            map.put(item.getType(), result);
        });
        return map;
    }

    @Override
    public List<DictView> getDataByDictTypeV2(List<String> types) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dict::getType, types)
                .eq(Dict::getStatus, YesOrNoEnum.YES.getCode());
        List<Dict> dictList = dictMapper.selectList(queryWrapper);

        Set<Long> dictIdSet = dictList.stream().map(Dict::getId).collect(Collectors.toSet());
        LambdaQueryWrapper<DictData> sysDictDataQueryWrapper = new LambdaQueryWrapper<DictData>();
        sysDictDataQueryWrapper.eq(DictData::getStatus, YesOrNoEnum.YES.getCode());
        sysDictDataQueryWrapper.in(DictData::getDictId, dictIdSet);
        sysDictDataQueryWrapper.orderByAsc(DictData::getSort);

        List<DictData> dataList = baseMapper.selectList(sysDictDataQueryWrapper);
        Map<Long, List<DictData>> dictDataMap = dataList.stream().collect(Collectors.groupingBy(DictData::getDictId));

        return dictList.stream().map(item -> {
            List<DictData> itemValueList = dictDataMap.get(item.getId());
            DictData defaultValue = itemValueList.stream().filter(d -> YesOrNoEnum.YES.getCodeToString().equals(d.getIsDefault()))
                    .findFirst().orElse(null);

            DictView dictView = new DictView();
            dictView.setName(item.getName());
            dictView.setType(item.getType());
            dictView.setDefaultValue(defaultValue);
            dictView.setValues(itemValueList);
            return dictView;
        }).toList();
    }
}
