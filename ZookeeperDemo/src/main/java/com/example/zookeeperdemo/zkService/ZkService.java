package com.example.zookeeperdemo.zkService;

import org.apache.zookeeper.data.Stat;

public interface ZkService {
    Stat exists(String path, boolean needWatch);
}
