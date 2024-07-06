package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.Dict;

import java.util.List;


public interface DictService extends IService<Dict> {
    Page<Dict> selectDictPage(String name, Integer status);

    void addDict(Dict dict);

    void updateDict(Dict dict);

    void deleteDict(List<Long> list);
}
