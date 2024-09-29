package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOUserLogView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.UserLogService;
import com.helloscala.service.web.view.UserLogView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author stevezou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOUserLogService {
    private final UserLogService userLogService;
    public Page<BOUserLogView> listByPage() {
        Page<?> page = PageUtil.getPage();
        Page<UserLogView> userLogPage = userLogService.selectUserLogPage(page);
        return PageHelper.convertTo(userLogPage, userLog-> {
            BOUserLogView userLogView = new BOUserLogView();
            userLogView.setId(userLog.getId());
            userLogView.setIp(userLog.getIp());
            userLogView.setAddress(userLog.getAddress());
            userLogView.setType(userLog.getType());
            userLogView.setDescription(userLog.getDescription());
            userLogView.setModel(userLog.getModel());
            userLogView.setAccessOs(userLog.getAccessOs());
            userLogView.setClientType(userLog.getClientType());
            userLogView.setBrowser(userLog.getBrowser());
            userLogView.setCreateTime(userLog.getCreateTime());
            userLogView.setResult(userLog.getResult());
            return userLogView;
        });
    }
    public void deleteBatch(String userId, List<String> ids) {
        userLogService.deleteUserLog(ids);
        log.info("userId={}, deleted UserLog ids=[{}]", userId, String.join(",", ids));
    }

}
