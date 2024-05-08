package com.helloscala.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Comment;
import com.helloscala.mapper.CommentMapper;
import com.helloscala.service.CommentService;
import com.helloscala.utils.PageUtil;
import com.helloscala.vo.message.SystemCommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    /**
     * 评论列表
     *
     * @param keywords
     * @return
     */
    @Override
    public ResponseResult selectCommentPage(String keywords) {
        Page<SystemCommentVO> dtoPage = baseMapper.selectPageList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), keywords);
        return ResponseResult.success(dtoPage);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseResult deleteComment(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

}
