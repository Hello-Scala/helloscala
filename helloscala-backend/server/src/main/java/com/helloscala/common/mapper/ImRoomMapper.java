package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.common.entity.ImRoom;
import com.helloscala.common.vo.message.ImRoomListVO;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImRoomMapper extends BaseMapper<ImRoom> {
    List<ImRoomListVO> selectListByUserId(String userId);
}
