package com.zmx.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zmx.weblog.admin.model.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.zmx.weblog.admin.service.AdminDashboardService;
import com.zmx.weblog.common.domain.dos.ArticleDO;
import com.zmx.weblog.common.domain.dos.ArticlePublishCountDO;
import com.zmx.weblog.common.domain.mapper.ArticleMapper;
import com.zmx.weblog.common.domain.mapper.CategoryMapper;
import com.zmx.weblog.common.domain.mapper.TagMapper;
import com.zmx.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    /**
     * 获取仪表盘基础统计数据
     * @return
     */

    @Override
    public Response findDashboardStatistics() {
        //查询文章总数
        Long articleCount = articleMapper.selectCount(null);
        //查询分类总数
        Long categoryCount = categoryMapper.selectCount(null);
        //查询标签总数
        Long tagCount = tagMapper.selectCount(null);

        //总浏览量
        List<ArticleDO> articleDOS = articleMapper.selectAllReadNum();
        Long pvTotal = 0L;
        for (ArticleDO articleDO : articleDOS) {
            pvTotal += articleDO.getReadNum();
        }

        //组装VO

        FindDashboardStatisticsInfoRspVO vo = FindDashboardStatisticsInfoRspVO.builder()
                .articleTotalCount(articleCount)
                .categoryTotalCount(categoryCount)
                .tagTotalCount(tagCount)
                .pvTotalCount(pvTotal)
                .build();

        return Response.success(vo);
    }

    @Override
    public Response findDashboardPublishArticleStatistics() {
        //当前日期
        LocalDate currentDate = LocalDate.now();
        //当前日期倒退一年的日期
        LocalDate startDate = currentDate.minusYears(1);
        //查找这一年内，每日发布的文章数量
        List<ArticlePublishCountDO> articlePublishCountDOS = articleMapper.selectDateArticlePublishCount(startDate, currentDate);

        //有序map，返回的日期文章数量需要升序排列
        Map<LocalDate,Long> map = new LinkedHashMap<>();
        if(CollectionUtils.isNotEmpty(articlePublishCountDOS)){
            //DO 转MAP
            Map<LocalDate,Long> dateArticleCountMap  = articlePublishCountDOS.stream()
                    .collect(Collectors.toMap(ArticlePublishCountDO::getDate,ArticlePublishCountDO::getCount));

            //遍历一年的日期，填充数据
            for(;startDate.isBefore(currentDate) || startDate.isEqual(currentDate);startDate = startDate.plusDays(1)){
                //以日期作为KEY 从dateArticleCountMap 中获取文章数量
                Long count = dateArticleCountMap.get(startDate);
                //设置到返回参数 MAP
                map.put(startDate,count == null ? 0L : count);
            }
        }

        return Response.success(map);
    }
}
