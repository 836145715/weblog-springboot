package com.zmx.weblog.common.domain.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zmx.weblog.common.domain.dos.StatisticsArticlePVDO;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsArticlePVMapper extends BaseMapper<StatisticsArticlePVDO> {

    /**
     * 增加pv访问量
     * @param date 日期
     * @return
     */
    default int increasePVCount(LocalDate date){
        return update(null,
                Wrappers.<StatisticsArticlePVDO>lambdaUpdate()
                        .setSql("pv_count = pv_count + 1")
                        .eq(StatisticsArticlePVDO::getPvDate,date)
        );
    }

    /**
     * 查询最近7天的访问量
     * 按日期降序排序
     * @return
     */
    default List<StatisticsArticlePVDO> selectLatestWeekPvCount(){
        return selectList(Wrappers.<StatisticsArticlePVDO>lambdaQuery()
                .le(StatisticsArticlePVDO::getPvDate,LocalDate.now().plusDays(1)) //小于明天
                .orderByDesc(StatisticsArticlePVDO::getPvDate)
                .last("limit 7")  //查询最新7天的数据
        );
    }


}
