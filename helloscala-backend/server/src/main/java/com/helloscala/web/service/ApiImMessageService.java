package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.common.vo.user.ImOnlineUserVO;

import java.util.List;
import java.util.Set;


public interface ApiImMessageService {
    ResponseResult selectHistoryList();

    List<ImOnlineUserVO> selectOnlineUserList(Set<String> strings);

    ResponseResult selectUserImHistoryList(String fromUserId, String toUserId);

    ResponseResult selectRoomList();

    ResponseResult addRoom(String userId);

    ResponseResult read(String userId);

    ResponseResult deleteRoom(String roomId);

    ResponseResult chat(ImMessageVO message);

    ResponseResult withdraw(ImMessageVO message);

    ResponseResult getMessageNotice(Integer type);

    ResponseResult getNewSystemNotice();

    ResponseResult deleteByNoticeType(String id,Integer type);

    ResponseResult getMessageNoticeApplet(Integer type);

    ResponseResult markReadMessageNoticeApplet(String id);
}
