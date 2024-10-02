package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.entity.Tag;
import com.helloscala.common.vo.tag.ApiTagListVO;
import com.helloscala.common.vo.tag.SystemTagListVo;
import com.helloscala.service.web.view.ArticleTagCountView;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Repository
public interface TagMapper extends BaseMapper<Tag> {
    List<String> selectByArticleId(Long articleId);

    Page<SystemTagListVo> selectPageRecord(@Param("page") Page<Tag> objectPage, @Param("name") String name);

    @MapKey("id")
    List<Map<String, Object>> countTags();

    int validateTagIdIsExistArticle(String id);

    List<ApiTagListVO> selectTagListApi();
}
