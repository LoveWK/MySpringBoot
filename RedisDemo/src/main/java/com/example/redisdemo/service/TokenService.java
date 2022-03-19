package com.example.redisdemo.service;

import javax.servlet.http.HttpServletRequest;

/**
 * token的服务
 */
public interface TokenService {
    /**
     * 创建token
     * @return
     */
    public String createToken();

    /**
     * 校验token
     * @param request
     * @return
     * @throws Exception
     */
    public boolean checkToken(HttpServletRequest request) throws Exception;

}
