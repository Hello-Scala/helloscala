package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.service.entity.ImRoom;
import com.helloscala.common.vo.message.ImRoomListVO;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImRoomMapper extends BaseMapper<ImRoom> {
    List<ImRoomListVO> selectListByUserId(String userId);
}
