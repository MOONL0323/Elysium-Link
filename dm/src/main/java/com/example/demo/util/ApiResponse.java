package com.example.demo.util;

import lombok.Data;

/**
 * @author 86188
 *
 */
@Data
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private String error;

    public static <T> ApiResponse<T> bulid(T data, String message, Integer code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        if(data != null) {
            response.setData(data);
        }
        return response;
    }

    public static <T> ApiResponse<T> bulid(T data,ResultCodeEnum resultCodeEnum) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(resultCodeEnum.getCode());
        response.setMessage(resultCodeEnum.getMessage());
        if(data != null) {
            response.setData(data);
        }
        return response;
    }

    //成功
    public static<T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = bulid(data, ResultCodeEnum.SUCCESS);
        return response;
    }

    //失败
    public static<T> ApiResponse<T> fail(T data) {
        return bulid(data,ResultCodeEnum.FAIL);
    }
}