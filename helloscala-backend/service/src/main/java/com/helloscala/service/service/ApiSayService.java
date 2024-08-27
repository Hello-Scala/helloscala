package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.entity.Say;
import com.helloscala.common.vo.say.ApiSayVO;

public interface ApiSayService {
    Page<ApiSayVO> selectSayList();

    void insertSay(Say say);

}
