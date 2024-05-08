package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Dict;

import java.util.List;


public interface DictService extends IService<Dict> {

    /**
     * 分页
     * @param name
     * @param status
     * @return
     */
    ResponseResult selectDictPage(String name, Integer status);

    /**
     * 添加
     * @param dict
     * @return
     */
    ResponseResult addDict(Dict dict);

    /**
     * 修改
     * @param dict
     * @return
     */
    ResponseResult updateDict(Dict dict);

    /**
     * 删除
     * @param list
     * @return
     */
    ResponseResult deleteDict(List<Long> list);

}
