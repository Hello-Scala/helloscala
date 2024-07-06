package com.helloscala.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.common.vo.message.ImRoomListVO;
import com.helloscala.common.vo.user.ImOnlineUserVO;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ApiImMessageService {
    Page<ImMessageVO> selectHistoryList();

    List<ImOnlineUserVO> selectOnlineUserList(Set<String> strings);

    Page<ImMessageVO> selectUserImHistoryList(String fromUserId, String toUserId);

    List<ImRoomListVO> selectRoomList();

    ImRoomListVO addRoom(String userId);

    void read(String userId);

    void deleteRoom(String roomId);

    void chat(ImMessageVO message);

    void withdraw(ImMessageVO message);

    Page<ImMessageVO> getMessageNotice(Integer type);

    Map<String, Long> getNewSystemNotice();

    void deleteByNoticeType(String id, Integer type);

    Page<ImMessageVO> getMessageNoticeApplet(Integer type);

    void markReadMessageNoticeApplet(String id);
}
