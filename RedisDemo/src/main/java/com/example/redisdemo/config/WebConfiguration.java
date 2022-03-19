package com.example.redisdemo.config;

import com.example.redisdemo.interceptor.AutoAccessInterceptor;
import com.example.redisdemo.interceptor.AutoIdempotentInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * 拦截器的配置，将自定义注解AutoIdempotent加到配置类中
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Resource
    private AutoIdempotentInterceptor autoIdempotentInterceptor;

    @Resource
    private AutoAccessInterceptor autoAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autoIdempotentInterceptor);
        registry.addInterceptor(autoAccessInterceptor);
        super.addInterceptors(registry);
    }
}
