package com.helloscala.service.web.view;

import lombok.Data;

import java.util.List;


@Data
public class MenuOptionView {
    private String menuId;

    private String label;

    private List<MenuOptionView> children;


    public MenuOptionView(String value, String label) {
        this.menuId = value;
        this.label = label;
    }
}
