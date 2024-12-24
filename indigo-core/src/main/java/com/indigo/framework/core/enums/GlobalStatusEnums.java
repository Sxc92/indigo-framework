package com.indigo.framework.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author 史偕成
 * @date 2024/12/22 21:45
 **/
@Getter
@AllArgsConstructor
public enum GlobalStatusEnums implements BasicEnums<Integer> {

    ERROR(500, "操作失败"),

    VALIDATE_PARAM(100, "参数校验异常");

    private final Integer code;

    private final String msg;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return msg;
    }

    @Override
    public List<Integer> arrays() {
        return Arrays.stream(values()).map(GlobalStatusEnums::getCode).toList();
    }
}
