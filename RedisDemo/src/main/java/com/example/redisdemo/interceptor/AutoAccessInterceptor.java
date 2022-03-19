package com.example.redisdemo.interceptor;

import cn.hutool.json.JSONUtil;
import com.example.redisdemo.annotation.AccessLimit;
import com.example.redisdemo.returnCode.ResponseDTO;
import com.example.redisdemo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 限流的拦截器
 */
@Component
public class AutoAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit methodAnnotation = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (null != methodAnnotation) {
                long seconds = methodAnnotation.seconds();
                long maxCount = methodAnnotation.maxCount();
                //关于key的生成规则可以自己定义 本项目需求是对每个方法都加上限流功能，如果你只是针对ip地址限流，那么key只需要只用ip就好
                String key = ((HandlerMethod) handler).getMethod().getName();
                //从redis中获取用户访问的次数
                try {
                    //此操作代表获取该key对应的值自增1后的结果
                    long q = redisService.incr(key, seconds);
                    if (q > maxCount) {
                        //加1
                        ResponseDTO responseDTO = ResponseDTO.succMsg("请求过于频繁，请稍后再试！");
                        writeReturnJson(response, JSONUtil.toJsonStr(responseDTO));
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private void writeReturnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }
}
