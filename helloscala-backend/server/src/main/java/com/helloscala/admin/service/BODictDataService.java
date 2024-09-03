package com.helloscala.admin.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateDictDataRequest;
import com.helloscala.admin.controller.request.BOUpdateDictDataRequest;
import com.helloscala.admin.controller.view.BODictDataView;
import com.helloscala.admin.controller.view.BODictView;
import com.helloscala.admin.controller.view.BOListByTypeDictView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.enums.YesOrNoEnum;
import com.helloscala.service.service.DictDataService;
import com.helloscala.service.service.DictService;
import com.helloscala.service.web.request.CreateDictDataRequest;
import com.helloscala.service.web.request.UpdateDictDataRequest;
import com.helloscala.service.web.view.DictDataView;
import com.helloscala.service.web.view.DictView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BODictDataService {
    private final DictService dictService;
    private final DictDataService dictDataService;

    public Page<BODictDataView> listDictDataByPage(Integer dictId, Integer isPublish) {
        Page<?> page = PageUtil.getPage();
        Page<DictDataView> dictDataViewPage = dictDataService.listByPage(page, dictId, isPublish);
        return PageHelper.convertTo(dictDataViewPage, BODictDataService::buildBoDictDataView);
    }

    private static @NotNull BODictDataView buildBoDictDataView(DictDataView dictDataView) {
        Optional<BODictView> boDictViewOptional = Optional.ofNullable(dictDataView.getDict()).map(dict -> {
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
        });

        BODictDataView boDictDataView = new BODictDataView();
        boDictDataView.setId(dictDataView.getId());
        boDictDataView.setDictId(dictDataView.getDictId());
        boDictDataView.setLabel(dictDataView.getLabel());
        boDictDataView.setValue(dictDataView.getValue());
        boDictDataView.setStyle(dictDataView.getStyle());
        boDictDataView.setIsDefault(dictDataView.getIsDefault());
        boDictDataView.setSort(dictDataView.getSort());
        boDictDataView.setStatus(dictDataView.getStatus());
        boDictDataView.setRemark(dictDataView.getRemark());
        boDictDataView.setDict(boDictViewOptional.orElse(null));
        return boDictDataView;
    }

    public void create(String userId, BOCreateDictDataRequest request) {
        CreateDictDataRequest createDictDataRequest = new CreateDictDataRequest();
        createDictDataRequest.setDictId(request.getDictId());
        createDictDataRequest.setLabel(request.getLabel());
        createDictDataRequest.setValue(request.getValue());
        createDictDataRequest.setStyle(request.getStyle());
        createDictDataRequest.setIsDefault(request.getIsDefault());
        createDictDataRequest.setSort(request.getSort());
        createDictDataRequest.setStatus(request.getStatus());
        createDictDataRequest.setRemark(request.getRemark());
        createDictDataRequest.setRequestBy(userId);
        dictDataService.addDictData(createDictDataRequest);
    }

    public void update(String userId, BOUpdateDictDataRequest request) {
        UpdateDictDataRequest updateDictDataRequest = new UpdateDictDataRequest();
        updateDictDataRequest.setId(request.getId());
        updateDictDataRequest.setDictId(request.getDictId());
        updateDictDataRequest.setLabel(request.getLabel());
        updateDictDataRequest.setValue(request.getValue());
        updateDictDataRequest.setStyle(request.getStyle());
        updateDictDataRequest.setIsDefault(request.getIsDefault());
        updateDictDataRequest.setSort(request.getSort());
        updateDictDataRequest.setStatus(request.getStatus());
        updateDictDataRequest.setRemark(request.getRemark());
        updateDictDataRequest.setRequestBy(userId);
        dictDataService.updateDictData(updateDictDataRequest);
    }

    public void deleteDictData(String userId, List<String> ids) {
        dictDataService.deleteDictData(ids);
        log.info("userId={}, deleted dictData ids=[{}]", userId, String.join(",", ids));
    }


    public Map<String, Map<String, Object>> getDataByDictType(List<String> types) {
        List<DictView> dictList = dictService.listAvailableTypes(types);

        Set<String> dictIdSet = dictList.stream().map(DictView::getId).collect(Collectors.toSet());

        List<DictDataView> dictDataViews = dictDataService.listAvailableByDictIds(dictIdSet);
        Map<String, List<DictDataView>> dictDataMap = dictDataViews.stream().collect(Collectors.groupingBy(DictDataView::getDictId));

        Map<String, Map<String, Object>> map = new HashMap<>();
        dictList.forEach(item -> {
            List<DictDataView> itemValueList = dictDataMap.getOrDefault(item.getId(), List.of());
            String defaultValue = itemValueList.stream().filter(d -> YesOrNoEnum.YES.getCodeToString().equals(d.getIsDefault()))
                    .findFirst().map(DictDataView::getValue).orElse(null);
            Map<String, Object> result = new HashMap<>();
            result.put("defaultValue", defaultValue);
            result.put("list", itemValueList);
            map.put(item.getType(), result);
        });
        return map;
    }

    public List<BOListByTypeDictView> listByDictType(List<String> types) {
        List<DictView> dictList = dictService.listAvailableTypes(types);

        Set<String> dictIdSet = dictList.stream().map(DictView::getId).collect(Collectors.toSet());

        List<DictDataView> dictDataViews = dictDataService.listAvailableByDictIds(dictIdSet);
        Map<String, List<DictDataView>> dictDataMap = dictDataViews.stream().collect(Collectors.groupingBy(DictDataView::getDictId));

        return dictList.stream().map(item -> {
            List<DictDataView> itemValueList = dictDataMap.get(item.getId());

            List<BODictDataView> boDictDataViews = itemValueList.stream().map(BODictDataService::buildBoDictDataView).toList();
            Optional<BODictDataView> boDictDataViewOptional = itemValueList.stream().filter(d -> YesOrNoEnum.YES.getCodeToString().equals(d.getIsDefault()))
                    .findFirst().map(BODictDataService::buildBoDictDataView);

            BOListByTypeDictView dictView = new BOListByTypeDictView();
            dictView.setName(item.getName());
            dictView.setType(item.getType());
            dictView.setDefaultValue(boDictDataViewOptional.orElse(null));
            dictView.setValues(boDictDataViews);
            return dictView;
        }).toList();
    }
}
