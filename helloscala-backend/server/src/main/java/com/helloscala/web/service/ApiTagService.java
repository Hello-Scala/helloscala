package com.helloscala.web.service;

import com.helloscala.common.vo.tag.ApiTagListVO;

import java.util.List;

public interface ApiTagService {
    public List<ApiTagListVO> selectTagList();
}
