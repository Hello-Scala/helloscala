package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.common.entity.Sign;
import com.helloscala.common.mapper.SignMapper;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.web.service.ApiSignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiSignServiceImpl implements ApiSignService {
    private final SignMapper signMapper;

    @Override
    public List<String> getSignRecords(String startTime, String endTime) {
        return signMapper.getSignRecordsByUserId(startTime, endTime, StpUtil.getLoginIdAsString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sign(String time) {
        String userId = StpUtil.getLoginIdAsString();

        Sign sign = signMapper.selctSignByUserIdAndTime(userId, time);
        if (sign != null) {
            throw new ConflictException("You've already signed, no need sign again!");
        }

        sign = Sign.builder().userId(userId).createTime(DateUtil.strToDateTime(time, DateUtil.YYYY_MM_DD)).build();
        signMapper.insert(sign);
    }

    @Override
    public Sign validateTodayIsSign() {
        String today = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYY_MM_DD);
        return signMapper.validateTodayIsSign(today, StpUtil.getLoginIdAsString());
    }
}
