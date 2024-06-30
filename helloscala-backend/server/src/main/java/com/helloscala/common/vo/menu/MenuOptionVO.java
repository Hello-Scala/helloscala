package com.helloscala.common.vo.menu;

import lombok.Data;

import java.util.List;

@Data
public class MenuOptionVO {
    private Integer value;

    private String label;

    private List<MenuOptionVO> children;

    public MenuOptionVO(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
