//package com.feiyu.novel.core.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.FanoutExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 配置小说相关缓存更新的交换机、队列以及绑定关系
// * 发布/订阅模型
// * 交换机类型：广播FanoutExchange
// */
//@Configuration
//public class AmqpConfig {
//
//    @Bean
//    public FanoutExchange bookCacheUpdateExchange(){
//        return new FanoutExchange("novel.fanout");
//    }
//
//    @Bean
//    public Queue bookCacheUpdateQueue(){
//        return new Queue("bookCache.queue");
//    }
//
//    @Bean
//    public Binding bindingCacheUptQueue(Queue bookCacheUpdateQueue,FanoutExchange bookCacheUpdateExchange){
//        return BindingBuilder.bind(bookCacheUpdateQueue).to(bookCacheUpdateExchange);
//    }
//}
