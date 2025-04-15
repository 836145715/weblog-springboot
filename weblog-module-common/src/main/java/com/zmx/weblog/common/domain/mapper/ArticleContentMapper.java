package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.weblog.common.domain.dos.ArticleContentDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleContentMapper extends BaseMapper<ArticleContentDO> {
    /**
     * 根据文章ID删除内容
     * 
     * @param articleId 文章ID
     * @return 删除条数
     */
    default int deleteByArticleId(Long articleId) {
        return delete(new LambdaQueryWrapper<ArticleContentDO>()
                .eq(ArticleContentDO::getArticleId, articleId));
    }

    /**
     * 根据文章ID查找文章内容
     * 
     * @param articleId 文章ID
     * @return 文章内容
     */
    default String selectContentByArticleId(Long articleId) {
        ArticleContentDO contentDO = selectOne(new LambdaQueryWrapper<ArticleContentDO>()
                .eq(ArticleContentDO::getArticleId, articleId));
        return contentDO != null ? contentDO.getContent() : null;
    }
}