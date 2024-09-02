package com.helloscala.service.service.util;

import com.helloscala.service.entity.Dict;
import com.helloscala.service.entity.DictData;
import com.helloscala.service.web.view.DictDataView;
import com.helloscala.service.web.view.DictView;
import org.jetbrains.annotations.NotNull;

public final class DictHelper {

    public static DictView buildDictView(Dict dict) {
        DictView view = new DictView();
        view.setId(dict.getId());
        view.setName(dict.getName());
        view.setType(dict.getType());
        view.setStatus(dict.getStatus());
        view.setRemark(dict.getRemark());
        view.setCreateTime(dict.getCreateTime());
        view.setUpdateTime(dict.getUpdateTime());
        view.setSort(dict.getSort());
        return view;
    }

    public static DictDataView buildDictDataView(DictData dictData) {
        return buildDictDataView(dictData, null);
    }

    public static DictDataView buildDictDataView(DictData dictData, DictView dictView) {
        DictDataView dictDataView = new DictDataView();
        dictDataView.setId(dictData.getId());
        dictDataView.setDictId(dictData.getDictId());
        dictDataView.setLabel(dictData.getLabel());
        dictDataView.setValue(dictData.getValue());
        dictDataView.setStyle(dictData.getStyle());
        dictDataView.setIsDefault(dictData.getIsDefault());
        dictDataView.setSort(dictData.getSort());
        dictDataView.setStatus(dictData.getStatus());
        dictDataView.setRemark(dictData.getRemark());
        dictDataView.setDict(dictView);
        return dictDataView;
    }
}
