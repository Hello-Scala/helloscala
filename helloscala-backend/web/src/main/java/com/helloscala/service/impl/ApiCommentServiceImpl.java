package com.helloscala.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Article;
import com.helloscala.entity.Comment;
import com.helloscala.entity.User;
import com.helloscala.exception.BusinessException;
import com.helloscala.handle.RelativeDateFormat;
import com.helloscala.handle.SystemNoticeHandle;
import com.helloscala.im.MessageConstant;
import com.helloscala.mapper.ArticleMapper;
import com.helloscala.mapper.CommentMapper;
import com.helloscala.mapper.UserMapper;
import com.helloscala.service.ApiCommentService;
import com.helloscala.utils.HTMLUtil;
import com.helloscala.utils.IpUtil;
import com.helloscala.utils.PageUtil;
import com.helloscala.vo.article.ApiArticleListVO;
import com.helloscala.vo.message.ApiCommentListVO;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCommentServiceImpl implements ApiCommentService {

    private final CommentMapper commentMapper;

    private final UserMapper userMapper;

    private final ArticleMapper articleMapper;

    /**
     * 发表评论
     * @param comment
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addComment(Comment comment) {
        UserAgent userAgent = UserAgent.parseUserAgentString(IpUtil.getRequest().getHeader("user-agent"));
        //获取ip地址
        String ip = IpUtil.getIp();
        String ipAddress = IpUtil.getIp2region(ip);
        String os = userAgent.getOperatingSystem().getName();
        if (os.contains("mac") || os.contains("Mac")) {
            comment.setSystem("mac");
        } else if (os.contains("Windows")) {
            comment.setSystem("windowns");
        }else {
            comment.setSystem("android");
        }
        //过滤内容 如删除html标签和敏感词替换
        String content = HTMLUtil.deleteTag(comment.getContent());
        comment.setContent(content);
        comment.setSystemVersion(os);
        comment.setIpAddress(ipAddress);
        comment.setUserId(StpUtil.getLoginIdAsString());
        int insert = commentMapper.insert(comment);
        if (insert == 0){
            throw new BusinessException("Comment failed!");
        }
        String toUserId =  comment.getReplyUserId();
        int mark = toUserId == null ? 2 : 1;
        if (toUserId == null) {
            Article article = articleMapper.selectById(comment.getArticleId());
            toUserId =  article.getUserId();
        }
        SystemNoticeHandle.sendNotice(toUserId, MessageConstant.MESSAGE_COMMENT_NOTICE, MessageConstant.SYSTEM_MESSAGE_CODE, comment.getArticleId(), mark, comment.getContent());
        return ResponseResult.success(comment);
    }

    @Override
    public ResponseResult selectCommentByArticleId(Long articleId) {
        //获取评论父级评论
        Page<ApiCommentListVO> pageList = commentMapper.selectCommentPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),articleId);
        //获取子级
        for (ApiCommentListVO vo : pageList.getRecords()) {
            List<Comment> commentList = commentMapper.selectList(
                    new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, vo.getId()).orderByDesc(Comment::getCreateTime));
            for (Comment e : commentList) {
                User replyUserInfo = userMapper.selectById(e.getReplyUserId());
                User userInfo1 = userMapper.selectById(e.getUserId());
                ApiCommentListVO apiCommentListVO = ApiCommentListVO.builder()
                        .id(e.getId())
                        .userId(e.getUserId())
                        .replyUserId(e.getReplyUserId())
                        .nickname(userInfo1.getNickname())
                        .webSite(userInfo1.getWebSite())
                        .replyNickname(replyUserInfo.getNickname())
                        .replyWebSite(replyUserInfo.getWebSite())
                        .content(e.getContent())
                        .avatar(userInfo1.getAvatar())
                        .createTimeStr(RelativeDateFormat.format(e.getCreateTime()))
                        .browser(e.getBrowser())
                        .browserVersion(e.getBrowserVersion())
                        .system(e.getSystem())
                        .systemVersion(e.getSystemVersion())
                        .ipAddress(e.getIpAddress())
                        .build();
                vo.getChildren().add(apiCommentListVO);
            }
            vo.setCreateTimeStr(RelativeDateFormat.format(vo.getCreateTime()));
        }
        return ResponseResult.success(pageList);
    }

    /**
     * 获取我的评论
     * @return
     */
    @Override
    public ResponseResult selectMyComment() {
        Page<ApiArticleListVO> result  = commentMapper.selectMyComment(new Page<ApiArticleListVO>(PageUtil.getPageNo(), PageUtil.getPageSize()),StpUtil.getLoginIdAsString());
        return ResponseResult.success(result);
    }
}
