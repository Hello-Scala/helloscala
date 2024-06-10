package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Comment;

import java.util.List;


public interface CommentService extends IService<Comment> {
    ResponseResult selectCommentPage(String keywords);

    ResponseResult deleteComment(List<Integer> ids);

}
