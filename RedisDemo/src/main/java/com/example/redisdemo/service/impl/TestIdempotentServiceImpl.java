package com.example.redisdemo.service.impl;

import com.example.redisdemo.service.TestIdempotentService;
import org.springframework.stereotype.Service;

@Service
public class TestIdempotentServiceImpl implements TestIdempotentService {

    @Override
    public String testIdempotent() {
        return "hello!!!";
    }
}
