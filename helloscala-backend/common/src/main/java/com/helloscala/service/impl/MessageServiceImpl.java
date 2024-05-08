package com.helloscala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Message;
import com.helloscala.mapper.MessageMapper;
import com.helloscala.service.MessageService;
import com.helloscala.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {


    /**
     * 留言列表
     * @param name
     * @return
     */
    @Override
    public ResponseResult selectMessagePage(String name) {
        LambdaQueryWrapper<Message> queryWrapper = new QueryWrapper<Message>().lambda()
                .like(StringUtils.isNotBlank(name),Message::getNickname,name).orderByDesc(Message::getCreateTime);
        Page<Message> list = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),queryWrapper);
        return ResponseResult.success(list);
    }

    /**
     * 删除留言
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteMessage(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

}
