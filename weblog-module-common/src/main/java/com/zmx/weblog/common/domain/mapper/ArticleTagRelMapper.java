package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.weblog.common.domain.dos.ArticleTagRelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ArticleTagRelMapper extends BaseMapper<ArticleTagRelDO> {

    int insertBatch(List<ArticleTagRelDO> relDOS);

    /**
     * 根据文章ID删除标签关联
     *
     * @param articleId 文章ID
     * @return 删除条数
     */
    default int deleteByArticleId(Long articleId) {
        return delete(new LambdaQueryWrapper<ArticleTagRelDO>()
                .eq(ArticleTagRelDO::getArticleId, articleId));
    }

    /**
     * 根据文章ID查找所有标签ID
     *
     * @param articleId 文章ID
     * @return 标签ID列表
     */
    default List<ArticleTagRelDO> selectByArticleId(Long articleId) {
        return selectList(
                new LambdaQueryWrapper<ArticleTagRelDO>()
                        .eq(ArticleTagRelDO::getArticleId, articleId));
    }
}