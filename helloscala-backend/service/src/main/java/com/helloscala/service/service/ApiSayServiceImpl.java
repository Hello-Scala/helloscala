package com.helloscala.service.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.Constants;
import com.helloscala.common.ResultCode;
import com.helloscala.service.entity.Say;
import com.helloscala.service.mapper.SayMapper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.say.ApiSayVO;
import com.helloscala.common.web.exception.ForbiddenException;
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
    public Page<ApiSayVO> selectSayList() {
        boolean showPrivate = StpUtil.isLogin() && StpUtil.hasRole(Constants.ADMIN_CODE);
        Page<Object> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<ApiSayVO> sayPage = sayMapper.selectPublicSayList(page, showPrivate);
        sayPage.getRecords().forEach(item -> item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime())));
        return sayPage;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSay(Say say) {
        String userId = StpUtil.getLoginIdAsString();
        if (!userId.equals("1")) {
            throw new ForbiddenException(ResultCode.NO_PERMISSION.desc);
        }
        say.setUserId(userId);
        sayMapper.insert(say);
    }
}
