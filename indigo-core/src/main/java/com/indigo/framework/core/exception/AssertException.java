package com.indigo.framework.core.exception;


import static com.indigo.framework.core.enums.GlobalStatusEnums.VALIDATE_PARAM;

public class AssertException extends BaseUnCheckException {
    /**
     * 构造函数，用于包装一个现有的异常
     *
     * @param cause 原始异常，用于进一步封装
     */
    public AssertException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数，用于创建一个带有错误信息的参数异常
     *
     * @param message 异常的详细信息
     */
    public AssertException(String message) {
        super(message, VALIDATE_PARAM.getCode());
    }

    /**
     * 构造函数，用于创建一个带有错误信息和原因的参数异常
     *
     * @param message 异常的详细信息
     * @param cause   异常的原因
     */
    public AssertException(String message, Throwable cause) {
        super(message, VALIDATE_PARAM.getCode(), cause);
    }

    /**
     * 构造函数，用于格式化字符串形式的异常信息
     *
     * @param format 格式化的字符串模板
     * @param args   格式化字符串时需要的参数
     */
    public AssertException(final String format, Object... args) {
        super(format, VALIDATE_PARAM.getCode(), args);
    }

    @Override
    public String toString() {
        return "AssertException [msg=" + getMessage() + ", code = " + getErrorCode() + "]";
    }
}
