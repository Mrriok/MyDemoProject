package com.zony.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程处理方式类型
 *
 * @Author gubin
 * @Date 2020-04-14
 **/
@AllArgsConstructor
public enum WorkflowDisposeWayEnum {

    OR_SIGN("or_sign", "或签"),
    COUNTER_SIGN("counter_sign", "会签"),
    ADD_SIGN("add_sign", "加签");

    private final String code;
    private final String name;
    private static Map<String, WorkflowDisposeWayEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @JsonCreator
    public static WorkflowDisposeWayEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (WorkflowDisposeWayEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<WorkflowDisposeWayEnum, String> {
        @Override
        public String convertToDatabaseColumn(WorkflowDisposeWayEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public WorkflowDisposeWayEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
