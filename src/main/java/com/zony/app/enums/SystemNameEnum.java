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

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/30 -10:17 
 */
@AllArgsConstructor
public enum SystemNameEnum {
    COMPANY_GOVERNANCE("1","公司治理"),
    DEFAULT("2","其他");


    private String code;
    private String name;
    private static Map<String,SystemNameEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (SystemNameEnum item : SystemNameEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static SystemNameEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (SystemNameEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<SystemNameEnum, String> {
        @Override
        public String convertToDatabaseColumn(SystemNameEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public SystemNameEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
