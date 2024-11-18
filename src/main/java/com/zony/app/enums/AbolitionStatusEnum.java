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
 * @date 2020/9/7 -10:36 
 */
@AllArgsConstructor
public enum AbolitionStatusEnum {


    NOT_ABOLISHED("0","未废除"),
    DOING_ABOLISH("1","审批中"),
    DONE_ABOLISH("2","审批完结"),
    OA_ADD_SIGN("3","OA处理中"),
    OA_ERROR("-1","OA处理失败"),
    BEEN_ABOLISHED("4","完结");

    private String code;
    private String name;
    private static Map<String, AbolitionStatusEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (AbolitionStatusEnum item : AbolitionStatusEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static AbolitionStatusEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (AbolitionStatusEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<AbolitionStatusEnum, String> {
        @Override
        public String convertToDatabaseColumn(AbolitionStatusEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public AbolitionStatusEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }

}
