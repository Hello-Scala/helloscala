package com.helloscala.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Software;

import java.util.List;

public interface SoftwareService  extends IService<Software> {
    ResponseResult selectSoftwareList();

    ResponseResult selectSoftwareById(Integer id);

    ResponseResult saveSoftware(Software bSoftware);

    ResponseResult updateSoftware(Software bSoftware);

    ResponseResult removeSoftwareByIds(List<Integer> ids);
}
