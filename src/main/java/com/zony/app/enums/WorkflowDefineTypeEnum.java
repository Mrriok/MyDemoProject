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
 * @date 2020/9/1 -11:10 
 */
@AllArgsConstructor
public enum WorkflowDefineTypeEnum {

    BASIC("basic", "基本定义"),
    //拓展类型1，需要从log中reminder中获取下一步代办人
    EXPAND("expand","reminder"),
    //拓展类型2，需要查当前办理人的部门负责人
    EXPAND1("expand1","部门负责人"),
    //拓展类型3，需要查当前办理人的处长
    EXPAND2("expand2","处长");

    private final String code;
    private final String name;
    private static Map<String, WorkflowDefineTypeEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @JsonCreator
    public static WorkflowDefineTypeEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (WorkflowDefineTypeEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<WorkflowDefineTypeEnum, String> {
        @Override
        public String convertToDatabaseColumn(WorkflowDefineTypeEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public WorkflowDefineTypeEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
