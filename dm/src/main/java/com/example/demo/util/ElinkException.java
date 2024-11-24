package com.example.demo.util;

/**
 * @author 86188
 */
public class ElinkException extends RuntimeException{

    //异常状态码
    private Integer code;

    /**
    * 通过状态码和错误消息创建异常对象
    * @param message
    * @param code
    */
    public ElinkException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public ElinkException(String msg) {
        super(msg);
        this.code = ResultCodeEnum.FAIL.getCode();
    }

    /**
     * 接收枚举类型对象
     */
    public ElinkException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}
