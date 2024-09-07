package com.helloscala.admin.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateDictRequest;
import com.helloscala.admin.controller.request.BOUpdateDictRequest;
import com.helloscala.admin.controller.view.BODictView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.DictService;
import com.helloscala.service.web.request.CreateDictRequest;
import com.helloscala.service.web.request.UpdateDictRequest;
import com.helloscala.service.web.view.DictView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BODictService {
    private final DictService dictService;

    public Page<BODictView> listDictByPage(String name, String status) {
        Page<?> page = PageUtil.getPage();
        Page<DictView> dictPage = dictService.listByPage(page, name, status);
        return PageHelper.convertTo(dictPage, BODictService::buildBoDictView);
    }

    public void create(String userId, BOCreateDictRequest request) {
        CreateDictRequest createDictRequest = new CreateDictRequest();
        createDictRequest.setName(request.getName());
        createDictRequest.setType(request.getType());
        createDictRequest.setStatus(request.getStatus());
        createDictRequest.setRemark(request.getRemark());
        createDictRequest.setSort(request.getSort());
        createDictRequest.setRequestBy(userId);
        dictService.createDict(createDictRequest);
    }

    public void update(String userId, BOUpdateDictRequest request) {
        UpdateDictRequest updateDictRequest = new UpdateDictRequest();
        updateDictRequest.setId(request.getId());
        updateDictRequest.setName(request.getName());
        updateDictRequest.setType(request.getType());
        updateDictRequest.setStatus(request.getStatus());
        updateDictRequest.setRemark(request.getRemark());
        updateDictRequest.setSort(request.getSort());
        updateDictRequest.setRequestBy(userId);
        dictService.updateDict(updateDictRequest);
    }


    public void deleteDictData(String userId, Set<String> ids) {
        dictService.deleteDict(ids);
        log.info("userId={}, deleted dict ids=[{}]", userId, String.join(",", ids));
    }

    private static @NotNull BODictView buildBoDictView(DictView dict) {
        BODictView boDictView = new BODictView();
        boDictView.setId(dict.getId());
        boDictView.setName(dict.getName());
        boDictView.setType(dict.getType());
        boDictView.setStatus(dict.getStatus());
        boDictView.setRemark(dict.getRemark());
        boDictView.setCreateTime(dict.getCreateTime());
        boDictView.setUpdateTime(dict.getUpdateTime());
        boDictView.setSort(dict.getSort());
        return boDictView;
    }
}
