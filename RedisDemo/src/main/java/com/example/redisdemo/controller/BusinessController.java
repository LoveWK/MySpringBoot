package com.example.redisdemo.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.redisdemo.annotation.AccessLimit;
import com.example.redisdemo.annotation.AutoIdempotent;
import com.example.redisdemo.returnCode.ResponseDTO;
import com.example.redisdemo.service.TestIdempotentService;
import com.example.redisdemo.service.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.ws.Service;

@RestController
public class BusinessController {
    @Resource
    private TokenService tokenService;

    @Resource
    private TestIdempotentService testIdempotentService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    /**
     * 接口幂等示例：https://www.cnblogs.com/wk-missQ1/p/15062070.html
     * @return
     */
    @PostMapping("/get/token")
    public String  getToken(){
        String token = tokenService.createToken();
        if (StrUtil.isNotEmpty(token)) {
            ResponseDTO resultVo = new ResponseDTO();
            resultVo.setCode(200);
            resultVo.setMsg("成功");
            resultVo.setData(token);
            return JSONUtil.toJsonStr(resultVo);
        }
        return token;
    }

    @AutoIdempotent
    @PostMapping("/test/testIdempotent")
    public String testIdempotentService(){
        String result = testIdempotentService.testIdempotent();
        if (StrUtil.isNotEmpty(result)) {
            return JSONUtil.toJsonStr(ResponseDTO.succData(result));
        }
        return null;
    }

    @AccessLimit(seconds = 20, maxCount = 5)
    @PostMapping("/test/testAccessLimit")
    public String testAccessLimit() {
        return "hello";
    }
}
