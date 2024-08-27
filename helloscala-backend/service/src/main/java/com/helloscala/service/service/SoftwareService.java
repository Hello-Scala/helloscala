package com.helloscala.service.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Software;

import java.util.List;

public interface SoftwareService  extends IService<Software> {
    Page<Software> selectSoftwareList();

    Software selectSoftwareById(Integer id);

    void saveSoftware(Software bSoftware);

    void updateSoftware(Software bSoftware);

    void removeSoftwareByIds(List<Integer> ids);
}
