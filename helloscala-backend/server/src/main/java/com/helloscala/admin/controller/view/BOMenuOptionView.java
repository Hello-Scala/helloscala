package com.helloscala.admin.controller.view;

import lombok.Data;

import java.util.List;


@Data
public class BOMenuOptionView {
    private String menuId;

    private String label;

    private List<BOMenuOptionView> children;

    public BOMenuOptionView() {
    }

    public BOMenuOptionView(String menuId, String label) {
        this.menuId = menuId;
        this.label = label;
    }
}
