package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.common.domain.dos.ArticleDO;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

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
    default Page<ArticleDO> findArticlePageList(
            String title, LocalDate startDate, LocalDate endDate, long current, long pageSize) {
        LambdaQueryWrapper<ArticleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.isNotBlank(title),
                        ArticleDO::getTitle, title)
                .ge(startDate != null, ArticleDO::getCreateTime, startDate)
                .le(endDate != null, ArticleDO::getCreateTime, endDate)
                .orderByDesc(ArticleDO::getCreateTime);
        return selectPage(new Page<>(current, pageSize),
                queryWrapper);
    }
}