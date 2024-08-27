package com.helloscala.service.service;

import com.helloscala.service.entity.Software;
import com.helloscala.service.mapper.SoftwareMapper;
import com.helloscala.web.service.ApiSoftwareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiSoftwareServiceImpl implements ApiSoftwareService {
    private final SoftwareMapper softwareMapper;

    @Override
    public List<Software> selectSoftwareList() {
        return softwareMapper.selectList(null);
    }
}
