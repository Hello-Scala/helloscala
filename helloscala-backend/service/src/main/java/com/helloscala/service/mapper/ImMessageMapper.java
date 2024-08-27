package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.entity.ImMessage;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.common.vo.user.ImOnlineUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface ImMessageMapper extends BaseMapper<ImMessage> {
    Page<ImMessageVO> selectPublicHistoryList(Page<ImMessageVO> imMessageVOPage );

    List<ImOnlineUserVO> selectPublicOnlineUserList(@Param("keys") Set<String> keys);

    Page<ImMessageVO> selectPublicUserImHistoryList(@Param("page")Page<ImMessageVO> imMessageVOPage,
                                              @Param("fromUserId") String fromUserId,@Param("toUserId")String toUserId);

    int selectListReadByUserId(@Param("toUserId") String toUserId, @Param("fromUserId")String fromUserId);

    void updateRead(@Param("fromUserId") String userId, @Param("toUserId") String loginIdAsString);

    Page<ImMessageVO> getMessageNotice(@Param("page")Page<Object> objectPage, @Param("userId")String userId
            , @Param("noticeType")Integer noticeType);
}
