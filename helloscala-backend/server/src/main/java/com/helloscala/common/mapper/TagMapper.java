package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.vo.tag.ApiTagListVO;
import com.helloscala.common.vo.tag.SystemTagListVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface TagMapper extends BaseMapper<Tag> {
    List<String> selectByArticleId(Long articleId);

    Page<SystemTagListVo> selectPageRecord(@Param("page") Page<Tag> objectPage, @Param("name") String name);

    @MapKey("id")
    List<Map<String, Object>> countTags();

    int validateTagIdIsExistArticle(Long id);

    List<ApiTagListVO> selectTagListApi();
}
