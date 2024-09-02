package com.helloscala.admin.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateDictDataRequest;
import com.helloscala.admin.controller.request.BOUpdateDictDataRequest;
import com.helloscala.admin.controller.view.BODictDataView;
import com.helloscala.admin.controller.view.BODictView;
import com.helloscala.service.service.DictDataService;
import com.helloscala.service.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BODictDataService {
    private final DictService dictService;
    private final DictDataService dictDataService;

    public Page<BODictDataView> selectDictDataPage(Integer dictId, Integer isPublish) {
        return null;
    }

    public void addDictData(BOCreateDictDataRequest request) {
    }

    public void update(BOUpdateDictDataRequest request) {
    }

    public void deleteDictData(List<String> ids) {
    }

    public List<BODictView> listByDictType(List<String> types) {
        return List.of();
    }
}
