package com.work.plat.handle;


import com.work.plat.constants.ApiResult;
import com.work.plat.exception.BaseException;
import com.work.plat.utils.MessageUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Log logger = LogFactory.getLog(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult jsonErrorHandler(HttpServletRequest req, Exception e) {
        ApiResult result = null;
        if (e instanceof BaseException) {
            BaseException be = (BaseException) e;
            result=ApiResult.fail(be.getCode(),be.getMessage());
        }else if(e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException me=(MethodArgumentNotValidException)e;
            result=ApiResult.fail(555,getErrorMessage(me.getBindingResult()));
        }else if(e instanceof HttpMessageNotReadableException){
            result=ApiResult.fail(555,"请求参数错误，请检查参数结构与接口是否对应");
        } else {
            logger.error("请求出现未处理错误", e);
            result=ApiResult.fail(555, MessageUtils.get("fail"));

        }
        //添加异常信息
        if(e!=null){
            req.setAttribute("exceptionClass",e.getClass().getName());
            req.setAttribute("exceptionMessage",e.getLocalizedMessage());
            logger.error("异常 ", e);
        }
        return result;
    }

    /**
     * 获取错误信息
     * @param bindingResult
     * @return
     */
    private String getErrorMessage(BindingResult bindingResult) {
        List<ObjectError> list = bindingResult.getAllErrors();
        StringBuilder message = new StringBuilder();
        for (ObjectError o : list) {
            if (o instanceof FieldError) {
                FieldError error = (FieldError) o;
                message.append(error.getField() + o.getDefaultMessage() + ";");
            } else {
                message.append(o.getDefaultMessage() + ";");
            }
        }
        return message.toString();
    }

}

