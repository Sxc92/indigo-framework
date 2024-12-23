package com.indigo.framework.core.exception;

/**
 * @author 史偕成
 * @date 2024/12/08 19:34
 **/
public interface BaseException {

    /**
     * 获取异常码
     *
     * @return 获取异常码
     */
    int getErrorCode();

    /**
     * 获取异常信息
     *
     * @return 获取异常信息
     */
    String getErrorMsg();
}
