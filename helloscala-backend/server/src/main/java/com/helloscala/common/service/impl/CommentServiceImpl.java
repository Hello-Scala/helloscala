package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.Comment;
import com.helloscala.common.mapper.CommentMapper;
import com.helloscala.common.service.CommentService;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.message.SystemCommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Override
    public Page<SystemCommentVO> selectCommentPage(String keywords) {
        return baseMapper.selectPageList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), keywords);
    }

    @Override
    public void deleteComment(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }
}
