package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.common.domain.dos.ArticleDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<ArticleDO> {
    /**
     * 文章分页查询
     * 
     * @param title     标题
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @param current   当前页
     * @param pageSize  每页大小
     * @return 分页结果
     */
    default Page<ArticleDO> selectPageList(
            String title, LocalDate startDate, LocalDate endDate, long current, long pageSize) {
        LambdaQueryWrapper<ArticleDO> queryWrapper = new LambdaQueryWrapper<>();
        Page page = new Page<>(current, pageSize);

        queryWrapper
                .like(StringUtils.isNotBlank(title),ArticleDO::getTitle, title)
                .ge(startDate != null, ArticleDO::getCreateTime, startDate)
                .le(endDate != null, ArticleDO::getCreateTime, endDate)
                .orderByDesc(ArticleDO::getCreateTime);
        return selectPage(page,queryWrapper);
    }


    /**
     * 根据文章 ID 批量分页查询
     * @param current
     * @param size
     * @param articleIds
     * @return
     */
    default Page<ArticleDO> selectPageListByArticleIds(Long current, Long size, List<Long> articleIds) {
        // 分页对象(查询第几页、每页多少数据)
        Page<ArticleDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<ArticleDO> wrapper = Wrappers.<ArticleDO>lambdaQuery()
                .in(ArticleDO::getId, articleIds) // 批量查询
                .orderByDesc(ArticleDO::getCreateTime); // 按创建时间倒叙

        return selectPage(page, wrapper);
    }

    /**
     * 查询上一篇文章
     * @param articleId
     * @return
     */
    default ArticleDO selectPreArticle(Long articleId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .orderByAsc(ArticleDO::getId) // 按文章 ID 升序排列
                .lt(ArticleDO::getId, articleId) // 查询比当前文章 ID 大的
                .last("limit 1")); // 第一条记录即为上一篇文章
    }

    /**
     * 查询下一篇文章
     * @param articleId
     * @return
     */
    default ArticleDO selectNextArticle(Long articleId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .orderByAsc(ArticleDO::getId) // 按文章 ID 倒序排列
                .gt(ArticleDO::getId, articleId) // 查询比当前文章 ID 小的
                .last("limit 1")); // 第一条记录即为下一篇文章
    }

}