package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Dict;
import com.helloscala.service.web.view.DictView;

import java.util.List;


public interface DictService extends IService<Dict> {
    Page<Dict> selectDictPage(String name, Integer status);

    void addDict(Dict dict);

    void updateDict(Dict dict);

    void deleteDict(List<Long> list);

    List<DictView> listAvailableTypes(List<String> types);
}
