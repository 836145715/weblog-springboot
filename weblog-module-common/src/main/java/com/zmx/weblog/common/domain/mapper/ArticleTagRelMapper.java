package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.weblog.common.domain.dos.ArticleTagRelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
}