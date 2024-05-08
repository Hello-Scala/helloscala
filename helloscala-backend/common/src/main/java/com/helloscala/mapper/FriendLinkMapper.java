package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.entity.FriendLink;
import com.helloscala.vo.friendLink.ApiFriendLinkVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FriendLinkMapper extends BaseMapper<FriendLink> {

    Integer getMaxSort();

    void top(@Param("id") Integer id, @Param("sort") int sort);

    List<ApiFriendLinkVO> selectLinkList(@Param("status") Integer status);

}
