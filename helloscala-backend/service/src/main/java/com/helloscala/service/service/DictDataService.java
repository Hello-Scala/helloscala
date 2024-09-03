package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.DictData;
import com.helloscala.service.web.request.CreateDictDataRequest;
import com.helloscala.service.web.request.UpdateDictDataRequest;
import com.helloscala.service.web.view.DictDataView;

import java.util.List;
import java.util.Set;


public interface DictDataService extends IService<DictData> {
    Page<DictDataView> listByPage(Page<?> page, Integer dictId, Integer isPublish);

    void addDictData(CreateDictDataRequest request);

    void updateDictData(UpdateDictDataRequest request);

    void deleteDictData(List<String> ids);

    List<DictDataView> listAvailableByDictIds(Set<String> dictIds);
}
