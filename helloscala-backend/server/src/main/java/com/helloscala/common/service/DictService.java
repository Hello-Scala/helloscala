package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Dict;

import java.util.List;


public interface DictService extends IService<Dict> {
    ResponseResult selectDictPage(String name, Integer status);

    ResponseResult addDict(Dict dict);

    ResponseResult updateDict(Dict dict);

    ResponseResult deleteDict(List<Long> list);
}
