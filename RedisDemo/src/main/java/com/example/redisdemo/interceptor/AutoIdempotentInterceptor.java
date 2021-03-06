package com.example.redisdemo.interceptor;

import cn.hutool.json.JSONUtil;
import com.example.redisdemo.annotation.AccessLimit;
import com.example.redisdemo.annotation.AutoIdempotent;
import com.example.redisdemo.returnCode.ResponseDTO;
import com.example.redisdemo.service.RedisService;
import com.example.redisdemo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 幂等拦截器
 */
@Component
public class AutoIdempotentInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    /**
     * 预处理
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //被autoIdempotent标记的扫描
        AutoIdempotent annotation = method.getAnnotation(AutoIdempotent.class);
        if (null != annotation) {
            try {
                return tokenService.checkToken(request);//幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
            } catch (Exception e) {
                ResponseDTO responseDTO = ResponseDTO.succMsg("重复操作了！");
                writeReturnJson(response, JSONUtil.toJsonStr(responseDTO));
                throw e;
            }
        }
        //必须返回true，否则会拦截一切请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    /**
     * 返回的json值
     * @param response
     * @param json
     * @throws Exception
     */
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
