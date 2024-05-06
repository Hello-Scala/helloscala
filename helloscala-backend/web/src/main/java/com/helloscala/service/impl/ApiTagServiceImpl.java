package com.helloscala.service.impl;

import com.helloscala.common.ResponseResult;
import com.helloscala.mapper.TagsMapper;
import com.helloscala.service.ApiTagService;
import com.helloscala.vo.tag.ApiTagListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiTagServiceImpl implements ApiTagService {

    private final TagsMapper tagsMapper;

    /**
     *  标签列表
     * @return
     */
    @Override
    public ResponseResult selectTagList() {
        List<ApiTagListVO> list = tagsMapper.selectTagListApi();
        return ResponseResult.success(list);
    }
}
