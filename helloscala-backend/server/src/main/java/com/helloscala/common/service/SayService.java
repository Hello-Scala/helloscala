package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.Say;

import java.util.List;


public interface SayService extends IService<Say> {
    Page<Say> selectSayPage(String keywords);

    void addSay(Say say);

    void deleteSay(List<String> ids);

    Say selectSayById(String id);

    void updateSay(Say say);
}
