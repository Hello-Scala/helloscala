package com.helloscala.service.web.view;

import lombok.Data;

import java.util.List;

@Data
public class RouteView {

    private String id;
    private String component;
    private String path;
    private String name;

    private Integer sort;
    private MetaView meta;

    private List<RouteView> children;

    @Data
    public static class MetaView {
        private String title;
        private String icon;
        private Boolean hidden;

        public MetaView(String title, String icon, Integer hidden) {
            this.title = title;
            this.icon = icon;
            this.hidden = hidden == 0;
        }
    }
}
