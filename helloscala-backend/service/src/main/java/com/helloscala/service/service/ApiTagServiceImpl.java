package com.helloscala.service.service;

import com.helloscala.service.mapper.TagMapper;
import com.helloscala.common.vo.tag.ApiTagListVO;
import com.helloscala.web.service.ApiTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiTagServiceImpl implements ApiTagService {
    private final TagMapper tagMapper;

    @Override
    public List<ApiTagListVO> list() {
        return tagMapper.selectTagListApi();
    }
}
