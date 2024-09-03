package com.helloscala.admin.controller.view;

import lombok.Data;

import java.util.List;

@Data
public class BOListByTypeDictView {
    private String name;

    private String type;

    private BODictDataView defaultValue;

    private List<BODictDataView> values;
}
