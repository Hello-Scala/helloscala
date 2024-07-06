package com.helloscala.web.service;

import com.helloscala.common.entity.Software;

import java.util.List;

public interface ApiSoftwareService {
    List<Software> selectSoftwareList();
}
