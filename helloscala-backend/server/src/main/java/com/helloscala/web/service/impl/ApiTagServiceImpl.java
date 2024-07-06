package com.helloscala.web.service.impl;

import com.helloscala.common.mapper.TagsMapper;
import com.helloscala.common.vo.tag.ApiTagListVO;
import com.helloscala.web.service.ApiTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiTagServiceImpl implements ApiTagService {
    private final TagsMapper tagsMapper;

    @Override
    public List<ApiTagListVO> selectTagList() {
        return tagsMapper.selectTagListApi();
    }
}
