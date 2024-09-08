package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOExceptionLogView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.ExceptionLogService;
import com.helloscala.service.web.view.ExceptionLogView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOExceptionLogService {
    private final ExceptionLogService exceptionLogService;

    public Page<BOExceptionLogView> listByPage() {
        Page<?> page = PageUtil.getPage();
        Page<ExceptionLogView> exceptionLogViewPage = exceptionLogService.listByPage(page);
        return PageHelper.convertTo(exceptionLogViewPage, exceptionLogView -> {
            BOExceptionLogView boExceptionLogView = new BOExceptionLogView();
            boExceptionLogView.setId(exceptionLogView.getId());
            boExceptionLogView.setUsername(exceptionLogView.getUsername());
            boExceptionLogView.setIp(exceptionLogView.getIp());
            boExceptionLogView.setIpSource(exceptionLogView.getIpSource());
            boExceptionLogView.setMethod(exceptionLogView.getMethod());
            boExceptionLogView.setOperation(exceptionLogView.getOperation());
            boExceptionLogView.setParams(exceptionLogView.getParams());
            boExceptionLogView.setExceptionJson(exceptionLogView.getExceptionJson());
            boExceptionLogView.setExceptionMessage(exceptionLogView.getExceptionMessage());
            boExceptionLogView.setCreateTime(exceptionLogView.getCreateTime());
            return boExceptionLogView;
        });
    }

    public void deleteExceptionLog(String userId, Set<String> ids) {
        exceptionLogService.deleteExceptionLog(ids);
        log.info("userId={}, deleted exception log ids=[{}]", userId, String.join(",", ids));
    }
}
