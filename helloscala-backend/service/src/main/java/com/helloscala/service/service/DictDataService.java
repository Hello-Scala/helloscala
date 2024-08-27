package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.dto.dict.DictView;
import com.helloscala.service.entity.DictData;

import java.util.List;
import java.util.Map;


public interface DictDataService extends IService<DictData> {
    Page<DictData> selectDictDataPage(Integer dictId, Integer isPublish);

    void addDictData(DictData dictData);

    void updateDictData(DictData dictData);

    void deleteDictData(List<Long> ids);

    @Deprecated
    Map<String, Map<String, Object>> getDataByDictType(List<String> types);

    List<DictView> getDataByDictTypeV2(List<String> types);

}
