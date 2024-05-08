package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Say;

import java.util.List;


public interface SayService extends IService<Say> {


    /**
     * 说说分页列表
     * @param keywords
     * @return
     */
    ResponseResult selectSayPage(String keywords);

    /**
     * 添加说说
     * @param say
     * @return
     */
    ResponseResult addSay(Say say);

    /**
     * 删除说说
     * @param ids 说说id集合
     * @return
     */
    ResponseResult deleteSay(List<String> ids);

    /**
     * 说说详情
     * @param id
     * @return
     */
    ResponseResult selectSayById(String id);

    /**
     * 修改说说
     * @param say
     * @return
     */
    ResponseResult updateSay(Say say);
}
