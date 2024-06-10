package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Say;

public interface ApiSayService {
    ResponseResult selectSayList();

    ResponseResult insertSay(Say say);

}
