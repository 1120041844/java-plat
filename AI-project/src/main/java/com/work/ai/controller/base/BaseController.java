package com.work.ai.controller.base;


import com.work.ai.constants.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;


    /**
     * 获取request
     *
     * @return HttpServletRequest
     */
    public HttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * 读取cookie
     *
     * @param request HttpServletRequest
     * @param name    cookie name
     * @return cookie value
     */
    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }


    protected void setCookies(String key, String value,int expiry) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
/** ============================     API_RESULT    =================================================  */

    /**
     * 返回ApiResult
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return ApiResult
     */
    public <T> ApiResult<T> data(T data) {
        return ApiResult.data(data);
    }

    public <T> ApiResult<T> data() {
        return ApiResult.data();
    }

    /**
     * 返回ApiResult
     *
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return ApiResult
     */
    public <T> ApiResult<T> data(T data, String msg) {
        return ApiResult.data(data, msg);
    }

    /**
     * 返回ApiResult
     *
     * @param data 数据
     * @param msg  消息
     * @param code 状态码
     * @param <T>  T 泛型标记
     * @return ApiResult
     */
    public <T> ApiResult<T> data(T data, String msg, Integer code) {
        return ApiResult.data(code, data, msg);
    }

    public ApiResult success() {
        return ApiResult.success();
    }

    /**
     * 返回ApiResult
     *
     * @param msg 消息
     * @return ApiResult
     */
    public ApiResult success(String msg) {
        return ApiResult.success(msg);
    }

    /**
     * 返回ApiResult
     *
     * @param msg 消息
     * @return ApiResult
     */
    public ApiResult fail(String msg) {
        return ApiResult.fail(msg);
    }

    /**
     * 返回ApiResult
     *
     * @param flag 是否成功
     * @return ApiResult
     */
    public ApiResult status(boolean flag) {
        return ApiResult.status(flag);
    }



}
