package com.example.redisdemo;

import com.example.redisdemo.service.QueueService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisDemoApplication.class)
class RedisDemoApplicationTests {

    @Autowired
    private QueueService queueService;

    @Test
    void contextLoads() {
    }

    @Test
    void testQueue() throws InterruptedException {
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                queueService.sendMessage("消息" + i);
            }
        }).start();

        new Thread(() -> queueService.onMessage()).start();

        Thread.currentThread().join();
    }

}
