//package com.feiyu.novel.core.listener;
//
//
//import com.feiyu.novel.dao.mapper.BookInfoMapper;
//import com.feiyu.novel.dto.resp.BookInfoRespDto;
//import com.feiyu.novel.manager.cache.BookInfoCacheManager;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class RabbitQueueListener {
//
//    private final BookInfoCacheManager bookInfoCacheManager;
//
//    private final BookInfoMapper bookInfoMapper;
//
//    @RabbitListener(queues = "bookCache.queue")
//    public void listenFanoutQueue1(Long msgBookId) {
//
//        //更新对应id的小说信息缓存
//        bookInfoCacheManager.cachePutBookInfo(msgBookId);
//
//        log.info("监听器接收消息后更新缓存：\n" + bookInfoMapper.selectById(msgBookId).toString() + "");
//
//    }
//}
