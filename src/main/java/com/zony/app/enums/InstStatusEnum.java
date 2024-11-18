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
 * @date 2020/7/30 -9:57 
 */
@AllArgsConstructor
public enum  InstStatusEnum {

    DRAFT("0","草稿"),
    TO_BE_ACCEPTED("1","编制审批中"),
    DONE_ACCEPTED("2","编制审批完结"),
    OA_ADD_SIGN("3","OA处理中"),
    OA_ERROR("-1","OA处理失败"),
    ACCEPTED("4","已入库"),

    DOING_ABOLISH("5","废除审批中"),
    DONE_ABOLISH("6","废除审批完结"),
    BEEN_ABOLISHED("7","已废除");

    private String code;
    private String name;
    private static Map<String,InstStatusEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (InstStatusEnum item : InstStatusEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static InstStatusEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (InstStatusEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<InstStatusEnum, String> {
        @Override
        public String convertToDatabaseColumn(InstStatusEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public InstStatusEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
