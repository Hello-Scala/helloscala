package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.entity.Say;
import com.helloscala.vo.say.ApiSayVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SayMapper extends BaseMapper<Say> {

    Page<ApiSayVO> selectPublicSayList(@Param("page") Page<Object> objectPage,@Param("showPrivate") boolean showPrivate);
}
