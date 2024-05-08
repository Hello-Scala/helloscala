package com.helloscala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.ResultCode;
import com.helloscala.entity.Article;
import com.helloscala.entity.Category;
import com.helloscala.exception.BusinessException;
import com.helloscala.mapper.ArticleMapper;
import com.helloscala.mapper.CategoryMapper;
import com.helloscala.service.CategoryService;
import com.helloscala.utils.PageUtil;
import com.helloscala.vo.category.SystemCategoryListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleMapper articleMapper;

    /**
     * 分类列表
     * @param name 分类名
     * @return
     */
    @Override
    public ResponseResult selectCategoryPage(String name) {
        Page<SystemCategoryListVO> categoryPage = baseMapper.selectCategory(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), name);
        return ResponseResult.success(categoryPage);
    }

    /**
     * 分类详情
     * @param id 分类id
     * @return
     */
    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = baseMapper.selectById(id);
        return ResponseResult.success(category);
    }
    /**
     * 添加分类
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addCategory(Category category) {
        Category vo = baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName()));
        if (vo != null) {
            throw new BusinessException(ResultCode.CATEGORY_IS_EXIST);
        }
        baseMapper.insert(category);
        return ResponseResult.success();
    }

    /**
     * 修改分类
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateCategory(Category category) {
        Category vo = baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName()));
        if (vo!= null && !vo.getId().equals(category.getId())) {
            throw new BusinessException(ResultCode.CATEGORY_IS_EXIST);
        }

        baseMapper.updateById(category);
        return ResponseResult.success();
    }

    /**
     * 批量删除分类
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteCategory(List<Long> list) {
        List<Long> ids = new ArrayList<>();
        for (Long id : list) {
            Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getCategoryId, id));
            if (count > 0) {
                throw new BusinessException( id + " 分类下有文章，不能删除");
            }
            ids.add(id);
        }

        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

}
