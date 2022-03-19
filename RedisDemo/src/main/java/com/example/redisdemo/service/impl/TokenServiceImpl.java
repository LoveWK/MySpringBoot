package com.example.redisdemo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.redisdemo.constant.ConstantRedis;
import com.example.redisdemo.service.RedisService;
import com.example.redisdemo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisService redisService;

    @Override
    public String createToken() {
        String str = UUID.randomUUID().toString().replace("-","");
        System.out.println("uuiId:"+str);
        StringBuilder token = new StringBuilder();
        try {
            token.append(ConstantRedis.TOKEN_PREFIX).append(str);
            redisService.setEx(token.toString(),token.toString(),1000L);
            boolean isNotEmpty = StrUtil.isNotEmpty(token.toString());
            if (isNotEmpty) {
                return token.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader(ConstantRedis.TOKEN_NAME);
        if (StrUtil.isBlank(token)) {//header中不存在token
            token = request.getParameter(ConstantRedis.TOKEN_NAME);
            if (StrUtil.isBlank(token)) {//parameter中也不存在token
                throw new Exception("request 中没有token");
            }
        }
        if (!redisService.exists(token)) {
            throw new Exception("redis 中没有token");
        }

        boolean remove = redisService.remove(token);
        if (!remove) {
            throw new Exception("redis 中没有token");
        }
        return true;
    }

}
