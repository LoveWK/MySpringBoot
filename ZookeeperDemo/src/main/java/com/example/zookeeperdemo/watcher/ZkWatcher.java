package com.example.zookeeperdemo.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Service;

@Service
public class ZkWatcher implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("【Watcher监听事件】="+watchedEvent.getState());
        System.out.println("【监听路径为】="+watchedEvent.getPath());
        System.out.println("【监听的类型为】="+watchedEvent.getType());
    }
}
