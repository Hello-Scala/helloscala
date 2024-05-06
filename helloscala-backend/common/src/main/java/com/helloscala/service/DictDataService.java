package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.DictData;

import java.util.List;


public interface DictDataService extends IService<DictData> {

    /**
     * 分页
     * @param dictId
     * @param isPublish
     * @return
     */
    ResponseResult selectDictDataPage(Integer dictId, Integer isPublish);

    /**
     * 添加
     * @param dictData
     * @return
     */
    ResponseResult addDictData(DictData dictData);

    /**
     * 修改
     * @param dictData
     * @return
     */
    ResponseResult updateDictData(DictData dictData);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteDictData(List<Long> ids);


    /**
     * 根绝字典类型集合获取字典数据
     * @param types
     * @return
     */
    ResponseResult getDataByDictType(List<String> types);

}
