//package com.feiyu.novel.manager.mq;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.support.TransactionSynchronization;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
///**
// * AMQP 消息管理类
// *
// * @author xiongxiaoyang
// * @date 2022/5/25
// */
//@Component
//@RequiredArgsConstructor
//public class AmqpMsgManager {
//
//    private final AmqpTemplate amqpTemplate;
//
//    /**
//     * 发送小说信息改变消息
//     */
//    public void sendBookChangeMsg(Long bookId) {
//        sendAmqpMessage(amqpTemplate,"novel.fanout",null,bookId);
//    }
//
//    private void sendAmqpMessage(AmqpTemplate amqpTemplate, String exchange, String routingKey, Object message) {
//        // 如果在事务中则在事务执行完成后再发送，否则可以直接发送
//        if (TransactionSynchronizationManager.isActualTransactionActive()) {
//            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//                @Override
//                public void afterCommit() {
//                    amqpTemplate.convertAndSend(exchange, routingKey, message);
//                }
//            });
//            return;
//        }
//        amqpTemplate.convertAndSend(exchange, routingKey, message);
//        System.out.println("小说章节更新了！");
//    }
//
//}
//
