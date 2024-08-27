package com.helloscala.service.service;

import com.helloscala.service.entity.Software;

import java.util.List;

public interface ApiSoftwareService {
    List<Software> selectSoftwareList();
}
