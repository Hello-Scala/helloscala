package com.helloscala.service;

import com.helloscala.common.ResponseResult;

public interface ApiTagService {
    /**
     * 获取所有标签
     * @return
     */
    public ResponseResult selectTagList();

}
