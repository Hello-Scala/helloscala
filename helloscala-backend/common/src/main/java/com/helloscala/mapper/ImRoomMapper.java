package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.entity.ImRoom;
import com.helloscala.vo.message.ImRoomListVO;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImRoomMapper extends BaseMapper<ImRoom> {


    List<ImRoomListVO> selectListByUserId(String userId);
}
