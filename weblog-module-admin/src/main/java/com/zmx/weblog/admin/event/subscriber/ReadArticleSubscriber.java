package com.zmx.weblog.admin.event.subscriber;

import com.zmx.weblog.admin.event.ReadArticleEvent;
import com.zmx.weblog.common.domain.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReadArticleSubscriber implements ApplicationListener<ReadArticleEvent> {

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(ReadArticleEvent event) {
        //在这里处理收到的事件，可以是任何逻辑操作
        Long articleId = event.getArticleId();
        //获取当前线程名称
        String threadName = Thread.currentThread().getName();
        log.info("线程名称：{}，消费文章阅读事件：{}", threadName, articleId);
        articleMapper.increaseReadNum(articleId);
    }
}
