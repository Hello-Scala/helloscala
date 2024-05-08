package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Comment;

import java.util.List;


public interface CommentService extends IService<Comment> {

    /**
     * 分页
     * @param keywords
     * @return
     */
    ResponseResult selectCommentPage(String keywords);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteComment(List<Integer> ids);

}
