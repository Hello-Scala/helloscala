package com.helloscala.service.impl;

import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Software;
import com.helloscala.mapper.SoftwareMapper;
import com.helloscala.service.ApiSoftwareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiSoftwareServiceImpl implements ApiSoftwareService {

    private final SoftwareMapper softwareMapper;

    @Override
    public ResponseResult selectSoftwareList() {
        List<Software> softwares = softwareMapper.selectList(null);
        return ResponseResult.success(softwares);
    }
}
