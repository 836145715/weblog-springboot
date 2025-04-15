package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.weblog.common.domain.dos.ArticleCategoryRelDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleCategoryRelMapper extends BaseMapper<ArticleCategoryRelDO> {
    /**
     * 根据文章ID删除分类关联
     * 
     * @param articleId 文章ID
     * @return 删除条数
     */
    default int deleteByArticleId(Long articleId) {
        return delete(new LambdaQueryWrapper<ArticleCategoryRelDO>()
                .eq(ArticleCategoryRelDO::getArticleId, articleId));
    }

    /**
     * 根据文章ID查找分类ID
     * 
     * @param articleId 文章ID
     * @return 分类ID
     */
    default Long selectCategoryIdByArticleId(Long articleId) {
        ArticleCategoryRelDO relDO = selectOne(new LambdaQueryWrapper<ArticleCategoryRelDO>()
                .eq(ArticleCategoryRelDO::getArticleId, articleId));
        return relDO != null ? relDO.getCategoryId() : null;
    }
}