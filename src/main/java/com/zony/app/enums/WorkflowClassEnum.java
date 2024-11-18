package com.zony.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程类型
 *
 * @Author gubin
 * @Date 2020-04-14
 **/
@AllArgsConstructor
public enum WorkflowClassEnum {
    /**
     * 借阅流程
     **/
    OPINION("solicit_opinions", "征求意见"),
    SYSTEM("system_approval","制度审批"),
    ABOLITION("system_abolition","制度废除"),
    PLAN("system_plan","制度编修计划");

    private final String code;
    private final String name;
    private static Map<String, WorkflowClassEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @JsonCreator
    public static WorkflowClassEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (WorkflowClassEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<WorkflowClassEnum, String> {

        @Override
        public String convertToDatabaseColumn(WorkflowClassEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public WorkflowClassEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
