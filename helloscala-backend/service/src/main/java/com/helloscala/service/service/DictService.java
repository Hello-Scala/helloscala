package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Dict;
import com.helloscala.service.web.request.CreateDictRequest;
import com.helloscala.service.web.request.UpdateDictRequest;
import com.helloscala.service.web.view.DictView;

import java.util.List;
import java.util.Set;


public interface DictService extends IService<Dict> {
    Page<DictView> listByPage(Page<?> page, String name, String status);

    void createDict(CreateDictRequest request);

    void updateDict(UpdateDictRequest request);

    void deleteDict(Set<String> ids);

    List<DictView> listAvailableTypes(List<String> types);
}
