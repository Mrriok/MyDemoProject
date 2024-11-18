package com.zony.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程处理对象类型
 *
 * @Author gubin
 * @Date 2020-04-14
 **/
@AllArgsConstructor
public enum WorkflowDisposeObjEnum {

    USER("user", "用户"),
    ROLE("role", "角色"),
    DEPT("dept", "部门");

    private final String code;
    private final String name;
    private static Map<String, WorkflowDisposeObjEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @JsonCreator
    public static WorkflowDisposeObjEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (WorkflowDisposeObjEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<WorkflowDisposeObjEnum, String> {
        @Override
        public String convertToDatabaseColumn(WorkflowDisposeObjEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public WorkflowDisposeObjEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
