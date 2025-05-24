package com.zmx.weblog.admin.event.subscriber;

import com.zmx.weblog.admin.event.ReadArticleEvent;
import com.zmx.weblog.common.domain.mapper.ArticleMapper;
import com.zmx.weblog.common.domain.mapper.StatisticsArticlePVMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class ReadArticleSubscriber implements ApplicationListener<ReadArticleEvent> {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private StatisticsArticlePVMapper articlePVMapper;

    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(ReadArticleEvent event) {
        //在这里处理收到的事件，可以是任何逻辑操作
        Long articleId = event.getArticleId();
        //获取当前线程名称
        String threadName = Thread.currentThread().getName();
        log.info("线程名称：{}，消费文章阅读事件：{}", threadName, articleId);
        articleMapper.increaseReadNum(articleId);

        //增加pv访问量
        articlePVMapper.increasePVCount(LocalDate.now());
        log.info("线程名称：{}，增加当日文章PV访问量 + 1 操作成功：{}", threadName, LocalDate.now());


    }
}
