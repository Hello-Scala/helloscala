package com.helloscala.common.vo.menu;

import lombok.Data;

import java.util.List;

@Data
public class MenuOptionVO {
    private String value;

    private String label;

    private List<MenuOptionVO> children;

    public MenuOptionVO(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
