package com.zmx.weblog.common.domain.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zmx.weblog.common.domain.dos.StatisticsArticlePVDO;

import java.time.LocalDate;

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
}
