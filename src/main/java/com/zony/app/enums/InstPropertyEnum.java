/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 制度属性枚举.
 * <p>制度属性枚举，name在前端进行显示.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/15 -10:06 
 */

@AllArgsConstructor
public enum InstPropertyEnum {
    ADD("add","新增"),//新增
    UPDATE("update","修订");//修订

    private String code;
    private String name;
    private static Map<String, InstPropertyEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (InstPropertyEnum item : InstPropertyEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static InstPropertyEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (InstPropertyEnum item : values()) {
                valueMap.put(item.getCode(), item);
            }
        }
        if (code == null) {
            return null;
        } else {
            return valueMap.get(code);
        }
    }

    @Converter
    public static class EnumConverter implements AttributeConverter<InstPropertyEnum, String> {
        @Override
        public String convertToDatabaseColumn(InstPropertyEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public InstPropertyEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
