package com.work.ai.constants;


import com.work.ai.exception.BaseException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApiResult<T> implements Serializable {

    private Integer code;

    private String message;

    private boolean success;

    private Long timestamp;

    private T data;

    private ApiResult(IResultCode resultCode) {
        this(resultCode, null, resultCode.getMessage());
    }

    private ApiResult(IResultCode resultCode, String message) {
        this(resultCode, null, message);
    }

    private ApiResult(IResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getMessage() );
    }

    private ApiResult(IResultCode resultCode, T data, String message) {
        this(resultCode.getCode(), data, message);
    }

    private ApiResult(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.success = ResultCodeEnum.SUCCESS.code.equals(code);
        this.timestamp=System.currentTimeMillis();
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isSuccess(ApiResult<?> result) {
        return Optional.ofNullable(result)
            .map(x -> Objects.equals(ResultCodeEnum.SUCCESS.code, x.code))
            .orElse(Boolean.FALSE);
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isNotSuccess(ApiResult<?> result) {
        return !ApiResult.isSuccess(result);
    }

    /**
     * 返回ApiResult
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> data(T data) {
        return data(data, "success");
    }


    public static ApiResult data() {
        return data(ResultCodeEnum.SUCCESS.getCode(),null,null);
    }

    /**
     * 返回ApiResult
     *
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> data(T data, String msg) {
        return data(ResultCodeEnum.SUCCESS.getCode(), data, msg);
    }

    /**
     * 返回ApiResult
     *
     * @param code 状态码
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> data(Integer code, T data, String msg) {
        return new ApiResult<>(code, data, data == null ? "null" : msg);
    }

    /**
     * 返回ApiResult
     *
     * @param <T> T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(ResultCodeEnum.SUCCESS);
    }

    /**
     * 返回ApiResult
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> success(String msg) {
        return new ApiResult<>(ResultCodeEnum.SUCCESS, msg);
    }

    /**
     * 返回ApiResult
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> success(IResultCode resultCode) {
        return new ApiResult<>(resultCode);
    }

    /**
     * 返回ApiResult
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> success(IResultCode resultCode, String msg) {
        return new ApiResult<>(resultCode, msg);
    }

    /**
     * 返回ApiResult
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> fail(String msg) {
        return new ApiResult<>(ResultCodeEnum.FAIL, msg);
    }


    /**
     * 返回ApiResult
     *
     * @param code 状态码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> fail(Integer code, String msg) {
        return new ApiResult<>(code, null, msg);
    }

    /**
     * 返回ApiResult
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> fail(IResultCode resultCode) {
        return new ApiResult<>(resultCode);
    }

    /**
     * 返回ApiResult
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return ApiResult
     */
    public static <T> ApiResult<T> fail(IResultCode resultCode, String msg) {
        return new ApiResult<>(resultCode, msg);
    }

    /**
     * 返回ApiResult
     *
     * @param flag 成功状态
     * @return ApiResult
     */
    public static <T> ApiResult<T> status(boolean flag) {
        return flag ? success("success") : fail("fail");
    }

    public static ApiResult result(Integer code, String message){
        return new ApiResult(code,null,message);
    }

    /**
     * 检查是否成功，不成功直接抛出异常
     * @param apiResult
     */
    public static void checkSuccess(ApiResult apiResult){
        apiResult.checkSuccess();
    }

    /**
     * 检查是否成功，不成功直接抛出异常
     */
    public void checkSuccess(){
        if(!this.isSuccess()){
            throw new BaseException(this.code,this.message);
        }
    }
}
