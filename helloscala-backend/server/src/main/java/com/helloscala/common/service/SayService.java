package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Say;

import java.util.List;


public interface SayService extends IService<Say> {
    ResponseResult selectSayPage(String keywords);

    ResponseResult addSay(Say say);

    ResponseResult deleteSay(List<String> ids);

    ResponseResult selectSayById(String id);

    ResponseResult updateSay(Say say);
}
