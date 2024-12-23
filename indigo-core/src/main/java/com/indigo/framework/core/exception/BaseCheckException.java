package com.indigo.framework.core.exception;

public  class BaseCheckException extends Exception implements BaseException {

    /**
     * 异常信息
     */
    private final String errorMsg;

    /**
     * 异常编码
     */
    private final int errorCode;

    /**
     * 构造函数，用于初始化异常对象，存储错误信息和错误编码
     *
     * @param errorMsg  错误信息
     * @param errorCode 错误编码
     */
    public BaseCheckException(final String errorMsg, final int errorCode) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 构造函数，用于初始化异常对象，支持错误信息的格式化
     *
     * @param errorMsg  错误信息
     * @param errorCode 错误编码
     * @param args      格式化错误信息的参数
     */
    public BaseCheckException(final String errorMsg, final int errorCode, Object... args) {
        super(String.format(errorMsg, args));
        this.errorMsg = String.format(errorMsg, args);
        this.errorCode = errorCode;
    }

    /**
     * 获取异常的错误编码
     *
     * @return 错误编码
     */
    @Override
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 获取异常的错误信息
     *
     * @return 错误信息
     */
    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
