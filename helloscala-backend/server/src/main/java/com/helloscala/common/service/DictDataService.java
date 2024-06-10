package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.DictData;

import java.util.List;


public interface DictDataService extends IService<DictData> {
    ResponseResult selectDictDataPage(Integer dictId, Integer isPublish);

    ResponseResult addDictData(DictData dictData);

    ResponseResult updateDictData(DictData dictData);

    ResponseResult deleteDictData(List<Long> ids);

    ResponseResult getDataByDictType(List<String> types);
}
