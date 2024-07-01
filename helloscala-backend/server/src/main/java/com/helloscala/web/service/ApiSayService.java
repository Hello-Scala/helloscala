package com.helloscala.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Say;
import com.helloscala.common.vo.say.ApiSayVO;

public interface ApiSayService {
    Page<ApiSayVO> selectSayList();

    void insertSay(Say say);

}
