package com.indigo.framework.core.enums;

import java.util.List;

/**
 * @author 史偕成
 * @date 2024/12/08 18:38
 **/
public interface BasicEnums<T> {

    /**
     * 枚举唯一编码
     *
     * @return T
     */
    T getCode();

    /**
     * 枚举描述
     *
     * @return String
     */
    String getDesc();

    /**
     * 枚举唯一编码数组
     *
     * @return List<T>
     */
    List<T> arrays();
}
