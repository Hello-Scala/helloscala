package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.dto.dict.DictView;
import com.helloscala.service.entity.DictData;
import com.helloscala.service.web.request.CreateDictDataRequest;
import com.helloscala.service.web.request.UpdateDictDataRequest;
import com.helloscala.service.web.view.DictDataView;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface DictDataService extends IService<DictData> {
    Page<DictDataView> selectDictDataPage(Page<?> page, Integer dictId, Integer isPublish);

    void addDictData(CreateDictDataRequest request);

    void updateDictData(UpdateDictDataRequest request);

    void deleteDictData(List<Long> ids);

    @Deprecated
    Map<String, Map<String, Object>> getDataByDictType(List<String> types);

    List<DictView> getDataByDictTypeV2(List<String> types);

    List<DictDataView> listAvailableByDictIds(Set<String> dictIds);
}
