package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zmx.weblog.common.domain.dos.ArticleCategoryRelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    default ArticleCategoryRelDO selectByArticleId(Long articleId) {
        return selectOne(new LambdaQueryWrapper<ArticleCategoryRelDO>()
                .eq(ArticleCategoryRelDO::getArticleId, articleId));
    }

    default List<ArticleCategoryRelDO> selectByArticleIds(List<Long> articleIds) {
        return selectList(new LambdaQueryWrapper<ArticleCategoryRelDO>()
                .in(ArticleCategoryRelDO::getArticleId, articleIds));
    }

    /**
     * 根据分类ID查找一个分类关联
     *
     * @param categoryId 分类ID
     * @return 分类关联
     */
    default ArticleCategoryRelDO selectOneByCategoryId(Long categoryId) {

        // 使用limit 1 来优化查询
        return selectOne(
                new LambdaQueryWrapper<ArticleCategoryRelDO>()
                        .eq(ArticleCategoryRelDO::getCategoryId, categoryId).last("limit 1"));
    }


    /**
     * 根据分类 ID 查询所有的关联记录
     * @param categoryId
     * @return
     */
    default List<ArticleCategoryRelDO> selectListByCategoryId(Long categoryId) {
        return selectList(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO::getCategoryId, categoryId));
    }

}