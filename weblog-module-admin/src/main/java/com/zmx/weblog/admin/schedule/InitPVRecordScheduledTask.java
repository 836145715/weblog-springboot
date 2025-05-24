package com.zmx.weblog.admin.schedule;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmx.weblog.common.domain.dos.StatisticsArticlePVDO;
import com.zmx.weblog.common.domain.mapper.StatisticsArticlePVMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
public class InitPVRecordScheduledTask {

    @Autowired
    private StatisticsArticlePVMapper articlePVMapper;

    @Scheduled(cron = "0 0 23 * * ?")  //每天晚间23点执行
    public void execute() {
        //定时任务执行逻辑
        log.info("==> 开始执行初始化明日 PV 访问量记录定时任务");

        //当日日期
        LocalDate today = LocalDate.now();
        //明日日期
        LocalDate tomorrow = today.plusDays(1);

        //判断记录是否存在
        LambdaQueryWrapper<StatisticsArticlePVDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatisticsArticlePVDO::getPvDate, tomorrow);
        StatisticsArticlePVDO pvRecord = articlePVMapper.selectOne(wrapper);
        if (pvRecord != null) {
            log.info("==> 明日 PV 访问量记录已存在，无需初始化");
            return;
        }

        //组装插入记录
        StatisticsArticlePVDO build = StatisticsArticlePVDO.builder()
                .pvDate(tomorrow)
                .pvCount(0L)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        articlePVMapper.insert(build);
        log.info("==> 初始化明日 PV 访问量记录定时任务执行完成");
    }



}
