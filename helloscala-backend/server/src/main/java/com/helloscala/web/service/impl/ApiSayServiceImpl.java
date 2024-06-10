package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.ResultCode;
import com.helloscala.common.entity.Say;
import com.helloscala.common.exception.BusinessException;
import com.helloscala.common.mapper.SayMapper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.say.ApiSayVO;
import com.helloscala.web.handle.RelativeDateFormat;
import com.helloscala.web.service.ApiSayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ApiSayServiceImpl implements ApiSayService {
    private final SayMapper sayMapper;


    @Override
    public ResponseResult selectSayList() {
        boolean showPrivate = StpUtil.isLogin() && StpUtil.hasRole(Constants.ADMIN_CODE);
        Page<ApiSayVO>  sayPage = sayMapper.selectPublicSayList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),showPrivate);
        sayPage.getRecords().forEach(item -> item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime())));
        return ResponseResult.success(sayPage);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertSay(Say say) {
        String userId = StpUtil.getLoginIdAsString();
        if (!userId.equals("1")) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }
        say.setUserId(userId);
        sayMapper.insert(say);
        return ResponseResult.success();
    }
}
