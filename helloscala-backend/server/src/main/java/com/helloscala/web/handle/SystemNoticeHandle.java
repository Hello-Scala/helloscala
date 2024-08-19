package com.helloscala.web.handle;

import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.common.entity.ImMessage;
import com.helloscala.common.mapper.ImMessageMapper;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SystemNoticeHandle {
    public static void sendNotice(String toUserId, Integer noticeType, Integer noticeCode, String articleId, Integer commentMark, String content) {
        ImMessageMapper imMessageMapper = SpringUtil.getBean(ImMessageMapper.class);
        try {
            String ip = IpUtil.getIp();

            ImMessage message = ImMessage.builder().fromUserId(StpUtil.getLoginIdAsString()).toUserId(toUserId).content(content).commentMark(commentMark)
                    .noticeType(noticeType).code(noticeCode).ip(ip).ipSource(IpUtil.getIp2region(ip)).articleId(articleId).build();
            imMessageMapper.insert(message);
        } catch (Exception e) {
            log.error("Failed to send notification to userId={}!", toUserId, e);
        }
    }
}
