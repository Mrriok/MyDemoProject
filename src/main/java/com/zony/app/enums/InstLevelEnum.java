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
 * 制度层级枚举类.
 * <p>制度层级枚举，name在前端进行显示.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/15 -10:23 
 */

@AllArgsConstructor
public enum InstLevelEnum {

    BASE_INST("baseInst","基本制度"),
    OPERATING_RULE("operatingRule","操作细则"),
    MANAGEMENT_METHOD("managementMethod","管理办法");


    private String code;
    private String name;
    private static Map<String, InstLevelEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (InstLevelEnum item : InstLevelEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }
    @JsonCreator
    public static InstLevelEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (InstLevelEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<InstLevelEnum, String> {
        @Override
        public String convertToDatabaseColumn(InstLevelEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public InstLevelEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
