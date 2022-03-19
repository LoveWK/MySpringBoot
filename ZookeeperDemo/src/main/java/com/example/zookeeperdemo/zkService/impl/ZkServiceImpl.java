package com.example.zookeeperdemo.zkService.impl;

import com.example.zookeeperdemo.zkService.ZkService;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("zkService")
public class ZkServiceImpl implements ZkService {
    @Autowired
    private ZooKeeper zkClient;

    @Override
    public Stat exists(String path, boolean needWatch) {
        try {
            Stat exists = zkClient.exists(path, needWatch);
            return zkClient.exists(path,needWatch);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
