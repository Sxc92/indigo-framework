package com.indigo.framework.core.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.indigo.framework.core.enums.GlobalStatusEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Optional;

import static com.indigo.framework.core.enums.GlobalStatusEnums.ERROR;


/**
 * @author 史偕成
 * @date 2024/12/08 18:33
 **/
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private final static int SUCCESS_CODE = 200;

    private final static String SUCCESS_MSG = "操作成功";

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 时间戳，用于记录操作时的时间
     */
    private long timestamp;


    /**
     * 构造一个Result对象，用于封装操作结果
     * 该构造函数主要用于初始化Result对象的返回码、消息和时间戳
     *
     * @param code 操作结果的返回码，通常用于表示操作的成功与否
     * @param msg  操作结果的消息，通常用于描述操作结果的详细信息
     */
    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public <T> Result(T data, int code, String msg) {

    }


    /**
     * 生成一个表示成功的Result对象
     *
     * @param code 状态码，用于表示成功的具体状态
     * @param data 成功返回的数据
     * @param msg  附加消息，用于进一步描述成功的情况
     * @param <T>  泛型参数，表示返回的数据类型
     * @return 返回一个Result对象，其中包含状态码、数据和消息
     */
    public static <T> Result<T> success(T data, int code, String msg) {
        return new Result<>(data, code, msg);
    }

    public static <T> Result<T> success(T data) {
        return success(data, SUCCESS_CODE, SUCCESS_MSG);
    }

    public static <T> Result<T> success() {
        return success(null, SUCCESS_CODE, SUCCESS_MSG);
    }

    public static <T> Result<T> fail() {
        return fail(ERROR.getMsg(), ERROR.getCode());
    }

    public static <T> Result<T> fail(String msg) {
        return fail(msg, ERROR.getCode());
    }

    public static <T> Result<T> fail(String msg, int code) {
        return new Result<>(code, msg);
    }

    /**
     * 创建一个表示失败的Result对象
     *
     * @param baseCode 错误代码对象，用于指示失败的原因
     * @param <T>      泛型参数，表示Result中数据的类型
     * @return 返回一个Result对象，包含错误代码和消息
     */
    public static <T> Result<T> fail(GlobalStatusEnums baseCode) {
        // 确保baseCode非空，如果为空则使用默认的错误代码
        GlobalStatusEnums validBaseCode = Optional.ofNullable(baseCode)
                .orElseGet(() -> {
                    log.info("BaseCode is null, using default error code.");
                    return ERROR;
                });
        // 使用错误代码和消息创建并返回Result对象
        return fail(validBaseCode.getMsg(), validBaseCode.getCode());
    }

//    public static <T> Result<T> timeout() {
//        return fail(TIMEOUT_ERROR.getMsg(), TIMEOUT_ERROR.getCode());
//    }

    /**
     * 判断操作是否成功
     * 此方法用于判断当前对象所表示的操作或状态是否为成功
     * 它通过比较对象的code属性与预定义的成功代码（SUCCESS_CODE或200）来确定
     * 如果code等于任一成功代码，则返回true，表示操作成功；否则返回false
     *
     * @return Boolean 表示操作是否成功的布尔值
     */
    @JsonIgnore
    public Boolean isSuccess() {
        return this.code == SUCCESS_CODE;
    }
}
