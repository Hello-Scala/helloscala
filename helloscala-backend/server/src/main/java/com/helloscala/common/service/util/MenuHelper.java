package com.helloscala.common.service.util;

import com.helloscala.common.entity.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Steve Zou
 */
public final class MenuHelper {
    public static List<Menu> toMenusWithChildren(List<Menu> flattenMenus) {
        List<Menu> result = new ArrayList<>(flattenMenus);
        Map<String, List<Menu>> childMenuMap = result.stream().filter(m -> Objects.nonNull(m.getParentId()))
                .collect(Collectors.groupingBy(Menu::getParentId));
        result.forEach(m -> m.setChildren(childMenuMap.get(m.getId())));
        return result;
    }
}
