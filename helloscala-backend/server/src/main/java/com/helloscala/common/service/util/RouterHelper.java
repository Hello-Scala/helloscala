package com.helloscala.common.service.util;

import com.helloscala.common.entity.Menu;
import com.helloscala.common.service.impl.MenuServiceImpl;
import com.helloscala.common.vo.menu.RouterVO;

import java.util.List;

/**
 * @author Steve Zou
 */
public final class RouterHelper {
    public static List<RouterVO> toRouterVo(List<Menu> menuWithChildrenList) {
        return menuWithChildrenList.stream().map(menu -> {
            RouterVO routeVo = toRouterVO(menu);
            List<RouterVO> subRoutes = menu.getChildren().stream().map(RouterHelper::toRouterVO).toList();
            routeVo.setChildren(subRoutes);
            return routeVo;
        }).toList();
    }

    private static RouterVO toRouterVO(Menu menu) {
        RouterVO.MetaVO metaVO = new RouterVO.MetaVO(menu.getTitle(), menu.getIcon(), menu.getHidden());
        new RouterVO.MetaVO(menu.getTitle(), menu.getIcon(), menu.getHidden());

        return RouterVO.builder().id(menu.getId()).path(menu.getPath()).name(menu.getName()).component(menu.getComponent())
                .meta(metaVO).sort(menu.getSort()).build();
    }
}
