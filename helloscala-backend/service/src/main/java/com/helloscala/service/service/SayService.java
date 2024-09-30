package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Say;
import com.helloscala.service.web.request.CreateSayRequest;
import com.helloscala.service.web.request.UpdateSayRequest;
import com.helloscala.service.web.view.SayView;

import java.util.Set;


public interface SayService extends IService<Say> {
    Page<SayView> selectSayPage(Page<?> page, String keywords);

    void addSay(CreateSayRequest request);

    void deleteSay(Set<String> ids);

    SayView selectSayById(String id);

    void updateSay(UpdateSayRequest request);
}
