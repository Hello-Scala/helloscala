package com.helloscala.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.ResultCode;
import com.helloscala.entity.Say;
import com.helloscala.exception.BusinessException;
import com.helloscala.handle.RelativeDateFormat;
import com.helloscala.mapper.SayMapper;
import com.helloscala.service.ApiSayService;
import com.helloscala.utils.PageUtil;
import com.helloscala.vo.say.ApiSayVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ApiSayServiceImpl implements ApiSayService {

    private final SayMapper sayMapper;


    @Override
    public ResponseResult selectSayList() {

        //是否显示未公开的说说 登录用户id为1时显示所有说说
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
