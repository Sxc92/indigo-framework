package com.indigo.framework.core.exception;


import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;

public  class BaseUnCheckException extends RuntimeException implements BaseException {
    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 异常编码
     */
    private int errorCode;


    public BaseUnCheckException(Throwable cause) {
        super(cause);
    }

    public BaseUnCheckException(Throwable cause, final int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BaseUnCheckException(final String errorMsg, final int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public BaseUnCheckException(final String errorMsg, final int errorCode) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public BaseUnCheckException(final String errorMsg, final int errorCode, Object... args) {
        // 调用父类的构造函数必须放在第一行就很无奈！！
        super(StrUtil.contains(errorMsg, StrPool.EMPTY_JSON) ? StrUtil.format(errorMsg, args) : String.format(errorMsg, args));
        this.errorMsg = StrUtil.contains(errorMsg, StrPool.EMPTY_JSON) ? StrUtil.format(errorMsg, args) : String.format(errorMsg, args);
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
