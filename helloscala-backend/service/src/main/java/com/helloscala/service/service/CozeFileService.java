package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.CozeFile;


public interface CozeFileService extends IService<CozeFile> {
    CozeFile getByFileName(String fileName);
}
