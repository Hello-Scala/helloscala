package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.CozeFile;


public interface CozeFileService extends IService<CozeFile> {
    CozeFile getByFileName(String fileName);
}
