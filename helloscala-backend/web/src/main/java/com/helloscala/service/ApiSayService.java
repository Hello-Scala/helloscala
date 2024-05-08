package com.helloscala.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Say;

public interface ApiSayService {


    /**
     * 说说列表
     * @return
     */
    ResponseResult selectSayList();


    /**
     * 添加说说说
     * @param say
     * @return
     */
    ResponseResult insertSay(Say say);

}
