package com.example.redisdemo.service;

import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    @Autowired
    private RedissonClient redissonClient;

    private static final String REDIS_MQ = "redisMQ";

    /**
     * 发送消息到列的头部
     * @param message
     */
    public void sendMessage(String message) {
        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(REDIS_MQ);
        try {
            blockingDeque.putFirst(message);
            System.out.println("将消息：【"+message+"】插入到了队列里！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onMessage(){
        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(REDIS_MQ);
        while (true) {
            try {
                String message = (String) blockingDeque.takeLast();
                System.out.println("从队列中取出了消息：【"+message+"】");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
