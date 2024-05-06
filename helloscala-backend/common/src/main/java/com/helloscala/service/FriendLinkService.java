package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.FriendLink;

import java.util.List;


public interface FriendLinkService extends IService<FriendLink> {

    /**
     * 列表
     * @param name
     * @param status
     * @return
     */
    ResponseResult selectFriendLinkPage(String name, Integer status);

    /**
     * 添加
     * @param friendLink
     * @return
     */
    ResponseResult addFriendLink(FriendLink friendLink);

    /**
     * 修改
     * @param friendLink
     * @return
     */
    ResponseResult updateFriendLink(FriendLink friendLink);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteFriendLink(List<Integer> ids);

    ResponseResult top(Integer id);

}
