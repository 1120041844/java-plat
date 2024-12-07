package com.work.ai.intercepter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.ai.entity.dto.UserDTO;
import com.work.ai.utils.SecureUtil;
import com.work.ai.service.IUserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Claims claims = SecureUtil.getClaims(request);

            if (claims == null) {
                unLogin(response,"未登录");
                return false;
            }

            Map<String, Object> map = new HashMap<>();
            Enumeration<?> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();

                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length > 0) {
                    String paramValue = paramValues[0];
                    map.put(paramName, paramValue);
                }
            }
            log.info("request url={} params={}",request.getRequestURI(), JSON.toJSONString(map));
            UserDTO userDTO= BeanUtil.toBean(claims, UserDTO.class);

            if (userDTO == null) {
                unLogin(response,"登录已过期，请重新登录");
                return false;
            }
            boolean checkUser = userService.checkUser(userDTO);
            if (!checkUser) {
                unLogin(response,"未登录");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.info(ExceptionUtil.getMessage(e));
            throw e;
        }
    }

    private void unLogin(HttpServletResponse response,String msg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject res = new JSONObject();
        res.put("code", 500);
        res.put("msg", msg);
        PrintWriter out = response.getWriter();
        out.append(res.toString());
    }


}
