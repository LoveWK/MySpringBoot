package com.example.zookeeperdemo.controller;

import com.example.zookeeperdemo.zkService.ZkService;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ZkController {
    @Autowired
    private ZkService zkService;

    @RequestMapping(value = "/exist",method = RequestMethod.GET)
    @ResponseBody
    private String isExist(){
        Stat exists = zkService.exists("/hello", false);

        return exists.toString();
    }

}
